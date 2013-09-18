package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class QueryRoomControl extends CEntity implements Entity {

    private Long ID;

    // 酒店id
    private long hotelID;

    // 合同id
    private long contractid;

    // 酒店名称
    private String hotelName;

    // 合同名称
    private String contractName;

    // 合同类型
    private String contracttype;

    // 合同开始时间
    private Date begindate;

    // 合同结束时间
    private Date enddate;

    // 城市名称
    private String cityname;

    // 城区名称
    private String zonename;

    // 酒店星级
    private String hotelstar;

    // 酒店推荐级别
    private String goldType;

    public Date getBegindate() {
        return begindate;
    }

    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public long getContractid() {
        return contractid;
    }

    public void setContractid(long contractid) {
        this.contractid = contractid;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getGoldType() {
        return goldType;
    }

    public void setGoldType(String goldType) {
        this.goldType = goldType;
    }

    public long getHotelID() {
        return hotelID;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelstar() {
        return hotelstar;
    }

    public void setHotelstar(String hotelstar) {
        this.hotelstar = hotelstar;
    }

    public String getZonename() {
        return zonename;
    }

    public void setZonename(String zonename) {
        this.zonename = zonename;
    }

    public String getContracttype() {
        return contracttype;
    }

    public void setContracttype(String contracttype) {
        this.contracttype = contracttype;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

}
