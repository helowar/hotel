package com.mangocity.hotel.order.dao;

import java.util.List;

import com.mangocity.hotel.order.service.assistant.EverydayParams;

public interface INewOrderParamDao {

	public List<Object[]> searchNewOrderParams(String priceTypeID,String checkinDate,String checkoutDate);
	
	public List<Object[]> searchEverdayParams(String priceTypeID,String checkinDate,String checkoutDate ,String payMethod);
	
	public List<Object[]> searchQuotaParams(String priceTypeID,String checkinDate,String checkoutDate,String payMethod);
	
	public  Boolean queryOrgByAgentCode(String agentCode);
}
