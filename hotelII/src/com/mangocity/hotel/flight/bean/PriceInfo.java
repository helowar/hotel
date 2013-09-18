package com.mangocity.hotel.flight.bean;

public class PriceInfo implements java.io.Serializable{
	private static final long serialVersionUID = 4175271798639570185L;
	private String hotelId;
	private String roomId;
	private String ratePlanId;
	private String saleDate;
	private String salePrice;
	private String currency;
	private String breakfastType;
	private String breakfastNum;
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getRatePlanId() {
		return ratePlanId;
	}
	public void setRatePlanId(String ratePlanId) {
		this.ratePlanId = ratePlanId;
	}
	public String getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getBreakfastType() {
		return breakfastType;
	}
	public void setBreakfastType(String breakfastType) {
		this.breakfastType = breakfastType;
	}
	public String getBreakfastNum() {
		return breakfastNum;
	}
	public void setBreakfastNum(String breakfastNum) {
		this.breakfastNum = breakfastNum;
	}
	
}
