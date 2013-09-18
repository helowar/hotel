package com.mangocity.hotel.search.dao.impl;

import com.mangocity.hotel.search.dao.HtlQueryspeedLogDao;
import com.mangocity.hotel.search.log.HtlQueryspeedLog;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class HtlQueryspeedLogDaoImpl extends GenericDAOHibernateImpl implements HtlQueryspeedLogDao{
	
	public void save(HtlQueryspeedLog htlQueryspeedLog){
		super.save(htlQueryspeedLog);
	}

}
