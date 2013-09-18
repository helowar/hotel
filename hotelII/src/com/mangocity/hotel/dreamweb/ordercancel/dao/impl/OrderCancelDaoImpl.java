package com.mangocity.hotel.dreamweb.ordercancel.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.dreamweb.ordercancel.dao.OrderCancelDao;
import com.mangocity.hotel.dreamweb.ordercancel.persistence.OrderCancel;
import com.mangocity.hotel.orderrecord.model.OrderRecord;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class OrderCancelDaoImpl extends GenericDAOHibernateImpl implements OrderCancelDao {

	public Serializable addOrderCancel(OrderCancel orderCancel) {
		// TODO Auto-generated method stub
		return super.save(orderCancel);	
	}

	public void updateOrderCancel(OrderCancel orderCancel) {
		// TODO Auto-generated method stub
		super.update(orderCancel);
	}

	public OrderCancel getOrderCancel(Long id) {
		// TODO Auto-generated method stub
		return super.get(OrderCancel.class, id);
	}
	
	public OrderCancel getOrderCancelByOrderId(Long orderId) {
		// TODO Auto-generated method stub
		StringBuffer hql = new StringBuffer("from OrderCancel where 1=1");
		hql.append(" and orderid="+String.valueOf(orderId.longValue()));
		List list = super.getHibernateTemplate().find(hql.toString());
		OrderCancel orderCancel = null;
		if(list.size()>0){
			orderCancel = (OrderCancel)list.get(0);
		}
		return orderCancel;
		//return super.get(OrderCancel.class, orderId);
	}

	public List<OrderCancel> getAllOrderCancel() {
		// TODO Auto-generated method stub
		return super.loadAll(OrderCancel.class);
	}

}
