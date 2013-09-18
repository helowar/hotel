package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.SellSeason;
import com.mangocity.hotel.base.web.webwork.GenericAction;

/**
 */
public class SelectSellSeasonAction extends GenericAction {

    /**
     * 酒店id
     */
    private long hotelId;

    private List<SellSeason> sellSeasonList = new ArrayList<SellSeason>();

    private HotelManage hotelManage;

    public String showSelectDate() {
        sellSeasonList = hotelManage.qryHtlSellSeason(hotelId);
        return "showSelectDate";
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public List<SellSeason> getSellSeasonList() {
        return sellSeasonList;
    }

    public void setSellSeasonList(List<SellSeason> sellSeasonList) {
        this.sellSeasonList = sellSeasonList;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

}
