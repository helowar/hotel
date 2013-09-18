/**
 * 
 */
package com.mangocity.hotel.order.service;

import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.order.persistence.OrOrder;

/**
 * @author zhuangzhineng
 * 
 */
public interface IcalculatePriceService {

    /**
     * 计算订单担保金额 add by zhineng.zhuang 2009-02-12
     * 
     * @return
     */
    public double getcaluclateVouchPrice(ReservationAssist reservationAssist, OrOrder orOrder);

}
