package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;
import java.util.Date;

public class QuotaJudgeCalendar implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long quotaDetailId;
	private Long roomTypeIds;
	private Long bedId;
	private String quotaType;
	private String quotaPattern;
	private String quotaShare;
	private String quotaHolder;
	private Date ableDate;
	private Long blnBack; 
	private Long oldCutoffday;
	private Long cutoffday;
	private String oldCutoffTime;
	private String cutofftime;
	/**
	 * 调整类型
	 */
	private String judgeTypeStr;
	/**
	 * 新配额数
	 */
	private Long quotaNum;
	/**
	 * 原配额数
	 */
	private Long oldQuotaNum;
	 /**
     * 配额可用数
     */
    private Long quotaAvailable;

    /**
     * 配额已用数
     */
    private Long quotaUsed;

	public Date getAbleDate() {
		return ableDate;
	}

	public void setAbleDate(Date ableDate) {
		this.ableDate = ableDate;
	}


	public Long getBedId() {
		return bedId;
	}

	public void setBedId(Long bedId) {
		this.bedId = bedId;
	}


	public Long getBlnBack() {
		return blnBack;
	}

	public void setBlnBack(Long blnBack) {
		this.blnBack = blnBack;
	}

	public String getJudgeTypeStr() {
		return judgeTypeStr;
	}

	public void setJudgeTypeStr(String judgeTypeStr) {
		this.judgeTypeStr = judgeTypeStr;
	}


	public Long getCutoffday() {
		return cutoffday;
	}

	public void setCutoffday(Long cutoffday) {
		this.cutoffday = cutoffday;
	}

	public Long getOldCutoffday() {
		return oldCutoffday;
	}

	public void setOldCutoffday(Long oldCutoffday) {
		this.oldCutoffday = oldCutoffday;
	}

	public String getOldCutoffTime() {
		return oldCutoffTime;
	}

	public void setOldCutoffTime(String oldCutoffTime) {
		this.oldCutoffTime = oldCutoffTime;
	}

	public Long getOldQuotaNum() {
		return oldQuotaNum;
	}

	public void setOldQuotaNum(Long oldQuotaNum) {
		this.oldQuotaNum = oldQuotaNum;
	}

	public Long getQuotaAvailable() {
		return quotaAvailable;
	}

	public void setQuotaAvailable(Long quotaAvailable) {
		this.quotaAvailable = quotaAvailable;
	}

	public Long getQuotaDetailId() {
		return quotaDetailId;
	}

	public void setQuotaDetailId(Long quotaDetailId) {
		this.quotaDetailId = quotaDetailId;
	}

	public String getQuotaHolder() {
		return quotaHolder;
	}

	public void setQuotaHolder(String quotaHolder) {
		this.quotaHolder = quotaHolder;
	}

	public Long getQuotaNum() {
		return quotaNum;
	}

	public void setQuotaNum(Long quotaNum) {
		this.quotaNum = quotaNum;
	}

	public String getQuotaPattern() {
		return quotaPattern;
	}

	public void setQuotaPattern(String quotaPattern) {
		this.quotaPattern = quotaPattern;
	}

	public String getQuotaShare() {
		return quotaShare;
	}

	public void setQuotaShare(String quotaShare) {
		this.quotaShare = quotaShare;
	}

	public String getQuotaType() {
		return quotaType;
	}

	public void setQuotaType(String quotaType) {
		this.quotaType = quotaType;
	}

	public Long getQuotaUsed() {
		return quotaUsed;
	}

	public void setQuotaUsed(Long quotaUsed) {
		this.quotaUsed = quotaUsed;
	}


	public Long getRoomTypeIds() {
		return roomTypeIds;
	}

	public void setRoomTypeIds(Long roomTypeIds) {
		this.roomTypeIds = roomTypeIds;
	}

	public String getCutofftime() {
		return cutofftime;
	}

	public void setCutofftime(String cutofftime) {
		this.cutofftime = cutofftime;
	}
    
    
}
