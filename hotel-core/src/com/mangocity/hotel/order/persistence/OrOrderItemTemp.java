
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.hotel.order.constant.OrderItemType;
import com.mangocity.util.Entity;
import com.mangocity.util.hotel.constant.QuotaType;

/** 
 *  or_orderitem_temp
 *  
 *  @author chenkeming
 */
public class OrOrderItemTemp implements Entity {

    private static final long serialVersionUID = 3942516026264657840L;

    /**
	 * ID <pk>
	 */
    private Long ID;

    /**
	 * 订单ID <fk> 和OrOrder关联
	 */
    private Long orderId;    

    /**
	 * Night (短日期)
	 */
    private Date night;

    /**
	 * 房态
	 */
    private String roomState;
    
    /**
	 * 房间数（都为1）
	 */
    private int quantity;
    
    /**
	 * 订单状态：已撤单,noshow,提前退房
	 */
    private int orderState;

    /**
	 * 是否为订单第一天：方便计算订单是否noshow
	 */
    private boolean firstNight;

    /**
	 * 创建时间
	 */
    private Date createTime;

    /**
	 * 如果是红单、或附加单，则该单应与原订单关联。<br>
	 * 即是哪条记录的红单，哪条记录的附加
	 */
    private Long itemsIdLink;
    
    /**
	 * 备注
	 */
    private String notes;
    
    /**
	 * 明细数据类型：1order（正常订单明细）、2red（红单）、3add（冲后附加单）
	 * 
	 * @see OrderItemType
	 */
    private int orderItemsType;
    
    /**
	 * 是否已日结  
	 */
    private boolean settlement;
    
    /**
	 * 结算日期
	 */
    private Date settlementDate;
    
    /**
	 * 是否已确认
	 */
    private boolean isConfirm;
    
    /**
	 * 配额ID
	 */
    private Long quotaId;
    
    /**
	 * 配额类型
	 * 
	 * @see QuotaType
	 */
    private String quotaType;
    
    /**
	 * 配额批次id
	 */
    private Long quotaBatchId;
    
    /**
	 * cutoffday
	 */
    private long cutoffDay;
    
    /**
	 * 单价
	 */
    private double price;
    
    /**
	 * 销售价
	 */
    private double salePrice;
    
    /**
	 * 底价
	 */
    private double basePrice;

    /**
	 * 加幅
	 */
    private double amplitude;
    
    /**
	 * 门市价
	 */
    private double marketPrice;
    
    /**
	 * 建议门市价
	 */
    private double adviceMarketPrice;
    
    /**
	 * 服务费
	 */
    private double serviceFee;
    
    /**
	 * 房价是否含服务费
	 */
    private boolean includeService;
    
    /**
	 * 佣金
	 */
    private double commission;
        
    /**
	 * 含早类型
	 */
    private int breakfast;
    
    /**
	 * 含早数量
	 */
    private int breakfastNum;
    
    /**
	 * 含早形式
	 */
    private String breakfastWay;
    
    /**
	 * 含早价
	 */
    private double breakfastPrice;
    
    /**
	 * 含早价是否返佣
	 */
    private boolean includeBreakfast;    

    /**
	 * 日审ID <fk> 和OrDailyAudit关联
	 */
    private Long auditId;    
    
    /**
	 * 日审状态：完成/操作中
	 */
    private int auditState;
    
    /**
	 * 房间号
	 */
    private String roomNo;
    
    /**
	 * 特别说明
	 */
    private String specialNote;
    
    /**
	 * 分配
	 */
    private Long assignTo;
    
    /**
	 * 记录日审的人
	 */
    private String notesMan;
    
    /**
	 * 日审记录时间
	 */
    private Date noteTime;
    
    /**
	 * 已入住/未入住
	 */
    private int noteResult;
    
    /**
	 * 是订单的第几天(zero based)
	 */
    private int dayIndex;
    
    /**
	 * 酒店ID
	 */
    private Long hotelId;
    
    /**
	 * 是订单的第几间房(zero based)
	 */
    private int roomIndex;
    
    /**
	 * 分配ID
	 */
    private long assignCustomId;
    
