package com.mangocity.util.bean;

import java.io.Serializable;

/**
 */
public class RoomTypeStatus implements Serializable {

    private String roomTypeChoose;

    /**
     * 房型id
     */
    private String roomTypeID;

    /**
     * 房型的床型状态
     */
    private String roomBedStatus;

    /**
     * 床型id
     */
    private String roomBedId;

    private String roomName;

    public String getRoomBedId() {
        return roomBedId;
    }

    public void setRoomBedId(String roomBedId) {
        this.roomBedId = roomBedId;
    }

    public String getRoomBedStatus() {
        return roomBedStatus;
    }

    public void setRoomBedStatus(String roomBedStatus) {
        this.roomBedStatus = roomBedStatus;
    }

    public String getRoomTypeID() {
        return roomTypeID;
    }

    public void setRoomTypeID(String roomTypeID) {
        this.roomTypeID = roomTypeID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomTypeChoose() {
        return roomTypeChoose;
    }

    public void setRoomTypeChoose(String roomTypeChoose) {
        this.roomTypeChoose = roomTypeChoose;
    }

}
