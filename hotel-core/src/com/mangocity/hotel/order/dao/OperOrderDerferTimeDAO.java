package com.mangocity.hotel.order.dao;

import java.util.List;

import com.mangocity.hotel.order.persistence.DeferOrder;
import com.mangocity.hotel.order.persistence.DerferOrderParam;
import com.mangocity.hotel.order.persistence.OrHandleLog;

public interface OperOrderDerferTimeDAO {
   
	/**
	 * 修改订单暂缓时间
	 */
	public void modifyOrderDerferTime(Integer deferTime,Long orderId);
	
	/**
	 * 查询暂缓时间的订单
	 */
	public List<DeferOrder> queryDerferOrderData(DerferOrderParam param);
	
	/**
	 * 判断订单是否属于中台以及有分配人
	 */
	public Integer validateOrderSts(Long orderId);
	
	/**
	 * 重新交接订单时，取消原订单暂缓时间
	 */
	
	public void modifyDerferOrder(Long orderId);
	
	  /**
	 * 根据条件得到总行数
	 */
   public Long querySumRow(DerferOrderParam param);
   
   /**
    * 保存暂缓时间日志
    */
   public void saveDerferTimeLog(OrHandleLog handleLog);
}
