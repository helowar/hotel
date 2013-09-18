package com.mangocity.hotel.search.model;

import java.util.ArrayList;
import java.util.List;

public class RoomType {
	
	/**
	 * 房型Id
	 */
	private long roomTypeId;
	
	/**
	 * 房型名称
	 */
	private String roomTypeName;
    /**
     * 推荐级别
     */
    private String recommend;
	
    /**
	 * 房间数量
	 */
	private String roomNumber;
    /**
     * 房间面积
     */
	private String roomAcreage;
    
	/**
	 * 房型所在楼层
	 */
	private String roomFloor;

	
	
	/**
	 * 最多入住人数
	 */
	private int maxNumOfPersons;
	
	/**
	 * 房间设施（包括了宽带，加床）(值为数据库中的值)
	 */
	private String roomEquipment;
	
	/**
	 * 是否有加床
	 */
	private boolean flagAddBed;
	
	/**
	 * 加床数量
	 */
	private int addBedNum;
	
	/**
	 * 是否是中旅房型
	 */
	private boolean flagCtsHK;
	
	/**
	 * 基础房型
	 */
	private boolean baseRoom;
	
	/**
	 * 加床情况
	 */
	private List<AddBed> bedLst;
	
	/**
	 * 加早情况
	 */
	private List<AddBreakfast> breakfastLst;

	
	
	public long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public String getRoomTypeName() {
		return roomTypeName;
	}

	public void setRoomTypeName(String roomTypeName) {
		this.roomTypeName = roomTypeName;
	}

	public String getRoomAcreage() {
		return roomAcreage;
	}

	public void setRoomAcreage(String roomAcreage) {
		this.roomAcreage = roomAcreage;
	}

	public String getRoomFloor() {
		return roomFloor;
	}

	public void setRoomFloor(String roomFloor) {
		this.roomFloor = roomFloor;
	}

	public int getMaxNumOfPersons() {
		return maxNumOfPersons;
	}

	public void setMaxNumOfPersons(int maxNumOfPersons) {
		this.maxNumOfPersons = maxNumOfPersons;
	}

	public String getRoomEquipment() {
		return roomEquipment;
	}

	public void setRoomEquipment(String roomEquipment) {
		this.roomEquipment = roomEquipment;
	}

	public boolean isFlagAddBed() {
		return flagAddBed;
	}

	public void setFlagAddBed(boolean flagAddBed) {
		this.flagAddBed = flagAddBed;
	}

	public int getAddBedNum() {
		return addBedNum;
	}

	public void setAddBedNum(int addBedNum) {
		this.addBedNum = addBedNum;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}


	public boolean isFlagCtsHK() {
		return flagCtsHK;
	}

	public void setFlagCtsHK(boolean flagCtsHK) {
		this.flagCtsHK = flagCtsHK;
	}

	public List<AddBed> getBedLst() {
		if (null == bedLst) {
			bedLst = new ArrayList<AddBed>();
		}
		return bedLst;
	}

	public void setBedLst(List<AddBed> bedLst) {
		this.bedLst = bedLst;
	}

	public List<AddBreakfast> getBreakfastLst() {
		if (null == breakfastLst) {
			breakfastLst = new ArrayList<AddBreakfast>();
		}
		return breakfastLst;
	}

	public void setBreakfastLst(List<AddBreakfast> breakfastLst) {
		this.breakfastLst = breakfastLst;
	}

	public boolean isBaseRoom() {
		return baseRoom;
	}

	public void setBaseRoom(boolean baseRoom) {
		this.baseRoom = baseRoom;
	}
	
}
