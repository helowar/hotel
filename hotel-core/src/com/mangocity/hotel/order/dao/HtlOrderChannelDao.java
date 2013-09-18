package com.mangocity.hotel.order.dao;

import com.mangocity.hotel.order.service.assistant.HtlOrderChannel;

public interface HtlOrderChannelDao {
  
    public void saveHtlOrderChannel(HtlOrderChannel htlOrderChannel);
	
	public void modifyHtlOrderChannel(Long orderId,String channel);
	
	public HtlOrderChannel queryHtlOrderChannel(long orderId);
}
