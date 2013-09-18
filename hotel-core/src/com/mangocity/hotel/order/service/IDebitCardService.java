package com.mangocity.hotel.order.service;

import java.io.Serializable;
import java.util.List;


/**
 */
public interface IDebitCardService extends Serializable {

	/*
	 * 根据会员cd获取对应的借记卡（银联手机支付）的历史使用信息
	 */
    public List getDebitCardHistoryLis(String memberCd);
    
}
