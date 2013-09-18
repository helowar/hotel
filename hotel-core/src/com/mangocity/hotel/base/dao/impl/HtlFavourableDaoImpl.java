package com.mangocity.hotel.base.dao.impl;

import java.util.List;

import com.mangocity.hotel.base.dao.HtlFavourableDao;
import com.mangocity.hotel.base.persistence.HtlFavourable;
import com.mangocity.hotel.order.constant.PromoteType;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class HtlFavourableDaoImpl extends GenericDAOHibernateImpl implements HtlFavourableDao {

	/**
	 * 根据城市和活动类型查询
	 */
	public List<HtlFavourable> queryHtlFavourableByType(String cityCode,int promoteType){
		String hqlstr = "from HtlFavourable f where " +
				"f.cityCode = ? " +
		        "and f.flag=1 ";
		if(promoteType==PromoteType.Five){
			hqlstr += " and f.favA=1";
		}else if(promoteType==PromoteType.Seven){
			hqlstr += " and f.favB=1";
		}else if(promoteType==PromoteType.Zone){
			hqlstr += " and f.favC=1";
		}else{
			return null;
		}
		return super.query(hqlstr, new Object[]{cityCode} , 0, 0, false);
	}
	
	/**
	 * 根据酒店Id查询
	 */
	public List<HtlFavourable> queryHtlFavourableByHotelId(String hotelIdList){
		String hqlstr = "from HtlFavourable f where " +
				"f.hotelId in (" + hotelIdList + ") " +
				"and f.flag=1 ";
		return super.query(hqlstr, null , 0, 0, false);
	}
}
