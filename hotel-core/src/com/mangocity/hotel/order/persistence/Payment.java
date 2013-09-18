/**
 * 
 *  支付记录
 */
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class Payment implements Entity {

    // ID <pk>
    private Long ID;

    // 订单ID <fk> 和Order关联
    private Order order;

    /**
	 * 表示此记录是退款，还是支付
	 */
    private String fund;

    // 付/退款金额/现金券/积分兑换金额
    private double fundMoney;

    // 付款交易号
    private String dealingNo;

    /**
	 * 支付或退款的金额类型，网上支付、现金、积分、信用卡、POS机等
	 */ 
    private String fundType;
    
    /**
	 * 常量状态， PayState，已付或未付，如果是退款，表示已退或未退
	 */
    private String state;

    // 信用卡号
    private String creditcard;

    // 收/退款人ID
    private String funderId;
    
    
    
    // 收/退款人名称
    private String funderName;

    // 收/退款时间
    private Date fundDate;

    // 退款原因
    private String refundment;

    public String getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(String creditcard) {
        this.creditcard = creditcard;
    }

    public String getDealingNo() {
        return dealingNo;
    }

    public void setDealingNo(String dealingNo) {
        this.dealingNo = dealingNo;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public Date getFundDate() {
        return fundDate;
    }

    public void setFundDate(Date fundDate) {
        this.fundDate = fundDate;
    }

    public String getFunderId() {
        return funderId;
    }

    public void setFunderId(String funderId) {
        this.funderId = funderId;
    }

    public String getFunderName() {
        return funderName;
    }

    public void setFunderName(String funderName) {
        this.funderName = funderName;
    }

    public double getFundMoney() {
        return fundMoney;
    }

    public void setFundMoney(double fundMoney) {
        this.fundMoney = fundMoney;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long payId) {
        this.ID = payId;
    }

    public String getRefundment() {
        return refundment;
    }

    public void setRefundment(String refundment) {
        this.refundment = refundment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
