package com.mangocity.hdl.dao.impl;

import java.util.Collections;
import java.util.List;

import com.mangocity.hdl.dao.IExDao;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.hotel.base.persistence.HtlChannelMapInfo;

public class ExDaoImpl extends DAOHibernateImpl implements IExDao {

	private static final long serialVersionUID = -5707671272542616070L;

	/**
	 * 根据sql语句更新记录
	 * @param hsql
	 * @param values
	 */
	public void update(final String hsql, final Object[] values) {
		super.remove(hsql, values);
	}
	
	
    public List<HtlChannelMapInfo> queryHtlChannelMapInfoList(Long roomMappingType , int channelType){
    	String hsql = "FROM HtlChannelMapInfo aa WHERE aa.status='A' "
    		+" AND aa.channelType = ? AND aa.type = ?  "
    		+" AND exists (select 1 FROM HtlChannelMapInfo bb WHERE bb.type = 1 AND bb.status='A' AND bb.channelType = ? and bb.mapHotelId = aa.mapHotelId)";
    	List<HtlChannelMapInfo> queryHtlChannelMapInfoList = super.query(hsql, new Object[]{String.valueOf(channelType),String.valueOf(roomMappingType),String.valueOf(channelType)});
    	return  null != queryHtlChannelMapInfoList?queryHtlChannelMapInfoList:Collections.EMPTY_LIST;
    }

}
