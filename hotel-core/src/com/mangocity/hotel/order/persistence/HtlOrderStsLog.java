/**
 * 
 *  操作日志
 */
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class HtlOrderStsLog implements Entity {

    // ID <pk>
    private Long ID;
   
    private Integer oldQuotaOk;
    private Integer oldSendedhotelconfirm;
    private Integer oldHoteloralconfirm;
    private Integer oldHotelwrittenconfirm;
    private Integer oldHotelconfirmfaxreturn;
    private Integer oldCustomerconfirm;
    private Integer newQuotaok;
    private Integer newSendedhotelconfirm;
    private Integer newHoteloralconfirm;
    private Integer newHotelwrittenconfirm;
    private Integer newHotelconfirmfaxreturn;
    private Integer newCustomerconfirm;
    private Long ordercd;
    private String modifyName;
    private String modifyCode;
    private Date modifyTime;
	public Long getID() {
		return ID;
	}
	public void setID(Long id) {
		ID = id;
	}
	public Integer getOldQuotaOk() {
		return oldQuotaOk;
	}
	public void setOldQuotaOk(Integer oldQuotaOk) {
		this.oldQuotaOk = oldQuotaOk;
	}
	public Integer getOldSendedhotelconfirm() {
		return oldSendedhotelconfirm;
	}
	public void setOldSendedhotelconfirm(Integer oldSendedhotelconfirm) {
		this.oldSendedhotelconfirm = oldSendedhotelconfirm;
	}
	public Integer getOldHoteloralconfirm() {
		return oldHoteloralconfirm;
	}
	public void setOldHoteloralconfirm(Integer oldHoteloralconfirm) {
		this.oldHoteloralconfirm = oldHoteloralconfirm;
	}
	public Integer getOldHotelwrittenconfirm() {
		return oldHotelwrittenconfirm;
	}
	public void setOldHotelwrittenconfirm(Integer oldHotelwrittenconfirm) {
		this.oldHotelwrittenconfirm = oldHotelwrittenconfirm;
	}
	public Integer getOldHotelconfirmfaxreturn() {
		return oldHotelconfirmfaxreturn;
	}
	public void setOldHotelconfirmfaxreturn(Integer oldHotelconfirmfaxreturn) {
		this.oldHotelconfirmfaxreturn = oldHotelconfirmfaxreturn;
	}
	public Integer getOldCustomerconfirm() {
		return oldCustomerconfirm;
	}
	public void setOldCustomerconfirm(Integer oldCustomerconfirm) {
		this.oldCustomerconfirm = oldCustomerconfirm;
	}
	public Integer getNewQuotaok() {
		return newQuotaok;
	}
	public void setNewQuotaok(Integer newQuotaok) {
		this.newQuotaok = newQuotaok;
	}
	public Integer getNewSendedhotelconfirm() {
		return newSendedhotelconfirm;
	}
	public void setNewSendedhotelconfirm(Integer newSendedhotelconfirm) {
		this.newSendedhotelconfirm = newSendedhotelconfirm;
	}
	public Integer getNewHoteloralconfirm() {
		return newHoteloralconfirm;
	}
	public void setNewHoteloralconfirm(Integer newHoteloralconfirm) {
		this.newHoteloralconfirm = newHoteloralconfirm;
	}
	public Integer getNewHotelwrittenconfirm() {
		return newHotelwrittenconfirm;
	}
	public void setNewHotelwrittenconfirm(Integer newHotelwrittenconfirm) {
		this.newHotelwrittenconfirm = newHotelwrittenconfirm;
	}
	public Integer getNewHotelconfirmfaxreturn() {
		return newHotelconfirmfaxreturn;
	}
	public void setNewHotelconfirmfaxreturn(Integer newHotelconfirmfaxreturn) {
		this.newHotelconfirmfaxreturn = newHotelconfirmfaxreturn;
	}
	public Integer getNewCustomerconfirm() {
		return newCustomerconfirm;
	}
	public void setNewCustomerconfirm(Integer newCustomerconfirm) {
		this.newCustomerconfirm = newCustomerconfirm;
	}
	public Long getOrdercd() {
		return ordercd;
	}
	public void setOrdercd(Long ordercd) {
		this.ordercd = ordercd;
	}
	public String getModifyName() {
		return modifyName;
	}
	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}
	public String getModifyCode() {
		return modifyCode;
	}
	public void setModifyCode(String modifyCode) {
		this.modifyCode = modifyCode;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
    
    
    
    

}
