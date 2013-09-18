package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class RoomControlBean implements Serializable {

    private List dateSegments = new ArrayList();

    private String[] roomType;

    private String reason;

    private long hotelID;

    public List getDateSegments() {
        return dateSegments;
    }

    public void setDateSegments(List dateSegments) {
        this.dateSegments = dateSegments;
    }

    public long getHotelID() {
        return hotelID;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String[] getRoomType() {
        return roomType;
    }

    public void setRoomType(String[] roomType) {
        this.roomType = roomType;
    }
}
