package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.mangocity.hagtb2b.persistence.HtlB2bIncrease;
import com.mangocity.hotel.base.persistence.B2BIncrease;

/**
 * B2B加幅
 * @author xuyiwen
 *
 */
public interface B2BIncreaseDao {
	/**
	 * 根据酒店ID得到该酒店下的所有加幅记录
	 * @param hotelId
	 * @return
	 */
	List<B2BIncrease> query(long id);
	
	/**
	 * 批量设置加幅
	 * @param b2BIncreases
	 */
	void batchInsert(List<B2BIncrease> b2BIncreases);
	
	
	/**
	 * 根据酒店ID将该酒店下的所有加幅记录失效 flag = 0
	 * @param hotelId
	 */
	void update(long hotelId);
	/**
	 * 根据酒店查增幅规则
	 * @param hotelId
	 * @param flag 是否有效（0无效 1有效）
	 * @return
	 */
	List<B2BIncrease> queryEffectiveOrNotB2BIncrease(long hotelId);
	
}
