package com.mangocity.hotel.order.service;


import com.mangocity.hotel.order.persistence.OrOrder;


public interface IOrderPriceService {
	
	/**
	 * 计算销售价/底价总额,将入住期间的每天的销售价/底价相加
	 * @param order
	 * @param type "SALE" or "BASE"
	 * @return
	 */
	
	public double accountSaleBasePriceTotals(OrOrder order,String type)throws Exception;

}