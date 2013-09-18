package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 变价工单
 * 
 * @author xiaowumi
 * 
 */
public class HtlChangePrice implements Entity {

    private Long ID;

    /**
     * 酒店id
     */
    private long hotelId;

    /**
     * 变价工单CD
     */
    private String taskCode;

    /**
     * 状态
     */
    private String status;

    /**
     * 是否紧急变价
     */
    private boolean urgency;

    /**
     * 工单日期
     */
    private Date changeDate;

    /**
     * 创建人ID
     */
    private String createByUserId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     * 
     */
    private Date createTime;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getCreateByUserId() {
        return createByUserId;
    }

    public void setCreateByUserId(String createByUserId) {
        this.createByUserId = createByUserId;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isUrgency() {
        return urgency;
    }

    public void setUrgency(boolean urgency) {
        this.urgency = urgency;
    }

}
