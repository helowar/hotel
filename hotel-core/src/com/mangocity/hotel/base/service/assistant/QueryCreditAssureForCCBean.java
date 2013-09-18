package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class QueryCreditAssureForCCBean implements Serializable {
    // 酒店id
    private long hotelId;

    // 开始日期
    private Date beginDate;

    /* 结束日期 */
    private Date endDate;

    /* 房间类型 */
    private String roomType;

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

}
