/**
 * 
 */
package com.mangocity.hotel.order.constant;

/**
 * 酒店付款时限类型
 * 
 * @author zhuangzhineng
 * 
 */
public interface HotelPayLimitType {

    /**
     * 在某日期之前预付
     */
    public static String BEFOREDATE = "0";

    /**
     * 提前几天预付
     */
    public static String BEFOREDATENUM = "1";

    /**
     * 在订单确认后几天预付
     */
    public static String AFTERCONFIRMDATENUM = "2";

}
