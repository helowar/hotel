/**
 *  Modified by chenkeming@2007.05.08
 *  
 *  订单信用卡资料表
 */
package com.mangocity.hotel.order.persistence;

import java.util.Date;

/**
 */
public class DebitCardHistoryInfo{
	//持卡人手机号码
	private String mobileNumber;
	
	//银行名称
	private String bankName;
	
	//持卡人名字
	private String cardHolderName;
	
	//创建时间
	private Date createDate;
	
	//卡号后四位
	private String bankCardNoFour;
	
	//卡开户所在省份
	private String accountinProvince;
	
	//卡开户所在市
	private String accountinCity;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getBankCardNoFour() {
		return bankCardNoFour;
	}

	public void setBankCardNoFour(String bankCardNoFour) {
		this.bankCardNoFour = bankCardNoFour;
	}

	public String getAccountinProvince() {
		return accountinProvince;
	}

	public void setAccountinProvince(String accountinProvince) {
		this.accountinProvince = accountinProvince;
	}

	public String getAccountinCity() {
		return accountinCity;
	}

	public void setAccountinCity(String accountinCity) {
		this.accountinCity = accountinCity;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

}
