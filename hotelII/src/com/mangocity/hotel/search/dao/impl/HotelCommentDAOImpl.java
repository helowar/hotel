package com.mangocity.hotel.search.dao.impl;

import java.util.List;

import com.mangocity.hotel.base.persistence.CommentSummary;
import com.mangocity.hotel.search.dao.HotelCommentDAO;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class HotelCommentDAOImpl extends GenericDAOHibernateImpl implements HotelCommentDAO{

	public CommentSummary getHotelComment(Long hotelId) {
		String hql = " from CommentSummary where hotelId=?";
		List list = super.query(hql, new Object[] { hotelId });
		return (CommentSummary) ((null != list && list.size() > 0) ? list
				.get(0) : null);
	}

	public List<CommentSummary> getHotelComments(String hotelIds) {
		String hql = " from CommentSummary where hotelId in (" + hotelIds + ")";
		return super.query(hql, null);
	}
}
