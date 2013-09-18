package com.mangocity.webnew.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.hweb.manage.HotelManageWeb;

public class HotelQueryBusinessAction extends GenericCCAction{
	private static final long serialVersionUID = 1L;
	private String cityName;
	private HotelManageWeb hotelManageWeb;
	List districtList=new ArrayList();
	List businessList=new ArrayList();
	public String execute(){
		Map map=hotelManageWeb.queryBusinessForCityId(cityName);
		districtList=(List) map.get("district");
		businessList=(List) map.get("business");
		log.error("HotelQueryBusinessAction districtList size:"+districtList.size());
		log.error("HotelQueryBusinessAction businessList size:"+businessList.size());
		return SUCCESS;
	}
	
	
	
	public List getBusinessList() {
		return businessList;
	}

	public void setBusinessList(List businessList) {
		this.businessList = businessList;
	}

	public List getDistrictList() {
		return districtList;
	}



	public void setDistrictList(List districtList) {
		this.districtList = districtList;
	}



	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}
	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

}
