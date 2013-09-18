package com.mangocity.hotel.sendmessage.model;

/**
 * 发送短信的目标订单
 * @author liting
 *
 */
public abstract class TargetOrder {
	
	/**
	 * 手机号码
	 */
	protected String mobile;
	
	/**
	 * 订单编号
	 */
	protected String ordercd;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOrdercd() {
		return ordercd;
	}
	public void setOrdercd(String ordercd) {
		this.ordercd = ordercd;
	}

	
	
	
	
}
