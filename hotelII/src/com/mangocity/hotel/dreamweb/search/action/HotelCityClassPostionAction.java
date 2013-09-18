package com.mangocity.hotel.dreamweb.search.action;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlGeoDistance;
import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.service.GeographicalPositionService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.search.service.HotelMgisSearchService;
import com.mangocity.hotel.search.service.HotelSearcher;
import com.opensymphony.xwork2.ActionSupport;

public class HotelCityClassPostionAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private Integer type;
	private String cityCode;
	private GeographicalPositionService geographicalPositionService;
	private HotelMgisSearchService hotelMgisSearchService;
	private String cityName;
	private Map<String,List> resultMap;
	private List cityList;
	public String execute(){
		cityCode=cityCode.toUpperCase();
		resultMap=hotelMgisSearchService.getIndexMgisInfo(cityCode);
		cityList=geographicalPositionService.findCityByCode(cityCode);
		cityName=InitServlet.localCityObj.get(cityCode);
		return SUCCESS;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public GeographicalPositionService getGeographicalPositionService() {
		return geographicalPositionService;
	}
	public void setGeographicalPositionService(
			GeographicalPositionService geographicalPositionService) {
		this.geographicalPositionService = geographicalPositionService;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Map<String, List> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, List> resultMap) {
		this.resultMap = resultMap;
	}

	public HotelMgisSearchService getHotelMgisSearchService() {
		return hotelMgisSearchService;
	}

	public void setHotelMgisSearchService(
			HotelMgisSearchService hotelMgisSearchService) {
		this.hotelMgisSearchService = hotelMgisSearchService;
	}

	public List getCityList() {
		return cityList;
	}

	public void setCityList(List cityList) {
		this.cityList = cityList;
	}
	
}
