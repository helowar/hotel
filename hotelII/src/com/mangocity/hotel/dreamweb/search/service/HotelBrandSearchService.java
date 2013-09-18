package com.mangocity.hotel.dreamweb.search.service;

import java.util.List;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.search.model.BrandCitiesByLetter;
import com.mangocity.hotel.search.model.HotelBrandBean;
import com.mangocity.hotel.search.model.HotelForCityBean;
 

/**
 * @author diandian.hou
 * @date 2011-10-10
 */

public interface HotelBrandSearchService {
    
	/**
	 * 根据品牌查询酒店
	 */
	public List<HotelForCityBean> queryHotelsByBrand(String brandCode);
	
	/**
	 * 根据品牌和城市查询酒店
	 */
	public List<HotelForCityBean> queryHotelsByBrand(String brandCode,String cityCode);
	
	/**
	 * 根据品牌id查询品牌名称
	 */
	public String queryHotelBrandName(String brandCode);
	
	/**
	 * 把查询好的List<HotelForCityBean>按字母排序
	 */
	public List<BrandCitiesByLetter> sortBrandCitiesByLetter(List<HotelForCityBean> brandHotelList);
	
	/**
	 * 根据品牌id查询品牌介绍
	 */
	public String queryBrandIntroduce(String brandCode);
	
	/**
	 * 按酒店数的多少来查询酒店的其他品牌信息，不包括该品牌
	 */
	public List<HotelBrandBean> queryOtherHotelBrands(String brandCode);
	
	/**
	 * 按酒店数的多少来查询酒店的其他品牌信息，不包括该品牌
	 */
	public List<HotelBrandBean> queryOtherHotelBrands(String brandCode,String cityCode);
}
