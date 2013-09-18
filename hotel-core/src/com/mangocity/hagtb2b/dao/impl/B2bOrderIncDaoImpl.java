package com.mangocity.hagtb2b.dao.impl;

import java.util.List;

import com.mangocity.hagtb2b.dao.IB2bOrderIncDao;
import com.mangocity.hagtb2b.persistence.HtlB2bOrderIncrease;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class B2bOrderIncDaoImpl extends GenericDAOHibernateImpl implements IB2bOrderIncDao {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void saveOrUpdate(HtlB2bOrderIncrease htlB2bOrderIncrease) {
		super.saveOrUpdate(htlB2bOrderIncrease);
	}
	
	public void updateHtlB2bOrderIncrease(HtlB2bOrderIncrease htlB2bOrderIncrease) {
		super.update(htlB2bOrderIncrease);
	}
	
	public HtlB2bOrderIncrease getHtlB2bOrderIncrease(long  id){
		return super.get(HtlB2bOrderIncrease.class, id);
	}

	public HtlB2bOrderIncrease queryByOrderCD(String orderCD) {
		String hql = "from HtlB2bOrderIncrease h where h.orderCD = ?";
		List<HtlB2bOrderIncrease> htlB2bOrderIncreases = super.query(hql, new Object[]{orderCD}, 0, 0, false);
		if(htlB2bOrderIncreases.size() == 0) return null;
		return htlB2bOrderIncreases.get(0);
	}
}
