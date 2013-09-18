package com.mangocity.tmchotel.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.tmchotel.persistence.HdlPriceUtil;
import com.mangocity.tmchotel.persistence.TmcDateWeekUtil;
import com.mangocity.tmchotel.persistence.HotelOrderFromBean;
import com.mangocity.tmchotel.persistence.HtlCalendarHelperBean;

/**
 * 下单的service
 * @author zuoshengwei
 *
 */
public interface HotelBookingService {
	
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
     * 根据合同ID，入住日期，离店日期查询出 房费税费设定的记录  add by shengwei.zuo 2009-12-1
     */
    public  HtlTaxCharge  getHaveTaxCharge(Long contractId,Date beginDate,Date endDate);
    
    /**
     * 根据订单ID,更新支付相关表中的流水号
     * @param orderId
     */
    public int updatePayment(long orderId,String seriNo);
    
    /**
     * 刷畅联的价格 add by shengwei.zuo 2009-12-22
     */
    public String reReshReservateExResponse(long hotelId,long roomTypeId,String childRoomTypeId,int roomChannel,Date inDate,Date outDate);
    
	/***
	 * 获取页面上日期，星期，价格，早餐分数的显示
	 * add by shengwei.zuo  TMC-v2.0 2010-3-12 
	 * 
	 */
    public TmcDateWeekUtil  getDateWeekStrLst(HtlCalendarHelperBean calendarHelperEnty);
    
    /**
     * 根据订单金额计算出返回的金额 TMC-V2.0 add by shengwei.zuo 2010-3-16
     */
    public long getCashBackNum(HttpServletRequest request,Double priceNum,boolean isCashBA);
    
    /**
     * *
     * 刷畅联的价格返回实体类 TMC-V2.0  add by shengwei.zuo 2010-4-6
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @param inDate
     * @param outDate
     * @param request
     * @return
     */
    public HdlPriceUtil  reReshReseExHtlPrice(long hotelId,long roomTypeId,String childRoomTypeId,int roomChannel,Date inDate,Date outDate,
    		HttpServletRequest request);
}
