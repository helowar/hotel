package com.mangocity.hotel.order.constant;

/**
 * 酒店的担保条件
 * 
 * @author zhuangzhineng
 * 
 */
public interface HotelVouchCondiction {

    /**
     * 超时担保
     */
    public static int OVERTIMEVOUCH = 1;

    /**
     * 超房数担保
     */
    public static int OVERROOMNUMVOUCH = 2;

    /**
     * 超间夜担保
     */
    public static int OVERNIGHTNUMVOUCH = 3;

    /**
     * 无条件担保
     */
    public static int UNCONDICTIONVOUCH = 8;

}
