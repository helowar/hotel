package com.mangocity.hotel.order.service.assistant;

/**
 * 
 * 用于传真模板
 * 
 * @author chenkeming
 * 
 */
public class OrderItemFax {

    private String itemCheckInDate;

    private String itemName;

    private int itemCount;

    private String breakfast;

    private String breakfastNum;

    private String salePrice;

    private String basePrice;

    private String payType;

    private String itemOrderCD;

    private String itemFellowNames;

    private String itemConfirmNo;

    // 传真模版修改 add by baofeng.si V2.3 2008-6-19 Start
    // 退房时间
    private String itemCheckOutDate;

    // 房号
    private String roomNo;
    
    private String itemType;

    // 传真模版修改 add by baofeng.si V2.3 2008-6-19 End

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getItemConfirmNo() {
        return itemConfirmNo;
    }

    public void setItemConfirmNo(String itemConfirmNo) {
        this.itemConfirmNo = itemConfirmNo;
    }

    public String getItemFellowNames() {
        return itemFellowNames;
    }

    public void setItemFellowNames(String itemFellowNames) {
        this.itemFellowNames = itemFellowNames;
    }

    public String getItemOrderCD() {
        return itemOrderCD;
    }

    public void setItemOrderCD(String itemOrderCD) {
        this.itemOrderCD = itemOrderCD;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getItemCheckInDate() {
        return itemCheckInDate;
    }

    public void setItemCheckInDate(String itemCheckInDate) {
        this.itemCheckInDate = itemCheckInDate;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(String breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public String getItemCheckOutDate() {
        return itemCheckOutDate;
    }

    public void setItemCheckOutDate(String itemCheckOutDate) {
        this.itemCheckOutDate = itemCheckOutDate;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

}
