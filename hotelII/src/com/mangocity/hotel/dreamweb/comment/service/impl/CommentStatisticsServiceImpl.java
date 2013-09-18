package com.mangocity.hotel.dreamweb.comment.service.impl;

import com.mangocity.hotel.dreamweb.comment.dao.CommentStatisticsDao;
import com.mangocity.hotel.dreamweb.comment.model.CommentStatistics;

public class CommentStatisticsServiceImpl implements com.mangocity.hotel.dreamweb.comment.service.CommentStatisticsService {

	private CommentStatisticsDao commentStatisticsDao;
	public CommentStatistics queryCommentStatistics(Long hotelId) {
		
		return commentStatisticsDao.queryCommentStatistics(hotelId);
	}
	
	public void setCommentStatisticsDao(CommentStatisticsDao commentStatisticsDao) {
		this.commentStatisticsDao = commentStatisticsDao;
	}
	
	

}
