package com.mangocity.hotel.dreamweb.ordercheck.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.OrGuaranteeItem;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.SystemDataService;

import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.dreamweb.orderrecord.service.BookOrderRecordService;
import com.mangocity.hotel.dreamweb.priceUtil.HotelRereshPrice;
import com.mangocity.hotel.dreamweb.search.service.HotelInfoService;
import com.mangocity.hotel.ext.member.dto.MbrInfoDTO;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.dto.PointDTO;
import com.mangocity.hotel.ext.member.util.MemberUtil;
import com.mangocity.hotel.order.constant.OrderExtInfoType;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderExtInfo;
import com.mangocity.hotel.order.service.IOrderEditService;
import com.mangocity.hotel.orderrecord.model.OrderRecord;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.util.PriceUtil;
import com.mangocity.hotel.search.vo.SaleItemVO;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.lang.StringUtils;
import com.mangocity.model.mbr.Mbr;
import com.mangocity.model.mbrinterface.MbrInfo;
import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.model.person.PersonMainInfo;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.proxy.vo.MembershipVO;
import com.mangocity.services.mbrship.MbrshipServiceException;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.persistence.ElongAssureResult;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.util.action.OrderCompleteWebAction;

//财务的
import com.mangocity.framework.creditcard.SidGenerator;

public class HotelOrderCheckActionNew extends OrderCompleteWebAction {

	/**
	 * 
	 */
	//private static final long serialVersionUID = -2640519693070060289L;
	private List<SaleItemVO> saleItemVOList; // 价格详情展示
	private Map params; // 全局变量
	private String orderCD;
	private String forward;
	private boolean showCreditCard;
	private HotelBasicInfo hotelBasicInfo;
	private String bookhintCancelAndModifyStr;

	private String assureCreditCard;//担保支持的信用卡
	private String prePayCreditCard;//预付支持的信用卡
	// 注入的service
	//private OrderRecordService orderRecordService;
	private IOrderEditService orderEditService;
	private IHotelService hotelService;
	private HotelInfoService hotelInfoService;
	private HotelRereshPrice hotelRereshPrice;

	private SystemDataService systemDataService;
	private BookOrderRecordService bookOrderRecordService;

	private String memberCd;
	private String fagChange;

	/**
	 * 用于财务的，判断交易的类型，取值：pay（支付）、assure（担保），不填默认为pay
	 */
	private String payMethodStrForShow = "pay";

	private static final String ERROR_MESSAGE = "预订失败!该段时间酒店价格发生变化，请致电芒果网客服电话40066-40066或者重新选择别的酒店！";

