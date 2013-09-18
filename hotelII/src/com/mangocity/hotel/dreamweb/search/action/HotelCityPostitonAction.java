package com.mangocity.hotel.dreamweb.search.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.service.GeographicalPositionService;
import com.opensymphony.xwork2.ActionSupport;

public class HotelCityPostitonAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private Integer type;
	private Map cityList;
	private String typeName;
	private GeographicalPositionService geographicalPositionService;
	public String execute(){
		cityList=geographicalPositionService.findAllCity();
		//typeName=(String) getTypeMap().get(type);
		return SUCCESS;
	}
	
	private Map getTypeMap(){
		Map classMap=new HashMap<Integer,String>();
		classMap.put(21, "交通枢纽");
		classMap.put(23, "景点地标");
		classMap.put(24, "地铁站");
		classMap.put(26, "大学");
		classMap.put(27, "医院");
		classMap.put(28, "主题");
		classMap.put(29, "交通");
		classMap.put(30, "商企");
		classMap.put(31, "生活服务");
		classMap.put(32, "购物");
		classMap.put(33, "餐馆");
		return classMap;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	public Map getCityList() {
		return cityList;
	}

	public void setCityList(Map cityList) {
		this.cityList = cityList;
	}

	public GeographicalPositionService getGeographicalPositionService() {
		return geographicalPositionService;
	}
	public void setGeographicalPositionService(
			GeographicalPositionService geographicalPositionService) {
		this.geographicalPositionService = geographicalPositionService;
	}
	
}
