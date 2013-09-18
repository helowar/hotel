package com.mangocity.hotel.base.manage;

import java.util.List;

import com.mangocity.hotel.base.persistence.B2BIncrease;

/**
 * B2B加幅设置
 * @author xuyiwen
 *
 */
public interface B2BIncreaseManage {
	
	/**
	 * 批量设置B2B加幅
	 * @param hotelIds 酒店ID集合
	 * @param increaseRate 加幅
	 * @param createName 
	 */
	void saveB2BIncrease(List<Long> hotelIds,double increaseRate,String createName);
	
	
	B2BIncrease queryEffectiveB2BIncrease(long hotelId);
}
