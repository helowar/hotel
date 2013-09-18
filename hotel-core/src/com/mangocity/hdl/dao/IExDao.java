package com.mangocity.hdl.dao;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlChannelMapInfo;

public interface IExDao extends IDao {	
	
	/**
	 * 根据sql语句更新记录
	 * @param hsql
	 * @param values
	 */
	public void update(final String hsql, final Object[] values);
	
	 /**
     * 查询channel mapping 里面的酒店、房型编码信息
     * @param roomMappingType
     * @param channelType
     * @return
     */
    public List<HtlChannelMapInfo> queryHtlChannelMapInfoList(Long roomMappingType , int channelType);
	
}
