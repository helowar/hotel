package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author zengyong
 * Mar 11, 2010,5:33:12 PM
 *描述:设置佣金率
 */
public class CommisionSet implements Cloneable,Serializable{
	
	/**
	 * 佣金ID 主键
	 */
	private Long  commID;
	/**
	 * B2B代理商CD
	 */
	private String b2BCd;
	
	/**
	 * 酒店星级 5,4,3,2分别代表五星,四星,三星,二星及以下和经济性酒店
	 */
	private String hotelStar;
	
	/**
	 * 设置单位,>=,==,<=,<=x<= 分别代表1,2,3,4
	 */
	private Long setType;
	
	/**
	 * 单位 1:%,2:固定值
	 */
	private Long valueUnit;
	
	/**
	 * 佣金值1
	 */
	private Double value1;
	/**
	 * 佣金值2
	 */	
	private Double value2;
	
	/**
	 * 最后修改者姓名
	 */
	private String modifyBy;
	/**
	 * 最后修改者ID
	 */
	private String modifyById;
	/**
	 * 最后修改日期
	 */
	private Date modifyDate;
	/**
	 * 创建者姓名
	 */
	private String createBy;
	/**
	 * 创建者ID
	 */
	private String createById;
	/**
	 * 创建日期
	 */
	private Date createDate;

	public Long getCommID() {
		return commID;
	}
	public void setCommID(Long commID) {
		this.commID = commID;
	}

	public String getB2BCd() {
		return b2BCd;
	}
	public void setB2BCd(String cd) {
		b2BCd = cd;
	}
	public String getHotelStar() {
		return hotelStar;
	}
	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}
	public Long getSetType() {
		return setType;
	}
	public void setSetType(Long setType) {
		this.setType = setType;
	}
	public Long getValueUnit() {
		return valueUnit;
	}
	public void setValueUnit(Long valueUnit) {
		this.valueUnit = valueUnit;
	}
	public Double getValue1() {
		return value1;
	}
	public void setValue1(Double value1) {
		this.value1 = value1;
	}
	public Double getValue2() {
		return value2;
	}
	public void setValue2(Double value2) {
		this.value2 = value2;
	}
	public String getModifyBy() {
		return modifyBy;
	}
	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}
	public String getModifyById() {
		return modifyById;
	}
	public void setModifyById(String modifyById) {
		this.modifyById = modifyById;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateById() {
		return createById;
	}
	public void setCreateById(String createById) {
		this.createById = createById;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	public Object clone() { 
		Object o = null; 
		try { 
		o = super.clone(); 
		} catch (CloneNotSupportedException e) {} 
		return o;

		}
	
	
}
