package com.mangocity.hotel.order.web;

import java.util.Map;

import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.util.hotel.constant.PayMethod;

/**
 * 财务退款分页Action
 * 
 * @author chenkeming
 */
public class PaginateWaitRefundingAction extends PaginateAction {

    public String execute() {

        Map params = super.getParams();
        params.put("orderState", OrderState.HAS_AUDIT_REFUND);
        params.put("payMethod", PayMethod.PRE_PAY);
        return super.execute();
    }

}
