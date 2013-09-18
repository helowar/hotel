package com.mangocity.hotel.order.persistence.view;

import java.io.Serializable;

/**
 * 
 *  试预订参数类型
 *  
 *  @author chenkeming
 */
public class CheckParamVO implements Serializable {
    
    private String hotelId = "";
    private String roomTypeId = "";
    private String childRoomTypeId = "";
    private String payMethod = "";
    private String quotaType = "";
    private String hotelName = "";
    private String roomTypeName = "";
    private String balanceMethod = "";
    private String childRoomTypeName = "";
    private String hotelStar = "";
    private String cityId = "";
    private String roomStatus = "";
    private String acceptCustom = "";
    private String lastAssureTime = "";
    private String clueInfo = "";
    private String paymentCurrency = "";    
    private String payToHotel = "";
    private String isUserComment = "";
    private String isSalesPromo = "";
    private String isReservation = "";
    private String isTaxCharges = "";
    private String quotaLeastNum = "";
    private String quotaBool = "";
    private String bedStateOneBool = "";
    private String bedStateTwoBool = "";
    private String bedStateThrBool = "";    
    private String channel = "";
    private String payToPrepay = "";
    
    private String priceFirstDay = "";
    private String priceAllDay = "";
    
    private String canRoomNumberAlert="";
    
    private String tipContent = "";
    
    public String getTipContent() {
		return tipContent;
	}
	public void setTipContent(String tipContent) {
		this.tipContent = tipContent;
	}
	public String getCanRoomNumberAlert() {
		return canRoomNumberAlert;
	}
	public void setCanRoomNumberAlert(String canRoomNumberAlert) {
		this.canRoomNumberAlert = canRoomNumberAlert;
	}
	public String getAcceptCustom() {
        return acceptCustom;
    }
    public void setAcceptCustom(String acceptCustom) {
        this.acceptCustom = acceptCustom;
    }
    public String getBalanceMethod() {
        return balanceMethod;
    }
    public void setBalanceMethod(String balanceMethod) {
        this.balanceMethod = balanceMethod;
    }
    public String getBedStateOneBool() {
        return bedStateOneBool;
    }
    public void setBedStateOneBool(String bedStateOneBool) {
        this.bedStateOneBool = bedStateOneBool;
    }
    public String getBedStateThrBool() {
        return bedStateThrBool;
    }
    public void setBedStateThrBool(String bedStateThrBool) {
        this.bedStateThrBool = bedStateThrBool;
    }
    public String getBedStateTwoBool() {
        return bedStateTwoBool;
    }
    public void setBedStateTwoBool(String bedStateTwoBool) {
        this.bedStateTwoBool = bedStateTwoBool;
    }
    public String getChannel() {
        return channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChildRoomTypeId() {
        return childRoomTypeId;
    }
    public void setChildRoomTypeId(String childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }
    public String getChildRoomTypeName() {
        return childRoomTypeName;
    }
    public void setChildRoomTypeName(String childRoomTypeName) {
        this.childRoomTypeName = childRoomTypeName;
    }
    public String getCityId() {
        return cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
    public String getClueInfo() {
        return clueInfo;
    }
    public void setClueInfo(String clueInfo) {
        this.clueInfo = clueInfo;
    }
    public String getHotelId() {
        return hotelId;
    }
    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }
    public String getHotelName() {
        return hotelName;
    }
    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
    public String getHotelStar() {
        return hotelStar;
    }
    public void setHotelStar(String hotelStar) {
        this.hotelStar = hotelStar;
    }
    public String getIsReservation() {
        return isReservation;
    }
    public void setIsReservation(String isReservation) {
        this.isReservation = isReservation;
    }
    public String getIsSalesPromo() {
        return isSalesPromo;
    }
    public void setIsSalesPromo(String isSalesPromo) {
        this.isSalesPromo = isSalesPromo;
    }
    public String getIsTaxCharges() {
        return isTaxCharges;
    }
    public void setIsTaxCharges(String isTaxCharges) {
        this.isTaxCharges = isTaxCharges;
    }
    public String getIsUserComment() {
        return isUserComment;
    }
    public void setIsUserComment(String isUserComment) {
        this.isUserComment = isUserComment;
    }
    public String getLastAssureTime() {
        return lastAssureTime;
    }
    public void setLastAssureTime(String lastAssureTime) {
        this.lastAssureTime = lastAssureTime;
    }
    public String getPaymentCurrency() {
        return paymentCurrency;
    }
    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }
    public String getPayMethod() {
        return payMethod;
    }
    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }
    public String getPayToHotel() {
        return payToHotel;
    }
    public void setPayToHotel(String payToHotel) {
        this.payToHotel = payToHotel;
    }
    public String getPayToPrepay() {
        return payToPrepay;
    }
    public void setPayToPrepay(String payToPrepay) {
        this.payToPrepay = payToPrepay;
    }
    public String getPriceAllDay() {
        return priceAllDay;
    }
    public void setPriceAllDay(String priceAllDay) {
        this.priceAllDay = priceAllDay;
    }
    public String getPriceFirstDay() {
        return priceFirstDay;
    }
    public void setPriceFirstDay(String priceFirstDay) {
        this.priceFirstDay = priceFirstDay;
    }
    public String getQuotaBool() {
        return quotaBool;
    }
    public void setQuotaBool(String quotaBool) {
        this.quotaBool = quotaBool;
    }
    public String getQuotaLeastNum() {
        return quotaLeastNum;
    }
    public void setQuotaLeastNum(String quotaLeastNum) {
        this.quotaLeastNum = quotaLeastNum;
    }
    public String getQuotaType() {
        return quotaType;
    }
    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }
    public String getRoomStatus() {
        return roomStatus;
    }
    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }
    public String getRoomTypeId() {
        return roomTypeId;
    }
    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }
    public String getRoomTypeName() {
        return roomTypeName;
    }
    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }        
}
