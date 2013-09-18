package com.mangocity.ep.dao;

import java.util.List;

import com.mangocity.ep.entity.EpOrder;
import com.mangocity.ep.entity.RequestParam;
import com.mangocity.hotel.user.UserWrapper;

public interface EpOrderManagerDAO {
   
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
	
	/**
	 * 查询过去minuteTime分钟内EP系统已经确认的订单，其中EP订单包含订单id、订单Cd、酒店是否确认、确认特殊要求类型、确认号信息、备注、拒绝原因、特殊要求
	 * @param minuteTime 过去minuteTime分钟的时间
	 * @return List<EpOrder> 订单列表
	 */
	
	public List<EpOrder> queryHotelConfirmedEPOrder(int minuteTime);
	
	/**
	 * 根据订单ID查询酒店EP确认信息
	 * @param orderId 订单ID
	 * @return List<Long> 酒店EP确认ID
	 */
	
	public List<Long> queryEpOrderFaxId(Long orderId);
	
	/**
	 * 
	 * 根据酒店确认信息Id修改EBooking方式确认的酒店回传、酒店是否确认、确认号、确认号是否有效.
	 * @param orderFaxId 酒店确认信息ID
	 * @param hotelReturn 酒店是否回传
	 * @param isConfirm 酒店是否确认
	 * @param hotelConfirmNo 酒店确认号
	 * @return
	 */
	public void updateEpOrderFax(Long orderFaxId,int hotelReturn,int isConfirm,String hotelConfirmNo);
	
	/**
	 * 获得sequence
	 * @param synEpOrderSeq
	 * @return
	 */
	public long getOrParamSeqNextVal(String synEpOrderSeq);
}
