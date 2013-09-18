package com.mangocity.hotel.order.web;

import java.util.HashMap;
import java.util.Map;

import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.util.StringUtil;

/**
 * 管理订单分页Action
 * 
 * @author chenkeming
 */
public class PaginateQueryOrderAction extends PaginateAction {

    public String execute() {

        roleUser = getOnlineRoleUser();
        Map searchCond = (Map) getFromSession("HotelOrderManaSearchCond");

        Map params = super.getParams();

        String tempStr = (String) params.get("operatorLogon");
        if (StringUtil.isValidStr(tempStr) && tempStr.equals("请输入工号")) {
            params.put("operatorLogon", "");
        }
        tempStr = (String) params.get("operator");
        if (StringUtil.isValidStr(tempStr) && tempStr.equals("请输入名称")) {
            params.put("operator", "");
        }
        tempStr = (String) params.get("creatorLogon");
        if (StringUtil.isValidStr(tempStr) && tempStr.equals("请输入工号")) {
            params.put("creatorLogon", "");
        }
        tempStr = (String) params.get("creator");
        if (StringUtil.isValidStr(tempStr) && tempStr.equals("请输入名称")) {
            params.put("creator", "");
        }

        if (roleUser.isMango()) {
            String sOrderType = (String) params.get("orderType");
            if (null == sOrderType && null != searchCond) {
                sOrderType = (String) searchCond.get("orderType");
            }
            if (StringUtil.isValidStr(sOrderType) && !sOrderType.equals("0")) {
                if (Integer.parseInt(sOrderType) == OrderType.TYPE_MANGO) {
                    params.put("isMango", "true");
                } else {
                    params.put("isMango", "false");
                }
            }
        } else {
            params.put("isMango", "false");
            params.put("stateId", roleUser.getState());
        }

        isFromUnlock = (String) getFromSession("isFromUnlock");
        if (!StringUtil.isValidStr(isFromUnlock)) {
            if (null != params.get("orderType")) { // 如果是从查询界面进行查询
                HashMap searchCondition = new HashMap();
                searchCondition.put("orderCD", params.get("orderCD"));
                searchCondition.put("orderCDHotel", params.get("orderCDHotel"));
                searchCondition.put("orderStatus", params.get("orderStatus"));
                searchCondition.put("memberName", params.get("memberName"));
                searchCondition.put("fellowName", params.get("fellowName"));
                searchCondition.put("checkInDate", params.get("checkInDate"));
                searchCondition.put("checkInDateEnd", params.get("checkInDateEnd"));
                searchCondition.put("createTime", params.get("createTime"));
                searchCondition.put("createTimeEnd", params.get("createTimeEnd"));
                searchCondition.put("hotelName", params.get("hotelName"));
                searchCondition.put("checkOutDate", params.get("checkOutDate"));
                searchCondition.put("select_province", params.get("stateId"));
                searchCondition.put("select_city", params.get("cityId"));
                searchCondition.put("operator", params.get("operator"));
                searchCondition.put("operatorLogon", params.get("operatorLogon"));
                searchCondition.put("creator", params.get("creator"));
                searchCondition.put("creatorLogon", params.get("creatorLogon"));
                searchCondition.put("customerWay", params.get("customerWay"));
                searchCondition.put("linkmanInfo", params.get("linkmanInfo"));
                searchCondition.put("hotelConfirm", params.get("hotelConfirm"));
                searchCondition.put("isCustomerConfirmed", params.get("isCustomerConfirmed"));
                searchCondition.put("orderSource", params.get("orderSource"));
                searchCondition.put("orderType", params.get("orderType"));
                searchCondition.put("onHotelDate", params.get("onHotelDate"));
                putSession("HotelOrderManaSearchCond", searchCondition);

                HashMap searchECTable = new HashMap();
                searchECTable.put("ec_p", params.get("ec_p"));
                searchECTable.put("ec_crd", params.get("ec_crd"));
                searchECTable.put("ec_rd", params.get("ec_rd"));
                searchECTable.put("ec_pg", params.get("ec_pg"));
                searchECTable.put("ec_totalpages", params.get("ec_totalpages"));
                searchECTable.put("ec_totalrows", params.get("ec_totalrows"));
                putSession("HotelOrderManaSearchECTable", searchECTable);
            } else {
                if (null != searchCond) {
                    params.putAll(searchCond);
                    params.put("stateId", searchCond.get("select_province"));
                    params.put("cityId", searchCond.get("select_city"));
                }
            }
        } else {
            if (null != searchCond) {
                params.putAll(searchCond);
                params.put("stateId", searchCond.get("select_province"));
                params.put("cityId", searchCond.get("select_city"));
                searchCond = (Map) getFromSession("HotelOrderManaSearchECTable");
                params.putAll(searchCond);
            }
        }

        return super.execute();
    }

}
