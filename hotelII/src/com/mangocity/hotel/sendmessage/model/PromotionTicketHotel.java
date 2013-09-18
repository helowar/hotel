package com.mangocity.hotel.sendmessage.model;

import java.io.Serializable;
import java.util.Date;

public class PromotionTicketHotel implements Serializable{

	/**
	 * id
	 * 
	 */
	private Long ticketHotelId;
	
	/**
	 * 酒店id
	 */
	private Long hotelId;
	
	/**
	 * 酒店名称
	 */
	private String hotelName;
	
	/**
	 * 送门票的类型
	 */
	private PromotionTicketType ticketType;
	
	/**
	 * 标记是否参与活动
	 */
	private boolean fagAttend;
	
	/**
	 * 创建日期
	 */
	private Date createDate;

	public Long getTicketHotelId() {
		return ticketHotelId;
	}

	public void setTicketHotelId(Long ticketHotelId) {
		this.ticketHotelId = ticketHotelId;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public PromotionTicketType getTicketType() {
		return ticketType;
	}

	public void setTicketType(PromotionTicketType ticketType) {
		this.ticketType = ticketType;
	}

	public boolean isFagAttend() {
		return fagAttend;
	}

	public void setFagAttend(boolean fagAttend) {
		this.fagAttend = fagAttend;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
}
