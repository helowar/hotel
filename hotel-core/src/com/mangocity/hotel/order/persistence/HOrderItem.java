
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.hotel.order.constant.OrderItemType;
import com.mangocity.util.HEntity;
import com.mangocity.util.hotel.constant.QuotaType;

/** 
 *  历史订单明细
 *  
 *  @author chenkeming
 */
public class HOrderItem implements HEntity {

    private static final long serialVersionUID = 7180381581810780545L;

    /**
	 * 主键 <pk>
	 */
    private Long hisID;

    /**
	 * 历史订单ID <fk> 和HOrder关联
	 */
    private HOrder orderH;    

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
	 * 历史订单序号
	 */
    private Long hisNo = 0L;
    
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
	 * 配额类型
	 * 
	 * @see QuotaType
	 */
    private String quotaType;        
    
    /**
	 * 销售价
	 */
    private double salePrice;
    
    /**
	 * 底价
	 */
    private double basePrice;
    
    /**
	 * 门市价
	 */
    private double marketPrice;                
        
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
	 * 日审状态：完成/操作中
	 */
    private int auditState;
    
    /**
	 * 房间号
	 */
    private String roomNo;        
    
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
	 * 是否显示
	 */
    private boolean show;
    
    /**
	 * 订单来源: 1:mango 2:tmc
	 */
    private int auditType;

    /**
	 * 是否为订单最后一天
	 */
    private boolean lastNight;
    
    /**
	 * 配额模式
	 */
    private String quotaPattern;
    
    /**
	 * 入住人姓名
	 */
    private String fellowName;        
    
    /**
	 * 0: 能退
	 * 1: 不能退
	 */
    private boolean quotaCantReturn;

    public int getAuditState() {
        return auditState;
    }

    public void setAuditState(int auditState) {
        this.auditState = auditState;
    }

    public int getAuditType() {
        return auditType;
    }

    public void setAuditType(int auditType) {
        this.auditType = auditType;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    public String getFellowName() {
        return fellowName;
    }

    public void setFellowName(String fellowName) {
        this.fellowName = fellowName;
    }

    public boolean isFirstNight() {
        return firstNight;
    }

    public void setFirstNight(boolean firstNight) {
        this.firstNight = firstNight;
    }

    public Long getHisID() {
        return hisID;
    }

    public void setHisID(Long hisID) {
        this.hisID = hisID;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public boolean isLastNight() {
        return lastNight;
    }

    public void setLastNight(boolean lastNight) {
        this.lastNight = lastNight;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getQuotaPattern() {
        return quotaPattern;
    }

    public void setQuotaPattern(String quotaPattern) {
        this.quotaPattern = quotaPattern;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public int getRoomIndex() {
        return roomIndex;
    }

    public void setRoomIndex(int roomIndex) {
        this.roomIndex = roomIndex;
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

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public HOrder getOrderH() {
        return orderH;
    }

    public void setOrderH(HOrder orderH) {
        this.orderH = orderH;
    }

    public boolean getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(boolean isConfirm) {
        this.isConfirm = isConfirm;
    }

    public Long getHisNo() {
        return hisNo;
    }

    public void setHisNo(Long hisNo) {
        this.hisNo = hisNo;
    }

    public boolean isQuotaCantReturn() {
        return quotaCantReturn;
    }

    public void setQuotaCantReturn(boolean quotaCantReturn) {
        this.quotaCantReturn = quotaCantReturn;
    }
}
