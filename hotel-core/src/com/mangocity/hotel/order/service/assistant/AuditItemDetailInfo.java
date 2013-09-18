package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;

public class AuditItemDetailInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 日审明细ID
	 */
	private Long aitemid;
	/**
	 * 订单ID
	 */
	private Long orderId;
	/**
	 * 订单类型
	 */
	private int orderType;
	/**
	 * 日审明细备注
	 */
	private String remark;
	
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getAitemid() {
		return aitemid;
	}
	public void setAitemid(Long aitemid) {
		this.aitemid = aitemid;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
}
