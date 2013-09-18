package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 房控改版 动态排序 参数表
 * @author chenjiajie
 *
 */
public class HtlRoomcontrolSorting extends CEntity implements Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8147252532464237198L;

	/**
	 * 主键
	 */
	private Long ID;
	
	/**
	 * 列名
	 */
	private String columnName;
	
	/**
	 * 排序优先级 
	 */
	private Integer sorting;
	
	/**
	 * 排序的值
	 */
	private String value;
	
	/**
	 * 顺序或倒序排列 倒序 DESC
	 */
	private String sortingType;
	
	/**
	 * 最后修改人工号
	 */
	private String modifybyid;
	
	/**
	 * 最后修改人姓名
	 */
	private String modifybyname;
	
	/**
	 * 最后修改人时间
	 */
	private Date modifytime;
	
	/* getter and setter */
	
	public void setID(Long id) {
		ID = id;
	}

	public Long getID() {
		return ID;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getSorting() {
		return sorting;
	}

	public void setSorting(Integer sorting) {
		this.sorting = sorting;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSortingType() {
		return sortingType;
	}

	public void setSortingType(String sortingType) {
		this.sortingType = sortingType;
	}

	public String getModifybyid() {
		return modifybyid;
	}

	public void setModifybyid(String modifybyid) {
		this.modifybyid = modifybyid;
	}

	public String getModifybyname() {
		return modifybyname;
	}

	public void setModifybyname(String modifybyname) {
		this.modifybyname = modifybyname;
	}

	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}
	
}
