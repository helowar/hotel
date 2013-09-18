package com.mangocity.hotel.orderconstruct;

import com.mangocity.hotel.orderbuildservice.OrderBuild;


public class OrderConstructFactory {

	public static OrderConstruct createOrderConstruct(OrderBuild orderBuild){
		 return new OrderConstruct(orderBuild);
	}
}
