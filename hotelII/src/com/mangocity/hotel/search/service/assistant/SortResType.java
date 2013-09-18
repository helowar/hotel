package com.mangocity.hotel.search.service.assistant;



/**
 * 
 * 酒店查询结果辅助类
 * 
 * @author chenkeming
 *
 */
public class SortResType {
	
    /**
     * 酒店ID列表字符串
     */
    private String sortedHotelIdList;
    
    /**
     * 酒店数量
     */
    private int hotelCount;

	public String getSortedHotelIdList() {
		return sortedHotelIdList;
	}

	public void setSortedHotelIdList(String sortedHotelIdList) {
		this.sortedHotelIdList = sortedHotelIdList;
	}

	public int getHotelCount() {
		return hotelCount;
	}

	public void setHotelCount(int hotelCount) {
		this.hotelCount = hotelCount;
	}


}
