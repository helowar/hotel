package com.mangocity.hotel.order.dao.impl;

import java.util.List;

import com.mangocity.hotel.order.dao.IOrPaperDailyAuditDao;
import com.mangocity.hotel.order.persistence.OrPaperDailyAudit;
import com.mangocity.hotel.order.persistence.OrPaperDailyAuditItem;
import com.mangocity.util.dao.GenericDAOHibernateImpl;


public class OrPaperDailyAuditDao extends GenericDAOHibernateImpl implements IOrPaperDailyAuditDao{
    /**
     * 根据命名查询得到OrPaperDailyAuditItem
     */

    public List<OrPaperDailyAuditItem> getOrPaperDailyAuditItemByQueryName(Long auditId) {
    	return super.queryByNamedQuery("hQueryPaperDailyAuditItem", new Object[] { auditId });
    }
    
    
	/**
	 * 保存(新增)OrPaperDailyAuditItem
	 * @param order
	 * @return
	 */
    public void insertOrPaperDailyAuditItem(OrPaperDailyAuditItem auditItem){
    	save(auditItem);
    }


    /**
     * 根据ID获取OrPaperDailyAuditItem
     * @param auditItemId
     * @return
     */
    public OrPaperDailyAuditItem getOrPaperDailyAuditItem(Long auditItemId){
    	return load(OrPaperDailyAuditItem.class, auditItemId);
    }

	/**
	 * 更新auditItem
	 * @param auditItem
	 */
    public void updateOrPaperDailyAuditItem(OrPaperDailyAuditItem auditItem){
    	 saveOrUpdate(auditItem);
    }
    
	/**
	 * 更新audit
	 * @param audit
	 */
    public void updateOrPaperDailyAuditItem(OrPaperDailyAudit audit){
    	saveOrUpdate(audit);
    }

    
    /**
     * 根据ID获取OrPaperDailyAuditItem(直接操作数据库)
     * @param auditItemId
     * @return
     */
    public OrPaperDailyAuditItem findOrPaperDailyAuditItem(Long auditItemId){
    	return get(OrPaperDailyAuditItem.class, auditItemId);
    }
    
}
