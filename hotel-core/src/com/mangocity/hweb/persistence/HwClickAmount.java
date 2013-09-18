package com.mangocity.hweb.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 
 * 酒店网站点击量统计表
 * 
 * @author chenkeming
 */
public class HwClickAmount implements Entity {

    private static final long serialVersionUID = 5609816099221357564L;

    /**
     * 主键 <pk>
     */
    private Long ID;

    /**
     * 酒店ID
     */
    private String hotelId;

    /**
     * 客户IP地址
     */
    private String ipAddress;

    /**
     * 点击日期 yyyy-MM-dd
     */
    private Date clickDate;

    /**
     * 点击类型, 默认值是'1'
     */
    private String clickType;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getClickType() {
        return clickType;
    }

    public void setClickType(String clickType) {
        this.clickType = clickType;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getClickDate() {
        return clickDate;
    }

    public void setClickDate(Date clickDate) {
        this.clickDate = clickDate;
    }
}
