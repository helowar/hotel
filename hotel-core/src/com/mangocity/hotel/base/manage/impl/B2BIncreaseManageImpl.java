package com.mangocity.hotel.base.manage.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mangocity.hotel.base.dao.B2BIncreaseDao;
import com.mangocity.hotel.base.manage.B2BIncreaseManage;
import com.mangocity.hotel.base.persistence.B2BIncrease;

public class B2BIncreaseManageImpl implements B2BIncreaseManage {
	
	private B2BIncreaseDao b2BIncreaseDao; 
	
	
	public B2BIncreaseDao getB2BIncreaseDao() {
		return b2BIncreaseDao;
	}
	public void setB2BIncreaseDao(B2BIncreaseDao increaseDao) {
		b2BIncreaseDao = increaseDao;
	}
	public void saveB2BIncrease(List<Long> hotelIds, double increaseRate,
			String createName) {
		List<B2BIncrease> b2BIncreases = new ArrayList<B2BIncrease>(hotelIds.size());
		
		for(Long hotelId : hotelIds){
			//step 1 修改B2B_INCREASE表中的记录 将之前的状态改为0失效
			b2BIncreaseDao.update(hotelId);
			//step 2 封装B2BIncrease对象
			B2BIncrease b2BIncrease =new B2BIncrease();
			b2BIncrease.setHotelId(hotelId);
			b2BIncrease.setCreateName(createName);
			b2BIncrease.setCreateTime(new Date());
			b2BIncrease.setIncreaseRate(increaseRate);
			b2BIncrease.setFlag(1);
			b2BIncreases.add(b2BIncrease);
		}
		
		//step 3 批量保存
		b2BIncreaseDao.batchInsert(b2BIncreases);
	}
	
	
//	public B2BIncrease queryEffectiveB2BIncrease(long hotelId,String flag){
//		List<B2BIncrease> incList = b2BIncreaseDao.queryEffectiveOrNotB2BIncrease(hotelId, flag);
//		return incList.size() != 0?incList.get(0):null;
//	}
	
	public B2BIncrease queryEffectiveB2BIncrease(long hotelId){
		List<B2BIncrease> incList = b2BIncreaseDao.queryEffectiveOrNotB2BIncrease(hotelId);
		B2BIncrease inc = new  B2BIncrease();
		Iterator<B2BIncrease> item = incList.iterator();
		while(item.hasNext()){
			inc = item.next();
		}
		return inc;
	}
}
