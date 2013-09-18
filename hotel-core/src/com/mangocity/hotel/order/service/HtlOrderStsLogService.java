package com.mangocity.hotel.order.service;

import java.util.List;

import com.mangocity.hotel.order.persistence.DeferOrder;
import com.mangocity.hotel.order.persistence.DerferOrderParam;
import com.mangocity.hotel.order.persistence.HtlOrderStsLog;

public interface HtlOrderStsLogService {
   
	
	
	/**
	 * 保存数据
	 * @param htlOrderStsLog
	 */
	public  void insert(HtlOrderStsLog htlOrderStsLog);

}
	

