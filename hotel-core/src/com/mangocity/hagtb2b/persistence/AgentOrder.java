package com.mangocity.hagtb2b.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgentOrder {
	
	private long   ID;
	
	private String agentCode;
	
	private Long orderId;
	
	private String orderCd;
	
	private String hotelName;
	
	private String roomName;
	
	private String childRoomName;
	
	private int    bedType;
	
	private int    roomNum;
	
	private Date   checkInDate;
	
	private Date   checkOutDate;
	
	private double sumRmb;
	
	private double commisionPrice;
	private double commisionRate;
	
	private double commision;
	
	private double backCommission;
	private String fellowNames;
	/**
	 * 预订人
	 */
	private String bookingname;
	private StatisticsInfo satisInfo;
	private List<AgentOrderItem> orderItems = new ArrayList<AgentOrderItem>();
	
	private double rate;//汇率,临时用，不用存入database
	
	/**
	 * 是否底价支付 add by xiaowei.wang
	 */
	private String isMinPrice;
	
	
	public String getIsMinPrice() {
		return isMinPrice;
	}
	public void setIsMinPrice(String isMinPrice) {
		this.isMinPrice = isMinPrice;
	}
	
	public StatisticsInfo getSatisInfo() {
		return satisInfo;
	}
	public void setSatisInfo(StatisticsInfo satisInfo) {
		this.satisInfo = satisInfo;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getChildRoomName() {
		return childRoomName;
	}
	public void setChildRoomName(String childRoomName) {
		this.childRoomName = childRoomName;
	}
	public int getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}
	public Date getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}
	public Date getCheckOutDate() {
		return checkOutDate;
	}
	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
	public double getSumRmb() {
		return sumRmb;
	}
	public void setSumRmb(double sumRmb) {
		this.sumRmb = sumRmb;
	}
	public int getBedType() {
		return bedType;
	}
	public void setBedType(int bedType) {
		this.bedType = bedType;
	}
	public long getID() {
		return ID;
	}
	public void setID(long id) {
		ID = id;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public double getCommision() {
		return commision;
	}
	public void setCommision(double commision) {
		this.commision = commision;
	}
	public String getOrderCd() {
		return orderCd;
	}
	public void setOrderCd(String orderCd) {
		this.orderCd = orderCd;
	}
	public List<AgentOrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<AgentOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public double getBackCommission() {
		return backCommission;
	}
	public void setBackCommission(double backCommission) {
		this.backCommission = backCommission;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getCommisionPrice() {
		return commisionPrice;
	}
	public void setCommisionPrice(double commisionPrice) {
		this.commisionPrice = commisionPrice;
	}
	public double getCommisionRate() {
		return commisionRate;
	}
	public void setCommisionRate(double commisionRate) {
		this.commisionRate = commisionRate;
	}
	public String getFellowNames() {
		return fellowNames;
	}
	public void setFellowNames(String fellowNames) {
		this.fellowNames = fellowNames;
	}
	public String getBookingname() {
		return bookingname;
	}
	public void setBookingname(String bookingname) {
		this.bookingname = bookingname;
	}

	
}
