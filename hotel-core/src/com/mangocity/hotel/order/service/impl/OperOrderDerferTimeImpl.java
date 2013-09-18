package com.mangocity.hotel.order.service.impl;

import java.util.List;

import com.mangocity.hotel.order.dao.OperOrderDerferTimeDAO;
import com.mangocity.hotel.order.persistence.DeferOrder;
import com.mangocity.hotel.order.persistence.DerferOrderParam;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.service.OperOrderDerferTimeService;

public class OperOrderDerferTimeImpl implements OperOrderDerferTimeService{
    
	private OperOrderDerferTimeDAO orderDerferTimeDAO;
	
	public void modifyOrderDerferTime(Integer deferTime, Long orderId) {
		orderDerferTimeDAO.modifyOrderDerferTime(deferTime, orderId);
		
	}

	
	public List<DeferOrder> queryDerferOrderData(DerferOrderParam param) {
		// TODO Auto-generated method stub
		return orderDerferTimeDAO.queryDerferOrderData(param);
	}
	
	
	public Integer validateOrderSts(Long orderId) {
		// TODO Auto-generated method stub
		return orderDerferTimeDAO.validateOrderSts(orderId);
	}
   
	
	public void modifyDerferOrder(Long orderId) {
		// TODO Auto-generated method stub
		orderDerferTimeDAO.modifyDerferOrder(orderId);
	}

    
	public Long querySumRow(DerferOrderParam param) {
		// TODO Auto-generated method stub
		return orderDerferTimeDAO.querySumRow(param);
	}
    
	

	public void saveDerferTimeLog(OrHandleLog handleLog) {
		// TODO Auto-generated method stub
		orderDerferTimeDAO.saveDerferTimeLog(handleLog);
	}


	public OperOrderDerferTimeDAO getOrderDerferTimeDAO() {
		return orderDerferTimeDAO;
	}

	public void setOrderDerferTimeDAO(OperOrderDerferTimeDAO orderDerferTimeDAO) {
		this.orderDerferTimeDAO = orderDerferTimeDAO;
	}



	


  
	
}
