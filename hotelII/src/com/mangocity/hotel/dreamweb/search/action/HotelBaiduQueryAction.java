package com.mangocity.hotel.dreamweb.search.action;

import java.util.List;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;

import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.service.GeographicalPositionService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.dreamweb.util.action.GenericWebAction;
import com.mangocity.hotel.search.constant.CityBrandConstant;
import com.mangocity.hotel.search.handler.impl.HotelQueryHandler;
import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.service.HotelSearchService;
import com.mangocity.hotel.search.template.HotelListShowVm;
import com.mangocity.hotel.search.vo.HotelResultVO;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.hotel.constant.SaleChannel;
import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.CookieUtils;

public class HotelBaiduQueryAction extends GenericWebAction{
	
	/**
	 * 
	 */
	private static final MyLog log = MyLog.getLogger(HotelBaiduQueryAction.class);
	
	private String cityCode ;
	private String cityName;
	//日期是因为界面的传出类型为String
	private String inDate;
	private String outDate;
	private String hotelName;
	private String bizCode;//商区代码，比如：PEKAYYYB
	private String bizValue;//商区值
	private String distinctCode;//行政区代码 
	private String distinctValue;//行政区值
	private String priceStr; //价格范围
	private String hotelStar;//酒店星级
	private String geoId;
	private String geoName;
	private String geoType;
	private String hotelGroupId; //酒店品牌
	private String hotelGroupName;
	private String hotelListOut;
	private int hotelCount = 0;
	private String hotelIdsStr = "";
	private String label;//从海外酒店的标示
	private String promoteType;
	
	private String projectCode;
	
	static String BAIDUAPPPROJECTCODE = "0020025";  //百度APP渠道号
	
	//小助手是否展示
	private String display_helper ="none";
	
	//注入service
	private GeographicalPositionService geographicalPositionService;
	private HotelSearchService hotelSearchService;
	
