package com.mangocity.util.hotel.constant;

import java.io.Serializable;

/**
 * 酒店映射类型
 * 
 * @author chenjiajie
 * 
 */
public interface HotelMappingType extends Serializable {
    /**
     * 酒店
     */
    public static long HOTEL_TYPE = 1;

    /**
     * 房型
     */
    public static long ROOM_TYPE = 2;

    /**
     * 价格计划
     */
    public static long PRICE_TYPE = 3;

    /**
     * 酒店集团
     */
    public static long HOTEL_GROUP_TYPE = 4;
}
