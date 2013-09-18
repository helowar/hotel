package com.mangocity.hotel.base.service;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlGeographicalposition;

public interface GeographicalPositionService {
	long GEOGRAPHICAL_POSITION_TYPE=21;
	public void generatorPosition();
	
	/**
	 * 根据类型与城市名获取数据
	 * @param cityName
	 * @param type
	 * @return
	 */
	public List<HtlGeographicalposition>queryPositinList(String cityName,Long type); 
	 /**
	  * 根据酒店所在城市Id获取所在城市的行政区与商业区
	  * @param cityId
	  * @return
	  */
	public Map queryBusinessForCityName(String cityName);
	
	public String getGeoPostitionName(Long id);
	public HtlGeographicalposition getGeographicalposition(Long id);

	public Map findAllCity();

	public List queryPositinListByCityCode(String cityCode, Integer type);

	public Map queryBusinessForCityCode(String cityCode);

	public List findCityByCode(String cityCode);
}
