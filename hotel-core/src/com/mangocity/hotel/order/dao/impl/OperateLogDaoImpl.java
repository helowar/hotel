package com.mangocity.hotel.order.dao.impl;

import java.util.List;

import com.mangocity.hotel.order.dao.IOperateLogDao;
import com.mangocity.hotel.order.persistence.HtlCheckReservateLog;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class OperateLogDaoImpl extends GenericDAOHibernateImpl implements IOperateLogDao{
	
	public void saveOrUpdateAll(List<HtlCheckReservateLog> checkLogList){
		super.saveOrUpdateAll(checkLogList);
	}
	
	public void saveOrUpdate(HtlCheckReservateLog checkLog){
		super.saveOrUpdate(checkLog);
	}
}
