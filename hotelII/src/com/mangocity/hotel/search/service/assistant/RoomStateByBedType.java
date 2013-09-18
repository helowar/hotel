package com.mangocity.hotel.search.service.assistant;


public class RoomStateByBedType {
	
	/**
	 * 床型 1:大,2:双,3:单
	 */
	private int bedtype =0;
    /**
     * 配额数量
     */
    private long quotanumber =0;
    /**
     * 是否有房--房态(满房就没房了,否则就有房)
     */
    private boolean hasRoom;
    /**
     * 能否透支
     */
    private boolean hasoverdraft;
    
	public int getBedtype() {
		return bedtype;
	}
	public void setBedtype(int bedtype) {
		this.bedtype = bedtype;
	}
	public long getQuotanumber() {
		return quotanumber;
	}
	public void setQuotanumber(long quotanumber) {
		this.quotanumber = quotanumber;
	}
	public boolean isHasRoom() {
		return hasRoom;
	}
	public void setHasRoom(boolean hasRoom) {
		this.hasRoom = hasRoom;
	}
	public boolean isHasoverdraft() {
		return hasoverdraft;
	}
	public void setHasoverdraft(boolean hasoverdraft) {
		this.hasoverdraft = hasoverdraft;
	}
    
    
}
