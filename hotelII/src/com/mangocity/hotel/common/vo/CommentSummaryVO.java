package com.mangocity.hotel.common.vo;

import com.mangocity.hotel.search.vo.SerializableVO;

public class CommentSummaryVO implements SerializableVO{

	public CommentSummaryVO(){}
	
	private long hotelId;
	private int commentNum = 0;
	private String averAgepoint ="0";
	private int  commendUp = 0;
	private int  commendDown = 0;
	public long getHotelId() {
		return hotelId;
	}
	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public String getAverAgepoint() {
		return averAgepoint;
	}
	public void setAverAgepoint(String averAgepoint) {
		this.averAgepoint = averAgepoint;
	}
	public int getCommendUp() {
		return commendUp;
	}
	public void setCommendUp(int commendUp) {
		this.commendUp = commendUp;
	}
	public int getCommendDown() {
		return commendDown;
	}
	public void setCommendDown(int commendDown) {
		this.commendDown = commendDown;
	}
	
	
}
