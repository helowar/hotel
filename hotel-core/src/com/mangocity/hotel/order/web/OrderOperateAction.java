package com.mangocity.hotel.order.web;

import hk.com.cts.ctcp.hotel.constant.ResultConstant;
import hk.com.cts.ctcp.hotel.constant.TxnStatusType;
import hk.com.cts.ctcp.hotel.service.HKService;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.TxnStatusData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BasicData;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;

import com.mangocity.ep.service.EpOrderManagerService;
import com.mangocity.hagtb2b.service.B2bOrderIncService;
import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hdl.hotel.dto.CancelExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.CancelExRoomOrderResponse;
import com.mangocity.hdl.hotel.dto.MGExOrder;
import com.mangocity.hdl.hotel.dto.MGExOrderItem;
import com.mangocity.hdl.hotel.dto.MGExResult;
import com.mangocity.hdl.hotel.dto.ModifyExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.ModifyExRoomOrderResponse;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.hotel.base.constant.AreaType;
import com.mangocity.hotel.base.manage.HtlLimitFavourableManage;
import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.base.service.assistant.HotelPriceSearchParam;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.dto.PointDTO;
import com.mangocity.hotel.order.constant.GuaranteeState;
import com.mangocity.hotel.order.constant.OrderExtInfoType;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.manager.HraManager;
import com.mangocity.hotel.order.persistence.FITCashItem;
import com.mangocity.hotel.order.persistence.FITOrderCash;
import com.mangocity.hotel.order.persistence.HtlOrderStsLog;
import com.mangocity.hotel.order.persistence.OrChannelNo;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrLockedOrders;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderExtInfo;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrOrderStatistics;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrRefund;
import com.mangocity.hotel.order.persistence.OrToContractgroup;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.persistence.view.OrPaymentVO;
import com.mangocity.hotel.order.persistence.view.OrRefundVO;
import com.mangocity.hotel.order.service.HtlOrderChannelService;
import com.mangocity.hotel.order.service.HurryOrderService;
import com.mangocity.hotel.order.service.IMemberConfirmService;
import com.mangocity.hotel.order.service.IOrderEditService;
import com.mangocity.hotel.order.service.IVoucherInterfaceService;
import com.mangocity.hotel.order.service.OperOrderDerferTimeService;
import com.mangocity.hotel.order.service.OrChannelNoService;
import com.mangocity.hotel.order.service.assistant.HtlOrderChannel;
import com.mangocity.hotel.rmp.api.AffirmFacade;
import com.mangocity.hotel.rmp.api.PricePlanFacade;
import com.mangocity.hotel.rmp.api.QuotaFacade;
import com.mangocity.hotel.rmp.dto.AffirmDTO;
import com.mangocity.hotel.rmp.dto.QuotaShowDTO;
import com.mangocity.hotel.rmp.dto.QuotaStateDTO;
import com.mangocity.hotel.rmp.response.dto.PricePlanResponseDTO;
import com.mangocity.hotel.user.UserUtil;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.hotel.util.BeanUtil;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.proxy.payment.service.CreditCardPreAuthInterface;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.config.ConfigUtil;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.hotel.constant.QuotaType;
import com.mangocity.vch.app.service.exception.VCHException;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Mail;

/**
 * 订单操作相关，包括查看，修改
 * 
 * @author chenkeming
 * 
 */
public class OrderOperateAction extends PopulateOrderAction {

    private static final long serialVersionUID = 6552333123371282306L;

    private static final String BILLDETAIL_URL = ConfigUtil.getResourceByKey(
        "hotelii_i_delivery.billdetail_url");

    private static final String RESDETAIL_URL = ConfigUtil.getResourceByKey(
        "hotelii_i_delivery.resdetail_url");

    private static final String PREDETAIL_URL = ConfigUtil.getResourceByKey(
        "hotel.payment.creditCardIdHistoryUrl");

    private static final String TMC_ORDER_VIEW_URL = ConfigUtil.getResourceByKey(
    "TMC_ORDER_VIEW_URL");
    
    private static final String TMC_ORDER_EDIT_URL = ConfigUtil.getResourceByKey(
    "TMC_ORDER_EDIT_URL");
    
    /**
     * 查看TMC订单的url add by chenkeming
     */
    private String viewTmcOrderURL;
    
    /**
     * 修改TMC订单的url add by chenkeming
     */
    private String editTmcOrderURL;
    
    /**
     * 币种符号
     */
    private String idCurStr;

    private String unlockURL;

    private String mostStar;

    private String mostPriceLevel;

    private String creditRemark;

    /**
     * 以下用于撤单保存撤单原因
     */
    private String cancelReason;

    private String cancelMessage;

    private String guestCancelMessage;

    private OrOrderDao orOrderDao;

    // 默认联系方式
    private String ctcttype;

    /**
     * 酒店直联渠道编码channel
     * 
     * @author guojun 2008-12-10 15:30
     */
    private String channel;

    /**
     * hotel2.5.0酒店直联
     * 
     * @author guojun 定单是否允许修改 1表示可以修改,0表示不能修改.
     */
    private String isModify;

    /**
     * 酒店直联渠道编码集团酒店
     * 
     * @author guojun 2008-12-10 15:30
     */
    private String chainCode;

    /**
     * 酒店直联 IHDLService
     * 
     * @author guojun 2008-11-26 15:30
     */
    private IHDLService hdlService;

    /**
     * 处理给客人发短信的业务逻辑 add by chenjiajie V2.7.1 2009-02-17
     */
    private IMemberConfirmService memberConfirmService;

    /**
     * 订单是否修改单
     * 
     * @author chenkeming Feb 16, 2009 12:58:57 PM
     */
    private boolean hasHis;

    /**
     * 是否修改基本信息或者恢复历史订单
     * 
     * @author chenkeming Feb 17, 2009 11:48:08 AM
     */
    private boolean fromEditBase;

    /**
     * 订单修改service
     * 
     * @author chenkeming Feb 16, 2009 1:19:01 PM
     */
    private IOrderEditService orderEditService;

    /**
     * 原来担保金额
     * 
     * @author chenkeming Mar 10, 2009 3:15:20 PM
     */
    private double oriAssureMoney;

    /**
     * 中旅接口
     * 
     * @author chenkeming Mar 27, 2009 10:08:47 AM
     */
    private HKService hkService;

    private SystemDataService systemDataService;

    /**
     * message接口hotel2.8.1 by guojun
     */
    private CommunicaterService communicaterService;

    /**
     * 返回取消修改规则的提示语
     * 
     * @author guzhijie 2009-07-29
     */
    private String addModifyCancelStrr = "";

    private HraManager hraManager;
    
    /**
     * 信用卡预授权接口
     */
    private CreditCardPreAuthInterface creditcardPreAuthService;

    /**
     * 该房型下面配额是否存在共享
     */
    private IHotelService hotelService;
    
    private HotelRoomTypeService hotelRoomTypeService;
    
    private OrChannelNoService  orChannelNoService;
    
    /**
     * 订单所属组别
     */
    private Long orderGroupId;
    
    /**
     * 借记卡历史是否要默认显示
     */
    private String canDebitCardShow;
	
	
	private B2bOrderIncService b2bOrderIncService;
    
	private OperOrderDerferTimeService operOrderDerferTimeService;
	private Integer orderSts;
    
	private HurryOrderService hurryOrderService;
	private Integer hurryOrderTimes;
	
	private List<QuotaShowDTO> shadowQuotaList;

	
	private Boolean isDelQuoSuccess;
	
	private Boolean isUpdateStaSuccess;

	private String quotaType;
	 /**
	 * 现金返还服务接口 add by linpeng.fang 2010-10-21
	 */
    private IHotelFavourableReturnService returnService;
    
    /**
     * 芒果网限量返现活动  add by longkangfu
     */
    private HtlLimitFavourableManage limitFavourableManage;
    
    /**
     * 订单网上取消申请代码以及时间 add by zengming 2012-08-03
     */
    private String cancelCode;
	private String cancelDate;
    
	private String isEpOrder;
	
	private EpOrderManagerService epOrderManagerService;
	private BeanUtil beanUtil;
	
	private HtlOrderChannelService htlOrderChannelService;
	
	private HtlOrderChannel htlOrderChannel;
	/**
     * 查看订单内容
     * 
     * @return
     */
    public String viewOrder() throws Exception {
    	if(orderId!=null){
    		order = getOrder(orderId);
    	}else{
        	String orderCD = request.getParameter("orderCD");
        	if(orderCD!=null) {
        		order = this.getOrderService().getOrOrderByOrderCd(orderCD);
        	}
        }
        if (null == order) {
            return forwardError("order对象为空！");
        }
        
        // 如果是TMC订单则调用tmc url查看
        if(OrderType.TYPE_TMC == order.getType()) {
       	viewTmcOrderURL = TMC_ORDER_VIEW_URL;
       	return "view_order_tmc";
       }

        member = getOrderMember(order);
        if (null == member) {
            return forwardError("订单会员信息获取失败!");
        }
        // 会员信息
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

        // 设置紧急程度
        Date now = new Date();
        order.setEmergencyLevel(orderAssist.getEmergency(order, now, member, null));
        saveOrUpdateOrder(order);
        isEpOrder = epOrderManagerService.validateEpOrderByHotelId(String.valueOf(order.getHotelId()));
        // v2.7.1 订单记录会员的联名商家项目号 chenjiajie 2009-02-19
        order.setMemberAliasId(member.getAliasid());

        String isFromFront = (String) getParams().get("isFromFront");
        putSession("isFromFront", isFromFront);

        if (order.isPrepayOrder()) {
            List list = order.getPaymentList();
            int nSize = list.size();
            if (0 < nSize) {
                OrPaymentVO[] selPayment = new OrPaymentVO[nSize];
                for (int i = 0; i < nSize; i++) {
                    OrPayment payment = (OrPayment) list.get(i);
                    OrPaymentVO paymentVO = new OrPaymentVO("");
                    paymentVO.setPayment(payment);
                    selPayment[i] = paymentVO;
                }
                request.setAttribute("selPayment", selPayment);
            }
        }
        if (order.isHasPrepayed()) {
            String[] names = { "Cash", "CredInt", "CredDom", "Pt", "Bank", "Voucher", "Pos","Coupon", "Bla" };
            OrRefundVO[] selRefund = new OrRefundVO[9];
            for (int i = 0; 9 > i; i++) {
                selRefund[i] = new OrRefundVO(names[i]);
            }
            List list = order.getRefundList();
            for (int i = 0; i < list.size(); i++) {
                OrRefund refund = (OrRefund) list.get(i);
            	/** 正常1-7的支付方式可以按照原有减1的逻辑，代金券常量值特殊 不能按照原有逻辑 hotel2.9.3 modify by chenjiajie 2009-09-02 begin **/
            	int payTypeIndex = refund.getRefundType() - 1;
            	if(refund.getRefundType() == PrepayType.Coupon){
            		payTypeIndex = 7;
            	}else if(refund.getRefundType() == PrepayType.BALANCEPAYInt){//余额支付，TMC-V2.0 add by shengwei.zuo 2010-4-1
            		payTypeIndex = 8;
            	}
                selRefund[payTypeIndex].setRefund(refund);
                /** 正常1-7的支付方式可以按照原有减1的逻辑，代金券常量值特殊 不能按照原有逻辑 hotel2.9.3 modify by chenjiajie 2009-09-02 end **/
            }
            request.setAttribute("selRefund", selRefund);
        }
        // 查看预授权工单信息
        if (order.isHasCreatePreAuth()) {
            try {
                // List<PreAuthList> preAuthList =
                // creditcardPreAuthService.getPreAuthList("HOTEL",
                // order.getOrderCD());
                // request.setAttribute("preAuthList", preAuthList);
                String preFlag = creditcardPreAuthService.getPreAuthSucceedFlag("HOTEL", order
                    .getOrderCD());
                request.setAttribute("preFlag", preFlag);
            } catch (Exception re) {
            	log.error(re.getMessage(),re);
            }
            request.setAttribute("preUrl", PREDETAIL_URL);
        }

        // 查看配送单内容url
        if (StringUtil.isValidStr(order.getFulfillmentCD())) {
            request.setAttribute("fulUrl", BILLDETAIL_URL);
        }
        request.setAttribute("fulResUrl", RESDETAIL_URL);

        request.setAttribute("orderItemTotal", order.getOrderItems().size());

        OrReservation orReserv = order.getReservation();
        if (null != orReserv) {
            String str = orReserv.getCreditRemark();
            if (null != str) {
                creditRemark = StringUtil.formatHtmlString(str);
            }
        }

        String creditCardIdsSelect = order.getCreditCardIdsSelect();
        if (StringUtil.isValidStr(creditCardIdsSelect)) {
            creditCardIdsSelect = creditCardIdsSelect.replaceAll("&", ",");
        }
        request.setAttribute("creditCardIdsSelect", creditCardIdsSelect);
        idCurStr = CurrencyBean.idCurMap.get(order.getPaymentCurrency());
        request.setAttribute("rateCurrency", order.getRateId());
        /** 查询该订单发送短信给客人成功或失败已确认的次数 V2.7.1 chenjiajie 2009-02-17 begin **/
        int smsSucNum = memberConfirmService.getSuccessedConfirmNum(
            order.getID()); // 送短信给客人成功或失败已确认的记录数
        int smsNum = memberConfirmService.getConfirmNum(order.getID()); // 发送给客人短信的记录数
        request.setAttribute("smsSucNum", smsSucNum);
        log.info("smsSucNum:+++" + smsSucNum);
        request.setAttribute("smsNum", smsNum);
        log.info("smsNum:+++" + smsNum);
        /** 查询该订单发送短信给客人成功或失败已确认的次数 V2.7.1 chenjiajie 2009-02-17 end **/
        /** hotel 2.9.2 对不同type的取消修改规则返回不同的提示语add by guzhijie 2009-07-23 begin **/
        List liRes = new ArrayList();
        if (null != order.getReservation()) {
            liRes = orderEditService.getModifyCancelStr2(order, order.getReservation().getID(),
                order.getSumRmb());
            if (null != liRes && !liRes.isEmpty()) {
                addModifyCancelStrr = liRes.get(0).toString();

            }
        }
        if(!order.isPrepayOrder() && order.getChannel()==ChannelType.CHANNEL_ELONG){
        	List<OrOrderExtInfo> extlist = order.getOrOrderExtInfoList();
        	for(OrOrderExtInfo ext:extlist){
        		if(OrderExtInfoType.ELONG_ASSURE_TIP.equals(ext.getType().trim())){
        	        request.setAttribute("elAssureCondStr", ext.getContext());
        		}else if(OrderExtInfoType.ELONG_ASSURE_MODIFY.equals(ext.getType().trim())){
        			addModifyCancelStrr = ext.getContext();
        		}
        	}
        }
        /** hotel 2.9.2 对不同type的取消修改规则返回不同的提示语add by guzhijie 2009-07-23 end **/
        
        /**
         * 增加配额的床型是否共享
         * add by guojun 2010-1-25 17:50
         */
        HtlRoomtype HtlRoomtype = hotelRoomTypeService.getHtlRoomTypeByRoomTypeId(order.getRoomTypeId());
        if(HtlRoomtype!=null){
        	if(HtlRoomtype.getQuotaBedShare()>0){
        		 request.setAttribute("quotaBedShare", 1);
        	}else{
        		 request.setAttribute("quotaBedShare", 0);
        	}
        }
        
        /**
         * 判断是否为代理商底价加幅的订单 根据订单CD 查询在htlb2b_orderincrease表中是否有记录 有表明是加幅订单
         * add by xuyiwen 2010-12-9 
         */
        if(b2bOrderIncService.judgeIsIncreaseOrder(order.getOrderCD())){
        	request.setAttribute("isIncreaseOrder", 1);
        }else{
        	request.setAttribute("isIncreaseOrder", 0);
        }
        
        //elong酒店级别提示信息
        if(ChannelType.CHANNEL_ELONG==order.getChannel()) {
        	HtlHotel htlHotel = hotelService.findHotel(order.getHotelId());
        	request.setAttribute("hotelAlertMsg", htlHotel.getAlertMessage());
        }
        
        /**
         * 处理订单附近信息 add by alfred.
         */
        orderService.getOrOrderExtInfo(request,order);
        
        //获取订单projectcode
        HtlProjectCode projectcode = orderService.queryHtlOrderProject(order.getOrderCD());
        request.setAttribute("projectcode", projectcode.getProjectCode());
        request.setAttribute("ccorderid", projectcode.getExProjectCode1());
        return VIEW_ORDER;
    }

