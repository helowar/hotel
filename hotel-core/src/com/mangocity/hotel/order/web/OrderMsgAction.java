package com.mangocity.hotel.order.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlBookSetup;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.assistant.HotelInfo;
import com.mangocity.hotel.ext.member.constant.MemberAliasConstants;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.HotelConfirmType;
import com.mangocity.hotel.order.constant.MemberConfirmSmsStutas;
import com.mangocity.hotel.order.constant.MemberConfirmType;
import com.mangocity.hotel.order.constant.ModelType;
import com.mangocity.hotel.order.constant.OrderMessageSplit;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.dao.impl.OrOrderFaxDao;
import com.mangocity.hotel.order.manager.HotelFaxManager;
import com.mangocity.hotel.order.persistence.HtlOrderStsLog;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrMemberConfirm;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderFax;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.HtlOrderStsLogService;
import com.mangocity.hotel.order.service.IMemberConfirmService;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hotel.rmp.api.AffirmFacade;
import com.mangocity.hotel.rmp.api.SupplierFacade;
import com.mangocity.hotel.rmp.dto.AffirmDTO;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.msg.inside.dto.MsgRequest;
import com.mangocity.msg.inside.dto.MsgResponse;
import com.mangocity.msg.service.IMsgService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.MoneyHandle;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.log.MyLog;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Mail;
import com.mangoctiy.communicateservice.domain.Sms;

/**
 * 订单传真，邮件，短信等操作
 * 
 * @author chenkeming
 * 
 */
public class OrderMsgAction extends OrderAction {

	protected static final MyLog log = MyLog.getLogger(OrderMsgAction.class);

	private static final long serialVersionUID = -6480733904145267974L;

	/**
	 * message接口
	 */
	private CommunicaterService communicaterService;

	/**
	 * 传真邮件辅助类
	 */
	private MsgAssist msgAssist;

	private HotelFaxManager hotelFaxManager;

	/**
	 * 酒店本部接口
	 */
	private IHotelService hotelService;

	/*
	 * 酒店信息实体类
	 */
	private HotelInfo hotelInfo;

	/**
	 * OrOrderFax Dao类
	 */
	private OrOrderFaxDao orOrderFaxDao;

	private Long sumTo;

	private Long roomSumTo;

	private String confirmNo;

	private double orderSum;

	private Long hotelID;

	private String faxNum = "";

	/*
	 * 当前订单的状态
	 */
	private String orderType;

	private String tohotelNotes = "";

	private String faxType;

	private List orderItemGroupByList;

	private List hotelBookSetup;

	private int hotelBookSetupNumber;

	private String sendStatus = "";

	/**
	 * 重发发送短信日志ID V2.7.1 chenjiajie 2009-02-17
	 */
	private Long memberConfirmId;

	/**
	 * 处理给客人发短信的业务逻辑 add by chenjiajie V2.7.1 2009-02-17
	 */
	private IMemberConfirmService memberConfirmService;

	/**
	 * 发送SMS
	 */
	private String confirmString;

	private String cancelString;

	private String pauseString;

	private String returnString;

	private String noContactString;

	private String isAnewSend;

	// SMS模版修改 add by baofeng.si V2.3 2008-6-16 Start
	private String roomCrowdedString;

	private String changeRoomPriceString;

	private String guaranteeAnnounce;

	private String prepayAnnounce;

	private String checkoutAdvanced;

	private String partCheckin;

	private String noShow;

	private AffirmFacade affirmFacade;

	private String isShadowOrder;

	private List<AffirmDTO> arrirmList;

	private HtlOrderStsLogService htlOrderStsLogService;
	// SMS模版修改 add by baofeng.si V2.3 2008-6-16 End

	/**
	 * 短信平台service
	 */
	private IMsgService mgMsgInterface;

	private Long childRoomTypeId;

	private SupplierFacade supplierFacade;

	public Long getChildRoomTypeId() {
		return childRoomTypeId;
	}

	public void setChildRoomTypeId(Long childRoomTypeId) {
		this.childRoomTypeId = childRoomTypeId;
	}

	/**
	 * 准备发送SMS给客户
	 * 
	 * @return
	 */
	public String sendSMSToCus() {

		order = getOrder(orderId);

		if (null == order) {
			return forwardError("order对象为空！");
		}

        // 获取订单的member供预览页面用
        try {
        	member = getOnlineMember();
        } catch (RuntimeException e1) {
        	log.error(e1.getMessage(),e1);
            return forwardError("获取订单会员信息错误！");
        }
        if (null == member || !member.getMembercd().equals(order.getMemberCd())) {
            try {
            	member = memberInterfaceService.getMemberByCode(order
                    .getMemberCd());
            } catch (Exception e) {
            	log.error(e.getMessage(),e);
                return forwardError("获取订单会员信息错误！");
            }
        }
        
        String booingPhone = "芒果网";
        String auditPhone = null;
        if(order.isCooperateOrder()){ // 交行全卡商旅等渠道订单用交行热线
        	auditPhone = "400-678-5167";
        	booingPhone = "400-678-5167";
    	}else{
    		auditPhone = "4008876698";
            if(!order.isMango()|| MemberAliasConstants.isGDLT(member)){
            	if(MemberAliasConstants.LTT.equals(member.getAliasid())){
            		booingPhone = "联通商旅";
            	}else if(MemberAliasConstants.WTT.equals(member.getAliasid())){
            		booingPhone = "116114";
            	}else if(MemberAliasConstants.WTBJ.equals(member.getAliasid())){
            		booingPhone = "114";
            	}else if(MemberAliasConstants.NHZY.equals(member.getAliasid())){
            		booingPhone = "95539";
            	}else if(MemberAliasConstants.GDLT.equals(member.getAliasid())){
            		booingPhone = "020116114";
            		auditPhone = "020116114";
            		//中升集团 addby xiaowei.wang 2012.2.16
            	}else if(MemberAliasConstants.ZSJT.equals(member.getAliasid())){
            		booingPhone = "芒果网";
            	}else {
            		booingPhone = "114";
            	}
            }
        }                                
        
        assemblyMsgTemplate(order, booingPhone,auditPhone);

		return "sendSMSToCus";
	}

	public String sendAuditSMS() {
		sendSMSToCus();
		return "sendAuditSMS";
	}

	/**
	 * 处理发送SMS给客户
	 * 
	 * @return
	 */
	public String sendSMSProc() {

		roleUser = getOnlineRoleUser();

		Map params = getParams();
		String phoneNo = (String) params.get("phoneNo");
		String smsText = (String) params.get("smsText");
		int smsType = Integer.parseInt((String) params.get("smsType"));

		// 用户附加手机号码
		String appendMobile = (String) params.get("appendPhoneNo");
		String sendAppendMobile = (String) params.get("sendAppendMobile");
		log.info(sendAppendMobile);
		order = getOrder(orderId);
		// 调用发送短信接口和创建订单客户确认信息发送表信息
		sendMessage(smsText, smsType, phoneNo, order);
		if (null != sendAppendMobile && !sendAppendMobile.equals("")) {
			if (null != appendMobile && !"".equals(appendMobile)) {
				String[] sendMessage = appendMobile
						.split(OrderMessageSplit.SPLIT_APPENDMOBILE);
				for (String message : sendMessage) {
					// 调用发送短信接口和创建订单客户确认信息发送表信息
					sendMessage(smsText, smsType, message, order);
				}
			}
		}
		if (!order.isSendedMemberFax()) {
			order.setSendedMemberFax(true);
		}
		// 已发送客户确认
		boolean bChange = false;
		if (!order.isCustomerConfirm() && smsType == MemberConfirmType.CONFIRM) {
			order.setCustomerConfirm(true);
			sendCustomerconfirmLog(order, roleUser);
			bChange = true;
		}
		if (bChange) {
			addMemberConfirmLog();
		}
		if (roleUser.isOrgMid()) {
			order.setModifiedMidTime(new Date());
		} else if (roleUser.isOrgFront()) {
			order.setModifiedFrontTime(new Date());
		}

		// v2.8.1 增加操作人 add by chenkeming
		order.setModifier(roleUser.getLoginName());
		order.setModifierName(roleUser.getName());

		saveOrUpdateOrder(order);

		/** 更新重发短信日志状态为失败已确认 chenjiajie V2.7.1 2009-02-17 **/
		if (null != memberConfirmId && 0 < memberConfirmId) {
			memberConfirmService.updateSmsStatus(memberConfirmId,
					MemberConfirmSmsStutas.FAILED_CONFIRM);
		}

		request.setAttribute("errMessage", "短信发送成功！");

		return super.forwardMsgBox("发送成功！", "refreshSelf()");
	}

