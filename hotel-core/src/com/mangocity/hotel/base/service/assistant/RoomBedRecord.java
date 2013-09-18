package com.mangocity.hotel.base.service.assistant;

import java.util.Date;

import com.mangocity.util.DateUtil;

/**
 * 房态控制的辅助类
 * 
 * @author chenjuesu
 * 
 */
public class RoomBedRecord {
	/**
	 * 是否选中
	 */
	private int theSelected;

	/**
	 * 开始时间
	 */
	private Date theStart;

	/**
	 * 结束时间
	 */
	private Date theEnd;

	/**
	 * 星期
	 */
	private String theWeeks;

	/**
	 * 房型id
	 */
	private long theRoomTypeId;
	/**
	 * 所拥有的床型，如 1,2,3
	 */
	private String beds;

	/**
	 * 大床房态 1
	 */
	private Integer bigBedStatus;

	/**
	 * 双床房态 2
	 */
	private Integer doubleBedStatus;

	/**
	 * 单床房态 3
	 */
	private Integer singleBedStatus;
	
	/**
	 * 房型名称
	 */
	private String roomName;
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n房型为: ").append(roomName);
		sb.append("起止日期为: ").append(DateUtil.dateToString(theStart)).append("至")
			.append(DateUtil.dateToString(theStart));
		if(null != theWeeks)
			sb.append(" 星期为:").append(theWeeks);
		sb.append("更改房态为:");
		if(bigBedStatus != null)
			sb.append(" 大床/").append(getBedStatus(bigBedStatus));
		if(doubleBedStatus != null)
			sb.append(" 双床/").append(getBedStatus(doubleBedStatus));
		if(singleBedStatus != null)
			sb.append(" 单床/").append(getBedStatus(singleBedStatus));
		return sb.toString();
	}
	private String getBedStatus(int status){
		if(0 == status)
			return "Freesale";
		else if(1 == status)
			return "良好";
		else if(2 == status)
			return "紧张";
		else if(3 == status)
			return "不可超";
		else if(4 == status)
			return "满房";
		return "无此房态";
	}
	public Integer getBigBedStatus() {
		return bigBedStatus;
	}

	public void setBigBedStatus(Integer bigBedStatus) {
		this.bigBedStatus = bigBedStatus;
	}

	public Integer getDoubleBedStatus() {
		return doubleBedStatus;
	}

	public void setDoubleBedStatus(Integer doubleBedStatus) {
		this.doubleBedStatus = doubleBedStatus;
	}

	public Integer getSingleBedStatus() {
		return singleBedStatus;
	}

	public void setSingleBedStatus(Integer singleBedStatus) {
		this.singleBedStatus = singleBedStatus;
	}

	public String getTheWeeks() {
		if(null != theWeeks && theWeeks.endsWith(","))
			theWeeks = theWeeks.substring(0, theWeeks.length() - 1);
		return theWeeks;
	}

	public void setTheWeeks(String theWeeks) {
		this.theWeeks = theWeeks;
	}

	public Date getTheEnd() {
		return theEnd;
	}

	public void setTheEnd(Date theEnd) {
		this.theEnd = theEnd;
	}

	public long getTheRoomTypeId() {
		return theRoomTypeId;
	}

	public void setTheRoomTypeId(long theRoomTypeId) {
		this.theRoomTypeId = theRoomTypeId;
	}

	public int getTheSelected() {
		return theSelected;
	}

	public void setTheSelected(int theSelected) {
		this.theSelected = theSelected;
	}

	public Date getTheStart() {
		return theStart;
	}

	public void setTheStart(Date theStart) {
		this.theStart = theStart;
	}

	public String getBeds() {
		return beds;
	}

	public void setBeds(String beds) {
		this.beds = beds;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
}
