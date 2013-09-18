package com.mangocity.ep.entity;

public class RequestParam {
   
	private String orderCd;
	
	private String hotelName;
	
	private String hotelstatus;
	
	private String ccstatus;
	
    private Integer totalSum;
	
	private Integer pageNo;
	
    private Integer perPageSize=12;
	
	public String getOrderCd() {
		return orderCd;
	}

	public void setOrderCd(String orderCd) {
		this.orderCd = orderCd;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getHotelstatus() {
		return hotelstatus;
	}

	public void setHotelstatus(String hotelstatus) {
		this.hotelstatus = hotelstatus;
	}

	public String getCcstatus() {
		return ccstatus;
	}

	public void setCcstatus(String ccstatus) {
		this.ccstatus = ccstatus;
	}

	public Integer getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(Integer totalSum) {
		this.totalSum = totalSum;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPerPageSize() {
		return perPageSize;
	}

	public void setPerPageSize(Integer perPageSize) {
		this.perPageSize = perPageSize;
	}
	
	
}
