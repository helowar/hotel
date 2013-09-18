package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class HtlOpenCloseRoom implements Entity {

    private Long ID;

    /**
     * 酒店id
     */
    private long hotelId;

    /**
     * 房型 + 价格类型的名称组合。
     */
    private String roomType;

    /**
     * 价格类型id,曾经关房是关房型，后来改关价格类型，在数据库上不做变动了。
     */
    private String roomTypeId;

    /**
     * 开始日期
     */
    private Date beginDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 星期
     */
    private String week;

    /**
     * 开关房标志 'K'表示开房，'G'表示关房
     */
    private String opCloseSign;

    /**
     * 开关房原因
     */
    private String causeSign;

    /**
     * 开房操作员
     */
    private String openRoomOP;

    /**
     * 开房时间
     */
    private Date openRoomTime;

    /**
     * 关房操作员
     */
    private String closeRoomOP;

    /**
     * 关房时间
     */
    private Date closeRoomTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 提示
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
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

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getOpCloseSign() {
        return opCloseSign;
    }

    public void setOpCloseSign(String opCloseSign) {
        this.opCloseSign = opCloseSign;
    }

    public String getCauseSign() {
        return causeSign;
    }

    public void setCauseSign(String causeSign) {
        this.causeSign = causeSign;
    }

    public String getOpenRoomOP() {
        return openRoomOP;
    }

    public void setOpenRoomOP(String openRoomOP) {
        this.openRoomOP = openRoomOP;
    }

    public Date getOpenRoomTime() {
        return openRoomTime;
    }

    public void setOpenRoomTime(Date openRoomTime) {
        this.openRoomTime = openRoomTime;
    }

    public String getCloseRoomOP() {
        return closeRoomOP;
    }

    public void setCloseRoomOP(String closeRoomOP) {
        this.closeRoomOP = closeRoomOP;
    }

    public Date getCloseRoomTime() {
        return closeRoomTime;
    }

    public void setCloseRoomTime(Date closeRoomTime) {
        this.closeRoomTime = closeRoomTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

}
