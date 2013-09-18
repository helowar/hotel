package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;
/**
 * 艺龙担保条款规则表DAO
 * @author huanglingfeng
 */
import com.mangocity.hotel.base.persistence.HtlElAssureRule;

public interface HtlElAssureRuleDao {
	/**
	 * 查询艺龙担保条款
	 * @param elHotelId 艺龙酒店ID
	 * @param rateplanId 艺龙价格类型ID
	 * @param checkinDate 订单首晚日期
	 * @return
	 */
	List<HtlElAssureRule> queryElAssureList(String elHotelId,String rateplanId,Date checkinDate);
	
	/**
	 * 查艺龙在日期范围内的条款，包括在店条款
	 * key=datetype-VouchMoneyType value=rule
	 * 入住日期担保的条款只能有一个,在店日期判断的可能有多个
	 * @param hotelId
	 * @param priceTypeId
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	List<HtlElAssureRule> queryElAssureMap(long hotelId, long priceTypeId,
			Date checkInDate, Date checkOutDate);
}
