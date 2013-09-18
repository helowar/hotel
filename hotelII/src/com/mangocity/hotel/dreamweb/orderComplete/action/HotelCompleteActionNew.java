package com.mangocity.hotel.dreamweb.orderComplete.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;
import com.mangocity.hotel.base.persistence.OrGuaranteeItem;
import com.mangocity.webnew.persistence.ElongAssureResult;
import com.mangocity.webnew.service.CheckElongAssureService;
import com.mangocity.hotel.base.service.ChannelCashBackManagerService;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.dreamweb.datacheck.service.BookingDataCheckService;
import com.mangocity.hotel.dreamweb.datacheck.service.impl.BookingDataCheckServiceImpl;
import com.mangocity.hotel.dreamweb.orderrecord.service.BookOrderRecordService;
import com.mangocity.hotel.dreamweb.search.dao.impl.HotelBookDaoImpl;
import com.mangocity.hotel.dreamweb.search.service.HotelInfoService;
import com.mangocity.hotel.ejb.reservation.HotelSupplyDelegate;
import com.mangocity.hotel.ext.member.dto.MbrInfoDTO;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.exception.MemberException;
import com.mangocity.hotel.ext.member.util.MemberUtil;
import com.mangocity.hotel.order.constant.OrderExtInfoType;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.OrCouponRecords;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrFulfillment;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderExtInfo;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrPointRecords;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.orderrecord.model.OrderRecord;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.util.PriceUtil;
import com.mangocity.hotel.supply.dto.OrOrderInfoDTO;
import com.mangocity.hotel.supply.facade.HotelOrderSupplyProcessFacade;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.lang.StringUtils;
import com.mangocity.model.mbrinterface.MbrInfo;
import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.model.person.PersonMainInfo;
import com.mangocity.proxy.vo.MembershipVO;
import com.mangocity.services.mbrship.MbrshipServiceException;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.hotel.constant.WarningContant;
import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.CookieUtils;
import com.mangocity.webnew.constant.OrderPayType;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.util.action.OrderCompleteWebAction;
import com.opensymphony.xwork2.ActionContext;

/**
 * @author houdiandian
 * @see 这个可以接受从订单核对页过来的订单
 */
public class HotelCompleteActionNew extends OrderCompleteWebAction {

	private static final MyLog log = MyLog.getLogger(HotelCompleteActionNew.class);
	private static final long serialVersionUID = 1859286250134885159L;

