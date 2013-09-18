package com.mangocity.hotel.dreamweb.search.action;

import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;
import com.mangocity.hotel.search.service.HotelSearcher;
import com.opensymphony.xwork2.ActionSupport;

public class HotelInfoMapAction  extends ActionSupport{

	private static final long serialVersionUID = -1136703646232653139L;
	private HotelSearcher hotelSearcher;
	private String hotelId;
	private HotelBasicInfo hotelInfo;
	public String execute(){
		HotelBasicInfoSearchParam hotelBasicInfoSearchParam=new HotelBasicInfoSearchParam();
		hotelBasicInfoSearchParam.setHotelId(hotelId);
		hotelInfo = hotelSearcher.searchHotelBasicInfo(hotelBasicInfoSearchParam).get(hotelId);
		LOG.debug("hotel Name="+hotelInfo.getChnName());
		return SUCCESS;
	}
	public HotelSearcher getHotelSearcher() {
		return hotelSearcher;
	}
	public void setHotelSearcher(HotelSearcher hotelSearcher) {
		this.hotelSearcher = hotelSearcher;
	}
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	public HotelBasicInfo getHotelInfo() {
		return hotelInfo;
	}
	public void setHotelInfo(HotelBasicInfo hotelInfo) {
		this.hotelInfo = hotelInfo;
	}

}
