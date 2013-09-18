package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 采购批次配额的cutoffDay,记录某个批次的cutoff day的情况，记录的是一个时间段，用来生成每一天的cutoff day
 * 
 * @author xiaowumi
 * 
 */

public class HtlBatchCutoffDay implements Entity {

    // Fields
    // batch_cutoff_day_id
    private Long ID;

    // 采购批次id
    private HtlQuotabatch quotabatch;

    // 开始日期
    private Date beginDate;

    // 结束日期
    private Date endDate;

    // 调整周
    private String adjustWeek;

    // 面付配额数量
    private int quantity;

    // 截止天数
    private int cutoffDay;

    // 截止时间
    private String cutoffTime;

    // 创建人
    private String CreateBy;

    // 最近修改人
    private String modifyBy;

    // 创建时间
    private Date createTime;

    // 最近修改时间
    private Date modifyTime;

    // Constructors

    /** default constructor */
    public HtlBatchCutoffDay() {
    }

    // Property accessors

    public String getAdjustWeek() {
        return adjustWeek;
    }

    public void setAdjustWeek(String adjustWeek) {
        this.adjustWeek = adjustWeek;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public String getCreateBy() {
        return CreateBy;
    }

    public void setCreateBy(String createBy) {
        CreateBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getCutoffDay() {
        return cutoffDay;
    }

    public void setCutoffDay(int cutoffDay) {
        this.cutoffDay = cutoffDay;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCutoffTime() {
        return cutoffTime;
    }

    public void setCutoffTime(String cutoffTime) {
        this.cutoffTime = cutoffTime;
    }

    public HtlQuotabatch getQuotabatch() {
        return quotabatch;
    }

    public void setQuotabatch(HtlQuotabatch quotabatch) {
        this.quotabatch = quotabatch;
    }

}