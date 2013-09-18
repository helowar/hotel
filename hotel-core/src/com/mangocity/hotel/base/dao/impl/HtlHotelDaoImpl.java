package com.mangocity.hotel.base.dao.impl;

import java.util.List;

import com.mangocity.hotel.base.dao.HtHotelDao;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
*@author chenhuizhong e-mail:huizong.chen@mangocity.com
*@version 1.0
*@since 1.0
*/
public class HtlHotelDaoImpl extends GenericDAOHibernateImpl implements HtHotelDao
{

    public List<HtlHotel> lstAllHotels(String status) {
        
        return this.queryByNamedQuery("lstAllHotels", new Object[] { status });
    }

	public Object[] getHotelNamesByIds(String hotelIds) {
		String hql = "select chnName from HtlHotel where ID in ("+hotelIds+")";
		List<Object> names = super.query(hql,null);
		Object[] objs = new Object[names.size()];
		int index = 0;
		for (Object obj : names) {
			objs[index++] = obj;
		}
		return objs;
	}
}
