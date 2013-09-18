package com.mangocity.hotel.order.web;

import hk.com.cts.ctcp.hotel.constant.ResultConstant;
import hk.com.cts.ctcp.hotel.service.HKService;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BasicData;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.framework.exception.ServiceException;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlCreditAssure;
import com.mangocity.hotel.base.persistence.HtlExhibit;
import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.assistant.HotelEmapResultInfo;
import com.mangocity.hotel.base.service.assistant.HotelInfo;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.hotel.base.service.assistant.QueryCreditAssureForCCBean;
import com.mangocity.hotel.base.service.assistant.RoomType;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.exception.BusinessException;
import com.mangocity.hotel.order.constant.LocalFlag;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.SessionNames;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderStatistics;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.IHotelEmapService;
import com.mangocity.hotel.order.service.INewOrderParamService;
import com.mangocity.mgis.app.service.baseinfo.GisService;
import com.mangocity.mgis.domain.valueobject.LatLng;
import com.mangocity.mgis.domain.valueobject.MapsInfo;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.log.MyLog;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Mail;

/**
 * 前台查询酒店相关
 * 
 * @author chenkeming
 * 
 */
public class HotelSearchAction extends OrderAction {
	
	protected static final MyLog log = MyLog.getLogger(HotelSearchAction.class);

    private static final long serialVersionUID = -2155159557690548587L;
	
	private IHotelService hotelService;
	
	private HotelManage hotelManage;
	
	private HotelRoomTypeService hotelRoomTypeService;
	
	private GisService gisService;
	private String toMail;
	private String fromName;
	private String messageMail;
	 /**
	 * message接口
	 */
	private CommunicaterService communicaterService;
	
	private Map gisMaps = Collections.EMPTY_MAP;
	
	private List lstTaxCharge = Collections.EMPTY_LIST;
	
	private List lstRoomTaxCharge = Collections.EMPTY_LIST;	

    private List<HotelInfo> results = Collections.emptyList();

    private List dateStrList = Collections.EMPTY_LIST;

    private Date beginDate;

    private Date endDate;

    private int dateStrListLength;

    private String cityId;

    private String cityName;

    private String actionUrl;

    private Long hotelId;

    private String city;

    private List assureList = Collections.EMPTY_LIST;

    private List roomTypeses = Collections.EMPTY_LIST;

    private String mostStar;

    private String mostPriceLevel;

    // left Top latitude 左顶点纬度
    private double nwLat;

    // left Top longitude 左顶点经度
    private double nwLng;

    // left bottom latitude 左底点纬度
    private double swLat;

    // left bottom longitude 左底点经度
    private double swLng;

    // right Top latitude 右顶点纬度
    private double neLat;

    // right Top lantitude 右顶点经度
    private double neLng;

    // right bottom lantitude 右底点纬度
    private double seLat;

    // right bottom longitude 右底点经度
    private double seLng;
    //是否是暂存前台
    private String isSaveToFront;

    /**
     * 酒店查询条件类
     */
    private HotelQueryCondition queryCond;

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
     * add by szw 预订酒店房间间数
     */
    private String hotelroomcount;

    /**
     * 中旅接口
     * 
     * @author chenkeming Mar 27, 2009 10:08:47 AM
     */
    private HKService hkService;

    /**
     * 电子地图查询酒店
     */
    private IHotelEmapService hotelEmapService;

    /**
     * 电子地图酒店星级
     */
    private String star;

    /**
     * 电子地图酒店价格
     */
    private String price;

    /**
     * 电子地图酒店服务
     */
    private String specialRequest;

    /**
     * 电子地图酒店距离
     */
    private int distance;

    /**
     * 电子地图酒店距离
     */
    private int sortWay;

    /**
     * 电子地图酒店显示方式
     */
    private String display;

    /**
     * 电子地图经度
     */
    private Double longitude;

    /**
     * 电子地图经度
     */
    private Double latitude;

    /**
     * 名胜古迹关键字
     */
    private String notes;
    
    /**
     * 原单返现金额
     */
    private double oriOrderCashReturnAmount = 0.0;
    
    // 是否交行全卡商旅等渠道 add by chenkeming
    private String forCooperate;
    
    private ResourceManager resourceManager;
    


	private String bookRMPURL;
	
	private Boolean b2bHagt;
	
	private INewOrderParamService newOrderParamService;
    
