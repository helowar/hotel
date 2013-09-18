package com.mangocity.hotel.dreamweb.orderrecord.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.orderrecord.model.OrderRecord;
import com.mangocity.webnew.persistence.HotelOrderFromBean;

/**
 * 主要用于下单的流程的日志记录
 * @author liting
 *
 */
public interface BookOrderRecordService {
	
	/**
	 * 将预订流程相关的信息增加到数据库中去
	 * @param request
	 * @param response
	 * @param hotelOrderFromBean
	 * @param member
	 * @param order
	 * @param step
	 */
	public void saveOrderRecord(final HttpServletRequest request, final HttpServletResponse response, final HotelOrderFromBean hotelOrderFromBean,
			final MemberDTO member, final OrOrder order, final int step);

	/**
	 * 更新 orderRecord
	 * @param orderRecord
	 */
	public void updateOrderRecord(OrderRecord orderRecord);

}
