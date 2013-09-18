package com.mangocity.hotelweb.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrFellowInfo;

/**
 */
public class HotelWebOrderTemp {
    // 入住日期
    private Date checkinDate;

    // 离店日期
    private Date checkoutDate;

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
    private String linkmanName;

    // 联系人手机号码
    private String linkmanTelephone;

    // 固定电话区号
    private String fixedDistrictNum;

    // 固定电话号码
    private String fixedPhone;

    // 固定电话分机
    private String fixedExtension;

    // 联系人邮箱
    private String linkmanEmail;

    // 传真号码区号
    private String faxDistrictNum;

    // 传真号码
    private String faxPhone;

    // 传真号码分机
    private String faxExtension;

    // 确认方式
    private String confirmType;

    // 入住人信息
    private List<OrFellowInfo> fellowInfoList = new ArrayList();

    // 预定特别提示
    private List<BookSpecialHint> specialHintList = new ArrayList();

    // 酒店附加服务
    private List<HotelAdditionalServe> additionalServeList = new ArrayList();

    // 是否需要担保
    private boolean needAssure;

    public List<HotelAdditionalServe> getAdditionalServeList() {
        return additionalServeList;
    }

    public void setAdditionalServeList(List<HotelAdditionalServe> additionalServeList) {
        this.additionalServeList = additionalServeList;
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

    public String getLinkmanEmail() {
        return linkmanEmail;
    }

    public void setLinkmanEmail(String linkmanEmail) {
        this.linkmanEmail = linkmanEmail;
    }

    public String getLinkmanName() {
        return linkmanName;
    }

    public void setLinkmanName(String linkmanName) {
        this.linkmanName = linkmanName;
    }

    public String getLinkmanTelephone() {
        return linkmanTelephone;
    }

    public void setLinkmanTelephone(String linkmanTelephone) {
        this.linkmanTelephone = linkmanTelephone;
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

    public List<BookSpecialHint> getSpecialHintList() {
        return specialHintList;
    }

    public void setSpecialHintList(List<BookSpecialHint> specialHintList) {
        this.specialHintList = specialHintList;
    }

}
