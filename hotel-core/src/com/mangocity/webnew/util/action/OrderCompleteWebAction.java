package com.mangocity.webnew.util.action;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hdl.constant.ChinaOnlineConstant;
import com.mangocity.hdl.hotel.dto.AddExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.AddExRoomOrderResponse;
import com.mangocity.hdl.hotel.dto.CancelExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.CancelExRoomOrderResponse;
import com.mangocity.hdl.hotel.dto.MGExOrder;
import com.mangocity.hdl.hotel.dto.MGExOrderItem;
import com.mangocity.hdl.hotel.dto.MGExResult;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.HtlProjectCodeManage;
import com.mangocity.hotel.base.persistence.HtlElAssure;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlSupplierInfo;
import com.mangocity.hotel.base.persistence.OrAssureItemEvery;
import com.mangocity.hotel.base.persistence.OrGuaranteeItem;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.ChannelCashBackManagerService;
import com.mangocity.hotel.base.service.SupplierInfoService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.ext.member.dto.MbrInfoDTO;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderExtInfo;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.HotelElOrderService;
import com.mangocity.hotel.orderbuildservice.OrderBuild.Key_orderBaseMap;
import com.mangocity.hotel.orderbuildservice.OrderBuild.Key_orderClauseMap;
import com.mangocity.hotel.orderbuildservice.OrderBuild.Key_orderEBMap;
import com.mangocity.hotel.orderbuildservice.OrderBuild.Key_orderMemberMap;
import com.mangocity.hotel.orderbuildservice.impl.WebOrderBuild;
import com.mangocity.hotel.orderconstruct.OrderConstruct;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.model.mbrsys.MbrTransactionLogVO;
import com.mangocity.model.person.PersonMainInfo;
import com.mangocity.services.mbrship.MbrshipServiceException;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.hotel.constant.QuotaType;
import com.mangocity.util.web.CookieUtils;
import com.mangocity.webnew.constant.FITAliasConstant;
import com.mangocity.webnew.persistence.ElongAssureResult;
import com.mangocity.webnew.service.CheckElongAssureService;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.HotelBookingService;
import com.mangocity.webnew.service.IHotelOrderCompleteService;
import com.mangocity.webnew.service.OrderImmedConfirmService;

/**
 * @aim this action is afford the methods for orderComplete 
 * @author houdiandian
 * @see you can add and update some methods for orderComplete in the action ,but you must remember this action should be relation orderComplete
 */

public class OrderCompleteWebAction extends GenericWebAction {
	
	//界面用到的参数
	protected int roomPrice;
	protected int totalRoomNight;
	protected long mbrID;
	
	//注入的service
	protected OrderImmedConfirmService orderImmedConfirmService;
	protected IHDLService hdlService;
	protected IHotelOrderCompleteService hotelOrderCompleteService;

	protected SupplierInfoService supplierInfoService;
	protected HtlProjectCodeManage htlProjectCodeManage;
	protected String weekOfInDate;
	protected String weekOfOutDate;
	protected HotelBookingService   hotelBookingService;
	protected CheckElongAssureService checkElongAssureService;
    // 判断是否自动注册会员并登录
	protected boolean autoFlag=false;
	// 记录游比比的平台号
	protected String trace_code;
	
    /**
     * 返利查询service
     */
	public ChannelCashBackManagerService channelCashBackService;
	//渠道返现控制比例 add by hushunqiang
	private double cashbackratevalue;
	
	//组装订单
    public void  combineOrderInfo(){
//    	super.fillOrderBaseInfo();
//        //新网站的标示 
//		order.setCreator("hotelII");
//        order.setCreatorName("hotelII");
//        /* QC2935下订单时要记录供应商，add by xie yanhui 2011-11-02 begin*/
//		if(hotelOrderFromBean.getChildRoomTypeId() > 0){
//			HtlPriceType priceType = hotelManage.findHtlPriceType(order.getChildRoomTypeId());
//        	if(null != priceType && null != priceType.getSupplierID()){
//        		Long supplierID = priceType.getSupplierID();
//        		HtlSupplierInfo htlSupplierInfo =  supplierInfoService.querySupplierInfoByID(supplierID.longValue());
//        		if(null != htlSupplierInfo && 1 == htlSupplierInfo.getActive().intValue()){//供应商存在且有效
//            		String supplierAlias = htlSupplierInfo.getAlias();
//            		order.setSupplierAlias(supplierAlias);
//            		order.setSupplierID(supplierID);
//        		}
//        	}
//		}
//		/* QC2935下订单时要记录供应商，add by xie yanhui 2011-11-02 end*/
//		
//        /**
//         * 处理订单附加信息
//         */
//        orderService.updateExtInfo(order, super.getParams());
//        // 更新会员的常入住人和常联系人
//        if (null != member && member.isMango()) {
//        	boolean  bFellow  = true;
//        	if(!hotelOrderFromBean.getIsSavePerson()){
//        		bFellow = false;
//        	}
//            memberInterfaceService.updateMemberFellowAndLinkman(member, order, bFellow, true);
//        }
    	//TODO  new 
    	Map params = super.getParams();
        reservation = new OrReservation();
        MyBeanUtil.copyProperties(reservation, params);
        if(hotelOrderFromBean.getRoomChannel() != ChannelType.CHANNEL_ELONG){
        	setSomeValueToReservation(reservation);
        }
        
    	OrderConstruct orderConstruct = new OrderConstruct(new WebOrderBuild());
    	String tipContent = (String)params.get("tipInfo");
    	order = new OrOrder();
    
    	order.setTitle((String)params.get("title"));
    	Map<Key_orderBaseMap,Object> orderBaseInfoMap = getOrderBaseInfoMapFromWeb(hotelOrderFromBean,order,tipContent);
    	Map<Key_orderMemberMap,Object> orderMemberInfoMap = getOrderMemberInfoMapFromWeb();
    	Map<Key_orderClauseMap,Object> orderClauseInfoMap = getOrderClauseInfoMap(reservation);
    	Map<Key_orderEBMap,Object> orderEBInfoMap = getOrderEBInfoMap(request.getCookies());
    	orderConstruct.createWebOrder(order, hotelOrderFromBean, orderBaseInfoMap, orderClauseInfoMap, orderMemberInfoMap , null ,orderEBInfoMap);
    	setSomeValueToHotelOrderFromBean(hotelOrderFromBean);//主要是界面的值显示
    
    }
    
