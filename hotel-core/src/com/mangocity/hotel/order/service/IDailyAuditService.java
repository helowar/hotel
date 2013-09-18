package com.mangocity.hotel.order.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.order.persistence.DaDailyaudit;
import com.mangocity.hotel.order.persistence.DaPersonalWorkload;
import com.mangocity.hotel.order.persistence.DaReturnvisit;
import com.mangocity.hotel.order.persistence.HtlAuditInfo;
import com.mangocity.hotel.order.service.assistant.AuditItemDetailInfo;
import com.mangocity.hotel.order.service.assistant.DaAuditDetailInfo;
import com.mangocity.hotel.order.service.assistant.DaAuditRoomDetailInfo;
import com.mangocity.hotel.order.service.assistant.HotelAuditDetail;
import com.mangocity.hotel.order.service.assistant.HtlChannelDetailInfo;
import com.mangocity.hotel.order.service.assistant.ReturnInfo;


/**
 * 日审重构 service 接口
 */
public interface IDailyAuditService {
	/**
	 * 获得回访数据
	 * @param returnVisitID
	 * @return
	 */
	public DaReturnvisit getReturnvisit(long returnVisitID); 
	
	/**
	 *  封装所要查看的回访记录 
	 * @param returnVisitID 回访ID
	 * @return
	 */
	public List<ReturnInfo> getReturnData(long returnVisitID);
	
	/**
	 * 自动分配一条回访记录,如所有记录已分完毕则返回 false
	 * @param loginId 登录人ID
	 * @param userName 登录人姓名
	 * @return
	 */
	public boolean autoAllotReturnForUser(String loginId,String userName);
	
	/**
	 * 手动获取一条回访记录到我的工作档案中
	 * @param loginID 登录号
	 * @param userName 登录人姓名
	 * @param returnVisitID 回访ID
	 * @return true 获取成功，false获取失败
	 */
	public boolean achieveRetrunForUser(String loginID,String userName,String returnVisitID);
	
	
	
	
	
    /**
	 * 为用户自动分配一条审核记录,如所有记录已分完毕则返回 false
	 * @param Name 
	 * @param loginId 
	 * @return
	 */
    public boolean autoAllotAuditForUser(String loginId, String Name);

    /**
	 * 根据Id 查找一个实体对象
	 * @param id
	 * @return
	 */
    /**
	 * 查找一个渠道实体
	 * @param sid
	 * @return
	 */
    public HtlAuditInfo findHtlAuditInfoById(Serializable sid);
    public DaDailyaudit findDaDailyauditById(Serializable id);
    /**
	 * 保存一个实体对象
	 * @param audit
	 */
    public void updateDaDailyaudit(DaDailyaudit audit);

    /**
	 * 根据日审记录返回日审明细对象
	 * @param audit
	 * @param htlId 如果有指定酒店ID，则只会拿此酒店的记录
	 * @return
	 */
    public List<DaAuditDetailInfo> getAuditDetailsByDaDailyaudit(DaDailyaudit audit,Object htlId);
    /**
	 * 根据渠道记录返回渠道明细对象
	 * @param htlAudit
	 * @param date 
     * @param auditID 
	 * @return
	 */
    public HtlChannelDetailInfo getChannelDetailsByHtlAuditInfo(
            HtlAuditInfo htlAudit, Date date, Long auditID);
    /**
	 * 根据渠道ID,页面数据和操作方式保存到数据库
	 * @param operateType 
     * @param workState 
	 * @param htlAudit
	 * @param date 
	 */
    public void updateDaDailyaudit(Long auditId,
            Map roomInfosMap, int operateType, boolean workState);
    /**
     * 更新回访数据
     * @param roomInfosMap
     * @param operateType
     * @param userSate 
     */
    public void updateReturnData(String orderMount,String roomMount,String delayTime,String relaxReason,String returnID,String loginID,String userName,List roomInfosMap, int operateType, boolean userSate);
    /**
     * 更新日审明细中的备注
     * @param loginName 
     * @param loginId 
     */
    public void updateAuditItemDataRemark(List<AuditItemDetailInfo> list, String loginId, String loginName);
    /**
     * 根据用户ID获取当天工作量 by juesuchen
     * @param auditorid
     * @return
     */
    public DaPersonalWorkload getPersonWorkloadByAuditId(String auditorid,int type);
    /**
     * 根据类型获取当天工作量 by juesuchen
     * @param type 是回访组还是会员组
     * @return
     */
    public List getWorkloadByType(int type);

	public List getAllChannelInfo();

	public List<HotelAuditDetail> queryAuditInfo(String theHotelName, String theAuditDate, String theOrderCd, int i);

	public int saveAuditInfo(String noteMan,Map<Long,DaAuditRoomDetailInfo> roomInfosMap);
	/**
	 * 分配日审记录
	 * @param assignToId 被分配人ID
	 * @param assignToName 被分配人名字
	 * @param auditIds 被分配日审记录ID
	 */
	public void assignAuditByMid(String assignToId, String assignToName, String auditIds,String loginId,String loginName,int assignType);

}
