package com.mangocity.hotel.base.persistence;

import java.util.Date;

/**
 * 酒店预定条款模板操作日志明细类
 * 
 * @author lihaibo add by 2009-5-2
 */
public class HtlModifRecord {

    private long Id;

    /**
     * 酒店ID
     */
    private long hotelId;

    /**
     * 价格类型id
     */
    private long pricetypeId;

    /**
     * 每天日期
     */
    private Date roomDate;

    /**
     * 预订的操作明细记录
     */

    private String reservationNewRecord;

    private String reservationOldRecord;

    /**
     * 担保的操作明细记录
     */
    // private String assureRecord;
    private String assureNewRecord;

    private String assureOldRecord;

    private String assureTemplateNew;

    private String assureTemplateOld;

    private String prepayNewRecord;

    private String prepayOldRecord;

    private String prepayTemplateNew;

    private String prepayTemplateOld;

    /**
     * 预付的操作明细记录
     */
    // private String prepayRecord;
    /**
     * 修改人ID
     */
    private String modifId;

    /**
     * 修改时间
     */
    private Date modifDate;

    /**
     * 日志ID
     */
    private long recordId;

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getAssureNewRecord() {
        return assureNewRecord;
    }

    public void setAssureNewRecord(String assureNewRecord) {
        this.assureNewRecord = assureNewRecord;
    }

    public String getAssureOldRecord() {
        return assureOldRecord;
    }

    public void setAssureOldRecord(String assureOldRecord) {
        this.assureOldRecord = assureOldRecord;
    }

    public String getAssureTemplateNew() {
        return assureTemplateNew;
    }

    public void setAssureTemplateNew(String assureTemplateNew) {
        this.assureTemplateNew = assureTemplateNew;
    }

    public String getAssureTemplateOld() {
        return assureTemplateOld;
    }

    public void setAssureTemplateOld(String assureTemplateOld) {
        this.assureTemplateOld = assureTemplateOld;
    }

    public String getPrepayNewRecord() {
        return prepayNewRecord;
    }

    public void setPrepayNewRecord(String prepayNewRecord) {
        this.prepayNewRecord = prepayNewRecord;
    }

    public String getPrepayOldRecord() {
        return prepayOldRecord;
    }

    public void setPrepayOldRecord(String prepayOldRecord) {
        this.prepayOldRecord = prepayOldRecord;
    }

    public String getPrepayTemplateNew() {
        return prepayTemplateNew;
    }

    public void setPrepayTemplateNew(String prepayTemplateNew) {
        this.prepayTemplateNew = prepayTemplateNew;
    }

    public String getPrepayTemplateOld() {
        return prepayTemplateOld;
    }

    public void setPrepayTemplateOld(String prepayTemplateOld) {
        this.prepayTemplateOld = prepayTemplateOld;
    }

    public long getPricetypeId() {
        return pricetypeId;
    }

    public void setPricetypeId(long pricetypeId) {
        this.pricetypeId = pricetypeId;
    }

    public String getReservationNewRecord() {
        return reservationNewRecord;
    }

    public void setReservationNewRecord(String reservationNewRecord) {
        this.reservationNewRecord = reservationNewRecord;
    }

    public String getReservationOldRecord() {
        return reservationOldRecord;
    }

    public void setReservationOldRecord(String reservationOldRecord) {
        this.reservationOldRecord = reservationOldRecord;
    }

    public Date getRoomDate() {
        return roomDate;
    }

    public void setRoomDate(Date roomDate) {
        this.roomDate = roomDate;
    }

    public Date getModifDate() {
        return modifDate;
    }

    public void setModifDate(Date modifDate) {
        this.modifDate = modifDate;
    }

    public String getModifId() {
        return modifId;
    }

    public void setModifId(String modifId) {
        this.modifId = modifId;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

}
