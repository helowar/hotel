package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * 优惠立减DWR返回所需参数实体
 */
public class HtlPreconcertInfo implements Serializable {
	
	//酒店ID
	 private long hotelId;
	 
	 //价格类型ID
	 private long priceTypeId;
	 
	 //预付条款开始日期对应的酒店支付方式
	 private String beginPayMethod;
	 
	 //预付条款结束日期对应的酒店支付方式
	 private String endPayMethod;
	 
	 
	 //预付条款每天日期
	 private Date validDate;
	 
	 //立减优惠拿出的酒店支付方式
	 private String payMethod; 
	 
	 //根据立减优惠拿出的酒店支付方式转换成中文（1 转换成面付，2 转换成预付）
	 private String payMethodName;
	 
	 private String priceTypeName;
	 
	 //立减优惠拿出的立减优惠开始日期
	 private String beginDate ;
	 
	 //立减优惠拿出的立减优惠结束日期
	 private String endDate;
	 
	 //用来判断对应时间段内是否能保存预付立减；
	 private boolean havePrypay = false;
	 
	//用来判断对应时间段内是否能保存面付立减；
	 private boolean havePay =false;
	 
	 private boolean checkIsPayMethod = true;

	public boolean isCheckIsPayMethod() {
		return checkIsPayMethod;
	}

	public void setCheckIsPayMethod(boolean checkIsPayMethod) {
		this.checkIsPayMethod = checkIsPayMethod;
	}

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}

	public long getPriceTypeId() {
		return priceTypeId;
	}

	public void setPriceTypeId(long priceTypeId) {
		this.priceTypeId = priceTypeId;
	}
	
	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public String getPriceTypeName() {
		return priceTypeName;
	}

	public void setPriceTypeName(String priceTypeName) {
		this.priceTypeName = priceTypeName;
	}

	public String getBeginPayMethod() {
		return beginPayMethod;
	}

	public void setBeginPayMethod(String beginPayMethod) {
		this.beginPayMethod = beginPayMethod;
	}

	public String getEndPayMethod() {
		return endPayMethod;
	}

	public void setEndPayMethod(String endPayMethod) {
		this.endPayMethod = endPayMethod;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayMethodName() {
		return payMethodName;
	}

	public void setPayMethodName(String payMethodName) {
		this.payMethodName = payMethodName;
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

	public boolean isHavePrypay() {
		return havePrypay;
	}

	public void setHavePrypay(boolean havePrypay) {
		this.havePrypay = havePrypay;
	}

	public boolean isHavePay() {
		return havePay;
	}

	public void setHavePay(boolean havePay) {
		this.havePay = havePay;
	}

}
