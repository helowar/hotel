package com.mangocity.hotel.order.service;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.service.assistant.BookRoomCondition;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPreSale;
import com.mangocity.hotel.order.persistence.OrPriceDetail;

public interface IHotelReservationInfoService {

	 /**
     * hotel2.6 网站：add by zhineng.zhuang 2009-02-11 酒店2.6网站获取预订条款
     */
    public ReservationAssist getReservationInfoForWeb(
        BookRoomCondition bookRoomCond, OrOrder order);
    
    /**
     * 返回3G的预订条款
     * @param bookRoomCond
     * @param order
     * @return
     */
    public ReservationAssist getReservationInfoFor3G(BookRoomCondition bookRoomCond, OrOrder order);           

    /**
     * 查询酒店这个房型的促销信息 v2.9.2 lixiaoyong 2009-08-12
     */
    public List<OrPreSale> queryRoomPresale(OrOrder order);        

    /**
	* add by zhijie.gu 2009-09-15 hotel2.9.3 改变在优惠条款内的佣金、底价，
	*/
    public int changeBasePriceCommission(HtlPrice htlPrice,List orPriceDetailList,OrPriceDetail orPriceDetailItems,long hotelId,long childRoomTypeId,int y, int f);       

    /**
     * add by ting.li
     * 获取简单的担保条款
     */ 
    public OrReservation getAssureReservation(BookRoomCondition bookRoomCond);
}
