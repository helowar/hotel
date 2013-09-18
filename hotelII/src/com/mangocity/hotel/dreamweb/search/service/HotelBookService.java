package com.mangocity.hotel.dreamweb.search.service;

import java.util.Date;

import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.search.model.QueryCommodityInfo;
import com.mangocity.webnew.persistence.HotelOrderFromBean;


public interface HotelBookService {
	
	/**
	 * 根据priceTypeId得到hotelId
	 */
	public Long getHotelId(Long priceTypeId);
	
	/**
	 * 根据priceTypeId得到roomTypeId
	 */
	public Long getRoomTypeId(Long priceTypeId);
	
	/**
	 * 根据priceTypeId查询酒店基本信息
	 */
	public HtlHotel queryHtlHotelInfo(Long priceTypeId);	
	
	/**
	 * 根据priceTypeId查询房型的基本信息
	 */
	public HtlRoomtype queryHtlRoomtypeInfo(Long priceTypeId);
	
	/**
	 * 查询ExMapping，主要提供一个渠道号
	 */
	public ExMapping queryExMapping(Long priceTypeId);
	
	/**
	 * 获取渠道号
	 */
	public Long getCooperateChannel(Long priceTypeId);
	
	/**
	 * 获取商品信息<br>
	 *  
	 目前返回以下信息：<br>
	  
		roomTypeId=32904,      房型Id<br>
	    roomTypeName=海景预付, 房型名称<br>	
		childRoomTypeId=34053,  价格类型Id<br>
		childRoomTypeName=标准, 价格类型名称<br>				
		breakfastType=3,     早餐类型<br>
		breakfastNum=0,      早餐数<br>
		bedTypeStr=2,        可预订的床型(如"1,2")<br>
		currency=HKD,        币种(如"RMB")<br>
		canbook=null,		 是否可预订 ("1"为可预订)<br>
		payToPrepay=false,   面转预(请参看QueryCommodityInfo类的该成员说明)<br>		
		returnAmount=25,        返现金额<br>  
	 */
    public QueryCommodityInfo queryCommidity(Long priceTypeId,
			String payMehtod, Date inDate, Date outDate, boolean forCts);
		
	/**
	 * hotelOrderFromBean添加酒店信息，必须有childRoomTypeId
	 */
	public void addHotelInfoToOrderBean(HotelOrderFromBean hotelOrderFromBean);
	
	/**
	 * hotelOrderFromBean添加房型信息，必须有childRoomTypeId
	 */
	public void addRoomInfoToOrderBean(HotelOrderFromBean hotelOrderFromBean);
	/**
	 * hotelOrderFromBean添加价格类型信息，必须有childRoomTypeId,payMethod,Indate,oudate
	 */
	public void addPriceInfoToOrderBean(HotelOrderFromBean hotelOrderFromBean);
	
	/**
	 * 必须有childRoomTypeId,payMethod,Indate,oudate
	 */
	public void addsomeInfoToOrderBean(HotelOrderFromBean hotelOrderFromBean);
	
	/**
	 * 担保条款
	 */
	public String getBookhintAssure(OrReservation orReservation,String payMethod,boolean payToPrepay);
	
	/**
	 * 修改条款
	 */
	public String getBookhintCancelAndModify(OrReservation orReservation,ReservationAssist reservationAssist,String payMethod,boolean payToPrepay);
}