	private Map params;
	private String orderCD;
	private String cardID ;
	private final static String FORWARD_PAYONLINE = "payOnline";
	private HotelBasicInfo hotelBasicInfo;
	private String sid;
	private static final String ERROR_MESSAGE = "很抱歉，预订失败!该段时间酒店价格发生变化，请致电芒果网客服电话40066-40066或者重新选择别的酒店！";
	// 注入的service
	private IHotelFavourableReturnService returnService;
	private HotelBookDaoImpl hotelBookDao;
	private HotelInfoService hotelInfoService;
	private BookingDataCheckService bookingDataCheckService;
	private boolean isPoitAndUlmAllPay = false;
	private CheckElongAssureService checkElongAssureService;
	/**
	 * 限量返现促销活动
	 */
	private HtlLimitFavourableManage limitFavourableManage;
	private BookOrderRecordService bookOrderRecordService;
	private HotelSupplyDelegate hotelSupplyDelegate;
    private String isRegisterMember;
	public String execute() {
		super.cleanValueToSession("oldorderCd");

		String forward = "";
		
		//1,初始化渠道返现比例控制参数
		initreturncashrate();
		
		order = orderService.getOrOrderByOrderCd(orderCD);
		if (order != null) {
			saveYouBiBi();
			if (order.getOrderItems().size() > 0) {
				super.setErrorCode("HC01");
				return super.forwardError(WarningContant.REPEAT_SAME_ORDER);
			}
			order = orderService.getOrOrderByOrderCd(orderCD);
		}
		member = super.getMemberInfoForWeb(true);
		params = super.getParams();
		
		setParamsSqlReplace(params);
		
		hotelOrderFromBean = new HotelOrderFromBean();
		MyBeanUtil.copyProperties(hotelOrderFromBean, params);
		hotelOrderFromBean.setCurrencyStr(PriceUtil.currencyMap.get(hotelOrderFromBean.getCurrency()));
		ElongAssureResult assureResult=null;
		
		saveOrderRecord();// 记录预订订单流程相关信息 add by ting.li
	    addLogInfo(hotelOrderFromBean,"hotelCompleteActionNew ");
		
		rate = CurrencyBean.getRateToRMB(hotelOrderFromBean.getCurrency());
		
		try {
			
			if (hotelOrderFromBean.getRoomChannel()==ChannelType.CHANNEL_ELONG){//艺龙担保校验
				assureResult = checkElongAssureService.checkIsAssure(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getChildRoomTypeId()
		   				, hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(), Integer.parseInt(hotelOrderFromBean.getRoomQuantity()), hotelOrderFromBean.getArrivalTime());
				if(assureResult.isNeedAssure()  && !hotelOrderFromBean.isNeedAssure()){
					forward = "orderCheck";
					return forward;
				}
			}else if (bookingDataCheckService.checkChangeBookData(hotelOrderFromBean)) {
				forward = "orderCheck";
				return forward;
			}
		} catch (Exception e) {
			String orderCdError="";
            if(order!=null) {
            	orderCdError=order.getOrderCD();
            }
			log.error("校验担保报错" +orderCdError, e);
		}
		
		//面付担保、预付需要校验有没有信用卡 ，没有，返回订单填写页
		if (!checkHasCardId()){
			forward = "orderCheck";
			return forward;
		}
		
				
		try {
			// 是否满房
			if (hotelCheckOrderService.isRoomFull(params)) {
				super.setErrorCode("HC00");
				return super.forwardError("很抱歉，该房型已经满房了。如需要，请致电芒果网客服电话40066-40066或者重新选择别的酒店！");
			}
			// 查询酒店基本信息 by ting.li
			hotelBasicInfo = hotelInfoService.queryHotelInfoByHotelId(String.valueOf(hotelOrderFromBean.getHotelId()));
			// 判断预订数据是否被修改，如果预订数据被修改了，并且没有获取信用卡信息,则跳到信用卡信息填写页面

			// 面付非担保
			if (PayMethod.PAY.equals(hotelOrderFromBean.getPayMethod()) && !hotelOrderFromBean.isPayToPrepay() && !hotelOrderFromBean.isNeedAssure()) {
				String result = savePayOrder(assureResult);
				if (result == null) {
					forward = SUCCESS;
				} else {
					super.setErrorCode("HC05");
					return super.forwardError(ERROR_MESSAGE);
				}
			}
			if(hotelOrderFromBean.getReturnAmount()>=0.0 && memberCd!=null && !"".equals(memberCd)){
				//如果有返现 则关联会员号，这里是为了面付非担保之外的酒店做的
				order.setMemberCd(memberCd);
				order.setMemberId(mbrID);
			}
			// 面付担保，现直连酒店没有面付担保，考虑其未来的发展，应提供相应的接口
			if (PayMethod.PAY.equals(order.getPayMethod()) && order.isCreditAssured()) {
				payAssureOrderProcess();
			/*
				// 扣配额
			//	super.deductQuotaAndFillOrderItems(order, hotelOrderFromBean);
				// 修改状态				
				// 信用卡信息
				order.setCashBackTotal(hotelOrderFromBean.getReturnAmount());
				setOrderAssure(order, cardID);				
				order.getOrOrderExtInfoList().add(new OrOrderExtInfo(BaseConstant.ORDER_EXTINFO_SID, sid, order));
				//orderService.saveOrUpdate(order);
				super.saveOrderToMid(order);
				//hotelOrderCompleteService.updateMemberCredit(cardID, order);
				// 酒店发传真,//客人发短信
				
				if (hotelOrderFromBean.getRoomChannel() < 1) {
				    ///*modified at 2012-8-15 11:29:00 3697_自签酒店中担保、预付的酒店取消网站系统发送传真
					boolean flagSuccess_sendOrderInfoToHotel = super.sendOrderInfoToHotel(order, member);
					boolean quotaOk = orderImmedConfirmService.quotaOk(order);
					super.saveOrderToMid(order);

					// 面付担保不需要发送客户确认
					 if(flagSuccess_sendOrderInfoToHotel && quotaOk){
					 orderImmedConfirmService.sendImmedConfirmToCus(order,false, member);
					 }
				}
				// 直连酒店
				if (!order.isManualOrder() && hotelOrderFromBean.isChannelToWith()) {
					String hdlOrderPreStr = super.saveHDLOrderInfo();
					if (StringUtil.isValidStr(hdlOrderPreStr)) {
						super.setErrorCode("HC02");
						return super.forwardError(ERROR_MESSAGE);
					}
					super.saveOrderToMid(order);
				}				
			
				*/
				forward = SUCCESS;
			}

			// 设置积分和代价券
			if (PayMethod.PRE_PAY.equals(order.getPayMethod())) {
				setPointAndCoupon();
			}
			
			// 预付离线支付 , 现中旅不支持离线支付
			if (PayMethod.PRE_PAY.equals(order.getPayMethod()) && OrderPayType.CREDIT_PAY == hotelOrderFromBean.getOrderPayType()) {
				if (order.getChannel() == 0) { // 现直连没有预付，如果直连有预付，修改该逻辑
					String messageAwayLine = savePrepayAwayLineOrderInfo();
					if (messageAwayLine != null) {
						super.setErrorCode("HC03");
						return super.forwardError(messageAwayLine);
					}
					// 发酒店传真
					/*modified at 2012-8-15 11:29:00 3697_自签酒店中担保、预付的酒店取消网站系统发送传真
					super.sendOrderInfoToHotel(order, member);
					*/
					if (isPoitAndUlmAllPay) {
						orderImmedConfirmService.sendImmedConfirmToCus(order, false, member);
					}
					super.saveOrderToMid(order);

					forward = SUCCESS;
				}
			}
			// 预付在线支付,现只有中旅在线
			if (PayMethod.PRE_PAY.equals(order.getPayMethod()) && OrderPayType.ONLINE_PAY == hotelOrderFromBean.getOrderPayType()) {
				order.setChannel(ChannelType.CHANNEL_CTS);
				// test payonline
				// if(true){
				// super.saveOrderToMid(order);
				// return testpayonline();
				// }
				if (ChannelType.CHANNEL_CTS == order.getChannel()) { // 中旅
					String messageOnline = savePrepayOnLineOrderInfo();
					if (messageOnline == null) {
						forward = FORWARD_PAYONLINE;
					} else if (SUCCESS.equals(messageOnline)) {
						forward = SUCCESS;
					} else if (messageOnline != null) {
						super.setErrorCode("HC04");
						return super.forwardError(messageOnline);
					}

				} else if (false) {// 非中旅,可以添加其他的渠道的在线支付

				}
			}

			// 添加界面展示和电商需要的数据统计
			super.addEBSomeValue();// 电商

			if (hotelOrderFromBean.getReturnAmount() > 0 && null != order.getOrderCD() && member != null) {
				hotelOrderCompleteService.fillCashInformation(order, order.getOrderCD(), hotelOrderFromBean, member);
			}

			
		} catch (Exception e) {
			String ordercd=null;
			if(order!=null){
				
				ordercd=order.getOrderCD();
			}
			log.error("HotelComleteActionNew has a wrong + ordercd:"+ordercd, e);
		}
		
	    updateOrderRecord(order);
		
		if (SUCCESS.equals(forward) || FORWARD_PAYONLINE.equals(forward)) {
			return forward;
		}
		return super.forwardError("该房型无法安排，建议客人改订其他房型或其他酒店！");
	}
	/**
	 * 面付担保流程
	 * 
	 * @param order
	 */
	private void payAssureOrderProcess() {
		order.setCashBackTotal(hotelOrderFromBean.getReturnAmount());
		// 扣配额
		// super.deductQuotaAndFillOrderItems(order, hotelOrderFromBean);
		// 修改状态
		// 信用卡信息
		setOrderAssure(order, cardID);
		order.getOrOrderExtInfoList().add(new OrOrderExtInfo(BaseConstant.ORDER_EXTINFO_SID, sid, order));
		orderService.saveOrUpdate(order);
		super.saveOrderToMid(order);
		
		try{
			hotelSupplyDelegate.createCreditCardPreAuth(order, member);
		}catch(Exception e){
			log.error(" pay assure order has a wrong ", e);
		}
		
	}

