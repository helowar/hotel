package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;

/**
 */
public class ContractQueryCondition implements Serializable {
    // 酒店中文名称
    private String hotelChnName;

    // 酒店英文名称
    private String hotelEngName;

    // 酒店类型
    private String hotelType;

    // 酒店星级
    private String hotelStar;

    // 所在城市
    private Long cityId;

    // 所在区域(省份)
    private Long stateId;

    // 所在城区
    private Long zoneId;

    // 酒店编码
    private String hotelCode;

    // 合同编码
    private String contractCode;

    // 合约到期日
    private String contractMaturity;

    // 网站是否显示
    private String websiteDisplay;

    // 前台是否可卖
    private String ccCanSale;

    // 网站是否显预付价
    private String displayPrePrice;

    public String getCcCanSale() {
        return ccCanSale;
    }

    public void setCcCanSale(String ccCanSale) {
        this.ccCanSale = ccCanSale;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractMaturity() {
        return contractMaturity;
    }

    public void setContractMaturity(String contractMaturity) {
        this.contractMaturity = contractMaturity;
    }

    public String getDisplayPrePrice() {
        return displayPrePrice;
    }

    public void setDisplayPrePrice(String displayPrePrice) {
        this.displayPrePrice = displayPrePrice;
    }

    public String getHotelChnName() {
        return hotelChnName;
    }

    public void setHotelChnName(String hotelChnName) {
        this.hotelChnName = hotelChnName;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
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
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getWebsiteDisplay() {
        return websiteDisplay;
    }

    public void setWebsiteDisplay(String websiteDisplay) {
        this.websiteDisplay = websiteDisplay;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }
}
