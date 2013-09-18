package com.mangocity.proxy.persistence;

import com.mangocity.util.Entity;

public class CreditCardSerialNo implements Entity{
	private static final long serialVersionUID = -6696492039442931800L;
	
	private Long ID;
	private String customerId;
	private String orderCode;
	private String serialNo;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}
	

}
