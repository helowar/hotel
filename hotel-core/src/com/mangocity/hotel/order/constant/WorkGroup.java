package com.mangocity.hotel.order.constant;

/**
 * 操作员工作组
 * 
 * @author chenkeming
 * 
 */
public interface WorkGroup {

    /**
     * 芒果网(预付)
     */
    public static int MANGO_PREPAY = 1;

    /**
     * 芒果网(担保)
     */
    public static int MANGO_SURETY = 2;

    /**
     * 芒果网(其他)
     */
    public static int MANGO_NORMAL = 3;

    /**
     * 114(预付)
     */
    public static int PREPAY_114 = 4;

    /**
     * 114(担保)
     */
    public static int SURETY_114 = 5;

    /**
     * 114(其他)
     */
    public static int NORMAL_114 = 6;

    /**
     * 香港(预付)
     */
    public static int HK_PREPAY = 7;

    /**
     * 香港(担保)
     */
    public static int HK_SURETY = 8;

    /**
     * 香港(其他)
     */
    public static int HK_NORMAL = 9;

    /**
     * 疑难
     */
    public static int HARD = 0;

}
