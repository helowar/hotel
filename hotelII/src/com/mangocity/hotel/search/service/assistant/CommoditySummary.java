package com.mangocity.hotel.search.service.assistant;


/**
 * 
 * 商品的一些统计信息
 * 
 * 	此时commodity的以下属性已经有值:
	  	curComm.setHdltype(curInfo.getHdltype()); // 直连类型
	  	comm.setShow(isshow); // 商品是否显示
	  	
		// 商品能否预订
	comm.setCantbookReason(getCantBookReasonByCommodity(cantbookReason));
	comm.setCanBook(canbook);
 * 
 * 
 * @author chenkeming
 *
 */
public class CommoditySummary {
	
	/**
	 * 直联方式
	 */
	private String hdltype;

	/**
	 * 是否显示
	 */
	private boolean show;
	
	/**
	 * 能否预订
	 */
	private boolean canBook;
	
	
	/**
	 * 不能预订原因
	 */
	private String cantbookReason;
	
	/**
	 * 能预订的床型
	 */
	private String bedType;
	
	
	/**
	 * 宽带信息
	 */
	private String bandInfo;
	
	/**
	 * 该价格类型的最低价,用于排序
	 */
	private double minPirceRoomType;	


	public String getHdltype() {
		return hdltype;
	}


	public void setHdltype(String hdltype) {
		this.hdltype = hdltype;
	}


	public boolean isShow() {
		return show;
	}


	public void setShow(boolean show) {
		this.show = show;
	}


	public boolean isCanBook() {
		return canBook;
	}


	public void setCanBook(boolean canBook) {
		this.canBook = canBook;
	}


	public String getCantbookReason() {
		return cantbookReason;
	}


	public void setCantbookReason(String cantbookReason) {
		this.cantbookReason = cantbookReason;
	}


	public String getBedType() {
		return bedType;
	}


	public void setBedType(String bedType) {
		this.bedType = bedType;
	}


	public String getBandInfo() {
		return bandInfo;
	}


	public void setBandInfo(String bandInfo) {
		this.bandInfo = bandInfo;
	}


	public double getMinPirceRoomType() {
		return minPirceRoomType;
	}


	public void setMinPirceRoomType(double minPirceRoomType) {
		this.minPirceRoomType = minPirceRoomType;
	}
	
}
