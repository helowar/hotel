package com.mangocity.hotel.order.web;

import hk.com.cts.ctcp.hotel.constant.ResultConstant;
import hk.com.cts.ctcp.hotel.constant.TxnStatusType;
import hk.com.cts.ctcp.hotel.service.HKService;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BasicData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BeginData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.CustInfo;

import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.mangocity.delivery.unit.DSUnitService;
import com.mangocity.ep.service.EpOrderManagerService;
import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hdl.constant.ChinaOnlineConstant;
import com.mangocity.hdl.hotel.dto.AddExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.AddExRoomOrderResponse;
import com.mangocity.hdl.hotel.dto.CancelExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.CancelExRoomOrderResponse;
import com.mangocity.hdl.hotel.dto.CheckHotelReservateExRequest;
import com.mangocity.hdl.hotel.dto.CheckHotelReservateExResponse;
import com.mangocity.hdl.hotel.dto.CheckPriceTypeResponse;
import com.mangocity.hdl.hotel.dto.CheckRoomTypeResponse;
import com.mangocity.hdl.hotel.dto.CheckRoomTypeResponseItem;
import com.mangocity.hdl.hotel.dto.MGExOrder;
import com.mangocity.hdl.hotel.dto.MGExOrderItem;
import com.mangocity.hdl.hotel.dto.MGExReservItem;
import com.mangocity.hdl.hotel.dto.MGExResult;
import com.mangocity.hdl.hotel.dto.Result;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;
import com.mangocity.hotel.base.persistence.HtlElAssure;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlSupplierInfo;
import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.base.persistence.OrAssureItemEvery;
import com.mangocity.hotel.base.persistence.OrGuaranteeItem;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.SupplierInfoService;
import com.mangocity.hotel.base.service.assistant.HotelPriceSearchParam;
import com.mangocity.hotel.base.service.assistant.ReservationInfo;
import com.mangocity.hotel.exception.BusinessException;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.dto.PointDTO;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.GuaranteeState;
import com.mangocity.hotel.order.constant.HotelConfirmType;
import com.mangocity.hotel.order.constant.ModelType;
import com.mangocity.hotel.order.constant.OrderExtInfoType;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.constant.SessionNames;
import com.mangocity.hotel.order.persistence.FITCashItem;
import com.mangocity.hotel.order.persistence.FITOrderCash;
import com.mangocity.hotel.order.persistence.OrChannelNo;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderExtInfo;
import com.mangocity.hotel.order.persistence.OrOrderFax;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrOrderStatistics;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrPreSale;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.HotelElOrderService;
import com.mangocity.hotel.order.service.HtlOrderChannelService;
import com.mangocity.hotel.order.service.IHotelReservationInfoService;
import com.mangocity.hotel.order.service.INewOrderParamService;
import com.mangocity.hotel.order.service.IOrderBenefitService;
import com.mangocity.hotel.order.service.IOrderEditService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.service.IVoucherInterfaceService;
import com.mangocity.hotel.order.service.assistant.BaseParams;
import com.mangocity.hotel.order.service.assistant.EverydayParams;
import com.mangocity.hotel.order.service.assistant.HtlOrderChannel;
import com.mangocity.hotel.order.service.assistant.MemberAliasConstants;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.config.ConfigUtil;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.hotel.constant.QuotaType;
import com.mangocity.util.log.MyLog;
import com.mangocity.vch.app.service.exception.VCHException;
import com.mangocity.webnew.persistence.ElongAssureResult;
import com.mangocity.webnew.service.CheckElongAssureService;
import com.mangoctiy.communicateservice.CommunicaterService;

/**
 * 前台创建订单相关操作Action
 * 
 * @author chenkeming
 * 
 */
public class NewOrderAction extends PopulateOrderAction {

    private static final long serialVersionUID = 4169704016819392437L;
    
    private static final MyLog log = MyLog.getLogger(NewOrderAction.class);

    @SuppressWarnings("unused")

    private static final String RESDETAIL_URL =
        ConfigUtil.getResourceByKey("hotelii_i_delivery.resdetail_url");

    // 默认联系方式
    private String ctcttype;

    // 是否有房费需另缴税
    private String isTaxCharges;

    // 房费需另缴税detail
    private String taxChargesDetail;

    // 是否促销
    private String isSalesPromo;

    private Date beginDate;

    private Date endDate;

    private Long hotelId;

    /**
     * 配送单位接口
     */
    private DSUnitService deliveryUnitService;

    /**
     * message接口
     */
    private CommunicaterService communicaterService;
    
    private HotelRoomTypeService hotelRoomTypeService;

    private HotelElOrderService hotelElOrderService;
    /**
     * 是否保存到前台
     */
    private boolean saveToFront;

    /**
     * 源订单ID
     */
    private String oriOrderId;

    /**
     * 是否重下新单
     */
    private String sRenew;

    /**
     * 以下用于重下新单保存撤单原因
     */
    private String renewReason;

    private String renewMessage;

    private String guestRenewMessage;

    /**
     * 面付、预付、面付／转预付
     */
    private String payMethod;

    /**
     * 酒店预订信息，包括提示信息，等等
     */
    private ReservationInfo reserv;

    private String mostStar;

    private String mostPriceLevel;

    /**
     * 用于根据酒店及房态控制可订床型
     */
    private String useableRoom;

    /**
     * 币种符号
     */
    private String idCurStr;

    private IHotelService hotelService;
    
    private IHotelReservationInfoService hotelReservationInfoService;

    private String creditRemark;

    // 有一天配额为0时标志为真
    private boolean quotaBool;

    // 所有床型房态均为不可超标志为真

    private boolean bedStateOneBool;

    private boolean bedStateTwoBool;

    private boolean bedStateThrBool;

    private int quotaLeastNum;
    
    //hotel 2.9.3 房间数 add by shengwei.zuo 2009-09-09
    private String canRoomNumberAlert;

    /**
     * 渠道方类型 author guojun 2008-12-08 15:00
     */
    private String channel;

    /**
     * add by wuyun 2008-11-26 15:30 酒店直联
     * 
     * @return
     */
    private String cooperateChannel;

    /**
     * 酒店直联 IHDLService
     * 
     * @author guojun 2008-11-26 15:30
     */
    private IHDLService hdlService;

    /**
     * add by wuyun 2008-11-26 15:30 是否走传统渠道
     * 
     * @return
     */
    private boolean traditionalChannel;

    /**
     * add by szw 2008-12-18 预订房间间数
     */
    private String hotelroomcount;

    /**
     * 新下单时是否修改基本信息
     * 
     * @author chenkeming Feb 23, 2009 5:32:38 PM
     */
    private boolean forEdit;

    /**
     * add by chenjiajie 2009-01-23 V2.5 资源文件类
     */
    private ResourceManager resourceManagerA;

    /**
     * 中旅接口
     * 
     * @author chenkeming Mar 17, 2009 4:00:09 PM
     */
    private HKService hkService;

    /**
     * 修改订单相关service
     * 
     * @author chenkeming Mar 1, 2009 3:36:04 PM
     */
    private IOrderEditService orderEditService;

    /**
     * 返回取消修改规则的提示语
     * 
     * @author guzhijie 2009-07-29
     */
    private String addModifyCancelStrr = "";

    /**
     * 促销信息列表
     */
    private List<OrPreSale> preSaleList;

    /**
     * 促销信息数量
     */
    private int preSaleNum;
    
    /**
     * 二次确定标示（现用于如家） add by diandian.hou 2010-10-25
     */
    private boolean flagSecondConfirm;
    
    /**
     * 优惠立减服务接口 add by chenjiajie 2009-10-15 2009-10-15 
     */
    private IOrderBenefitService orderBenefitService;

    /**
	 * 现金返还服务接口 add by linpeng.fang 2010-10-21
	 */
   private IHotelFavourableReturnService returnService;
   
   /**
    * 芒果网限量返现活动  add by xiaojun.xiong 2011-3-5
    */
   private HtlLimitFavourableManage limitFavourableManage;
   
   
   // 是否交行全卡商旅等渠道 add by chenkeming
   private String forCooperate;
   
   private HotelManage hotelManage;
   
   private SupplierInfoService supplierInfoService;
   
   private INewOrderParamService newOrderParamService;

   private Map<String,String> params =null;
   
   private String isEpOrder;
	
   private EpOrderManagerService epOrderManagerService;
   
   private CheckElongAssureService checkElongAssureService;
   
   private String oldChannel;
   
   private HtlOrderChannelService htlOrderChannelService;

    /**
     * 暂存前台
     * 
     * @return
     */
    public String saveToFront() {

        // 防止重复提交
        if (isRepeatSubmit()) {
            return forwardMsg("请不要重复提交!");
        }

        putSession(SessionNames.QUERY_COND, null);

        roleUser = getOnlineRoleUser();
        member = getOnlineMember();

        order = new OrOrder();
        populateOrderSaveToFront(order);
        
        // 处理交行全卡商旅等渠道下单的信息, 设置order的agentId属性 add by chenkeming
        handleCooperate();

        Map params = getParams();
        String quotaType = (String) params.get("quotaType");
        if (!order.isManualOrder()) {
            order.setQuotaTypeOld(quotaType);
        }

        order.setSource(OrderSource.FROM_PHONE);
        order.setOrderState(OrderState.NOT_SUBMIT);

        if (member.isMango()) {
            order.setType(OrderType.TYPE_MANGO);
        } else {
            order.setType(OrderType.TYPE_114);
            order.setMemberState(member.getState());
        }
        //代理打电话到cc下单，判断当前登录会员是否为一开代理
        String mcd = member.getMembercd();
        if("9400100001".equals(member.getAliasid())){
        	order.setType(OrderType.TYPE_B2BAGENT);
        }else if(null!=mcd && orderService.isb2bMember(mcd)){
        	order.setType(OrderType.TYPE_B2BAGENT);
        }
        //代理add by zhijie.gu 2010-3-10 end
        // 预付单暂存前台的时候没有保存应付价格，导致修改的时候看不到
        if (PayMethod.PRE_PAY.equals(order.getPayMethod())) {
            String prepayTotalRmb = (String) params.get("prepayTotalRmb");
            if (StringUtil.isValidStr(prepayTotalRmb)) {
                order.setSumRmb(Double.valueOf(prepayTotalRmb));
            }
        }
        // 订单币种处理
        String paymentCurrency = order.getPaymentCurrency();
        double rateDou = 1.0;
        if (null == paymentCurrency) {
            idCurStr = CurrencyBean.idCurMap.get(CurrencyBean.RMB);
            order.setPaymentCurrency(CurrencyBean.RMB);
        } else {
            idCurStr = CurrencyBean.idCurMap.get(paymentCurrency);
            if (null == idCurStr) {
                idCurStr = CurrencyBean.idCurMap.get(CurrencyBean.RMB);
            }
            String rateStr = CurrencyBean.rateMap.get(paymentCurrency);
            if (null == rateStr) {
                rateStr = "1.0";
            }
            rateDou = Double.valueOf(rateStr).doubleValue();
        }
        order.setRateId(rateDou);

        // 获取订单预订条款
        OrReservation reserv = new OrReservation();
        MyBeanUtil.copyProperties(reserv, params);
        order.setReservation(reserv);
        // 有必要则获取取消修改规则信息
        if (!order.isManualOrder()) {
            if ((!order.isPrepayOrder() && reserv.isCanAssure()) || order.isPrepayOrder()) {
                orderEditService.fillAssureItemEvery(order, (String) params.get("cancelModifyStr"));
                if (!order.isPrepayOrder()) { // 如果有必要则获取担保明细
                    OrderUtil.fillGuaranteeItem(reserv, params);
                }
            }
        }

        if (roleUser.isOrgMid()) {
            order.setModifiedMidTime(new Date());
        } else if (roleUser.isOrgFront()) {
            order.setModifiedFrontTime(new Date());
        }

        // 获取房费另缴税和促销信息 v2.6 add by chenkeming
        if (!order.isManualOrder()) {
            if ("1".equals(isTaxCharges)) {
                orderService.getTaxCharge(order);
            }
            if ("1".equals(isSalesPromo)) {
                // hotelService.assumeOrderPresale(params, OrPreSale.class, preSaleNum,order);
                orderService.getPreSale(order);
            }
        }
        
        
        //判断跳转页面
        String queryPort="";
        String[] urls=new String[1];
        String url="";
    	Cookie cookies[]=request.getCookies();
    	for(int i=0;i<cookies.length;i++){    	
    		if(cookies[i].getName().equals("queryPort")){
    		 queryPort=cookies[i].getValue();	
    		}
    	}
        
        Serializable sID = orderService.saveOrUpdate(order);
        String sOrderCD = orderService.getOrderCDByID(sID);
        String alertMessage = "前台保存成功! 订单号：" + sOrderCD;
        String[] btns = { "重新预订" };
        if(null!=queryPort && queryPort.equals("New_Query")){
        	url = "/order/hotelSearch!searchPreNew.action?oriOrderId="+sID+"&isSaveToFront=true";
        }else{
            url = "/order/hotelSearch!searchPre.action?oriOrderId="+sID+"&isSaveToFront=true";
        }
        urls[0]=url;
        return forwardMsgEx(alertMessage, btns, urls, false);

    }
    
    

