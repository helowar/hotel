package com.mangocity.hotel.dreamweb.comment.dao;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.dreamweb.comment.model.ImpressionStatistics;
 /**
  * 
  * TODO liting:查询印象点评统计信息dao
  * @version   Revision History
  * <pre>
  * Author     Version       Date        Changes
  * liting    1.0           Feb 16, 2012     Created
  *
  * </pre>
  * @since 1.
  */
public interface ImpressionStatisticsDao {
	List<ImpressionStatistics> queryImpressionStatistics(Long hotelId);

	Map<Long,List<ImpressionStatistics>> queryImpressionStatistics(List<Long> hotelIds);
	
}
