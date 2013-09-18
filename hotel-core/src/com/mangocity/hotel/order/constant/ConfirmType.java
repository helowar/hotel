package com.mangocity.hotel.order.constant;

/**
 * 客户确认方式
 * 
 * @author chenkeming
 * 
 */
public interface ConfirmType {

    /**
     * 传真
     */
    public static int FAX = 1;

    /**
     * 电邮
     */
    public static int EMAIL = 2;

    /**
     * 短信
     */
    public static int SMS = 3;

    /**
     * 电话
     */
    public static int PHONE = 4;

    /**
     * 不用确认
     */
    public static int NO = 5;

    /**
     * 直连
     */
    public static int DIRECT = 6;
    
    /**
     * EBooking
     */
    public static int EBOOKING = 7;
}