   // private String MDP_URL="http://local.mangocity.com:8080/MDP/hotel-search.shtml?";
    private String MDP_URL="http://www.mangocity.com/MDP/hotel-search.shtml?";
    /**
     * 准备酒店查询<br>
     * Note: <br>
     * 1. 继续预订的连接也指向这里,此时要注意把前面订单的信息带过来(这里只带oriOrderId)
     * 
     * @return
     */
    public String searchPre() {

        putSession(SessionNames.QUERY_COND, null);
    
        // 如果是修改预订
        if (StringUtil.isValidStr(oriOrderId)) {
            member = getOnlineMember();
            OrOrder oriOrder = getOrder(Long.valueOf(oriOrderId));
            request.setAttribute("oriOrder", oriOrder);
            if (null == member || !member.getMembercd().equals(oriOrder.getMemberCd())) {
                try {
                    member = memberInterfaceService
                        .getMemberByCode(oriOrder.getMemberCd());
                    // member.getMembercd(), IPointService.USERNAME)));
                    if (member.isMango()) {
                        OrOrderStatistics orderStat = 
                            orderService.getOrderStatByMemberCd(member.getMembercd());
                        request.setAttribute("orderStat", orderStat);
                    }
                } catch (Exception e) {
                	log.error(e.getMessage(),e);
                    return this.forwardError("获取订单会员信息出错！");
                }
            }
            oriOrderCashReturnAmount = oriOrder.getCashBackTotal();
            
            // 是否交行全卡商旅等渠道 -- add by chenkeming
            if(oriOrder.isCooperateOrder()) {
            	this.forCooperate = "1"; 
            }
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
            
            return "pre";
        }

        try {
            if (!handleMemberLogin()) {
                return this.forwardError("获取会员信息出错！");
            }
        } catch (BusinessException e) {
        	log.error(e.getMessage(),e);
            return this.forwardError("会员不存在！");
        }

        member = getOnlineMember();
        roleUser = getOnlineRoleUser();

        if (null == member) {
            return "pre";
        }
        
        // 是否交行全卡商旅等渠道会员 -- add by chenkeming        
        if(member.isCooperate()) {
        	this.forCooperate = "1";
        }

        if (roleUser.isOrg114()) {
            if (member.isMango()) {
                return forwardError("114用户只能处理114的会员!");
            }
            if (!StringUtil.StringEquals1(member.getState(), roleUser.getState())) {
                return forwardError("114用户只能处理同一个省的114会员!");
            }
        }

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
        
        return "pre";
    }
    
    
    public String shadowBook() {
    	try {
            if (!handleMemberLogin()) {
                return this.forwardError("获取会员信息出错！");
            }
        } catch (BusinessException e) {
        	log.error(e.getMessage(),e);
            return this.forwardError("会员不存在！");
        }
    	
    	member = getOnlineMember();
       if(member==null){
    	   return this.forwardError("会员不存在！");
       }
    	
    	// 获取登录用户
    	roleUser = getOnlineRoleUser();
    	b2bHagt= newOrderParamService.isB2Bagent(member.getMembercd());
        bookRMPURL=MDP_URL+"agentCode="+member.getMembercd()+"&mangoUserId="+roleUser.getId();
        return "hotelShadowBook";
    }
    
