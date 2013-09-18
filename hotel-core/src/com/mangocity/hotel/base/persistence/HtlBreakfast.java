package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 */
public class HtlBreakfast extends CEntity implements Entity {

    private Long ID;

    // 早餐类型
    private String type;

    // 早餐底价
    private double basePrice;

    private double chineseFood;

    private double westernFood;

    private double buffetDinner;

    // 门市价
    private double saleroomPrice;

    // 协议价
    private double protocolPrice;

    // 早餐时间
    private HtlChargeBreakfast breakfast;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public HtlChargeBreakfast getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(HtlChargeBreakfast breakfast) {
        this.breakfast = breakfast;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getProtocolPrice() {
        return protocolPrice;
    }

    public void setProtocolPrice(double protocolPrice) {
        this.protocolPrice = protocolPrice;
    }

    public double getSaleroomPrice() {
        return saleroomPrice;
    }

    public void setSaleroomPrice(double saleroomPrice) {
        this.saleroomPrice = saleroomPrice;
    }

    public double getBuffetDinner() {
        return buffetDinner;
    }

    public void setBuffetDinner(double buffetDinner) {
        this.buffetDinner = buffetDinner;
    }

    public double getChineseFood() {
        return chineseFood;
    }

    public void setChineseFood(double chineseFood) {
        this.chineseFood = chineseFood;
    }

    public double getWesternFood() {
        return westernFood;
    }

    public void setWesternFood(double westernFood) {
        this.westernFood = westernFood;
    }

}
