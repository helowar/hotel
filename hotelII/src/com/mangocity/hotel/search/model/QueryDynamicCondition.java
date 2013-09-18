package com.mangocity.hotel.search.model;

import java.util.Date;

/**
 * 商品信息查询条件
 * 
 * 
 * 
 * 
 * @author yong.zeng
 * 
 */
public class QueryDynamicCondition{
	
	
	/**
	 * 查询ID
	 */
	protected String hotelIdLst;
	/**
	 * 城市三字码
	 */
	protected String cityCode;
	/**
	 * 销售渠道
	 */
	protected String fromChannel;
	/**
	 * 开始日期
	 */
	protected Date inDate;
	/**
	 * 结束日期
	 */
	protected Date outDate;
	
	protected int roomAmount = 1;
	/**
	 * 最低价格
	 */
	protected double minPrice;
	/**
	 * 最高价格
	 */
	protected double maxPrice;
	/**
	 * 是否含早
	 */
	protected boolean containBreakfast;
	/**
	 * 支付方式
	 */
	protected String payMethod;
	/**
	 * 酒店是否优惠
	 */
	protected boolean hasHotelFavourable;
	/**
	 * 商家是否优惠
	 */
	protected boolean hasSellerFavourable;
	
	/**
	 * 是否含免费宽带
	 */
	protected boolean hasFreeNet; 
	
	public String getHotelIdLst() {
		return hotelIdLst;
	}
	public void setHotelIdLst(String hotelIdLst) {
		this.hotelIdLst = hotelIdLst;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getFromChannel() {
		return fromChannel;
	}
	public void setFromChannel(String fromChannel) {
		this.fromChannel = fromChannel;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public double getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	public double getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}
	public boolean isContainBreakfast() {
		return containBreakfast;
	}
	public void setContainBreakfast(boolean containBreakfast) {
		this.containBreakfast = containBreakfast;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public boolean isHasHotelFavourable() {
		return hasHotelFavourable;
	}
	public void setHasHotelFavourable(boolean hasHotelFavourable) {
		this.hasHotelFavourable = hasHotelFavourable;
	}
	public boolean isHasSellerFavourable() {
		return hasSellerFavourable;
	}
	public void setHasSellerFavourable(boolean hasSellerFavourable) {
		this.hasSellerFavourable = hasSellerFavourable;
	}
	public int getRoomAmount() {
		return roomAmount;
	}
	public void setRoomAmount(int roomAmount) {
		if(roomAmount<=1){
			this.roomAmount=1;
			return;
		}
		this.roomAmount = roomAmount;
		
	}
	public boolean isHasFreeNet() {
		return hasFreeNet;
	}
	public void setHasFreeNet(boolean hasFreeNet) {
		this.hasFreeNet = hasFreeNet;
	}
	
	
	
}
