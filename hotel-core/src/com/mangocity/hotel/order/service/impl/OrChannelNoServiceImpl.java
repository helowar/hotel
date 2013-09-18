package com.mangocity.hotel.order.service.impl;

import com.mangocity.hotel.order.dao.OrChannelNoDAO;
import com.mangocity.hotel.order.persistence.OrChannelNo;
import com.mangocity.hotel.order.service.OrChannelNoService;



public class OrChannelNoServiceImpl implements OrChannelNoService{
    
	private OrChannelNoDAO orChannelNoDAO;
	
	

	

	public void updateOrChannelNo(OrChannelNo orChannelNo) {
		// TODO Auto-generated method stub
		orChannelNoDAO.updateOrChannelNo(orChannelNo);
	}





	public OrChannelNoDAO getOrChannelNoDAO() {
		return orChannelNoDAO;
	}





	public void setOrChannelNoDAO(OrChannelNoDAO orChannelNoDAO) {
		this.orChannelNoDAO = orChannelNoDAO;
	}




	
	
	
	

}
