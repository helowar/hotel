package com.mangocity.hotel.order.constant;
/**
 * 日审常量类
 * @author chenjuesu
 *
 */
public class DailyAuditConstant {
	/**
     *预约时间的回访记录类型
     */
    public static final int DELAY_RETURN_TYPE = 1;
    /**
     *随机分配的回访记录类型
     */
    public static final int RADOM_RETURN_TYPE = 2;
    /**
     *预约时间的日审记录类型
     */
    public static final int DELAY_AUDIT_TYPE = 1;
    /**
     *有回传的日审记录类型
     */
    public static final int RETURN_AUDIT_TYPE = 2;
    /**
     *随机分配的日审记录类型
     */
    public static final int RADOM_AUDIT_TYPE = 3;
    /**
     *日审(回访)获取方式: 自动获取
     */
    public static final int GETAUDIT_AUTO = 1;
    /**
     *日审(回访)获取方式: 手工获取
     */
    public static final int GETAUDIT_BYHAND = 2;
    /**
     *获取状态:已获取
     */
    public static final int HADGOTAUDIT = 1;
    /**
     *获取状态:未获取
     */
    public static final int NOTYETGOTAUDIT = 2;
    /**
     *日审状态:已审核
     */
    public static final int AUDIT_COMPLETE_STATE = 1;
    /**
     *日审状态:未审核
     */
    public static final int AUDIT_NOTCOMPLETE_STATE = 0;
    /**
     * 日审类型:入住
     */
    public static final int CHECKINTYPE = 1;
    /**
     * 日审类型:退房
     */
    public static final int CHECKOUTTYPE = 2;
    /**
     * 日审结果:正常入住
     */
    public static final int NORMAL_CHECKIN = 1;
    /**
     * 日审结果:未入住
     */
    public static final int NOSHOW = 2;
    /**
     * 日审结果:正常退房
     */
    public static final int NORMAL_CHECKOUT = 3;
    /**
     * 日审结果:提前退房
     */
    public static final int BEFORE_CHECKOUT = 4;
    /**
     * 日审结果：延住
     */
    public static final int EXTENDED_STAY = 5;
    /**
     * 日审操作:保存
     */
    public static final int SAVE_OPERATION = 1;
    /**
     * 日审操作:完成
     */
    public static final int COMPLETE_OPERATION = 2;
    /**
     * 日审(回访)操作:释放
     */
    public static final int RELEASE_OPERATION = 3;
    /**
     * 审核类型
     */
    public static final int AUDIT_TYPE = 1;
    /**
     * 回访类型
     */
    public static final int RETURNVISIT_TYPE = 2;
    /**
     * 回访状态：待回访
     */
    public static final int RETURNSTATE_NOTRETURN = 0;
    /**
     * 回访状态：回访完成
     */
    public static final int RETURNSTATE_ACHIEVE = 1;
}
