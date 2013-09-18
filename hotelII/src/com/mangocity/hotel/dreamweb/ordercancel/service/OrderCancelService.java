package com.mangocity.hotel.dreamweb.ordercancel.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mangocity.hotel.dreamweb.ordercancel.persistence.OrderCancel;
import com.mangocity.hotel.ext.member.dto.MemberDTO;

public interface OrderCancelService {
	
	/**
	 * 对外服务的接口方法
	 * @param request
	 * @param memberId
	 * @param member
	 * @throws Exception 
	 */
	public void service(HttpServletRequest request,MemberDTO member) throws Exception;
	
	/**
	 * 保存关键参数
	 * @param request
	 */
	public void saveParams(HttpServletRequest request,
			MemberDTO member);
	
	/**
	 * 增加一条订单网站取消申请记录
	 * @param hotelWebCancel
	 * @param request
	 */
	public void addOrderCancel(OrderCancel orderCancel,HttpServletRequest request);
	
	/**
	 * 修改一条订单网站取消申请记录
	 * @param hotelWebCancel
	 * @param request
	 */
	public void updateOrderCancel(OrderCancel orderCancel,HttpServletRequest request);
	
	/**
	 * 通过id获取一条订单网站取消申请记录
	 * @param id
	 * @return
	 */
	public OrderCancel getOrderCancel(Long id);
	
	/**
	 * 通过orderid获取一条订单网站取消申请记录
	 * @param orderId
	 * @return
	 */
	public OrderCancel getOrderCancelByOrderId(Long orderId);
	
	/**
	 * 获得所有订单网站取消申请记录
	 * @return
	 */
	public List<OrderCancel> getAllOrderCancel();

}