	/**
	 * 调用发送短信接口和创建订单客户确认信息发送表信息
	 */
	private void sendMessage(String smsText, int smsType, String message,
			OrOrder order) {

		Long res = 0L;
		MemberDTO tempmember = getMemberSimpleInfo(order.getMemberCd(),
				false);
		// 114用户或者订单类型是114的统一用HMSG短信平台发送短信modify by chenjiajie 2010-08-11
		if (roleUser.isOrg114() || OrderType.TYPE_114 == order.getType()
				|| MemberAliasConstants.isGDLT(tempmember)) {
			// 设置广东116114订单短信后缀
			if ("GDLT".equals(order.getMemberState())
					|| MemberAliasConstants.GDLT
							.equals(tempmember.getAliasid())) {
				smsText += " [广东116114电话导航]";
				res = sendMessageOrSMSByYswp(message, smsText);
				// 中升集团不带114后缀 以芒果网名义 add by xiaowei.wang 2012.02.15
			} else if ("XJG".equals(order.getMemberState())
					|| MemberAliasConstants.isZSJT(tempmember)) {
				res = sendMessageOrSMS(message, smsText);
			} else {
				smsText += " [114]";
				res = sendMessageOrSMSByYswp(message, smsText);
			}
			// res = sendMessageOrSMSByYswp(message, smsText);
		} else {
			res = sendMessageOrSMS(message, smsText);
		}

		// order = getOrder(orderId);
		// 创建订单客户确认信息发送表信息
		OrMemberConfirm memberConfirm = getOrMemberConfirm(message, smsType,
				smsText, res);
		// 114用户或者订单类型是114的统一用HMSG短信平台发送短信modify by chenjiajie 2010-08-11
		if (roleUser.isOrg114() || OrderType.TYPE_114 == order.getType()) {
			memberConfirm.setMsSpID(res);
			// 成功
			if (null != res && -1 != res.longValue()) {
				memberConfirm.setSendStatus(MemberConfirmSmsStutas.SUCCESS);
			} else {
				memberConfirm.setSendStatus(MemberConfirmSmsStutas.FAILED);
			}
		}
		order.getMemberConfirmList().add(memberConfirm);
	}

	/**
	 * 创建订单客户确认信息发送表信息
	 */
	private OrMemberConfirm getOrMemberConfirm(String phoneNo, int smsType,
			String smsText, Long res) {
		OrMemberConfirm memberConfirm = new OrMemberConfirm();
		memberConfirm.setChannel(ConfirmType.SMS);
		if ("XJG".equals(order.getMemberState())) {
			memberConfirm.setModelType(1);// 114还是芒果
		} else {
			memberConfirm.setModelType(order.getType());// 114还是芒果
		}

		memberConfirm.setType(smsType);
		memberConfirm.setSendTarget(phoneNo);

		memberConfirm.setSendMan(roleUser.getName());
		memberConfirm.setSendManId(roleUser.getLoginName()); // 发送人的工号
		memberConfirm.setSendTime(new Date());
		memberConfirm.setSendSucceed(true);
		memberConfirm.setContent(smsText);

		memberConfirm.setSendStatus(MemberConfirmSmsStutas.SENDING);
		memberConfirm.setUnicallRetId(res);

		memberConfirm.setOrder(order);
		return memberConfirm;
	}

	/**
	 * 调用发送短信接口 修改了方法的返回值类型为Long modify by chenjiajie V2.7.1 2009-02-17
	 */
	private Long sendMessageOrSMS(String phoneNo, String smsText) {
		Sms sms = new Sms();
		sms.setApplicationName("hotel");
		sms.setTo(new String[] { phoneNo });
		sms.setMessage(smsText);
		sms.setFrom(roleUser.getLoginName());
		return communicaterService.sendSms(sms);
	}

	/**
	 * 短信平台的接口
	 * 
	 * @param phoneNo
	 * @param smsText
	 * @return
	 */
	private Long sendMessageOrSMSByYswp(String phoneNo, String smsText) {

		Long reRS = -1L;

		MsgRequest msgRequest = new MsgRequest();
		try {
			msgRequest.setMobile(phoneNo);
			msgRequest.setContent(smsText);
			msgRequest.setSource("114");
			MsgResponse mResponse = mgMsgInterface.sendMsg(msgRequest);
			if (mResponse != null && mResponse.getSpid() != null
					&& !"".equals(mResponse.getSpid())) {
				reRS = Long.parseLong(mResponse.getSpid().replace(",", ""));
			} else {
				reRS = -1L;
			}
		} catch (Exception e) {
			reRS = -1L;
			e.printStackTrace();
		}
		return reRS;

	}

	/**
	 * 订单提交结果页面，点击发送酒店传真处理
	 * 
	 * 弹出传真发送平台页面。
	 */
	public String hotelOrderFaxSendPre() {

		order = getOrder(orderId);
		if (null == order) {
			return forwardError("order对象为空！");
		}
		// 如果没有发送或没有发送成功过，则不是新的预订单v2.6订单确认默认类型增加
		boolean succeedFlag = false;
		Iterator it = order.getFaxList().iterator();
		while (it.hasNext()) {
			OrOrderFax orderFax = (OrOrderFax) it.next();
			if (orderFax.isSendSucceed()) {
				succeedFlag = true;
				break;
			}
		}
		// 如果是取消单
		if (order.getOrderState() == OrderState.CANCEL) {
			orderType = "cancel";
			// 如果是新下单
		} else if (!succeedFlag) {
			orderType = "newOrder";
		} else {
			// 如果是修改
			orderType = "modified";
		}
		if(!order.getRmpOrder()){
			faxNum = hotelService.getHotelFaxNo(order.getHotelId(), order
					.getChildRoomTypeId());
		}
		

		// 魅影订单的确认方式num
		String faxNumInDate = "";
		if (order.getRmpOrder()) {
				
			
			faxNum = "";
			arrirmList = affirmFacade.queryAffirmByPPID(order.getOrOrderRMP()
					.getPricePlanId(), null, null);

			if (null != arrirmList && arrirmList.size() > 0) {
				List<AffirmDTO> hotelAffirm = new ArrayList<AffirmDTO>();
				List<AffirmDTO> contractAffirm = new ArrayList<AffirmDTO>();
				for (AffirmDTO affirm : arrirmList) {
					if (affirm.getAffirmType() == 2
							&& affirm.getAffirmControlApplyType() == 1) {
						contractAffirm.add(affirm);
					}
					if (affirm.getAffirmType() == 2
							&& affirm.getAffirmControlApplyType() == 2) {
						hotelAffirm.add(affirm);
					}

				}
				for (AffirmDTO affirm : contractAffirm) {
					if (affirm.isBetweenDate()) {
						faxNumInDate = affirm.getAffirmNum();
					}
				}
				for (AffirmDTO affirm : hotelAffirm) {
					if (affirm.isBetweenDate()) {
						faxNumInDate = affirm.getAffirmNum();
					}
				}
				if (null != (faxNumInDate) && !faxNumInDate.equals("")) {
					faxNum = faxNumInDate;
				}
			}
		}

		tohotelNotes = order.getRemark().getHotelRemark();

		if (null != faxType && !order.getRmpOrder()) {
			List<HtlBookSetup> htlBookSetupList = hotelService.getSupplierFax(
					order.getHotelId(), order.getChildRoomTypeId());
			if (null == htlBookSetupList || htlBookSetupList.isEmpty()) {
				hotelBookSetup = hotelService.getHtlBookSetupList(hotelID,
						faxType);
			} else {
				hotelBookSetup = htlBookSetupList;
			}
			if (null != hotelBookSetup) {
				hotelBookSetupNumber = hotelBookSetup.size();
			}
		}
		return "hotelOrderFaxSendPre";
	}

	/**
	 * 订单提交结果页面，点击发送酒店传真处理 hotel 2.5 酒店直联特殊要求通知单, 弹出传真发送平台页面。
	 */
	public String hotelOrderFaxSendChannelPre() {
		order = getOrder(orderId);
		if (null == order) {
			return forwardError("order对象为空！");
		}
		faxNum = hotelService.getHotelFaxNo(order.getHotelId(), order
				.getChildRoomTypeId());
		tohotelNotes = order.getRemark().getHotelRemark();
		return "hotelOrderFaxSendChannelPre";
	}

	/**
	 * 订单提交结果页面，点击发送酒店Email处理
	 * 
	 * 弹出Email发送平台页面。
	 */
	public String hotelOrderMailSendPre() {
		order = getOrder(orderId);
		if (null == order) {
			return forwardError("order对象为空！");
		}
		// 如果没有发送或没有发送成功过，则不是新的预订单 v2.6订单确认默认类型增加
		boolean succeedFlag = false;
		Iterator it = order.getFaxList().iterator();
		while (it.hasNext()) {
			OrOrderFax orderFax = (OrOrderFax) it.next();
			if (orderFax.isSendSucceed()) {
				succeedFlag = true;
				break;
			}
		}
		// 如果是取消单
		if (order.getOrderState() == OrderState.CANCEL) {
			orderType = "cancel";
			// 如果是新下单
		} else if (!succeedFlag) {
			orderType = "newOrder";
			// 如果是修改单
		} else {
			orderType = "modified";
		}
		faxNum = hotelService.getHotelMail(order.getHotelId());

		// 魅影订单的确认方式num
		String faxNumInDate = "";
		if (order.getRmpOrder()) {
			faxNum = "";
			arrirmList = affirmFacade.queryAffirmByPPID(order.getOrOrderRMP()
					.getPricePlanId(), null, null);
			if (null != arrirmList && arrirmList.size() > 0) {
				if (null != arrirmList && arrirmList.size() > 0) {
					List<AffirmDTO> hotelAffirm = new ArrayList<AffirmDTO>();
					List<AffirmDTO> contractAffirm = new ArrayList<AffirmDTO>();
					for (AffirmDTO affirm : arrirmList) {

						if (affirm.getAffirmType() == 1
								&& affirm.getAffirmControlApplyType() == 1) {
							contractAffirm.add(affirm);
						}
						if (affirm.getAffirmType() == 1
								&& affirm.getAffirmControlApplyType() == 2) {
							hotelAffirm.add(affirm);
						}

					}
					for (AffirmDTO affirm : contractAffirm) {
						if (affirm.isBetweenDate()) {
							faxNumInDate = affirm.getAffirmNum();
						}
					}
					for (AffirmDTO affirm : hotelAffirm) {
						if (affirm.isBetweenDate()) {
							faxNumInDate = affirm.getAffirmNum();
						}
					}
					if (null != (faxNumInDate) && !faxNumInDate.equals("")) {
						faxNum = faxNumInDate;
					}
				}
			}

		}

		tohotelNotes = order.getRemark().getHotelRemark();

		return "hotelOrderMailSendPre";
	}

