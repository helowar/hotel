package com.mangocity.hotel.order.dao;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.order.persistence.Order;
import com.mangocity.util.dao.DAO;

/**
 */
public interface IOrderDao extends DAO ,Serializable{

    /**
     * 生成订单编号，并持久化
     * 
     * @param order
     */
    public void insertOrder(Order order);

    /**
     * 保存订单
     * 
     * @param order
     */
    public void updateOrder(Order order);

    /**
     * 加载订单
     * 
     * @param orderID
     * @return
     */
    public Order loadOrder(Serializable orderID);

    /**
     * 命名查询
     * 
     * @param queryID
     * @param params
     * @return
     */
    public List queryByNamedQuery(String queryID, Object[] params);
    
    
	/**
	 * 根据会员编号和酒店id，查询出会员在酒店的入住的历史订单号
	 * @param membercd
	 * @param hotelId
	 * @return
	 */
	public List<Object[]> findOrderByMemberCd(String membercd, String hotelId);
	
	
	
	/**
	 * 根据审核日期，查询出此日期日审完已入住的散客订单信息
	 * @param auditedDate
	 * @return
	 */
	public List<Object[]> findOrderInfoByAuditedDate(String auditedDate);

}
