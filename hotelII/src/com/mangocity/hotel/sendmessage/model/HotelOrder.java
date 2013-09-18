package com.mangocity.hotel.sendmessage.model;

import java.io.Serializable;
import java.util.Date;

public class HotelOrder extends TargetOrder implements Serializable {

	private Date checkInDate;
	private Date checkOutDate;
	private int roomquantity;
	private Long hotelId;
	private String menberCd;
	
	public Date getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}
	public Date getCheckOutDate() {
		return checkOutDate;
	}
	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
	public int getRoomquantity() {
		return roomquantity;
	}
	public void setRoomquantity(int roomquantity) {
		this.roomquantity = roomquantity;
	}
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
	public String getMenberCd() {
		return menberCd;
	}
	public void setMenberCd(String menberCd) {
		this.menberCd = menberCd;
	}
	
		
}
