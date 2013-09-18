package com.mangocity.hotel.base.persistence;

import java.util.Date;

public class HtlLimitFavourableHotel {
	
	/**
	 * 酒店ID
	 */
	private Long hotelId;
	
	/**
	 * 返现活动ID
	 */
	private Long favId;
	
	/**
	 * 活动名称
	 */
	private String favName;
	
	/**
	 * 活动开始日期 YYYY-MM-DD
	 */
	private Date beginDate;
	
	/**
	 * 活动结束日期 YYYY-MM-DD
	 */
	private Date endDate;
	
	/**
	 * 活动开始时间  HH24
	 */
	private String beginTime;
	
	/**
	 * 活动结束时间  HH24
	 */
	private String endTime;
	
	/**
	 * 点击活动开始按钮时的实际开始时间
	 */
	private Date actualBeginDate;
	
	/**
	 * 点击活动结束按钮时的实际结束时间
	 */
	private Date actualEndDate;
	
	/**
	 * 入住日期
	 */
	private Date checkIn;
	
	/**
	 * 离店日期
	 */
	private Date checkOut;
	
	/**
	 * 返现规则
	 */
	private Double rule;
	
	/**
	 * 间夜量上限 0为无上限
	 */
	private  int topLimit;
	
	/**
	 * 活动是否开始 未开始0、已开始1、已结束2 默认为未开始(0)
	 */
	private int favIsStart;
	
	/**
	 * 用于软删除活动 0表示已删除(无效) 1表示有效 默认为有效(1)
	 */
	private int flag;
	
	//优惠活动种类 1表示按售价优惠，2表示按佣金优惠，0表示既不按售价也不按佣金
	private int favType;
	

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public Long getFavId() {
		return favId;
	}

	public void setFavId(Long favId) {
		this.favId = favId;
	}

	public String getFavName() {
		return favName;
	}

	public void setFavName(String favName) {
		this.favName = favName;
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

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Date getActualBeginDate() {
		return actualBeginDate;
	}

	public void setActualBeginDate(Date actualBeginDate) {
		this.actualBeginDate = actualBeginDate;
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public Date getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}

	public Date getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}

	public Double getRule() {
		return rule;
	}

	public void setRule(Double rule) {
		this.rule = rule;
	}

	public int getTopLimit() {
		return topLimit;
	}

	public void setTopLimit(int topLimit) {
		this.topLimit = topLimit;
	}

	public int getFavIsStart() {
		return favIsStart;
	}

	public void setFavIsStart(int favIsStart) {
		this.favIsStart = favIsStart;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getFavType() {
		return favType;
	}

	public void setFavType(int favType) {
		this.favType = favType;
	}
}
