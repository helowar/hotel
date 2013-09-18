package com.mangocity.hotel.base.web;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class QueryHotelAndContractAction extends PersistenceAction {

    /**
     * 酒店id
     */
    private Long hotelId;

    public String initQueryContract() {
        // 初始化界面，下一次不需要再更新页面。
        HtlHotel hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelId);
        super.getParams().put("hotel", hotel);
        super.getParams().put("init", "YES");
        return "initQueryContract";
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

}
