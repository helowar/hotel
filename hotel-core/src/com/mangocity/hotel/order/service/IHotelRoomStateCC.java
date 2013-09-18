package com.mangocity.hotel.order.service;

import com.mangocity.hotel.order.persistence.HtlRoomstateCc;

/**
 */
public interface IHotelRoomStateCC {
    /**
     * 获取满房日志
     * 
     */
    public HtlRoomstateCc findRoomFulLog(Long RoomStateCCID);

    /**
     * 更新满房日志
     * 
     */
    public void updateRoomFulLog(HtlRoomstateCc htlRoomstateCc);
}
