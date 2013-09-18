package com.mangocity.hotel.sendmessage.dao.impl;

import java.util.List;

import com.mangocity.hotel.sendmessage.dao.PromotionTicketHotelDao;
import com.mangocity.hotel.sendmessage.model.PromotionTicketHotel;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class PromotionTicketHotelDaoImpl extends GenericDAOHibernateImpl implements PromotionTicketHotelDao {

	public List<PromotionTicketHotel> queryPromotionTicketHotels() {
		StringBuilder sql=new StringBuilder();
		sql.append(" select th.* from t_htl_promotion_tickethotel th ");
		sql.append(" where th.is_attend = 1 ");
		return super.queryByNativeSQL(sql.toString(), null, PromotionTicketHotel.class);
	}

}