	/**
	 * 计算按底价(结算价)结算的订单的总金额
	 * 
	 * @param order
	 * @return
	 */
	private double calOrderBaseSum(OrOrder order) {
		List<OrOrderItem> list = order.getOrderItems();
		double sum = 0.0;
		for (int i = 0; i < list.size(); i++) {
			OrOrderItem item = list.get(i);
			sum += item.getBasePrice();
		}
		return sum;
	}

	/**
	 * 在Emale发送处理平台页面，点击“预览”，根据类型去到不同的显示页面。
	 * 
	 * @return
	 */
	public String hotelOrderEmailSendPreView() {
		order = getOrder(orderId);
		for (int i = order.getFaxList().size() - 1; 0 <= i; i--) {
			OrOrderFax orOrderFax = order.getFaxList().get(i);
			if (orOrderFax.isValidConfirm()) {
				confirmNo = orOrderFax.getConfirmNo();
				break;
			}
		}
		member = getMemberSimpleInfo(order.getMemberCd(), false);
		sumTo = (order.getCheckoutDate().getTime() - order.getCheckinDate()
				.getTime())
				/ 24 / 60 / 60 / 1000;
		roomSumTo = sumTo * order.getRoomQuantity();
		if (null == order) {
			return forwardError("order对象为空！");
		}
		if (order.isShowBasePrice()) {
			orderSum = calOrderBaseSum(order);
		} else {
			orderSum = order.getSum();
		}
		// 这个方法没有实现，所以注释掉
		// hotelInfo = hotelService.getHotelInfoById(order.getHotelId());

		orderItemGroupByList = orderService.hQueryOrderItemByFaxGroup(order
				.getID());

		if (null == faxType) {
			return forwardError("Email类型为空！");
		}

		int nFaxType = Integer.parseInt(faxType);
		if (nFaxType == HotelConfirmType.CONFIRM) {
			return "hotelOrderEmailSendBookPreView";
		} else if (nFaxType == HotelConfirmType.MODIFY) {
			return "hotelOrderEmailSendModifyPreView";
		} else if (nFaxType == HotelConfirmType.CANCEL) {
			return "hotelOrderEmailSendCancelPreView";
		} else if (nFaxType == HotelConfirmType.CONTINUE_TO_LIVE) {
			return "hotelOrderEmailSendContinueLivePreView";
		}

		return "hotelOrderEmailSendBookPreView";
	}

	/**
	 * 处理给酒店发送Email
	 * 
	 * @return
	 */
	public String hotelOrderEmailSend() {

		Map params = getParams();
		String isAnewSend = (String) params.get("isAnewSend");
		if (null == isAnewSend) {
			return forwardError("isAnewSend类型为空！");
		}
		roleUser = getOnlineRoleUser();

		order = getOrder(orderId);

		if (null == order) {
			return forwardError("order对象为空！");
		}

		boolean isStayInMid = order.isStayInMid();

		// 这个方法没有实现，所以注释掉
		// hotelInfo = hotelService.getHotelInfoById(order.getHotelId());

		orderItemGroupByList = orderService.hQueryOrderItemByFaxGroup(order
				.getID());

		member = getMemberSimpleInfo(order.getMemberCd(), false);
		Map<String, String> modifiedInfo = new HashMap<String, String>(5);
		modifiedInfo.put("faxNum", faxNum);
		modifiedInfo.put("tohotelNotes", tohotelNotes);
		modifiedInfo.put("faxType", faxType);
		if (null != order.getMemberState()
				&& "XJG".equals(order.getMemberState())) {
			modifiedInfo.put("city", "");
		} else {
			modifiedInfo.put("city", member.getMemberstate());
		}
		Long ret = null;
		Long ID = orderService.getparmsId();

		modifiedInfo.put("sender", getSender(member));
		if (null != faxType) {
			Mail mail = new Mail();
			mail.setApplicationName("hotel");
			mail.setTo(new String[] { faxNum });
			String templateNo = "";
			String xmlString = "";
			String subject = "";
			HtlHotel hotel = hotelService.findHotel(order.getHotelId()
					.longValue());
			int nFaxType = Integer.parseInt(faxType);
			if (nFaxType == HotelConfirmType.CONFIRM) {
				subject = "芒果网预定单";
				// 判断是否是魅影订单，魅影订单的模板与普通单有区别 加入供应商
				if (order.getRmpOrder()) {
					templateNo = FaxEmailModel.RMP_NOTIFY_EMAIL_HOTEL_ORDER_INFO;
				} else {
					templateNo = FaxEmailModel.NOTIFY_EMAIL_HOTEL_ORDER_INFO;
				}
				xmlString = msgAssist.genOrderFaxByHotelFaxConfirm(order,
						hotel, isAnewSend, ID, orderItemGroupByList,
						modifiedInfo);

			} else if (nFaxType == HotelConfirmType.MODIFY) {
				subject = "芒果网修改单";
				if (order.getRmpOrder()) {
					templateNo = FaxEmailModel.RMP_NOTIFY_EMAIL_HOTEL_ORDER_CHANGE;
				} else {
					templateNo = FaxEmailModel.NOTIFY_EMAIL_HOTEL_ORDER_CHANGE;
				}
				xmlString = msgAssist.genOrderFaxByHotelFaxModify(order, hotel,
						isAnewSend, ID, orderItemGroupByList, modifiedInfo);

			} else if (nFaxType == HotelConfirmType.CANCEL) {
				subject = "芒果取消单";
				if (order.getRmpOrder()) {
					templateNo = FaxEmailModel.RMP_NOTIFY_EMAIL_HOTEL_ORDER_CANCEL;
				} else {
					templateNo = FaxEmailModel.NOTIFY_EMAIL_HOTEL_ORDER_CANCEL;
				}
				xmlString = msgAssist.genOrderFaxByHotelFaxCancel(order, hotel,
						isAnewSend, ID, orderItemGroupByList, modifiedInfo);
			} else if (nFaxType == HotelConfirmType.CONTINUE_TO_LIVE) {
				subject = "芒果续住单";
				if (order.getRmpOrder()) {
					templateNo = FaxEmailModel.RMP_NOTIFY_EMAIL_HOTEL_ORDER_CONTINUE;
				} else {
					templateNo = FaxEmailModel.NOTIFY_EMAIL_HOTEL_ORDER_CONTINUE;
				}
				xmlString = msgAssist.genOrderFaxByHotelFaxConfirm(order,
						hotel, isAnewSend, ID, orderItemGroupByList,
						modifiedInfo);
			}
			mail.setSubject(subject);
			mail.setFrom("cs@mangocity.com");
			mail.setTemplateFileName(templateNo);
			mail.setXml(xmlString);
			try {
				ret = communicaterService.sendEmail(mail);
			} catch (RuntimeException e) {
				log.error(e.getMessage(), e);
				sendStatus = "2";
				// return super.forwardMsgBox("邮件发送失败！", "refreshSelf()");
			}

			if (null == ret || 0 >= ret) {
				sendStatus = "2";
			} else {
				sendStatus = "1";
				OrOrderFax orOrderFax = new OrOrderFax();
				orOrderFax.setBarCode(ID.toString());
				orOrderFax.setHotelId(order.getHotelId());
				orOrderFax.setChannel(ConfirmType.EMAIL);// 1为传真，2为电邮，3为短信
				orOrderFax.setType(nFaxType);// 1为确认，2为修改，3为取消
				if (null != member.getState()
						&& "XJG".equals(member.getState().toString())) {
					orOrderFax.setModelType(1);// 芒果
				} else {
					orOrderFax.setModelType(order.getType());// 114还是芒果
				}
				// orOrderFax.setModelNo() 增加一个模版号。
				orOrderFax.setSendTarget(faxNum);
				orOrderFax.setSendMan(roleUser.getName());
				orOrderFax.setSendTime(new Date());
				orOrderFax.setSendSucceed(true);
				orOrderFax.setUnicallRetId("" + ret);
				orOrderFax.setHisNo(order.getHisNo());
				orOrderFax.setOrder(order);

				order.getFaxList().add(orOrderFax);
				boolean flag_1 = false;
				boolean flag_2 = false;
				if (!order.isSendedHotelFax()) {
					order.setSendedHotelFax(true);
					flag_1 = true;
				}
				boolean bChange = false;
				if (order.isHotelConfirmFaxReturn()) {
					flag_2 = true;
					order.setHotelConfirmFaxReturn(false);
					OrderUtil.updateStayInMid(order);
					bChange = true;
				}

				sendHotelconfirmLog(order, roleUser, flag_1, flag_2);

				// 增加修改日志
				StringBuffer strCmp = new StringBuffer();
				if (nFaxType == HotelConfirmType.CONFIRM) {
					strCmp.append("发送酒店确认Email<br>");
				} else if (nFaxType == HotelConfirmType.MODIFY) {
					strCmp.append("发送酒店修改Email<br>");
				} else if (nFaxType == HotelConfirmType.CANCEL) {
					strCmp.append("发送酒店取消Email<br>");
				} else if (nFaxType == HotelConfirmType.CONTINUE_TO_LIVE) {
					strCmp.append("发送酒店续住Email<br>");
				}
				if (bChange) {
					strCmp.append("订单改为:<font color=red>未收酒店回传</font><br>");
				}
				OrHandleLog handleLog = new OrHandleLog();
				// handleLog.setModifier(new Long(roleUser.getId()));
				handleLog.setModifierName(roleUser.getName());
				handleLog.setModifierRole(roleUser.getLoginName()); // 保存工号
				handleLog.setBeforeState(order.getOrderState());
				handleLog.setAfterState(order.getOrderState());
				handleLog.setContent(strCmp.toString());
				handleLog.setModifiedTime(new Date());
				handleLog.setHisNo(order.getHisNo());
				handleLog.setOrder(order);
				order.getLogList().add(handleLog);

				if (roleUser.isOrgMid()) {
					order.setModifiedMidTime(new Date());
				} else if (roleUser.isOrgFront()) {
					order.setModifiedFrontTime(new Date());
				}

				// v2.8.1 增加操作人 add by chenkeming
				order.setModifier(roleUser.getLoginName());
				order.setModifierName(roleUser.getName());

				this.saveOrUpdateOrder(order);
				if (!isStayInMid && orOrderFax.isSendSucceed() && 3 == nFaxType) {
					if ("pay".equals(order.getPayMethod())) {
						if (order.isCreditAssured()) {
							orderService.confirmToMid(orderId, roleUser);
						}
					} else {
						orderService.confirmToMid(orderId, roleUser);
					}
				}
			}
		}
		if ("2".equals(sendStatus)) {
			return super.forwardMsgBox("发送失败！", "refreshSelf()");
		} else {
			return super.forwardMsgBox("发送成功！", "refreshSelf()");
		}
	}

