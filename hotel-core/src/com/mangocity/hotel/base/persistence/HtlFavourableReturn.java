/**
 * 
 */
package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiongxiaojun
 *
 */
public class HtlFavourableReturn implements Serializable {
	//ID
	private Long id;
	
	//酒店ID
	private Long hotelId;
	
	//价格类型ID
	private Long priceTypeId;
	
	//支付方式
	private Integer payMethod;
	
	//开始日期
	private Date beginDate;
	
	//结束日期
	private Date endDate;
	
	//星期
	private String week;
	
	//返还比例
	private Double returnScale;
	
	//价格类型名称
	private String priceTypeName;
	
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
	
	//价格类型字符串
	private String priceTypeIdStr;
	
	//房型字符串
	private String roomTypeIdStr;

	/**property accessors **/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public Integer getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}

	public Long getPriceTypeId() {
		return priceTypeId;
	}

	public void setPriceTypeId(Long priceTypeId) {
		this.priceTypeId = priceTypeId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public Double getReturnScale() {
		return returnScale;
	}

	public void setReturnScale(Double returnScale) {
		this.returnScale = returnScale;
	}

	public String getPriceTypeName() {
		return priceTypeName;
	}

	public void setPriceTypeName(String priceTypeName) {
		this.priceTypeName = priceTypeName;
	}

	public String getCreateById() {
		return createById;
	}

	public void setCreateById(String createById) {
		this.createById = createById;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyById() {
		return modifyById;
	}

	public void setModifyById(String modifyById) {
		this.modifyById = modifyById;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

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

	
}