	/**
	 * 面付非担保的处理流程
	 * 
	 * @return
	 * @throws Exception
	 */
	private String savePayOrder(ElongAssureResult assureResult){
		// 校验价格
		if (checkTheOrderPrice(hotelOrderFromBean)) {
			super.setErrorCode("HC06");
			return "priceChange";
		}
		if(hotelOrderFromBean.getReturnAmount()>=0.0){
		// 处理返现
		try {
			dealCashReturn();
		} catch (MbrshipServiceException e) {
			// TODO Auto-generated catch block
			log.error("面付非担保酒店处理返现时创建会籍失败",e);
		}
		}
		//多床型elong酒店订单添加特殊要求
		if(hotelOrderFromBean.getRoomChannel()==9 && hotelOrderFromBean.getBedTypeStr()!=null && hotelOrderFromBean.getBedTypeStr().indexOf(",")>-1){
			int bedtype = hotelOrderFromBean.getBedType();
        	String bedName= bedtype==1?"大床":(bedtype==2?"双床":(bedtype==3?"单床":""));
			hotelOrderFromBean.setSpecialRequest((hotelOrderFromBean.getSpecialRequest()==null?"":hotelOrderFromBean.getSpecialRequest())+"务必安排"+bedName);
		}
		// 记录订单
		super.combineOrderInfo();
		// 生成订单
		orderCD = super.processPaymentMethod(order);
		saveYouBiBi();
		if(memberCd!=null){
		order.setMemberCd(memberCd);
		order.setMemberId(mbrID);
		}
		if(assureResult!=null && assureResult.getAssureType()>0){
			if(assureResult.getAssureMoneyType()==1){
   				OrGuaranteeItem item = new OrGuaranteeItem();
   				item.setNight(order.getCheckinDate());
   				item.setAssureCondiction(this.getGuaranteeAssureCondition(assureResult));
   				item.setAssureType("2");
   				item.setAssureLetter("否");
   				item.setReserv(order.getReservation());
   				order.getReservation().getGuarantees().add(item);
   			}else if(assureResult.getAssureMoneyType()==2){
   				List<Date> datelist = DateUtil.getDates(order.getCheckinDate(), DateUtil.getPreviousDate(order.getCheckoutDate()));
   				for(Date date:datelist){
   					OrGuaranteeItem item = new OrGuaranteeItem();
   					item.setNight(date);
       				item.setAssureCondiction(this.getGuaranteeAssureCondition(assureResult));
   					item.setAssureType("4");
       				item.setAssureLetter("否");
       				item.setReserv(order.getReservation());
       				order.getReservation().getGuarantees().add(item);
   				}
   			}
			List extlist = order.getOrOrderExtInfoList();
           	OrOrderExtInfo ext1 = new OrOrderExtInfo();
           	ext1.setType(OrderExtInfoType.ELONG_ASSURE_TIP);
           	ext1.setContext(assureResult.getConditionStr());
           	ext1.setOrder(order);
           	OrOrderExtInfo ext2 = new OrOrderExtInfo();
           	ext2.setType(OrderExtInfoType.ELONG_ASSURE_MODIFY);
           	ext2.setContext(assureResult.getModifyStr());
           	ext2.setOrder(order);
           	extlist.add(ext1);
           	extlist.add(ext2);
           	order.setOrOrderExtInfoList(extlist);	
		}
		// 面付非担保

		// 扣配额，生成订单明细
		order.setCashBackTotal(hotelOrderFromBean.getReturnAmount());
		super.deductQuotaAndFillOrderItems(order, hotelOrderFromBean);

		super.saveOrderToMid(order);

		// 在订单完成时将cookie中的projectcode、exprojectcode1和exprojectcode2取出并保存 add by
		// wangjian --2012-7-17
		Cookie[] cookies = request.getCookies();
		if (cookies.length > 0 && null != orderCD) {
			htlProjectCodeManage.saveHtlProjectCode(cookies, orderCD);
		}

		// 酒店发传真,//客人发短信
		if (hotelOrderFromBean.getRoomChannel() < 1) {
			boolean flagSuccess_sendOrderInfoToHotel = super.sendOrderInfoToHotel(order, member);
			boolean quotaOk = orderImmedConfirmService.quotaOk(order);
			if (flagSuccess_sendOrderInfoToHotel && quotaOk) {
				orderImmedConfirmService.sendImmedConfirmToCus(order, false, member);
			}
		}
		// 直连酒店
		if (!order.isManualOrder() && hotelOrderFromBean.isChannelToWith()) {
			String hdlOrderPreStr = super.saveHDLOrderInfo();
			if (StringUtil.isValidStr(hdlOrderPreStr)) {
				super.setErrorCode("HC07");
				return hdlOrderPreStr;
			}
		}
		// 更新会员联系人或入住人
		if (member != null) {
			memberInterfaceService.updateMemberFellowAndLinkman(member, order, true, true);
		}

		return null;
	}

