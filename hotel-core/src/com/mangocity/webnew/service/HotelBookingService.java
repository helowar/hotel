package com.mangocity.webnew.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.persistence.HtlCalendarHelperBean;

/**
 * 下单的service
 * @author zuoshengwei
 *
 */
public interface HotelBookingService {
	
	
	/**
	 * 订单页面，日历控件显示每一天的价格
	 * @param inDate  入住日期
	 * @param outDate 离店日期
	 * @param year    年份
	 * @param month   月份
	 * @param day     入住天数
	 * @return
	 * add by shengwei.zuo 2009-11-04
	 */
	public  HtlCalendarHelperBean  getBookCalendarExtender();   
	
	/**
	 * 订单页面，日历控件table中 td的创建
	 * @param inDate  入住日期
	 * @param outDate 离店日期
	 * @param year    年份
	 * @param month   月份
	 * @param day     入住天数
	 * @return
	 * add by shengwei.zuo 2009-11-04
	 */
	public String getTdclass(String inDate,String outDate,int year,int month,String day);
	
	
	/**
	 * 组装css 和价格
	 * @param inDate
	 * @param outDate
	 * @param nextYear
	 * @param nextMonth
	 * @param nextDays
	 * @return
	 * add by shengwei.zuo 2009-11-06
	 */
	public  List  getCalendarPrice(String calendarStyle,HtlCalendarHelperBean calendarHelperBean);
	
	/**
	 * 连住优惠，改变相应的售价
	 * @param hotelOrderFromBean
	 * @param priceList
	 * @return
	 * add by shengwei.zuo 2009-11-04
	 */
	public List  changeFavPrice(HotelOrderFromBean hotelOrderFromBean,List priceList);
	
 
    /**
     * 组装URL参数
     * 
     * @param params
     * @return
     */
    public String getUrlPropertyByBean(HotelOrderFromBean hotelOrderFromBean,Map  params,double rate);
    
    /**
     * 获得通过连住优惠变价后的list 
     * @param queryFromBean
     * @return
     * add by shengwei.zuo 2009-11-13
     */
    public List  getFavChgPrice(List  queryFromBean);
    
    /**
     * 根据合同ID，入住日期，离店日期查询出 房费税费设定的记录  add by shengwei.zuo 2009-12-1
     */
    public  HtlTaxCharge  getHaveTaxCharge(Long contractId,Date beginDate,Date endDate);
    
    /**
     * 根据订单ID,更新支付相关表中的流水号
     * @param orderId
     */
    public int updatePayment(long orderId,String seriNo);
    
    /**
     * 根据订单ID,更新支付相关表中的流水号,繁体网站
     * @param orderId
     */
    public boolean updatePaymentForBig5(long orderId,String seriNo,long paymentId);
    
    /**
     * 刷畅联的价格 add by shengwei.zuo 2009-12-22
     */
    public String reReshReservateExResponse(long hotelId,long roomTypeId,String childRoomTypeId,int roomChannel,Date inDate,Date outDate) throws Exception;
    /**
     * 刷中旅的价格
     */
    public List<QueryHotelForWebSaleItems>  refreshHotelReservateExResponse(long hotelId,long roomTypeId,Long childRoomTypeId,int channelType,Date inDate,Date outDate)throws Exception;

}
