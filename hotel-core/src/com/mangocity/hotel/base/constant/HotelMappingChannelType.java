package com.mangocity.hotel.base.constant;

import java.io.Serializable;

/**
 * 酒店映射合作商类型
 * 
 * @author chenjiajie
 * 
 */
public interface HotelMappingChannelType extends Serializable {
    /**
     * 合作商的总数
     */
    public static int TOTEL_CHANNEL_COUNT = 4;

    /**
     * 德比
     */
    public static long DERBY = 1;

    /**
     * 畅联
     */
    public static long CHINA_ONLINE = 2;

    /**
     * 德尔
     */
    public static long DER = 3;

    /**
     * 希尔顿
     */
    public static long HILTOM = 4;
}
