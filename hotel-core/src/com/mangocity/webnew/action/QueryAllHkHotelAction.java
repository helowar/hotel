package com.mangocity.webnew.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.hweb.persistence.HotelBookingResultInfoForHkSale;
import com.mangocity.hweb.persistence.QHotelInfo;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.webnew.service.QueryAllHkHotelService;


public class QueryAllHkHotelAction extends GenericCCAction {

	private static final long serialVersionUID = -6371488760878200329L;
	
	private QueryAllHkHotelService queryAllHkHotelService;
	
	private String cityCode;
	
	//可售日期
	private String ableSaleDate;
	
	//结束日期=可售日期+1
	private String ableSaleEndDate;
	
	private String hotelURL;
	
	private String chnName;
	
	
	
	//酒店id字符串
	String hotelIdStr;
	
	private List<HotelBookingResultInfoForHkSale> hotelBookingResultInfoForHkSaleList;
	
	private Map<String,List<QHotelInfo>> qHotelBookingResultInfoForHkSaleMap;

	public String execute(){
		String reqeustUrl = request.getRequestURI();
		if(cityCode!=null && !cityCode.matches("^\\w+$")){
			cityCode="HKG";
		}
		
	    if(reqeustUrl.contains("mac")){
	    	cityCode = "MAC";
	    }
		//默认查询香港酒店
		if(null == cityCode){
			cityCode= "HKG";
		}
		if(ableSaleDate==null||(ableSaleDate!=null && !ableSaleDate.matches("^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}"))){
			//默认查询一天星期之后的数据
			ableSaleDate = DateUtil.dateToString(DateUtil.getDate(new Date(),7));
			ableSaleEndDate = DateUtil.dateToString(DateUtil.getDate(new Date(),8));
		}else{
			ableSaleEndDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.stringToDateMain(ableSaleDate, "yyyy-MM-dd"),1));
		}
		try{
			//获取芒果网的港澳酒店
			hotelBookingResultInfoForHkSaleList = 
				queryAllHkHotelService.queryHkHotelInfo(cityCode,ableSaleDate);
			
			/*
			 * modify by alfred.
			 * 获取青芒果酒店
			 * hotelBookingResultInfoForHkSaleList用于获取该城市的商业区编码
			 */
			qHotelBookingResultInfoForHkSaleMap = queryAllHkHotelService.queryQHotelInfo(cityCode,ableSaleDate);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return SUCCESS ;
	}
	
	/**
	 * 青芒果调整
	 * @return
	 */
	public String skipQmango() {
		
		return "skipQmango";
	}
	
	public void setQueryAllHkHotelService(
			QueryAllHkHotelService queryAllHkHotelService) {
		this.queryAllHkHotelService = queryAllHkHotelService;
	}

	public List<HotelBookingResultInfoForHkSale> getHotelBookingResultInfoForHkSaleList() {
		return hotelBookingResultInfoForHkSaleList;
	}

	public void setHotelBookingResultInfoForHkSaleList(
			List<HotelBookingResultInfoForHkSale> hotelBookingResultInfoForHkSaleList) {
		this.hotelBookingResultInfoForHkSaleList = hotelBookingResultInfoForHkSaleList;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAbleSaleDate() {
		return ableSaleDate;
	}

	public void setAbleSaleDate(String ableSaleDate) {
		this.ableSaleDate = ableSaleDate;
	}

	public String getAbleSaleEndDate() {
		return ableSaleEndDate;
	}

	public void setAbleSaleEndDate(String ableSaleEndDate) {
		this.ableSaleEndDate = ableSaleEndDate;
	}

	public Map<String, List<QHotelInfo>> getQHotelBookingResultInfoForHkSaleMap() {
		return qHotelBookingResultInfoForHkSaleMap;
	}

	public void setQHotelBookingResultInfoForHkSaleMap(
			Map<String, List<QHotelInfo>> hotelBookingResultInfoForHkSaleMap) {
		qHotelBookingResultInfoForHkSaleMap = hotelBookingResultInfoForHkSaleMap;
	}

	public String getHotelURL() {
		return hotelURL;
	}

	public void setHotelURL(String hotelURL) {
		this.hotelURL = hotelURL;
	}

	public String getChnName() {

		return this.chnName;
		
	}

	public void setChnName(String chnName) {
		
		this.chnName = chnName;
	}
}

