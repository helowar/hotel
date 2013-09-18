/**
 * 
 */
package com.mangocity.hotel.order.constant;

/**
 * @author zhuangzhineng 预订修改取消条款类型
 */
public interface HotelModifyOrCancleType {

    /**
     * 都要扣
     */
    public static String ALLDEDUCT = "1";

    /**
     * 从某日期与某日期之间要扣
     */
    public static String DEDUCTBETWEENDATE = "2";

    /**
     * 从某日期与入住日期之间要扣
     */
    public static String DEDUCTBEFORECHECKIN = "3";

    /**
     * 入住日期前N天至M天之间要扣
     */
    public static String DUDUCTBETWEENNUM = "4";

    /**
     * 入住当天某时间点前后要扣
     */
    public static String BEFORECHECKINTIME = "5";

}
