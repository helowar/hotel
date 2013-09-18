package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * @author zengyong
 * Mar 11, 2010,5:33:41 PM
 *描述:调整佣金率
 */
public class CommisionAdjust implements Serializable {
	/**
	 * 调整ID
	 */
	private Long adjustID;
	/**
	 * b2b代理商CD
	 */
	private String b2BCd;
	/**
	 * 支付方式 pay,pre_pay
	 */
	private String payType;
	/**
	 * 子房型ID(价格类型ID)
	 */
	private Long childRoomId;
	/**
	 * 房型ID
	 */
	private Long roomTypeId;
	/**
	 * 酒店ID
	 */
	private Long hotelId;
	/**
	 * 酒店星级
	 */
	private String hotelStar;
	/**
	 * 开始日期
	 */
	private Date startDate;
	/**
	 * 结束日期
	 */
	private Date endDate;
	/**
	 * 佣金值类型,百分比或固定值
	 */
	private Long valueType;
	/**
	 * 佣金值
	 */
	private Double comm_value;
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
	
	private String roomAndPricetypeTemp;//临时属性，是房型和价格类型的组合，如：111&&1234
	/**
	 * 创建日期
	 */
	private Date createDate;
	public Long getAdjustID() {
		return adjustID;
	}
	public void setAdjustID(Long adjustID) {
		this.adjustID = adjustID;
	}


	public String getB2BCd() {
		return b2BCd;
	}
	public void setB2BCd(String cd) {
		b2BCd = cd;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Long getChildRoomId() {
		return childRoomId;
	}
	public void setChildRoomId(Long childRoomId) {
		this.childRoomId = childRoomId;
	}
	public Long getRoomTypeId() {
		return roomTypeId;
	}
	public void setRoomTypeId(Long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
	public String getHotelStar() {
		return hotelStar;
	}
	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getValueType() {
		return valueType;
	}
	public void setValueType(Long valueType) {
		this.valueType = valueType;
	}
	public Double getComm_value() {
		return comm_value;
	}
	public void setComm_value(Double comm_value) {
		this.comm_value = comm_value;
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
	public String getRoomAndPricetypeTemp() {
		return roomAndPricetypeTemp;
	}
	public void setRoomAndPricetypeTemp(String roomAndPricetypeTemp) {
		this.roomAndPricetypeTemp = roomAndPricetypeTemp;
	}
	
}
