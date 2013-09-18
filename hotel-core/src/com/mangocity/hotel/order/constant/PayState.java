package com.mangocity.hotel.order.constant;

/**
 * 付款状态常量(最长不能超过20字符)
 * 
 * @author chenkeming
 * 
 */
public interface PayState {

    // 客户已付款
    public static String HAVE_PAY = "have_pay";

    // 待客户付款
    public static String NO_PAY = "no_pay";

    // 待付款给供应商
    public static String NOT_PAY_HOTEL = "not_pay_hotel";

    // 已付款给供应商
    public static String HAVE_PAY_HOTEL = "have_pay_hotel";
}
