package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.dao.IPriceDao;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class DisplayPriceAction extends PersistenceAction {

    /**
     * 房间id
     */
    private long roomId;

    /**
     * 酒店id
     */
    private long hotelId;

    /**
     * 价格列表
     */
    private List lstPrice = new ArrayList();

    private IPriceDao priceDao;

    public String displayPrice() {
        lstPrice = priceDao.getRoomPrices(roomId);
        return "displayPrice";
    }

    public List getLstPrice() {
        return lstPrice;
    }

    public void setLstPrice(List lstPrice) {
        this.lstPrice = lstPrice;
    }

    public IPriceDao getPriceDao() {
        return priceDao;
    }

    public void setPriceDao(IPriceDao priceDao) {
        this.priceDao = priceDao;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

}
