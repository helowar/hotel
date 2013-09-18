package com.mangocity.hotel.dreamweb.comment.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author liting
 * 用于记录点评统计信息
 *
 */
public class CommentStatistics implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long commentStatisticsId;
	private Long hotelId;
	private double totalScore;		//酒店总评分
	private int commentNumber;		//点评人数
	private int recommendNumber;	//推荐人数
	private int unrecommendNumber;	//不推荐人数
	private Date updateDate;		//更新时间
	
	public Long getCommentStatisticsId() {
		return commentStatisticsId;
	}
	
	public void setCommentStatisticsId(Long commentStatisticsId) {
		this.commentStatisticsId = commentStatisticsId;
	}
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
	public double getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}
	public int getCommentNumber() {
		return commentNumber;
	}
	public void setCommentNumber(int commentNumber) {
		this.commentNumber = commentNumber;
	}
	public int getRecommendNumber() {
		return recommendNumber;
	}
	public void setRecommendNumber(int recommendNumber) {
		this.recommendNumber = recommendNumber;
	}
	public int getUnrecommendNumber() {
		return unrecommendNumber;
	}
	public void setUnrecommendNumber(int unrecommendNumber) {
		this.unrecommendNumber = unrecommendNumber;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	

}
