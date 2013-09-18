package com.mangocity.hotel.base.persistence;

import com.mangocity.util.FakeDeletedEntity;

/**
 * 促销信息的酒店列表
 * 
 * @author xiaowumi
 * 
 */
public class HtlPresaleHotel extends CEntity implements FakeDeletedEntity {

    // Fields

    // 促销信息ID
    private Long ID;

    // 酒店
    private Long presaleId;

    // 促销
    private Long hotelId;

    /**
     * 假删除
     */
    private boolean deleted;

    // Constructors

    /** default constructor */
    public HtlPresaleHotel() {
    }

    // Property accessors
    public Long getID() {
        return this.ID;
    }

    public void setID(Long roomTypeId) {
        this.ID = roomTypeId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getPresaleId() {
        return presaleId;
    }

    public void setPresaleId(Long presaleId) {
        this.presaleId = presaleId;
    }

}
