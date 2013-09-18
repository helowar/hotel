package com.mangocity.hagtb2b.persistence;

import java.util.Date;

/**
 * B2B代理  代理模板详细信息 add by zhijie.gu 2010-8-5
 * 
 */

public class HtlB2bTempComminfoItem implements java.io.Serializable {

    // Fields

    // Id
    private Long itemId;
    
    //模板id
    private Long tempId;
    
    // 酒店id
    private Long hotelId;
    
    // 房型id
    private Long roomtypeId;
    
    // 价格类型id
    private Long chileRoomtypeId;
    
    // 开始日期
    private Date beginDate;
    
    // 结束日期
    private Date endDate;

    // 佣金率
    private double comissionRate;

    // 是否有效
    private int active;

    // 创建人id
    private String createId;

    // 创建人中文名
    private String createName;

    // 创建时间
    private Date createTime;

    // 修改人ID
    private String modifyId;

    // 修改人名称
    private String modifyName;

    // 修改时间
    private Date modifyTime;
    
    private String payMethod;
    
    private int valueType;
    
    private String hotelStar;
    
    //查看佣金模板是使用的辅助字段
    private String hotelChnName;
    
    private String roomTypeName;
    
    private String roomAndPricetypeTemp;//临时属性，是房型和价格类型的组合，如：111&&1234

    public String getRoomAndPricetypeTemp() {
		return roomAndPricetypeTemp;
	}

	public void setRoomAndPricetypeTemp(String roomAndPricetypeTemp) {
		this.roomAndPricetypeTemp = roomAndPricetypeTemp;
	}

	/** default constructor */
    public HtlB2bTempComminfoItem() {
    }

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public Long getRoomtypeId() {
		return roomtypeId;
	}

	public void setRoomtypeId(Long roomtypeId) {
		this.roomtypeId = roomtypeId;
	}

	public Long getChileRoomtypeId() {
		return chileRoomtypeId;
	}

	public void setChileRoomtypeId(Long chileRoomtypeId) {
		this.chileRoomtypeId = chileRoomtypeId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getComissionRate() {
		return comissionRate;
	}

	public void setComissionRate(double comissionRate) {
		this.comissionRate = comissionRate;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyId() {
		return modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}

	public String getModifyName() {
		return modifyName;
	}

	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getTempId() {
		return tempId;
	}

	public void setTempId(Long tempId) {
		this.tempId = tempId;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public int getValueType() {
		return valueType;
	}

	public void setValueType(int valueType) {
		this.valueType = valueType;
	}

	public String getHotelStar() {
		return hotelStar;
	}

	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}

	public String getHotelChnName() {
		return hotelChnName;
	}

	public void setHotelChnName(String hotelChnName) {
		this.hotelChnName = hotelChnName;
	}

	public String getRoomTypeName() {
		return roomTypeName;
	}

	public void setRoomTypeName(String roomTypeName) {
		this.roomTypeName = roomTypeName;
	}

 
}