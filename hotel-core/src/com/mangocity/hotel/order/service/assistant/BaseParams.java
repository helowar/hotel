package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;
import java.util.List;

public class BaseParams implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long hotelId;
	private String roomTypeId;
	private String childRoomTypeId;
	private String childRoomTypeName;			
	private String payMethod;
	private String quotaType;
	private String canRoomNumberAlert;
	private String oriOrderId;
	private String sRenew;			
	private String renewReason;
	private String renewMessage;
	private String guestRenewMessage;				
	private String  checkinDate;
	private String checkoutDate;
	private String hotelName;
	private String roomTypeName;			
	private String balanceMethod;						
	private String priceFirstDay;
	private String priceAllDay;
	private String hotelStar;
	private String cityId;
	private String payToPrepay;
	private String roomStatus;
	private String acceptCustom;
	private String lastAssureTime;
	private String clueInfo;
	private String tipContent;
	private String paymentCurrency;
	private Boolean payToHotel;			
	private int quotaLeastNum;
	private Boolean quotaBool;
	private Boolean bedStateOneBool;
	private Boolean bedStateTwoBool;
	private Boolean bedStateThrBool;
	private String channel;
	private Boolean traditionalChannel;
	private String hotelroomcount;			
	private String isTaxCharges;
	private String isReservation;
	private String isSalesPromo;
	private String isUserComment;	
	private String colFirstDayPrice;
	private String favourableFlag;
	private String forCooperate;
	private List<EverydayParams> everydayParams;
	
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
	public String getRoomTypeId() {
		return roomTypeId;
	}
	public void setRoomTypeId(String roomTypeId) {
		this.roomTypeId = roomTypeId;
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
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getQuotaType() {
		return quotaType;
	}
	public void setQuotaType(String quotaType) {
		this.quotaType = quotaType;
	}
	public String getCanRoomNumberAlert() {
		return canRoomNumberAlert;
	}
	public void setCanRoomNumberAlert(String canRoomNumberAlert) {
		this.canRoomNumberAlert = canRoomNumberAlert;
	}
	public String getOriOrderId() {
		return oriOrderId;
	}
	public void setOriOrderId(String oriOrderId) {
		this.oriOrderId = oriOrderId;
	}
	public String getsRenew() {
		return sRenew;
	}
	public void setsRenew(String sRenew) {
		this.sRenew = sRenew;
	}
	public String getRenewReason() {
		return renewReason;
	}
	public void setRenewReason(String renewReason) {
		this.renewReason = renewReason;
	}
	public String getRenewMessage() {
		return renewMessage;
	}
	public void setRenewMessage(String renewMessage) {
		this.renewMessage = renewMessage;
	}
	public String getGuestRenewMessage() {
		return guestRenewMessage;
	}
	public void setGuestRenewMessage(String guestRenewMessage) {
		this.guestRenewMessage = guestRenewMessage;
	}
	public String getCheckinDate() {
		return checkinDate;
	}
	public void setCheckinDate(String checkinDate) {
		this.checkinDate = checkinDate;
	}
	public String getCheckoutDate() {
		return checkoutDate;
	}
	public void setCheckoutDate(String checkoutDate) {
		this.checkoutDate = checkoutDate;
	}
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getRoomTypeName() {
		return roomTypeName;
	}
	public void setRoomTypeName(String roomTypeName) {
		this.roomTypeName = roomTypeName;
	}
	public String getBalanceMethod() {
		return balanceMethod;
	}
	public void setBalanceMethod(String balanceMethod) {
		this.balanceMethod = balanceMethod;
	}
	public String getPriceFirstDay() {
		return priceFirstDay;
	}
	public void setPriceFirstDay(String priceFirstDay) {
		this.priceFirstDay = priceFirstDay;
	}
	public String getPriceAllDay() {
		return priceAllDay;
	}
	public void setPriceAllDay(String priceAllDay) {
		this.priceAllDay = priceAllDay;
	}
	public String getHotelStar() {
		return hotelStar;
	}
	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getPayToPrepay() {
		return payToPrepay;
	}
	public void setPayToPrepay(String payToPrepay) {
		this.payToPrepay = payToPrepay;
	}
	public String getRoomStatus() {
		return roomStatus;
	}
	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}
	public String getAcceptCustom() {
		return acceptCustom;
	}
	public void setAcceptCustom(String acceptCustom) {
		this.acceptCustom = acceptCustom;
	}
	public String getLastAssureTime() {
		return lastAssureTime;
	}
	public void setLastAssureTime(String lastAssureTime) {
		this.lastAssureTime = lastAssureTime;
	}
	public String getClueInfo() {
		return clueInfo;
	}
	public void setClueInfo(String clueInfo) {
		this.clueInfo = clueInfo;
	}
	public String getTipContent() {
		return tipContent;
	}
	public void setTipContent(String tipContent) {
		this.tipContent = tipContent;
	}
	public String getPaymentCurrency() {
		return paymentCurrency;
	}
	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}
	public Boolean getPayToHotel() {
		return payToHotel;
	}
	public void setPayToHotel(Boolean payToHotel) {
		this.payToHotel = payToHotel;
	}
	public int getQuotaLeastNum() {
		return quotaLeastNum;
	}
	public void setQuotaLeastNum(int quotaLeastNum) {
		this.quotaLeastNum = quotaLeastNum;
	}
	public Boolean getQuotaBool() {
		return quotaBool;
	}
	public void setQuotaBool(Boolean quotaBool) {
		this.quotaBool = quotaBool;
	}
	public Boolean getBedStateOneBool() {
		return bedStateOneBool;
	}
	public void setBedStateOneBool(Boolean bedStateOneBool) {
		this.bedStateOneBool = bedStateOneBool;
	}
	public Boolean getBedStateTwoBool() {
		return bedStateTwoBool;
	}
	public void setBedStateTwoBool(Boolean bedStateTwoBool) {
		this.bedStateTwoBool = bedStateTwoBool;
	}
	public Boolean getBedStateThrBool() {
		return bedStateThrBool;
	}
	public void setBedStateThrBool(Boolean bedStateThrBool) {
		this.bedStateThrBool = bedStateThrBool;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Boolean getTraditionalChannel() {
		return traditionalChannel;
	}
	public void setTraditionalChannel(Boolean traditionalChannel) {
		this.traditionalChannel = traditionalChannel;
	}
	public String getHotelroomcount() {
		return hotelroomcount;
	}
	public void setHotelroomcount(String hotelroomcount) {
		this.hotelroomcount = hotelroomcount;
	}
	public String getIsTaxCharges() {
		return isTaxCharges;
	}
	public void setIsTaxCharges(String isTaxCharges) {
		this.isTaxCharges = isTaxCharges;
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
	public String getIsUserComment() {
		return isUserComment;
	}
	public void setIsUserComment(String isUserComment) {
		this.isUserComment = isUserComment;
	}
	public String getColFirstDayPrice() {
		return colFirstDayPrice;
	}
	public void setColFirstDayPrice(String colFirstDayPrice) {
		this.colFirstDayPrice = colFirstDayPrice;
	}
	public String getFavourableFlag() {
		return favourableFlag;
	}
	public void setFavourableFlag(String favourableFlag) {
		this.favourableFlag = favourableFlag;
	}
	public String getForCooperate() {
		return forCooperate;
	}
	public void setForCooperate(String forCooperate) {
		this.forCooperate = forCooperate;
	}
	public List<EverydayParams> getEverydayParams() {
		return everydayParams;
	}
	public void setEverydayParams(List<EverydayParams> everydayParams) {
		this.everydayParams = everydayParams;
	}
	

}
