package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;


/**
 * 订单的担保明细表
 * @author chenkeming Feb 18, 2009 11:19:16 AM
 */
public class OrGuaranteeItem implements Entity {

    private static final long serialVersionUID = -3681115578463854751L;

    /**
	 * 主键
	 */
    private Long ID;
    
    //日期
    private Date night;

    private String notConditional;// 是否无条件 1:是无条件担保 

    private String latestAssureTime; //超时

    private int overRoomNumber = -1; //超房数
    
    private Integer overNightsNumber = -1; //超间夜数
    
    //担保类型  2:首日担保, 4:全额担保
    private String assureType;
    
    //是否要担保函
    private String assureLetter;
    
    //担保条件
    private String assureCondiction;
    
    private OrReservation reserv;

    public String getAssureLetter() {
        return assureLetter;
    }

    public void setAssureLetter(String assureLetter) {
        this.assureLetter = assureLetter;
    }

    public String getAssureType() {
        return assureType;
    }

    public void setAssureType(String assureType) {
        this.assureType = assureType;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getLatestAssureTime() {
        return latestAssureTime;
    }

    public void setLatestAssureTime(String latestAssureTime) {
        this.latestAssureTime = latestAssureTime;
    }

    public Date getNight() {
        return night;
    }

    public void setNight(Date night) {
        this.night = night;
    }

    public String getNotConditional() {
        return notConditional;
    }

    public void setNotConditional(String notConditional) {
        this.notConditional = notConditional;
    }

    public int getOverRoomNumber() {
        return overRoomNumber;
    }

    public void setOverRoomNumber(int overRoomNumber) {
        this.overRoomNumber = overRoomNumber;
    }

    public OrReservation getReserv() {
        return reserv;
    }

    public void setReserv(OrReservation reserv) {
        this.reserv = reserv;
    }

    public String getAssureCondiction() {
        return assureCondiction;
    }

    public void setAssureCondiction(String assureCondiction) {
        this.assureCondiction = assureCondiction;
    }

    public Integer getOverNightsNumber() {
        return overNightsNumber;
    }

    public void setOverNightsNumber(Integer overNightsNumber) {
        this.overNightsNumber = overNightsNumber;
    }


}