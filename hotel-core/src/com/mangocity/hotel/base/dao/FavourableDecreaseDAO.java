package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;

public interface FavourableDecreaseDAO {
	
	/**
	 * 根据价格类型查询某个时间段的优惠立减信息
	 * 
	 * @param priceTypeId
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	public List<HtlFavourableDecrease> qryBenefitByPriceTypeAndDate(String priceTypeId, Date checkInDate, 
			Date checkOutDate);
	
	/**
	 * 根据多个价格类型查询某个时间段的优惠立减信息
	 * 
	 * @param priceTypeIds
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	public List<HtlFavourableDecrease> qryBenefitByMultiPriceTypeAndDate(String[] priceTypeIds, Date checkInDate, 
			Date checkOutDate);

}
