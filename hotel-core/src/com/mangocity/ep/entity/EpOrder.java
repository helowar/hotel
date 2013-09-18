package com.mangocity.ep.entity;



public class EpOrder {
  
	private Long epOrder_id;
	private Long orderid;
	private Long hotelid;
	private String hotelname;
	private Long roomtypeid;
	private Long childroomtypeid;
	private String ordercd;
	private String fellownames;
	private String checkindate;
	private String checkoutdate;
	private String roomtypename;
	private Integer roomquantity;
	private String bedtype;
	private String arrivaltime;
	private String latestarrivaltime;
	private double sumrmb;
	private String paymethod;
	private Integer iscreditassured;
	private String specialrequest;
	private Integer hotelstatus;
	private Integer ccstatus;
	private String introduce;
	private String remark;
	private String createTime;
	private String modifyTime;
	private String modifyName;
	private String ishotelconfirm;
	private String isccConfirm;
	private String paymentCurrency;
	private String ccModifyTime;
	private String ccModifyName;
	private String hotelConfirmNo;
	private int confirmspecialReqType;
	public Long getEpOrder_id() {
		return epOrder_id;
	}
	public void setEpOrder_id(Long epOrder_id) {
		this.epOrder_id = epOrder_id;
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
	public String getHotelname() {
		return hotelname;
	}
	public void setHotelname(String hotelname) {
		this.hotelname = hotelname;
	}
	public Long getRoomtypeid() {
		return roomtypeid;
	}
	public void setRoomtypeid(Long roomtypeid) {
		this.roomtypeid = roomtypeid;
	}
	public Long getChildroomtypeid() {
		return childroomtypeid;
	}
	public void setChildroomtypeid(Long childroomtypeid) {
		this.childroomtypeid = childroomtypeid;
	}
	public String getOrdercd() {
		return ordercd;
	}
	public void setOrdercd(String ordercd) {
		this.ordercd = ordercd;
	}
	public String getFellownames() {
		return fellownames;
	}
	public void setFellownames(String fellownames) {
		this.fellownames = fellownames;
	}
    
	public String getCheckindate() {
		return checkindate;
	}
	public void setCheckindate(String checkindate) {
		this.checkindate = checkindate;
	}
	public String getCheckoutdate() {
		return checkoutdate;
	}
	public void setCheckoutdate(String checkoutdate) {
		this.checkoutdate = checkoutdate;
	}
	public String getRoomtypename() {
		return roomtypename;
	}
	public void setRoomtypename(String roomtypename) {
		this.roomtypename = roomtypename;
	}
	public Integer getRoomquantity() {
		return roomquantity;
	}
	public void setRoomquantity(Integer roomquantity) {
		this.roomquantity = roomquantity;
	}
	public String getBedtype() {
		return bedtype;
	}
	public void setBedtype(String bedtype) {
		this.bedtype = bedtype;
	}
	public String getArrivaltime() {
		return arrivaltime;
	}
	public void setArrivaltime(String arrivaltime) {
		this.arrivaltime = arrivaltime;
	}
	public String getLatestarrivaltime() {
		return latestarrivaltime;
	}
	public void setLatestarrivaltime(String latestarrivaltime) {
		this.latestarrivaltime = latestarrivaltime;
	}
	public double getSumrmb() {
		return sumrmb;
	}
	public void setSumrmb(double sumrmb) {
		this.sumrmb = sumrmb;
	}
	public String getPaymethod() {
		return paymethod;
	}
	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}
	public Integer getIscreditassured() {
		return iscreditassured;
	}
	public void setIscreditassured(Integer iscreditassured) {
		this.iscreditassured = iscreditassured;
	}
	public String getSpecialrequest() {
		return specialrequest;
	}
	public void setSpecialrequest(String specialrequest) {
		this.specialrequest = specialrequest;
	}
	public Integer getHotelstatus() {
		return hotelstatus;
	}
	public void setHotelstatus(Integer hotelstatus) {
		this.hotelstatus = hotelstatus;
	}
	public Integer getCcstatus() {
		return ccstatus;
	}
	public void setCcstatus(Integer ccstatus) {
		this.ccstatus = ccstatus;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyName() {
		return modifyName;
	}
	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}
	public String getIshotelconfirm() {
		return ishotelconfirm;
	}
	public void setIshotelconfirm(String ishotelconfirm) {
		this.ishotelconfirm = ishotelconfirm;
	}
	public String getIsccConfirm() {
		return isccConfirm;
	}
	public void setIsccConfirm(String isccConfirm) {
		this.isccConfirm = isccConfirm;
	}
	public String getPaymentCurrency() {
		return paymentCurrency;
	}
	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}
	public String getCcModifyTime() {
		return ccModifyTime;
	}
	public void setCcModifyTime(String ccModifyTime) {
		this.ccModifyTime = ccModifyTime;
	}
	public String getCcModifyName() {
		return ccModifyName;
	}
	public void setCcModifyName(String ccModifyName) {
		this.ccModifyName = ccModifyName;
	}
	public String getHotelConfirmNo() {
		return hotelConfirmNo;
	}
	public void setHotelConfirmNo(String hotelConfirmNo) {
		this.hotelConfirmNo = hotelConfirmNo;
	}
	public int getConfirmspecialReqType() {
		return confirmspecialReqType;
	}
	public void setConfirmspecialReqType(int confirmspecialReqType) {
		this.confirmspecialReqType = confirmspecialReqType;
	}
	
	
	
}
