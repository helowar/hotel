package com.mangocity.hotel.base.persistence;

import java.io.Serializable;

public class CommentSummary implements Serializable {
	private static final long serialVersionUID = -2913249345440853897L;
	private Long hotelId;
	private Integer commentNum;
	private String averAgepoint;
	private Integer commendUp;
	private Integer commendDown;
	
	public CommentSummary() {
	}
	public CommentSummary(Long hotelId, Integer commentNum,
			String averAgepoint, Integer commendUp, Integer commendDown) {
		this.hotelId = hotelId;
		this.commentNum = commentNum;
		this.averAgepoint = averAgepoint;
		this.commendUp = commendUp;
		this.commendDown = commendDown;
	}
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
	public Integer getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}
	public String getAverAgepoint() {
		return averAgepoint;
	}
	public void setAverAgepoint(String averAgepoint) {
		this.averAgepoint = averAgepoint;
	}
	public Integer getCommendUp() {
		return commendUp;
	}
	public void setCommendUp(Integer commendUp) {
		this.commendUp = commendUp;
	}
	public Integer getCommendDown() {
		return commendDown;
	}
	public void setCommendDown(Integer commendDown) {
		this.commendDown = commendDown;
	}
	

}
