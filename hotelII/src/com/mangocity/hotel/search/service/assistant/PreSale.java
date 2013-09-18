package com.mangocity.hotel.search.service.assistant;


public class PreSale {
	
	protected long preSaleId;
	 /**
     * 酒店是否有芒果网大礼包优惠信息 1: 有 0: 无
     */
    protected int hasPreSale;

    /**
     * 芒果网优惠信息名称
     */
    protected String preSaleName;

    /**
     * 芒果网优惠信息内容
     */
    protected String preSaleContent;

    /**
     * 芒果网优惠起止日期字符串
     */
    protected String preSaleBeginEnd;

    /**
     * 芒果网优惠URL
     */
    protected String preSaleURL;

	public int getHasPreSale() {
		return hasPreSale;
	}

	public void setHasPreSale(int hasPreSale) {
		this.hasPreSale = hasPreSale;
	}

	public String getPreSaleName() {
		return preSaleName;
	}

	public void setPreSaleName(String preSaleName) {
		this.preSaleName = preSaleName;
	}

	public String getPreSaleContent() {
		return preSaleContent;
	}

	public void setPreSaleContent(String preSaleContent) {
		this.preSaleContent = preSaleContent;
	}

	public String getPreSaleBeginEnd() {
		return preSaleBeginEnd;
	}

	public void setPreSaleBeginEnd(String preSaleBeginEnd) {
		this.preSaleBeginEnd = preSaleBeginEnd;
	}

	public String getPreSaleURL() {
		return preSaleURL;
	}

	public void setPreSaleURL(String preSaleURL) {
		this.preSaleURL = preSaleURL;
	}

	public long getPreSaleId() {
		return preSaleId;
	}

	public void setPreSaleId(long preSaleId) {
		this.preSaleId = preSaleId;
	}
 
}