    /**
     * 修改订单前显示订单内容
     * 
     * @return
     */
    public String edit() throws Exception {
        
    	hurryOrderTimes=hurryOrderService.queryHurryOrderNum(orderId);
    	
        // 获取订单对象
        order = (OrOrder) getFromSession("orderEditObj");
        if (null == order) {
            order = getOrder(orderId);
            if (null == order) {
                return forwardError("order对象为空！");
            }
            if(validateHKorderStatus(order)>0){
            	order = getOrder(orderId);
            }
        } else {
            putSession("orderEditObj", null);
        }
        isEpOrder = epOrderManagerService.validateEpOrderByHotelId(String.valueOf(order.getHotelId()));
        String isFromFront = (String) getParams().get("isFromFront");
        boolean bFromFront = StringUtil.isValidStr(isFromFront) && isFromFront.equals("1") ? true
            : false;
        // 如果是TMC订单则调用tmc url打开
        if(OrderType.TYPE_TMC == order.getType()) {
        	editTmcOrderURL = TMC_ORDER_EDIT_URL;
        	//如果是从处理预定单里点进来的，则变成是从前台进来一样by juesuchen@2010-4-28
        	if(bFromFront)
        		editTmcOrderURL += "isFromFront=1&";
        	return "edit_order_tmc";
        }

        // add shizhongwen 2009-03-07
        // modify by chenkeming@2009-03-18 v2.8 中旅网站订单不走2.5直连网站订单流程
        if (OrderSource.FROM_WEB.equals(order.getSource()) && !order.isCtsHK()) {
            channel = orderService.getChannelByHotelId(String.valueOf(order.getHotelId()));
            if (null == channel) {
                channel = "0";
            }
        } else {
            channel = "" + order.getChannel();
        }

        // 设置是否从日审界面链接过来
        String isFromAudit = (String) getParams().get("isFromAudit");
        boolean bFromAudit = false;
        if (StringUtil.isValidStr(isFromAudit)) {
            bFromAudit = StringUtil.isValidStr(isFromAudit) && isFromAudit.equals("1") ? true
                : false;
        }
        request.setAttribute("isFromAudit", bFromAudit ? 1 : 0);

        // 设置订单是否前台操作 TODO: 是否有必要用session?
        putSession("isFromFront", isFromFront);

        // 获取登陆用户
        roleUser = getOnlineRoleUser();

        // 获取会员信息
        // 修改生产环境td:790, 修改订单页面也需要常入住人和常联系人信息 modify by chenkeming@2008.10.14
        member = getOnlineMember();
        if (null == member || !member.getMembercd().equals(order.getMemberCd())) {
        	MemberDTO loginMember = memberInterfaceService
                .getMemberByCode(order.getMemberCd());
            if (null != loginMember) {
                if (loginMember.isMango()) {
                    try {
                        loginMember.setPoint(
                        		pointsDelegate.getPonitsByMemberCd(loginMember.getMembercd(), "mango"));
                    } catch (Exception e) {
                    	log.error(e.getMessage(),e);
                        /** 捕获取会员积分失败的异常，把积分设为0 by chenjiajie@2009-01-12 **/
                        PointDTO point = new PointDTO();
                        point.setBalanceValue("0");
                        point.setRc("0");
                        point.setMessage("");
                        loginMember.setPoint(point);
                    }
          
                    // 获取会员常入住人和常联系人
                    memberInterfaceService.getFellowAndLinkmanByMemberCd(loginMember);
                }
            }
            member = loginMember;
        }
        if (null == member) {
            return forwardError("订单会员信息获取失败!");
        }
    	
        
        // 不是从日审过来的链接，设置常入住人和常联系人数供修改页面使用
        if (false == bFromAudit) {
            request.setAttribute("oftenFellowCount", null == member.getFellowList() ? 0 : member
                .getFellowList().size());
            request.setAttribute("oftenLinkmanCount", null == member.getLinkmanList() ? 0 : member
                .getLinkmanList().size());
        }

        // 设置芒果会员信息，供修改订单页面使用
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

        // 订单加锁相关操作
        String locker = roleUser.getLoginName();
        OrLockedOrders lockedOrders = new OrLockedOrders();
        lockedOrders.setOrderId(orderId);
        OrLockedOrders lockedOrder = lockedOrderService.loadLockedOrder(lockedOrders);
        // 订单是否被其他操作员锁住
        if (null != lockedOrder && null != lockedOrder.getLocker()
            && !lockedOrder.getLocker().equals(locker)) {
            boolean bCanEdit = false;
            if (bFromFront && !lockedOrder.isFrontLock()) {
                bCanEdit = true;
            }
            if (!bCanEdit) {
                if (bFromFront) {
                    return forwardMsg("订单已被其他操作员(" + lockedOrder.getLocker() + ")"
                        + (lockedOrder.isFrontLock() ? "在前台" : "在中台") + "锁住！");
                } else {
                    return this.forwardMsgBox("订单已被其他操作员(" + lockedOrder.getLocker() + ")"
                        + (lockedOrder.isFrontLock() ? "在前台" : "在中台") + "锁住！", null);
                }
            }
        }
        // 锁住订单
        if (null == lockedOrder) {
            lockedOrders.setFrontLock(bFromFront);
            lockedOrders.setLocker(locker);
            lockedOrders.setOrderCD(order.getOrderCD());
            lockedOrders.setType(order.getType());
            lockedOrders.setLockTime(new java.util.Date());
            if (!lockedOrderService.insertLockedOrder(lockedOrders)) {
                if (bFromFront) {
                    return forwardMsg("订单加锁失败,可能同时被别人锁定！");
                } else {
                    return this.forwardMsgBox("订单加锁失败,可能同时被别人锁定！", null);
                }
            }
        } else {
            lockedOrders.setFrontLock(bFromFront);
            lockedOrders.setLocker(locker);
            lockedOrders.setOrderCD(order.getOrderCD());
            lockedOrders.setType(order.getType());
            lockedOrders.setLockTime(new java.util.Date());
            lockedOrderService.replaceLockedOrders(lockedOrder, lockedOrders);
        }
        
     

        // 如果是预付单，适当设置预付方式常量数组供修改页面使用（要考虑到网站预付单的情况）
        if (order.isPrepayOrder()) {
            List list = order.getPaymentList();
            int nTotalWays = 10;
            int webIndex = -1;
            int rmpIndex = -1;
            //hotel2.9.3 增加代金券支付方式 .从TMC-V2.0开始， 有余额支付的支付方式 add by shengwei.zuo  2010-4-1
            String[] names = { "Cash", "CredInt", "CredDom", "Pt", "Bank", "Voucher", "Pos","Coupon", "Bla","UnionPayPhone","" ,""};
            // 网站预付单支付成功
            if (order.isFromWeb() && 0 < order.getOrderItems().size()) {
                for (int i = 0; i < list.size(); i++) {
                    OrPayment payment = (OrPayment) list.get(i);
                    if (payment.isWebWay()) {
                    	webIndex = nTotalWays;
                        nTotalWays += 1;
                        names[nTotalWays-1] = PrepayType.strNames[payment.getPayType() - 1];
                        break;
                    }
                }
            }
            //如果是魅影订单需要加上月结支付方式
			if (order.getRmpOrder()) {
				rmpIndex = nTotalWays;
				nTotalWays += 1;
				names[nTotalWays - 1] = "HOTELMONTHPAY";
			}
      
            OrPaymentVO[] selPayment = new OrPaymentVO[nTotalWays];
            for (int i = 0; i < nTotalWays; i++) {
                selPayment[i] = new OrPaymentVO(names[i]);
            }
            for (int i = 0; i < list.size(); i++) {
                OrPayment payment = (OrPayment) list.get(i);
                int nType = payment.getPayType();
                if(nType==0){
                	continue;
                }
                //如果是通过借记卡（银联手机支付），默认显示该会员借记卡历史信息 begin add by zhijie.gu 2010-5-12
                if(payment.getPayType() == PrepayType.DEBITCARDInt){
                	canDebitCardShow = "1";
                }
                //如果是通过借记卡（银联手机支付），默认显示该会员借记卡历史信息 end add by zhijie.gu 2010-5-12
                
                if (!payment.isWebWay()) {
                	/** 正常1-7的支付方式可以按照原有减1的逻辑，代金券常量值特殊 不能按照原有逻辑 hotel2.9.3 modify by chenjiajie 2009-09-02 begin **/
                	int payTypeIndex = payment.getPayType() - 1;
                	if(payment.getPayType() == PrepayType.Coupon){
                		payTypeIndex = 7;
                	}else if(payment.getPayType() == PrepayType.BALANCEPAYInt){//余额支付 add by shengwei.zuo  2010-4-1
                		payTypeIndex = 8;
                	}else if(payment.getPayType() == PrepayType.UNTIONPAYPHONE){//银联电话支付 add by wupingxiang 2012-12-13
                		payTypeIndex = 9;
                	}else if (payment.getPayType() == PrepayType.HOTELMONTHPAYInt){//魅影 月结
                		payTypeIndex = rmpIndex;
                	}
                    selPayment[payTypeIndex].setPayment(payment);
                    /** 正常1-7的支付方式可以按照原有减1的逻辑，代金券常量值特殊 不能按照原有逻辑 hotel2.9.3 modify by chenjiajie 2009-09-02 end **/
				} else if (payment.isPaySucceed()) {
					if (payment.getPayType() == PrepayType.HOTELMONTHPAYInt){
						selPayment[rmpIndex].setPayment(payment);
					}else{
						selPayment[webIndex].setPayment(payment);
					}
				} else {
					if (payment.getPayType() == PrepayType.HOTELMONTHPAYInt){
						selPayment[rmpIndex].setPayment(payment);
					}else{
						selPayment[webIndex].setPayment(payment);
						request.setAttribute("sWebFailPay", PrepayType.payStrMap.get(nType));
					}
				}
            }
            request.setAttribute("selPayment", selPayment);
        }

        // 已经付款成功的预付单，设置退款方式常量数组供修改页面使用
        if (order.isHasPrepayed()) {
            String[] names = { "Cash", "CredInt", "CredDom", "Pt", "Bank", "Voucher", "Pos" ,"Coupon", "Bla","UnionPayPhone"};
            OrRefundVO[] selRefund = new OrRefundVO[names.length];
            for (int i = 0; names.length > i; i++) {
                selRefund[i] = new OrRefundVO(names[i]);
            }
            List list = order.getRefundList();
            for (int i = 0; i < list.size(); i++) {
                OrRefund refund = (OrRefund) list.get(i);
                
                //如果是通过借记卡（银联手机支付），默认显示该会员借记卡历史信息 begin add by zhijie.gu 2010-5-12
                if(refund.getRefundType() == PrepayType.DEBITCARDInt){
                	canDebitCardShow = "1";
                }
                //如果是通过借记卡（银联手机支付），默认显示该会员借记卡历史信息 end add by zhijie.gu 2010-5-12
                
            	/** 正常1-7的支付方式可以按照原有减1的逻辑，代金券常量值特殊 不能按照原有逻辑 hotel2.9.3 modify by chenjiajie 2009-09-02 begin **/
            	int payTypeIndex = refund.getRefundType() - 1;
            	if(refund.getRefundType() == PrepayType.Coupon){
            		payTypeIndex = 7;
            	}else if(refund.getRefundType() == PrepayType.BALANCEPAYInt){//余额支付 add by shengwei.zuo  2010-4-1
            		payTypeIndex = 8;
            	}else if(refund.getRefundType() == PrepayType.UNTIONPAYPHONE){//银联电话支付 
            		payTypeIndex = 9;
            	}
                selRefund[payTypeIndex].setRefund(refund);
                /** 正常1-7的支付方式可以按照原有减1的逻辑，代金券常量值特殊 不能按照原有逻辑 hotel2.9.3 modify by chenjiajie 2009-09-02 end **/
            }
            request.setAttribute("selRefund", selRefund);
        }

        boolean bEditBaseUpdate = false;
        int newSuretyState = order.getSuretyState();
        // 如果订单已经创建预授权工单，则根据担保情况设置订单担保状态，并设置预授权工单url供修改页面使用
        if (order.isHasCreatePreAuth()) {

            try {

                String preFlag = creditcardPreAuthService.getPreAuthSucceedFlag("HOTEL", order
                    .getOrderCD());
                request.setAttribute("preFlag", preFlag);

                 // 如果查询出来的值为担保成功.则往数据库更新担保状态字段
                 if (preFlag.equals("succeed") && newSuretyState != GuaranteeState.SUCCESS) {
	                 if(!fromEditBase) {
	                	 order.setSuretyState(GuaranteeState.SUCCESS);
	                 } else {
		                 newSuretyState = GuaranteeState.SUCCESS;
		                 bEditBaseUpdate = true;
	                 }
	                   // handleLog = OrderUtil.updateSurtyPrice(order, GuaranteeState.SUCCESS,roleUser);
                 }
            } catch (Exception e) {
            	log.error(e.getMessage(),e);
            }
            request.setAttribute("preUrl", PREDETAIL_URL);
            
//            /**担保转支付 add by haibo.li 计算出只要修改或者取消的金额和提示语 2010-6-7 **/
//            String reservationDelStr = orderEditService.getModifyandCancelCheck(order.getReservation().getID(),order.getID(),order.getCheckinDate().toString(),order.getRoomQuantity(),ModifyScopeType.CANCEL);
//            String reservationModifStr = orderEditService.getModifyandCancelCheck(order.getReservation().getID(),order.getID(),order.getCheckinDate().toString(),order.getRoomQuantity(),ModifyScopeType.MODIFY);
//            log.info("reservationDelStr"+reservationDelStr);
//            log.info("reservationModifStr"+reservationModifStr);
//          if(StringUtil.isValidStr(reservationDelStr) && !reservationDelStr.equals("ok")){
//            	String [] str = reservationDelStr.split("@");
//            	request.setAttribute("reservationDelSum", str[0]);
//            	request.setAttribute("reservationDelStr", str[1]);
//            }
//            if(StringUtil.isValidStr(reservationModifStr)&& !reservationModifStr.equals("ok")){
//            	String [] str = reservationModifStr.split("@");
//            	request.setAttribute("reservationModifSum", str[0]);
//            	request.setAttribute("reservationModifStr", str[1]);
//            }
        }

        // 更新订单紧急程度
        Date now = new Date();
        int newLevel = orderAssist.getEmergency(order, now, member, null);
        if (!fromEditBase) {
            order.setEmergencyLevel(newLevel);
            saveOrUpdateOrder(order);
        } else {
            if (newLevel != order.getEmergencyLevel()) {
                bEditBaseUpdate = true;
            }
            if (bEditBaseUpdate) {
                orderEditService.updateOrderForEdit(order, newLevel, newSuretyState);
                order.setEmergencyLevel(newLevel);
            }
        }

        // 设置查看配送信息相关url，供修改页面使用
        if (StringUtil.isValidStr(order.getFulfillmentCD())) {
            request.setAttribute("fulUrl", BILLDETAIL_URL);
        }
        request.setAttribute("fulResUrl", RESDETAIL_URL);

        // 获取订单配额需要呼出项数，供修改页面使用
        List list = order.getOrderItems();
        request.setAttribute("orderItemTotal", list.size());
        int nCallOut = 0;
        for (int i = 0; i < list.size(); i++) {
            OrOrderItem item = (OrOrderItem) list.get(i);
            if (false == item.getIsConfirm()&&null!=item.getQuotaType()&&("4").equals(item.getQuotaType())) {
                nCallOut++;
            }
        }
        request.setAttribute("orderItemCallOut", nCallOut);

        // 预订担保注意事项换行符处理
        OrReservation orReserv = order.getReservation();
        if (null != orReserv) {
            String str = orReserv.getCreditRemark();
            if (null != str) {
                creditRemark = StringUtil.formatHtmlString(str);
                // creditRemark = str.replace("\r\n", "");
            }
        }

        // 订单表的使用信用卡列表字符串分隔符'&'换成','
        String creditCardIdsSelect = order.getCreditCardIdsSelect();
        if (StringUtil.isValidStr(creditCardIdsSelect)) {
            creditCardIdsSelect = creditCardIdsSelect.replaceAll("&", ",");
        }
        request.setAttribute("creditCardIdsSelect", creditCardIdsSelect);

        // 获取酒店订单的所有订单号绑定的回传
        List faxLogList = orderService.getOrderFaxLot(order.getOrderCD());
        request.setAttribute("faxLogList", faxLogList);

        // 获取订单的币种字符串和币种汇率值，供修改页面使用
        idCurStr = CurrencyBean.idCurMap.get(order.getPaymentCurrency());
        request.setAttribute("rateCurrency", order.getRateId());

        // 获取历史订单列表
        if (!order.isManualOrder()) {
            request.setAttribute("lstHisOrder", orderEditService.getHisOrders(order.getID()));
        } else {
            request.setAttribute("lstHisOrder", null);
        }

        // hotel 2.5 如果是重新下单,页面显示旧定单的详细信息 add by guojun
        // v2.8 只有2.5直连模式订单才需要取原单信息 modify by chenkeming@2009-03-18
        int nChannel = Integer.parseInt(channel);
        if ((0 < nChannel && !order.isCtsHK())||order.getRmpOrder()) {
            OrOrder originOrOrder = orderService.getOriOrOrder(order.getOrderCD(), order
                .getOriginCD());
            request.setAttribute("originOrOrder", originOrOrder);
        }
        //代理系统查询订单是否修改 add by haibo.li 2010-1-10
        List b2borderLst = orderService.getB2bHagtOrder(order.getOrderCD());
        request.setAttribute("b2borderLst", b2borderLst);
        

        /** 查询该订单发送短信给客人成功或失败已确认的次数 V2.7.1 chenjiajie 2009-02-17 begin **/
        int smsSucNum = memberConfirmService.getSuccessedConfirmNum(
            order.getID()); // 送短信给客人成功或失败已确认的记录数
        int smsNum = memberConfirmService.getConfirmNum(order.getID()); // 发送给客人短信的记录数
        // add by lixiaoyong v2.6 2009-04-29 修改单的前后两天价格详情的显示
        
        //refactor
        HotelPriceSearchParam hotelPriceSearchParam = new HotelPriceSearchParam();
        hotelPriceSearchParam.setCheckinDate(order.getCheckinDate());
        hotelPriceSearchParam.setCheckoutDate(order.getCheckoutDate());
        hotelPriceSearchParam.setHotelId(order.getHotelId());
        hotelPriceSearchParam.setPayMethod(order.getPayMethod());
        hotelPriceSearchParam.setPayToPrepay(order.isPayToPrepay());
        hotelPriceSearchParam.setRoomTypeId(order.getRoomTypeId());
        request.setAttribute("hotelPriceList", hotelService.getHotelPriceList(hotelPriceSearchParam));
        request.setAttribute("smsSucNum", smsSucNum);
        request.setAttribute("smsNum", smsNum);
        // v2.7.1 订单记录会员的联名商家项目号
        order.setMemberAliasId(member.getAliasid());
        /** 查询该订单发送短信给客人成功或失败已确认的次数 V2.7.1 chenjiajie 2009-02-17 end **/
        /** hotel 2.9.2 进入订单填写页面需要查询房型的附加信息，并传到界面做判断 add by chenjiajie 2009-07-23 begin **/
		//生产bug773 系统外订单编辑的时候会报空指针异常 modify by chenjiajie 2009-08-25
		//非手工单才填充附加服务信息
		if(!order.isManualOrder()){
		    fillRoomAppendInfo();
		}
        /** hotel 2.9.2 进入订单填写页面需要查询房型的附加信息，并传到界面做判断 add by chenjiajie 2009-07-23 begin **/

        /** hotel 2.9.2 对不同type的取消修改规则返回不同的提示语add by guzhijie 2009-07-23 begin **/
        if (!order.isPrepayOrder() && order.getChannel()!=ChannelType.CHANNEL_ELONG) {
            List liRes = new ArrayList();
            if (null != order.getReservation()) {
                liRes = orderEditService.getModifyCancelStr2(order, order.getReservation().getID(),
                    order.getSumRmb());
                if (null != liRes && !liRes.isEmpty()) {
                    addModifyCancelStrr = liRes.get(0).toString();
                }
            }
        }else if(!order.isPrepayOrder() && order.getChannel()==ChannelType.CHANNEL_ELONG){
        	List<OrOrderExtInfo> extlist = order.getOrOrderExtInfoList();
        	for(OrOrderExtInfo ext:extlist){
        		if(OrderExtInfoType.ELONG_ASSURE_TIP.equals(ext.getType().trim())){
        	        request.setAttribute("elAssureCondStr", ext.getContext());
        		}else if(OrderExtInfoType.ELONG_ASSURE_MODIFY.equals(ext.getType().trim())){
        			addModifyCancelStrr = ext.getContext();
        		}
        	}
        }
        
        /**
         * 增加配额的床型是否共享
         * add by guojun 2010-1-25 17:50
         */
        HtlRoomtype HtlRoomtype = hotelRoomTypeService.getHtlRoomTypeByRoomTypeId(order.getRoomTypeId());
        if(HtlRoomtype!=null){
        	if(HtlRoomtype.getQuotaBedShare()>0){
        		 request.setAttribute("quotaBedShare", 1);
        	}else{
        		 request.setAttribute("quotaBedShare", 0);
        	}
        }
        
        /**
         * 判断是否为代理商底价加幅的订单 根据订单CD 查询在htlb2b_orderincrease表中是否有记录 有表明是加幅订单
         * add by xuyiwen 2010-12-9 
         */
        if(b2bOrderIncService.judgeIsIncreaseOrder(order.getOrderCD())){
        	request.setAttribute("isIncreaseOrder", 1);
        }else{
        	request.setAttribute("isIncreaseOrder", 0);
        }
        
        /** hotel 2.9.2 对不同type的取消修改规则返回不同的提示语add by guzhijie 2009-07-23 end **/
        
        /**
         * 处理订单附加信息 add by alfred.
         */
        orderService.getOrOrderExtInfo(request,order);
        
        /** hotel2.9.3 代金券需要传递酒店相关基本信息 add by diandian.hou 2011-11-23-15 begin **/
        HtlHotel htlHotel = hotelService.findHotel(order.getHotelId());
        //elong酒店级别提示信息
        if(ChannelType.CHANNEL_ELONG==order.getChannel()) {
        	request.setAttribute("hotelAlertMsg", htlHotel.getAlertMessage());
        }
        request.setAttribute("htlHotel", htlHotel);
        /** hotel2.9.3 代金券需要传递酒店相关基本信息 add by diandian.hou 2011-11-23-15 end **/
        
        htlOrderChannel = htlOrderChannelService.queryHtlOrderChannel(order.getID());

        //获取订单projectcode
        HtlProjectCode projectcode = orderService.queryHtlOrderProject(order.getOrderCD());
        request.setAttribute("projectcode", projectcode.getProjectCode());
        request.setAttribute("ccorderid", projectcode.getExProjectCode1());
        return EDIT_ORDER;
    }

