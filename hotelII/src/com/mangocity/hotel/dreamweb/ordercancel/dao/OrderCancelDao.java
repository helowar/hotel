package com.mangocity.hotel.dreamweb.ordercancel.dao;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.dreamweb.ordercancel.persistence.OrderCancel;

/*
 * 网站取消订单的Dao层接口
 */
public interface OrderCancelDao {
	
	public Serializable addOrderCancel(OrderCancel orderCancel);
	
	public void updateOrderCancel(OrderCancel orderCancel);
	
	public OrderCancel getOrderCancel(Long id);
	
	public OrderCancel getOrderCancelByOrderId(Long orderId);
	
	public List<OrderCancel> getAllOrderCancel();

}