	/**
	 * 对于返现的处理
	 * @throws MbrshipServiceException
	 */
	private void dealCashReturn() throws MbrshipServiceException {
		String isRegister = hotelOrderFromBean.getIsRegisterMember();
		if("3".equals(isRegister)){//公共会籍下单,返现为0
			memberCd = "0001397022";
			mbrID = 3065103L;
			hotelOrderFromBean.setReturnAmount(0.0);
		}else if("2".equals(isRegister)){//当前会籍下单,
			memberCd=hotelOrderFromBean.getMemberCd();
			mbrID=hotelOrderFromBean.getMbrID();
			String isReturnCash =  hotelOrderFromBean.getIsReturnCash();
			if("false".equals(isReturnCash)){
				hotelOrderFromBean.setReturnAmount(0.0);
			}
		}else if("1".equals(isRegister)){//注册新会籍
			String mobile=(hotelOrderFromBean.getMobile()==null)?"":hotelOrderFromBean.getMobile();
			String email=(hotelOrderFromBean.getEmail()==null)?"":hotelOrderFromBean.getEmail();
			long mbrId= 0L;
			if(null != member){
				mbrId =member.getId();
			}
			List<MembershipVO> mbrshipList =memberInterfaceService.getMemberShipList(mbrId,mobile, email);
			if(null != mbrshipList && mbrshipList.size()>0){//会员存在，则添加会籍
		    	//给这个会员增加一个会籍---M 芒果会籍-芒果网站，并将其登陆
		    	Long mbrshipCategoryId=Long.parseLong(MbrInfo.MbrshipType.mangocityWeb.getValue());	
		    	Mbrship mbrship=mbrshipCreate(mbrshipList.get(0).getMbrId(),mbrshipCategoryId,null,null,null);
		    	memberCd=mbrship.getOldMbrshipCd();
		    	 mbrID=mbrshipList.get(0).getMbrId();
			}else{//会员不存在，注册新会员
				//用手机号码注册一个会员，并帮其自动登陆
				MbrInfoDTO mbrInfoDTO=new MbrInfoDTO();
				mbrInfoDTO.setMobileNo(mobile);
				mbrInfoDTO.setCategoryId(MbrInfo.MbrshipType.mangocityWeb);
				mbrInfoDTO.setGender(MbrInfo.Sex.male);
				PersonMainInfo personMainInfo=register(mbrInfoDTO);
				String mbrCd=personMainInfo.getOldMbrshipCd();
				//设置会自动注册会员的标识
			    autoFlag=true;
			    memberCd=mbrCd;
			    mbrID=personMainInfo.getMbrId();
			}
		}
	}

	private String testpayonline() {
		int onlinePaytype = hotelOrderFromBean.getOnlinePaytype();
		order.setPrepayType(onlinePaytype);

		// 支付相关的类 add by shengwei.zuo 2009-11-27
		OrPayment payment = new OrPayment();
		payment.setPayType(onlinePaytype);
		payment.setMoney(hotelOrderFromBean.getActuralAmount());
		Date now = new Date();
		if (null != member)
			payment.setConfirmer(member.getName());
		payment.setConfirmTime(now);
		payment.setCreator("HWEB");
		payment.setOperator("HWEB");
		payment.setCreateTime(now);
		payment.setOperateTime(now);
		payment.setOrder(order);
		order.getPaymentList().add(payment);
		OrHandleLog handleLog = new OrHandleLog();
		handleLog.setModifiedTime(new Date());
		String payTypeStr = PrepayType.payStrMap.get(onlinePaytype);
		handleLog.setContent("客人选择在线支付方式：" + payTypeStr);
		handleLog.setModifierName("网站");
		handleLog.setOrder(order);
		order.getLogList().add(handleLog);
		orderService.updateOrder(order);
		return FORWARD_PAYONLINE;
	}

