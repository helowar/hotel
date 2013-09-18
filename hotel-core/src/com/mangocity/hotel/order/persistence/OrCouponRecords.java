package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 订单使用的代金券记录
 * @author chenjiajie
 *
 */
public class OrCouponRecords implements Entity {

    /**
	 * 
	 */
    private static final long serialVersionUID = -796390154170405040L;

    /**
	 * 代金券记录主键
	 */
    private Long couponId;
    
    /**
	 * 订单id
	 */
    private OrOrder order;
    
    /**
	 * 代金券代码
	 */
    private String couponCode;
    
    /**
	 * 代金券名
	 */
    private String couponName;
    
    /**
	 * 代金券价值
	 */
    private Float couponValue;
    
    /**
	 * 代金券有效开始日期
	 */
    private Date couponBeginDate;
    
    /**
	 * 代金券有效结束日期
	 */
    private Date couponEndDate;
    
    /**
	 * 代金券描述
	 */
    private String description;
    
    /**
	 * 是否支付成功
	 */
    private Boolean paysucceed;
    
    /**
	 * 币种
	 */
    private String currencyType;
    
    /**
	 * 最后操作人
	 */
    private String operator;
    
    /**
	 * 最后操作时间
	 */
    private Date operateTime;
    
    /**
	 * 备注
	 */
    private String notes;
    
    /** getter and setter **/
    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public OrOrder getOrder() {
        return order;
    }

    public void setOrder(OrOrder order) {
        this.order = order;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Float getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(Float couponValue) {
        this.couponValue = couponValue;
    }

    public Date getCouponBeginDate() {
        return couponBeginDate;
    }

    public void setCouponBeginDate(Date couponBeginDate) {
        this.couponBeginDate = couponBeginDate;
    }

    public Date getCouponEndDate() {
        return couponEndDate;
    }

    public void setCouponEndDate(Date couponEndDate) {
        this.couponEndDate = couponEndDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPaysucceed() {
        return paysucceed;
    }

    public void setPaysucceed(Boolean paysucceed) {
        this.paysucceed = paysucceed;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getID() {
        return couponId;
    }

}
