package com.mangocity.hotel.flight.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class CurrencyBean implements Serializable {
    public static Map<String, String> curMap = null;
    static {
    	curMap = new HashMap<String, String>();
    	curMap.put("RMB", "¥");
    	curMap.put("USD", "USD$");
    	curMap.put("HKD", "HK$");
    	curMap.put("EUR", "EUR€");
    	curMap.put("GBP", "GBP$");
    	curMap.put("JPY", "JPY￥");
    	curMap.put("DEM", "DM");
    	curMap.put("MOP", "MOP");
    }
}