    public String searchPreNew() {

        putSession(SessionNames.QUERY_COND, null);
        
        //保存新增订单取消标识、Flex页面传过来的查询条件参数，用于取消新增订单时带回查询条件 add by wupingxiang at 2012-06-12
        request.setAttribute("flag", request.getParameter("isFromNewOrder"));
        request.setAttribute("queryConditions", request.getParameter("queryConditions"));
        
        // 如果是修改预订
         if (StringUtil.isValidStr(oriOrderId)) {
            member = getOnlineMember();
            OrOrder oriOrder = getOrder(Long.valueOf(oriOrderId));
            if(oriOrder.getRmpOrder()&&oriOrder.getType()==4){
                // 获取登录用户
            	roleUser = getOnlineRoleUser();
                Integer mangoUserId=roleUser.getId();
                // 添加日志
                saveOrUpdateOrder(oriOrder);
                String reBookReason="";
		
					reBookReason = renewReason+"/"+renewMessage+"/"+guestRenewMessage;
			
              
				try {
					bookRMPURL=MDP_URL+"orderId="+oriOrderId+"&mangoUserId="+mangoUserId+"&reBookReasons="+URLEncoder.encode(reBookReason,"UTF-8")+"&agentCode="+oriOrder.getMemberCd();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	return "saveRnewReason";
            }
            request.setAttribute("oriOrder", oriOrder);
            if (null == member || !member.getMembercd().equals(oriOrder.getMemberCd())) {
                try {
                    member = memberInterfaceService
                        .getMemberByCode(oriOrder.getMemberCd());
                    // member.getMembercd(), IPointService.USERNAME)));
                    if (member.isMango()) {
                        OrOrderStatistics orderStat = 
                        	orderService.getOrderStatByMemberCd(member.getMembercd());
                        request.setAttribute("orderStat", orderStat);
                    }
                } catch (Exception e) {
                	log.error(e.getMessage(),e);
                    return this.forwardError("获取订单会员信息出错！");
                }
            }
            oriOrderCashReturnAmount = oriOrder.getCashBackTotal();
            
            // 是否交行全卡商旅等渠道 -- add by chenkeming
            if(oriOrder.isCooperateOrder()) {
            	this.forCooperate = "1"; 
            }
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
            
            return "preNew";
        }

        try {
            if (!handleMemberLogin()) {
                return this.forwardError("获取会员信息出错！");
            }
        } catch (BusinessException e) {
        	log.error(e.getMessage(),e);
            return this.forwardError("会员不存在！");
        }

        member = getOnlineMember();
        roleUser = getOnlineRoleUser();

        if (null == member) {
            return "preNew";
        }
        
        // 是否交行全卡商旅等渠道会员 -- add by chenkeming        
        if(member.isCooperate()) {
        	this.forCooperate = "1";
        }

        if (roleUser.isOrg114()) {
            if (member.isMango()) {
                return forwardError("114用户只能处理114的会员!");
            }
            if (!StringUtil.StringEquals1(member.getState(), roleUser.getState())) {
                return forwardError("114用户只能处理同一个省的114会员!");
            }
        }

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
        
        return "preNew";
    }
    
    /**
     * 准备酒店查询(用于补单)<br>
     * Note: <br>
     * 1. 继续预订的连接也指向这里,此时要注意把前面订单的信息带过来(这里只带oriOrderId)
     * 
     * @return
     */
    public String supplement() {

        putSession(SessionNames.QUERY_COND, null);

        // 如果是修改预订
        String or=oriOrderId;
        if (StringUtil.isValidStr(oriOrderId)) {
            member = getOnlineMember();
            OrOrder oriOrder = getOrder(Long.valueOf(oriOrderId));
            request.setAttribute("oriOrder", oriOrder);
            if ( !member.getMembercd().equals(oriOrder.getMemberCd())) {
                try {
                    member = memberInterfaceService
                        .getMemberByCode(oriOrder.getMemberCd());
                    // member.getMembercd(), IPointService.USERNAME)));
                    if (member.isMango()) {
                        OrOrderStatistics orderStat = 
                        	orderService.getOrderStatByMemberCd(member.getMembercd());
                        request.setAttribute("orderStat", orderStat);
                    }
                } catch (Exception e) {
                	log.error(e.getMessage(),e);
                    return this.forwardError("获取订单会员信息出错！");
                }
            }
            oriOrderCashReturnAmount = oriOrder.getCashBackTotal();
            
            // 是否交行全卡商旅等渠道 -- add by chenkeming
            if(oriOrder.isCooperateOrder()) {
            	this.forCooperate = "1"; 
            }
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
            
            return "supplement";
        }

        try {
            if (!handleMemberLogin()) {
                return this.forwardError("获取会员信息出错！");
            }
        } catch (BusinessException e) {
        	log.error(e.getMessage(),e);
            return this.forwardError("会员不存在！");
        }

        member = getOnlineMember();
        roleUser = getOnlineRoleUser();

        if (null == member) {
            return "supplement";
        }
        
        // 是否交行全卡商旅等渠道会员 -- add by chenkeming        
        if(member.isCooperate()) {
        	this.forCooperate = "1";
        }

        if (roleUser.isOrg114()) {
            if (member.isMango()) {
                return forwardError("114用户只能处理114的会员!");
            }
            if (!StringUtil.StringEquals1(member.getState(), roleUser.getState())) {
                return forwardError("114用户只能处理同一个省的114会员!");
            }
        }

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
        
        return "supplement";
    }
    /**
     * 查询酒店，返回结果
     * 
     * @return
     * @throws SQLException 
     */
    public String search() throws SQLException {
        //TODO:delete debug 003
        long searchBeginTime = System.currentTimeMillis();
        member = getOnlineMember();
        roleUser = getOnlineRoleUser();
        // 判断用户是否有超级权限，有则可以下单 hotel2.6 @zhuangzhineng 2009-03-10

        // 判断用户是否有超级权限.应该根据登陆账号，也就是CC的工号，不能根据会员编号 add by shengwei.zuo hotel2.6 2009-06-04
        if (orderService.queryUserPower(roleUser.getLoginName())) {
            request.setAttribute("vip", "1");
        } else {
            request.setAttribute("vip", "0");
        }
        Map map = super.getParams();
        String sFromNewOrder = (String) map.get("isFromNewOrder");
        oriOrderCashReturnAmount = map.get("oriOrderCashReturnAmount") != null ? Double.valueOf(map.get("oriOrderCashReturnAmount").toString()) : 0.0;
        if ("1".equals(sFromNewOrder)) {

            // v2.8 如果是取消下中旅订单，则要rollback相应中旅订单 add by chenkeming
            String orderChannels = (String) map.get("orderChannel");
            if (StringUtil.isValidStr(orderChannels)) {
                String[] channels = orderChannels.split(",");
                for (int i = 0; i < channels.length; i++) {
                    try {
                        BasicData retRoll = hkService.saleRollback(channels[i]);
                        if (retRoll.getNRet() < ResultConstant.RESULT_SUCCESS) {
                            log.error("取消下中旅订单, 回滚中旅订单失败," + ", 中旅订单号:" + channels[i]
                                + ", WebService错误代码: " + retRoll.getNRet() + ", 错误信息:"
                                + retRoll.getSMessage());
                        }
                    } catch (Exception e) {
                    	log.error(e.getMessage(),e);
                    }
                }
            }

            queryCond = (HotelQueryCondition) getFromSession(SessionNames.QUERY_COND);
            if (null == queryCond) {
                return forwardError("session过期!请重新登录。");
            }
            
            // 如果是下单页面取消查询, 则要重新设置该属性 add by chenkeming
            queryCond.setForCooperate(forCooperate);
            
            beginDate = queryCond.getBeginDate();
            endDate = queryCond.getEndDate();
        } else {
            queryCond = new HotelQueryCondition();
            MyBeanUtil.copyProperties(queryCond, map);
            String citystr = queryCond.getCityId();
            if (!(null != citystr && 0 < citystr.length())) {
                queryCond.setCityId((String) map.get("city"));
                queryCond.setCityName(InitServlet.cityObj.get(queryCond.getCityId()));
            }

            queryCond.setDistrict((String) map.get("district"));
            String districtstr = queryCond.getDistrict();
            queryCond.setDistrictName(InitServlet.citySozeObj.get(districtstr));
            queryCond.setHotelCount(hotelroomcount);

            queryCond.setBusiness((String) map.get("business"));
            String businessstr = queryCond.getBusiness();
            queryCond.setBizZone((String) map.get("business"));
            queryCond.setBusinessName(InitServlet.businessSozeObj.get(businessstr));

            if (null == member) { // 如果当前没登录会员, 则根据roleUser属性设置
                if (roleUser.isOrg114()) {
                    String memberState = roleUser.getState();
                    if (StringUtil.isValidStr(memberState)) {
                        if (memberState.equals("WTBJ")) { // 北京网通
                            queryCond.setSaleChannel("04");
                        } else if (memberState.equals("WTT")) { // 网通
                            queryCond.setSaleChannel("05");
                        } else if (memberState.equals("LTT")) { // 联通
                            queryCond.setSaleChannel("02");
                        } else if (memberState.equals("GDLT")) { // 联通116114
                            queryCond.setSaleChannel("04");
                        } else if (!memberState.equals("NHZY")) { // 非南航的，就取电信114
                            queryCond.setSaleChannel("01");
                        }
                    } else {
                        return forwardError("操作员没有省份!");
                    }
                }
            } else if (!member.isMango()) {
                String memberState = member.getState();
                if (StringUtil.isValidStr(memberState)) {
                    if (memberState.equals("WTBJ")) { // 北京网通
                        queryCond.setSaleChannel("04");
                    } else if (memberState.equals("WTT")) { // 网通
                        queryCond.setSaleChannel("05");
                    } else if (memberState.equals("LTT")) { // 联通
                        queryCond.setSaleChannel("02");
                    } else if (memberState.equals("GDLT")) { // 联通116114
                        queryCond.setSaleChannel("04");
                    } else if (!memberState.equals("NHZY")) { // 非南航的，就取电信114
                        queryCond.setSaleChannel("01");
                    }
                } else {
                    return forwardError("会员没有省份!");
                }
            }
            // 设置查询条件session, 当取消新建订单时用到
            putSession(SessionNames.QUERY_COND, queryCond);
        }

		//异步分页，设置查询条件，忽略分页查询 add by chenjiajie 2010-03-22
		queryCond.setIgnorePageCount(1);
		queryCond.setIgnoreQueryList(0);
		results = hotelService.queryHotels(queryCond);
        HotelInfo hotelInfo = new HotelInfo();
        Iterator it = results.iterator();
        while (it.hasNext()) {
            hotelInfo = (HotelInfo) it.next();
            hotelId = hotelInfo.getHotelId();
            this.lstTaxCharge();
            /*
             * () &&
             * orderService.getPreOrderPresaleList(hotelInfo.getHotelId(),beginDate,endDate).isEmpty
             * ()) { hotelInfo.setIsSalesPromo(0); } else { hotelInfo.setIsSalesPromo(1); }
             */
            if (0 == hotelManage.queryHtlUsersCommentTotal(hotelInfo.getHotelId())) {
                hotelInfo.setIsUserComment(0);
            } else {
                hotelInfo.setIsUserComment(1);
            }

            if (lstTaxCharge.isEmpty()) {
                hotelInfo.setIsTaxCharges(0);
            } else {
                hotelInfo.setIsTaxCharges(1);
                // 用于房费需另缴税直接显示结果 v2.9.2 addby chenjuesu
                hotelInfo.setLstRoomTaxCharge(lstRoomTaxCharge);
            }
        }
        // 查询一年内是否有展会
        String checkExhibitInOneYear = queryExhibitInOneYear();
        if (checkExhibitInOneYear.equals("lsExhibit")) {// 没有会展信息,则在页面上不显示链接
            request.setAttribute("isHaveExhibitInOneYear", true);
        }
        // 查询预订时间期间是否有展会 v2.9.2 addby chenjuesu
        Date threeDaysAfterEndDate = DateUtil.getDate(endDate, 3);// 延迟三天时间
		List<HtlExhibit> exhibits = hotelService
			.queryExhibit(queryCond.getCityId(), beginDate, threeDaysAfterEndDate);
		request.setAttribute("exhibits", exhibits);
		int difdays = DateUtil.getDay(beginDate, endDate);
		request.setAttribute("difdays", difdays);
		int weekNum = (difdays - 1) / 7 + 1;
		for (int m = 0; m < results.size(); m++) {			 
			HotelInfo result = (HotelInfo) results.get(m);
			List roomTypes = result.getRoomTypes();
			//循环房型
			for (int j = 0; j < roomTypes.size(); j++) {
				
				RoomType roomType = (RoomType) roomTypes.get(j);
				//hotel 2.9.2 提示信息 add by shengwei.zuo 2009-08-17 begin
				
				//查询出该子房型下在该时间段的提示信息字符串
				String priceTypeclueInfo=hotelService.queryAlertInfoStr(result.getHotelId(),roomType.getChildRoomTypeId(),beginDate,endDate,LocalFlag.CC);
				
				//校验是否为空
				if(StringUtil.isValidStr(priceTypeclueInfo)){
					roomType.setHasTip(1);
				}
				
				//子房型的提示信息字符串
				priceTypeclueInfo= priceTypeclueInfo.replace("\r\n", "");
				roomType.setTipContent(priceTypeclueInfo);
				
				//hotel 2.9.2 提示信息 add by shengwei.zuo 2009-08-17 end;
				
				roomType.setTotalCount(roomType.getSaleItems().size() * weekNum);
			}
			
			if(result.getClueInfo()!=null){
				String temp=result.getClueInfo().replace("\r\n", "");
				result.setClueInfo(temp);
			}
			
		}
		dateStrList = DateUtil.getDateStrList(beginDate, endDate, false);
		dateStrListLength = dateStrList.size();
        //TODO:delete debug 003
        log.info("debug 003:Action use time:" + (System.currentTimeMillis() - searchBeginTime) + "ms");
		return SUCCESS;
	}
	
	/**
	 * 电子地图查询
	 * 
	 * @author Guo.Jun
	 * @return
	 */
	public String emapSearch() {

        member = getOnlineMember();
        roleUser = getOnlineRoleUser();
        // 判断用户是否有超级权限，有则可以下单 hotel2.6 @zhuangzhineng 2009-03-10

        // 判断用户是否有超级权限.应该根据登陆账号，也就是CC的工号，不能根据会员编号 add by shengwei.zuo hotel2.6 2009-06-04
        if (orderService.queryUserPower(roleUser.getLoginName())) {
            request.setAttribute("vip", "1");
        } else {
            request.setAttribute("vip", "0");
        }
        Map map = super.getParams();

        cityId = null == map.get("cityId") ? "" : map.get("cityId").toString();

        distance = null == map.get("distance") ? 0 : Integer
            .valueOf(map.get("distance").toString());

        queryCond = new HotelQueryCondition();

        MyBeanUtil.copyProperties(queryCond, map);
		String citystr = queryCond.getCityId();
        if (StringUtil.isValidStr(cityId)) {
            cityName = InitServlet.cityObj.get(String.valueOf(cityId));
            queryCond.setCityId(cityId);
            queryCond.setCityName(cityName);
        }
        if (!StringUtil.isValidStr(notes)) {
            longitude = null;
            latitude = null;
        }
		/*
		if (!(citystr != null && citystr.length() > 0)) {
			queryCond.setCityId((String) map.get("city"));
			queryCond.setCityName(InitServlet.cityObj.get(queryCond.getCityId()));	
		}
		*/
        if (StringUtil.isValidStr(price)) {
            queryCond.setMinPrice(price);
        }
        queryCond.setDistrict((String) map.get("district"));
        String districtstr = queryCond.getDistrict();
        queryCond.setDistrictName(InitServlet.citySozeObj.get(districtstr));
        queryCond.setHotelCount(hotelroomcount);

        queryCond.setBusiness((String) map.get("business"));
        String businessstr = queryCond.getBusiness();
        queryCond.setBizZone((String) map.get("business"));
        queryCond.setBusinessName(InitServlet.businessSozeObj.get(businessstr));

        if (null == member) { // 如果当前没登录会员, 则根据roleUser属性设置
            if (roleUser.isOrg114()) {
                String memberState = roleUser.getState();
                if (StringUtil.isValidStr(memberState)) {
                    if (memberState.equals("WTBJ")) { // 北京网通
                        queryCond.setSaleChannel("04");
                    } else if (memberState.equals("WTT")) { // 网通
                        queryCond.setSaleChannel("05");
                    } else if (memberState.equals("LTT")) { // 联通
                        queryCond.setSaleChannel("02");
                    } else if (!memberState.equals("NHZY")) { // 非南航的，就取电信114
                        queryCond.setSaleChannel("01");
                    }
                } else {
                    return forwardError("操作员没有省份!");
                }
            }
        } else if (!member.isMango()) {
            String memberState = member.getState();
            if (StringUtil.isValidStr(memberState)) {
                if (memberState.equals("WTBJ")) { // 北京网通
                    queryCond.setSaleChannel("04");
                } else if (memberState.equals("WTT")) { // 网通
                    queryCond.setSaleChannel("05");
                } else if (memberState.equals("LTT")) { // 联通
                    queryCond.setSaleChannel("02");
                } else if (!memberState.equals("NHZY")) { // 非南航的，就取电信114
                    queryCond.setSaleChannel("01");
                }
            } else {
                return forwardError("会员没有省份!");
            }
        }
        // 设置查询条件session, 当取消新建订单时用到
        putSession(SessionNames.QUERY_COND, queryCond);

        // 设置测试页面数据
        queryCond.setPageSize(10);

        boolean isDisplayAllHotel = false;
        List allHotelList = null;
        if (StringUtil.isValidStr(display) && "1".equals(display)) {
            isDisplayAllHotel = true;
            allHotelList = new ArrayList();
        } else {
            isDisplayAllHotel = false;
        }
        if (0 == distance) {
            if (StringUtil.isValidStr(queryCond.getScale())) {
                distance = Integer.valueOf(queryCond.getScale()).intValue();
            }
        }
        try {
			results = hotelEmapService.queryHotels(queryCond, longitude, latitude, distance, null,
			    null, null, null, isDisplayAllHotel, allHotelList);
		} catch (SQLException e1) {
			log.error(e1.getMessage(),e1);
		}

        try {
            List<MapsInfo> mapsInfoList = new ArrayList<MapsInfo>();
            String path = request.getRealPath("/");
            List mapShowResults = null;
            if (isDisplayAllHotel) {
                mapShowResults = allHotelList;
            } else {
                mapShowResults = results;
            }
            if (null != mapShowResults && 0 < mapShowResults.size()) {
                for (int i = 0; i < mapShowResults.size(); i++) {
                    HotelEmapResultInfo result = (HotelEmapResultInfo) mapShowResults.get(i);
                    if ((null != result.getLatitude()) && (null != result.getLongitude())
                        && (0 < result.getGisid())) {
                        MapsInfo mapsInfo = new MapsInfo();
                        LatLng latlng = new LatLng();
                        latlng.setLatitude(result.getLatitude());
                        latlng.setLongitude(result.getLongitude());
                        mapsInfo.setLatLng(latlng);
                        mapsInfo.setName(result.getHotelChnName());
                        mapsInfo.setGisId(result.getGisid());
                        int index = result.getHotelStar().indexOf(".");
                        mapsInfo.setImageUrl(path
                            + "/images/emap/plack"
                            + (0 < index ? result.getHotelStar().substring(0, index) : result
                                .getHotelStar()) + ".png");
                        if (10 > i) {
                            mapsInfo.setImageInfo(" " + String.valueOf(i + 1) + "   $"
                                + result.getLowestPrice());
                        } else {
                            mapsInfo.setImageInfo(String.valueOf(i + 1) + "  $"
                                + result.getLowestPrice());
                        }
                        mapsInfoList.add(mapsInfo);
                    }
                }
            }
            gisMaps = gisService.kmlGenerator(mapsInfoList, 0, 0, path);

        } catch (ServiceException e) {
        	log.error(e.getMessage(),e);
        } catch (IOException e) {
        	log.error(e.getMessage(),e);
        }

        return "mapSearch";
    }

    /**
     * 地图框选
     * 
     * @return
     * @throws SQLException 
     */
    public String emapDrag() throws SQLException {
        member = getOnlineMember();
        roleUser = getOnlineRoleUser();
        // 判断用户是否有超级权限，有则可以下单 hotel2.6 @zhuangzhineng 2009-03-10

        // 判断用户是否有超级权限.应该根据登陆账号，也就是CC的工号，不能根据会员编号 add by shengwei.zuo hotel2.6 2009-06-04
        if (orderService.queryUserPower(roleUser.getLoginName())) {
            request.setAttribute("vip", "1");
        } else {
            request.setAttribute("vip", "0");
        }
        Map map = super.getParams();

        cityId = null == map.get("cityId") ? "" : map.get("cityId").toString();

        distance = null == map.get("distance") ? 0 : Integer
            .valueOf(map.get("distance").toString());

        queryCond = new HotelQueryCondition();

        MyBeanUtil.copyProperties(queryCond, map);
        String citystr = queryCond.getCityId();
        if (StringUtil.isValidStr(cityId)) {
            cityName = InitServlet.cityObj.get(String.valueOf(cityId));
        }
        if (!(null != citystr && 0 < citystr.length())) {
            queryCond.setCityId((String) map.get("city"));
            queryCond.setCityName(InitServlet.cityObj.get(queryCond.getCityId()));
        }
        if (StringUtil.isValidStr(price)) {
            queryCond.setMinPrice(price);
        }
        queryCond.setDistrict((String) map.get("district"));
        String districtstr = queryCond.getDistrict();
        queryCond.setDistrictName(InitServlet.citySozeObj.get(districtstr));
        queryCond.setHotelCount(hotelroomcount);

        queryCond.setBusiness((String) map.get("business"));
        String businessstr = queryCond.getBusiness();
        queryCond.setBizZone((String) map.get("business"));
        queryCond.setBusinessName(InitServlet.businessSozeObj.get(businessstr));

        if (null == member) { // 如果当前没登录会员, 则根据roleUser属性设置
            if (roleUser.isOrg114()) {
                String memberState = roleUser.getState();
                if (StringUtil.isValidStr(memberState)) {
                    if (memberState.equals("WTBJ")) { // 北京网通
                        queryCond.setSaleChannel("04");
                    } else if (memberState.equals("WTT")) { // 网通
                        queryCond.setSaleChannel("05");
                    } else if (memberState.equals("LTT")) { // 联通
                        queryCond.setSaleChannel("02");
                    } else if (!memberState.equals("NHZY")) { // 非南航的，就取电信114
                        queryCond.setSaleChannel("01");
                    }
                } else {
                    return forwardError("操作员没有省份!");
                }
            }
        } else if (!member.isMango()) {
            String memberState = member.getState();
            if (StringUtil.isValidStr(memberState)) {
                if (memberState.equals("WTBJ")) { // 北京网通
                    queryCond.setSaleChannel("04");
                } else if (memberState.equals("WTT")) { // 网通
                    queryCond.setSaleChannel("05");
                } else if (memberState.equals("LTT")) { // 联通
                    queryCond.setSaleChannel("02");
                } else if (!memberState.equals("NHZY")) { // 非南航的，就取电信114
                    queryCond.setSaleChannel("01");
                }
            } else {
                return forwardError("会员没有省份!");
            }
        }
        // 设置查询条件session, 当取消新建订单时用到
        putSession(SessionNames.QUERY_COND, queryCond);

        // 设置测试页面数据
        queryCond.setPageSize(10);
        boolean isDisplayAllHotel = false;
        List allHotelList = null;
        if (StringUtil.isValidStr(display) && "1".equals(display)) {
            isDisplayAllHotel = true;
            allHotelList = new ArrayList();
        } else {
            isDisplayAllHotel = false;
        }
        results = hotelEmapService.queryHotels(queryCond, longitude, latitude, distance,
            new LatLng(nwLng, nwLat), new LatLng(swLng, swLat), new LatLng(neLng, neLat),
            new LatLng(seLng, seLat), isDisplayAllHotel, allHotelList);
        try {
            List<MapsInfo> mapsInfoList = new ArrayList<MapsInfo>();
            String path = request.getRealPath("/");
            List mapShowResults = null;
            if (isDisplayAllHotel) {
                mapShowResults = allHotelList;
            } else {
                mapShowResults = results;
            }
            if (null != mapShowResults && 0 < mapShowResults.size()) {
                for (int i = 0; i < mapShowResults.size(); i++) {
                    HotelEmapResultInfo result = (HotelEmapResultInfo) mapShowResults.get(i);
                    if ((null != result.getLatitude()) && (null != result.getLongitude())
                        && (0 < result.getGisid())) {
                        MapsInfo mapsInfo = new MapsInfo();
                        LatLng latlng = new LatLng();
                        latlng.setLatitude(result.getLatitude());
                        latlng.setLongitude(result.getLongitude());
                        mapsInfo.setLatLng(latlng);
                        mapsInfo.setName(result.getHotelChnName());
                        mapsInfo.setGisId(result.getGisid());
                        int index = result.getHotelStar().indexOf(".");
                        mapsInfo.setImageUrl(path
                            + "/images/emap/plack"
                            + (0 < index ? result.getHotelStar().substring(0, index) : result
                                .getHotelStar()) + ".png");
                        if (10 > i) {
                            mapsInfo.setImageInfo(" " + String.valueOf(i + 1) + "   $"
                                + result.getLowestPrice());
                        } else {
                            mapsInfo.setImageInfo(String.valueOf(i + 1) + "  $"
                                + result.getLowestPrice());
                        }
                        mapsInfoList.add(mapsInfo);
                    }
                }
            }
            gisMaps = gisService.kmlGenerator(mapsInfoList, 0, 0, path);

        } catch (ServiceException e) {
        	log.error(e.getMessage(),e);
        } catch (IOException e) {
        	log.error(e.getMessage(),e);
        }
        return "ajaxSerch";
    }

    public String sendMail() {
        Mail mail = new Mail();
        mail.setApplicationName("hotel");
        mail.setTo(new String[] { toMail });
        mail.setSubject("芒果网电子地图");
        mail.setFrom("cs@mangocity.com");
        mail.setTemplateFileName(FaxEmailModel.HOTEL_EMAP_SEND_MAIL);
        mail.setXml(messageMail);
        try {
            communicaterService.sendEmail(mail);
        } catch (Exception e) {
            log.error("EMapAction.sendMail send failed: " + e);
        }
        return "ajaxSerch";
    }

    public String printMap() {

        return "printMap";
    }

    /**
     * 查询预定条款信息
     */
    public String reservation() {

        QueryCreditAssureForCCBean queryBean = new QueryCreditAssureForCCBean();
        queryBean.setBeginDate(beginDate);
        queryBean.setEndDate(endDate);
        queryBean.setHotelId(hotelId);
        queryBean.setRoomType("");
		roomTypeses = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());
        assureList = new ArrayList<HtlCreditAssure>();
        List<HtlCreditAssure> tmpList = hotelService.queryCreditAssureForCC(queryBean);
        // 以下只添加真正有"预订条款"的项
        if (null != tmpList && 0 < tmpList.size()) {
            for (int i = 0; i < tmpList.size(); i++) {
                HtlCreditAssure assure = tmpList.get(i);
                String strCreditAssure = "";
                // ----------------1-------------
                // 天数
                if (null == assure.getAheadDays() && null != assure.getAheadTimer()
                    && !("".equals(assure.getAheadTimer()))) {
                    strCreditAssure = "提前";
                    strCreditAssure += "0天";
                    strCreditAssure += assure.getAheadTimer() + "时间";
                }

                if (null != assure.getAheadDays() && null != assure.getAheadTimer()
                    && !("".equals(assure.getAheadTimer()))) {
                    strCreditAssure = "提前";
                    strCreditAssure += assure.getAheadDays() + "天";
                    strCreditAssure += assure.getAheadTimer() + "时间";
                }
                if (null != assure.getAheadDays()
                    && (null == assure.getAheadTimer() || "".equals(assure.getAheadTimer()))) {
                    strCreditAssure = "提前";
                    strCreditAssure += assure.getAheadDays() + "天";
                }

                // ----------------2------------------------
                Date date = new Date();
                // 日期
                if (null == assure.getMustBeforeDate() && null != assure.getMustBeforeTime()
                    && !("".equals(assure.getMustBeforeTime()))) {
                    if (null != strCreditAssure && !("".equals(strCreditAssure))) {
                        strCreditAssure += "，或者";
                    }
                    strCreditAssure += "必须在" + DateUtil.dateToString(date) + "日期";
                    strCreditAssure += assure.getMustBeforeTime() + "时间之前预订";
                }

                if (null != assure.getMustBeforeDate() && null != assure.getMustBeforeTime()
                    && !("".equals(assure.getMustBeforeTime()))) {
                    if (null != strCreditAssure && !("".equals(strCreditAssure))) {
                        strCreditAssure += "，或者";
                    }
                    strCreditAssure += "必须在"
                        + DateUtil.dateToString(assure.getMustBeforeDate()) + "日期";
                    strCreditAssure += assure.getMustBeforeTime() + "时间之前预订";
                }
                if (null != assure.getMustBeforeDate()
                    && (null == assure.getMustBeforeTime() 
                        || "".equals(assure.getMustBeforeTime()))) {
                    if (null != strCreditAssure && !("".equals(strCreditAssure))) {
                        strCreditAssure += "，或者";
                    }
                    strCreditAssure += "必须在"
                        + DateUtil.dateToString(assure.getMustBeforeDate()) + "日期之前预定";
                }
                // ------------3-----------------------------

                if (null != assure.getContinueNight() && !("".equals(assure.getContinueNight()))) {
                    if (null != strCreditAssure && !("".equals(strCreditAssure))) {
                        strCreditAssure += "，或者";
                    }
                    strCreditAssure += "连住" + assure.getContinueNight()
                        + "晚.</br>";
                }
                // -----------4---------------------------------
                if (null != assure.getMustDate() && !("".equals(assure.getMustDate()))) {
                    strCreditAssure += "连住日期:";
                    strCreditAssure += assure.getMustDate();
                }

                // modify by chenkeming@2008.09.19
                // 紧急需求,酒店前台查询结果需要看到完整的预订条款信息
                // if(StringUtil.isValidStr(strCreditAssure)) {
                assure.setAssureString(strCreditAssure);
                assureList.add(assure);
                // }
            }
        }
        return "reservation";
    }

