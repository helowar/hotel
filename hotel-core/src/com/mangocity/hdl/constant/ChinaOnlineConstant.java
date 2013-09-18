package com.mangocity.hdl.constant;

import java.io.Serializable;

/**
 * ChinaOnline常量
 * 
 * @author chenjiajie
 * 
 */
public interface ChinaOnlineConstant extends Serializable {
    // 查询历史订单信息的订单过滤类型
    public static String RESERVATIONSTATUS_AL = "ALL";

    public static String RESERVATIONSTATUS_RESERVED = "RESERVED";

    public static String RESERVATIONSTATUS_NO_SHOW = "NO SHOW";

    public static String RESERVATIONSTATUS_CHECKED_IN = "CHECKED IN";

    public static String RESERVATIONSTATUS_CHECKED_OUT = "CHECKED OUT";

    public static String RESERVATIONSTATUS_CANNELLED = "CANNELLED";

    /** 担保类型 begin **/
    // 6PM-无担保
    public static String NO_UARANTEE = "6PM";

    // CCGTD-信用卡担保
    public static String CREDIT_CARD_UARANTEE = "CCGTD";

    // TAGTD-公司担保
    public static String COMPANY_UARANTEE = "TAGTD";
    /** 担保类型 end **/
}
