package com.mangocity.hotel.order.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.mangocity.hotel.order.persistence.DaAuditingWorkload;
import com.mangocity.hotel.order.persistence.DaDailyaudit;
import com.mangocity.hotel.order.persistence.DaDailyauditItem;
import com.mangocity.hotel.order.persistence.DaDailyauditItemSubtable;
import com.mangocity.hotel.order.persistence.DaPersonalWorkload;
import com.mangocity.hotel.order.persistence.DaReturnvisit;
import com.mangocity.hotel.order.persistence.HtlAuditInfo;
import com.mangocity.hotel.order.persistence.OrDailyAudit;
import com.mangocity.hotel.order.persistence.VOrOrder;


/**
 * OrDailyAudit Dao类
 * 
 * @author chenkeming
 * 
 */
public interface IOrDailyAuditDao{
	
	/**
     * loadObject改为loadOrDailyAudit
     * @param objID
     * @return
     */
    public OrDailyAudit loadOrDailyAudit(Serializable objID);

    /**
     * 插入或更新obj
     * 
     * @param obj
     */
    public void saveOrUpdateOrDailyAudit(OrDailyAudit obj) ;
    
    /**
     * 命名查询
     * @param queryID
     * @param params
     */
    public List<VOrOrder> queryByNamedQuery(String queryID, Object[] params) ;
    
    /**
     * 检查用户是否有自动分配的回访记录,有则返回true
     * @param loginId
     * @return
     */
    public boolean checkUserhasAutoReturnRecord(String loginId); 
    /**
     * 更新回访的订单为以统计
     * @param returnID
     */
    public void setHasReturnMark(Long returnID);
    
    
    
    /**
     * 根据类型为用户自动分配一条回访记录
     * @param loginId
     * @param Name
     * @param type
     * @return 返回成功分配的记录,如无则返回null
     */
    public DaReturnvisit allotReturnRecordForUserByType(String loginId, String name,int type);
    
    /**
     * 检查用户是否有自动分配的日审记录,有则返回true addby juesuchen
     * @param loginId
     * @return
     */
    public boolean checkUserhasAutoAuditRecord(String loginId);
    
    /**
     * 同一联系人的所有回访记录(不包括非延时)
     * @param returnvisit
     * @return
     */
    
    public void sameContactData(DaReturnvisit returnvisit);
    
    /**
     * 根据类型为用户自动分配一条审核记录addby juesuchen
     * @param loginId
     * @param Name
     * @param type
     * @return 返回成功分配的记录,如无则返回null
     */
    public DaDailyaudit allotAuditRecordForUserByType(String loginId, String name,int type);
    
    /**
     * 根据酒店ID取得其名称
     * @param sid
     * @return
     */
    public String getHotelNameById(Long sid);
    
    /**
     * 根据用户ID获取当天工作量 by juesuchen
     * @param auditorid
     * @param updatePersonelAudit
     * @return
     */
	public DaPersonalWorkload getPersonWorkloadByAuditId(String auditorid,int updatePersonelAudit);
	
	/**
     * 根据类型获取当天工作量 by juesuchen
     * @param type 是回访组还是会员组
     * @return
     */
	public List<DaAuditingWorkload> getWorkloadByType(int type);
	
	/**
	 * 获取所有日审渠道信息
	 * @return
	 */
	public List getAllChannelInfo();
	
	/**
	 * 根据日审ID取得回传URL
	 * @param auditId
	 * @return
	 */
	public String getHotelReturnURL(Long auditId);
	/**
	 * 
	 * @param theOrderCd
	 * @return
	 */
	public List<DaDailyauditItem> getDaDailyauditItemByOrder(String theOrderCd);
	
	/**
	 * 
	 * @param dailyauditid
	 * @return
	 */
	public Object[] getChaNameByAuditId(Long dailyauditid);
	
	/**
	 * 
	 * @param theHotelName
	 * @param theAuditDate
	 * @return
	 */
	public List getOrderCdsByHotelNameAndDate(String theHotelName, String theAuditDate);
	
	/**
	 * 更新订单的noshow原因
	 * @param orderid
	 * @param noshowCode
	 * @param orderType 
	 * @param noshow
	 */
	public void updateNoshowReason(Long orderid, Integer noshowCode, int orderType, String noshow);
	
	/**
	 * 
	 * @param linkmannumber
	 * @return
	 */
	public DaReturnvisit findDaReturnvisitToday(String linkmannumber);
	
	/**
	 * 
	 * @param audit
	 */
	public void updateDaDailyauditBatchInSameChannel(DaDailyaudit audit);
	
