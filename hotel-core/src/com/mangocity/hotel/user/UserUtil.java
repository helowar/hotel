package com.mangocity.hotel.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

import com.mangocity.security.domain.Role;

/**
 * 用户辅助类
 * 
 * @author chenkeming
 * 
 */
public class UserUtil implements Serializable {

    /**
     * 组织 - 酒店客服
     */
    public final static String ORG_CC_STR = "MangoCC";

    /**
     * 组织 - 酒店客服 - 其他人员 - CC管理员
     */
    public final static String ORG_CC_ADMIN_STR = "MangoCCAdmin";

    /**
     * 组织 - 酒店客服 - 中台
     */
    public final static String ORG_MID_STR = "MangoMid";

    /**
     * 组织 - 酒店客服 - 其他人员 - 中台管理员
     */
    public final static String ORG_MID_ADMIN_STR = "MangoMidAdmin";

    /**
     * 组织 - 酒店客服 - 日审
     */
    public final static String ORG_AUDIT_STR = "MangoAudit";

    /**
     * 组织 - 酒店客服 - 其他人员 - 日审管理员
     */
    public final static String ORG_AUDIT_ADMIN_STR = "MangoAuditAdmin";

    /**
     * 组织 - 酒店客服 - 房控疑难
     * 
     * @author chenjiajie v2.4.2 2008-12-26
     */
    public final static String ORG_MID_RSC_STR = "MangoMidRSC";

    /**
     * 组织 - 酒店客服 - 前台
     */
    public final static String ORG_FRONT_STR = "HotelFront";

    /**
     * 组织 - 酒店客服 - 芒果网前台
     */
    public final static String ORG_FRONT_MANGO_ID = "MangoFront";

    /**
     * 组织 - 酒店客服 - 114前台
     */
    // public final static String ORG_FRONT_114_STR = "Front114";
    /**
     * 组织 - 酒店客服 - 114前台 - 广东省
     */
    // public final static String ORG_FRONT_114_GD_STR = "Front114_GD";
    /**
     * 组织 - 酒店客服 - 114前台 - 湖北省
     */
    // public final static String ORG_FRONT_114_HB_STR = "Front114_HB";
    /**
     * 组织 - 酒店客服 - 114前台 - 联通
     */
    // public final static String ORG_FRONT_114_UNICOM_STR = "Front114_UNICOM";
    /**
     * 组织 - 酒店客服 - 114前台 - 网通
     */
    // public final static String ORG_FRONT_114_CNC_STR = "Front114_CNC";
    /**
     * 组织 - 酒店客服 - 财务人员
     */
    public final static String ORG_FINANCE = "HotelFinance";

    /**
     * 组织 - 酒店客服 - 财务人员 - 财务付款
     */
    public final static String ORG_FINANCE_PAYMENT = "HotelFinancePayment";

    /**
     * 组织 - 酒店客服 - 财务人员 - 财务退款
     */
    public final static String ORG_FINANCE_REFUND = "HotelFinanceRefund";

    /**
     * 组织 - 酒店客服 - 财务人员 - 退款审批
     */
    public final static String ORG_FINANCE_AUDIT = "HotelFinanceAudit";

    /**
     * 补登日审角色
     */
    public final static String ROLE_HOTELPATCHAUDIT = "HotelPatchAudit";

    /**
     * 组织 - 酒店客服 - 合约组
     * 
     * @author GuoJun2.8.1 2009-05-25
     */
    public final static String ORG_CONTRACTOR = "HotelContratorGroup";

    /**
     * 组织 - 酒店客服 - 合约组名称
     * 
     * @author GuoJun2.8.1 2009-05-25
     */
    public final static String ORG_CONTRACTOR_NAME = "转合约组中";
    
    /**
     * 组织 - 酒店客服 - TMC
     */
    public final static String ORG_TMC = "MangoMidTMC";

    /**
     * 组织的省份ID -> CDM的省份编码Map
     */
    // public static Map stateMap = new HashMap();
    // public static Map tempMap;
    // static {
    // tempMap = new HashMap();
    // tempMap.put("G广东", ORG_FRONT_114_GD_STR);
    // tempMap.put("H湖北", ORG_FRONT_114_HB_STR);
    //		
    // }
    /**
     * 初始化省份数据 stateMap
     * 
     * @param map
     */
    // public static void initOrgInfo(Map map) {
    // Object obj;
    // Set setObj = map.keySet();
    // Iterator it = setObj.iterator();
    // while(it.hasNext()) {
    // obj= it.next();
    // String orgStr = (String)tempMap.get(map.get(obj));
    // if(orgStr != null) {
    // stateMap.put(orgStr, obj);
    // }
    // }
    //		
    // stateMap.put(ORG_FRONT_114_UNICOM_STR, "UNICOM");
    // stateMap.put(ORG_FRONT_114_CNC_STR, "CNC");
    //		
    // tempMap = null;
    // }
    /**
     * 用户是否有补登日审角色
     * 
     * @param user
     * @return
     */
    public static boolean isCanPatchAudit(UserWrapper user) {
        HashSet hashSet = ((HashSet) user.getRoles());
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            Role role = (Role) it.next();
            if (role.getName().equals(ROLE_HOTELPATCHAUDIT)) {
                return true;
            }
        }
        return false;
    }

}
