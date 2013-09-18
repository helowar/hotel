package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 变价工单日志
 * 
 * @author xiaowumi
 * 
 */
public class HtlChangePriceLog implements Entity {

    private Long ID;

    /**
     * 酒店id
     */
    private long hotelId;

    /**
     * 变价工单CD
     */
    private String taskCode;;

    /**
     * 操作类型
     */
    private String operateState;

    /**
     * 是否紧急变价
     */
    private boolean urgency;

    /**
     * 操作人ID
     */
    private String operaterId;

    /**
     * 工单日期
     */
    private Date changeDate;

    /**
     * 操作人
     */
    private String operater;

    /**
     * 操作时间
     */
    private Date operateDate;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }

    public String getOperateState() {
        return operateState;
    }

    public void setOperateState(String operateState) {
        this.operateState = operateState;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getOperaterId() {
        return operaterId;
    }

    public void setOperaterId(String operaterId) {
        this.operaterId = operaterId;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public boolean isUrgency() {
        return urgency;
    }

    public void setUrgency(boolean urgency) {
        this.urgency = urgency;
    }

}
