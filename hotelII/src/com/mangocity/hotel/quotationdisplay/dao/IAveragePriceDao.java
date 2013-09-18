package com.mangocity.hotel.quotationdisplay.dao;

import java.util.List;

import com.mangocity.hotel.pricelowest.persistence.HtlLowestPrice;
import com.mangocity.hotel.quotationdisplay.model.QueryParam;
/***
 * 
 * @author panjianping
 *
 */
public interface IAveragePriceDao {
	/***
	 * 通过queryParam查询参数对象，根据条件查询价格统计表中的信息
	 * （需要用到价格统计表中的最低价信息min_price字段）
	 * @param queryParam
	 * @return
	 */
	public List queryHotelOnCondition(QueryParam queryParam);
}
