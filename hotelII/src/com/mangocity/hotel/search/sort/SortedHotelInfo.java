package com.mangocity.hotel.search.sort;



/**
 * 
 * 仅用于排序的HotelInfo VO类，可被各个客户端HotelInfo VO继承
 * 
 * @author chenkeming
 *
 */
public abstract class SortedHotelInfo implements Comparable<SortedHotelInfo> {
	
    /**
     * 排名
     */
    protected int sort;

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	 
	public int compareTo(SortedHotelInfo o) {		
		return (this.sort - o.sort);
	}
	
}
