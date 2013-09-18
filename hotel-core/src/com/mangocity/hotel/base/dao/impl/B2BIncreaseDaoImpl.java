package com.mangocity.hotel.base.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mangocity.hagtb2b.persistence.HtlB2bIncrease;
import com.mangocity.hotel.base.dao.B2BIncreaseDao;
import com.mangocity.hotel.base.persistence.B2BIncrease;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class B2BIncreaseDaoImpl extends GenericDAOHibernateImpl implements
		B2BIncreaseDao {
	
	private static final Log log = LogFactory.getLog(B2BIncreaseDaoImpl.class);

	public List<B2BIncrease> query(long id) {
		String hql = " from B2BIncrease b2b where b2b.hotelId = ? ";
		return super.query(hql, new Object[]{Long.valueOf(id)}, 0, 0, false);
	}

	public void batchInsert(List<B2BIncrease> increases) {
		super.saveOrUpdateAll(increases);
	}

	public void update(long hotelId) {
		List<B2BIncrease> b2BIncreases = query(hotelId); //先根据酒店ID查询出加幅记录
		for(B2BIncrease b2BIncrease:b2BIncreases){
			if(b2BIncrease.getFlag() == 1){
				b2BIncrease.setFlag(0); //将加幅记录设置为0
				super.update(b2BIncrease);
				break; //理论上只有一条记录是有效的 故找出该条记录设置为0后就可以跳出循环
			}
		}
	}
	
	public List<B2BIncrease> queryEffectiveOrNotB2BIncrease(long hotelId){
		String hql = " from B2BIncrease b2b where b2b.hotelId = ? and b2b.flag = 1";
		return super.query(hql, new Object[]{Long.valueOf(hotelId)}, 0, 0, false);
	}
}
