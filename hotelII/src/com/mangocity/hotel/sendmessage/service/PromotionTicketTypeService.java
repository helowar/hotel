package com.mangocity.hotel.sendmessage.service;

import java.util.List;

import com.mangocity.hotel.sendmessage.model.PromotionTicketType;

public interface PromotionTicketTypeService {
	public List<PromotionTicketType> queryPromotionTicketTypes();
	public List<PromotionTicketType> queryPromotionTicketTypes(PromotionTicketType promotionTicketType); 
}
