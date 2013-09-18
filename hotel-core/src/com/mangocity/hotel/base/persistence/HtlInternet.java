package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 税费设定
 * 
 * @author xiaowumi
 * 
 */
public class HtlInternet implements Entity {

    private Long ID;

    private Long htlInternetID;

    /**
     * 开始日期
     */
    private Date internetBeginDate;

    /**
     * 结束日期
     */
    private Date internetEndDate;

    /**
     * 酒店ID
     */
    private long hotelId;

    /**
     * 房型id
     */
    private long roomTypeId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人ID
     */
    private String createById;

    /**
     * 修改人
     */
    private String modifyBy;

    /**
     * 修改人ID
     */
    private String modifyById;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 是否有效
     */
    private String active;

    private HtlContract htlContract;

    public HtlContract getHtlContract() {
        return htlContract;
    }

    public void setHtlContract(HtlContract htlContract) {
        this.htlContract = htlContract;
    }

    public Long getHtlInternetID() {
        return htlInternetID;
    }

    public void setHtlInternetID(Long htlInternetID) {
        this.htlInternetID = htlInternetID;
    }

    public Date getInternetBeginDate() {
        return internetBeginDate;
    }

    public void setInternetBeginDate(Date internetBeginDate) {
        this.internetBeginDate = internetBeginDate;
    }

    public Date getInternetEndDate() {
        return internetEndDate;
    }

    public void setInternetEndDate(Date internetEndDate) {
        this.internetEndDate = internetEndDate;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getModifyById() {
        return modifyById;
    }

    public void setModifyById(String modifyById) {
        this.modifyById = modifyById;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

}
