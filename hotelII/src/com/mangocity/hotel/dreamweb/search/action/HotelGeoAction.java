package com.mangocity.hotel.dreamweb.search.action;

import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.service.GeoDistanceService;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;
import com.mangocity.hotel.search.service.HotelSearcher;
import com.opensymphony.xwork2.ActionSupport;

public class HotelGeoAction extends ActionSupport{
	private GeoDistanceService geoDistanceService;
	public HotelSearcher hotelSearcher;	
	private Map hotelBasicInfoMap;
	private String positionName;
	private String cityCode;
	public String execute(){
		//geoDistanceService.buildGeoInfo();
		HotelBasicInfoSearchParam hotelBasicInfoSearchParam = new HotelBasicInfoSearchParam();
		hotelBasicInfoSearchParam.setCityCode(cityCode);
		hotelBasicInfoSearchParam.setHotelName("酒店");
		HtlGeographicalposition htlGeoPos = new HtlGeographicalposition();
		htlGeoPos.setCityCode(cityCode);
		htlGeoPos.setName(positionName);
		hotelBasicInfoSearchParam.setHtlGeographicalposition(htlGeoPos);
			try {
			
			long beginTime = System.currentTimeMillis();
			hotelBasicInfoMap = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam);
			long endTime = System.currentTimeMillis();
			//assertEquals(196, hotelBasicInfoMap.size());
			
			System.out.println("Hotel Count: " + hotelBasicInfoMap.size() + ", Total Time:" + (endTime - beginTime));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public GeoDistanceService getGeoDistanceService() {
		return geoDistanceService;
	}
	public void setGeoDistanceService(GeoDistanceService geoDistanceService) {
		this.geoDistanceService = geoDistanceService;
	}
	
	public HotelSearcher getHotelSearcher() {
		return hotelSearcher;
	}
	public void setHotelSearcher(HotelSearcher hotelSearcher) {
		this.hotelSearcher = hotelSearcher;
	}
	public Map getHotelBasicInfoMap() {
		return hotelBasicInfoMap;
	}
	public void setHotelBasicInfoMap(Map hotelBasicInfoMap) {
		this.hotelBasicInfoMap = hotelBasicInfoMap;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	
}
