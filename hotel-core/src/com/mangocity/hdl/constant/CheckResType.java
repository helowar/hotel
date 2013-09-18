package com.mangocity.hdl.constant;

/**
 * 试预订结果常量，供HDL和芒果内部交互使用
 * 
 * @author chenkeming
 * 
 */
public interface CheckResType {

    /**
     * 通过
     */
    static int PASS = 1;

    /**
     * 拒绝
     */
    static int REJECT = 0;
    
    /**
     * 酒店级别的试预订数据，-1表示拒绝
     */
    static int HOTEL_REJECT = -1;

    /**
     * add by shizhongwen 2009-2-18 连接失败
     */
    static int CONNECT = -1;

}
