package com.mangocity.util.bean;

import java.io.Serializable;

/**
 */
public class HotelBaseConstantBean implements Serializable {

    /*
     * 支付方式－－预付
     */
    public final static String PREPAY = "pre_pay";

    /*
     * 支付方式－－面付
     */
    public final static String PAY = "pay";

    /*
     * 配额类型--包房配额
     */
    public final static String CHARTERQUOTA = "2";

    /*
     * 配额类型－－普通配额
     */
    public final static String GENERALQUOTA = "1";

    /*
     * 配额类型－－临时配额
     */
    public final static String TEMPQUOTA = "3";

    /*
     * 配额类型－－呼出配额
     */
    public final static String OUTSIDEQUOTA = "4";

    /*
     * 配额表中配额共享方式--预付独占
     */
    public final static String QUOTASHARETYPEPREPAY = "2";

    /*
     * 配额表中配额共享方式--面付独占
     */
    public final static String QUOTASHARETYPEPAY = "1";

    /*
     * 配额表中配额共享方式--面预共享
     */
    public final static String QUOTASHARETYPE = "3";

    /*
     * 配额模式－－在店
     */
    public final static String QUOTAPATTERNSI = "S-I";

    /*
     * 配额模式－－进店
     */
    public final static String QUOTAPATTERNCI = "C-I";

    /*
     * CUTOFFDAY表中是否过期标志 A有效没过期
     */
    public final static String CUTOFFDAYSTATEA = "A";

    /*
     * CUTOFFDAY表中是否过期标志 B无效过期
     */
    public final static String CUTOFFDAYSTATEB = "B";

    /*
     * CUTOFFDAY表中时间分割符
     */
    public final static String CUTOFFDAYTIME = ":";

    // 配额会员类型 "1" "CC"
    public final static String CC = "1";

    // 配额会员类型 "2" "TMC"
    public final static String TMC = "2";

    // 配额会员类型 "3" "B2B"
    public final static String B2B = "3";

    // 配额会员类型 "4" "TP"
    public final static String TP = "4";
    
    // 配额调整类型CC预订
    public final static String JUDGE_TYPE_CC_ADD = "ORDER";
    
    // 配额调整类型CC取消预订
    public final static String JUDGE_TYPE_CC_CANCEL = "CANCEL";
    
    // 配额批量调整时新增或临时配额的新增
    public final static String JUDGE_TYPE_ADD = "ADD";
    
    // 配额批量调整时减少或临时配额的减少
    public final static String JUDGE_TYPE_DEC = "DEC";
    
    //临时配额只修改cotofftime
    public final static String JUDGE_TYPE_UTP = "UPT";
    
    //扣配额LOG日志
    public final static String DEDUCT_QUOTA = "DEDUCT";
    
    //退配额LOG日志
    public final static String RETURN_QUOTA = "RETURN";

}
