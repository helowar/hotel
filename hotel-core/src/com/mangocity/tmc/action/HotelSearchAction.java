package com.mangocity.tmc.action;

import hk.com.cts.ctcp.hotel.constant.ResultConstant;
import hk.com.cts.ctcp.hotel.service.HKService;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BasicData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.mangocity.hotel.order.constant.SessionNames;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderStatistics;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.IHotelEmapService;
import com.mangocity.hotel.order.web.OrderAction;
import com.mangocity.mgis.app.service.baseinfo.GisService;
import com.mangocity.mgis.app.service.baseinfo.GisToolbarService;
import com.mangocity.mgis.domain.valueobject.LatLng;
import com.mangocity.mgis.domain.valueobject.MapsInfo;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * 前台查询酒店相关
 * 
 * @author chenkeming
 * 
 */
public class HotelSearchAction extends OrderAction {

	private static final long serialVersionUID = -2155159557690548587L;

	private ResourceManager resourceManager;

	protected IHotelService hotelService;
	
	private HotelManage hotelManage;
	
	private HotelRoomTypeService hotelRoomTypeService;
	
	private GisService gisService;
	private String toMail;
	private String fromName;
	private String messageMail;
	private GisToolbarService gisToolbarService;
	
	private Map gisMaps = new HashMap();
	
	private List lstTaxCharge = new ArrayList();
	
	private List lstRoomTaxCharge = new ArrayList();
	
	private List lstCommTaxCharge = new ArrayList();

	List results = new ArrayList();

	List dateStrList = new ArrayList();

	Date beginDate;

	Date endDate;
	private int dateStrListLength;

	private String cityId;
	
	private String cityName;
	private String actionUrl;
	private Long hotelId;
	private String city;
	private List assureList = new ArrayList();

	private List roomTypeses = new ArrayList();

	private Map priceTypes = new HashMap();

	String mostStar;

	String mostPriceLevel;
	
	//left Top latitude 左顶点纬度
	private double nwLat;

	//left Top longitude 左顶点经度
	private double nwLng;

	//left bottom latitude 左底点纬度
	private double swLat;

	//left bottom longitude 左底点经度
	private double swLng;

	//right Top latitude 右顶点纬度
	private double neLat;

	//right Top lantitude 右顶点经度
	private double neLng;

	//right bottom lantitude 右底点纬度
	private double seLat;
	
	//right bottom longitude 右底点经度
	private double seLng;



	/**
	 * 酒店查询条件类
	 */
	private HotelQueryCondition queryCond;

	/**
	 * 源订单ID
	 */
	String oriOrderId;

	/**
	 * 是否重下新单
	 */
	String sRenew;
	
	/**
	 * 以下用于重下新单保存撤单原因
	 */
	String renewReason;
	String renewMessage;
	String guestRenewMessage;	
	
	/**
	 * add by szw
	 * 预订酒店房间间数
	 */
	private String hotelroomcount ;
    
