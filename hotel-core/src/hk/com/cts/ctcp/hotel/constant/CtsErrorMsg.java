package hk.com.cts.ctcp.hotel.constant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mangocity.util.StringUtil;

/**
 * 中科接口错误信息常量
 * 
 * @author chenkeming Apr 24, 2009 3:08:06 PM
 */
public class CtsErrorMsg implements Serializable {

    /**
     * 错误信息常量map
     * 
     * @author chenkeming Apr 24, 2009 3:08:21 PM
     */
    public static Map<Integer, String> MsgMap = null;

    static {
        MsgMap = new HashMap<Integer, String>();
        MsgMap.put(-111, "配额不足!"); // -111 Not enough room !
    }

    /**
     * 把中科传过来的英文错误信息转成中文信息
     * 
     * @author chenkeming Apr 24, 2009 3:12:09 PM
     * @param engMsg
     * @return
     */
    public static String toChnMsg(String engMsg) {
        if (!StringUtil.isValidStr(engMsg)) {
            return engMsg;
        }
        String subStr = engMsg.substring(0, engMsg.indexOf(' '));
        String chnMsg = MsgMap.get(Integer.valueOf(subStr));
        if (!StringUtil.isValidStr(chnMsg)) {
            return engMsg;
        }
        return chnMsg;
    }
}