    /**
     * 修改保存订单
     * 
     * @return
     */
    public String save() throws Exception {
    	
        Map params = super.getParams();

        String isCancel = (String) params.get("isCancel");
        boolean bCancel = (StringUtil.isValidStr(isCancel) && isCancel.equals("1")) ? true : false;

        // 防止重复提交, 允许重复撤单
        if (!bCancel && isRepeatSubmit()) {
            return forwardMsg("请不要重复提交!");
        }

        // 获取订单对象 
        order = getOrder(orderId);
        orderSts=operOrderDerferTimeService.validateOrderSts(orderId);
        
        roleUser = getOnlineRoleUser();
        if (!order.isNewOrder()) {
            OrOrder tempOrder = new OrOrder();
            MyBeanUtil.copyProperties(tempOrder, params);
            String isFromFront=(String) params.get("isFromFront");
            // 设置基本修改信息
            if(isFromFront==null){
            HtlOrderStsLog htlOrderStsLog=OrderStsLogData(order, tempOrder,roleUser);
            if(htlOrderStsLog!=null){
            htlOrderStsLogService.insert(htlOrderStsLog);
            }
            }           
        }
        
        // 处理不能撤单的情况
        if (bCancel) {
            if (order.isCancel()) {
                return super.forwardMsg("该订单已经被取消！");
            }
            if (!order.isPrepayOrder() && order.getOrderState() >= OrderState.CHECKIN
                && order.getOrderState() <= OrderState.EXTEND) {
                return super.forwardMsg("客人已入住，订单不能取消！");
            }
        }

        // 获取会员
        member = getOrderMember(order);
        

        boolean oriCreditAssure = order.isCreditAssured();

        // 对于已经撤单的订单，不能置空其撤单原因，处理一个订单同时被两个人打开操作的情况
        // modify by chenkeming@2008.11.18
        if (order.isCancel() && (!StringUtil.isValidStr(cancelReason) 
            || cancelReason.equals("0"))) {
            int tmpReason = order.getCancelReason();
            String tmpMsg = order.getCancelMessage();
            String tmpGuestMsg = order.getGuestCancelMessage();
            populateOrder(order);
            OrderUtil.updateStayInMid(order);
            if(order.getRmpOrder()){
            	order.setIsStayInMid(false);
            }
            order.setCancelReason(tmpReason);
            order.setCancelMessage(tmpMsg);
            order.setGuestCancelMessage(tmpGuestMsg);
        } else {
            populateOrder(order);
            if (!bCancel) {
                OrderUtil.updateStayInMid(order);
            }
        }

        // 设置是否前台操作,同时设置订单中(前)台修改时间 TODO:是否有必要使用session
        String isFromFront = (String) params.get("isFromFront");
        boolean bFromFront = StringUtil.isValidStr(isFromFront) && isFromFront.equals("1") ? true
            : false;
        putSession("isFromFront", isFromFront);
        Date nowDate = new Date();
        if (!bFromFront) {
            order.setModifiedMidTime(nowDate);
        } else {
            order.setModifiedFrontTime(nowDate);
        }
        order.setModifiedTime(nowDate);

        int channelType = 0;
        if (!"".equals(channel)) {
            channelType = Integer.valueOf(channel);
        }
        
        //将中旅订单酒店备注清空 add by wupingxiang 2012-9-18
        if (!order.isManualOrder() && channelType == ChannelType.CHANNEL_CTS){
        	order.getRemark().setHotelRemark("");
        }
        
//      增加取消金额 add by haibo.li
    	try{
    		if (0.001 < reservationDelSum) {
    			OrHandleLog handleLog = new OrHandleLog();
        		handleLog.setModifierName(roleUser.getName());
                handleLog.setModifierRole(roleUser.getLoginName());
            OrderUtil.addCancelMoney(order, reservationDelSum, roleUser);
            //调用财务担保转支付接口 add by haibo.li 2010-6-23
            //String sid = orderService.getOrderExtInfoByType(order, BaseConstant.ORDER_EXTINFO_SID);
            //if(sid != null && !"".equals(sid)) {
                String issuccess = creditcardPreAuthService.payCharge(reservationDelSum, order.getOrderCD());
                if(StringUtil.isValidStr(issuccess)){
                	if(issuccess.equals("T")){
                		order.setPayCharge(reservationDelSum);//信用卡扣款金额 
                		handleLog.setContent("担保转支付,扣款成功");
                	}else{
                		//增加操作日志.add by haibo.li
                        handleLog.setContent("担保转支付,扣款失败");  
                	}
                }
                handleLog.setModifiedTime(new Date());
                handleLog.setBeforeState(order.getOrderState());
                handleLog.setAfterState(order.getOrderState());
                handleLog.setOrder(order);
            	order.getLogList().add(handleLog);
            //}else {
            //	log.error("ErrorMSG:MPM:get sid is null where orderCd :=" + order.getOrderCD());
            //}
        }	
    	}catch(Exception e){
    		log.error(e.getMessage(), e);
    	}
    	
        // 撤单处理
        if (bCancel) {
        	
        	StringBuilder bCancelMsg = new StringBuilder();
        	
            /** hotel2.9.3 代金券支付方式,调用代金券接口取消代金券 add by chenjiajie 2009-09-03 begin **/
            if(order.isIncludeCouponPrepay()){
            	try {
                	voucherInterfaceService.callVchServiceCancelOrder(order, roleUser);
				} catch (VCHException e) {
					OrHandleLog handleLog = new OrHandleLog();
			        handleLog.setModifierName("system");
			        handleLog.setModifierRole("system");
			        handleLog.setContent(e.getMessage());
			        handleLog.setModifiedTime(new Date());
			        handleLog.setOrder(order);
			        order.getLogList().add(handleLog);
			        log.info(" vch010 取消代金券失败 原因：" + e.getMessage());
			        log.error(e.getMessage(),e);
				}
            }
            /** hotel2.9.3 代金券支付方式,调用代金券接口取消代金券 add by chenjiajie 2009-09-03 end **/
            
            if (!hasHis) {
                orderService.cancelOrder(order, Integer.parseInt(cancelReason), cancelMessage,
                    guestCancelMessage, roleUser);
            } else {
                orderEditService.cancelOrderEdit(order, Integer.parseInt(cancelReason),
                    cancelMessage, guestCancelMessage, roleUser);
            }
            
            bCancelMsg.append("取消系统订单成功！");
            
            /**
             * 酒店直联取消定单 start
             * 
             * @author guojun 2008-12-11 17:40 v2.8中旅订单撤单另外逻辑 modify by chenkeming
             */
            if (0 < channelType && !order.isCtsHK()) {

                CancelExRoomOrderResponse cancelExRoomOrderResponse = null;
                MGExResult mgresult = null;
                //生产bug直联未成功确认订单 不调用HDL取消方法 modify by chenjiajie 2009-09-24
                if(null != order && StringUtil.isValidStr(order.getOrderCdForChannel())){
                    CancelExRoomOrderRequest cancelExRoomOrderRequest = new CancelExRoomOrderRequest();
                    cancelExRoomOrderRequest.setChannelType(channelType);
                    cancelExRoomOrderRequest.setHotelId(order.getHotelId());
                    cancelExRoomOrderRequest.setChannelCode(String.valueOf(channelType));
                    cancelExRoomOrderRequest.setOrderId(orderId);
                    cancelExRoomOrderRequest.setCancelReason(null);
                    cancelExRoomOrderRequest.setCancelMessage("guest reason, " + cancelMessage + " "
                        + guestCancelMessage);
                    try{
                    	cancelExRoomOrderResponse = hdlService.cancelExRoomOrder(cancelExRoomOrderRequest);
                    }catch(Exception eCancel){
                    	log.error("=================OrderOperateAction.save()==hdlService.cancelExRoomOrder() exception : ", eCancel);
                    	
                    	order = getOrder(orderId);
                    	// 取消订单失败,则增加操作日志 add by chenkeming@2009-04-21
                        OrHandleLog handleLog = new OrHandleLog();
                        handleLog.setModifierName(roleUser.getName());
                        handleLog.setModifierRole(roleUser.getLoginName());
                        handleLog.setBeforeState(order.getOrderState());
                        handleLog.setAfterState(order.getOrderState());
                        Map channelMap = resourceManager.getDescription("select_cooperator");
                        handleLog.setContent("<font color='red'>合作方"+channelMap.get(String.valueOf(channelType))
                            + "订单取消失败!请发送取消传真至酒店！</font>");
                        handleLog.setModifiedTime(new Date());
                        handleLog.setOrder(order);
                        order.getLogList().add(handleLog);
                        
                        bCancelMsg.append("<font color='red'>")
                        		  .append("合作方").append(channelMap.get(String.valueOf(channelType)))
                                  .append("订单取消失败!请发送取消传真至酒店！")
                        		  .append("</font>");
                        request.setAttribute("bCancelMsg", bCancelMsg.toString());
                        orderService.saveOrUpdate(order);
                        return "cancel_alert";
                    }
                    
                    if (null != cancelExRoomOrderResponse) {
                        mgresult = cancelExRoomOrderResponse.getResult();
                        if (1 == mgresult.getValue()) {
                            // 取消订单成功  则增加操作日志 add by chenkeming@2009-04-21
                            OrHandleLog handleLog = new OrHandleLog();
                            handleLog.setModifierName(roleUser.getName());
                            handleLog.setModifierRole(roleUser.getLoginName());
                            handleLog.setBeforeState(order.getOrderState());
                            handleLog.setAfterState(order.getOrderState());
                            Map channelMap = resourceManager.getDescription("select_cooperator");
                            handleLog.setContent("合作方"+channelMap.get(String.valueOf(channelType))
                                + "订单取消成功！" );
                            handleLog.setModifiedTime(new Date());
                            handleLog.setOrder(order);
                            order.getLogList().add(handleLog);
                            
                            bCancelMsg.append("合作方").append(channelMap.get(String.valueOf(channelType)))
                                      .append("订单取消成功！");
                            		 
                        } else {
                            // 取消订单失败,则增加操作日志 add by chenkeming@2009-04-21
                            OrHandleLog handleLog = new OrHandleLog();
                            handleLog.setModifierName(roleUser.getName());
                            handleLog.setModifierRole(roleUser.getLoginName());
                            handleLog.setBeforeState(order.getOrderState());
                            handleLog.setAfterState(order.getOrderState());
                            Map channelMap = resourceManager.getDescription("select_cooperator");
                            handleLog.setContent("<font color='red'>合作方"+channelMap.get(String.valueOf(channelType))
                                + "订单取消失败:" + mgresult.getMessage()+"请发送取消传真至酒店！</font>");
                            handleLog.setModifiedTime(new Date());
                            handleLog.setOrder(order);
                            order.getLogList().add(handleLog);
                            
                            bCancelMsg.append("<font color='red'>")
                            		  .append("合作方").append(channelMap.get(String.valueOf(channelType)))
                                      .append("订单取消失败:")
                            		  .append(mgresult.getMessage())
                            		  .append("请发送取消传真至酒店！")
                            		  .append("</font>");
                        }
                    } else {
                        // 取消订单失败
                    }
                }
            }
            /**
             * 酒店直联取消定单 end
             * 
             * @author guojun 2008-12-10 17:40
             */
            else if (order.isCtsHK()) { // v2.8 中旅酒店芒果单撤单,要rollback相应中旅订单
                for (OrChannelNo orderChannel : order.getChannelList()) {
                    try {
                        if (TxnStatusType.Rollbacked != orderChannel.getStatus()) {
                            BasicData retRoll = hkService.saleRollback(orderChannel
                                .getOrderChannel());
                            if (retRoll.getNRet() < ResultConstant.RESULT_SUCCESS) {
                                log.error("回滚中旅订单失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                                    + orderChannel.getOrderChannel() + ", WebService错误代码: "
                                    + retRoll.getNRet() + ", 错误信息:" + retRoll.getSMessage());
                            }
                        }
                    } catch (Exception e) {
                    	log.error(e.getMessage(),e);
                    }
                }
            }

            // 解锁订单
            OrLockedOrders lockedOrders = new OrLockedOrders();
            lockedOrders.setOrderId(orderId);
            lockedOrderService.deleteLockedOrder(lockedOrders);
            //取消完后，再进行分配订单
            user = getOnlineWorkStates();
        	orderService.getMidOrderTransfer().autoAssignOrder(user);
        	request.setAttribute("bCancelMsg", bCancelMsg.toString());
            return "cancel_alert";
        } else { // 114前台取消
            String isFrontCancel = (String) getParams().get("isFrontCancel");
            boolean bFrontCancel = isFrontCancel.equals("1") ? true : false;
            if (bFrontCancel) {
                order.setFrontCancel(true);
            }
        }

        // 处理暂存订单和网站未扣配额订单，需要获取配额
        String sDeductQuota = (String) params.get("isDeductQuota");
        if ((order.getOrderState() == OrderState.NOT_SUBMIT
            || (StringUtil.isValidStr(sDeductQuota) && "1".equals(sDeductQuota)))
            && 0 >= order.getOrderItems().size()) {
            if (!order.isManualOrder()) {
                // 获取配额
                if (orderService.deductOrderQuota(order, null, null, order.getQuotaTypeOld())) {
                    // 获取配额成功
                    order.setQuotaOk(true);
                    //系统自动判断是否满足即时确认条件 addby juesuchen 2009-12-25 begin
                    params.put("firstSubmit","1");
                    order.setInstantConfirm(OrderUtil.popDialogBox(order, params));
                    //系统自动判断是否满足即时确认条件 addby juesuchen 2009-12-25 end
                    orderService.calculateTotalAmount(order);
                } else { // 获取配额失败
                    order.setQuotaOk(false);
                    orderService.calculateTotalAmount(order);
                }
            } else { // 如果是手工单
                orderService.getManualOrderQuota(order, 0, 0);
                order.setQuotaOk(false);
            }
            // 写日志
            OrHandleLog handleLog = new OrHandleLog();
            handleLog.setModifierName(roleUser.getName());
            handleLog.setModifierRole(roleUser.getLoginName());
            handleLog.setBeforeState(order.getOrderState());
            handleLog.setAfterState(order.getOrderState());
            handleLog.setContent("保存网站订单并扣配额");
            handleLog.setModifiedTime(new Date());
            handleLog.setHisNo(order.getHisNo());
            handleLog.setOrder(order);
            order.getLogList().add(handleLog);

            // 如果是新建订单或者暂存前台订单则修改订单状态为前台已提交
            if (order.isNewOrder() || order.getOrderState() <= OrderState.HAS_SUBMIT) {
                order.setOrderState(OrderState.HAS_SUBMIT);
            }
            
            //如果是网站来源，并且是直联订单，并且会员不为空，则计算返现--------add by longkangfu---------------
            if("NET".equals(order.getSource()) && order.getType() ==1 && order.getChannel()>0 && 
            		!MemberInterfaceService.COMMONMEMBERCD.equals(order.getMemberCd())){
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
				saveCashInformation(order);
            }
			
        } else if (!order.isPrepayOrder()
            && bChangeFellow
            && !(order.getOrderState() >= OrderState.CHECKIN
                && order.getOrderState() <= OrderState.EXTEND || 
                order.getOrderState() >= OrderState.NOSHOW)) {
            /** v2.4.2 如果是前台已提交||已提交中台订单，需要更新OrOrderItem的入住人信息 by chenjiajie 2008-12-31 **/
            // modify by chenkeming@2009-03-02 只有修改了入住人以及还没开始日审的面付单才更新orderItem的入住人信息
            OrderUtil.modifyOrderItem(order);
        } 
        
        if(order.getRmpOrder() && order.getOrderState() == OrderState.NOT_SUBMIT){  //处理API下单，暂存前台无法提交
        	 order.setOrderState(OrderState.HAS_SUBMIT);
        }
        /** v2.7.1 保存给客人发信息的备注数据 by chenjiajie 2009-02-18 begin **/
        // log.info("hidMemberConfirmNotes:"+params.get("hidMemberConfirmNotes"));
        String hidMemberConfirmNotes = (String) params.get("hidMemberConfirmNotes");
        memberConfirmService.updateSmsNotes(hidMemberConfirmNotes);
        /** v2.7.1 保存给客人发信息的备注数据 by chenjiajie 2009-02-18 end **/

        String errMsg = "";

        
        // 视情况创建预授权工单(预付单有信用卡支付方式或者面付担保单)
        boolean bAuth = false;
        String sid = orderService.getOrderExtInfoByType(order, BaseConstant.ORDER_EXTINFO_SID);
        if (order.isPrepayOrder() && this.isNotifyBalance()) {
            // 预付单
            List<OrPayment> payments = order.getPaymentList();
            for (int i = 0; null != payments && i < payments.size(); i++) {
                OrPayment payment = payments.get(i);
                if (payment.isNeedCreditCard()) {
                    String orderCode = order.getOrderCD();
                    double preAuthAmount = payment.getMoney();
                    String loginName = roleUser.getLoginName();
                    String creditCardIds = order.getCreditCardIdsSelect();
                    String description = "酒店修改订单生成";
                    String prePayType = "1";
                    try {
						String result = this.creditcardPreAuthService
								.createPreAuthList(orderCode, "HOTEL",
										preAuthAmount, member.getMembercd(), loginName,
										creditCardIds, sid, description, prePayType,order.getActualPayCurrency());
                        if (result.equals("succeed") || result.equals("orderCd existed")) {
                            order.setSuretyState(GuaranteeState.PREAUTH);
                            bAuth = true;
                        } else {
                            order.setSuretyState(GuaranteeState.PROCESSING);
                        }

                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        } else if (order.isCreditAssured() && bCanCreatePreAuth && order.canPreAuth()
            && StringUtil.isValidStr(order.getCreditCardIdsSelect())) {
            // 面付单
    		try {
    			String isSucceed = creditcardPreAuthService.createPreAuthList(
						order.getOrderCD(), "HOTEL", (double) order.getSuretyPrice(),
						member.getMembercd(), roleUser.getLoginName(),
						order.getCreditCardIdsSelect(),sid, "酒店前台生成", "2",order.getActualPayCurrency());
                if (isSucceed.equals("succeed")) {
                    order.setSuretyState(GuaranteeState.PREAUTH);
                    OrderUtil.addPreAuthCard(order, getParams());
                    bAuth = true;
                } else {
                    order.setSuretyState(GuaranteeState.PROCESSING);
                }
            } catch (Exception re) {
                log.error(re.getMessage(), re);
                errMsg += " 创建预授权工单失败!";
            }
        }

        // 增加修改金额
        if (0.001 < modifyPrice) {
            OrderUtil.addModifyMoney(order, modifyPrice, roleUser);
        }

        // 视情况创建配送单
        if (bCanCreateFul) {
            if (!orderAssist.createDeliveryBill(order, member, roleUser, order.getOrderCD())) {
                order.setFulfillmentCD(null);
                errMsg += " 创建配送单失败!";
            }
        }

        // 修改订单提交担保单时的金额列表处理,同时保存订单
        orderEditService.updateOrderMoney(order, roleUser, oriCreditAssure, sysAssureMoney,
            oriAssureMoney, bAuth);
        /****
         * 将本地的订单保存到数据库后 orderChannelModify将信息带到hotelOrderAlert.jsp页面 允许将修改单传给合作方
         */
        if (0 < channelType) {
            isModify = orderService.queryHotelDirectionModify(order.getHotelId().toString());
            if (null == isModify) { // 如果为空,默认为不能修改.
                isModify = "0";
            }
            request.setAttribute("isModify", isModify);
            request.setAttribute("orderChannelModify", "OK");
        }
        
        //代理保存修改订单,同时更新到B2BMODIFYORDER表中数据 add by haibo.li 2010-1-17
        orderService.saveB2BOrderWithOrderState(order.getOrderCD(), bCancel);
        
        /** 如果要提交到房控疑难组v2.8.1 by chenjuesu 2009-05-27 begin */
        String commitToRSC = (String) params.get("commitToRSC");
        if (StringUtil.isValidStr(commitToRSC) && "commitToRSC".equals(commitToRSC)) {
            // 合约组处理完后转交给房控疑难组
            contractGroupCommitToRSC(order, roleUser);
        }
        /** end */
        
        saveOrUpdateOrder(order);
        
        
        /** 增加担保更改信用卡卡号 v2.9.3 add by shaojun.yang 2009-09-16**/
        String isChangeCredit=(String) params.get("isChangeCredit");
        boolean bChangeCredit = (StringUtil.isValidStr(isChangeCredit) && isChangeCredit.equals("1")) ? true : false;
        if(bChangeCredit){
            String creditCardIds=params.get("creditCardIds").toString();
            String loginName = roleUser.getLoginName();
            String description = "重新生成预授权担保";
            String prePayType = "2";
            double preAuthAmount=order.getSuretyPrice();
                String result=creditcardPreAuthService.createPreAuthList(order.getOrderCD(), "HOTEL", preAuthAmount, member.getMembercd(), loginName, creditCardIds, sid, description, prePayType,order.getActualPayCurrency());
                //ChangeCreditCard(order.getOrderCD(),creditCardIds);
                log.info("更改信用卡 "+result);
        }
        /** end **/
        
        /**增加预付单更改信用卡卡号 add by shaojun.yang 2010-03-09**/
        String prepayChangeCredit=(String) params.get("prepayChangeCredit");
        boolean isPrepayChangeCredit = (StringUtil.isValidStr(prepayChangeCredit) && prepayChangeCredit.equals("1")) ? true : false;
        if(isPrepayChangeCredit){
        	 List<OrPayment> payments = order.getPaymentList();
             for (int i = 0; null != payments && i < payments.size(); i++) {
                 OrPayment payment = payments.get(i);
                 if (payment.isNeedCreditCard()) {
                     String orderCode = order.getOrderCD();
                     double preAuthAmount = payment.getMoney();
                     String loginName = roleUser.getLoginName();
                     String creditCardIds=params.get("creditCardIds").toString();
                     String description = "重新生成预授权预付单";
                     String prePayType = "1";
                     try {
  						String result = this.creditcardPreAuthService
  								.createPreAuthList(orderCode, "HOTEL",
  										preAuthAmount, member.getMembercd(), loginName,
  										creditCardIds, sid, description, prePayType,order.getActualPayCurrency());
  						log.info("更改信用卡 "+result);
                      } catch (Exception e) {
                          log.error(e.getMessage(), e);
                      }
                 }
             }
        }
        /**end**/

        // 解锁订单
        OrLockedOrders lockedOrders = new OrLockedOrders();
        lockedOrders.setOrderId(orderId);
        lockedOrderService.deleteLockedOrder(lockedOrders);
        
        /** 视情况是否调用代金券接口 hotel2.9.3 add by chenjiajie 2009-09-02 begin **/
        useVoucher(params, errMsg);
        /** 视情况是否调用代金券接口 hotel2.9.3 add by chenjiajie 2009-09-02 end **/
        
        // 如果以上调用ejb接口操作有错误
        if (StringUtil.isValidStr(errMsg)) {
            return forwardError(errMsg);
        }

        // 获取订单汇率字符串，供保存成功页面使用
        idCurStr = CurrencyBean.idCurMap.get(order.getPaymentCurrency());
        request.setAttribute("rateCurrency", order.getRateId());

        // 设置芒果会员信息，供保存成功页面使用
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

        // 获取酒店默认联系方式,供保存成功页面使用
        ctcttype = hotelService.getHotelSendType(order.getHotelId().toString());
        if(order.getRmpOrder()){
        	if(null!=getAffirmFacade()){
        	boolean isEmail=false;
        	boolean isFax=false;
        	ctcttype="";
        	List<AffirmDTO> arrirmList=getAffirmFacade().queryAffirmByPPID(order.getOrOrderRMP().getPricePlanId(),null, null);
        	if(null!=arrirmList&&arrirmList.size()>0){
        		for(AffirmDTO affirm:arrirmList){
        			if(affirm.isBetweenDate()){
        				if(affirm.getAffirmType()==1){
        					isEmail=true;
        					ctcttype="01";
        				}
        				if(affirm.getAffirmType()==2){
        					isFax=true;
        					ctcttype="02";
        				}
        			}
        		}
        		if(isEmail&&isFax){
        			ctcttype="03";
        		}
        	}
        }else{
        	ctcttype="04";
        }
        }   
        //RMS3007底价/售价的操作日志 add by chenjiajie 2010-01-04
        logShowBasePrice();
        if(order.isStayInMid())
    		orderGroupId=orderService.getOrderGroup(order.getID());
        if(!bFromFront && !order.isStayInMid()){//如果不是从前台进入，且标记不在中台，则再进行分配订单
        	user = getOnlineWorkStates();
        	orderService.getMidOrderTransfer().autoAssignOrder(user);
        }
        return PROCESS_ORDER;
    }

    /**
     * 调用代金券接口
     * @param params
     * @param errMsg
     */
    private void useVoucher(Map params, String errMsg) {
        String couponUsed = (String) params.get("couponUsed"); //编辑页面使用了代金券的标志
        if(StringUtil.isValidStr(couponUsed) && Boolean.parseBoolean(couponUsed)){
            try {
                //调用代金券接口试预订确认接口
                int vchResult = voucherInterfaceService.callVchServicePreOrder(order, roleUser, member, IVoucherInterfaceService.CHANNEL_CC);   
                //调用接口内部返回出错
                if(vchResult == 0){
                    errMsg += " 代金券支付失败!原因：接口内部错误。";
                    log.info(" vch005 代金券支付失败!原因：接口内部错误。OrderCD: "+order.getOrderCD());
                }else{
                    //确认使用代金券的方法 hotel2.9.3 add by chenjiajie 2009-09-15
                    voucherInterfaceService.confirmVoucherState(order, roleUser, member);
                    orderService.saveOrUpdate(order);
                }
            } catch (VCHException e) {
                voucherInterfaceService.rollBackVchOrderState(order, e.getMessage());
                errMsg += " 代金券支付失败!原因："+e.getMessage();
                log.error(" vch006 代金券支付失败!原因："+e.getMessage()+" OrderCD: "+order.getOrderCD(),e);
                log.error(e.getMessage(),e);
            }
        }
    }

    /**
     * 合约组处理完后转交给房控疑难组 v2.8.1 by juesu.chen 2009-06-02
     */
    private void contractGroupCommitToRSC(OrOrder tmpOrder, UserWrapper userWrapper) {
        // TODO Auto-generated method stub
        // 设置房控组
        tmpOrder.setContractlog(null);
        // 实时分配给房控疑难人员
        /** 新房控部门分配疑难订单更改 by juesuchen 2010-1-20 begin **/
        //List resultLst = hraManager.batchMoveToRSC(String.valueOf(tmpOrder.getID()), userWrapper,false);
        //List resultLst = hraManager.batchMoveToRSCNew(String.valueOf(tmpOrder.getID()), userWrapper,false);
        /** 新房控部门分配疑难订单更改 by juesuchen 2010-1-20 end **/
        // 修改转和约组记录
        // List contractgroups = orOrderDao.queryByNamedQuery("queryOrToContractByOrderId", new
        // Object[]{orderId});
        // OrToContractgroup contractgroup = (OrToContractgroup)contractgroups.get(0);
        OrToContractgroup contractgroup = orderService
            .getContractgroup(tmpOrder.getID());
        if (null == contractgroup) {
            log.error("contractGroupCommitToRSC(),找不到OrToContractgroup对象,订单编号:"
                + tmpOrder.getOrderCD());
        }
        contractgroup.setToroomcontime(new Date());
        contractgroup.setContracter(userWrapper.getName());
        orderService.toContractGoup(contractgroup);
        // 写日志，
        String logContext = "合约组转回疑难订单给房控组";
        /*if (resultLst.isEmpty()) {
            logContext = "订单转房控疑难组成功！";
        } else {
            if (resultLst.contains(Integer.valueOf(0))) { // 0即不存在该订单
                logContext = "订单转房控疑难组失败！原因如下：订单不存在。";
            } else { // 返回是那个RSC组无人Open状态
                logContext = "可能有部分订单转房控疑难组失败！原因如下：";
                for (Iterator it = resultLst.iterator(); it.hasNext();) {
                    Integer tempResult = (Integer) it.next();
                    switch (tempResult.intValue()) {
                    case HraOrderType.RSC_BBQ:
                        logContext += "RSC深圳组 无人Open状态。";
                        break;
                    case HraOrderType.RSC_GZQ:
                        logContext += "RSC广州组 无人Open状态。";
                        break;
                    case HraOrderType.RSC_GAZ:
                    	logContext += "RSC港澳组 无人Open状态。";
                        break;
                    case HraOrderType.RSC_SHZ:
                    	logContext += "RSC上海组 无人Open状态。";
                        break;
                    case HraOrderType.RSC_JJZ:
                    	logContext += "RSC江浙组 无人Open状态。";
                        break;
                    case HraOrderType.RSC_BJZ:
                    	logContext += "RSC北京组 无人Open状态。";
                        break;
                    case HraOrderType.RSC_DBZ:
                    	logContext += "RSC北方组 无人Open状态。";
                        break;
                    }
                }
            }
        }*/
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setModifierName(userWrapper.getName());
        handleLog.setModifierRole(userWrapper.getLoginName());
        handleLog.setBeforeState(order.getOrderState());
        handleLog.setAfterState(order.getOrderState());
        handleLog.setContent(logContext);
        handleLog.setModifiedTime(new Date());
        handleLog.setOrder(order);
        tmpOrder.getLogList().add(handleLog);
        
        /** 让此订单在房控组执行分配 Addby juesuchen 2010-3-30 begin **/
        orderService.getMidOrderTransfer().orderTransTo(roleUser, orderId, 8, 99);// 6 代表 专家组,原因传入 99，特指是合约组转房控
        /** 让此订单在房控组执行分配 Addby juesuchen 2010-3-30 begin **/
        

    }

    /**
     * 关闭订单
     * 
     * @return
     */
    public String unlock() {

        roleUser = getOnlineRoleUser();
        OrLockedOrders lockedOrders = new OrLockedOrders();
        lockedOrders.setOrderId(orderId);
        OrLockedOrders lockedOrder = lockedOrderService.loadLockedOrder(lockedOrders);
        // 解锁订单
        if (null != lockedOrder) {
            String locker = lockedOrder.getLocker();
            if (roleUser.isOrgMidAdmin() || roleUser.getLoginName().equals(locker)) {
                lockedOrderService.deleteLockedOrder(lockedOrders);
            }
        }
        order = getOrder(orderId);

        Map params = getParams();
        String isFromFront = (String) params.get("isFromFront");
        if (StringUtil.isValidStr(isFromFront) && isFromFront.equals("1")) {
            unlockURL = (String) getFromSession("originalOperate");
            if (StringUtil.isValidStr(unlockURL)) {
                putSession("originalOperate", null);
                if (0 < unlockURL.indexOf("paginateQueryOrder.action?")
                    || 0 < unlockURL.indexOf("paginateFrontNotOper.action?")) {
                    Map ecSession = (Map) getFromSession("HotelOrderManaSearchECTable");
                    if (null != ecSession) {
                        String tempStr = (String) ecSession.get("ec_p");
                        if (StringUtil.isValidStr(tempStr)) {
                            unlockURL += "&ec_p=" + tempStr;
                        }
                        tempStr = (String) ecSession.get("ec_crd");
                        if (StringUtil.isValidStr(tempStr)) {
                            unlockURL += "&ec_crd=" + tempStr;
                        }
                        tempStr = (String) ecSession.get("ec_rd");
                        if (StringUtil.isValidStr(tempStr)) {
                            unlockURL += "&ec_rd=" + tempStr;
                        }
                        tempStr = (String) ecSession.get("ec_pg");
                        if (StringUtil.isValidStr(tempStr)) {
                            unlockURL += "&ec_pg=" + tempStr;
                        }
                        tempStr = (String) ecSession.get("ec_totalpages");
                        if (StringUtil.isValidStr(tempStr)) {
                            unlockURL += "&ec_totalpages=" + tempStr;
                        }
                        tempStr = (String) ecSession.get("ec_totalrows");
                        if (StringUtil.isValidStr(tempStr)) {
                            unlockURL += "&ec_totalrows=" + tempStr;
                        }
                        unlockURL += "&";
                    }
                } else {
                    member = getOnlineMember();
                }
                return "unlock";
            }
            member = getOnlineMember();
            String alertMessage = "关闭订单成功！";
            String[] btns = { "返回" };
            String[] urls = { "/order/hotelSearch!searchPre.action" };
            return forwardMsgEx(alertMessage, btns, urls, false);
        } else {
            request.setAttribute("selfClose", "1");
            return super.forwardMsgBox("关闭订单成功！", null);
        }
    }

    public String refreshSelf() {
        order = getOrder(orderId);
        orderSts=operOrderDerferTimeService.validateOrderSts(orderId);
        if (null == order) {
            return forwardError("order对象不存在!");
        }
        member = getOnlineMember();
        if (null == member || !member.getMembercd().equals(order.getMemberCd())) {
            member = getMemberSimpleInfo(order.getMemberCd(), false);
        }
        if(order.isStayInMid())
        	orderGroupId=orderService.getOrderGroup(order.getID());
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

        return "refreshSelf";
    }

    /**
     * 删除未提交订单 并未真正删除，仅置illusive为真
     * 
     * @return
     */
    public String updateOrderIllusiveFlag() {
        roleUser = getOnlineRoleUser();
        order = getOrder(orderId);
        member = getOrderMember(order);
        if (null == member) {
            return forwardError("会员session过期,请重新获取会员信息");
        }
        if (roleUser.isOrgMid()) {
            order.setModifiedMidTime(new Date());
        } else if (roleUser.isOrgFront()) {
            order.setModifiedFrontTime(new Date());
        }
        order.setIllusive(true);
        saveOrUpdateOrder(order);
        return "frontNotSubmit";
    }

    /**
     * 解锁订单
     * 
     * @return
     */
    public String unLockOrder() {
        roleUser = getOnlineRoleUser();
        order = getOrder(orderId);

        // 解锁订单,所有操作员均可解锁订单
        OrLockedOrders lockedOrders = new OrLockedOrders();
        lockedOrders.setOrderId(orderId);
        lockedOrderService.deleteLockedOrder(lockedOrders);

        return super.forwardMsgBox("解锁订单成功！", "refreshSelf()");
    }

    /**
     * 批解锁订单(只有中台管理员可以操作)
     * 
     * @return
     */
    public String unLockOrders() {
        String setIDs = (String) getParams().get("setIDs");
        // 解锁订单
        lockedOrderService.deleteLockedOrders(setIDs);

        return super.forwardMsgBox("解锁订单成功！", "refreshSelf()");
    }

    /**
     * 查看会员资料
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String readVIP() {
        Map params = getParams();
        String vvipname = params.get("vvipname").toString().trim();

        log.info(vvipname);
        String[] retVip = vvipname.split("##");
        log.info(retVip[0]);
        return "readVip";
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

    /***
     * 将修改后的直联酒店的订单发送给合作方 channel 合作方, order 修改后的订单信息 return修改后的状态. hotel 2.5 author guojun
     */
    public String transferModifyOrderToChannel() {

        int channelType = 0;
        if (false == "".equals(channel.trim())) {
            channelType = Integer.valueOf(channel);
        }

        // 获取订单对象
        order = getOrder(orderId);

        String errMsg = "";
        if (order.getOrderState() == OrderState.CANCEL) {
            errMsg = "订单已经撤消,不能将已经撤消的单发送给合作方!! ";
            request.setAttribute("errMsg", errMsg);
            return "transferModifyToChannel";
        }

        /**
         * 酒店直联修改定单 start
         * 
         * @author guojun 2008-12-10 17:48
         */
        ModifyExRoomOrderResponse modifyExRoomOrderResponse = null;
        MGExResult mgresult = null;
        if (!order.isManualOrder()) {
            if (0 < channelType) {
                /**
                 *如果isModify不为空，表示允许修改定单，就直接调用修改定单
                 */
                isModify = orderService.queryHotelDirectionModify(order.getHotelId().toString());
                if (null != isModify) {
                    if (isModify.equals("1")) {
                        ModifyExRoomOrderRequest modifyExRoomOrderRequest = 
                            new ModifyExRoomOrderRequest();
                        modifyExRoomOrderRequest.setChannelType(Integer.valueOf(channel));
                        modifyExRoomOrderRequest.setHotelId(order.getHotelId().intValue());
                        /**
                         * 渠道方 hotelCode
                         */
                        MGExOrder exRoomOrder = new MGExOrder();
                        exRoomOrder.setCheckindate(DateUtil.toStringByFormat(
                            order.getCheckinDate(), "yyyy-MM-dd"));
                        exRoomOrder.setCheckoutdate(DateUtil.toStringByFormat(order
                            .getCheckoutDate(), "yyyy-MM-dd"));
                        exRoomOrder.setRoomquantity(order.getRoomQuantity());
                        exRoomOrder.setPaymethod(order.getPayMethod());
                        exRoomOrder.setCurrency(order.getPaymentCurrency());
                        exRoomOrder.setTotalamount(Double.valueOf(order.getSum()).floatValue());
                        exRoomOrder.setLocaltotalamout(Double.valueOf(
                            order.getSumRmb()).floatValue());

                        exRoomOrder.setExchangerate(Double.valueOf( 
                            order.getRateId()).floatValue());
                        exRoomOrder.setArrivaltime(order.getArrivalTime());
                        exRoomOrder.setLatestarrivaltime(order.getLatestArrivalTime());
                        exRoomOrder.setNosmoking(null);
                        exRoomOrder.setHotelnotes(order.getRemark().getHotelRemark());
                        /**
                         * 特殊要求
                         */
                        exRoomOrder.setSpecialrequest(order.getSpecialRequest());
                        exRoomOrder.setLinkman(order.getLinkMan());
                        // 子房型ID
                        exRoomOrder.setPricetypecode(order.getChildRoomTypeId().intValue());
                        exRoomOrder.setTitle(order.getTitle());
                        exRoomOrder.setMobile(order.getMobile());
                        exRoomOrder.setTelephone(order.getTelephone());
                        exRoomOrder.setCustomerfax(order.getCustomerFax());
                        exRoomOrder.setEmail(order.getEmail());
                        exRoomOrder.setArrivaltraffic(order.getArrivalTraffic());
                        exRoomOrder.setFlight(order.getFlight());
                        exRoomOrder.setFellownames(order.getFellowNames());
                        exRoomOrder.setAdultnum(order.getFellowList().size());
                        exRoomOrder.setPaysatus(String.valueOf(order.getPayState()));
                        exRoomOrder.setOrderstate(String.valueOf(order.getOrderState()));
                        exRoomOrder.setHotelid(order.getHotelId());
                        exRoomOrder.setHotelname(order.getHotelName());
                        exRoomOrder.setRoomtypeid(order.getRoomTypeId().intValue());
                        exRoomOrder.setRoomtypecode(order.getRoomTypeId().intValue());
                        exRoomOrder.setRoomtypename(order.getRoomTypeName());
                        exRoomOrder.setBedtype(order.getBedType());
                        exRoomOrder.setIsguarantee(String.valueOf((order.getIsCreditAssured())));
                        exRoomOrder.setGuaranteetype("credit");
                        exRoomOrder.setCreditcardtype("credit card type");
                        exRoomOrder.setCreditcardname("credit");
                        exRoomOrder.setCreditcardno(null);
                        exRoomOrder.setCreditcardexpires(null);
                        /**
                         * 传给中间层定单orderid与ordercdhotel
                         */
                        exRoomOrder.setOrderid(orderId);
                        exRoomOrder.setOrdercd(order.getOrderCD());
                        exRoomOrder.setOrdercdhotel(order.getOrderCDHotel());

                        if (null == order.getCreateDate()) {
                            exRoomOrder.setCreatedate(DateUtil.toStringByFormat(new Date(),
                                "yyyy-MM-dd HH:mm:ss"));
                        } else {
                            exRoomOrder.setCreatedate(DateUtil.toStringByFormat(order
                                .getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
                        }

                        List<OrOrderItem> orderItemList = order.getOrderItems();
                        for (Iterator itr = orderItemList.iterator(); itr.hasNext();) {
                            OrOrderItem orderItem = (OrOrderItem) itr.next();
                            MGExOrderItem mgExOrderItem = new MGExOrderItem();
                            mgExOrderItem.setNumber(orderItem.getRoomIndex());
                            mgExOrderItem.setCheckindate(DateUtil.toStringByFormat(orderItem
                                .getNight(), "yyyy-MM-dd"));
                            mgExOrderItem.setCheckoutdate(DateUtil.toStringByFormat(orderItem
                                .getNight(), "yyyy-MM-dd"));
                            mgExOrderItem.setGuests(order.getFellowNames());
                            if (null != orderItem.getRoomNo()) {
                                mgExOrderItem.setRoomno(Integer.valueOf(orderItem.getRoomNo()));
                            }
                            mgExOrderItem.setSum(Double.valueOf(
                                orderItem.getBasePrice()).floatValue());
                            mgExOrderItem.setBaseprice(Double.valueOf(
                                orderItem.getBasePrice()).floatValue());
                            mgExOrderItem.setSaleprice(Double.valueOf( 
                                orderItem.getSalePrice()).floatValue());
                            mgExOrderItem.setBaserate(Double.valueOf(
                                orderItem.getBasePrice()).floatValue());
                            // mgExOrderItem.setTotalcharges((float)orderItem.getServiceFee());
                            // mgExOrderItem.setSpecialnote(orderItem.getSpecialNote());
                            mgExOrderItem.setCreatetime(exRoomOrder.getCreatedate());
                            mgExOrderItem.setQuantity(1);
                            orderItem.setQuotaType(QuotaType.GENERALQUOTA);
                            mgExOrderItem.setSum(Double.valueOf(order.getSum()).floatValue());
                            mgExOrderItem.setNoteresult(orderItem.getNoteResult());
                            mgExOrderItem.setOrderid(orderId);
                            mgExOrderItem.setOrderstate(String.valueOf(order.getOrderState()));
                            mgExOrderItem.setHotelconfirm(null);
                            mgExOrderItem.setHotelconfirmid(null);
                            exRoomOrder.getExOrderItems().add(mgExOrderItem);
                        }
                        modifyExRoomOrderRequest.setRoomOrder(exRoomOrder);
                        try {
                            modifyExRoomOrderResponse = hdlService
                                .modifyExRoomOrder(modifyExRoomOrderRequest);
                        } catch (Exception e) {
                            errMsg = "酒店系统异常!";
                            log.error("===============OrderOperateAction.transferModifyOrderToChannel()==hdlService.modifyExRoomOrder() exception : ",e);
                        }
                        if (null != modifyExRoomOrderResponse) {
                            mgresult = modifyExRoomOrderResponse.getResult();
                            // 判断定单是否成功
                            if (1 == mgresult.getValue()) {
                                // 修改订单成功
                                mgresult.setMessage("订单修改成功!");
                            } else {
                                // 修改订单失败
                                errMsg += mgresult.getMessage();
                            }
                        }
                    }
                }
            }
        }
        /**
         * 酒店直联修改定单 end
         * 
         * @author guojun 2008-12-10 17:48
         */

        // 获取登录用户
        roleUser = getOnlineRoleUser();
        
        order = getOrder(orderId);
        // 写日志
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName());
        handleLog.setBeforeState(order.getOrderState());
        handleLog.setAfterState(order.getOrderState());
        if (1 < errMsg.length()) {
            request.setAttribute("errMsg", errMsg);
            Map channelMap = resourceManager.getDescription("select_cooperator");
            if(null== mgresult){
            	 handleLog.setContent("<font color='red'>合作方"+channelMap.get(String.valueOf(channelType))+"订单修改失败!"
 						+ "<br>请发送修改传真（邮件）至酒店</font>");	
            }else{
            	 handleLog.setContent("<font color='red'>合作方"+channelMap.get(String.valueOf(channelType))+"订单修改失败!原因：" + mgresult.getMessage()
 						+ "<br>请发送修改传真（邮件）至酒店</font>");	
            }
           
        } else {
            request.setAttribute("errMsg", null);
            handleLog.setContent("合作方订单修改成功!");
        }
        handleLog.setModifiedTime(new Date());
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);

        // 将日志保存到数据库
        saveOrUpdateOrder(order);

        return "transferModifyToChannel";
    }

    /** getter and setter begin */

    /**
     * HOTEL2.8.1 转合约组的Action
     * 
     * @author guojun 2009-05-19 16:22
     **/
    public String toContractGroup() {
        order = getOrder(orderId);

        // 获取登录用户
        roleUser = getOnlineRoleUser();

        // 获取OrToContractgroup对象
        OrToContractgroup orToContractgroup = orderService.getContractgroup(orderId);
        String toContractRemark = (String) getParams().get("toConRe");
        if (null != orToContractgroup) {
            orToContractgroup.setTocontracttime(new Date());
            orToContractgroup.setRoomcontroller(roleUser.getLoginName());
            orToContractgroup.setConsecond(toContractRemark);
            orderService.toContractGoup(orToContractgroup);
        } else { // 打印异常日志
            log.error("房控疑难单没找到相应的OrToContractgroup记录,订单编号:" + order.getOrderCD());
        }

        // 设置转合约组
        order.setContractlog(UserUtil.ORG_CONTRACTOR);
        order.setAssignTo(UserUtil.ORG_CONTRACTOR);
        order.setAssignToName(UserUtil.ORG_CONTRACTOR_NAME);

        // 写日志
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName());
        handleLog.setBeforeState(order.getOrderState());
        handleLog.setAfterState(order.getOrderState());
        handleLog.setContent("疑难订单已转合约组!转合约选项:" + ("1".equals(toContractRemark) ? "不成功N" : "传递"));
        handleLog.setModifiedTime(new Date());
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);
        // 将日志保存到数据库
        saveOrUpdateOrder(order);

        Long ret = null;

        // 发邮件给合约组
        String citycode = order.getCity();
        String toaddress = "";
        // 查询区域
        HtlArea htlArea = systemDataService.queryAreaCode(citycode);
        if (null != htlArea) {
            if (htlArea.getAreaCode().equals(AreaType.BBQ)) {
                toaddress = "Hotel-Hnsz";
            } else if (htlArea.getAreaCode().equals(AreaType.HDQ)) {
                toaddress = "Hotel_HuaDong";
            } else if (htlArea.getAreaCode().equals(AreaType.HBQ)) {
                toaddress = "Hotel_HuaBei";
            } else if (htlArea.getAreaCode().equals(AreaType.GZQ)) {
                toaddress = "Hotel_GuangZhou";
            }else if(htlArea.getAreaCode().equals(AreaType.GAZ)){
            	toaddress = "hotel-hnhk";
            }
            // 根据酒店ID查询酒店具体信息
            HtlHotel htlHotel = hotelService.findHotel(order.getHotelId());
            if (null == htlHotel) {
                log.info("查询不到相应的酒店,不能发送合约组邮件!");
            }
            String hotelAddress = "";
            if (null != htlHotel.getState()) {
                hotelAddress += InitServlet.localProvinceObj.get(htlHotel.getState());
            }

            if (null != htlHotel.getCity()) {
                hotelAddress += InitServlet.localCityObj.get(htlHotel.getCity());
            }
            if (null != htlHotel.getZone()) {
                hotelAddress += InitServlet.citySozeObj.get(htlHotel.getZone());
            }
            if (null != htlHotel.getBizZone()) {
                hotelAddress += InitServlet.businessSozeObj.get(htlHotel.getBizZone());
            }
            hotelAddress += htlHotel.getChnName();
            Mail mail = new Mail();
            mail.setApplicationName("hotel");
            String templateNo = FaxEmailModel.NOTIFY_EMAIL_CONTRACTOR;
            mail.setTo(new String[] { toaddress });
            String title = "疑难单--" + order.getOrderCD() + " " + hotelAddress;
            log.info("疑难单标题:" + title);
            mail.setSubject(title);

            mail.setTemplateFileName(templateNo);
            mail.setFrom("cs@mangocity.com");
            mail.setUserLoginId(roleUser.getLoginName());
            String xmlString = genMailContractor(order);
            mail.setXml(xmlString);

            try {
                ret = communicaterService.sendEmail(mail);
            } catch (Exception e) {
                log.info("转合约组邮件发送失败！ " + e);
            }
            if (null == ret || 0 >= ret) {
                log.info("转合约组邮件发送失败");
            }
        } else {
            log.info("hotelArea不能为空！ ");
        }

        return forwardMsgBox("订单成功转移到合约组！", "forwardToMyList()");

    }
    /**
     * 魅影扣取订单配额
     * @param order
     * @param tempOrder
     * @param roleUser
     * @return
     */
    public String  showShadowQuota(){
    	order = getOrder(orderId);
    	List<OrOrderItem> items=order.getOrderItems();
    	if(items.size()>0){
        	quotaType =items.get(0).getQuotaType();
    	}
    	List<Long> pricePlanIds=new ArrayList<Long>();
    	pricePlanIds.add(order.getOrOrderRMP().getPricePlanId());  
    	if(null==getPricePlanRemote()){
    		return "showShadowQuota";
    	}
    	List<PricePlanResponseDTO> pricePlanList=getPricePlanRemote().queryByPricePlanIds(pricePlanIds);
    	Long quotaAccountID=null;
    	if(null!=pricePlanList&&pricePlanList.size()>0){
    		PricePlanResponseDTO pricePlanDTO=pricePlanList.get(0);
    		quotaAccountID=pricePlanDTO.getQuotaAccountId();
    	}    
    	if(null==quotaAccountID){
    		 return forwardMsg("该价格计划没有关联配额账户");
    	}
    	if(null!=getQuotaFacade()){
        	shadowQuotaList=getQuotaFacade().queryQuotasForShow(quotaAccountID,DateUtil.dateToString(order.getCheckinDate()), DateUtil.dateToString(DateUtil.getDate(order.getCheckoutDate(), -1)));   	
    	}else{
    		shadowQuotaList=Collections.EMPTY_LIST;
    	}
    	return "showShadowQuota";
    	
    }
    
    public String  showShadowRoomSta(){
    	order = getOrder(orderId);
    	List<OrOrderItem> items=order.getOrderItems();
    	if(items.size()>0){
        	quotaType =items.get(0).getQuotaType();
    	}
    	List<Long> pricePlanIds=new ArrayList<Long>();
    	pricePlanIds.add(order.getOrOrderRMP().getPricePlanId()); 
    	if(null==getPricePlanRemote()){
    		return "showShadowRoomSta";
    	}
    	List<PricePlanResponseDTO> pricePlanList=getPricePlanRemote().queryByPricePlanIds(pricePlanIds);
    	Long quotaAccountID=null;
    	if(null!=pricePlanList&&pricePlanList.size()>0){
    		PricePlanResponseDTO pricePlanDTO=pricePlanList.get(0);
    		quotaAccountID=pricePlanDTO.getQuotaAccountId();
    	}    
    	if(null==quotaAccountID){
    		 return forwardMsg("该价格计划没有关联配额账户");
    	}
    	if(null!=getQuotaFacade()){
        	shadowQuotaList=getQuotaFacade().queryQuotasForShow(quotaAccountID,DateUtil.dateToString(order.getCheckinDate()), DateUtil.dateToString(DateUtil.getDate(order.getCheckoutDate(), -1)));   	
    	}else{
    		shadowQuotaList=Collections.EMPTY_LIST;
    	}
    	return "showShadowRoomSta";
    	
    }
    
    
    
    public String deleteShadowQuota(){
    	order = getOrder(orderId);
        // 获取登录用户
    	Map params=super.getParams();
    	int datelength=DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
    	List<QuotaShowDTO> quotaShowList = MyBeanUtil.getBatchObjectFromParam(params, QuotaShowDTO.class, datelength);	
        roleUser = getOnlineRoleUser();
    	List<Long> pricePlanIds=new ArrayList<Long>();
    	pricePlanIds.add(order.getOrOrderRMP().getPricePlanId());
    	if(null==getPricePlanRemote()){
    		return "showShadowQuota";
    	}
    	List<PricePlanResponseDTO> pricePlanList=getPricePlanRemote().queryByPricePlanIds(pricePlanIds);
    	Long quotaAccountID=null;
    	if(null!=pricePlanList&&pricePlanList.size()>0){
    		PricePlanResponseDTO pricePlanDTO=pricePlanList.get(0);
    		quotaAccountID=pricePlanDTO.getQuotaAccountId();
    	} 
    	String alertMsg="";
    	if(quotaAccountID==null){
    		alertMsg="该订单所对应的价格计划没有关联配额账户，不能扣配！";
    		request.setAttribute("alertMsg", alertMsg);
    		OrHandleLog handleLog = new OrHandleLog();
        	handleLog.setModifierName(roleUser.getName());
            handleLog.setModifierRole(roleUser.getLoginName());
            handleLog.setContent("订单扣取配额失败");        
            handleLog.setModifiedTime(new Date());
            handleLog.setBeforeState(order.getOrderState());
            handleLog.setAfterState(order.getOrderState());
            handleLog.setOrder(order);
            order.getLogList().add(handleLog);
        	saveOrUpdateOrder(order);
        	showShadowQuota();
        	return "showShadowQuota";
    	}
    	
    	//查询是否有配额账户，或者是否有录配额
    	if(null!=getQuotaFacade()){
        	shadowQuotaList=getQuotaFacade().queryQuotasForShow(quotaAccountID,DateUtil.dateToString(order.getCheckinDate()), DateUtil.dateToString(DateUtil.getDate(order.getCheckoutDate(), -1)));   	
    	}else{
    		shadowQuotaList=Collections.EMPTY_LIST;
    	}
    	Boolean delQuotaIsTrue=true;
    	for(QuotaShowDTO QuotaShowDTO:shadowQuotaList){
    		if(!QuotaShowDTO.isContainQuota()){
    			delQuotaIsTrue=false; 
    			break;
    		}
    	}
    	if(!delQuotaIsTrue){
    		alertMsg="该订单所对应的价格计划没录配额，不能扣配！";
    		request.setAttribute("alertMsg", alertMsg);
    		OrHandleLog handleLog = new OrHandleLog();
        	handleLog.setModifierName(roleUser.getName());
            handleLog.setModifierRole(roleUser.getLoginName());
            handleLog.setContent("订单扣取配额失败");        
            handleLog.setModifiedTime(new Date());
            handleLog.setBeforeState(order.getOrderState());
            handleLog.setAfterState(order.getOrderState());
            handleLog.setOrder(order);
            order.getLogList().add(handleLog);
        	saveOrUpdateOrder(order);
        	showShadowQuota();
        	return "showShadowQuota";
    	}
		int num=order.getRoomQuantity();
		isDelQuoSuccess=false;
    	if(null!=getQuotaFacade()){
        	isDelQuoSuccess=getQuotaFacade().deductQuotaItems(quotaAccountID, DateUtil.dateToString(order.getCheckinDate()), DateUtil.dateToString(DateUtil.getDate(order.getCheckoutDate(), -1)), num,roleUser.getLoginName() );
    	}else{
    		shadowQuotaList=Collections.EMPTY_LIST;
    	}
    	
		OrHandleLog handleLog = new OrHandleLog();
    	handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName());
        
        if(isDelQuoSuccess){
        	handleLog.setContent("订单扣取配额成功");
    	}else{
        	handleLog.setContent("订单扣取配额失败");
    	}
        handleLog.setModifiedTime(new Date());
        handleLog.setBeforeState(order.getOrderState());
        handleLog.setAfterState(order.getOrderState());
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);

