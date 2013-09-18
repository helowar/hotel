package com.mangocity.hotel.dreamweb.comment.service;

import java.util.List;

import com.mangocity.hotel.dreamweb.comment.model.CommentStatistics;

/**
 * 
 * TODO liting:查询点评统计信息service
 * @version   Revision History
 * <pre>
 * Author     Version       Date        Changes
 * liting    1.0           Feb 16, 2012     Created
 *
 * </pre>
 * @since 1.
 */
public interface CommentStatisticsService {
	CommentStatistics queryCommentStatistics(Long hotelId);

}
