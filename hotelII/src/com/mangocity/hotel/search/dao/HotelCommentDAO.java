package com.mangocity.hotel.search.dao;

import java.util.List;

import com.mangocity.hotel.base.persistence.CommentSummary;

public interface HotelCommentDAO {
	public CommentSummary getHotelComment(Long hotelId);
	
	public List<CommentSummary> getHotelComments(String hotelIds);
}
