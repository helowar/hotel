package com.mangocity.hotel.orderrecord.service;

import com.mangocity.hotel.orderrecord.model.OrderRecord;

public interface OrderRecordService {

	public Long getActionId ()throws Exception;
	public void addOrderRecord(OrderRecord orderRecord)throws Exception;
	public Long queryActionIdByOrderCd(String orderCd)throws Exception;
	public void updateOrderRecord(OrderRecord orderRecord)throws Exception;
	
}
