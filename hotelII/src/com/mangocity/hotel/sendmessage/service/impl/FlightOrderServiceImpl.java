package com.mangocity.hotel.sendmessage.service.impl;

import java.util.List;

import com.mangocity.hotel.sendmessage.dao.QueryTargetOrderDao;
import com.mangocity.hotel.sendmessage.model.FlightOrder;
import com.mangocity.hotel.sendmessage.service.TargetOrderService;

public class FlightOrderServiceImpl implements TargetOrderService {

	private QueryTargetOrderDao flightOrderDao;
	
	@SuppressWarnings("unchecked")
	public  List<FlightOrder> queryTargetOrder() throws Exception {		
		return flightOrderDao.<FlightOrder>queryTargetOrder();
	}
	
	public void setFlightOrderDao(QueryTargetOrderDao flightOrderDao) {
		this.flightOrderDao = flightOrderDao;
	}


}
