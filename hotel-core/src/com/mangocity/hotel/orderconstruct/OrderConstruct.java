package com.mangocity.hotel.orderconstruct;

import java.util.Map;

import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.orderbuildservice.OrderBuild;
import com.mangocity.webnew.persistence.HotelOrderFromBean;

/**
 * @author houdiandian
 * @see the class is for creating order 
 */
public class OrderConstruct {

	private OrderBuild orderBuild;
	public OrderConstruct(OrderBuild orderBuild){
		this.orderBuild = orderBuild;
	}
	
	//创建电话面付订单
	public void createTeleOrder(OrOrder order,HotelOrderFromBean hotelOrderFromBean){
	
	}
	
	
	//创建网站订单
	public void createWebOrder(OrOrder order,HotelOrderFromBean hotelOrderFromBean,Map orderBaseInfoMap,Map orderClauseMap,Map orderMemberMap,Map orderMoneyMap,Map orderEBMap){
		orderBuild.combineOrderBaseInfo(order, hotelOrderFromBean, orderBaseInfoMap);
		orderBuild.combineOrderClauseInfo(order, hotelOrderFromBean, orderClauseMap);
		orderBuild.combineOrderMemberInfo(order, hotelOrderFromBean, orderMemberMap);
		orderBuild.combineOrderMoneyInfo(order, hotelOrderFromBean, orderMoneyMap);
		orderBuild.combineOrderEBInfo(order, hotelOrderFromBean, orderEBMap);
	}
	
   //创建魅影订单
	public void createShadowOrder(OrOrder order,HotelOrderFromBean hotelOrderFromBean,Map orderBaseInfoMap,Map orderClauseMap,Map orderMemberMap,Map orderMoneyMap,Map orderEBMap){
		orderBuild.combineOrderBaseInfo(order, hotelOrderFromBean, orderBaseInfoMap);
		orderBuild.combineOrderMemberInfo(order, hotelOrderFromBean, orderMemberMap);
		orderBuild.combineOrderMoneyInfo(order, hotelOrderFromBean, orderMoneyMap);
	}
	
}
