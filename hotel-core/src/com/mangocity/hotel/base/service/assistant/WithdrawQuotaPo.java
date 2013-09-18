package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class WithdrawQuotaPo implements Serializable {

    // 扣配额日期
    private Date quotaDate;

    // 扣配额数量
    private int quotaNum;

    // 配额类型(目前四种配额 包房配额2，普通配额1，临时配额3,呼出配额4)
    private String quotaType;

    // 底价
    private double basePrice;

    // 门市价
    private double salesroomPrice;

    // 销售价
    private double salePrice;

    // 房态
    private String roomState;

    // 配额ID，如果是呼出配额则记录的是房型Id
    private long quotaId;

    // cutoffdayID
    private long cuttOffDayId;

    // 分配ID
    private long assignCustomId;

    // 配额模式
    private String quotaPattern;

    // 扣的配额对会员属于共享还是独占,独占为1，共享为0
    private int memberQuotaType;

    // 退回成功标志0失败1成功
    private int sign;

    public Date getQuotaDate() {
        return quotaDate;
    }

    public void setQuotaDate(Date quotaDate) {
        this.quotaDate = quotaDate;
    }

    public int getQuotaNum() {
        return quotaNum;
    }

    public void setQuotaNum(int quotaNum) {
        this.quotaNum = quotaNum;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getSalesroomPrice() {
        return salesroomPrice;
    }

    public void setSalesroomPrice(double salesroomPrice) {
        this.salesroomPrice = salesroomPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getRoomState() {
        return roomState;
    }

    public void setRoomState(String roomState) {
        this.roomState = roomState;
    }

    public long getQuotaId() {
        return quotaId;
    }

    public void setQuotaId(long quotaId) {
        this.quotaId = quotaId;
    }

    public long getCuttOffDayId() {
        return cuttOffDayId;
    }

    public void setCuttOffDayId(long cuttOffDayId) {
        this.cuttOffDayId = cuttOffDayId;
    }

    public long getAssignCustomId() {
        return assignCustomId;
    }

    public void setAssignCustomId(long assignCustomId) {
        this.assignCustomId = assignCustomId;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public String getQuotaPattern() {
        return quotaPattern;
    }

    public void setQuotaPattern(String quotaPattern) {
        this.quotaPattern = quotaPattern;
    }

    public int getMemberQuotaType() {
        return memberQuotaType;
    }

    public void setMemberQuotaType(int memberQuotaType) {
        this.memberQuotaType = memberQuotaType;
    }

}
