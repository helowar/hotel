package com.mangocity.hotel.constant;

/**
 * 信用卡结算方法
 * 
 * @author chenkeming
 * 
 */
public interface BalanceMethod {

    /**
     * GOA 前台现付返佣
     */
    String GOA = "01";

    /**
     * COA 客人到之前
     */
    String COA = "02";

    /**
     * COD 退房前付
     */
    String COD = "03";

    /**
     * SEND BILL 月结房费
     */
    String SEND_BILL = "04";
}
