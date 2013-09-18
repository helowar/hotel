package com.mangocity.hotel.base.constant;

/**
 * 订单中台分类类型
 * 
 * @author chenkeming
 * 
 */
public interface HraOrderType {

    /**
     * 芒果网(预付)
     */
    public static int MANGO_PREPAY = 1;

    /**
     * 芒果网(担保)
     */
    public static int MANGO_SURETY = 2;

    /**
     * 芒果网(其他)
     */
    public static int MANGO_NORMAL = 3;

    /**
     * 114(预付)
     */
    public static int PREPAY_114 = 4;

    /**
     * 114(担保)
     */
    public static int SURETY_114 = 5;

    /**
     * 114(其他)
     */
    public static int NORMAL_114 = 6;

    /**
     * 港澳(预付)
     */
    public static int HK_PREPAY = 7;

    /**
     * 港澳(担保)
     */
    public static int HK_SURETY = 8;

    /**
     * 港澳(其他)
     */
    public static int HK_NORMAL = 9;

    /**
     * 中旅
     * 
     * @author chenkeming Mar 12, 2009 3:24:06 PM
     */
    public static int HK_CTS = 90;

    /**
     * 专家组(原 疑难)
     */
    public static int HARD = 0;
    
    /**
     * B2B代理组		 add by shizhongwen 2010.1.27
     */
    public static int AGENT_B2B=93;

    // 屏蔽原因：香港和澳门的订单统一合并为"港澳(预付)，港澳(担保)，港澳(其它)" v2.4.2 by chenjiajie 2008-12-26
    // /**
    // * 澳门(预付)
    // */
    // public static int MA_PREPAY = 90;
    //	
    // /**
    // * 澳门(担保)
    // */
    // public static int MA_SURETY = 91;
    //	
    // /**
    // * 澳门(其他)
    // */
    // public static int MA_NORMAL = 92;
    /**
     * RSC港澳组
     * chenjuesu 2010-1-20
     */
    public static int RSC_GAZ = 10;
    /**
     * RSC深圳组
     * 
     * @author chenjiajie v2.4.2 2008-12-26
     */
    public static int RSC_BBQ = 11;

    /**
     * RSC广州组
     * 
     * @author chenjiajie v2.4.2 2008-12-26
     */
    public static int RSC_GZQ = 12;

    /**
     * RSC华北组
     * 
     * @author chenjiajie v2.4.2 2008-12-26
     */
    public static int RSC_HBQ = 13;

    /**
     * RSC华东组
     * 
     * @author chenjiajie v2.4.2 2008-12-26
     */
    public static int RSC_HDQ = 14;
    /**
     * RSC上海组
     * chenjuesu 2010-1-20
     */
    public static int RSC_SHZ = 15;
    /**
     * RSC江浙组
     * chenjuesu 2010-1-20
     */
    public static int RSC_JJZ = 16;
    /**
     * RSC北京组
     * chenjuesu 2010-1-20
     */
    public static int RSC_BJZ = 17;
    /**
     * RSC北方组
     * chenjuesu 2010-1-20
     */
    public static int RSC_DBZ = 18;
    /**
     * 合约组
     * 
     * @author chenjuesu v2.8.1 2009-05-26
     */
    public static int ORG_CONTRACTOR = 20;
    
    /**
     * TMC组
     */
    public static int TMC = 30;
    
 
}