    // 傅建波 获取另缴税信息
    public String lstTaxCharge() {

        lstTaxCharge = orderService.getTaxCharges(hotelId, beginDate, endDate);
        lstRoomTaxCharge = new ArrayList();
        // lstCommTaxCharge = new ArrayList();
        if (null != lstTaxCharge) {
            for (int i = 0; i < lstTaxCharge.size(); i++) {
                HtlTaxCharge taxCharge = (HtlTaxCharge) lstTaxCharge.get(i);
                if (null != taxCharge.getRoomIncTax()) {
                    lstRoomTaxCharge.add(taxCharge);
                    continue;
                }
                // lstCommTaxCharge.add(taxCharge);
            }
        }
        super.getParams().put("hotelId", hotelId);
        super.getParams().put("hotelID", hotelId);
        return "lstTaxCharge";
    }

    /**
     * 查询一年内的展会
     * 
     * @return
     */
    public String queryExhibitInOneYear() {
        // 获取当前日期,然后加一年
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, 1);
        String cityId = (String) super.getParams().get("cityId");
        if (!StringUtil.isValidStr(cityId)) {// 如果cityId不是正常的,则从city中取值
            cityId = (String) super.getParams().get("city");
        }
		List<HtlExhibit> exhibits = hotelService
			.queryExhibit(cityId, new Date(), now.getTime());
        if (0 == exhibits.size()) {
            return "noExhibitInOneYear";
        } else {
            request.setAttribute("exhibits", exhibits);
            return "lsExhibit";
        }
    }
    /**
     * 查询虚假订单
     * @return
     */
    public String queryFlaseOrder(){
    	beginDate =  DateUtil.getDate(super.getParams().get("beginDate").toString());
    	endDate = DateUtil.getDate(super.getParams().get("endDate").toString());
    	if(super.getParams().get("cityId")!=null){
    		cityId = (String)super.getParams().get("cityId");
    	}
    	if(super.getParams().get("hotelId")!=null){
    		hotelId = new Long(super.getParams().get("hotelId").toString());
    	}
    	return "falseOrder";
    }
   
    /** getter ans setter begin */

    public List<HotelInfo> getResults() {
        return results;
    }

    public void setResults(List<HotelInfo> results) {
        this.results = results;
    }

    public List getDateStrList() {
        return dateStrList;
    }

    public void setDateStrList(List dateStrList) {
        this.dateStrList = dateStrList;
    }

    public String getOriOrderId() {
        return oriOrderId;
    }

    public void setOriOrderId(String oriOrderId) {
        this.oriOrderId = oriOrderId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDateStr) {
        this.beginDate = beginDateStr;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDateStr) {
        this.endDate = endDateStr;
    }

    public HotelQueryCondition getQueryCond() {
        return queryCond;
    }

    public void setQueryCond(HotelQueryCondition queryCond) {
        this.queryCond = queryCond;
    }

    public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
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

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public List getAssureList() {
        return assureList;
    }

    public void setAssureList(List assureList) {
        this.assureList = assureList;
    }

    public List getRoomTypeses() {
        return roomTypeses;
    }

    public void setRoomTypeses(List roomTypeses) {
        this.roomTypeses = roomTypeses;
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

    public List getLstRoomTaxCharge() {
        return lstRoomTaxCharge;
    }

    public void setLstRoomTaxCharge(List lstRoomTaxCharge) {
        this.lstRoomTaxCharge = lstRoomTaxCharge;
    }

    public List getLstTaxCharge() {
        return lstTaxCharge;
    }

    public void setLstTaxCharge(List lstTaxCharge) {
        this.lstTaxCharge = lstTaxCharge;
    }

    public String getHotelroomcount() {
        return hotelroomcount;
    }

    public void setHotelroomcount(String hotelroomcount) {
        this.hotelroomcount = hotelroomcount;
    }

    public HKService getHkService() {
        return hkService;
    }

    public void setHkService(HKService hkService) {
        this.hkService = hkService;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public IHotelEmapService getHotelEmapService() {
        return hotelEmapService;
    }

    public void setHotelEmapService(IHotelEmapService hotelEmapService) {
        this.hotelEmapService = hotelEmapService;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public GisService getGisService() {
        return gisService;
    }

    public void setGisService(GisService gisService) {
        this.gisService = gisService;
    }

    public Map getGisMaps() {
        return gisMaps;
    }

    public void setGisMaps(Map gisMaps) {
        this.gisMaps = gisMaps;
    }

    public double getNeLat() {
        return neLat;
    }

    public void setNeLat(double neLat) {
        this.neLat = neLat;
    }

    public double getNeLng() {
        return neLng;
    }

    public void setNeLng(double neLng) {
        this.neLng = neLng;
    }

    public double getNwLat() {
        return nwLat;
    }

    public void setNwLat(double nwLat) {
        this.nwLat = nwLat;
    }

    public double getNwLng() {
        return nwLng;
    }

    public void setNwLng(double nwLng) {
        this.nwLng = nwLng;
    }

    public double getSeLat() {
        return seLat;
    }

    public void setSeLat(double seLat) {
        this.seLat = seLat;
    }

    public double getSeLng() {
        return seLng;
    }

    public void setSeLng(double seLng) {
        this.seLng = seLng;
    }

    public double getSwLat() {
        return swLat;
    }

    public void setSwLat(double swLat) {
        this.swLat = swLat;
    }

    public double getSwLng() {
        return swLng;
    }

    public void setSwLng(double swLng) {
        this.swLng = swLng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public int getSortWay() {
        return sortWay;
    }

    public void setSortWay(int sortWay) {
        this.sortWay = sortWay;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getMessageMail() {
        return messageMail;
    }

    public void setMessageMail(String messageMail) {
        this.messageMail = messageMail;
    }

    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public CommunicaterService getCommunicaterService() {
        return communicaterService;
    }

    public void setCommunicaterService(CommunicaterService communicaterService) {
        this.communicaterService = communicaterService;
    }

    public int getDateStrListLength() {
        return dateStrListLength;
    }

    public void setDateStrListLength(int dateStrListLength) {
        this.dateStrListLength = dateStrListLength;
    }

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}
	
	public double getOriOrderCashReturnAmount() {
		return oriOrderCashReturnAmount;
	}

	public void setOriOrderCashReturnAmount(double oriOrderCashReturnAmount) {
		this.oriOrderCashReturnAmount = oriOrderCashReturnAmount;
	}
	
	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}	

	public String getForCooperate() {
		return forCooperate;
	}

	public void setForCooperate(String forCooperate) {
		this.forCooperate = forCooperate;
	}

	public String getIsSaveToFront() {
		return isSaveToFront;
	}

	public void setIsSaveToFront(String isSaveToFront) {
		this.isSaveToFront = isSaveToFront;
	}

	public String getBookRMPURL() {
		return bookRMPURL;
	}

	public void setBookRMPURL(String bookRMPURL) {
		this.bookRMPURL = bookRMPURL;
	}


	public ResourceManager getResourceManager() {
		return resourceManager;
	}


	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}


	public Boolean getB2bHagt() {
		return b2bHagt;
	}


	public void setB2bHagt(Boolean b2bHagt) {
		this.b2bHagt = b2bHagt;
	}


	public INewOrderParamService getNewOrderParamService() {
		return newOrderParamService;
	}


	public void setNewOrderParamService(INewOrderParamService newOrderParamService) {
		this.newOrderParamService = newOrderParamService;
	}

    /** getter ans setter end */
}
