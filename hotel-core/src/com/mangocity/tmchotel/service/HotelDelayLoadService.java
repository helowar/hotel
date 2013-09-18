package com.mangocity.tmchotel.service;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;

/**
 * 处理延时加载信息的Service
 * @author chenjiajie
 *
 */
public interface HotelDelayLoadService extends Serializable {

	/**
	 * 按照大礼包ID查询
	 * @param presaleId
	 * @return
	 */
	public HtlPresale findHtlPresaleById(Long presaleId);
	
	/**
	 * 按照房型ID查询
	 * @param roomTypeId
	 * @return
	 */
	public HtlRoomtype findHtlRoomtype(Long roomTypeId);
	
	/**
	 * 按照价格类型ID查询酒店促销信息
	 * @param hotelId
	 * @param priceTypeId
	 * @return
	 */
	public List<HtlSalesPromo> findHtlSalesPromo(Long hotelId,String priceTypeId);
}
