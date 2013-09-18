package com.mangocity.hotel.search.model;

import java.io.Serializable;

public class CommonDataModel implements Serializable {

	private static final long serialVersionUID = -5049241066570259066L;
	
	private String code;
	
	private String fullName;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
