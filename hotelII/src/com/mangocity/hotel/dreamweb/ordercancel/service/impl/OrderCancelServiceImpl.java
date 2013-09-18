package com.mangocity.hotel.dreamweb.ordercancel.service.impl;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import com.mangocity.hotel.dreamweb.ordercancel.dao.ITempOrderDAO;
import com.mangocity.hotel.dreamweb.ordercancel.dao.OrderCancelDao;
import com.mangocity.hotel.dreamweb.ordercancel.persistence.OrderCancel;
import com.mangocity.hotel.dreamweb.ordercancel.persistence.TempOrder;
import com.mangocity.hotel.dreamweb.ordercancel.service.OrderCancelService;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.util.log.MyLog;

public class OrderCancelServiceImpl implements OrderCancelService {

	private OrderCancelDao orderCancelDao;

	private OrOrderDao orOrderDao;
	
	private ITempOrderDAO tempOrderDao;
	
	private static final MyLog log = MyLog.getLogger(OrderCancelServiceImpl.class);

	/**
	 * @param orOrderDao
	 *            the orOrderDao to set
	 */
	public void setOrOrderDao(OrOrderDao orOrderDao) {
		this.orOrderDao = orOrderDao;
	}

	/**
	 * @param orderCancelDao
	 *            the orderCancelDao to set
	 */
	public void setOrderCancelDao(OrderCancelDao orderCancelDao) {
		this.orderCancelDao = orderCancelDao;
	}
		
	/**
	 * @param tempOrderDao the tempOrderDao to set
	 */
	public void setTempOrderDao(ITempOrderDAO tempOrderDao) {
		this.tempOrderDao = tempOrderDao;
	}

	public void service(HttpServletRequest request,
			MemberDTO member) throws Exception {
		// TODO Auto-generated method stub
		// 订单id变量初始化
		String orderId = null;
		// 订单cd变量初始化
		String orderCD = null;
		// 酒店id变量初始化
		String hotelId = null;
		// 酒店名称变量初始化
		String hotelName = null;
		// 会员会籍变量初始化
		String memberCD = null;
		String memberId = null;
		// 会员名称变量初始化
		String memberName = null;
		// 取消原因编码变量初始化
		String reasonCD = null;
		// 取消原因内容变量初始化
		String reason = null;
		// 获得当前系统日期
		Date nowDate = new Date();
		// 取消实体类初始化
		OrderCancel orderCancel = null;
		// 订单实体类初始化
		OrOrder order = null;
		// 订单操作日志实体类初始化
		OrHandleLog cancelhandleLog = null;
		// 订单id变量赋值
		orderId = request.getParameter("orderId");
		// 酒店id变量赋值
		hotelId = request.getParameter("hotelId");
		try{
			// 酒店名称变量赋值
			hotelName = request.getParameter("hotelName");
			if (null != hotelName) {
			hotelName = URLDecoder.decode(
						URLDecoder.decode(hotelName, "utf-8"), "utf-8");
				//hotelName = URLDecoder.decode(hotelName,"utf-8");
			}
			// 会员会籍变量赋值
			memberCD = request.getParameter("memberCD");
			//memberId = request.getParameter("memberId");
			// 会员名称变量赋值
			memberName = member.getFamilyName()+member.getName();
			// 取消原因编码变量赋值
			reasonCD = request.getParameter("reasonCD");
			// 取消原因内容变量赋值
			reason = request.getParameter("reason");
			if (null != reason) {
				reason = URLDecoder.decode(URLDecoder.decode(reason, "utf-8"),
						"utf-8");
			}
			if (null != orderId && null != hotelId) {
				orderCancel = this.getOrderCancelByOrderId(Long.valueOf(orderId));
				order = orOrderDao.getOrder(Long.valueOf(orderId));
				cancelhandleLog = new OrHandleLog();
				String canStr = "客户在网站提交订单取消申请，原因为：" + reason;
				// cancelhandleLog.setModifierName("网站");
				cancelhandleLog.setModifierName("网站客户:"+memberName);
				cancelhandleLog.setBeforeState(order.getOrderState());
				cancelhandleLog.setAfterState(order.getOrderState());
				cancelhandleLog.setContent(canStr.toString());
				cancelhandleLog.setModifiedTime(new Date());
				cancelhandleLog.setOrder(order);
				if (orderCancel == null) {
					orderCancel = new OrderCancel();
					orderCancel.setOrderId(Long.parseLong(orderId));
					orderCancel.setHotelId(Long.parseLong(hotelId));
					orderCancel.setHotelName(hotelName);
				//	orderCancel.setMemberId(null);
					orderCancel.setMemberCD(memberCD);
					orderCancel.setMemberName(memberName);
					orderCancel.setCreateDate(nowDate);
					orderCancel.setModifyDate(nowDate);
					orderCancel.setReasonCD(reasonCD);
					orderCancel.setReason(reason);
					orderCancel.getLogList().add(cancelhandleLog);
					this.addOrderCancel(orderCancel, request);
				} else {
					orderCancel.setReasonCD(reasonCD);
					orderCancel.setReason(reason);
					orderCancel.setModifyDate(nowDate);
					orderCancel.getLogList().add(cancelhandleLog);
					this.updateOrderCancel(orderCancel, request);
				}			
				
				//如果该订单已在跟单组则无需重新分配到跟单组
				TempOrder tempOrder = null;
				tempOrder = tempOrderDao.queryByOrderId(orderCancel.getOrderId());
				if(null == tempOrder  || tempOrder.getGroupType() != 1){
					//更新ororder和temporder表以便该订单能流入中台跟单组
					OrOrder orOrder = null;
					orOrder = orOrderDao.getOrder(orderCancel.getOrderId());
					orOrder.setOrderState(OrderState.SUBMIT_TO_MID);
					order.setToMidTime(new Date());
					orOrder.setIsStayInMid(true);//提交中台订单池
					orOrder.setAssignTo(null);
					orOrder.setAssignToName(null);
					orOrder.setCustomerConfirm(false);//改成客人未确认状态
					orOrderDao.updateOrder(orOrder);
					if (null != tempOrder) {
						tempOrder.setAssignState(Long.valueOf("0"));
						// 1为跟单组
						Long groupType = Long.valueOf("1");
						tempOrder.setGroupType(groupType);
						tempOrderDao.update(tempOrder);
					}
				}
				
				
			}
		}catch (Exception e){
			log.error(e);
			e.printStackTrace();
		}
		
	}

