package com.mangocity.hotel.sendmessage.model;

import java.io.Serializable;
import java.util.Date;

public class PromotionTicketType implements Serializable{
	
	/**
	 * id
	 * 
	 */
	private Long ticketTypeId;
	
	/**
	 * 票的类型，1迪士尼乐园门票，2海洋公园门票，3杜莎夫人蜡像馆门票、4昂坪360缆车票
	 */
	private int ticketType;
	
	/**
	 * 类型的名字
	 */
	private String typeName;
	
	/**
	 * 简单说明
	 */
	private String comment;
	
	/**
	 * 是否参与活动,1:参与，0：不参与
	 */
	private boolean fagAttend;
	/**
	 * 创建日期
	 */
	private Date createDate;

	public Long getTicketTypeId() {
		return ticketTypeId;
	}

	public void setTicketTypeId(Long ticketTypeId) {
		this.ticketTypeId = ticketTypeId;
	}


	public int getTicketType() {
		return ticketType;
	}

	public void setTicketType(int ticketType) {
		this.ticketType = ticketType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
