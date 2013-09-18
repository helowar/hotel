package com.mangocity.hagtb2b.persistence;

import java.io.Serializable;
import java.util.Date;

import com.mangocity.util.Entity;


/**
 * 
 * @author zengyong
 * Mar 11, 2010,5:33:41 PM
 *描述:第二套佣金政策适用范围设置
 */
public class CommPolicySecond implements Serializable {
	/**
	 * ID
	 */
	private Long ID;
	
	/**
	 * 代理商code
	 */
	private String agentCode;
	/**
	 * 是否可用
	 */
	private int active;
	/**
	 * 间夜量,如为100
	 */
	private long nightRoomNum;
	/**
	 * 小于等于nightRoomNum 佣金设置为comm1 如：20
	 */
	private double comm1;
	/*
	 * 大于nightRoomNum 佣金设置为comm1 如：25
	 */
	private double comm2;
	public Long getID() {
		return ID;
	}
	public void setID(Long id) {
		ID = id;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public long getNightRoomNum() {
		return nightRoomNum;
	}
	public void setNightRoomNum(long nightRoomNum) {
		this.nightRoomNum = nightRoomNum;
	}
	public double getComm1() {
		return comm1;
	}
	public void setComm1(double comm1) {
		this.comm1 = comm1;
	}
	public double getComm2() {
		return comm2;
	}
	public void setComm2(double comm2) {
		this.comm2 = comm2;
	}
	
	
}
