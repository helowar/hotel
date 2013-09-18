package com.mangocity.hotel.order.constant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mangocity.hotel.base.constant.HraOrderType;

/**
 * 房控疑难组别的区域划分
 * 
 * @author chenjuesu 2010-1-20
 * 
 */
public class RSCGroupType implements Serializable {
    /**
     * 港澳组
     */
    public static String GAZ = "GAZ";
    /**
     * 深圳组
     */
    public static String SZZ = "SZZ";
    /**
     * 广州组
     */
    public static String GZZ = "GZZ";
    /**
     * 上海组
     */
    public static String SHZ = "SHZ";
    /**
     * 江浙组
     */
    public static String JJZ = "JJZ";
    /**
     * 北京组
     */
    public static String BJZ = "BJZ";
    /**
     * 北方组
     */
    public static String DBZ = "DBZ";
    /**
     * 与HraOrderType的RSC组别映射
     */
    public static Map RSCGroup = new HashMap();
    
    static {
    	
    	RSCGroup.put(GAZ, HraOrderType.RSC_GAZ);
    	RSCGroup.put(SZZ, HraOrderType.RSC_BBQ);
    	RSCGroup.put(GZZ, HraOrderType.RSC_GZQ);
    	RSCGroup.put(SHZ, HraOrderType.RSC_SHZ);
    	RSCGroup.put(JJZ, HraOrderType.RSC_JJZ);
    	RSCGroup.put(BJZ, HraOrderType.RSC_BJZ);
    	RSCGroup.put(DBZ, HraOrderType.RSC_DBZ);
    	
    }
}