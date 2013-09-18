package com.mangocity.hotel.base.dao.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mangocity.hotel.base.dao.GeoDistanceDao;
import com.mangocity.hotel.base.persistence.HtlGeoDistance;
import com.mangocity.hotel.constant.HotelDistance;
import com.mangocity.util.dao.DAOHibernateImpl;

public class GeoDistanceDaoImpl extends DAOHibernateImpl implements GeoDistanceDao{

	private static final long serialVersionUID = -2860314112796716599L;

	public List getGeoList(Long hotelId, Integer[] types,String cityCode ) {
		String hql="from HtlGeographicalposition where  cityCode=?  and gptypeId in (21,23,24,26,27,28,29,30,31,32,33) ";
		//,arrayToString(types)
		return super.query(hql, new Object[]{cityCode});
	}
	
	public List<HtlGeoDistance> queryGeoDistanceByHotelId(long hotelId) {
		String hql="from HtlGeoDistance where hotelId=? and distance<?";
		return super.query(hql,new Object[]{hotelId,HotelDistance.CONSTANTS_SET_DISTANCE});
	}
	
	public List<HtlGeoDistance> queryGeoDistanceByGeoId(long geoId) {
		String hql="from HtlGeoDistance where geoId=? and distance<?";
		return super.query(hql,new Object[]{geoId,HotelDistance.CONSTANTS_SET_DISTANCE});
	}
	
	private static String arrayToString(Integer[] types){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<types.length;i++){
			sb.append(types[i]);
			if(i<types.length){
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public void save(HtlGeoDistance constructGeoInfo) {
		super.saveOrUpdate(constructGeoInfo);
	}

	public Collection<HtlGeoDistance> queryGeoDistance() {
		String hql="from HtlGeoDistance";
		return super.query(hql);
	}

	public List<HtlGeoDistance> getDistanceList(String cityCode, Long type) {
		String hql="select distinct new HtlGeoDistance(geoId,geoType,cityCode,name) from HtlGeoDistance where cityCode=? and geoType=?";
		return super.query(hql,new Object[]{cityCode,type});
	}

	public List<HtlGeoDistance> queryGeoDistanceList(Long hotelId, Long geoId) {
		String hql="from HtlGeoDistance where hotelId=? and geoId=?";
		return super.query(hql,new Object[]{hotelId,geoId});
	}

	public List queryCityList() {
		String sql="select b.id, b.name  ,b.title  from cmd.t_cdm_basedata b where b.levels=5 and b.treepath like '/root/comment/area/CN%'";
		return super.doquerySQL(sql, false);
	}
	public void deleteGeoDistanceByHotelId(Long hotelId){
		String hsql="delete  from  HtlGeoDistance where hotelId=?";
		super.remove(hsql, new Object[]{hotelId});
	}

	public void deleteGeoDistanceByGeoId(Long geoId) {
		String hsql="delete  from  HtlGeoDistance where geoId=?";
		super.remove(hsql, new Object[]{geoId});
		
	}

	public void updateByGeoIdAndHotelId(Long hotelId, Long geoId,
			double distance) {
		String hql="update from  HtlGeoDistance set distance=? where hotelId=? and geoId=?";
		//super.doSqlUpdate(sql)
		
		super.doUpdateBatch(hql, new Object[]{distance,hotelId,geoId});
		
	}

	
}
