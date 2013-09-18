package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;

public class EverydayParams implements Serializable{
	
	private String hSalePrice;
	private String hRoomStatus="大:良:150";
	private String hBreakfast="自 : 不含 ";
	private String hBreakfasts="3";
	private String hBreakNum="0";	
	private String hBasePrice="59.0";	
	private String hQuantity=null;
	private String hMarketPrice="150";
	private String maxQty	=null;
	private String  ableSaleDate;
	private int quotaNum;
	public String gethSalePrice() {
		return hSalePrice;
	}
	public void sethSalePrice(String hSalePrice) {
		this.hSalePrice = hSalePrice;
	}
	public String gethRoomStatus() {
		return hRoomStatus;
	}
	public void sethRoomStatus(String hRoomStatus) {
		this.hRoomStatus = hRoomStatus;
	}
	public String gethBreakfast() {
		return hBreakfast;
	}
	public void sethBreakfast(String hBreakfast) {
		this.hBreakfast = hBreakfast;
	}
	public String gethBreakfasts() {
		return hBreakfasts;
	}
	public void sethBreakfasts(String hBreakfasts) {
		this.hBreakfasts = hBreakfasts;
	}
	public String gethBreakNum() {
		return hBreakNum;
	}
	public void sethBreakNum(String hBreakNum) {
		this.hBreakNum = hBreakNum;
	}
	public String gethBasePrice() {
		return hBasePrice;
	}
	public void sethBasePrice(String hBasePrice) {
		this.hBasePrice = hBasePrice;
	}
	public String gethQuantity() {
		return hQuantity;
	}
	public void sethQuantity(String hQuantity) {
		this.hQuantity = hQuantity;
	}
	public String gethMarketPrice() {
		return hMarketPrice;
	}
	public void sethMarketPrice(String hMarketPrice) {
		this.hMarketPrice = hMarketPrice;
	}
	public String getMaxQty() {
		return maxQty;
	}
	public void setMaxQty(String maxQty) {
		this.maxQty = maxQty;
	}
	public String getAbleSaleDate() {
		return ableSaleDate;
	}
	public void setAbleSaleDate(String ableSaleDate) {
		this.ableSaleDate = ableSaleDate;
	}
	public int getQuotaNum() {
		return quotaNum;
	}
	public void setQuotaNum(int quotaNum) {
		this.quotaNum = quotaNum;
	}
}
