/**
 * 
 */
package com.mangocity.webnew.persistence;

import java.util.Date;

/**
 * 统计
 * @author xiongxiaojun
 *
 */
public class HtlChannelClickLog {
    private Long ID;
    private Long hotelId;
    private String projectCode;
    private Date clickDate;
    private String clickTime;
    private Date checkInDate;
    private Date checkOutDate;
    private String click;
    private String city;
    private String roomType;
    private String channel;
    private double roomPrice;
    private long priceTypeId;
    private String payMethod;
	
	public Long getID() {
		return ID;
	}
	public void setID(Long id) {
		ID = id;
	}
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public Date getClickDate() {
		return clickDate;
	}
	public void setClickDate(Date clickDate) {
		this.clickDate = clickDate;
	}
	public String getClickTime() {
		return clickTime;
	}
	public void setClickTime(String clickTime) {
		this.clickTime = clickTime;
	}
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
	public String getClick() {
		return click;
	}
	public void setClick(String click) {
		this.click = click;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public double getRoomPrice() {
		return roomPrice;
	}
	public void setRoomPrice(double roomPrice) {
		this.roomPrice = roomPrice;
	}
	public long getPriceTypeId() {
		return priceTypeId;
	}
	public void setPriceTypeId(long priceTypeId) {
		this.priceTypeId = priceTypeId;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	
}
