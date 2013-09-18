package com.mangocity.hotel.search.sort;

/**
 * 
 * 仅用于排序的RoomTypeInfo VO类，可被各个客户端RoomTypeInfo VO继承
 * 
 * @author chenkeming
 *
 */
public abstract class SortedRoomTypeInfo implements Comparable<SortedRoomTypeInfo> {
	
	/**
	 * 能否预订
	 */
	protected boolean canbook;
	
    /**
     * 推荐级别
     */
	protected String recommend;
    
	/**
	 * 是否基础房型
	 */	
	protected boolean baseRoom;
	
	/**
	 * 最低价
	 */
	private double minPrice;
	 
	public int compareTo(SortedRoomTypeInfo o) {	
		
		// 是否可预订
		int r1int = this.isCanbook() ? 0 : 1;
		int r2int = o.isCanbook() ? 0 : 1;
		if (r1int != r2int) {
			return r1int - r2int;
		} 
		
		// 最低价
		if (this.getMinPrice() <= o.getMinPrice()) {
			return -1;
		} else {
			return 1;
		}
		
		// 主推级别
		/*r1int = false == isValidStr(this.getRecommend()) ? 0
				: Integer.parseInt(this.getRecommend());
		r2int = false == isValidStr(o.getRecommend()) ? 0 : Integer
				.parseInt(o.getRecommend());
		if (r1int > r2int) {
			return 1;
		} else if (r1int < r2int) {
			return -1;
		}*/
		
		// 是否基础房型
		/*if (this.isBaseRoom() && o.isBaseRoom()) {
			return 0;
		} else if (!this.isBaseRoom() && !o.isBaseRoom()) {
			return 0;
		} else if (this.isBaseRoom()) {
			return 1;
		} else {
			return -1;
		}*/		
		
	}
	
	/*private boolean isValidStr(String str){
		 return null != str && 0 < str.trim().length();
	}*/
	

	public boolean isCanbook() {
		return canbook;
	}

	public void setCanbook(boolean canbook) {
		this.canbook = canbook;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public boolean isBaseRoom() {
		return baseRoom;
	}

	public void setBaseRoom(boolean baseRoom) {
		this.baseRoom = baseRoom;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	
}
