package com.mangocity.hotel.search.service.assistant;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SaleInfo {

	/**
	 * 会员类型
	 */
	private String memberType;
	/**
	 * 用户类型
	 */
	private String userType;
	
	/**
	 * 商品编码
	 */
	private String commodityNo;
	
	/**
	 * 价格ID
	 */
	private long priceId;
	/**
	 * 日期
	 */
	private Date ableDate;
	/**
	 * 门市价
	 */
	private double fullPrice;

	/**
	 * 销售价
	 */
	private double salePrice;
	
	/**
	 * 支付方式
	 */
    private String payMethod;
    /**
     * 是否优惠立减
     */
    
    private boolean hasBenifit;
    /**
	 * 优惠立减金额
	 */
	private double benifitAmount = 0;
	
	/**
	 * 直联方式
	 */
	private String hdltype;
	
	
	/**
	 * 是否返现
	 */
	private boolean hasReturnCash;
	/**
	 * 返现
	 */
	private double returnAmount = 0.0;
	
	// --------- 早餐相关start -------
	private double breakfastPrice;
	/**
	 * 房间含早类型
	 */
	private int breakfastType;

	/**
	 * 含早数量
	 */
	private int breakfastNum;
	// --------- 早餐相关end --------
	

	/**
	 * 币种
	 */
	private String currency;//币种
	
	/**
	 * 商家优惠id(新模型)
	 */
	private long dealerFavourableId;
	/**
	 * 商家促销Id(新模型)
	 */
	private long dealerPromotionsaleId;
	/**
	 * 供应商优惠(新模型)
	 */
	private long providerFavourableId;
	/**
	 * 供应商促销(新模型)
	 */
	private long providerPromotionsaleId;
	/**
	 * 预订条款Id
	 */
	private long bookclauseId;
		
	  /**
     * 是否可以面转预 （可以，必须，不需）
     */
    private String payToprepayType;
	
	/**
	 * 床型
	 */
	private String bedtype = "";//大，双，单
    /**
     * 是否需要担保
     */
    private boolean needAssure;
    
    /**
     * 关房标记
     */
    private String closeflag;
    /**
     * 关房原因
     */
    private String closereason;
    
    /**是否显示价格
     * 如果关房，根据关房原因判断是否显示价格<br>
     * 因变价原因关房时，不显示价格<br>
     */
    private boolean showPrice;
    
    
	/**
	 * 否满足预订条款
	 */
	private boolean satisfyClause;
	
	/**
	 * 不满足预订条款原因
	 */
	private String notsatisfyClauseOfReason;
	
	/**
     * 优惠(连住,打折)金额
     */
    private double favourableByClause;
	/**
	 * 是否可预订
	 */
	private boolean bookEnAble;
	
	/**
	 * 不可预订原因是否可预订
	 */
	private String reasonOfDisableBook;
	
	/**
	 * 房态和配额列表
	 */
	private Map<String, RoomStateByBedType> roomstateMaps;
	
	//酒店给芒果的佣金
	private Double commission = 0d;

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCommodityNo() {
		return commodityNo;
	}

	public void setCommodityNo(String commodityNo) {
		this.commodityNo = commodityNo;
	}

	

	public long getPriceId() {
		return priceId;
	}

	public void setPriceId(long priceId) {
		this.priceId = priceId;
	}

	public Date getAbleDate() {
		return ableDate;
	}

	public void setAbleDate(Date ableDate) {
		this.ableDate = ableDate;
	}

	public double getFullPrice() {
		return fullPrice;
	}

	public void setFullPrice(double fullPrice) {
		this.fullPrice = fullPrice;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public double getBenifitAmount() {
		return benifitAmount;
	}

	public void setBenifitAmount(double benifitAmount) {
		this.benifitAmount = benifitAmount;
	}

	public double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(double returnAmount) {
		this.returnAmount = returnAmount;
	}

	public double getBreakfastPrice() {
		return breakfastPrice;
	}

	public void setBreakfastPrice(double breakfastPrice) {
		this.breakfastPrice = breakfastPrice;
	}

	

	public int getBreakfastNum() {
		return breakfastNum;
	}

	public void setBreakfastNum(int breakfastNum) {
		this.breakfastNum = breakfastNum;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public long getDealerFavourableId() {
		return dealerFavourableId;
	}

	public void setDealerFavourableId(long dealerFavourableId) {
		this.dealerFavourableId = dealerFavourableId;
	}

	public long getDealerPromotionsaleId() {
		return dealerPromotionsaleId;
	}

	public void setDealerPromotionsaleId(long dealerPromotionsaleId) {
		this.dealerPromotionsaleId = dealerPromotionsaleId;
	}

	public long getProviderFavourableId() {
		return providerFavourableId;
	}

	public void setProviderFavourableId(long providerFavourableId) {
		this.providerFavourableId = providerFavourableId;
	}

	public long getProviderPromotionsaleId() {
		return providerPromotionsaleId;
	}

	public void setProviderPromotionsaleId(long providerPromotionsaleId) {
		this.providerPromotionsaleId = providerPromotionsaleId;
	}

	public long getBookclauseId() {
		return bookclauseId;
	}

	public void setBookclauseId(long bookclauseId) {
		this.bookclauseId = bookclauseId;
	}

	public String getPayToprepayType() {
		return payToprepayType;
	}

	public void setPayToprepayType(String payToprepayType) {
		this.payToprepayType = payToprepayType;
	}

	public String getBedtype() {
		return bedtype;
	}

	public void setBedtype(String bedtype) {
		this.bedtype = bedtype;
	}

	public boolean isNeedAssure() {
		return needAssure;
	}

	public void setNeedAssure(boolean needAssure) {
		this.needAssure = needAssure;
	}


	public String getReasonOfDisableBook() {
		return reasonOfDisableBook;
	}

	public void setReasonOfDisableBook(String reasonOfDisableBook) {
		this.reasonOfDisableBook = reasonOfDisableBook;
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


	public Map<String, RoomStateByBedType> getRoomstateMaps() {
		if (null == roomstateMaps) {
			roomstateMaps = new HashMap<String, RoomStateByBedType>();
		}
		return roomstateMaps;
	}

	public void setRoomstateMaps(Map<String, RoomStateByBedType> roomstateMaps) {
		this.roomstateMaps = roomstateMaps;
	}

	public boolean isSatisfyClause() {
		return satisfyClause;
	}

	public void setSatisfyClause(boolean satisfyClause) {
		this.satisfyClause = satisfyClause;
	}

	public String getNotsatisfyClauseOfReason() {
		return notsatisfyClauseOfReason;
	}

	public void setNotsatisfyClauseOfReason(String notsatisfyClauseOfReason) {
		this.notsatisfyClauseOfReason = notsatisfyClauseOfReason;
	}

	public boolean isBookEnAble() {
		return bookEnAble;
	}

	public void setBookEnAble(boolean bookEnAble) {
		this.bookEnAble = bookEnAble;
	}



	public double getFavourableByClause() {
		return favourableByClause;
	}

	public void setFavourableByClause(double favourableByClause) {
		this.favourableByClause = favourableByClause;
	}

	public boolean isHasBenifit() {
		return hasBenifit;
	}

	public void setHasBenifit(boolean hasBenifit) {
		this.hasBenifit = hasBenifit;
	}

	public boolean isHasReturnCash() {
		return hasReturnCash;
	}

	public void setHasReturnCash(boolean hasReturnCash) {
		this.hasReturnCash = hasReturnCash;
	}

	public int getBreakfastType() {
		return breakfastType;
	}

	public void setBreakfastType(int breakfastType) {
		this.breakfastType = breakfastType;
	}

	public boolean isShowPrice() {
		return showPrice;
	}

	public void setShowPrice(boolean showPrice) {
		this.showPrice = showPrice;
	}

	public String getHdltype() {
		return hdltype;
	}

	public void setHdltype(String hdltype) {
		this.hdltype = hdltype;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}


}
