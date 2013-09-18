package com.mangocity.tmchotel.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * 
 * 日期 星期 价格 TMC-V2.0
 * add by shengwei.zuo 
 */

public class TmcWeekPriceUtil {
	
	//日期字符串
	private String dateStr;
	//星期字符串
	private String weekStr;
	//每天价格
	private Double everDayPrice;
	//每天价格的字符串
	private String everDayPriceStr;
	
	  /**
     * 含早数量,如果数量为0，表示不含早.
     */
    private String dayBreakfastNumber;
    
    /**
     * 是否有免费宽带 1:有，2：宽带收费；3：无
     */
    private String dayFreeNet;
    
    /**
     * 是否显示多余的日期空格
     */
    private boolean showKongge;
	
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getWeekStr() {
		return weekStr;
	}
	public void setWeekStr(String weekStr) {
		this.weekStr = weekStr;
	}
	public Double getEverDayPrice() {
		return everDayPrice;
	}
	public void setEverDayPrice(Double everDayPrice) {
		this.everDayPrice = everDayPrice;
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
	public String getEverDayPriceStr() {
		return everDayPriceStr;
	}
	public void setEverDayPriceStr(String everDayPriceStr) {
		this.everDayPriceStr = everDayPriceStr;
	}
	public boolean isShowKongge() {
		return showKongge;
	}
	public void setShowKongge(boolean showKongge) {
		this.showKongge = showKongge;
	}

}