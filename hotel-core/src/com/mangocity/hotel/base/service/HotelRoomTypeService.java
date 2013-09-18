package com.mangocity.hotel.base.service;

import java.util.List;

import com.mangocity.hotel.base.manage.assistant.BedTypeBean;
import com.mangocity.hotel.base.persistence.HtlRoomtype;

public interface HotelRoomTypeService {
	
	/**
	 * 更新房型
	 * 
	 * @param htlRoomtype
	 */
	public void updateHtlRoomType(HtlRoomtype htlRoomtype);
	
	/**
	 * 根据房型ID得到房型
	 * 
	 * @param roomTypeId
	 * @return
	 */
	public HtlRoomtype getHtlRoomTypeByRoomTypeId(long roomTypeId);
	
	/**
	 * 根据酒店ID得到该酒店的所有房型
	 * 
	 * @param hotelId
	 * @return
	 */
	public List<HtlRoomtype> getHtlRoomTypeListByHotelId(long hotelId);
	
	/**
	 * 根据房型ID得到床型
	 * 
	 * @param roomTypeId
	 * @return
	 */
	public List<BedTypeBean> queryBedsByRoomId(Long roomTypeId);
	
	/**
	 * 根据房型ID和酒店ID获取酒店的特定房型
	 * 
	 * @param hotelId
	 * @param roomTypeId
	 * @return
	 */
	public HtlRoomtype getHtlRoomTypeByIdAndHtlId(long hotelId, long roomTypeId);

	/**
	 * 获取酒店列表的房型ID
	 * @param hotelIds
	 * @return
	 */
	public List<HtlRoomtype> getHtlRoomTypeListByHotelIds(String hotelIds);

}
