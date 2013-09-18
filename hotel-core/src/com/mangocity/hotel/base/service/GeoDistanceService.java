package com.mangocity.hotel.base.service;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlGeoDistance;

public interface GeoDistanceService {
	public void buildGeoInfo() throws InterruptedException;
	public List<HtlGeoDistance> getDistanceList(String cityCode,Long type);
	/**
	 * 重新计算酒店与地理位置的距离
	 * @param hotelId
	 */
	public void generateGoeInfoByHotel(Long hotelId);
	/**
	 * 重新计算该地理与所有酒店的距离
	 * @param geoId
	 */
	public void generateGoeInfoByGeoId(Long geoId);
}
