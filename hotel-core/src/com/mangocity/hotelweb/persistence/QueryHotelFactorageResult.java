package com.mangocity.hotelweb.persistence;

import java.io.Serializable;

/**
 * 提供给各个渠道供应商使用，根据渠道ID查询出来的供应商信息
 * 
 * @author neil
 * 
 */
public class QueryHotelFactorageResult implements Serializable {
    private long memberid; // 渠道id

    private String telephone; // 联系电话

    private String email;// 电邮

    private String fax; // 传真

    private String logo; // 渠道logo

    private String title; // 页头title

    private String bgColor; // 背景颜色

    private String color; // 行颜色

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public long getMemberid() {
        return memberid;
    }

    public void setMemberid(long memberid) {
        this.memberid = memberid;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

}
