package com.mangocity.hotel.order.web;

import java.util.Map;

import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.util.hotel.constant.PayMethod;

/**
 * 退款审批分页Action
 * 
 * @author chenkeming
 */
public class PaginateWaitRefundAction extends PaginateAction {

    public String execute() {

        Map params = super.getParams();
        params.put("payMethod", PayMethod.PRE_PAY);
        params.put("orderState", OrderState.HAS_CREATE_REFUND);

        return super.execute();
    }

}
