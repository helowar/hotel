
package com.mangocity.hotel.newroomcontrol.service.assistant;

import java.util.List;


/**
 * 房控保留房协议传真信息辅助类
 *  @author zhijie.gu 2010-5-18
 */
public class NewRoomControlFaxInfo  {
	//保留房申请开始时间
	private String protocolBeginDate;
	
	//保留房申请结束时间
	private String protocolEndDate;
	
	//当天时间
	private String today;
	
	//联系人
	private String linkMan;
	
	//酒店名称
	private String hotelChnName;
	
	
	//联系电话
	private String linkNumber;
	
	//房型加床型的为单位的数据list list里面是FaxInfoForRoomcontrol对象
	private List roomBedInfo;

	public String getProtocolBeginDate() {
		return protocolBeginDate;
	}

	public void setProtocolBeginDate(String protocolBeginDate) {
		this.protocolBeginDate = protocolBeginDate;
	}

	public String getProtocolEndDate() {
		return protocolEndDate;
	}

	public void setProtocolEndDate(String protocolEndDate) {
		this.protocolEndDate = protocolEndDate;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getHotelChnName() {
		return hotelChnName;
	}

	public void setHotelChnName(String hotelChnName) {
		this.hotelChnName = hotelChnName;
	}

	public String getLinkNumber() {
		return linkNumber;
	}

	public void setLinkNumber(String linkNumber) {
		this.linkNumber = linkNumber;
	}

	public List getRoomBedInfo() {
		return roomBedInfo;
	}

	public void setRoomBedInfo(List roomBedInfo) {
		this.roomBedInfo = roomBedInfo;
	}
}
