package com.mangocity.hweb.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrFulfillment;

/**
 */
public class HotelWebOrderTemp implements Serializable {
    // 入住日期
    private Date checkinDate;

    // 离店日期
    private Date checkoutDate;

    // 入住晚数
    private int roomQuantity;

    //
    // 担保金额
    private double assureAmount;

    // 支付方式
    private String payMethod;

    // 最早到店时间
    private String arrivalTime;

    // 最晚到店时间
    private String latestArrivalTime;

    // 到达方式
    private String arrivalMethod;

    // 联系人姓名
    private String linkMan;

    // 联系人手机号码
    private String mobile;

    // 固定电话区号
    private String fixedDistrictNum;

    // 固定电话号码
    private String fixedPhone;

    // 固定电话分机
    private String fixedExtension;

    // 完整固定电话
    private String telephone;

    // 联系人邮箱
    private String email;

    // 传真号码区号
    private String faxDistrictNum;

    // 传真号码
    private String faxPhone;

    // 传真号码分机
    private String faxExtension;

    // 完整传真号码
    private String customerFax;

    // 确认方式
    private String confirmType;

    // 特殊要求
    private String specialRequest;

    // 费用总计
    private double totalAmount;

    // 实收金额
    private double acturalAmount;

    // 配送信息
    private OrFulfillment fulfill = new OrFulfillment();

    // 入住人信息
    private List<OrFellowInfo> fellowInfoList = new ArrayList();

    // 预定特别提示
    private BookSpecialHint specialHint = new BookSpecialHint();

    // 酒店附加服务
    private HotelAdditionalServe additionalServe = new HotelAdditionalServe();

    // 是否需要担保
    private boolean needAssure;

    // 价格信息
    private List priceList = new ArrayList();

    // 担保条款、担保取消条款、预付取消条款
    private OrReservation reservation;

    public double getActuralAmount() {
        return acturalAmount;
    }

    public void setActuralAmount(double acturalAmount) {
        this.acturalAmount = acturalAmount;
    }

    public OrFulfillment getFulfill() {
        return fulfill;
    }

    public void setFulfill(OrFulfillment orFulfillment) {
        this.fulfill = orFulfillment;
    }

    public int getRoomQuantity() {
        return roomQuantity;
    }

    public void setRoomQuantity(int roomQuantity) {
        this.roomQuantity = roomQuantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getCustomerFax() {
        return customerFax;
    }

    public void setCustomerFax(String customerFax) {
        this.customerFax = customerFax;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List getPriceList() {
        return priceList;
    }

    public void setPriceList(List priceList) {
        this.priceList = priceList;
    }

    public HotelAdditionalServe getAdditionalServe() {
        return additionalServe;
    }

    public void setAdditionalServe(HotelAdditionalServe additionalServe) {
        this.additionalServe = additionalServe;
    }

    public String getArrivalMethod() {
        return arrivalMethod;
    }

    public void setArrivalMethod(String arrivalMethod) {
        this.arrivalMethod = arrivalMethod;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getAssureAmount() {
        return assureAmount;
    }

    public void setAssureAmount(double assureAmount) {
        this.assureAmount = assureAmount;
    }

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(String confirmType) {
        this.confirmType = confirmType;
    }

    public String getFaxDistrictNum() {
        return faxDistrictNum;
    }

    public void setFaxDistrictNum(String faxDistrictNum) {
        this.faxDistrictNum = faxDistrictNum;
    }

    public String getFaxExtension() {
        return faxExtension;
    }

    public void setFaxExtension(String faxExtension) {
        this.faxExtension = faxExtension;
    }

    public String getFaxPhone() {
        return faxPhone;
    }

    public void setFaxPhone(String faxPhone) {
        this.faxPhone = faxPhone;
    }

    public List<OrFellowInfo> getFellowInfoList() {
        return fellowInfoList;
    }

    public void setFellowInfoList(List<OrFellowInfo> fellowInfoList) {
        this.fellowInfoList = fellowInfoList;
    }

    public String getFixedDistrictNum() {
        return fixedDistrictNum;
    }

    public void setFixedDistrictNum(String fixedDistrictNum) {
        this.fixedDistrictNum = fixedDistrictNum;
    }

    public String getFixedExtension() {
        return fixedExtension;
    }

    public void setFixedExtension(String fixedExtension) {
        this.fixedExtension = fixedExtension;
    }

    public String getFixedPhone() {
        return fixedPhone;
    }

    public void setFixedPhone(String fixedPhone) {
        this.fixedPhone = fixedPhone;
    }

    public String getLatestArrivalTime() {
        return latestArrivalTime;
    }

    public void setLatestArrivalTime(String latestArrivalTime) {
        this.latestArrivalTime = latestArrivalTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isNeedAssure() {
        return needAssure;
    }

    public void setNeedAssure(boolean needAssure) {
        this.needAssure = needAssure;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public BookSpecialHint getSpecialHint() {
        return specialHint;
    }

    public void setSpecialHint(BookSpecialHint specialHint) {
        this.specialHint = specialHint;
    }

    public OrReservation getReservation() {
        return reservation;
    }

    public void setReservation(OrReservation reservation) {
        this.reservation = reservation;
    }

}
