package com.mangocity.hotel.search.sort;



/**
 * 
 * 仅用于排序的Commodity VO类，可被各个客户端Commodity VO继承
 * 
 * @author chenkeming
 *
 */
public abstract class SortedCommodity implements Comparable<SortedCommodity> {
	
	 
	/**
	 * 该价格类型是否能预订,用于排序
	 */
	protected boolean canBookRoomType;
	
	/**
	 * 该价格类型的最低价,用于排序
	 */
	protected double minPirceRoomType;
	
	/**
	 * 支付方式
	 */
	protected String payMethod;
	
	public int compareTo(SortedCommodity o) {	
		
		int c1book = this.isCanBookRoomType() == true ? 1 : 0;
		int c2book = o.isCanBookRoomType() == true ? 1 : 0;
		if (c1book > c2book) {
			return -1;
		} else if (c1book < c2book) {
			return 1;
		}

		if (this.getMinPirceRoomType() > o.getMinPirceRoomType()) {
			return 1;
		} else if (this.getMinPirceRoomType() < o.getMinPirceRoomType()) {
			return -1;
		}
		return 0;
	}

	public boolean isCanBookRoomType() {
		return canBookRoomType;
	}

	public void setCanBookRoomType(boolean canBookRoomType) {
		this.canBookRoomType = canBookRoomType;
	}

	public double getMinPirceRoomType() {
		return minPirceRoomType;
	}

	public void setMinPirceRoomType(double minPirceRoomType) {
		this.minPirceRoomType = minPirceRoomType;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	
}
