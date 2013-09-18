package com.mangocity.hotel.order.service;

import java.util.Date;
import java.util.List;

public interface OrderInfoToXMLService {
	
	
	/**
	 * 根据会员编号和酒店id，查询出会员在酒店的入住的历史订单号
	 * @param memberCd
	 * @param hotelId
	 * @return
	 */
	public String findOrderByMemberCd(String memberCd, String hotelId);
	
	
	/**
	 * 根据审核日期，查询出此日期日审完已入住的散客订单信息
	 * @param auditedDate
	 * @return
	 */
	public String findOrderInfoByAuditedDate(String auditedDate);

}
