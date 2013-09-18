package com.mangocity.hotel.search.service;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.search.model.HotelCurrentlyBookstate;
import com.mangocity.hotel.search.model.HotelCurrentlySatisfyQuery;
import com.mangocity.hotel.search.model.QueryDynamicCondition;
import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.service.assistant.HotelInfo;


/**
 * 包括酒店的静态信息查询(Lucene实现)和动态信息查询(含早和价格过滤)
 * @author zengyong
 *
 */
public interface HotelBasicInfoSearchService {
	/**
	 * 根据查询条件得到酒店静态信息,实际上是LuceneHotelSearchService.searchHotelBasicInfo
	 * @return
	 */
	public List<HotelInfo> searchstaticInfo(QueryHotelCondition queryHotelCondition);
	
	/**
	 * 查询动态信息,早餐或价格过滤
	 * @param queryHotelCondition
	 * @return
	 */
	public List<HotelInfo> searchDynamicInfo(QueryHotelCondition queryHotelCondition);
	
	/**
	 * 包括静动态信息查询
	 * @param queryHotelCondition
	 * @return
	 */
	public List<HotelInfo> searchHotelBasicInfo(QueryHotelCondition queryHotelCondition);
	
	/**
	 * 过滤掉不满足早餐和价格的商品信息
	 * @return
	 */
	public String filterDynamicinfoGetIdLst(QueryDynamicCondition  qcc);
	/**
	 * 过滤掉不满足早餐和价格的商品信息(用于扩展)
	 * @return
	 */
	public List<HotelCurrentlySatisfyQuery> filterDynamicinfoGetBean(QueryDynamicCondition  qcc);
}
