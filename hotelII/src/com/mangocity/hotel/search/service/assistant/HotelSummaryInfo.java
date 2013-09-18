package com.mangocity.hotel.search.service.assistant;


/**
 * 
 *   酒店的预订统计信息类 
 * 
 * @author chenkeming
 *
 */
public class HotelSummaryInfo {
	
	/**
	 * 能否预订
	 */
	protected boolean canbook;
	
    /**
     * 最低价
     */
    protected double lowestPrice;

	public boolean isCanbook() {
		return canbook;
	}

	public void setCanbook(boolean canbook) {
		this.canbook = canbook;
	}

	public double getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(double lowestPrice) {
		this.lowestPrice = lowestPrice;
	}
	 
}
