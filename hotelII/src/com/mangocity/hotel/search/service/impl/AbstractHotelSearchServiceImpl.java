package com.mangocity.hotel.search.service.impl;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.service.HotelSearchService;
import com.mangocity.hotel.search.service.IHotelQueryHandler;
import com.mangocity.hotel.search.service.assistant.HotelInfo;

public abstract class AbstractHotelSearchServiceImpl implements
		HotelSearchService,InitializingBean {

	public List<HotelInfo> filterHotel(QueryHotelCondition queryHotelCondition) {
		// TODO Auto-generated method stub
		return null;
	}

	public void queryHotelsGetBean(
			QueryHotelCondition queryHotelCondition, IHotelQueryHandler handler) {
        processHotelBasicInfo();
        sortHotel();
        processCommoditySearch();
        assembleHotelInfoAndCommodityInfo();
	}

	abstract protected void processHotelBasicInfo();
	abstract protected void sortHotel();
	abstract protected void processCommoditySearch();
	abstract protected void assembleHotelInfoAndCommodityInfo();
}
