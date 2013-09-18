package com.mangocity.hotelweb.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class HotelInfoForWebBean implements Serializable {

    // 酒店名称
    private String chnName;

    // 酒店id
    private long hotelId;

    // 酒店星级
    private String hotelStar;

    // 酒店简介
    private String hotelIntroduce;

    // 酒店地址
    private String chnAddress;

    // 酒店电话
    private String telephone;

    // 酒店外观图片
    private String pictureName;

    // 酒店加床价
    private double addPricebed;

    // 酒店可用信用卡类型
    private String creditCard;

    // 酒店规定入住时间
    private String checkInTime;

    // 规定退房时间
    private String checkOutTime;

    // 酒店付费说明
    // 酒店星级数目
    private int starNum;

    // 酒店星级类型
    private int starBody;

    // 酒店价格列表
    private List<QueryHotelForWebRoomType> priceType = new ArrayList<QueryHotelForWebRoomType>();

    // 主推类型
    private String commendType;

    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(String hotelStar) {
        this.hotelStar = hotelStar;
        switch (Integer.parseInt(hotelStar)) {
        case 19:
            setStarNum(5);
            setStarBody(1);
            break;
        case 29:
            setStarNum(5);
            setStarBody(2);
            break;
        case 39:
            setStarNum(4);
            setStarBody(1);
            break;
        case 49:
            setStarNum(4);
            setStarBody(2);
            break;
        case 59:
            setStarNum(3);
            setStarBody(1);
            break;
        // 加个准3星，modify by zhineng.zhuang
        case 64:
            setStarNum(3);
            setStarBody(2);
            break;
        case 69:
            setStarNum(2);
            setStarBody(1);
            break;
        default:
            break;
        }
    }

    public String getHotelIntroduce() {
        return hotelIntroduce;
    }

    public void setHotelIntroduce(String hotelIntroduce) {
        this.hotelIntroduce = hotelIntroduce;
    }

    public String getChnAddress() {
        return chnAddress;
    }

    public void setChnAddress(String chnAddress) {
        this.chnAddress = chnAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public double getAddPricebed() {
        return addPricebed;
    }

    public void setAddPricebed(double addPricebed) {
        this.addPricebed = addPricebed;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public List<QueryHotelForWebRoomType> getPriceType() {
        return priceType;
    }

    public void setPriceType(List<QueryHotelForWebRoomType> priceType) {
        this.priceType = priceType;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getCommendType() {
        return commendType;
    }

    public void setCommendType(String commendType) {
        this.commendType = commendType;
    }

    public int getStarNum() {
        return starNum;
    }

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public int getStarBody() {
        return starBody;
    }

    public void setStarBody(int starBody) {
        this.starBody = starBody;
    }

}
