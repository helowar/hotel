package com.mangocity.hotel.dreamweb.search.action;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.search.service.HotelMgisSearchService;
import com.opensymphony.xwork2.ActionSupport;

public class HotelHotAreaAction extends ActionSupport {

	private static final long serialVersionUID = 5546637269934629212L;
	private HotelMgisSearchService hotelMgisSearchService;
	private String cityCode;
	private Map<String,List> resultMap;
	public String execute(){
		resultMap=hotelMgisSearchService.getIndexMgisInfo(cityCode);
	   return SUCCESS ;
	}
	public HotelMgisSearchService getHotelMgisSearchService() {
		return hotelMgisSearchService;
	}
	public void setHotelMgisSearchService(
			HotelMgisSearchService hotelMgisSearchService) {
		this.hotelMgisSearchService = hotelMgisSearchService;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public Map<String, List> getResultMap() {
		return resultMap;
	}
	public void setResultMap(Map<String, List> resultMap) {
		this.resultMap = resultMap;
	}
}
