package hk.com.cts.ctcp.hotel.constant;

/**
 */
public class ResultConstant {
    public static int RESULT_SUCCESS = 0; // 大于0 返回成功

    public static int RESULT_FAIL = -1;// 返回失败

    /**
     * cc查询页面,调中科接口查询配额,cutoff time过期ret返回本值
     * 
     * @author chenkeming Apr 17, 2009 3:31:41 PM
     */
    public static int CHK_RESULT_CUTOFF_TIME = -2;
    
    /**
     * 房型无房
     */
    public static int CHK_RESULT_ROOM_NO_SCHEDULE = -9;
    
    /**
     * 酒店无安排
     */
    public static int CHK_RESULT_HOTEL_NO_SCHEDULE = -10;
    
    /**
     * 可订
     */
    public static int CHK_RESULT_CAN_BOOK = 0;
    
    /**
     * 不可订
     */
    public static int CHK_RESULT_CANT_BOOK = -1;

    public static String DATEFORMAT_CUTOFF = "yyyyMMddHHmm";// 截止时间模式

    public static String DATEFOMAT_HK = "yyyyMMdd";

    public static String DATEFOMAT_MANGO = "yyyy-MM-dd";

}
