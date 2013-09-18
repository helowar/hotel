package com.mangocity.hotel.orderrecord.dao;

import com.mangocity.hotel.orderrecord.model.OrderRecord;

public interface OrderRecordDao {
	public void addOrderRecord(OrderRecord orderRecord);
	public Long getActionIdSeq();
	public Long queryActionIdByOrderCd(String orderCd);
	public void updateOrderRecord(OrderRecord orderRecord);
}
