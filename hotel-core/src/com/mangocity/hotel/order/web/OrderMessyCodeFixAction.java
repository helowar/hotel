package com.mangocity.hotel.order.web;

import java.util.Map;


import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.order.service.IOrderMessyCodeFixService;
import com.mangocity.util.StringUtil;
/**
 * 订单乱码修复Action
 * @author zhouna
 *
 */
public class OrderMessyCodeFixAction extends PersistenceAction{
	/**
	 * 订单乱码修复调用接口
	 */
	 private IOrderMessyCodeFixService orderMessyCodeFixService;
	 private String message;
	    public String fixOrderMessyCode(){
	        Map params = super.getParams();	        
	        String orderCdStr =  (String) params.get("orderCD");
	        log.info("订单乱码修复："+orderCdStr);

	        if(StringUtil.isValidStr(orderCdStr)){
	            //调用乱码修复的存储过程 
	         try {
				message = orderMessyCodeFixService.callFixOrderMessyCodeProcdure(orderCdStr);
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        }
	        roleUser = getOnlineRoleUser();
	        return SUCCESS;
	    }
	 
	 /** getter and setter **/
	public IOrderMessyCodeFixService getOrderMessyCodeFixService() {
		return orderMessyCodeFixService;
	}

	public void setOrderMessyCodeFixService(
			IOrderMessyCodeFixService orderMessyCodeFixService) {
		this.orderMessyCodeFixService = orderMessyCodeFixService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	 
}
