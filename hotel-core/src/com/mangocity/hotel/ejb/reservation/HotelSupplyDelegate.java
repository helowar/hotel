package com.mangocity.hotel.ejb.reservation;

import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.persistence.OrOrder;

/**
 * 担保订单EJB处理
 * @author liting
 *
 */
public interface HotelSupplyDelegate {
	
	public void payAssureOrderProcess(String orderCd);
	
	public void createCreditCardPreAuth(OrOrder order, MemberDTO member);

}
