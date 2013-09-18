package com.mangocity.hagtb2b.persistence;

import java.util.List;


public class StatisticsInfo {
	
	private long   ID;
	
	private String statYear;
	
	private int statMonth;
	
	private String orgId;
	
	private String agentCode;
	
	private String agentName;
	
	private String operId;
	
	private int orderNum;
	
	private int nightsNum;
	
	private double sumAcount;
	
	private int actNightsNum;
	
	private double actSumAcount;
	
	private double commsion;
	private double backCommission;
	
	private double commrate;
	private double factcomm;
	
	//是否确认 1表示确认 2表示未确认
	private int confirmed;
	
	private List<AgentOrder> orderItems;
	
	public int getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(int confirmed) {
		this.confirmed = confirmed;
	}
	public long getID() {
		return ID;
	}
	public void setID(long id) {
		ID = id;
	}
	public String getStatYear() {
		return statYear;
	}
	public void setStatYear(String statYear) {
		this.statYear = statYear;
	}
	public int getStatMonth() {
		return statMonth;
	}
	public void setStatMonth(int statMonth) {
		this.statMonth = statMonth;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getOperId() {
		return operId;
	}
	public void setOperId(String operId) {
		this.operId = operId;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public int getNightsNum() {
		return nightsNum;
	}
	public void setNightsNum(int nightsNum) {
		this.nightsNum = nightsNum;
	}
	public double getSumAcount() {
		return sumAcount;
	}
	public void setSumAcount(double sumAcount) {
		this.sumAcount = sumAcount;
	}
	public int getActNightsNum() {
		return actNightsNum;
	}
	public void setActNightsNum(int actNightsNum) {
		this.actNightsNum = actNightsNum;
	}
	public double getActSumAcount() {
		return actSumAcount;
	}
	public void setActSumAcount(double actSumAcount) {
		this.actSumAcount = actSumAcount;
	}
	public double getCommsion() {
		return commsion;
	}
	public void setCommsion(double commsion) {
		this.commsion = commsion;
	}
	public List<AgentOrder> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<AgentOrder> orderItems) {
		this.orderItems = orderItems;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public double getBackCommission() {
		return backCommission;
	}
	public void setBackCommission(double backCommission) {
		this.backCommission = backCommission;
	}
	public double getCommrate() {
		return commrate;
	}
	public void setCommrate(double commrate) {
		this.commrate = commrate;
	}
	public double getFactcomm() {
		return factcomm;
	}
	public void setFactcomm(double factcomm) {
		this.factcomm = factcomm;
	}
	
}
