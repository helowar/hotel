package com.mangocity.hotel.search.album.vo;

import java.util.List;

public class QuotationQueryInfoVO {
	private String hotelId;
	
	private String hotelChName;
	
	private String hotelEnName;
	
	private String roomTypeId;
	
	private String roomTypeName;
	
	private String priceTypeId;
	
	private String priceTypeName;

	private List<QuotationSaleInfoVO> saleInfo;
	
	private String cityCode;
	
	/**
	 * 是否一个下线,true 已经下架，false ：没有下架
	 */
	private boolean hasReferrals;
	
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	
	public String getHotelChName() {
		return hotelChName;
	}
	public void setHotelChName(String hotelChName) {
		this.hotelChName = hotelChName;
	}
	public String getHotelEnName() {
		return hotelEnName;
	}
	public void setHotelEnName(String hotelEnName) {
		this.hotelEnName = hotelEnName;
	}
	public String getRoomTypeId() {
		return roomTypeId;
	}
	public void setRoomTypeId(String roomTypeId) {
		this.roomTypeId = roomTypeId;
	}
	public String getRoomTypeName() {
		return roomTypeName;
	}
	public void setRoomTypeName(String roomTypeName) {
		this.roomTypeName = roomTypeName;
	}
	public String getPriceTypeId() {
		return priceTypeId;
	}
	public void setPriceTypeId(String priceTypeId) {
		this.priceTypeId = priceTypeId;
	}
	public String getPriceTypeName() {
		return priceTypeName;
	}
	public void setPriceTypeName(String priceTypeName) {
		this.priceTypeName = priceTypeName;
	}
	
	public boolean getHasReferrals() {
		return hasReferrals;
	}
	public void setHasReferrals(boolean hasReferrals) {
		this.hasReferrals = hasReferrals;
	}
	public List<QuotationSaleInfoVO> getSaleInfo() {
		return saleInfo;
	}
	public void setSaleInfo(List<QuotationSaleInfoVO> saleInfo) {
		this.saleInfo = saleInfo;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	

}
