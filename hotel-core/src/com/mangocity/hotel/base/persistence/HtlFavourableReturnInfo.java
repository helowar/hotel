/**
 * 
 */
package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 *  现金返还DWR返回所需参数封装实体
 *  @author xiaojun.xiong
 *
 */
public class HtlFavourableReturnInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//酒店ID
    private long hotelId;
    
    //价格类型ID
    private long priceTypeId;
    
    //现金返还开始日期对应的酒店的支付方式
    private String beginPayMethod;
    
    //现金返还结束日期对应的酒店的支付方式
    private String endPayMethod;
    
    //现金返还的每天日期
    private Date validDate;
    
    //现金返还的酒店支付方式
    private String payMethod;
    
    //根据现金返还拿出的酒店支付方式转换成中文（1 转换为面付，2 转换为预付）
    private String payMethodName;
    
    //价格类型名称
    private String priceTypeName;
    
    //现金返还拿出的开始日期
    private String beginDate;
    
    //现金返还拿出的结束日期
    private String endDate;
    
    //判断对应时间段内是否可以保存预付返现
    private boolean pryPay  = false;
    
    //判断对应时间段内是否可以保存面付返现
    private boolean pay = false;
    
    private boolean checkIsPayMethod = true;

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

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
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

	public String getPriceTypeName() {
		return priceTypeName;
	}

	public void setPriceTypeName(String priceTypeName) {
		this.priceTypeName = priceTypeName;
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

	public boolean isPryPay() {
		return pryPay;
	}

	public void setPryPay(boolean pryPay) {
		this.pryPay = pryPay;
	}

	public boolean isCheckIsPayMethod() {
		return checkIsPayMethod;
	}

	public void setCheckIsPayMethod(boolean checkIsPayMethod) {
		this.checkIsPayMethod = checkIsPayMethod;
	}

	public void setPay(boolean pay) {
		this.pay = pay;
	}

	public boolean isPay() {
		return pay;
	}
    
}
