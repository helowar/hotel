package com.mangocity.hagtb2b.assisbean;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hagtb2b.persistence.AgentOrderItem;
import com.mangocity.hagtb2b.persistence.AgentOrderItemStatis;


public class AgentOrderStatis {
	
	private int   actNights;
	private double commisionPrice;
	private double commisionRate;
	
	private double actSumRmb;
	
	private double commision;
	
	private double commision_old;//阶梯奖励之前的佣金
	
	List<AgentOrderItem> agentOrderitemStatis = new ArrayList();
	public int getActNights() {
		return actNights;
	}
	public void setActNights(int actNights) {
		this.actNights = actNights;
	}
	public double getActSumRmb() {
		return actSumRmb;
	}
	public void setActSumRmb(double actSumRmb) {
		this.actSumRmb = actSumRmb;
	}
	public double getCommision() {
		return commision;
	}
	public void setCommision(double commision) {
		this.commision = commision;
	}
	public double getCommision_old() {
		return commision_old;
	}
	public void setCommision_old(double commision_old) {
		this.commision_old = commision_old;
	}
	public List<AgentOrderItem> getAgentOrderitemStatis() {
		return agentOrderitemStatis;
	}
	public void setAgentOrderitemStatis(List<AgentOrderItem> agentOrderitemStatis) {
		this.agentOrderitemStatis = agentOrderitemStatis;
	}
	public double getCommisionPrice() {
		return commisionPrice;
	}
	public void setCommisionPrice(double commisionPrice) {
		this.commisionPrice = commisionPrice;
	}
	public double getCommisionRate() {
		return commisionRate;
	}
	public void setCommisionRate(double commisionRate) {
		this.commisionRate = commisionRate;
	}
	
	
}
