package com.mangocity.hotel.order.constant;

/**
 * 配送任务类型
 * 
 * @author chenkeming
 * 
 */
public interface FulfillTaskType {

    /**
     * 配送加同一地收款
     */
    public static int DELIVER_PAY_SAME = 1;

    /**
     * 配送加异地收款
     */
    public static int DELIVER_PAY_DIF = 2;

    /**
     * 配送
     */
    public static int DELIVER_ONLY = 3;

    /**
     * 收款(产品提供地与收款地不一致)
     */
    public static int PAY_DIF = 4;

    /**
     * 收款(产品提供地与收款地一致)
     */
    public static int PAY_SAME = 5;
}
