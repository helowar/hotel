package com.mangocity.hotel.search.service;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlGeoDistance;
import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;

public interface HotelSearcher {
	
	/**
	 * 查询酒店基本信息
	 * 
	 * @param hotelBasicInfoSearchParam	查询参数对象
	 * @return	酒店信息的Map，其中key为酒店ID，value为HotelBasicInfo对象
	 */
	public Map<String, HotelBasicInfo> searchHotelBasicInfo(HotelBasicInfoSearchParam hotelBasicInfoSearchParam);
	
	/**
	 * 查询经纬度信息
	 * @param geo
	 * @return
	 */
	public List<HtlGeographicalposition> searchHotelGeoInfo(HtlGeographicalposition geo);

}
