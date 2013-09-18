package com.mangocity.hotel.base.web;

import java.util.List;

import com.mangocity.hotel.base.manage.RoomStateManage;
import com.mangocity.hotel.base.web.webwork.GenericAction;

/**
 */
public class SelectCloseRoomDateAction extends GenericAction {

    /**
     * 酒店id
     */
    private long hotelId;

    private RoomStateManage roomStateManage;

    private List lstCloseRoom;

    public List getLstCloseRoom() {
        return lstCloseRoom;
    }

    public void setLstCloseRoom(List lstCloseRoom) {
        this.lstCloseRoom = lstCloseRoom;
    }

    public String selectDate() {
        lstCloseRoom = roomStateManage.selectRoomCloseDate(hotelId);
        return "selectDate";
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public RoomStateManage getRoomStateManage() {
        return roomStateManage;
    }

    public void setRoomStateManage(RoomStateManage roomStateManage) {
        this.roomStateManage = roomStateManage;
    }

}
