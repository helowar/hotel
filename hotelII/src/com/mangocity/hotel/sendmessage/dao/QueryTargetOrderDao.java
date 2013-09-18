package com.mangocity.hotel.sendmessage.dao;

import java.util.List;

import com.mangocity.hotel.sendmessage.model.TargetOrder;

/**
 * 查询需要发送短信的订单
 * @author liting
 *
 */
public interface QueryTargetOrderDao {
	public <T extends TargetOrder> List<T> queryTargetOrder()throws Exception;
}
