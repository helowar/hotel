
package com.mangocity.hotel.newroomcontrol.service.assistant;



/**
 * 房控保留房协议传真信息辅助类
 *  @author zhijie.gu 2010-5-18
 */
public class FaxInfoForRoomcontrol  {
	//申请需要保留的房型名称
	private String roomTypeName;
	
	//申请需要保留的房型对应床型名称
	private String bedTypeName;
	
	//申请保留房的数量
	private String roomNumber;
	
	//申请保留房当天最晚可以保留至多少点
	private String lastTime;

	public String getRoomTypeName() {
		return roomTypeName;
	}

	public void setRoomTypeName(String roomTypeName) {
		this.roomTypeName = roomTypeName;
	}

	public String getBedTypeName() {
		return bedTypeName;
	}

	public void setBedTypeName(String bedTypeName) {
		this.bedTypeName = bedTypeName;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
}