	// 离线支付,扣积分，绑定代金券，
	private String savePrepayAwayLineOrderInfo() {
		String result = null;
		if (hotelOrderFromBean.isUseUlmPoint()) {
			try {
				// hotel2.9.3 增加了代金券支付方式，计算使用了积分的人民币需要再减去代金券的金额
				double ulmPointValue = Double.parseDouble(hotelOrderFromBean.getUlmPoint()) / 100;
				boolean isDeducted = hotelOrderCompleteService.deductUlmPoint(order, ulmPointValue, hotelOrderFromBean.isUseUlmPoint());
				if (!isDeducted) {
					super.setErrorCode("HC08");
					result = "抱歉，您所输入的积分不足已支付本次交易";
					return result;
				}
			} catch (MemberException e) {
				// 记录操作日志
				OrHandleLog handleLog = new OrHandleLog();
				handleLog.setModifiedTime(new Date());
				handleLog.setContent("客人选择积分支付，积分支付失败！");
				handleLog.setModifierName("网站");
				handleLog.setOrder(order);
				order.getLogList().add(handleLog);
				super.setErrorCode("HC09");
				result = WarningContant.CUT_POINT_ERROR;
			} catch (Exception e) {
				log.error("ErrCord:75211 HotelCompleteAction.fillPrepayOrderInfo() deduct member point is error: ", e);
			}
		}
		/** 如果使用了积分，则调用积分接口 end * */

		/** 如果使用了代金券，需要调用代金券接口 hotel2.9.3 add by chenjiajie begin* */
		if (hotelOrderFromBean.isUsedCoupon()) {
			hotelOrderCompleteService.deductUsedCoupon(params, order, member);
		}
		/** 如果使用了代金券，需要调用代金券接口 hotel2.9.3 add by chenjiajie end* */

		/* 如果使用了积分或代金券累加是全额支付 (扣配额) */
		if ((hotelOrderFromBean.isUseUlmPoint() || hotelOrderFromBean.isUsedCoupon()) && 0.001D > hotelOrderFromBean.getActuralAmount()) {
			log.info("==========HotelCompleteAction.fillPrepayOrderInfo() 积分或代金券累加是全额支付 code:5652 orderCD:" + orderCD + "=============");

			order.setHasPrepayed(true);
			order.setOrderState(OrderState.HAS_PAID);

			if (super.deductQuotaAndFillOrderItems(order, hotelOrderFromBean)) {
				isPoitAndUlmAllPay = true;
			}
		}
		/**
		 * 订单的支付类型 面付方式：(酒店前台面付:1) 预付方式：(信用卡支付:2),(营业部付款:3) 直联方式：(网上银行支付:4) add
		 * by chenjiajie 2009-11-06
		 */
		// 信用卡支付 (扣配额)
		else if (OrderPayType.CREDIT_PAY == hotelOrderFromBean.getOrderPayType()) {
			// 是否使用这个变量判断
			if (isInvoice) {
				log.info("==========HotelCompleteAction.fillPrepayOrderInfo() 信用卡支付->需要发票 code:565 orderCD:" + orderCD + "=============");
				OrFulfillment fulfill = new OrFulfillment();
				MyBeanUtil.copyProperties(fulfill, params);
				String invoiceDeliveryAddress = (String) params.get("invoiceDeliveryAddress");
				fulfill.setDeliveryAddress(invoiceDeliveryAddress);
				fulfill.setFulfillTaskType(3);// 任务类型为"配送"
				fulfill.setDeliveryType("FRP");// 配送方式为"免费挂号邮寄"
				order.setFulfill(fulfill);
			}
			if (StringUtil.isValidStr(cardID)) {
				// 绑定支付的信用卡
				order.setCreditCardIdsSelect(cardID);
				order.getOrOrderExtInfoList().add(new OrOrderExtInfo(BaseConstant.ORDER_EXTINFO_SID, sid, order));
			}
			OrPayment payment = new OrPayment();
			payment.setPayType(PrepayType.CreditCardDom);// 信用卡暂时写成国内
			payment.setMoney(hotelOrderFromBean.getActuralAmount());
			Date now = new Date();
			if (null != member)
				payment.setConfirmer(member.getName());
			payment.setConfirmTime(now);
			payment.setCreator("HWEB");
			payment.setOperator("HWEB");
			payment.setCreateTime(now);
			payment.setOperateTime(now);
			payment.setOrder(order);
			order.getPaymentList().add(payment);
			order.setCashBackTotal(hotelOrderFromBean.getReturnAmount());
            // 扣配额
	        super.deductQuotaAndFillOrderItems(order,hotelOrderFromBean);      
	        order.setOrderState(OrderState.SUBMIT_TO_MID);
		}
		// 营业部付款 (不扣配额)
		else if (OrderPayType.COUNTER_PAY == hotelOrderFromBean.getOrderPayType()) {
			log.info("==========HotelCompleteAction.fillPrepayOrderInfo() 现付 code:5654 orderCD:" + orderCD + "=============");
			// 设置配送信息
			OrFulfillment fulfill = new OrFulfillment();
			MyBeanUtil.copyProperties(fulfill, params);
			fulfill.setFulfillTaskType(1);// 任务类型为"营业部付款"
			fulfill.setDeliveryType("SDD");// 配送方式为"营业部自取"
			// 配送日期为空bug
			if (null == fulfill.getDeliveryDate() && null != fulfill.getFulfillPayDate()) {
				fulfill.setDeliveryDate(fulfill.getFulfillPayDate());
			}
			order.setFulfill(fulfill);
			OrPayment payment = new OrPayment();
			payment.setCurrencyType(hotelOrderFromBean.getCurrency());
			// 现付的方式
			String saleDepartmentPay = (String) params.get("saleDepartmentPay");
			if (saleDepartmentPay.equals("1")) {
				payment.setPayType(PrepayType.Cash);
			} else {
				payment.setPayType(PrepayType.POS);
			}
			// 如果是非人民币，也取人民币的价格
			payment.setMoney(hotelOrderFromBean.getActuralAmount());
			payment.setOrder(order);
			order.getPaymentList().add(payment);
			order.setOrderState(OrderState.SUBMIT_TO_MID);
			order.setToMidTime(DateUtil.getSystemDate());

			// 记录操作日志
			OrHandleLog handleLog = new OrHandleLog();
			handleLog.setModifiedTime(new Date());
			handleLog.setContent("会员使用营业部付款，未扣配额");
			handleLog.setModifierName("网站");
			handleLog.setOrder(order);
			order.getLogList().add(handleLog);
		}
		return null;
	}

