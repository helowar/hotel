package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class CloseRoomReasonBean implements Serializable {

    /**
     * 销售日期
     */
    private Date saleDate;

    /**
     * 房型（价格类型）
     */
    private String roomTypeAndPriceType;

    /**
     * 关房原因
     */
    private String closeRoomReason;

    public String getCloseRoomReason() {
        return closeRoomReason;
    }

    public void setCloseRoomReason(String closeRoomReason) {
        this.closeRoomReason = closeRoomReason;
    }

    public String getRoomTypeAndPriceType() {
        return roomTypeAndPriceType;
    }

    public void setRoomTypeAndPriceType(String roomTypeAndPriceType) {
        this.roomTypeAndPriceType = roomTypeAndPriceType;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }
}
