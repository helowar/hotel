package com.mangocity.hotel.search.model;

import java.util.Date;

import com.mangocity.hotel.search.service.assistant.SpecialRequest;

public class FilterHotelCondition {
	/**
	 * 城市代码（三字码）
	 */
	protected String cityCode;
	
	/**
	 * 城市名称(给seo用)
	 */
	protected String cityName;

	/**
	 * 入住日期
	 */
	protected Date inDate;

	/**
	 * 离店日期
	 */
	protected Date outDate;

	/**
	 * 酒店名称
	 */
	protected String hotelName;

	/**
	 * 最低价格
	 */
	protected String minPrice;

	/**
	 * 最高价格
	 */
	protected String maxPrice;

	/**
	 * 酒店星级
	 */
	protected String starLeval;

	/**
	 * 特殊要求
	 */
	protected String specialRequestString;

	/**
	 * 酒店类型
	 */
	protected String HotelType;

	/**
	 * 行政区代码
	 */
	protected String district;

	/**
	 * 商业区代码
	 */
	protected String bizZone;

	/**
	 * 查询渠道
	 */
	protected String fromChannel;
	
	/**
	 * 优惠立减
	 */
	
	/**
	 * 支付方式
	 */
	/**
	 * 1推荐,2,价格,3,星级，默认是推荐
	 */
	protected int sorttype;
	 /**
	  * 1代表升序,2代表降序,默认是推荐从高到底,价格从低到高,星级从高到底
	  */
	protected int sortUpOrDown;
	/**
     * 多少条记录/页，默认15个酒店/页
     */
	protected int pageSize = 15;
    
    /**
     * 页码
     */
	protected int pageNo = 1;

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getStarLeval() {
		return starLeval;
	}

	public void setStarLeval(String starLeval) {
		this.starLeval = starLeval;
	}


	public String getHotelType() {
		return HotelType;
	}

	public void setHotelType(String hotelType) {
		HotelType = hotelType;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getBizZone() {
		return bizZone;
	}

	public void setBizZone(String bizZone) {
		this.bizZone = bizZone;
	}

	public String getFromChannel() {
		return fromChannel;
	}

	public void setFromChannel(String fromChannel) {
		this.fromChannel = fromChannel;
	}

	public String getSpecialRequestString() {
		return specialRequestString;
	}

	public void setSpecialRequestString(String specialRequestString) {
		this.specialRequestString = specialRequestString;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getSorttype() {
		return sorttype;
	}

	public void setSorttype(int sorttype) {
		this.sorttype = sorttype;
	}

	public int getSortUpOrDown() {
		return sortUpOrDown;
	}

	public void setSortUpOrDown(int sortUpOrDown) {
		this.sortUpOrDown = sortUpOrDown;
	}



	
}
