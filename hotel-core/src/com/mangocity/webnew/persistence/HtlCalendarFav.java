package com.mangocity.webnew.persistence;

import java.util.Date;

/**
 * 
 * 日历显示连住优惠更改后的价格
 * add by shengwei.zuo 
 */

public class HtlCalendarFav {
    
	//连住优惠的日期
	private Date favDate;
	
	
	//连住优惠的售价
	private double favPrice;


	public Date getFavDate() {
		return favDate;
	}


	public void setFavDate(Date favDate) {
		this.favDate = favDate;
	}


	public double getFavPrice() {
		return favPrice;
	}


	public void setFavPrice(double favPrice) {
		this.favPrice = favPrice;
	}

}