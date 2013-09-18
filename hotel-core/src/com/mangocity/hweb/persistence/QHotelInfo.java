package com.mangocity.hweb.persistence;

import java.io.Serializable;

public class QHotelInfo implements Serializable {
	/*
	 * 酒店Id
	 */
	private Long hotelId;
	
	/*
	 * 城市编码
	 */
	private String city;
	
	/*
	 * 商业区编码
	 */
	private String bizZone;
	
	/*
	 * 酒店中文名
	 */
	private String chnName;
	
	/*
	 * 酒店英文名
	 */
	private String engName;
	
	/*
	 * 可预订标志
	 */
	private boolean bookingFlag;
	
	/*
	 * 最低销售价格
	 */
	private String minSalePrice;
	
	/*
	 * 币种
	 */
	private String currency;
	
	/*
	 * 酒店链接URL
	 */
	private String hotelURL;
	
	/*
	 * 是否主推
	 */
	private boolean recommend;

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBizZone() {
		return bizZone;
	}

	public void setBizZone(String bizZone) {
		this.bizZone = bizZone;
	}

	public String getChnName() {
		return chnName;
	}

	public void setChnName(String chnName) {
		this.chnName = chnName;
	}

	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}

	public boolean isBookingFlag() {
		return bookingFlag;
	}

	public void setBookingFlag(boolean bookingFlag) {
		this.bookingFlag = bookingFlag;
	}

	public String getMinSalePrice() {
		return minSalePrice;
	}

	public void setMinSalePrice(String minSalePrice) {
		this.minSalePrice = minSalePrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getHotelURL() {
		return hotelURL;
	}

	public void setHotelURL(String hotelURL) {
		this.hotelURL = hotelURL;
	}

	public boolean isRecommend() {
		return recommend;
	}

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

}
