package com.mangocity.hotel.order.constant;

/**
 * AuditItem(没有部分入住),Audit的状态
 * 
 * @author chenkeming
 * 
 */
public interface AuditItemState {

    /**
     * 已入住
     */
    public static String CHECKIN = "1";

    /**
     * 未入住
     */
    public static String NOSHOW = "2";

    /**
     * 部分入住
     */
    public static String PART_CHECKIN = "3";
}