	/**
	 * 传入show（）方法所需的参数
	 */
	public String newOrderParam() {
		OrOrder oriOrder = null;
		BaseParams param = null;

		member = getOnlineMember();
		roleUser=getOnlineRoleUser();
		
		params = super.getParams();
		if(null!=params.get("memberCD")&&!"".equals(params.get("memberCD"))){			
            member = memberInterfaceService
                    .getMemberByCode(params.get("memberCD"));
		}
		
		//用于保存Flex页面传过来的查询条件参数，用于取消新增订单时，带回查询条件 add by wupingxiang at 2012-06-12
		request.setAttribute("queryConditions", params.get("queryConditions"));
		
		//封装重下新单的参数
		if(null!=params.get("reNewOrder")&&!"".equals(params.get("reNewOrder"))&&!params.get("reNewOrder").equalsIgnoreCase("null")){
			
			String reNewOrder=params.get("reNewOrder");
			String reNewInfo[]=reNewOrder.split("/");
            if(reNewInfo.length==3){
    			params.put("oriOrderId",reNewInfo[0]);
    			oriOrderId=reNewInfo[0].toString();
    			sRenew=reNewInfo[1].toString();
    			params.put("sRenew", reNewInfo[1]);
    			params.put("renewReason", reNewInfo[2]);
    			renewReason=reNewInfo[2].toString();
            }else{
    			params.put("oriOrderId",reNewInfo[0]);
    			oriOrderId=reNewInfo[0].toString();
    			params.put("sRenew", reNewInfo[1]);
    			sRenew=reNewInfo[1].toString();
    			params.put("renewReason", reNewInfo[2]);
    			renewReason=reNewInfo[2].toString();
    			params.put("renewMessage", reNewInfo[3]);
    			renewMessage=reNewInfo[3].toString();
    			params.put("guestRenewMessage", reNewInfo[4]);
    			guestRenewMessage=reNewInfo[4].toString();
            }

		}
		//用于接收补单，参考预订试穿过来的订单号
		if (params.get("oriOrderId") != null&&!"".equals(params.get("oriOrderId"))&&!params.get("oriOrderId").equalsIgnoreCase("null")) {
			oriOrder = getOrder(Long.valueOf(params.get("oriOrderId")
					.toString()));
		}
		
			param = newOrderParamService.getBaseParams(params.get(
			"priceTypeID").toString(),
			params.get("checkinDate").toString(), params
					.get("checkoutDate").toString(),params.get("payMethod").toString(),roleUser);


		if(param==null){
			  return forwardError("不能预订该价格类型，配额或价格信息不全！");
		}
		//对订单所需要的参数赋值
		params.put("hotelId", param.getHotelId().toString());
		this.setHotelId(Long.parseLong(param.getHotelId().toString()));
		params.put("roomTypeId", param.getRoomTypeId());
		params.put("childRoomTypeId", param.getChildRoomTypeId());
		params.put("childRoomTypeName", param.getChildRoomTypeName());
		params.put("quotaType", param.getQuotaType());
		params.put("canRoomNumberAlert", param.getCanRoomNumberAlert());
		params.put("hotelName", param.getHotelName());
		params.put("roomTypeName", param.getRoomTypeName());
		params.put("balanceMethod", param.getBalanceMethod());
		params.put("hotelStar", param.getHotelStar());
		params.put("cityId", param.getCityId());
		params.put("acceptCustom", param.getAcceptCustom());
		params.put("paymentCurrency", param.getPaymentCurrency());
		params.put("payToHotel",param.getPayToHotel().toString());
		if(null==params.get("Channel")||"".equals(params.get("Channel"))){
			params.put("Channel", param.getChannel());
		}
		params.put("isUserComment", param.getIsUserComment());
		params.put("isSalesPromo",param.getIsSalesPromo()==null?"0":param.getIsSalesPromo());
		isSalesPromo=param.getIsSalesPromo()==null?"0":param.getIsSalesPromo();
		params.put("favourableFlag", param.getFavourableFlag());
		params.put("forCooperate", param.getForCooperate());
		params.put("bedStateOneBool", param.getBedStateOneBool().toString());
		params.put("bedStateTwoBool", param.getBedStateTwoBool().toString());							
		params.put("bedStateThrBool", param.getBedStateThrBool().toString());
		params.put("quotaBool", Boolean.valueOf(param.getQuotaBool()).toString());
		if(null!=param.getTipContent()){
			params.put("tipContent",param.getTipContent());
		}
		if(null!=param.getClueInfo()){
			params.put("clueInfo", param.getClueInfo().toString());
		}
		bedStateOneBool=Boolean.valueOf(param.getBedStateOneBool().toString());
		bedStateTwoBool=Boolean.valueOf(param.getBedStateTwoBool().toString());
		bedStateThrBool=Boolean.valueOf(param.getBedStateThrBool().toString());
		quotaBool=Boolean.valueOf(param.getQuotaBool().toString());
		params.put("isTaxCharges","0");
		params.put("quotaLeastNum", Integer.valueOf(param.getQuotaLeastNum()).toString());
		quotaLeastNum=Integer.valueOf(param.getQuotaLeastNum());
		
		String cooperate = param.getForCooperate();
		if ((member!=null&&member.isCooperate())||(oriOrder!=null&&oriOrder.isCooperateOrder())) {

			params.put("forCooperate", "1");
		}
		List<EverydayParams> everydayParams = param.getEverydayParams();
		
		if(everydayParams==null||everydayParams.size()==0){
			return forwardError("该天的该价格类型不能预订！");
		}
		 Double  priceAllDay=new Double(0.00); 
		 //对每天的预订信息赋值
		if(everydayParams!=null){	
   		for (int i = 0; i < everydayParams.size(); i++) {
			EverydayParams p = everydayParams.get(i);
			params.put("hSalePrice" + i, p.gethSalePrice());
			params.put("hRoomStatus" + i, p.gethRoomStatus());
			params.put("hBreakfast" + i, p.gethBreakfast());
			params.put("hBreakfasts" + i, p.gethBreakfasts());
			params.put("hBreakNum" + i, p.gethBreakNum());
			params.put("hBasePrice" + i, p.gethBasePrice());
			params.put("hQuantity" + i, p.gethQuantity());
			params.put("hMarketPrice" + i, p.gethMarketPrice());
			params.put("maxQty" + i, p.getMaxQty());
			if(i==0){			
//				param.setPriceFirstDay( new Double(Double.parseDouble(p.gethSalePrice().toString())* Integer.parseInt(params.get("hotelroomcount").toString())).toString());
				param.setPriceFirstDay( Double.valueOf(p.gethSalePrice()).toString());

			}
			priceAllDay+=Double.parseDouble( p.gethSalePrice());
		}
       }
       params.put("priceFirstDay", param.getPriceFirstDay());
       params.put("priceAllDay", priceAllDay.toString());
       params.put("colFirstDayPrice",param.getPriceFirstDay());
		String thisreturn=this.show();
		return thisreturn;
	}



