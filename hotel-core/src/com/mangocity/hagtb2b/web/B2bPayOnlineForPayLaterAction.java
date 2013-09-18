package com.mangocity.hagtb2b.web;

import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.web.OrderAction;
import com.mangocity.util.web.DesCryptUtil;
import com.mangocity.webnew.service.IHotelOrderCompleteService;

public class B2bPayOnlineForPayLaterAction  extends OrderAction {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String orderid;
	
	private String amount;
	
	private String orderCD_payLater;
	
	private String createTime;
	
	private String theTrueServerTime;
	
	 protected IOrderService orderService;
	 

	/**
	 * 新版网站处理订单完成Service接口 主要功能是生成订单，订单提交中台，扣取会员信用卡费用，积分，锁定代金 
	 */
	private IHotelOrderCompleteService hotelOrderCompleteService;

	public String b2bPayLater(){
		//对orderid解密
        try {
    		cryptOrderId = new DesCryptUtil("hotel").decrypt(cryptOrderId);
    		orderid = cryptOrderId;
		} catch (java.lang.NumberFormatException e) {
			log.error("订单解密后格式转换错误！订单id="+cryptOrderId);
			return forwardError("查找此订单出错，请重试！");
		} catch (Exception e) {
			log.error("对订单解密错误！订单id="+cryptOrderId);
			return forwardError("查找此订单出错，请重试！");
		}
		OrOrder order_minPrice = orderService.getOrder(Long.valueOf(orderid));
		//防止变价
		hotelOrderCompleteService.synchroSumPriceToOrder(order_minPrice,true);
		request.setAttribute("order", order_minPrice);
		request.setAttribute("amount", order_minPrice.getSumRmb());
		//System.out.println("aada");
		return "payOnline";
	}
	
	
	public IOrderService getOrderService() {
		return orderService;
	}
	
	
	public IHotelOrderCompleteService getHotelOrderCompleteService() {
		return hotelOrderCompleteService;
	}


	public void setHotelOrderCompleteService(
			IHotelOrderCompleteService hotelOrderCompleteService) {
		this.hotelOrderCompleteService = hotelOrderCompleteService;
	}


	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}
	public String getOrderid() {
		return orderid;
	}
	
	
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getOrderCD_payLater() {
		return orderCD_payLater;
	}


	public void setOrderCD_payLater(String orderCDPayLater) {
		orderCD_payLater = orderCDPayLater;
	}


	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public String getTheTrueServerTime() {
		return theTrueServerTime;
	}


	public void setTheTrueServerTime(String theTrueServerTime) {
		this.theTrueServerTime = theTrueServerTime;
	}
	
}
