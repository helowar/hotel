package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * HtlPrice generated by MyEclipse - Hibernate Tools 酒店价格信息
 */

public class HtlPrice extends CEntity implements Entity {

    /**
     * 价格id
     */
    private Long ID;

    /**
     * 可销售日期
     */
    private Date ableSaleDate;

    /**
     * 星期
     */
    private int week;

    /**
     * 房型id
     */
    private long roomTypeId;

    /**
     * 房型名(页面显示)
     */
    private String RoomTypeName;

    /**
     * 价格类型id
     */
    private long childRoomTypeId;

    /**
     *价格类型名(页面显示)
     */
    private String chileRoomTypeName;

    /**
     * 酒店id
     */
    private long hotelId;

    /**
     * 采购批次id
     */
    private long quotaBatchId;

    /**
     * 配额类型
     */
    private String quotaType;

    /**
     * 大客户ID
     */
    private long bigCustomId;

    /**
     * 会员类型
     */
    private long memberType;

    /**
     * 底价
     */
    private double basePrice;

    /**
     * 净价
     */
    private double advicePrice;

    /**
     * 门市价
     */
    private double salesroomPrice;

    /**
     * 服务费
     */
    private double serviceCharge;

    /**
     * 能否加幅
     */
    private boolean canAddScope;

    /**
     * 加幅
     */
    private double addScope;

    /**
     * 含早数量,如果数量为0，表示不含早.
     */
    private String incBreakfastNumber;

    /**
     * 含早类型
     */
    private String incBreakfastType;

    /**
     * 含早价格
     */
    private double incBreakfastPrice;

    /**
     * 含早是否返佣金
     */
    private boolean returnComm;

    /**
     * 佣金
     */
    private double commission;

    /**
     * 酒店提供的佣金百分比
     */
    private double commissionRate;

    /**
     * 酒店销售价(芒果网卖价)
     */
    private double salePrice;

    /**
     * 币种
     */
    private String currency;

    /**
     * 支付方式
     */
    private String payMethod;

    /**
     * 计算公式ID
     */
    private String formulaId;

    private HtlRoom room;

    private String closeFlag;

    // 开关房原因
    private String reason;

    private String compagesDate;

    /**
     * 合作伙伴计划ID
     */
    private String planID;

    // 全部关房原因
    private String allCloseReason;

    /*
     * Begin Add by Shengwei.Zuo 2009-02-04 酒店2.6新增字段: 面预付标志 --> PAY_TO_PREPAY ; 最晚可预订日期
     * -->LATEST_BOOKABLE_DATE ; 最晚可预订时间点 -->LATEST_BOKABLE_TIME ; 是否担保 -->NEED_ASSURE ; 必住最后日期
     * -->MUST_LAST_DATE DATE; 必住最早日期 -->MUST_FIRST_DATE DATE;
     */

    // 面预付标志
    private String payToPrepay;

    // 最晚可预订日期
    private Date latestBookableDate;

    // 最晚可预订时间点
    private String latestBokableTime;

    // 是否担保
    private String needAssure;

    // 必住最后日期
    private Date mustLastDate;

    // 必住最早日期
    private Date mustFirstDate;
    
    
    /**hotel 2.9.3  
     * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 begin  
     */
    
    //最早可预订日期 
    private Date  firstBookableDate;
    
    //最早可预订时间
    private String firstBookableTime;

    /**hotel 2.9.3  
     * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 end  
     */
    
    /**
     * End Add by Shengwei.Zuo 2009-02-04
     */

