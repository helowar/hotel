package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 * HtlPicture generated by MyEclipse - Hibernate Tools 酒店图片记录
 */

public class HtlPicture extends CEntity implements Entity {

    // Fields

    // 酒店图片id
    private Long ID;

    // 酒店id
    private long hotelId;

    // 图片类型
    private String pictureType;

    // 图片名
    private String pictureName;

    // Constructors

    /** default constructor */
    public HtlPicture() {
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }
}