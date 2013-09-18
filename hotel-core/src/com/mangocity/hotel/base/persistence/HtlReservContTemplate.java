package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * HtlReservContTemplate generated by MyEclipse Persistence Tools 预订必须连住日期模板
 */

public class HtlReservContTemplate extends CEntity implements Entity {

    // Fields

    private Long id;

    /*
     * 预订条款模板ID
     */
    // private Long reservationClauseId;
    /*
     * 连住日期
     */
    private Date continueDate;

    /*
     * 预订条款模板
     */
    private HtlReservationTemplate htlReservationTemplate;

    private Date continueEndDate;

    private String weeks;

    public Date getContinueEndDate() {
        return continueEndDate;
    }

    public void setContinueEndDate(Date continueEndDate) {
        this.continueEndDate = continueEndDate;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    // Constructors

    /** default constructor */
    public HtlReservContTemplate() {
    }

    // Property accessors

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getContinueDate() {
        return this.continueDate;
    }

    public void setContinueDate(Date continueDate) {
        this.continueDate = continueDate;
    }

    public Long getID() {
        // TODO Auto-generated method stub
        return id;
    }

    public HtlReservationTemplate getHtlReservationTemplate() {
        return htlReservationTemplate;
    }

    public void setHtlReservationTemplate(HtlReservationTemplate htlReservationTemplate) {
        this.htlReservationTemplate = htlReservationTemplate;
    }

    @Override
    /*
     * * 重写equals和hashCode方法,比较两个必住时期是否相等 Add by chenjuesu
     */
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return obj instanceof HtlReservContTemplate
            && this.continueDate.getTime() == ((HtlReservContTemplate) obj).getContinueDate()
                .getTime();
    }

    public int hashCode() {
        long ht = this.continueDate.getTime();
        return Long.valueOf(ht).intValue() ^ Long.valueOf(ht >> 32).intValue();
    }
}