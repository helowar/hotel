package com.mangocity.hotel.search.service.assistant;

import java.util.ArrayList;
import java.util.List;

public class SpecialRequest {

	/**
	 * 是否包含宽带
	 */
	protected boolean containBroadBand;
	
	/**
	 * 是否包含早餐
	 */
	protected boolean containBreakfast;
	
	/**
	 * 是否最近开业装修
	 */
	protected boolean recentlyOpenedAndFit;
	
	/**
	 * 房间设施 对于htl_roomtype下的字段
	 */
	protected List<String> listRoomEquipment ;
	
	/**
	 * 餐饮休闲设施
	 */
	protected List<String> listMealFixtrue;
	
	/**
	 * 免费设施
	 */
	protected List<String> freeService;	

	public List<String> getListRoomEquipment() {
		if (null == listRoomEquipment) {
			listRoomEquipment = new ArrayList<String>();
		}
		return listRoomEquipment;
	}

	public void setListRoomEquipment(List<String> listRoomEquipment) {
		this.listRoomEquipment = listRoomEquipment;
	}

	public boolean isContainBroadBand() {
		return containBroadBand;
	}

	public void setContainBroadBand(boolean containBroadBand) {
		this.containBroadBand = containBroadBand;
	}

	public boolean isContainBreakfast() {
		return containBreakfast;
	}

	public void setContainBreakfast(boolean containBreakfast) {
		this.containBreakfast = containBreakfast;
	}

	public List<String> getListMealFixtrue() {
		if (null == listMealFixtrue) {
			listMealFixtrue = new ArrayList<String>();
		}
		return listMealFixtrue;
	}

	public void setListMealFixtrue(List<String> listMealFixtrue) {
		this.listMealFixtrue = listMealFixtrue;
	}

	public List<String> getFreeService() {
		if (null == freeService) {
			freeService = new ArrayList<String>();
		}
		return freeService;
	}

	public void setFreeService(List<String> freeService) {
		this.freeService = freeService;
	}

	public boolean isRecentlyOpenedAndFit() {
		return recentlyOpenedAndFit;
	}

	public void setRecentlyOpenedAndFit(boolean recentlyOpenedAndFit) {
		this.recentlyOpenedAndFit = recentlyOpenedAndFit;
	}


}
