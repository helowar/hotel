package com.mangocity.hotel.order.web;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.util.StringUtil;

/**
 * 分配酒店查询 分页Action
 * 
 * @author chenjiajie
 * 
 */
public class PaginateAuditAllotHotelAction extends PaginateAction {
    private List userList;

    @Override
    public String execute() {
        Map params = super.getParams();

        String assignStr = (String) params.get("assignToId");
        if (StringUtil.isValidStr(assignStr) && assignStr.equals("请输入工号")) {
            params.put("assignToId", "");
        }
        assignStr = (String) params.get("assignToName");
        if (StringUtil.isValidStr(assignStr) && assignStr.equals("请输入名称")) {
            params.put("assignToName", "");
        }
        roleUser = getOnlineRoleUser();
        return super.execute();
    }

    public List getUserList() {
        return userList;
    }

    public void setUserList(List userList) {
        this.userList = userList;
    }

}