	/**
	 * 
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public Object[] getHotelComfirmByOrderId(Long orderId, int orderType);
	
	/**
	 * 
	 * @param orderId
	 * @param audit_type
	 * @return
	 */
	public boolean isCheckOutAuditDone(Long orderId, int audit_type);
	
	/**
	 * 
	 * @param assignToId
	 * @param assignToName
	 * @param auditIds
	 * @param loginId
	 * @param loginName
	 * @param assignType
	 */
	public void assignAuditByMid(String assignToId, String assignToName, String auditIds, String loginId,
			String loginName, int assignType);
	
	/**
	 * 日审备注保存到订单中,cc的保存到订单中，TMC的保存到日审记录中
	 * @param orderId
	 * @param orderType
	 * @param auditRemark
	 */
	public void updateAuditItemDataRemark(Long orderId, int orderType, String auditRemark);
	/**
	 * 获取日审备注
	 * @param itemId
	 * @return
	 */
	public String getRemark(Long itemId);
	
	/**
	 * 
	 * @param acquireid
	 * @param isAll
	 * @return
	 */
	public int getNoshowBackCountOrAll(String acquireid, boolean isAll);
	
	/**
	 * 根据id查询回访总表
	 * @return
	 */
	public DaReturnvisit queryDaReturnvisitById(Class daReturnvisitClass, long returnVisitID);
	
	/**
	 * 根据id查询日审明细表
	 * @return
	 */
	public List<DaDailyauditItem> queryDaDailyauditItemById(long returnVisitID);
	
	/**
	 * 保存回访总表对象
	 * @param returnVisit
	 */
	public void saveOrUpdateDaReturnvisit(DaReturnvisit returnVisit);
	
	/**
	 * 根据ID查询日审表对象
	 * @param daDailyauditClass
	 * @param daDailyauditId
	 * @return
	 */
	public DaDailyaudit queryDaDailyauditById(Class daDailyauditClass,Serializable daDailyauditId);
	
	/**
	 * 查找一个渠道实体
	 * @param htlAuditInfoClass
	 * @param htlAuditInfoSid
	 * @return
	 */
	public HtlAuditInfo queryHtlAuditInfoById(Class htlAuditInfoClass,Serializable htlAuditInfoSid);
	
	/**
	 * 保存日审表对象
	 * @param audit
	 */
	public void saveOrUpdateDaDailyaudit(DaDailyaudit audit);
	
	/**
	 * 查询实体日审明细子表对象
	 * @param daDailyauditItemSubtableClass
	 * @param Id
	 * @return
	 */
	public DaDailyauditItemSubtable queryDaDailyauditItemSubtableById(Class daDailyauditItemSubtableClass, long daDailyauditItemSubtableId);
	
	/**
	 * 根据returnID获取日审明细表对象列表
	 * @param returnID
	 * @return
	 */
	public List<DaDailyauditItem> queryDaDailyauditItemByreturnID(Long returnID);
	
	/**
	 * 保存实体日审明细子表对象
	 * @param vitemSub
	 */
	public void saveOrUpdateDaDailyauditItemSubtable(DaDailyauditItemSubtable vitemSub);
	
	/**
	 * 根据returnID和hasReturnMark获取回访的DaDailyauditItem对象列表
	 * @param returnID
	 * @return
	 */
	public List<DaDailyauditItem> queryDaDailyauditItemByReturnIDAndHasReturnMark(Long returnID);
	
	/**
	 * 批量保存日审明细表的集合
	 * @param dailyItems
	 */
	public void saveOrUpdateDaDailyauditItemCollection(Collection dailyItems);
	
	/**
	 * 批量保存日审工作量表
	 * @param auditWorkload
	 */
	public void saveOrUpdateDaAuditingWorkloadCollection(Collection auditWorkload);
	
	/**
	 * 统计工作量数据
	 * @return
	 */
	public Object[] queryDataForDaAuditingWorkload(int type,int re_type);
	
	/**
	 * 保存个人工作量表
	 * @param thePerson
	 */
	public void saveOrUpdateDaPersonalWorkload(DaPersonalWorkload thePerson);
	
	/**
	 * 保存日审工作量表
	 * @param toLoad
	 */
	public void saveOrUpdateDaAuditingWorkload(DaAuditingWorkload toLoad);
	
	/**
	 * 保存日审明细表
	 * @param item
	 */
	public void saveorupdatedadailyaudititem(DaDailyauditItem item);
	
	/**
	 * 查询退房审核，把入住回访的结果记录到退房审核中
	 */
	public DaDailyauditItemSubtable queryAuditItemSubForCheckOut(Long auditItemSubId);
}
