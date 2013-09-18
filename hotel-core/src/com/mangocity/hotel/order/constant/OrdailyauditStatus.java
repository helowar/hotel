package com.mangocity.hotel.order.constant;

/**
 * 酒店操作状态常量 ordailyaudit.status
 * 
 * @author chenkeming
 * 
 */
public interface OrdailyauditStatus {

    /**
     * 未回复
     */
    public static int NOT_REVERT = 0;

    /**
     * 已回复
     */
    public static int ALREADY_REVERT = 1;

    /**
     * 待审核
     */
    public static int STAY_AUDITING = 2;

    /**
     * 完成
     */
    public static int ACHIEVE = 3;

}