	public String execute()throws Exception{
		hotelName = URLDecoder.decode(hotelName,"UTF-8");
		geoName = URLDecoder.decode(geoName,"UTF-8");
		
		request=getRequest();
		httpResponse=getHttpResponse();
		
		if(StringUtil.isValidStr(projectCode)&&BAIDUAPPPROJECTCODE.equals(projectCode)){
			Cookie cookieProject = new Cookie("projectcode",projectCode);
			cookieProject.setPath("/");
			httpResponse.addCookie(cookieProject);
		}
		
		
		label = request.getParameter("label");
        //处理日期为空的情况
        if (null == inDate || null == outDate) {
			inDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 1));
			outDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 2));
        }
       
        
        //添加入住、离店日期到cookie
		CookieUtils.setCookie(request, httpResponse, "inDate", inDate, -1, "", "/");
		CookieUtils.setCookie(request, httpResponse, "outDate", outDate, -1, "", "/");
        int nightNum = DateUtil.getDay(DateUtil.stringToDateMain(inDate,"yyyy-MM-dd"),DateUtil.stringToDateMain(outDate,"yyyy-MM-dd"));
        if (0 >= nightNum || 28 < nightNum) {
            return super.forwardError("查询不能超过28天");
        }
        if(StringUtil.isValidStr(cityCode)){
        	cityCode = cityCode.toUpperCase();
        	cityName = InitServlet.cityObj.get(cityCode);
        }
        if(StringUtil.isValidStr(bizCode)){
        	bizCode = bizCode.toUpperCase();
        	bizValue = InitServlet.businessSozeObj.get(bizCode);
        }
        if(StringUtil.isValidStr(distinctCode)){
        	distinctCode = distinctCode.toUpperCase();
        	distinctValue = InitServlet.citySozeObj.get(distinctCode);
        }
        if(StringUtil.isValidStr(hotelGroupId)){
        	hotelGroupName = CityBrandConstant.getCityBrandName(Long.parseLong(hotelGroupId));
        }
        if(StringUtil.isValidStr(geoId)){
        	try{
        	   HtlGeographicalposition geographicalposition = geographicalPositionService.getGeographicalposition(Long.valueOf(geoId));
        	   geoName = geographicalposition.getName();
        	   geoType = String.valueOf(geographicalposition.getGptypeId());
        	}catch(Exception e){
        		log.error("HotelQueryAction 查询地理信息出错:geoId ="+geoId+"error:",e);
        	}
        }
        try {
        //设值
        QueryHotelCondition queryHotelCondition = new QueryHotelCondition();
        queryHotelCondition.setFromChannel(SaleChannel.WEB);
        queryHotelCondition.setInDate(DateUtil.getDate(inDate));
        queryHotelCondition.setOutDate(DateUtil.getDate(outDate));
        queryHotelCondition.setHotelName(hotelName);
        queryHotelCondition.setCityCode(cityCode);
        queryHotelCondition.setBizZone(bizCode);
        queryHotelCondition.setDistrict(distinctCode);
        if(hotelStar != null ){
            queryHotelCondition.setStarLeval(hotelStar.replaceAll("#", ""));
        }
        if(StringUtil.isValidStr(priceStr)){
        	String[] priceArr = priceStr.split("-");
        	queryHotelCondition.setMinPrice(priceArr[0]);
        	if(priceArr.length>1) {
        		queryHotelCondition.setMaxPrice(priceArr[1]);
        	}
        }
        queryHotelCondition.setHotelGroup(hotelGroupId);
        queryHotelCondition.setGeoName(geoName);
        queryHotelCondition.setGeoId(geoId);
        if(StringUtil.isValidStr(promoteType)){
        	queryHotelCondition.setPromoteType(Integer.parseInt(promoteType));
        }else{
        	promoteType = "1";
        }      
        CookieUtils.setCookie(request, httpResponse, "promoteType", promoteType, -1, "", "/");
       
			HotelQueryHandler handler = new HotelQueryHandler();
			long time_start = System.currentTimeMillis();
			handler.setQueryHotelCondition(queryHotelCondition);
			hotelSearchService.queryOnlyHotelsByHandler(queryHotelCondition, handler);
			log.info("酒店基本信息查询时间（ms）:" + (System.currentTimeMillis() - time_start));
			List<HotelResultVO> hotelVOList = handler.getHotelResutlList();
			hotelCount = handler.getHotelCount();
			for (int i = 0; i < hotelVOList.size(); i++) {
				String hotelId = String.valueOf(hotelVOList.get(i).getHotelId());
				hotelIdsStr += hotelId + ",";
			}
			hotelIdsStr = StringUtil.deleteLastChar(hotelIdsStr, ',');

			HotelListShowVm vm = new HotelListShowVm();
			hotelListOut = new String();
			if (hotelVOList.size() == 0) {
				display_helper = "";
			}
			for (int i = 0; i < hotelVOList.size(); i++) {
				String hotelListOut1 = vm.getHotelListWithTemplate(hotelVOList.get(i));
				hotelListOut += hotelListOut1;
			}
			log.info("酒店总时间（ms）:" + (System.currentTimeMillis() - time_start));
		} catch (Exception e) {
			super.setErrorCode("H02");
			log.error("HotelQueryAction  query hotel has a wrong!", e);
		}
		
        return SUCCESS;
	}


	public String getCityCode() {
		return cityCode;
	}


	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}


	public String getInDate() {
		return inDate;
	}


	public void setInDate(String inDate) {
		this.inDate = inDate;
	}


	public String getOutDate() {
		return outDate;
	}


	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}


	public String getHotelName() {
		return hotelName;
	}


	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}


	public String getBizCode() {
		return bizCode;
	}


	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}



	public String getPriceStr() {
		return priceStr;
	}


	public void setPriceStr(String priceStr) {
		this.priceStr = priceStr;
	}


	public String getHotelStar() {
		return hotelStar;
	}


	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}


	public String getCityName() {
		return cityName;
	}


	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


	public String getDistinctCode() {
		return distinctCode;
	}


	public void setDistinctCode(String distinctCode) {
		this.distinctCode = distinctCode;
	}


	public String getGeoId() {
		return geoId;
	}


	public void setGeoId(String geoId) {
		this.geoId = geoId;
	}


	public String getGeoName() {
		return geoName;
	}


	public void setGeoName(String geoName) {
		this.geoName = geoName;
	}


	public void setGeographicalPositionService(
			GeographicalPositionService geographicalPositionService) {
		this.geographicalPositionService = geographicalPositionService;
	}


	public String getBizValue() {
		return bizValue;
	}


	public void setBizValue(String bizValue) {
		this.bizValue = bizValue;
	}


	public String getDistinctValue() {
		return distinctValue;
	}


	public void setDistinctValue(String distinctValue) {
		this.distinctValue = distinctValue;
	}


	public String getGeoType() {
		return geoType;
	}


	public void setGeoType(String geoType) {
		this.geoType = geoType;
	}


	public String getHotelGroupId() {
		return hotelGroupId;
	}


	public void setHotelGroupId(String hotelGroupId) {
		this.hotelGroupId = hotelGroupId;
	}


	public String getHotelGroupName() {
		return hotelGroupName;
	}


	public void setHotelGroupName(String hotelGroupName) {
		this.hotelGroupName = hotelGroupName;
	}


	public void setHotelSearchService(HotelSearchService hotelSearchService) {
		this.hotelSearchService = hotelSearchService;
	}


	public String getHotelListOut() {
		return hotelListOut;
	}


	public void setHotelListOut(String hotelListOut) {
		this.hotelListOut = hotelListOut;
	}


	public String getHotelIdsStr() {
		return hotelIdsStr;
	}


	public void setHotelIdsStr(String hotelIdsStr) {
		this.hotelIdsStr = hotelIdsStr;
	}


	public int getHotelCount() {
		return hotelCount;
	}


	public void setHotelCount(int hotelCount) {
		this.hotelCount = hotelCount;
	}


	public String getDisplay_helper() {
		return display_helper;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getPromoteType() {
		return promoteType;
	}


	public void setPromoteType(String promoteType) {
		this.promoteType = promoteType;
	}


	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	
}
