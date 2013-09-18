package com.mangocity.hotel.order.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;


/**
 * 日审
 * 
 * @author chenkeming
 *
 */
public class OrDailyAudit implements Entity {            

    private static final long serialVersionUID = 4997600461099189265L;

    /**
	 * 日审ID <pk>
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
	 * 发送时间
	 */
    private Date sendTime;    

    /**
	 * 状态: 0未收回传/1已回传/2待审查/3完成
	 * @see OrdailyauditStatus
	 */
    private int status;

    /**
	 * 是否已超时
	 */
    private int isOverTime;

    /**
	 * 酒店返回的unicall传真号，通过该号，可查看酒店回传的传真jpg图片。
	 */
    private String faxFile;

    /**
	 * 完成的时间
	 */
    private Date completeTime;
    
    /**
	 * 待审核的时间
	 */
    private Date dailyAuditTime;
    
    /**
	 *  应该生成日审记录的时间　（短日期）
	 */
    private Date checkNight;
    
    /**
	 * default sysdate　创建时间
	 */
    private Date addTime;
    
    /**
	 * 分配的员工工号
	 */
    private String assignTo;
    
    /**
	 * 是否传真确认
	 */
    private int faxConfirm;
    
    /**
	 * 订单数
	 */
    private int orderNumbers;
        
    /**
	 * 和OrOrderItem关联
	 */
    private List orderItems = new ArrayList();

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }


    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Date getCheckNight() {
        return checkNight;
    }

    public void setCheckNight(Date checkNight) {
        this.checkNight = checkNight;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public int getFaxConfirm() {
        return faxConfirm;
    }

    public void setFaxConfirm(int faxConfirm) {
        this.faxConfirm = faxConfirm;
    }

    public String getFaxFile() {
        return faxFile;
    }

    public void setFaxFile(String faxFile) {
        this.faxFile = faxFile;
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

    public int getIsOverTime() {
        return isOverTime;
    }

    public void setIsOverTime(int isOverTime) {
        this.isOverTime = isOverTime;
    }

    public List getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List orderItems) {
        this.orderItems = orderItems;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOrderNumbers() {
        return orderNumbers;
    }

    public void setOrderNumbers(int orderNumbers) {
        this.orderNumbers = orderNumbers;
    }

    public Date getDailyAuditTime() {
        return dailyAuditTime;
    }

    public void setDailyAuditTime(Date dailyAuditTime) {
        this.dailyAuditTime = dailyAuditTime;
    }




}
