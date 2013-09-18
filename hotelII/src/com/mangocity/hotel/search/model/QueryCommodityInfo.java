package com.mangocity.hotel.search.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询表实体类
 * @author zengyong
 *
 */

public  class QueryCommodityInfo  implements Serializable {

	/**
	 * 查询ID
	 */
     private Long queryId = 0L;
     
     /**
      * 上架ID
      */
     private Long distId = 0L;
     /**
      * 日期
      */
     private Date abledate;
     /**
      * 销售渠道,为""表示全部渠道
      */
     private String distchannel = "";
     /**
      * 会员类型
      */
     private String membertype = "";
     /**
      * 用户类型
      */
     private String usertype = "";
     /**
      * 关房标记
      */
     private String closeflag = "";
     /**
      * 关房原因
      */
     private String closereason = "";
     
     /**
     * 不能预订原因
     */
    private String cantbookReason;
     
     /**
      * 支付方式
      */
     private String paymethod = "";
     /**
      * 商品ID,对应原价格类型id
      */
     private Long commodityId = 0L;
     /**
      * 商品名称
      */
     private String commodityName = "";
     /**
      * 商品编码
      */
     private String commodityNo = "";
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
      * 房型名称
      */
     private String roomtypeName = "";
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
      * 净价
      */
     private Double advicePrice = 0d;
     /**
      * 门市价
      */
     private Double salesroomprice = 0d;
     /**
      * 早餐类型
      */
     private Long breakfasttype = 0L;
     /**
      * 早餐数量
      */
     private Long breakfastnumber = 0L;
     /**
      * 早餐价格
      */
     private Double breakfastprice = 0d;
     /**
      * 币种
      */
     private String currency = "";
     /**
      * 佣金计算公式
      */
     private String formula = "";
     
     /**
      * 佣金COMMISSION
      */
     private Double commission = 0d;
     /**
      * 佣金率COMMISSION_RATE
      */
     private Double commissionRate = 0d;
     /**
      * 商家优惠ID
      */
     private Long dealerFavourableId = 0L;
     /**
      * 商家促销ID
      */
     private Long dealerPromotionsaleId = 0L;
     /**
      * 供应商优惠Id
      */
     private Long providerFavourableId = 0L;
     /**
      * 供应商促销ID
      */
     private Long providerPromotionsaleId = 0L;
     /**
      * 预订条款ID
      */
     private Long bookclauseId = 0L;
     
     /**
      * 面转预<br>
      * "3" : 不许<br>
      * "2" ：允许<br>
      * "1" : 必须 
      */
     private String paytoprepay = "";
     /**
      * 预订开始日期
      */
     private Date bookstartdate;
     /**
      * 预订结束日期
      */
     private Date bookenddate;
     /**
      * 预订开始时间
      */
     private String morningtime = "";
     /**
      * 预订结束时间
      */
     private String eveningtime = "";
     /**
      * 连住开始日期
      */
     private Date continuumInEnd;
     /**
      * 连住结束日期
      */
     private Date continuumInStart;
     
     /**
      * 必住日期
      */
     private String mustIn = "";
     /**
      * 限住几天
      */
     private Long restrictIn = 0L;
     /**
      * 连住天数 
      */
     private Long continueDay = 0L;
     /**
      * 必住日期间的关系 or and
      */
     private String continueDatesRelation = "";
     /**
      * 是否无条件担保
      */
     private String needAssure = "";
     /**
      * 配额数量
      */
     private Long quotanumber = 0L;
     
     /**
      * 能否预订--房态
      * 3 : 不许, 2 : 允许, 1 : 必须 
      */
     private String hasbook = "";
     /**
      * 能否透支
      */
     private String hasoverdraft = "";
     
     
     /**
      * 优惠数(连住,打折,住M送N)
      */
     private Double favourableNumber = 0.0;
     /**
      * 是否返现
      */
       private boolean hasReturnCash;
       /**
        * 返现金额
        */
       private Double returnCash = 0d;
       /**
        * 返现币种
        */
       
