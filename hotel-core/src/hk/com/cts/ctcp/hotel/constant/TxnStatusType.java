package hk.com.cts.ctcp.hotel.constant;

/**
 * 查询中旅订单当前状态常量类
 * 
 * @author chenkeming Mar 27, 2009 10:47:11 AM
 */
public class TxnStatusType {

    /**
     * 初始
     * 
     * @author chenkeming Mar 27, 2009 10:47:29 AM
     */
    public static int Default = 0;

    /**
     * 可以提交
     * 
     * @author chenkeming Mar 27, 2009 10:47:29 AM
     */
    public static int Begin = 1;

    /**
     * 已经提交
     * 
     * @author chenkeming Mar 27, 2009 10:47:38 AM
     */
    public static int Commited = 2;

    /**
     * 超时等原因已经撤销
     * 
     * @author chenkeming Mar 27, 2009 10:47:44 AM
     */
    public static int Rollbacked = 3;

    /**
     * 可以提交
     * 
     * @author chenkeming Mar 27, 2009 10:47:29 AM
     */
    public static String sBegin = "N";

    /**
     * 已经提交
     * 
     * @author chenkeming Mar 27, 2009 10:47:38 AM
     */
    public static String sCommited = "C";

    /**
     * 超时等原因已经撤销
     * 
     * @author chenkeming Mar 27, 2009 10:47:44 AM
     */
    public static String sRollbacked = "R";
}
