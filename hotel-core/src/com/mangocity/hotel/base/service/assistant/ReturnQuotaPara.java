package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;

/**
 */
public class ReturnQuotaPara implements Serializable {

    private long cutoffDayID;

    private int quotaNum;

    private long memberType;

    // 记录退配额的情况，0为成功，-1为退配额不成功
    private int state;

    public long getCutoffDayID() {
        return cutoffDayID;
    }

    public void setCutoffDayID(long cutoffDayID) {
        this.cutoffDayID = cutoffDayID;
    }

    public int getQuotaNum() {
        return quotaNum;
    }

    public void setQuotaNum(int quotaNum) {
        this.quotaNum = quotaNum;
    }

    public long getMemberType() {
        return memberType;
    }

    public void setMemberType(long memberType) {
        this.memberType = memberType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
