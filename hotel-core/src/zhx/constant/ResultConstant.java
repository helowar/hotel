package zhx.constant;

/**
 * 调用合作方返回的结果代码 
 * 
 * @author chenkeming
 *
 */
public interface ResultConstant {
	
    /**
     * 返回成功
     */
    public static int RESULT_SUCCESS = 0; 

    /**
     * 合作方返回失败
     */
    public static int RESULT_CHANNEL_ERROR = -1; 

    /**
     * 芒果网系统返回错误
     */
    public static int RESULT_MANGO_ERROR = -2;
    
    /**
     * 调用中出异常
     */
    public static int RESULT_EXCEPTION = -3;

}
