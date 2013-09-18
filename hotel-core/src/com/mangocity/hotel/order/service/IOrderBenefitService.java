package com.mangocity.hotel.order.service;

import java.util.Map;

import com.mangocity.hotel.base.service.IBenefitService;
import com.mangocity.hotel.order.persistence.OrOrder;

public interface IOrderBenefitService extends IBenefitService{
	
	/**
	 * 生成订单明细后，计算订单明细的立减金额
	 * @param order
	 */
	public void calculateOrderItemBenefit(OrOrder order);
	
	
	/**
	 * 计算订单的优惠总金额
	 * @param rateMap 汇率
	 * @param order
	 */
	public void reCalculateBenefitAmount(Map<String, String> rateMap,OrOrder order);
	
	

}
