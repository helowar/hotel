package com.mangocity.hotel.order.constant;

/**
 * 面付订单的信用卡担保状态常量
 * 
 * @author chenkeming
 * 
 */
public interface GuaranteeState {

    /**
     * 未担保
     */
    public static int NOT_GUARANTEE = 1;

    /**
     * 已提交担保信息,未创建预授权工单
     */
    public static int PROCESSING = 2;

    /**
     * 已创建预授权工单,等待担保处理
     */
    public static int PREAUTH = 3;

    /**
     * 已创建预授权工单,担保成功
     */
    public static int SUCCESS = 4;

    /**
     * 已创建预授权工单,担保失败
     */
    public static int FAIL = 5;

    /**
     * 已创建预授权工单,担保未做
     */
    public static int NOT_PROCESS = 6;

    /**
     * 已创建预授权工单,但担保未找到
     */
    public static int NOT_FOUND = 7;

    /**
     * 已撤消担保
     */
    public static int CANCEL = 8;

    /**
     * 已确认扣款金额
     */
    public static int DETAINED = 9;

}
