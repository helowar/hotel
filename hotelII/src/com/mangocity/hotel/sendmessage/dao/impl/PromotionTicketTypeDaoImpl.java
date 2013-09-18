package com.mangocity.hotel.sendmessage.dao.impl;

import java.util.List;

import com.mangocity.hotel.sendmessage.dao.PromotionTicketTypeDao;
import com.mangocity.hotel.sendmessage.model.PromotionTicketType;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class PromotionTicketTypeDaoImpl extends GenericDAOHibernateImpl implements PromotionTicketTypeDao {

	public List<PromotionTicketType> queryPromotionTicketTypes() {
		StringBuilder sql=new StringBuilder();
		sql.append(" select htt.* from t_htl_promotion_tickettype htt where htt.is_attend=1");
		
		return super.queryByNativeSQL(sql.toString(), null, PromotionTicketType.class);
	}

	public List<PromotionTicketType> queryPromotionTicketTypes(PromotionTicketType promotionTicketType) {
		// TODO Auto-generated method stub
		return null;
	}

}
