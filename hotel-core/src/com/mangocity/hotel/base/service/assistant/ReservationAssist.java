package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 */
public class ReservationAssist implements Serializable {

    /**
     * 总的担保类型
     */
    private int suretyType = 0;

    /**
     * 每天具体的担保类型和担保<br>
     * 元素类型定义： objects[0] : 日期 objects[1] : 担保类型 objects[2] : 是否向酒店出具担保函 objects[3] : 担保条件
     */
    private List<Object[]> suretyDetails = new ArrayList<Object[]>();

    /**
     * 每天具体的担保类型和担保<br>
     * 元素类型定义： AssureInforAssistant每天具体的担保类型 assureInforList是整个单的担保明细
     */
    private List<AssureInforAssistant> assureInforList = new ArrayList<AssureInforAssistant>();

    /**
     * 每天的预订条款详情 元素 Object[0]:日期 Object[1]:在某日期某时间点之前 Object[2]:提前N天，在某时间点之前 Object[3]:连住N天
     * Object[4]:必住日期，用逗号隔开 Object[5]:面付担保条件/预付结算方法 Object[6]:面付担保类型/预付付款时限
     * 
     */
    private List<Object[]> bookPrompt = new ArrayList<Object[]>();

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

    /**
     * 担保条件
     */
    private String assureConditions;

    /**
     * 担保条款的记录数
     */
    private int itemCounts;

    /**
     * 最早不需要担保/预付日期
     */
    private Date earliestNoPayDate;

    /**
     * 最早不需要担保/预付时间
     */
    private String earliestNoPayTime;

    /**
     * 取消修改显示类型1、不接受免费取消2、不接受免费修改3、不接受免费取消或修4、请您务必于某日期前提出修改取消
     */
    private String cancmodiType;

    public String getCancmodiType() {
        return cancmodiType;
    }

    public void setCancmodiType(String cancmodiType) {
        this.cancmodiType = cancmodiType;
    }

    public String getAssureConditions() {
        return assureConditions;
    }

    public void setAssureConditions(String assureConditions) {
        this.assureConditions = assureConditions;
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

    public List<Object[]> getSuretyDetails() {
        return suretyDetails;
    }

    public void setSuretyDetails(List<Object[]> suretyDetails) {
        this.suretyDetails = suretyDetails;
    }

    public int getSuretyType() {
        return suretyType;
    }

    public void setSuretyType(int suretyType) {
        this.suretyType = suretyType;
    }

    public List<Object[]> getBookPrompt() {
        return bookPrompt;
    }

    public void setBookPrompt(List<Object[]> bookPrompt) {
        this.bookPrompt = bookPrompt;
    }

    public Date getEarliestNoPayDate() {
        return earliestNoPayDate;
    }

    public void setEarliestNoPayDate(Date earliestNoPayDate) {
        this.earliestNoPayDate = earliestNoPayDate;
    }

    public String getEarliestNoPayTime() {
        return earliestNoPayTime;
    }

    public void setEarliestNoPayTime(String earliestNoPayTime) {
        this.earliestNoPayTime = earliestNoPayTime;
    }

    public List<AssureInforAssistant> getAssureInforList() {
        return assureInforList;
    }

    public void setAssureInforList(List<AssureInforAssistant> assureInforList) {
        this.assureInforList = assureInforList;
    }

}
