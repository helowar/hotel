package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author xiaowumi 扣减或退还配额时，还有价格，需要的参数,及操作结果写到同一个类里，进行上下传递
 */

public class OperateQuotaPricePara implements Serializable {

    // 开始日期
    private String beginDate;

    // 结束日期
    private String endDate;

    // 酒店id
    private long hotelId;

    // 房型id
    private long roomTypeId;

    // 扣减或者退还的数量
    private int quotaAmount;

    // 配额模式(进店与在店模式)
    private String quotaPattern;

    // 配额类型
    private String quotaType;

    // 会员类型
    private String memberType;

    // 会员id
    private long memberId;

    // 操作者
    private long operator;

    // 配额是否要回收
    private String takeBackQuota;

    // 是使用了哪个cutoff day 的配额,或者哪天有呼出配额 保存UsedCutoffDayInfo
    private List lstUsedCutoffDayInfo;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public List getLstUsedCutoffDayInfo() {
        return lstUsedCutoffDayInfo;
    }

    public void setLstUsedCutoffDayInfo(List lstUsedCutoffDayInfo) {
        this.lstUsedCutoffDayInfo = lstUsedCutoffDayInfo;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public long getOperator() {
        return operator;
    }

    public void setOperator(long operator) {
        this.operator = operator;
    }

    public int getQuotaAmount() {
        return quotaAmount;
    }

    public void setQuotaAmount(int quotaAmount) {
        this.quotaAmount = quotaAmount;
    }

    public String getQuotaPattern() {
        return quotaPattern;
    }

    public void setQuotaPattern(String quotaPattern) {
        this.quotaPattern = quotaPattern;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getTakeBackQuota() {
        return takeBackQuota;
    }

    public void setTakeBackQuota(String takeBackQuota) {
        this.takeBackQuota = takeBackQuota;
    }

}
