package com.mangocity.hotelweb.persistence;

import java.io.Serializable;

/**
 */
public class HotelOrderGroupBean implements Serializable {

    // 会员 可能为 注册名 或 注册手机号 或 注册证件号＋证件类型
    private String memberStr;

    // 会员姓名
    private String memberName;

    // 入住城市
    private String cityName;

    // 酒店名称
    private String hotelName;

    // 入住日期
    private String inDate;

    // 退房日期
    private String outDate;

    // 入住天数
    private int dateNum;

    // 房间数目： 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 20间以上
    private String roomNum;

    // 房间类型： 请选择 标准客房 豪华客房 三人客房 总统套房
    private String roomType;

    // 特别要求
    private String remark;

    // * 订购人：
    private String name;

    // * 联络电话：
    private String telNum;

    // 传真： 　
    private String faxNum;

    // * 电子邮件：
    private String linkEmail;

    public String getMemberStr() {
        return memberStr;
    }

    public void setMemberStr(String memberStr) {
        this.memberStr = memberStr;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public int getDateNum() {
        return dateNum;
    }

    public void setDateNum(int dateNum) {
        this.dateNum = dateNum;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getFaxNum() {
        return faxNum;
    }

    public void setFaxNum(String faxNum) {
        this.faxNum = faxNum;
    }

    public String getLinkEmail() {
        return linkEmail;
    }

    public void setLinkEmail(String linkEmail) {
        this.linkEmail = linkEmail;
    }

}