    /**
     * 中旅接口
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
			OrOrder oriOrder = getOrder(new Long(oriOrderId));
			request.setAttribute("oriOrder", oriOrder);
			if (member == null
					|| !member.getMembercd().equals(oriOrder.getMemberCd())) {
				try {
					member = memberInterfaceService
							.getMemberByCode(oriOrder.getMemberCd());
					// member.setPoint(TranslateUtil.translatePoint(pointService.getPonitsByMemberCd(
					// member.getMembercd(), IPointService.USERNAME)));
					if (member.isMango()) {
						OrOrderStatistics orderStat = orderService.getOrderStatByMemberCd(member.getMembercd());
						request.setAttribute("orderStat", orderStat);
					}
				} catch (Exception e) {
					log.error(e);
					return this.forwardError("获取订单会员信息出错！");
				}
			}

			return "pre";
		}

		try {
			if (!handleMemberLogin()) {
				return this.forwardError("获取会员信息出错！");
			}
		} catch (BusinessException e) {
			log.error(e);
			return this.forwardError("会员不存在！");
		}
		
		member = getOnlineMember();
		roleUser = getOnlineRoleUser();
		
		if(member == null) {
			return "pre";
		}
		
		if (roleUser.isOrg114()) {
			if (member.isMango()) {
				return forwardError("114用户只能处理114的会员!");
			}
			if (!StringUtil.StringEquals1(member.getState(), roleUser
					.getState())) {
				return forwardError("114用户只能处理同一个省的114会员!");
			}
		}

		if (member.isMango()) {
			OrOrderStatistics orderStat =orderService.getOrderStatByMemberCd(member.getMembercd());
			request.setAttribute("orderStat", orderStat);

			if (orderStat != null) {
				double price = orderStat.getAvgPrice();
				double star = orderStat.getAvgStar();
				mostStar = OrderUtil.getMostStar(star);
				mostPriceLevel = OrderUtil.getPriceLevel(price);
			}
		}

		return "pre";
	}

	/**
	 * 查询酒店，返回结果
	 * 
	 * @return
	 */
	public String search() {

		member = getOnlineMember();		
		roleUser = getOnlineRoleUser();
		//判断用户是否有超级权限，有则可以下单 hotel2.6 @zhuangzhineng 2009-03-10
		
		//判断用户是否有超级权限.应该根据登陆账号，也就是CC的工号，不能根据会员编号 add by shengwei.zuo hotel2.6 2009-06-04
		if(orderService.queryUserPower(roleUser.getLoginName())){
			request.setAttribute("vip", "1");
		}
		else{
			request.setAttribute("vip", "0");
		}
		Map map = super.getParams();
		
		String sFromNewOrder = (String)map.get("isFromNewOrder");
		if("1".equals(sFromNewOrder)) {
            
            // v2.8 如果是取消下中旅订单，则要rollback相应中旅订单 add by chenkeming
            String orderChannels = (String)map.get("orderChannel");
            if(StringUtil.isValidStr(orderChannels)) {
                String[] channels = orderChannels.split(",");
                for(int i=0; i<channels.length; i++) {
                    try {
                        BasicData retRoll = hkService
                                .saleRollback(channels[i]);
                        if (retRoll.getNRet() < ResultConstant.RESULT_SUCCESS) {
                            log.error("取消下中旅订单, 回滚中旅订单失败," + 
                                    ", 中旅订单号:" + channels[i] + 
                                    ", WebService错误代码: " + retRoll.getNRet() + 
                                    ", 错误信息:" + retRoll.getSMessage());
                        }
                    } catch (Exception e) {
                        log.error(e);
                    }
                }
            }
            
			queryCond = (HotelQueryCondition)getFromSession(SessionNames.QUERY_COND);
			if(queryCond == null) {
				return forwardError("session过期!请重新登录。");
			}
			beginDate = queryCond.getBeginDate();
			endDate = queryCond.getEndDate();
		} else {
			queryCond = new HotelQueryCondition();		
			MyBeanUtil.copyProperties(queryCond, map);
			String citystr = queryCond.getCityId();
			if (!(citystr != null && citystr.length() > 0)) {
				queryCond.setCityId((String) map.get("city"));
				queryCond.setCityName(InitServlet.cityObj.get(queryCond.getCityId()));	
			}

			queryCond.setDistrict((String) map.get("district"));
			String districtstr = queryCond.getDistrict();
			queryCond.setDistrictName(InitServlet.citySozeObj.get(districtstr));
			queryCond.setHotelCount(hotelroomcount);
			
			queryCond.setBusiness((String)map.get("business"));
			String businessstr = queryCond.getBusiness();
			queryCond.setBizZone((String)map.get("business"));
			queryCond.setBusinessName(InitServlet.businessSozeObj.get(businessstr));

			if(member == null) { // 如果当前没登录会员, 则根据roleUser属性设置
				if(roleUser.isOrg114()) {
					String memberState = roleUser.getState(); 
					if(StringUtil.isValidStr(memberState)) {
						if(memberState.equals("WTBJ")) { // 北京网通
							queryCond.setSaleChannel("04");	
						} else if(memberState.equals("WTT")) { // 网通
							queryCond.setSaleChannel("05");	
						} else if(memberState.equals("LTT")) { // 联通
							queryCond.setSaleChannel("02");	
						} else if(!memberState.equals("NHZY")){ // 非南航的，就取电信114
							queryCond.setSaleChannel("01");
						}
					} else {
						return forwardError("操作员没有省份!");
					}
				}
			} else if (!member.isMango()) {
				String memberState = member.getState(); 
				if(StringUtil.isValidStr(memberState)) {
					if(memberState.equals("WTBJ")) { // 北京网通
						queryCond.setSaleChannel("04");	
					} else if(memberState.equals("WTT")) { // 网通
						queryCond.setSaleChannel("05");	
					} else if(memberState.equals("LTT")) { // 联通
						queryCond.setSaleChannel("02");	
					} else if(!memberState.equals("NHZY")) { // 非南航的，就取电信114
						queryCond.setSaleChannel("01");
					}
				} else {
					return forwardError("会员没有省份!");
				}
			}	
			// 设置查询条件session, 当取消新建订单时用到
			putSession(SessionNames.QUERY_COND, queryCond);		
		}
		try {
			results = hotelService.queryHotels(queryCond);
		} catch(Exception e) {
			results = new ArrayList();
		}
		HotelInfo hotelInfo = new HotelInfo();
		Iterator it = results.iterator();
		while(it.hasNext()){
			hotelInfo = (HotelInfo)it.next();
			hotelId = hotelInfo.getHotelId();
			this.lstTaxCharge();
			/*if(orderService.getPreOrderSalePromos(hotelInfo.getHotelId(),beginDate,endDate).isEmpty()
				&& orderService.getPreOrderPresaleList(hotelInfo.getHotelId(),beginDate,endDate).isEmpty()) { 
				hotelInfo.setIsSalesPromo(0);
			} else { 
				hotelInfo.setIsSalesPromo(1);
			}*/
			if(hotelManage.queryHtlUsersCommentTotal(hotelInfo.getHotelId())==0) {hotelInfo.setIsUserComment(0);}else{hotelInfo.setIsUserComment(1);}
			
			if(lstTaxCharge.isEmpty()) {
				hotelInfo.setIsTaxCharges(0);
			}
			else{
				hotelInfo.setIsTaxCharges(1);
				//用于房费需另缴税直接显示结果 v2.9.2 addby chenjuesu
				hotelInfo.setLstRoomTaxCharge(lstRoomTaxCharge);
			}
		}
		//查询一年内是否有展会
		String checkExhibitInOneYear = queryExhibitInOneYear();
		if(checkExhibitInOneYear.equals("lsExhibit")){//没有会展信息,则在页面上不显示链接
			request.setAttribute("isHaveExhibitInOneYear", true);
		}
//		查询预订时间期间是否有展会 v2.9.2 addby chenjuesu
		Date threeDaysAfterEndDate = DateUtil.getDate(endDate, 3);//延迟三天时间
		List<HtlExhibit> exhibits = hotelService
			.queryExhibit(queryCond.getCityId(), beginDate, threeDaysAfterEndDate);
		request.setAttribute("exhibits", exhibits);
		int difdays = DateUtil.getDay(beginDate, endDate);
		request.setAttribute("difdays", difdays);
		int weekNum = (difdays - 1) / 7 + 1;
		for (int m = 0; m < results.size(); m++) {			 
			HotelInfo result = (HotelInfo) results.get(m);
			List roomTypes = result.getRoomTypes();
			for (int j = 0; j < roomTypes.size(); j++) {
				RoomType roomType = (RoomType) roomTypes.get(j);
				roomType.setTotalCount(roomType.getSaleItems().size() * weekNum);
			}
			if(result.getClueInfo()!=null){
				String temp=result.getClueInfo().replace("\r\n", "");
				result.setClueInfo(temp);
			}
		}
		dateStrList = DateUtil.getDateStrList(beginDate, endDate, false);
		 dateStrListLength = dateStrList.size();
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
		//判断用户是否有超级权限，有则可以下单 hotel2.6 @zhuangzhineng 2009-03-10
		
		//判断用户是否有超级权限.应该根据登陆账号，也就是CC的工号，不能根据会员编号 add by shengwei.zuo hotel2.6 2009-06-04
		if(orderService.queryUserPower(roleUser.getLoginName())){
			request.setAttribute("vip", "1");
		}else{
			request.setAttribute("vip", "0");
		}
		Map map = super.getParams();
		
		cityId = map.get("cityId")==null?"":map.get("cityId").toString();
		
		distance = map.get("distance")==null?0:Integer.valueOf(map.get("distance").toString());
		
		queryCond = new HotelQueryCondition();		
		
		MyBeanUtil.copyProperties(queryCond, map);
		String citystr = queryCond.getCityId();
		if(StringUtil.isValidStr(cityId)){
			cityName = InitServlet.cityObj.get(String.valueOf(cityId));
		}
		if(!StringUtil.isValidStr(notes)){
			longitude = null;
			latitude = null;
		}
		if (!(citystr != null && citystr.length() > 0)) {
			queryCond.setCityId((String) map.get("city"));
			queryCond.setCityName(InitServlet.cityObj.get(queryCond.getCityId()));	
		}
		if(StringUtil.isValidStr(price)){
			queryCond.setMinPrice(price);
		}
		queryCond.setDistrict((String) map.get("district"));
		String districtstr = queryCond.getDistrict();
		queryCond.setDistrictName(InitServlet.citySozeObj.get(districtstr));
		queryCond.setHotelCount(hotelroomcount);
		
		queryCond.setBusiness((String)map.get("business"));
		String businessstr = queryCond.getBusiness();
		queryCond.setBizZone((String)map.get("business"));
		queryCond.setBusinessName(InitServlet.businessSozeObj.get(businessstr));
		
		if(member == null) { // 如果当前没登录会员, 则根据roleUser属性设置
			if(roleUser.isOrg114()) {
				String memberState = roleUser.getState(); 
				if(StringUtil.isValidStr(memberState)) {
					if(memberState.equals("WTBJ")) { // 北京网通
						queryCond.setSaleChannel("04");	
					} else if(memberState.equals("WTT")) { // 网通
						queryCond.setSaleChannel("05");	
					} else if(memberState.equals("LTT")) { // 联通
						queryCond.setSaleChannel("02");	
					} else if(!memberState.equals("NHZY")){ // 非南航的，就取电信114
						queryCond.setSaleChannel("01");
					}
				} else {
					return forwardError("操作员没有省份!");
				}
			}
		} else if (!member.isMango()) {
			String memberState = member.getState(); 
			if(StringUtil.isValidStr(memberState)) {
				if(memberState.equals("WTBJ")) { // 北京网通
					queryCond.setSaleChannel("04");	
				} else if(memberState.equals("WTT")) { // 网通
					queryCond.setSaleChannel("05");	
				} else if(memberState.equals("LTT")) { // 联通
					queryCond.setSaleChannel("02");	
				} else if(!memberState.equals("NHZY")) { // 非南航的，就取电信114
					queryCond.setSaleChannel("01");
				}
			} else {
				return forwardError("会员没有省份!");
			}
		}	
		// 设置查询条件session, 当取消新建订单时用到
		putSession(SessionNames.QUERY_COND, queryCond);		
		
		//设置测试页面数据
		queryCond.setPageSize(10);
		
		boolean isDisplayAllHotel = false;
		List allHotelList = null ;
		if(StringUtil.isValidStr(display)&&"1".equals(display)){
			isDisplayAllHotel = true;
			allHotelList = new ArrayList();
		}else{
			isDisplayAllHotel = false;
		}
		try {
			results = hotelEmapService.queryHotels(queryCond, longitude, latitude,
					distance, null, null, null, null, isDisplayAllHotel,
					allHotelList);	
		} catch (Exception e) {
			results = new ArrayList();
		}
		
		try {
			List<MapsInfo> mapsInfoList=new ArrayList<MapsInfo>();
			String path = request.getRealPath("/");
			List mapShowResults = null;
			if(isDisplayAllHotel==true){
				mapShowResults = allHotelList ;
			}else{
				mapShowResults = results;
			}
			if(mapShowResults!=null&&mapShowResults.size()>0){
				for(int i=0;i<mapShowResults.size();i++){
					HotelEmapResultInfo result = (HotelEmapResultInfo) mapShowResults.get(i);	
					if((null!=result.getLatitude())&&(null!=result.getLongitude())&&(result.getGisid()>0)){
						MapsInfo mapsInfo = new MapsInfo();
						LatLng latlng=new LatLng();
						latlng.setLatitude(result.getLatitude());
						latlng.setLongitude(result.getLongitude());
						mapsInfo.setLatLng(latlng);
						mapsInfo.setName(result.getHotelChnName());
						mapsInfo.setGisId(result.getGisid());
						int index=result.getHotelStar().indexOf(".");
						mapsInfo.setImageUrl(path+"/images/emap/plack"+(index>0?result.getHotelStar().substring(0,index):result.getHotelStar())+".png");
						if(i<10){
							mapsInfo.setImageInfo(" "+String.valueOf(i+1)+"   $"+result.getLowestPrice());
						}else{
							mapsInfo.setImageInfo(String.valueOf(i+1)+"  $"+result.getLowestPrice());
						}
						mapsInfoList.add(mapsInfo);
					}
				}
			}
			gisMaps = gisService.kmlGenerator(mapsInfoList, 0, 0,path);
			
		} catch (ServiceException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		
		return "mapSearch";
	}
	
	/**
	 * 地图框选
	 * @return
	 */
	public String emapDrag(){
		member = getOnlineMember();		
		roleUser = getOnlineRoleUser();
		//判断用户是否有超级权限，有则可以下单 hotel2.6 @zhuangzhineng 2009-03-10
		
		//判断用户是否有超级权限.应该根据登陆账号，也就是CC的工号，不能根据会员编号 add by shengwei.zuo hotel2.6 2009-06-04
		if(orderService.queryUserPower(roleUser.getLoginName())){
			request.setAttribute("vip", "1");
		}else{
			request.setAttribute("vip", "0");
		}
		Map map = super.getParams();
		
		cityId = map.get("cityId")==null?"":map.get("cityId").toString();
		
		distance = map.get("distance")==null?0:Integer.valueOf(map.get("distance").toString());
		
		queryCond = new HotelQueryCondition();		
		
		MyBeanUtil.copyProperties(queryCond, map);
		String citystr = queryCond.getCityId();
		if(StringUtil.isValidStr(cityId)){
			cityName = InitServlet.cityObj.get(String.valueOf(cityId));
		}
		if (!(citystr != null && citystr.length() > 0)) {
			queryCond.setCityId((String) map.get("city"));
			queryCond.setCityName(InitServlet.cityObj.get(queryCond.getCityId()));	
		}
		if(StringUtil.isValidStr(price)){
			queryCond.setMinPrice(price);
		}
		queryCond.setDistrict((String) map.get("district"));
		String districtstr = queryCond.getDistrict();
		queryCond.setDistrictName(InitServlet.citySozeObj.get(districtstr));
		queryCond.setHotelCount(hotelroomcount);
		
		queryCond.setBusiness((String)map.get("business"));
		String businessstr = queryCond.getBusiness();
		queryCond.setBizZone((String)map.get("business"));
		queryCond.setBusinessName(InitServlet.businessSozeObj.get(businessstr));
		
		if(member == null) { // 如果当前没登录会员, 则根据roleUser属性设置
			if(roleUser.isOrg114()) {
				String memberState = roleUser.getState(); 
				if(StringUtil.isValidStr(memberState)) {
					if(memberState.equals("WTBJ")) { // 北京网通
						queryCond.setSaleChannel("04");	
					} else if(memberState.equals("WTT")) { // 网通
						queryCond.setSaleChannel("05");	
					} else if(memberState.equals("LTT")) { // 联通
						queryCond.setSaleChannel("02");	
					} else if(!memberState.equals("NHZY")){ // 非南航的，就取电信114
						queryCond.setSaleChannel("01");
					}
				} else {
					return forwardError("操作员没有省份!");
				}
			}
		} else if (!member.isMango()) {
			String memberState = member.getState(); 
			if(StringUtil.isValidStr(memberState)) {
				if(memberState.equals("WTBJ")) { // 北京网通
					queryCond.setSaleChannel("04");	
				} else if(memberState.equals("WTT")) { // 网通
					queryCond.setSaleChannel("05");	
				} else if(memberState.equals("LTT")) { // 联通
					queryCond.setSaleChannel("02");	
				} else if(!memberState.equals("NHZY")) { // 非南航的，就取电信114
					queryCond.setSaleChannel("01");
				}
			} else {
				return forwardError("会员没有省份!");
			}
		}	
		// 设置查询条件session, 当取消新建订单时用到
		putSession(SessionNames.QUERY_COND, queryCond);		
		
		//设置测试页面数据
		queryCond.setPageSize(10);
		boolean isDisplayAllHotel = false;
		List allHotelList = null ;
		if(StringUtil.isValidStr(display)&&"1".equals(display)){
			isDisplayAllHotel = true;
			allHotelList = new ArrayList();
		}else{
			isDisplayAllHotel = false;
		}
		
		try {
			results = hotelEmapService.queryHotels(queryCond, longitude, latitude,
					distance, new LatLng(nwLng, nwLat), new LatLng(swLng, swLat),
					new LatLng(neLng, neLat), new LatLng(seLng, seLat),
					isDisplayAllHotel, allHotelList);	
		} catch(Exception e) {
			results = new ArrayList();
		}
		
		try {
			List<MapsInfo> mapsInfoList=new ArrayList<MapsInfo>();
			String path = request.getRealPath("/");
			List mapShowResults = null;
			if(isDisplayAllHotel==true){
				mapShowResults = allHotelList ;
			}else{
				mapShowResults = results;
			}
			if(mapShowResults!=null&&mapShowResults.size()>0){
				for(int i=0;i<mapShowResults.size();i++){
					HotelEmapResultInfo result = (HotelEmapResultInfo) mapShowResults.get(i);	
					if((null!=result.getLatitude())&&(null!=result.getLongitude())&&(result.getGisid()>0)){
						MapsInfo mapsInfo = new MapsInfo();
						LatLng latlng=new LatLng();
						latlng.setLatitude(result.getLatitude());
						latlng.setLongitude(result.getLongitude());
						mapsInfo.setLatLng(latlng);
						mapsInfo.setName(result.getHotelChnName());
						mapsInfo.setGisId(result.getGisid());
						int index=result.getHotelStar().indexOf(".");
						mapsInfo.setImageUrl(path+"/images/emap/plack"+(index>0?result.getHotelStar().substring(0,index):result.getHotelStar())+".png");
						if(i<10){
							mapsInfo.setImageInfo(" "+String.valueOf(i+1)+"   $"+result.getLowestPrice());
						}else{
							mapsInfo.setImageInfo(String.valueOf(i+1)+"  $"+result.getLowestPrice());
						}
						mapsInfoList.add(mapsInfo);
					}
				}
			}
			gisMaps = gisService.kmlGenerator(mapsInfoList, 0, 0,path);
			
		} catch (ServiceException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		return "ajaxSerch";
	}
	
	public String sendMail(){
		gisToolbarService.sendMail(fromName,toMail,messageMail,false);
		return "ajaxSerch";
	}
	
	public String printMap(){
		
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
		if(tmpList!=null&&tmpList.size()>0){
			for(int i = 0; i<tmpList.size(); i++) {			
				HtlCreditAssure assure = tmpList.get(i);			
				String strCreditAssure = "";
				//----------------1-------------
				//天数
				if(assure.getAheadDays()==null && assure.getAheadTimer()!=null && assure.getAheadTimer()!=""){
					 strCreditAssure="提前";
					 strCreditAssure=strCreditAssure+"0天";
					 strCreditAssure=strCreditAssure+assure.getAheadTimer()+"时间";
				}
				
				if(assure.getAheadDays()!=null && assure.getAheadTimer()!=null && assure.getAheadTimer()!=""){
					strCreditAssure="提前";
				    strCreditAssure=strCreditAssure+assure.getAheadDays()+"天";		
				    strCreditAssure=strCreditAssure+assure.getAheadTimer()+"时间";
				}
				if(assure.getAheadDays()!=null && (assure.getAheadTimer()==null || assure.getAheadTimer()=="")){
					strCreditAssure="提前";
				    strCreditAssure=strCreditAssure+assure.getAheadDays()+"天";						  
				}
				
	           
				//----------------2------------------------
				Date date=new Date();	
				//日期
				if(assure.getMustBeforeDate()==null && assure.getMustBeforeTime()!=null && assure.getMustBeforeTime()!=""){
					if(strCreditAssure!=null && strCreditAssure!=""){
					  strCreditAssure = strCreditAssure+"，或者";
					}
					strCreditAssure=strCreditAssure+"必须在"+DateUtil.dateToString(date)+"日期";
					strCreditAssure=strCreditAssure+assure.getMustBeforeTime()+"时间之前预订";
				}
				
				if(assure.getMustBeforeDate()!=null && assure.getMustBeforeTime()!=null && assure.getMustBeforeTime()!=""){
					if(strCreditAssure!=null && strCreditAssure!=""){
						  strCreditAssure = strCreditAssure+"，或者";
					}
					strCreditAssure=strCreditAssure+"必须在"+DateUtil.dateToString(assure.getMustBeforeDate())+"日期";
					strCreditAssure=strCreditAssure+assure.getMustBeforeTime()+"时间之前预订";
				}
				if(assure.getMustBeforeDate()!=null && (assure.getMustBeforeTime()==null || assure.getMustBeforeTime()=="")){
					if(strCreditAssure!=null && strCreditAssure!=""){
						  strCreditAssure = strCreditAssure+"，或者";
					}
					strCreditAssure=strCreditAssure+"必须在"+DateUtil.dateToString(assure.getMustBeforeDate())+"日期之前预定";
				}
				//------------3-----------------------------
				
				if(assure.getContinueNight()!=null && assure.getContinueNight()!=""){
					if(strCreditAssure!=null && strCreditAssure!=""){
						  strCreditAssure = strCreditAssure+"，或者";
					}
					strCreditAssure=strCreditAssure+"连住"+assure.getContinueNight()+"晚.</br>";
				}
				//-----------4---------------------------------
				if(assure.getMustDate()!=null && assure.getMustDate()!=""){
					strCreditAssure=strCreditAssure+"连住日期:";
					strCreditAssure=strCreditAssure+assure.getMustDate();	
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
    public String lstTaxCharge(){
		
		lstTaxCharge = orderService.getTaxCharges(hotelId,beginDate,endDate);
		lstRoomTaxCharge = new ArrayList();
		//lstCommTaxCharge = new ArrayList();
		if(lstTaxCharge!=null){
			for(int i = 0 ; i < lstTaxCharge.size() ; i++){
				HtlTaxCharge taxCharge = (HtlTaxCharge)lstTaxCharge.get(i);
				if(taxCharge.getRoomIncTax()!=null){
					lstRoomTaxCharge.add(taxCharge);
					continue;
				}
				//lstCommTaxCharge.add(taxCharge);
			}
		}
		super.getParams().put("hotelId", hotelId);
		super.getParams().put("hotelID", hotelId);
		return "lstTaxCharge";
	}

    /**
	 * 查询一年内的展会
	 * @return
	 */
	public String queryExhibitInOneYear(){
		//获取当前日期,然后加一年
		Calendar now = Calendar.getInstance();
		now.add(Calendar.YEAR, 1);
		String cityId = (String) super.getParams().get("cityId");
		if(!StringUtil.isValidStr(cityId)){//如果cityId不是正常的,则从city中取值
			cityId = (String) super.getParams().get("city");
		}
		List<HtlExhibit> exhibits = hotelService
			.queryExhibit(cityId, new Date(), now.getTime());
		if(exhibits.size() == 0){
			return "noExhibitInOneYear";
		}else{
			request.setAttribute("exhibits", exhibits);
			return "lsExhibit";
		}
	}
	/** getter ans setter begin */

	public List getResults() {
		return results;
	}

	public void setResults(List results) {
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

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public IHotelService getHotelService() {
		return hotelService;
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

	public Map getPriceTypes() {
		return priceTypes;
	}

	public void setPriceTypes(Map priceTypes) {
		this.priceTypes = priceTypes;
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

	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public List getLstCommTaxCharge() {
		return lstCommTaxCharge;
	}

	public void setLstCommTaxCharge(List lstCommTaxCharge) {
		this.lstCommTaxCharge = lstCommTaxCharge;
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

	public void setGisToolbarService(GisToolbarService gisToolbarService) {
		this.gisToolbarService = gisToolbarService;
	}
	public int getDateStrListLength() {
		return dateStrListLength;
	}

	public void setDateStrListLength(int dateStrListLength) {
		this.dateStrListLength = dateStrListLength;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}
	
	/** getter ans setter end */
}
