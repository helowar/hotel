package com.mangocity.ep.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AuditOrderItem {
  
	private Long epAuditItemId;
	private Long epDailyauditId;
	private String  checkinname;
	private String  actualcheckinname;
	private String  checkintime;
	private String  checkouttime;
	private Long  _checkouttime;
	private String  roomid;
	private String  roomname;
	private String  roomnumber;
	private String  affirmnumber;
	private String  auditresults;
	private String  hotelModifytime;
	private String  hotelModifyname;
	private String  ccModifytime;
	private String  ccModifyname;
	private String  createtime;
	private String attachAuditDate;
	public Long getEpAuditItemId() {
		return epAuditItemId;
	}
	public void setEpAuditItemId(Long epAuditItemId) {
		this.epAuditItemId = epAuditItemId;
	}
	public Long getEpDailyauditId() {
		return epDailyauditId;
	}
	public void setEpDailyauditId(Long epDailyauditId) {
		this.epDailyauditId = epDailyauditId;
	}
	public String getCheckinname() {
		return checkinname;
	}
	public void setCheckinname(String checkinname) {
		this.checkinname = checkinname;
	}
	public String getActualcheckinname() {
		return actualcheckinname;
	}
	public void setActualcheckinname(String actualcheckinname) {
		this.actualcheckinname = actualcheckinname;
	}
	public String getCheckintime() {
		return checkintime;
	}
	public void setCheckintime(String checkintime) {
		this.checkintime = checkintime;
	}
	public String getCheckouttime() {
		return checkouttime;
	}
	public void setCheckouttime(String checkouttime) {
		this.checkouttime = checkouttime;
	}
	public String getRoomid() {
		return roomid;
	}
	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
	public String getRoomname() {
		return roomname;
	}
	public void setRoomname(String roomname) {
		this.roomname = roomname;
	}
	public String getRoomnumber() {
		return roomnumber;
	}
	public void setRoomnumber(String roomnumber) {
		this.roomnumber = roomnumber;
	}
	public String getAffirmnumber() {
		return affirmnumber;
	}
	public void setAffirmnumber(String affirmnumber) {
		this.affirmnumber = affirmnumber;
	}
	public String getAuditresults() {
		return auditresults;
	}
	public void setAuditresults(String auditresults) {
		this.auditresults = auditresults;
	}
	public String getHotelModifytime() {
		return hotelModifytime;
	}
	public void setHotelModifytime(String hotelModifytime) {
		this.hotelModifytime = hotelModifytime;
	}
	public String getHotelModifyname() {
		return hotelModifyname;
	}
	public void setHotelModifyname(String hotelModifyname) {
		this.hotelModifyname = hotelModifyname;
	}
	public String getCcModifytime() {
		return ccModifytime;
	}
	public void setCcModifytime(String ccModifytime) {
		this.ccModifytime = ccModifytime;
	}
	public String getCcModifyname() {
		return ccModifyname;
	}
	public void setCcModifyname(String ccModifyname) {
		this.ccModifyname = ccModifyname;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getAttachAuditDate() {
		return attachAuditDate;
	}
	public void setAttachAuditDate(String attachAuditDate) {
		this.attachAuditDate = attachAuditDate;
	}
	public Long get_checkouttime() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		this._checkouttime = df.parse(checkouttime).getTime();
		return _checkouttime;
	}
    
	
}
