package com.mangocity.ep.service;

import java.util.List;

import com.mangocity.ep.entity.EpOrder;

public interface SynEpOrderService {
   
	/**
	 * 查询过去minuteTime分钟内EP系统已经确认的订单，其中EP订单包含订单id、订单Cd、酒店是否确认、确认特殊要求类型、确认号信息
	 * @param minuteTime 过去minuteTime分钟的时间
	 * @return List<EpOrder> 订单列表
	 */
	
	public List<EpOrder> queryHotelConfirmedEPOrder(int minuteTime);
	
	/**
	 * 订单流转到专家组
	 * @param orderId
	 */
	public void sendOrder2ExpertGroup(Long orderId);
	
	/**
	 * 同步EP订单状态到or_order订单表状态，酒店确认则并向客人发送确认信息，否则订单流转到专家组
	 * @param ep ep订单
	 */
	public void synEpOrderHandler(EpOrder ep);
	
	/**
	 * 获得sequence
	 * @param synEpOrderSeq
	 * @return
	 */
	public long getOrParamSeqNextVal(String synEpOrderSeq);
}
