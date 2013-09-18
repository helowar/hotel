package com.mangocity.hotel.order.service;

import java.sql.SQLException;
import java.util.List;

import com.mangocity.hotel.order.persistence.HtlRoomstateCc;
import com.mangocity.hotel.order.persistence.HtlRoomstateCcBean;

/**
 */
public interface IRoomStratFromCCService {

    /**
     * 保存房间房态
     * 
     * @return
     * @throws SQLException
     */
    public int saveRoomState(HtlRoomstateCcBean htlRoomstateCcBean) throws SQLException;

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
