package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 * HtlAddscope generated by MyEclipse - Hibernate Tools
 */

public class HtlArea extends CEntity implements Entity {

    // Fields

    // id
    private Long ID;

    private String areaName;

    private String areaCode;

    private String cityName;

    private String cityCode;

    private String qpinyin;

    private String jpinyin;
    
    private String stateCode;
    
    private String stateName;

    public String getJpinyin() {
        return jpinyin;
    }

    public void setJpinyin(String jpinyin) {
        this.jpinyin = jpinyin;
    }

    public String getQpinyin() {
        return qpinyin;
    }

    public void setQpinyin(String qpinyin) {
        this.qpinyin = qpinyin;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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

    public HtlArea() {

    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

}