package com.mangocity.hotel.order.constant;

/**
 * 订单来源常量
 * 
 * @author chenkeming
 * 
 */
public interface OrderSource {

    /**
     * 电话, CCmango
     */
    public static String FROM_PHONE = "PHE"; // "1";

    /**
     * 网站
     */
    public static String FROM_WEB = "NET"; // "2";
    
    /**
     * B2B代理 add by shengwei.zuo  2010-1-19
     */
    public static String FROM_B2B = "B2B"; // 

    /**
     * 香港组
     */
    public static String FROM_HK = "HKT"; // "2";
    /**
     * 3G客户
     */
    public static String FROM_MTL = "MTL";
    /**
     * 繁体网站
     */  
    public static String FAN_TI_NET = "FTNET";
    
    /**
     * 去哪儿
     */
    public static String FROM_GOTOWHERE = "QUNAR";
    
    /**
     * 魅影
     */
    public static String FROM_SHADOW = "MYB2B";
	
	/**
     * 酒店地图
     */
    public static String FROM_EMAP = "hotelEmap";
}
