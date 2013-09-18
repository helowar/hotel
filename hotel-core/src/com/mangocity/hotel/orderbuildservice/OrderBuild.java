package com.mangocity.hotel.orderbuildservice;

import java.util.Map;

import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.webnew.persistence.HotelOrderFromBean;

/**
 *@author houdiandian
 *@see the interface afford some methods for building order ,the classes where extends
 *this should not be related with DAO and DB
 *不和数据库打交道 
 */

public interface OrderBuild {

	/**
	 * 订单基础信息，不包含钱相关的信息
	 */
	public void combineOrderBaseInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean ,Map<Key_orderBaseMap,Object> orderBaseInfoMap);
	
	/**
	 *订单条款信息
	 */
	public void combineOrderClauseInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean, Map<Key_orderClauseMap,Object> orderClauseMap);
	
	/**
	 *与电商相关的信息
	 */
	public void combineOrderEBInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean,Map<Key_orderEBMap,Object> orderEBMap);
	
	/**
	 *与会员相关的信息
	 */
	public void combineOrderMemberInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean,Map<Key_orderMemberMap,Object> orderMemberMap);
	/**
	 *与订单金额相关的信息 
	 */
	public void combineOrderMoneyInfo(OrOrder order,HotelOrderFromBean hotelOrderFromBean,Map<Key_orderMoneyMap,Object> orderMoneyMap);
	
	public enum Key_orderBaseMap{
		PriceList,OrOrderExtInfoList,SupplierAlias,SupplierID,;
	}
	public enum Key_orderClauseMap{
		Reservation,;
	}
	public enum Key_orderMemberMap{
		Member,FollewList;
	}
	public enum Key_orderMoneyMap{
		
	}
	public enum Key_orderEBMap{
		ProjectCode,;
	}
	
	
	
}
