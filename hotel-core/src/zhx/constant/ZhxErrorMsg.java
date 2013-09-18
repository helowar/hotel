package zhx.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 中航信接口错误信息常量
 * 
 * @author chenkeming
 */
public class ZhxErrorMsg {

    /**
     * 错误信息常量map
     * 
     * @author chenkeming
     */
    public static Map<String, String> MsgMap = null;

    static {
        MsgMap = new HashMap<String, String>();
        MsgMap.put("11", "渠道号要大于1!");
    }

}
