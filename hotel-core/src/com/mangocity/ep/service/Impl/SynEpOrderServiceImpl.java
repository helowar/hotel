package com.mangocity.ep.service.Impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.ep.dao.EpOrderManagerDAO;
import com.mangocity.ep.entity.EpOrder;
import com.mangocity.ep.service.SynEpOrderService;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.TempOrder;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.assistant.GroupTypeConstants;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.MemberConfirmSmsStutas;
import com.mangocity.hotel.order.constant.MemberConfirmType;
import com.mangocity.hotel.order.constant.ModelType;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.dao.TempOrderDao;
import com.mangocity.hotel.order.manager.HotelFaxManager;
import com.mangocity.hotel.order.persistence.HtlOrderStsLog;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrMemberConfirm;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.HtlOrderStsLogService;
import com.mangocity.hotel.order.service.IMemberConfirmService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.log.MyLog;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Mail;
import com.mangoctiy.communicateservice.domain.Sms;

public class SynEpOrderServiceImpl implements SynEpOrderService{
	private static final MyLog log = MyLog.getLogger(SynEpOrderServiceImpl.class);
	
	private EpOrderManagerDAO epOrderManagerDAO;
	private IOrderService orderService;
	private TempOrderDao tempOrderDao;
	private IHotelService hotelService;
	private MsgAssist msgAssist;
	private HtlOrderStsLogService htlOrderStsLogService;
	private CommunicaterService communicaterService;//发送邮件
	private HotelFaxManager hotelFaxManager;//发送传真
	private IMemberConfirmService memberConfirmService;//发送短信
	private MemberInterfaceService memberInterfaceService;//会员接口
	
	public List<EpOrder> queryHotelConfirmedEPOrder(int minuteTime) {
		return epOrderManagerDAO.queryHotelConfirmedEPOrder(minuteTime);
	}
	
