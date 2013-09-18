package com.mangocity.hotel.constant;

/**
 * 信用卡修订取消/修改类型
 * 
 * @author chenkeming
 * 
 */
public interface CardOperateType {

    /**
     * 取消
     */
    String CANCEL = "01";

    /**
     * 修改
     */
    String MODIFY = "02";

    /**
     * 取消或修改
     */
    String BOTH = "03";
}
