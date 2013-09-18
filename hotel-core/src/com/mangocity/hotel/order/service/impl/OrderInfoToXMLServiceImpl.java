package com.mangocity.hotel.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.order.dao.IOrderDao;
import com.mangocity.hotel.order.service.OrderInfoToXMLService;
import com.mangocity.hotel.util.RowToXML;


public class OrderInfoToXMLServiceImpl implements OrderInfoToXMLService{
	

	private IOrderDao orderDao ;
	
	public String findOrderByMemberCd(String memberCd, String hotelId) {
		// TODO Auto-generated method stub
		
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		result += "<result>";
		
		List list = orderDao.findOrderByMemberCd(memberCd,hotelId);
		if(list != null && list.size() > 0) {
			for(Object obj : list) {
				Map orderInfo = (Map) obj;
				result += RowToXML.createXMLFromMap(orderInfo);
			}
		}
		result += "</result>";
		
		return result;
	}

	public String findOrderInfoByAuditedDate(String auditedDate) {
		// TODO Auto-generated method stub
		
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		result += "<result>";
		
		List list = orderDao.findOrderInfoByAuditedDate(auditedDate);
		if(list != null && list.size() > 0) {
			for(Object obj : list) {
				result += "<orderInfo>";
				Map orderInfo = (Map) obj;
				result += RowToXML.createXMLFromMap(orderInfo);
				result += "</orderInfo>";
			}
		}
		
		result += "</result>";
		return result;
	}

	public IOrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	}

}
