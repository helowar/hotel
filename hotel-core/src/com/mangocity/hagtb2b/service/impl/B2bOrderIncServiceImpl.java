package com.mangocity.hagtb2b.service.impl;

import com.mangocity.hagtb2b.dao.IB2bOrderIncDao;
import com.mangocity.hagtb2b.persistence.HtlB2bOrderIncrease;
import com.mangocity.hagtb2b.service.B2bOrderIncService;

public class B2bOrderIncServiceImpl implements B2bOrderIncService {
	
	
	 private IB2bOrderIncDao b2bOrderIncDao;

	public void saveOrUpdate(HtlB2bOrderIncrease htlB2bOrderIncrease){
		 b2bOrderIncDao.saveOrUpdate(htlB2bOrderIncrease);
	}

	public void setB2bOrderIncDao(IB2bOrderIncDao b2bOrderIncDao) {
		this.b2bOrderIncDao = b2bOrderIncDao;
	}

	public boolean judgeIsIncreaseOrder(String orderCD) {
		HtlB2bOrderIncrease htlB2bOrderIncrease = b2bOrderIncDao.queryByOrderCD(orderCD);
		boolean flag = false ;
		if(htlB2bOrderIncrease !=null) flag = true;
		return flag;
	}
}
