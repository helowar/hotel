package com.mangocity.hotel.order.service.impl;

import java.util.List;

import com.mangocity.hotel.order.dao.HurryOrderDAO;
import com.mangocity.hotel.order.dao.OperOrderDerferTimeDAO;
import com.mangocity.hotel.order.persistence.DeferOrder;
import com.mangocity.hotel.order.persistence.DerferOrderParam;
import com.mangocity.hotel.order.service.HurryOrderService;
import com.mangocity.hotel.order.service.OperOrderDerferTimeService;

public class HurryOrderServiceImpl implements HurryOrderService{
    
	private HurryOrderDAO hurryOrderDAO;

	public Integer modifyHurryOrderNum(Long orderId) {
		// TODO Auto-generated method stub
		return hurryOrderDAO.modifyHurryOrderNum(orderId);
	}
    
	
	
	
	
	public void cleanHurryTimes(Long orderId) {
		// TODO Auto-generated method stub
		hurryOrderDAO.cleanHurryTimes(orderId);
	}





	public Integer queryHurryOrderNum(Long orderId) {
		// TODO Auto-generated method stub
		return hurryOrderDAO.queryHurryOrderNum(orderId);
	}




	public HurryOrderDAO getHurryOrderDAO() {
		return hurryOrderDAO;
	}

	public void setHurryOrderDAO(HurryOrderDAO hurryOrderDAO) {
		this.hurryOrderDAO = hurryOrderDAO;
	}
	
	

}
