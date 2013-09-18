package com.mangocity.hotel.base.persistence;

import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 */
public class HtlAddscopeHdr extends CEntity implements Entity {

    private Long ID;

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
     * 配额类型
     */
    private String quotaType;

    /**
     * 酒店id
     */
    private long hotelId;

    /**
     * 加幅子表
     */
    private List lstAddscope;

    public List getLstAddscope() {
        return lstAddscope;
    }

    public void setLstAddscope(List lstAddscope) {
        this.lstAddscope = lstAddscope;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
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

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

}
