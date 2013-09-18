package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * @author houdiandian
 * @time 2011-11-21 
 */
public class OrPointRecords  implements Entity {

	private Long pointId;//id
	private OrOrder order;//关联的订单
	private Long pointValue;//积分值
	private String operator;//操作者
	private Date operateTime ;//操作时间
	private String notes;//备注
	
	public Long getPointId() {
		return pointId;
	}
	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}
	public OrOrder getOrder() {
		return order;
	}
	public void setOrder(OrOrder order) {
		this.order = order;
	}
	public Long getPointValue() {
		return pointValue;
	}
	public void setPointValue(Long pointValue) {
		this.pointValue = pointValue;
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
	        return pointId;
	    }
	
	
}
