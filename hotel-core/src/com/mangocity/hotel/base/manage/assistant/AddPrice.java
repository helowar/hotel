package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;

/**
 */
public class AddPrice implements Serializable {

    private Long roomTypeId;

    private Long childRoomTypeId;

    private double payAmount;

    private double prePayAmount;

    private String addScopeType;

    public String getAddScopeType() {
        return addScopeType;
    }

    public void setAddScopeType(String addScopeType) {
        this.addScopeType = addScopeType;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public double getPrePayAmount() {
        return prePayAmount;
    }

    public void setPrePayAmount(double prePayAmount) {
        this.prePayAmount = prePayAmount;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeID) {
        this.roomTypeId = roomTypeID;
    }

    public Long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(Long childRoomTypeID) {
        this.childRoomTypeId = childRoomTypeID;
    }

}
