package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 
 * 纸质日审明细表
 * 
 * @author chenkeming
 */
public class OrPaperDailyAuditItem implements Entity {

    private static final long serialVersionUID = 2821516713717124681L;

    /**
	 * ID <pk> 
	 */
    private Long ID;
    
    /**
	 * auditId <fk>, 和OrPaperDailyAudit关联
	 */
    private OrPaperDailyAudit paperAudit;
    
    /**
	 * 
	 */
    private Long sendId;

    /**
	 * 酒店ID
	 */
    private Long hotelId;

    /**
	 * 日审明细id列表
	 */
    private String auditIds;

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
    
    /**
	 * 发送后返回id
	 */
    private String faxId;

    public String getAuditIds() {
        return auditIds;
    }

    public void setAuditIds(String auditIds) {
        this.auditIds = auditIds;
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

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFaxId() {
        return faxId;
    }

    public void setFaxId(String faxId) {
        this.faxId = faxId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public OrPaperDailyAudit getPaperAudit() {
        return paperAudit;
    }

    public void setPaperAudit(OrPaperDailyAudit paperAudit) {
        this.paperAudit = paperAudit;
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
