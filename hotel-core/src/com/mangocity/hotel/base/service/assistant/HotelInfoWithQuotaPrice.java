package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 * 用来返回CC查询酒店的结果，包括了配额与价格
 * 
 * @author xiaowumi
 * 
 */
public class HotelInfoWithQuotaPrice implements Serializable {

    /**
     * 酒店ID
     */
    private String hotelId;

    /**
     * 酒店CD
     */
    private String hotelCd;

    /**
     * 酒店状态
     */
    private String hotelStatus;

    /**
     * 酒店中文名
     */
    private String hotelChnName;

    /**
     * 酒店英文名
     */
    private String hotelEngName;

    /**
     * 城区
     */
    private String zone;

    /**
     * 商区
     */
    private String bizZone;

    /**
     * 城市
     */
    private String city;

    /**
     * 酒店星级
     */
    private String hotelStar;

    /**
     * 酒店类型
     */
    private String hotelType;

    /**
     * 中文地址
     */
    private String chnAddress;

    /**
     * 英文地址
     */
    private String engAddress;

    /**
     * 酒店简介
     */
    private String hotelIntroduce;

    /**
     * 酒店生动生成的简介
     */
    private String autoIntroduce;

    /**
     * 提示信息
     */
    private String clueInfo;

    /**
     * 销售渠道
     */
    private String saleChannel;

    /**
     * 房间ID
     */
    private String roomId;

    /**
     * 房型ID
     */
    private String roomTypeId;

    /**
     * 是否含早
     */
    private String incBreakfast;

    /**
     * 含早类型
     */
    private String breakfastType;

    /**
     * 房态
     */
    private String roomState;

    /**
     * 价格id
     */
    private String priceId;

    /**
     * 销售价
     */
    private String salePrice;

    /**
     * 付款方式
     */
    private String payMethod;

    /**
     * 币种
     */
    private String currency;

    /**
     * 配额ID
     */
    private String quotaId;

    /**
     * 配额类型
     */
    private String quotaType;

    /**
     * 前台看到的未使用配额
     */
    private String availQty;

    /**
     * 可销售配额
     */
    private String ableQty;

    /**
     * 已经释放的配额
     */
    private String freeQty;

    /**
     * 配额模式
     */
    private String quotaPattern;

    /**
     * 共享方式
     */
    private String sharetype;

    /**
     * 价格类型(子房型)
     */
    private String pricetype;

    /**
     * 推荐级别
     */
    private String commlevel;

    /**
     * 房型名称
     */
    private String roomName;

    /**
     * 子房型ID
     */
    private String childRoomTypeId;

    /*
     * 可售日期
     */
    private Date ableSaleDate;

    public String getAbleQty() {
        return ableQty;
    }

    public void setAbleQty(String ableQty) {
        this.ableQty = ableQty;
    }

    public String getAutoIntroduce() {
        return autoIntroduce;
    }

    public void setAutoIntroduce(String autoIntroduce) {
        this.autoIntroduce = autoIntroduce;
    }

    public String getAvailQty() {
        return availQty;
    }

    public void setAvailQty(String availQty) {
        this.availQty = availQty;
    }

    public String getBizZone() {
        return bizZone;
    }

    public void setBizZone(String bizZone) {
        this.bizZone = bizZone;
    }

    public String getBreakfastType() {
        return breakfastType;
    }

    public void setBreakfastType(String breakfastType) {
        this.breakfastType = breakfastType;
    }

    public String getChnAddress() {
        return chnAddress;
    }

    public void setChnAddress(String chnAddress) {
        this.chnAddress = chnAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getClueInfo() {
        return clueInfo;
    }

    public void setClueInfo(String clueInfo) {
        this.clueInfo = clueInfo;
    }

    public String getCommlevel() {
        return commlevel;
    }

    public void setCommlevel(String commlevel) {
        this.commlevel = commlevel;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEngAddress() {
        return engAddress;
    }

    public void setEngAddress(String engAddress) {
        this.engAddress = engAddress;
    }

    public String getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(String freeQty) {
        this.freeQty = freeQty;
    }

    public String getHotelCd() {
        return hotelCd;
    }

    public void setHotelCd(String hotelCd) {
        this.hotelCd = hotelCd;
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

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
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

    public String getHotelStatus() {
        return hotelStatus;
    }

    public void setHotelStatus(String hotelStatus) {
        this.hotelStatus = hotelStatus;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public String getIncBreakfast() {
        return incBreakfast;
    }

    public void setIncBreakfast(String incBreakfast) {
        this.incBreakfast = incBreakfast;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getPricetype() {
        return pricetype;
    }

    public void setPricetype(String pricetype) {
        this.pricetype = pricetype;
    }

    public String getQuotaId() {
        return quotaId;
    }

    public void setQuotaId(String quotaId) {
        this.quotaId = quotaId;
    }

    public String getQuotaPattern() {
        return quotaPattern;
    }

    public void setQuotaPattern(String quotaPattern) {
        this.quotaPattern = quotaPattern;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomState() {
        return roomState;
    }

    public void setRoomState(String roomState) {
        this.roomState = roomState;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getSaleChannel() {
        return saleChannel;
    }

    public void setSaleChannel(String saleChannel) {
        this.saleChannel = saleChannel;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getSharetype() {
        return sharetype;
    }

    public void setSharetype(String sharetype) {
        this.sharetype = sharetype;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(String childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public Date getAbleSaleDate() {
        return ableSaleDate;
    }

    public void setAbleSaleDate(Date ableSaleDate) {
        this.ableSaleDate = ableSaleDate;
    }
}
