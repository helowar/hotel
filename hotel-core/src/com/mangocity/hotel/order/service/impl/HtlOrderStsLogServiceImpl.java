package com.mangocity.hotel.order.service.impl;

import java.util.List;

import com.mangocity.hotel.order.dao.HtlOrderStsLogDAO;
import com.mangocity.hotel.order.dao.HurryOrderDAO;
import com.mangocity.hotel.order.dao.OperOrderDerferTimeDAO;
import com.mangocity.hotel.order.persistence.DeferOrder;
import com.mangocity.hotel.order.persistence.DerferOrderParam;
import com.mangocity.hotel.order.persistence.HtlOrderStsLog;
import com.mangocity.hotel.order.service.HtlOrderStsLogService;
import com.mangocity.hotel.order.service.HurryOrderService;
import com.mangocity.hotel.order.service.OperOrderDerferTimeService;

public class HtlOrderStsLogServiceImpl implements HtlOrderStsLogService{
    
	private HtlOrderStsLogDAO htlOrderStsLogDAO;

	public void insert(HtlOrderStsLog htlOrderStsLog) {
		htlOrderStsLogDAO.insert(htlOrderStsLog);
		
	}

	public HtlOrderStsLogDAO getHtlOrderStsLogDAO() {
		return htlOrderStsLogDAO;
	}

	public void setHtlOrderStsLogDAO(HtlOrderStsLogDAO htlOrderStsLogDAO) {
		this.htlOrderStsLogDAO = htlOrderStsLogDAO;
	}

	
	
   
}
