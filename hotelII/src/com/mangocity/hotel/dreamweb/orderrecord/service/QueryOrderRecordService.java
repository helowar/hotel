package com.mangocity.hotel.dreamweb.orderrecord.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mangocity.hotel.orderrecord.model.OrderRecord;

/**
 * 主要用于酒店查询、酒店详情页的日志记录
 * @author liting
 *
 */
public interface QueryOrderRecordService {
	/**
	 * 将预订流程相关的信息增加到数据库中去
	 * @param request
	 * @param response
	 * @param checkInDate
	 * @param checkOutDate
	 * @param hotelId
	 * @param step
	 */
	public void saveOrderRecord(final HttpServletRequest request, final HttpServletResponse response, final Date checkInDate, final Date checkOutDate,
			final Long hotelId, final int step);

	/**
	 * 更新 orderRecord
	 * @param orderRecord
	 */
	public void updateOrderRecord(OrderRecord orderRecord);

}