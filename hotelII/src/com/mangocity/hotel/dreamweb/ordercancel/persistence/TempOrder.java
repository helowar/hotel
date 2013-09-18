package com.mangocity.hotel.dreamweb.ordercancel.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

public class TempOrder implements Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -966594343332651188L;
	
	private Long ID;
	
	private Date assignTime;
	
	private Long groupType;
	
	private Long assignState;
	
	private Integer relaxTime;
	
	private Long clientType;
	
	private String bakA;
	
	private String bakB;
	
	private String bakC;
	
	private String bakD;
	
	private Long deferTime;
	
	private Date modifyTime;

	/**
	 * @return the iD
	 */
	public Long getID() {
		return ID;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(Long iD) {
		ID = iD;
	}

	/**
	 * @return the assignTime
	 */
	public Date getAssignTime() {
		return assignTime;
	}

	/**
	 * @param assignTime the assignTime to set
	 */
	public void setAssignTime(Date assignTime) {
		this.assignTime = assignTime;
	}

	/**
	 * @return the groupType
	 */
	public Long getGroupType() {
		return groupType;
	}

	/**
	 * @param groupType the groupType to set
	 */
	public void setGroupType(Long groupType) {
		this.groupType = groupType;
	}

	/**
	 * @return the assignState
	 */
	public Long getAssignState() {
		return assignState;
	}

	/**
	 * @param assignState the assignState to set
	 */
	public void setAssignState(Long assignState) {
		this.assignState = assignState;
	}

	/**
	 * @return the relaxTime
	 */
	public Integer getRelaxTime() {
		return relaxTime;
	}

	/**
	 * @param relaxTime the relaxTime to set
	 */
	public void setRelaxTime(Integer relaxTime) {
		this.relaxTime = relaxTime;
	}

	/**
	 * @return the clientType
	 */
	public Long getClientType() {
		return clientType;
	}

	/**
	 * @param clientType the clientType to set
	 */
	public void setClientType(Long clientType) {
		this.clientType = clientType;
	}

	/**
	 * @return the bakA
	 */
	public String getBakA() {
		return bakA;
	}

	/**
	 * @param bakA the bakA to set
	 */
	public void setBakA(String bakA) {
		this.bakA = bakA;
	}

	/**
	 * @return the bakB
	 */
	public String getBakB() {
		return bakB;
	}

	/**
	 * @param bakB the bakB to set
	 */
	public void setBakB(String bakB) {
		this.bakB = bakB;
	}

	/**
	 * @return the bakC
	 */
	public String getBakC() {
		return bakC;
	}

	/**
	 * @param bakC the bakC to set
	 */
	public void setBakC(String bakC) {
		this.bakC = bakC;
	}

	/**
	 * @return the bakD
	 */
	public String getBakD() {
		return bakD;
	}

	/**
	 * @param bakD the bakD to set
	 */
	public void setBakD(String bakD) {
		this.bakD = bakD;
	}

	/**
	 * @return the deferTime
	 */
	public Long getDeferTime() {
		return deferTime;
	}

	/**
	 * @param deferTime the deferTime to set
	 */
	public void setDeferTime(Long deferTime) {
		this.deferTime = deferTime;
	}

	/**
	 * @return the modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime the modifyTime to set
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	



}
