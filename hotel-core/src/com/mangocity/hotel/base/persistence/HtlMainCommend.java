package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.FakeDeletedEntity;

/**
 */
public class HtlMainCommend extends CEntity implements FakeDeletedEntity {

    // Fields

    // 主推ID
    private Long ID;

    // 酒店ID
    private Long hotelID;

    // 推荐类型
    private String commendType;

    // 起始日期
    private Date beginDate;

    // 终止时期
    private Date endDate;

    // 备注
    private String memo;

    // 假删除

    private boolean deleted;

    // 主推评分
    private List lstCommScore = new ArrayList();

    // Constructors

    /** default constructor */
    public HtlMainCommend() {
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

    public String getCommendType() {
        return commendType;
    }

    public void setCommendType(String commendType) {
        this.commendType = commendType;
    }

    public Long getHotelID() {
        return hotelID;
    }

    public void setHotelID(Long hotelID) {
        this.hotelID = hotelID;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List getLstCommScore() {
        return lstCommScore;
    }

    public void setLstCommScore(List lstCommScore) {
        this.lstCommScore = lstCommScore;
    }

}
