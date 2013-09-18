package com.mangocity.hotel.search.model;


public class HotelCurrentlySatisfyQuery {
	/**
	 * 酒店id
	 */
	protected long hotelId;
	/**
	 * 满足早餐条件
	 */
	protected boolean canSatisfyBreakfast;
	/**
	 * 满足价格条件
	 */
	protected boolean canSatisfyPrice;


	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}

	public boolean isCanSatisfyBreakfast() {
		return canSatisfyBreakfast;
	}

	public void setCanSatisfyBreakfast(boolean canSatisfyBreakfast) {
		this.canSatisfyBreakfast = canSatisfyBreakfast;
	}

	public boolean isCanSatisfyPrice() {
		return canSatisfyPrice;
	}

	public void setCanSatisfyPrice(boolean canSatisfyPrice) {
		this.canSatisfyPrice = canSatisfyPrice;
	}

	
}