	public void synEpOrderHandler(EpOrder ep) {
		try{
			if(null != ep){
				boolean flag = false;
				String logContent = "";
				if("1".equals(ep.getIshotelconfirm())){//酒店确认预订
					if(null != ep.getSpecialrequest()  && !"".equals(ep.getSpecialrequest())){//有特殊要求
						if(1 == ep.getConfirmspecialReqType()){//满足特殊要求
							flag = true;
							logContent = "酒店确认预订且满足特殊要求,"+ep.getRemark();
						}else if(0 == ep.getConfirmspecialReqType()){//尽量满足特殊要求
							flag = false;
							logContent = "酒店确认预订且尽量满足特殊要求,"+ep.getRemark();
						}
					}else{//无特殊要求
						flag = true;
						logContent = "酒店确认预订，"+ep.getRemark();
					}
				}else{//酒店拒绝预订
					flag = false;
					logContent = "酒店打拒绝预订,"+ep.getIntroduce();
				}
				//记录操作日志
				OrOrder order = orderService.getOrder(ep.getOrderid());
	            OrHandleLog handleLog = new OrHandleLog();
	            handleLog.setModifierName("ebooking");
	            handleLog.setModifierRole("ebooking");
	            handleLog.setBeforeState(order.getOrderState());
	            handleLog.setAfterState(order.getOrderState());
	            handleLog.setContent("<font color='red'>"+logContent+"</font>");
	            handleLog.setModifiedTime(new Date());
	            handleLog.setOrder(order);
	            order.getLogList().add(handleLog);
	            //保存日志信息
	            
	            List<Long> orderFaxIdLst = epOrderManagerDAO.queryEpOrderFaxId(order.getID());
	            if(flag){//酒店确认预订并满足特殊要求
	            	//修改订单状态
	            	order.setHotelConfirm(true);
	            	order.setHotelConfirmTel(true);
	            	order.setHotelConfirmFax(true);
	            	order.setHotelConfirmFaxReturn(true);
	            	orderService.saveOrUpdate(order);
	            	
	            	//修改确认信息
	            	if(null != orderFaxIdLst && !orderFaxIdLst.isEmpty()){
	            		epOrderManagerDAO.updateEpOrderFax(orderFaxIdLst.get(0), 1, 1, ep.getHotelConfirmNo());
	            	}
	            	
	            	//给客人确认
	            	int confirmType = order.getConfirmType();
	            	HtlHotel hotel = hotelService.findHotel(order.getHotelId());
	            	if(ConfirmType.FAX == confirmType){//传真
	            		if(null != order.getCustomerFax()){
	            			sendFax2Cus(order);
	            		}else{
	            			sendOrder2ExpertGroup(order.getID());
	            		}
	            	}else if(ConfirmType.EMAIL == confirmType){//邮件
	            		if(null != order.getEmail()){
	            			sendEmail2Cus(order,hotel);
	            		}else{
	            			sendOrder2ExpertGroup(order.getID());
	            		}
	            	}else if(ConfirmType.NO == confirmType){//不用确认
	            		//不操作
	            	}else if(ConfirmType.SMS == confirmType){//短信
	            		 if(order.getOrderState()!=14&&order.isCustomerConfirm()!=true){
	            			 sendSMS(order,hotel.getChnAddress(),hotel.getTelephone());
	            		 }
	            	}else {//电话、直联则转中台专家组
	            		sendOrder2ExpertGroup(order.getID());
	            	}
	            }else{
	            	orderService.saveOrUpdate(order);
	            	
	            	//更改酒店确认信息
	            	if(null != orderFaxIdLst && !orderFaxIdLst.isEmpty()){
	            		epOrderManagerDAO.updateEpOrderFax(orderFaxIdLst.get(0), 1, 0, null);
	            	}
	            	
	            	//订单流转到专家组
	            	sendOrder2ExpertGroup(order.getID());
	            }
	            
	            //更改EP订单CC已确认状态
	            UserWrapper roleUser = new UserWrapper();
	            roleUser.setLoginName("ebooking");
	            epOrderManagerDAO.updateConfrimStatus(ep.getOrdercd(),roleUser);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void sendOrder2ExpertGroup(Long orderId){
		try{
			TempOrder tr = tempOrderDao.queryTempOrder(orderId);
			if(tr != null){
					tempOrderDao.updateTempOrder(orderId, GroupTypeConstants.GROUP_ZJ);
			}else{
				tr = new TempOrder();
				tr.setOrderid(orderId);
				tr.setGrouptype(GroupTypeConstants.GROUP_ZJ);
				tempOrderDao.insertTempOrder(tr);
			}
		}catch(Exception e){
			log.error("酒店流转到专家组失败，orderId="+orderId, e);
		}
	}
	
	/**
	 * 
	 * 向客人发送短信并添加发送短信日志记录、客人短信确认日志
	 * @param order
	 * @param hotelAddress
	 */
	public void sendSMS2Cus(OrOrder order,String hotelAddress){
		//发送短信到客人
		sendSMS(order, hotelAddress, "");
		
	}

	/**
	 * 发送短信到客人
	 * @param order
	 * @param hotelAddress
	 * @param hotelTel
	 */
	private void sendSMS(OrOrder order, String hotelAddress, String hotelTel) {
		try{
			String phoneNo = order.getMobile();
			String smsText = assemblyMsgTemplate(order,hotelAddress,hotelTel);
			int smsType = 1;

			// 调用发送短信接口和创建订单客户确认信息发送表信息
			Long res = 0L;
			res = sendMessageOrSMS(phoneNo, smsText,"ebooking");

			// 创建订单客户确认信息发送表信息
			OrMemberConfirm memberConfirm = getOrMemberConfirm(order,phoneNo, smsType,
					smsText, res,"ebooking");
			order.getMemberConfirmList().add(memberConfirm);
			
			if (!order.isSendedMemberFax()) {
				order.setSendedMemberFax(true);
			}
			// 已发送客户确认
			boolean bChange = false;
			if (!order.isCustomerConfirm() && smsType == MemberConfirmType.CONFIRM) {
				order.setCustomerConfirm(true);
				sendCustomerconfirmLog(order, "ebooking");
				bChange = true;
			}
			if (bChange) {
				addMemberConfirmLog(order, "ebooking");
			}
			
			order.setModifiedMidTime(new Date());

			order.setModifier("ebooking");
			order.setModifierName("ebooking");

			orderService.saveOrUpdate(order);
		}catch(Exception e){
			log.error("发送短信失败：", e);
		}
	}
	
	/**
	 * 创建订单客户确认信息发送表信息
	 */
	private OrMemberConfirm getOrMemberConfirm(OrOrder order,String phoneNo, int smsType,
			String smsText, Long res,String loginName) {
		OrMemberConfirm memberConfirm = new OrMemberConfirm();
		memberConfirm.setChannel(ConfirmType.SMS);
		if ("XJG".equals(order.getMemberState())) {
			memberConfirm.setModelType(1);// 114还是芒果
		} else {
			memberConfirm.setModelType(order.getType());// 114还是芒果
		}

		memberConfirm.setType(smsType);
		memberConfirm.setSendTarget(phoneNo.trim());

		memberConfirm.setSendMan(loginName);
		memberConfirm.setSendManId(loginName); // 发送人的工号
		memberConfirm.setSendTime(new Date());
		memberConfirm.setSendSucceed(true);
		memberConfirm.setContent(smsText.trim());

		memberConfirm.setSendStatus(MemberConfirmSmsStutas.SENDING);
		memberConfirm.setUnicallRetId(res);

		memberConfirm.setOrder(order);
		return memberConfirm;
	}
	
	/**
	 * 调用发送短信接口
	 */
	private Long sendMessageOrSMS(String phoneNo, String smsText,String loginName) {
		Sms sms = new Sms();
		sms.setApplicationName("hotel");
		sms.setTo(new String[] { phoneNo });
		sms.setMessage(smsText);
		sms.setFrom(loginName);
		return communicaterService.sendSms(sms);
	}
	
	public void sendFax2Cus(OrOrder order) {
		MemberDTO member = null;
		try {
			member = memberInterfaceService.getMemberByCode(order.getMemberCd());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return;
		}

		String faxNum = order.getCustomerFax();

		String faxType = "1";
		List orderItemGroupByList = orderService.hQueryOrderItemByFaxGroup(order.getID());

		Map<String, String> modifiedInfo = new HashMap<String, String>();
		modifiedInfo.put("faxNum", faxNum);
		modifiedInfo.put("faxType", faxType);
		modifiedInfo.put("sender", "0");// 0代表芒果网

		Long ret = null;
		UserWrapper roleUser = new UserWrapper();
		roleUser.setLoginName("ebooking");
		if (null != faxType) {
			try {

				ret = hotelFaxManager.sendNotifyGuestOrderInfoFax(modifiedInfo,
						order, null, orderItemGroupByList, member, roleUser);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

			int sendedStatus = MemberConfirmSmsStutas.SENDING; // 发送状态
			// 改变了逻辑:V2.7.1 发送成功或失败都需要记录发送日志 chenjiajie 2009-02-17
			if (null == ret || 0 >= ret) {
				sendedStatus = MemberConfirmSmsStutas.FAILED;
			}
			int nFaxType = Integer.parseInt(faxType);

			if (!order.isSendedMemberFax()) {
				order.setSendedMemberFax(true);
			}
			// 已发送客户确认
			boolean bChange = false;
			if (!order.isCustomerConfirm()
					&& nFaxType == MemberConfirmType.CONFIRM) {
				order.setCustomerConfirm(true);
				sendCustomerconfirmLog(order, "ebooking");
				bChange = true;
			}
			if (bChange) {
				addMemberConfirmLog(order, "ebooking");
			}

			OrMemberConfirm memberConfirm = new OrMemberConfirm();
			memberConfirm.setChannel(ConfirmType.FAX);
			memberConfirm.setType(nFaxType);
			memberConfirm.setSendTarget(faxNum);
			memberConfirm.setSendMan(roleUser.getName());
			memberConfirm.setSendTime(new Date());
			memberConfirm.setSendSucceed(true);
			if ("XJG".equals(order.getMemberState())) {
				memberConfirm.setModelType(1);// 114还是芒果
			} else {
				memberConfirm.setModelType(order.getType());// 114还是芒果
			}

			memberConfirm.setSendManId(roleUser.getLoginName()); // 发送人的工号
			memberConfirm.setUnicallRetId(ret); // UnitCall返回的流水号
			memberConfirm.setSendStatus(sendedStatus); // 发送状态，初始化为1

			memberConfirm.setOrder(order);
			order.getMemberConfirmList().add(memberConfirm);
			if (roleUser.isOrgMid()) {
				order.setModifiedMidTime(new Date());
			} else if (roleUser.isOrgFront()) {
				order.setModifiedFrontTime(new Date());
			}

			order.setModifier("ebooking");
			order.setModifierName("ebooking");

			orderService.saveOrUpdate(order);
		}
	}
	
	private String assemblyMsgTemplate(OrOrder order,String hotelChnAddress,String hotelTel) {
		boolean needArrivalTime = OrderUtil.isShowArrivalTime(order);
		
		Date choutDate = new Date(order.getCheckoutDate().getTime() + 24 * 60
				* 60 * 1000);

		StringBuilder dateSb = new StringBuilder();;
		long checkInLong = order.getCheckinDate().getTime() + 24 * 60 * 60 * 1000;
		long checkOutLong = choutDate.getTime();
		if (checkOutLong > checkInLong) {
			dateSb.append(DateUtil.toStringByFormat(order.getCheckinDate(), "MM"));
			dateSb.append("月");
			dateSb.append(DateUtil.toStringByFormat(order.getCheckinDate(), "dd"));
			dateSb.append("-");
			dateSb.append(DateUtil.toStringByFormat(order.getCheckoutDate(), "dd"));
			dateSb.append("日");
		} else {
			dateSb.append(DateUtil.toStringByFormat(order.getCheckinDate(),"MM月dd日"));
		}
		
		StringBuilder confirmString = new StringBuilder();
		confirmString.append(dateSb.toString()).append(order.getHotelName()).append("(").append(hotelChnAddress).append(" ").append(hotelTel).append(")");
		confirmString.append(order.getRoomQuantity()).append("间").append(order.getRoomTypeName()).append("已确认");

		if (needArrivalTime) {
			confirmString.append("，保留至").append(order.getLatestArrivalTime()).append("点");
		}
		return confirmString.toString();
	}
	
	/**
	 * 
	 * 向客人发送邮件记录日志，并进行订单状态流转
	 * @param order
	 * @param hotel
	 */
	public void sendEmail2Cus(OrOrder order, HtlHotel hotel) {
		String subject = "";
		String templateNo = "";
		String xmlString = "";
		if (OrderSource.FROM_HK.equals(order.getSource())) { // 香港
			subject = "芒果網酒店預定成功";
			templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK_HK;
		} else {
			subject = "芒果网酒店预定成功";
			if (OrderSource.FAN_TI_NET.equals(order.getSource())) {
				templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MANGOBOOK_HK_BIGFIVE;
			} else {
				templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK_HK;
			}
		}
		MemberDTO member = null;
		try {
			member = memberInterfaceService.getMemberByCode(order.getMemberCd());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return;
		}
		xmlString = msgAssist.genOrderMailByGuestMangoConfirm(order, hotel, member, "0");//0代表芒果网
		
		Mail mail = new Mail();
		mail.setApplicationName("hotel");
		mail.setTo(new String[] { order.getEmail() });
		
		mail.setSubject(subject);
		mail.setTemplateFileName(templateNo);
		mail.setXml(xmlString);
		mail.setFrom("cs@mangocity.com");
		Long ret = null;
		try {
			ret = communicaterService.sendEmail(mail);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		int sendedStatus = MemberConfirmSmsStutas.SENDING; // 发送状态
		
		if (null == ret || 0 >= ret) {
			sendedStatus = MemberConfirmSmsStutas.FAILED;
		}
		if (!order.isSendedMemberFax()) {
			order.setSendedMemberFax(true);
		}
		// 已发送客户确认
		boolean bChange = false;
		if (!order.isCustomerConfirm()) {
			order.setCustomerConfirm(true);
			sendCustomerconfirmLog(order, "ebooking");
			bChange = true;
		}
		if (bChange) {
			addMemberConfirmLog(order,"ebooking");
		}

		OrMemberConfirm memberConfirm = new OrMemberConfirm();
		memberConfirm.setChannel(ConfirmType.EMAIL);
		memberConfirm.setType(MemberConfirmType.CONFIRM);
		memberConfirm.setModelType(ModelType.MODEL_MANGO);
		if (order.getRmpOrder()) {
			memberConfirm.setModelType(order.getType());// 114还是芒果
		} 
		memberConfirm.setSendTarget(order.getEmail());
		memberConfirm.setSendMan("ebooking");
		memberConfirm.setSendTime(new Date());
		memberConfirm.setSendSucceed(true);

		memberConfirm.setUnicallRetId(ret); // UnitCall返回的流水号
		memberConfirm.setSendStatus(sendedStatus); // 发送状态，初始化为1

		memberConfirm.setOrder(order);
		order.getMemberConfirmList().add(memberConfirm);

		// 已发送客户确认
		order.setSendedMemberFax(true);
		order.setModifiedMidTime(new Date());

		order.setModifier("ebooking");
		order.setModifierName("ebooking");

		orderService.saveOrUpdate(order);
	}
	
	/**
	 * 发送客人确认日志
	 * 
	 */
	private void addMemberConfirmLog(OrOrder order,String modifer) {
		StringBuffer strCmp = new StringBuffer();
		strCmp.append("订单改为:<font color='red'>" + "已发送客户确认" + "</font>");
		OrHandleLog handleLog = new OrHandleLog();
		handleLog.setModifierName(modifer);
		handleLog.setModifierRole(modifer);
		handleLog.setBeforeState(order.getOrderState());
		handleLog.setAfterState(order.getOrderState());
		handleLog.setContent(strCmp.toString());
		handleLog.setModifiedTime(new Date());
		handleLog.setHisNo(order.getHisNo());
		handleLog.setOrder(order);
		order.getLogList().add(handleLog);
		OrderUtil.updateStayInMid(order);
	}
	
	private void sendCustomerconfirmLog(OrOrder order, String modifer) {
		try{
			boolean bOld = order.isQuotaOk();
			boolean bOld1 = order.isSendedHotelFax();
			boolean bOld2 = order.isHotelConfirmTel();
			boolean bOld3 = order.isHotelConfirmFax();
			boolean bOld4 = order.isHotelConfirmFaxReturn();
			boolean bOld5 = order.isCustomerConfirm();
			HtlOrderStsLog htlOrderStsLog = new HtlOrderStsLog();
			htlOrderStsLog.setOldQuotaOk(bOld == false ? 0 : 1);
			htlOrderStsLog.setNewQuotaok(bOld == false ? 0 : 1);
			htlOrderStsLog.setOldSendedhotelconfirm(bOld1 == false ? 0 : 1);
			htlOrderStsLog.setNewSendedhotelconfirm(bOld1 == false ? 0 : 1);
			htlOrderStsLog.setOldHoteloralconfirm(bOld2 == false ? 0 : 1);
			htlOrderStsLog.setNewHoteloralconfirm(bOld2 == false ? 0 : 1);
			htlOrderStsLog.setOldHotelwrittenconfirm(bOld3 == false ? 0 : 1);
			htlOrderStsLog.setNewHotelwrittenconfirm(bOld3 == false ? 0 : 1);
			htlOrderStsLog.setOldHotelconfirmfaxreturn(bOld4 == false ? 0 : 1);
			htlOrderStsLog.setNewHotelconfirmfaxreturn(bOld4 == false ? 0 : 1);
			htlOrderStsLog.setOldCustomerconfirm(0);
			htlOrderStsLog.setNewCustomerconfirm(1);
			String orderCd = order.getOrderCD();
			htlOrderStsLog.setOrdercd(Long.parseLong(orderCd));
			htlOrderStsLog.setModifyName(modifer);
			htlOrderStsLog.setModifyCode(modifer);
			htlOrderStsLog.setModifyTime(new Date());

			htlOrderStsLogService.insert(htlOrderStsLog);
		}catch(Exception e){
			log.error("记录HtlOrderStsLog失败：", e);
		}
		
	}
	
	public long getOrParamSeqNextVal(String synEpOrderSeq) {
		return epOrderManagerDAO.getOrParamSeqNextVal(synEpOrderSeq);
	}
	
	public EpOrderManagerDAO getEpOrderManagerDAO() {
		return epOrderManagerDAO;
	}

	public void setEpOrderManagerDAO(EpOrderManagerDAO epOrderManagerDAO) {
		this.epOrderManagerDAO = epOrderManagerDAO;
	}

	public IOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}

	public TempOrderDao getTempOrderDao() {
		return tempOrderDao;
	}

	public void setTempOrderDao(TempOrderDao tempOrderDao) {
		this.tempOrderDao = tempOrderDao;
	}

	public IHotelService getHotelService() {
		return hotelService;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

	public MsgAssist getMsgAssist() {
		return msgAssist;
	}

	public void setMsgAssist(MsgAssist msgAssist) {
		this.msgAssist = msgAssist;
	}

	public HtlOrderStsLogService getHtlOrderStsLogService() {
		return htlOrderStsLogService;
	}

	public void setHtlOrderStsLogService(HtlOrderStsLogService htlOrderStsLogService) {
		this.htlOrderStsLogService = htlOrderStsLogService;
	}

	public CommunicaterService getCommunicaterService() {
		return communicaterService;
	}

	public void setCommunicaterService(CommunicaterService communicaterService) {
		this.communicaterService = communicaterService;
	}

	public HotelFaxManager getHotelFaxManager() {
		return hotelFaxManager;
	}

	public void setHotelFaxManager(HotelFaxManager hotelFaxManager) {
		this.hotelFaxManager = hotelFaxManager;
	}

	public MemberInterfaceService getMemberInterfaceService() {
		return memberInterfaceService;
	}

	public void setMemberInterfaceService(
			MemberInterfaceService memberInterfaceService) {
		this.memberInterfaceService = memberInterfaceService;
	}

	public IMemberConfirmService getMemberConfirmService() {
		return memberConfirmService;
	}

	public void setMemberConfirmService(IMemberConfirmService memberConfirmService) {
		this.memberConfirmService = memberConfirmService;
	}

}
