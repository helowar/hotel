package com.mangocity.hotel.base.manage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlIncreasePrice;

/**
 */
public class IncreaseBean implements Serializable {

    /**
     * 加幅记录
     */
    public static Map<String, List<HtlIncreasePrice>> increaseMap = null;
    
    /**
     * 中旅的加幅Map
     */
    public static Map<Long, List<HtlIncreasePrice>> increaseCtsMap = null;

    static {
        increaseMap = new HashMap<String, List<HtlIncreasePrice>>();
        increaseCtsMap = new HashMap<Long, List<HtlIncreasePrice>>();
    }

    /**
     * 德比
     */
    public final static String DEBY = "DEBY";

    /**
     * 畅联
     */
    public final static String COL = "COL";

    /**
     * 香港中科
     */
    public final static String CTS = "CTS";

    /**
     * 德尔
     */
    public final static String DEER = "DEER";

    /**
     * 希尔顿
     */
    public final static String SHELTON = "SHELTON";

    /**
     * 传统渠道
     */
    public final static String TRA = "TRA";
}
