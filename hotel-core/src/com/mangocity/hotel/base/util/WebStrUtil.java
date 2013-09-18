package com.mangocity.hotel.base.util;

import com.mangocity.util.StringUtil;

/**
 * 网站字符串解析工具类
 * 
 * @author chenkeming
 * 
 */
public class WebStrUtil {

    /**
     * 根据房态字符串和配额数量设置网站床型
     * 
     * @param roomState
     * @param qty
     * @return
     */
    public static String getWebRoomStatue(String roomState, int qty) {
        String[] str = roomState.split("/");
        String res = "";
        String res1 = "";
        for (int i = 0; i < str.length; i++) {
            String[] testStr2 = str[i].split(":");
            if (testStr2[0].equals("1")) {
                if (0 < res1.length()) {
                    res1 += "/大";
                } else {
                    res1 += "大";
                }
                if (!testStr2[1].equals("4") && !(testStr2[1].equals("3") && 0 == qty)) { // 大
                    if (0 < res.length()) {
                        res += "/大";
                    } else {
                        res += "大";
                    }
                }
            } else if (testStr2[0].equals("2")) {
                if (0 < res1.length()) {
                    res1 += "/双";
                } else {
                    res1 += "双";
                }
                if (!testStr2[1].equals("4") && !(testStr2[1].equals("3") && 0 == qty)) { // 双
                    if (0 < res.length()) {
                        res += "/双";
                    } else {
                        res += "双";
                    }
                }
            } else if (testStr2[0].equals("3")) {
                if (0 < res1.length()) {
                    res1 += "/单";
                } else {
                    res1 += "单";
                }
                if (!testStr2[1].equals("4") && !(testStr2[1].equals("3") && 0 == qty)) { // 单
                    if (0 < res.length()) {
                        res += "/单";
                    } else {
                        res += "单";
                    }
                }
            }
        }
        if (1 > res.length()) {
            return res1;
        } 
        return res;
    }
    
    
    /**
     * 根据房态字符串设置网站房型对应的床型列表
     * add by shengwei.zuo 2009-10-26
     * @param roomState
     * @param qty
     * @return
     */
    public static String getWebRoomBedType(String roomState) {
        String[] str = roomState.split("/");
        String bedTypeStr = "";
        for (int i = 0; i < str.length; i++) {
        	
            String[] testStr2 = str[i].split(":");
            
            bedTypeStr+=(i>0?",":"")+testStr2[0];
  
        }
 
        return bedTypeStr;
    }

    /**
     * 根据早餐数量设置网站早餐字符串
     * 
     * @param breakfastNum
     * @return
     */
    public static String getWebBreakfastStr(int breakfastNum) {
        String temp = "";
        switch (breakfastNum) {
        case 0:
            temp = "无早";
            break;
        case 1:
            /* falls through */
        case -1:
            temp = "单早";
            break;
        case 2:
            temp = "双早";
            break;
        case 3:
            temp = "三早";
            break;
        case 4:
            temp = "四早";
            break;
        case 5:
            temp = "五早";
            break;
        case 6:
            temp = "六早";
            break;
        case 7:
            temp = "七早";
            break;
        case 8:
            temp = "八早";
            break;
        case 9:
            temp = "九早";
            break;
        case 10:
            temp = "十早";
            break;
        }
        return temp;
    }

    /**
     * 网站根据房态字符串获取房间的最高房态
     * 
     * @param roomState
     * @return
     */
    public static String strRoomStatue(String roomState) {
        String testStr = new String(roomState);
        String[] str = testStr.split("/");
        int t = 10;
        for (int i = 0; i < str.length; i++) {
            String[] testStr2 = str[i].split(":");
            for (int j = 1; j < testStr2.length; j += 2) {
                try {
                    int status = Integer.parseInt(testStr2[j]);
                    if (t > status) {
                        t = status;
                    }
                } catch (Exception ex) {
                    return "0";
                }
            }
        }
        return String.valueOf(t);
    }

