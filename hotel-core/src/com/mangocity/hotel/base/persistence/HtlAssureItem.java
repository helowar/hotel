package com.mangocity.hotel.base.persistence;

import java.io.Serializable;

/**
 * 预付取消条款
 */

public class HtlAssureItem extends CEntity implements Serializable {

    private static final long serialVersionUID = -2366391620144970944L;

    // private Long ID;

    // Fields

    private long assureItemID;

    // 提前天数
    private String aheadDay;

    // 取消/修改
    private String operateType;

    // 扣款类型
    private String deductType;

    // 百分比
    private Double amount;

    private HtlCreditAssure htlCreditAssure;

    // 提前日期
    private String aheadTime;

    // Constructors

    // Property accessors

    /*
     * 预付取消条款的时间点
     */
    private String creditCardAssureTime;

    public String getAheadDay() {
        return this.aheadDay;
    }

    public void setAheadDay(String aheadDay) {
        this.aheadDay = aheadDay;
    }

    public String getOperateType() {
        return this.operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getDeductType() {
        return this.deductType;
    }

    public void setDeductType(String deductType) {
        this.deductType = deductType;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public HtlCreditAssure getHtlCreditAssure() {
        return htlCreditAssure;
    }

    public void setHtlCreditAssure(HtlCreditAssure htlCreditAssure) {
        this.htlCreditAssure = htlCreditAssure;
    }

    public long getAssureItemID() {
        return assureItemID;
    }

    public void setAssureItemID(long assureItemID) {
        this.assureItemID = assureItemID;
    }

    public String getAheadTime() {
        return aheadTime;
    }

    public void setAheadTime(String aheadTime) {
        this.aheadTime = aheadTime;
    }

    public String getCreditCardAssureTime() {
        return creditCardAssureTime;
    }

    public void setCreditCardAssureTime(String creditCardAssureTime) {
        this.creditCardAssureTime = creditCardAssureTime;
    }

    /*
     * public Long getID() { return ID; }
     * 
     * public void setID(Long id) { ID = id; }
     */

}