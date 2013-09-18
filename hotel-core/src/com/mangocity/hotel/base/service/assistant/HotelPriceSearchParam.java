package com.mangocity.hotel.base.service.assistant;

import java.util.Date;



public class HotelPriceSearchParam {
	
	private Long hotelId;
	  /**
	 * 房型ID
	 */
    private Long roomTypeId;
    
    /**
	 * 面付/预付<br>
	 * 为了和酒店本部一致，这里采用字符串:<br>
	 * 1. "pay" : 面付<br>
	 * 2. "pre_pay" : 预付<br>
	 * 
	 */
    private String payMethod;

    /**
	 * 面付转预付标识
	 */
    private boolean payToPrepay;
    
    /**
	 * 入住日期
	 */
    private Date checkinDate;

    /**
	 * 退房时间
	 */
    private Date checkoutDate;

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public Long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(Long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public boolean isPayToPrepay() {
		return payToPrepay;
	}

	public void setPayToPrepay(boolean payToPrepay) {
		this.payToPrepay = payToPrepay;
	}

	public Date getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(Date checkinDate) {
		this.checkinDate = checkinDate;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}
}
