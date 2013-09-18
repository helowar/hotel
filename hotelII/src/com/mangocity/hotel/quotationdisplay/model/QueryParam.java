package com.mangocity.hotel.quotationdisplay.model;

import java.util.Date;

import com.mangocity.hotel.search.util.DateUtil;
/***
 * 查询条件类
 * @author panjianping
 *
 */
public class QueryParam {
	
	private boolean level; /*酒店档次，
                               酒店分两个档次：中高档酒店(true)和经济型酒店(false)。
                               中高档酒店包括三星级、高档型、四星级、豪华型、五星级酒店；
                               经济型酒店包括舒适型、二星级及以下的酒店 
                            */
     private Date date =DateUtil.getDate(DateUtil.getSystemDate());     //表示日期
     private String zone;  //酒店所属城区，null表示该城市的所有城区 
     private String cityCode="HKG"; //酒店所属城市的代码，默认为香港的城市代码
     
	
	public boolean getLevel() {
		return level;
	}
	public void setLevel(boolean level) {
		this.level = level;
	}
	
	
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
     
     

}
