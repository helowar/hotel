package com.mangocity.hotel.base.service;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;

/**
 * 处理延时加载信息的Service
 * @author chenjiajie
 *
 */
public interface HotelDelayLoadService {

	/**
	 * 按照大礼包ID查询
	 * @param presaleId
	 * @return
	 */
	public HtlPresale findHtlPresaleById(Long presaleId);
	
	/**
	 * 按照价格类型ID查询酒店促销信息
	 * @param hotelId
	 * @param priceTypeId
	 * @param beginDate 入住日期
	 * @return
	 */
	public List<HtlSalesPromo> findHtlSalesPromo(Long hotelId,String priceTypeId,Date beginDate);
}
