package com.mangocity.hotel.order.constant;

/**
 * OrOrderItem类型：1order（正常订单明细）、2red（红单）、3add（冲后附加单）
 * 
 * @author chenkeming
 * 
 */
public interface OrderItemType {

    /**
     * 正常订单明细
     */
    public static int NORMAL = 1;

    /**
     * 红单
     */
    public static int RED = 2;

    /**
     * 冲后附加单
     */
    public static int ADD = 3;
}
