package com.mangocity.hotel.order.dao.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.mangocity.hotel.order.dao.HtlOrderStsLogDAO;
import com.mangocity.hotel.order.dao.OperOrderDerferTimeDAO;
import com.mangocity.hotel.order.persistence.DeferOrder;
import com.mangocity.hotel.order.persistence.DerferOrderParam;
import com.mangocity.hotel.order.persistence.HtlOrderStsLog;
import com.mangocity.util.dao.DAOHibernateImpl;

public class HtlOrderStsLogImpl extends DAOHibernateImpl implements HtlOrderStsLogDAO{
    
	
	
	
	public void insert(HtlOrderStsLog htlOrderStsLog) {
		
		getHibernateTemplate().save(htlOrderStsLog);
						
	}

	
	
	
}
