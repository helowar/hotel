package com.mangocity.hotel.sendmessage.dao.impl;

import java.util.List;

import com.mangocity.hotel.sendmessage.dao.PromotionTicketDao;
import com.mangocity.hotel.sendmessage.model.PromotionTicket;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class PromotionTicketDaoImpl extends GenericDAOHibernateImpl implements PromotionTicketDao {

	public PromotionTicket queryPromotionTicketByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	public PromotionTicket queryPromotionTicketById(Long ticketId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<PromotionTicket> queryPromotionTickets() {
		String sql="select tpt.* from t_htl_promotion_ticket tpt where tpt.is_used=0";
		return super.queryByNativeSQL(sql, null, PromotionTicket.class);
	}

	public void updateBatch(List<PromotionTicket> ticketList) {
		
		super.saveOrUpdateAll(ticketList);
	}

}
