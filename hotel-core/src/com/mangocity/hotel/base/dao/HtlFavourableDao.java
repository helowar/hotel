package com.mangocity.hotel.base.dao;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlFavourable;

public interface HtlFavourableDao {

	/**
	 * 根据城市和活动类型查询
	 */
	public List<HtlFavourable> queryHtlFavourableByType(String cityCode,int promoteType);
	
	/**
	 * 根据酒店Id查询
	 */
	public List<HtlFavourable> queryHtlFavourableByHotelId(String hotelIdList);
}
