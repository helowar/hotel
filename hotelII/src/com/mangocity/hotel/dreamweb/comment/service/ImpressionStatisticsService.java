package com.mangocity.hotel.dreamweb.comment.service;

import java.util.List;

import com.mangocity.hotel.dreamweb.comment.model.ImpressionStatistics;

/**
 * 
 * TODO liting:查询印象点评统计信息Service
 * @version   Revision History
 * <pre>
 * Author     Version       Date        Changes
 * liting    1.0           Feb 16, 2012     Created
 *
 * </pre>
 * @since 1.
 */
public interface ImpressionStatisticsService {
	List<ImpressionStatistics> queryImpressionStatistics(Long hotelId);
}
