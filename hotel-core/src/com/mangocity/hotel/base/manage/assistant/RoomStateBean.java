package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.bean.RoomTypeStatus;

/**
 */
public class RoomStateBean implements Serializable {
	
	/**
	 * refactor
	 * 增加泛型
	 */
    private List<RoomTypeStatus> roomTypeStatus = new ArrayList<RoomTypeStatus>();

    private String[] week;

    private Date begindate;

    private Date enddate;

    private long hotelID;

    private long roomTypeID;

    private String roomStatus;

    private String userId;

    private String userName;

    private String processRemark;

    private String isRoomStatusReport;

    public Date getBegindate() {
        return begindate;
    }

    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public long getHotelID() {
        return hotelID;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public String[] getWeek() {
        return week;
    }

    public void setWeek(String[] week) {
        this.week = week;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public long getRoomTypeID() {
        return roomTypeID;
    }

    public void setRoomTypeID(long roomTypeID) {
        this.roomTypeID = roomTypeID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProcessRemark() {
        return processRemark;
    }

    public void setProcessRemark(String processRemark) {
        this.processRemark = processRemark;
    }

    public String getIsRoomStatusReport() {
        return isRoomStatusReport;
    }

    public void setIsRoomStatusReport(String isRoomStatusReport) {
        this.isRoomStatusReport = isRoomStatusReport;
    }

	public List<RoomTypeStatus> getRoomTypeStatus() {
		return roomTypeStatus;
	}

	public void setRoomTypeStatus(List<RoomTypeStatus> roomTypeStatus) {
		this.roomTypeStatus = roomTypeStatus;
	}
}
