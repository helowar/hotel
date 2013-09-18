package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 * 集团酒店品牌基本信息
 * 
 * @author xiaowumi
 * 
 */
public class HtlNameplate extends CEntity implements Entity {

    /**
     * nameplate_id 集团酒店品牌id
     */
    private Long ID;

    /**
     * 集团酒店
     */
    private HtlHotelGroup hotelGroup;

    /**
     * 集团酒店品牌名称
     */
    private String nameplateName;

    /**
     *品牌中国总部所在地
     */
    private String chinaHqAddress;

    private long hotel_group_id;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public HtlHotelGroup getHotelGroup() {
        return hotelGroup;
    }

    public void setHotelGroup(HtlHotelGroup hotelGroup) {
        this.hotelGroup = hotelGroup;
    }

    public String getNameplateName() {
        return nameplateName;
    }

    public void setNameplateName(String nameplateName) {
        this.nameplateName = nameplateName;
    }

    public String getChinaHqAddress() {
        return chinaHqAddress;
    }

    public void setChinaHqAddress(String chinaHqAddress) {
        this.chinaHqAddress = chinaHqAddress;
    }

    public long getHotel_group_id() {
        return hotel_group_id;
    }

    public void setHotel_group_id(long hotel_group_id) {
        this.hotel_group_id = hotel_group_id;
    }

}
