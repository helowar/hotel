package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class InputCutoffDay implements Serializable {

    /**
     * id
     */
    private Long cutoffDayID;

    /**
     * 房型ID
     */
    private String roomTypeId;

    /**
     * 开始日期
     */
    private Date cutoffBeginDate;

    /**
     * 结束日期
     */
    private Date cutoffEndDate;

    /**
     * 提前天数
     */
    private Integer cutoffDay;

    /**
     * 星期
     */
    private String weeks;

    /**
     * 提前时间
     */
    private String cutoffTime;

    /**
     * 面付或共享配额数
     */
    private int quotaQty;

    public Date getCutoffBeginDate() {
        return cutoffBeginDate;
    }

    public void setCutoffBeginDate(Date cutoffBeginDate) {
        this.cutoffBeginDate = cutoffBeginDate;
    }

    public Integer getCutoffDay() {
        return cutoffDay;
    }

    public void setCutoffDay(Integer cutoffDay) {
        this.cutoffDay = cutoffDay;
    }

    public Long getCutoffDayID() {
        return cutoffDayID;
    }

    public void setCutoffDayID(Long cutoffDayID) {
        this.cutoffDayID = cutoffDayID;
    }

    public Date getCutoffEndDate() {
        return cutoffEndDate;
    }

    public void setCutoffEndDate(Date cutoffEndDate) {
        this.cutoffEndDate = cutoffEndDate;
    }

    public String getCutoffTime() {
        return cutoffTime;
    }

    public void setCutoffTime(String cutoffTime) {
        this.cutoffTime = cutoffTime;
    }

    public int getQuotaQty() {
        return quotaQty;
    }

    public void setQuotaQty(int quotaQty) {
        this.quotaQty = quotaQty;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

}
