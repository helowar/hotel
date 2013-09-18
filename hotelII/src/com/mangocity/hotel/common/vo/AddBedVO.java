package com.mangocity.hotel.common.vo;

import com.mangocity.hotel.search.vo.SerializableVO;

public class AddBedVO implements SerializableVO {

	public AddBedVO(){}
	
	 // 加床价格id
    private long ID;

    // 起始日期
    private String beginDate;

    // 终止时期
    private String endDate;

    // 房型
    private String roomType;

    // 加床价
    private String addPrice;

    // 加床价类型
    private String addPriceType;

    private String remark;

    /**
     * 是否有效
     */
    private String active;
    /**
     * 支付方式
     */
    private String payMethod;
	public long getID() {
		return ID;
	}
	public void setID(long id) {
		ID = id;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getAddPrice() {
		return addPrice;
	}
	public void setAddPrice(String addPrice) {
		this.addPrice = addPrice;
	}
	public String getAddPriceType() {
		return addPriceType;
	}
	public void setAddPriceType(String addPriceType) {
		this.addPriceType = addPriceType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

}
