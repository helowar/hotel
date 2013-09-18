package com.mangocity.webnew.persistence;

import java.util.Date;


/**
 * add by shengwei.zuo 
 */

public class HtlCalendarColsPriceBean {
		
	//天数ID
  	private int daysId;
	
	//每一天的CSS
    private String daysCss;
    
    private String  dayIndex;
	
	//每一天的价格
    private double dayPrice;
    
    //每一天的价格 字符串 add by shengwei.zuo 
    private String dayPriceStr;
    
    /**
     * 含早数量,如果数量为0，表示不含早.
     */
    private String dayBreakfastNumber;
    
    /**
     * 是否有免费宽带 1:有，2：宽带收费；3：无
     */
    private String dayFreeNet;

    private Date currDate;

	public int getDaysId() {
		return daysId;
	}

	public void setDaysId(int daysId) {
		this.daysId = daysId;
	}

	public String getDaysCss() {
		return daysCss;
	}

	public void setDaysCss(String daysCss) {
		this.daysCss = daysCss;
	}



	public String getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(String dayIndex) {
		this.dayIndex = dayIndex;
	}

	public double getDayPrice() {
		return dayPrice;
	}

	public void setDayPrice(double dayPrice) {
		this.dayPrice = dayPrice;
	}

	public String getDayBreakfastNumber() {
		return dayBreakfastNumber;
	}

	public void setDayBreakfastNumber(String dayBreakfastNumber) {
		this.dayBreakfastNumber = dayBreakfastNumber;
	}

	public String getDayFreeNet() {
		return dayFreeNet;
	}

	public void setDayFreeNet(String dayFreeNet) {
		this.dayFreeNet = dayFreeNet;
	}

	public String getDayPriceStr() {
		return dayPriceStr;
	}

	public void setDayPriceStr(String dayPriceStr) {
		this.dayPriceStr = dayPriceStr;
	}

	public Date getCurrDate() {
		return currDate;
	}

	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}

}