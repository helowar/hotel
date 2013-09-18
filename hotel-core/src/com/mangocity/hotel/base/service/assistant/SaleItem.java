package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售项 主要是支付方式和配额类型 支付方式分为面付、预付 配额类型有包房、普通配额等
 * 
 * @author zhengxin
 * 
 */
public class SaleItem implements Serializable {
	
	private static final long serialVersionUID = 2845315897918623982L;
	
    /**
     * 配额类型
     */
    private String quotaType;

    /**
     * 支付方法
     */
    private String payMethod;

    /**
     * 有一天配额为0时标志为真
     */
    private boolean quotaBool = false;

    // 所有床型房态均为不可超标志为真
    private boolean bedStateOneBool = false;

    private boolean bedStateTwoBool = false;

    private boolean bedStateThrBool = false;

    /**
     * 入住到离店间夜里最小配额数
     */
    private int quotaLeastNum = 1000;

    /**
     * 房间价格明细, RoomInfo类
     */
    private List roomItems = new ArrayList();

    /**
     * 预订按钮显示名称 ‘0’表示面付 ‘1’表示预付 ‘2’表示担保 add by zhineng.zhuang 2009-02-10 酒店2.6 RMS2388 预订按钮显示
     */
    private String bookButton;

    /**
     *预订提示：在某日期之前预订，且要预订此房型，必须连住4晚，且入住日期必须包括某天。 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private String bookHintNotMeet;

    /**
     * 该支付方式是否有预订条款, 供cc前台查酒店用
     * 
     * @author chenkeming Mar 5, 2009 8:55:39 AM
     */
    private boolean hasReserv = false;

    /**
     * 面付转预付, 必须:1, 允许:2, 不许:3
     * 
     * @author shengwei.zuo 2009-04-01
     */
    private int payToPrepay;

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public List getRoomItems() {
        return roomItems;
    }

    public void setRoomItems(List roomItems) {
        this.roomItems = roomItems;
    }

    public int getWeekNum() {
        return (this.roomItems.size() - 1) / 7 + 1;
    }

    public boolean isQuotaBool() {
        return quotaBool;
    }

    public void setQuotaBool(boolean quotaBool) {
        this.quotaBool = quotaBool;
    }

    public boolean isBedStateOneBool() {
        return bedStateOneBool;
    }

    public void setBedStateOneBool(boolean bedStateOneBool) {
        this.bedStateOneBool = bedStateOneBool;
    }

    public boolean isBedStateTwoBool() {
        return bedStateTwoBool;
    }

    public void setBedStateTwoBool(boolean bedStateTwoBool) {
        this.bedStateTwoBool = bedStateTwoBool;
    }

    public boolean isBedStateThrBool() {
        return bedStateThrBool;
    }

    public void setBedStateThrBool(boolean bedStateThrBool) {
        this.bedStateThrBool = bedStateThrBool;
    }

    public int getQuotaLeastNum() {
        return quotaLeastNum;
    }

    public void setQuotaLeastNum(int quotaLeastNum) {
        this.quotaLeastNum = quotaLeastNum;
    }

    public String getBookButton() {
        return bookButton;
    }

    public void setBookButton(String bookButton) {
        this.bookButton = bookButton;
    }

    public String getBookHintNotMeet() {
        return bookHintNotMeet;
    }

    public void setBookHintNotMeet(String bookHintNotMeet) {
        this.bookHintNotMeet = bookHintNotMeet;
    }

    public boolean isHasReserv() {
        return hasReserv;
    }

    public void setHasReserv(boolean hasReserv) {
        this.hasReserv = hasReserv;
    }

    public int getPayToPrepay() {
        return payToPrepay;
    }

    public void setPayToPrepay(int payToPrepay) {
        this.payToPrepay = payToPrepay;
    }

}
