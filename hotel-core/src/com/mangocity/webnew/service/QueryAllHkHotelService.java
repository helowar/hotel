package com.mangocity.webnew.service;

import java.util.List;
import java.util.Map;

import com.mangocity.hweb.persistence.HotelBookingResultInfoForHkSale;
import com.mangocity.hweb.persistence.QHotelInfo;


public interface QueryAllHkHotelService  {
	
	/**
	 * 根据酒店id和查询日期，查询当天最低价和酒店总体是否可预订状态
	 * @param hotelId
	 * @param ableSaleDate
	 * @return
	 */
	public List<HotelBookingResultInfoForHkSale> queryHkHotelInfo(String cityCode,String ableSaleDate)throws Exception;
	
	/**
	 * 根据酒店id和查询日期，查询青芒果的酒店预订情况
	 * @param cityCode
	 * @param ableSaleDate
	 * @return
	 */
	public Map<String, List<QHotelInfo>> queryQHotelInfo(String cityCode, String ableSaleDate); 
	
    
}
