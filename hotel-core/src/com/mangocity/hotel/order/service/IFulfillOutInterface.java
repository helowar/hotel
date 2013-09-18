package com.mangocity.hotel.order.service;

import com.mangocity.hotel.order.persistence.view.OrOrderVO;

/**
 * 配送回调接口
 * 
 * @author chenkeming
 * 
 */
public interface IFulfillOutInterface {

    /**
     * 配送系统调用,获取配送相关信息
     * 
     * @param orderCD
     *            酒店订单编码
     * 
     *            return OrOrderVO
     * */
    public OrOrderVO loadHotelOrderByOrderCD(final String orderCD);

    /**
     * 配送系统调用,修改酒店订单状态
     * 
     * @param orderCD
     *            酒店订单编码
     * 
     *            return int
     * */
    public int updateOrderStatus(final String orderCD, final String status);

    /**
     * 配送系统调用,修改配送订单状态
     * 
     * @param orderCD
     *            酒店订单编码
     * 
     *            return int
     * */
    public int updateFulfillStatus(final String orderCD, final String status);
}
