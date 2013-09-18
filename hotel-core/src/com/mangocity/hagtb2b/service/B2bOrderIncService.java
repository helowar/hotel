package com.mangocity.hagtb2b.service;

import com.mangocity.hagtb2b.persistence.HtlB2bOrderIncrease;

public interface B2bOrderIncService {
	
	/**
	 * 保存订单增幅对象
	 * add by xiaowei.wang
	 * @return
	 */
	public void saveOrUpdate(HtlB2bOrderIncrease htlB2bOrderIncrease);
	
	/**
	 * 根据订单CD 判断是否是代理商加幅订单
	 * @param orderCD
	 * @return
	 */
	boolean judgeIsIncreaseOrder(String orderCD);
}
