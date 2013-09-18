package com.mangocity.webnew.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * add by shengwei.zuo 
 */

public class HtlCalendarHelperBean {
	
	//第一个年份
	private  int caYear;
	
	//第二个年份
	private  int caNextYear;
	
	//第一个月份
	private  int caMonth;
	
	//第二个月份
	private  int caNextMonth;
	
	//第一个月的日期数组；
	private String Cadays[];
	
	//第二个月的日期数组；
	private String  CaNextDays[];
	
	//入住日期
	private String inDateStr;
	
	//离店日期
	private String outDateStr;
	
    // "hotelId"酒店ID
    private long hotelId;

    // "roomTypeId"房型ID
    private long roomTypeId;

    // "childRoomTypeId"子房型ID
    private long childRoomTypeId;

    // "payMethod"预付面付类型
    private String payMethod;

    // 配额类型
    private String quotaType;
    
    //有连住优惠更改后的list;
    List<HtlCalendarFav>  lstFavChgPri =  new  ArrayList<HtlCalendarFav>();

	public int getCaYear() {
		return caYear;
	}

	public void setCaYear(int caYear) {
		this.caYear = caYear;
	}

	public int getCaNextYear() {
		return caNextYear;
	}

	public void setCaNextYear(int caNextYear) {
		this.caNextYear = caNextYear;
	}

	public int getCaMonth() {
		return caMonth;
	}

	public void setCaMonth(int caMonth) {
		this.caMonth = caMonth;
	}

	public int getCaNextMonth() {
		return caNextMonth;
	}

	public void setCaNextMonth(int caNextMonth) {
		this.caNextMonth = caNextMonth;
	}

	public String[] getCadays() {
		return Cadays;
	}

	public void setCadays(String[] cadays) {
		Cadays = cadays;
	}

	public String[] getCaNextDays() {
		return CaNextDays;
	}

	public void setCaNextDays(String[] caNextDays) {
		CaNextDays = caNextDays;
	}

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}

	public long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public long getChildRoomTypeId() {
		return childRoomTypeId;
	}

	public void setChildRoomTypeId(long childRoomTypeId) {
		this.childRoomTypeId = childRoomTypeId;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getQuotaType() {
		return quotaType;
	}

	public void setQuotaType(String quotaType) {
		this.quotaType = quotaType;
	}

	public String getInDateStr() {
		return inDateStr;
	}

	public void setInDateStr(String inDateStr) {
		this.inDateStr = inDateStr;
	}

	public String getOutDateStr() {
		return outDateStr;
	}

	public void setOutDateStr(String outDateStr) {
		this.outDateStr = outDateStr;
	}

	public List<HtlCalendarFav> getLstFavChgPri() {
		return lstFavChgPri;
	}

	public void setLstFavChgPri(List<HtlCalendarFav> lstFavChgPri) {
		this.lstFavChgPri = lstFavChgPri;
	}

}