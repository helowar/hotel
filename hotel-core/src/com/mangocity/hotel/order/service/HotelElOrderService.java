package com.mangocity.hotel.order.service;

import java.math.BigDecimal;
import java.util.Date;

import com.mangocity.hotel.base.persistence.HtlElAssure;
import com.mangocity.hotel.base.persistence.HtlElAssureRule;

/**
 * 处理艺龙订单
 *
 */
public interface HotelElOrderService {
	/**
	 * 根据芒果价格类型ID,入住日期 查询该价格类型所对应的艺龙担保规则
	 * @param priceTypeId
	 * @param checkInDate
	 * @return
	 */
	HtlElAssureRule queryElAssureRule(long priceTypeId,Date checkInDate);
	
	/**
	 * 计算艺龙担保金额
	 * @param priceTypeId
	 * @param checkInDate
	 * @param priceNum
	 * @param firstNightSalePrice
	 * @return
	 */
	HtlElAssure calculateElongAssureRule(long priceTypeId,Date checkInDate,double priceNum,double firstNightSalePrice);
}
