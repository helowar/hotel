package com.mangocity.hagtb2b.persistence;

public class HtlB2bOrderIncrease {
	/**
	 * 主键ID
	 */
	private Long ID;
	/**
	 *增幅规则
	 */
	private double increaseRule;
	/**
	 * 备注字段
	 */
	private String orderRemark;
	/**
	 * 0:稍后支付 1:立即支付
	 */
	private int payTimeType;
	
	private String orderCD;
	
	private int isMinPrice;

	/**
	 * 订单Id
	 */
	private Long orderId;
	
	/**
	 * 0:未撤单 1：已撤单
	 */
	private int isCancelForOutOfTime;

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}
	public String getOrderCD() {
		return orderCD;
	}
	
	public void setOrderCD(String orderCD) {
		this.orderCD = orderCD;
	}
	public int getPayTimeType() {
		return payTimeType;
	}
	
	public void setPayTimeType(int payTimeType) {
		this.payTimeType = payTimeType;
	}

	public double getIncreaseRule() {
		return increaseRule;
	}

	public void setIncreaseRule(double increaseRule) {
		this.increaseRule = increaseRule;
	}

	public String getOrderRemark() {
		return orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	public int getIsCancelForOutOfTime() {
		return isCancelForOutOfTime;
	}

	public void setIsCancelForOutOfTime(int isCancelForOutOfTime) {
		this.isCancelForOutOfTime = isCancelForOutOfTime;
	}

	public int getIsMinPrice() {
		return isMinPrice;
	}

	public void setIsMinPrice(int isMinPrice) {
		this.isMinPrice = isMinPrice;
	}
	
	
}
