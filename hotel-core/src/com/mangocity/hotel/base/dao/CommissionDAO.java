package com.mangocity.hotel.base.dao;

import java.util.List;

import com.mangocity.hotel.base.persistence.CommisionAdjust;
import com.mangocity.hotel.base.persistence.CommisionSet;
import com.mangocity.hotel.base.persistence.HtlB2bComminfo;

public interface CommissionDAO {
	
	/**
	 * 查询所有佣金率
	 * 
	 * @return
	 */
	public List<HtlB2bComminfo> queryAllCommission();
	
	/**
	 * 根据酒店星级查询佣金设置
	 * 
	 * @param b2bCd
	 * @param hotelStar
	 * @return
	 */
	public List<CommisionSet> queryCommissionSettingByHtlStar(String b2bCd, String hotelStar);
	
	/**
	 * 查询佣金调整
	 * 
	 * @param commissionAdjust
	 * @return
	 */
	public List<CommisionAdjust> queryCommissionAdjust(CommisionAdjust commissionAdjust);

}
