package com.mangocity.hotel.dreamweb.comment.dao.impl;

import java.util.List;

import com.mangocity.hotel.dreamweb.comment.dao.CommentStatisticsDao;
import com.mangocity.hotel.dreamweb.comment.model.CommentStatistics;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * 
 * TODO liting: Change to the actual description of this class
 * @version   Revision History
 * <pre>
 * Author     Version       Date        Changes
 * liting    1.0           Feb 16, 2012     Created
 *
 * </pre>
 * @since 1.
 */
public class CommentStatisticsDaoImpl extends GenericDAOHibernateImpl implements CommentStatisticsDao {

	/**
	 * 查询一个酒店的点评统计信息
	 * @param 酒店的Id
	 */
	public CommentStatistics queryCommentStatistics(Long hotelId) {
		String hql="from CommentStatistics cs where cs.hotelId=?";
		
		List<CommentStatistics> commentStatistics=super.query(hql, new Object[]{hotelId});
		if(commentStatistics!=null&&commentStatistics.size()>0){
			return commentStatistics.get(0);
		}else{
		return null;
		}
	}

	/**
	 * 查询多个酒店的点评统计信息，酒店id才有in语句进行查询
	 * @param 酒店的Ids，将酒店的id拼接成字符串
	 */
	public List<CommentStatistics> queryCommentStatistics(String hotelIds) {
		String hql="from CommentStatistics cs where cs.hotelId in ("+hotelIds+")";
		return super.query(hql, null);
	}

}
