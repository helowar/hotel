package com.mangocity.hotel.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.order.dao.IDebitCardDao;
import com.mangocity.hotel.order.service.IDebitCardService;





/**
 */
public class DebitCardService implements IDebitCardService {
	private IDebitCardDao debitCardDao;
	/*
	 * 根据会员cd获取对应的借记卡（银联手机支付）的历史使用信息
	 */
    public List getDebitCardHistoryLis(String memberCd){
    	List lis = new ArrayList();
    	lis = debitCardDao.getDebitCardHistotyLis(memberCd);
    	return lis;
    }
	public IDebitCardDao getDebitCardDao() {
		return debitCardDao;
	}
	public void setDebitCardDao(IDebitCardDao debitCardDao) {
		this.debitCardDao = debitCardDao;
	}
}
