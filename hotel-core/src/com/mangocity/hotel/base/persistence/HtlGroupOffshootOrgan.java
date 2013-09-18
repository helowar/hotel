package com.mangocity.hotel.base.persistence;

/**
 * 集团酒店分支机构联系信息
 * 
 * @author xiaowumi
 * 
 */
public class HtlGroupOffshootOrgan extends HtlContactInfo {
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
