package com.mangocity.util.bean;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class SingleContractContinue implements Serializable {

    // 单天复制-房型价格类型ID
    private String singleIds;

    // 单天复制-源开始日期
    private Date sourceBeginDate;

    // 单天复制-目标开始日期
    private Date targetBeginDate;

    // 单天复制-目标结束日期
    private Date targetEndDate;

    // 单天复制-目标天数
    private int targetDays;

    private long roomTypeId;

    private long childRoomTypeId;

    /**
     * 目标房型价格类型ID v2.8.1价格复制功能 add by wuyun 2009-05-20
     */
    private String targetSingleIds;

    /**
     * 目标房型ID v2.8.1价格复制功能 add by wuyun 2009-05-20
     */
    private long targetRoomTypeId;

    /**
     * 目标价格类型ID v2.8.1价格复制功能 add by wuyun 2009-05-20
     */
    private long targetChildRoomTypeId;

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

    public String getSingleIds() {
        return singleIds;
    }

    public void setSingleIds(String singleIds) {
        this.singleIds = singleIds;
        String[] kk = singleIds.split("&&");
        if (null != kk && 1 < kk.length) {
            this.roomTypeId = Long.valueOf(kk[0]).longValue();
            this.childRoomTypeId = Long.valueOf(kk[1]).longValue();
        }
    }

    public Date getSourceBeginDate() {
        return sourceBeginDate;
    }

    public void setSourceBeginDate(Date sourceBeginDate) {
        this.sourceBeginDate = sourceBeginDate;
    }

    public Date getTargetBeginDate() {
        return targetBeginDate;
    }

    public void setTargetBeginDate(Date targetBeginDate) {
        this.targetBeginDate = targetBeginDate;
    }

    public int getTargetDays() {
        return targetDays;
    }

    public void setTargetDays(int targetDays) {
        this.targetDays = targetDays;
    }

    public Date getTargetEndDate() {
        return targetEndDate;
    }

    public void setTargetEndDate(Date targetEndDate) {
        this.targetEndDate = targetEndDate;
    }

    public String getTargetSingleIds() {
        return targetSingleIds;
    }

    public void setTargetSingleIds(String targetSingleIds) {
        this.targetSingleIds = targetSingleIds;
        String[] kk = targetSingleIds.split("&&");
        if (null != kk && 1 < kk.length) {
            this.targetRoomTypeId = Long.valueOf(kk[0]).longValue();
            this.targetChildRoomTypeId = Long.valueOf(kk[1]).longValue();
        }
    }

    public long getTargetRoomTypeId() {
        return targetRoomTypeId;
    }

    public void setTargetRoomTypeId(long targetRoomTypeId) {
        this.targetRoomTypeId = targetRoomTypeId;
    }

    public long getTargetChildRoomTypeId() {
        return targetChildRoomTypeId;
    }

    public void setTargetChildRoomTypeId(long targetChildRoomTypeId) {
        this.targetChildRoomTypeId = targetChildRoomTypeId;
    }
}
