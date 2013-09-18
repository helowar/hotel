package com.mangocity.hotel.order.web;

import java.io.Serializable;

import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.service.ILockedOrderService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.util.StringUtil;

/**
 * 订单Action 主要是订单的查看、获取、分配、修改等页面，都可以从此Action继承出来 用于对Order对象的统一处理，避免重复编写代码
 * 
 * @author zhengxin
 * 
 */
public abstract class OrderAction extends GenericCCAction {

    protected static final String VIEW_ORDER = "view_order";

    protected static final String PROCESS_ORDER = "process_order";

    protected static final String NEW_ORDER = "new_order";

    protected static final String EDIT_ORDER = "edit_order";

    protected static final String QUERY_HOTEL = "queryHotel";

    protected static final String RECREATE_ORDER = "recreate_order";

    /**
     * 订单ID
     */
    protected Long orderId;

    protected IOrderService orderService;

    protected OrOrder order;
    protected String cryptOrderId;//加密后的orderid
    /**
     * 订单加解锁接口
     */
    protected ILockedOrderService lockedOrderService;

    /**
     * 任务Id
     */
    protected String taskId;

    /**
     * 封装此方法的目的，就是为了以后，将Order缓存在Session当中，这样以后可以提高效率。
     * 
     * @return
     */
    protected OrOrder getOrder(Serializable OrderID) {

        /**
         * OrOrder order = (OrOrder)session.getAttribute(orderID); if (order == null)
         * 
         */

        return orderService.getOrder(OrderID);

    }

    protected void saveOrUpdateOrder(OrOrder order) {
        /**
         * 此处可以将订单放在缓存当中 session.setAttribute(OrOrder.getID(), order);
         */

        if (null == order.getID())
            orderService.newOrder(order);
        else
            orderService.updateOrder(order);
    }

    protected void flush() {
        /**
         * 此处清除订单的缓存
         */
    }

    /**
     * 获取订单的会员信息, 和当前登录会员比较
     * 
     * @param order
     * @return
     */
    protected MemberDTO getOrderMember(OrOrder order) {
    	member = getOnlineMember();
        if (null != member && member.getMembercd().equals(order.getMemberCd())) {
            return member;
        }

        boolean needCred = false;
        if (order.isNeedCreditCard()) {
            needCred = true;
        }

        return getMemberSimpleInfo(order.getMemberCd(), needCred);
    }

    /**
     * 处理重复提交
     */
    protected boolean isRepeatSubmit() {
        String strutsToken = (String) getParams().get("struts.token");
        String sessionToken = (String) getFromSession("struts.token.session");
        if (StringUtil.StringEquals2(strutsToken, sessionToken)) {
            return true;
        }
        putSession("struts.token.session", strutsToken);
        return false;
    }

    /** getter and setter begin */

    public IOrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public OrOrder getOrder() {
        return order;
    }

    public void setOrder(OrOrder order) {
        this.order = order;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public ILockedOrderService getLockedOrderService() {
        return lockedOrderService;
    }

    public void setLockedOrderService(ILockedOrderService lockedOrderService) {
        this.lockedOrderService = lockedOrderService;
    }

	public String getCryptOrderId() {
		return cryptOrderId;
	}

	public void setCryptOrderId(String cryptOrderId) {
		this.cryptOrderId = cryptOrderId;
	}

    /** getter and setter end */

}