    /** default constructor */
    public HtlPrice() {
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public long getQuotaBatchId() {
        return quotaBatchId;
    }

    public void setQuotaBatchId(long quotaBatchId) {
        this.quotaBatchId = quotaBatchId;
    }

    public long getBigCustomId() {
        return bigCustomId;
    }

    public void setBigCustomId(long bigCustomId) {
        this.bigCustomId = bigCustomId;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public long getMemberType() {
        return memberType;
    }

    public void setMemberType(long memberType) {
        this.memberType = memberType;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getAdvicePrice() {
        return advicePrice;
    }

    public void setAdvicePrice(double advicePrice) {
        this.advicePrice = advicePrice;
    }

    public boolean isCanAddScope() {
        return canAddScope;
    }

    public void setCanAddScope(boolean canAddScope) {
        this.canAddScope = canAddScope;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public String getIncBreakfastNumber() {
        return incBreakfastNumber;
    }

    public void setIncBreakfastNumber(String incBreakfastNumber) {
        this.incBreakfastNumber = incBreakfastNumber;
    }

    public double getIncBreakfastPrice() {
        return incBreakfastPrice;
    }

    public void setIncBreakfastPrice(double incBreakfastPrice) {
        this.incBreakfastPrice = incBreakfastPrice;
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

    public double getSalesroomPrice() {
        return salesroomPrice;
    }

    public void setSalesroomPrice(double salesroomPrice) {
        this.salesroomPrice = salesroomPrice;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public double getAddScope() {
        return addScope;
    }

    public void setAddScope(double addScope) {
        this.addScope = addScope;
    }

    public long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(long childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public HtlRoom getRoom() {
        return room;
    }

    public void setRoom(HtlRoom room) {
        this.room = room;
    }

    public String getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(String formulaId) {
        this.formulaId = formulaId;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public Date getAbleSaleDate() {
        return ableSaleDate;
    }

    public void setAbleSaleDate(Date ableSaleDate) {
        this.ableSaleDate = ableSaleDate;
    }

    public String getCloseFlag() {
        return closeFlag;
    }

    public void setCloseFlag(String closeFlag) {
        this.closeFlag = closeFlag;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCompagesDate() {
        return compagesDate;
    }

    public void setCompagesDate(String compagesDate) {
        this.compagesDate = compagesDate;
    }

    public int getWeek() {
        return week;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getPlanID() {
        return planID;
    }

    public void setPlanID(String planID) {
        this.planID = planID;
    }

    public String getChileRoomTypeName() {
        return chileRoomTypeName;
    }

    public void setChileRoomTypeName(String chileRoomTypeName) {
        this.chileRoomTypeName = chileRoomTypeName;
    }

    public String getRoomTypeName() {
        return RoomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        RoomTypeName = roomTypeName;
    }

    public String getAllCloseReason() {
        return allCloseReason;
    }

    public void setAllCloseReason(String allCloseReason) {
        this.allCloseReason = allCloseReason;
    }

    public String getLatestBokableTime() {
        return latestBokableTime;
    }

    public void setLatestBokableTime(String latestBokableTime) {
        this.latestBokableTime = latestBokableTime;
    }

    public Date getLatestBookableDate() {
        return latestBookableDate;
    }

    public void setLatestBookableDate(Date latestBookableDate) {
        this.latestBookableDate = latestBookableDate;
    }

    public Date getMustFirstDate() {
        return mustFirstDate;
    }

    public void setMustFirstDate(Date mustFirstDate) {
        this.mustFirstDate = mustFirstDate;
    }

    public Date getMustLastDate() {
        return mustLastDate;
    }

    public void setMustLastDate(Date mustLastDate) {
        this.mustLastDate = mustLastDate;
    }

    public String getNeedAssure() {
        return needAssure;
    }

    public void setNeedAssure(String needAssure) {
        this.needAssure = needAssure;
    }

    public String getPayToPrepay() {
        return payToPrepay;
    }

    public void setPayToPrepay(String payToPrepay) {
        this.payToPrepay = payToPrepay;
    }

	public Date getFirstBookableDate() {
		return firstBookableDate;
	}

	public void setFirstBookableDate(Date firstBookableDate) {
		this.firstBookableDate = firstBookableDate;
	}

	public String getFirstBookableTime() {
		return firstBookableTime;
	}

	public void setFirstBookableTime(String firstBookableTime) {
		this.firstBookableTime = firstBookableTime;
	}

}