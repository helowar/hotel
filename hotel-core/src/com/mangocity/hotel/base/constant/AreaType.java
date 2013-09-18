package com.mangocity.hotel.base.constant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * CC系统对城市的区域划分
 * 
 * @author chenjiajie v2.4.2 2008-12-26
 * 
 */
public class AreaType implements Serializable {
    /**
     * 华北区
     */
    public static String HBQ = "HBQ";

    /**
     * 华南深圳组
     */
    public static String BBQ = "BBQ";

    /**
     * 广州区
     */
    public static String GZQ = "GZQ";
    /**
     * 港澳组
     */
    public static String GAZ = "GAZ";

    /**
     * 华南香港组
     */
    public static String HNK = "HNK";

    /**
     * 华东区
     */
    public static String HDQ = "HDQ";

    /**
     * 与HraOrderType的RSC组别映射
     */
    public static Map AREA_MAP = new HashMap();
    static {
        AREA_MAP.put(HBQ, HraOrderType.RSC_HBQ);
        AREA_MAP.put(BBQ, HraOrderType.RSC_BBQ);
        AREA_MAP.put(GZQ, HraOrderType.RSC_GZQ);
        AREA_MAP.put(HDQ, HraOrderType.RSC_HDQ);
        AREA_MAP.put(GAZ, HraOrderType.RSC_GAZ);
    }
}
