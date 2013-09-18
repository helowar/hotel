package com.mangocity.hotel.sendmessage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.sendmessage.dao.PromotionTicketHotelDao;
import com.mangocity.hotel.sendmessage.model.PromotionTicketHotel;
import com.mangocity.hotel.sendmessage.service.PromotionTicketHotelService;

public class PromotionTicketHotelServiceImpl implements PromotionTicketHotelService {

	private PromotionTicketHotelDao promotionTicketHotelDao;
	public List<PromotionTicketHotel> queryPromotionTicketHotels(){
		return promotionTicketHotelDao.queryPromotionTicketHotels();
	}
		
	public Map<String,PromotionTicketHotel> queryPromotionTicketHotelsToMap(){
		List<PromotionTicketHotel> ticketHotelList=queryPromotionTicketHotels();
		Map<String,PromotionTicketHotel> ticketHotelMap=new HashMap<String,PromotionTicketHotel>();
		if(ticketHotelList!=null){
			String key=null;
			for(PromotionTicketHotel hotel: ticketHotelList){
				key=hotel.getHotelId()+hotel.getTicketType().getTicketType()+"";
				ticketHotelMap.put(key, hotel);
			}
		}
		return ticketHotelMap;
	}

	public void setPromotionTicketHotelDao(PromotionTicketHotelDao promotionTicketHotelDao) {
		this.promotionTicketHotelDao = promotionTicketHotelDao;
	}

	
	
	
}
