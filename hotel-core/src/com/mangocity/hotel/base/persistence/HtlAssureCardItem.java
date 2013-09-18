package com.mangocity.hotel.base.persistence;

import java.io.Serializable;

/**
 * 信用卡担保取消条款
 */

public class HtlAssureCardItem extends CEntity implements Serializable {

    private static final long serialVersionUID = -6695871702277729839L;

    // Fields
    // private Long ID;

    private long assureCardItemID;

    // 提前天数
    private String cardAheadDay;

    // 取消/修改
    private String cardOperateType;

    // 扣款类型
    private String cardDeductType;

    // 金额/百分比
    private Double cardAmount;

    private HtlCreditAssure htlCreditAssure;

    // 提前日期
    private String cardAheadTime;

    // Constructors

    // 信用卡担保取消条款的时间点
    private String cardCancelAheadTime;

    public long getAssureCardItemID() {
        return assureCardItemID;
    }

    public void setAssureCardItemID(long assureCardItemID) {
        this.assureCardItemID = assureCardItemID;
    }

    public String getCardAheadDay() {
        return cardAheadDay;
    }

    public void setCardAheadDay(String cardAheadDay) {
        this.cardAheadDay = cardAheadDay;
    }

    public String getCardAheadTime() {
        return cardAheadTime;
    }

    public void setCardAheadTime(String cardAheadTime) {
        this.cardAheadTime = cardAheadTime;
    }

    public Double getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(Double cardAmount) {
        this.cardAmount = cardAmount;
    }

    public String getCardDeductType() {
        return cardDeductType;
    }

    public void setCardDeductType(String cardDeductType) {
        this.cardDeductType = cardDeductType;
    }

    public String getCardOperateType() {
        return cardOperateType;
    }

    public void setCardOperateType(String cardOperateType) {
        this.cardOperateType = cardOperateType;
    }

    public HtlCreditAssure getHtlCreditAssure() {
        return htlCreditAssure;
    }

    public void setHtlCreditAssure(HtlCreditAssure htlCreditAssure) {
        this.htlCreditAssure = htlCreditAssure;
    }

    public String getCardCancelAheadTime() {
        return cardCancelAheadTime;
    }

    public void setCardCancelAheadTime(String cardCancelAheadTime) {
        this.cardCancelAheadTime = cardCancelAheadTime;
    }

    /*
     * public Long getID() { return ID; }
     * 
     * public void setID(Long id) { ID = id; }
     */

    // Property accessors
}