package com.mangocity.hotel.search.vo;

import java.io.Serializable;

public class HotelResultForWebVO extends HotelResultVO  {
	public HotelResultForWebVO(){
		super();
	}


	
	// 添加酒店特色设施的标示，给界面的样式用
	private String flagFreePlane;
	private String flagFreeGym;
	private String flagFreeStop;
	private String flagFreePool;
	
	private String haveFreePlane;
	private String haveFreeGym;
	private String haveFreeStop;
	private String haveFreePool;
	
	//for seo 
	private String cityCodeLower;
	private String bizZoneLower;
	public String getFlagFreePlane() {
		return flagFreePlane;
	}
	public void setFlagFreePlane(String flagFreePlane) {
		this.flagFreePlane = flagFreePlane;
	}
	public String getFlagFreeGym() {
		return flagFreeGym;
	}
	public void setFlagFreeGym(String flagFreeGym) {
		this.flagFreeGym = flagFreeGym;
	}
	public String getFlagFreeStop() {
		return flagFreeStop;
	}
	public void setFlagFreeStop(String flagFreeStop) {
		this.flagFreeStop = flagFreeStop;
	}
	public String getFlagFreePool() {
		return flagFreePool;
	}
	public void setFlagFreePool(String flagFreePool) {
		this.flagFreePool = flagFreePool;
	}
	public String getHaveFreePlane() {
		return haveFreePlane;
	}
	public void setHaveFreePlane(String haveFreePlane) {
		this.haveFreePlane = haveFreePlane;
	}
	public String getHaveFreeGym() {
		return haveFreeGym;
	}
	public void setHaveFreeGym(String haveFreeGym) {
		this.haveFreeGym = haveFreeGym;
	}
	public String getHaveFreeStop() {
		return haveFreeStop;
	}
	public void setHaveFreeStop(String haveFreeStop) {
		this.haveFreeStop = haveFreeStop;
	}
	public String getHaveFreePool() {
		return haveFreePool;
	}
	public void setHaveFreePool(String haveFreePool) {
		this.haveFreePool = haveFreePool;
	}
	public String getCityCodeLower() {
		return cityCodeLower;
	}
	public void setCityCodeLower(String cityCodeLower) {
		this.cityCodeLower = cityCodeLower;
	}
	public String getBizZoneLower() {
		return bizZoneLower;
	}
	public void setBizZoneLower(String bizZoneLower) {
		this.bizZoneLower = bizZoneLower;
	}
	
}