	/**
	 * 在传真发送处理平台页面，点击“预览”，根据类型去到不同的显示页面。
	 * 
	 * @return
	 */
	public String hotelOrderFaxSendPreView() {
		order = getOrder(orderId);

		sumTo = (order.getCheckoutDate().getTime() - order.getCheckinDate()
				.getTime())
				/ 24 / 60 / 60 / 1000;
		for (int i = order.getFaxList().size() - 1; 0 <= i; i--) {
			OrOrderFax orOrderFax = order.getFaxList().get(i);
			if (orOrderFax.isValidConfirm()) {
				confirmNo = orOrderFax.getConfirmNo();
				break;
			}
		}
		member = getMemberSimpleInfo(order.getMemberCd(), false);
		roomSumTo = sumTo * order.getRoomQuantity();
		if (null == order) {
			return forwardError("order对象为空！");
		}
		if (order.isShowBasePrice()) {
			orderSum = calOrderBaseSum(order);
		} else {
			orderSum = order.getSum();
		}
		// 这个方法没有实现，所以注释掉
		// hotelInfo = hotelService.getHotelInfoById(order.getHotelId());

		orderItemGroupByList = orderService.hQueryOrderItemByFaxGroup(order
				.getID());

		int nFaxType = Integer.parseInt(faxType);
		String nameInfo = "";
		List fellowList = order.getFellowList();
		for (int i = 0; i < fellowList.size(); i++) {
			OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
			nameInfo += fellow.getFellowName();// + "("+
			// MsgAssist.fellowNation.get(fellow.getFellowNationality());
			if (nFaxType == HotelConfirmType.CHANNLE_SPECIAL_REQUEST) {
				if ("1".equals(fellow.getFellowNationality())) {
					nameInfo += "(内宾) ";
				} else if ("2".equals(fellow.getFellowNationality())) {
					nameInfo += "(外宾) ";
				} else if ("3".equals(fellow.getFellowNationality())) {
					nameInfo += "(港澳) ";
				}
			} else {
				if (fellow.isFellowSub()) {
					nameInfo += "(代订)      ";
				}
			}
		}
		request.setAttribute("customerInfo", nameInfo);

		if (null == faxType) {
			return forwardError("传真类型为空！");
		}

		boolean isQuotaInner = orderService.getIsSystemQuota(order);

		if (isQuotaInner && !order.getRmpOrder()) {
			tohotelNotes += "   该订单为配额内用房";
		}

		if (nFaxType == HotelConfirmType.CONFIRM) {
			return "hotelOrderFaxSendBookPreView";
		} else if (nFaxType == HotelConfirmType.MODIFY) {
			return "hotelOrderFaxSendModifyPreView";
		} else if (nFaxType == HotelConfirmType.CANCEL) {
			return "hotelOrderFaxSendCancelPreView";
		} else if (nFaxType == HotelConfirmType.CONTINUE_TO_LIVE) {
			return "hotelOrderFaxSendContinueLivePreView";
		} else if (nFaxType == HotelConfirmType.CHANNLE_SPECIAL_REQUEST) {
			/**
			 * 酒店直联hotel2.5特殊要求通知单
			 */
			return "hotelOrderFaxSendChannelPreView";
		}
		return "hotelOrderFaxSendBookPreView";
	}

	/**
	 * 处理给酒店发送传真
	 * 
	 * @return
	 */
	public String hotelOrderFaxSend() {

		Map params = getParams();
		String isAnewSend = (String) params.get("isAnewSend");
		if (null == isAnewSend) {
			return forwardError("isAnewSend类型为空！");
		}

		roleUser = getOnlineRoleUser();

		order = getOrder(orderId);
		if (null == order) {
			return forwardError("order对象为空！");
		}

		boolean isStayInMid = order.isStayInMid();

		// 这个方法没有实现，所以注释掉
		// hotelInfo = hotelService.getHotelInfoById(order.getHotelId());

		orderItemGroupByList = orderService.hQueryOrderItemByFaxGroup(order
				.getID());

		member = getMemberSimpleInfo(order.getMemberCd(), false);

		Map<String, String> modifiedInfo = new HashMap<String, String>(5);
		modifiedInfo.put("faxNum", faxNum);
		modifiedInfo.put("tohotelNotes", tohotelNotes);
		modifiedInfo.put("faxType", faxType);
		if (null != order.getMemberState()
				&& "XJG".equals(order.getMemberState())) {
			modifiedInfo.put("city", "");
		} else {
			modifiedInfo.put("city", member.getMemberstate());
		}

		modifiedInfo.put("sender", getSender(member));

		Long ret = null;
		Long ID = orderService.getparmsId();

		if (null != faxType) {

			try {
				ret = hotelFaxManager.sendNotifyHotelOrderInfoFax(modifiedInfo,
						order, ID, getOnlineTaskId(), orderItemGroupByList,
						isAnewSend, roleUser);
			} catch (Exception e) {
				// 发送失败处理
				sendStatus = "2";
			}

			if (null == ret || 0 >= ret) {
				sendStatus = "2";
			} else {
				sendStatus = "1";

				int nFaxType = Integer.parseInt(faxType);
				OrOrderFax orOrderFax = new OrOrderFax();
				orOrderFax.setBarCode(ID.toString());
				orOrderFax.setHotelId(order.getHotelId());
				orOrderFax.setChannel(ConfirmType.FAX);// 1为传真，2为电邮，3为短信
				orOrderFax.setType(nFaxType);// 1为确认，2为修改，3为取消
				if (null != member.getState()
						&& "XJG".equals(member.getState().toString())) {
					orOrderFax.setModelType(1);// 芒果
				} else {
					orOrderFax.setModelType(order.getType());// 114还是芒果
				}
				// orOrderFax.setModelNo() 增加一个模版号。
				orOrderFax.setSendTarget(faxNum);
				orOrderFax.setSendMan(roleUser.getName());
				orOrderFax.setSendTime(new Date());
				orOrderFax.setSendSucceed(true);
				orOrderFax.setUnicallRetId("" + ret);
				orOrderFax.setHisNo(order.getHisNo());
				orOrderFax.setOrder(order);
				order.getFaxList().add(orOrderFax);

				// 发送成功处理
				boolean flag_1 = false;
				boolean flag_2 = false;
				if (!order.isSendedHotelFax()) {
					order.setSendedHotelFax(true);
					flag_1 = true;
				}
				boolean bChange = false;
				if (order.isHotelConfirmFaxReturn()) {
					flag_2 = true;
					order.setHotelConfirmFaxReturn(false);
					OrderUtil.updateStayInMid(order);
					bChange = true;
				}

				sendHotelconfirmLog(order, roleUser, flag_1, flag_2);
				// 增加修改日志
				StringBuffer strCmp = new StringBuffer();
				if (nFaxType == HotelConfirmType.CONFIRM) {
					strCmp.append("发送酒店确认传真<br>");
				} else if (nFaxType == HotelConfirmType.MODIFY) {
					strCmp.append("发送酒店修改传真<br>");
				} else if (nFaxType == HotelConfirmType.CANCEL) {
					strCmp.append("发送酒店取消传真<br>");
				} else if (nFaxType == HotelConfirmType.CONTINUE_TO_LIVE) {
					strCmp.append("发送酒店续住传真<br>");
				}
				if (bChange) {
					strCmp.append("订单改为:<font color=red>未收酒店回传</font><br>");
				}
				OrHandleLog handleLog = new OrHandleLog();
				// handleLog.setModifier(new Long(roleUser.getId()));
				handleLog.setModifierName(roleUser.getName());
				handleLog.setModifierRole(roleUser.getLoginName()); // 保存工号
				handleLog.setBeforeState(order.getOrderState());
				handleLog.setAfterState(order.getOrderState());
				handleLog.setContent(strCmp.toString());
				handleLog.setHisNo(order.getHisNo());
				handleLog.setModifiedTime(new Date());
				handleLog.setOrder(order);
				order.getLogList().add(handleLog);

                if (roleUser.isOrgMid()) {
                    order.setModifiedMidTime(new Date());
                } else if (roleUser.isOrgFront()) {
                    order.setModifiedFrontTime(new Date());
                }

				// v2.8.1 增加操作人 add by chenkeming
				order.setModifier(roleUser.getLoginName());
				order.setModifierName(roleUser.getName());

				this.saveOrUpdateOrder(order);
				if (!isStayInMid && orOrderFax.isSendSucceed() && 3 == nFaxType) {
					if ("pay".equals(order.getPayMethod())) {
						if (order.isCreditAssured()) {
							orderService.confirmToMid(orderId, roleUser);
						}
					} else {
						orderService.confirmToMid(orderId, roleUser);
					}
				}
			}
		}
		if ("2".equals(sendStatus)) {
			return super.forwardMsgBox("发送失败！", "refreshSelf()");
		} else {
			return super.forwardMsgBox("发送成功！", "refreshSelf()");
		}

	}

