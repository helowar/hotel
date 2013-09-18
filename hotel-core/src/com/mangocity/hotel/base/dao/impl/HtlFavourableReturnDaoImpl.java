package com.mangocity.hotel.base.dao.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.HtlFavourableReturnDao;
import com.mangocity.hotel.base.persistence.HtlFavourableReturn;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class HtlFavourableReturnDaoImpl extends GenericDAOHibernateImpl implements HtlFavourableReturnDao {

	
	/**
	 * 根据ID查询对应的现金返还规则 add by xiaowei.wang 2010-11-15
	 * @param id
	 * @return
	 */
	public HtlFavourableReturn getHtlFavourableReturnById(long id) {
		return super.get(HtlFavourableReturn.class, id); 
	}
	
	/**
	 * 取出对应酒店所有的现金返还条款 add by xiaowei.wang 2010-11-15
	 * @param hotelId
	 * @return
	 */
	public List<HtlFavourableReturn> getHtlFavourableReturnListByHotelId(
			long hotelId) {
		String sql = " select  a  from HtlFavourableReturn a   where " +
		 " a.hotelId=? and a.endDate >= trunc(sysdate) order by a.modifyTime desc";
		return super.query(sql,new Object[] {hotelId});
	}
	
	/**
	 * 取出对应酒店价格类型现金返还条款 add by xiaowei.wang 2010-11-15
	 * @param hotelId
	 * @param priceTypeId
	 * @return
	 */
	public List<HtlFavourableReturn> getHtlFavourableReturnListByPriceTypeId(
			long hotelId, long priceTypeId) {
		String sql = " select  a  from HtlFavourableReturn a   where " +
		" a.hotelId=?  and a. priceTypeId=? and a.endDate >= trunc(sysdate) order by a.modifyTime desc";
		return super.query(sql, new Object[] {hotelId,priceTypeId});
	}



	public List<HtlFavourableReturn> getFavourableReturnOrderListByPayMethod(
			long hotelId, long priceTypeId, Integer payMethod) {
		return super.queryByNamedQuery("queryFavourableReturnOrder", new Object[]{hotelId,priceTypeId,payMethod});
	}

	public List<HtlFavourableReturn> getSubFavReturnListByModifyTime(long hotelId,
			long priceTypeId, Date modifyTime) {
		return super.queryByNamedQuery("querySubFavReturn", new Object[]{hotelId,priceTypeId,modifyTime});
	}

	public void removeHtlFavourableReturnById(long id) {
		super.remove(HtlFavourableReturn.class, id);
	}

	public void saveHtlFavourableReturn(HtlFavourableReturn htlFavourableReturn) {
		super.save(htlFavourableReturn);
	}

	public void saveHtlFavourableReturnList(List<HtlFavourableReturn> list) {
		super.saveOrUpdateAll(list);
	}
}