package com.mangocity.tmchotel.persistence;

/**
 * 
 * 刷畅联价格后返回的实体 TMC-V2.0
 * add by shengwei.zuo 
 */

public class HdlPriceUtil {
	
	//首日价格
	private Double firstDayPrice;
	
	//刷回的总金额
	private Double hdlPriceNum;
	
	//返现的总金额
	private Long hdlPriceCash;

	public Double getHdlPriceNum() {
		return hdlPriceNum;
	}

	public void setHdlPriceNum(Double hdlPriceNum) {
		this.hdlPriceNum = hdlPriceNum;
	}

	public Long getHdlPriceCash() {
		return hdlPriceCash;
	}

	public void setHdlPriceCash(Long hdlPriceCash) {
		this.hdlPriceCash = hdlPriceCash;
	}

	public Double getFirstDayPrice() {
		return firstDayPrice;
	}

	public void setFirstDayPrice(Double firstDayPrice) {
		this.firstDayPrice = firstDayPrice;
	}
  
}