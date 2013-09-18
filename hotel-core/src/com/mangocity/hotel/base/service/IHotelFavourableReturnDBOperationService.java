package com.mangocity.hotel.base.service;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlFavourableReturn;

public interface IHotelFavourableReturnDBOperationService {
	
	/**
	 * 取出对应酒店所有的现金返还条款 add by xiaojun.xiong 2010-9-13
	 * @param hotelId
	 * @return
	 */
	public List<HtlFavourableReturn> queryAllFavourableReturn(long hotelId);
	
	/**
	 * 根据ID查询对应的现金返还规则 add by xiaojun.xiong 2010-9-13
	 * @param id
	 * @return
	 */
	public HtlFavourableReturn getFavReturnById(long id);
	
    
	public long deleteFavReturnObj(HtlFavourableReturn favourableReturn); 
	
	
	/**
	 * 取出对应酒店价格类型现金返还条款 add by xiaojun.xiong j2010-9-13
	 * @param hotelId
	 * @param priceTypeId
	 * @return
	 */
	public List<HtlFavourableReturn> queryFavourableReturnForPriceTypeId(long hotelId,long priceTypeId); 
}
