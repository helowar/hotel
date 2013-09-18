package com.mangocity.hotel.sendmessage.service.impl;

import java.util.List;

import com.mangocity.hotel.sendmessage.dao.QueryTargetOrderDao;
import com.mangocity.hotel.sendmessage.model.HotelOrder;
import com.mangocity.hotel.sendmessage.service.TargetOrderService;

public class HotelOrderServiceImpl implements TargetOrderService {

	private QueryTargetOrderDao hotelOrderDao;
	
	
	@SuppressWarnings("unchecked")
	public List<HotelOrder> queryTargetOrder() throws Exception {
		
		return hotelOrderDao.<HotelOrder>queryTargetOrder();
	}

	public void setHotelOrderDao(QueryTargetOrderDao hotelOrderDao) {
		this.hotelOrderDao = hotelOrderDao;
	}
	

	

}
