package com.mangocity.hotel.order.service;

import com.mangocity.hotel.order.service.assistant.HtlOrderChannel;

/**
 * 目前只供合作方渠道发生改变时使用，真正的订单渠道没有发生改变
 */
public interface HtlOrderChannelService {
  
	public void saveHtlOrderChannel(HtlOrderChannel htlOrderChannel);
	
	public void modifyHtlOrderChannel(Long orderId,String channel);
	
	public HtlOrderChannel queryHtlOrderChannel(long orderId);
}
