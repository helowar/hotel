package com.mangocity.hotel.search.service;

import java.util.List;

import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.service.assistant.HotelInfo;

public interface HotelSearchService {

	/**
	 * 
	 * 根据查询条件得到HotelInfo
	 * 
	 * @param queryHotelCondition
	 * @param handler
	 */
	public void queryHotelsByHandler(QueryHotelCondition queryHotelCondition, IHotelQueryHandler handler);

	/**
	 * 查询酒店并组装为对应的VO,handler只有hotelVO,没有roomtypeVO
	 */
	public void queryOnlyHotelsByHandler(QueryHotelCondition queryHotelCondition,IHotelQueryHandler handler);
	
	/**
	 * 查询酒店全部信息并组装对应的VO
	 */
	public void queryHotelsByHandler(String hotelIdsStr,QueryHotelCondition queryHotelCondition,IHotelQueryHandler handler);
	
	/**
	 * 根据过滤条件过滤酒店(传统方式)
	 * @return
	 */
	public List<HotelInfo> queryHotelsByIO(QueryHotelCondition queryHotelCondition);
	
	/**
	 * 根据指定的酒店ID获取酒店信息
	 * @param idLst
	 * @return
	 */
	public List<HotelInfo> queryHotelsByIdLst(String idLst);
	
	/**
	 * 根据酒店ID查询酒店的详细信息
	 */
	public HotelInfo queryDetailHotelInfoById(long hotelId);
	
	/**
	 * 根据地理位置信息获取酒店列表
	 * @param mgisId 地理位置信息ID,如:世界之窗的GisId
	 * @param distance 距离,单位:公里.如:3,就代表3公里之内
	 * @return
	 */
	public List<HotelInfo> searchHotelByMgisInfo(long mgisId,int distance);
	

}
