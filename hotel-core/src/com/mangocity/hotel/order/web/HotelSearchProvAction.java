package com.mangocity.hotel.order.web;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.web.webwork.GenericAction;

/**
 * CC前台查询中航信等供应商的酒店信息相关
 * 
 * @author chenkeming
 * 
 */
public class HotelSearchProvAction extends GenericAction {
	
	private static final long serialVersionUID = 3966611547915151504L;

	private long channelType;
	
	private Long hotelId;
	
	private String checkInDate;
	
	private String checkOutDate;
	
	private int sortType;
	
	/**
	 * 
	 * 查询各供应商提供的酒店详细信息
	 * 
	 * @return
	 */
	public String search() {
				
		if(ChannelType.CHANNEL_ZHX == channelType) { // 中航信
			
		}
		
		return "srchProv";
	}

	public String getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}

	public String getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public int getSortType() {
		return sortType;
	}

	public void setSortType(int sortType) {
		this.sortType = sortType;
	}

	public long getChannelType() {
		return channelType;
	}

	public void setChannelType(long channelType) {
		this.channelType = channelType;
	}

}
