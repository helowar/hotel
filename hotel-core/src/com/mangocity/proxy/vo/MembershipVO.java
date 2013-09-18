package com.mangocity.proxy.vo;

import java.util.Date;

public class MembershipVO {
	private String fontColor = "";
	  private String labelText = "切换会籍";
	  private String shortAliasMbrCd;
	  private Long mbrId;
	  private Long categoryId;
	  private String categoryCd;
	  private String aliasNo;
	  private String ctsMbrshipCategoryCd;
	  private String categoryName;
	  private Long mbrshipId;
	  private String mbrshipCd;
	  private String oldMbrshipCd;
	  private Long hierarchyId;
	  private String hierarchyName;
	  private String stus;
	  private Date availableDat;
	  private Date createTim;
	  private boolean returnCash=false;//当前会籍是否支持返现，‘True'支持,'False'不支持
	  
	public boolean getReturnCash() {
		return returnCash;
	}
	public void setReturnCash(Boolean returnCash) {
		this.returnCash = returnCash;
	}
	public String getFontColor() {
		return fontColor;
	}
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	public String getLabelText() {
		return labelText;
	}
	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}
	public String getShortAliasMbrCd() {
		return shortAliasMbrCd;
	}
	public void setShortAliasMbrCd(String shortAliasMbrCd) {
		this.shortAliasMbrCd = shortAliasMbrCd;
	}
	public Long getMbrId() {
		return mbrId;
	}
	public void setMbrId(Long mbrId) {
		this.mbrId = mbrId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryCd() {
		return categoryCd;
	}
	public void setCategoryCd(String categoryCd) {
		this.categoryCd = categoryCd;
	}
	public String getAliasNo() {
		return aliasNo;
	}
	public void setAliasNo(String aliasNo) {
		this.aliasNo = aliasNo;
	}
	public String getCtsMbrshipCategoryCd() {
		return ctsMbrshipCategoryCd;
	}
	public void setCtsMbrshipCategoryCd(String ctsMbrshipCategoryCd) {
		this.ctsMbrshipCategoryCd = ctsMbrshipCategoryCd;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Long getMbrshipId() {
		return mbrshipId;
	}
	public void setMbrshipId(Long mbrshipId) {
		this.mbrshipId = mbrshipId;
	}
	public String getMbrshipCd() {
		return mbrshipCd;
	}
	public void setMbrshipCd(String mbrshipCd) {
		this.mbrshipCd = mbrshipCd;
	}
	public String getOldMbrshipCd() {
		return oldMbrshipCd;
	}
	public void setOldMbrshipCd(String oldMbrshipCd) {
		this.oldMbrshipCd = oldMbrshipCd;
	}
	public Long getHierarchyId() {
		return hierarchyId;
	}
	public void setHierarchyId(Long hierarchyId) {
		this.hierarchyId = hierarchyId;
	}
	public String getHierarchyName() {
		return hierarchyName;
	}
	public void setHierarchyName(String hierarchyName) {
		this.hierarchyName = hierarchyName;
	}
	public String getStus() {
		return stus;
	}
	public void setStus(String stus) {
		this.stus = stus;
	}
	public Date getAvailableDat() {
		return availableDat;
	}
	public void setAvailableDat(Date availableDat) {
		this.availableDat = availableDat;
	}
	public Date getCreateTim() {
		return createTim;
	}
	public void setCreateTim(Date createTim) {
		this.createTim = createTim;
	}
	  
}
