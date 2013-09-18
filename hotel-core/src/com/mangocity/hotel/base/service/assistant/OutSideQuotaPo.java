package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class OutSideQuotaPo implements Serializable {

    private static final long serialVersionUID = -8811885040885337946L;

    // 呼出配额日期
    private Date date;

    // 呼出配额房型
    private long roomTypeId;

    // 呼出配额数
    private int quotaNum;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public int getQuotaNum() {
        return quotaNum;
    }

    public void setQuotaNum(int quotaNum) {
        this.quotaNum = quotaNum;
    }
}
