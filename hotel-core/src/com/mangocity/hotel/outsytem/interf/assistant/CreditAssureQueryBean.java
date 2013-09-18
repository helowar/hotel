package com.mangocity.hotel.outsytem.interf.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class CreditAssureQueryBean implements Serializable {

    private static final long serialVersionUID = -5298278224806173400L;

    // 酒店id
    private long hotelId;

    // 房型id
    private String roomTypeId;

    // 查询日期
    private Date queryDate;

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Date getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }
}
