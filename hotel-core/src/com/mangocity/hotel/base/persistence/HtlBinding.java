package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

import java.io.Serializable;
import java.util.*;

/**
 */
public class HtlBinding extends CEntity implements Entity, Serializable {

    public Long getID() {
        // TODO 自动生成方法存根
        return null;
    }

    // 开始日期
    private Date beginDate;

    // 结束日期
    private Date endDate;

    /**
     * 预览的时候价格类型以数组形式存放
     */
    private String[] pricetypes;

    /**
     * 保存的时候拆分后价格类型（由pricetype拆分所得）
     */
    private String pricetype;

    /**
     * 是否面转预
     */
    private String paytoprepay;

    /**
     *状态
     */
    private String tactive;

    /**
     * 房型
     */
    private String[] roomTypes;

    /**
     * 条款
     */
    private String tiaokuan;

    /**
     * 预定条款名称
     */
    private String reservationNames;

    /**
     * 星期
     */
    private String weeks;

    // add by shengwei.zuo 2009-04-15 用于按日期调整预定条款预览显示 begin

    // 房型名称
    private String roomName;

    // 当天日期
    private String validDt;

    // 价格类型ID；
    private Long priceTypeID;

    private String beginDt;

    private String endDt;

    // add by shengwei.zuo 2009-04-15 用于按日期调整预定条款预览显示 end

    private HtlPreconcertItemBatch htlPreconcertItemBatch;

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getTiaokuan() {
        return tiaokuan;
    }

    public void setTiaokuan(String tiaokuan) {
        this.tiaokuan = tiaokuan;
    }

    public String[] getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(String[] roomTypes) {
        this.roomTypes = roomTypes;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPaytoprepay() {
        return paytoprepay;
    }

    public void setPaytoprepay(String paytoprepay) {
        this.paytoprepay = paytoprepay;
    }

    public String getTactive() {
        return tactive;
    }

    public void setTactive(String tactive) {
        this.tactive = tactive;
    }

    public String getPricetype() {
        return pricetype;
    }

    public void setPricetype(String pricetype) {
        this.pricetype = pricetype;
    }

    public String getReservationNames() {
        return reservationNames;
    }

    public void setReservationNames(String reservationNames) {
        this.reservationNames = reservationNames;
    }

    public HtlPreconcertItemBatch getHtlPreconcertItemBatch() {
        return htlPreconcertItemBatch;
    }

    public void setHtlPreconcertItemBatch(HtlPreconcertItemBatch htlPreconcertItemBatch) {
        this.htlPreconcertItemBatch = htlPreconcertItemBatch;
    }

    public Long getPriceTypeID() {
        return priceTypeID;
    }

    public void setPriceTypeID(Long priceTypeID) {
        this.priceTypeID = priceTypeID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getValidDt() {
        return validDt;
    }

    public void setValidDt(String validDt) {
        this.validDt = validDt;
    }

    public String getBeginDt() {
        return beginDt;
    }

    public void setBeginDt(String beginDt) {
        this.beginDt = beginDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String[] getPricetypes() {
        return pricetypes;
    }

    public void setPricetypes(String[] pricetypes) {
        this.pricetypes = pricetypes;
    }

}
