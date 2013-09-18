package com.mangocity.webco.action;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.hweb.persistence.HotelBookingResultInfoForHkSale;
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
	
	//酒店id字符串
	String hotelIdStr;
	
	private List<HotelBookingResultInfoForHkSale> hotelBookingResultInfoForHkSaleList;

	public String execute(){
		//默认查询香港酒店
		if(null == cityCode){
			cityCode= "HKG";
		}
		if(!StringUtil.isValidStr(ableSaleDate)){
			//默认查询一天星期之后的数据
			ableSaleDate = DateUtil.dateToString(DateUtil.getDate(new Date(),7));
			ableSaleEndDate = DateUtil.dateToString(DateUtil.getDate(new Date(),8));
		}else{
			ableSaleEndDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.stringToDateMain(ableSaleDate, "yyyy-MM-dd"),1));
		}
		try{
			hotelBookingResultInfoForHkSaleList = 
				queryAllHkHotelService.queryHkHotelInfo(cityCode,ableSaleDate);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return SUCCESS ;
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


}

