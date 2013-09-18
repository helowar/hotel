package com.mangocity.hotel.order.service.assistant;

/**
 * 
 * 用于直联酒店定单的特殊要求
 * 
 * @author chenkeming
 * 
 */
public class ChannelOrderSpecialRequest {

    // 日期
    private String faxDate;

    // 时间
    private String faxTime;

    // 芒果网定单编号
    private String orderCd;

    // CRS确认号
    private String crsConfirmID;

    // CRS酒店名称
    private String crsHotelName;

    // CRS酒店传真编号
    private String crsHotelFaxNo;

    // 客人名称
    private String memberName;

    // 入住日期
    private String checkInDate;

    // 退房日期
    private String checkOutDate;

    // 几晚
    private int sumNight;

    // 房型
    private String roomType;

    // 房间数量
    private int roomCount;

    // 床型名称
    private String bedName;

    // 总计进金额
    private double sumRMB;

    // 抵店时间
    private String arrivalTime;

    // 交通工具
    private String arrivalTraffic;

    // 特殊要求
    private String specialRequest;

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getArrivalTraffic() {
        return arrivalTraffic;
    }

    public void setArrivalTraffic(String arrivalTraffic) {
        this.arrivalTraffic = arrivalTraffic;
    }

    public String getBedName() {
        return bedName;
    }

    public void setBedName(String bedName) {
        this.bedName = bedName;
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

    public String getCrsConfirmID() {
        return crsConfirmID;
    }

    public void setCrsConfirmID(String crsConfirmID) {
        this.crsConfirmID = crsConfirmID;
    }

    public String getCrsHotelFaxNo() {
        return crsHotelFaxNo;
    }

    public void setCrsHotelFaxNo(String crsHotelFaxNo) {
        this.crsHotelFaxNo = crsHotelFaxNo;
    }

    public String getCrsHotelName() {
        return crsHotelName;
    }

    public void setCrsHotelName(String crsHotelName) {
        this.crsHotelName = crsHotelName;
    }

    public String getFaxDate() {
        return faxDate;
    }

    public void setFaxDate(String faxDate) {
        this.faxDate = faxDate;
    }

    public String getFaxTime() {
        return faxTime;
    }

    public void setFaxTime(String faxTime) {
        this.faxTime = faxTime;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getOrderCd() {
        return orderCd;
    }

    public void setOrderCd(String orderCd) {
        this.orderCd = orderCd;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public int getSumNight() {
        return sumNight;
    }

    public void setSumNight(int sumNight) {
        this.sumNight = sumNight;
    }

    public double getSumRMB() {
        return sumRMB;
    }

    public void setSumRMB(double sumRMB) {
        this.sumRMB = sumRMB;
    }

}
