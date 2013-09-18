/**
 * 
 */
package com.mangocity.hk.constant;

/**
 * @author wuyun
 * 
 */
public class HkConstant {

    /**
     * 用于标志在线支付来源，HOTEL表示酒店系统
     */
    public static final String ONLINE_PAY_SOURCE = "HOTEL";

    /**
     * 招商银行 bankId
     */
    public static final String BANK_CMBCHINA = "CMBCHINA-NET";

    /**
     * 农业银行 bankId=ABC-NET
     */
    public static final String BANK_ABC = "ABC-NET";

    /**
     * 失败提示信息
     */
    public static final String REGULAR_EXCEPTION = "支付出现异常!";

    /**
     * 锁定配额失败
     */
    public static final String HOLD_QUOTA_FAIL = "锁定配额失败！";

    /**
     * 支付失败
     */
    public static final String ONLINE_PAY_FIAL = "支付失败!";

    /**
     * 查询交易不成功
     */
    public static final String SALE_FIAL = "支付已经成功，查询香港中科方交易不成功!";

    /**
     * 确认交易不成功
     */
    public static final String CONFIRM_SALE_FIAL = "锁定配额成功，支付已经成功，确认交易不成功!";

    /**
     * 支付验证失败
     */
    public static final String PAY_SIGN_FIAL = "数据验证失败！";

    /**
     * 中旅订单添加入住人失败
     */
    public static final String ADD_CUSTOMER_FIAL = "中旅订单添加入住人失败！";

    /**
     * 查询交易状濁：成功
     */
    public static final String RETURN_SUCCESS = "SUCCESS";

    /**
     * 查询交易状濁：报错
     */
    public static final String RETURN_ERROR = "ERROR";

    /**
     * 查询交易状濁：支付失败
     */
    public static final String RETURN_FAILURE = "FAILURE";

    /**
     * 查询交易状濁：没找到订卿
     */
    public static final String RETURN_NOTFIND = "NOTFIND";

    /**
     * 查询交易状濁：取消
     */
    public static final String RETURN_CANCEL = "CANCEL";

    /**
     * 查询交易状濁：支付丿
     */
    public static final String RETURN_WAITING = "WAITING";

}
