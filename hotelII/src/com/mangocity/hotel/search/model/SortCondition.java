package com.mangocity.hotel.search.model;

import java.util.Date;

public class SortCondition {
	/**
	 * 城市代码（三字码）
	 */
	protected String cityCode;
	
	/**
	 * 入住日期
	 */
	protected Date inDate;

	/**
	 * 离店日期
	 */
	protected Date outDate;
	
	/**
	 * 地标位置的Id
	 */
	private Long geoId;



	/**
	 * 1推荐,2,价格,3,星级，默认是推荐
	 */
	protected int sorttype = 1;
	 /**
	  * 1代表升序,2代表降序,默认是推荐从高到底,价格从低到高,星级从高到底
	  */
	protected int sortUpOrDown = 1;
	/**
     * 多少条记录/页，默认15个酒店/页
     */
	protected int pageSize = 15;
    
    /**
     * 页码
     */
	protected int pageNo = 1;

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public int getSorttype() {
		return sorttype;
	}

	public void setSorttype(int sorttype) {
		this.sorttype = sorttype;
	}

	public int getSortUpOrDown() {
		return sortUpOrDown;
	}

	public void setSortUpOrDown(int sortUpOrDown) {
		this.sortUpOrDown = sortUpOrDown;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public Long getGeoId() {
		return geoId;
	}

	public void setGeoId(Long geoId) {
		this.geoId = geoId;
	}

	
}
