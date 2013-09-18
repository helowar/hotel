package com.mangocity.hagtb2b.persistence;

import java.util.Date;

/**
 * HtlB2bDaoImpl queryIncreasePrice()返回参数的封装
 * @author xuyiwen
 *
 */
public class HtlB2bIncrease {
	
	private long hotelId;
	
	private long roomTypeId;
	
	private long priceTypeId;
	
	/**
	 * 底价
	 */
	private double basePrice;
	
	/**
	 * 佣金
	 */
	private double commission;
	
	/**
	 * 可售日期
	 */
	private Date ableSaleDate;
	
	/**
	 * 经过加幅计算后的价格
	 */
	private double increasePrice;
	
	private String currency;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}

	public long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public long getPriceTypeId() {
		return priceTypeId;
	}

	public void setPriceTypeId(long priceTypeId) {
		this.priceTypeId = priceTypeId;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public Date getAbleSaleDate() {
		return ableSaleDate;
	}

	public void setAbleSaleDate(Date ableSaleDate) {
		this.ableSaleDate = ableSaleDate;
	}

	public double getIncreasePrice() {
		return increasePrice;
	}

	public void setIncreasePrice(double increasePrice) {
		this.increasePrice = increasePrice;
	}
	
	
}