    public void initreturncashrate(){
    	projectCode = CookieUtils.getCookieValue(request, "projectcode");
    	//1，渠道返现控制比例 add by hushunqiang
		setCashbackratevalue(channelCashBackService.getChannelCashBacktRate(projectCode));
		log.info("最后的渠道比例控制=======cashbackratevalue="+cashbackratevalue+",projectCode="+projectCode);
    }
	
    //订单保存到中台
    protected void saveOrderToMid(OrOrder order){
    	if(order.getOrderState()!=OrderState.HAS_PAID){
        order.setOrderState(OrderState.SUBMIT_TO_MID);
    	}
        order.setToMidTime(DateUtil.getSystemDate());
        OrderUtil.updateStayInMid(order);
        orderService.updateOrder(order);
    }
    
    protected String getGuaranteeAssureCondition(ElongAssureResult assureResult) {
    	String assureCondiction="";
    	if(assureResult.getAssureType()==1){
			assureCondiction="无条件担保";
		}else if(assureResult.getAssureType()==2){
			assureCondiction="超房数担保("+assureResult.getOverQtyNum()+")";
		}else if(assureResult.getAssureType()==3){
			String assuretime = assureResult.getOverTimeMin()<10 ? "0"+assureResult.getOverTimeMin() : ""+assureResult.getOverTimeMin();
			assuretime = assureResult.getOverTimeHour()+assuretime;
			assureCondiction="超时担保("+assuretime+")";
		}else if(assureResult.getAssureType()==4){
			String assuretime = assureResult.getOverTimeMin()<10 ? "0"+assureResult.getOverTimeMin() : ""+assureResult.getOverTimeMin();
			assuretime = assureResult.getOverTimeHour()+assuretime;
			assureCondiction="超房数("+assureResult.getOverQtyNum()+"),超时("+assuretime+")担保";
		}
    	return assureCondiction;
	}
    
	//发传真
    protected boolean sendOrderInfoToHotel(OrOrder order , MemberDTO member){
    	if(PayMethod.PAY.equals(order.getPayMethod())){
    		order.setShowBasePrice(false);
    	}
    	boolean flag_sendToHotel = false; //是否发送
    	try{
    	if(StringUtil.isValidStr(order.getCity()) && "HKG,MAC".contains(order.getCity())){
    		boolean quotaOk = orderImmedConfirmService.quotaOk(order);
    		if(quotaOk){
    			flag_sendToHotel = true;
    		}
    	}else{
    		flag_sendToHotel = true;
    	}
    	if(flag_sendToHotel){
    		String sendHotelResult = orderImmedConfirmService.sendImmedConfirmToHotel(order, member);
    		if("success".equals(sendHotelResult)){
    			return true;
    		}
    	}
    	}catch(Exception e){
    		log.error("sendOrderInfoToHotel error:",e);
    	}
    	return false;
    }
    
  //扣配额
    protected boolean deductQuotaAndFillOrderItems(OrOrder order , HotelOrderFromBean hotelOrderFromBean){
    	int[] deduct =  new int[2];
    	try{
    		hotelOrderFromBean.setCashbackratevalue(getCashbackratevalue());
    	    deduct  = hotelOrderCompleteService.fillOrderItems(order,hotelOrderFromBean);
    	}catch(Exception e){
    		log.error("hotelCompleteAction deduct quota error:" ,e);
    	}
    	boolean flag_deductQuota = false;
		//成功扣配额后，增加日志
		if(0 == deduct[0]){
			flag_deductQuota = true;
			// 记录操作日志
            OrHandleLog handleLog = new OrHandleLog();
            handleLog.setModifiedTime(new Date());
            if ((hotelOrderFromBean.isUseUlmPoint() || hotelOrderFromBean.isUsedCoupon())
    				&& 0.001D > hotelOrderFromBean.getActuralAmount()){
            	 handleLog.setContent("会员使用积分或代金券累加是全额支付，扣配额成功");
            }
            else{
            handleLog.setContent("扣配额成功");
            }
            handleLog.setModifierName("网站");
            handleLog.setOrder(order);
            order.getLogList().add(handleLog);
          
		}
		return flag_deductQuota;
    }
    
    
    //  保存订单并返回订单CD

