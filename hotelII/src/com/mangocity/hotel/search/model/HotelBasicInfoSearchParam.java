package com.mangocity.hotel.search.model;

import java.util.Date;

import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.search.service.assistant.SpecialRequest;

public class HotelBasicInfoSearchParam {
	
	/**
	 * 酒店ids
	 */
	protected String hotelIdsStr;
	
	/**
	 * 酒店id
	 */
	protected String hotelId;
	
	/**
	 * 城市代码（三字码）
	 */
	protected String cityCode;

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
	protected SpecialRequest specialRequest;

	/**
	 * 酒店类型
	 */
	protected String hotelType;

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
	 * 地理信息
	 */
	private HtlGeographicalposition htlGeographicalposition;
	
	/**
	 * 酒店集团,对应界面的酒店品牌
	 */
	protected String hotelGroup;
	
	/**
	 * 酒店品牌，真正的品牌
	 */
	protected String hotelBrand;
	
	//支付方式
	protected String payMethod;
 
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

	public SpecialRequest getSpecialRequest() {
		return specialRequest;
	}

	public void setSpecialRequest(SpecialRequest specialRequest) {
		this.specialRequest = specialRequest;
	}

	public String getHotelType() {
		return hotelType;
	}

	public void setHotelType(String hotelType) {
		this.hotelType = hotelType;
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

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public HtlGeographicalposition getHtlGeographicalposition() {
		return htlGeographicalposition;
	}

	public void setHtlGeographicalposition(HtlGeographicalposition htlGeoPos) {
		this.htlGeographicalposition = htlGeoPos;
	}

	public String getHotelGroup() {
		return hotelGroup;
	}

	public void setHotelGroup(String hotelGroup) {
		this.hotelGroup = hotelGroup;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public String getHotelIdsStr() {
		return hotelIdsStr;
	}

	public void setHotelIdsStr(String hotelIdsStr) {
		this.hotelIdsStr = hotelIdsStr;
	}

	public String getHotelBrand() {
		return hotelBrand;
	}

	public void setHotelBrand(String hotelBrand) {
		this.hotelBrand = hotelBrand;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	
	
}
