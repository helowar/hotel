package com.mangocity.hotel.base.persistence;

import java.util.Date;

public class HtlFavourable {

	private Long id;
	
	//城市编码
	private String cityCode;
	
	//城市名称
	private String cityName;
	
	//酒店Id
	private Long hotelId;
	
	//酒店名称
	private String hotelName;
	
	//半价优惠
	private int favA;
	
	//7折优惠
	private int favB;
	
	//零得优惠
	private int favC;
	
	//更新人
	private String updateBy;
	
	//更新时间
	private Date updateTime;
	
	//软删除
	private int flag;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

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

	public int getFavA() {
		return favA;
	}

	public void setFavA(int favA) {
		this.favA = favA;
	}

	public int getFavB() {
		return favB;
	}

	public void setFavB(int favB) {
		this.favB = favB;
	}

	public int getFavC() {
		return favC;
	}

	public void setFavC(int favC) {
		this.favC = favC;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