    	if(isDelQuoSuccess){
    		List<OrOrderItem> orderItems= order.getOrderItems();
    		for(OrOrderItem orderItem:orderItems){
    			orderItem.setQuotaType("1");
    		}
    	}
    	saveOrUpdateOrder(order);
    	showShadowQuota();
    	return "showShadowQuota";
    }
    
    
    
    public String updateRoomSta(){
    	//查询以前的DTO用语记录房态更改日志
    	order = getOrder(orderId);
    	List<OrOrderItem> items=order.getOrderItems();
    	if(items.size()>0){
        	quotaType =items.get(0).getQuotaType();
    	}
    	List<Long> pricePlanIds=new ArrayList<Long>();
    	pricePlanIds.add(order.getOrOrderRMP().getPricePlanId()); 
    	if(null==getPricePlanRemote()){
    		return "showShadowRoomSta";
    	}
    	List<PricePlanResponseDTO> pricePlanList=getPricePlanRemote().queryByPricePlanIds(pricePlanIds);
    	Long quotaAccountID=null;
    	if(null!=pricePlanList&&pricePlanList.size()>0){
    		PricePlanResponseDTO pricePlanDTO=pricePlanList.get(0);
    		quotaAccountID=pricePlanDTO.getQuotaAccountId();
    	}    
    	if(null==quotaAccountID){
    		 return forwardMsg("该价格计划没有关联配额账户");
    	}
    	if(null!=getQuotaFacade()){
        	shadowQuotaList=getQuotaFacade().queryQuotasForShow(quotaAccountID,DateUtil.dateToString(order.getCheckinDate()), DateUtil.dateToString(DateUtil.getDate(order.getCheckoutDate(), -1)));   
    	}else{
    		shadowQuotaList=Collections.EMPTY_LIST;
    	}
    	
    	//设置要更改房态的DTO
    	Map params=super.getParams();
    	int datelength=DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
    	List<QuotaShowDTO> quotaShowList = MyBeanUtil.getBatchObjectFromParam(params, QuotaShowDTO.class, datelength);	
        roleUser = getOnlineRoleUser();
    	List<QuotaStateDTO> quotaStateDTOList=new ArrayList<QuotaStateDTO>();
    	for(int i=0;i< quotaShowList.size();i++){
    		QuotaShowDTO quotaShowDTO=quotaShowList.get(i);
    		QuotaStateDTO quotaStateDTO=new QuotaStateDTO();		
    		quotaStateDTO.setQuotaAccountID(quotaAccountID);
    		quotaStateDTO.setOverDraft(quotaShowDTO.getOverDraft());
    		quotaStateDTO.setOverDraftNum(quotaShowDTO.getOverDraftNum());
    		quotaStateDTO.setQuotaState(quotaShowDTO.getQuotaState());
    		quotaStateDTO.setSaleDate(quotaShowDTO.getSaleDate());
    		quotaStateDTO.setCreatedate(quotaShowDTO.getCreatedate());
    		quotaStateDTO.setCreator(quotaShowDTO.getCreator());
    		quotaStateDTO.setModifier(roleUser.getLoginName());
    		quotaStateDTO.setModifydate(new Date());
    		quotaStateDTOList.add(quotaStateDTO);
    	}
    	
    	try {
        	if(null!=getQuotaFacade()){
        		getQuotaFacade().saveOrUpdateQuotaStates(quotaStateDTOList);
        	}else{
        		isUpdateStaSuccess=false;
        	}
			isUpdateStaSuccess=true;
		} catch (Exception e) {
			// TODO: handle exception
			isUpdateStaSuccess=false;
		}
		for(int i=0;i<shadowQuotaList.size();i++){
    		QuotaShowDTO show=shadowQuotaList.get(i);
    		for(int j=0;j<quotaStateDTOList.size();j++){
    			QuotaStateDTO state=quotaStateDTOList.get(j);
    			if(DateUtil.formatDate(show.getSaleDate()).equals(DateUtil.formatDate(state.getSaleDate()))&&show.getQuotaState()!=state.getQuotaState()){
    				OrHandleLog handleLog = new OrHandleLog();
    		    	handleLog.setModifierName(roleUser.getName());
    		        handleLog.setModifierRole(roleUser.getLoginName());
    		        handleLog.setContent(DateUtil.formatDate(state.getSaleDate())+"房态由<font color='red'>"+getQuotaState(show.getQuotaState())+"</font>更改为<font color='red'>"+getQuotaState(state.getQuotaState())+"</font>");
    		        handleLog.setModifiedTime(new Date());
    		        handleLog.setBeforeState(order.getOrderState());
    		        handleLog.setAfterState(order.getOrderState());
    		        handleLog.setOrder(order);
    		        order.getLogList().add(handleLog);
    			}
    		}
    	}
    	saveOrUpdateOrder(order);
    	showShadowRoomSta();
    	return "showShadowRoomSta";
    }
    
    private String getQuotaState(Integer quotaState){
    	String State="";
    	switch(quotaState)
    	{
    	case 1:State="有房";
    	break;
    	case 2:State="待查";
    	break;
    	case 3: State="满房"; 
    	break;
    	default : State="有房";
    	}
    	return State;
    }
    
    /**
     * 酒店直联增加特殊要求的的传真 HOTEL 2.8.1 方法名称： 将客人的特殊要求传给酒店
     */
    private String genMailContractor(OrOrder order) {
        String contractorURL = "http://10.10.5.166/HOP/order/orderOperate!edit.action?orderId=";
        org.dom4j.Document document = DocumentHelper.createDocument();
        org.dom4j.Element rootElementE = document.addElement("DifficultOrder");
        org.dom4j.Element orderUrl = rootElementE.addElement("orderUrl");
        orderUrl.setText(contractorURL + order.getID());
        return document.asXML();
    }        
    
    public HtlOrderStsLog OrderStsLogData(OrOrder order, OrOrder tempOrder,UserWrapper roleUser){
    	HtlOrderStsLog htlOrderStsLog = null;
    	boolean bOld  = order.isQuotaOk();
    	boolean bOld1 = order.isSendedHotelFax();
		boolean bNew1 = tempOrder.isSendedHotelFax();
		boolean bOld2 = order.isHotelConfirmTel();
		boolean bNew2 = tempOrder.isHotelConfirmTel();
		boolean bOld3 = order.isHotelConfirmFax();
		boolean bNew3 = tempOrder.isHotelConfirmFax();
		boolean bOld4 = order.isHotelConfirmFaxReturn();
		boolean bNew4 = tempOrder.isHotelConfirmFaxReturn();
		boolean bOld5 = order.isCustomerConfirm();
		boolean bNew5 = tempOrder.isCustomerConfirm();
		if(bOld1 == bNew1 && bOld2 == bNew2 && bOld3 == bNew3 && bOld4 == bNew4 && bOld5 == bNew5){
			return null;
		}else{
			htlOrderStsLog = new HtlOrderStsLog();
			htlOrderStsLog.setOldQuotaOk(bOld==false ? 0 : 1);
			htlOrderStsLog.setNewQuotaok(bOld==false ? 0 : 1);
			htlOrderStsLog.setOldSendedhotelconfirm(bOld1==false ? 0 : 1);
			htlOrderStsLog.setNewSendedhotelconfirm(bNew1==false ? 0 : 1);
			htlOrderStsLog.setOldHoteloralconfirm(bOld2==false ? 0 : 1);
			htlOrderStsLog.setNewHoteloralconfirm(bNew2==false ? 0 : 1);
			htlOrderStsLog.setOldHotelwrittenconfirm(bOld3==false ? 0 : 1);
			htlOrderStsLog.setNewHotelwrittenconfirm(bNew3==false ? 0 : 1);
			htlOrderStsLog.setOldHotelconfirmfaxreturn(bOld4==false ? 0 : 1);
			htlOrderStsLog.setNewHotelconfirmfaxreturn(bNew4==false ? 0 : 1);
			htlOrderStsLog.setOldCustomerconfirm(bOld5==false ? 0 : 1);
			htlOrderStsLog.setNewCustomerconfirm(bNew5==false ? 0 : 1);
			String orderCd=order.getOrderCD();
			htlOrderStsLog.setOrdercd(Long.parseLong(orderCd));
			htlOrderStsLog.setModifyName(roleUser.getName());
			htlOrderStsLog.setModifyCode(roleUser.getLoginName());
			htlOrderStsLog.setModifyTime(new Date());  
			return htlOrderStsLog;
		}
    	
    }
    
    
    public Integer validateHKorderStatus(OrOrder order){
     Integer flag=0;
   	 if(order.getChannelList()!=null && order.getChannelList().size()>0){
         for (OrChannelNo orderChannel : order.getChannelList()){
         	TxnStatusData ret = hkService.enqTxnStatus(orderChannel.getOrderChannel());
         	if(ret!=null &&  ret.getNRet()!=orderChannel.getStatus()){
         		orderChannel.setStatus(ret.getNRet());
         		orChannelNoService.updateOrChannelNo(orderChannel);
         		flag++;
         	}        	
         }              
     }    	
   	 return flag;
    }
    
    /**
     * 保存返现相关信息
     * @param order
     * @param hotelOrderFromBean
     * @param member
     */
    public void saveCashInformation(OrOrder order){
    	if( order.getCashBackTotal()>0){
    		FITOrderCash fitCash = new FITOrderCash();
    		fitCash.setMemberCd(order.getMemberCd());
    		fitCash.setOrderCd(order.getOrderCD());
    		fitCash.setReturnCash(order.getCashBackTotal());
    	
    		Map<String,Double> cashMap = returnService.fillCashItem(order.getChildRoomTypeId(), order.isPayToPrepay()?PayMethod.PAY:order.getPayMethod(), order.getCheckinDate(), order.getCheckoutDate());
    		
    		List<OrOrderItem> orderItems = order.getOrderItems();
    		List<FITCashItem> cashItems = new ArrayList<FITCashItem>();
    		for(OrOrderItem orderItem : orderItems){
    			FITCashItem cashItem = new FITCashItem();
    			cashItem.setOrderCd(order.getOrderCD());
    			cashItem.setReturnCash(orderItem.getCashReturnAmount());
    			cashItem.setReturnDate(orderItem.getNight());
    			cashItem.setReturnScale(cashMap.get(DateUtil.formatDateToSQLString(orderItem.getNight()))==null ? 0 : cashMap.get(DateUtil.formatDateToSQLString(orderItem.getNight())));
    			
    			cashItems.add(cashItem);
    		}
    		returnService.clearCashInformation(fitCash);
    		returnService.saveCashInformation(fitCash, cashItems);
    	}    	
    }
    
    
    private QuotaFacade getQuotaFacade(){
    	QuotaFacade quotaRemote=null;
    	try{
    		quotaRemote=(QuotaFacade)beanUtil.getBean("quotaRemote");
    		if(null==quotaRemote){
        		log.error("RMP EJB获取quotaRemote出错 ,EJB调不通");
    		}
        	
    	}catch(Exception e){
    		log.error("OrderOperateAction获取quotaRemote出错 ", e);
    	}  	
    	return quotaRemote;
    }
    
    private PricePlanFacade getPricePlanRemote(){
    	PricePlanFacade pricePlanRemote=null;
    	try{
    		pricePlanRemote=(PricePlanFacade)beanUtil.getBean("pricePlanRemote");
    		if(null==pricePlanRemote){
        		log.error("RMP EJB获取pricePlanRemote出错 ,EJB调不通");
    		}
        	
    	}catch(Exception e){
    		log.error("OrderOperateAction获取PricePlanFacade出错 ", e);
    	}  	
    	return pricePlanRemote;
    }
    
    private AffirmFacade getAffirmFacade(){
    	AffirmFacade affirmFacade=null;
    	try{
    		affirmFacade=(AffirmFacade)beanUtil.getBean("affirmFacade");
    		if(null==affirmFacade){
        		log.error("RMP EJB获取affirmFacade出错 ,EJB调不通");
    		}
    	}catch(Exception e){
    		log.error("OrderOperateAction获取affirmFacade出错 ", e);
    	}  	
    	return affirmFacade;
    }
    public OrChannelNoService getOrChannelNoService() {
		return orChannelNoService;
	}

	public void setOrChannelNoService(OrChannelNoService orChannelNoService) {
		this.orChannelNoService = orChannelNoService;
	}

    public String getUnlockURL() {
        return unlockURL;
    }

    public void setUnlockURL(String unlockURL) {
        this.unlockURL = unlockURL;
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

    public String getIdCurStr() {
        return idCurStr;
    }

    public void setIdCurStr(String idCurStr) {
        this.idCurStr = idCurStr;
    }

    public String getCreditRemark() {
        return creditRemark;
    }

    public void setCreditRemark(String creditRemark) {
        this.creditRemark = creditRemark;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    public void setCancelMessage(String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getGuestCancelMessage() {
        return guestCancelMessage;
    }

    public void setGuestCancelMessage(String guestCancelMessage) {
        this.guestCancelMessage = guestCancelMessage;
    }

    public OrOrderDao getOrOrderDao() {
        return orOrderDao;
    }

    public void setOrOrderDao(OrOrderDao orOrderDao) {
        this.orOrderDao = orOrderDao;
    }

    public String getCtcttype() {
        return ctcttype;
    }

    public void setCtcttype(String ctcttype) {
        this.ctcttype = ctcttype;
    }

    public String getChainCode() {
        return chainCode;
    }

    public void setChainCode(String chainCode) {
        this.chainCode = chainCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public IHDLService getHdlService() {
        return hdlService;
    }

    public void setHdlService(IHDLService hdlService) {
        this.hdlService = hdlService;
    }

    public IMemberConfirmService getMemberConfirmService() {
        return memberConfirmService;
    }

    public void setMemberConfirmService(IMemberConfirmService memberConfirmService) {
        this.memberConfirmService = memberConfirmService;
    }

    public boolean isHasHis() {
        return hasHis;
    }

    public void setHasHis(boolean hasHis) {
        this.hasHis = hasHis;
    }

    public CommunicaterService getCommunicaterService() {
        return communicaterService;
    }

    public void setCommunicaterService(CommunicaterService communicaterService) {
        this.communicaterService = communicaterService;
    }

    public void setHotelService(IHotelService hotelService) {
        this.hotelService = hotelService;
    }

    public HraManager getHraManager() {
        return hraManager;
    }

    public void setHraManager(HraManager hraManager) {
        this.hraManager = hraManager;
    }

    public IOrderEditService getOrderEditService() {
        return orderEditService;
    }

    public void setOrderEditService(IOrderEditService orderEditService) {
        this.orderEditService = orderEditService;
    }

    public boolean isFromEditBase() {
        return fromEditBase;
    }

    public void setFromEditBase(boolean fromEditBase) {
        this.fromEditBase = fromEditBase;
    }

    public double getOriAssureMoney() {
        return oriAssureMoney;
    }

    public void setOriAssureMoney(double oriAssureMoney) {
        this.oriAssureMoney = oriAssureMoney;
    }

    public HKService getHkService() {
        return hkService;
    }

    public void setHkService(HKService hkService) {
        this.hkService = hkService;
    }

    public void setAreaService(SystemDataService systemDataService) {
        this.systemDataService = systemDataService;
    }

    public String getAddModifyCancelStrr() {
        return addModifyCancelStrr;
    }

    public void setAddModifyCancelStrr(String addModifyCancelStrr) {
        this.addModifyCancelStrr = addModifyCancelStrr;
    }

	public CreditCardPreAuthInterface getCreditcardPreAuthService() {
		return creditcardPreAuthService;
	}

	public void setCreditcardPreAuthService(
			CreditCardPreAuthInterface creditcardPreAuthService) {
		this.creditcardPreAuthService = creditcardPreAuthService;
	}

	public String getEditTmcOrderURL() {
		return editTmcOrderURL;
	}

	public void setEditTmcOrderURL(String editTmcOrderURL) {
		this.editTmcOrderURL = editTmcOrderURL;
	}

	public String getViewTmcOrderURL() {
		return viewTmcOrderURL;
	}

	public void setViewTmcOrderURL(String viewTmcOrderURL) {
		this.viewTmcOrderURL = viewTmcOrderURL;
	}

	public Long getOrderGroupId() {
		return orderGroupId;
	}

	public void setOrderGroupId(Long orderGroupId) {
		this.orderGroupId = orderGroupId;
	}

	public String getCanDebitCardShow() {
		return canDebitCardShow;
	}

	public void setCanDebitCardShow(String canDebitCardShow) {
		this.canDebitCardShow = canDebitCardShow;
	}

	public void setB2bOrderIncService(B2bOrderIncService orderIncService) {
		b2bOrderIncService = orderIncService;
	}

	public String getIsModify() {
		return isModify;
	}

	public void setIsModify(String isModify) {
		this.isModify = isModify;
	}

	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}
	
	 public OperOrderDerferTimeService getOperOrderDerferTimeService() {
			return operOrderDerferTimeService;
		}

		public void setOperOrderDerferTimeService(
				OperOrderDerferTimeService operOrderDerferTimeService) {
			this.operOrderDerferTimeService = operOrderDerferTimeService;
		}

		public Integer getOrderSts() {
			return orderSts;
		}

		public void setOrderSts(Integer orderSts) {
			this.orderSts = orderSts;
		}

		public HurryOrderService getHurryOrderService() {
			return hurryOrderService;
		}

		public void setHurryOrderService(HurryOrderService hurryOrderService) {
			this.hurryOrderService = hurryOrderService;
		}

		public Integer getHurryOrderTimes() {
			return hurryOrderTimes;
		}

		public void setHurryOrderTimes(Integer hurryOrderTimes) {
			this.hurryOrderTimes = hurryOrderTimes;
		}
		
		public void setReturnService(IHotelFavourableReturnService returnService) {
			this.returnService = returnService;
		}

		public void setLimitFavourableManage(
				HtlLimitFavourableManage limitFavourableManage) {
			this.limitFavourableManage = limitFavourableManage;
		}
		
		/**
		 * @return the cancelCode
		 */
		public String getCancelCode() {
			return cancelCode;
		}

		/**
		 * @param cancelCode the cancelCode to set
		 */
		public void setCancelCode(String cancelCode) {
			this.cancelCode = cancelCode;
		}

		/**
		 * @return the cancelDate
		 */
		public String getCancelDate() {
			return cancelDate;
		}

		/**
		 * @param cancelDate the cancelDate to set
		 */
		public void setCancelDate(String cancelDate) {
			this.cancelDate = cancelDate;
		}
        //------start shadow------


		public List<QuotaShowDTO> getShadowQuotaList() {
			return shadowQuotaList;
		}

		public void setShadowQuotaList(List<QuotaShowDTO> shadowQuotaList) {
			this.shadowQuotaList = shadowQuotaList;
		}



		public Boolean getIsDelQuoSuccess() {
			return isDelQuoSuccess;
		}

		public void setIsDelQuoSuccess(Boolean isDelQuoSuccess) {
			this.isDelQuoSuccess = isDelQuoSuccess;
		}

		public String getQuotaType() {
			return quotaType;
		}

		public void setQuotaType(String quotaType) {
			this.quotaType = quotaType;
		}

		public Boolean getIsUpdateStaSuccess() {
			return isUpdateStaSuccess;
		}

		public void setIsUpdateStaSuccess(Boolean isUpdateStaSuccess) {
			this.isUpdateStaSuccess = isUpdateStaSuccess;
		}
	    //--------------end shadow

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

		public BeanUtil getBeanUtil() {
			return beanUtil;
		}

		public void setBeanUtil(BeanUtil beanUtil) {
			this.beanUtil = beanUtil;
		}

		public HtlOrderChannelService getHtlOrderChannelService() {
			return htlOrderChannelService;
		}

		public void setHtlOrderChannelService(
				HtlOrderChannelService htlOrderChannelService) {
			this.htlOrderChannelService = htlOrderChannelService;
		}

		public HtlOrderChannel getHtlOrderChannel() {
			return htlOrderChannel;
		}

		public void setHtlOrderChannel(HtlOrderChannel htlOrderChannel) {
			this.htlOrderChannel = htlOrderChannel;
		}
		
       
}
