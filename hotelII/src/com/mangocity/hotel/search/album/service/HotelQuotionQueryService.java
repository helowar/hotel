package com.mangocity.hotel.search.album.service;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.search.vo.HotelResultVO;


/**
 * 提供给一些专辑查询酒店的报价、返现
 * 一些专辑的逻辑写在这个接口中
 * @author liting
 *
 */
public interface HotelQuotionQueryService {

	/**
	 * 通过酒店Id查询酒店相关的价格类型，返回一个List,暂时没有用上
	 * @param hotelIds
	 * @param priceTypeIds
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	public List<HotelResultVO> queryHotelQuotionInfoByHotelIds(List<String> hotelIds,Date checkInDate,Date checkOutDate);
	
	/**
	 * 通过priceTypeId 查询该价格类型的报价，返回一个Json字符串
	 * @param hotelId
	 * @param priceTypeId
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	public String queryHotelQuotionInfoByHotelId(String hotelId,String priceTypeId,Date checkInDate,Date checkOutDate,String projectcode);
}
