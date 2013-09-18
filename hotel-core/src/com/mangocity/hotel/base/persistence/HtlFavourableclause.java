package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * HtlFavourableclause entity.
 * 
 * @author shengwei.zuo hotel2.9.3 优惠条款实体类 2009-08-25
 */

public class HtlFavourableclause implements java.io.Serializable {

	private Long id;
	
	private String favClauseIdStr;
	
	// 酒店ID
	private Long hotelId;

	// 合同ID
	private Long contractId;

	// 价格类型ID
	private Long priceTypeId;
	
	//价格类型ID字符串
	private String priceTypeIdStr;

	// 价格类型名称
	private String priceTypeName;
	
	// 价格类型名称字符串
	private String priceTypeNameStr;

	// 优惠类型
	private String favourableType;

	// 开始日期
	private Date beginDate;

	// 结束日期
	private Date endDate;

	// 星期
	private String week;

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

	// 随机数
	private Long randomNumber;
	
	private List<HtlFavouraParameter> lstPackagerate; 
	

	// Constructors

	/** default constructor */
	public HtlFavourableclause() {
		
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

	public Long getContractId() {
		return this.contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public Long getPriceTypeId() {
		return this.priceTypeId;
	}

	public void setPriceTypeId(Long priceTypeId) {
		this.priceTypeId = priceTypeId;
	}

	public String getPriceTypeName() {
		return this.priceTypeName;
	}

	public void setPriceTypeName(String priceTypeName) {
		this.priceTypeName = priceTypeName;
	}

	public String getFavourableType() {
		return this.favourableType;
	}

	public void setFavourableType(String favourableType) {
		this.favourableType = favourableType;
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

	public Long getRandomNumber() {
		return randomNumber;
	}

	public void setRandomNumber(Long randomNumber) {
		this.randomNumber = randomNumber;
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


	public String getPriceTypeIdStr() {
		return priceTypeIdStr;
	}

	public void setPriceTypeIdStr(String priceTypeIdStr) {
		this.priceTypeIdStr = priceTypeIdStr;
	}

	public String getPriceTypeNameStr() {
		return priceTypeNameStr;
	}

	public void setPriceTypeNameStr(String priceTypeNameStr) {
		this.priceTypeNameStr = priceTypeNameStr;
	}



	public List<HtlFavouraParameter> getLstPackagerate() {
		return lstPackagerate;
	}

	public void setLstPackagerate(List<HtlFavouraParameter> lstPackagerate) {
		this.lstPackagerate = lstPackagerate;
	}

	public String getFavClauseIdStr() {
		return favClauseIdStr;
	}

	public void setFavClauseIdStr(String favClauseIdStr) {
		this.favClauseIdStr = favClauseIdStr;
	}

}