package com.mangocity.hotel.order.persistence.assistant;

import java.io.Serializable;

/**
 * 用于与配送接口之间紧急程度的转换
 * @author wuyun
 *
 */
public class EmergencyUtil implements Serializable {
    
    /**
	 * 非常紧急
	 */
    public static String VMGC = "VMGC";
    
    /**
	 * 紧急
	 */
    public static String MGC = "MGC";
    
    /**
	 * 正常
	 */
    public static String NML = "NML";
    
    public static String convert(int emergency){
        if(15 <= emergency){
            return NML;
        }else if(9 <= emergency){
            return MGC;    
        }else if(3 <= emergency){
            return VMGC;
        }
        return NML;
    }

}
