package com.mangocity.hotel.order.constant;

/**
 * 酒店确认类型
 * 
 * @author chenkeming
 * 
 */
public interface HotelConfirmType {

    /**
     * 确认
     */
    public static int CONFIRM = 1;

    /**
     * 修改
     */
    public static int MODIFY = 2;

    /**
     * 取消
     */
    public static int CANCEL = 3;

    /**
     * 续住
     */
    public static int CONTINUE_TO_LIVE = 7;

    /**
     * hotel2.5 特殊要求 add by guojun 2009-03-22 15:42
     */
    public static int CHANNLE_SPECIAL_REQUEST = 9;
}
