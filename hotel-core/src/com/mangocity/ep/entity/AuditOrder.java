package com.mangocity.ep.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class AuditOrder {
  
	private Long epDailyAuditId;
	private Long orderid;
	private Long hotelid;
	private String hotelName;
	private String ordercd;
	private String checkintime;
	private String checkouttime;
	private Long _checkouttime;
	private String auditdate;
	private Integer roommount;
	private String roomid;
	private String roomname;
	private String audittype;
	private String hotel_state;
	private String cc_state;
	private String hotelModifytime;
	private String hotelModifyname;
	private String ccModifytime;
	private String ccModifyname;
	private String createtime;
	private List<AuditOrderItem> itemList;
	private Long days=0l;
	private String remark;
	public Long getDays() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		long from = df.parse(checkintime).getTime();
		long to = df.parse(checkouttime).getTime();
		days=(to - from) / (1000 * 60 * 60 * 24);
		return days;
	}
	public Long getEpDailyAuditId() {
		return epDailyAuditId;
	}
	public void setEpDailyAuditId(Long epDailyAuditId) {
		this.epDailyAuditId = epDailyAuditId;
	}
	public Long getOrderid() {
		return orderid;
	}
	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}
	public Long getHotelid() {
		return hotelid;
	}
	public void setHotelid(Long hotelid) {
		this.hotelid = hotelid;
	}
	public String getOrdercd() {
		return ordercd;
	}
	public void setOrdercd(String ordercd) {
		this.ordercd = ordercd;
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
	public String getAuditdate() {
		return auditdate;
	}
	public void setAuditdate(String auditdate) {
		this.auditdate = auditdate;
	}
	public Integer getRoommount() {
		return roommount;
	}
	public void setRoommount(Integer roommount) {
		this.roommount = roommount;
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
	public String getAudittype() {
		return audittype;
	}
	public void setAudittype(String audittype) {
		this.audittype = audittype;
	}
	public String getHotel_state() {
		return hotel_state;
	}
	public void setHotel_state(String hotel_state) {
		this.hotel_state = hotel_state;
	}
	public String getCc_state() {
		return cc_state;
	}
	public void setCc_state(String cc_state) {
		this.cc_state = cc_state;
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
	public List<AuditOrderItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<AuditOrderItem> itemList) {
		this.itemList = itemList;
	}
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long get_checkouttime() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		this._checkouttime = df.parse(checkouttime).getTime();
		return _checkouttime;
	}

    
	

}
