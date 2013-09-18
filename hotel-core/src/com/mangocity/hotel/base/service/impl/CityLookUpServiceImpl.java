package com.mangocity.hotel.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.service.ICityLookUpService;
import com.mangocity.util.dao.DAOHibernateImpl;

public class CityLookUpServiceImpl implements ICityLookUpService {

	private DAOHibernateImpl dAOHibernateImpl;
	
	 public DAOHibernateImpl getDAOHibernateImpl() {
		return dAOHibernateImpl;
	}
	public void setDAOHibernateImpl(DAOHibernateImpl hibernateImpl) {
		dAOHibernateImpl = hibernateImpl;
	}
	/**
     * 网站查询全部城市 add by zhineng.zhuang 2008-9-24
     */
    public List<HtlArea> queryAllCity() {
        String hsql = "from HtlArea";
        List<HtlArea> qPopList = new ArrayList<HtlArea>();
        qPopList = dAOHibernateImpl.doquery(hsql, false);
        return qPopList;
    }
}
