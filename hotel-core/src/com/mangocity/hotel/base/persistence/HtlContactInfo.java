package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 * 集团酒店及品牌用到的联系信息抽象类
 * 
 * @author xiaowumi
 * 
 */
public class HtlContactInfo extends CEntity implements Entity {
    /**
     * 联系信息ID
     */
    private Long ID;

    /**
     * 分支机构名称
     */
    private String organName;

    /**
     * 通信地址
     */
    private String address;

    /**
     * 电话
     */
    private String telephone;

    /**
     * 传真
     */
    private String fax;

    /**
     * 电邮
     */
    private String email;

    /**
     * 网址
     */
    private String website;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactTelephone;

    /**
     * 联系人传真
     */
    private String contactFax;

    /**
     * 联系人电邮
     */
    private String contactEmail;

    /**
     * 联系人手机
     */
    private String contactMobile;

    /**
     * 是总部，还是中国总部 ，还是分支机构
     */
    private String communicateType;

    /**
     * 酒店数量
     */
    private String hotelNumber;

    /**
     * 品牌酒店数量
     */
    private String brandHotelNumber;

    /**
     * 未签约酒店数量
     */
    private String nocontractHotelNumber;
    
    /**
     * 酒店品牌介绍
     */
    private String brandIntroduce;

    public String getCommunicateType() {
        return communicateType;
    }

    public void setCommunicateType(String communicateType) {
        this.communicateType = communicateType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactFax() {
        return contactFax;
    }

    public void setContactFax(String contactFax) {
        this.contactFax = contactFax;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTelephone() {
        return contactTelephone;
    }

    public void setContactTelephone(String contactTelephone) {
        this.contactTelephone = contactTelephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getBrandHotelNumber() {
        return brandHotelNumber;
    }

    public void setBrandHotelNumber(String brandHotelNumber) {
        this.brandHotelNumber = brandHotelNumber;
    }

    public String getHotelNumber() {
        return hotelNumber;
    }

    public void setHotelNumber(String hotelNumber) {
        this.hotelNumber = hotelNumber;
    }

    public String getNocontractHotelNumber() {
        return nocontractHotelNumber;
    }

    public void setNocontractHotelNumber(String nocontractHotelNumber) {
        this.nocontractHotelNumber = nocontractHotelNumber;
    }

	public String getBrandIntroduce() {
		return brandIntroduce;
	}

	public void setBrandIntroduce(String brandIntroduce) {
		this.brandIntroduce = brandIntroduce;
	}

}
