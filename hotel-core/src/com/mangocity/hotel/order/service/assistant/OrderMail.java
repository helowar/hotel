package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;

/**
 * 
 * 用于邮件模板
 * 
 * @author chenkeming
 * 
 */
public class OrderMail implements Serializable {

    /**
     * [Linkman] 先生/小姐
     */
    private String linkman;

    /**
     * 登录用户名：[Aliasname]
     */
    private String aliasName;

    /**
     * 在 [CheckDate] 入住
     */
    private String checkDate;

    private String hotelName;

    private String quantity;

    private String roomType;

    private String orderCD;

    /**
     * 入住人姓名：[fellowNames]
     */
    private String fellowNames;

    private String checkinDate;

    private String checkoutDate;

    /**
     * 含早情况：[isContainMeal]
     */
    private String isContainMeal;

    /**
     * 单价
     */
    private String roomPrice;

    /**
     * 总计房费：[TotalSum]（含服务费，不含城市建设费等其他费用）
     */
    private String totalSum;

    /**
     * 付款方式：[PayMethod]
     */
    private String payMethod;

    /**
     * 抵店时间：[ArrivalTime]
     */
    private String arrivalTime;

    /**
     * 特殊要求：[SepRequest]
     */
    private String sepRequest;

    /**
     * 备 注：[CustomerNotice]
     */
    private String customerNotice;

    /**
     * 酒店确认号：[Hotelconfirmno]
     */
    private String hotelconfirmno;

    /**
     * 酒店地址：[HotelAddress]
     */
    private String hotelAddress;

    /**
     * 酒店电话：[HotelTelephone]
     */
    private String hotelTelephone;

    /**
     * 酒店传真：[HotelFax]
     */
    private String hotelFax;

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
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

    public String getCustomerNotice() {
        return customerNotice;
    }

    public void setCustomerNotice(String customerNotice) {
        this.customerNotice = customerNotice;
    }

    public String getFellowNames() {
        return fellowNames;
    }

    public void setFellowNames(String fellowNames) {
        this.fellowNames = fellowNames;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getHotelconfirmno() {
        return hotelconfirmno;
    }

    public void setHotelconfirmno(String hotelconfirmno) {
        this.hotelconfirmno = hotelconfirmno;
    }

    public String getHotelFax() {
        return hotelFax;
    }

    public void setHotelFax(String hotelFax) {
        this.hotelFax = hotelFax;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelTelephone() {
        return hotelTelephone;
    }

    public void setHotelTelephone(String hotelTelephone) {
        this.hotelTelephone = hotelTelephone;
    }

    public String getIsContainMeal() {
        return isContainMeal;
    }

    public void setIsContainMeal(String isContainMeal) {
        this.isContainMeal = isContainMeal;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getSepRequest() {
        return sepRequest;
    }

    public void setSepRequest(String sepRequest) {
        this.sepRequest = sepRequest;
    }

    public String getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(String totalSum) {
        this.totalSum = totalSum;
    }
}
