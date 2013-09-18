package com.mangocity.hotel.base.persistence;

public class MangoEmapVo {
	private Long id;
	private Long type;
	private String cityName;
	private String address;
	private String name;
	private Double latitude;
	private Double longitude;
	//百度纬度
	private Double baiduLatitude;
	//百度经度
	private Double baiduLongitude;
	
	public MangoEmapVo() {
	}
	public MangoEmapVo(Long id, Long type, String address, String name,
			Double latitude, Double longitude) {
		this.id = id;
		this.type = type;
		this.address = address;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Double getBaiduLatitude() {
		return baiduLatitude;
	}
	public void setBaiduLatitude(Double baiduLatitude) {
		this.baiduLatitude = baiduLatitude;
	}
	public Double getBaiduLongitude() {
		return baiduLongitude;
	}
	public void setBaiduLongitude(Double baiduLongitude) {
		this.baiduLongitude = baiduLongitude;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}
