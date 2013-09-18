package com.mangocity.hotel.search.model;

import java.util.Date;

public class SalePromotionMango {
	
	/**
	 * 促销信息Id
	 */
    private long promotionId;

    /**
     * 促销名称
     */
    private String promotionName;

    /**
     * 促销内容
     */
    private String promotionContent;

    /**
     * 起始日期
     */
    private Date beginDate;

    /**
     * 终止时期
     */
    private Date endDate;

    /**
     * url地址
     */ 
    private String promotionUrl;
    
    /**
     * 优惠代码
     */
    private String preferentialCode;

	public long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(long promotionId) {
		this.promotionId = promotionId;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String getPromotionContent() {
		return promotionContent;
	}

	public void setPromotionContent(String promotionContent) {
		this.promotionContent = promotionContent;
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

	public String getPromotionUrl() {
		return promotionUrl;
	}

	public void setPromotionUrl(String promotionUrl) {
		this.promotionUrl = promotionUrl;
	}

	public String getPreferentialCode() {
		return preferentialCode;
	}

	public void setPreferentialCode(String preferentialCode) {
		this.preferentialCode = preferentialCode;
	}

	
}