	/**
     * 前台进入创建订单 将预订信息输出到页面当中
     * 
     * @return
     */
    public String show() {

 		System.out.println("===========show()==========");
		if(params==null){
			params = super.getParams();
		}		
		if(member==null){
			member = getOnlineMember();
		}
        boolean isB2BOrder = false;

        // 如果是参考预定或者修改预订，把源订单的入住人信息、联系人信息等带过来
        if (!forEdit && StringUtil.isValidStr(oriOrderId)) {
            OrOrder oriOrder = getOrder(Long.valueOf(oriOrderId));

            order = new OrOrder();
            MyBeanUtil.copyProperties(order, params);

            if (StringUtil.isValidStr(sRenew)) {
                order.setOrderCDHotel(oriOrder.getOrderCDHotel());
                order.setOriginCD(oriOrder.getOrderCD());
                order.setCancelReason(Integer.parseInt(renewReason));
                order.setCancelMessage(renewMessage);
                order.setGuestCancelMessage(guestRenewMessage);
            }
            // 解决重下新单时，带入的是订单的会员而不是登陆会员 modified by wuyun
            if (null == member || !member.getMembercd().equals(oriOrder.getMemberCd())) {
                try {
                    member = memberInterfaceService
                        .getMemberByCode(oriOrder.getMemberCd());
                    if (null == member) {
                        return forwardError("获取不到原订单会员信息！");
                    }
                } catch (Exception e) {
                	log.error(e.getMessage(),e);
                    return forwardError("获取订单会员信息出错！");
                }
            }

            // 芒果会员和114会员均须把入住人,联系人和特殊要求信息带过来
            // copy源订单基本信息
            order.setLinkMan(oriOrder.getLinkMan());
            order.setTitle(oriOrder.getTitle());
            order.setMobile(oriOrder.getMobile());
            order.setTelephone(oriOrder.getTelephone());
            order.setCustomerFax(oriOrder.getCustomerFax());
            order.setEmail(oriOrder.getEmail());
            order.setConfirmType(oriOrder.getConfirmType());
            order.setSpecialRequest(oriOrder.getSpecialRequest());

            // copy源订单入住人信息
            List list = MyBeanUtil.copyCollection(oriOrder.getFellowList(), OrFellowInfo.class);
            order.setFellowList(list);
            
            //如果原单为b2b的单，需要带出以下数据 add by zhijie.gu 2010-3-19 
            if(4 == oriOrder.getType()){
            	isB2BOrder = true;
            }
            if(null != oriOrder.getB2bOperaterId() && !"".equals(oriOrder.getB2bOperaterId()) ){
            	order.setB2bOperaterId(oriOrder.getB2bOperaterId());
            }

        } else {
            // 如果会员没有登录
            if (null == member) {
                return forwardError("获取会员信息失败!请先登录会员，再下订单。");
            }

            order = new OrOrder();
            MyBeanUtil.copyProperties(order, params);
        }

        // 如果是修改基本信息,则把之前修改的入住人信息传过来
        if (forEdit) {
            List fellowList = MyBeanUtil.getBatchObjectFromParam(params, OrFellowInfo.class,
                fellowNum);
            order.setFellowList(fellowList);
        }

        // 面付转预付和信用卡担保互斥?
        if (order.isPayToPrepay()) {
            order.setIsCreditAssured(false);
            order.setPayMethod(PayMethod.PRE_PAY);
        }
       
        // 设置订单类型(mango or 114)
        if (member.isMango()) {
            order.setType(OrderType.TYPE_MANGO);
        } else {
            order.setType(OrderType.TYPE_114);
        }
        //如果重下新单时，被撤销的单为b2b时，新单订单类型应为 B2B
        if(isB2BOrder == true){
        	order.setType(OrderType.TYPE_B2BAGENT);
        }

        // 设置下单酒店所属城市
        if (!forEdit) {
            order.setCity((String) params.get("cityId"));
        } else {
            order.setCity((String) params.get("city"));
        }
              
        if(null != order.getChildRoomTypeId()){
        	HtlPriceType priceType = hotelManage.findHtlPriceType(order.getChildRoomTypeId());
        	if(null != priceType && null != priceType.getSupplierID()){
        		Long supplierID = priceType.getSupplierID();
        		HtlSupplierInfo htlSupplierInfo =  supplierInfoService.querySupplierInfoByID(supplierID);
        		if(null != htlSupplierInfo && 1 == htlSupplierInfo.getActive()){//供应商存在且有效
            		String supplierAlias = htlSupplierInfo.getAlias();
            		order.setSupplierAlias(supplierAlias);
            		order.setSupplierID(supplierID);
        		}
        	}
        }

        // 设置订单会员信息
        order.setMemberId(Long.valueOf(member.getId()));
        order.setMemberCd(member.getMembercd());
        order.setMemberName((null == member.getName() ? "" : member.getName()) + " "
            + (null == member.getFirstname() ? "" : member.getFirstname()) + " "
            + (null == member.getLastname() ? "" : member.getLastname()));

        // 获取下订单页面第一栏会员相关信息
        if (member.isMango()) {
            OrOrderStatistics orderStat = orderService.getOrderStatByMemberCd(member.getMembercd());
            request.setAttribute("orderStat", orderStat);
            if (null != orderStat) {
                double price = orderStat.getAvgPrice();
                double star = orderStat.getAvgStar();
                mostStar = OrderUtil.getMostStar(star);
                mostPriceLevel = OrderUtil.getPriceLevel(price);
            }
        }

        // 根据会员信息设置下单页面用到的常入住人和常联系人数
        request.setAttribute("oftenFellowCount", member.getFellowList().size());
        request.setAttribute("oftenLinkmanCount", member.getLinkmanList().size());

        // 设置订单的总金额，汇率，以及下单页面所用到的币种字符串和币种符号
        OrReservation reserv = order.getReservation();
        if (null == reserv) {
            reserv = new OrReservation();
            order.setReservation(reserv);
        }
        double priceFirstDay = Double.parseDouble((String) params.get("priceFirstDay"));
        double priceAllDay = Double.parseDouble((String) params.get("priceAllDay"));
        Map<String, String> rateMap = CurrencyBean.rateMap;
        if (null != rateMap) {
            String rateStr = rateMap.get(order.getPaymentCurrency());
            if (null == rateStr) {
                rateStr = "1.0";
            }
            request.setAttribute("rateCurrency", rateStr);
            double rate = Double.valueOf(rateStr.trim()).doubleValue();
            order.setRateId(rate);
			//生产bug 783 香港酒店check in day 担保会多乘一次汇率 add by haibo.li begin
			reserv.setFirstPrice(priceFirstDay);
			//end
            order.setSum(priceAllDay);
            order.setSumRmb(priceAllDay * rate);
            idCurStr = CurrencyBean.idCurMap.get(order.getPaymentCurrency());
        } else {
            reserv.setFirstPrice(priceFirstDay);
            order.setSum(priceAllDay);
            order.setSumRmb(priceAllDay);
        }

        // 直联酒店ChinaOnline可能需要使用的首日房价 add by chenjiajie V2.5@2009-02-02
        request.setAttribute("colFirstDayPrice", (String) params.get("colFirstDayPrice"));

        // 从查酒店结果页面（参数）获取价格详细信息
        Date tempDate = order.getCheckinDate();
        int difdays = DateUtil.getDay(tempDate, order.getCheckoutDate());
        request.setAttribute("difdays", difdays);
        String[] hDays = new String[difdays];
        String[] hSalePrice = new String[difdays];
        String[] hRoomStatus = new String[difdays];
        String[] hBreakfast = new String[difdays];
        String[] hBreakfast1 = new String[difdays];
        String[] hBreakNum = new String[difdays];
        String[] hBasePrices = new String[difdays]; // 底价信息 add by baofeng.si V2.3 2008-6-16
        String[] hQuantity = new String[difdays];
        String[] hMarketPrice = new String[difdays];
        // 直连酒店增加原有价格，用于和从试预订接口返回的价格作比较 add by chenkeming
        String[] hSalePriceOld = null;
        String[] hBasePricesOld = null;
        if (!order.isOriChannel()) {
            hSalePriceOld = new String[difdays];
            hBasePricesOld = new String[difdays];
        }
        List<String> dateStrList = DateUtil
            .getDateStrList(tempDate, order.getCheckoutDate(), false);
        for (int i = 0; i < difdays; i++) {
            if (0 < i) {
                tempDate = DateUtil.getDate(tempDate, 1);
            }
            hDays[i] = DateUtil.dateToString(tempDate);
            String everyDaPri = (String) params.get("hSalePrice" + i);
            hSalePrice[i] = everyDaPri;
            String roomSatuDaStr = (String) params.get("hRoomStatus" + i);
            hRoomStatus[i] = roomSatuDaStr;
            hBreakfast[i] = (String) params.get("hBreakfast" + i);
            hBreakfast1[i] = (String) params.get("hBreakfasts" + i);
            hBreakNum[i] = (String) params.get("hBreakNum" + i);
            // 底价信息 add by baofeng.si V2.3 2008-6-16
            hBasePrices[i] = (String) params.get("hBasePrice" + i);
            String quaDaStr = (String) params.get("hQuantity" + i);
            hQuantity[i] = quaDaStr;
            hMarketPrice[i] = (String) params.get("hMarketPrice" + i);
            if (!order.isOriChannel()) {
                hSalePriceOld[i] = (String) params.get("hSalePriceOld" + i);
                hBasePricesOld[i] = (String) params.get("hBasePriceOld" + i);
            }
            // 加每天的价格详情到订单的 priceList，add by zhineng.zhuang 2009-02-18 begin
            OrPriceDetail priceDetail = new OrPriceDetail();
            priceDetail.setNight(tempDate);
            int dayIndex = i + 1;
            priceDetail.setDayIndex(dayIndex);
            priceDetail.setDateStr(dateStrList.get(i));
            if (StringUtil.isValidStr(everyDaPri)) {
                priceDetail.setSalePrice(Double.valueOf(everyDaPri));
            }
            if (StringUtil.isValidStr(roomSatuDaStr)) {
                priceDetail.setRoomState(roomSatuDaStr);
            }
            if (StringUtil.isValidStr(quaDaStr)) {
                priceDetail.setQuantity(Integer.valueOf(quaDaStr));
            }
            priceDetail.setBreakfastStr(hBreakfast[i]);
            order.getPriceList().add(priceDetail);

            // 加每天的价格详情到订单的 priceList，add by zhineng.zhuang 2009-02-18 end
        }
        // modified by lixiaoyong v2.6 2009-05-06 文件回滚后，价格详情前后多加两天
        //refactor
        HotelPriceSearchParam hotelPriceSearchParam = new HotelPriceSearchParam();
        hotelPriceSearchParam.setCheckinDate(order.getCheckinDate());
        hotelPriceSearchParam.setCheckoutDate(order.getCheckoutDate());
        hotelPriceSearchParam.setHotelId(order.getHotelId());
        hotelPriceSearchParam.setPayMethod(order.getPayMethod());
        hotelPriceSearchParam.setPayToPrepay(order.isPayToPrepay());
        hotelPriceSearchParam.setRoomTypeId(order.getRoomTypeId());
        request.setAttribute("hotelPriceList", hotelService.getHotelPriceList(hotelPriceSearchParam));
        request.setAttribute("hDays", hDays);
        request.setAttribute("hSalePrices", hSalePrice);
        request.setAttribute("hRoomStatuss", hRoomStatus);
        request.setAttribute("hBreakfasts", hBreakfast);
        request.setAttribute("hBreakfasts1", hBreakfast1);
        request.setAttribute("hBreakNums", hBreakNum);
        request.setAttribute("hQuantity", hQuantity);
        // 底价信息 add by baofeng.si V2.3 2008-6-16
        request.setAttribute("hBasePrices", hBasePrices);
        request.setAttribute("hMarketPrice", hMarketPrice);
        request.setAttribute("hBasePricesOld", hBasePricesOld);
        request.setAttribute("hSalePricesOld", hSalePriceOld);
        // 如果有房费另缴税,则传到页面显示
        if ("1".equals(isTaxCharges)) {
            List lstRoomTaxCharge = lstRoomTaxCharge(order);
            request.setAttribute("lstRoomTaxCharge", lstRoomTaxCharge);
        }
        // modified by lixiaoyong v2.6 2009-05-06 房间数目的传递
        if (null == params.get("hotelroomcount")) {
            order.setRoomQuantity(Integer.parseInt((String) params.get("roomQuantity")));
            hotelroomcount = (String) params.get("roomQuantity");
        } else {
            order.setRoomQuantity(Integer.parseInt((String) params.get("hotelroomcount")));
            hotelroomcount = (String) params.get("hotelroomcount");
        }
        
        //hotel 2.9.3 总的房间数 add by shengwei.zuo 2009-09-09
        canRoomNumberAlert = (String) params.get("canRoomNumberAlert");
        
        //艺龙担保信息单独处理 add by luo
        String bookhintCancelAndModifyStr = "";//elong取消修改规则
		if((ChannelType.CHANNEL_ELONG+"").equals(channel)){
			ElongAssureResult assureResult = checkElongAssureService.checkIsAssure(order.getHotelId(), order.getChildRoomTypeId()
	   				, order.getCheckinDate(), order.getCheckoutDate(), 1, "00:00");
			if(assureResult.getConditionStr()!=null && !"".equals(assureResult.getConditionStr())){
				HtlElAssure htlElAssure = new HtlElAssure();
				//最晚担保时间
				String overTimeHour = assureResult.getOverTimeHour()==0 ? "18":assureResult.getOverTimeHour()+"";
				overTimeHour = overTimeHour.length()<2 ? "0"+overTimeHour:overTimeHour;
				String overTimeMin = assureResult.getOverTimeMin()==0 ? "00":assureResult.getOverTimeMin()+"";
				overTimeMin = overTimeMin.length()<2 ? "0"+overTimeMin:overTimeMin;
				htlElAssure.setAssureDate(overTimeHour+":"+overTimeMin);
				int assureType=assureResult.getAssureType();//转换担保类型
				if(assureType==2){
					assureType=3;
				}else if(assureType==3){
					assureType=2;
				}else if(assureType==4){
					assureType=5;
				}
				htlElAssure.setAssureType(assureType);
				htlElAssure.setAssureRoomQuantity(assureResult.getOverQtyNum());
				htlElAssure.setMoneyType(assureResult.getAssureMoneyType());
				if(assureResult.getAssureMoneyType()==1){
					htlElAssure.setAssureAmount(priceFirstDay);
				}else if(assureResult.getAssureMoneyType()==2){
					htlElAssure.setAssureAmount(priceAllDay);
				}
				htlElAssure.setAssureCondition(this.getGuaranteeAssureCondition(assureResult));
				htlElAssure.setCancelAssureType(assureResult.getModifyBeforeDate()==null?"1":"2");
				if(assureResult.getModifyBeforeDate()!=null){
					htlElAssure.setAssureDateDay(DateUtil.getTimeStrFromDate(assureResult.getModifyBeforeDate(),"yyyy-MM-dd"));
					htlElAssure.setAssureSaveDate(DateUtil.getTimeStrFromDate(assureResult.getModifyBeforeDate(),"HH:mm"));
				}else{
					htlElAssure.setAssureDateDay("");
					htlElAssure.setAssureSaveDate("");
				}
				htlElAssure.setCancelAndModifySentence(assureResult.getModifyStr());
				this.setElongAssureToOrder(order,htlElAssure);
				bookhintCancelAndModifyStr = htlElAssure.getCancelAndModifySentence();
			}
		}else{
			// 获取本部的预订条款
	        orderEditService.getReservationInfo(order, false);
		}
		
        // 预订担保注意事项换行符处理
        String str = reserv.getCreditRemark();
        if (null != str) {
            creditRemark = StringUtil.formatHtmlString(str);
        }

        // 根据房态字符串数组获取供选择的可预订床型
        useableRoom = OrderUtil.getAbleBedTypes(hRoomStatus);
        request.setAttribute("useableRoom", useableRoom);

        // 从页面参数获取值
        request.setAttribute("quotaType", (String) params.get("quotaType")); // 配额类型
        request.setAttribute("acceptCustom", (String) params.get("acceptCustom")); // 不接受国家
        request.setAttribute("clueInfo", (String) params.get("clueInfo")); // 酒店的提示信息
        // add by shengwei.zuo hotel2.9.2 2009-08-18 begin
        request.setAttribute("tipContent", (String) params.get("tipContent")); // 提示信息
        // add by shengwei.zuo hotel2.9.2 2009-08-18 end

        // 配送饱和度等配送相关信息url
        request.setAttribute("fulResUrl", RESDETAIL_URL);

        // 获取权限用户,下单页面用到
        roleUser = getOnlineRoleUser();

        // v2.8 如果是下中旅单,hold配额 add by chenkeming@2009-03-17
        order.setRoomQuantity(StringUtil.isValidStr(hotelroomcount) ? Integer
            .parseInt(hotelroomcount) : 1);
        if (String.valueOf(ChannelType.CHANNEL_CTS).equals(channel)) {
            int[] maxNo = new int[difdays];
            for (int i = 0; i < difdays; i++) {
                String sMaxQty = (String) params.get("maxQty" + i);
                maxNo[i] = StringUtil.isValidStr(sMaxQty) ? Integer.parseInt(sMaxQty) : 9999;
            }
            OrderUtil.splitOrderForChannel(order, maxNo);
            List<OrChannelNo> li = order.getChannelList();
            boolean bSuc = true;
            for (OrChannelNo orderChannel : li) {
                BeginData beginData = hkService.saleBegin();
                if (0 > beginData.getNRet()) {
                    return forwardError(beginData.getSMessage());
                }
                String sTxnNo = beginData.getSTxnNo();
                orderChannel.setOrderChannel(sTxnNo);
                BasicData ret = new BasicData();
                try {
                    ret = hkService.holdRoom(sTxnNo, order.getHotelId(), order.getCheckinDate(),
                        order.getCheckoutDate(), order.getRoomTypeId(), order.getChildRoomTypeId(),
                        orderChannel.getQuantity());
                    if (ret.getNRet() < ResultConstant.RESULT_SUCCESS) {
                        log.error("中旅订单hold配额失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                            + orderChannel.getOrderChannel() + ", WebService错误代码: " + ret.getNRet()
                            + ", 错误信息:" + ret.getSMessage());
                        bSuc = false;
                    }
                } catch (Exception e) {
                    bSuc = false;
                    ret.setSMessage("中旅订单hold配额出异常!");
                    log.error(e.getMessage(),e);
                }

                // 如果中旅订单hold配额失败
                if (!bSuc) {
                    // 回滚释放之前hold的配额
                    for (OrChannelNo rollChannel : li) {
                        int status = rollChannel.getStatus();
                        if (TxnStatusType.Begin == status || TxnStatusType.Commited == status) {
                            try {
                                BasicData retRoll = hkService.saleRollback(rollChannel
                                    .getOrderChannel());
                                if (retRoll.getNRet() < ResultConstant.RESULT_SUCCESS) {
                                    log.error("释放中旅订单配额失败, 芒果订单编号:" + order.getOrderCD()
                                        + ", 中旅订单号:" + rollChannel.getOrderChannel()
                                        + ", WebService错误代码: " + retRoll.getNRet() + ", 错误信息:"
                                        + retRoll.getSMessage());
                                }
                            } catch (Exception e2) {
                            	log.error(e2.getMessage(),e2);
                            }
                        }
                    }
                    return forwardError(ret.getSMessage());
                } else {
                    orderChannel.setStatus(TxnStatusType.Begin);
                }
            }
        }
        /** hotel 2.9.2 进入订单填写页面需要查询房型的附加信息，并传到界面做判断 add by chenjiajie 2009-07-23 begin **/
        super.fillRoomAppendInfo();
        /** hotel 2.9.2 进入订单填写页面需要查询房型的附加信息，并传到界面做判断 add by chenjiajie 2009-07-23 begin **/
        /** hotel 2.9.2 对不同type的取消修改规则返回不同的提示语add by guzhijie 2009-07-23 begin **/
        if((ChannelType.CHANNEL_ELONG+"").equals(channel)){
        	addModifyCancelStrr = bookhintCancelAndModifyStr;
        }else if (null != order.getReservation()
            && StringUtil.isValidStr(order.getReservation().getCancelModifyStr())) {
            List liRes = new ArrayList();
            liRes = orderEditService.getCancelModifyStrNew(order, order.getPayMethod(), order
                .getReservation().getCancelModifyStr());
            if (null != liRes && !liRes.isEmpty()) {
                addModifyCancelStrr = liRes.get(0).toString();
            }
        }
        /** hotel 2.9.2 对不同type的取消修改规则返回不同的提示语add by guzhijie 2009-07-23 end **/
        // v2.9.2 lixiaoyong 2009-08-13 促销信息选择需求 begin
        preSaleList = hotelReservationInfoService.queryRoomPresale(order);
        preSaleNum = preSaleList.size();
        // v2.9.2 lixiaoyong 2009-08-13 促销信息选择需求 end
        
        /** hotel2.9.3 代金券需要传递酒店相关基本信息 add by chenjiajie 2009-09-15 begin **/
        HtlHotel htlHotel = hotelService.findHotel(hotelId);
        //elong酒店级别提示信息
        if((ChannelType.CHANNEL_ELONG+"").equals(channel)) {
        	request.setAttribute("hotelAlertMsg", htlHotel.getAlertMessage());
        }
        request.setAttribute("htlHotel", htlHotel);
        /** hotel2.9.3 代金券需要传递酒店相关基本信息 add by chenjiajie 2009-09-15 end **/
        
        /** hotel2.9.3.1 优惠立减计算订单金额 注：在原有计算好的订单金额中计算优惠金额 add by chenjiajie 2009-10-15 begin **/
        if(1 == order.getFavourableFlag() && PayMethod.PRE_PAY.equals(order.getPayMethod())){
        	//计算订单的优惠总金额 返回支付酒店方式(1:底价,2:售价)
        	orderBenefitService.reCalculateBenefitAmount(rateMap,order);
        	//计算预订期间1间房的立减金额，用于页面计算使用
        	int benefitAmountRMBPerRoom = orderBenefitService.calculateBenefitAmount(String.valueOf(order.getChildRoomTypeId()), 
																	order.getCheckinDate(), 
																	order.getCheckoutDate(), 
																	1,
																	order.getPaymentCurrency());
        	request.setAttribute("benefitAmountRMBPerRoom", benefitAmountRMBPerRoom);
        }
        isEpOrder = epOrderManagerService.validateEpOrderByHotelId(String.valueOf(order.getHotelId()));
        /** hotel2.9.3.1 优惠立减计算订单金额 注：在原有计算好的订单金额中计算优惠金额 add by chenjiajie 2009-10-15 end **/
        return NEW_ORDER;
    }

    /**
     * 把艺龙的规则转过来放到order.reservation  add by luo
     * @param order
     * @param htlElAssure
     */
    private void setElongAssureToOrder(OrOrder order, HtlElAssure htlElAssure) {
    	OrReservation reservation = order.getReservation();
    	if(reserv == null){
    		reservation = new OrReservation();
    	}
    	   if(1 == htlElAssure.getAssureType()){
			   reservation.setUnCondition(true);
			   reservation.setOverTimeAssure(false);
			   reservation.setNightsAssure(false);
			   reservation.setRoomsAssure(false);
			   reservation.setNeedCredit(true);
			   reservation.setNights(0);
		   }else if(2 == htlElAssure.getAssureType()){
		   		reservation.setUnCondition(false);
		   		reservation.setOverTimeAssure(true);
		   		reservation.setNightsAssure(false);
				reservation.setRoomsAssure(false);	
				reservation.setNights(0);
		   		reservation.setLateSuretyTime(htlElAssure.getAssureDate());
	  		}else if(3 == htlElAssure.getAssureType()){
	  			reservation.setUnCondition(false);
	  			reservation.setOverTimeAssure(false);
	  			reservation.setRoomsAssure(true);
	  			reservation.setNightsAssure(false);
	  			reservation.setNights(0);
	  			reservation.setRooms(htlElAssure.getAssureRoomQuantity());
	  		}else if(5 == htlElAssure.getAssureType()){
	  			reservation.setUnCondition(false);
	  			reservation.setOverTimeAssure(true);
	  			reservation.setRoomsAssure(true);
	  			reservation.setNightsAssure(false);
	  			reservation.setNights(0);
	  			reservation.setLateSuretyTime(htlElAssure.getAssureDate());
	  			reservation.setRooms(htlElAssure.getAssureRoomQuantity());
	  		}
		   reservation.setReservSuretyPrice(Math.ceil(htlElAssure.getAssureAmount()*order.getRateId()));
		   
		   List<OrGuaranteeItem> guaranteeList = new ArrayList<OrGuaranteeItem>();
		   if(1 == htlElAssure.getMoneyType()){//首日担保
			   OrGuaranteeItem orGuaranteeItem = new OrGuaranteeItem();
	       		orGuaranteeItem.setAssureType("2");
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
	       		orGuaranteeItem.setNight(order.getCheckinDate());
	       		orGuaranteeItem.setReserv(reservation);
	       		guaranteeList.add(orGuaranteeItem);  
		   }else if(2 == htlElAssure.getMoneyType()){//全额
			   for(int i=0;i<DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());i++){
				   OrGuaranteeItem orGuaranteeItem = new OrGuaranteeItem();
				   orGuaranteeItem.setAssureType("4");
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
		       		orGuaranteeItem.setNight(DateUtil.getDate(order.getCheckinDate(),i));
		       		orGuaranteeItem.setReserv(reservation);
		       		guaranteeList.add(orGuaranteeItem);  
			   }
		   }
	       reservation.setGuarantees(guaranteeList);
	       
