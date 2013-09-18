package com.mangocity.hotel.order.service.impl;

import com.mangocity.hotel.order.dao.HtlOrderChannelDao;
import com.mangocity.hotel.order.service.HtlOrderChannelService;
import com.mangocity.hotel.order.service.assistant.HtlOrderChannel;

public class HtlOrderChannelServiceImpl implements HtlOrderChannelService{
   
	private HtlOrderChannelDao htlOrderChannelDao;
	
	public void modifyHtlOrderChannel(Long orderId,String channel) {
		htlOrderChannelDao.modifyHtlOrderChannel( orderId, channel);
		
	}

	public HtlOrderChannel queryHtlOrderChannel(long orderId) {
		return htlOrderChannelDao.queryHtlOrderChannel(orderId);
		
	}

	public void saveHtlOrderChannel(HtlOrderChannel htlOrderChannel) {
		htlOrderChannelDao.saveHtlOrderChannel(htlOrderChannel);
		
	}

	public HtlOrderChannelDao getHtlOrderChannelDao() {
		return htlOrderChannelDao;
	}

	public void setHtlOrderChannelDao(HtlOrderChannelDao htlOrderChannelDao) {
		this.htlOrderChannelDao = htlOrderChannelDao;
	}
  
	
}
