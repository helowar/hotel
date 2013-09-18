package com.mangocity.proxy.dao;

import java.util.List;

import com.mangocity.proxy.persistence.CreditCardSerialNo;

public interface CreditCardSerialNoDAO {
	
	/**
	 * 
	 * 
	 * @param customerId
	 * @param orderNo
	 * @return
	 */
	public List<CreditCardSerialNo> getSerialNoByCustomerIdOrderCode(String customerId, String orderNo);
	
	/**
	 * 
	 * 
	 * @param creditCardSerialNo
	 */
	public void updateCreditCardSerialNo(CreditCardSerialNo creditCardSerialNo);
	
	/**
	 * 
	 * 
	 * @param creditCardSerialNo
	 */
	public void saveCreditCardSerialNo(CreditCardSerialNo creditCardSerialNo);

}
