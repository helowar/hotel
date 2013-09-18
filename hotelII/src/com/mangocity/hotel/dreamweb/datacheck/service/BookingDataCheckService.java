package com.mangocity.hotel.dreamweb.datacheck.service;

import java.util.Map;

import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.webnew.persistence.HotelOrderFromBean;

/**
 * 用于检验预订的一些数据
 * @author liting
 *
 */
public interface BookingDataCheckService {
	
	/**
	 * 过滤掉满房的床型
	 * @param hotelOrderFrommBean
	 * @return
	 */
	public String filterFullBedType(HotelOrderFromBean hotelOrderFromBean);
	
	/**
	 * 获取床型对应的最小配额数。
	 * 入住离店日期段内，在床型房态出现不可超的情况下，Ｙ等于不可超的床型取该床型该时间段内最小配额数，需要大于等于１，小于等于５。
	 * y为预订最多房间数
	 * @param hotelOrderFromBean
	 * @return map，key为床型，value为预订时间段内的最小的配额数
	 */
	public Map<String,Integer> getBedTypeMinQuato(HotelOrderFromBean hotelOrderFromBean);
	
	/**
	 * 校验担保，防止使用浏览器的功能擅改担保相关的信息
	 * @param hotelOrderFromBean
	 * @param reservation
	 * @return
	 */
	public boolean checkChangeBookData(HotelOrderFromBean hotelOrderFromBean);
}
