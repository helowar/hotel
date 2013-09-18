package com.mangocity.hotel.order.service.assistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.order.persistence.DaDailyauditItem;

public class DaOrderAudit {
	/**
	 * 订单ID
	 */
	private Long orderId;

	/**
	 * 有一些订单信息
	 */
	private DaDailyauditItem auditItem;

	/**
	 * 房间信息集合
	 */
	private List<DaSubItem> roomInfos = new ArrayList<DaSubItem>();

	private Map<Integer, DaSubItem> roomInfosMap = new HashMap<Integer, DaSubItem>();
	/**
	 * 把MAP的信息转到LIST中,并且把MAP清空
	 *
	 */
	public void converMapToList() {
		for (Iterator<Integer> htlIdsIter = roomInfosMap.keySet().iterator(); htlIdsIter
				.hasNext();) {
			Integer roomIndex = htlIdsIter.next();
			DaSubItem tempInfor = roomInfosMap.get(roomIndex);
			roomInfos.add(tempInfor);
		}
		//清除map的信息
		roomInfosMap.clear();
	}

	public Map<Integer, DaSubItem> getRoomInfosMap() {
		return roomInfosMap;
	}

	public void setRoomInfosMap(Map<Integer, DaSubItem> roomInfosMap) {
		this.roomInfosMap = roomInfosMap;
	}

	public DaDailyauditItem getAuditItem() {
		return auditItem;
	}

	public void setAuditItem(DaDailyauditItem auditItem) {
		this.auditItem = auditItem;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public List<DaSubItem> getRoomInfos() {
		return roomInfos;
	}

	public void setRoomInfos(List<DaSubItem> roomInfos) {
		this.roomInfos = roomInfos;
	}

}
