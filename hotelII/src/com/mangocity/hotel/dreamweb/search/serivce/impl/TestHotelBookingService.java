package com.mangocity.hotel.dreamweb.search.serivce.impl;

import java.util.Date;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.mangocity.hotel.dreamweb.search.service.HotelBookService;
import com.mangocity.hotel.search.model.QueryCommodityInfo;
import com.mangocity.util.DateUtil;

/**
 * 
 * HotelBookingServiceImpl测试类
 * 
 * @author chenkeming
 *
 */
public class TestHotelBookingService extends AbstractDependencyInjectionSpringContextTests {
	
	public HotelBookService hotelBookService;
	
	@Override
	protected String[] getConfigLocations() {
		// 按照名称注入
		this.setAutowireMode(1);
		String[] configFiles = { "file:src/spring/applicationContext.xml"};
		return configFiles;
	}
	
	/**
	 * 测试queryCommodity()方法
	 */
	public void testQueryCommodity() {
		Date now = new Date();
		Date today = DateUtil.getDate(now);
		Date tommorrow = DateUtil.getDate(today, 3);
		QueryCommodityInfo comm = hotelBookService.queryCommidity(11436L,
				"pay", today, tommorrow, false);
		
		System.out.println("返现金额: " + comm.getReturnCash());
		System.out.println("finish testQueryCommodity");
	}

	public void setHotelBookService(HotelBookService hotelBookService) {
		this.hotelBookService = hotelBookService;
	}	
	
}
