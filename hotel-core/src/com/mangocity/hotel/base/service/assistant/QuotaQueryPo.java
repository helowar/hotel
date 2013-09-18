package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class QuotaQueryPo implements Serializable {

    private static final long serialVersionUID = -2315432399154349791L;

    // 房型ID
    private long roomTypeId;

    // 价格类型ID
    private long childRoomId;

    // 酒店ID
    private long hotelId;

    // 支付方式(面付pay，预付pre_pay)
    private String payMethod;

    // 会员类型(1.CC, 2.TP, 3.TMC)
    private int memberType;

    // 配额类型(目前三种配额 包房配额2，普通配额1，临时配额3,普通配额面付4，普通配额预付5,普通配额面预付共享6)
    private String quotaType;

    // 开始时间
    private Date beginDate;

    // 结束时间
    private Date endDate;

    // 扣配额数
    private int quotaNum;

    // 床型ID
    private long bedID;

    public long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public long getChildRoomId() {
        return childRoomId;
    }

    public void setChildRoomId(long childRoomId) {
        this.childRoomId = childRoomId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
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

    public int getQuotaNum() {
        return quotaNum;
    }

    public void setQuotaNum(int quotaNum) {
        this.quotaNum = quotaNum;
    }

    public long getBedID() {
        return bedID;
    }

    public void setBedID(long bedID) {
        this.bedID = bedID;
    }

}
