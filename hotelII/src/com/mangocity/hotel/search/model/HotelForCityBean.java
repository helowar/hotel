package com.mangocity.hotel.search.model;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.search.sort.SortedHotelInfo;
 
public class HotelForCityBean implements Comparable<HotelForCityBean> {

	private String cityCode;
	private String cityName;
	private int hotelCount;
	private List<HotelBasicInfo> hotelList;
	private String  cityFirstLetterCn;//城市的拼音的首字母
	
	public int compareTo(HotelForCityBean hotelForCityBean){
		return hotelForCityBean.getHotelList().size() - this.getHotelList().size(); 
	}
	
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public int getHotelCount() {
		return hotelCount;
	}
	public void setHotelCount(int hotelCount) {
		this.hotelCount = hotelCount;
	}
	public List<HotelBasicInfo> getHotelList() {
		if(null == hotelList){
			hotelList = new ArrayList<HotelBasicInfo>();
		}
		return hotelList;
	}
	public void setHotelList(List<HotelBasicInfo> hotelList) {
		this.hotelList = hotelList;
	}

	public String getCityFirstLetterCn() {
		return cityFirstLetterCn;
	}

	public void setCityFirstLetterCn(String cityFirstLetterCn) {
		this.cityFirstLetterCn = cityFirstLetterCn;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	
}
