package com.mangocity.hotel.base.persistence;

import java.util.Date;

/**
 * HtlFavourableDecrease entity.
 * 
 * @author zhijie.gu hotel2.9.3.1 优惠立减规则实体类 2009-10-20
 */

public class HtlFavourableDecrease implements java.io.Serializable {

	private Long id;
	
	// 酒店ID
	private Long hotelId;

	// 房型id
	private Long roomTypeId;

	// 价格类型ID
	private Long priceTypeId;
	
	// 开始日期
	private Date beginDate;

	// 结束日期
	private Date endDate;
	
	// 星期
	private String week;
	
	//支付方式（1为面付/2为预付）
	private long payMethod;
	
	// 立减金额
	private long decreasePrice;
	
	// 支付酒店方式（1:面付底价,2、面付芒果售价；）
	private long hotelPayType;

	// 创建人ID
	private String createById;

	// 创建人名称
	private String createBy;

	// 创建时间
	private Date createTime;

	// 修改人ID
	private String modifyById;

	// 修改人名称
	private String modifyBy;

	// 修改时间
	private Date modifyTime;
	
	// 价格类型名称
	private String priceTypeName;
	
	private String priceTypeIdStr;
	
	private String roomTypeIdStr;
	
	private double basePrice;

	public String getPriceTypeIdStr() {
		return priceTypeIdStr;
	}

	public void setPriceTypeIdStr(String priceTypeIdStr) {
		this.priceTypeIdStr = priceTypeIdStr;
	}

	public String getRoomTypeIdStr() {
		return roomTypeIdStr;
	}

	public void setRoomTypeIdStr(String roomTypeIdStr) {
		this.roomTypeIdStr = roomTypeIdStr;
	}

	/** default constructor */
	public HtlFavourableDecrease() {
		
	}
	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHotelId() {
		return this.hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public Long getPriceTypeId() {
		return this.priceTypeId;
	}

	public void setPriceTypeId(Long priceTypeId) {
		this.priceTypeId = priceTypeId;
	}

	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getWeek() {
		return this.week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getCreateById() {
		return this.createById;
	}

	public void setCreateById(String createById) {
		this.createById = createById;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyById() {
		return this.modifyById;
	}

	public void setModifyById(String modifyById) {
		this.modifyById = modifyById;
	}

	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(Long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public long getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(long payMethod) {
		this.payMethod = payMethod;
	}

	public long getDecreasePrice() {
		return decreasePrice;
	}

	public void setDecreasePrice(long decreasePrice) {
		this.decreasePrice = decreasePrice;
	}

	public long getHotelPayType() {
		return hotelPayType;
	}

	public void setHotelPayType(long hotelPayType) {
		this.hotelPayType = hotelPayType;
	}

	public String getPriceTypeName() {
		return priceTypeName;
	}

	public void setPriceTypeName(String priceTypeName) {
		this.priceTypeName = priceTypeName;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

}