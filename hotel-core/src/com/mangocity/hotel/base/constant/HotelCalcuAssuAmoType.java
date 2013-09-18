/**
 * 
 */
package com.mangocity.hotel.base.constant;

/**
 * 担保金额的计算规则
 * 
 * @author zhuangzhineng
 * 
 */
public interface HotelCalcuAssuAmoType {

    /**
     * 按check in day 判定
     */
    public static int CHECKIN = 1;

    /**
     * 全额判定
     */
    public static int ALLAMOUNT = 2;

    /**
     * 系统默认累加判定
     */
    public static int TOTTINGUP = 3;
    
    /**
     * 艺龙判定 add by huanglingfeng 2012-5-2
     */
    public static int ELONG_JUDGE=4;

}
