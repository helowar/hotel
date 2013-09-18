package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;

/**
 * 
 * 下订单时保存在session的一些属性
 * 
 * @author chenkeming
 * 
 */
public class OrderSession implements Serializable {

    /**
     * 酒店名称
     */
    private String hotelName;

    /**
     * 房型名称
     */
    private String roomTypeName;

    /**
     * 酒店结算方式
     */
    private String balanceMethod;

    /**
     * 紧急程度
     */
    private int emergencyLevel;

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getBalanceMethod() {
        return balanceMethod;
    }

    public void setBalanceMethod(String balanceMethod) {
        this.balanceMethod = balanceMethod;
    }

    public int getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(int emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

}
