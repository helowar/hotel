package com.mangocity.hotel.order.dao;

import com.mangocity.hotel.order.persistence.HtlRoomstateCcBean;
import com.mangocity.hotel.order.persistence.HtlRoomstateCcBed;

public interface IRoomStatusControlDao {

    public int updateRoomStatuCC(final HtlRoomstateCcBean htlRoomstateCcBean,
        final HtlRoomstateCcBed htlRoomstateCcBed);

}