	/**
	 * 给客人发送传真页面
	 * 
	 * @return
	 */
	public String sendFaxToCus() {

		order = getOrder(orderId);
		if (null == order) {
			return forwardError("order对象为空！");
		}

		faxNum = order.getCustomerFax();
		member = getMemberSimpleInfo(order.getMemberCd(), false);
		return "sendFaxToCus";
	}

	/**
	 * 在给客人发送传真页面，点击“预览”，根据类型去到不同的显示页面。
	 * 
	 * @return
	 */
	public String hotelOrderFaxSendToCustomerPreView() {
		order = getOrder(orderId);

		if (null == order) {
			return forwardError("order对象为空！");
		}
		for (int i = order.getFaxList().size() - 1; 0 <= i; i--) {
			OrOrderFax orOrderFax = order.getFaxList().get(i);
			if (orOrderFax.isValidConfirm()) {
				confirmNo = orOrderFax.getConfirmNo();
				break;
			}
		}
		HtlHotel hotelInfo = hotelService.findHotel(order.getHotelId()
				.longValue());
		request.setAttribute("hotelInfo", hotelInfo);

		orderItemGroupByList = orderService.hQueryOrderItemByFaxGroup(order
				.getID());

		if (null == faxType) {
			return forwardError("传真类型为空！");
		}

		// 获取订单的member供预览页面用
		try {
			member = getOnlineMember();
		} catch (RuntimeException e1) {
			log.error(e1.getMessage(), e1);
			return forwardError("获取订单会员信息错误！");
		}
		if (null == member || !member.getMembercd().equals(order.getMemberCd())) {
			try {
				member = memberInterfaceService
						.getMemberByCode(order.getMemberCd());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return forwardError("获取订单会员信息错误！");
			}
		}

		String nameInfo = "";
		List fellowList = order.getFellowList();
		for (int i = 0; i < fellowList.size(); i++) {
			OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
			nameInfo += fellow.getFellowName() + "("
					+ MsgAssist.fellowNation.get(fellow.getFellowNationality());
			if (fellow.isFellowSub()) {
				nameInfo += ",代订)      ";
			} else {
				nameInfo += ")      ";
			}
		}
		request.setAttribute("customerInfo", nameInfo);

		int nightCount = DateUtil.getDay(order.getCheckinDate(), order
				.getCheckoutDate());
		request.setAttribute("nightCount", nightCount);

		// RMS2983香港组紧急需求 当支付币种非人民币 并且和订单原币种不同的时候界面需要用到rate转换价格 add by
		// chenjiajie 2009-11-27
		request.setAttribute("rate", MoneyHandle.getCurrenyRate(order
				.getActualPayCurrency()));
		return "hotelOrderCustomerFaxSendPreView";
	}

