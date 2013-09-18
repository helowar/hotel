package com.mangocity.util.bean;

import java.io.Serializable;

/**
 */
public class TempCutoffDay implements Serializable {

    private int cutoffDay;

    // 截止时间
    private String cutoffTime;

    // 配额面付数量
    private int quotaQty;

    public int getCutoffDay() {
        return cutoffDay;
    }

    public void setCutoffDay(int cutoffDay) {
        this.cutoffDay = cutoffDay;
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
}
