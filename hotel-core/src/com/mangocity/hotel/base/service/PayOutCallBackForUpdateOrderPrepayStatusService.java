package com.mangocity.hotel.base.service;

public interface PayOutCallBackForUpdateOrderPrepayStatusService {
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
	
	/**
	 * 银联电话支付 财务回调处理
	 * @param orderCD
	 * @param bHasPay
	 * @param confirmUser
	 * @return
	 */
	public boolean updateOrderUnionPayPhone(String orderCD, boolean bHasPay, String confirmUser) throws Exception;
}
