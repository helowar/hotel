package com.mangocity.hotel.base.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.FavourableDecreaseDAO;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class FavourableDecreaseDAOImpl extends GenericDAOHibernateImpl implements FavourableDecreaseDAO {

	public List<HtlFavourableDecrease> qryBenefitByPriceTypeAndDate(String priceTypeId, Date checkInDate, 
			Date checkOutDate) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from HtlFavourableDecrease where priceTypeId = ? ");
		hql.append(" and endDate >= ? and beginDate < ? ");
		hql.append(" and fun_date_week_judge(?, ?, week) > 0 ");

		return super.query(hql.toString(), new Object[]{Long.valueOf(priceTypeId), checkInDate, checkOutDate, 
			checkInDate, checkOutDate});
	}
	
	@SuppressWarnings("unchecked")
	public List<HtlFavourableDecrease> qryBenefitByMultiPriceTypeAndDate(String[] priceTypeIds, Date checkInDate, 
			Date checkOutDate) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from HtlFavourableDecrease where priceTypeId in ( ");
		
		List priceTypeIdList = new ArrayList(priceTypeIds.length + 4);
		for (String priceTypeId : priceTypeIds) {
			hql.append("?, ");
			priceTypeIdList.add(Long.valueOf(priceTypeId));
		}		
		hql.setCharAt(hql.length() - 2, ')');
		
		hql.append(" and endDate >= ? and beginDate < ? ");
		hql.append(" and fun_date_week_judge(?, ?, week) > 0 ");
		priceTypeIdList.add(checkInDate);
		priceTypeIdList.add(checkOutDate);
		priceTypeIdList.add(checkInDate);
		priceTypeIdList.add(checkOutDate);
		
		return super.query(hql.toString(), priceTypeIdList.toArray());
	}

}
