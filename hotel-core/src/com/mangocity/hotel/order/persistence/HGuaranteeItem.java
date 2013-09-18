package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.HEntity;


/**
 * 历史订单的担保明细表
 * @author chenkeming Feb 18, 2009 11:19:16 AM
 */
public class HGuaranteeItem implements HEntity {

    private static final long serialVersionUID = 1710802877839904259L;

    /**
	 * 主键
	 */
    private Long hisID;
    
    private Date night;

    private String notConditional;

    private String latestAssureTime;

    private int overRoomNumber = -1;
    
    private String assureType;
    
    private String assureLetter;
    
    //担保条件
    private String assureCondiction;
    
    private HReservation reservH;

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

    public Long getHisID() {
        return hisID;
    }

    public void setHisID(Long hisID) {
        this.hisID = hisID;
    }

    public HReservation getReservH() {
        return reservH;
    }

    public void setReservH(HReservation reservH) {
        this.reservH = reservH;
    }

    public String getAssureCondiction() {
        return assureCondiction;
    }

    public void setAssureCondiction(String assureCondiction) {
        this.assureCondiction = assureCondiction;
    }

}