package com.mangocity.hotel.base.persistence;

import java.io.Serializable;

public class BatchUpdateParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2533430761916824349L;
	
	private String hotelName;
	private String supplyId;
	private String productManager;
	private String province;
	private String city;
	private String payMethod;
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getSupplyId() {
		return supplyId;
	}
	public void setSupplyId(String supplyId) {
		this.supplyId = supplyId;
	}
	public String getProductManager() {
		return productManager;
	}
	public void setProductManager(String productManager) {
		this.productManager = productManager;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	
	
	
  
}
