package com.mangocity.hotel.base.dao.impl;

import java.util.List;

import com.mangocity.hotel.base.dao.IOldOnSaleDao;
import com.mangocity.hotel.base.persistence.HtlOnSale;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class OldOnSaleDaoImpl extends GenericDAOHibernateImpl implements IOldOnSaleDao{

	/**
	 * 查询商品对应渠道上下架信息
	 * @return
	 */
	public List<HtlOnSale> queryOldOnSale(long commdityId,int channelId){
		String sql="from htl_onsale o where o.commodityId=? and o.saleChannelId=?";
		List<HtlOnSale> onsales=this.query(sql, new Object[]{commdityId,channelId});
		return onsales;
	}
	
	/**
	 * 保存上下架信息
	 * @return
	 */
	public Boolean saveOldCommdityOnSale(HtlOnSale onSale){
		this.save(onSale);
		return true;
	}
}
