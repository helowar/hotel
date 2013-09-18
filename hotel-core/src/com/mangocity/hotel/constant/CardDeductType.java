package com.mangocity.hotel.constant;

/**
 * 取消/修改扣款类型
 * 
 * @author chenkeming
 * 
 */
public interface CardDeductType {

    /**
     * 首日金额
     */
    String FIRST_DAY = "01";

    /**
     * 全额金额
     */
    String ALL = "02";

    /**
     * 最高价格百分比
     */
    String TOP_RATE = "03";

    /**
     * 首日百分比
     */
    String FIRST_RATE = "04";
}
