package com.mangocity.hotel.order.persistence;

public class OrOrderExtInfo {
	/*
	 * 主键
	 */
	private Long ID;
	
	/*
	 * 订单主键
	 */
	private OrOrder order;
	
	/*
	 * 信息类别
	 * 01、订单提示信息
	 * 02、信用卡支付凭证
	 * 03、短信取消订单随机码
	 * 04、艺龙担保提示 
	 * 05、艺龙取消修改规则 
	 */
	private String type;
	
	/*
	 * 内容
	 */
	private String context;
	
	
	
	public OrOrderExtInfo() {
		
	}
	
	public OrOrderExtInfo(String type, String context, OrOrder order) {
		this.type = type;
		this.context = context;
		this.order = order;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public OrOrder getOrder() {
		return order;
	}

	public void setOrder(OrOrder order) {
		this.order = order;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
}
