package com.mangocity.hotel.base.dao.impl;

import java.util.List;

import com.mangocity.hotel.base.dao.HtlFavourableHotelDao;
import com.mangocity.hotel.base.persistence.HtlFavourableHotel;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class HtlFavourableHotelDaoImpl extends GenericDAOHibernateImpl implements HtlFavourableHotelDao {

	public void saveOrUpdateAll(List<HtlFavourableHotel> htlFavourableHotels) {
		super.saveOrUpdateAll(htlFavourableHotels);
	}

	public void delete(Long htlID, Long parID) {
		String hql = "delete from HtlFavourableHotel hfh where hfh.hotelId = ? and hfh.favId = ?";
		Object[] paramValues = new Object[]{htlID,parID};
		super.updateByQL(hql, paramValues);
	}

	public void batchDelete(List<Long> hotelIdList, Long parID) {
		StringBuilder sb = new StringBuilder("delete from HtlFavourableHotel hfh where hfh.hotelId in(");
		for(Long id:hotelIdList){
			sb.append(id).append(",");
		}
		sb.setLength(sb.length()-1);
		sb.append(")");
		String hql = sb.toString()+" and hfh.favId=? ";
		Object[] paramValues = new Object[]{parID};
		super.updateByQL(hql, paramValues);
	}
}
