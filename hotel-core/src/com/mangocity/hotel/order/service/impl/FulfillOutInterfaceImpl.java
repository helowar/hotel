package com.mangocity.hotel.order.service.impl;

import java.io.Serializable;

import com.mangocity.hotel.order.persistence.view.OrOrderVO;
import com.mangocity.hotel.order.service.IFulfillOutInterface;
import com.mangocity.hotel.order.service.IOrderService;

/**
 */
public class FulfillOutInterfaceImpl implements IFulfillOutInterface, Serializable {

    private IOrderService orderService;

    public OrOrderVO loadHotelOrderByOrderCD(final String orderCD) {
        return orderService.getHotelOrderByOrderCD(orderCD);
    }

    public int updateOrderStatus(final String orderCD, final String status) {
        return 0;
    }

    public int updateFulfillStatus(final String orderCD, final String status) {
        return 0;
    }

    public IOrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }

}
