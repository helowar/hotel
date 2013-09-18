package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 
 * 纸质日审明细表
 * 
 * @author chenkeming
 */
public class DaPaperFaxItem implements Entity {

	private static final long serialVersionUID = -3143721625702616360L;

	/**
	 * ID <pk> 
	 */
    private Long ID;
    
    /**
	 * auditId <fk>, 和DaDailyaudit关联
	 */
    private DaDailyaudit dailyAudit;
    
    /**
	 * 
	 */
    private Long sendId;

    /**
	 * channelId
	 */
    private Long channelId;

    /**
	 * 传真号码
	 */
    private String fax;
    
    /**
	 * 发送者
	 */
    private String createdBy;
    
    /**
	 * 发送时间
	 */
    private Date createDate;    
    
    /**
	 * 发送状态
	 */
    private int sendState;
    

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public DaDailyaudit getDailyAudit() {
		return dailyAudit;
	}

	public void setDailyAudit(DaDailyaudit dailyAudit) {
		this.dailyAudit = dailyAudit;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public Long getSendId() {
		return sendId;
	}

	public void setSendId(Long sendId) {
		this.sendId = sendId;
	}

	public int getSendState() {
		return sendState;
	}

	public void setSendState(int sendState) {
		this.sendState = sendState;
	}
    
}
