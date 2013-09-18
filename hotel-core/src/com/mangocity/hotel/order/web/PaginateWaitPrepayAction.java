package com.mangocity.hotel.order.web;

import java.util.Map;

import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.util.hotel.constant.PayMethod;

/**
 * 待处理预付单分页Action
 * 
 * @author chenkeming
 */
public class PaginateWaitPrepayAction extends PaginateAction {

    public String execute() {

        Map params = super.getParams();
        params.put("payMethod", PayMethod.PRE_PAY);
        params.put("orderState", OrderState.HAS_PAID);
        params.put("orderState1", OrderState.HAS_SUBMIT);
        params.put("hasPrepayed", 1);

        return super.execute();
    }

}
