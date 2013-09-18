package com.mangocity.hotel.sendmessage.dao;

import java.util.List;

import com.mangocity.hotel.sendmessage.model.PromotionTicketType;

public interface PromotionTicketTypeDao {

	public List<PromotionTicketType> queryPromotionTicketTypes();
	public List<PromotionTicketType> queryPromotionTicketTypes(PromotionTicketType promotionTicketType);
	
}
