package com.mangocity.tmchotel.service;


import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;






public interface HotelQueryService {
	
	/**
	 * 酒店查询接口
	 */
	public HotelPageForWebBean queryHotelsForWeb(QueryHotelForWebBean queryForWebBean);

}
