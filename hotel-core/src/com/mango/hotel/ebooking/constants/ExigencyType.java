package com.mango.hotel.ebooking.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 紧急程度
 * 
 * @author chenjiajie
 * 
 */
@SuppressWarnings("unchecked")
public class ExigencyType {

    /**
     * 一般
     */
    public final static long NORMAL = 1L;

    /**
     * 紧急
     */
    public final static long EXIGENCE = 2L;

    /**
     * 紧急程度常量与中文映射
     */
    public final static Map EXIGENCY_TYPE_MAP = new HashMap();

    static {
        EXIGENCY_TYPE_MAP.put(NORMAL, "一般");
        EXIGENCY_TYPE_MAP.put(EXIGENCE, "紧急");
    }

}
