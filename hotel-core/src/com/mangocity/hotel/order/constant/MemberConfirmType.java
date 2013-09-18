package com.mangocity.hotel.order.constant;

/**
 * 客户确认类型
 * 
 * @author chenkeming
 * 
 */
public interface MemberConfirmType {

    /**
     * 确认
     */
    public static int CONFIRM = 1;

    /**
     * 修改
     */
    public static int MODIFY = 2;

    /**
     * 取消
     */
    public static int CANCEL = 3;

    /**
     * 暂缓确认
     */
    public static int DELAY = 4;

    /**
     * 提前退房
     */
    public static int ADVANCE = 5;

    /**
     * 无法联系
     */
    public static int LOST = 6;

    /**
     * 日审 add by chenjiajie V2.7.1 2009-02-17
     */
    public static int AUDIT = 7;
}
