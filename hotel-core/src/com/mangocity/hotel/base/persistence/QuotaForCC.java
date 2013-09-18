package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.DateUtil;
import com.mangocity.util.Entity;

/**
 * 配额
 */

public class QuotaForCC extends CEntity implements Entity {

    /**
     * 配额id
     */
    private Long ID;

    /*
     * 房间id
     */
    private long roomId;

    /**
     * 采购批次ID
     */
    private long quotaBatchId;

    /**
     * 配额类型
     */
    private String quotaType;

    /**
     * 配额模式 (进店还是在店)
     */
    private String quotaPattern;

    /**
     * 酒店id
     */
    private long hotelId;

    /**
     * 合同id
     */
    private long contractId;

    /**
     * 可售日期
     */
    private Date ableSaleDate;

    /**
     * 星期
     */
    private int week;

    /**
     * 未使用配额数
     */
    private int availQty;

    /**
     * 可售配额总数 -----是指经过cutoff计算后的总数.
     */
    private int ableQty;

    /**
     * 呼出配额数
     */
    private int outsideQty;

    /**
     * 已释放配额数
     */
    private int freeQty;

    /**
     * 已使用配额数
     */
    private int usedQty;

    /**
     * 共享数量
     */
    private int shareQty;

    /**
     * 独占数量
     */
    private int privateQty;

    /**
     * 共享配额方式
     */
    private String shareType;

    /**
     * 配额是否可以回收
     */
    private boolean takebackQuota;

    // cutoff day
    private List lstCutOffDay = new ArrayList();

    // 分配表
    private List lstAssign = new ArrayList();

    // Constructors

    public List getLstAssign() {
        return lstAssign;
    }

    public void setLstAssign(List lstAssign) {
        this.lstAssign = lstAssign;
    }

    /** default constructor */
    public QuotaForCC() {
    }

    public int getAbleQty() {
        return ableQty;
    }

    public void setAbleQty(int ableQty) {
        this.ableQty = ableQty;
    }

    public Date getAbleSaleDate() {
        return ableSaleDate;
    }

    public void setAbleSaleDate(Date ableSaleDate) {
        this.week = DateUtil.getWeekOfDate(ableSaleDate);
        this.ableSaleDate = ableSaleDate;
    }

    public int getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(int freeQty) {
        this.freeQty = freeQty;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public int getOutsideQty() {
        return outsideQty;
    }

    public void setOutsideQty(int outsideQty) {
        this.outsideQty = outsideQty;
    }

    public int getPrivateQty() {
        return privateQty;
    }

    public void setPrivateQty(int privateQty) {
        this.privateQty = privateQty;
    }

    public int getShareQty() {
        return shareQty;
    }

    public void setShareQty(int shareQty) {
        this.shareQty = shareQty;
    }

    public int getUsedQty() {
        return usedQty;
    }

    public void setUsedQty(int usedQty) {
        this.usedQty = usedQty;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public List getLstCutOffDay() {
        return lstCutOffDay;
    }

    public void setLstCutOffDay(List lstCutOffDay) {
        this.lstCutOffDay = lstCutOffDay;
    }

    public long getQuotaBatchId() {
        return quotaBatchId;
    }

    public void setQuotaBatchId(long quotaBatchId) {
        this.quotaBatchId = quotaBatchId;
    }

    public int getAvailQty() {
        return availQty;
    }

    public void setAvailQty(int availQty) {
        this.availQty = availQty;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public boolean isTakebackQuota() {
        return takebackQuota;
    }

    public void setTakebackQuota(boolean takebackQuota) {
        this.takebackQuota = takebackQuota;
    }

    public String getQuotaPattern() {
        return quotaPattern;
    }

    public void setQuotaPattern(String quotaPattern) {
        this.quotaPattern = quotaPattern;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

}