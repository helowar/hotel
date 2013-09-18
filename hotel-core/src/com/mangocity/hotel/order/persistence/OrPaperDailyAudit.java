package com.mangocity.hotel.order.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * 
 * 纸质日审表
 * 
 * @author chenkeming
 */
public class OrPaperDailyAudit implements Entity {

    private static final long serialVersionUID = -1036158151774038647L;

    /**
	 * ID <pk> 
	 */
    private Long ID;

    /**
	 * 酒店ID
	 */
    private Long hotelId;

    /**
	 * 日审时间
	 */
    private Date checkTime;
    
    /**
	 * 日审表ID
	 */
    private Long auditId;
    
    /**
	 * 传真号码
	 */
    private String fax;
    
    /**
	 * 传真成功次数
	 */
    private int successCount;
    
    /**
	 * 传真失败次数
	 */
    private int lostCount;
    
    /**
	 * 操作者
	 */
    private String createdBy;
    
    /**
	 * 操作时间
	 */
    private Date createDate;
    
    /**
	 * 备注
	 */
    private String notes;

    
    /**
	 * 和OrPaperDailyAuditItem关联
	 */
    private List items = new ArrayList();

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
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

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public int getLostCount() {
        return lostCount;
    }

    public void setLostCount(int lostCount) {
        this.lostCount = lostCount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

}
