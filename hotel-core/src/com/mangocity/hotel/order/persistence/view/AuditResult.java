package com.mangocity.hotel.order.persistence.view;

import java.util.Date;

/**
 * 
 *  日审结果参数
 *  
 *  @author chenkeming
 */
public class AuditResult {

	/**
	 * 0开始的下标,房间序号
	 */
	private int roomIndex;
	
	
	/**
	 * 1:入住, 2:未入住
	 */
	private int noteResult;
	
	
	/**
	 * 入住审核时,如果有入住人姓名改变放到这里,和roomIndexs房间序号对应
	 */
	private String fellowName;
	
	/**
	 * 之前的入住人
	 */
	private String oldName;
	/**
	 * 入住审核时, 设置字符串的房间号
	 */
	private String roomNo;
		
	/**
	 * 有提前退房的提前退房日期数组,和roomIndexs房间序号对应
	 */
	private Date checkoutDate;
	
	/**
	 * 回访结果
	 */
	private int reciprocal;
	
	public Date getCheckoutDate() {
		return checkoutDate;
	}
	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}
	public String getFellowName() {
		return fellowName;
	}
	public void setFellowName(String fellowName) {
		this.fellowName = fellowName;
	}
	public int getNoteResult() {
		return noteResult;
	}
	public void setNoteResult(int noteResult) {
		this.noteResult = noteResult;
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
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	public int getReciprocal() {
		return reciprocal;
	}
	public void setReciprocal(int reciprocal) {
		this.reciprocal = reciprocal;
	}	
	    
}
