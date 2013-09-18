package com.mangocity.hotel.base.persistence;

import java.util.Date;

/**
 * 房控操作日志列表显示辅助实体
 */

public class HotelRoomcontrolLogsDisplay implements java.io.Serializable {

/**
	 * 主键ID
	 */
	private Long logid;
	
	/**
	 * 操作人姓名
	 */
	private String operatorname;
	
	/**
	 * 任务结束时间
	 */
	private Date finishtime;
	
	/**
	 * 辅助字段，用来存放拼装字表中操作内容后的内容
	 */
	private String roomStateLogContent;
	
	/**
	 * 辅助字段，用来存放拼装字表中操作内容后的内容
	 */
	private int totalPageNumber;
	
	/**
	 * 总共几页
	 */
	private int pageSize;
	
	private String dealWithSource;

	public String getDealWithSource() {
		return dealWithSource;
	}

	public void setDealWithSource(String dealWithSource) {
		this.dealWithSource = dealWithSource;
	}

	public Long getLogid() {
		return logid;
	}

	public void setLogid(Long logid) {
		this.logid = logid;
	}

	public String getOperatorname() {
		return operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}

	public Date getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Date finishtime) {
		this.finishtime = finishtime;
	}

	public String getRoomStateLogContent() {
		return roomStateLogContent;
	}

	public void setRoomStateLogContent(String roomStateLogContent) {
		this.roomStateLogContent = roomStateLogContent;
	}

	public int getTotalPageNumber() {
		return totalPageNumber;
	}

	public void setTotalPageNumber(int totalPageNumber) {
		this.totalPageNumber = totalPageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	// Constructors

	

}