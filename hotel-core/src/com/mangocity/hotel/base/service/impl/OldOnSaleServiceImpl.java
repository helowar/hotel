package com.mangocity.hotel.base.service.impl;

import java.util.List;

import com.mangocity.hotel.base.dao.IOldOnSaleDao;
import com.mangocity.hotel.base.persistence.HtlOnSale;
import com.mangocity.hotel.base.service.IOldOnSaleService;

public class OldOnSaleServiceImpl implements IOldOnSaleService{
	
	private IOldOnSaleDao oldOnSaleDao;

	/**
	 * 查询商品对应渠道上下架信息
	 * @return
	 */
	public List<HtlOnSale> queryOldOnSale(long commdityId,int channelId){
		return oldOnSaleDao.queryOldOnSale(commdityId, channelId);
	}
	
	/**
	 * 添加上架信息
	 * @return
	 */
	public Boolean addOldCommdityOnSale(HtlOnSale onSale){
		onSale.setSaleState(1);
		return oldOnSaleDao.saveOldCommdityOnSale(onSale);
	}
	     
	/**
	 * 添加下架信息
	 * @return
	 */
	public Boolean addOldCommdityOffSale(HtlOnSale onSale){
		onSale.setSaleState(0);
		return oldOnSaleDao.saveOldCommdityOnSale(onSale);
	}

	public void setOldOnSaleDao(IOldOnSaleDao oldOnSaleDao) {
		this.oldOnSaleDao = oldOnSaleDao;
	}
}
