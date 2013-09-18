package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 
 * 纸质日审联系表，哪些酒店需要发送纸质日审，<br>
 * 系统自动发送日审时，该表有数据的酒店需要发送纸质日审
 * 
 * @author chenkeming
 */
public class OrPaperContact implements Entity {

    private static final long serialVersionUID = -8827392795364376225L;

    /**
	 * 联系表ID <pk> 
	 */
    private Long ID;

    /**
	 * 酒店ID
	 */
    private Long hotelId;

    /**
	 * 传真号
	 */
    private String fax;
    
    /**
	 * 是否传真确认
	 */
    private boolean faxConfirm;
    
    /**
	 * 操作者
	 */
    private String createdBy;
    
    /**
	 * 操作时间
	 */
    private Date createDate;

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

    public boolean isFaxConfirm() {
        return faxConfirm;
    }

    public void setFaxConfirm(boolean faxConfirm) {
        this.faxConfirm = faxConfirm;
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
    
}
