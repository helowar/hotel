package com.mangocity.hotel.order.constant;

/**
 * 给客人发短信，短信在unitcall的状态
 * 
 * @author chenjiajie
 * @version V2.7.1
 * 
 */
public interface MemberConfirmSmsStutas {
    /**
     * 等待（业务系统点击发送按钮）
     */
    public static int SENDING = 1;

    /**
     * 成功（收到unicall发来的发送成功的标识）
     */
    public static int SUCCESS = 2;

    /**
     * 失败（收到unicall发来的发送失败的标识）
     */
    public static int FAILED = 3;

    /**
     * 已确认（短信服务商的服务器对该短信没有返回任何信息）
     */
    public static int CONFIRM = 4;

    /**
     * 失败已确认（点击重发按钮后的状态）
     */
    public static int FAILED_CONFIRM = 5;

    /**
     * 其他
     */
    public static int OTHER = 6;
}
