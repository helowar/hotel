package com.mangocity.hotel.dreamweb.search.action;

import java.util.Map;

import com.mangocity.hotel.base.service.GeoDistanceService;
import com.opensymphony.xwork2.ActionSupport;

public class HotelGeoDistanceAction  extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private GeoDistanceService geoDistanceService;
	private Map hotelBasicInfoMap;
	public String execute(){
		long a = System.currentTimeMillis();
		try {
			geoDistanceService.buildGeoInfo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis()-a);
		return SUCCESS;
	}
	public GeoDistanceService getGeoDistanceService() {
		return geoDistanceService;
	}
	public void setGeoDistanceService(GeoDistanceService geoDistanceService) {
		this.geoDistanceService = geoDistanceService;
	}
	public Map getHotelBasicInfoMap() {
		return hotelBasicInfoMap;
	}
	public void setHotelBasicInfoMap(Map hotelBasicInfoMap) {
		this.hotelBasicInfoMap = hotelBasicInfoMap;
	}
	
}
