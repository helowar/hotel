package com.mangocity.hotel.base.service;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlOnSale;

public interface IOldOnSaleService {

	/**
	 * 查询商品对应渠道上下架信息
	 * @return
	 */
	public List<HtlOnSale> queryOldOnSale(long commdityId,int channelId);
	
	/**
	 * 添加上架信息
	 * @return
	 */
	public Boolean addOldCommdityOnSale(HtlOnSale onSale);
	     
	/**
	 * 添加下架信息
	 * @return
	 */
	public Boolean addOldCommdityOffSale(HtlOnSale onSale);

}
