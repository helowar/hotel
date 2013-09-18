package com.mangocity.webnew.service;

import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.user.UserWrapper;

public interface OrderCancelConfirmService {
	/**
	 * 根据酒店确认方式发送传真或email
	 * @param order
	 * @param member
	 * @param roleUser
	 * @return
	 */
	public String orderCancelSend(OrOrder order,MemberDTO member,UserWrapper roleUser);
	/**
	 * 给酒店发送取消email
	 * @return
	 */
	public String hotelOrderEmailSend(OrOrder order,MemberDTO member,UserWrapper roleUser);
	/**
	 * 给酒店发送取消传真
	 * @return
	 */
	public String hotelOrderFaxSend(OrOrder order,MemberDTO member,UserWrapper roleUser);
	
}
