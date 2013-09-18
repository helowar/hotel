package com.mangocity.hotel.base.web;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class ShowHotelInfoByRoomStateAction extends PersistenceAction {

    private Long hotelId;

    private HtlHotel hotel;

    private String commendType;

    private HotelManage hotelManage;

    public String getCommendType() {
        return commendType;
    }

    public void setCommendType(String commendType) {
        this.commendType = commendType;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public String showHotelInfoByRoomState() {
        hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelId);
        commendType = hotelManage.findCommendTypeByHotelId(hotelId);
        if (null == commendType || commendType.equals(""))
            commendType = " ";
        if (null == hotel.getFitmentDegree() || hotel.getFitmentDegree().equals("")) {
            hotel.setFitmentDegree(" ");
        }
        return "showHotelInfoByRoomState";
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public HtlHotel getHotel() {
        return hotel;
    }

    public void setHotel(HtlHotel hotel) {
        this.hotel = hotel;
    }
}
