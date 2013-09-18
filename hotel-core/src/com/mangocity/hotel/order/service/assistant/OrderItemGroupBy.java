package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 用于传真,邮件等
 * 
 * @author chenkeming
 */
public class OrderItemGroupBy implements Serializable {

    private Date night;

    private int quantityByNight;

    private double basepriceByAvg;

    private double salepriceByAvg;

    /**
     * 含早类型
     */
    private int breakfast;

    private int breakfastNum;

    public Date getNight() {
        return night;
    }

    public void setNight(Date night) {
        this.night = night;
    }

    public int getQuantityByNight() {
        return quantityByNight;
    }

    public void setQuantityByNight(int quantityByNight) {
        this.quantityByNight = quantityByNight;
    }

    public int getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(int breakfast) {
        this.breakfast = breakfast;
    }

    public int getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(int breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public double getBasepriceByAvg() {
        return basepriceByAvg;
    }

    public void setBasepriceByAvg(double basepriceByAvg) {
        this.basepriceByAvg = basepriceByAvg;
    }

    public double getSalepriceByAvg() {
        return salepriceByAvg;
    }

    public void setSalepriceByAvg(double salepriceByAvg) {
        this.salepriceByAvg = salepriceByAvg;
    }

}
