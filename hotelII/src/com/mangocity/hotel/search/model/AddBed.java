package com.mangocity.hotel.search.model;

import java.util.Date;


public class AddBed {

    // 加床价格id
    private Long ID;


    // 起始日期
    private Date beginDate;

    // 终止时期
    private Date endDate;

    // 房型
    private String roomType;

    // 加床价
    private Double addPrice;

    // 加床价类型
    private Long addPriceType;

    private String remark;

    /**
     * 是否有效
     */
    private String active;
    /**
     * 支付方式
     */
    private String payMethod;

    public Double getAddPrice() {
        return addPrice;
    }

    public void setAddPrice(Double addPrice) {
        this.addPrice = addPrice;
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

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Long getAddPriceType() {
        return addPriceType;
    }

    public void setAddPriceType(Long addPriceType) {
        this.addPriceType = addPriceType;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

}