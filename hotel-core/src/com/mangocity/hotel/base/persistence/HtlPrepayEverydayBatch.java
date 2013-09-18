package com.mangocity.hotel.base.persistence;

import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * HtlPrepayEverydayBatch generated by MyEclipse Persistence Tools
 */

public class HtlPrepayEverydayBatch extends CEntity implements Entity {

    // Fields

    private Long id;

    private HtlPreconcertItemBatch htlPreconcertItemBatch;

    private String balanceType;

    private String paymentType;

    private String amountType;

    private Date limitDate;

    private String limitTime;

    private Long limitAheadDays;

    private String limitAheadDaysTime;

    private Long daysAfterConfirm;

    private String timeAfterConfirm;

    private String timelimitType;

    private String prepaydebuctType;

    private List htlPrepayItemEverydayBatch;

    // Constructors

    public HtlPreconcertItemBatch getHtlPreconcertItemBatch() {
        return htlPreconcertItemBatch;
    }

    public void setHtlPreconcertItemBatch(HtlPreconcertItemBatch htlPreconcertItemBatch) {
        this.htlPreconcertItemBatch = htlPreconcertItemBatch;
    }

    /** default constructor */
    public HtlPrepayEverydayBatch() {
    }

    // Property accessors

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBalanceType() {
        return this.balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public String getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getAmountType() {
        return this.amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public Date getLimitDate() {
        return this.limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }

    public String getLimitTime() {
        return this.limitTime;
    }

    public void setLimitTime(String limitTime) {
        this.limitTime = limitTime;
    }

    public Long getLimitAheadDays() {
        return this.limitAheadDays;
    }

    public void setLimitAheadDays(Long limitAheadDays) {
        this.limitAheadDays = limitAheadDays;
    }

    public String getLimitAheadDaysTime() {
        return this.limitAheadDaysTime;
    }

    public void setLimitAheadDaysTime(String limitAheadDaysTime) {
        this.limitAheadDaysTime = limitAheadDaysTime;
    }

    public Long getDaysAfterConfirm() {
        return this.daysAfterConfirm;
    }

    public void setDaysAfterConfirm(Long daysAfterConfirm) {
        this.daysAfterConfirm = daysAfterConfirm;
    }

    public String getTimeAfterConfirm() {
        return this.timeAfterConfirm;
    }

    public void setTimeAfterConfirm(String timeAfterConfirm) {
        this.timeAfterConfirm = timeAfterConfirm;
    }

    public List getHtlPrepayItemEverydayBatch() {
        return htlPrepayItemEverydayBatch;
    }

    public void setHtlPrepayItemEverydayBatch(List htlPrepayItemEverydayBatch) {
        this.htlPrepayItemEverydayBatch = htlPrepayItemEverydayBatch;
    }

    public String getPrepaydebuctType() {
        return prepaydebuctType;
    }

    public void setPrepaydebuctType(String prepaydebuctType) {
        this.prepaydebuctType = prepaydebuctType;
    }

    public String getTimelimitType() {
        return timelimitType;
    }

    public void setTimelimitType(String timelimitType) {
        this.timelimitType = timelimitType;
    }

    public Long getID() {
        // TODO Auto-generated method stub
        return id;
    }

}