package com.mangocity.hotel.quotationdisplay.action;

import com.opensymphony.xwork2.ActionSupport;
import com.mangocity.hotel.base.web.InitServletImpl;

public class PriceTrendmapAction extends ActionSupport {

	private String cityCode;
	private String cityName;

	public String execute() {
		cityCode = cityCode.toUpperCase();
		cityName = InitServletImpl.cityObj.get(cityCode);
		if (cityName == null) {
			cityName = "香港";
		}
		return SUCCESS;
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

}
