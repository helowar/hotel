package com.mangocity.hotel.dreamweb.displayvo;

public class BreakfastAndPriceItemVO {

	//子房型ID    
	private long priceId;

	//房间日期(MM-DD)
	private String fellowDate;

	//销售价    
	private double salePrice;

	// 显示页面的价格－－－如果是港币则为计算后的人民币价格
	private double rmbPrice;

	// 日期所对应的星期（周一，周二）
	private String weekDay;

	// 早餐类型
	private String breakfastType;

	// 早餐数量
	private int breakfastNum;
	
	//页面显示早餐的字符串（无早，双早）
	private String breakfastNumName;

	//是否在连住天数内，用于页面展示
	private boolean inTheDays;
	
	private String currencyStr;
	
	public long getPriceId() {
		return priceId;
	}

	public void setPriceId(long priceId) {
		this.priceId = priceId;
	}

	public String getFellowDate() {
		return fellowDate;
	}

	public void setFellowDate(String fellowDate) {
		this.fellowDate = fellowDate;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public double getRmbPrice() {
		return rmbPrice;
	}

	public void setRmbPrice(double rmbPrice) {
		this.rmbPrice = rmbPrice;
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


	public String getBreakfastNumName() {
		return breakfastNumName;
	}

	public void setBreakfastNumName(String breakfastNumName) {
		this.breakfastNumName = breakfastNumName;
	}

	public boolean isInTheDays() {
		return inTheDays;
	}

	public void setInTheDays(boolean inTheDays) {
		this.inTheDays = inTheDays;
	}

	public String getCurrencyStr() {
		return currencyStr;
	}

	public void setCurrencyStr(String currencyStr) {
		this.currencyStr = currencyStr;
	}

	
	
	
	
	

}
