package com.mangocity.hotel.base.service;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlRoom;

public interface HotelRoomService {

	/**
     * 根据房型, 可售日期范围查询房间
     * 
     * @param roomTypeID
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<HtlRoom> qryHtlRoomByRoomTypeSaleDateRange(long roomTypeID, Date beginDate, Date endDate);
    
    /**
     * 根据房型，酒店ID，可售日期获取酒店客房 
     * 
     * @param roomTypeId
     * @param hotelId
     * @param ableSaleDate
     * @return HtlRoom
     */
    public HtlRoom getRoomByRoomTypeHotelIdSaleDate(long roomTypeId, long hotelId, Date ableSaleDate);
    
    /**
     * 根据房型获得某个酒店在某个可售日期范围内的客房
     * 
     * @param roomType
     * @param hotelId
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<HtlRoom> getHtlRoomByHotelIdRoomType(long roomType, long hotelId, Date beginDate, Date endDate);
    
    /**
     * 根据room id获得房间信息
     * 
     * @param roomId
     * @return
     */
    public HtlRoom getHtlRoomByRoomId(long roomId);
        
    /**
     * 更新HtlRoom对象
     * 
     * @param htlRoom
     */
    public void updateHtlRoom(HtlRoom htlRoom);
	
    public List<HtlRoom> getHtlRooms(Long hotelId, Long roomTypeId, Date checkinDate, Date checkoutDate);
}
