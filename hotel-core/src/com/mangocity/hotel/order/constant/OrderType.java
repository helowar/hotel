package com.mangocity.hotel.order.constant;

/**
 * 订单类型常量 :(1-mango, 2-114,3-TMC)
 * 
 * @author chenkeming
 * 
 */
public interface OrderType {

///////////////////////////////////////////////////////    
//用于存放订单类型
    /**
     * mango
     */
    public static int TYPE_MANGO = 1; // "1";

    /**
     * 114
     */
    public static int TYPE_114 = 2; // "2";
    
    
    /**
     * TMC
     * @author shizhongwen 2009-09-30
     */
    public static int TYPE_TMC = 3;
    
    /**
     * B2B代理 add by shizhongwen 2010-1-7
     */
    public static int TYPE_B2BAGENT=4;
    
    /**
     * 南航 add by shaojunyang 2010-3-17
     */
    public static int TYPE_NHZY=5;
	
	    /**
     * TMC-V2.0 诺曼底  add by shengwei.zuo 2010-3-16
     */
    public static int TYPE_NORMANDY = 6;
    
    
/////////////////////////////////////////////////
//用于 TBTMCHOTEL_ORDER表中 存放酒店类型    
    
    /**
     * add by shizhongwen 2009-09-30 表示为本部酒店
     */
    public static String TYPE_BASE="base";
    
    /**
     * add by shizhongwen 2009-09-30 表示为三方协议
     */
    public static String TYPE_PROTOCOL="protocol";
    
   /**
    * add by shizhongwen 2009-11-02 表示外购酒店
    */
    public static final String TYPE_OUT = "out";
    
    /**
     * 3G add by haibo.li
     */
    public static final String TYPE_3G = "MTL";

}
