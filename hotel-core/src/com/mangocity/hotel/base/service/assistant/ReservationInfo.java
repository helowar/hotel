package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 预订信息
 */
public class ReservationInfo implements Serializable {

    /**
     * 总的担保类型
     */
    private int suretyType = 0;

    /**
     * 每天具体的担保类型和担保<br>
     * 元素类型定义： objects[0] : 日期 objects[1] : 担保类型 objects[2] : 是否向酒店出具担保函 objects[3] : 不接受取消和修改
     * objects[4] : 担保条件
     */
    private List<Object[]> suretyDetails = new ArrayList();

    /**
     * 是否需要担保
     */
    private boolean needCredit;

    /**
     * 
     * 酒店提示信息
     */
    private String note;

    /**
     * 取消扣款类型
     */
    private String cardCancelDeductType;

    /**
     * 修改扣款类型
     */
    private String cardModifyDeductType;

    /**
     * 取消 扣款额
     */
    private double cardCancelAmount;

    /**
     * 修改 扣款额
     */
    private double cardModifyAmount;

    /**
     * 促销信息
     */
    private List presaleInfo = new ArrayList();

    // 预定条款起止日期
    private Date beginDate;

    private Date endDate;

    // 担保条件
    private String assureConditions;

    // 担保条款的记录数
    private int itemCounts;

    public String getAssureConditions() {
        return assureConditions;
    }

    public void setAssureConditions(String assureConditions) {
        this.assureConditions = assureConditions;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /** getter and setter begin */

    public boolean isNeedCredit() {
        return needCredit;
    }

    public void setNeedCredit(boolean needCredit) {
        this.needCredit = needCredit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List getPresaleInfo() {
        return presaleInfo;
    }

    public void setPresaleInfo(List presaleInfo) {
        this.presaleInfo = presaleInfo;
    }

    public int getSuretyType() {
        return suretyType;
    }

    public void setSuretyType(int suretyType) {
        this.suretyType = suretyType;
    }

    public List<Object[]> getSuretyDetails() {
        return suretyDetails;
    }

    public void setSuretyDetails(List<Object[]> suretyDetails) {
        this.suretyDetails = suretyDetails;
    }

    public double getCardCancelAmount() {
        return cardCancelAmount;
    }

    public void setCardCancelAmount(double cardCancelAmount) {
        this.cardCancelAmount = cardCancelAmount;
    }

    public String getCardCancelDeductType() {
        return cardCancelDeductType;
    }

    public void setCardCancelDeductType(String cardCancelDeductType) {
        this.cardCancelDeductType = cardCancelDeductType;
    }

    public double getCardModifyAmount() {
        return cardModifyAmount;
    }

    public void setCardModifyAmount(double cardModifyAmount) {
        this.cardModifyAmount = cardModifyAmount;
    }

    public String getCardModifyDeductType() {
        return cardModifyDeductType;
    }

    public void setCardModifyDeductType(String cardModifyDeductType) {
        this.cardModifyDeductType = cardModifyDeductType;
    }

    public int getItemCounts() {
        return itemCounts;
    }

    public void setItemCounts(int itemCounts) {
        this.itemCounts = itemCounts;
    }

    /** getter and setter end */
}
