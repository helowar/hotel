package com.mangocity.ep.service;

import java.util.List;

import com.mangocity.ep.entity.EpOrder;
import com.mangocity.ep.entity.RequestParam;
import com.mangocity.hotel.user.UserWrapper;

public interface EpOrderManagerService {
   
	/**
	 * @ 根据 requestParam 查询 订单
	 * @Param requestParam
	 * @result List<EpOrder>
	 */
	public List<EpOrder> queryEpOrder(RequestParam requestParam);
	
	
	/**
	 * @ 更具订单CD 更新 cc订单 确认状态
	 * @Param orderCd
	 * 
	 */
	public void updateConfrimStatus(String orderCd,UserWrapper roleUser);
	
	
	/**
	 * @  更具条件 查看记录数 用于分页
	 * @Param requestParam
	 * @result Integer
	 */
	public Integer queryOrderSum(RequestParam requestParam);
	
	/**
	 * @ 通过酒店id判断是否为Ep订单
	 * @ Param hotelId
	 * @ String 1: 是 0：不是
	 */
	
	public String validateEpOrderByHotelId(String hotelId);
}
