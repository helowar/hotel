package com.mangocity.hotel.order.constant;

/**
 * 预订条款取消修改的扣款类型
 * 
 * @author chenkeming
 * 
 */
public interface DeductType {

    /**
     * 首日金额
     */
    public static int FIRST_DAY = 1;

    /**
     * 全额金额
     */
    public static int ALL_DAY = 2;

    /**
     * 现金
     */
    public static int CASH = 3;

    /**
     * 首日金额百分比
     */
    public static int FIRST_DAY_PERCENTAGE = 4;

    /**
     * 单日最高房价
     */
    public static int HIGHEST = 5;

    /**
     * 全额金额百分比
     */
    public static int ALL_DAY_PERCENTAGE = 6;

}
