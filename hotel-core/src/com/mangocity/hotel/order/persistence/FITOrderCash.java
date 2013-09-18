/**
 * 
 */
package com.mangocity.hotel.order.persistence;

/**
 * 订单返现记录
 * @author xiongxiaojun
 *
 */
public class FITOrderCash {
    private long ID;
    private String orderCd;
    private double returnCash;
    private String memberCd;
    
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
	public String getMemberCd() {
		return memberCd;
	}
	public void setMemberCd(String memberCd) {
		this.memberCd = memberCd;
	}
 }
