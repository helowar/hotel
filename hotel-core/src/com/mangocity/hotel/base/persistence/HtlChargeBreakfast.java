package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * 记录收费早餐情况的表
 * 
 * @author xiaowumi HtlChargeBreakfast generated by MyEclipse - Hibernate Tools
 */

public class HtlChargeBreakfast extends CEntity implements Entity {

    // Fields

    // 早餐收费id
    private Long ID;

    // 合同id
    private Long contractId;

    // 起始日期
    private Date beginDate;

    // 终止时期
    private Date endDate;

    // 早餐类型
    private List breakfastFees = new ArrayList();

    /**
     * 支付类型
     */
    private String payMethod;

    /**
     * 备注
     */
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

    //

    // Constructors

    /** default constructor */
    public HtlChargeBreakfast() {
    }

    // Property accessors

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
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

    public List getBreakfastFees() {
        return breakfastFees;
    }

    public void setBreakfastFees(List breakfastFees) {
        this.breakfastFees = breakfastFees;
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

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}