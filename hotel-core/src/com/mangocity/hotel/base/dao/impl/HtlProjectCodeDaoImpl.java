package com.mangocity.hotel.base.dao.impl;

import java.util.List;

import com.mangocity.hotel.base.dao.HtlProjectCodeDao;
import com.mangocity.hotel.base.persistence.HtlProjectCode;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class HtlProjectCodeDaoImpl extends GenericDAOHibernateImpl implements HtlProjectCodeDao {

	public void saveHtlProjectCode(HtlProjectCode htlProjectCode) {
		super.save(htlProjectCode);
	}
	
	public boolean haveProjectCode(String orderCD){
		String sql = "select count(*) from htl_projectcode h where h.ordercd='"+ orderCD +"'";
		List result = this.queryByNativeSQL(sql, null);
		if(null != result && result.size()>0){
			int count = Integer.parseInt(result.get(0).toString());
			if(count==0){
				return false;
			}
		}
		return true;
	}
}
