package com.mangocity.hotel.dreamweb.search.action;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlGeoDistance;
import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.service.GeoDistanceService;
import com.mangocity.hotel.search.service.HotelSearcher;
import com.opensymphony.xwork2.ActionSupport;

public class HotelPostionAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private GeoDistanceService geoDistanceService;
	public HotelSearcher hotelSearcher;	
	private List<HtlGeographicalposition>geoList;
	private String cityCode;
	private Long type;
	public String execute(){
		HtlGeographicalposition geo=new HtlGeographicalposition();
		geo.setCityCode(cityCode.toUpperCase());
		geo.setGptypeId(type);
		geoList=hotelSearcher.searchHotelGeoInfo(geo);
		return SUCCESS;
	}
	public GeoDistanceService getGeoDistanceService() {
		return geoDistanceService;
	}
	public void setGeoDistanceService(GeoDistanceService geoDistanceService) {
		this.geoDistanceService = geoDistanceService;
	}
	public List<HtlGeographicalposition> getGeoList() {
		return geoList;
	}
	public void setGeoList(List<HtlGeographicalposition> geoList) {
		this.geoList = geoList;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	public HotelSearcher getHotelSearcher() {
		return hotelSearcher;
	}
	public void setHotelSearcher(HotelSearcher hotelSearcher) {
		this.hotelSearcher = hotelSearcher;
	}
	

}
