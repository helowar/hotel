package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 * 这个类主要是用来临时记录在使用配额的时候，是扣除了哪一个cutoff day 的配额 如果是呼出配额，则记录呼出配额
 * 
 * @author xiaowumi
 * 
 */
public class UsedCutoffDayInfo implements Serializable {

    // 是呼出配额
    public static final int IS_OUTSIDE = 1;

    // 不是呼出配额
    public static final int NOT_OUTSIDE = 0;

    // cut off day 的id
    private long cutoffDayId;

    // 是不是呼出配额,如果是呼出配额，则cutoff_day_id是没有用的。
    private int outsideFlag;

    // 配额数量
    private int quotaQty;

    // 日期
    private Date quotaDate;

    // 失效天数
    private int cutOffDay;

    // 单个配额的价格
    private double quotaPrice;

    // 底价
    private double basePrice;

    // 门市价
    private double saleroomPrice;

    // 服务费
    private double serviceCharge;

    // 销售税
    private double saleTax;

    // 佣金
    private double commission;

    // 佣金是否含税
    private boolean includeTax;

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public int getCutOffDay() {
        return cutOffDay;
    }

    public void setCutOffDay(int cutOffDay) {
        this.cutOffDay = cutOffDay;
    }

    public long getCutoffDayId() {
        return cutoffDayId;
    }

    public void setCutoffDayId(long cutoffDayId) {
        this.cutoffDayId = cutoffDayId;
    }

    public boolean isIncludeTax() {
        return includeTax;
    }

    public void setIncludeTax(boolean includeTax) {
        this.includeTax = includeTax;
    }

    public int getOutsideFlag() {
        return outsideFlag;
    }

    public void setOutsideFlag(int outsideFlag) {
        this.outsideFlag = outsideFlag;
    }

    public Date getQuotaDate() {
        return quotaDate;
    }

    public void setQuotaDate(Date quotaDate) {
        this.quotaDate = quotaDate;
    }

    public double getQuotaPrice() {
        return quotaPrice;
    }

    public void setQuotaPrice(double quotaPrice) {
        this.quotaPrice = quotaPrice;
    }

    public int getQuotaQty() {
        return quotaQty;
    }

    public void setQuotaQty(int quotaQty) {
        this.quotaQty = quotaQty;
    }

    public double getSaleroomPrice() {
        return saleroomPrice;
    }

    public void setSaleroomPrice(double saleroomPrice) {
        this.saleroomPrice = saleroomPrice;
    }

    public double getSaleTax() {
        return saleTax;
    }

    public void setSaleTax(double saleTax) {
        this.saleTax = saleTax;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

}
