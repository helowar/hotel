/**
 *  Modified by chenkeming@2007.05.08
 *  
 *  日审酒店列表
 */
package com.mangocity.hotel.order.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * 日审酒店表
 * @author zhengxin
 *
 */
public class AuditHotel implements Entity {            
    /**
	 * ID 
	 */
    private Long ID;

    /**
	 * 酒店ID
	 */
    private Long hotelId;

    /**
	 * 酒店名称
	 */
    private String hotelName;

    /**
	 * 酒店回复状态
	 */
    private String state;
    
    /**
	 * 日审日期(进行日审的日期)
	 */
    private Date auditDate;
    
    
    /**
	 * 日审人ID
	 */
    private String auditorId;
    
    /**
	 * 日审人名称
	 */
    private String auditorName;
    
    /**
	 * 创建时间
	 */
    private Date createDate;
    
    /**
	 * 电话和传真日审
	 */
    private String type;
    
    /**
	 * 和Audit关联
	 */
    private List auditList = new ArrayList();

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List getAuditList() {
        return auditList;
    }

    public void setAuditList(List auditList) {
        this.auditList = auditList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
