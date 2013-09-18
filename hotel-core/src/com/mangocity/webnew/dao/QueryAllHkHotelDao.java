package com.mangocity.webnew.dao;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlHotelSortByArea;
import com.mangocity.hweb.persistence.HotelBookingInfoForHkSale;
import com.mangocity.hweb.persistence.QHotelInfo;


public interface QueryAllHkHotelDao  {
	
	/**
	 * 根据酒店id查询对应酒店的价格信息、房态、预订条款等、
	 * @param hotelIdStr
	 * @param ableSaleDate
	 * @return
	 */
    public List<HotelBookingInfoForHkSale> queryAllHotelInfoByHotelIdAndAbleSaleDate(String cityCode,String ableSaleDate);
    
    /**
     * 查询对应城市中商业区和行政区的指定排序数据
     * @param cityCode
     * @return
     */
    public List<HtlHotelSortByArea> queryHtlHotelSortByArea(String cityCode);
    
    /**
     * 查询青芒果的酒店信息
     * @param cityCode
     * @return
     */
	 public List<QHotelInfo> queryQMangocityHotelInfo(String cityCode, String ableSaleDate);
}
