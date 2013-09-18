package com.mangocity.hotel.order.service;

/**
 * 财务收款回调接口
 * 
 * @author chenkeming
 * 
 */
public interface IPayOutInterface {

    /**
     * 更新订单的付款状态
     * 
     * @param orderCD
     * @param bHasPay
     *            true:已付款, false:未付款
     * @param confirmUser
     *            操作人
     * @return true:成功 false:失败
     */
    public boolean updateOrderPrepayStatus(final String orderCD, final boolean bHasPay,
        String confirmUser);
}
