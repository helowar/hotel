package com.mangocity.proxy.dao.impl;

import java.util.List;

import com.mangocity.proxy.dao.CreditCardSerialNoDAO;
import com.mangocity.proxy.persistence.CreditCardSerialNo;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class CreditCardSerialNoDAOImpl extends GenericDAOHibernateImpl implements CreditCardSerialNoDAO {

	public List<CreditCardSerialNo> getSerialNoByCustomerIdOrderCode(String customerId, String orderCode) {
		String hql=" from CreditCardSerialNo c where c.customerId = ? and c.orderCode = ? " 
				 + " order by c.serialNo desc ";
		
		return super.query(hql, new Object[]{customerId, orderCode});
	}
	
	public void updateCreditCardSerialNo(CreditCardSerialNo creditCardSerialNo) {		
		super.update(creditCardSerialNo);
	}
	
	public void saveCreditCardSerialNo(CreditCardSerialNo creditCardSerialNo) {		
		super.save(creditCardSerialNo);
	}

}
