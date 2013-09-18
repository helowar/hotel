package com.mangocity.hdl.service;

import java.util.List;

import com.mangocity.hotel.base.persistence.ExMapping;

public interface IExMappingService {	
	
	/**
	 * 根据芒果编码信息获取直连价格计划编码信息
	 * @param channelType
	 * @param hotelId
	 * @param roomTypeId
	 * @param childRoomTypeId
	 * @return
	 */
	public ExMapping getMapping(long channelType, long hotelId, long roomTypeId, 
			long childRoomTypeId);
	
	/**
	 * 根据芒果酒店id列表获取直连编码信息
	 * @param channelType
	 * @param hotelIds
	 * @return
	 */
	public List getMapping(long channelType, List<Long> hotelIds);
	
	/**
	 * 根据芒果编码信息获取直连房型编码信息
	 * @param channelType
	 * @param hotelId
	 * @param roomTypeId
	 * @return
	 * @author chenjiajie
	 */
	public ExMapping getMapping(long channelType, long hotelId, long roomTypeId);
	
	/**
	 * 根据芒果编码信息获取直连房型编码的价格计划信息
	 * @param channelType
	 * @param hotelId
	 * @param roomTypeId
	 * @return
	 * @author chenjiajie
	 */
	public List getPriceMapping(long channelType, long hotelId, long roomTypeId);
	
	/**
	 * 查询合作方的所有可用,处于激活状态的价格计划
	 * @param channelType
	 * @return
	 * @author guojun
	 */
	public List getAllRoomTypeMapping(long channelType);
	
	/**
	 * 根据合作方的编码类型读取合作方的映射信息
	 * @param channelType
	 * @param hotelCodeChannel
	 * @param roomCodeChannel
	 * @return
	 */
	public List getColPriceMapping(long channelType, String hotelCodeChannel,
			String roomCodeChannel);
	
	/**
	 * 根据type查询所有属于type类型的Mapping
	 * @author:shizhongwen
	 * @date:Mar 8, 2010  11:16:44 AM
	 * @param type
	 * @return
	 */
	public List getAllPriceMapping(long type);
	
	  /**
     * 根据渠道，酒店ID，房型ID， 查询相应的Exampping 
     * @param channelType
     * @param hotelid
     * @param roomtypecode
     * @param type
     * @return
     */
    public List<ExMapping> getHotelRoomMapping(long channelType,long hotelid,String roomtypeid,long type) ;
	
}
