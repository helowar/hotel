package com.mangocity.hweb.persistence;

import java.io.Serializable;
import java.util.List;


public class HotelBookingResultInfoForHkSale implements Serializable {
	
	//商业区编码
	private String  businessArea;
	
	//商业区中文名
	private String  businessAreaName;
	
	private int sort;
	
	private List<DistrictHotelInfo> districtHotelInfo;
	
	public List<DistrictHotelInfo> getDistrictHotelInfo() {
		return districtHotelInfo;
	}

	public void setDistrictHotelInfo(List<DistrictHotelInfo> districtHotelInfo) {
		this.districtHotelInfo = districtHotelInfo;
	}

	public String getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}

	public String getBusinessAreaName() {
		return businessAreaName;
	}

	public void setBusinessAreaName(String businessAreaName) {
		this.businessAreaName = businessAreaName;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
