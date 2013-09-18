package com.mangocity.hotel.quotationdisplay.vo;

import java.util.List;

/*
 * 存储未来30天中高级酒店和经济型酒店的平均价格信息，
 * 该对象会被转化为json数组返回给客户端
 */
public class AveragePricesVO {
    
	private List<HotelAveragePriceVO> advanced;//中高级酒店未来30天平均价格信息
	private List<HotelAveragePriceVO> economy;//经济型酒店未来30天平均价格信息
	private int maxAvgPrice; //中高级酒店未来30天平均价格的最大值
	public List<HotelAveragePriceVO> getAdvanced() {
		return advanced;
	}
	public void setAdvanced(List<HotelAveragePriceVO> advanced) {
		this.advanced = advanced;
	}
	public List<HotelAveragePriceVO> getEconomy() {
		return economy;
	}
	public void setEconomy(List<HotelAveragePriceVO> economy) {
		this.economy = economy;
	}
	public int getMaxAvgPrice() {
		return maxAvgPrice;
	}
	public void setMaxAvgPrice(int maxAvgPrice) {
		this.maxAvgPrice = maxAvgPrice;
	}
	
	
	
	

}
