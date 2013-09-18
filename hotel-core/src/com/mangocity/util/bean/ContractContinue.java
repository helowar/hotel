package com.mangocity.util.bean;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class ContractContinue implements Serializable {

    // 时间段复制-房型价格类型ID
    private String periodIds;

    // 时间段复制-源开始日期
    private Date srcBeginDate;

    // 时间段复制-源结束日期
    private Date srcEndDate;

    // 时间段复制-源天数
    private int srcDays;

    // 时间段复制-目标开始日期
    private Date tarBeginDate;

    // 时间段复制-目标结束日期
    private Date tarEndDate;

    // 时间段复制-目标天数
    private int tarDays;

    private long roomTypeId;

    private long childRoomTypeId;

    /**
     * 目标房型价格类型ID v2.8.1价格复制功能 add by wuyun 2009-05-20
     */
    private String tarPeriodIds;

    /**
     * 目标房型ID v2.8.1价格复制功能 add by wuyun 2009-05-20
     */
    private long tarRoomTypeId;

    /**
     * 目标价格类型ID v2.8.1价格复制功能 add by wuyun 2009-05-20
     */
    private long tarChildRoomTypeId;
    
    /**
     * 按时段复制增加星期 add by chenjiajie 2010-02-01
     */
    private String weeks;

    public long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(long childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Date getSrcBeginDate() {
        return srcBeginDate;
    }

    public void setSrcBeginDate(Date srcBeginDate) {
        this.srcBeginDate = srcBeginDate;
    }

    public int getSrcDays() {
        return srcDays;
    }

    public void setSrcDays(int srcDays) {
        this.srcDays = srcDays;
    }

    public Date getSrcEndDate() {
        return srcEndDate;
    }

    public void setSrcEndDate(Date srcEndDate) {
        this.srcEndDate = srcEndDate;
    }

    public Date getTarBeginDate() {
        return tarBeginDate;
    }

    public void setTarBeginDate(Date tarBeginDate) {
        this.tarBeginDate = tarBeginDate;
    }

    public int getTarDays() {
        return tarDays;
    }

    public void setTarDays(int tarDays) {
        this.tarDays = tarDays;
    }

    public Date getTarEndDate() {
        return tarEndDate;
    }

    public void setTarEndDate(Date tarEndDate) {
        this.tarEndDate = tarEndDate;
    }

    public String getPeriodIds() {
        return periodIds;
    }

    public void setPeriodIds(String periodIds) {
        this.periodIds = periodIds;
        String[] kk = periodIds.split("&&");
        if (null != kk && 1 < kk.length) {
            this.roomTypeId = Long.valueOf(kk[0]).longValue();
            this.childRoomTypeId = Long.valueOf(kk[1]).longValue();
        }
    }

    public String getTarPeriodIds() {
        return tarPeriodIds;
    }

    public void setTarPeriodIds(String tarPeriodIds) {
        this.tarPeriodIds = tarPeriodIds;
        String[] kk = tarPeriodIds.split("&&");
        if (null != kk && 1 < kk.length) {
            this.tarRoomTypeId = Long.valueOf(kk[0]).longValue();
            this.tarChildRoomTypeId = Long.valueOf(kk[1]).longValue();
        }
    }

    public long getTarRoomTypeId() {
        return tarRoomTypeId;
    }

    public void setTarRoomTypeId(long tarRoomTypeId) {
        this.tarRoomTypeId = tarRoomTypeId;
    }

    public long getTarChildRoomTypeId() {
        return tarChildRoomTypeId;
    }

    public void setTarChildRoomTypeId(long tarChildRoomTypeId) {
        this.tarChildRoomTypeId = tarChildRoomTypeId;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

}
