package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 酒店合同的促销信息 HtlSalesPromo generated by MyEclipse - Hibernate Tools
 */

public class HtlSalesPromo extends CEntity implements Entity {

    //     

    private Long ID;

    // 合同ID
    private Long contractID;

    // 促销类型
    private String salePromoType;

    // 名字叫房型，但存的是价格类型ID
    private String roomType;

    // 促销内容
    private String salePromoCont;

    // 促销起始时间
    private Date beginDate;

    // 促销终止时间
    private Date endDate;

    // 房刑名称，在hibern配置文件中不定义
    private String roomTypeName;

    /**
     * 不在外网显示<br>
     * 默认为不显示 0 '0' : 不显示<br>
     * '1' : 显示
     */
    private String webShow;

    // Constructors

    /** default constructor */
    public HtlSalesPromo() {
    }

    // Property accessors

    public String getSalePromoType() {
        return this.salePromoType;
    }

    public void setSalePromoType(String salePromoType) {
        this.salePromoType = salePromoType;
    }

    public String getWebShow() {
        return webShow;
    }

    public void setWebShow(String webShow) {
        this.webShow = webShow;
    }

    public String getRoomType() {
        return this.roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getSalePromoCont() {
        return this.salePromoCont;
    }

    public void setSalePromoCont(String salePromoCont) {
        this.salePromoCont = salePromoCont;
    }

    public Date getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Long getContractID() {
        return contractID;
    }

    public void setContractID(Long contractID) {
        this.contractID = contractID;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

}