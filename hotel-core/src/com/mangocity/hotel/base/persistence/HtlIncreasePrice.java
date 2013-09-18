package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 香港中科加幅设置
 * 
 * @author wuyun 2009-04-18
 * 
 */
public class HtlIncreasePrice extends CEntity implements Entity {

    /**
     * ID
     */
    private Long ID;

    /**
     * 酒店 hotel 2.9.2 中旅加幅需要绑定酒店 解开注释 modify by chenjiajie 2009-08-03
     */
    private Long hotelId;

    /**
     * 渠道类型
     */
    private String channelType;

    /**
     * 大于金额
     */
    private double greaterThan;

    /**
     * 小于等于金额
     */
    private double smallerThan;

    /**
     * 金额或百分比
     */
    private double amount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人工号
     */
    private String createById;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人
     */
    private String modifyBy;

    /**
     * 修改人工号
     */
    private String modifyById;

    /**
     * 酒店
     */
    private HtlHotel htlHotel;

    /**
     * 加幅类型 1为金额，2为百分比
     * 
     * @return
     */
    private String increaseType;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public double getGreaterThan() {
        return greaterThan;
    }

    public void setGreaterThan(double greaterThan) {
        this.greaterThan = greaterThan;
    }

    public double getSmallerThan() {
        return smallerThan;
    }

    public void setSmallerThan(double smallerThan) {
        this.smallerThan = smallerThan;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
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

    public void setID(Long id) {
        ID = id;
    }

    public Long getID() {
        return ID;
    }

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    public String getIncreaseType() {
        return increaseType;
    }

    public void setIncreaseType(String increaseType) {
        this.increaseType = increaseType;
    }

}
