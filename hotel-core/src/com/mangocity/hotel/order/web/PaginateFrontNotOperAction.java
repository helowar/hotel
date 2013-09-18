package com.mangocity.hotel.order.web;

import java.util.HashMap;
import java.util.Map;

import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.util.StringUtil;

/**
 * 前台未操作订单的分页Action
 * 
 * @author chenkeming
 */
public class PaginateFrontNotOperAction extends PaginateAction {

    @Override
    public String execute() {

        Map params = super.getParams();
        params.put("orderState", Integer.valueOf(OrderState.NOT_SUBMIT));

        roleUser = getOnlineRoleUser();

        if (roleUser.isMango()) {
            params.put("isMango", "true");
        } else {
            params.put("isMango", "false");
            params.put("stateId", roleUser.getState());
        }

        isFromUnlock = (String) getFromSession("isFromUnlock");
        if (!StringUtil.isValidStr(isFromUnlock)) {
            HashMap searchECTable = new HashMap();
            searchECTable.put("ec_p", params.get("ec_p"));
            searchECTable.put("ec_crd", params.get("ec_crd"));
            searchECTable.put("ec_rd", params.get("ec_rd"));
            searchECTable.put("ec_pg", params.get("ec_pg"));
            searchECTable.put("ec_totalpages", params.get("ec_totalpages"));
            searchECTable.put("ec_totalrows", params.get("ec_totalrows"));

            putSession("HotelOrderManaSearchECTable", searchECTable);
        } else {
            Map searchCond = (Map) getFromSession("HotelOrderManaSearchECTable");
            if (null != searchCond) {
                params.putAll(searchCond);
            }
        }

        return super.execute();
    }

}
