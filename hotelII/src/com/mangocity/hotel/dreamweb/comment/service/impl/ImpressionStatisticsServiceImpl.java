package com.mangocity.hotel.dreamweb.comment.service.impl;

import java.util.List;

import com.mangocity.hotel.dreamweb.comment.dao.ImpressionStatisticsDao;
import com.mangocity.hotel.dreamweb.comment.model.ImpressionStatistics;
import com.mangocity.hotel.dreamweb.comment.service.ImpressionStatisticsService;

public class ImpressionStatisticsServiceImpl implements ImpressionStatisticsService {

	private ImpressionStatisticsDao impressionStatisticsDao;
	public List<ImpressionStatistics> queryImpressionStatistics(Long hotelId) {
		
		return impressionStatisticsDao.queryImpressionStatistics(hotelId);
	}
	
	public void setImpressionStatisticsDao(ImpressionStatisticsDao impressionStatisticsDao) {
		this.impressionStatisticsDao = impressionStatisticsDao;
	}
	
	

}