	public void saveParams(HttpServletRequest request,
			MemberDTO member) {
		// 订单id变量初始化
		String orderId = null;
		// 订单cd变量初始化
		String orderCD = null;
		// 酒店id变量初始化
		String hotelId = null;
		// 酒店名称变量初始化
		String hotelName = null;
		// 会员会籍变量初始化
		String memberCD = null;
		String memberId = null;
		//订单id变量赋值
		orderId = request.getParameter("orderId");
		//酒店id变量赋值
		hotelId = request.getParameter("hotelId");
		//酒店名称变量赋值
		hotelName = request.getParameter("hotelName");
		//会员会籍变量赋值
		memberCD = request.getParameter("memberCD");
		//会员ID变量赋值
		memberId = request.getParameter("memberId");
		// TODO Auto-generated method stub
		request.getSession().setAttribute("orderId", orderId);
		request.getSession().setAttribute("hotelId", hotelId);
		request.getSession().setAttribute("hotelName", hotelName);
		request.getSession().setAttribute("memberId", memberId);
		request.getSession().setAttribute("memberCD", memberCD);
	}

	public void addOrderCancel(OrderCancel orderCancel,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		orderCancelDao.addOrderCancel(orderCancel);
		
	}

	public void updateOrderCancel(OrderCancel orderCancel,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		orderCancelDao.updateOrderCancel(orderCancel);
	}

	public OrderCancel getOrderCancel(Long id) {
		// TODO Auto-generated method stub
		return orderCancelDao.getOrderCancel(id);
	}

	public OrderCancel getOrderCancelByOrderId(Long orderId) {
		// TODO Auto-generated method stub
		return orderCancelDao.getOrderCancelByOrderId(orderId);
	}

	public List<OrderCancel> getAllOrderCancel() {
		// TODO Auto-generated method stub
		return orderCancelDao.getAllOrderCancel();
	}

}
