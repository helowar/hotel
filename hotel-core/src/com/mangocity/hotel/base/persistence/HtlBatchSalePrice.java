package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 一个时间段类的价格情况，用来生成每一天的价格，含早情况，等记录。
 * 
 * @author xiaowumi
 * 
 */

public class HtlBatchSalePrice implements Entity {
    // Fields

    // 销售价格id
    private Long ID;

    // 采购批次id
    private long contractId;

    // 起始日期
    private Date beginDate;

    // 终止时期
    private Date endDate;

    // 房型id
    private long roomTypeId;

    // 子房型id
    private long childRoomTypeId;

    // 支付方式
    private String payMethod;

    // 调整周
    private String adjustWeek;

    // 面付底价
    private double basePrice;

    // 面付建议售价
    private double advicePrice;

    // 门市价
    private double salesroomPrice;

    // 服务费
    private double serviceCharge;

    // 能否加幅
    private boolean canAddScope;

    // 含早数量
    private String incBreakfastNumber;

    // 含早类型
    private String incBreakfastType;

    // 含早价格
    private double incBreakfastPrice;

    // 含早是否返佣金
    private boolean returnComm;

    // 佣金
    private double commission;

    /**
     * 酒店提供的佣金百分比
     */
    private double commissionRate;

    // 销售价
    private double salePrice;

    // 创建人ID
    private String createById;

    // 修改人ID
    private String modifyById;

    // 创建人
    private String createBy;

    // 最近修改人
    private String modifyBy;

    // 创建时间
    private Date createTime;

    // 最近修改时间
    private Date modifyTime;

    // 面付计算公式
    private String calcFormula;

    // 预付计算公式
    private String preCalcFormula;

    private String updateFlag;

    // Constructors

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    /** default constructor */
    public HtlBatchSalePrice() {
    }

    // Property accessors

    public String getAdjustWeek() {
        return adjustWeek;
    }

    public void setAdjustWeek(String adjustWeek) {
        this.adjustWeek = adjustWeek;
    }

    public double getAdvicePrice() {
        return advicePrice;
    }

    public void setAdvicePrice(double advicePrice) {
        this.advicePrice = advicePrice;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
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

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public double getIncBreakfastPrice() {
        return incBreakfastPrice;
    }

    public void setIncBreakfastPrice(double incBreakfastPrice) {
        this.incBreakfastPrice = incBreakfastPrice;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public String getModifyById() {
        return modifyById;
    }

    public void setModifyById(String modifyById) {
        this.modifyById = modifyById;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
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

    public long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public double getSalesroomPrice() {
        return salesroomPrice;
    }

    public void setSalesroomPrice(double salesroomPrice) {
        this.salesroomPrice = salesroomPrice;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public boolean isCanAddScope() {
        return canAddScope;
    }

    public void setCanAddScope(boolean canAddScope) {
        this.canAddScope = canAddScope;
    }

    public String getIncBreakfastNumber() {
        return incBreakfastNumber;
    }

    public void setIncBreakfastNumber(String incBreakfastNumber) {
        this.incBreakfastNumber = incBreakfastNumber;
    }

    public String getIncBreakfastType() {
        return incBreakfastType;
    }

    public void setIncBreakfastType(String incBreakfastType) {
        this.incBreakfastType = incBreakfastType;
    }

    public boolean isReturnComm() {
        return returnComm;
    }

    public void setReturnComm(boolean returnComm) {
        this.returnComm = returnComm;
    }

    public String getCalcFormula() {
        return calcFormula;
    }

    public void setCalcFormula(String calcFormula) {
        this.calcFormula = calcFormula;
    }

    public long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(long childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPreCalcFormula() {
        return preCalcFormula;
    }

    public void setPreCalcFormula(String preCalcFormula) {
        this.preCalcFormula = preCalcFormula;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

}