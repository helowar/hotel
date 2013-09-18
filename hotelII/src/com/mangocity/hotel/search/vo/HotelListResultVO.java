package com.mangocity.hotel.search.vo;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.search.vo.HotelResultVO;

// this VO is used for template
public class HotelListResultVO implements SerializableVO {
	
	public HotelListResultVO(){}
	
	//酒店总数
	private int hotelCount;
	
	private String hotelIdsStr;
	
	//酒店列表
	private List<HotelResultVO> hotelResutlList = new ArrayList<HotelResultVO>(15);

	public int getHotelCount() {
		return hotelCount;
	}

	public void setHotelCount(int hotelCount) {
		this.hotelCount = hotelCount;
	}

	public List<HotelResultVO> getHotelResutlList() {
		return hotelResutlList;
	}

	public void setHotelResutlList(List<HotelResultVO> hotelResutlList) {
		this.hotelResutlList = hotelResutlList;
	}

	public String getHotelIdsStr() {
		return hotelIdsStr;
	}

	public void setHotelIdsStr(String hotelIdsStr) {
		this.hotelIdsStr = hotelIdsStr;
	}

}
