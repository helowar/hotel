package com.mangocity.fantiweb.service;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.hweb.persistence.QueryHotelForWebRoomType;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.persistence.HtlCalendarRowsBean;

public interface FantiWebTransactService {
	
	/**
	 * 把多个内地酒店预付,币种为澳门币MOP用港币显示，the second param payMethod hasn't been used.
	 */
	public void showToHK(List<QueryHotelForWebResult> webHotelResultLis,
			String payMethod);
	/**
	 * 把一个内地酒店的预付价，rmb转为hk,the second param hasn't been used.
	 */
	public void showForRMBToHKByOneHotel(QueryHotelForWebResult hotelInfo,
			String payMethod);
	/**
	 * 把一个币种为澳门币MOP的面付或预付显示金额都转为港币，the second param hasn't been used.
	 * @param hotelInfo,payMethod,currency
	 */
	public void  showForMOPToHKByOneHotel(QueryHotelForWebResult hotelInfo,String payMethod);
	
	/**
	 * 把内地的酒店的预付金额或者澳门币酒店面预付金额用港币显示（在日历中显示）
	 */
	public void calendarPriceToHKD(
			List<HtlCalendarRowsBean> calendarExtenderLst,String currency);

	/**
	 * 把order中的总价格转为HKD,面预付处理在方法里
	 */
	public void orderPriceNumToHKD(HotelOrderFromBean hotelOrderFromBean, String currency);

	

	/**
	 * 判断是否minPrice用港币显示
	 * 在hotelinfobean中加了FlagMinPrice_RMBToHKD,已修改minprice的值,必须在showToHK前实现
	 */
	public void minPrice_RMBToHKD(QueryHotelForWebResult hotelInfo,
			String currency);
	/**
	 * 判断是否minPrice用港币显示
	 * 在hotelinfobean中加了FlagMinPrice_MOPToHKD,已修改minprice的值,必须在showToHK前实现
	 */
    public void minPrice_MOPToHKD(QueryHotelForWebResult hotelInfo,
			String currency);
	/**
	 * 判断是否minPrice用港币显示
	 * ,在hotelinfobean中加了FlagMinPrice_RMBToHKD，已修改minprice的值,必须在showForRMBToHK前实现
	 */
	public void minPriceToHKD(
			List<QueryHotelForWebResult> webHotelResultLis);
     /**
      * 给roomType中的currency赋值，由于港澳酒店和内地酒店公用一个界面
      */
	public void addRoomTypeCurrency(List<QueryHotelForWebResult> webHotelResultLis, String currency);
	/**
     * 给roomType中的currency赋值，由于港澳酒店和内地酒店公用一个界面
     */
	public void addRoomTypeCurrency(QueryHotelForWebResult hotelInfo, String currency);
	/**
	 * 繁体优惠立减（预付立减）以港币显示
	 */
	public void changePriceInHKDCurrenyOfBenefit(List<QueryHotelForWebResult> webHotelResultLis,Date inDate, Date outDate);
	/**
	 * 繁体优惠立减（预付立减）以港币显示
	 */
	public void changePriceInHKDCurrenyOfBenefit(QueryHotelForWebResult hotelInfo,Date inDate, Date outDate);
	 /**
     * 同步配额明细中的总价格到订单表，防止变价，导致总金额和配额明细中的金额不相等 add by shengwei.zuo 2010-6-4
     * @param order
     * @return
     */
    public void synchroSumPriceToOrder(OrOrder order);
    /**
     * 发送需担保的订单邮件给hk组
     * @param order
     */
    public void sendNeedAssureOrderMailToHK(OrOrder order);
}
         

