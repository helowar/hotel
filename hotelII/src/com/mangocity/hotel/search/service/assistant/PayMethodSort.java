package com.mangocity.hotel.search.service.assistant;


/**
 * 用于排序商品
 * @author 
 *
 */
public class PayMethodSort {
	
	
	/**
	 * 支付方式
	 */
	
	private String payMethod;
	
	/**
	 * 能否预订
	 */
	private boolean canbook;

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public boolean isCanbook() {
		return canbook;
	}

	public void setCanbook(boolean canbook) {
		this.canbook = canbook;
	}
	
	
	
}
