package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.dao.IPriceDao;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlExhibit;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class ExhibitAction extends PersistenceAction {

    private List changePriceWarnings = new ArrayList();

    private long hotelID;

    private HotelManage hotelManage;

    private IPriceDao priceDao;

    protected Class getEntityClass() {
        return HtlExhibit.class;
    }

    public String list() {
        // 查询酒店所在的城市

        if (0 >= hotelID)
            super.forwardError("hotelID不能为空!");

        // hotelManage=new HotelManageImpl();
        // HtlHotel hotel = hotelManage.findHotel(hotelID);

        // 根据酒店查询调价提示
        changePriceWarnings = priceDao.getChangePriceWarnings(hotelID);
        return "list";
    }

    public Long getHotelID() {
        return hotelID;
    }

    public void setHotelID(Long hotelID) {
        this.hotelID = hotelID;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public IPriceDao getPriceDao() {
        return priceDao;
    }

    public void setPriceDao(IPriceDao priceDao) {
        this.priceDao = priceDao;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public List getChangePriceWarnings() {
        return changePriceWarnings;
    }

    public void setChangePriceWarnings(List changePriceWarnings) {
        this.changePriceWarnings = changePriceWarnings;
    }

}
