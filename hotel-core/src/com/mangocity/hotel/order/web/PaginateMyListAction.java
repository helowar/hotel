package com.mangocity.hotel.order.web;

import java.util.Map;

import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.order.constant.OrderState;

/**
 * 给我的工作档案用的分页Action
 * 
 * @author chenkeming
 */
public class PaginateMyListAction extends PaginateAction {

    @Override
    public String execute() {

        user = getOnlineWorkStates();
        Map params = super.getParams();
        params.put("assignTo", user.getLogonId());
        params.put("orderState", OrderState.SUBMIT_TO_MID);
        roleUser = getOnlineRoleUser();

        return super.execute();
    }

}
