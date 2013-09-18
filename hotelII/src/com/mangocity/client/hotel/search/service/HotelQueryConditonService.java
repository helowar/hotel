package com.mangocity.client.hotel.search.service;

import com.mangocity.client.hotel.gwt.queryCondition.GWTQueryCondition;

public interface HotelQueryConditonService {
   
	/**
	 * 用GWT给界面设置查询条件
	 */
	public void setHotelQueryCondtion();
  
	/**
	 * 得到查询参数
	 */
	public GWTQueryCondition getGWTQueryCondition();
}
