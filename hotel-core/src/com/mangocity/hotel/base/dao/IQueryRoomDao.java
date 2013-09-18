package com.mangocity.hotel.base.dao;


import com.mangocity.hotel.base.persistence.HtlRoom;


/**
 * 用Ibatis来查询房间信息
 * @author zuoshengwei
 *
 */
public interface IQueryRoomDao {
	
	/**
	 * 通过业务主键找到一个房间
	 * @param roomTypeId
	 * @param hotelId
	 * @param ableSaleDate
	 * @return
	 */
    public HtlRoom findRoomByBizKey(Long roomTypeId, Long hotelId, String ableSaleDate);
     
}
