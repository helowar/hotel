package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;
import com.mangocity.util.hotel.constant.QuotaType;

/**
 * 订单配额记录表
 * @author chenkeming Feb 12, 2009 10:05:35 AM
 */
public class OrQuotaRecord implements Entity {

    private static final long serialVersionUID = 8022518910178561386L;

    /**
	 * ID <pk>
	 */
    private Long ID;

    /**
	 * 订单ID <fk> 和OrOrder关联
	 */
    private Long orderId;
    
    /**
	 * 历史订单序号,zero based
	 */
    private int hisNo;
    
    /**
	 * 支付方式
	 * @author chenkeming Feb 12, 2009 10:10:17 AM
	 */
    private String payMethod;
    
    private Long roomTypeId;
    
    private Long childRoomTypeId;
    
    private Date night;
    
    /**
	 * 配额类型
	 * 
	 * @see QuotaType
	 */
    private String quotaType;
    
    /**
	 * 要扣的配额类型
	 * 
	 * @see QuotaType
	 */
    private String quotaTypeOld;
    
    /**
	 * 配额模式
	 */
    private String quotaPattern;
    
    private int quantity;
    
    private Date createTime;
    
    private String creator;         
    
    /**
	 * 是否已确认
	 */
    private boolean isConfirm;
    
    /**
	 * 销售价
	 */
    private double salePrice;
    
    /**
	 * 底价
	 */
    private double basePrice;
    
    /**
	 * 门市价
	 */
    private double marketPrice;
    
    /**
	 * 房态
	 */
    private String roomState;
    
    /**
	 * 含早类型
	 */
    private int breakfast;
    
    /**
	 * 含早数量
	 */
    private int breakfastNum;
    
    /**
	 * 0: 能退
	 * 1: 不能退
	 */
    private boolean quotaCantReturn;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(Long childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getHisNo() {
        return hisNo;
    }

    public void setHisNo(int hisNo) {
        this.hisNo = hisNo;
    }

    public Date getNight() {
        return night;
    }

    public void setNight(Date night) {
        this.night = night;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getQuotaPattern() {
        return quotaPattern;
    }

    public void setQuotaPattern(String quotaPattern) {
        this.quotaPattern = quotaPattern;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public boolean getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(boolean isConfirm) {
        this.isConfirm = isConfirm;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public int getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(int breakfast) {
        this.breakfast = breakfast;
    }

    public int getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(int breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getRoomState() {
        return roomState;
    }

    public void setRoomState(String roomState) {
        this.roomState = roomState;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getQuotaTypeOld() {
        return quotaTypeOld;
    }

    public void setQuotaTypeOld(String quotaTypeOld) {
        this.quotaTypeOld = quotaTypeOld;
    }

    public boolean isQuotaCantReturn() {
        return quotaCantReturn;
    }

    public void setQuotaCantReturn(boolean quotaCantReturn) {
        this.quotaCantReturn = quotaCantReturn;
    }

}
