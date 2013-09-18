package com.mangocity.hotel114.util;

import java.io.Serializable;

import com.mangocity.hotel114.manage.Hotel114ManageWeb;
import com.mangocity.hotelweb.persistence.HotelInfoForWeb;

/**
 */
public class Hotel114InfoForWebUtil implements Serializable {

    private Hotel114ManageWeb hotelManageWeb;

    public Hotel114InfoForWebUtil() {

    }

    public Hotel114InfoForWebUtil(Hotel114ManageWeb mweb) {
        this.hotelManageWeb = mweb;
    }

    public HotelInfoForWeb queryHotelInfo(long hotelId) {
        HotelInfoForWeb hotelInfoForWeb = hotelManageWeb.queryHotelInfoForWeb(hotelId);
        return hotelInfoForWeb;
    }

    public Hotel114ManageWeb getHotelManageWeb() {
        return hotelManageWeb;
    }

    public void setHotelManageWeb(Hotel114ManageWeb hotelManageWeb) {
        this.hotelManageWeb = hotelManageWeb;
    }

}
