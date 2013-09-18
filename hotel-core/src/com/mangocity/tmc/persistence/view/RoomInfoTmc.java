package com.mangocity.tmc.persistence.view;

import java.io.Serializable;
import java.util.Date;

/**
 * 房间价格和含早信息
 */
public class RoomInfoTmc implements Serializable {
    
    private static final long serialVersionUID = -4209021876771134983L;

	// 房间日期(YYYY-MM-DD)
	private Date fellowDate;

	// 合同币种房间价格
	private double salePrice;
	
	//base price	
	private double basePrice;

	// 合同币种
	private String currency;

	// /房间当前配额数量
	private int quotaAmount;

	// 房间含早类型
	private String breakfast;
	//含早数量
	private String breakNum;

	// 房间状态
	private String roomStatus;

	// 配额批次id
	private long quotaBatchId;

	// 配额模式(在店模式还是进店模式)
	private String quotaPattern;

	//人民币报价
	private double rmbSalePrice;
	//人民币标识
	private String rmbCurrency;
	
	//门市价
	private double salesRoomPrice;
	/**
	 * 开关房标志?
	 */
	private String closeFlag;
	

	/**
	 * 开关房原因
	 */
	private String reason;

	/**
	 * 酒店最晚到达时间
	 */
	private String lastAssureTime;
	
	/**
	 * 是否需要无条件担保, 是:1(true), 否:0(false) 
	 * @author chenkeming Feb 6, 2009 8:58:29 AM
	 */
	private boolean needAssure;
	
	/**
	 * 面付转预付, 必须:1, 允许:2, 不许:3
	 * @author chenkeming Feb 6, 2009 8:59:16 AM
	 */
	private int payToPrepay;
	
	/**
	 * 最晚可预订日期
	 * hotel2.6 add by zhineng.zhuang 2009-02-16
	 */
	private Date latestBookableDate;
	
	/**
	 * 最晚可预订时间点
	 * hotel2.6 add by zhineng.zhuang 2009-02-16
	 */
	private String latestBokableTime;
	
	/**
	 * 必住最后日期
	 * hotel2.6 add by zhineng.zhuang 2009-02-16
	 */
	private Date mustLastDate;
	
	/**
	 * 必住最早日期
	 * hotel2.6 add by zhineng.zhuang 2009-02-16
	 */
	private Date mustFirstDate;
	
	/**
	 * 连住天数
	 * hotel2.6 add by zhineng.zhuang 2009-02-16
	 */
	private long continueDay; 
	
	/**
	 * 必住日期
	 * hotel2.6 add by zhineng.zhuang 2009-02-16
	 */
	private String mustInDate;
	
	/**
	 * 该支付方式是否有预订条款,
	 * 供cc前台查酒店用
	 * @author chenkeming Mar 5, 2009 8:55:39 AM
	 */
	private boolean hasReserv = false;
	

	public double getSalesRoomPrice() {
		return salesRoomPrice;
	}

	public void setSalesRoomPrice(double salesRoomPrice) {
		this.salesRoomPrice = salesRoomPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBreakfast() {
		return breakfast;
	}

	public void setBreakfast(String incBreakfast) {
		this.breakfast = incBreakfast;
	}

	public int getQuotaAmount() {
		return quotaAmount;
	}

	public void setQuotaAmount(int quotaAmount) {
		this.quotaAmount = quotaAmount;
	}

	public long getQuotaBatchId() {
		return quotaBatchId;
	}

	public void setQuotaBatchId(long quotaBatchId) {
		this.quotaBatchId = quotaBatchId;
	}

	public String getQuotaPattern() {
		return quotaPattern;
	}

	public void setQuotaPattern(String quotaPattern) {
		this.quotaPattern = quotaPattern;
	}


	public Date getFellowDate() {
		return fellowDate;
	}

	public void setFellowDate(Date fellowDate) {
		this.fellowDate = fellowDate;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double roomPrice) {
		this.salePrice = roomPrice;
	}

	public String getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}

	public double getRmbSalePrice() {
		return rmbSalePrice;
	}

	public void setRmbSalePrice(double rmbSalePrice) {
		this.rmbSalePrice = rmbSalePrice;
	}

	public String getRmbCurrency() {
		return rmbCurrency;
	}

	public void setRmbCurrency(String rmbCurrency) {
		this.rmbCurrency = rmbCurrency;
	}

	public String getBreakNum() {
		return breakNum;
	}

	public void setBreakNum(String breakNum) {
		this.breakNum = breakNum;
	}


	public String getCloseFlag() {
		return closeFlag;
	}

	public String getLastAssureTime() {
		return lastAssureTime;
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



	public void setLastAssureTime(String lastAssureTime) {
		this.lastAssureTime = lastAssureTime;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public boolean isNeedAssure() {
		return needAssure;
	}

	public void setNeedAssure(boolean needAssure) {
		this.needAssure = needAssure;
	}

	public int getPayToPrepay() {
		return payToPrepay;
	}

	public void setPayToPrepay(int payToPrepay) {
		this.payToPrepay = payToPrepay;
	}

	public long getContinueDay() {
		return continueDay;
	}

	public void setContinueDay(long continueDay) {
		this.continueDay = continueDay;
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

	public String getMustInDate() {
		return mustInDate;
	}

	public void setMustInDate(String mustInDate) {
		this.mustInDate = mustInDate;
	}

	public Date getMustLastDate() {
		return mustLastDate;
	}

	public void setMustLastDate(Date mustLastDate) {
		this.mustLastDate = mustLastDate;
	}

	public boolean isHasReserv() {
		return hasReserv;
	}

	public void setHasReserv(boolean hasReserv) {
		this.hasReserv = hasReserv;
	}


}
