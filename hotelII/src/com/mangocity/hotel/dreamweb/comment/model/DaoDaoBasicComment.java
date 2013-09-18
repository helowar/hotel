package com.mangocity.hotel.dreamweb.comment.model;

import java.io.Serializable;

/**
 * 到到网点评基本信息
 * @author panjianping
 *
 */
public class DaoDaoBasicComment implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long hotelId;
	private Long daodaoId;
	private String hotelName;
	private String ratingUrl;
	private Integer totalNumber;
	
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getRatingUrl() {
		return ratingUrl;
	}
	public void setRatingUrl(String ratingUrl) {
		this.ratingUrl = ratingUrl;
	}		
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotellId) {
			this.hotelId = hotellId;
	}
	public Long getDaodaoId() {
		return daodaoId;
	}
	public void setDaodaoId(Long daodaoId) {
		this.daodaoId = daodaoId;
	}
	public Integer getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}
	
	
	

}
