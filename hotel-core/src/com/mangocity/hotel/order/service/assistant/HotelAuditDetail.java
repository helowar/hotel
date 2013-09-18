package com.mangocity.hotel.order.service.assistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HotelAuditDetail {
	/**
	 * 酒店名
	 */
	private String hotelName;

	/**
	 * 酒店Id
	 */
	private Long hotelId;

	/**
	 * 渠道Id
	 */
	private Long channelId;

	/**
	 * 渠道名称
	 */
	private String channelName;

	private List<DaOrderAudit> orderAudits = new ArrayList<DaOrderAudit>();
	
	private Map<String,DaOrderAudit> orderAuditsMap = new HashMap<String, DaOrderAudit>();
	/**
	 * 把MAP的信息转到LIST中,并且把MAP清空
	 *
	 */
	public void converMapToList() {
		for (Iterator<String> htlIdsIter = orderAuditsMap.keySet().iterator(); htlIdsIter
				.hasNext();) {
			String roomIndex = htlIdsIter.next();
			DaOrderAudit tempInfor = orderAuditsMap.get(roomIndex);
			orderAudits.add(tempInfor);
		}
		//清除map的信息
		orderAuditsMap.clear();
	}
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public List<DaOrderAudit> getOrderAudits() {
		return orderAudits;
	}

	public void setOrderAudits(List<DaOrderAudit> orderAudits) {
		this.orderAudits = orderAudits;
	}

	public Map<String, DaOrderAudit> getOrderAuditsMap() {
		return orderAuditsMap;
	}

	public void setOrderAuditsMap(Map<String, DaOrderAudit> orderAuditsMap) {
		this.orderAuditsMap = orderAuditsMap;
	}
}

