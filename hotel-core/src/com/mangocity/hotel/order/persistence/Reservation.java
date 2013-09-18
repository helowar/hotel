/**
 * 
 *  预订规则
 */
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class Reservation implements Entity {

    // ID <pk>
    private Long ID;

    // 最晚取消时间
    private Date allowCancelTime;

    // 预付付款时限
    private Date advancePayTime;
    
    // 最晚修改时间
    private Date allowModifyTime;
    
    // 是否接受修改和取消
    private String allowModify;

    // 酒店促销信息
    private String hotelPresale;

    // 酒店结算方式
    private String balanceMode;

    // 市场促销信息
    private String marketPresale;

    // 最晚担保时间
    private String lateSuretyTime;

    public Date getAdvancePayTime() {
        return advancePayTime;
    }

    public void setAdvancePayTime(Date advancePayTime) {
        this.advancePayTime = advancePayTime;
    }

    public Date getAllowCancelTime() {
        return allowCancelTime;
    }

    public void setAllowCancelTime(Date allowCancelTime) {
        this.allowCancelTime = allowCancelTime;
    }

    public String getAllowModify() {
        return allowModify;
    }

    public void setAllowModify(String allowModify) {
        this.allowModify = allowModify;
    }

    public Date getAllowModifyTime() {
        return allowModifyTime;
    }

    public void setAllowModifyTime(Date allowModifyTime) {
        this.allowModifyTime = allowModifyTime;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long attribute186) {
        this.ID = attribute186;
    }

    public String getBalanceMode() {
        return balanceMode;
    }

    public void setBalanceMode(String balanceMode) {
        this.balanceMode = balanceMode;
    }

    public String getHotelPresale() {
        return hotelPresale;
    }

    public void setHotelPresale(String hotelPresale) {
        this.hotelPresale = hotelPresale;
    }

    public String getLateSuretyTime() {
        return lateSuretyTime;
    }

    public void setLateSuretyTime(String lateSuretyTime) {
        this.lateSuretyTime = lateSuretyTime;
    }

    public String getMarketPresale() {
        return marketPresale;
    }

    public void setMarketPresale(String marketPresale) {
        this.marketPresale = marketPresale;
    }


}
