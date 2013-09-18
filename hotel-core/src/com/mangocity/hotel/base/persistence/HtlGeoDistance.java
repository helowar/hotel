package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

public class HtlGeoDistance implements Entity{
	   // Fields
    // 释放操作id
    private Long ID;
    private Long hotelId;
    private Long geoId;
    private Long geoType;
    private String cityCode;
    private String name;
    private double distance;
    private Date createTime;
    
	public HtlGeoDistance() {
	}
	
	public HtlGeoDistance(Long geoId, Long geoType, String cityCode, String name) {
		this.geoId = geoId;
		this.geoType = geoType;
		this.cityCode = cityCode;
		this.name = name;
	}

	public HtlGeoDistance(Long id, Long hotelId, Long geoId, Long geoType,
			String cityCode, String name, double distance, Date createTime) {
		ID = id;
		this.hotelId = hotelId;
		this.geoId = geoId;
		this.geoType = geoType;
		this.cityCode = cityCode;
		this.name = name;
		this.distance = distance;
		this.createTime = createTime;
	}
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
	public Long getGeoId() {
		return geoId;
	}
	public void setGeoId(Long geoId) {
		this.geoId = geoId;
	}
	public Long getGeoType() {
		return geoType;
	}
	public void setGeoType(Long geoType) {
		this.geoType = geoType;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

    
	

}
