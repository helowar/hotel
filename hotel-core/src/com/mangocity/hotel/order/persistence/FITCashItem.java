/**
 * 
 */
package com.mangocity.hotel.order.persistence;

import java.util.Date;

/**
 * 订单返现记录
 * @author xiongxiaojun
 *
 */
public class FITCashItem {
    private long ID;
    private String orderCd;
    private double returnCash;
    private double returnScale;
    private Date returnDate;
    
	public long getID() {
		return ID;
	}
	
	public void setID(long id) {
		ID = id;
	}
	
	public String getOrderCd() {
		return orderCd;
	}
	
	public void setOrderCd(String orderCd) {
		this.orderCd = orderCd;
	}
	
	public double getReturnCash() {
		return returnCash;
	}
	
	public void setReturnCash(double returnCash) {
		this.returnCash = returnCash;
	}
	
	public double getReturnScale() {
		return returnScale;
	}
	
	public void setReturnScale(double returnScale) {
		this.returnScale = returnScale;
	}
	
	public Date getReturnDate() {
		return returnDate;
	}
	
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
}
