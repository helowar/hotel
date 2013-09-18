package com.mangocity.hagtb2b.service;

import java.util.Date;
import java.util.List;

import com.mangocity.hagtb2b.persistence.HtlB2bIncrease;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;

public interface IHtlB2bService {
	/**
	 * b2b取消优惠立减功能
	 * @param hotelPageForWebBean
	 * @return
	 */
	public HotelPageForWebBean setBenefitFlagAllFlase(HotelPageForWebBean hotelPageForWebBean);
	
	HotelPageForWebBean judgeB2BIncrease(HotelPageForWebBean hotelPageForWebBean,Date inDate,Date outDate);
	
	/**
	 * 
	 * @param items  QueryHotelForWebSaleItems集合
	 * @param hotelId 酒店ID
	 * @param priceTypeIds 价格类型ID集合
	 * @param inDate 入店日期
	 * @param outDate 离店日期
	 * @return
	 */
	List<QueryHotelForWebSaleItems> modifyIncreaePrice(List<QueryHotelForWebSaleItems> items,long hotelId,long priceTypeId,Date inDate,Date outDate);

	List<HtlB2bIncrease> queryIncreasePrice(Long hotelIdParam,
			Long priceTypeIdParam, Date inDate, Date outDate);
	/**
	 * 提供给订单校验的接口
	 * @param order
	 * @return
	 */
	OrOrder modifyOrderIncreasePrice(OrOrder order);
}
