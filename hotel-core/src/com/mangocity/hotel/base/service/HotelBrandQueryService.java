package com.mangocity.hotel.base.service;

import java.util.List;

import com.mangocity.hotel.base.persistence.HotelBrandinfo;


public interface HotelBrandQueryService {
	
	/**
	 * 全部酒店品牌获取 add by hushunqiang
	 */
	public List<HotelBrandinfo> queryHotelBrands();
	
	/**
	 * 全部酒店品牌获取 add by hushunqiang
	 */
	public List<HotelBrandinfo> queryHotelBybrands(String citycode,String brand);
}
