package com.mangocity.hotelweb.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class QueryHotelForWebResult implements Serializable {
    // 酒店id
    private long hotelId;

    // 酒店中文名称
    private String hotelChnName;

    // 酒店英文名称
    private String hotelEngName;

    // 酒店星级
    private String hotelStar;

    // 酒店类型
    private String hotelType;

    // 酒店中文地址
    private String chnAddress;

    // 酒店英文地址
    private String engAddress;

    // 酒店介绍
    private String hotelIntroduce;

    // 提示信息
    private String clueInfo;

    // 取消信息
    private String cancelMessage;

    private String room_state;

    private String childRoomTypeId;

    private String roomName;

    private String quotaType;

    private String payMethod;

    private String able_sale_date;

    private long room_type_id;

    private String pictureType;

    private String pictureName;

    private Double salesPrice;

    private Double salesRoomPrice;

    private String priceType;

    // 地区
    private String bizZone;

    // 推荐级别
    private String commendType;

    private String city;

    private String autoIntroduce;

    private String hotelPicture;

    private int breakfastType; // 早餐类型

    private int breakfastNum; // 早餐数量

    private String close_flag; // 开关房标志

    private String reason; // 关房原因

    // 酒店星级数目
    private int starNum;

    // 酒店星级类型 1代表实心mango 2代表准星级酒店空心mango
    private int starBody;

    // 房间列表，是RoomType类的集合
    private List roomTypes = new ArrayList();

    private String hallPictureName;

    private String outPictureName;

    private String roomPictureName;

    // 酒店LOGO照片
    private String hotelLogo;

    // 3D照片数量
    private int sandNum;

    /**
     * 周行数
     */
    private int weekTotal;

    /**
     * 显示行的宽度
     */
    private String avgWidthStr;

    /**
     * 行显示的占用列数
     */
    private int colCount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 房态
     */
    private String roomState;

    /**
     * 周天数
     */
    private List weekNum = new ArrayList();

    /**
     * 页面是否显示全部房型
     */
    private int fx;

    /**
     * 汇率
     */
    private String rateStr;

    /*
     * 价格开始个数
     */
    private int priceId;

    private int endpriceId;

    public int getWeekTotal() {
        return weekTotal;
    }

    public void setWeekTotal(int weekTotal) {
        this.weekTotal = weekTotal;
    }

    public String getAvgWidthStr() {
        return avgWidthStr;
    }

    public void setAvgWidthStr(String avgWidthStr) {
        this.avgWidthStr = avgWidthStr;
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRoomState() {
        return roomState;
    }

    public void setRoomState(String roomState) {
        this.roomState = roomState;
    }

    public List getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(List weekNum) {
        this.weekNum = weekNum;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelChnName() {
        return hotelChnName;
    }

    public void setHotelChnName(String hotelChnName) {
        this.hotelChnName = hotelChnName;
    }

    public String getHotelEngName() {
        return hotelEngName;
    }

    public void setHotelEngName(String hotelEngName) {
        this.hotelEngName = hotelEngName;
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

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public String getChnAddress() {
        return chnAddress;
    }

    public void setChnAddress(String chnAddress) {
        this.chnAddress = chnAddress;
    }

    public String getEngAddress() {
        return engAddress;
    }

    public void setEngAddress(String engAddress) {
        this.engAddress = engAddress;
    }

    public String getHotelIntroduce() {
        return hotelIntroduce;
    }

    public void setHotelIntroduce(String hotelIntroduce) {
        this.hotelIntroduce = hotelIntroduce;
    }

    public String getClueInfo() {
        return clueInfo;
    }

    public void setClueInfo(String clueInfo) {
        this.clueInfo = clueInfo;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    public void setCancelMessage(String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    public String getBizZone() {
        return bizZone;
    }

    public void setBizZone(String bizZone) {
        this.bizZone = bizZone;
    }

    public List getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List roomTypes) {
        this.roomTypes = roomTypes;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAutoIntroduce() {
        return autoIntroduce;
    }

    public void setAutoIntroduce(String autoIntroduce) {
        this.autoIntroduce = autoIntroduce;
    }

    public String getHotelPicture() {
        return hotelPicture;
    }

    public void setHotelPicture(String hotelPicture) {
        this.hotelPicture = hotelPicture;
    }

    public int getFx() {
        return fx;
    }

    public void setFx(int fx) {
        this.fx = fx;
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

    public String getHallPictureName() {
        return hallPictureName;
    }

    public void setHallPictureName(String hallPictureName) {
        this.hallPictureName = hallPictureName;
    }

    public String getOutPictureName() {
        return outPictureName;
    }

    public void setOutPictureName(String outPictureName) {
        this.outPictureName = outPictureName;
    }

    public String getRoomPictureName() {
        return roomPictureName;
    }

    public void setRoomPictureName(String roomPictureName) {
        this.roomPictureName = roomPictureName;
    }

    public int getSandNum() {
        return sandNum;
    }

    public void setSandNum(int sandNum) {
        this.sandNum = sandNum;
    }

    public String getRateStr() {
        return rateStr;
    }

    public void setRateStr(String rateStr) {
        this.rateStr = rateStr;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public int getEndpriceId() {
        return endpriceId;
    }

    public void setEndpriceId(int endpriceId) {
        this.endpriceId = endpriceId;
    }

    public String getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(String childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public long getRoom_type_id() {
        return room_type_id;
    }

    public void setRoom_type_id(long room_type_id) {
        this.room_type_id = room_type_id;
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

    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getRoom_state() {
        return room_state;
    }

    public void setRoom_state(String room_state) {
        this.room_state = room_state;
    }

    public Double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getAble_sale_date() {
        return able_sale_date;
    }

    public void setAble_sale_date(String able_sale_date) {
        this.able_sale_date = able_sale_date;
    }

    public Double getSalesRoomPrice() {
        return salesRoomPrice;
    }

    public void setSalesRoomPrice(Double salesRoomPrice) {
        this.salesRoomPrice = salesRoomPrice;
    }

    public String getHotelLogo() {
        return hotelLogo;
    }

    public void setHotelLogo(String hotelLogo) {
        this.hotelLogo = hotelLogo;
    }

    public int getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(int breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public int getBreakfastType() {
        return breakfastType;
    }

    public void setBreakfastType(int breakfastType) {
        this.breakfastType = breakfastType;
    }

    public String getClose_flag() {
        return close_flag;
    }

    public void setClose_flag(String close_flag) {
        this.close_flag = close_flag;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
