package com.mangocity.hotel.dreamweb.comment.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author liting
 *	用于记录点评统计信息中的酒店点评各项的评分
 */
public class HotelScore implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long hotelScoreId;
	private Long hotelId;
	private Long scoreItemId;		//评分项Id
	private String scoreItemName;	//评分项名称
	private double scoreValue;			//评分值
	private Date updateDate;
	
	public Long getHotelScoreId() {
		return hotelScoreId;
	}
	public void setHotelScoreId(Long hotelScoreId) {
		this.hotelScoreId = hotelScoreId;
	}
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public Long getScoreItemId() {
		return scoreItemId;
	}
	public void setScoreItemId(Long scoreItemId) {
		this.scoreItemId = scoreItemId;
	}
	public String getScoreItemName() {
		return scoreItemName;
	}
	public void setScoreItemName(String scoreItemName) {
		this.scoreItemName = scoreItemName;
	}
	public double getScoreValue() {
		return scoreValue;
	}
	public void setScoreValue(double scoreValue) {
		this.scoreValue = scoreValue;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	
}
