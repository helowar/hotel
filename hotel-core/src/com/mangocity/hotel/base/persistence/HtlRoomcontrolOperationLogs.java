package com.mangocity.hotel.base.persistence;

import java.util.Date;

/**
 * HtlRoomcontrolOperationLogs generated by MyEclipse Persistence Tools
 * 房控操作列表
 */

public class HtlRoomcontrolOperationLogs implements java.io.Serializable {

	// Fields
	/**
	 * 主键ID
	 */
	private Long logid;
	/**
	 *酒店ID
	 */
	private Long hotelid;
	/**
	 * 酒店名称
	 */
	private String hotelname;
	/**
	 * 第几次询房
	 */
	private Integer setup;
	/**
	 * 操作人ID
	 */
	private String operatorid;
	/**
	 * 操作人姓名
	 */
	private String operatorname;
	/**
	 * 任务开始时间
	 */
	private Date starttime;
	/**
	 * 任务结束时间
	 */
	private Date finishtime;
	/**
	 * 操作类型，1：完成 2,：释放 3,工作状态关闭时退出的
	 */
	private Integer operationtype;
	/**
	 * 操作内容（如果释放操作，则为释放下次询房时间点）
	 */
	private String operatorcontent;
	/**
	 * 任务花费时间,单位为毫秒
	 */
	private Long costtime;
	/**
	 * 释放原因：1：电话未通 2：负责人不在 3：稍后提供
	 */
	private Integer relaxreason;
	/**
	 * 获取方式：1，自动2，手工
	 */
	private Integer acquireway;
	
	/**
	 * 记录此次处理房态的处理来源：1呼出，2呼入，3MSN，4QQ，5邮件
	 */
	private String  dealWithSource;
	// Constructors

	public String getDealWithSource() {
		return dealWithSource;
	}

	public void setDealWithSource(String dealWithSource) {
		this.dealWithSource = dealWithSource;
	}

	public Integer getAcquireway() {
		return acquireway;
	}

	public void setAcquireway(Integer acquireway) {
		this.acquireway = acquireway;
	}

	/** default constructor */
	public HtlRoomcontrolOperationLogs() {
	}

	// Property accessors

	public Long getLogid() {
		return this.logid;
	}

	public void setLogid(Long logid) {
		this.logid = logid;
	}

	public Long getHotelid() {
		return this.hotelid;
	}

	public void setHotelid(Long hotelid) {
		this.hotelid = hotelid;
	}

	public String getHotelname() {
		return this.hotelname;
	}

	public void setHotelname(String hotelname) {
		this.hotelname = hotelname;
	}

	public Integer getSetup() {
		return this.setup;
	}

	public void setSetup(Integer setup) {
		this.setup = setup;
	}

	public String getOperatorid() {
		return this.operatorid;
	}

	public void setOperatorid(String operatorid) {
		this.operatorid = operatorid;
	}

	public String getOperatorname() {
		return this.operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}

	public Date getStarttime() {
		return this.starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getFinishtime() {
		return this.finishtime;
	}

	public void setFinishtime(Date finishtime) {
		this.finishtime = finishtime;
	}

	public Integer getOperationtype() {
		return this.operationtype;
	}

	public Long getCosttime() {
		return costtime;
	}

	public void setCosttime(Long costtime) {
		this.costtime = costtime;
	}

	public Integer getRelaxreason() {
		return relaxreason;
	}
	
	public void setOperationtype(Integer operationtype) {
		this.operationtype = operationtype;
	}

	public String getOperatorcontent() {
		return this.operatorcontent;
	}

	public void setOperatorcontent(String operatorcontent) {
		this.operatorcontent = operatorcontent;
	}

	public void setRelaxreason(Integer relaxreason) {
		this.relaxreason = relaxreason;
	}

}