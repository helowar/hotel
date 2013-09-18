package com.mangocity.hotel.order.service.assistant;

import com.mangocity.hotel.order.persistence.DaDailyauditItemSubtable;

public class DaSubItem {
	/**
	 * 房间号
	 */
	private Integer roomIndex;
	/**
	 * 入住审核信息
	 */
	private DaDailyauditItemSubtable checkInSub;
	/**
	 * 退房审核信息
	 */
	private DaDailyauditItemSubtable checkOutSub;
	
	public DaDailyauditItemSubtable getCheckInSub() {
		return checkInSub;
	}
	public void setCheckInSub(DaDailyauditItemSubtable checkInSub) {
		this.checkInSub = checkInSub;
	}
	public DaDailyauditItemSubtable getCheckOutSub() {
		return checkOutSub;
	}
	public void setCheckOutSub(DaDailyauditItemSubtable checkOutSub) {
		this.checkOutSub = checkOutSub;
	}
	public Integer getRoomIndex() {
		return roomIndex;
	}
	public void setRoomIndex(Integer roomIndex) {
		this.roomIndex = roomIndex;
	}
}
