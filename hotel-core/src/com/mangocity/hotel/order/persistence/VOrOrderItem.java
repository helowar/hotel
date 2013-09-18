
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.hotel.order.constant.OrderItemAuditState;
import com.mangocity.util.Entity;

/** 
 *  or_orderitem
 *  
 *  @author chenkeming
 */
public class VOrOrderItem implements Entity {

    private static final long serialVersionUID = -1016864514184748021L;

    /**
	 * ID <pk>
	 */
    private Long ID;
    /**
	 * TMC订单编号重复,修改日审的问题
	 */
    private Integer version;

    /**
	 * 订单ID <fk> 和OrOrder关联
	 */
    private Long orderID;    

    /**
	 * Night (短日期)
	 */
    private Date night;
    
    /** 
	 * <br>房间明细入住状态:
	 * <br>1: 已入住
	 * <br>2: 未入住 
	 */
    private int orderState;

    /**
	 * 是否为订单第一天：方便计算订单是否noshow
	 */
    private boolean firstNight;

    /**
	 * 是否为订单最后一天：方便计算订单是否延住
	 */
    private boolean lastNight;    

    /**
	 * 日审状态：完成/操作中
	 * @see OrderItemAuditState
	 */
    private int auditState;
    
    /**
	 * 房间号
	 */
    private String roomNo;
    
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
	 * <br>该明细是否属于Noshow单
	 * <br>true : 是
	 * <br>false : 否
	 */
    private boolean show;
    
    /**
	 * 订单来源: 1:mango 2:tmc
	 */
    private int auditType;

    /**
	 * 订单来源: 1:mango 2:tmc 3临时
	 */
    private int auditType2;    
        
    /**
	 * 记录日审的人
	 */
    private String notesMan;
    /**
	 * 是否需要日审 
	 */
    private Long auditId;
    
    /**
	 * 日审记录时间
	 */
    private Date noteTime;
    
    /**
	 * 入住人姓名
	 */
    private String fellowName;
    
    /**
	 * 会员名称
	 */
    private String memberName;
    
    /**
     * 是否已结算
     */
    private boolean settlement;

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

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    public boolean isFirstNight() {
        return firstNight;
    }

    public void setFirstNight(boolean firstNight) {
        this.firstNight = firstNight;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Date getNight() {
        return night;
    }

    public void setNight(Date night) {
        this.night = night;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
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

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isLastNight() {
        return lastNight;
    }

    public void setLastNight(boolean lastNight) {
        this.lastNight = lastNight;
    }

    public int getAuditType2() {
        return auditType2;
    }

    public void setAuditType2(int auditType2) {
        this.auditType2 = auditType2;
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

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public String getFellowName() {
        return fellowName;
    }

    public void setFellowName(String fellowName) {
        this.fellowName = fellowName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        if(null != this.version) {
            return;
        } else {
            this.version = version;
        }
    }

	public boolean isSettlement() {
		return settlement;
	}

	public void setSettlement(boolean settlement) {
		this.settlement = settlement;
	}

}
