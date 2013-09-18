package com.mangocity.hotel.base.dao;

import java.util.Collection;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlGeoDistance;

public interface GeoDistanceDao {
	public List getGeoList(Long hotelId,Integer[]types,String cityCode);

	public void save(HtlGeoDistance constructGeoInfo)throws Exception;

	public List<HtlGeoDistance> queryGeoDistanceByHotelId(long hotelId);
	
	public List<HtlGeoDistance> queryGeoDistanceByGeoId(long geoId);

	public Collection<HtlGeoDistance> queryGeoDistance();

	public List<HtlGeoDistance> getDistanceList(String cityCode, Long type );

	public List<HtlGeoDistance> queryGeoDistanceList(Long hotelId, Long geoId);
	
	public List queryCityList();
	
	public void deleteGeoDistanceByHotelId(Long hotelId);

	public void deleteGeoDistanceByGeoId(Long geoId);

	public void updateByGeoIdAndHotelId(Long hotelId, Long geoId, double distance);
}
