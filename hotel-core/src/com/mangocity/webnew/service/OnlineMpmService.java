package com.mangocity.webnew.service;


/**
 * 查询支付是否成功的service
 * @author zuoshengwei
 *
 */
public interface OnlineMpmService {
	
	/**
	 * 根据外部交易号查询支付是否成功
	 * ngwei.zuo 2009-11-04
	 */
	public  String   getOnlineSucceedFlag(String outTradeNoStr);   

}
