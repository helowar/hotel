package com.mangocity.hotel.order.constant;

/**
 * 订单操作状态常量 ordailyaudit.status
 * 
 * @author chenkeming
 * 
 */
public interface OrderItemAuditState {

    /**
     * 未操作
     */
    public static int NOT_WORK = 0;

    /**
     * 已保存
     */
    public static int ALREADY_SAVE = 1;

    /**
     * 待审核
     */
    public static int STAY_AUDITING = 2;

    /**
     * 完成
     */
    public static int ACHIEVE = 3;

}
