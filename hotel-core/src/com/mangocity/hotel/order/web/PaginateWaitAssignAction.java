package com.mangocity.hotel.order.web;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.util.StringUtil;

/**
 * 处理预订单分页Action
 * 
 * @author chenkeming
 */
public class PaginateWaitAssignAction extends PaginateAction {

    private List userList;

    @Override
    public String execute() {

        Map params = super.getParams();

        String tempStr = (String) params.get("operatorLogon");
        if (StringUtil.isValidStr(tempStr) && tempStr.equals("请输入工号")) {
            params.put("operatorLogon", "");
        }
        tempStr = (String) params.get("operator");
        if (StringUtil.isValidStr(tempStr) && tempStr.equals("请输入名称")) {
            params.put("operator", "");
        }
        tempStr = (String) params.get("assignToId");
        if (StringUtil.isValidStr(tempStr) && tempStr.equals("请输入工号")) {
            params.put("assignToId", "");
        }
        tempStr = (String) params.get("assignToName");
        if (StringUtil.isValidStr(tempStr) && tempStr.equals("请输入名称")) {
            params.put("assignToName", "");
        }

        // params.put("orderState", OrderState.SUBMIT_TO_MID);

        putSession("waitAssignChoose", params.get("hidHraOrderType"));

        // userList = workStatesManager.lstWorkStatesByType(WorkType.HRA);
        roleUser = getOnlineRoleUser();

        String sOrderType = (String) params.get("orderType");
        if (!StringUtil.isValidStr(sOrderType) || sOrderType.equals("0")) {
            params.put("orderType", null);
        }

        return super.execute();
    }

    public List getUserList() {
        return userList;
    }

    public void setUserList(List userList) {
        this.userList = userList;
    }

}
