package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * 查询商业区或行政区的指定排序数据
 * @author guzhijie
 *
 */

public class HtlHotelSortByArea{
	//城市三字码
	private String cityCode ;
	
	//商业区
	private String bizZone ;
	
	//行政区
	private String zone ;
	
	//该商业区或行政区排序所在顺序
	private int sortByArea;

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getBizZone() {
		return bizZone;
	}

	public void setBizZone(String bizZone) {
		this.bizZone = bizZone;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public int getSortByArea() {
		return sortByArea;
	}

	public void setSortByArea(int sortByArea) {
		this.sortByArea = sortByArea;
	}
	
}