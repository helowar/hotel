package com.mangocity.hotel.sendmessage.service.impl;

import java.util.List;

import com.mangocity.hotel.sendmessage.dao.PromotionTicketTypeDao;
import com.mangocity.hotel.sendmessage.model.PromotionTicketType;
import com.mangocity.hotel.sendmessage.service.PromotionTicketTypeService;

public class PromotionTicketTypeServiceImpl implements PromotionTicketTypeService {

	private PromotionTicketTypeDao promotionTicketTypeDao;
	public List<PromotionTicketType> queryPromotionTicketTypes() {
		
		return promotionTicketTypeDao.queryPromotionTicketTypes();
	}

	public List<PromotionTicketType> queryPromotionTicketTypes(PromotionTicketType promotionTicketType) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPromotionTicketTypeDao(PromotionTicketTypeDao promotionTicketTypeDao) {
		this.promotionTicketTypeDao = promotionTicketTypeDao;
	}

	

	
}
