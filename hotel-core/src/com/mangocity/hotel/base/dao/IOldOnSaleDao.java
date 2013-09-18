package com.mangocity.hotel.base.dao;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlOnSale;

public interface IOldOnSaleDao {

	/**
	 * 查询商品对应渠道上下架信息
	 * @return
	 */
	public List<HtlOnSale> queryOldOnSale(long commdityId,int channelId);
	
	/**
	 * 保存上下架信息
	 * @return
	 */
	public Boolean saveOldCommdityOnSale(HtlOnSale onSale);
}