	// 在线支付流程，返回success,空,错误
	private String savePrepayOnLineOrderInfo() {
		if (ChannelType.CHANNEL_CTS == hotelOrderFromBean.getRoomChannel()) {
			// 订单状态设置为“暂存前台”
			order.setOrderState(OrderState.NOT_SUBMIT);
			// 订单创建时间
			order.setCreateDate(new Date());
			// 获取中旅价格start the method is not good
			Date checkIn = hotelOrderFromBean.getCheckinDate();
			Date checkOut = hotelOrderFromBean.getCheckoutDate();
			int days = DateUtil.getDay(checkIn, checkOut);
			List<QueryHotelForWebSaleItems> priceLis = new ArrayList<QueryHotelForWebSaleItems>();
			try {
				priceLis = hotelBookingService.refreshHotelReservateExResponse(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(),
						hotelOrderFromBean.getChildRoomTypeId(), hotelOrderFromBean.getRoomChannel(), hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean
								.getCheckoutDate());
			} catch (Exception e) {
				log.error("===========HotelBookingAction==========CTS=========call HDL interfaces exception: ", e);
				super.setErrorCode("HC10");
				return WarningContant.PRICE_CHANGE;
			}
			StringBuffer saleBuffer = new StringBuffer();
			StringBuffer baseBuffer = new StringBuffer();
			if (null != priceLis && days <= priceLis.size()) {
				double reTotal = 0.0;
				for (int k = 0; k < priceLis.size(); k++) {
					QueryHotelForWebSaleItems it = (QueryHotelForWebSaleItems) priceLis.get(k);
					saleBuffer.append(it.getSalePrice() + "#");
					baseBuffer.append(it.getBasePrice() + "#");
					reTotal += it.getSalePrice();
					log.info("HotelBookingAction==========CTS===========priceLis[k].salePrice:" + it.getSalePrice());
					log.info("HotelBookingAction==========CTS===========priceLis[k].basePrice:" + it.getBasePrice());
				}
			}
			String hkPrices = saleBuffer.toString();
			String hkBasePrices = baseBuffer.toString();
			// 获取中旅价格end
			// 生成订单明细
			OrderUtil.generateOrderItems(order, hkPrices, hkBasePrices);
			// 拆单
			List<Object[]> liRes = hotelBookDao.queryCommidity(hotelOrderFromBean.getChildRoomTypeId(), hotelOrderFromBean.getPayMethod(), hotelOrderFromBean
					.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(), true);
			if (liRes == null || liRes.size() == 0) {
				super.setErrorCode("HC11");
				return WarningContant.PRICE_ADJUST;
			}
			int minRoomNumCts = Integer.parseInt((liRes.get(0)[17] == null ? "999" : liRes.get(0)[17]).toString());
			OrderUtil.splitOrderForChannel(order, minRoomNumCts);
			// 填充入住人
			// 添加order中的fellow的gender add by diandian.hou
			addGenderToOrderFellow(order.getFellowList());

			OrderUtil.fillCtsFellows(order);
			// 锁定配额
			int resultHK = hotelManageWeb.checkQuotaForWebHK(order);
			// 锁定配额失败
			if (0 != resultHK) {
				orderService.saveOrUpdate(order);
				super.setErrorCode("HC12");
				return WarningContant.ROOM_STATE_FULL;
			}
			// 填充中科订单入住人
			resultHK = hotelManageWeb.saleAddCustInfo(order);
			if (0 != resultHK) {
				orderService.saveOrUpdate(order);
				super.setErrorCode("HC13");
				return WarningContant.ROOM_STATE_FULL;
			}
			int onlinePaytype = hotelOrderFromBean.getOnlinePaytype();
			order.setPrepayType(onlinePaytype);

			// 支付相关的类 add by shengwei.zuo 2009-11-27
			OrPayment payment = new OrPayment();
			payment.setPayType(onlinePaytype);
			payment.setMoney(hotelOrderFromBean.getActuralAmount());
			Date now = new Date();
			if (null != member)
				payment.setConfirmer(member.getName());
			payment.setConfirmTime(now);
			payment.setCreator("HWEB");
			payment.setOperator("HWEB");
			payment.setCreateTime(now);
			payment.setOperateTime(now);
			payment.setOrder(order);
			order.getPaymentList().add(payment);
			OrHandleLog handleLog = new OrHandleLog();
			handleLog.setModifiedTime(new Date());
			String payTypeStr = PrepayType.payStrMap.get(onlinePaytype);
			handleLog.setContent("客人选择在线支付方式：" + payTypeStr);
			handleLog.setModifierName("网站");
			handleLog.setOrder(order);
			order.getLogList().add(handleLog);

			// 中旅酒店返现
			if (hotelOrderFromBean.getReturnAmount() > 0) {
				List<OrOrderItem> items = order.getOrderItems();
				double totalCashReturnAmount = 0.0;
				String payMethod = order.isPayToPrepay() ? "pay" : order.getPayMethod();
				for (OrOrderItem item : items) {

					// 设置俑金---------------add by longkangfu-------2012-7-3----
					item.setCommission(item.getSalePrice() - item.getBasePrice());

					// 计算限量返现
					int cashReturnAmount = limitFavourableManage.calculateCashLimitReturnAmount(order.getHotelId(), order.getChildRoomTypeId(),
							item.getNight(), order.getPaymentCurrency(), new BigDecimal(item.getSalePrice()), item.getCommission());

					// 如果没有限量返现，再计算普通返现，如果有，则不计算普通返现
					if (cashReturnAmount == -1) {
						cashReturnAmount = returnService.calculateCashReturnAmount(order.getChildRoomTypeId(), item.getNight(),
								"pay".equals(payMethod) ? 1 : 2, order.getPaymentCurrency(), 1, new BigDecimal(item.getSalePrice()));
					}
					Double cashReturnAmount1 = channelCashBackService.getlastCashBackAmount(getCashbackratevalue(), (double)cashReturnAmount);
					log.info("cashReturnAmount1=========="+cashReturnAmount1);
					item.setCashReturnAmount(cashReturnAmount1);
					totalCashReturnAmount += cashReturnAmount1;
				}
				order.setCashBackTotal(totalCashReturnAmount);
				// 记录操作日志
			}
		}

		orderService.saveOrUpdate(order);

		// 如果使用了积分或代金券累加是全额支付 add by diandian.hou 2011-11-23
		if ((hotelOrderFromBean.isUseUlmPoint() || hotelOrderFromBean.isUsedCoupon()) && 0.001D > hotelOrderFromBean.getActuralAmount()) {
			order.setOrderState(OrderState.HAS_PAID);// 修改为已支付
			Map params = super.getParams();
			boolean flag_errorCoupon = false;
			boolean flag_errorPoint = false;
			if (hotelOrderFromBean.isUsedCoupon()) { // 代金券支付
				try {
					hotelOrderCompleteService.deductUsedCoupon(params, order, member); // 锁定代价券
					voucherInterfaceService.confirmVoucherState(order, roleUser, member);// 确认并扣除代金券
				} catch (Exception e) {
					flag_errorCoupon = true;
					log.error("HotelCompleteAction调用会员代金券错误：", e);
				}
			}
			if (hotelOrderFromBean.isUseUlmPoint()) { // 积分支付
				try {
					double ulmPointValue = Double.parseDouble(hotelOrderFromBean.getUlmPoint()) / 100;
					boolean isDeducted = hotelOrderCompleteService.deductUlmPoint(order, ulmPointValue, hotelOrderFromBean.isUseUlmPoint());
					if (!isDeducted) {
						order.setOrderState(OrderState.SUBMIT_TO_MID);
						OrderUtil.updateStayInMid(order);
						orderService.updateOrder(order);
						super.setErrorCode("HC14");
						return WarningContant.CUT_POINT_ERROR;
					}
				} catch (MemberException e) {
					// 记录操作日志
					OrHandleLog handleLog = new OrHandleLog();
					handleLog.setModifiedTime(new Date());
					handleLog.setContent("客人选择积分支付，积分支付失败！");
					handleLog.setModifierName("网站");
					handleLog.setOrder(order);
					order.getLogList().add(handleLog);
					log.error("HotelCompleteAction.fillPrepayOrderInfo() deduct member point is error", e);
					// return WarningContant.CUT_POINT_ERROR;
				} catch (Exception e) {
					flag_errorPoint = true;
					log.error("ErrCord:75211 HotelCompleteAction.fillPrepayOrderInfo() deduct member point is error: ", e);
					order.setOrderState(OrderState.SUBMIT_TO_MID);
					OrderUtil.updateStayInMid(order);
					orderService.updateOrder(order);
					super.setErrorCode("HC15");
					return WarningContant.CUT_POINT_ERROR;
				}
			}

			log.info("==========HotelCompleteAction.fillPrepayOrderInfo() 中旅积分或代金券累加是全额支付 code:5652 orderCD:" + orderCD + "=============");
			boolean flag = hotelManageWeb.saleCommitFowWebHK(order);
			if (flag) {
				orderService.updateOrder(order, order.getPrepayType(), true, true);
				// 进行即时确认给客人，盗梦计划测试过程中发现 add by jiajie.chen 2011-06-15
				orderImmedConfirmService.sendImmedConfirmToCus(order, false, member);
			} else {
				orderService.updateOrder(order, order.getPrepayType(), true, false);
			}
			if (flag_errorCoupon || flag_errorPoint) {
				order.setOrderState(OrderState.SUBMIT_TO_MID);
				OrderUtil.updateStayInMid(order);
				orderService.updateOrder(order);
			}
			return SUCCESS;
		}

		return null;
	}

