package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class QuotaQuery implements Serializable {

	private static final long serialVersionUID = -2315432399154349791L;
	
	//渠道,是否为直联: 0表示传统,>0表示直联
	private Long channel;

	// 房型ID
	private long roomTypeId;

	// 价格类型ID
	private long childRoomId;

	// 酒店ID
	private long hotelId;

	// 支付方式(面付pay，预付pre_pay)
	private String payMethod;

	// 会员类型(1.CC, 2.TP, 3.TMC)
	private int memberType;

	/***************************************************************************
	 * 配额类型 -- 普通配额HotelBaseConstantBean.GENERALQUOTA = "1"; 配额类型 --
	 * 包房配额HotelBaseConstantBean.CHARTERQUOTA = "2"; 配额类型 --
	 * 临时配额HotelBaseConstantBean.TEMPQUOTA = "3"; 配额类型 --
	 * 呼出配额HotelBaseConstantBean.OUTSIDEQUOTA = "4";
	 */
	private String quotaType;

	/***************************************************************************
	 * 配额共享类型 面付共享HotelBaseConstantBean.QUOTASHARETYPEPAY = "1";
	 * 预付共享HotelBaseConstantBean.QUOTASHARETYPEPREPAY = "2";
	 * 面预付共享HotelBaseConstantBeanQUOTASHARETYPE = "3";
	 */
	private String quotaShare;

	// 开始时间
	private Date beginDate;

	// 结束时间
	private Date endDate;

	// 扣配额数 (每天有多少间房)
	private int quotaNum;

	// 床型ID
	private long bedID;
	
	// 操作部门,可以为空
	private String operatorDept;
	
	// 操作人(会员名称)
	private String operatorName;
	
	// 操作ID(会员ID)
	private String operatorId;
	

	public long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public long getChildRoomId() {
		return childRoomId;
	}

	public void setChildRoomId(long childRoomId) {
		this.childRoomId = childRoomId;
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

	public int getMemberType() {
		return memberType;
	}

	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}

	public String getQuotaType() {
		return quotaType;
	}

	public void setQuotaType(String quotaType) {
		this.quotaType = quotaType;
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

	public int getQuotaNum() {
		return quotaNum;
	}

	public void setQuotaNum(int quotaNum) {
		this.quotaNum = quotaNum;
	}

	public long getBedID() {
		return bedID;
	}

	public void setBedID(long bedID) {
		this.bedID = bedID;
	}

	public String getQuotaShare() {
		return quotaShare;
	}

	public void setQuotaShare(String quotaShare) {
		this.quotaShare = quotaShare;
	}

	public String getOperatorDept() {
		return operatorDept;
	}

	public void setOperatorDept(String operatorDept) {
		this.operatorDept = operatorDept;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public Long getChannel() {
		return channel;
	}

	public void setChannel(Long channel) {
		this.channel = channel;
	}

}
