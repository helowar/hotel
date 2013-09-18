package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

public class ShadowQuota implements Entity{

	private static final long serialVersionUID = 1L;
	/**
	 * 配额账号ID
	 */
	private Long quotaAccountID;  
		
	/**
	 * 售卖日期
	 */
	private Date saleDate;                 
	
	/**
	 * cutoffDay
	 */
	private Integer cutoffDay;             
	
	/**
	 * cutoffTime
	 */
	private String cutoffTime;             
	
	/**
	 * 配额总数
	 */
	private Integer quotaTotalNum;         
	
	/**
	 * 配额已用数
	 */
	private Integer quotaUsedNum;
	
	/**
	 * 配额剩余数
	 */
	private Integer quotaOverNum;
	
	/**
	 * 配额状态
	 */
	private Integer quotaState = 1;         
		
	/**
	 * 配额是否可以透支
	 */
	private Integer overDraft = 1;      
	
	/**
	 * 透支额度
	 */
	private Integer overDraftNum ;
	
	/**
	 * 是否过期
	 */
	private boolean overdue;
	
	/**
	 * 是否包含配额对象
	 */
	private boolean containQuota;
	
	/**
	 * 是否包含配额状态对象
	 */
	private boolean containQuotaState;
	
	/**
	 * 需要扣除的配额
	 */
	private Integer deleteQuotaNum;
	
	public Long getID() {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getQuotaAccountID() {
		return quotaAccountID;
	}

	public void setQuotaAccountID(Long quotaAccountID) {
		this.quotaAccountID = quotaAccountID;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public Integer getCutoffDay() {
		return cutoffDay;
	}

	public void setCutoffDay(Integer cutoffDay) {
		this.cutoffDay = cutoffDay;
	}

	public String getCutoffTime() {
		return cutoffTime;
	}

	public void setCutoffTime(String cutoffTime) {
		this.cutoffTime = cutoffTime;
	}

	public Integer getQuotaTotalNum() {
		return quotaTotalNum;
	}

	public void setQuotaTotalNum(Integer quotaTotalNum) {
		this.quotaTotalNum = quotaTotalNum;
	}

	public Integer getQuotaUsedNum() {
		return quotaUsedNum;
	}

	public void setQuotaUsedNum(Integer quotaUsedNum) {
		this.quotaUsedNum = quotaUsedNum;
	}

	public Integer getQuotaOverNum() {
		return quotaOverNum;
	}

	public void setQuotaOverNum(Integer quotaOverNum) {
		this.quotaOverNum = quotaOverNum;
	}

	public Integer getQuotaState() {
		return quotaState;
	}

	public void setQuotaState(Integer quotaState) {
		this.quotaState = quotaState;
	}

	public Integer getOverDraft() {
		return overDraft;
	}

	public void setOverDraft(Integer overDraft) {
		this.overDraft = overDraft;
	}

	public Integer getOverDraftNum() {
		return overDraftNum;
	}

	public void setOverDraftNum(Integer overDraftNum) {
		this.overDraftNum = overDraftNum;
	}

	public boolean isOverdue() {
		return overdue;
	}

	public void setOverdue(boolean overdue) {
		this.overdue = overdue;
	}

	public boolean isContainQuota() {
		return containQuota;
	}

	public void setContainQuota(boolean containQuota) {
		this.containQuota = containQuota;
	}

	public boolean isContainQuotaState() {
		return containQuotaState;
	}

	public void setContainQuotaState(boolean containQuotaState) {
		this.containQuotaState = containQuotaState;
	}

	public Integer getDeleteQuotaNum() {
		return deleteQuotaNum;
	}

	public void setDeleteQuotaNum(Integer deleteQuotaNum) {
		this.deleteQuotaNum = deleteQuotaNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



}
