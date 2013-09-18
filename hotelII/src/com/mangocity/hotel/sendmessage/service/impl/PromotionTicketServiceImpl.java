package com.mangocity.hotel.sendmessage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.sendmessage.dao.PromotionTicketDao;
import com.mangocity.hotel.sendmessage.model.PromotionTicket;
import com.mangocity.hotel.sendmessage.service.PromotionTicketService;

public class PromotionTicketServiceImpl implements PromotionTicketService {

	private PromotionTicketDao promotionTicketDao;
	public PromotionTicket queryPromotionTicketByCode(String code) {
		
		return null;
	}

	public PromotionTicket queryPromotionTicketById(Long ticketId) {
		
		return null;
	}

	public List<PromotionTicket> queryPromotionTickets() {
		
		return promotionTicketDao.queryPromotionTickets();
	}

	public void updateBatch(List<PromotionTicket> ticketList) {
		
		
	}

	public void setPromotionTicketDao(PromotionTicketDao promotionTicketDao) {
		this.promotionTicketDao = promotionTicketDao;
	}

	
}
