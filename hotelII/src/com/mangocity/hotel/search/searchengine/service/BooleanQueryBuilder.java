package com.mangocity.hotel.search.searchengine.service;

import org.apache.lucene.search.BooleanQuery;

import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;

/**
 * 
 * lucene查询的query builder
 * 
 * @author chenkeming
 *
 */
public interface BooleanQueryBuilder {

	/**
	 * 
	 * 根据基本信息查询条件，构造lucene查询的Query对象
	 * 
	 * @param hotelBasicInfoSearchParam
	 * @return
	 */
	public BooleanQuery buildQuery(HotelBasicInfoSearchParam hotelBasicInfoSearchParam);

}
