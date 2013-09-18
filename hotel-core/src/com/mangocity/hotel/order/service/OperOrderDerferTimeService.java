package com.mangocity.hotel.order.service;

import java.util.List;

import com.mangocity.hotel.order.persistence.DeferOrder;
import com.mangocity.hotel.order.persistence.DerferOrderParam;
import com.mangocity.hotel.order.persistence.OrHandleLog;

public interface OperOrderDerferTimeService {
   
	
	/**
	 * 修改订单的暂缓时间
	 * @param deferTimem
	 * @param orderId
	 */
	public  void modifyOrderDerferTime(Integer deferTime,Long orderId);
	
	/**
	 * 查询暂缓订单信息
	 */
   public List<DeferOrder> queryDerferOrderData(DerferOrderParam param);
   
   /**
    * 判断订单 是否属于中台 且订单已分配 1:是 0：否
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
