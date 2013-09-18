package com.mangocity.hotel.search.vo;

import java.util.Date;

/**
 * 
 * 酒店网站v2.2查询结果<br>
 * 价格明细类<br>
 * 
 * @author zengyong
 * 
 */
public class SaleItemVO implements SerializableVO {
	
	public SaleItemVO(){}
	
	private static final long serialVersionUID = 4884058131631491381L;

    private long saleItemId; 

    /**
     * 销售价
     */
    private double salePrice;
    
    /**
     * 界面显示销售价
     */
    private String salePriceStr;
    
    /**
     * 门市价
     */
    private double saleRoomPrice;
    
    /**
     * 界面显示门市价
     */
    private String saleRoomPriceStr; 
    
    
    /**
     *  日期
     */
	private String ableDate;
	
	/**
	 * Date类型的销售日期
	 */
	private Date realDate;
	    
    // 日期所对应的星期(主要用于展示)
    private String weekDay;

    // 早餐类型
    private String breakfastType;

    // 早餐数量
    private int breakfastNum;


    // 能否预定
    private int canBook;


    //币种
    private String currency;
    
    //币种符号
    private String currencySymbol;
    
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
	/**
	 * 免费宽带
	 */
	private boolean freeNet;
	  /**
     * 是否可以面转预 （可以，必须，不需）
     */
    private String payToprepayType;
	
	/**
	 * 床型 
	 */
	private String bedtype = "";//大，双，单 example:"1,2,3"
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
    private boolean showPrice = true;
    
    
		
	/**
     * 优惠(连住,打折)金额
     */
    private double favourableByClause;
    
    /**
     * 房态字符串
     */
    private String roomstateStr = "";
    /**
     * 配额字符串
     */
    private String quotaStr = "";
    
    // 佣金
    private double commission = 0d;
    
	public long getSaleItemId() {
		return saleItemId;
	}
	public void setSaleItemId(long saleItemId) {
		this.saleItemId = saleItemId;
	}
	public double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}
	public String getAbleDate() {
		return ableDate;
	}
	public void setAbleDate(String ableDate) {
		this.ableDate = ableDate;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public String getBreakfastType() {
		return breakfastType;
	}
	public void setBreakfastType(String breakfastType) {
		this.breakfastType = breakfastType;
	}
	public int getBreakfastNum() {
		return breakfastNum;
	}
	public void setBreakfastNum(int breakfastNum) {
		this.breakfastNum = breakfastNum;
	}
	public int getCanBook() {
		return canBook;
	}
	public void setCanBook(int canBook) {
		this.canBook = canBook;
	}
	public boolean isHasBenifit() {
		return hasBenifit;
	}
	public void setHasBenifit(boolean hasBenifit) {
		this.hasBenifit = hasBenifit;
	}
	public double getBenifitAmount() {
		return benifitAmount;
	}
	public void setBenifitAmount(double benifitAmount) {
		this.benifitAmount = benifitAmount;
	}
	public String getHdltype() {
		return hdltype;
	}
	public void setHdltype(String hdltype) {
		this.hdltype = hdltype;
	}
	public boolean isHasReturnCash() {
		return hasReturnCash;
	}
	public void setHasReturnCash(boolean hasReturnCash) {
		this.hasReturnCash = hasReturnCash;
	}
	public double getReturnAmount() {
		return returnAmount;
	}
	public void setReturnAmount(double returnAmount) {
		this.returnAmount = returnAmount;
	}
	public boolean isFreeNet() {
		return freeNet;
	}
	public void setFreeNet(boolean freeNet) {
		this.freeNet = freeNet;
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
	public boolean isShowPrice() {
		return showPrice;
	}
	public void setShowPrice(boolean showPrice) {
		this.showPrice = showPrice;
	}
	public double getFavourableByClause() {
		return favourableByClause;
	}
	public void setFavourableByClause(double favourableByClause) {
		this.favourableByClause = favourableByClause;
	}
	public String getRoomstateStr() {
		return roomstateStr;
	}
	public void setRoomstateStr(String roomstateStr) {
		this.roomstateStr = roomstateStr;
	}
	public String getQuotaStr() {
		return quotaStr;
	}
	public void setQuotaStr(String quotaStr) {
		this.quotaStr = quotaStr;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public String getSalePriceStr() {
		return salePriceStr;
	}
	public void setSalePriceStr(String salePriceStr) {
		this.salePriceStr = salePriceStr;
	}
	public double getSaleRoomPrice() {
		return saleRoomPrice;
	}
	public void setSaleRoomPrice(double saleRoomPrice) {
		this.saleRoomPrice = saleRoomPrice;
	}
	public String getSaleRoomPriceStr() {
		return saleRoomPriceStr;
	}
	public void setSaleRoomPriceStr(String saleRoomPriceStr) {
		this.saleRoomPriceStr = saleRoomPriceStr;
	}
	public Date getRealDate() {
		return realDate;
	}
	public void setRealDate(Date realDate) {
		this.realDate = realDate;
	}
	public double getCommission() {
		return commission;
	}
	public void setCommission(double commission) {
		this.commission = commission;
	}

}
