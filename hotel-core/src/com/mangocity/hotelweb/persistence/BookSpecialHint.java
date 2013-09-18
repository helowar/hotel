package com.mangocity.hotelweb.persistence;

import java.util.Date;

/**
 */
public class BookSpecialHint {
    // 预定条款开始日期
    private Date beginDate;

    // 预订条款结束日期
    private Date endDate;

    // 担保类型
    private String assureType;

    // 提前日期
    private Date aheadDate;

    // 提前天数
    private int aheadDays;

    // 提前时间
    private String aheadTime;

    // 扣款类型
    private String deductType;

    // 扣款金额
    private double deductAmount;

    // 最晚担保时间
    private String lastAssureTime;

    // 峰时担保起始日期
    private Date highBeginDate;

    // 峰时担保结束日期
    private Date highEndDate;

    public Date getAheadDate() {
        return aheadDate;
    }

    public void setAheadDate(Date aheadDate) {
        this.aheadDate = aheadDate;
    }

    public int getAheadDays() {
        return aheadDays;
    }

    public void setAheadDays(int aheadDays) {
        this.aheadDays = aheadDays;
    }

    public String getAheadTime() {
        return aheadTime;
    }

    public void setAheadTime(String aheadTime) {
        this.aheadTime = aheadTime;
    }

    public String getAssureType() {
        return assureType;
    }

    public void setAssureType(String assureType) {
        this.assureType = assureType;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public double getDeductAmount() {
        return deductAmount;
    }

    public void setDeductAmount(double deductAmount) {
        this.deductAmount = deductAmount;
    }

    public String getDeductType() {
        return deductType;
    }

    public void setDeductType(String deductType) {
        this.deductType = deductType;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getHighBeginDate() {
        return highBeginDate;
    }

    public void setHighBeginDate(Date highBeginDate) {
        this.highBeginDate = highBeginDate;
    }

    public Date getHighEndDate() {
        return highEndDate;
    }

    public void setHighEndDate(Date highEndDate) {
        this.highEndDate = highEndDate;
    }

    public String getLastAssureTime() {
        return lastAssureTime;
    }

    public void setLastAssureTime(String lastAssureTime) {
        this.lastAssureTime = lastAssureTime;
    }

}
