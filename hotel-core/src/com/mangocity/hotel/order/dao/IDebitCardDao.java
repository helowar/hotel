package com.mangocity.hotel.order.dao;

import java.util.List;


/**
 */
public interface IDebitCardDao  {
	/*
	 * 根据会员cd获取对应的借记卡（银联手机支付）的历史使用信息
	 */
	public List getDebitCardHistotyLis(String memberCd);
  

}
