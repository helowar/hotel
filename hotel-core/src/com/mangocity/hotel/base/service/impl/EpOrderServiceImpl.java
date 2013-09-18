package com.mangocity.hotel.base.service.impl;

import java.util.List;

import com.mangocity.hotel.base.dao.EpOrderDAO;
import com.mangocity.hotel.base.service.EpOrderService;

public class EpOrderServiceImpl implements EpOrderService{
  
    
	private EpOrderDAO epOrderDAO;
	
	/**
	 * @ 查找Ep 酒店
	 * @return List<String>
	 * 
	 */
	public List<String> queryEpHotelId() {
		// TODO Auto-generated method stub
		return epOrderDAO.queryEpHotelId();
	}
    
	/**
	 * @ 保存Ep 酒店订单信息
	 * @param List<hotelId>
	 */
	
	public void saveEpOrderData(List<String> hotelList) {
		epOrderDAO.saveEpOrderData(hotelList);

	}

	public EpOrderDAO getEpOrderDAO() {
		return epOrderDAO;
	}

	public void setEpOrderDAO(EpOrderDAO epOrderDAO) {
		this.epOrderDAO = epOrderDAO;
	}
	
	
}
