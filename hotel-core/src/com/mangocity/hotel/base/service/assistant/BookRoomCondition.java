package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.Date;

import com.mangocity.util.hotel.constant.PayMethod;

/**
 * 
 * @author xiaowumi 预订房间需要的条件
 */

public class BookRoomCondition implements Serializable {
    /**
     * 酒店id
     */
    private Long hotelId;

    /**
     * 房型id
     */
    private Long roomTypeId;

    /**
     * 子房型Id
     */
    private Long childRoomTypeId;

    /**
     * 房间数
     */
    private int roomNum;

    /**
     *会员类型
     */
    private String memberType;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 付款类型(预付还是面付)
     * 
     */
    private String payMethod;

    /**
     * 预订开始日期
     */
    private Date checkinDate;

    /**
     * 预订结束日期
     */
    private Date checkoutDate;

    /**
     * 配额类型
     */
    private String quotaType;

    private boolean getQuota;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String paymentMethod) {
        this.payMethod = paymentMethod;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomQty) {
        this.roomNum = roomQty;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public Long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(Long childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public boolean isGetQuota() {
        return getQuota;
    }

    public void setGetQuota(boolean getQuota) {
        this.getQuota = getQuota;
    }

    /**
     * 是否预付订单
     * 
     * @return
     */
    public boolean isPrepayOrder() {
        return PayMethod.PRE_PAY.equals(this.payMethod);
    }

}
