package com.mangocity.hotel.search.vo;

public class HotelTemplateVO  implements SerializableVO {

	public HotelTemplateVO(){};
	
	private String hotelListOut;
	private String hotelIdsStr;
	private int hotelCount = 0;
	public String getHotelListOut() {
		return hotelListOut;
	}
	public void setHotelListOut(String hotelListOut) {
		this.hotelListOut = hotelListOut;
	}
	public String getHotelIdsStr() {
		return hotelIdsStr;
	}
	public void setHotelIdsStr(String hotelIdsStr) {
		this.hotelIdsStr = hotelIdsStr;
	}
	public int getHotelCount() {
		return hotelCount;
	}
	public void setHotelCount(int hotelCount) {
		this.hotelCount = hotelCount;
	}
	
}
