package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class SellSeason extends CEntity implements Entity {

    private long sellID;

    private Long ID;

    private String sellName;

    private Date sellBeginDate;

    private Date sellEndDate;

    private String sellWeek;

    private String sellCD;

    private String sellRemark;

    private HtlHotel htlHotel;

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    public Date getSellBeginDate() {
        return sellBeginDate;
    }

    public void setSellBeginDate(Date sellBeginDate) {
        this.sellBeginDate = sellBeginDate;
    }

    public String getSellCD() {
        return sellCD;
    }

    public void setSellCD(String sellCD) {
        this.sellCD = sellCD;
    }

    public Date getSellEndDate() {
        return sellEndDate;
    }

    public void setSellEndDate(Date sellEndDate) {
        this.sellEndDate = sellEndDate;
    }

    public long getSellID() {
        return sellID;
    }

    public void setSellID(long sellID) {
        this.sellID = sellID;
    }

    public String getSellName() {
        return sellName;
    }

    public void setSellName(String sellName) {
        this.sellName = sellName;
    }

    public String getSellRemark() {
        return sellRemark;
    }

    public void setSellRemark(String sellRemark) {
        this.sellRemark = sellRemark;
    }

    public String getSellWeek() {
        return sellWeek;
    }

    public void setSellWeek(String sellWeek) {
        this.sellWeek = sellWeek;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

}
