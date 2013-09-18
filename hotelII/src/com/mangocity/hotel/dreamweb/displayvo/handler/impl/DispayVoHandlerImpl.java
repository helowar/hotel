package com.mangocity.hotel.dreamweb.displayvo.handler.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.dreamweb.displayvo.AssureInfoVO;
import com.mangocity.hotel.dreamweb.displayvo.BreakfastAndPriceItemVO;
import com.mangocity.hotel.search.constant.PayMethod;
import com.mangocity.hotel.search.handler.impl.HotelQueryHandler;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.webnew.persistence.HotelOrderFromBean;

/**
 * 
 * @author liting
 * 设置页面展示的VO的属性
 */
public class DispayVoHandlerImpl{
	
	/**
	 * 改变订单填写页的价格列表中，设置酒店优惠中，每天的价格。改变价格
	 * @param hotelOrderFromBean
	 * @param queryPriceList 查询出每天的价格，包括前两天和后两天
	 */
	public BreakfastAndPriceItemVO handleBreakfastAndPriceItemVO(HotelOrderFromBean hotelOrderFromBean, QueryHotelForWebSaleItems queryPrice) {
		BreakfastAndPriceItemVO breakfastAndPriceItemVO = new BreakfastAndPriceItemVO();
		breakfastAndPriceItemVO.setFellowDate(DateUtil.formatDateToMMDD(queryPrice.getFellowDate()));
		breakfastAndPriceItemVO.setPriceId(queryPrice.getPriceId());

		breakfastAndPriceItemVO.setSalePrice(getSalePriceOfBreakfastAndPriceItemVO(queryPrice,hotelOrderFromBean));
		breakfastAndPriceItemVO.setRmbPrice(queryPrice.getRmbPrice());
		breakfastAndPriceItemVO.setBreakfastType(queryPrice.getBreakfastType());
		breakfastAndPriceItemVO.setBreakfastNum(queryPrice.getBreakfastNum());
		breakfastAndPriceItemVO.setBreakfastNumName(HotelQueryHandler.BreakFastNumConvert.getValueForNum(queryPrice.getBreakfastNum()));
		breakfastAndPriceItemVO.setWeekDay(HotelQueryHandler.WeekDay.getValueByKey(queryPrice.getWeekDay()));
		breakfastAndPriceItemVO.setCurrencyStr(getCurrencyStrOfBreakfastAndPriceItemVO(hotelOrderFromBean));
		if (DateUtil.between(queryPrice.getFellowDate(), hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate())) {
			if (queryPrice.getFellowDate().equals(hotelOrderFromBean.getCheckoutDate())) {
				breakfastAndPriceItemVO.setInTheDays(false);
			} else {
				breakfastAndPriceItemVO.setInTheDays(true);
			}
		} else {
			breakfastAndPriceItemVO.setInTheDays(false);
		}
		return breakfastAndPriceItemVO;
	}
	
	
	/**
	 * 填充有关担保的信息，担保类型，担保提示语，担保金额等等
	 */
	public  AssureInfoVO handleAssureInfoVo(OrReservation reservation,String inDate,String outDate) {
		Date nowDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nowDateStr = dateFormat.format(nowDate);
		AssureInfoVO assureInfoVO = new AssureInfoVO();
		//String bookAssureHintStr = hotelBookService.getBookhintAssure(reservation, hotelOrderFromBean.getPayMethod(), hotelOrderFromBean
				//.isPayToPrepay());
		//assureInfoVO.setAssureHintStr(bookAssureHintStr);
		assureInfoVO.setAssureMoney(reservation.getReservSuretyPrice());

		Calendar calendar = Calendar.getInstance();
		int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
		int nowMinute=calendar.get(Calendar.MINUTE);

		if (reservation.isUnCondition()) {
			assureInfoVO.setAssureType(1);// 无条件担保
		} else if (reservation.isOverTimeAssure()) {
			assureInfoVO.setAssureType(2);// 超时担保
			//String nowHourStr=nowHour+":00";
			//测试用
			String nowHourStr = nowHour+""+nowMinute;
			String lateSuretyTime = reservation.getLateSuretyTime();
			String lateSuretyTimeTemp=lateSuretyTime.replace(":", "");
			if (nowDateStr.equals(inDate) && DateUtil.compareHour(lateSuretyTimeTemp, nowHourStr )) {
				assureInfoVO.setFirstRetentionTime(null);
			} else {
				assureInfoVO.setFirstRetentionTime(lateSuretyTime);
			}

		} else if (reservation.isRoomsAssure()) {
			assureInfoVO.setAssureType(3);// 超房担保			
		} else if (reservation.isNightsAssure()) {
			assureInfoVO.setAssureType(4);// 超间夜担保
		} else {// 不须担保
		if (nowDateStr.equals(inDate) && nowHour >= 18) {
			if(nowHour>=22){
				assureInfoVO.setFirstRetentionTime(null);
				assureInfoVO.setMidFirstRetentionTime(null);
				assureInfoVO.setMidSecondRetentionTime(null);
				}
				else if(nowHour>=20&&nowHour<22){
					assureInfoVO.setFirstRetentionTime(null);
					assureInfoVO.setMidFirstRetentionTime(null);
					assureInfoVO.setMidSecondRetentionTime("22:00");
					
				}
				else if(nowHour>=18&& nowHour<20){
					assureInfoVO.setFirstRetentionTime(null);
					assureInfoVO.setMidFirstRetentionTime("20:00");
					assureInfoVO.setMidSecondRetentionTime("22:00");
				}
				else if(nowHour<18){
					assureInfoVO.setFirstRetentionTime("18:00");
					assureInfoVO.setMidFirstRetentionTime("20:00");
					assureInfoVO.setMidSecondRetentionTime("22:00");
				}
		} else {
				assureInfoVO.setMidFirstRetentionTime("20:00");
				assureInfoVO.setMidSecondRetentionTime("22:00");
				assureInfoVO.setFirstRetentionTime("18:00");
			}
			assureInfoVO.setAssureType(0);
		}

		assureInfoVO.setSecondRetentionTime("次日凌晨1:00");
		return assureInfoVO;
	}
	
	
	
	/**
	 * 
	 * TODO 对于所有预付酒店统一显示为人民币
	 * @return
	 */
	private String getCurrencyStrOfBreakfastAndPriceItemVO(HotelOrderFromBean hotelOrderFromBean) {

		if (PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod())) {
			return "&yen;";
		}
		return hotelOrderFromBean.getCurrencyStr();
	}

	/**
	 * 
	 * TODO 计算非人民币的价格
	 * @param queryPrice
	 * @return
	 */
	private Double getSalePriceOfBreakfastAndPriceItemVO(QueryHotelForWebSaleItems queryPrice,HotelOrderFromBean hotelOrderFromBean) {
		if (PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod())) {
			double rateToRMB = CurrencyBean.getRateToRMB(hotelOrderFromBean.getCurrency());
			double priceRMB = BigDecimal.valueOf(queryPrice.getSalePrice()).multiply(BigDecimal.valueOf(rateToRMB)).doubleValue();
			priceRMB = Math.ceil(priceRMB);

			return priceRMB;
		}
		return queryPrice.getSalePrice();
	}
}
