package com.mangocity.hotel.sendmessage.dao;

import java.util.List;

import com.mangocity.hotel.sendmessage.model.PromotionTicket;

public interface PromotionTicketDao {
	public PromotionTicket queryPromotionTicketById(Long ticketId);
	public PromotionTicket queryPromotionTicketByCode(String code);
	public List<PromotionTicket> queryPromotionTickets();
	public void updateBatch(List<PromotionTicket> ticketList);
	

}
