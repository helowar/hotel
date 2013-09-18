package com.mango.hotel.ebooking.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * Ebooking价格调整实体
 * 
 * @author chenjiajie
 * 
 */
public class HtlEbookingPriceRedressal implements Serializable {
    /**
     * 调价申请号
     */
    private long priceRedressalID;

    /**
     * 酒店名称
     */
    private String hotelname;

    /**
     * 酒店ID
     */
    private String hotelid;

    /**
     * 调价名称ID
     */
    private long priceRedressalNameId;

    /**
     * 调价名称
     */
    private String priceRedressalName;

    /**
     * 开始日期
     */
    private Date beginDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 房型ID
     */
    private long roomTypeID;

    /**
     * 房型名称
     */
    private String roomTypeName;

    /**
     * 价格类型ID
     */
    private long priceTypeID;

    /**
     * 价格类型名称
     */
    private String priceTypeName;

    /**
     * 支付方式
     */
    private String payMethod;

    /**
     * 底价
     */
    private float basePrice;

    /**
     * 售价
     */
    private float sellingPrice;

    /**
     * 佣金
     */
    private float commision;

    /**
     * 早餐份数
     */
    private long breakfastNum;

    /**
     * 早餐类型ID
     */
    private long breakfastTypeID;

    /**
     * 早餐类型名称
     */
    private String breakfastTypeName;

    /**
     * 紧急程度
     */
    private long exigenceGrade;

    /**
     * 星期
     */
    private String week;

    /**
     * 申请状态
     */
    private long requisitionState;

    /**
     * 操作人
     */
    private String operationer;

    /**
     * 操作人ID
     */
    private String operationerID;

    /**
     * 操作日期
     */
    private Date operationdate;

    /**
     * 审核人姓名
     */
    private String auditingName;

    /**
     * 审核人ID
     */
    private String auditingID;

    /**
     * 审核时间
     */
    private Date auditingDate;

    /** getter and setter * */
    public Date getAuditingDate() {
        return auditingDate;
    }

    public void setAuditingDate(Date auditingDate) {
        this.auditingDate = auditingDate;
    }

    public String getAuditingID() {
        return auditingID;
    }

    public void setAuditingID(String auditingID) {
        this.auditingID = auditingID;
    }

    public String getAuditingName() {
        return auditingName;
    }

    public void setAuditingName(String auditingName) {
        this.auditingName = auditingName;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public long getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(long breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public long getBreakfastTypeID() {
        return breakfastTypeID;
    }

    public void setBreakfastTypeID(long breakfastTypeID) {
        this.breakfastTypeID = breakfastTypeID;
    }

    public String getBreakfastTypeName() {
        return breakfastTypeName;
    }

    public void setBreakfastTypeName(String breakfastTypeName) {
        this.breakfastTypeName = breakfastTypeName;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getExigenceGrade() {
        return exigenceGrade;
    }

    public void setExigenceGrade(long exigenceGrade) {
        this.exigenceGrade = exigenceGrade;
    }

    public String getHotelid() {
        return hotelid;
    }

    public void setHotelid(String hotelid) {
        this.hotelid = hotelid;
    }

    public String getHotelname() {
        return hotelname;
    }

    public void setHotelname(String hotelname) {
        this.hotelname = hotelname;
    }

    public Date getOperationdate() {
        return operationdate;
    }

    public void setOperationdate(Date operationdate) {
        this.operationdate = operationdate;
    }

    public String getOperationer() {
        return operationer;
    }

    public void setOperationer(String operationer) {
        this.operationer = operationer;
    }

    public String getOperationerID() {
        return operationerID;
    }

    public void setOperationerID(String operationerID) {
        this.operationerID = operationerID;
    }

    public long getPriceRedressalID() {
        return priceRedressalID;
    }

    public void setPriceRedressalID(long priceRedressalID) {
        this.priceRedressalID = priceRedressalID;
    }

    public String getPriceRedressalName() {
        return priceRedressalName;
    }

    public void setPriceRedressalName(String priceRedressalName) {
        this.priceRedressalName = priceRedressalName;
    }

    public long getPriceTypeID() {
        return priceTypeID;
    }

    public void setPriceTypeID(long priceTypeID) {
        this.priceTypeID = priceTypeID;
    }

    public String getPriceTypeName() {
        return priceTypeName;
    }

    public void setPriceTypeName(String priceTypeName) {
        this.priceTypeName = priceTypeName;
    }

    public long getRequisitionState() {
        return requisitionState;
    }

    public void setRequisitionState(long requisitionState) {
        this.requisitionState = requisitionState;
    }

    public float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public long getRoomTypeID() {
        return roomTypeID;
    }

    public void setRoomTypeID(long roomTypeID) {
        this.roomTypeID = roomTypeID;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public long getPriceRedressalNameId() {
        return priceRedressalNameId;
    }

    public void setPriceRedressalNameId(long priceRedressalNameId) {
        this.priceRedressalNameId = priceRedressalNameId;
    }

    public float getCommision() {
        return commision;
    }

    public void setCommision(float commision) {
        this.commision = commision;
    }
}
