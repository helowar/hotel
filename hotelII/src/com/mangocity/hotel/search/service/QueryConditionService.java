package com.mangocity.hotel.search.service;

import java.util.Map;

import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;
import com.mangocity.hotel.search.model.QueryCommodityCondition;
import com.mangocity.hotel.search.model.QueryDynamicCondition;
import com.mangocity.hotel.search.model.QueryHotelCondition;


/**
 * 查询条件Service
 * 用于酒店的数据查询,同时给商旅提供服务
 * 
 * @author zengyong
 *
 */
public interface QueryConditionService {

	
	/**
	 * 组装静态查询条件(用于过滤静态信息)
	 * @param queryHotelCondition
	 * @return
	 */
	public HotelBasicInfoSearchParam fitQueryStaticCondition(QueryHotelCondition queryHotelCondition);
	/**
	 * 组装动态查询条件(用于过滤动态信息) 
	 * @param hotelBasicInfoSearchParam
	 * @param hotelBasicInfos
	 * @return
	 * @author 
	 */
	public QueryDynamicCondition fitQueryDynamicCondition(HotelBasicInfoSearchParam hotelBasicInfoSearchParam,Map<String, HotelBasicInfo> hotelBasicInfos);
	
	
	/**
	 * 组装商品查询条件(用于查询商品信息) 
	 * @param hotelBasicInfoSearchParam
	 * @param hotelBasicInfos
	 * @return
	 * @author 
	 */
	public QueryCommodityCondition fitQueryCommodityCondition(HotelBasicInfoSearchParam hotelBasicInfoSearchParam,Map<String, HotelBasicInfo> hotelBasicInfos);
	
	/**
	 * 组装hotelIdLst,将查询过滤结果转化成hotelIdLst
	 * @param hotelBasicInfos
	 * @return
	 * @author 
	 */
	public String fitHotelLstFromHotelInfos(Map<String, HotelBasicInfo> hotelBasicInfos);
	
}
