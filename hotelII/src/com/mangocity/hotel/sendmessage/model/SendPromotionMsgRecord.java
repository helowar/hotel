package com.mangocity.hotel.sendmessage.model;

import java.io.Serializable;
import java.util.Date;

public class SendPromotionMsgRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 发送促销信息记录id号。
	 */
	private Long recordId;
	
	/**
	 * 订单编号
	 */
	private String orderCd;
	
	/**
	 * 活动专辑代码号，5位数字
	 */
	private String funtionCode;
	
	/**
	 * 发送的业务线,例如给机票订单发送短信：则为flight
	 */
	private String productOrder;
	
	private String sendMobile;
	
	 /**
	 * Unicall返回ID,发送短信返回的id。
	 * 
	 */
	private Long unicallRetId;
		
	/**
	 * 活动简单说明
	 */
	private String funtionRemark;
	
	/**
	 * 保存使用的代金券的编码
	 */
	private String ticketCode;
	/**
	 * 记录创建的日期
	 */
	private Date createDate;

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public String getOrderCd() {
		return orderCd;
	}

	public void setOrderCd(String orderCd) {
		this.orderCd = orderCd;
	}

	public String getFuntionCode() {
		return funtionCode;
	}

	public void setFuntionCode(String funtionCode) {
		this.funtionCode = funtionCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getProductOrder() {
		return productOrder;
	}

	public void setProductOrder(String productOrder) {
		this.productOrder = productOrder;
	}

	public String getSendMobile() {
		return sendMobile;
	}

	public void setSendMobile(String sendMobile) {
		this.sendMobile = sendMobile;
	}

	public Long getUnicallRetId() {
		return unicallRetId;
	}

	public void setUnicallRetId(Long unicallRetId) {
		this.unicallRetId = unicallRetId;
	}

	public String getFuntionRemark() {
		return funtionRemark;
	}

	public void setFuntionRemark(String funtionRemark) {
		this.funtionRemark = funtionRemark;
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	
	
	
}
