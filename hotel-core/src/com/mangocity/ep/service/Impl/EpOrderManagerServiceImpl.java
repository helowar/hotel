package com.mangocity.ep.service.Impl;

import java.util.List;

import com.mangocity.ep.dao.EpOrderManagerDAO;
import com.mangocity.ep.entity.EpOrder;
import com.mangocity.ep.entity.RequestParam;
import com.mangocity.ep.service.EpOrderManagerService;
import com.mangocity.hotel.user.UserWrapper;

public class EpOrderManagerServiceImpl implements EpOrderManagerService{
    
	private EpOrderManagerDAO epOrderManagerDAO;
	
	public List<EpOrder> queryEpOrder(RequestParam requestParam) {

		return epOrderManagerDAO.queryEpOrder(requestParam);
	}
    
	public void updateConfrimStatus(String orderCd,UserWrapper roleUser) {
		
		epOrderManagerDAO.updateConfrimStatus(orderCd,roleUser);
	}
	
	
	public String validateEpOrderByHotelId(String hotelId) {
		
		return epOrderManagerDAO.validateEpOrderByHotelId(hotelId);
	}
	
	public Integer queryOrderSum(RequestParam requestParam) {
		
		return epOrderManagerDAO.queryOrderSum(requestParam);
	}
	
	public EpOrderManagerDAO getEpOrderManagerDAO() {
		return epOrderManagerDAO;
	}

	public void setEpOrderManagerDAO(EpOrderManagerDAO epOrderManagerDAO) {
		this.epOrderManagerDAO = epOrderManagerDAO;
	}
	
	
}