       private String cashCurrency = "";
       
       /**
        * 是否立减
        */
       private boolean haspromptlyReduce;
       /**
        * 立减金额
        */
       private Double romptlyReduce = 0d;
       
       /**
        * 立减币种
        */
       private String reduceCurrency = "";
       
       /**
        * 是否有连住优化的标志
        */
       private boolean favourableFlag;
       
    /**
     * 是否有免费宽带 
     */
    private boolean freeNet;

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public Long getDistId() {
		return distId;
	}

	public void setDistId(Long distId) {
		this.distId = distId;
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

	public String getMembertype() {
		return membertype;
	}

	public void setMembertype(String membertype) {
		this.membertype = membertype;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getCloseflag() {
		return closeflag;
	}

	public void setCloseflag(String closeflag) {
		this.closeflag = closeflag;
	}

	public String getClosereason() {
		return closereason;
	}

	public void setClosereason(String closereason) {
		this.closereason = closereason;
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

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCommodityNo() {
		return commodityNo;
	}

	public void setCommodityNo(String commodityNo) {
		this.commodityNo = commodityNo;
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

	public String getRoomtypeName() {
		return roomtypeName;
	}

	public void setRoomtypeName(String roomtypeName) {
		this.roomtypeName = roomtypeName;
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

	public Double getSalesroomprice() {
		return salesroomprice;
	}

	public void setSalesroomprice(Double salesroomprice) {
		this.salesroomprice = salesroomprice;
	}

	public Long getBreakfasttype() {
		return breakfasttype;
	}

	public void setBreakfasttype(Long breakfasttype) {
		this.breakfasttype = breakfasttype;
	}

	public Long getBreakfastnumber() {
		return breakfastnumber;
	}

	public void setBreakfastnumber(Long breakfastnumber) {
		this.breakfastnumber = breakfastnumber;
	}

	public Double getBreakfastprice() {
		return breakfastprice;
	}

	public void setBreakfastprice(Double breakfastprice) {
		this.breakfastprice = breakfastprice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Long getDealerFavourableId() {
		return dealerFavourableId;
	}

	public void setDealerFavourableId(Long dealerFavourableId) {
		this.dealerFavourableId = dealerFavourableId;
	}

	public Long getDealerPromotionsaleId() {
		return dealerPromotionsaleId;
	}

	public void setDealerPromotionsaleId(Long dealerPromotionsaleId) {
		this.dealerPromotionsaleId = dealerPromotionsaleId;
	}

	public Long getProviderFavourableId() {
		return providerFavourableId;
	}

	public void setProviderFavourableId(Long providerFavourableId) {
		this.providerFavourableId = providerFavourableId;
	}

	public Long getProviderPromotionsaleId() {
		return providerPromotionsaleId;
	}

	public void setProviderPromotionsaleId(Long providerPromotionsaleId) {
		this.providerPromotionsaleId = providerPromotionsaleId;
	}

	public Long getBookclauseId() {
		return bookclauseId;
	}

	public void setBookclauseId(Long bookclauseId) {
		this.bookclauseId = bookclauseId;
	}

	public String getPaytoprepay() {
		return paytoprepay;
	}

	public void setPaytoprepay(String paytoprepay) {
		this.paytoprepay = paytoprepay;
	}

	public Date getBookstartdate() {
		return bookstartdate;
	}

	public void setBookstartdate(Date bookstartdate) {
		this.bookstartdate = bookstartdate;
	}

	public Date getBookenddate() {
		return bookenddate;
	}

	public void setBookenddate(Date bookenddate) {
		this.bookenddate = bookenddate;
	}

	public String getMorningtime() {
		return morningtime;
	}

	public void setMorningtime(String morningtime) {
		this.morningtime = morningtime;
	}

	public String getEveningtime() {
		return eveningtime;
	}

	public void setEveningtime(String eveningtime) {
		this.eveningtime = eveningtime;
	}

	public Date getContinuumInEnd() {
		return continuumInEnd;
	}

	public void setContinuumInEnd(Date continuumInEnd) {
		this.continuumInEnd = continuumInEnd;
	}

	public Date getContinuumInStart() {
		return continuumInStart;
	}

	public void setContinuumInStart(Date continuumInStart) {
		this.continuumInStart = continuumInStart;
	}

	public String getMustIn() {
		return mustIn;
	}

	public void setMustIn(String mustIn) {
		this.mustIn = mustIn;
	}

	public Long getRestrictIn() {
		return restrictIn;
	}

	public void setRestrictIn(Long restrictIn) {
		this.restrictIn = restrictIn;
	}

	public Long getContinueDay() {
		return continueDay;
	}

	public void setContinueDay(Long continueDay) {
		this.continueDay = continueDay;
	}

	public String getContinueDatesRelation() {
		return continueDatesRelation;
	}

	public void setContinueDatesRelation(String continueDatesRelation) {
		this.continueDatesRelation = continueDatesRelation;
	}

	public String getNeedAssure() {
		return needAssure;
	}

	public void setNeedAssure(String needAssure) {
		this.needAssure = needAssure;
	}

	public Long getQuotanumber() {
		return quotanumber;
	}

	public void setQuotanumber(Long quotanumber) {
		this.quotanumber = quotanumber;
	}

	public String getHasbook() {
		return hasbook;
	}

	public void setHasbook(String hasbook) {
		this.hasbook = hasbook;
	}

	public String getHasoverdraft() {
		return hasoverdraft;
	}

	public void setHasoverdraft(String hasoverdraft) {
		this.hasoverdraft = hasoverdraft;
	}

	public boolean isHasReturnCash() {
		return hasReturnCash;
	}

	public void setHasReturnCash(boolean hasReturnCash) {
		this.hasReturnCash = hasReturnCash;
	}

	public Double getReturnCash() {
		return returnCash;
	}

	public void setReturnCash(Double returnCash) {
		this.returnCash = returnCash;
	}

	public String getCashCurrency() {
		return cashCurrency;
	}

	public void setCashCurrency(String cashCurrency) {
		this.cashCurrency = cashCurrency;
	}

	public boolean isHaspromptlyReduce() {
		return haspromptlyReduce;
	}

	public void setHaspromptlyReduce(boolean haspromptlyReduce) {
		this.haspromptlyReduce = haspromptlyReduce;
	}

	public Double getRomptlyReduce() {
		return romptlyReduce;
	}

	public void setRomptlyReduce(Double romptlyReduce) {
		this.romptlyReduce = romptlyReduce;
	}

	public String getReduceCurrency() {
		return reduceCurrency;
	}

	public void setReduceCurrency(String reduceCurrency) {
		this.reduceCurrency = reduceCurrency;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public Double getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(Double commissionRate) {
		this.commissionRate = commissionRate;
	}

	public Double getFavourableNumber() {
		return favourableNumber;
	}

	public void setFavourableNumber(Double favourableNumber) {
		this.favourableNumber = favourableNumber==null?0:favourableNumber;
		
	}

	public boolean isFavourableFlag() {
		return favourableFlag;
	}

	public void setFavourableFlag(boolean favourableFlag) {
		this.favourableFlag = favourableFlag;
	}

	public boolean isFreeNet() {
		return freeNet;
	}

	public void setFreeNet(boolean freeNet) {
		this.freeNet = freeNet;
	}

	public String getCantbookReason() {
		return cantbookReason;
	}

	public void setCantbookReason(String cantbookReason) {
		this.cantbookReason = cantbookReason;
	}

	public Double getAdvicePrice() {
		return advicePrice;
	}

	public void setAdvicePrice(Double advicePrice) {
		this.advicePrice = advicePrice;
	}

   
}