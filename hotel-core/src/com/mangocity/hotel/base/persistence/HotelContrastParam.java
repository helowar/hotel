package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.Date;

public class HotelContrastParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5700724179638974436L;
    
	private Long hotelId;
	private String hotelName;
	private String productManager;
	private String productAssistant;
	private String province;
	private String city;
	private String startDate;
	private String district;
	private String businesszone;
	private String endDate;
	
	
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
	public String getProductManager() {
		return productManager;
	}
	public void setProductManager(String productManager) {
		this.productManager = productManager;
	}
	public String getProductAssistant() {
		return productAssistant;
	}
	public void setProductAssistant(String productAssistant) {
		this.productAssistant = productAssistant;
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
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getBusinesszone() {
		return businesszone;
	}
	public void setBusinesszone(String businesszone) {
		this.businesszone = businesszone;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	
	
}
