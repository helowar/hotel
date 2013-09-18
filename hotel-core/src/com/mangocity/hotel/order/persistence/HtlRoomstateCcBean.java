package com.mangocity.hotel.order.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import com.mangocity.util.Entity;

/**
 * AbstractHtlRoomstateCc generated by MyEclipse Persistence Tools
 */

public  class HtlRoomstateCcBean implements Entity {

    // Fields
    private Long ID;
    private Long roomstateccid;

    private Long hotelid;

    private Date begindate;

    private Date enddate;

    private String inform;

    private String operatedept;

    private String processby;

    private String processbyid;

    private Date processdatetime;

    private Date processdate;

    private String reviewdept;

    private String reviewname;

    private Long reviewid;

    private String reviewremark;

    private String reviewstate;

    private String remark;
    
    private Set htlRoomstateCcBeds = new HashSet(0);

    private List roomTypeStatus = new ArrayList();
    
    // Property accessors

    public Long getRoomstateccid() {
        return this.roomstateccid;
    }

    public void setRoomstateccid(Long roomstateccid) {
        this.roomstateccid = roomstateccid;
    }

    public Long getHotelid() {
        return this.hotelid;
    }

    public void setHotelid(Long hotelid) {
        this.hotelid = hotelid;
    }

    public Date getBegindate() {
        return this.begindate;
    }

    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public Date getEnddate() {
        return this.enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getInform() {
        return this.inform;
    }

    public void setInform(String inform) {
        this.inform = inform;
    }

    public String getOperatedept() {
        return this.operatedept;
    }

    public void setOperatedept(String operatedept) {
        this.operatedept = operatedept;
    }

    public String getProcessby() {
        return this.processby;
    }

    public void setProcessby(String processby) {
        this.processby = processby;
    }


    public Date getProcessdatetime() {
        return this.processdatetime;
    }

    public void setProcessdatetime(Date processdatetime) {
        this.processdatetime = processdatetime;
    }

    public Date getProcessdate() {
        return this.processdate;
    }

    public void setProcessdate(Date processdate) {
        this.processdate = processdate;
    }

    public String getReviewdept() {
        return this.reviewdept;
    }

    public void setReviewdept(String reviewdept) {
        this.reviewdept = reviewdept;
    }

    public String getReviewname() {
        return this.reviewname;
    }

    public void setReviewname(String reviewname) {
        this.reviewname = reviewname;
    }

    public Long getReviewid() {
        return this.reviewid;
    }

    public void setReviewid(Long reviewid) {
        this.reviewid = reviewid;
    }

    public String getReviewremark() {
        return this.reviewremark;
    }

    public void setReviewremark(String reviewremark) {
        this.reviewremark = reviewremark;
    }

    public String getReviewstate() {
        return this.reviewstate;
    }

    public void setReviewstate(String reviewstate) {
        this.reviewstate = reviewstate;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Set getHtlRoomstateCcBeds() {
        return htlRoomstateCcBeds;
    }

    public void setHtlRoomstateCcBeds(Set htlRoomstateCcBeds) {
        this.htlRoomstateCcBeds = htlRoomstateCcBeds;
    }

    public List getRoomTypeStatus() {
        return roomTypeStatus;
    }

    public void setRoomTypeStatus(List roomTypeStatus) {
        this.roomTypeStatus = roomTypeStatus;
    }

    public String getProcessbyid() {
        return processbyid;
    }

    public void setProcessbyid(String processbyid) {
        this.processbyid = processbyid;
    }

}