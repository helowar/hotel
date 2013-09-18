package com.mangocity.hotel.pricelowest.persistence;

import java.util.Date;
import com.mangocity.util.Entity;

public class HtlLowestPrice implements Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long ID;
	private String cityCode;//城市code
	private String cityName;//城市名称
	private Long hotelId;//酒店id
	private String hotelName;//酒店名称
	
	private String hotelStar;//酒店星级
	private String bizZoneCode;//商区
	private String bizZoneName;//商区名称
	private String lowestPrice;//最低价
	private String lowestPriceCurrency;//最低价
	
	private String returnCash;//最低价返现
	private Date ableDate;//售卖日期
	private Date createTime;//创建时间
	private Date modifyTime;//修改时间
	private String hotelTheme;//酒店主题

	private String supplierChannel;//供应商渠道号
	private String hotelLongitude;//酒店经度
	private String hotelLatitude;//酒店纬度
	private String lowestPriceRoomName;//最低价的房间名称
	private String presaleInfo;
	public Long getID() {
		return ID;
	}
	public void setID(Long id) {
		ID = id;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getHotelStar() {
		return hotelStar;
	}
	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}
	public String getBizZoneCode() {
		return bizZoneCode;
	}
	public void setBizZoneCode(String bizZoneCode) {
		this.bizZoneCode = bizZoneCode;
	}
	public String getBizZoneName() {
		return bizZoneName;
	}
	public void setBizZoneName(String bizZoneName) {
		this.bizZoneName = bizZoneName;
	}
	public String getLowestPrice() {
		return lowestPrice;
	}
	public void setLowestPrice(String lowestPrice) {
		this.lowestPrice = lowestPrice;
	}
	public String getLowestPriceCurrency() {
		return lowestPriceCurrency;
	}
	public void setLowestPriceCurrency(String lowestPriceCurrency) {
		this.lowestPriceCurrency = lowestPriceCurrency;
	}
	public String getReturnCash() {
		return returnCash;
	}
	public void setReturnCash(String returnCash) {
		this.returnCash = returnCash;
	}
	public Date getAbleDate() {
		return ableDate;
	}
	public void setAbleDate(Date ableDate) {
		this.ableDate = ableDate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getHotelTheme() {
		return hotelTheme;
	}
	public void setHotelTheme(String hotelTheme) {
		this.hotelTheme = hotelTheme;
	}
	public String getSupplierChannel() {
		return supplierChannel;
	}
	public void setSupplierChannel(String supplierChannel) {
		this.supplierChannel = supplierChannel;
	}
	public String getHotelLongitude() {
		return hotelLongitude;
	}
	public void setHotelLongitude(String hotelLongitude) {
		this.hotelLongitude = hotelLongitude;
	}
	public String getHotelLatitude() {
		return hotelLatitude;
	}
	public void setHotelLatitude(String hotelLatitude) {
		this.hotelLatitude = hotelLatitude;
	}
	public String getLowestPriceRoomName() {
		return lowestPriceRoomName;
	}
	public void setLowestPriceRoomName(String lowestPriceRoomName) {
		this.lowestPriceRoomName = lowestPriceRoomName;
	}
	public String getPresaleInfo() {
		return presaleInfo;
	}
	public void setPresaleInfo(String presaleInfo) {
		this.presaleInfo = presaleInfo;
	}


	
	
	

}
