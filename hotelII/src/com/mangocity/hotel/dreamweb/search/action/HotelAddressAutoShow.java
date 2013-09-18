package com.mangocity.hotel.dreamweb.search.action;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jsonplugin.annotations.JSON;
import com.mangocity.hotel.dreamweb.search.service.HotelInfoService;
import com.opensymphony.xwork2.ActionSupport;

public class HotelAddressAutoShow extends ActionSupport{

	private HotelInfoService hotelInfoService;
	private String cityCode="PEK";
	private String distanceName;
	private List<String> content=new ArrayList<String>();
	
	public String execute(){
		content=hotelInfoService.queryHotelInfoByMgis(cityCode,distanceName);
		return SUCCESS;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistanceName() {
		return distanceName;
	}

	public void setDistanceName(String distanceName) {
		this.distanceName = distanceName;
	}

	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}
	@JSON(serialize=false)
	public HotelInfoService getHotelInfoService() {
		return hotelInfoService;
	}

	public void setHotelInfoService(HotelInfoService hotelInfoService) {
		this.hotelInfoService = hotelInfoService;
	}
	
}