    /**
	 * 扣的配额对会员属于共享还是独占,独占为1，共享为0
	 */
    private int memberQuotaType;
    
    /**
	 * 是否显示
	 */
    private boolean show;
    
    /**
	 * 订单来源: 1:mango 2:tmc
	 */
    private int auditType;

    public int getMemberQuotaType() {
        return memberQuotaType;
    }

    public void setMemberQuotaType(int memberQuotaType) {
        this.memberQuotaType = memberQuotaType;
    }

    public int getAuditType() {
        return auditType;
    }

    public void setAuditType(int auditType) {
        this.auditType = auditType;
    }

    public long getAssignCustomId() {
        return assignCustomId;
    }

    public void setAssignCustomId(long assignCustomId) {
        this.assignCustomId = assignCustomId;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    public int getRoomIndex() {
        return roomIndex;
    }

    public void setRoomIndex(int roomIndex) {
        this.roomIndex = roomIndex;
    }

    public double getAdviceMarketPrice() {
        return adviceMarketPrice;
    }

    public void setAdviceMarketPrice(double adviceMarketPrice) {
        this.adviceMarketPrice = adviceMarketPrice;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public Long getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(Long assignTo) {
        this.assignTo = assignTo;
    }

    public int getAuditState() {
        return auditState;
    }

    public void setAuditState(int auditState) {
        this.auditState = auditState;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public int getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(int breakfast) {
        this.breakfast = breakfast;
    }

    public int getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(int breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public double getBreakfastPrice() {
        return breakfastPrice;
    }

    public void setBreakfastPrice(double breakfastPrice) {
        this.breakfastPrice = breakfastPrice;
    }

    public String getBreakfastWay() {
        return breakfastWay;
    }

    public void setBreakfastWay(String breakfastWay) {
        this.breakfastWay = breakfastWay;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getCutoffDay() {
        return cutoffDay;
    }

    public void setCutoffDay(long cutoffDay) {
        this.cutoffDay = cutoffDay;
    }

    public boolean isFirstNight() {
        return firstNight;
    }

    public void setFirstNight(boolean firstNight) {
        this.firstNight = firstNight;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public boolean isIncludeBreakfast() {
        return includeBreakfast;
    }

    public void setIncludeBreakfast(boolean includeBreakfast) {
        this.includeBreakfast = includeBreakfast;
    }

    public boolean isIncludeService() {
        return includeService;
    }

    public void setIncludeService(boolean includeService) {
        this.includeService = includeService;
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public void setConfirm(boolean isConfirm) {
        this.isConfirm = isConfirm;
    }

    public boolean getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(boolean isConfirm) {
        this.isConfirm = isConfirm;
    }
    
    public Long getItemsIdLink() {
        return itemsIdLink;
    }

    public void setItemsIdLink(Long itemsIdLink) {
        this.itemsIdLink = itemsIdLink;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Date getNight() {
        return night;
    }

    public void setNight(Date night) {
        this.night = night;
    }

    public int getNoteResult() {
        return noteResult;
    }

    public void setNoteResult(int noteResult) {
        this.noteResult = noteResult;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotesMan() {
        return notesMan;
    }

    public void setNotesMan(String notesMan) {
        this.notesMan = notesMan;
    }

    public Date getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(Date noteTime) {
        this.noteTime = noteTime;
    }

    public int getOrderItemsType() {
        return orderItemsType;
    }

    public void setOrderItemsType(int orderItemsType) {
        this.orderItemsType = orderItemsType;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getQuotaBatchId() {
        return quotaBatchId;
    }

    public void setQuotaBatchId(Long quotaBatchId) {
        this.quotaBatchId = quotaBatchId;
    }

    public Long getQuotaId() {
        return quotaId;
    }

    public void setQuotaId(Long quotaId) {
        this.quotaId = quotaId;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomState() {
        return roomState;
    }

    public void setRoomState(String roomState) {
        this.roomState = roomState;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public boolean isSettlement() {
        return settlement;
    }

    public void setSettlement(boolean settlement) {
        this.settlement = settlement;
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    } 
    
    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
    
    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

}
