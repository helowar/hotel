/**
 * 
 */
package com.mangocity.hotel.order.constant;

/**
 * @author zhuangzhineng
 * 
 */
public interface HotelModifyCancel {

    /**
     * 均需扣取
     */
    public static String ALLNEEDCOST = "1";

    /**
     * 日期到日期
     */
    public static String DATETODATE = "2";

    /**
     * 日期到入住当日
     */
    public static String DATETOCHECDATE = "3";

    /**
     * 前几天到前几天
     */
    public static String DATENUMTONUM = "4";

    /**
     * 入住当天某时间
     */
    public static String CHECKINDATETIME = "5";

    /**
     * 修改
     */
    public static String MODIFY = "1";

    /**
     * 取消
     */
    public static String CANCEL = "2";

    /**
     * 取消/修改
     */
    public static String MODIORCANC = "3";

    /**
     * 取消修改条款带日期
     */
    public static String MODIORCANWITHDATE = "4";

}
