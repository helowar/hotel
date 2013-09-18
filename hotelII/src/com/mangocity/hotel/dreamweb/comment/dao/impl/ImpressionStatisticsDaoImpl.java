package com.mangocity.hotel.dreamweb.comment.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.dreamweb.comment.dao.ImpressionStatisticsDao;
import com.mangocity.hotel.dreamweb.comment.model.ImpressionStatistics;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class ImpressionStatisticsDaoImpl extends GenericDAOHibernateImpl implements ImpressionStatisticsDao {

	/**
	 * 查询一个酒店的点评印象数
	 */
	@SuppressWarnings("unchecked")
	public List<ImpressionStatistics> queryImpressionStatistics(Long hotelId) {
		if(hotelId==null){
			return Collections.EMPTY_LIST;
		}
		String hql = "from ImpressionStatistics his where his.hotelId=? order by his.impressionNumber desc,his.updateDate desc";

		return super.query(hql, new Object[] { hotelId });
	}

	/**
	 * 查询多个酒店的点评印象
	 */
	@SuppressWarnings("unchecked")
	public Map<Long, List<ImpressionStatistics>> queryImpressionStatistics(List<Long> hotelIds) {
		if(hotelIds==null && hotelIds.size()==0){
			return Collections.EMPTY_MAP;
		}
		
		String hql = "from ImpressionStatistics his where his.hotelId=? order by his.impressionNumber desc,his.updateDate desc";
		Map<Long, List<ImpressionStatistics>> impressionStatisticsMap = new HashMap<Long, List<ImpressionStatistics>>();
		List<ImpressionStatistics> impressionStatistics;
		for (Long hotelId : hotelIds) {
			impressionStatistics = super.query(hql, new Object[] { hotelId });
			impressionStatisticsMap.put(hotelId, impressionStatistics);
		}
		return impressionStatisticsMap;
	}

}
