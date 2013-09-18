package com.mangocity.hotelweb.persistence;

import java.io.Serializable;

/**
 */
public class HotelInfoForWeb implements Serializable {

    private long hotelId;

    private String hotelName;

    private String chnAddress;

    private String hotelIntroduce;

    private String starType;

    private String hotelStar;

    private int starNum;

    private int starBody;

    private String commendType;

    private String hallPictureName;

    private String outPictureName;

    private String roomPictureName;

    public String getHallPictureName() {
        return hallPictureName;
    }

    public void setHallPictureName(String hallPictureName) {
        this.hallPictureName = hallPictureName;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getRoomPictureName() {
        return roomPictureName;
    }

    public void setRoomPictureName(String roomPictureName) {
        this.roomPictureName = roomPictureName;
    }

    public String getOutPictureName() {
        return outPictureName;
    }

    public void setOutPictureName(String outPictureName) {
        this.outPictureName = outPictureName;
    }

    public int getStarBody() {
        return starBody;
    }

    private void setStarBody(int starBody) {
        this.starBody = starBody;
    }

    public int getStarNum() {
        return starNum;
    }

    private void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public String getStarType() {
        return starType;
    }

    public void setStarType(String starType) {
        this.starType = starType;
        switch (Integer.parseInt(starType)) {
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

    public String getCommendType() {
        return commendType;
    }

    public void setCommendType(String commendType) {
        this.commendType = commendType;
    }

    public String getChnAddress() {
        return chnAddress;
    }

    public void setChnAddress(String chnAddress) {
        this.chnAddress = chnAddress;
    }

    public String getHotelIntroduce() {
        return hotelIntroduce;
    }

    public void setHotelIntroduce(String hotelIntroduce) {
        this.hotelIntroduce = hotelIntroduce;
    }

    public String getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(String hotelStar) {
        this.hotelStar = hotelStar;
    }

}
