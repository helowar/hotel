package com.mangocity.hotelweb.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class QueryHotelForWebBean implements Serializable {
    // 城市ＩＤ
    private String cityId;

    private String cityName;

    private long hotelId;

    // 酒店名称
    private String hotelName;

    private String district;

    private String bizZone;

    private String hotelChnName;

    private String hotelEngName;

    private int pageNo;

    // 入住日期
    private Date inDate;

    // 离店日期
    private Date outDate;

    // 价格范围
    private String priceStr;

    // 最低价
    private String minPrice;

    // 最高价
    private String maxPrice;

    // 酒店星级
    private String hotelStar;

    // 多少条记录
    private int pageSize = 10;

    // 当前页
    private int pageIndex = 1;

    // 总页数
    private int totalIndex;

    // 芒果推荐1 价格2 星级3
    private int qrymethod = 1;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }

    public String getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(String hotelStar) {
        this.hotelStar = hotelStar;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotalIndex() {
        return totalIndex;
    }

    public void setTotalIndex(int totalIndex) {
        this.totalIndex = totalIndex;
    }

    public int getQrymethod() {
        return qrymethod;
    }

    public void setQrymethod(int qrymethod) {
        this.qrymethod = qrymethod;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getBizZone() {
        return bizZone;
    }

    public void setBizZone(String bizZone) {
        this.bizZone = bizZone;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

}
