package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * HtlFreeOperate generated by MyEclipse - Hibernate Tools 释放配额操作对象
 */

public class HtlFreeOperate implements Entity {

    // Fields
    // 释放操作id
    private Long ID;

    // 采购批次id
    private long quotaBatchId;

    // 配额共享方式
    private String shareType;

    /**
     * 配额类型
     */
    private String quotaType;

    /**
     * 房型id
     */
    private long roomTypeId;

    // 合同id
    private long contractId;

    // 酒店id
    private long hotelId;

    // 开始日期
    private Date beginDate;

    // 结束日期
    private Date endDate;

    // 调整周
    private String adjustWeek;

    // 面付释放配额数量
    private int freeQty;

    // 面付共享数量
    private int shareQty;

    // 预付释放配额数量
    private int preFreeQty;

    // 预付共享数量
    private int preShareQty;

    // 创建人
    private String createBy;

    // 最近修改人
    private String modifyBy;

    // 创建时间
    private Date createTime;

    // 最近修改时间
    private Date modifyTime;

    // 加幅列表
    private List lstAddscope = new ArrayList();

    // 分配列表
    private List lstBatchAssign = new ArrayList();

    // Constructors

    public List getLstAddscope() {
        return lstAddscope;
    }

    public void setLstAddscope(List lstAddscope) {
        this.lstAddscope = lstAddscope;
    }

    public List getLstBatchAssign() {
        return lstBatchAssign;
    }

    public void setLstBatchAssign(List lstBatchAssign) {
        this.lstBatchAssign = lstBatchAssign;
    }

    /** default constructor */
    public HtlFreeOperate() {
    }

    public String getAdjustWeek() {
        return adjustWeek;
    }

    public void setAdjustWeek(String adjustWeek) {
        this.adjustWeek = adjustWeek;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getQuotaBatchId() {
        return quotaBatchId;
    }

    public void setQuotaBatchId(long quotaBatchId) {
        this.quotaBatchId = quotaBatchId;
    }

    public int getShareQty() {
        return shareQty;
    }

    public void setShareQty(int shareQty) {
        this.shareQty = shareQty;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public int getPreFreeQty() {
        return preFreeQty;
    }

    public void setPreFreeQty(int preFreeQty) {
        this.preFreeQty = preFreeQty;
    }

    public int getPreShareQty() {
        return preShareQty;
    }

    public void setPreShareQty(int preShareQty) {
        this.preShareQty = preShareQty;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

}