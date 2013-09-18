package com.mangocity.hotel.base.persistence;

import java.util.Date;

/**
 * hotel 2.9.2 提示信息表 add by shengwei.zuo 2009-08-06
 * 
 */

public class HtlAlerttypeInfo implements java.io.Serializable {

    // Fields

    // Id
    private Long id;
    
    // Id
    private String ids;

    // 酒店ID
    private Long hotelId;

    // 合同ID
    private Long contractId;

    // 价格类型ID
    private String priceTypeId;

    // 价格类型名称
    private String priceTypeName;

    // 开始日期
    private Date beginDate;

    // 结束日期
    private Date endDate;

    // 星期
    private String week;

    // 可见位置标记(1:CC;2:网站;)
    private String localFlag;

    // 提示信息内容
    private String alerttypeInfo;

    // 创建人ID
    private String createById;

    // 创建人名称
    private String createBy;

    // 创建时间
    private Date createTime;

    // 修改人ID
    private String modifyById;

    // 修改人名称
    private String modifyBy;

    // 修改时间
    private Date modifyTime;

    // 辅助的开始日期
    private Date start;

    // 辅助的结束日期
    private Date end;

    // Constructors

    /** default constructor */
    public HtlAlerttypeInfo() {
    }

    // Property accessors

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getPriceTypeId() {
        return priceTypeId;
    }

    public void setPriceTypeId(String priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public String getPriceTypeName() {
        return priceTypeName;
    }

    public void setPriceTypeName(String priceTypeName) {
        this.priceTypeName = priceTypeName;
    }

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}