package com.mangocity.hotel.base.persistence;

import java.util.Date;

public class HtlTempRoomState{
	private String roomName;
	private Long roomType;
	private String bedId;
	private Date saleDate;
	private Long quatoCount;
	private Long ableUseQuoatCount;
	private String newBedStatus;
	private String oldBedStatus;

	public HtlTempRoomState() {
	}
	

	public HtlTempRoomState(String roomName, Long roomType, String bedId, Date saleDate, Long quatoCount, Long ableUseQuoatCount) {
		this.roomName = roomName;
		this.roomType = roomType;
		this.bedId = bedId;
		this.saleDate = saleDate;
		this.quatoCount = quatoCount;
		this.ableUseQuoatCount = ableUseQuoatCount;
	}

	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public Long getRoomType() {
		return roomType;
	}
	public void setRoomType(Long roomType) {
		this.roomType = roomType;
	}
	public Date getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}
	public Long getAbleUseQuoatCount() {
		return ableUseQuoatCount;
	}
	public void setAbleUseQuoatCount(Long ableUseQuoatCount) {
		this.ableUseQuoatCount = ableUseQuoatCount;
	}
	public Long getQuatoCount() {
		return quatoCount;
	}
	public void setQuatoCount(Long quatoCount) {
		this.quatoCount = quatoCount;
	}


	public String getNewBedStatus() {
		return newBedStatus;
	}


	public void setNewBedStatus(String newBedStatus) {
		this.newBedStatus = newBedStatus;
	}


	public String getOldBedStatus() {
		return oldBedStatus;
	}


	public void setOldBedStatus(String oldBedStatus) {
		this.oldBedStatus = oldBedStatus;
	}
	

}