	// 给中旅入住人添加性别
	private void addGenderToOrderFellow(List<OrFellowInfo> fellowList) {
		if (fellowList == null) {
			return;
		}
		for (int i = 0; i < fellowList.size(); i++) {
			OrFellowInfo orFellowInfo = fellowList.get(i);
			orFellowInfo.setFellowGender("M");
		}
	}

	// 记录积分和代金券
	private void setPointAndCoupon() {
		if (order == null)
			return;
		// 封装积分明细 add by diandian.hou 2011-11-22
		if (null != hotelOrderFromBean.getUlmPoint() && Long.parseLong(hotelOrderFromBean.getUlmPoint()) > 0L) {
			Long pointValue = Long.parseLong(hotelOrderFromBean.getUlmPoint());
			Long[] pointValues = new Long[] { pointValue };
			List<OrPointRecords> pointRecordsList = getPointRecordsList(pointValues);
			for (int i = 0; i < pointRecordsList.size(); i++) {
				OrPointRecords orPointRecords = pointRecordsList.get(i);
				orPointRecords.setOrder(order);
				order.addPointRecordsList(orPointRecords);
			}
			// order.setPointRecordsList(pointRecordsList);该方法不能用的 hibernate的问题
			// add by diandian.hou
		}
		// 代金券封装
		if (hotelOrderFromBean.isUsedCoupon()) {
			List<OrCouponRecords> couponRecords = getOrCouponRecordsList();
			if (null != couponRecords && !couponRecords.isEmpty()) {
				for (Iterator it = couponRecords.iterator(); it.hasNext();) {
					OrCouponRecords orCouponRecord = (OrCouponRecords) it.next();
					orCouponRecord.setOrder(order);
					order.addCouponRecords(orCouponRecord);
				}
			}
		}
	}

