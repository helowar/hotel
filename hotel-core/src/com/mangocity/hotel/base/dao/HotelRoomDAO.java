package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlRoom;

/**
*@author chenhuizhong e-mail:huizong.chen@mangocity.com
*@version 1.0
*@since 1.0
*/
public interface HotelRoomDAO
{
	/**
     * 根据room id查询房间
     * 
     * @param roomId
     * @return
     */
    public HtlRoom qryHtlRoomByRoomId(long roomId);

    /**
     * 查找一个房间，如果不存在就新建一个房间
     * @param roomTypeId
     * @param hotelId
     * @param ableSaleDate
     * @param contractId
     * @return HtlRoom
     */
    public HtlRoom createRoomIfNotExist(Long roomTypeId, Long hotelId,
            Date ableSaleDate, Long contractId);

    /**
     * 根据房型，酒店ID，可售日期查询酒店客房 
     * 
     * @param roomTypeId
     * @param hotelId
     * @param ableSaleDate
     * @return HtlRoom
     */
    public HtlRoom qryRoomByRoomTypeHotelIdSaleDate(long roomTypeId, long hotelId, Date ableSaleDate);

    /**
     * 通过房型id,酒店id,起止日期 找出合符条件的房间
     * @param hotelId
     * @param roomTypeId
     * @param beginDate
     * @param endDate
     * @return List<HtlRoom>
     */
    public List<HtlRoom> lstRooms(Long hotelId, Long roomTypeId, String beginDate, String endDate);

    /**
     * 通过酒店id,开始日期，结束日期,选择的星期，选择的房型 找出房间列表
     * 
     * @param hotelId
     * @param beginDate
     * @param endDate
     * @param week
     * @param roomTypes
     * @return List
     */
    public List<HtlRoom> qryRoomState(long hotelId, Date beginDate,
            Date endDate, String[] weeks, String[] roomTypes);
    
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
     * 更新HtlRoom对象
     * 
     * @param htlRoom
     */
    public void updateHtlRoom(HtlRoom htlRoom);
    
    /**
     * 根据房型查询某个酒店在某个可售日期范围内的客房
     * 
     * @param roomType
     * @param hotelId
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<HtlRoom> qryHtlRoomByHotelIdRoomType(long roomType, long hotelId, Date beginDate, Date endDate);
    
	   /**
     * 根据就酒店Id和房型Id查出所有的房型，判断房态。add by ting.li
     * @param hotelId
     * @param roomTypeId
     * @param checkinDate
     * @param checkoutDate
     * @return
     */
    public List<HtlRoom>  getHtlRooms(Long hotelId,Long roomTypeId, Date checkinDate, Date checkoutDate);
}