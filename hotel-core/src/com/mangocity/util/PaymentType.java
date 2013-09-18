package com.mangocity.util;

/**
 * 选择的支付方式：1为信用卡，2为现付,3为商旅月结
 * @author:shizhongwen
 * 创建日期:Oct 10, 2009,4:14:57 PM
 * 描述：
 */
public interface PaymentType {

    //信用卡支付
    public static String  CreditCard_Pay="1";
    
    //现付
    public static String Cash_Pay="2";
    
    //商旅月结
    public static String Month_Pay="3";
    
}
