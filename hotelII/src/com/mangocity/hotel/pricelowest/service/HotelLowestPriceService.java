package com.mangocity.hotel.pricelowest.service;

import java.util.List;

import com.mangocity.hotel.pricelowest.persistence.HtlLowestTask;

public interface HotelLowestPriceService {

	public final static int QUERY_SIZE = 100;
	/**
	 * 初始化Task
	 */
	public void initHtlLowestTask();
			
	/**
	 * 添加或修改最低价表
	 */
	public void saveOrUpdateHtlLowestPrices(int size);
	
	/**
	 * 获得最低价表,取生成js用到的string
	 */
	public String getHtlLowestPricesForJS();
	
}
