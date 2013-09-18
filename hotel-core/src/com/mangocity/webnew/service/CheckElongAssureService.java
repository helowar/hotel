package com.mangocity.webnew.service;

import java.util.Date;

import com.mangocity.webnew.persistence.ElongAssureResult;

public interface CheckElongAssureService {
	/**
	 * 根据输入参数检查是否需要担保，
	 * 返回ElongAssureResult艺龙判断担保结果
	 * @param hotelId
	 * @param priceTypeId
	 * @param checkInDate
	 * @param checkOutDate
	 * @param roomQty
	 * @param arriveTime
	 * @return ElongAssureResult
	 */
	ElongAssureResult checkIsAssure(long hotelId,long priceTypeId,Date checkInDate,Date checkOutDate,int roomQty,String arriveTime);
}
