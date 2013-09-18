package com.mangocity.hotel.dreamweb.search.dao;

import java.util.List;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.search.model.HotelBrandBean;
 
public interface HotelBrandSearchDao {

	/**
	 * 根据品牌id查询品牌名称
	 */
	public String queryHotelBrandName(String brandCode);
	
	/**
	 * 根据品牌id查询品牌介绍
	 */
	public String queryBrandIntroduce(String brandCode);
	
	/**
	 * 按酒店数的多少来查询酒店的其他品牌信息，不包括该品牌
	 */
	public List<Object[]> queryOtherHotelBrands(String brandCode);
	
	/**
	 * 按酒店数的多少来查询酒店的其他品牌信息，不包括该品牌
	 */
	public List<Object[]> queryOtherHotelBrands(String brandCode,String cityCode);
}
