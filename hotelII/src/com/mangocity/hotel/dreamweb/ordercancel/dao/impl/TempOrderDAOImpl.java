package com.mangocity.hotel.dreamweb.ordercancel.dao.impl;

import java.util.List;

import com.mangocity.hotel.dreamweb.ordercancel.dao.ITempOrderDAO;
import com.mangocity.hotel.dreamweb.ordercancel.dao.impl.TempOrderDAOImpl;
import com.mangocity.hotel.dreamweb.ordercancel.dao.impl.TempOrderDAOImpl;
import com.mangocity.hotel.dreamweb.ordercancel.persistence.TempOrder;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.log.MyLog;

public class TempOrderDAOImpl extends GenericDAOHibernateImpl  implements ITempOrderDAO {
	
	private static final MyLog log = MyLog.getLogger(TempOrderDAOImpl.class);

	public List<TempOrder> queryAll() {
		// TODO Auto-generated method stub
		return super.getHibernateTemplate().loadAll(TempOrder.class);
	}

	public TempOrder queryByOrderId(Long orderId) {
		// TODO Auto-generated method stub
		return super.get(TempOrder.class, orderId);
	}

	public TempOrder query(TempOrder tempOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	public void add(TempOrder tempOrder) {
		// TODO Auto-generated method stub
		super.save(tempOrder);
	}

	public void batchAdd(List<TempOrder> tempOrders) {
		// TODO Auto-generated method stub
	}	

	public void update(TempOrder tempOrder) {
		// TODO Auto-generated method stub
		super.saveOrUpdate(tempOrder);

	}

	public void batchUpdate(List<TempOrder> tempOrders) {
		// TODO Auto-generated method stub
		
	}
	
	public void delete(TempOrder tempOrder) {
		// TODO Auto-generated method stub
		super.getHibernateTemplate().delete(tempOrder);

	}

	public void batchDelete(List<TempOrder> tempOrder) {
		// TODO Auto-generated method stub
		
	}

}
