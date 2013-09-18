package com.mangocity.hotel.pricelowest.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

public class HtlLowestTask implements Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long ID;
	private String cityCode;//城市名称
	private Long hotelId;//酒店Id
	private Boolean isFinish;//是否执行
	private Date finishTime;//完成时间
	private Boolean isTaken;//是否获取
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
	public Boolean getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(Boolean isFinish) {
		this.isFinish = isFinish;
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
	public Boolean getIsTaken() {
		return isTaken;
	}
	public void setIsTaken(Boolean isTaken) {
		this.isTaken = isTaken;
	}

}