	private  void updateOrderRecord(OrOrder order){
		try {
			HttpSession httpSession = request.getSession(true);
			OrderRecord orderRecord = (OrderRecord) httpSession.getAttribute("orderRecord");
			if (orderRecord != null && order != null) {
				orderRecord.setOrorderCd(order.getOrderCD());
				bookOrderRecordService.updateOrderRecord(orderRecord);
				boolean isCts = PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod()) && ChannelType.CHANNEL_CTS == hotelOrderFromBean.getRoomChannel();
				if (!isCts) {
					httpSession.removeAttribute("orderRecord");
					CookieUtils.setCookie(request, super.getHttpResponse(), "actionId", null, 0, null, null);
				}
			}
		} catch (Exception e) {
			log.error(" update order record has a wrong", e);
		}
	}
	
	// 记录订单
	private void saveOrderRecord() {
		try {
			bookOrderRecordService.saveOrderRecord(request, super.getHttpResponse(), hotelOrderFromBean, member, order, 7);
		} catch (Exception e) {
			log.error(" save order record has a wrong", e);
		}
	}
	

	// 担保
	private void setOrderAssure(OrOrder order, String cardID) {
		if (order.getReservation() != null) {
			int roomQuantity = order.getRoomQuantity();
			double suretyPrice = order.getReservation().getReservSuretyPrice();
			order.setSuretyPrice(suretyPrice * roomQuantity);
		}
		order.setCreditCardIdsSelect(cardID);
	}


	/**
	 * 记录游比比的标记信息
	 */
	private void saveYouBiBi(){
		//判断是否是从游比比过来的订单
		String projectcodeFromCookie=CookieUtils.getCookieValue(request, "projectcode");
		if(StringUtil.isValidStr(trace_code) && BaseConstant.YOU_BI_BI_CODE.equals(projectcodeFromCookie)){
			OrOrderExtInfo orderExtInfo=new OrOrderExtInfo();
			orderExtInfo.setContext(trace_code);
			//这里的06是用来专门记录游比比的渠道号得
			orderExtInfo.setType("06");
			orderExtInfo.setOrder(order);
			order.getOrOrderExtInfoList().add(orderExtInfo);
			
		}
	}
	
	/**
	 * 校验有没有信用卡
	 * @param hotelOrderFromBean
	 * @return
	 */
	private boolean  checkHasCardId(){
		boolean isHasCardId=true;
		
		if(order!=null){
		boolean isNeedAssur=(PayMethod.PAY.equals(order.getPayMethod()) && order.isCreditAssured());
		boolean isPrepay=PayMethod.PRE_PAY.equals(order.getPayMethod()) && OrderPayType.CREDIT_PAY == hotelOrderFromBean.getOrderPayType();
		if(isNeedAssur || isPrepay){
			if(cardID==null || "0".equals(cardID)){
				isHasCardId=false;
			}
			
		}
		}
		return isHasCardId;
	}
	
	
	public String getOrderCD() {
		return orderCD;
	}

	public void setOrderCD(String orderCD) {
		this.orderCD = orderCD;
	}

	public String getCardID() {
		return cardID;
	}

	public void setCardID(String cardID) {
		this.cardID = cardID;
	}

	public void setReturnService(IHotelFavourableReturnService returnService) {
		this.returnService = returnService;
	}

	public void setHotelBookDao(HotelBookDaoImpl hotelBookDao) {
		this.hotelBookDao = hotelBookDao;
	}

	public void setHotelInfoService(HotelInfoService hotelInfoService) {
		this.hotelInfoService = hotelInfoService;
	}

	public HotelBasicInfo getHotelBasicInfo() {
		return hotelBasicInfo;
	}

	public void setHotelBasicInfo(HotelBasicInfo hotelBasicInfo) {
		this.hotelBasicInfo = hotelBasicInfo;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public void setLimitFavourableManage(HtlLimitFavourableManage limitFavourableManage) {
		this.limitFavourableManage = limitFavourableManage;
	}

	public void setBookingDataCheckService(BookingDataCheckService bookingDataCheckService) {
		this.bookingDataCheckService = bookingDataCheckService;
	}

	public CheckElongAssureService getCheckElongAssureService() {
		return checkElongAssureService;
	}

	public void setCheckElongAssureService(
			CheckElongAssureService checkElongAssureService) {
		this.checkElongAssureService = checkElongAssureService;
	}

	public void setBookOrderRecordService(BookOrderRecordService bookOrderRecordService) {
		this.bookOrderRecordService = bookOrderRecordService;
	}

	public void setHotelSupplyDelegate(HotelSupplyDelegate hotelSupplyDelegate) {
		this.hotelSupplyDelegate = hotelSupplyDelegate;
	}
	public String getIsRegisterMember() {
		return isRegisterMember;
	}
	public void setIsRegisterMember(String isRegisterMember) {
		this.isRegisterMember = isRegisterMember;
	}

}
