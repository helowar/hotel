package com.mangocity.hotel.order.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;


/**
 * 订单日审记录
 * 
 * @author chenkeming
 *
 */
public class Audit implements Entity {

    // 日审ID <pk>
    private Long ID;

    // 订单ID <fk> 和Order关联
    private Order order;
    
    /**
	 * 日审酒店ID <fk> 和AuditHotel关联
	 */
    private AuditHotel auditHotel;    

    // 日审时间
    private Date auditDate;

    // 日审状态
    private String auditState;

    // 房间日期
    private Date checkinDate;

    // 审核标记
    private String auditingWait;
    
    // 日审人ID
    private String auditorId;
    
    // 日审人姓名
    private String auditorName;
    
    // 审核人ID
    private String checkerId;
    
    // 审核人姓名
    private String checkerName;
    
    // 审核时间
    private Date checkTime;
    
    /**
	 * 电话和传真日审
	 */
    private String type;
    
    // 和AuditItem关联
    private List auditItems = new ArrayList();

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long auditId) {
        this.ID = auditId;
    }

    public String getAuditingWait() {
        return auditingWait;
    }

    public void setAuditingWait(String auditingWait) {
        this.auditingWait = auditingWait;
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

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public String getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List getAuditItems() {
        return auditItems;
    }

    public void setAuditItems(List auditItems) {
        this.auditItems = auditItems;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AuditHotel getAuditHotel() {
        return auditHotel;
    }

    public void setAuditHotel(AuditHotel auditHotel) {
        this.auditHotel = auditHotel;
    }

}
