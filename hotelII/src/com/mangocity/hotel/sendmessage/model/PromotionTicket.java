package com.mangocity.hotel.sendmessage.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 港澳酒店暑期促销活动，门票类
 * @author liting
 *
 */
public class PromotionTicket implements Serializable {
	/**
	 * id
	 */
	private Long ticketId;
	/**
	 * 促销门票编码
	 */
	private String ticketCode;
	
	/**
	 * 门票的类别，是什么门票
	 */	
	private PromotionTicketType ticketType;
	
	/**
	 * 标记是否已经使用过了
	 */
	private boolean hasUsed;
	
	/**
	 * 创建日期
	 */
	private Date createDate;
	
	public Long getTicketId() {
		return ticketId;
	}
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
	public String getTicketCode() {
		return ticketCode;
	}
	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}
	public PromotionTicketType getTicketType() {
		return ticketType;
	}
	public void setTicketType(PromotionTicketType ticketType) {
		this.ticketType = ticketType;
	}
	public boolean isHasUsed() {
		return hasUsed;
	}
	public void setHasUsed(boolean hasUsed) {
		this.hasUsed = hasUsed;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	

}
