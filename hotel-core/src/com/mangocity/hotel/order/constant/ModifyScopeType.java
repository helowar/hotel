package com.mangocity.hotel.order.constant;

/**
 * 订单修改类型类型
 * 
 * @author chenkeming
 * 
 */
public interface ModifyScopeType {

    /**
     * 取消
     */
    public static int CANCEL = 1;

    /**
     * 修改
     */
    public static int MODIFY = 2;

    /**
     * 取消/修改
     */
    public static int BOTH = 3;

}
