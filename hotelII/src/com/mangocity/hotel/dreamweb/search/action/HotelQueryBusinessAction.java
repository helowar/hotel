package com.mangocity.hotel.dreamweb.search.action;

import java.util.List;
import java.util.Map;

import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.util.log.MyLog;
import com.opensymphony.xwork2.ActionSupport;

public class HotelQueryBusinessAction extends ActionSupport {
	private String cityName;
	List districtList;
	List businessList;
	private static final MyLog log = MyLog.getLogger(HotelQueryBusinessAction.class);

	// 注入的service
	private HotelManageWeb hotelManageWeb;

	public String execute(){
		try{
		    Map map=hotelManageWeb.queryBusinessForCityId(cityName);
		    districtList=(List) map.get("district");
		    businessList=(List) map.get("business");
		}catch(Exception e){
			log.error("查询商区错误", e);
		}
		// log.info("cityName:"+cityName);
		// log.info("districtList:"+districtList);
		// log.info("districtList:"+districtList ==null ? 0 :
		// districtList.size());

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

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

}
