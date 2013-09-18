package com.mangocity.hotel.search.service.assistant;


/**
 * 
 * 房型的一些预订统计信息
 * 
 * 	  		curRoom.setCanbook(true); // 是否可订
			curRoom.setMinPrice(curComm.getMinPirceRoomType()); // 最低价
 * 
 * 
 * @author chenkeming
 *
 */
public class RoomTypeSummaryInfo {

	/**
	 * 能否预订
	 */
	private boolean canbook;	
	
	/**
	 * 最低价
	 */
	private double minPrice;

	public boolean isCanbook() {
		return canbook;
	}

	public void setCanbook(boolean canbook) {
		this.canbook = canbook;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	
}
