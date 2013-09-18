package com.mangocity.hotel.sendmessage.service;

import java.util.List;

/**
 * 用来查询发送短信的订单，从这些订单中获取电话号码发送
 * @author liting
 *
 */
public interface TargetOrderService   {
	public <T> List <T> queryTargetOrder()throws Exception;
}
