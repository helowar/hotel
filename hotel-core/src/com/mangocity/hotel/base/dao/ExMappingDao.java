package com.mangocity.hotel.base.dao;

import com.mangocity.hotel.base.persistence.ExMapping;

/**
 * 酒店映射表Dao
 * @author huanglingfeng
 *
 */
public interface ExMappingDao {
	/**
	 * 查询价格类型级别的映射
	 * @param mgPriceTypeId 芒果价格类型Id
	 * @return
	 */
	ExMapping queryExPrice(Long mgPriceTypeId);
}
