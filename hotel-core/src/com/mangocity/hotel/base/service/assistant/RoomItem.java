package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author chenkeming
 * 
 */
public class RoomItem implements Serializable {

    /**
     * 房间ID
     */
    private long roomId;

    /**
     * 房间日期
     */
    private Date saleDate;

    /**
     * 销售价
     */
    private double salePrice;

    /**
     * 底价
     */
    private double basePrice;

    /**
     * 佣金
     */
    private double commisssion;

    /**
     * 是否含早
     */
    private boolean breakfast;

    /**
     * 含早数量
     */
    private int breakfastNum;

    /**
     * 含早类型?
     */
    private String breakfastType;

    /**
     * 门市价
     */
    private double roomPrice;

    /**
     * 房态
     */
    private String state;

    /**
     * 配额ID
     */
    private long quotaId;

    /**
     * 房间数量
     */
    private int roomNum;

    // RoomPriceQuotaForEachDay的List
    // private List items = new ArrayList();

    // public List getItems() {
    // return items;
    // }
    //
    // public void setItems(List items) {
    // this.items = items;
    // }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double price) {
        this.roomPrice = price;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getState() {
        return state;
    }

    public void setState(String roomState) {
        this.state = roomState;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double saleTruePrice) {
        this.salePrice = saleTruePrice;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date ableSaleDate) {
        this.saleDate = ableSaleDate;
    }

    public long getQuotaId() {
        return quotaId;
    }

    public void setQuotaId(long quotaId) {
        this.quotaId = quotaId;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public boolean isBreakfast() {
        return breakfast;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public int getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(int breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public String getBreakfastType() {
        return breakfastType;
    }

    public void setBreakfastType(String breakfastType) {
        this.breakfastType = breakfastType;
    }

    public double getCommisssion() {
        return commisssion;
    }

    public void setCommisssion(double commisssion) {
        this.commisssion = commisssion;
    }

}
