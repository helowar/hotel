package com.mangocity.hotel.order.dao;

import java.util.List;

import com.mangocity.hotel.order.persistence.HtlCheckReservateLog;

public interface IOperateLogDao {
	
	public void saveOrUpdateAll(List<HtlCheckReservateLog> checkLogList);
	
	public void saveOrUpdate(HtlCheckReservateLog checkLog);
}
