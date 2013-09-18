package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 * 记录cutoff_day的表，通过cutoff day 计算，可以计算出一天的配额情况，及过期的配额，可销售的配额
 * 
 * @author xiaowumi HtlCutoffDayQuota generated by MyEclipse - Hibernate Tools
 */

public class HtlCutoffDayQuota extends CEntity implements Entity {

    // Fields

    // cutoff_day_id
    private Long ID;

    // 配额id
    private long quotaId;

    private HtlQuota quota;

    // 提前天数
    private int cutoffDay;

    // 截止时间
    private String cutoffTime;

    // 配额数量
    private int quotaQty;

    // 已使用数量
    private int cutoffUsedQty;

    // 是否过期
    private String status;

    // Constructors

    public int getCutoffUsedQty() {
        return cutoffUsedQty;
    }

    public void setCutoffUsedQty(int cutoffUsedQty) {
        this.cutoffUsedQty = cutoffUsedQty;
    }

    public int getCutoffDay() {
        return cutoffDay;
    }

    public void setCutoffDay(int cutoffDay) {
        this.cutoffDay = cutoffDay;
    }

    public long getQuotaId() {
        return quotaId;
    }

    public void setQuotaId(long quotaId) {
        this.quotaId = quotaId;
    }

    public int getQuotaQty() {
        return quotaQty;
    }

    public void setQuotaQty(int quotaQty) {
        this.quotaQty = quotaQty;
    }

    /** default constructor */
    public HtlCutoffDayQuota() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public HtlQuota getQuota() {
        return quota;
    }

    public void setQuota(HtlQuota quota) {
        if (null != quota.getID()) {
            this.quotaId = quota.getID().longValue();
        }
        this.quota = quota;
    }

    public String getCutoffTime() {
        return cutoffTime;
    }

    public void setCutoffTime(String cutoffTime) {
        this.cutoffTime = cutoffTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}