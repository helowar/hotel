package com.mangocity.tmc.service.assistant;



import java.util.Date;

import com.mangocity.util.DateUtil;

/**
 * 房间价格和含早信息
 * @author bruce.yang
 *
 */
public class RoomInfo {

	/**
	 * 房间日期(YYYY-MM-DD)
	 */
	private Date fellowDate;
	
	/**
	 * 房间日期ddMMMyy格式字符串
	 */
	private String fellowDateEngStr;

	/**
	 * 房间价格
	 */
	private String salePrice;

	/**
	 * 币种
	 */
	private String currency;

	/**
	 * 房间当前配额数量
	 */
	private String quotaAmount;

	/**
	 * 房间含早情况
	 */
	private String breakfast;
    
    /**
     * 含早数量
     */
    private String breakNum;

	/**
	 * 房间状态
	 */
	private String roomStatus;

	/**
	 * 配额批次id
	 */
	private String quotaBatchId;

	/**
	 * 配额模式(在店模式还是进店模式)
	 */
	private String quotaPattern;
	
	/**
	 * 网费情况
	 */
	private String netFee;
	
	/**
	 * 服务费情况
	 */
	private String serviceFee;
	
	/**
	 * 酒店最晚到达时间
	 * @author xiaoyong.li
	 */	
	private String lastAssureTime;
	
	/**
	 * 是否需要无条件担保, 是:1(true), 否:0(false) 
	 * @author xiaoyong.li
	 */
	private boolean needAssure;
	
	/**
	 * 面付转预付, 必须:1, 允许:2, 不许:3
	 * @author xiaoyong.li
	 */
	private int payToPrepay;
	
	/**
	 * 最晚可预订日期
	 * @author xiaoyong.li
	 */
	private Date latestBookableDate;
	
	/**
	 * 最晚可预订时间点
	 * @author xiaoyong.li
	 */
	private String latestBokableTime;
	
	/**
	 * 必住最后日期
	 * @author xiaoyong.li
	 */
	private Date mustLastDate;
	
	/**
	 * 必住最早日期
	 * @author xiaoyong.li
	 */
	private Date mustFirstDate;
	
	/**
	 * 连住天数
	 * @author xiaoyong.li
	 */
	private long continueDay; 
	
	/**
	 * 必住日期
	 * @author xiaoyong.li
	 */
	private String mustInDate;

	
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

	public String getQuotaAmount() {
		return quotaAmount;
	}

	public void setQuotaAmount(String quotaAmount) {
		this.quotaAmount = quotaAmount;
	}

	public String getQuotaBatchId() {
		return quotaBatchId;
	}

	public void setQuotaBatchId(String quotaBatchId) {
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

	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String roomPrice) {
		this.salePrice = roomPrice;
	}

	public String getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}

	public String getNetFee() {
		return netFee;
	}

	public void setNetFee(String netFee) {
		this.netFee = netFee;
	}

	public String getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}
	
	public String getFellowDateEngStr(){
		return DateUtil.dateToString(fellowDate);
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

	public String getLastAssureTime() {
		return lastAssureTime;
	}

	public void setLastAssureTime(String lastAssureTime) {
		this.lastAssureTime = lastAssureTime;
	}

    public String getBreakNum() {
        return breakNum;
    }

    public void setBreakNum(String breakNum) {
        this.breakNum = breakNum;
    }
	
	


}
