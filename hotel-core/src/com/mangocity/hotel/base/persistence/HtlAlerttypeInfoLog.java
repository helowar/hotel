package com.mangocity.hotel.base.persistence;

import java.util.Date;

/**
 * hotel 2.9.2 提示信息日志表 add by shengwei.zuo 2009-08-06
 * 
 */

public class HtlAlerttypeInfoLog implements java.io.Serializable {

    // 日志ID
    private Long id;

    // 提示信息ID
    private Long alerttypeId;

    // 酒店ID
    private Long hotelId;

    // 合同ID
    private Long contractId;

    // 价格类型ID
    private String priceTypeId;

    // 开始日期
    private Date beginDate;

    // 结束日期
    private Date endDate;

    // 星期
    private String week;

    // 可见位置
    private String localFlag;

    // 提示信息内容
    private String alerttypeInfo;

    // 创建者ID
    private String createById;

    // 创建者名称
    private String createBy;

    // 创建时间
    private Date createTime;

    // 修改人ID
    private String modifyById;

    // 修改人名称
    private String modifyBy;

    // 修改时间
    private Date modifyTime;

    // 价格类型名称
    private String priceTypeName;

    // 操作标记
    private String operateFlag;

    // Constructors

    /** default constructor */
    public HtlAlerttypeInfoLog() {
    }

    /** minimal constructor */
    public HtlAlerttypeInfoLog(Long hotelId, Long contractId, String priceTypeId, Date beginDate,
        Date endDate, String alerttypeInfo) {
        this.hotelId = hotelId;
        this.contractId = contractId;
        this.priceTypeId = priceTypeId;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.alerttypeInfo = alerttypeInfo;
    }

    /** full constructor */
    public HtlAlerttypeInfoLog(Long alerttypeId, Long hotelId, Long contractId, String priceTypeId,
        Date beginDate, Date endDate, String week, String localFlag, String alerttypeInfo,
        String createById, String createBy, Date createTime, String modifyById, String modifyBy,
        Date modifyTime, String priceTypeName, String operateFlag) {
        this.alerttypeId = alerttypeId;
        this.hotelId = hotelId;
        this.contractId = contractId;
        this.priceTypeId = priceTypeId;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.week = week;
        this.localFlag = localFlag;
        this.alerttypeInfo = alerttypeInfo;
        this.createById = createById;
        this.createBy = createBy;
        this.createTime = createTime;
        this.modifyById = modifyById;
        this.modifyBy = modifyBy;
        this.modifyTime = modifyTime;
        this.priceTypeName = priceTypeName;
        this.operateFlag = operateFlag;
    }

    // Property accessors

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlerttypeId() {
        return this.alerttypeId;
    }

    public void setAlerttypeId(Long alerttypeId) {
        this.alerttypeId = alerttypeId;
    }

    public Long getHotelId() {
        return this.hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getContractId() {
        return this.contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getPriceTypeId() {
        return this.priceTypeId;
    }

    public void setPriceTypeId(String priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public Date getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getWeek() {
        return this.week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getLocalFlag() {
        return this.localFlag;
    }

    public void setLocalFlag(String localFlag) {
        this.localFlag = localFlag;
    }

    public String getAlerttypeInfo() {
        return this.alerttypeInfo;
    }

    public void setAlerttypeInfo(String alerttypeInfo) {
        this.alerttypeInfo = alerttypeInfo;
    }

    public String getCreateById() {
        return this.createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyById() {
        return this.modifyById;
    }

    public void setModifyById(String modifyById) {
        this.modifyById = modifyById;
    }

    public String getModifyBy() {
        return this.modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getPriceTypeName() {
        return this.priceTypeName;
    }

    public void setPriceTypeName(String priceTypeName) {
        this.priceTypeName = priceTypeName;
    }

    public String getOperateFlag() {
        return this.operateFlag;
    }

    public void setOperateFlag(String operateFlag) {
        this.operateFlag = operateFlag;
    }

}