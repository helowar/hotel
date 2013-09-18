package com.mangocity.hotel.base.dao.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.HotelQuotaNewDao;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class HotelQuotaNewDaoImpl extends GenericDAOHibernateImpl implements HotelQuotaNewDao {

	public List<HtlQuotaNew> queryQuotaByRoomTypeId(Long hotelId, Long roomTypeId, Date checkinDate, Date checkoutDate) {
		String sql="select quotanew.* from hwtemp_htl_quota_new quotanew where quotanew.hotel_id = ? and  quotanew.roomtype_id=?"
			 +" and quotanew.able_sale_date>=? and quotanew.able_sale_date<? order by quotanew.able_sale_date";
		Object[] params=new Object[]{hotelId,roomTypeId,checkinDate,checkoutDate};

		return super.queryByNativeSQL(sql, params, HtlQuotaNew.class);
	}
}
