package com.mangocity.hotel.orderrecord.service.impl;

import com.mangocity.hotel.orderrecord.dao.OrderRecordDao;
import com.mangocity.hotel.orderrecord.model.OrderRecord;
import com.mangocity.hotel.orderrecord.service.OrderRecordService;

public class OrderRecordServiceImpl implements OrderRecordService {

	private OrderRecordDao orderRecordDao;
	public void addOrderRecord(OrderRecord orderRecord) throws Exception{
		orderRecordDao.addOrderRecord(orderRecord);
	}

	public Long getActionId() throws Exception{
		return orderRecordDao.getActionIdSeq();
	}

	public Long queryActionIdByOrderCd(String orderCd) throws Exception {
		// TODO Auto-generated method stub
		return orderRecordDao.queryActionIdByOrderCd(orderCd);
	}
	public void setOrderRecordDao(OrderRecordDao orderRecordDao) {
		this.orderRecordDao = orderRecordDao;
	}

	public void updateOrderRecord(OrderRecord orderRecord) throws Exception {
		orderRecordDao.updateOrderRecord(orderRecord);
		
	}

	
	
	

}
