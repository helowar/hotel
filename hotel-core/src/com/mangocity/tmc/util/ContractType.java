package com.mangocity.tmc.util;

/**
 * 合同类型定义
 * 
 * @author:shizhongwen 创建日期:Jul 27, 2009,3:30:34 PM 描述：
 */
public class ContractType {
    // ---------------------合同类型------------------------------
    /**
     * 服务合同.
     */
    public static final String COMPANY_CONTRACT = "C";

    /**
     * 酒店协议合同.
     */
    public static final String HOTEL_CONTRACT = "H";

    // -----------------------服务合同收费类别--------------------------------------
    // 酒店
    public static final long CHARGE_TYPE1 = 1;
    // 机票
    public static final long CHARGE_TYPE2 = 2;
    // 其他
    public static final long CHARGE_TYPE3 = 3;

    // -----------------------收费项目--------------------------------------
    // 协议酒店
    public static final long CHARGE_ITEM11 = 11;
    // 系统酒店
    public static final long CHARGE_ITEM12 = 12;
    // 外购酒店
    public static final long CHARGE_ITEM13 = 13;
    // 国内机票
    public static final long CHARGE_ITEM21 = 21;
    // 国际机票通用航线
    public static final long CHARGE_ITEM22 = 22;
    // 国际机票区分航线
    public static final long CHARGE_ITEM23 = 23;
    // 国际机票金额
    public static final long CHARGE_ITEM24 = 24;
    // 火车
    public static final long CHARGE_ITEM31 = 31;
    // 租车
    public static final long CHARGE_ITEM32 = 32;
    // 签证
    public static final long CHARGE_ITEM33 = 33;
    // 度假
    public static final long CHARGE_ITEM34 = 34;
    // 其他
    public static final long CHARGE_ITEM35 = 35;

    // -----------------------结算方式--------------------------------------
    // 底价加服务费
    public static final long FOOT_WAY1 = 2;
    // 卖价返点
    public static final long FOOT_WAY2 = 1;

    // -----------------------结算单位--------------------------------------
    // 间/夜
    public static final long FOOT_UNIT1 = 1;
    // 交易
    public static final long FOOT_UNIT2 = 2;
    // 航程
    public static final long FOOT_UNIT3 = 3;

    //	-----------------------结算费用方式--------------------------------------
    //	百分比 
    public static final long FOOT_FEE_WAY1 = 1;
    //绝对值
    public static final long FOOT_FEE_WAY2 = 2;
}
