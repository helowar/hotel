package com.mangocity.hotel.base.dao;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlRoomtype;

/**
 * 
 * @author xuyiwen
 *
 */
public interface HtlRoomtypeDao {
	
	/**
	 * 根据房型ID查询房型
	 * 
	 * @param roomTypeId
	 * @return
	 */
	public HtlRoomtype qryHtlRoomTypeByRoomTypeId(long roomTypeId);
	
	/**
	 * 
	 * @param hotelId
	 * @return
	 */
	public List<HtlRoomtype> lstHotelRoomType(long hotelId);
	
	/**
	 * 根据酒店ID查询该酒店下的所有房型
	 * @param hotelId 酒店ID
	 * @return
	 */
	List<HtlRoomtype> qryHtlRoomTypeByHtlId(long hotelId);
	
	/**
	 * 根据房型ID和酒店ID查询酒店的特定房型
	 * 
	 * @param id
	 * @param hotelId
	 * @return
	 */
	public HtlRoomtype qryHtlRoomTypeByIdAndHtlId(long id, long hotelId);

	/**
	 * 获取酒店列表的房型ID
	 * @param hotelIds
	 * @return
	 */
	public List<HtlRoomtype> getHtlRoomTypeListByHotelIds(String hotelIds);
	
	/**
	 * 更新房型
	 * 
	 * @param htlRoomType
	 */
	public void updateHtlRoomType(HtlRoomtype htlRoomType);
	
}
