package com.mangocity.hotel.pricelowest.service;


public interface HotelHighestReturnService {

	public final static int QUERY_SIZE = 100;
	/**
	 * 初始化Task
	 */
	public void initHtlHighestReturnTask();
			
	/**
	 * 添加或修改最低价表
	 */
	public void saveOrUpdateHtlHighestReturns(int size);
	
	/**
	 * 获得最低价表,取生成js用到的string
	 */
	public String getHtlHighestReturnsForJS();
	
}
