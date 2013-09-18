package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class HtlRoomStatusProcess extends CEntity implements Entity {

    private Long ID;

    /**
     * 酒店ID
     */
    private long hotelId;

    /**
     * 处理时间
     */
    private Date processDatetime;

    /**
     * 处理日期
     */
    private Date processDate;

    /**
     * 处理人
     */
    private String processBy;

    /**
     * 处理人ID
     */
    private String processById;

    /**
     * 交接事项
     */
    private String processRemark;

    private Long isRoomStatusReport;

    public String getProcessRemark() {
        return processRemark;
    }

    public void setProcessRemark(String processRemark) {
        this.processRemark = processRemark;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getProcessBy() {
        return processBy;
    }

    public void setProcessBy(String processBy) {
        this.processBy = processBy;
    }

    public String getProcessById() {
        return processById;
    }

    public void setProcessById(String processById) {
        this.processById = processById;
    }

    public Date getProcessDatetime() {
        return processDatetime;
    }

    public void setProcessDatetime(Date processDatetime) {
        this.processDatetime = processDatetime;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public Long getIsRoomStatusReport() {
        return isRoomStatusReport;
    }

    public void setIsRoomStatusReport(Long isRoomStatusReport) {
        this.isRoomStatusReport = isRoomStatusReport;
    }

}
