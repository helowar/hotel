package com.mangocity.hotel.search.model;

import java.util.Date;

public class DynamicCommodityInfo {
	/**
	 * 查询ID
	 */
     private Long queryId = 0L;
     /**
      * 日期
      */
     private Date abledate;
     
     /**
      * 销售渠道,为""表示全部渠道
      */
     private String distchannel = "";
     /**
      * 支付方式
      */
     private String paymethod = "";
     
     /**
      * 商品ID,对应原价格类型id
      */
     private Long commodityId = 0L;
     
     /**
      * 组合商品数量
      */
     private Long commodityCount = 0L;
     
     /**
      * 床型
      */
     private String bedtype = "";
     
     /**
      * 房型ID
      */
     private Long roomtypeId = 0L;

     /**
      * 酒店ID
      */
     private Long hotelId = 0L;
     
     /**
      * 直联方式
      */
     private String hdltype = "";
     
     /**
      * 价格ID
      */
     private Long priceId = 0L;
     
     /**
      * 售价
      */
     private Double saleprice = 0d;

     /**
      * 早餐数量
      */
     private Long breakfastnumber = 0L;
     
     /**
      * 币种
      */
     private String currency = "";
     
     /**
      * 连住天数 
      */
     private Long continueDay = 0L;
     
     /**
      * 是否无条件担保
      */
     private String needAssure = "";
     
     /**
      * 能否预订--房态
      */
     private String hasbook = "";
     
     
     /**
      * 优惠数(连住,打折,住M送N)
      */
     private boolean hasFavourable;
     /**
      * 是否返现
      */
       private boolean hasReturnCash;
       
       /**
        * 是否立减
        */
       private boolean haspromptlyReduce;

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public Date getAbledate() {
		return abledate;
	}

	public void setAbledate(Date abledate) {
		this.abledate = abledate;
	}

	public String getDistchannel() {
		return distchannel;
	}

	public void setDistchannel(String distchannel) {
		this.distchannel = distchannel;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public Long getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
	}

	public Long getCommodityCount() {
		return commodityCount;
	}

	public void setCommodityCount(Long commodityCount) {
		this.commodityCount = commodityCount;
	}

	public String getBedtype() {
		return bedtype;
	}

	public void setBedtype(String bedtype) {
		this.bedtype = bedtype;
	}

	public Long getRoomtypeId() {
		return roomtypeId;
	}

	public void setRoomtypeId(Long roomtypeId) {
		this.roomtypeId = roomtypeId;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public String getHdltype() {
		return hdltype;
	}

	public void setHdltype(String hdltype) {
		this.hdltype = hdltype;
	}

	public Long getPriceId() {
		return priceId;
	}

	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}

	public Double getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(Double saleprice) {
		this.saleprice = saleprice;
	}

	public Long getBreakfastnumber() {
		return breakfastnumber;
	}

	public void setBreakfastnumber(Long breakfastnumber) {
		this.breakfastnumber = breakfastnumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Long getContinueDay() {
		return continueDay;
	}

	public void setContinueDay(Long continueDay) {
		this.continueDay = continueDay;
	}

	public String getNeedAssure() {
		return needAssure;
	}

	public void setNeedAssure(String needAssure) {
		this.needAssure = needAssure;
	}

	public String getHasbook() {
		return hasbook;
	}

	public void setHasbook(String hasbook) {
		this.hasbook = hasbook;
	}

	public boolean isHasFavourable() {
		return hasFavourable;
	}

	public void setHasFavourable(boolean hasFavourable) {
		this.hasFavourable = hasFavourable;
	}

	public boolean isHasReturnCash() {
		return hasReturnCash;
	}

	public void setHasReturnCash(boolean hasReturnCash) {
		this.hasReturnCash = hasReturnCash;
	}

	public boolean isHaspromptlyReduce() {
		return haspromptlyReduce;
	}

	public void setHaspromptlyReduce(boolean haspromptlyReduce) {
		this.haspromptlyReduce = haspromptlyReduce;
	}

}
