package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * HtlAddBedPrice generated by MyEclipse - Hibernate Tools
 */

public class HtlAddBedPrice extends CEntity implements Entity {

    // Fields

    // 加床价格id
    private Long ID;

    // 合同id
    private long contractId;

    // 起始日期
    private Date beginDate;

    // 终止时期
    private Date endDate;

    // 房型
    private String roomType;

    // 加床价
    private Double addPrice;

    // 加床价类型
    private Long addPriceType;

    private String remark;

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
    /**
     * 支付方式
     */
    private String payMethod;

    // Constructors

    /** default constructor */
    public HtlAddBedPrice() {
    }

    // Property accessors
    public Double getAddPrice() {
        return addPrice;
    }

    public void setAddPrice(Double addPrice) {
        this.addPrice = addPrice;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Long getAddPriceType() {
        return addPriceType;
    }

    public void setAddPriceType(Long addPriceType) {
        this.addPriceType = addPriceType;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

}