package com.mangocity.hweb.persistence;

import java.io.Serializable;


public class DistrictHotelInfo implements Serializable {
	
	//酒店Id
	private Long hotelId;
	
	//酒店中文名
	private String hotelChnName;
	
	//是否可预订标志
	private boolean bookingFlag;
	
	//酒店最低价格
	private int minSalePrice;
	
	//酒店星级
	private float hotelStar;
	
	private String hotelStarChnName;
	
	//商业区编码
	private String  businessArea;
	
	//行政区
	private String zone;
	
	//城市三字码
	private String cityCode;
	
	//合同币种
	private  String currency;
	
	//推荐级别
	private  int tuijian;
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getTuijian() {
		return tuijian;
	}

	public void setTuijian(int tuijian) {
		this.tuijian = tuijian;
	}

	public String getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(String businessArea) {
		if(null == businessArea){
			this.businessArea = "";
		}else{
			this.businessArea = businessArea;
		}
		
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public boolean isBookingFlag() {
		return bookingFlag;
	}

	public void setBookingFlag(boolean bookingFlag) {
		this.bookingFlag = bookingFlag;
	}

	public int getMinSalePrice() {
		return minSalePrice;
	}

	public void setMinSalePrice(int minSalePrice) {
		this.minSalePrice = minSalePrice;
	}

	public String getHotelChnName() {
		return hotelChnName;
	}

	public void setHotelChnName(String hotelChnName) {
		this.hotelChnName = hotelChnName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getZone() {
		return zone;
	}

	public float getHotelStar() {
		return hotelStar;
	}

	public void setZone(String zone) {
		if(null == zone){
			this.zone = "";
		}else{
			this.zone = zone;
		}
		
	}

	public void setHotelStar(float hotelStar) {
		this.hotelStar = hotelStar;
	}

	public String getHotelStarChnName() {
		return hotelStarChnName;
	}

	public void setHotelStarChnName(String hotelStarChnName) {
		this.hotelStarChnName = hotelStarChnName;
	}
	
	

}