	       List<OrAssureItemEvery> assureList = new ArrayList<OrAssureItemEvery>();
	       OrAssureItemEvery orAssureItemEvery = new OrAssureItemEvery();
  		   orAssureItemEvery.setType(htlElAssure.getCancelAssureType());
  		   orAssureItemEvery.setFirstDateOrDays(htlElAssure.getAssureDateDay());
  		   orAssureItemEvery.setFirstTime(htlElAssure.getAssureSaveDate());
  		   orAssureItemEvery.setReserv(reservation);
  		   
  		   assureList.add(orAssureItemEvery);
  		   this.getSession().put("orAssureItemEvery", orAssureItemEvery);
	       reservation.setAssureList(assureList);
	       
	 	   order.setReservation(reservation);
	}



	/**
     * 手工单前台进入创建订单
     * 
     * @return
     */
    public String showUncontract() {

        Map params = super.getParams();
        member = getOnlineMember();
        if (null == member) {
            return forwardError("获取会员信息失败!请先登录会员，再下订单。");
        }

        // 如果是参考预定，把源订单的入住人信息、联系人信息等带过来
        if (StringUtil.isValidStr(oriOrderId)) {
            OrOrder oriOrder = getOrder(Long.valueOf(oriOrderId));
            order = new OrOrder();
            MyBeanUtil.copyProperties(order, params);

            if (oriOrder.getMemberId().longValue() != member.getId()) {
                member = getMemberInfo(oriOrder.getMemberCd());
                if (null == member) {
                    return forwardError("获取订单会员信息失败!");
                }
            }

            // copy源订单基本信息
            order.setLinkMan(oriOrder.getLinkMan());
            order.setTitle(oriOrder.getTitle());
            order.setMobile(oriOrder.getMobile());
            order.setTelephone(oriOrder.getTelephone());
            order.setCustomerFax(oriOrder.getCustomerFax());
            order.setEmail(oriOrder.getEmail());
            order.setConfirmType(oriOrder.getConfirmType());
            order.setSpecialRequest(oriOrder.getSpecialRequest());

            order.setOrderCDHotel(oriOrder.getOrderCDHotel());

            // copy源订单入住人信息
            List list = MyBeanUtil.copyCollection(oriOrder.getFellowList(), OrFellowInfo.class);
            order.setFellowList(list);

            order.setPaymentCurrency(oriOrder.getPaymentCurrency());
            request.setAttribute("systemHotelSign", "01");
        } else {
            order = new OrOrder();
            MyBeanUtil.copyProperties(order, params);
            String systemHotelSign = (String) params.get("systemHotelSign");
            request.setAttribute("systemHotelSign", systemHotelSign);
            if (StringUtil.isValidStr(systemHotelSign) && systemHotelSign.equals("01")) { // 系统内酒店
                order.setPaymentCurrency(orderService.getHotelSysCurrency(order.getHotelId()));
            }
        }

        // order.setType(OrderType.TYPE_MANGO);
        order.setType(member.isMango() ? OrderType.TYPE_MANGO : OrderType.TYPE_114);
        order.setCity((String) params.get("cityId"));
        order.setIsManualOrder(true);

        // 设置订单会员信息
        order.setMemberId(Long.valueOf(member.getId()));
        order.setMemberCd(member.getMembercd());
        order.setMemberName((null == member.getName() ? "" : member.getName()) + " "
            + (null == member.getFirstname() ? "" : member.getFirstname()) + " "
            + (null == member.getLastname() ? "" : member.getLastname()));

        List roomTypeList = hotelRoomTypeService.getHtlRoomTypeListByHotelId(order.getHotelId().longValue());
        if (null != roomTypeList && 0 < roomTypeList.size()) {
            request.setAttribute("roomTypeList", roomTypeList);
        } else {
            return forwardError("该酒店没有房型, 请先添加房型!");
        }

        request.setAttribute("oftenFellowCount", member.getFellowList().size());
        request.setAttribute("oftenLinkmanCount", member.getLinkmanList().size());

        request.setAttribute("fulResUrl", RESDETAIL_URL);

        if (member.isMango()) {
            OrOrderStatistics orderStat = orderService.getOrderStatByMemberCd(member.getMembercd());
            request.setAttribute("orderStat", orderStat);

            if (null != orderStat) {
                double price = orderStat.getAvgPrice();
                double star = orderStat.getAvgStar();
                mostStar = OrderUtil.getMostStar(star);
                mostPriceLevel = OrderUtil.getPriceLevel(price);
            }
        }
        return "new_order_uncontract";
    }

    
    /**
     * 处理交行全卡商旅等渠道下单的信息, 设置order的agentId属性 add by chenkeming
     */
    private void handleCooperate() {
        if("1".equals(forCooperate)) {
        	String projectCode = MemberAliasConstants.mapAlias2ProjectCode.get(member.getAliasid());
        	if(null != projectCode) {
        		order.setAgentid(projectCode);	
        	}        	
        }
    }
    
    /**
     * 前台创建订单Process, "下一步"
     * 
     * @return
     */
    public String process() {
         
    	
        // 防止重复提交
        if (isRepeatSubmit()) {
            return forwardMsg("请不要重复提交!");
        }
        
        String channelBak=channel;
        
        // 置空酒店查询条件的session
        putSession(SessionNames.QUERY_COND, null);

        // 是否重下新单
        String originCD = (String) getParams().get("originCD");
        boolean bIsRenew = StringUtil.isValidStr(originCD) && StringUtil.isValidStr(sRenew);
        OrOrder orderObj = null;

        // 获取会员
        member = getOnlineMember();
        String memberCd = (String) getParams().get("memberCd");
        if (null != member && StringUtil.isValidStr(memberCd)
            && !member.getMembercd().equals(memberCd)) {
            member = getMemberInfo(memberCd);
            if (null == member) {
                return forwardError("获取会员信息失败!");
            }
        } else if (null == member && bIsRenew) { // 处理未登录的情况下重下新单
            orderObj = orderService.getCustomOrderByOrderCD(originCD, null);
            if (null == orderObj) {
                return forwardMsg("原订单不存在!");
            }
            member = getMemberInfo(orderObj.getMemberCd());
        }

        // 获取权限用户
        roleUser = getOnlineRoleUser();
        taskId = getOnlineTaskId();

        // 填充页面数据到order对象
        order = new OrOrder();
        Map params = getParams();
        try {
            long lBegin = System.currentTimeMillis();
            // 将页面表单数据读入到订单当中
            populateOrder(order);
            order.setOrderCdForChannel(order.getOrderCdForChannel()!=null ? order.getOrderCdForChannel().trim() : "");
            order.setChannel(Integer.parseInt(oldChannel));
           
            //添加日志
            if(oldChannel!=null && !oldChannel.equals(channel)){
            	OrHandleLog handleLog = new OrHandleLog();
    	        handleLog.setModifierName(roleUser.getName());
    	        handleLog.setModifierRole(roleUser.getLoginName());
    	        String _channelName = "";
    	        switch(Integer.parseInt(channel)){
	    	        case 2 :_channelName="畅联";break;
	    	        case 5 :_channelName="格林豪泰";break;
	    	        case 6 :_channelName="锦江集团";break;
	    	        case 8 :_channelName="中旅";break;
	    	        case 9 :_channelName="艺龙";break;
    	        }
    	        handleLog.setContent("<font color='red'>合作方从 传统 改为 "+_channelName+" ！</font>");
    	        handleLog.setModifiedTime(new Date());
    	        handleLog.setOrder(order);
    	        order.getLogList().add(handleLog);
            }
            
            channel=oldChannel;
            
            
            //艺龙订单，只要填写了客人特色要求，前面添加务必前缀
            if(String.valueOf(ChannelType.CHANNEL_ELONG).equals(this.channel)
            		&& order.getSpecialRequest() != null 
            		&& !"".equals(order.getSpecialRequest())) {
            	order.setSpecialRequest("务必"+order.getSpecialRequest());
            }
            OrderUtil.updateStayInMid(order);
            log.debug("process-执行populateOrder : " + (System.currentTimeMillis() - lBegin) + "毫秒");
        } catch (BusinessException e) {
        	log.error(e.getMessage(),e);
            return forwardError("积分支付不成功!");
        }

        // 处理交行全卡商旅等渠道下单的信息, 设置order的agentId属性 add by chenkeming
        handleCooperate();
        
        // 订单币种处理
        String paymentCurrency = order.getPaymentCurrency();
        double rateDou = 1.0;
        if (null == paymentCurrency) {
            idCurStr = CurrencyBean.idCurMap.get(CurrencyBean.RMB);
            order.setPaymentCurrency(CurrencyBean.RMB);
        } else {
            idCurStr = CurrencyBean.idCurMap.get(paymentCurrency);
            if (null == idCurStr) {
                idCurStr = CurrencyBean.idCurMap.get(CurrencyBean.RMB);
            }
            String rateStr = CurrencyBean.rateMap.get(paymentCurrency);
            if (null == rateStr) {
                rateStr = "1.0";
            }
            rateDou = Double.valueOf(rateStr).doubleValue();
        }
        order.setRateId(rateDou);
        request.setAttribute("rateCurrency", order.getRateId());

        // 获取订单预订条款
        OrReservation reserv = new OrReservation();
        MyBeanUtil.copyProperties(reserv, params);
        order.setReservation(reserv);
        ElongAssureResult assureResult = null;
        if(order.getChannel() == ChannelType.CHANNEL_ELONG){//填充ELONG担保明细
       		assureResult = checkElongAssureService.checkIsAssure(order.getHotelId(), order.getChildRoomTypeId()
       				, order.getCheckinDate(), order.getCheckoutDate(), order.getRoomQuantity(), order.getArrivalTime());
       		if(assureResult.getAssureType()>0){
       			if(assureResult.getAssureMoneyType()==1){
       				OrGuaranteeItem item = new OrGuaranteeItem();
       				item.setNight(order.getCheckinDate());
       				item.setAssureCondiction(this.getGuaranteeAssureCondition(assureResult));
       				item.setAssureType("2");
       				item.setAssureLetter("否");
       				item.setReserv(reserv);
       				reserv.getGuarantees().add(item);
       			}else if(assureResult.getAssureMoneyType()==2){
       				List<Date> datelist = DateUtil.getDates(order.getCheckinDate(), DateUtil.getPreviousDate(order.getCheckoutDate()));
       				for(Date date:datelist){
       					OrGuaranteeItem item = new OrGuaranteeItem();
       					item.setNight(date);
           				item.setAssureCondiction(this.getGuaranteeAssureCondition(assureResult));
       					item.setAssureType("4");
           				item.setAssureLetter("否");
           				item.setReserv(reserv);
           				reserv.getGuarantees().add(item);
       				}
       			}
       		}
       	}else if (!order.isManualOrder()) {//其他订单， 有必要则获取取消修改规则信息
            if ((!order.isPrepayOrder() && reserv.isCanAssure()) || order.isPrepayOrder()) {
            	orderEditService.fillAssureItemEvery(order, (String) params.get("cancelModifyStr"));
                if (!order.isPrepayOrder()) { // 如果有必要则获取担保明细
                    OrderUtil.fillGuaranteeItem(reserv, params);
                }
            }
        }

        order.setOrderState(OrderState.HAS_SUBMIT);

        if (!order.isMango()) {
            order.setMemberState(member.getState());
        }

        // 获取早餐类型和早餐数量
        int difdays = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        int[] nBreakfast = new int[difdays];
        int[] nBreakNum = new int[difdays];
        for (int i = 0; i < difdays; i++) {
            String sBreakfast = (String) params.get("hBreakfast" + i);
            if (StringUtil.isValidStr(sBreakfast)) {
                nBreakfast[i] = Integer.parseInt(sBreakfast);
            } else {
                nBreakfast[i] = 0;
            }
            String sBreakfastNum = (String) params.get("hBreakNum" + i);
            if (StringUtil.isValidStr(sBreakfastNum)) {
                nBreakNum[i] = Integer.parseInt(sBreakfastNum);
            } else {
                nBreakNum[i] = 0;
            }
        }

        // 酒店v2.3 RMS1407
        // 中台重下的新单一定分配给重下该订单的中台操作人员。
        // （工作状态组别设置类型与订单类型不对应的，以及工作状态为close除外
        /** 中台订单流转优化后，分配模式改变，所以注释掉这段代码 by juesuchen 2010-3-30 begin**/
        if (bIsRenew) {
            if (null == orderObj) {
                orderObj = orderService.getCustomOrderByOrderCD(order.getOriginCD(), null);
            }
            if (null == orderObj) {
                return forwardMsg("原订单不存在!");
            }
            
            order.setSource(orderObj.getSource());
            order.setAgentid(orderObj.getAgentid());
            
            //标记订单为 醒狮计划促销活动
            orderService.updateExtInfoForWakeUp(order);
        }
            /*if (roleUser.isOrgMid()) {
                if (orderObj.isStayInMid()) {
                    user = getOnlineWorkStates();
                    if (null != user && user.getType() == WorkType.HRA && 1 == user.getState()) {
                        String hraType = "" + order.getHraOrderType();
                        String userGroups = user.getGroups();
                        if (0 <= userGroups.indexOf(hraType)
                            && !(0 <= userGroups.indexOf("9" + hraType))
                            || 0 <= userGroups.indexOf("," + hraType)) {
                            order.setAssignTo(user.getLogonId());
                            order.setAssignToName(user.getName());
                        }
                    }
                }
            }
        }*/
        /** 中台订单流转优化后，分配模式改变，所以注释掉这段代码 by juesuchen 2010-3-30 end**/
        /**
         * 获取渠道类型 hotel2.5
         */
        int channelType = 0;
        // 生产bug326 系统外订单下单报空指针异常 modify by chenjiajie 2009-04-03
        if (StringUtil.isValidStr(channel)) {
            channelType = Integer.valueOf(channel);
        }

        // 如果是重下新单,则对原订单进行撤单操作
        if (bIsRenew && 0 == channelType) {
            if (null == orderObj) {
                return forwardMsg("原订单不存在!");
            }
            if (!orderObj.isCancel()) { // 防止对已撤单进行重下新单操作
                orderService.cancelOrderByOrderCD(order.getOriginCD(), Integer
                    .parseInt(renewReason), renewMessage, guestRenewMessage, roleUser);
            }
        }

        String quotaType = (String) params.get("quotaType");
        long lBegin = System.currentTimeMillis();
        //代理打电话到cc下单，判断当前登录会员是否为一开代理
        String mcd = member.getMembercd();
        //判断它的渠道是否为代理
        if("9400100001".equals(member.getAliasid())){
        	order.setType(OrderType.TYPE_B2BAGENT);
        }else if(null!=mcd && orderService.isb2bMember(mcd)){
        	order.setType(OrderType.TYPE_B2BAGENT);
        	MemberDTO b2bMember= memberInterfaceService.getMemberByCode(mcd);
        	if(null != b2bMember){
        		order.setMemberId(b2bMember.getId());
        	}
        } 
        //代理add by zhijie.gu 2010-3-10 end
        if (!order.isManualOrder()) {
            // 获取配额
            order.setQuotaTypeOld(quotaType);
            if (!order.isCtsHK()) { // 非中旅单扣配额
                if (orderService.deductOrderQuota(order, nBreakfast, nBreakNum, quotaType)) {
                    // 获取配额成功
                    order.setQuotaOk(true);
                    orderService.calculateTotalAmount(order);
                } else { // 获取配额失败
                    order.setQuotaOk(false);
                    orderService.calculateTotalAmount(order);
                }
            } else { // v2.8 中旅单填充配额明细 add by chenkeming@2009-04-21
                orderService.getCtsOrderQuota(order, nBreakfast, nBreakNum, params);
                orderService.calculateTotalAmount(order);
                // 中旅单人民币总金额逢一进十
                order.setSumRmb(BigDecimal.valueOf(order.getSumRmb()).setScale(0,
                    BigDecimal.ROUND_UP).doubleValue());
            }
        } else { // 如果是手工单
            orderService.getManualOrderQuota(order, 0, 0);
            order.setQuotaOk(false);
        }
        log.debug("process-扣配额 : " + (System.currentTimeMillis() - lBegin) + "毫秒");

        // 初始化订单各种确认状态
        order.setCustomerConfirm(false);
        order.setHotelConfirm(false);
        order.setSendedHotelFax(false);
        order.setHotelConfirmFax(false);
        order.setHotelConfirmTel(false);
        order.setHotelConfirmFaxReturn(false);
        order.setSendedMemberFax(false);
        order.setTaskId(taskId);

        // 根据roleUser的中前台属性设置订单中前台修改时间
        if (roleUser.isOrgMid()) {
            order.setModifiedMidTime(new Date());
        } else if (roleUser.isOrgFront()) {
            order.setModifiedFrontTime(new Date());
        }

        // 获取房费另缴税和促销信息 v2.6 add by chenkeming
        if (!order.isManualOrder()) {
            if ("1".equals(isTaxCharges)) {
                orderService.getTaxCharge(order);
            }
            if ("1".equals(isSalesPromo)) {
                // hotelService.assumeOrderPresale(params, OrPreSale.class, preSaleNum,order);
                orderService.getPreSale(order);
            }
        }

        // 预付单则增加预付酒店金额
        if (order.isPrepayOrder()) {
            OrderUtil.addPrepayToHotel(order, roleUser);
        }

        // v2.8 如果是下中旅订单 add by chenkeming@2009-03-17
        if (!order.isManualOrder() && channelType == ChannelType.CHANNEL_CTS) {
        	
        	//如果中旅售价发生变化，并且确认以新价格下单，则记录日志
        	if(params.get("ctsChangePriceMsg")!=null && StringUtil.isValidStr(params.get("ctsChangePriceMsg").toString())){
	            OrHandleLog handleLog = new OrHandleLog();
	            handleLog.setModifierName(roleUser.getName());
	            handleLog.setModifierRole(roleUser.getLoginName());
	            handleLog.setBeforeState(order.getOrderState());
	            handleLog.setAfterState(order.getOrderState());
	            handleLog.setContent("<font color='red'>"+params.get("ctsChangePriceMsg").toString()+"</font>");
	            handleLog.setModifiedTime(new Date());
	            handleLog.setOrder(order);
	            order.getLogList().add(handleLog);
        	}
        	
            int totalRooms = order.getRoomQuantity();
            for (int i = 0; i < totalRooms; i++) {
                String sQty = (String) params.get("quantity" + i);
                if (!StringUtil.isValidStr(sQty)) {
                    break;
                }
                int nQty = Integer.parseInt(sQty);
                OrChannelNo orderChannel = new OrChannelNo();
                orderChannel.setQuantity(nQty);
                orderChannel.setOrderChannel((String) params.get("orderChannel" + i));
                orderChannel.setStatus(Integer.parseInt((String) params.get("status" + i)));
                orderChannel.setOrder(order);
                order.getChannelList().add(orderChannel);
            }
            // TODO: 备注要从简体->繁体
            //中旅订单，需要将酒店备注内容清空 modify by wupingxiang at 2012-9-12
            String remark = "";
            order.getRemark().setHotelRemark("");
            OrderUtil.fillCtsFellows(order);
            List<OrChannelNo> liC = order.getChannelList();
            boolean bSuc = true;
            for (OrChannelNo orderChannel : liC) {
                String sTxnNo = orderChannel.getOrderChannel();
                String[] names = orderChannel.getFellows().split("#");
                List<CustInfo> liCust = new ArrayList<CustInfo>();
                for (int i = 0; i < names.length; i++) {
                    CustInfo custom = new CustInfo();
                    custom.setSName(names[i]);
                    custom.setSPhone("");
                    liCust.add(custom);
                }
                BasicData ret = new BasicData();
                try {
                    if (null == remark) {
                        remark = "";
                    }
                    ret = hkService.saleAddCustInfo(sTxnNo, liCust, remark);
                    if (ret.getNRet() < ResultConstant.RESULT_SUCCESS) {
                        log.error("中旅订单添加入住人信息失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                            + orderChannel.getOrderChannel() + ", WebService错误代码: " + ret.getNRet()
                            + ", 错误信息:" + ret.getSMessage());
                        bSuc = false;
                    }
                } catch (Exception e) {
                    bSuc = false;
                    ret.setSMessage("中旅订单添加入住人信息出异常!");
                    log.error(e.getMessage(),e);
                }

                // 如果中旅订单添加入住人信息失败
                if (!bSuc) {
                    // 回滚释放之前hold的配额
                    for (OrChannelNo rollChannel : liC) {
                        int status = rollChannel.getStatus();
                        if (TxnStatusType.Begin == status || TxnStatusType.Commited == status) {
                            try {
                                BasicData retRoll = hkService.saleRollback(rollChannel
                                    .getOrderChannel());
                                if (retRoll.getNRet() < ResultConstant.RESULT_SUCCESS) {
                                    log.error("释放中旅订单配额失败, 芒果订单编号:" + order.getOrderCD()
                                        + ", 中旅订单号:" + rollChannel.getOrderChannel()
                                        + ", WebService错误代码: " + retRoll.getNRet() + ", 错误信息:"
                                        + retRoll.getSMessage());
                                }
                            } catch (Exception e2) {
                            	log.error(e2.getMessage(),e2);
                            }
                        }
                    }
                    return forwardError(ret.getSMessage());
                } else { // 添加入住人成功
                    orderChannel.setStatus(TxnStatusType.Begin);
                }
            }
        }
        
        
        //应对变价
        orderService.synchroSumPriceToOrder(order);
        
        
        //处理订单附加信息
        orderService.updateExtInfo(order,params);
        
        //new add by lgm 把艺龙担保提示和修改信息添加到extinfo
        if(order.getChannel() == ChannelType.CHANNEL_ELONG){
       		
       		if(assureResult!=null && assureResult.getAssureType()>0){
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

        
        
        // 保存订单
        lBegin = System.currentTimeMillis();
        Serializable sID = orderService.saveOrUpdate(order);
        String sOrderCD = orderService.getOrderCDByID(sID);

        String errMsg = "";
        
        //用来校验取消原单是否成功；
        String  reNewCannel= "";
        
        /**
         * 如果是直联酒店 (comment start) author: guojun 2008-12-10 15:08 modify: v2.8 中旅订单的情况要另外处理
         * chenkeming@2009-03-17
         */
        if (!order.isManualOrder() && 0 < channelType && channelType != ChannelType.CHANNEL_CTS) {
            // 获取resourceDescr.xml中的资源 add by chenjiajie V2.5 2009-01-23
            Map channelMap = resourceManagerA.getDescription("select_cooperator");
            MGExResult mgresult = null;
            boolean bSuc = false;
            AddExRoomOrderResponse addExRoomOrderResponse = null;
            if (0 < channelType) {
                // 如果渠道为非传统渠道
                AddExRoomOrderRequest addExRoomOrderRequest = new AddExRoomOrderRequest();
                addExRoomOrderRequest.setChannelType(Integer.valueOf(channel));
                addExRoomOrderRequest.setHotelId(order.getHotelId());
              //如果是艺龙的订单，设置ChainCode值为客户类型
                if(ChannelType.CHANNEL_ELONG == channelType)
                	addExRoomOrderRequest.setChainCode(order.getFellowList().get(0).getFellowNationality());
                else
                	addExRoomOrderRequest.setChainCode(String.valueOf(order.getType()));
                /*
                 * 渠道方 hotelCode
                 */
                MGExOrder exRoomOrder = new MGExOrder();
                // 直联酒店ChinaOnline可能需要使用的首日房价 add by chenjiajie V2.5@2009-02-02
                float colFirstDayPrice = Float.parseFloat((String) params.get("colFirstDayPrice"));
                exRoomOrder.setFirstDayPrice(colFirstDayPrice);
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
                exRoomOrder.setHotelnotes(order.getRemark().getHotelRemark());
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
                exRoomOrder.setOrderid(Long.valueOf(sID.toString()));
                exRoomOrder.setOrdercd(sOrderCD);
                exRoomOrder.setOrdercdhotel(sOrderCD);
                exRoomOrder.setHotelnotes(order.getRemark().getHotelRemark());
                if (null == order.getCreateDate()) {
                    exRoomOrder.setCreatedate(DateUtil.toStringByFormat(new Date(),
                        "yyyy-MM-dd HH:mm:ss"));
                } else {
                    exRoomOrder.setCreatedate(DateUtil.toStringByFormat(order.getCreateDate(),
                        "yyyy-MM-dd HH:mm:ss"));
                }
                
                List<OrOrderItem> orderItemList = order.getOrderItems();
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
                    //艺龙的使用临时配额 add 2012-2-2
                    if(ChannelType.CHANNEL_ELONG == channelType)
                    	orderItem.setQuotaType(QuotaType.TEMPQUOTA);
                    else
                    	orderItem.setQuotaType(QuotaType.GENERALQUOTA);
                    
                    mgExOrderItem.setQuantity(1);
                    mgExOrderItem.setSum(Double.valueOf(order.getSum()).floatValue());
                    mgExOrderItem.setNoteresult(orderItem.getNoteResult());
                    mgExOrderItem.setOrderid(Long.valueOf(sID.toString()));
                    mgExOrderItem.setOrderstate(String.valueOf(order.getOrderState()));
                    mgExOrderItem.setHotelconfirm(null);
                    mgExOrderItem.setHotelconfirmid(null);
                    exRoomOrder.getExOrderItems().add(mgExOrderItem);
                }
                addExRoomOrderRequest.setRoomOrder(exRoomOrder);
                //直连下单失败有撤销订单的操作，需要先保存订单生成订单id
                order.setOrderCD(sOrderCD);
                try {
                    addExRoomOrderResponse = hdlService.addExRoomOrder(addExRoomOrderRequest);
                } catch (Exception e) {
                	log.error("==========NewOrderAction.process()===hdlService.addExRoomOrder() exception : ",e);
                	order = getOrder(sID);
                	order.setCancelReason(88);
                    orderService.cancelOrder(order, OrderState.CANCEL, "接口异常！", "88", roleUser);
                    
                    
                    order = getOrder(sID);
                    OrHandleLog handleLog = new OrHandleLog();
                    handleLog.setModifierName(roleUser.getName());
                    handleLog.setModifierRole(roleUser.getLoginName());
                    handleLog.setBeforeState(order.getOrderState());
                    handleLog.setAfterState(order.getOrderState());
                    MGExResult  result  = addExRoomOrderResponse.getResult();
                    String message = "";
                    if(null != result && StringUtil.isValidStr(result.getMessage())){
                    	message = result.getMessage();
                    }
                    handleLog.setContent("<font color='red'>下合作方"+channelMap.get(String.valueOf(channelType))
                        + "订单失败!"+message+"</font>");
                    handleLog.setModifiedTime(new Date());
                    handleLog.setOrder(order);
                    order.getLogList().add(handleLog);
                    orderService.saveOrUpdate(order);
                    
                	String alertMessage = "<font color='red'>下合作方订单失败！</font>";
                	String[] btns = { "重新预订", "查看该订单" };
                    String[] urls = { "/order/hotelSearch!searchPre.action",
                           "/order/orderOperate!edit.action?orderId=" + order.getID() + "&isFromFront=1" };
                    return forwardMsgEx(alertMessage, btns, urls, false);
                }

                if (null != addExRoomOrderResponse) {
                    mgresult = addExRoomOrderResponse.getResult();
                    // 判断订单是否成功,1为成功,0为不成功
                    if (1 == mgresult.getValue()) {
                        bSuc = true;
                        // 订单如果成功,更新OrOrder的orderCdForChannel
                        orderService.updateOrdercdForChannel(sID.toString(), sOrderCD, mgresult
                            .getMessage());
                        /**
                         * 如果客人没有特殊要求,酒店默认成已经确认 否则客人的特殊要求需要和酒店确认一下,
                         */
                        String ordercdForChannel = mgresult.getMessage();
                        if (null == ordercdForChannel || "".equals(ordercdForChannel)) {
                        } else {
                            order.setOrderCdForChannel(mgresult.getMessage());
                        }
                        String strLog = "";
                        if ((null == order.getSpecialRequest()
                            || "".equals(order.getSpecialRequest()))  
                            && !order.isCreditAssured()) {
                            order.setQuotaOk(true);
                            order.setSendedHotelFax(true);
                            if(String.valueOf(ChannelType.CHANNEL_ELONG).equals(this.channel)) {
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
                        
                        //二次确定，取得二次确定标示，根据该标示在界面做不同的处理 add by diandian.hou 2010-10-25
                        flagSecondConfirm = addExRoomOrderResponse.getFlagSecondConfirm();
                        //  测试用的，正式用上面的，把下面一句删除
                        // flagSecondConfirm = true;
                        if(flagSecondConfirm){
                       	 order.setQuotaOk(true);
                            order.setSendedHotelFax(true);
                            order.setHotelConfirm(false);
                            order.setHotelConfirmFax(false);
                            order.setHotelConfirmTel(false);
                            order.setHotelConfirmFaxReturn(false);
                            strLog = "二次确认,已发送酒店确认";
                        }
                        
                        OrHandleLog handleLog = new OrHandleLog();
                        handleLog.setModifierName(roleUser.getName());
                        handleLog.setModifierRole(roleUser.getLoginName()); // 保存工号
                        handleLog.setBeforeState(order.getOrderState());
                        handleLog.setAfterState(order.getOrderState());
                        handleLog.setContent(strLog);
                        handleLog.setModifiedTime(new Date());
                        handleLog.setOrder(order);
                        order.getLogList().add(handleLog);

                        // v2.5 生成一条给酒店的确认记录 add by chenkeming@2009-04-08
                        OrOrderFax orderFax = new OrOrderFax();
                        orderFax.setChannel(ConfirmType.DIRECT);
                        orderFax.setType(HotelConfirmType.CONFIRM);
                        orderFax.setModelType(order.isMango() ? ModelType.MODEL_MANGO
                            : ModelType.MODEL_114);
                        orderFax.setSendMan(roleUser.getName());
                        orderFax.setSendTime(new Date());
                        orderFax.setSendSucceed(true);
                        orderFax.setIsConfirm(true);
                        orderFax.setConfirmNo(ordercdForChannel);
                        orderFax.setHotelReturn(true);
                        // 二次确定，需修改确认记录的状态 add by diandian.hou 2010-10-25
                        if(flagSecondConfirm){
                        	orderFax.setIsConfirm(false);
                        	orderFax.setHotelReturn(false);
                        }
                        orderFax.setValidConfirm(true);
                        orderFax.setHotelId(order.getHotelId());
                        orderFax.setOrder(order);
                        orderFax.setLogList(null);
                        order.getFaxList().add(orderFax);

                    } else {
                        // 直联订单失败的原因是合作方系统原因,
                        // order.setCancelMessage("89");
                        // 直联订单失败
                        order.setCancelReason(88);
                        orderService.cancelOrder(order, OrderState.CANCEL, mgresult.getMessage(),
                            "88", roleUser);
                    }
                }
            }

            /**
             * 如果直联酒店下单失败 create by guojun 2009-1-8 start
             */
            if (null != mgresult) {
                if (1 != mgresult.getValue()) {
                    errMsg += "下" + channelMap.get(String.valueOf(channelType))
                        + "的订单失败, 原因:" + mgresult.getMessage();
                    // v2.5 直连订单重下新单,如果下合作方失败,则增加发传真提示 add by chenkeming@2009-04-10
                    if (bIsRenew && 0 < channelType) {
                        errMsg += "。直连修改失败，请发送修改传真（邮件）至酒店";
                    }
                }
            }
            mgresult = null;
            /**
             * 如果直联酒店下单失败 create by guojun 2009-1-8 start
             */

            if (bIsRenew) {

                if (bSuc) {
                    // v2.5 先撤掉芒果原单 add by chenkeming@2009-04-10
                    if (null == orderObj) {
                        return forwardMsg("原订单不存在!");
                    }
                    if (!orderObj.isCancel()) { // 防止对已撤单进行重下新单操作
                        orderService.cancelOrderByOrderCD(order.getOriginCD(), Integer
                            .parseInt(renewReason), renewMessage, guestRenewMessage, roleUser);
                    }

                    CancelExRoomOrderResponse cancelExRoomOrderResponse = null;
                    /**
                     * 从合作方的订单改成传统渠道时，需要判定合作方订单的编号是否已经存在，并取消该单。 这里不能采用channel>0来判断
                     */
                    OrOrder orOrder = orderService.getOrOrderByOrderCd(originCD);
                    if (null != orOrder.getOrderCdForChannel()) {
                        CancelExRoomOrderRequest cancelExRoomOrderRequest = 
                            new CancelExRoomOrderRequest();
                        cancelExRoomOrderRequest.setChannelType(channelType);
                        cancelExRoomOrderRequest.setHotelId(order.getHotelId());
                        cancelExRoomOrderRequest.setChannelCode(String.valueOf(channelType));
                        cancelExRoomOrderRequest.setOrderId(Long.valueOf(oriOrderId));
                        cancelExRoomOrderRequest.setChainCode(null);
                        cancelExRoomOrderRequest
                            .setCancelReason("Cancel order because of customer!");
                        cancelExRoomOrderRequest
                            .setCancelMessage("Cancel order because of customer!");
                        try {
                            cancelExRoomOrderResponse = hdlService
                                .cancelExRoomOrder(cancelExRoomOrderRequest);
                        } catch (Exception e) {
                        	/**
                             * 如果直联酒店下单失败,将错误信息显示到页面上。
                             */
                        	 log.error("================NewOrderAction.process()  bIsRenew==hdlService.cancelExRoomOrder()= orderCd :"+originCD+"== exception : ",e);
                        	 reNewCannel = "新单"+sOrderCD+"下到合作方系统成功！<font color='red'>原单"+originCD+"取消异常，请发送取消传真或邮件至酒店！</font>";
                        	 OrHandleLog handleLog = new OrHandleLog();
                             handleLog.setModifierName(roleUser.getName());
                             handleLog.setModifierRole(roleUser.getLoginName());
                             handleLog.setBeforeState(order.getOrderState());
                             handleLog.setAfterState(order.getOrderState());
                             handleLog.setContent("<font color='red'>合作方订单取消失败！请发送取消传真或邮件至酒店！</font>");
                             handleLog.setModifiedTime(new Date());
                             handleLog.setOrder(orOrder);
                             List<OrHandleLog> listLog = new ArrayList<OrHandleLog>();
                             listLog.add(handleLog);
                             orOrder.setLogList(listLog);
                             orderService.saveOrUpdate(orOrder);
                        	 
                        }
                        if (null != cancelExRoomOrderResponse) {
                            // 取消订单错误mgresult = addExRoomOrderResponse.getResult();
                            mgresult = cancelExRoomOrderResponse.getResult();
                            if (1 == mgresult.getValue()) {
                                // 取消订单成功
                            } else {
                                // 取消订单失败
                            	if(null == mgresult){
                            		reNewCannel = "新单"+sOrderCD+"下到合作方系统成功！<font color='red'>原单"+originCD+"取消异常，原因：接口异常！请发送取消传真或邮件至酒店！</font> ";
                            	}else{
                            		reNewCannel = "新单"+sOrderCD+"下到合作方系统成功！<font color='red'>原单"+originCD+"取消异常，原因："+mgresult.getMessage()+"</font>";
                            	}
                            }
                        }
                        if(StringUtil.isValidStr(reNewCannel)){
                             order = getOrder(sID);
                        	 OrHandleLog handleLog = new OrHandleLog();
                             handleLog.setModifierName(roleUser.getName());
                             handleLog.setModifierRole(roleUser.getLoginName());
                             handleLog.setBeforeState(order.getOrderState());
                             handleLog.setAfterState(order.getOrderState());
                             handleLog.setContent(reNewCannel);
                             handleLog.setModifiedTime(new Date());
                             handleLog.setOrder(order);
                             order.getLogList().add(handleLog);
                        }
                    }
                }
            }

            /**
             * 如果直联酒店取消订单失败 create by guojun 2009-1-8 start 取消合作方订单失败，则增加操作日志 modify by
             * chenkeming@2009-04-21
             */
            if (null != mgresult) {
                if (1 != mgresult.getValue()) {
                    String sMsg = channelMap.get(String.valueOf(channelType)) + "订单取消失败:"
                        + mgresult.getMessage();
                    errMsg += sMsg;
                    OrHandleLog handleLog = new OrHandleLog();
                    handleLog.setModifierName(roleUser.getName());
                    handleLog.setModifierRole(roleUser.getLoginName());
                    handleLog.setBeforeState(order.getOrderState());
                    handleLog.setAfterState(order.getOrderState());
                    handleLog.setContent(sMsg);
                    handleLog.setModifiedTime(new Date());
                    handleLog.setOrder(order);
                    order.getLogList().add(handleLog);
                }
            }
        }
        /**
         * 如果直联酒店取消订单失败 create by guojun 2009-1-8 start
         */

        log.debug("process-saveorUpdate : " + (System.currentTimeMillis() - lBegin) + "毫秒");

        boolean bSaveAgain = false;
        
        String sid = orderService.getOrderExtInfoByType(order, BaseConstant.ORDER_EXTINFO_SID);

        // 视情况创建预授权工单(预付单有信用卡支付方式或者面付担保单)
        if (this.order.isPrepayOrder() && this.isNotifyBalance()) {
            // 预付单
            List<OrPayment> payments = order.getPaymentList();
            for (int i = 0; null != payments && i < payments.size(); i++) {
                OrPayment payment = payments.get(i);
                if (payment.isNeedCreditCard()) {
                    String orderCode = sOrderCD;
                    double preAuthAmount = payment.getMoney();
                    String loginName = roleUser.getLoginName();
                    String creditCardIds = order.getCreditCardIdsSelect();
                    String description = "酒店前台生成";
                    String prePayType = "1";
                    try {
						String result = this.creditcardPreAuthService
								.createPreAuthList(orderCode, "HOTEL",
										preAuthAmount, member.getMembercd(), loginName,
										creditCardIds, sid, description, prePayType,order.getActualPayCurrency());
                        if (result.equals("succeed") || result.equals("orderCd existed")) {
                            order.setSuretyState(GuaranteeState.PREAUTH);
                        } else {
                            order.setSuretyState(GuaranteeState.PROCESSING);
                        }
                        bSaveAgain = true;
                    } catch (Exception e) {
                    	log.error(e.getMessage(),e);
                    }
                }
            }
        } else if (bCanCreatePreAuth && order.canPreAuth()
            && StringUtil.isValidStr(order.getCreditCardIdsSelect())) {
            // 面付单
            try {
				String isSucceed = creditcardPreAuthService.createPreAuthList(
						sOrderCD, "HOTEL", (double) order.getSuretyPrice(),
						member.getMembercd(), roleUser.getLoginName(), order
                        .getCreditCardIdsSelect(), sid, "酒店前台生成", "2",order.getActualPayCurrency());
                if (isSucceed.equals("succeed") || isSucceed.equals("orderCd existed")) {
                    order.setSuretyState(GuaranteeState.PREAUTH);
                    OrderUtil.addPreAuthCard(order, params);
                } else {
                    order.setSuretyState(GuaranteeState.PROCESSING);
                }
                bSaveAgain = true;
            } catch (Exception re) {
            	log.error(re.getMessage(),re);
                errMsg += " 创建预授权工单失败!";
            }
        }

        // 视情况创建配送单
        if (bCanCreateFul) {
            if (!orderAssist.createDeliveryBill(order, member, roleUser, sOrderCD)) {
                order.setFulfillmentCD(null);
                errMsg += " 创建配送单失败!";
            }
            bSaveAgain = true;
        }

        // 视情况扣会员积分
        if (member.isMango() && order.isIncludePtPrepay()) {
            List paymentList = order.getPaymentList();
            for (int i = 0; i < paymentList.size(); i++) {
                OrPayment payment = (OrPayment) paymentList.get(i);
                if (payment.isPoints()) {
                    double points = payment.getMoney() * 100;
                    PointDTO pt = null;
                    try {
                    	if (order.getSource() != null
    							&& order.getSource().equals(
    									OrderSource.FROM_WEB)) {
                    		pt = pointsDelegate.changePonitsByMemberCd(
                    				member.getMembercd(),
                            		"mango", "1", points, sOrderCD,
    								BaseConstant.TRANSCHANNEL_NET,
    								BaseConstant.TRANSCHANNELSN_NET);
    					} else if (order.getSource() != null
    							&& order.getSource().equals(
    									OrderSource.FAN_TI_NET)) {
    						pt = pointsDelegate.changePonitsByMemberCd(
    								member.getMembercd(),
                            		"mango", "1", points, sOrderCD,
    								BaseConstant.TRANSCHANNEL_NET,
    								BaseConstant.TRANSCHANNELSN_BIG);
    					} else {
    						pt = pointsDelegate.changePonitsByMemberCd(
    								member.getMembercd(),
                            		"mango", "1", points, sOrderCD,
    								BaseConstant.TRANSCHANNEL_CC,
    								BaseConstant.TRANSCHANNELSN_CC);
    					}
                    } catch (Exception e) {    					
                    	log.error(e.getMessage(),e);
                        errMsg += " 积分支付失败!";
                        
                        OrHandleLog handleLog = new OrHandleLog();
        				handleLog.setModifiedTime(new Date());
        				handleLog.setContent("CC选择积分支付，积分支付失败！");
        				handleLog.setModifierName("CC");
        				handleLog.setOrder(order);
        				order.getLogList().add(handleLog);
        				
                        break;
                    }
                    if (null != pt && StringUtil.isValidStr(pt.getRc()) && pt.getRc().equals("0")) {
                        long extPt = Long.parseLong(member.getPoint().getBalanceValue());
                        extPt -= BigDecimal.valueOf(points).longValue();
                        member.getPoint().setBalanceValue(String.valueOf(extPt));
                    } else {
                        errMsg += " 积分支付失败!";
                        break;
                    }
                    payment.setPaySucceed(true);
                    payment.setConfirmer(roleUser.getName());
                    payment.setConfirmTime(new Date());
                    if (OrderUtil.checkHasPrepayed(order)) {
                        order.setHasPrepayed(true);
                        order.setOrderState(OrderState.HAS_PAID);
                        OrderUtil.updateStayInMid(order);
                        // 中旅单积分全额支付成功后的处理
                        if (order.isCtsHK()) {
                            OrHandleLog handleLog = new OrHandleLog();
                            handleLog.setOrder(order);
                            handleLog.setModifiedTime(new Date());
                            handleLog.setModifierName(roleUser.getName());
                            handleLog.setModifierRole(roleUser.getLoginName());
                            order.getLogList().add(handleLog);
                            orderService.getPayFinishCts(order, handleLog, true);
                        }
                    }
                    bSaveAgain = true;
                    break;
                }
            }
        }
        
        /** 视情况是否调用代金券接口 hotel2.9.3 add by chenjiajie 2009-09-02 begin **/
        if(order.isIncludeCouponPrepay() && order.getCouponRecords() != null){
        	if(StringUtil.isValidStr(sOrderCD)){
        		order.setOrderCD(sOrderCD);
        	}
        	try {
        		//调用代金券接口试预订确认接口
				int vchResult = voucherInterfaceService.callVchServicePreOrder(order, roleUser, member, IVoucherInterfaceService.CHANNEL_CC);	
				//调用接口内部返回出错
				if(vchResult == 0){
					errMsg += " 代金券支付失败!原因：接口内部错误。";
					log.info(" vch003 代金券支付失败!原因：接口内部错误。OrderCD: "+sOrderCD);
				}else{
                    bSaveAgain = true;
				}
			} catch (VCHException e) {
				voucherInterfaceService.rollBackVchOrderState(order, e.getMessage());
                errMsg += " 代金券支付失败!原因："+e.getMessage();
                log.error(" vch004 代金券支付失败!原因："+e.getMessage()+" OrderCD: "+sOrderCD);
			}
        }
        /** 视情况是否调用代金券接口 hotel2.9.3 add by chenjiajie 2009-09-02 end **/

        /**
		 * 现金返还  add by linpeng.fang 2010-10-22
		 * 如果原单有返现， 并且撤单非客人原因，则新单根据规则返现
		 */
		if (bIsRenew) {
			if (null == orderObj) {
				return forwardMsg("原订单不存在!");
			}	
			boolean oriHasReturn = returnService.isOrderHasCashReturn(orderObj.getOrderCD());
			
			if (oriHasReturn && orderObj.getCancelReason() != 1 && orderObj.getCancelReason() != 4) {
				int payMethod = PayMethod.PAY.equals(order.getPayMethod()) ? 1 : 2; 
				if(order.isPayToPrepay()){
					payMethod = 1;
				}
                int totalCashReturnAmount = 0;
				for(int index=0; index < order.getOrderItems().size();index++){
					OrOrderItem item = order.getOrderItems().get(index);
					if(item.getSalePrice() > 0){
					    HtlPrice htlPrice = hotelService.qryHtlPriceForCC(order.getChildRoomTypeId(), item.getNight(), payMethod==1?PayMethod.PAY:PayMethod.PRE_PAY, "");
					    if(null!=htlPrice){
					    	BigDecimal price = returnService.calculateRoomTypePrice(htlPrice.getFormulaId(), new BigDecimal(htlPrice.getCommission()), new BigDecimal(htlPrice.getCommissionRate()), new BigDecimal(item.getSalePrice()));
					    	int cashReturnAmount = 0; 
					    	
					    	if (order.isCtsHK()) {
					    		//设置俑金---------------add by longkangfu-------2012-7-3----
								item.setCommission(item.getSalePrice()-item.getBasePrice());
					    	}
					    	
					    	//计算限量返现
							cashReturnAmount=limitFavourableManage.calculateCashLimitReturnAmount(order.getHotelId(), order.getChildRoomTypeId(), 
									item.getNight(), htlPrice.getCurrency(), new BigDecimal(item.getSalePrice()), item.getCommission());
							
							////如果没有限量返现，再计算普通返现，如果有，则不计算普通返现
							if(cashReturnAmount==-1){
								cashReturnAmount = returnService.calculateCashReturnAmount(order.getChildRoomTypeId(), item.getNight(), payMethod, htlPrice.getCurrency(), 1, price);
							}
						   item.setCashReturnAmount(cashReturnAmount);
						   totalCashReturnAmount += cashReturnAmount;
						   order.getOrderItems().set(index, item);
					    }
					}
				}
				order.setCashBackTotal(totalCashReturnAmount);	
				saveCashInformation(order,sOrderCD);
				
			}
		}
        
        // 系统自动判断是否满足即时确认条件
        if (OrderUtil.popDialogBox(order, params)) {
            request.setAttribute("popDialogBox", "show");
            order.setInstantConfirm(true);
            bSaveAgain = true;
        } else {
            request.setAttribute("popDialogBox", "noShow");
        }

        // 如果有必要更新订单
        if (order.isCreditAssured()) { // 担保单插入担保金额
            orderEditService.newOrderMoney(order, roleUser, sysAssureMoney
                * order.getRoomQuantity());// 比较金额时得*房间数量
        } else if (bSaveAgain) {
        	try{
        		orderService.saveOrUpdate(order);
        	}catch(Exception e){
        		log.error("新下单发生异常！",e);
        	}
            
        }

        // 设置订单前台操作session
        putSession("isFromFront", "1");

        // 如果以上调用ejb接口操作有错误
        if (0 < errMsg.length()) {
            String alertMessage = "订单创建成功!" + errMsg;
            /**
             * 如果直联酒店下单失败,将错误信息显示到页面上。
             */
            if (0 != channelType) {
                alertMessage = errMsg;
            }
            String[] btns = { "重新预订", "修改该订单" };
            
            String queryPort="";
            String url = "/order/hotelSearch!searchPre.action" ;

        	Cookie cookies[]=request.getCookies();
        	for(int i=0;i<cookies.length;i++){    	
        		if(cookies[i].getName().equals("queryPort")){
        		 queryPort=cookies[i].getValue();	
        		}
        	}
             if(null!=queryPort && queryPort.equals("New_Query")){
            	url = "/order/hotelSearch!searchPreNew.action" ;
            }
                
             String[] urls = { url,"/order/orderOperate!edit.action?orderId=" + order.getID() + "&isFromFront=1" };         
            return forwardMsgEx(alertMessage, btns, urls, false);
        }
        
        //取消原单失败！
        request.setAttribute("reNewCannel", reNewCannel);
         
        request.setAttribute("sOrderCD", sOrderCD);

        // 设置会员信息,供下单成功页面使用
        if (member.isMango()) {
            OrOrderStatistics orderStat = orderService.getOrderStatByMemberCd(member.getMembercd());
            request.setAttribute("orderStat", orderStat);

            if (null != orderStat) {
                double price = orderStat.getAvgPrice();
                double star = orderStat.getAvgStar();
                mostStar = OrderUtil.getMostStar(star);
                mostPriceLevel = OrderUtil.getPriceLevel(price);
            }
        }

        // 获取酒店默认联系方式,供下单成功页面使用
        ctcttype = hotelService.getHotelSendType(order.getHotelId(),order.getChildRoomTypeId());

        //RMS3007底价/售价的操作日志 add by chenjiajie 2010-01-04
        logShowBasePrice();
      //记录渠道订单变化
        if(channelBak!=null && !channelBak.equals(channel)){
        	HtlOrderChannel htlOrderChannel = new HtlOrderChannel();
            htlOrderChannel.setOrderId(order.getID());
            htlOrderChannel.setChannel(channelBak);
            htlOrderChannel.setCreateName(roleUser.getLoginName());
            htlOrderChannelService.saveHtlOrderChannel(htlOrderChannel);
        }   
        //在此添加EP酒店确认信息
        isEpOrder = epOrderManagerService.validateEpOrderByHotelId(String.valueOf(order.getHotelId()));
         if("1".equals(isEpOrder)){ 
        	//进入该程序段 酒店将显示在EP上！
            OrOrderFax orderFax = new OrOrderFax();
            orderFax.setChannel(ConfirmType.EBOOKING);
            orderFax.setType(HotelConfirmType.CONFIRM);
            orderFax.setModelType(order.isMango() ? ModelType.MODEL_MANGO
                : ModelType.MODEL_114);
            orderFax.setSendMan(roleUser.getName());
            orderFax.setSendTime(new Date());
            orderFax.setSendTarget("");
            orderFax.setUnicallRetId("");
            orderFax.setSendSucceed(true);
            orderFax.setHotelId(order.getHotelId());
            orderFax.setOrder(order);
            orderFax.setLogList(null);
            order.getFaxList().add(orderFax);
        	orderService.saveOrUpdate(order);
         }
        return PROCESS_ORDER;
    }

    private String getGuaranteeAssureCondition(ElongAssureResult assureResult) {
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



	/**
     * 保存返现相关信息
     * @param order
     * @param hotelOrderFromBean
     * @param member
     */
    public void saveCashInformation(OrOrder order,String orderCD){
    	if( order.getCashBackTotal()>0){
    		FITOrderCash fitCash = new FITOrderCash();
    		fitCash.setMemberCd(order.getMemberCd());
    		fitCash.setOrderCd(orderCD);
    		fitCash.setReturnCash(order.getCashBackTotal());
    	
    		Map<String,Double> cashMap = returnService.fillCashItem(order.getChildRoomTypeId(), order.isPayToPrepay()?PayMethod.PAY:order.getPayMethod(), order.getCheckinDate(), order.getCheckoutDate());
    		
    		List<OrOrderItem> orderItems = order.getOrderItems();
    		List<FITCashItem> cashItems = new ArrayList<FITCashItem>();
    		for(OrOrderItem orderItem : orderItems){
    			FITCashItem cashItem = new FITCashItem();
    			cashItem.setOrderCd(orderCD);
    			cashItem.setReturnCash(orderItem.getCashReturnAmount());
    			cashItem.setReturnDate(orderItem.getNight());
    			cashItem.setReturnScale(cashMap.get(DateUtil.formatDateToSQLString(orderItem.getNight()))==null ? 0 : cashMap.get(DateUtil.formatDateToSQLString(orderItem.getNight())));
    			
    			cashItems.add(cashItem);
    		}
    		returnService.saveCashInformation(fitCash, cashItems);
    	}    	
    }

    /**
     * 提交到中台 并回到预订页面
     * 
     * @return
     */
    public String submitToMid() {

        // 防止重复提交
        if (isRepeatSubmit()) {
            return forwardMsg("请不要重复提交!");
        }

        /**
         * 生产bug934订单是已付款状态，如果提交中台会改成了提交中台状态 modify by chenjiajie 2009-11-02
         * 这里取订单不再load的方法，使用getCustomOrderForMail()是为了重新到数据库取数据，保证订单状态的一致
         */
        order = orderService.getCustomOrderForMail(orderId);
        int oriOrderState = order.getOrderState();
        if(OrderState.HAS_PAID != order.getOrderState()){
            order.setOrderState(OrderState.SUBMIT_TO_MID);
        }

        OrderUtil.updateStayInMid(order);

        StringBuffer strCmp = new StringBuffer("").append("订单改为:<font color='red'>" + "已提交中台"
            + "</font><br>");
        roleUser = getOnlineRoleUser();
        OrHandleLog handleLog = new OrHandleLog();
        // handleLog.setModifier(new Long(roleUser.getId()));
        handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName()); // 保存工号
        handleLog.setBeforeState(oriOrderState);
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
        order.setModifiedTime(new Date());

        saveOrUpdateOrder(order);
        member = getOrderMember(order);

        // ////////////////会员信息
        if (member.isMango()) {
            OrOrderStatistics orderStat =orderService.getOrderStatByMemberCd(member.getMembercd());
            request.setAttribute("orderStat", orderStat);

            if (null != orderStat) {
                double price = orderStat.getAvgPrice();
                double star = orderStat.getAvgStar();
                mostStar = OrderUtil.getMostStar(star);
                mostPriceLevel = OrderUtil.getPriceLevel(price);
            }
        }

        // 币种处理
        String paymentCurrency = order.getPaymentCurrency();
        if (null == paymentCurrency) {
            idCurStr = CurrencyBean.idCurMap.get(CurrencyBean.RMB);
        } else {
            idCurStr = CurrencyBean.idCurMap.get(paymentCurrency);
            if (null == idCurStr) {
                idCurStr = CurrencyBean.idCurMap.get(CurrencyBean.RMB);
            }
        }

        ctcttype = hotelService.getHotelSendType(order.getHotelId(),order.getChildRoomTypeId());

        return PROCESS_ORDER;
    }

    /**
     * 判断是否需要通知结算组。
     * 
     * 说明： <li>（1）预付单指信用卡支付。 <li>（2）面付单指信用卡担保。
     * 
     * @return 是否需要通知结算组。
     */
    private boolean isNotifyBalance() {
        Map parameters = this.getParams();
        String isNotifyBalance = (String) parameters.get("isNotifyBalance");
        boolean bNotifyBalance = StringUtil.isValidStr(isNotifyBalance)
            && isNotifyBalance.equals("1") ? true : false;
        return bNotifyBalance;
    }

    // 获取另缴税信息
    public List lstRoomTaxCharge(OrOrder order) {

        List lstTaxCharge = orderService.getTaxCharges(order.getHotelId(), order.getCheckinDate(),
            order.getCheckoutDate());
        List lstRoomTaxCharge = new ArrayList();
        if (null != lstTaxCharge) {
            for (int i = 0; i < lstTaxCharge.size(); i++) {
                HtlTaxCharge taxCharge = (HtlTaxCharge) lstTaxCharge.get(i);
                if (null != taxCharge.getRoomIncTax()) {
                    lstRoomTaxCharge.add(taxCharge);
                    continue;
                }
            }
        }
        return lstRoomTaxCharge;
    }
    
    //校验中旅价格和配额
    public String checkCTS(){
    	Map params = getParams();
    	OrOrder order=new OrOrder();
    	order.setHotelId(Long.valueOf((String)params.get("hotelId")));
    	order.setRoomTypeId(Long.valueOf((String)params.get("roomTypeId")));
    	order.setChildRoomTypeId(Long.valueOf((String)params.get("childRoomTypeId")));
    	String checkInDate=(String)params.get("checkinDate");
    	String checkOutDate=(String)params.get("checkoutDate");
    	order.setCheckinDate(DateUtil.getDate(checkInDate));
    	order.setCheckoutDate(DateUtil.getDate(checkOutDate));
    	order.setRoomQuantity(Integer.valueOf((String)params.get("roomQuantity")));
    	
    	int days=DateUtil.getDay(DateUtil.getDate(checkInDate), DateUtil.getDate(checkOutDate));
    	for(int i=0;i<days;i++){
    		double salePrice = Double.valueOf((String) params.get("salePrice" + i));
            double basePrice = Double.valueOf((String) params.get("basePrice" + i));
            OrOrderItem orderItem=new OrOrderItem();
            orderItem.setDayIndex(i);
            orderItem.setSalePrice(salePrice);
            orderItem.setBasePrice(basePrice);
            order.getOrderItems().add(orderItem);
    	}
    	
    	List<MGExReservItem> changePriceList=new ArrayList<MGExReservItem>();
    	
    	//判断是否代理
    	Boolean isb2bMember=false;
    	
    	// 是否重下新单
        String originCD = (String) getParams().get("originCD");
        boolean bIsRenew = StringUtil.isValidStr(originCD) && StringUtil.isValidStr(sRenew);
        OrOrder orderObj = null;
    	// 获取会员
        member = getOnlineMember();
        String memberCd = (String) getParams().get("memberCd");
        if (null != member && StringUtil.isValidStr(memberCd)
            && !member.getMembercd().equals(memberCd)) {
            member = getMemberInfo(memberCd);
            if (null == member) {
                return forwardError("获取会员信息失败!");
            }
        } else if (null == member && bIsRenew) { // 处理未登录的情况下重下新单
            orderObj = orderService.getCustomOrderByOrderCD(originCD, null);
            if (null == orderObj) {
                return forwardMsg("原订单不存在!");
            }
            member = getMemberInfo(orderObj.getMemberCd());
        }
    	
    	//代理打电话到cc下单，判断当前登录会员是否为一开代理
        String mcd = member.getMembercd();
        //判断它的渠道是否为代理
        if("9400100001".equals(member.getAliasid())){
        	isb2bMember=true;
        }else if(null!=mcd && orderService.isb2bMember(mcd)){
        	isb2bMember=true;
        }
		
		//校验中旅配额和价格
    	Result result=checkCTSQuotaAndPrice(order,changePriceList,isb2bMember);
    	
    	Document document = DocumentHelper.createDocument();
    	Element root=document.addElement("result");
    	
    	Element type=root.addElement("checkCTSResultType");
    	Element message=root.addElement("resultMessage");
    	
    	if(result.getValue()==-1){
    		type.setText("error");
    		message.setText(result.getMessage());
    	}else if(result.getValue()==-2){
    		type.setText("quota");
    		message.setText(result.getMessage());
    	}else if(result.getValue()==-3){
    		type.setText("price");
    		message.setText(result.getMessage());
    		
    		Element changePriceListElement=root.addElement("changePriceList");
    		
    		for(int i=0;i<changePriceList.size();i++){
    			Element priceItem=changePriceListElement.addElement("item");
    			Element dayIndex=priceItem.addElement("dayIndex");
    			dayIndex.setText(String.valueOf(changePriceList.get(i).getDayIndex()));
    			Element basePrice=priceItem.addElement("basePrice");
    			basePrice.setText(String.valueOf(changePriceList.get(i).getBasePrice()));
    			Element salePrice=priceItem.addElement("salePrice");
    			salePrice.setText(String.valueOf(changePriceList.get(i).getSalePrice()));
    		}
    	}else if(result.getValue()==1){
    		type.setText("success");
    		message.setText(result.getMessage());
    	}
    	String context=document.asXML();
    	try
    	 {
    		getHttpResponse().setContentType("text/xml;charset=utf-8");
    		getHttpResponse().setCharacterEncoding("utf-8");
    		getHttpResponse().setHeader("cache-control", "no-cache");
    		PrintWriter out = getHttpResponse().getWriter(); 
    	    out.print(context);
    	    out.flush();
    	    out.close();
    	 }catch(Exception e){
    		 log.error("HttpResponse PrintWriter 出错:",e);
    	 }
    	return null;
    }
    
    //校验中旅配额和价格
    private Result checkCTSQuotaAndPrice(OrOrder order,List<MGExReservItem> changePriceList,Boolean isb2bMember){
    	Result result=new Result();
    	result.setValue(1);
    	result.setMessage("");
    	CheckHotelReservateExRequest checkHotelRequest = new CheckHotelReservateExRequest();
    	checkHotelRequest.setHotelId(order.getHotelId());
    	checkHotelRequest.setChannelType(8);
    	checkHotelRequest.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
    	checkHotelRequest.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
    	checkHotelRequest.setRoomTypes(String.valueOf(order.getRoomTypeId()));
    	checkHotelRequest.setChildRoomTypes(String.valueOf(order.getChildRoomTypeId()));
    	checkHotelRequest.setChainCode(null);
    	checkHotelRequest.setQuantity(1);
    	CheckHotelReservateExResponse checkHotelResponse = new CheckHotelReservateExResponse();
    	try{
    		checkHotelResponse = hdlService.checkHotelReservate(checkHotelRequest);
    	}catch(Exception e){
    		log.error("=====中旅订单配额和价格校验失败，调HDL异常===hdlService.checkHotelReservateFromQuery exception: ",e);
    		result.setValue(-1);
    		result.setMessage("中旅订单配额和价格校验失败，调HDL异常");
    		return result;
    	}
    	
    	if(checkHotelResponse==null || checkHotelResponse.getRoomTypes()==null){
    		log.info("=====中旅订单配额和价格校验失败，调HDL，返回失败=====");
    		result.setValue(-1);
    		result.setMessage("中旅订单配额和价格校验失败，调HDL异常");
    		return result;
		}
		
		for(CheckRoomTypeResponse roomType:checkHotelResponse.getRoomTypes()){
			if(Long.valueOf(roomType.getRoomTypeId()).longValue()==order.getRoomTypeId().longValue()){
				if(roomType.getRoomTypeRespItems()==null){
					result.setValue(-1);
					result.setMessage("中旅配额校验失败");
					return result;
				}
				for(CheckRoomTypeResponseItem item:roomType.getRoomTypeRespItems()){
					String date=DateUtil.dateToString(DateUtil.getDate(order.getCheckinDate(),item.getDayIndex()));
					if(item.getResult().getValue() == -1){
						result.setValue(-2);
						result.setMessage(result.getMessage() + date + "（无配额）;");
						continue;
					}else if(item.getResult().getValue() == -2){
						result.setValue(-2);
						result.setMessage(result.getMessage() + date + "（配额过期）;");
						continue;
					}
					if(item.getQty() < order.getRoomQuantity()){
						result.setValue(-2);
						result.setMessage(result.getMessage() + date+"（剩余"+item.getQty()+"间）;");
						continue;
					}
				}
			}
		}
		
		if(result.getValue()!=1){
    		return result;
    	}
		
		if(checkHotelResponse.getPriceTypeResponse()==null){
			result.setValue(-1);
    		result.setMessage("中旅订单价格校验失败，调HDL异常");
    		return result;
		}
		for(CheckPriceTypeResponse priceType:checkHotelResponse.getPriceTypeResponse()){
			if(Long.valueOf(priceType.getChildRoomTypeId()).longValue()==order.getChildRoomTypeId().longValue()){
				if(priceType.getReservItems()==null){
					result.setValue(-1);
		    		result.setMessage("中旅订单价格校验失败，调HDL异常");
		    		return result;
				}
				for(MGExReservItem item:priceType.getReservItems()){
					String date=DateUtil.dateToString(DateUtil.getDate(order.getCheckinDate(),item.getDayIndex()));
					
					for(OrOrderItem orderItem:order.getOrderItems()){
						if(orderItem.getDayIndex()==item.getDayIndex()){
							//判断是否代理,如果是则校验底价
							if(isb2bMember){
								double orderItemPrice=Math.ceil(orderItem.getBasePrice());
								double refreshItemPrice=Math.ceil(item.getBasePrice());
								if(orderItemPrice != refreshItemPrice){
									result.setValue(-3);
						    		
									double orderItemRmb=0;
						    		Map<String, String> rateMap = CurrencyBean.rateMap;
						            if (null != rateMap) {
						            	String rateStr = rateMap.get(CurrencyBean.HKD);
						                if (null != rateStr || !"".equals(rateStr)) {
						                	float rate = Float.valueOf(rateStr.trim()).floatValue();
							                item.setRmbBaseprice(item.getBasePrice() * rate);
							                item.setRmbSalePrice(item.getSalePrice() * rate);
							                
							                if(item.getRmbBaseprice()-Double.valueOf(item.getRmbBaseprice()).intValue() > 0){
							                	item.setRmbBaseprice(Double.valueOf(item.getRmbBaseprice()).intValue()+1);
							                }
							                orderItemRmb=orderItem.getBasePrice()*rate;
							                if(orderItemRmb-Double.valueOf(orderItemRmb).intValue() > 0){
							                	orderItemRmb=Double.valueOf(orderItemRmb).intValue()+1;
							                }
						                }
						            }
						            result.setMessage(result.getMessage() + date+"由（底价："+orderItem.getBasePrice()+"HKD/"+orderItemRmb+"RMB）变为（底价："+item.getBasePrice()+"HKD/"+item.getRmbSalePrice()+"RMB）;");
									changePriceList.add(item);
								}
							}else{
								//如果不为代理则校验售价
								double orderItemPrice=Math.ceil(orderItem.getSalePrice());
								double refreshItemPrice=Math.ceil(item.getSalePrice());
								if(orderItemPrice != refreshItemPrice){
									result.setValue(-3);
						    		
									double orderItemRmb=0;
						    		Map<String, String> rateMap = CurrencyBean.rateMap;
						            if (null != rateMap) {
						            	String rateStr = rateMap.get(CurrencyBean.HKD);
						                if (null != rateStr || !"".equals(rateStr)) {
						                	float rate = Float.valueOf(rateStr.trim()).floatValue();
							                item.setRmbBaseprice(item.getBasePrice() * rate);
							                item.setRmbSalePrice(item.getSalePrice() * rate);
							                
							                if(item.getRmbSalePrice()-Double.valueOf(item.getRmbSalePrice()).intValue() > 0){
							                	item.setRmbSalePrice(Double.valueOf(item.getRmbSalePrice()).intValue()+1);
							                }
							                orderItemRmb=orderItem.getSalePrice()*rate;
							                if(orderItemRmb-Double.valueOf(orderItemRmb).intValue() > 0){
							                	orderItemRmb=Double.valueOf(orderItemRmb).intValue()+1;
							                }
						                }
						            }
						            result.setMessage(result.getMessage() + date+"由（售价："+orderItem.getSalePrice()+"HKD/"+orderItemRmb+"RMB）变为（售价："+item.getSalePrice()+"HKD/"+item.getRmbSalePrice()+"RMB）;");
									changePriceList.add(item);
								}
							}
						}
					}
				}
			}
		}
		return result;
    }

    /** getter and setter begin */

    public String getOriOrderId() {
        return oriOrderId;
    }

    public void setOriOrderId(String oriOrderId) {
        this.oriOrderId = oriOrderId;
    }

    public boolean isSaveToFront() {
        return saveToFront;
    }

    public void setSaveToFront(boolean saveToFront) {
        this.saveToFront = saveToFront;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public ReservationInfo getReserv() {
        return reserv;
    }

    public void setReserv(ReservationInfo reserv) {
        this.reserv = reserv;
    }

    public DSUnitService getDeliveryUnitService() {
        return deliveryUnitService;
    }

    public void setDeliveryUnitService(DSUnitService deliveryUnitService) {
        this.deliveryUnitService = deliveryUnitService;
    }

    public String getMostPriceLevel() {
        return mostPriceLevel;
    }

    public void setMostPriceLevel(String mostPriceLevel) {
        this.mostPriceLevel = mostPriceLevel;
    }

    public String getMostStar() {
        return mostStar;
    }

    public void setMostStar(String mostStar) {
        this.mostStar = mostStar;
    }

    public String getSRenew() {
        return sRenew;
    }

    public void setSRenew(String renew) {
        sRenew = renew;
    }

    public String getIdCurStr() {
        return idCurStr;
    }

    public void setIdCurStr(String idCurStr) {
        this.idCurStr = idCurStr;
    }

    public CommunicaterService getCommunicaterService() {
        return communicaterService;
    }

    public void setCommunicaterService(CommunicaterService communicaterService) {
        this.communicaterService = communicaterService;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    /** getter and setter end */

    public String getIsSalesPromo() {
        return isSalesPromo;
    }

    public void setIsSalesPromo(String isSalesPromo) {
        this.isSalesPromo = isSalesPromo;
    }

    public String getIsTaxCharges() {
        return isTaxCharges;
    }

    public void setIsTaxCharges(String isTaxCharges) {
        this.isTaxCharges = isTaxCharges;
    }

    public boolean isQuotaBool() {
        return quotaBool;
    }

    public void setQuotaBool(boolean quotaBool) {
        this.quotaBool = quotaBool;
    }

    public boolean isBedStateOneBool() {
        return bedStateOneBool;
    }

    public void setBedStateOneBool(boolean bedStateOneBool) {
        this.bedStateOneBool = bedStateOneBool;
    }

    public boolean isBedStateTwoBool() {
        return bedStateTwoBool;
    }

    public void setBedStateTwoBool(boolean bedStateTwoBool) {
        this.bedStateTwoBool = bedStateTwoBool;
    }

    public boolean isBedStateThrBool() {
        return bedStateThrBool;
    }

    public void setBedStateThrBool(boolean bedStateThrBool) {
        this.bedStateThrBool = bedStateThrBool;
    }

    public int getQuotaLeastNum() {
        return quotaLeastNum;
    }

    public void setQuotaLeastNum(int quotaLeastNum) {
        this.quotaLeastNum = quotaLeastNum;
    }

    public String getCtcttype() {
        return ctcttype;
    }

    public void setCtcttype(String ctcttype) {
        this.ctcttype = ctcttype;
    }

    public IHDLService getHdlService() {
        return hdlService;
    }

    public void setHdlService(IHDLService hdlService) {
        this.hdlService = hdlService;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * 预订房间间数 默认为1间 add by shizhongwen 时间:Jan 6, 2009 3:26:08 PM
     * 
     * @return
     */
    public String getHotelroomcount() {
        return hotelroomcount;
    }

    public void setHotelroomcount(String hotelroomcount) {

        this.hotelroomcount = hotelroomcount;
    }

    public boolean isForEdit() {
        return forEdit;
    }

    public void setForEdit(boolean forEdit) {
        this.forEdit = forEdit;
    }

    public boolean isTraditionalChannel() {
        return traditionalChannel;
    }

    public void setTraditionalChannel(boolean traditionalChannel) {
        this.traditionalChannel = traditionalChannel;
    }

    public String getCooperateChannel() {
        return cooperateChannel;
    }

    public void setCooperateChannel(String cooperateChannel) {
        this.cooperateChannel = cooperateChannel;
    }

    public String getCreditRemark() {
        return creditRemark;
    }

    public void setCreditRemark(String creditRemark) {
        this.creditRemark = creditRemark;
    }

    public String getGuestRenewMessage() {
        return guestRenewMessage;
    }

    public void setGuestRenewMessage(String guestRenewMessage) {
        this.guestRenewMessage = guestRenewMessage;
    }

    public String getRenewMessage() {
        return renewMessage;
    }

    public void setRenewMessage(String renewMessage) {
        this.renewMessage = renewMessage;
    }

    public String getRenewReason() {
        return renewReason;
    }

    public void setRenewReason(String renewReason) {
        this.renewReason = renewReason;
    }

    public void setHotelService(IHotelService hotelService) {
        this.hotelService = hotelService;
    }

    public String getUseableRoom() {
        return useableRoom;
    }

    public void setUseableRoom(String useableRoom) {
        this.useableRoom = useableRoom;
    }

    public IOrderEditService getOrderEditService() {
        return orderEditService;
    }

    public void setOrderEditService(IOrderEditService orderEditService) {
        this.orderEditService = orderEditService;
    }

    public ResourceManager getResourceManager() {
        return resourceManagerA;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManagerA = resourceManager;
    }

    public HKService getHkService() {
        return hkService;
    }

    public void setHkService(HKService hkService) {
        this.hkService = hkService;
    }

    public String getTaxChargesDetail() {
        return taxChargesDetail;
    }

    public void setTaxChargesDetail(String taxChargesDetail) {
        this.taxChargesDetail = taxChargesDetail;
    }

    public String getAddModifyCancelStrr() {
        return addModifyCancelStrr;
    }

    public void setAddModifyCancelStrr(String addModifyCancelStrr) {
        this.addModifyCancelStrr = addModifyCancelStrr;
    }

    public List<OrPreSale> getPreSaleList() {
        return preSaleList;
    }

    public void setPreSaleList(List<OrPreSale> preSaleList) {
        this.preSaleList = preSaleList;
    }
    public void setPreSaleNum(int preSaleNum) {
        this.preSaleNum = preSaleNum;
    }

	public String getCanRoomNumberAlert() {
		return canRoomNumberAlert;
	}

	public void setCanRoomNumberAlert(String canRoomNumberAlert) {
		this.canRoomNumberAlert = canRoomNumberAlert;
	}

	 public IOrderBenefitService getOrderBenefitService() {
			return orderBenefitService;
	}

	public void setOrderBenefitService(IOrderBenefitService orderBenefitService) {
			this.orderBenefitService = orderBenefitService;
	}
	
	public boolean isFlagSecondConfirm() {
		return flagSecondConfirm;
	}
	
	public void setFlagSecondConfirm(boolean flagSecondConfirm) {
		this.flagSecondConfirm = flagSecondConfirm;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

	public void setHotelReservationInfoService(
			IHotelReservationInfoService hotelReservationInfoService) {
		this.hotelReservationInfoService = hotelReservationInfoService;
	}

	public IHotelFavourableReturnService getReturnService() {
		return returnService;
	}

	public void setReturnService(IHotelFavourableReturnService returnService) {
		this.returnService = returnService;
	}
	
	public HtlLimitFavourableManage getLimitFavourableManage() {
		return limitFavourableManage;
	}

	public void setLimitFavourableManage(
			HtlLimitFavourableManage limitFavourableManage) {
		this.limitFavourableManage = limitFavourableManage;
	} 
	
	public String getForCooperate() {
		return forCooperate;
	}

	public void setForCooperate(String forCooperate) {
		this.forCooperate = forCooperate;
	}

	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public SupplierInfoService getSupplierInfoService() {
		return supplierInfoService;
	}

	public void setSupplierInfoService(SupplierInfoService supplierInfoService) {
		this.supplierInfoService = supplierInfoService;
	}
	
    public void setNewOrderParamService(INewOrderParamService newOrderParamService) {
		this.newOrderParamService = newOrderParamService;
	}

	public HotelElOrderService getHotelElOrderService() {
		return hotelElOrderService;
	}
	public void setHotelElOrderService(HotelElOrderService hotelElOrderService) {
		this.hotelElOrderService = hotelElOrderService;
	}



	public String getIsEpOrder() {
		return isEpOrder;
	}



	public void setIsEpOrder(String isEpOrder) {
		this.isEpOrder = isEpOrder;
	}



	public EpOrderManagerService getEpOrderManagerService() {
		return epOrderManagerService;
	}



	public void setEpOrderManagerService(EpOrderManagerService epOrderManagerService) {
		this.epOrderManagerService = epOrderManagerService;
	}



	public CheckElongAssureService getCheckElongAssureService() {
		return checkElongAssureService;
	}
	public void setCheckElongAssureService(
			CheckElongAssureService checkElongAssureService) {
		this.checkElongAssureService = checkElongAssureService;
	}



	public String getOldChannel() {
		return oldChannel;
	}



	public void setOldChannel(String oldChannel) {
		this.oldChannel = oldChannel;
	}
    

	public HtlOrderChannelService getHtlOrderChannelService() {
		return htlOrderChannelService;
	}



	public void setHtlOrderChannelService(
			HtlOrderChannelService htlOrderChannelService) {
		this.htlOrderChannelService = htlOrderChannelService;
	}
    
}
