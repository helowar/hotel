package com.mangocity.hotel.order.persistence;

import java.util.Date;

public class HtlCheckReservateLog {
	/**
	 * key
	 */
	private Long id;
	
	/**
	 * 酒店id
	 */
	private Long hotelId;
	
	/**
	 * 房型id
	 */
	private Long roomTypeId;
	
	/**
	 * 价格类型id
	 */
	private Long priceTypeId;
	
	/**
	 * 入住日期
	 */
	private Date checkInDate;
	
	/**
	 * 离店日期
	 */
	private Date checkOutDate;
	
	/**
	 * 操作类型
	 */
	private String operateType;
	
	/**
	 * 应用名称（日志由哪个应用写入的）
	 */
	private String appName;
	
	/**
	 * 操作开始时间
	 */
	private Date operateBeginTime;
	
	/**
	 * 操作结束时间
	 */
	private Date operateEndTime;
	
	/**
	 * 操作的总时间数（单位是毫秒）
	 */
	private Long operateUsedTime;
	
	/**
	 * 渠道类型
	 */
	private Integer channelType;
	
	/**
	 * 创建时间
	 */
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public Long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(Long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public Long getPriceTypeId() {
		return priceTypeId;
	}

	public void setPriceTypeId(Long priceTypeId) {
		this.priceTypeId = priceTypeId;
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

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Date getOperateBeginTime() {
		return operateBeginTime;
	}

	public void setOperateBeginTime(Date operateBeginTime) {
		this.operateBeginTime = operateBeginTime;
	}

	public Date getOperateEndTime() {
		return operateEndTime;
	}

	public void setOperateEndTime(Date operateEndTime) {
		this.operateEndTime = operateEndTime;
	}

	public Long getOperateUsedTime() {
		return operateUsedTime;
	}

	public void setOperateUsedTime(Long operateUsedTime) {
		this.operateUsedTime = operateUsedTime;
	}

	public Integer getChannelType() {
		return channelType;
	}

	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
	}
}
