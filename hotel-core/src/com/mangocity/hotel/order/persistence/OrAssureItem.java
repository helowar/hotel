package com.mangocity.hotel.order.persistence;

import java.io.Serializable;

import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.util.Entity;

/**
 * 取消/修改 条款
 */

public class OrAssureItem implements Entity, Serializable {

    private static final long serialVersionUID = 4365490342650412468L;

    private Long ID;    

    private String aheadDay;

    private String operateType;

    private String deductType;

    private double amount;
    
    //提前日期
    private String aheadTime;    
    
    //提前时间点
    private String cardCancelAheadTime;
    
    private OrReservation reserv;

    public String getCardCancelAheadTime() {
        return cardCancelAheadTime;
    }

    public void setCardCancelAheadTime(String cardCancelAheadTime) {
        this.cardCancelAheadTime = cardCancelAheadTime;
    }

    public String getAheadDay() {
        return aheadDay;
    }

    public void setAheadDay(String aheadDay) {
        this.aheadDay = aheadDay;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getAheadTime() {
        return aheadTime;
    }

    public void setAheadTime(String aheadTime) {
        this.aheadTime = aheadTime;
    }

    public String getDeductType() {
        return deductType;
    }

    public void setDeductType(String deductType) {
        this.deductType = deductType;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public OrReservation getReserv() {
        return reserv;
    }

    public void setReserv(OrReservation reserv) {
        this.reserv = reserv;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    
    
}