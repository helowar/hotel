package com.mangocity.hotel.base.persistence;

import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * HtlReservation generated by MyEclipse Persistence Tools
 */

public class HtlReservation extends CEntity implements Entity {

    // Fields

    private Long id;

    private Long aheadDay;

    private String aheadTime;

    private Date mustAheadDate;

    private String mustAheadTime;

    private Long continueNights;

    private Long maxRestrictNights;

    private Long minRestrictNights;

    /*
     * add by shengwei.zuo 2009-02-10 酒店预订担保预付条款总表
     */
    private HtlPreconcertItem htlPreconcertItem;

    /*
     * 必须连住日期的关系,或者 or 并且
     */
    private String continueDatesRelation;
    
    /**
     * add by shengwei.zuo 预定条款的时限 新增时间段的的预订规则 2009-09-03 begin
     */
    //必须的起始日期  
    private Date mustFromDate;
    
    //必须的截止日期
    private Date mustToDate;
    
    //必须的起始时间  
    private String mustFromTime;
    
    //必须的截止时间
    private String mustToTime;
    
    /**
     * add by shengwei.zuo 预定条款的时限 新增时间段的的预订规则 2009-09-03 end
     */
    	
    /**
     * 每日预订必须连住日期表
     */
    private List<HtlReservCont> htlReservacontList;

    // Constructors

    /** default constructor */
    public HtlReservation() {
    }

    /** full constructor */
    public HtlReservation(Long aheadDay, String aheadTime, Date mustAheadDate,
        String mustAheadTime, Long continueNights) {
        this.aheadDay = aheadDay;
        this.aheadTime = aheadTime;
        this.mustAheadDate = mustAheadDate;
        this.mustAheadTime = mustAheadTime;
        this.continueNights = continueNights;
    }

    // Property accessors

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAheadDay() {
        return this.aheadDay;
    }

    public void setAheadDay(Long aheadDay) {
        this.aheadDay = aheadDay;
    }

    public String getAheadTime() {
        return this.aheadTime;
    }

    public void setAheadTime(String aheadTime) {
        this.aheadTime = aheadTime;
    }

    public Date getMustAheadDate() {
        return this.mustAheadDate;
    }

    public void setMustAheadDate(Date mustAheadDate) {
        this.mustAheadDate = mustAheadDate;
    }

    public String getMustAheadTime() {
        return this.mustAheadTime;
    }

    public void setMustAheadTime(String mustAheadTime) {
        this.mustAheadTime = mustAheadTime;
    }

    public Long getContinueNights() {
        return this.continueNights;
    }

    public void setContinueNights(Long continueNights) {
        this.continueNights = continueNights;
    }

    public Long getID() {
        // TODO Auto-generated method stub
        return id;
    }

    public HtlPreconcertItem getHtlPreconcertItem() {
        return htlPreconcertItem;
    }

    public void setHtlPreconcertItem(HtlPreconcertItem htlPreconcertItem) {
        this.htlPreconcertItem = htlPreconcertItem;
    }

    public List<HtlReservCont> getHtlReservacontList() {
        return htlReservacontList;
    }

    public void setHtlReservacontList(List<HtlReservCont> htlReservacontList) {
        this.htlReservacontList = htlReservacontList;
    }

    public String getContinueDatesRelation() {
        return continueDatesRelation;
    }

    public void setContinueDatesRelation(String continueDatesRelation) {
        this.continueDatesRelation = continueDatesRelation;
    }

    public Long getMaxRestrictNights() {
        return maxRestrictNights;
    }

    public void setMaxRestrictNights(Long maxRestrictNights) {
        this.maxRestrictNights = maxRestrictNights;
    }

    public Long getMinRestrictNights() {
        return minRestrictNights;
    }

    public void setMinRestrictNights(Long minRestrictNights) {
        this.minRestrictNights = minRestrictNights;
    }

	public Date getMustFromDate() {
		return mustFromDate;
	}

	public void setMustFromDate(Date mustFromDate) {
		this.mustFromDate = mustFromDate;
	}

	public Date getMustToDate() {
		return mustToDate;
	}

	public void setMustToDate(Date mustToDate) {
		this.mustToDate = mustToDate;
	}

	public String getMustFromTime() {
		return mustFromTime;
	}

	public void setMustFromTime(String mustFromTime) {
		this.mustFromTime = mustFromTime;
	}

	public String getMustToTime() {
		return mustToTime;
	}

	public void setMustToTime(String mustToTime) {
		this.mustToTime = mustToTime;
	}

}