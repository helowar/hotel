package com.mangocity.hotel.dreamweb.ordercancel.dao;

import java.util.List;

import com.mangocity.hotel.dreamweb.ordercancel.persistence.TempOrder;


public interface ITempOrderDAO {
	
	public List<TempOrder>queryAll();
	
	public TempOrder queryByOrderId(Long orderId);
	
	public TempOrder query(TempOrder tempOrder);
	
	public void add(TempOrder tempOrder);
	
	public void batchAdd(List<TempOrder> tempOrders);
	
	public void update(TempOrder tempOrder);
	
	public void batchUpdate(List<TempOrder>tempOrders);
	
	public void delete(TempOrder tempOrder);
	
	public void batchDelete(List<TempOrder> tempOrder);

}
