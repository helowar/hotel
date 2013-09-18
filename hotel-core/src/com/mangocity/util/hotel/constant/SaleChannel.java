package com.mangocity.util.hotel.constant;

import java.io.Serializable;

/**
 * 多渠道预定
 * 
 * @author xieyanhui 2011-07-28
 * 
 */
public class SaleChannel implements Serializable {
    /**
     * 简体网站
     */
    public static String WEB = "1";

    /**
     * 散客CC
     */
    public static String CC = "2";

    /**
     * B2B
     */
    public static String B2B = "4";
    /**
     * 114
     */
    public static String H114 = "8";
    
    /**
     * TMC 网站
     */
    public static String TMCWEB = "16";

    /**
     * TMC CC
     */
    public static String TMCCC = "32";

    /**
     * 套票TP网站
     */
    public static String TPWEB = "64";
    
    /**
     * 套票TP CC
     */
    public static String TPCC = "128";
    
    /**
     * 3G
     */
    public static String G3 = "256";
    
    /**
     * Open API
     */
    public static String OAPI = "512";
    
    /**
     * 芒果快线
     */
    public static String MGEXPRESS = "1024";
    
    /**
     * 繁体网站-traditional Chinese web
     */
    public static String TCWEB = "2048";
    
    /**
     * 嵌套版
     */
    public static String NESTWEB = "4096";
    
    /**
     * 策略合作交行-co Bank of Communication
     */
    public static String COBOC = "8192";
    
    /**
     * 策略合作电商-co E-commerce
     */
    public static String COECOMMERCE = "16384";
    
    /**
     * 网络推广
     */
    public static String EMARKETING = "32768";
    
}
