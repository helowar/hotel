package com.mangocity.hotel.order.web;

import com.mangocity.hotel.base.web.webwork.PaginateAction;


/**
 * 订单解锁的分页Action
 * 
 * @author Neil
 */
public class PaginateOrderUnlockAction extends PaginateAction {

    private static final long serialVersionUID = 6363401675739964467L;

    @Override
    @SuppressWarnings("unchecked")
    public String execute() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        roleUser = getOnlineRoleUser();

        return super.execute();
    }
}
