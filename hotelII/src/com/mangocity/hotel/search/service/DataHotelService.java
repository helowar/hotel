package com.mangocity.hotel.search.service;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlGeoDistance;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;
import com.mangocity.hotel.search.model.QueryCommodityInfo;
import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.service.assistant.HotelInfo;


/**
 * 数据操作Service
 * 用于酒店的数据查询,同时给商旅提供服务
 * 
 * @author zengyong
 *
 */
public interface DataHotelService {

	
public Map<String, HotelBasicInfo> searchHotelBasicInfo(QueryHotelCondition queryHotelCondition);
	



	
	
	public List<QueryCommodityInfo> geDynamicInfo(QueryHotelCondition queryHotelCondition);
	
}
