package com.mangocity.hotel.order.dao;

import java.util.List;

import com.mangocity.hotel.order.persistence.OrPaperDailyAudit;
import com.mangocity.hotel.order.persistence.OrPaperDailyAuditItem;

/**
 */
public interface IOrPaperDailyAuditDao{

    /**
     * 根据命名查询得到OrPaperDailyAuditItem
     */

    public List<OrPaperDailyAuditItem> getOrPaperDailyAuditItemByQueryName(Long auditId);
	/**
	 * 保存(新增)OrPaperDailyAuditItem
	 * @param order
	 * @return
	 */
    public void insertOrPaperDailyAuditItem(OrPaperDailyAuditItem auditItem);


    /**
     * 根据ID获取OrPaperDailyAuditItem
     * @param auditItemId
     * @return
     */
    public OrPaperDailyAuditItem getOrPaperDailyAuditItem(Long auditItemId);

	/**
	 * 更新auditItem
	 * @param auditItem
	 */
    public void updateOrPaperDailyAuditItem(OrPaperDailyAuditItem auditItem);

    
	/**
	 * 更新audit
	 * @param audit
	 */
    public void updateOrPaperDailyAuditItem(OrPaperDailyAudit audit);
    
    /**
     * 根据ID获取OrPaperDailyAuditItem(直接操作数据库)
     * @param auditItemId
     * @return
     */
    public OrPaperDailyAuditItem findOrPaperDailyAuditItem(Long auditItemId);
    }
