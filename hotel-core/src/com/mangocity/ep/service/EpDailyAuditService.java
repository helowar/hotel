package com.mangocity.ep.service;

import java.util.List;
import java.util.Map;

import com.mangocity.ep.entity.AuditOrder;
import com.mangocity.ep.entity.AuditOrderItem;
import com.mangocity.ep.entity.OrderParam;
import com.mangocity.hotel.user.UserWrapper;

public interface EpDailyAuditService {
    
	
	/**
	 * @ 通过定时器 每天 早上6:30 将ep 入住审核及退房审核订单 保存
	 * 
	 */
	public void saveAuditOrder();
	
	/**
	 * @ 通过定时器 每天 早上6:30 将ep 入住审核及退房审核的订单明细 入库
	 * 
	 */
	
	public void saveAuditOrderItem();
	
	/**
	 * @ 通过 DAILYAUDIT_ID 获得 酒店ID
	 * 
	 */
	public Map<String,String> queryHotelIdByDaliyAuditId(String ids);
	
	
	/**
	 * @ 删除多于的重复日审订单
	 */
	public void deleteRepeatAuditOrder();
	
	/**
	 * @ 删除多余的日审订单明细
	 */
	
	public void deleteRepeatAuditOrderItem();
	
	/**
	 * @ 获得订单审核信息
	 * @result List<AuditOrder>
	 */
   public List<AuditOrder> queryEpOrderAuditData(OrderParam param);
   
   /**
    * @ 查询审核明细信息
    * @param epDailyauditId
    * @result List<AuditOrderItem>
    */
   public List<AuditOrderItem> queryAuditItemById(Long epDailyauditId);
   
   /**
    * @ 查询订单审核记录数
    */
   public Long queryOrderAuditSum(OrderParam param);
   
   /**
    * @ cc确认 EP订单审核结果,更新审核状态
    */
   public void updateOrderCcStatus(UserWrapper user,String orderCd);
   
   /**
    * @ 通过订单号，审核类型 查询备注信息
    * @ orderCd,auditType
    * @result remark
    */
   public String queryRemark(String orderCd,String auditType);
   
   
   /**
    * @ 通过订单号,审核类型 更新备注信息
    * @ orderCd,auditType
    */
   public void updateRemark(String orderCd,String auditType,String remark);
   
	/**
	 * @ 查找Ep 酒店
	 * @return List<String>
	 * 
	 */
	
	public List<String> queryEpHotelId();
   
}
