package com.mangocity.hotel.order.web;

import java.util.Map;

import com.mangocity.hotel.base.web.webwork.PaginateAction;


/**
 * 给日审工作档案用的分页Action
 * 
 * @author chenkeming
 */
public class PaginateAuditMyListAction extends PaginateAction {

    @Override
    public String execute() {

        user = getOnlineWorkStates();
        Map params = super.getParams();
        params.put("assignTo", user.getLogonId());
        // params.put("assignTo","hotel59907");
        params.get("auditDate");
        String auditstete = (String) params.get("auditstete");
        if (null == auditstete) {
            params.put("auditstete", "4");
        }
        // if(auditDate == null){
        // params.put("auditDate", DateUtil.dateToString(new Date()));
        // }

        return super.execute();
    }

}