    /**
     * 根据房态字符串和配额数量转换成订单详情中的房态字符串
     * 
     * @author chenkeming Jun 24, 2009 9:14:53 AM
     * @param roomStatus
     * @param quotaAmount
     * @return
     */
    public static String showRoomType(String roomStatus, int quotaAmount) {
        String str = "";
        String returnstr = "";
        // 解析房态1:0/2:-1/3:4
        String[] roomArray = roomStatus.split("/");
        for (int i = 0; i < roomArray.length; i++) {
            String[] rsbArray = roomArray[i].split(":");
            if (StringUtil.isValidStr(rsbArray[0]) && !"-1".equals(rsbArray[0])) {
                str = getResourceValue("bedTypeForCC", rsbArray[0]);
            }
            if (StringUtil.isValidStr(rsbArray[1]) && !"-1".equals(rsbArray[1])
                && !"null".equals(rsbArray[1])) {
                // 当房态不可超且配额为0时，房态显示满房。
                if ("3".equals(rsbArray[1]) && 0 == quotaAmount) {
                    // add by shengwei.zuo 2009-05-12 hotel 2.6 房态不可超且配额为0时，页面房态字体显示颜色为红色的BUG修复
                    // begin
                    // modify by shizhongwen 2009-05-21 hotel2.6
                    // 将getResourceValue("select_roomStatusForCC", 4)
                    // 修改为getResourceValue("select_roomStatusForCC", "4")
                    str = "<font color=red>" + str + ":"
                        + getResourceValue("select_roomStatusForCC", "4") + "</font>,";
                    // add by shengwei.zuo 2009-05-12 hotel 2.6 房态不可超且配额为0时，页面房态字体显示颜色为红色的BUG修复 end
                } else if ("4".equals(rsbArray[1])) {
                    // add by shengwei.zuo 2009-05-12 hotel 2.6 房态为满房时，页面房态字体显示颜色为红色的BUG修复 begin
                    str = "<font color=red>" + str + ":"
                        + getResourceValue("select_roomStatusForCC", rsbArray[1]) + "</font>,";
                    // add by shengwei.zuo 2009-05-12 hotel 2.6 房态为满房时，页面房态字体显示颜色为红色的BUG修复 end
                } else {
                    str += ":" + getResourceValue("select_roomStatusForCC", rsbArray[1]) + ",";
                }
            } else {
                // 当为请选择时，默认为良 modify by zhineng.zhuang 2008-11-05
                str += ":良,";
            }
            returnstr += str;
        }
        return returnstr;
    }

    /**
     * 根据基础数据类型返回相关的中文描述
     * 
     * @author chenkeming Jun 24, 2009 9:15:35 AM
     * @param resourceID
     * @param key
     * @return
     */
    public static String getResourceValue(String resourceID, String key) {
        String value = "";
        // 仅供呼叫中心早餐类型显示和本部进行了对应绑定
        if ("breakfast_typeForCC".equals(resourceID)) {
            if ("1".equals(key)) {
                value = "中";
            } else if ("2".equals(key)) {
                value = "西";
            } else if ("3".equals(key)) {
                value = "自";
            } else {
                value = "中";
            }
            return value;
        }
        // 仅供呼叫中心房态显示和本部进行了对应绑定
        if ("select_roomStatusForCC".equals(resourceID)) {
            if ("0".equals(key)) {
                value = "F";
            } else if ("1".equals(key)) {
                value = "良";
            } else if ("2".equals(key)) {
                value = "紧";
            } else if ("3".equals(key)) {
                value = "不";
            } else if ("4".equals(key)) {
                value = "满";
            } else {
                value = "";
            }
            return value;
        }
        // 仅供呼叫中心床型显示和本部进行了对应绑定
        if ("bedTypeForCC".equals(resourceID)) {
            if ("1".equals(key)) {
                value = "大";
            } else if ("2".equals(key)) {
                value = "双";
            } else if ("3".equals(key)) {
                value = "单";
            } else {
                value = "";
            }
        }
        // 早餐数量
        if ("breakfast_num_new".equals(resourceID)) {
            if ("0".equals(key)) {
                value = "不含";
            } else if ("-1".equals(key)) {
                value = "人数";
            } else {
                value = key;
            }
        }
        return value;
    }
    
    /**
     * 根据床型字符串(如"1,2")转成中文
     * @author chenkeming 
     * @param bedTypeStr
     * @return
     */
    public static String getTmcBedType(String bedTypeStr) {
        if(!StringUtil.isValidStr(bedTypeStr)) {
            return "";
        }
        String[] bedStrs = bedTypeStr.split(",");
        String res = "";
        for (int i = 0; i < bedStrs.length; i++) {
            String bedStr = bedStrs[i];
            if ("1".equals(bedStr)) {
                if (0 < i) {
                    res += "/大";
                } else {
                    res += "大";
                }
            } else if ("2".equals(bedStr)) {
                if (0 < i) {
                    res += "/双";
                } else {
                    res += "双";
                }
            } else {
                if (0 < i) {
                    res += "/单";
                } else {
                    res += "单";
                }
            }
        }
        return res + "床";
    }
}
