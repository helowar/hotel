package com.mangocity.hagtb2b.dao;

import com.mangocity.hagtb2b.persistence.HtlB2bOrderIncrease;

/**
 * 预付底价 订单增幅 接口类
 * @author wangxiaowei
 */
public interface IB2bOrderIncDao {
	/**
	 * 添加或更新实体
	 */
	public void saveOrUpdate(HtlB2bOrderIncrease htlB2bOrderIncrease);

	/**
	 * 根据订单CD 获取对象
	 * @param orderCD
	 * @return
	 */
	HtlB2bOrderIncrease queryByOrderCD(String orderCD);
	
	/**
	 *  更新实体
	 * @param htlB2bOrderIncrease
	 */
	public void updateHtlB2bOrderIncrease(HtlB2bOrderIncrease htlB2bOrderIncrease);
	/**
	 *  查询实体
	 * @param htlB2bOrderIncrease
	 */
	public HtlB2bOrderIncrease getHtlB2bOrderIncrease(long  id);
}