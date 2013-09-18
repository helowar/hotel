package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 */
public class HtlCtlDsply implements Entity {

    /**
     * 主健
     */
    private Long ID;

    /**
     * 酒店ID
     */
    private Long hotelID;

    /**
     * 酒店名称
     */
    private String hotelName;

    /**
     * 房型ID
     */
    private Long roomTypeId;

    /**
     * 房型名称
     */
    private String roomTypeName;

    /**
     * 价格类型ID
     */
    private Long priceTypeId;

    /**
     * 价格类型名称
     */
    private String priceTypeName;

    /**
     * 支付方式
     */
    private String payMethod;

    /**
     * CC是否可见 =1表示可见 =0表示不可见
     */
    private Long CC;

    /**
     * 度假是否可见 =1表示可见 =0表示不可见
     */
    private Long TP;

    /**
     * TMC是否可见 =1表示可见 =0表示不可见
     */
    private Long TMC;

    /**
     * 网站是否可见 =1表示可见 =0表示不可见
     */
    private Long WEB;

    /**
     * 代理是否可见 =1表示可见 =0表示不可见
     */
    private Long AGENT;

    /**
     * 备用字段
     */
    private Long FLD1;

    /**
     * 备用字段
     */
    private Long FLD2;

    /**
     * 备用字段
     */
    private Long FLD3;

    /**
     * 备用字段
     */
    private Long FLD4;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Long getAGENT() {
        return AGENT;
    }

    public void setAGENT(Long agent) {
        AGENT = agent;
    }

    public Long getCC() {
        return CC;
    }

    public void setCC(Long cc) {
        CC = cc;
    }

    public Long getFLD1() {
        return FLD1;
    }

    public void setFLD1(Long fld1) {
        FLD1 = fld1;
    }

    public Long getFLD2() {
        return FLD2;
    }

    public void setFLD2(Long fld2) {
        FLD2 = fld2;
    }

    public Long getFLD3() {
        return FLD3;
    }

    public void setFLD3(Long fld3) {
        FLD3 = fld3;
    }

    public Long getFLD4() {
        return FLD4;
    }

    public void setFLD4(Long fld4) {
        FLD4 = fld4;
    }

    public Long getHotelID() {
        return hotelID;
    }

    public void setHotelID(Long hotelID) {
        this.hotelID = hotelID;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Long getPriceTypeId() {
        return priceTypeId;
    }

    public void setPriceTypeId(Long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Long getTMC() {
        return TMC;
    }

    public void setTMC(Long tmc) {
        TMC = tmc;
    }

    public Long getTP() {
        return TP;
    }

    public void setTP(Long tp) {
        TP = tp;
    }

    public Long getWEB() {
        return WEB;
    }

    public void setWEB(Long web) {
        WEB = web;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getPriceTypeName() {
        return priceTypeName;
    }

    public void setPriceTypeName(String priceTypeName) {
        this.priceTypeName = priceTypeName;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }
}
