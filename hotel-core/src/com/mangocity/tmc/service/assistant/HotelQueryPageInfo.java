package com.mangocity.tmc.service.assistant;



import java.util.List;

/**
 * 酒店查询结果分页信息
 * @author bruce.yang
 *
 */

public class HotelQueryPageInfo {
	/**
	 * 本页查询结果
	 */
	private List hotelList;
	/**
	 * 所有查询结果xuyun
	 */
	private List allHotelList;
	/**
	 * 每页酒店数目
	 */
	private int pageSize = 10;
	/**
	 * 页码
	 */
	private int pageNo;
	/**
	 * 总页数
	 */
	private int totalPage;
	/**
	 * 模数xuyun
	 */
	private int residue;
	/**
	 * 总记录数
	 */
	private int totalRecord;
	/**
	 * 排序方式
	 * @return
	 * creater by xuyun
	 */
	private String sort;	
	/**
	 * 排序类别
	 * @return
	 */
	private String sortType;
	
	
	public List getHotelList() {
		return hotelList;
	}
	public void setHotelList(List hotelList) {
		this.hotelList = hotelList;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public List getAllHotelList() {
		return allHotelList;
	}
	public void setAllHotelList(List allHotelList) {
		this.allHotelList = allHotelList;
	}
	public int getResidue() {
		return residue;
	}
	public void setResidue(int residue) {
		this.residue = residue;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	
	

}
