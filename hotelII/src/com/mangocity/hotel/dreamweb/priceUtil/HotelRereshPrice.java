package com.mangocity.hotel.dreamweb.priceUtil;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import com.mangocity.hdl.constant.ChannelType;

import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.HotelBookingService;

/**
 * 主要用于刷价格，主要获取售价，底价，畅联的没有底价
 * @author liting
 *
 */
public class HotelRereshPrice {

	private HotelManageWeb hotelManageWeb;
	private HotelBookingService hotelBookingService;
	private Logger log=Logger.getLogger(HotelRereshPrice.class);
	public double getSalePrice(HotelOrderFromBean hotelOrderFromBean) {
		double salePrice = 0;
		
		try{
		if (hotelOrderFromBean.getRoomChannel() != 0 && hotelOrderFromBean.getRoomChannel() != ChannelType.CHANNEL_CTS) {
			salePrice = queryHDLPrice(hotelOrderFromBean);
		} else {

			List<QueryHotelForWebSaleItems> priceList = queryPriceList(hotelOrderFromBean);
			int days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
			if (priceList != null && priceList.size() >= days) {

				for (QueryHotelForWebSaleItems priceItem : priceList) {
					salePrice += priceItem.getSalePrice();
				}

			}

		}
		}catch (Exception e){
			log.error("getSalePrice has a wrong ", e);
			salePrice = 0;
		}
		return salePrice;
	}
	public double getBasePrice(HotelOrderFromBean hotelOrderFromBean){
		try{
		if (hotelOrderFromBean.getRoomChannel() == 0 || hotelOrderFromBean.getRoomChannel() == ChannelType.CHANNEL_CTS) {
			double basePrice = 0;
			List<QueryHotelForWebSaleItems> priceList = queryPriceList(hotelOrderFromBean);
			int days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
			if (priceList != null && priceList.size() >= days) {

				for (QueryHotelForWebSaleItems priceItem : priceList) {
					basePrice += priceItem.getBasePrice();
				}

			}
			return basePrice;
		}
		}catch(Exception e){
			log.error("getBasePrice has a wrong", e);
		}
		return 0;
	}
	
	/**
	 * 目前网站是处理预付酒店，才转换人民币，其他的是什么币就是什么币。
	 * @param hotelOrderFromBean
	 * @return
	 * @throws Exception 
	 */
	public double getPriceRMB(HotelOrderFromBean hotelOrderFromBean) {
		double rateToRMB = CurrencyBean.getRateToRMB(hotelOrderFromBean.getCurrency());
		double priceRMB = getSalePrice(hotelOrderFromBean)*rateToRMB;
		return priceRMB;
		
	}
	
	/**
	 * 查询中旅和传统的预订价格列表
	 * @param hotelOrderFromBean
	 * @return
	 */
	private List<QueryHotelForWebSaleItems> queryPriceList(HotelOrderFromBean hotelOrderFromBean){
		List<QueryHotelForWebSaleItems> queryPriceList = null;
		try {
			// 非中旅
			if (hotelOrderFromBean.getRoomChannel() <= 0) {
				queryPriceList = hotelManageWeb.queryPriceDetailForWeb(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean
						.getCheckoutDate(), hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(), hotelOrderFromBean.getRoomTypeId(),
						hotelOrderFromBean.getChildRoomTypeId(), hotelOrderFromBean.getPayMethod());

				int f = 0;
				// 处理连住优惠，打折优惠，传统的
				if (queryPriceList != null) {
					for (int y = 0; y < queryPriceList.size(); y++) {
						QueryHotelForWebSaleItems queryHotelForWebSaleItems = queryPriceList.get(y);
						f = hotelManageWeb.changePriceForFavourableThree(queryPriceList, queryHotelForWebSaleItems, hotelOrderFromBean.getHotelId(),
								hotelOrderFromBean.getChildRoomTypeId(), y, f);

					}
				}
				return queryPriceList;
			} else if (PayMethod.PRE_PAY.equals(hotelOrderFromBean.getPayMethod()) && ChannelType.CHANNEL_CTS == hotelOrderFromBean.getRoomChannel()) {
				queryPriceList = hotelBookingService.refreshHotelReservateExResponse(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(),
						hotelOrderFromBean.getChildRoomTypeId(), hotelOrderFromBean.getRoomChannel(), hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean
								.getCheckoutDate());
				return queryPriceList;
			}
		} catch (Exception e) {
			log.error("the queryPriceList() method of RereshPriceUtil has a Exception", e);
			return null;
		}
		return null;
	}
	
	private double queryHDLPrice(HotelOrderFromBean hotelOrderFromBean){
		try{
			String hdlPriceStr = hotelBookingService.reReshReservateExResponse(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(), String
					.valueOf(hotelOrderFromBean.getChildRoomTypeId()), hotelOrderFromBean.getRoomChannel(), hotelOrderFromBean.getCheckinDate(),
					hotelOrderFromBean.getCheckoutDate());
			if (null != hdlPriceStr && !"".equals(hdlPriceStr) && !hdlPriceStr.equals("hdlRefreshPriceFail")) {
				return Double.parseDouble(hdlPriceStr);
			}
			else {
				return 0;
			}
		}catch(Exception e){
			log.error("the queryHDLPrice method of RereshPriceUtil call HDL interfaces exception has a Exception", e);
			return 0;
		}
		
	}
	
	public void setHotelBookingService(HotelBookingService hotelBookingService) {
		this.hotelBookingService = hotelBookingService;
	}
	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}
	

}
