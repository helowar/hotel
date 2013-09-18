package com.mangocity.hotel.search.service.assistant;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于排序商品
 * @author 
 *
 */
public class PriceType {
	
	
	/**
	 * 价格类型ID
	 */
	
	private long priceTypeId;
	

	/**
	 * 是否显示
	 */
	private boolean show;
	
	/**
	 * 能否预订
	 */
	private boolean canBook;
	
	/**
	 * 最低价
	 */
	private double minPrice;
	
	private Map<String, PayMethodSort> mapPm;
	
	public long getPriceTypeId() {
		return priceTypeId;
	}
	public void setPriceTypeId(long priceTypeId) {
		this.priceTypeId = priceTypeId;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public boolean isCanBook() {
		return canBook;
	}
	public void setCanBook(boolean canBook) {
		this.canBook = canBook;
	}
	public double getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	public Map<String, PayMethodSort> getMapPm() {
		if (null == mapPm) {
			mapPm = new HashMap<String, PayMethodSort>();
		}
		return mapPm;
	}
	public void setMapPm(Map<String, PayMethodSort> mapPm) {
		this.mapPm = mapPm;
	}


}
