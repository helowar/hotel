/**
 * 
 */
package com.mangocity.hweb.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fuhoujun
 *
 */
public class AuditInfoVO {
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
    private String voucherExpended;
    private String hotelEmail;
    private String nowDate;
    private String newTime;
    private String orderNotes;
    
    private String costCenter;//成本中心
    private String ordercd;//订单号
    private String star;//酒店星级
    private String reasonCode;//原因代码
    private String settlementUnit;//结算单位
    private String bookPerson;//预定人
    private String department;//部门
    private double serviceFee;//服务费
    private String imgRoot;//图片的根
    private String latestArrivalTime;//最晚入住时间

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

    public String getOrdercd() {
        return ordercd;
    }

    public void setOrdercd(String ordercd) {
        this.ordercd = ordercd;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getSettlementUnit() {
        return settlementUnit;
    }

    public void setSettlementUnit(String settlementUnit) {
        this.settlementUnit = settlementUnit;
    }

    public String getBookPerson() {
        return bookPerson;
    }

    public void setBookPerson(String bookPerson) {
        this.bookPerson = bookPerson;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getImgRoot() {
        return imgRoot;
    }

    public void setImgRoot(String imgRoot) {
        this.imgRoot = imgRoot;
    }

    public String getLatestArrivalTime() {
        return latestArrivalTime;
    }

    public void setLatestArrivalTime(String latestArrivalTime) {
        this.latestArrivalTime = latestArrivalTime;
    }
    
}
