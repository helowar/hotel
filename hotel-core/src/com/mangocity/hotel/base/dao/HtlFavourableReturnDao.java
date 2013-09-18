package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlFavourableReturn;

public interface HtlFavourableReturnDao {
	
	/**
	 * 取出对应酒店所有的现金返还条款 add by xiaowei.wang 2010-11-15
	 * @param hotelId
	 * @return
	 */
	public List<HtlFavourableReturn> getHtlFavourableReturnListByHotelId(long hotelId);
	
	/**
	 * 根据ID查询对应的现金返还规则 add by xiaowei.wang 2010-11-15
	 * @param id
	 * @return
	 */
	public HtlFavourableReturn getHtlFavourableReturnById(long id);
	
	/**
	 * 取出对应酒店价格类型现金返还条款 add by xiaowei.wang 2010-11-15
	 * @param hotelId
	 * @param priceTypeId
	 * @return
	 */
	public List<HtlFavourableReturn> getHtlFavourableReturnListByPriceTypeId(long hotelId,long priceTypeId);
	
	
	public  List<HtlFavourableReturn> getFavourableReturnOrderListByPayMethod(long hotelId,long priceTypeId,Integer payMethod);
	
	
	public  List<HtlFavourableReturn> getSubFavReturnListByModifyTime(long hotelId,long priceTypeId,Date modifyTime);
	
	public void saveHtlFavourableReturn(HtlFavourableReturn htlFavourableReturn);
	
	public void removeHtlFavourableReturnById(long id);
	
	public void saveHtlFavourableReturnList(List<HtlFavourableReturn> list);
}