	private static final MyLog log = MyLog.getLogger(HotelOrderCheckActionNew.class);
	public String execute() {
		
			cancelTheOldOrder();
			params = super.getParams();
			setParamsSqlReplace(params);
			
			log.info("订单提交params===="+params);

			hotelOrderFromBean = new HotelOrderFromBean();
			MyBeanUtil.copyProperties(hotelOrderFromBean, params);
			initHotelOrderFromBean();
			if ("1".equals(fagChange)) {
				hotelOrderFromBean.setNeedAssure(true);
			}
			
			
			addLogInfo(hotelOrderFromBean,"hotelOrderCheckActionNew");
			
			rate = CurrencyBean.getRateToRMB(hotelOrderFromBean.getCurrency());
		
			try{
			member = super.getMemberInfoForWeb(true);
			}catch(Exception e){
				log.error("获取会员信息发生异常", e);
				
			}
			if (null != member) { 	// 封装会员的Point类
				memberCd = member.getMembercd();
				PointDTO pw = member.getPoint();
				hotelOrderFromBean.setAbleUlmPoint(null != pw ? pw.getBalanceValue() : "0");
			}
			if(hotelOrderFromBean.getReturnAmount()>=0.0){

			     try {
					dealCashReturn();
				} catch (MbrshipServiceException e) {
					// TODO Auto-generated catch block
					log.error("非面付担保酒店处理会员返现时创建会籍失败",e);
				} 	//处理会员返现
			}
		  //保存预订流程，add by ting.li
			log.info("订单提交hotelOrderFromBean.getReturnAmount()==="+hotelOrderFromBean.getReturnAmount());
			saveOrderRecord();		
		// 面付担保
		if (PayMethod.PAY.equals(hotelOrderFromBean.getPayMethod()) && hotelOrderFromBean.isNeedAssure() && !hotelOrderFromBean.isPayToPrepay()) {
			try {
				assureCreditCard = getCreditCard("HOTELII_ASSURE_CREDITCARD");
				
				if (hotelCheckOrderService.isRoomFull(params)) {	// 是否满房
					super.setErrorCode("HH01");
					log.error("满房，无法预订酒店。");
					return super.forwardError("很抱歉，造成您的不便。该房型已经满房，无法进行预订。您可以考虑预订其它房型，或者致电芒果网客服电话40066-40066。");
				}
				// 刷价
				// flushOrderPrice(hotelOrderFromBean,falase);//刷价要不要待商榷
				
				if (checkTheOrderPrice(hotelOrderFromBean)) {	// 校验价格,防止使用火狐进行修改价格，价格发生了变化，进入错误提示。
					super.setErrorCode("HH02");
					return super.forwardError(ERROR_MESSAGE);
				}
				//多床型elong酒店订单添加特殊要求
				if(hotelOrderFromBean.getRoomChannel()==9 && hotelOrderFromBean.getBedTypeStr()!=null && hotelOrderFromBean.getBedTypeStr().indexOf(",")>-1){
					int bedtype = hotelOrderFromBean.getBedType();
		        	String bedName= bedtype==1?"大床":(bedtype==2?"双床":(bedtype==3?"单床":""));
					hotelOrderFromBean.setSpecialRequest((hotelOrderFromBean.getSpecialRequest()==null?"":hotelOrderFromBean.getSpecialRequest())+"务必安排"+bedName);
				}
				super.combineOrderInfo();		// 记录订单
				
				orderCD = super.processPaymentMethod(order);	// 生成订单
				//艺龙担保提示和修改信息
				  if(hotelOrderFromBean.getRoomChannel() == ChannelType.CHANNEL_ELONG){
			      		ElongAssureResult assureResult = checkElongAssureService.checkIsAssure(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getChildRoomTypeId()
			      				, hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(), Integer.parseInt(hotelOrderFromBean.getRoomQuantity()), hotelOrderFromBean.getArrivalTime());
			      		
			      		if(assureResult!=null && assureResult.getAssureType()>0){
			      			if(assureResult.getAssureMoneyType()==1){
			       				OrGuaranteeItem item = new OrGuaranteeItem();
			       				item.setNight(order.getCheckinDate());
			       				item.setAssureCondiction(getGuaranteeAssureCondition(assureResult));
			       				item.setAssureType("2");
			       				item.setAssureLetter("否");
			       				item.setReserv(order.getReservation());
			       				order.getReservation().getGuarantees().add(item);
			       			}else if(assureResult.getAssureMoneyType()==2){
			       				List<Date> datelist = DateUtil.getDates(order.getCheckinDate(), DateUtil.getPreviousDate(order.getCheckoutDate()));
			       				for(Date date:datelist){
			       					OrGuaranteeItem item = new OrGuaranteeItem();
			       					item.setNight(date);
			           				item.setAssureCondiction(getGuaranteeAssureCondition(assureResult));
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
			      	  }
				setOrderAssure(order);
				order.setIsCreditAssured(true);
				order.setOrderState(OrderState.NOT_SUBMIT);
				orderService.updateOrder(order);
				
				forward = "orderCheck";
			} catch (Exception e) {
				log.error("处理面付担保流程发生异常 orderCd:"+orderCD, e);
				super.setErrorCode("HH03");
				return super.forwardError(ERROR_MESSAGE);
			}
		}
		// 预付
		else if (PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod()) || hotelOrderFromBean.hasPayToPrepay()) {
			try {
				
				if (hotelCheckOrderService.isRoomFull(params)) {	 // 是否满房
					log.error("满房，无法预订酒店。");
					super.setErrorCode("HH04");
					return super.forwardError("很抱歉，造成您的不便。该房型已经满房，无法进行预订。您可以考虑预订其它房型，或者致电芒果网客服电话40066-40066。");
				}
				
				if (checkTheOrderPrice(hotelOrderFromBean)) { 	// 校验价格
					super.setErrorCode("HH05");
					return super.forwardError(ERROR_MESSAGE);
				}
				
				super.combineOrderInfo();	// 记录订单
				
				if (hotelOrderFromBean.getRoomChannel() != ChannelType.CHANNEL_CTS) {	// 预付方式需要查询预付条款，取预付条款售价/底价支付酒店
					orderEditService.getReservationInfo(order, false);
				}
			
				order.setOrderState(OrderState.NOT_SUBMIT);
				order.setSumRmb(Math.ceil(order.getSumRmb()));
				order.setSum(Math.ceil(order.getSum()));
				orderCD = super.processPaymentMethod(order);

				prePayCreditCard = getCreditCard("HOTELII_PREPAY_CREDITCARD");

				forward = "orderCheck";
			} catch (Exception e) {
				log.error("处理预付或面转预发生异常 orderCd:"+orderCD, e);
				super.setErrorCode("HH06");
				return super.forwardError(ERROR_MESSAGE);
			}
		}
		
		
		 updateOrderRecord(order);// 更新预订流程信息，设置订单编号
		 
		try {
			if (order != null) {
				bookhintCancelAndModifyStr = (String) params.get("cancelModifyItem");// 获取预订取消条款

				if (member != null) { // 更新会员联系人或入住人
					memberInterfaceService.updateMemberFellowAndLinkman(member, order, true, true);
				}
				

				priceShow();// 界面展示用的

				Cookie[] cookies = request.getCookies();
				if (cookies.length > 0 && null != order.getOrderCD()) {
					htlProjectCodeManage.saveHtlProjectCode(request.getCookies(), order.getOrderCD()); // 电商的相关记录
				}

				//
				showCreditCard = isNotOrCreditPay(hotelOrderFromBean);

				gainOrderCreditSid(); // 设置信用卡sid

				HtlHotel htlHotel = hotelService.findHotel(hotelOrderFromBean.getHotelId()); // 给代金券用
				request.setAttribute("htlHotel", htlHotel);

				hotelBasicInfo = hotelInfoService.queryHotelInfoByHotelId(String.valueOf(hotelOrderFromBean.getHotelId()));
			}
			else{
				log.error("订单为空，订单没有正常生成。");
				super.setErrorCode("HH07");
				return super.forwardError(ERROR_MESSAGE);
								
			}
		} catch (Exception e) {
			super.setErrorCode("HH08");
			log.error("生成订单后，处理相关后期信息发生异常", e);
		}

		if ("orderComplete".equals(forward)) {
			super.cleanValueToSession("oldorderCd");
		}

		else {
			super.putValueToSession("oldorderCd", orderCD);
		}
		log.info("order.getConfirmTotal() ==="+order.getConfirmTotal());
		log.info("订单提交hotelOrderFromBean.getReturnAmount() last ==="+hotelOrderFromBean.getReturnAmount());
		return forward;

	}

	//担保
	private void setOrderAssure(OrOrder order) {
		if (order.getReservation() != null) {
			int roomQuantity = order.getRoomQuantity();
			double suretyPrice = order.getReservation().getReservSuretyPrice();
			order.setSuretyPrice(suretyPrice * roomQuantity);
		}
	}

	/**
	 * 取消客人返回上一步而造成多余的订单
	 */
	private void cancelTheOldOrder() {
		String oldOrderCd = (String) super.getObjectFromSession("oldorderCd");

		if (!StringUtils.isEmpty(oldOrderCd)) {
			OrOrder oldOrder = orderService.getOrOrderByOrderCd(oldOrderCd);
			if (oldOrder != null && oldOrder.getOrderState() < OrderState.SUBMIT_TO_MID ) {
				UserWrapper roleUser = new UserWrapper();
				roleUser.setName("System");
				roleUser.setLoginName("System");
				oldOrder.setMemberId(MemberInterfaceService.COMMONMEMBERID);
				oldOrder.setMemberCd(MemberInterfaceService.COMMONMEMBERCD);
				oldOrder.setCancelReason(1);//1--重下新单-客人原因
				oldOrder.setGuestCancelMessage("5");//5，客人错误预订
				
				orderService.cancelOrder(oldOrder, 1, "网站客人返回填写页", "5", roleUser);	//撤单的原因：1--重下新单-客人原因，5，客人错误预订
			}
		}
	}

	private void gainOrderCreditSid() {

		// 订支付方式为信用卡时，获取支付凭证
		log.info("hotelOrderFromBean.getOrderPayType()=" + hotelOrderFromBean.getOrderPayType());
		log.info("hotelOrderFromBean.isNeedAssure()=" + hotelOrderFromBean.isNeedAssure());

		if (isNotOrCreditPay(hotelOrderFromBean)) {
			if (hotelOrderFromBean.isNeedAssure()) {
				payMethodStrForShow = "assure";
			} else {
				payMethodStrForShow = "pay";
			}
			String sid = SidGenerator.generate("H2");
			log.info("sid=" + sid);
			request.setAttribute("sid", sid);
		}
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

	private  void updateOrderRecord(OrOrder order){
		try{
		HttpSession httpSession = request.getSession(true);
		OrderRecord orderRecord= (OrderRecord) httpSession.getAttribute("orderRecord");
		if(orderRecord!=null && order !=null){
			orderRecord.setOrorderCd(order.getOrderCD());
			bookOrderRecordService.updateOrderRecord(orderRecord);
		}
		}catch(Exception e){
			log.error(" update order record has a wrong", e);
		}
	}

	/**
	 * create by ting.li
	 * 
	 * @return
	 */
	private void saveOrderRecord() {
		try{
		bookOrderRecordService.saveOrderRecord(request, super.getHttpResponse(), hotelOrderFromBean, member, order, 6);
		}catch(Exception e){
			log.error(" save order record has a wrong", e);
		}
	}

	// 界面展示用
	private void priceShow() {
		String payMethodTemp = hotelOrderFromBean.isPayToPrepay() ? "pre_pay" : hotelOrderFromBean.getPayMethod();
		hotelOrderFromBean.setCurrencyStr(PriceUtil.currencyMap.get(hotelOrderFromBean.getCurrency()));
		Date inDate = hotelOrderFromBean.getCheckinDate();
		Date outDate = hotelOrderFromBean.getCheckoutDate();

		if (inDate != null) {
			weekOfInDate = DateUtil.WeekDay.getValueByKey(DateUtil.getWeekOfDate(inDate));
		}

		if (outDate != null) {
			weekOfOutDate = DateUtil.WeekDay.getValueByKey(DateUtil.getWeekOfDate(outDate));
		}

		try {
			if (order != null) {
				saleItemVOList = PriceUtil.getSaleItemVOList(order.getPriceList(), payMethodTemp, hotelOrderFromBean.getCurrency());
			}
		} catch (Exception e) {
			log.error("query every price error", e);
		}
	}

	//判断是否需要展示信息卡展示和用于生成财务的sid
	private boolean isNotOrCreditPay(HotelOrderFromBean hotelOrderFromBean) {
		if (hotelOrderFromBean == null)
			return false;
		boolean isNeedAssure = hotelOrderFromBean.isNeedAssure();//担保判断
		boolean isPrepay = PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod()) || hotelOrderFromBean.isPayToPrepay();
		boolean isCts = hotelOrderFromBean.getRoomChannel() == 8;
		boolean isCreditPrepay = isPrepay && !isCts;//信用卡语法判断
		if(isNeedAssure || isCreditPrepay){
			return true;
		}
		return false;
		
	}
	
	/**
	 * 获取担保和预付的支持的信息用卡
	 * @param orParamName，参数名字
	 * @return
	 */
	private String getCreditCard(String orParamName) {
		OrParam orParam = null;
		orParam = systemDataService.getSysParamByName(orParamName);
		if (orParam != null) {
			return orParam.getValue();
		} else {
			return null;
		}
	}

	//给hotelOrderFromBean赋值，防止转换异常
	private void initHotelOrderFromBean(){
		hotelOrderFromBean.setAbleUlmPoint("0");
		hotelOrderFromBean.setCityName(InitServlet.cityObj.get(hotelOrderFromBean.getCityCode()));
	}
	 
	
	public List<SaleItemVO> getSaleItemVOList() {
		return saleItemVOList;
	}

	public void setSaleItemVOList(List<SaleItemVO> saleItemVOList) {
		this.saleItemVOList = saleItemVOList;
	}

	public void setHdlService(IHDLService hdlService) {
		this.hdlService = hdlService;
	}

	public String getOrderCD() {
		return orderCD;
	}

	public void setOrderCD(String orderCD) {
		this.orderCD = orderCD;
	}

	public String getBookhintCancelAndModifyStr() {
		return bookhintCancelAndModifyStr;
	}

	public void setBookhintCancelAndModifyStr(String bookhintCancelAndModifyStr) {
		this.bookhintCancelAndModifyStr = bookhintCancelAndModifyStr;
	}

	public void setOrderEditService(IOrderEditService orderEditService) {
		this.orderEditService = orderEditService;
	}

	public boolean isShowCreditCard() {
		return showCreditCard;
	}

	public void setShowCreditCard(boolean showCreditCard) {
		this.showCreditCard = showCreditCard;
	}

	
	public String getAssureCreditCard() {
		return assureCreditCard;
	}


	public void setAssureCreditCard(String assureCreditCard) {
		this.assureCreditCard = assureCreditCard;
	}


	public String getPrePayCreditCard() {
		return prePayCreditCard;
	}


	public void setPrePayCreditCard(String prePayCreditCard) {
		this.prePayCreditCard = prePayCreditCard;
	}


	public String getMemberCd() {
		return memberCd;
	}

	public void setMemberCd(String memberCd) {
		this.memberCd = memberCd;
	}

	
	public String getPayMethodStrForShow() {
		return payMethodStrForShow;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

	public HotelBasicInfo getHotelBasicInfo() {
		return hotelBasicInfo;
	}

	public void setHotelBasicInfo(HotelBasicInfo hotelBasicInfo) {
		this.hotelBasicInfo = hotelBasicInfo;
	}

	public void setHotelInfoService(HotelInfoService hotelInfoService) {
		this.hotelInfoService = hotelInfoService;
	}

	public void setHotelRereshPrice(HotelRereshPrice hotelRereshPrice) {
		this.hotelRereshPrice = hotelRereshPrice;
	}


	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}


	public String getFagChange() {
		return fagChange;
	}


	public void setFagChange(String fagChange) {
		this.fagChange = fagChange;
	}

	public void setBookOrderRecordService(BookOrderRecordService bookOrderRecordService) {
		this.bookOrderRecordService = bookOrderRecordService;
	}
}
