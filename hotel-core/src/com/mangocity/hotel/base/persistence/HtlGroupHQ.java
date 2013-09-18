package com.mangocity.hotel.base.persistence;

import java.io.Serializable;

/**
 * 集团酒店总部联系信息
 * 
 * @author xiaowumi
 * 
 */
public class HtlGroupHQ extends HtlContactInfo implements Serializable {

    /**
     * 集团酒店
     */
    private HtlHotelGroup hotelGroup;

    public HtlHotelGroup getHotelGroup() {
        return hotelGroup;
    }

    public void setHotelGroup(HtlHotelGroup hotelGroup) {
        this.hotelGroup = hotelGroup;
    }
}
