package com.mangocity.hweb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 用于预订行程单 传真, 邮件模板
 * 
 * @author ChenJiaJie
 * 
 */
public class ScheduleFax implements Serializable {

    // 酒店名称
    private String hotelName;

    // 入住日期
    private String checkInDate;

    // 离店日期
    private String checkOutDate;

    // 房间数量
    private int roomQuantity;

    // 住店几晚
    private int nightCount;

    // 房型
    private String roomTypeName;

    // 子房型名
    private String childRoomTypeName;

    // 入住人姓名
    private String customerName;

    // 价格
    private List<String> priceList = new ArrayList<String>();

    // 付款方式
    private String payMethod;

    // 费用总计
    private String totalPrice;

    // 积分消费
    private String pointExpended;

    // 实收金额
    private String amountReceivable;

    // 特殊要求
    private String specialRequestOverView;

    // 免费修改/取消订单时间
    private String orderUpdateDate;

    // 联系人姓名
    private String linkmanName;

    // 联系人电子邮件
    private String linkmanEmail;

    // 联系人电话/手机
    private String linkmanTelephone;

    // 确认方式
    private String confirmType;

    // 酒店电话
    private String hotelTelephone;

    // 酒店地址
    private String hotelAddress;

    // 预订提示
    private String bookhint;

    // 取消提示
    private String cancelModify;
    
    /**
     * hotel2.9.3 代金券消费金额 add by chenjiajie 2009-09-07
     */
    private String voucherExpended;

    /** ******** 以下为邮件传真用 begin *********** */

    private String hotelEmail;

    private String nowDate;

    private String newTime;

    private String orderNotes;

    // 传真类型
    private String FaxType;

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPointExpended() {
        return pointExpended;
    }

    public void setPointExpended(String pointExpended) {
        this.pointExpended = pointExpended;
    }

    public String getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(String amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getSpecialRequestOverView() {
        return specialRequestOverView;
    }

    public void setSpecialRequestOverView(String specialRequestOverView) {
        this.specialRequestOverView = specialRequestOverView;
    }

    public String getOrderUpdateDate() {
        return orderUpdateDate;
    }

    public void setOrderUpdateDate(String orderUpdateDate) {
        this.orderUpdateDate = orderUpdateDate;
    }

    public String getLinkmanName() {
        return linkmanName;
    }

    public void setLinkmanName(String linkmanName) {
        this.linkmanName = linkmanName;
    }

    public String getLinkmanEmail() {
        return linkmanEmail;
    }

    public void setLinkmanEmail(String linkmanEmail) {
        this.linkmanEmail = linkmanEmail;
    }

    public String getLinkmanTelephone() {
        return linkmanTelephone;
    }

    public void setLinkmanTelephone(String linkmanTelephone) {
        this.linkmanTelephone = linkmanTelephone;
    }

    public String getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(String confirmType) {
        this.confirmType = confirmType;
    }

    public String getHotelTelephone() {
        return hotelTelephone;
    }

    public void setHotelTelephone(String hotelTelephone) {
        this.hotelTelephone = hotelTelephone;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getNightCount() {
        return nightCount;
    }

    public void setNightCount(int nightCount) {
        this.nightCount = nightCount;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<String> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<String> priceList) {
        this.priceList = priceList;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getHotelEmail() {
        return hotelEmail;
    }

    public void setHotelEmail(String hotelEmail) {
        this.hotelEmail = hotelEmail;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public String getNewTime() {
        return newTime;
    }

    public void setNewTime(String newTime) {
        this.newTime = newTime;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        this.orderNotes = orderNotes;
    }

    public String getFaxType() {
        return FaxType;
    }

    public void setFaxType(String faxType) {
        FaxType = faxType;
    }

    public String getBookhint() {
        return bookhint;
    }

    public void setBookhint(String bookhint) {
        this.bookhint = bookhint;
    }

    public String getCancelModify() {
        return cancelModify;
    }

    public void setCancelModify(String cancelModify) {
        this.cancelModify = cancelModify;
    }

    public String getChildRoomTypeName() {
        return childRoomTypeName;
    }

    public void setChildRoomTypeName(String childRoomTypeName) {
        this.childRoomTypeName = childRoomTypeName;
    }

    public int getRoomQuantity() {
        return roomQuantity;
    }

    public void setRoomQuantity(int roomQuantity) {
        this.roomQuantity = roomQuantity;
    }

	public String getVoucherExpended() {
		return voucherExpended;
	}

	public void setVoucherExpended(String voucherExpended) {
		this.voucherExpended = voucherExpended;
	}

}
