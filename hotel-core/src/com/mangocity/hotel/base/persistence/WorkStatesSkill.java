package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

public class WorkStatesSkill implements Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9071225844358821473L;
	
	private Long ID;
	private String logonId;
	private Long groupId;
	private String clientType;
	private String cooperator;
	private String orderType;
	private String orderArea;
	private int count;
	private Date createTime;
	private String creator;

	private String orderSource;// ¶©µ¥À´Ô´ add by diandian.hou 2012-7-31
	
	public WorkStatesSkill() {
	}




	public WorkStatesSkill(Long id, String logonId, Long groupId, String clientType, String cooperator, String orderType, String orderArea, int count, Date createTime, String creator) {
		ID = id;
		this.logonId = logonId;
		this.groupId = groupId;
		this.clientType = clientType;
		this.cooperator = cooperator;
		this.orderType = orderType;
		this.orderArea = orderArea;
		this.count = count;
		this.createTime = createTime;
		this.creator = creator;
	}









	public String getLogonId() {
		return logonId;
	}









	public void setLogonId(String logonId) {
		this.logonId = logonId;
	}









	public String getClientType() {
		return clientType;
	}


	public void setClientType(String clientType) {
		this.clientType = clientType;
	}


	public String getCooperator() {
		return cooperator;
	}


	public void setCooperator(String cooperator) {
		this.cooperator = cooperator;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getCreator() {
		return creator;
	}


	public void setCreator(String creator) {
		this.creator = creator;
	}


	public Long getGroupId() {
		return groupId;
	}


	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}


	public String getOrderArea() {
		return orderArea;
	}


	public void setOrderArea(String orderArea) {
		this.orderArea = orderArea;
	}


	public String getOrderType() {
		return orderType;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}


	public void setID(Long id) {
		ID = id;
	}


	public Long getID() {
		return this.ID;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

}
