package com.mangocity.hotel.dreamweb.search.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.ExMapping;

public interface HotelBookDao {

	/**
	 * 根据priceTypeId得到HotelId
	 */
	public Long getHotelId(Long priceTypeId);
	
	/**
	 * 根据priceTypeId得到roomTypeId
	 */
	public Long getRoomTypeId(Long priceTypeId);
	
	/**
	 * 查询渠道号 对应ex_mapping表
	 */
	public ExMapping getExMapping(Long priceTypeId);
	
	/**
	 * 获取商品信息
	 */
    public List<Object[]> queryCommidity(Long priceTypeId, String payMehtod,
			Date inDate, Date outDate, boolean forCts);
    
    /**
	 * 查询直连酒店是否激活
	 */
	public boolean getIsActive(Long priceTypeId,int roomChannel);
}
