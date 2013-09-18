package com.mangocity.hotel.dreamweb.comment.dao;

import java.util.List;

import com.mangocity.hotel.dreamweb.comment.model.CommentStatistics;

/**
 * 
 * TODO liting: 查询点评统计信息Dao
 * @version   Revision History
 * <pre>
 * Author     Version       Date        Changes
 * liting    1.0           Feb 16, 2012     Created
 *
 * </pre>
 * @since 1.
 */
public interface CommentStatisticsDao {
	CommentStatistics queryCommentStatistics(Long hotelId);
	List<CommentStatistics> queryCommentStatistics(String hotelIds);

}
