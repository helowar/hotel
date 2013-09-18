package com.mangocity.tmchotel.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.tmchotel.persistence.HotelOrderFromBean;
import com.mangocity.tmchotel.persistence.HtlCalendarHelperBean;

/**
 * 查询支付是否成功的service
 * @author zuoshengwei
 *
 */
public interface OnlineMpmService {
	
	/**
	 * 根据外部交易号查询支付是否成功
	 * ngwei.zuo 2009-11-04
	 */
	public  String   getOnlineSucceedFlag(String outTradeNoStr);   

}
