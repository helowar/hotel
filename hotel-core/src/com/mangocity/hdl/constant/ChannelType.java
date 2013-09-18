package com.mangocity.hdl.constant;

/**
 * 渠道常量
 * 
 * @author chenkeming
 * 
 */
public interface ChannelType {

    /**
     * 合作商的总数 by chenjiajie (HK不计算在内)
     */
    public static int TOTEL_CHANNEL_COUNT = 4;

    /*************** 芒果网内部使用的各个第三方渠道编码 begin *********************/

    /**
     * 德比
     */
    static int CHANNEL_DERBEY = 1;

    /**
     * 畅联
     */
    static int CHANNEL_CHINAONLINE = 2;

    /**
     * 德尔
     */
    static int CHANNEL_DELL = 3;

    /**
     * 希尔顿
     */
    static int CHANNEL_SHELLTON = 4;
    
    
    /**
     * add by shizhongwen 2009-12-08 
     * 格林豪泰
     */
    static int CHANNEL_GLHT=5;
    
    /**
     * add by shengwei.zuo 2010-11-08 
     * 锦江集团
     */
    static int CHANNEL_JREZ = 6;
    
    /**
     * add by chenkeming
     * 中航信
     */
    static int CHANNEL_ZHX = 7;

    /**
     * 中旅
     * 
     * @author chenkeming Mar 12, 2009 3:30:49 PM 因resourceDescr.xml中对应的值改为8，并且已经发布到测试环境， 所以改为8
     *         modify by chenjiajie V2.8 2009-03-16
     */
    
    static int CHANNEL_CTS = 8;
    /**
     * 艺龙 
     */
    static int CHANNEL_ELONG = 9;

    /*************** 芒果网内部使用的各个第三方渠道编码 end *********************/

    /*************** 各个第三方渠道走传统渠道下单编码(规则:渠道编码+50) begin *********************/

    /**
     * 直连酒店传统渠道下单编码增加
     */
    static int ORI_ADD = 50;

    /**
     * 德比走传统渠道下单
     */
    static int CHANNEL_DERBEY_ORI = CHANNEL_DERBEY + ORI_ADD;

    /**
     * 畅联走传统渠道下单
     */
    static int CHANNEL_CHINAONLINE_ORI = CHANNEL_CHINAONLINE + ORI_ADD;

    /**
     * 德尔走传统渠道下单
     */
    static int CHANNEL_DELL_ORI = CHANNEL_DELL + ORI_ADD;

    /**
     * 希尔顿走传统渠道下单
     */
    static int CHANNEL_SHELLTON_ORI = CHANNEL_SHELLTON + ORI_ADD;

    /**
     * 中旅走传统渠道下单
     * 
     * @author chenkeming Mar 12, 2009 3:31:16 PM
     */
    static int CHANNEL_CTS_ORI = CHANNEL_CTS + ORI_ADD;

    /*************** 各个第三方渠道走传统渠道下单编码(规则:渠道编码+50) end *********************/
}
