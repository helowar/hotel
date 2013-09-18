package com.mangocity.hotel.order.dao;

import com.mangocity.hotel.base.persistence.TempOrder;

/**
 * 
 * xieyanhui: 订单临时表temp_order Dao接口
 * @version   Revision History
 * <pre>
 * Author     Version       Date        Changes
 * xieyanhui    1.0           Mar 26, 2013     Created
 *
 * </pre>
 * @since 1.
 */
public interface TempOrderDao {

	/**
	 * 向订单临时表插入一条记录，把订单跳转到相应的组
	 * @param tempOrder
	 */
     public void insertTempOrder(TempOrder tempOrder);
     
	 /**
	  * 将订单跳转到相关的组别
	  * @param orderId 订单ID
	  * @param groupType 组别
	  * @return
	  */
     public void updateTempOrder(Long orderId,Long groupType);
     
     /**
      * 
      * 根据订单ID查询临时订单记录
      * @param orderId
      * @return List<TempOrder>
      */
     public TempOrder queryTempOrder(Long orderId);
}
