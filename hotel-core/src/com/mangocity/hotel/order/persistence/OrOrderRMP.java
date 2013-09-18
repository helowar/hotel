package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

public class OrOrderRMP implements Entity{

	private static final long serialVersionUID = -6081115506557688238L;
	/**
	 * id
	 */
	private Long orderrmpId;
	/**
	 * 订单id
	 */
	private OrOrder order;
	/**
	 * 供应商id
	 */
	private Long supplierId;
	/**
	 * 供应商名称
	 */
	private String supplierName;
	
	/**
	 * 供应商简称
	 */
	private String abbreviation;
	/**
	 * 供应合同id
	 */
	private Long supplyContactId;
	/**
	 * 供应合同名称
	 */
	private String supplyContactName;
	/**
	 * 价格计划
	 */
	private Long pricePlanId;
	/**
	 * 价格计划名称
	 */
	private String pricePlanName;
	/**
	 * 商品id
	 */
	private Long commdityId;
	/**
	 * 提示信息
	 */
	private String noticeInfo;
	
	private String creator;
	private Date createtime;
	private String modifier ;
	private Date modifytime;
	
	public Long getID() {
		return null;
	}

	public Long getOrderrmpId() {
		return orderrmpId;
	}

	public void setOrderrmpId(Long orderrmpId) {
		this.orderrmpId = orderrmpId;
	}
	public OrOrder getOrder() {
		return order;
	}

	public void setOrder(OrOrder order) {
		this.order = order;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public Long getSupplyContactId() {
		return supplyContactId;
	}

	public void setSupplyContactId(Long supplyContactId) {
		this.supplyContactId = supplyContactId;
	}

	public String getSupplyContactName() {
		return supplyContactName;
	}

	public void setSupplyContactName(String supplyContactName) {
		this.supplyContactName = supplyContactName;
	}

	public Long getPricePlanId() {
		return pricePlanId;
	}

	public void setPricePlanId(Long pricePlanId) {
		this.pricePlanId = pricePlanId;
	}

	public String getPricePlanName() {
		return pricePlanName;
	}

	public void setPricePlanName(String pricePlanName) {
		this.pricePlanName = pricePlanName;
	}

	public Long getCommdityId() {
		return commdityId;
	}

	public void setCommdityId(Long commdityId) {
		this.commdityId = commdityId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	public String getNoticeInfo() {
		return noticeInfo;
	}

	public void setNoticeInfo(String noticeInfo) {
		this.noticeInfo = noticeInfo;
	}

}
