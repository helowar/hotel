package com.mangocity.hotel.order.constant;

/**
 * 订单的紧急程度
 * 
 * @author chenkeming
 * 
 */
public interface EmergencyLevel {

    /**
     * 3-已到店
     */
    public static int ARRIVED = 3;

    /**
     * 6-两小时以内
     */
    public static int IN_TWO_HOURS = 6;
    /**
     *  五星
     */
    public static int VIP4 = 9;

    /**
     * 铂星
     */
    public static int VIP3 = 12;

    /**
     * 金星
     */
    public static int VIP2 = 15;

    /**
     * 银星
     */
    public static int VIP1 = 23;

}
