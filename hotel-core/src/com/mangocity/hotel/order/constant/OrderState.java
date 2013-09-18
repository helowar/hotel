package com.mangocity.hotel.order.constant;

/**
 * 订单的状态常量 OrOrder.orderState
 * 
 * @author chenkeming
 * 
 */
public interface OrderState {

    /**
     * 前台订单未提交
     */
    public static int NOT_SUBMIT = 1;

    /**
     * 已提交订单
     */
    public static int HAS_SUBMIT = 2;

    /**
     * 已提交中台
     */
    public static int SUBMIT_TO_MID = 3;

    /**
     * 已入住
     */
    public static int CHECKIN = 4;

    /**
     * 提前退房
     */
    public static int EARLY_QUIT = 5;

    /**
     * 正常退房
     */
    public static int NORMAL_QUIT = 6;

    /**
     * 延住
     */
    public static int EXTEND = 7;

    /**
     * 已付款
     */
    public static int HAS_PAID = 8;

    /**
     * 已创建退款单
     */
    public static int HAS_CREATE_REFUND = 9;

    /**
     * 退款单已审批
     */
    public static int HAS_AUDIT_REFUND = 10;

    /**
     * 财务已退款, 目前暂时不用
     */
    public static int FINANCE_REFUND = 11;

    /**
     * 退款成功
     */
    public static int REFUND_SUCCESS = 12;

    /**
     * NOSHOW
     */
    public static int NOSHOW = 13;

    /**
     * 已撤单
     */
    public static int CANCEL = 14;

}
