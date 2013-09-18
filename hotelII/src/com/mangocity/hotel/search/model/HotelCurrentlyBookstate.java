package com.mangocity.hotel.search.model;

import java.util.Date;

public class HotelCurrentlyBookstate {
	/**
	 * 酒店id
	 */
	protected long hotelId;
	/**
	 * 销售渠道
	 */
	protected String channel;
	/**
	 * 开始日期
	 */
	protected Date startDate;
	/**
	 * 结束日期
	 */
	protected Date endDate;
	/**
	 * 最低价格
	 */
	protected double minprice;
	/**
	 * 最高价格
	 */
	protected double maxprice;
	/**
	 * 是否含早
	 */
	protected boolean hasBreakfast;
	/**
	 * 能否预订
	 */
	protected boolean canBook;
	/**
	 * 是否显示
	 */
	protected boolean isDisplay;

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getMinprice() {
		return minprice;
	}

	public void setMinprice(double minprice) {
		this.minprice = minprice;
	}

	public double getMaxprice() {
		return maxprice;
	}

	public void setMaxprice(double maxprice) {
		this.maxprice = maxprice;
	}

	public boolean isHasBreakfast() {
		return hasBreakfast;
	}

	public void setHasBreakfast(boolean hasBreakfast) {
		this.hasBreakfast = hasBreakfast;
	}

	public boolean isCanBook() {
		return canBook;
	}

	public void setCanBook(boolean canBook) {
		this.canBook = canBook;
	}

	public boolean isDisplay() {
		return isDisplay;
	}

	public void setDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
	}

	

}
