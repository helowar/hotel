package com.mangocity.hotel.ejb.reservation.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.mangocity.hotel.ejb.reservation.HotelSupplyDelegate;
import com.mangocity.hotel.exception.BusinessException;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.service.MemberBaseInfoDelegate;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.search.util.PayMethod;
import com.mangocity.hotel.supply.dto.OrOrderInfoDTO;
import com.mangocity.hotel.supply.facade.HotelOrderSupplyProcessFacade;
import com.mangocity.util.bean.MyBeanUtil;

public class HotelSupplyDelegateImpl implements HotelSupplyDelegate {

	private Logger log = Logger.getLogger(HotelSupplyDelegateImpl.class);

	private HotelOrderSupplyProcessFacade hotelOrderSupplyProcessFacade;
	private MemberBaseInfoDelegate memberBaseInfoDelegate;

	private OrOrderDao orOrderDao;

	public void createCreditCardPreAuth(OrOrder order, MemberDTO member) {
		log.info("payAssureOrderProcess star " + order.getOrderCD());
		if (order != null) {
			OrOrderInfoDTO orderDTO = new OrOrderInfoDTO();
			MyBeanUtil.copyProperties(orderDTO, order);
			try {
				if (member == null) {
					member = new MemberDTO();
				}
				hotelOrderSupplyProcessFacade.createCreditCardPreAuth(orderDTO, member);

				log.info("payAssureOrderProcess normal end");
			} catch (Exception e) {

				log.error("call hotelOrderSupplyProcessFacade has a wrong ", e);
			}
		}
	}

	/**
	 * 担保订单处理
	 */
	public void payAssureOrderProcess(String orderCd) {
		log.info("supply payAssureOrderProcess orderCd=" + orderCd);
		OrOrder order = getOrOrder(orderCd);
		if (order != null) {
			String payMethod = order.getPayMethod();
			if (PayMethod.PAY.equals(payMethod) && order.isCreditAssured() && "NET".equals(order.getSource())) {

				OrOrderInfoDTO orderDTO = new OrOrderInfoDTO();
				MyBeanUtil.copyProperties(orderDTO, order);
				try {
					MemberDTO member = getMemberByMemberCd(order.getMemberCd());
					if (member == null) {
						member = new MemberDTO();
					}
					hotelOrderSupplyProcessFacade.payAssureOrderProcess(orderDTO, member);
					log.info("payAssureOrderProcess normal end");
				} catch (Exception e) {

					log.error("call hotelOrderSupplyProcessFacade has a wrong ", e);
				}
			}
		}
	}

	/**
	 * 根据会员CD获得会员信息。
	 * 
	 * @param memberCd
	 *            会员CD。
	 * @return 会员
	 */
	private MemberDTO getMemberByMemberCd(String memberCd) throws BusinessException {
		if (memberCd == null || "".equals(memberCd)) {
			return null;
		}
		return memberBaseInfoDelegate.getMemberByMemberCd(memberCd);

	}

	/**
	 * 据订单号查询订单信息
	 * 
	 * @param orderCD
	 * @return
	 */
	private OrOrder getOrOrder(String orderCD) {
		List<OrOrder> orderList = orOrderDao.query(" from OrOrder where orderCD = ? ", new Object[] { orderCD });
		if (orderList.isEmpty()) {
			return null;
		}
		OrOrder order = orderList.get(0);
		return order;
	}

	public void setHotelOrderSupplyProcessFacade(HotelOrderSupplyProcessFacade hotelOrderSupplyProcessFacade) {
		this.hotelOrderSupplyProcessFacade = hotelOrderSupplyProcessFacade;
	}

	public void setOrOrderDao(OrOrderDao orOrderDao) {
		this.orOrderDao = orOrderDao;
	}

	public void setMemberBaseInfoDelegate(MemberBaseInfoDelegate memberBaseInfoDelegate) {
		this.memberBaseInfoDelegate = memberBaseInfoDelegate;
	}

}
