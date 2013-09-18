package com.mangocity.hotel.base.persistence;

import java.util.List;

/**
 * HtlFavouraParameter entity.
 * 
 * @author shengwei.zuo hotel2.9.3 优惠参数实体类 2009-08-25
 */

public class HtlFavouraParameter implements java.io.Serializable {

	// Fields

	private Long id;
	
	//优惠条款ID
	private Long favourableClauseId;
	
	//优惠类型
	private String favourableType;
	
	//住几晚
	private Long continueNight;
	
	//送几晚
	private Long donateNight;
	
	//循环类型
	private Long circulateType;
	
	//折扣
	private Double discount;
	
	//小数点类型
	private Long decimalPointType;
	
	//包价售价
	private Double packagerateSaleprice;
	
	//包价佣金
	private Double packagerateCommission;
	
	//包价晚数
	private Long packagerateNight;

	// Constructors
	
	private HtlFavourableclause favourableclauseEntiy;
	
	private List<HtlEveningsRent>  lstEveningsRent;

	public List<HtlEveningsRent> getLstEveningsRent() {
		return lstEveningsRent;
	}

	public void setLstEveningsRent(List<HtlEveningsRent> lstEveningsRent) {
		this.lstEveningsRent = lstEveningsRent;
	}

	/** default constructor */
	public HtlFavouraParameter() {
	}

	/** minimal constructor */
	public HtlFavouraParameter(Long favourableClauseId, String favourableType) {
		this.favourableClauseId = favourableClauseId;
		this.favourableType = favourableType;
	}

	/** full constructor */
	public HtlFavouraParameter(Long favourableClauseId, String favourableType,
			Long continueNight, Long donateNight, Long circulateType,
			Double discount, Long decimalPointType,
			Double packagerateSaleprice, Double packagerateCommission) {
		this.favourableClauseId = favourableClauseId;
		this.favourableType = favourableType;
		this.continueNight = continueNight;
		this.donateNight = donateNight;
		this.circulateType = circulateType;
		this.discount = discount;
		this.decimalPointType = decimalPointType;
		this.packagerateSaleprice = packagerateSaleprice;
		this.packagerateCommission = packagerateCommission;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFavourableClauseId() {
		return this.favourableClauseId;
	}

	public void setFavourableClauseId(Long favourableClauseId) {
		this.favourableClauseId = favourableClauseId;
	}

	public String getFavourableType() {
		return this.favourableType;
	}

	public void setFavourableType(String favourableType) {
		this.favourableType = favourableType;
	}

	public Long getContinueNight() {
		return this.continueNight;
	}

	public void setContinueNight(Long continueNight) {
		this.continueNight = continueNight;
	}

	public Long getDonateNight() {
		return this.donateNight;
	}

	public void setDonateNight(Long donateNight) {
		this.donateNight = donateNight;
	}

	public Long getCirculateType() {
		return this.circulateType;
	}

	public void setCirculateType(Long circulateType) {
		this.circulateType = circulateType;
	}

	public Double getDiscount() {
		return this.discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Long getDecimalPointType() {
		return this.decimalPointType;
	}

	public void setDecimalPointType(Long decimalPointType) {
		this.decimalPointType = decimalPointType;
	}

	public Double getPackagerateSaleprice() {
		return this.packagerateSaleprice;
	}

	public void setPackagerateSaleprice(Double packagerateSaleprice) {
		this.packagerateSaleprice = packagerateSaleprice;
	}

	public Double getPackagerateCommission() {
		return this.packagerateCommission;
	}

	public void setPackagerateCommission(Double packagerateCommission) {
		this.packagerateCommission = packagerateCommission;
	}

	public HtlFavourableclause getFavourableclauseEntiy() {
		return favourableclauseEntiy;
	}

	public void setFavourableclauseEntiy(HtlFavourableclause favourableclauseEntiy) {
		this.favourableclauseEntiy = favourableclauseEntiy;
	}

	public Long getPackagerateNight() {
		return packagerateNight;
	}

	public void setPackagerateNight(Long packagerateNight) {
		this.packagerateNight = packagerateNight;
	}

}