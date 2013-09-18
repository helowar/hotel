package com.mangocity.tmchotel.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 辅助类 TMC-V2.0
 * add by shengwei.zuo 
 */

public class TmcDateWeekUtil {
	
	//总的返现金额
	private long allBashMoney;
	
	//日期，星期，价格显示的
	private List<TmcWeekPriceUtil>  lstWeekPrice = new ArrayList<TmcWeekPriceUtil>();

	public long getAllBashMoney() {
		return allBashMoney;
	}

	public void setAllBashMoney(long allBashMoney) {
		this.allBashMoney = allBashMoney;
	}

	public List<TmcWeekPriceUtil> getLstWeekPrice() {
		return lstWeekPrice;
	}

	public void setLstWeekPrice(List<TmcWeekPriceUtil> lstWeekPrice) {
		this.lstWeekPrice = lstWeekPrice;
	}

}