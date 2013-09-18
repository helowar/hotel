/**
 * 
 *  房间明细
 */

package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 
 * @author zhengxin
 * 房间信息
 */
public class RoomDetail implements Entity {

    // ID <pk>
    private Long ID;         

    // 房间ID
    private Long productId;
    
    /**
	 * 配额ID
	 */
    private Long quotaId;
    
    
    /**
	 * 房间数量
	 */
    private int roomNum;
    
    // 房间日期
    private Date saleDate;
    
    // 销售价
    private double salePrice;
    
    // 底价
    private double basePrice;
    
    // 门市价
    private double roomPrice;
    
    // 建议销售价
    private double recommendPrice;
    
    // 服务费
    private double serviceFee;
    
    // 房价是否含服务费
    private String includeService;
    
    // 佣金
    private double commission;
    
    // 含早类型
    private boolean breakfast;
    
    // 含早数量
    private int breakfastNum;
    
    // 含早形式
    private String breakfastWay;
    
    // 含早价
    private double breakfastPrice;
    
    // 含早价是否返拥
    private String includeBreakfast;
    
    // 房态
    private String roomState;
    
    // 和Order关联
    private Order order;

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }


    public int getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(int breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public double getBreakfastPrice() {
        return breakfastPrice;
    }

    public void setBreakfastPrice(double breakfastPrice) {
        this.breakfastPrice = breakfastPrice;
    }

    public String getBreakfastWay() {
        return breakfastWay;
    }

    public void setBreakfastWay(String breakfastWay) {
        this.breakfastWay = breakfastWay;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date fellowDate) {
        this.saleDate = fellowDate;
    }

    public String getIncludeBreakfast() {
        return includeBreakfast;
    }

    public void setIncludeBreakfast(String includeBreakfast) {
        this.includeBreakfast = includeBreakfast;
    }

    public String getIncludeService() {
        return includeService;
    }

    public void setIncludeService(String includeService) {
        this.includeService = includeService;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public double getRecommendPrice() {
        return recommendPrice;
    }

    public void setRecommendPrice(double recommendPrice) {
        this.recommendPrice = recommendPrice;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long roomId) {
        this.ID = roomId;
    }


    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
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

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isBreakfast() {
        return breakfast;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public Long getQuotaId() {
        return quotaId;
    }

    public void setQuotaId(Long quotaId) {
        this.quotaId = quotaId;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    
    
}
