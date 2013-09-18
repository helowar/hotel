package com.mangocity.hagtb2b.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.mangocity.hagtb2b.persistence.HtlB2bIncrease;


public interface HtlB2bDao {
	/**
	 * 
	 * @param minPriceParams
	 * @param date
	 * @return
	 */
	List<Object[]> queryMinPrice(Set<String> minPriceParams,Date date);
	
	/**
	 * 
	 * @param hotelIdParams 酒店ID查询条件
	 * @param priceTypeIdParams 价格类型查询条件
	 * @param inDate //入住日期
	 * @param outDate 
	 * @return
	 */
	List<HtlB2bIncrease> queryIncreasePrice(List<Long> hotelIdParams,List<Long> priceTypeIdParams,Date inDate,Date outDate);
	
	/**
	 * 根据酒店Id获取批量底价支付信息
	 * @param hotelIds
	 * @param inDate
	 * @param outDate
	 * @return
	 */
	List<HtlB2bIncrease> queryIncreasePrice(List<String> hotelIds, Date inDate);
}
