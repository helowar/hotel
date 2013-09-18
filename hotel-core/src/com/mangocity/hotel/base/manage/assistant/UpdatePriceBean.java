package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class UpdatePriceBean implements Serializable {

    private long childRoomID;

    private long roomTypeID;

    private String causeSign;

    private Date beginDate;

    private Date endDate;

    private Integer[] weeks;

    public long getChildRoomID() {
        return childRoomID;
    }

    public void setChildRoomID(long childRoomID) {
        this.childRoomID = childRoomID;
    }

    public long getRoomTypeID() {
        return roomTypeID;
    }

    public void setRoomTypeID(long roomTypeID) {
        this.roomTypeID = roomTypeID;
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

    public Integer[] getWeeks() {
        return weeks;
    }

    public void setWeeks(Integer[] weeks) {
        this.weeks = weeks;
    }

    public String getCauseSign() {
        return causeSign;
    }

    public void setCauseSign(String causeSign) {
        this.causeSign = causeSign;
    }

}
