package com.mangocity.ep.service.Impl;

import java.util.List;
import java.util.Map;

import com.mangocity.ep.dao.EpDailyAuditDAO;
import com.mangocity.ep.entity.AuditOrder;
import com.mangocity.ep.entity.AuditOrderItem;
import com.mangocity.ep.entity.OrderParam;
import com.mangocity.ep.service.EpDailyAuditService;
import com.mangocity.hotel.user.UserWrapper;

public class EpDailyAuditServiceImpl implements EpDailyAuditService{
    
	private EpDailyAuditDAO epDailyAuditDAO ;
	
	public void saveAuditOrder() {
		
		epDailyAuditDAO.saveAuditOrder();
	}
    
	public void saveAuditOrderItem() {
		
		epDailyAuditDAO.saveAuditOrderItem();
	}
	
	public Map<String,String> queryHotelIdByDaliyAuditId(String ids) {
		
		return epDailyAuditDAO.queryHotelIdByDaliyAuditId(ids);
	}
	
	public void deleteRepeatAuditOrder() {
		
		epDailyAuditDAO.deleteRepeatAuditOrder();
	}
	
	public void deleteRepeatAuditOrderItem() {
		
		epDailyAuditDAO.deleteRepeatAuditOrderItem();
	}
	
    public List<AuditOrderItem> queryAuditItemById(Long epDailyauditId) {
    		
    	return epDailyAuditDAO.queryAuditItemById(epDailyauditId);
    }
    
    public List<AuditOrder> queryEpOrderAuditData(OrderParam param) {
    	
    	return epDailyAuditDAO.queryEpOrderAuditData(param);
    }
    
    public Long queryOrderAuditSum(OrderParam param) {
    	
    	return epDailyAuditDAO.queryOrderAuditSum(param);
    }
	
    public void updateOrderCcStatus(UserWrapper user,String orderCd) {
    	
    	 epDailyAuditDAO.updateOrderCcStatus(user,orderCd);
    }
    
    
    
	public String queryRemark(String orderCd, String auditType) {
		
		return epDailyAuditDAO.queryRemark(orderCd, auditType);
	}
    
	
	
	
	public void updateRemark(String orderCd, String auditType, String remark) {
		
		epDailyAuditDAO.updateRemark(orderCd, auditType, remark);
	}
    
	
	
	public List<String> queryEpHotelId() {
		
		return epDailyAuditDAO.queryEpHotelId();
	}

	public EpDailyAuditDAO getEpDailyAuditDAO() {
		return epDailyAuditDAO;
	}

	public void setEpDailyAuditDAO(EpDailyAuditDAO epDailyAuditDAO) {
		this.epDailyAuditDAO = epDailyAuditDAO;
	}
	
	
	
	
}
