package com.mangocity.hotel.pricelowest.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

public class HtlHighestReturnTask implements Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long ID;
	private String cityCode;//城市名称
	private Long hotelId;//酒店Id
	private int isFinish;//是否执行
	private Date finishTime;//完成时间
	private int isTaken;//是否获取
	
	
	public Long getID() {
		return ID;
	}
	public void setID(Long id) {
		ID = id;
	}
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public int getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(int isFinish) {
		this.isFinish = isFinish;
	}
	public int getIsTaken() {
		return isTaken;
	}
	public void setIsTaken(int isTaken) {
		this.isTaken = isTaken;
	}
}