    protected String processPaymentMethod(OrOrder order) {
        String orderCD = "";
        Serializable orderId = orderService.saveOrUpdate(order);        
        orderCD = orderService.getOrderCDByID(orderId.toString());
        if(null != orderId && null == order.getID()){
        	order.setID(new Long(orderId.toString()));
        }
        if(!StringUtil.isValidStr(order.getOrderCD())){
        	order.setOrderCD(orderCD);
            //发送传真没有订单号，必须填充订单号
            order.setOrderCDHotel(orderCD);
            
            //标记订单为 醒狮计划促销活动
            orderService.updateExtInfoForWakeUp(order);
            try {
				String hotelIdStr = this.getCookie("hotelIdArray");
				if(null!=hotelIdStr&&!"".equals(hotelIdStr)){
					String hotelId =order.getHotelId().toString();
					if(-1!=hotelIdStr.indexOf(hotelId)){
					 log.info("记录订单为百分点推荐栏来源");
				     orderService.updateExtInfoforBFD(order);
					}
					
				}
			
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
            
            
        }
        return orderCD;
    }
    
    
  //畅联
    @SuppressWarnings("unchecked")
	protected String saveHDLOrderInfo(){
		@SuppressWarnings("unused")
		Map params = super.getParams();
        String errMsg = "";  
        Map channelMap=null;
        // 获取resourceDescr.xml中的资源 add by chenjiajie V2.5 2009-01-23
        try{
       
       channelMap = resourceManager.getDescription("select_cooperator");
        }catch(Exception e){
        	log.error("saveHDLOrderInfo() has a wrong", e);
        }
        MGExResult mgresult = null;
        boolean bSuc = false;
        AddExRoomOrderResponse addExRoomOrderResponse = null;
        if (0 < hotelOrderFromBean.getRoomChannel()) {
            // 如果渠道为非传统渠道
            AddExRoomOrderRequest addExRoomOrderRequest = new AddExRoomOrderRequest();
            addExRoomOrderRequest.setChannelType(hotelOrderFromBean.getRoomChannel());
            addExRoomOrderRequest.setHotelId(order.getHotelId());
            addExRoomOrderRequest.setChainCode(null);
            /*
             * 渠道方 hotelCode
             */
            MGExOrder exRoomOrder = new MGExOrder();
            // 直联酒店ChinaOnline可能需要使用的首日房价 add by chenjiajie V2.5@2009-02-02
           // float colFirstDayPrice = order;
           // exRoomOrder.setFirstDayPrice(colFirstDayPrice);
            exRoomOrder.setCheckindate(DateUtil.toStringByFormat(order.getCheckinDate(),
                "yyyy-MM-dd"));
            exRoomOrder.setCheckoutdate(DateUtil.toStringByFormat(order.getCheckoutDate(),
                "yyyy-MM-dd"));
            exRoomOrder.setRoomquantity(order.getRoomQuantity());
            exRoomOrder.setPaymethod(order.getPayMethod());
            exRoomOrder.setCurrency(order.getPaymentCurrency());
            if (order.getPayMethod().equals(PayMethod.PRE_PAY)) {
                exRoomOrder.setTotalamount((float) 0.0);
            } else {
                exRoomOrder.setTotalamount(Double.valueOf(order.getSum()).floatValue());
            }
            exRoomOrder.setLocaltotalamout(Double.valueOf(order.getSumRmb()).floatValue());

            exRoomOrder.setExchangerate(Double.valueOf(order.getRateId()).floatValue());
            exRoomOrder.setArrivaltime(order.getArrivalTime());
            exRoomOrder.setLatestarrivaltime(order.getLatestArrivalTime());
            exRoomOrder.setNosmoking(null);
            log.info("HotelCompleteAction=============================saveHDLOrderInfo===order.getRemark().getHotelRemark() start");
            if(order.getRemark()!=null){
            	exRoomOrder.setHotelnotes(order.getRemark().getHotelRemark());
            }
            exRoomOrder.setSpecialrequest(order.getSpecialRequest());
            exRoomOrder.setLinkman(order.getLinkMan());
            // 子房型ID
            exRoomOrder.setPricetypecode(order.getChildRoomTypeId().intValue()); // *****
            exRoomOrder.setTitle(order.getTitle());
            exRoomOrder.setMobile(order.getMobile());
            exRoomOrder.setTelephone(order.getTelephone());
            exRoomOrder.setCustomerfax(order.getCustomerFax());
            exRoomOrder.setEmail(order.getEmail());
            exRoomOrder.setArrivaltraffic(order.getArrivalTraffic());
            exRoomOrder.setFlight(order.getFlight());
            exRoomOrder.setFellownames(order.getFellowNames());
            exRoomOrder.setAdultnum(order.getFellowList().size());
            // exRoomOrder.setPaysatus(String.valueOf(order.getPayState()));
            exRoomOrder.setOrderstate(String.valueOf(order.getOrderState()));
            exRoomOrder.setHotelid(order.getHotelId());
            exRoomOrder.setHotelname(order.getHotelName());
            exRoomOrder.setRoomtypeid(order.getRoomTypeId().intValue());
            exRoomOrder.setRoomtypecode(order.getRoomTypeId().intValue());
            exRoomOrder.setRoomtypename(order.getRoomTypeName());
            exRoomOrder.setBedtype(order.getBedType());
            exRoomOrder.setIsguarantee(String.valueOf((order.getIsCreditAssured())));
            exRoomOrder.setGuaranteetype(ChinaOnlineConstant.NO_UARANTEE); 
            // 对德比没用，但COL需要设置，这里设施未无担保类型
            exRoomOrder.setCreditcardtype("credit card type");
            exRoomOrder.setCreditcardname(null);
            exRoomOrder.setCreditcardno(null);
            exRoomOrder.setCreditcardexpires(null);
            /**
             * 传给中间层订单orderid与ordercdhotel
             */
            exRoomOrder.setOrderid(order.getID());
            exRoomOrder.setOrdercd(order.getOrderCD());
            exRoomOrder.setOrdercdhotel(order.getOrderCD());
            if (null == order.getCreateDate()) {
                exRoomOrder.setCreatedate(DateUtil.toStringByFormat(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
            } else {
                exRoomOrder.setCreatedate(DateUtil.toStringByFormat(order.getCreateDate(),
                    "yyyy-MM-dd HH:mm:ss"));
            }
            
            List<OrOrderItem> orderItemList = order.getOrderItems();
            log.info("HotelCompleteAction=================saveHDLOrderInfo===orderItemList start ");
            for (Iterator itr = orderItemList.iterator(); itr.hasNext();) {
            	OrOrderItem orderItem = (OrOrderItem) itr.next();
                MGExOrderItem mgExOrderItem = new MGExOrderItem();
                mgExOrderItem.setNumber(orderItem.getRoomIndex());
                mgExOrderItem.setCheckindate(DateUtil.toStringByFormat(orderItem.getNight(),
                    "yyyy-MM-dd"));
                mgExOrderItem.setCheckoutdate(DateUtil.toStringByFormat(orderItem.getNight(),
                    "yyyy-MM-dd"));
                mgExOrderItem.setGuests(order.getFellowNames());
                if (null != orderItem.getRoomNo()) {
                    mgExOrderItem.setRoomno(Integer.valueOf(orderItem.getRoomNo()));
                }
                if (order.getPayMethod().equals(PayMethod.PRE_PAY)) {
                    exRoomOrder.setTotalamount(exRoomOrder.getTotalamount()
                        + Double.valueOf(orderItem.getBasePrice()).floatValue());
                }
                mgExOrderItem.setSum(Double.valueOf(orderItem.getBasePrice()).floatValue());
                mgExOrderItem.setBaseprice(Double.valueOf(
                    orderItem.getBasePrice()).floatValue());
                mgExOrderItem.setSaleprice(Double.valueOf( 
                    orderItem.getSalePrice()).floatValue());
                mgExOrderItem.setBaserate(Double.valueOf( 
                    orderItem.getBasePrice()).floatValue());
                mgExOrderItem.setTotalcharges(Double.valueOf(
                    orderItem.getServiceFee()).floatValue());
                mgExOrderItem.setSpecialnote(orderItem.getSpecialNote());
                mgExOrderItem.setCreatetime(exRoomOrder.getCreatedate());
                orderItem.setQuotaType(QuotaType.GENERALQUOTA);
                mgExOrderItem.setQuantity(1);
                mgExOrderItem.setSum(Double.valueOf(order.getSum()).floatValue());
                mgExOrderItem.setNoteresult(orderItem.getNoteResult());
                mgExOrderItem.setOrderid(order.getID());
                mgExOrderItem.setOrderstate(String.valueOf(order.getOrderState()));
                mgExOrderItem.setHotelconfirm(null);
                mgExOrderItem.setHotelconfirmid(null);
                exRoomOrder.getExOrderItems().add(mgExOrderItem);
                
                //直联酒店ChinaOnline可能需要使用的首日房价 add by shengwei.zuo  2009-12-23
                if(0==DateUtil.compare(orderItem.getNight(),hotelOrderFromBean.getCheckinDate())){
                	 float colFirstDayPrice = Double.valueOf(orderItem.getSalePrice()).floatValue();
                     exRoomOrder.setFirstDayPrice(colFirstDayPrice);
                }
            }
            log.info("HotelCompleteAction=================saveHDLOrderInfo===orderItemList end ");
            addExRoomOrderRequest.setRoomOrder(exRoomOrder);
            
            if(roleUser==null){
            	roleUser  = new UserWrapper();
            }
            roleUser.setLoginName("网站用户");
        	roleUser.setName("网站用户");
            
            try {
                addExRoomOrderResponse = hdlService.addExRoomOrder(addExRoomOrderRequest);
            } catch (Exception e) {
            	log.error("=================HotelCompleteAction.saveHDLOrderInfo()===hdlService.addExRoomOrder() exception  beging cancel order:",e);
            	String failLog = "接口异常！下直联合作方"+channelMap.get(String.valueOf(hotelOrderFromBean.getRoomChannel()))+ "订单失败！";
            	orderService.handleExceptionOrder(order, 1, failLog, roleUser);
            	return "预订失败!该段时间酒店价格发生变化，请致电芒果网客服电话40066-40066或者重新选择别的酒店！";
            }
           
            if (null != addExRoomOrderResponse) {
                mgresult = addExRoomOrderResponse.getResult();
                // 判断订单是否成功,1为成功,0为不成功
                if (1 == mgresult.getValue()) {
                    bSuc = true;
               
                    order = orderService.getOrder(order.getID());
                    if (StringUtil.isValidStr(mgresult.getMessage())) {
                        order.setOrderCdForChannel(mgresult.getMessage());
                    }
                    StringBuilder orderSuccessLog =  new  StringBuilder();
                    orderSuccessLog.append("<font color='green'>下单到直连合作方"+
                    		channelMap.get(String.valueOf(hotelOrderFromBean.getRoomChannel()))+"成功！</font>");
                    String strLog = "";
                    if ((null == order.getSpecialRequest()
                        || "".equals(order.getSpecialRequest())) 
                        && !order.isCreditAssured()) {
                        order.setQuotaOk(true);
                        order.setSendedHotelFax(true);
                        if(ChannelType.CHANNEL_ELONG == hotelOrderFromBean.getRoomChannel()) {
                        	/*
                        	 * 如果是艺龙的订单
                        	 * 订单子状态:酒店已口头确认,酒店已书面确认,已收回传
                        	 * 都设置为false，页面不勾选
                        	 */
                            order.setHotelConfirm(false);
                            order.setHotelConfirmFax(false);
                            order.setHotelConfirmTel(false);
                            order.setHotelConfirmFaxReturn(false);
                            strLog = "已满足配额,已发送酒店确认";
                        }else {
                        	order.setHotelConfirm(true);
                        	order.setHotelConfirmFax(true);
                        	order.setHotelConfirmTel(true);
                        	order.setHotelConfirmFaxReturn(true);
                        	strLog = "已满足配额,已发送酒店确认,酒店已口头确认,酒店已书面确认,已收回传";
                        }
                    } else {
                        order.setQuotaOk(true);
                        order.setSendedHotelFax(true);
                        order.setHotelConfirmTel(true);
                        strLog = "已满足配额,已发送酒店确认";
                    }
                     // 二次确定，取得二次确定标示，用于是否发即时确定短信 add by diandian.hou 2010-10-26 
					     boolean flagSecondConfirm = addExRoomOrderResponse.getFlagSecondConfirm();
					     // 测试用的，正式用上面的，把下面一句删除
					     //flagSecondConfirm = true;
					     if (flagSecondConfirm) {
						      strLog = orderService.changeOrderForSencondConfirm(order);
					    }
					    orderSuccessLog.append(strLog);
					    
                    OrHandleLog handleLog = new OrHandleLog();
                    handleLog.setModifierName("网站");
                    handleLog.setBeforeState(order.getOrderState());
                    handleLog.setAfterState(order.getOrderState());
                    handleLog.setContent(orderSuccessLog.toString());
                    handleLog.setModifiedTime(new Date());
                    handleLog.setOrder(order);
                    order.getLogList().add(handleLog);
                
                    try{
                    	orderService.saveOrUpdate(order);   
                    }catch(Exception e){
                    	log.error("==============HotelCompleteAction.saveHDLOrderInfo()=======orderService.saveOrUpdate() update orderCdChannel and log ecception: ", e);
                  	    orderService.handleExceptionOrder(order, 3, "订单已下到直联合作方，本地数据库保存出现异常！", roleUser);
                  	    return "预订失败!该段时间酒店价格发生变化，请致电芒果网客服电话40066-40066或者重新选择别的酒店！";
                    }
                    //只有保存订单成功后才会发确认短信给客人,add by shengwei.zuo 2010-12-21
                    try {
                    	order = orderService.getOrder(order.getID());
                    	if (StringUtil.isValidStr(order.getOrderCdForChannel())){
                    		orderImmedConfirmService.sendImmedConfirmToCus(order, flagSecondConfirm,member);
                    	}
						//orderService.saveOrUpdate(order);
					} catch (Exception eSMS) {
						log.error("==============HotelCompleteAction.saveHDLOrderInfo()=========this.sendImmedConfirmToCus() exception : ", eSMS);
					}
                }else{
                	log.info("=======HotelCompleteAction.saveHDLOrderInfo()====== new order failed! reason :  "+ mgresult.getMessage());
                    // 直联订单失败的原因是合作方系统原因,
                	orderService.handleExceptionOrder(order, 2, mgresult.getMessage(), roleUser);
                	return "预订失败!该段时间酒店价格发生变化，请致电芒果网客服电话40066-40066或者重新选择别的酒店！";
                }
            }else{
            	 log.info("============HotelCompleteAction.saveHDLOrderInfo() new order failed resaon: addExRoomOrderResponse is null !");
              	String failLog = "接口异常！<font color='red'>下直联合作方"+channelMap.get(String.valueOf(hotelOrderFromBean.getRoomChannel()))+ "订单失败！</font>";
              	orderService.handleExceptionOrder(order, 1, failLog, roleUser);
              	return "预订失败!该段时间酒店价格发生变化，请致电芒果网客服电话40066-40066或者重新选择别的酒店！";
            }
        }
	return errMsg;
}
   
    
    /**
	 * 校验价格,如果在页面修改了重新设置正确的价格，或者价格发生了变化
	 * @return true表示价格发生了变化，false表示价格没有发生变化
	 */
    protected boolean checkTheOrderPrice(HotelOrderFromBean hotelOrderFromBean){
		int days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
		double priceNum = 0.0;
		// 中旅的刷价
		if (PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod()) && ChannelType.CHANNEL_CTS == hotelOrderFromBean.getRoomChannel()) {
			List<QueryHotelForWebSaleItems> priceLis = new ArrayList<QueryHotelForWebSaleItems>();
			try {
				priceLis = hotelBookingService.refreshHotelReservateExResponse(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(),
						hotelOrderFromBean.getChildRoomTypeId(), hotelOrderFromBean.getRoomChannel(), hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean
								.getCheckoutDate());

			} catch (Exception e) {
				log.error("HotelOrderCheckAction HDL interfaces exception:", e);
				return true;
			}
			if (null != priceLis && days <= priceLis.size()) {
				
				for (int k = 0; k < priceLis.size(); k++) {
					QueryHotelForWebSaleItems it = (QueryHotelForWebSaleItems) priceLis.get(k);
					priceNum += it.getSalePrice();
				}
			}

		}

		// 畅联的刷价
		else if (hotelOrderFromBean.getRoomChannel() != 0 && hotelOrderFromBean.getRoomChannel() != ChannelType.CHANNEL_CTS) {
			String hdlPriceStr = null;
			try {
				hdlPriceStr = hotelBookingService.reReshReservateExResponse(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(), String
						.valueOf(hotelOrderFromBean.getChildRoomTypeId()), hotelOrderFromBean.getRoomChannel(), hotelOrderFromBean.getCheckinDate(),
						hotelOrderFromBean.getCheckoutDate());
			} catch (Exception e) {
				log.error("HotelOrderCheckAction HDL interfaces exception:", e);
				return true;
			}
			if (null != hdlPriceStr && !"".equals(hdlPriceStr) && !hdlPriceStr.equals("hdlRefreshPriceFail")) {
				priceNum = Double.parseDouble(hdlPriceStr);
			} else {
				log.info("HotelOrderCheckAction==========CHINAONLINE==========刷畅联的价格报错");
				return true;
			}
		}

		else {
			// 非中旅和直联的。
			List<QueryHotelForWebSaleItems> queryPriceList = hotelManageWeb.queryPriceDetailForWeb(hotelOrderFromBean.getHotelId(), hotelOrderFromBean
					.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(), hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(),
					hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean.getChildRoomTypeId(), hotelOrderFromBean.getPayMethod());

			int f = 0;
			for (int y = 0; y < queryPriceList.size(); y++) {
				QueryHotelForWebSaleItems queryHotelForWebSaleItems = queryPriceList.get(y);
				f = hotelManageWeb.changePriceForFavourableThree(queryPriceList, queryHotelForWebSaleItems, hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getChildRoomTypeId(), y, f);

			}
			for (int i = 0; i < queryPriceList.size(); i++) {
				priceNum += queryPriceList.get(i).getSalePrice();

			}
		}
		
		
		double fromWebPriceSum = hotelOrderFromBean.getPriceNum();
		priceNum=Math.ceil(priceNum);
		fromWebPriceSum=Math.ceil(fromWebPriceSum);
		if (priceNum != fromWebPriceSum) {
			return true;
		}
		return false;
	} 
    
    
    
   //添加界面展示和电商需要的数据统计
   protected void addEBSomeValue(){
	    rate = CurrencyBean.getRateToRMB(order.getPaymentCurrency());//添加汇率
		weekOfInDate = DateUtil.WeekDay.getValueByKey(DateUtil.getWeekOfDate(hotelOrderFromBean.getCheckinDate())); //
		weekOfOutDate = DateUtil.WeekDay.getValueByKey(DateUtil.getWeekOfDate(hotelOrderFromBean.getCheckoutDate()));
	    //设置days
	    int days = DateUtil.getDay(order.getCheckinDate(),order.getCheckoutDate());
		hotelOrderFromBean.setDays(days);
	    int nights = DateUtil.compare(order.getCheckinDate(), order.getCheckoutDate());
		roomPrice = (int)(order.getSumRmb() / order.getRoomQuantity() / nights);		
		 //根据三字码 填充hotelOrderFromBean中的城市名称
        String cityCode = hotelOrderFromBean.getCityCode();
        if(null!=cityCode){
        	String cityName = InitServlet.cityObj.get(cityCode);
        	if(StringUtil.isValidStr(cityName)) hotelOrderFromBean.setCityName(cityName);
        } 
		//根据or_order计算订单的总间夜量
        totalRoomNight = DateUtil.compare(order.getCheckinDate(), order.getCheckoutDate()) * order.getRoomQuantity();
   }
   
   //订单基本信息
   protected Map<Key_orderBaseMap,Object> getOrderBaseInfoMapFromWeb(HotelOrderFromBean hotelOrderFromBean,OrOrder order,String tipContent){
	   List detailList = getOrPriceDetailList();
	   Map<Key_orderBaseMap,Object> orderBaseInfoMap = new HashMap<Key_orderBaseMap,Object>();
	   //priceList
	   if (null != detailList && !detailList.isEmpty()) {
           for (Iterator it = detailList.iterator(); it.hasNext();) {
               OrPriceDetail item = (OrPriceDetail) it.next();
               item.setOrder(order);
           }
       }
	   orderBaseInfoMap.put(Key_orderBaseMap.PriceList, detailList);
	   //附加信息
	   if(tipContent != null && !"".equals(tipContent.trim())) {
		  OrOrderExtInfo orderExtInfo = new OrOrderExtInfo();
		  //设置类型为01（订单提示信息提示信息）
		  orderExtInfo.setType("01");
		  //设置内容
		  orderExtInfo.setContext(tipContent);
		  orderExtInfo.setOrder(order);
		  List<OrOrderExtInfo> orOrderExtInfoList=new ArrayList<OrOrderExtInfo>();
		  orOrderExtInfoList.add(orderExtInfo);
	      orderBaseInfoMap.put(Key_orderBaseMap.OrOrderExtInfoList, orOrderExtInfoList);
	  }
	   //提供商信息
	   if(hotelOrderFromBean.getChildRoomTypeId() > 0){
		   	try{	  
			HtlPriceType priceType = hotelManage.findHtlPriceType(Long.valueOf(hotelOrderFromBean.getChildRoomTypeId()));
	       	if(null != priceType && null != priceType.getSupplierID()){
	       		Long supplierID = priceType.getSupplierID();
	       		HtlSupplierInfo htlSupplierInfo =  supplierInfoService.querySupplierInfoByID(supplierID.longValue());
	       		if(null != htlSupplierInfo && 1 == htlSupplierInfo.getActive().intValue()){//供应商存在且有效
	           		String supplierAlias = htlSupplierInfo.getAlias();
	           		orderBaseInfoMap.put(Key_orderBaseMap.SupplierAlias,supplierAlias);
	           		orderBaseInfoMap.put(Key_orderBaseMap.SupplierID,supplierID);
	       		}
	       	}
		   	}catch(RuntimeException e){
		   		log.error("OrderCompleteWebAction hotelManage" +(hotelManage==null), e);
		   		throw new RuntimeException(e);
		   	}
		}
		
       return orderBaseInfoMap;
   }
      
   //条款信息
   protected Map<Key_orderClauseMap,Object> getOrderClauseInfoMap(OrReservation reservation){
	   Map<Key_orderClauseMap,Object> orderClauseInfoMap = new HashMap<Key_orderClauseMap,Object>();
	   orderClauseInfoMap.put(Key_orderClauseMap.Reservation, reservation);
	   return orderClauseInfoMap;
   }
   
   //订单会员信息
   protected Map<Key_orderMemberMap,Object> getOrderMemberInfoMapFromWeb(){
	   Map<Key_orderMemberMap,Object> orderMemberInfoMap = new HashMap<Key_orderMemberMap,Object>();
	   MemberDTO member = super.getMemberInfoForWeb(true);
	   orderMemberInfoMap.put(Key_orderMemberMap.Member, member);
	   List fellowList = getOrFellowInfoList();
	   orderMemberInfoMap.put(Key_orderMemberMap.FollewList,fellowList);
	   return orderMemberInfoMap;
   }
   
   //电商相关的信息
   protected Map<Key_orderEBMap,Object> getOrderEBInfoMap(Cookie[] cookies){
	   Map<Key_orderEBMap,Object> orderEBInfoMap = new HashMap<Key_orderEBMap,Object>();
	   if (null != cookies) {
		   String projectCodeValue = "";
           for (int i = 0; i < cookies.length; i++) {
               Cookie sCookie = cookies[i];
               String svalue = sCookie.getValue();
               String sname = sCookie.getName();
               if (sname.equals("projectcode")) {
            	   projectCodeValue = svalue;
               }
           }
           orderEBInfoMap.put(Key_orderEBMap.ProjectCode, projectCodeValue);
       }
	   return orderEBInfoMap;
   }
      
   protected void setSomeValueToHotelOrderFromBean(HotelOrderFromBean hotelOrderFromBean){
	   hotelOrderFromBean.setSuretyPriceRMB(reservation.getReservSuretyPrice());
   }
   
   //封装艺龙担保明细
   private void setElAssureToReservation(OrReservation reservation,HtlElAssure htlElAssure){
	   if(1 == htlElAssure.getAssureType()){
		   reservation.setUnCondition(true);
		   reservation.setOverTimeAssure(false);
		   reservation.setNightsAssure(false);
		   reservation.setRoomsAssure(false);
		   reservation.setNights(0);
	   }	
	   if(2 == htlElAssure.getAssureType()){
	   		reservation.setUnCondition(false);
	   		reservation.setOverTimeAssure(true);
	   		reservation.setNightsAssure(false);
			reservation.setRoomsAssure(false);	
			reservation.setNights(0);
	   		reservation.setLateSuretyTime(htlElAssure.getAssureDate());
  		}
  		if(3 == htlElAssure.getAssureType()){
  			reservation.setUnCondition(false);
  			reservation.setOverTimeAssure(false);
  			reservation.setRoomsAssure(true);
  			reservation.setNightsAssure(false);
  			reservation.setNights(0);
  			reservation.setRooms(htlElAssure.getAssureRoomQuantity());
  		}
  		if(5 == htlElAssure.getAssureType()){
  			reservation.setUnCondition(false);
  			reservation.setOverTimeAssure(true);
  			reservation.setRoomsAssure(true);
  			reservation.setNightsAssure(false);
  			reservation.setNights(0);
  			reservation.setLateSuretyTime(htlElAssure.getAssureDate());
  			reservation.setRooms(htlElAssure.getAssureRoomQuantity());
  		}
	   reservation.setReservSuretyPrice(Math.ceil(htlElAssure.getAssureAmount()*rate));
	   
	   List<OrGuaranteeItem> guaranteeList = getOrGuaranteeItemList();
       if(null != guaranteeList && !guaranteeList.isEmpty()){
       	for (OrGuaranteeItem orGuaranteeItem : guaranteeList) {
       		if(1 == htlElAssure.getMoneyType()){
       			orGuaranteeItem.setAssureType("2");
       		}
       		if(2 == htlElAssure.getMoneyType()){
       			orGuaranteeItem.setAssureType("4");
       		}
       		if(2 == htlElAssure.getAssureType()){
       			orGuaranteeItem.setLatestAssureTime(htlElAssure.getAssureDate());
       		}
       		if(3 == htlElAssure.getAssureType()){
       			orGuaranteeItem.setOverRoomNumber(htlElAssure.getAssureRoomQuantity());
       		}
       		if(5 == htlElAssure.getAssureType()){
       			orGuaranteeItem.setLatestAssureTime(htlElAssure.getAssureDate());
       			orGuaranteeItem.setOverRoomNumber(htlElAssure.getAssureRoomQuantity());
       		}
       		orGuaranteeItem.setAssureCondiction(htlElAssure.getAssureCondition());
       		orGuaranteeItem.setReserv(reservation);
			}
           reservation.setGuarantees(guaranteeList);
       }

       List<OrAssureItemEvery> assureList = getOrAssureItemEveryList();
       if(null != assureList && !assureList.isEmpty()){
       	for (OrAssureItemEvery orAssureItemEvery : assureList) {
       		orAssureItemEvery.setType(htlElAssure.getCancelAssureType());
       		orAssureItemEvery.setFirstDateOrDays(htlElAssure.getAssureDateDay());
       		orAssureItemEvery.setFirstTime(htlElAssure.getAssureSaveDate());
       		orAssureItemEvery.setReserv(reservation);
			}
           reservation.setAssureList(assureList);
       }
   }
   
   protected void setSomeValueToReservation(OrReservation reservation){
       List<OrGuaranteeItem> guaranteeList = getOrGuaranteeItemList();
       if(null != guaranteeList && !guaranteeList.isEmpty()){
       	for (OrGuaranteeItem orGuaranteeItem : guaranteeList) {
       		orGuaranteeItem.setReserv(reservation);
			}
           reservation.setGuarantees(guaranteeList);
       }

       List<OrAssureItemEvery> assureList = getOrAssureItemEveryList();
       if(null != assureList && !assureList.isEmpty()){
       	for (OrAssureItemEvery orAssureItemEvery : assureList) {
       		orAssureItemEvery.setReserv(reservation);
			}
           reservation.setAssureList(assureList);
       }
       
       if (StringUtil.isValidStr(assureDetailStr)) {
           List assureDetailList = hotelManageWeb.convertToAssureInforAssistant(assureDetailStr);
           orignalSuretyPrice = hotelCheckOrderService.calculateSuretyAmount(assureDetailList,hotelOrderFromBean);
           RMBPrice = Math.ceil(orignalSuretyPrice * rate);
           reservation.setReservSuretyPrice(RMBPrice);
       }
   }
     
   /**
    * 从session取值
    * @param name
    * @return
    */
	protected Object getObjectFromSession(String name){
		HttpSession httpSession = request.getSession(false);
		if (httpSession == null) {
			return null;
		}
		
		return httpSession.getAttribute(name);
	}
	/**
	 * 根据手机号或者电邮查询会员信息，优先以手机号进行检索,手机检索出的会员返回第一个会员
	 * @return
	 * dimin.liu 2012/10/24
	 */
	protected MemberDTO getMemberDtoByPhoneNumOrEmail(String mobilePhone,String email){
		MemberDTO memberDto=null;
		List members=null;
		memberDto=memberInterfaceService.getMemberByMobile(mobilePhone);
		if ( memberDto==null ){
			if(email==null||email.equals("")){
				return memberDto;
			}else{
				members=memberInterfaceService.getMemberByEmail(email);
				if(members!=null && members.size() > 0){
					memberDto=(MemberDTO)members.get(0);
				}
			}
		}
		return memberDto;
	}

	/**
	 * 自动注册一个会员
	 * @param mobilePhone
	 */
	protected PersonMainInfo register(MbrInfoDTO mbrInfoDTO){
		return memberInterfaceService.register(mbrInfoDTO);			
	}
	/**
	 * 根据会员ID获取该会员下的所有的会籍信息
	 * @param mbrId
	 * @return
	 */
	protected  List<Mbrship> mbrshipListByMbrId(Long mbrId) {
		return memberInterfaceService.mbrshipListByMbrId(mbrId);
	}
	/**
	 * 判断某个会籍支持返现
	 * @param mbrship
	 * @return
	 */
	protected boolean judgeReturnMbrship(String aliasId){
		boolean flag;
		String id = FITAliasConstant.fitAliasObj.get(aliasId);
		flag= id==null?false:true;
		return flag;
	}
	
	protected Mbrship mbrshipCreate(Long mbrId,Long mbrshipCategoryId,
			String aliasNo,String isBloc, MbrTransactionLogVO mbrTransactionLogVO)throws MbrshipServiceException
	{
		return memberInterfaceService.mbrshipCreate(mbrId, mbrshipCategoryId, aliasNo, isBloc, mbrTransactionLogVO);
		
	}
	protected Long  getMbrIdByMemberCd(String memberCd) {
        return memberInterfaceService.getMbrIdByMemberCd(memberCd);		
	}
	/**
	 * 重写Cookie
	 * @param mbrId
	 * @param memberCd
	 */
	protected void mbrLogin(HttpServletRequest request, HttpServletResponse response,String mbrId,String memberCd){
		CookieUtils.setCookie(request, response, "membercd", memberCd, 0, null, null);
		CookieUtils.setCookie(request, response, "mbrID", mbrId, 0, null, null);
		
	}
	
	/**
	 * 将值存进session中
	 * @param name
	 * @param value
	 */
	protected void putValueToSession(String name,Object value){
		HttpSession httpSession = request.getSession(false);
		if (httpSession == null) {
			return ;
		}
		httpSession.setAttribute(name, value);
	}
	
	/**
	 * 将值清楚session中的值
	 * @param name
	 * @param value
	 */
	protected void cleanValueToSession(String name){
		HttpSession httpSession = request.getSession(false);
		if (httpSession == null) {
			return ;
		}
		httpSession.removeAttribute(name);
	}
	
	public void setOrderImmedConfirmService(
			OrderImmedConfirmService orderImmedConfirmService) {
		this.orderImmedConfirmService = orderImmedConfirmService;
	}

	public void setHdlService(IHDLService hdlService) {
		this.hdlService = hdlService;
	}


	public void setSupplierInfoService(SupplierInfoService supplierInfoService) {
		this.supplierInfoService = supplierInfoService;
	}

	public void setHotelOrderCompleteService(
			IHotelOrderCompleteService hotelOrderCompleteService) {
		this.hotelOrderCompleteService = hotelOrderCompleteService;
	}

	public void setHotelBookingService(HotelBookingService hotelBookingService) {
		this.hotelBookingService = hotelBookingService;
	}

	public int getRoomPrice() {
		return roomPrice;
	}

	public int getTotalRoomNight() {
		return totalRoomNight;
	}

	public void setHtlProjectCodeManage(HtlProjectCodeManage htlProjectCodeManage) {
		this.htlProjectCodeManage = htlProjectCodeManage;
	}

	public String getWeekOfInDate() {
		return weekOfInDate;
	}

	public void setWeekOfInDate(String weekOfInDate) {
		this.weekOfInDate = weekOfInDate;
	}

	public String getWeekOfOutDate() {
		return weekOfOutDate;
	}

	public void setWeekOfOutDate(String weekOfOutDate) {
		this.weekOfOutDate = weekOfOutDate;
	}

	public CheckElongAssureService getCheckElongAssureService() {
		return checkElongAssureService;
	}
	 public String getCookie(String cookieName) throws UnsupportedEncodingException { 
	    	HttpServletRequest request = ServletActionContext.getRequest();
	        Cookie[] cookies = request.getCookies();  
	        String value = ""; 
	        if (cookies != null) {  
	            for (Cookie cookie : cookies) {
	                if (cookieName.equals(cookie.getName())) {  //获取具体的cookie；
	                	value = URLDecoder.decode(cookie.getValue(),"UTF-8");
	                	return value;  
	                }  
	            }  
	        }  
	        return null;  
	}
	public void setCheckElongAssureService(
			CheckElongAssureService checkElongAssureService) {
		this.checkElongAssureService = checkElongAssureService;
	}

	public boolean isAutoFlag() {
		return autoFlag;
	}

	public void setAutoFlag(boolean autoFlag) {
		this.autoFlag = autoFlag;
	}
	public long getMbrID() {
		return mbrID;
	}

	public void setMbrID(long mbrID) {
		this.mbrID = mbrID;
	}

	public String getTrace_code() {
		return trace_code;
	}

	public void setTrace_code(String trace_code) {
		this.trace_code = trace_code;
	}

	public ChannelCashBackManagerService getChannelCashBackService() {
		return channelCashBackService;
	}

	public void setChannelCashBackService(
			ChannelCashBackManagerService channelCashBackService) {
		this.channelCashBackService = channelCashBackService;
	}

	public double getCashbackratevalue() {
		return cashbackratevalue;
	}

	public void setCashbackratevalue(double cashbackratevalue) {
		this.cashbackratevalue = cashbackratevalue;
	}
}
