package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 * 酒店的联系人信息
 * 
 * @author xiaowumi
 * 
 */
public class HtlCtct extends CEntity implements Entity {

    private Long ID;
    
    //主key
    private Long ctctId;
    /**
     * 中文名称
     */
    private String ctctchnName;

    /**
     * 联系人类型(部门)
     */
    private String ctctType;

    /**
     * 英文名
     */
    private String ctctengName;

    /**
     * 电话
     */
    private String ctcttelephone;

    /**
     * 联系人时间
     */
    private String ctctheadtime;

    /**
     * 手机
     */
    private String ctctMobile;

    /**
     * 电邮
     */
    private String ctctemail;

    /**
     * 传真
     */
    private String ctctfax;

    /**
     * 职务描述
     */
    private String ctctheadship;

    private HtlHotel htlHotel;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;

    }

    public String getCtctchnName() {
        return ctctchnName;
    }

    public void setCtctchnName(String ctctchnName) {
        this.ctctchnName = ctctchnName;
    }

    public String getCtctemail() {
        return ctctemail;
    }

    public void setCtctemail(String ctctemail) {
        this.ctctemail = ctctemail;
    }

    public String getCtctengName() {
        return ctctengName;
    }

    public void setCtctengName(String ctctengName) {
        this.ctctengName = ctctengName;
    }

    public String getCtctfax() {
        return ctctfax;
    }

    public void setCtctfax(String ctctfax) {
        this.ctctfax = ctctfax;
    }

    public String getCtctheadship() {
        return ctctheadship;
    }

    public void setCtctheadship(String ctctheadship) {
        this.ctctheadship = ctctheadship;
    }

    /*
     * public long getHotelID() { return hotelID; } public void setHotelID(long hotelID) {
     * this.hotelID = hotelID; }
     */
    public String getCtcttelephone() {
        return ctcttelephone;
    }

    public void setCtcttelephone(String ctcttelephone) {
        this.ctcttelephone = ctcttelephone;
    }

    public String getCtctType() {
        return ctctType;
    }

    public void setCtctType(String ctctType) {
        this.ctctType = ctctType;
    }

    public String getCtctMobile() {
        return ctctMobile;
    }

    public void setCtctMobile(String ctctMobile) {
        this.ctctMobile = ctctMobile;
    }

    public String getCtctheadtime() {
        return ctctheadtime;
    }

    public void setCtctheadtime(String ctctheadtime) {
        this.ctctheadtime = ctctheadtime;
    }

	public Long getCtctId() {
		return ctctId;
	}

	public void setCtctId(Long ctctId) {
		this.ctctId = ctctId;
	}

}
