package com.mangocity.tmchotel.service.impl;



import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;



import com.mangocity.tmchotel.service.HotelQueryService;

public class HotelQueryServiceImpl implements HotelQueryService{
	
	
	private HotelManageWeb hotelManageWeb;
	
	private HotelPageForWebBean hotelPageForWebBean;
	
	private QueryHotelForWebBean queryHotelForWebBean;

	public HotelPageForWebBean queryHotelsForWeb(QueryHotelForWebBean queryForWebBean) {
		
		hotelPageForWebBean = hotelManageWeb.queryHotelsForWeb(queryForWebBean);
		
		
		// TODO 自动生成方法存根
		return null;
	}

	
	public HotelPageForWebBean getHotelPageForWebBean() {
		return hotelPageForWebBean;
	}

	public void setHotelPageForWebBean(HotelPageForWebBean hotelPageForWebBean) {
		this.hotelPageForWebBean = hotelPageForWebBean;
	}

	public QueryHotelForWebBean getQueryHotelForWebBean() {
		return queryHotelForWebBean;
	}

	public void setQueryHotelForWebBean(QueryHotelForWebBean queryHotelForWebBean) {
		this.queryHotelForWebBean = queryHotelForWebBean;
	}

	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

}
