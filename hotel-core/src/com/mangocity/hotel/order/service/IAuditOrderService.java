package com.mangocity.hotel.order.service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.order.persistence.DaDailyaudit;
import com.mangocity.hotel.order.persistence.DaPaperFaxItem;
import com.mangocity.hotel.order.persistence.view.AuditResult;


/**
 * 日审service
 * 
 * @author chenkeming
 *
 */
public interface IAuditOrderService extends Serializable {

	/**
	 * 生成日审记录
	 * 
	 * @param auditDate
	 * @param channelId
	 * @param newL
	 * @param editL
	 */
	public void genAuditRecords(Date auditDate, Long channelId, List<Object[]> newL, List<Object[]> editL);
	
    /**
     * 根据参数获取日审传真list
     * 
     * @param params
     * @return
     */
    public List getChannels(Map params);

    /**
     * 获取日审记录
     * 
     * @param auditID
     * @return
     */
    public DaDailyaudit getDaDailyAudit(Serializable auditID);
    
    /**
	 * 提供给审核的接口方法，用于更新item表数据
	 * 
	 * @param notesMan
	 *            操作人名字
	 * @param orderId
	 *            订单Id
	 * @param orderType
	 *            1:散客, 2:tmc订单
	 * @param auditType
	 *            1: 入住审核, 2: 退房审核
	 * @param results
	 *            审核结果数组
	 * @param bFinish
	 *            改订单是否审核已完成
	 * @param orderState
	 *            看OrderState类
	 * @return 0: 成功, 1:失败, 2:财务已获取
	 */
	public int noteAuditResult(String notesMan, Long orderId, int orderType, int auditType, Date night,
			AuditResult[] results, boolean bFinish, int orderState); 
    
    /**
     * 根据渠道ID和给的日期获取传真号码
     * 
     * @param channelId
     * @param now
     * @return
     */
    public String getFaxNoByChannelId(Long channelId, Date now);
    
    /**
     * 获取退房日审传真信息
     * 
     * @param dailyAudit
     * @return
     */
    public List getItemsForAuditFaxCheckout(DaDailyaudit dailyAudit);
    
    /**
     * 获取入住日审传真信息
     * 
     * @param dailyAudit
     * @return
     */
    public List getItemsForAuditFaxCheckin(DaDailyaudit dailyAudit);

    public void saveOrUpdateDailyAudit(DaDailyaudit dailyAudit);
    
    /**
     * 根据日期获取OrAuditFaxLog
     * 
     * @param data
     * @return
     */
    public List getAuditLogList(Date data);
    
    /**
     * 发送日审传真
     * 
     * @param dailyAudit
     * @param faxNo
     * @param user
     * @return
     */
    public Long sendAuditFax(DaDailyaudit dailyAudit, String faxNo, 
    		OrWorkStates user,String remarkToHotel);
    
    /**
     * 记录日志
     * 
     * @param seanState
     * @param channelId
     * @param night
     * @param fax
     * @param user
     * @param channelName
     */
    public void noteLog(boolean seanState, Long channelId, Date night, 
    		String fax, OrWorkStates user, String channelName,String remarkToHotel);
    
    /**
     * 获取日审传真发送次数
     * 
     * @param auditId
     * @return
     */
    public List getPaperFaxItem(Long auditId);
    
    public void saveOrUpdateFaxItem(DaPaperFaxItem faxItem);
    
    /**
     * 根据渠道名称获取渠道
     * 
     * @param data
     * @return
     */
    public List getAuditInfoByChannelName(String channelName);
}