	/**
	 * 处理给客人发送传真
	 * 
	 * @return
	 */
	public String sendFaxToCusProc() {

		roleUser = getOnlineRoleUser();

		order = getOrder(orderId);
		if (null == order) {
			return forwardError("order对象为空！");
		}

        // 获取订单的member, 供发送客人传真用
        try {
            member = getOnlineMember();
        } catch (RuntimeException e1) {
        	log.error(e1.getMessage(),e1);
            return forwardError("获取订单会员信息错误！");
        }
        if (null == member || !member.getMembercd().equals(order.getMemberCd())) {
            try {
                member = memberInterfaceService.getMemberByCode(order
                    .getMemberCd());
            } catch (Exception e) {
            	log.error(e.getMessage(),e);
                return forwardError("获取订单会员信息错误！");
            }
        }

		faxNum = request.getParameter("faxNum");

		String faxType = request.getParameter("faxType");
		// request.getParameter("sender");
		orderItemGroupByList = orderService.hQueryOrderItemByFaxGroup(order
				.getID());

		Map<String, String> modifiedInfo = new HashMap<String, String>();
		modifiedInfo.put("faxNum", faxNum);
		modifiedInfo.put("faxType", faxType);
		modifiedInfo.put("sender", getParams().get("sender").toString());

		Long ret = null;

		if (null != faxType) {

			try {
				ret = hotelFaxManager.sendNotifyGuestOrderInfoFax(modifiedInfo,
						order, getOnlineTaskId(), orderItemGroupByList, member,
						roleUser);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				// 发送失败处理
				sendStatus = "2";
			}

			int sendedStatus = MemberConfirmSmsStutas.SENDING; // 发送状态
			// 改变了逻辑:V2.7.1 发送成功或失败都需要记录发送日志 chenjiajie 2009-02-17
			if (null == ret || 0 >= ret) {
				sendStatus = "2";
				sendedStatus = MemberConfirmSmsStutas.FAILED;
			} else {
				sendStatus = "1";
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
				sendCustomerconfirmLog(order, roleUser);
				bChange = true;
			}
			if (bChange) {
				addMemberConfirmLog();
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

			/** v2.7.1 RMS 2416订单页面增加短信发送状态显示及实时更新功能 chenjiajie 2009-02-17 begin **/
			memberConfirm.setSendManId(roleUser.getLoginName()); // 发送人的工号
			memberConfirm.setUnicallRetId(ret); // UnitCall返回的流水号
			memberConfirm.setSendStatus(sendedStatus); // 发送状态，初始化为1
			/** v2.7.1 RMS 2416订单页面增加短信发送状态显示及实时更新功能 chenjiajie 2009-02-17 end **/

			memberConfirm.setOrder(order);
			order.getMemberConfirmList().add(memberConfirm);
			if (roleUser.isOrgMid()) {
				order.setModifiedMidTime(new Date());
			} else if (roleUser.isOrgFront()) {
				order.setModifiedFrontTime(new Date());
			}

			// v2.8.1 增加操作人 add by chenkeming
			order.setModifier(roleUser.getLoginName());
			order.setModifierName(roleUser.getName());

			saveOrUpdateOrder(order);
		}
		if ("2".equals(sendStatus)) {
			return super.forwardMsgBox("发送失败！", "refreshSelf()");
		} else {
			return super.forwardMsgBox("发送成功！", "refreshSelf()");
		}
	}

	/**
	 * 进入发送邮件给客人页面
	 * 
	 * @return
	 */
	public String sendEmail2customer() {

		order = getOrder(orderId);
		if (null == order) {
			return forwardError("order对象为空！");
		}

		faxNum = order.getEmail();
		member = getMemberSimpleInfo(order.getMemberCd(), false);
		return "sendEmail2customer";
	}

	/**
	 * 处理给客人发送邮件
	 * 
	 * @return
	 */
	public String sendEmailToCusProc() {

		roleUser = getOnlineRoleUser();
		Map params = getParams();
		int sendtype = Integer.parseInt((String) params.get("sendtype"));
		String toaddress = (String) params.get("toaddress");
		String sender = request.getParameter("fromaddress");
		order = getOrder(orderId);

        // 获取订单的member, 供发邮件获取member的alias name用
        try {
            member = getOnlineMember();
        } catch (RuntimeException e1) {
        	log.error(e1.getMessage(),e1);
            return forwardError("获取订单会员信息错误！");
        }
        if (null == member || !member.getMembercd().equals(order.getMemberCd())) {
            try {
                member = memberInterfaceService.getMemberByCode(order
                    .getMemberCd());
            } catch (Exception e) {
            	log.error(e.getMessage(),e);
                return forwardError("获取订单会员信息错误！");
            }
        }

		Mail mail = new Mail();
		mail.setApplicationName("hotel");
		mail.setTo(new String[] { toaddress });

		String subject = "";
		String templateNo = "";
		String xmlString = "";
		String state = member.getState();
		HtlHotel hotel = hotelService.findHotel(order.getHotelId().longValue());

		if (order.isCooperateOrder()) { // 是否交行等渠道订单 - add by chenkeming
			if (sendtype == MemberConfirmType.CONFIRM) {
				subject = "酒店预订成功";

				templateNo = FaxEmailModel.JIAOHANG_ORDER_EMAIL_SEND_CONFIRM;
				xmlString = msgAssist.genOrderMailByGuest114Confirm(order,
						hotel, member, sender);
			} else {
				subject = "酒店预订已经成功取消";

				templateNo = FaxEmailModel.JIAOHANG_ORDER_EMAIL_SEND_CANCEL;
				xmlString = msgAssist.genOrderMailByGuest114Cancel(order,
						hotel, member, sender);
			}
		} else if (order.isMango()) {
			if (OrderSource.FROM_HK.equals(order.getSource())) { // 香港
				subject = "芒果網酒店預定";
				if (sendtype == MemberConfirmType.CONFIRM) {
					subject = "芒果網酒店預定成功";

					templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK_HK;
					xmlString = msgAssist.genOrderMailByGuestMangoConfirm(
							order, hotel, member, sender);
				} else {
					subject = "芒果網酒店預定已經成功取消";

					templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOCANCEL_HK;
					xmlString = msgAssist.genOrderMailByGuestMangoCancel(order,
							hotel, member, sender);
				}
				// mail.setFrom("cs@mangocity.com");
			} else {
				if (sendtype == MemberConfirmType.CONFIRM) {
					subject = "芒果网酒店预定成功";
					if (OrderSource.FAN_TI_NET.equals(order.getSource())) {
						templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MANGOBOOK_HK_BIGFIVE;
					} else {
						templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK_HK;
					}
					xmlString = msgAssist.genOrderMailByGuestMangoConfirm(
							order, hotel, member, sender);
				} else {
					subject = "芒果网酒店预定已经成功取消";
					if (OrderSource.FAN_TI_NET.equals(order.getSource())) {
						templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MANGOCANCEL_HK_BIGFIVE;
					} else {
						templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOCANCEL_HK;
					}

					xmlString = msgAssist.genOrderMailByGuestMangoCancel(order,
							hotel, member, sender);
				}
				// mail.setFrom("cs@mangocity.com");
			}
		} else if ("LTT".equals(state)) {
			if (sendtype == MemberConfirmType.CONFIRM) {
				subject = "联通商旅酒店预定成功";

				templateNo = FaxEmailModel.UNICOM_ORDER_EMAIL_SEND_CONFIRM;
				xmlString = msgAssist.genOrderMailByGuest114Confirm(order,
						hotel, member, sender);
			} else {
				subject = "联通商旅酒店预定已经成功取消";

				templateNo = FaxEmailModel.UNICOM_ORDER_EMAIL_SEND_CANCEL;
				xmlString = msgAssist.genOrderMailByGuest114Cancel(order,
						hotel, member, sender);
			}
			// mail.setFrom("cs@mangocity.com");
		} else if ("WTT".equals(state)) {
			if (sendtype == MemberConfirmType.CONFIRM) {
				subject = "网通商旅热线酒店预定成功";

				templateNo = FaxEmailModel.NETCOM_ORDER_EMAIL_SEND_CONFIRM;
				xmlString = msgAssist.genOrderMailByGuest114Confirm(order,
						hotel, member, sender);
			} else {
				subject = "网通商旅热线酒店预定已经成功取消";

				templateNo = FaxEmailModel.NETCOM_ORDER_EMAIL_SEND_CANCEL;
				xmlString = msgAssist.genOrderMailByGuest114Cancel(order,
						hotel, member, sender);
			}
			// mail.setFrom("cs@mangocity.com");
		} else if ("WTBJ".equals(state)) {
			if (sendtype == MemberConfirmType.CONFIRM) {
				subject = "北京网通114电话导航酒店预定成功";

				templateNo = FaxEmailModel.BJNETCOM_ORDER_EMAIL_SEND_CONFIRM;
				xmlString = msgAssist.genOrderMailByGuest114Confirm(order,
						hotel, member, sender);
			} else {
				subject = "北京网通114电话导航酒店预定已经成功取消";

				templateNo = FaxEmailModel.BJNETCOM_ORDER_EMAIL_SEND_CANCEL;
				xmlString = msgAssist.genOrderMailByGuest114Cancel(order,
						hotel, member, sender);
			}
			// mail.setFrom("cs@mangocity.com");
		} else if ("NHZY".equals(state)) {
			if (sendtype == MemberConfirmType.CONFIRM) {
				subject = "南航酒店预定成功";

				templateNo = FaxEmailModel.NHZY_ORDER_EMAIL_SEND_CONFIRM;
				xmlString = msgAssist.genOrderMailByGuest114Confirm(order,
						hotel, member, sender);
			} else {
				subject = "南航酒店预定已经成功取消";

				templateNo = FaxEmailModel.NHZY_ORDER_EMAIL_SEND_CANCEL;
				xmlString = msgAssist.genOrderMailByGuest114Cancel(order,
						hotel, member, sender);
			}
			// mail.setFrom("cs@mangocity.com");
		} else if ("XJG".equals(state)) {
			if (sendtype == MemberConfirmType.CONFIRM) {
				subject = "芒果网酒店预定成功";
				if (OrderSource.FAN_TI_NET.equals(order.getSource())) {
					templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MANGOBOOK_HK_BIGFIVE;
				} else {
					templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK_HK;
				}
				xmlString = msgAssist.genOrderMailByGuestMangoConfirm(order,
						hotel, member, sender);
			} else {
				subject = "芒果网酒店预定已经成功取消";
				if (OrderSource.FAN_TI_NET.equals(order.getSource())) {
					templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MANGOCANCEL_HK_BIGFIVE;
				} else {
					templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOCANCEL_HK;
				}

				xmlString = msgAssist.genOrderMailByGuestMangoCancel(order,
						hotel, member, sender);
			}
		} else {
			if (sendtype == MemberConfirmType.CONFIRM) {
				subject = "114号码百事通酒店预定成功";

				templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_114BOOK;
				xmlString = msgAssist.genOrderMailByGuest114Confirm(order,
						hotel, member, sender);
			} else {
				subject = "114号码百事通酒店预定已经成功取消";

				templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_114CANCEL;
				xmlString = msgAssist.genOrderMailByGuest114Cancel(order,
						hotel, member, sender);
			}
			// mail.setFrom("cs@mangocity.com");
		}
		mail.setSubject(subject);
		mail.setTemplateFileName(templateNo);
		mail.setXml(xmlString);
		mail.setFrom("cs@mangocity.com");
		mail.setUserLoginId(roleUser.getLoginName());
		Long ret = null;
		try {
			ret = communicaterService.sendEmail(mail);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendStatus = "2";
			// return super.forwardMsgBox("邮件发送失败！", "refreshSelf()");
		}

		int sendedStatus = MemberConfirmSmsStutas.SENDING; // 发送状态
		// 改变了逻辑:V2.7.1 发送成功或失败都需要记录发送日志 chenjiajie 2009-02-17
		if (null == ret || 0 >= ret) {
			sendStatus = "2";
			sendedStatus = MemberConfirmSmsStutas.FAILED;
		} else {
			sendStatus = "1";
		}
		if (!order.isSendedMemberFax()) {
			order.setSendedMemberFax(true);
		}
		// 已发送客户确认
		boolean bChange = false;
		if (!order.isCustomerConfirm() && sendtype == MemberConfirmType.CONFIRM) {
			order.setCustomerConfirm(true);
			sendCustomerconfirmLog(order, roleUser);
			bChange = true;
		}
		if (bChange) {
			addMemberConfirmLog();
		}

		OrMemberConfirm memberConfirm = new OrMemberConfirm();
		memberConfirm.setChannel(ConfirmType.EMAIL);
		memberConfirm.setType(sendtype);
		memberConfirm
				.setModelType((order.isMango() || state == "XJG") ? ModelType.MODEL_MANGO
						: ModelType.MODEL_114);
		if (order.getRmpOrder()) {
			memberConfirm.setModelType(order.getType());// 114还是芒果
		} 
		memberConfirm.setSendTarget(toaddress);
		memberConfirm.setSendMan(roleUser.getName());
		memberConfirm.setSendTime(new Date());
		memberConfirm.setSendSucceed(true);

		/** v2.7.1 RMS 2416订单页面增加短信发送状态显示及实时更新功能 chenjiajie 2009-02-17 begin **/
		memberConfirm.setSendManId(roleUser.getLoginName()); // 发送人的工号
		memberConfirm.setUnicallRetId(ret); // UnitCall返回的流水号
		memberConfirm.setSendStatus(sendedStatus); // 发送状态，初始化为1
		/** v2.7.1 RMS 2416订单页面增加短信发送状态显示及实时更新功能 chenjiajie 2009-02-17 end **/

		memberConfirm.setOrder(order);
		order.getMemberConfirmList().add(memberConfirm);

		// 已发送客户确认
		order.setSendedMemberFax(true);
		if (roleUser.isOrgMid()) {
			order.setModifiedMidTime(new Date());
		} else if (roleUser.isOrgFront()) {
			order.setModifiedFrontTime(new Date());
		}

		// v2.8.1 增加操作人 add by chenkeming
		order.setModifier(roleUser.getLoginName());
		order.setModifierName(roleUser.getName());

		saveOrUpdateOrder(order);
		if ("2".equals(sendStatus)) {
			return super.forwardMsgBox("邮件发送失败！", "refreshSelf()");
		} else {
			return super.forwardMsgBox("邮件发送成功！", "refreshSelf()");
		}
	}

	/**
	 * 发送客人确认日志
	 * 
	 */
	private void addMemberConfirmLog() {
		StringBuffer strCmp = new StringBuffer();
		strCmp.append("订单改为:<font color='red'>" + "已发送客户确认" + "</font>");
		OrHandleLog handleLog = new OrHandleLog();
		handleLog.setModifierName(roleUser.getName());
		handleLog.setModifierRole(roleUser.getLoginName());
		handleLog.setBeforeState(order.getOrderState());
		handleLog.setAfterState(order.getOrderState());
		handleLog.setContent(strCmp.toString());
		handleLog.setModifiedTime(new Date());
		handleLog.setHisNo(order.getHisNo());
		handleLog.setOrder(order);
		order.getLogList().add(handleLog);

		OrderUtil.updateStayInMid(order);
	}

	/**
	 * 取出预定邮件列表
	 * 
	 * @param
	 * @return List
	 */
	public String choiceEmailView() {
		// 如果是魅影订单
		if (isShadowOrder.equals("1")) {
			order = getOrder(orderId);
			List<AffirmDTO> affirmTempListActive = new ArrayList<AffirmDTO>();
			List<AffirmDTO> affirmTempListNotActive = new ArrayList<AffirmDTO>();
			arrirmList = affirmFacade.queryAffirmByPPID(order.getOrOrderRMP()
					.getPricePlanId(), null, null);
			if (null != arrirmList && arrirmList.size() > 0) {
				int tempSize = arrirmList.size();
				for (int i = 0; i < tempSize; i++) {
					AffirmDTO affirm = arrirmList.get(i);
					if (affirm.getAffirmType() == 1) {
						if(affirm.isBetweenDate()){
							affirmTempListActive.add(affirm);
						}else{
							affirmTempListNotActive.add(affirm);
						}
					}
				}
				affirmTempListActive.addAll(affirmTempListNotActive);
				arrirmList = affirmTempListActive;
			}
			return "choiceEmailView";
		}
		hotelBookSetup = hotelService.getHtlBookSetupList(hotelID, faxType);
		return "choiceEmailView";
	}
	

	/**
	 * 取出预定传真列表
	 * 
	 * @param
	 * @return List
	 */
	public String choiceFaxView() {

		String forwardString = "choiceFaxView";
		// 如果是魅影订单
		if (isShadowOrder.equals("1")) {
			order = getOrder(orderId);
			List<AffirmDTO> affirmTempListActive = new ArrayList<AffirmDTO>();
			List<AffirmDTO> affirmTempListNotActive = new ArrayList<AffirmDTO>();
			arrirmList = affirmFacade.queryAffirmByPPID(order.getOrOrderRMP()
					.getPricePlanId(), null, null);
			if (null != arrirmList && arrirmList.size() > 0) {
				int tempSize = arrirmList.size();
				for (int i = 0; i < tempSize; i++) {
					AffirmDTO affirm = arrirmList.get(i);
					if (affirm.getAffirmType() == 2) {
						if(affirm.isBetweenDate()){
							affirmTempListActive.add(affirm);
						}else{
							affirmTempListNotActive.add(affirm);
						}
					}
				}
					affirmTempListActive.addAll(affirmTempListNotActive);
					arrirmList =affirmTempListActive;

			}
			return forwardString;
		}
		// 获取供应商传真信息
		hotelBookSetup = hotelService.getSupplierFax(hotelID, childRoomTypeId);
		// 如果该价格类型是由其他供应商提供的，只展示该供应商传真
		if (!hotelBookSetup.isEmpty())
			return forwardString;

		// 如果是由酒店提供的，只展示酒店供应商传真信息
		hotelBookSetup = hotelService.getHtlBookSetupList(hotelID, faxType);

		return forwardString;
	}
	/**
	 * 增加中升集团，用新疆电信的114号代替，修改了新疆典型的MEMBERCD为660009097127 邮件渠道，传真渠道 add by
	 * xiaowei.wang 2012.02.14
	 */
	private String getSender(MemberDTO member) {
		String sender = "";
		if (member.isMango()
				|| "XJG".equals(member.getMemberstate().toString())) {
			sender = "1";
		} else {
			if (member.getMemberstate().toString().equals("LTT")) {// 联通商旅 LTT
				sender = "4";
			} else if (member.getMemberstate().toString().equals("WTT")) {// 中国网通116114
				// WTT
				sender = "3";
			} else if (member.getMemberstate().toString().equals("WTBJ")) {// 北京网通114电话导航
				// WTBJ
				sender = "5";
			} else if (member.getMemberstate().toString().equals("NHZY")) {// 南航
																			// NHZY
				sender = "6";
			} else { // 114
				sender = "2";
			}
		}
		return sender;
	}

	private void assemblyMsgTemplate(OrOrder order, String bookingPhone,
			String auditPhone) {
		boolean needArrivalTime = OrderUtil.isShowArrivalTime(order);
		HtlHotel hotel = hotelService.findHotel(order.getHotelId().longValue());
		String hotelLink = "(" + hotel.getChnAddress() + " "
				+ hotel.getTelephone() + ")";

		String hotelInfoStr = order.getHotelName();

		Date choutDate = new Date(order.getCheckoutDate().getTime() + 24 * 60
				* 60 * 1000);

		String dateStr = "";
		long checkInLong = order.getCheckinDate().getTime() + 24 * 60 * 60
				* 1000;
		long checkOutLong = choutDate.getTime();
		if (checkOutLong > checkInLong) {
			dateStr = DateUtil.toStringByFormat(order.getCheckinDate(), "MM")
					+ "月"
					+ DateUtil.toStringByFormat(order.getCheckinDate(), "dd")
					+ "-"
					+ DateUtil.toStringByFormat(order.getCheckoutDate(), "dd")
					+ "日";
		} else {
			dateStr = DateUtil.toStringByFormat(order.getCheckinDate(),
					"MM月dd日");
		}

		String roomType = (order.getRoomQuantity() + "").trim() + "间"
				+ order.getRoomTypeName();
		confirmString = dateStr + hotelInfoStr + hotelLink + roomType + "已确认";

		if (needArrivalTime) {
			confirmString += "，保留至" + order.getLatestArrivalTime() + "点";
		}

		cancelString = "您预订的" + dateStr + order.getHotelName() + roomType
				+ "已取消,如有需要请再致电" + bookingPhone;

         pauseString = "您预订的" + dateStr + order.getHotelName() + roomType
             + "正在处理中,我们将在";

		roomCrowdedString = "满房通知:您预订的" + dateStr + order.getHotelName()
				+ roomType + "已满,如需再预订请尽快致电" + bookingPhone;

		changeRoomPriceString = "价格调整通知:您预订的" + dateStr + order.getHotelName()
				+ roomType + "价格有变,请尽快致电" + bookingPhone;

		guaranteeAnnounce = "担保通知:您预订的" + dateStr + order.getHotelName()
				+ roomType + ",房间紧张故需担保,请尽快致电" + bookingPhone;

		prepayAnnounce = "付款通知:您预订的" + dateStr + order.getHotelName()
				+ roomType + ",酒店要求提前付款,请尽快致电" + bookingPhone;

		if (null!=order.getMemberAliasId() && "9400100001,9200600002,8500200002,8500200001".indexOf(order
				.getMemberAliasId()) >= 0) {
			checkoutAdvanced = "您预订的" + dateStr + order.getHotelName()
					+ ",酒店反馈您已提前退房,如不符请致电" + auditPhone;

			partCheckin = "您预订的" + dateStr + order.getHotelName() + roomType
					+ ",酒店反馈您未全部入住,如不符请致电" + auditPhone;

			noShow = "您预订的" + dateStr + order.getHotelName() + roomType
					+ ",酒店反馈您未入住,如不符请致电" + auditPhone;
		} else {
			checkoutAdvanced = "您预订的" + dateStr + order.getHotelName()
					+ ",酒店反馈您已提前退房,如不符请直接回复退房日期，以确保您的积分准确。";

			partCheckin = "您预订的" + dateStr + order.getHotelName() + roomType
					+ ",酒店反馈您未全部入住,若有不符请直接回复“入住人姓名+房号”，以确保您的积分准确。";

			noShow = "您预订的" + dateStr + order.getHotelName() + roomType
					+ ",酒店反馈您未入住,若有不符请直接回复“入住人姓名+房号”，以确保您的积分准确。";
		}

	}

	public void sendCustomerconfirmLog(OrOrder order, UserWrapper roleUser) {
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
		htlOrderStsLog.setModifyName(roleUser.getName());
		htlOrderStsLog.setModifyCode(roleUser.getLoginName());
		htlOrderStsLog.setModifyTime(new Date());

		htlOrderStsLogService.insert(htlOrderStsLog);
	}

	public void sendHotelconfirmLog(OrOrder order, UserWrapper roleUser,
			boolean flag_1, boolean flag_2) {
		if (flag_1 || flag_2) {
			boolean bOld = order.isQuotaOk();
			boolean bOld1 = order.isSendedHotelFax();
			boolean bOld2 = order.isHotelConfirmTel();
			boolean bOld3 = order.isHotelConfirmFax();
			boolean bOld4 = order.isHotelConfirmFaxReturn();
			boolean bOld5 = order.isCustomerConfirm();

			HtlOrderStsLog htlOrderStsLog = new HtlOrderStsLog();
			htlOrderStsLog.setOldQuotaOk(bOld == false ? 0 : 1);
			htlOrderStsLog.setNewQuotaok(bOld == false ? 0 : 1);
			if (flag_1) {
				htlOrderStsLog.setOldSendedhotelconfirm(0);
				htlOrderStsLog.setNewSendedhotelconfirm(1);
			} else {
				htlOrderStsLog.setOldSendedhotelconfirm(bOld1 == false ? 0 : 1);
				htlOrderStsLog.setNewSendedhotelconfirm(bOld1 == false ? 0 : 1);
			}
			htlOrderStsLog.setOldHoteloralconfirm(bOld2 == false ? 0 : 1);
			htlOrderStsLog.setNewHoteloralconfirm(bOld2 == false ? 0 : 1);
			htlOrderStsLog.setOldHotelwrittenconfirm(bOld3 == false ? 0 : 1);
			htlOrderStsLog.setNewHotelwrittenconfirm(bOld3 == false ? 0 : 1);
			if (flag_2) {
				htlOrderStsLog.setOldHotelconfirmfaxreturn(1);
				htlOrderStsLog.setNewHotelconfirmfaxreturn(0);
			} else {
				htlOrderStsLog.setOldHotelconfirmfaxreturn(bOld4 == false ? 0
						: 1);
				htlOrderStsLog.setNewHotelconfirmfaxreturn(bOld4 == false ? 0
						: 1);
			}
			htlOrderStsLog.setOldCustomerconfirm(bOld5 == false ? 0 : 1);
			htlOrderStsLog.setNewCustomerconfirm(bOld5 == false ? 0 : 1);
			String orderCd = order.getOrderCD();
			htlOrderStsLog.setOrdercd(Long.parseLong(orderCd));
			htlOrderStsLog.setModifyName(roleUser.getName());
			htlOrderStsLog.setModifyCode(roleUser.getLoginName());
			htlOrderStsLog.setModifyTime(new Date());

			htlOrderStsLogService.insert(htlOrderStsLog);
		}
	}

	/** getter and setter begin */
	public String getCancelString() {
		return cancelString;
	}

	public void setCancelString(String cancelString) {
		this.cancelString = cancelString;
	}

	public String getConfirmString() {
		return confirmString;
	}

	public void setConfirmString(String confirmString) {
		this.confirmString = confirmString;
	}

	public String getNoContactString() {
		return noContactString;
	}

	public void setNoContactString(String noContactString) {
		this.noContactString = noContactString;
	}

	public String getPauseString() {
		return pauseString;
	}

	public void setPauseString(String pauseString) {
		this.pauseString = pauseString;
	}

	public String getReturnString() {
		return returnString;
	}

	public void setReturnString(String returnString) {
		this.returnString = returnString;
	}

	public CommunicaterService getCommunicaterService() {
		return communicaterService;
	}

	public void setCommunicaterService(CommunicaterService communicaterService) {
		this.communicaterService = communicaterService;
	}

	public String getFaxNum() {
		return faxNum;
	}

	public void setFaxNum(String faxNum) {
		this.faxNum = faxNum;
	}

	public String getFaxType() {
		return faxType;
	}

	public void setFaxType(String faxType) {
		this.faxType = faxType;
	}

	public HotelFaxManager getHotelFaxManager() {
		return hotelFaxManager;
	}

	public void setHotelFaxManager(HotelFaxManager hotelFaxManager) {
		this.hotelFaxManager = hotelFaxManager;
	}

	public HotelInfo getHotelInfo() {
		return hotelInfo;
	}

	public void setHotelInfo(HotelInfo hotelInfo) {
		this.hotelInfo = hotelInfo;
	}

	public List getOrderItemGroupByList() {
		return orderItemGroupByList;
	}

	public void setOrderItemGroupByList(List orderItemGroupByList) {
		this.orderItemGroupByList = orderItemGroupByList;
	}

	public OrOrderFaxDao getOrOrderFaxDao() {
		return orOrderFaxDao;
	}

	public void setOrOrderFaxDao(OrOrderFaxDao orOrderFaxDao) {
		this.orOrderFaxDao = orOrderFaxDao;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getTohotelNotes() {
		return tohotelNotes;
	}

	public void setTohotelNotes(String tohotelNotes) {
		this.tohotelNotes = tohotelNotes;
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

	public Long getSumTo() {
		return sumTo;
	}

	public void setSumTo(Long sumTo) {
		this.sumTo = sumTo;
	}

	public Long getHotelID() {
		return hotelID;
	}

	public void setHotelID(Long hotelID) {
		this.hotelID = hotelID;
	}

	public List getHotelBookSetup() {
		return hotelBookSetup;
	}

	public void setHotelBookSetup(List hotelBookSetup) {
		this.hotelBookSetup = hotelBookSetup;
	}

	public String getIsAnewSend() {
		return isAnewSend;
	}

	public void setIsAnewSend(String isAnewSend) {
		this.isAnewSend = isAnewSend;
	}

	public Long getRoomSumTo() {
		return roomSumTo;
	}

	public void setRoomSumTo(Long roomSumTo) {
		this.roomSumTo = roomSumTo;
	}

	public String getConfirmNo() {
		return confirmNo;
	}

	public void setConfirmNo(String confirmNo) {
		this.confirmNo = confirmNo;
	}

	public double getOrderSum() {
		return orderSum;
	}

	public void setOrderSum(double orderSum) {
		this.orderSum = orderSum;
	}

	public String getRoomCrowdedString() {
		return roomCrowdedString;
	}

	public void setRoomCrowdedString(String roomCrowdedString) {
		this.roomCrowdedString = roomCrowdedString;
	}

	public String getChangeRoomPriceString() {
		return changeRoomPriceString;
	}

	public void setChangeRoomPriceString(String changeRoomPriceString) {
		this.changeRoomPriceString = changeRoomPriceString;
	}

	public String getPrepayAnnounce() {
		return prepayAnnounce;
	}

	public void setPrepayAnnounce(String prepayAnnounce) {
		this.prepayAnnounce = prepayAnnounce;
	}

	public String getGuaranteeAnnounce() {
		return guaranteeAnnounce;
	}

	public void setGuaranteeAnnounce(String guaranteeAnnounce) {
		this.guaranteeAnnounce = guaranteeAnnounce;
	}

	/** getter and setter end */

	public String getCheckoutAdvanced() {
		return checkoutAdvanced;
	}

	public void setCheckoutAdvanced(String checkoutAdvanced) {
		this.checkoutAdvanced = checkoutAdvanced;
	}

	public String getPartCheckin() {
		return partCheckin;
	}

	public void setPartCheckin(String partCheckin) {
		this.partCheckin = partCheckin;
	}

	public String getNoShow() {
		return noShow;
	}

	public void setNoShow(String noShow) {
		this.noShow = noShow;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Long getMemberConfirmId() {
		return memberConfirmId;
	}

	public void setMemberConfirmId(Long memberConfirmId) {
		this.memberConfirmId = memberConfirmId;
	}

	public IMemberConfirmService getMemberConfirmService() {
		return memberConfirmService;
	}

	public void setMemberConfirmService(
			IMemberConfirmService memberConfirmService) {
		this.memberConfirmService = memberConfirmService;
	}

	public int getHotelBookSetupNumber() {
		return hotelBookSetupNumber;
	}

	public void setHotelBookSetupNumber(int hotelBookSetupNumber) {
		this.hotelBookSetupNumber = hotelBookSetupNumber;
	}

	public IMsgService getMgMsgInterface() {
		return mgMsgInterface;
	}

	public void setMgMsgInterface(IMsgService mgMsgInterface) {
		this.mgMsgInterface = mgMsgInterface;
	}

	public HtlOrderStsLogService getHtlOrderStsLogService() {
		return htlOrderStsLogService;
	}

	public void setHtlOrderStsLogService(
			HtlOrderStsLogService htlOrderStsLogService) {
		this.htlOrderStsLogService = htlOrderStsLogService;
	}

	public AffirmFacade getAffirmFacade() {
		return affirmFacade;
	}

	public void setAffirmFacade(AffirmFacade affirmFacade) {
		this.affirmFacade = affirmFacade;
	}

	public String getIsShadowOrder() {
		return isShadowOrder;
	}

	public void setIsShadowOrder(String isShadowOrder) {
		this.isShadowOrder = isShadowOrder;
	}

	public List<AffirmDTO> getArrirmList() {
		return arrirmList;
	}

	public void setArrirmList(List<AffirmDTO> arrirmList) {
		this.arrirmList = arrirmList;
	}

	public SupplierFacade getSupplierFacade() {
		return supplierFacade;
	}

	public void setSupplierFacade(SupplierFacade supplierFacade) {
		this.supplierFacade = supplierFacade;
	}

}
