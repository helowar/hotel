package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlRoom;

/**
*@author chenhuizhong e-mail:huizong.chen@mangocity.com
*@version 
*@since 
*/
public interface HtlRoomDao {

    /**
     * 查询订单中roomTypeId的房型记录 add by chenhuizhong
     * @param roomTypeId
     * @param checkinDate
     * @param checkoutDate
     * @return List
     */
    public abstract List<HtlRoom> getHtlRoomByRoomTypeId(Long roomTypeId,
            Date checkinDate, Date checkoutDate);
    
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