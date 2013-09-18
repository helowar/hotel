package com.mangocity.hdl.hotel.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ratePlanId",
    "ratePlanName",
    "guestType",
    "paymentType",
    "minAdvanceBookingHours",
    "maxAdvanceBookingHours",
    "minStayDays",
    "maxStayDays",
    "minCheckInRooms"
})
@XmlRootElement(name = "RatePlanResponse")
public class RatePlanResponse  implements Serializable{
	private static final long serialVersionUID = 9050093022203188236L;
	/**
	 * 价格类型ID
	 */
	private int ratePlanId;
	
	/**
	 * 价格类型名称
	 */
	private String ratePlanName;
	
	/**
	 * 客人类别
	 */
	private String guestType;
	
	/**
	 * 支付方式
	 */
	private String paymentType;
	
	/**
	 * 最少提前预订小时数
	 */
	private int minAdvanceBookingHours;
	
	/**
	 * 最多提前预订小时数
	 */
	private int maxAdvanceBookingHours;
	
	/**
	 * 最少入住天数
	 */
	private int minStayDays;
	
	/**
	 * 最长入住天数
	 */
	private int maxStayDays;
	
	/**
	 * 首日最少房间数
	 */
	private int minCheckInRooms;

	public int getRatePlanId() {
		return ratePlanId;
	}

	public void setRatePlanId(int ratePlanId) {
		this.ratePlanId = ratePlanId;
	}

	public String getRatePlanName() {
		return ratePlanName;
	}

	public void setRatePlanName(String ratePlanName) {
		this.ratePlanName = ratePlanName;
	}

	public String getGuestType() {
		return guestType;
	}

	public void setGuestType(String guestType) {
		this.guestType = guestType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public int getMinAdvanceBookingHours() {
		return minAdvanceBookingHours;
	}

	public void setMinAdvanceBookingHours(int minAdvanceBookingHours) {
		this.minAdvanceBookingHours = minAdvanceBookingHours;
	}

	public int getMaxAdvanceBookingHours() {
		return maxAdvanceBookingHours;
	}

	public void setMaxAdvanceBookingHours(int maxAdvanceBookingHours) {
		this.maxAdvanceBookingHours = maxAdvanceBookingHours;
	}

	public int getMinStayDays() {
		return minStayDays;
	}

	public void setMinStayDays(int minStayDays) {
		this.minStayDays = minStayDays;
	}

	public int getMaxStayDays() {
		return maxStayDays;
	}

	public void setMaxStayDays(int maxStayDays) {
		this.maxStayDays = maxStayDays;
	}

	public int getMinCheckInRooms() {
		return minCheckInRooms;
	}

	public void setMinCheckInRooms(int minCheckInRooms) {
		this.minCheckInRooms = minCheckInRooms;
	}
	
}
