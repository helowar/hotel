package com.mangocity.hotel.order.dao;

import java.util.List;

import com.mangocity.hotel.order.persistence.DeferOrder;
import com.mangocity.hotel.order.persistence.DerferOrderParam;

public interface HurryOrderDAO {
   
	/**
	 * 修改催单次数（调用 +1）
	 */
	public Integer modifyHurryOrderNum(Long orderId);
	
	/**
	 * 查询某订单的催单次数
	 */
	public Integer queryHurryOrderNum(Long orderId);
	
	/**
	 * 催单次数清零
	 */
	public void cleanHurryTimes(Long orderId);

}
