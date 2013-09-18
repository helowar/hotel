package com.mangocity.hotel.order.constant;

/**
 * 订单的信用卡担保类型
 * 
 * @author chenkeming
 * 
 */
public interface GuaranteeType {

    /**
     * 峰时
     */
    public static int PEAK_DAY = 3;

    /**
     * 首日, 担保金额为订单首日费用
     */
    public static int FIRST_DAY = 2;

    /**
     * 全额, 担保金额为订单所有费用
     */
    public static int ALL_DAY = 4;

    /**
     * 最晚担保
     */
    public static int LATE_CHECK_IN = 1;

    /**
     * add by shizhongwen 2009-6-2 酒店2.6用于撤单时担保类型断定 首日担保
     */
    public static String FIRST_ASSURE_TYPE = "首日担保";

    /**
     * add by shizhongwen 2009-6-2 酒店2.6用于撤单时担保类型断定 全额担保
     */
    public static String ALL_ASSURE_TYPE = "全额担保";
}
