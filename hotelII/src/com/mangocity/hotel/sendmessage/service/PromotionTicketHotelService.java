package com.mangocity.hotel.sendmessage.service;

import java.util.List;
import java.util.Map;


import com.mangocity.hotel.sendmessage.model.PromotionTicketHotel;

public interface PromotionTicketHotelService {
	public List<PromotionTicketHotel> queryPromotionTicketHotels();
	
	/**
	 * 
	 * @return map,key为hotelId+ticketType
	 */
	public Map<String,PromotionTicketHotel> queryPromotionTicketHotelsToMap();
}
