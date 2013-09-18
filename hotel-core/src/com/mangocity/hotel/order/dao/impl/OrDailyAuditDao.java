package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.constant.DailyAuditConstant;
import com.mangocity.hotel.order.dao.IOrDailyAuditDao;
import com.mangocity.hotel.order.persistence.DaAuditingWorkload;
import com.mangocity.hotel.order.persistence.DaDailyaudit;
import com.mangocity.hotel.order.persistence.DaDailyauditItem;
import com.mangocity.hotel.order.persistence.DaDailyauditItemSubtable;
import com.mangocity.hotel.order.persistence.DaPersonalWorkload;
import com.mangocity.hotel.order.persistence.DaReturnvisit;
import com.mangocity.hotel.order.persistence.HtlAuditInfo;
import com.mangocity.hotel.order.persistence.OrDailyAudit;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
/**
 * OrDailyAudit Dao类
 * 
 * @author chenkeming
 * 
 */
public class OrDailyAuditDao extends GenericDAOHibernateImpl implements IOrDailyAuditDao{
	
	/**
     * loadObject改为loadOrDailyAudit
     * @param objID
     * @return
     */
    public OrDailyAudit loadOrDailyAudit(Serializable objID) {
        return (OrDailyAudit) super.get(OrDailyAudit.class, objID);
    }

    /**
     * 插入或更新obj.parasoft-suppress PB.IMC "与父类同名,暂不修改"
     * 
     * @param obj
     */
    public void saveOrUpdateOrDailyAudit(OrDailyAudit orDailyAudit) {
        super.saveOrUpdate(orDailyAudit);
    }
    
    /**
     * 命名查询
     * @param queryID
     * @param params
     */
    public List queryByNamedQuery(String queryID, Object[] params) {
        return super.getHibernateTemplate().findByNamedQuery(queryID, params);
    }
    
    /**
     * 检查用户是否有自动分配的回访记录,有则返回true
     * @param loginId
     * @return
     */
    public boolean checkUserhasAutoReturnRecord(String loginId){
    	String hsql = " from DaReturnvisit d where d.acquireid = ? and d.aquireway = 1";
    	List<DaReturnvisit> rtnVisitList = super.query(hsql, new Object[]{loginId});
    
    	return rtnVisitList.isEmpty()? false:true;
    }    
    /**
     * 更新回访的订单为以统计
     * @param returnID
     */
    public void setHasReturnMark(Long returnID){
    	String hql = "update DaDailyauditItem  set hasReturnMark = ? where returnvisitid='"+ returnID +"' and hasReturnMark = 0";
    	Object[] values = new Object[1];
    	values[0] = 1;
    	super.updateByQL(hql,values);
    }
    
    
    
    /**
     * 根据类型为用户自动分配一条回访记录
     * @param loginId
     * @param Name
     * @param type
     * @return 返回成功分配的记录,如无则返回null
     */
    public DaReturnvisit allotReturnRecordForUserByType(String loginId, String name,int type){
    	StringBuilder hqlBuildStr =new StringBuilder();
    	//未审核,未分配,当天,
    	hqlBuildStr.append("from DaReturnvisit where returnstate=0 and aquireState=2 ");
    	List result = null;
    	switch (type) {
			case DailyAuditConstant.DELAY_RETURN_TYPE:
				//预约时间到
				hqlBuildStr.append("and reassigntime is not null and sysdate>=reassigntime order by reassigntime asc");
				break;
			case DailyAuditConstant.RADOM_RETURN_TYPE:
				//随机,这个时候要注意把预约时间未到的记录排除
				hqlBuildStr.append("and reassigntime is null order by auditordate asc");
				break;
			default:
				return null;
		}
    	result = super.query(hqlBuildStr.toString(), null,0,0, false);
    	
    	return result.size()>0?(DaReturnvisit)result.get(0):null;
	
    }
    
    /**
     * 检查用户是否有自动分配的日审记录,有则返回true addby juesuchen
     * @param loginId
     * @return
     */
    public boolean checkUserhasAutoAuditRecord(String loginId){
    	String hsql = "select count(ID) from DaDailyaudit where acquireid=? and aquireWay=1 and aquireState=1";
    	List result = super.query(hsql, new Object[]{loginId},0,0,false);
    	Long countRow = (Long)result.get(0);
    	return countRow > 0  ? true:false;
    }
    
    /**
     * 同一联系人的所有回访记录(不包括非延时)
     * @param returnvisit
     * @return
     */
    
    public void sameContactData(DaReturnvisit returnvisit){
    	StringBuilder sqllBuildStr =new StringBuilder();
    	sqllBuildStr.append("update DaReturnvisit ret set acquireid=?,acquirename=?,acquiretime=?,aquirestate=?,aquireway=?"); 
    	sqllBuildStr.append("where ret.aquirestate = 2 and ret.returnstate = 0 and ret.linkmannumber=?"); 
    	Object[] values = new Object[6];
		values[0] = returnvisit.getAcquireid();
		values[1] = returnvisit.getAcquirename();
		values[2] = returnvisit.getAcquiretime();
		values[3] = returnvisit.getAquirestate();
		values[4] = returnvisit.getAquireway();
		values[5] = returnvisit.getLinkmannumber();
		super.updateByQL(sqllBuildStr.toString(), values);
    }
    
    /**
     * 根据类型为用户自动分配一条审核记录addby juesuchen
     * @param loginId
     * @param Name
     * @param type
     * @return 返回成功分配的记录,如无则返回null
     */
    public DaDailyaudit allotAuditRecordForUserByType(String loginId, String name,int type){
    	//未审核,未分配,当天,
    	StringBuilder hqlBuildStr =new StringBuilder();
    	hqlBuildStr.append("from DaDailyaudit where state=0 and aquireState=2 and channelid is not null ");
    	switch (type) {
			case DailyAuditConstant.DELAY_AUDIT_TYPE:
				//预约时间到
				hqlBuildStr.append("and reassigntime is not null and sysdate>=reassigntime order by reassigntime asc");
				break;
			case DailyAuditConstant.RETURN_AUDIT_TYPE:
				//已回传,这个时候要注意把预约时间未到的记录排除
				hqlBuildStr.append("and isreturn=1 and reassigntime is null");
				break;
			case DailyAuditConstant.RADOM_AUDIT_TYPE:
				//随机,这个时候要注意把预约时间未到的记录排除
				hqlBuildStr.append("and reassigntime is null order by auditdate asc");
				break;
			default:
				return null;
		}
    	List result = super.query(hqlBuildStr.toString(), null,0,0, false) ;
		return result.size()>0?(DaDailyaudit)result.get(0):null;
    }
    
    /**
     * 根据酒店ID取得其名称
     * @param sid
     * @return
     */
    public String getHotelNameById(Long sid){
    	String hql = "select chnName from HtlHotel where ID=?";
    	List result = super.query(hql, new Object[]{sid},0,0, false) ;
    	if(result.size() > 0){
    		return (String)result.get(0);
    	}
    	return null;
    }
    
    /**
     * 根据用户ID获取当天工作量 by juesuchen
     * @param auditorid
     * @param updatePersonelAudit
     * @return
     */
	public DaPersonalWorkload getPersonWorkloadByAuditId(String auditorid,int updatePersonelAudit) {
		String hql = "from DaPersonalWorkload where auditortime=trunc(sysdate) and auditorid=? and type=?";
		List<DaPersonalWorkload> workloadList = super.query(hql, new Object[]{auditorid,new Integer(updatePersonelAudit)});
        return workloadList.isEmpty()?null : workloadList.get(0);
	}
	
	/**
     * 根据类型获取当天工作量 by juesuchen
     * @param type 是回访组还是会员组
     * @return
     */
	public List getWorkloadByType(int type) {
		String hsql = "from DaAuditingWorkload where type=?";
		return super.query(hsql,new Object[]{(long)type},0,0,false);
	}
	
	/**
	 * 获取所有日审渠道信息
	 * @return
	 */
	public List getAllChannelInfo() {
		String hsql = "select ID,channelName from HtlAuditInfo";
		return super.query(hsql,null,0,0,false);
	}
	
	/**
	 * 根据日审ID取得回传URL
	 * @param auditId
	 * @return
	 */
	public String getHotelReturnURL(Long auditId) {
		String hsql = "select url from OrFaxLog where type='HB' and barCode=?";
		List result = super.query(hsql, new Object[]{String.valueOf(auditId)},0,0,false);
		if(!result.isEmpty())
			return (String)result.get(0);
		return !result.isEmpty()?(String)result.get(0):null;
	}
	/**
	 * 
	 * @param theOrderCd
	 * @return
	 */
	public List<DaDailyauditItem> getDaDailyauditItemByOrder(String theOrderCd) {
		String hql = "from DaDailyauditItem where orderCd=? order by audittype asc";
		return super.query(hql, new Object[]{theOrderCd},0,0,false);
	}
	
	/**
	 * 
	 * @param dailyauditid
	 * @return
	 */
	public Object[] getChaNameByAuditId(Long dailyauditid) {
		String hql = "select channelname,channelid from DaDailyaudit where ID=?";
    	List result = super.query(hql, new Object[]{dailyauditid}, 0,0,false) ;
		return result.size() > 0?(Object[])result.get(0):null;
	}
	
	/**
	 * 
	 * @param theHotelName
	 * @param theAuditDate
	 * @return
	 */
	public List getOrderCdsByHotelNameAndDate(String theHotelName, String theAuditDate) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select distinct(item.ordercd) from da_dailyaudit t,htl_hotel h ,");
		sqlBuilder.append("htl_audit_info_hotel ao,da_dailyaudit_item item where t.channelid = ao.auditinfoid");
		sqlBuilder.append(" and ao.hotelid = h.hotel_id and t.dailyaudit_id = item.dailyauditid ");
		sqlBuilder.append(" and t.auditdate=to_date('"+theAuditDate+"','yyyy-mm-dd') and upper(h.chn_name) like upper('%"+theHotelName+"%')");
		return super.queryByNativeSQL(sqlBuilder.toString(), 1, 30, null, null);
	}
	
	/**
	 * 更新订单的noshow原因
	 * @param orderid
	 * @param noshowCode
	 * @param orderType 
	 * @param noshow
	 */
	public void updateNoshowReason(Long orderid, Integer noshowCode, int orderType, String noshow) {
		String hql = "update VOrOrder set noshowCode=?,noshowReason=? where ID=? and orderType=?";
		Object[] values = {noshowCode.intValue(),noshow,orderid,orderType};
		super.updateByQL(hql, values);
	}
	
	/**
	 * 
	 * @param linkmannumber
	 * @return
	 */
	public DaReturnvisit findDaReturnvisitToday(String linkmannumber) {
		String hql = "from DaReturnvisit where linkmannumber = ? and auditordate = trunc(sysdate)";
		List<DaReturnvisit> returnVisitList = super.query(hql,new Object[]{linkmannumber});
		return returnVisitList.isEmpty()?null : returnVisitList.get(0);
	}
	
	/**
	 * 
	 * @param audit
	 */
	public void updateDaDailyauditBatchInSameChannel(DaDailyaudit audit) {
		//未完成,未获取,同渠道,自动方式
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append("update DaDailyaudit set acquireid=?,acquirename=?,acquiretime=?,aquireState=1,aquireWay=1 ");
		hqlBuilder.append(" where state=0 and channelid=? and aquireState=2 and aquireWay=1");
		Object[] values = new Object[4];
		values[0] = audit.getAcquireid();
		values[1] = audit.getAcquirename();
		values[2] = audit.getAcquiretime();
		values[3] = audit.getChannelid();
		super.updateByQL(hqlBuilder.toString(), values);
	}
	
	/**
	 * 
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public Object[] getHotelComfirmByOrderId(Long orderId, int orderType) {
		String hql = "select confirmNo,roomNum,auditRemark from VOrOrder where ID=? and orderType=?" ;
		List result = super.query(hql, new Object[]{orderId,orderType},0,0, false);
		return !result.isEmpty()?(Object[])result.get(0):null;
	}
	
	/**
	 * 
	 * @param orderId
	 * @param audit_type
	 * @return
	 */
	public boolean isCheckOutAuditDone(Long orderId, int audit_type) {
		String hsql = "from DaDailyauditItem where orderid=? and audittype=2";
		List<DaDailyauditItem> dailyAuditItemList = super.query(hsql, new Object[]{orderId});
		
		//如果已经退房审核完成,则入住审核和回访审核都不去更新item了
		return dailyAuditItemList.isEmpty()?false : 
			(dailyAuditItemList.get(0).getHasAuditMark() != null && dailyAuditItemList.get(0).getHasAuditMark() == 1);
	}
	
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
			String loginName, int assignType) {
		StringBuilder hqlBuilder =new StringBuilder();
		if(DailyAuditConstant.AUDIT_TYPE == assignType){
			//审核组
			hqlBuilder.append("update DaDailyaudit set acquireid=?,acquirename=?,acquiretime=sysdate,");
			hqlBuilder.append("deliverid=?,delivername=?,delivertime=sysdate,aquireWay=2,aquireState=1,");
			hqlBuilder.append("releaseReason=3 where ID in ("+ auditIds +") and state = 0");
		}else{
			//回访组
			hqlBuilder.append("update DaReturnvisit set acquireid=?,acquirename=?,acquiretime=sysdate,");
			hqlBuilder.append("deliverid=?,delivername=?,delivertime=sysdate,aquireway=2,aquirestate=1,");
			hqlBuilder.append("releasereason=3 where ID in ("+ auditIds +") and returnstate = 0");
		}
		Object[] values = new Object[]{assignToId,assignToName,loginId,loginName};
		super.updateByQL(hqlBuilder.toString(), values);
	}
	
	/**
	 * 日审备注保存到订单中,cc的保存到订单中，TMC的保存到日审记录中
	 * @param orderId
	 * @param orderType
	 * @param auditRemark
	 */
	public void updateAuditItemDataRemark(Long orderId, int orderType, String auditRemark) {
		String hql = null;
		if (null != auditRemark){
			auditRemark = auditRemark.replaceAll("\n", "&&");
			auditRemark = auditRemark.replaceAll("\r", "");
		}
		if(1 == orderType)
			hql = "update OrOrder set auditRemark=substr(?,1,2048) where ID=? ";
		else
			hql = "update DaDailyauditItem set remark=substr(?,1,2048) where orderid=? and orderType=2";
		super.updateByQL(hql, new Object[]{auditRemark,orderId});
	}
	
	/**
	 * 获取日审备注
	 * @param itemId
	 * @return
	 */
	public String getRemark(Long itemId) {
		String hql = "select remark from DaDailyauditItem where ID = ?";
		List result = super.query(hql, new Object[]{itemId},0,0,false);
		return !result.isEmpty()?(String)result.get(0):"";
	}
	
	/**
	 * 
	 * @param acquireid
	 * @param isAll
	 * @return
	 */
	public int getNoshowBackCountOrAll(String acquireid, boolean isAll) {
		StringBuilder hqlBuilder =new StringBuilder();
		hqlBuilder.append("select count(ID) from DaDailyauditItemSubtable ");
		hqlBuilder.append(" where reciprocal is not null and returnvisitid=? ");
		hqlBuilder.append(" and trunc(returnvisittime)=trunc(sysdate) and auditresults in (2,4)");
		if(!isAll)
			hqlBuilder.append(" and (reciprocal in ('1','3','5') or (reciprocal = 4 and rtAdvancecheckouttime > advancecheckouttime))");//已入住+正常退房+延住
		List result = super.query(hqlBuilder.toString(), new Object[]{acquireid},0,0,false);
		return !result.isEmpty()?Integer.valueOf(result.get(0).toString()).intValue():0;
	}
	
	/**
	 * 根据id查询DaReturnvisi
	 * @return
	 */
	public DaReturnvisit queryDaReturnvisitById(Class daReturnvisitClass, long returnVisitID){
		return (DaReturnvisit)super.get(daReturnvisitClass, returnVisitID);
	}
	
	/**
	 * 根据id查询DaDailyauditItem
	 * @return
	 */
	public List<DaDailyauditItem> queryDaDailyauditItemById(long returnVisitID){
		String hql = "from DaDailyauditItem where returnvisitid = ?";
		return super.query(hql, new Object[]{returnVisitID}, 0, 0, false);
	}
	
	/**
	 * 保存DaReturnvisit对象
	 * @param returnVisit
	 */
	public void saveOrUpdateDaReturnvisit(DaReturnvisit returnVisit){
		super.saveOrUpdate(returnVisit);
	}
	
	/**
	 * 根据ID查询DaDailyaudit
	 * @param daDailyauditClass
	 * @param daDailyauditId
	 * @return
	 */
	public DaDailyaudit queryDaDailyauditById(Class daDailyauditClass,Serializable daDailyauditId){
		return (DaDailyaudit)super.get(daDailyauditClass, daDailyauditId);
	}
	
	/**
	 * 查找一个渠道实体
	 * @param htlAuditInfoClass
	 * @param htlAuditInfoSid
	 * @return
	 */
	public HtlAuditInfo queryHtlAuditInfoById(Class htlAuditInfoClass,Serializable htlAuditInfoSid){
		return (HtlAuditInfo)super.get(htlAuditInfoClass, htlAuditInfoSid);
	}
	
	/**
	 * 保存DaDailyaudit对象
	 * @param audit
	 */
	public void saveOrUpdateDaDailyaudit(DaDailyaudit audit){
		super.saveOrUpdate(audit);
		
	}
	
	/**
	 * 查询实体DaDailyauditItemSubtable对象
	 * @param daDailyauditItemSubtableClass
	 * @param Id
	 * @return
	 */
	public DaDailyauditItemSubtable queryDaDailyauditItemSubtableById(Class daDailyauditItemSubtableClass, long daDailyauditItemSubtableId){
		return (DaDailyauditItemSubtable)super.get(daDailyauditItemSubtableClass, daDailyauditItemSubtableId);
	}
	
	/**
	 * 根据returnID获取DaDailyauditItem对象列表
	 * @param returnID
	 * @return
	 */
	public List<DaDailyauditItem> queryDaDailyauditItemByreturnID(Long returnID){
		String hql = "from DaDailyauditItem where returnvisitid = ?";
		return super.query(hql, new Object[]{returnID}, 0, 0, false);
	}
	
	/**
	 * 保存实体DaDailyauditItemSubtable对象
	 * @param vitemSub
	 */
	public void saveOrUpdateDaDailyauditItemSubtable(DaDailyauditItemSubtable vitemSub){
		super.saveOrUpdate(vitemSub);
	}
	
	/**
	 * 根据returnID和hasReturnMark获取回访的DaDailyauditItem对象列表
	 * @param returnID
	 * @return
	 */
	public List<DaDailyauditItem> queryDaDailyauditItemByReturnIDAndHasReturnMark(Long returnID){
		String hql = "from DaDailyauditItem where returnvisitid =? and (hasReturnMark is null or hasReturnMark = 0)";
		return super.query(hql, new Object[]{returnID}, 0, 0, false);
	}
	
	/**
	 * 批量保存日审明细表的集合
	 * @param dailyItems
	 */
	public void saveOrUpdateDaDailyauditItemCollection(Collection dailyItems){
		super.saveOrUpdateAll(dailyItems);
	}
	
	/**
	 * 批量保存日审工作量表
	 * @param auditWorkload
	 */
	public void saveOrUpdateDaAuditingWorkloadCollection(Collection auditWorkload){
		super.saveOrUpdateAll(auditWorkload);
	}
	
	/**
	 * 统计工作量数据
	 * @return
	 */
	public Object[] queryDataForDaAuditingWorkload(int type,int re_type){
		String hql = null;
		if(DailyAuditConstant.AUDIT_TYPE == re_type){
			hql = "select count(distinct i.hotelid), count(i.orderCd), sum(i.roomamount) from DaDailyaudit da, DaDailyauditItem i "
				+"where da.ID = i.dailyauditid and i.hasAuditMark = 0 and da.auditdate > trunc(sysdate - 15) and da.auditdate opt trunc(sysdate - 1)";
		}else{
			hql = "select count(distinct v.ID),count(i.orderCd),sum(i.roomamount) from DaReturnvisit v,DaDailyauditItem i "
				+"where v.ID = i.returnvisitid and i.hasReturnMark = 0 and v.auditordate > trunc(sysdate - 15) and v.auditordate opt trunc(sysdate)";
		}
		if(1 == type)//今天的
			hql = hql.replaceFirst("opt", "=");
		else
			hql = hql.replaceFirst("opt", "<");
		List result = super.query(hql, null, 0, 0, false);
		return !result.isEmpty()?(Object[])result.get(0):new Object[3];
	}
	
	/**
	 * 保存个人工作量表
	 * @param thePerson
	 */
	public void saveOrUpdateDaPersonalWorkload(DaPersonalWorkload thePerson){
		super.saveOrUpdate(thePerson);
	}
	/**
	 * 保存日审工作量表
	 * @param toLoad
	 */
	public void saveOrUpdateDaAuditingWorkload(DaAuditingWorkload toLoad){
		super.saveOrUpdate(toLoad);
	}
	
	/**
	 * 保存日审明细表
	 * @param item
	 */
	public void saveorupdatedadailyaudititem(DaDailyauditItem item){
		super.saveOrUpdate(item);
	}
	
	  /**
     * 根据参数获得OrDailyAudit对象
     * 
     * @param date
     * @return
     */

    public List<OrDailyAudit> getNewOrDailyAudit(Date date) {

    	return queryByNamedQuery("hQueryOrder_Hotel", new Object[] { date });

    }
    
    
    /**
     * 取出操作人员现处理数量
     * 
     * @param date
     * @param max
     */
    public List getUserWorkNum(Date date) {
        String hsql = "select od.assignTo,od.orderNumbers from OrDailyAudit od "
            + "where od.checkNight = ? and od.assignTo is not null order by od.assignTo";

        return query(hsql, new Object[] { date });
    }
    
    /**
     * 取出未被分配的酒店订单
     * 
     * @param date
     * @param max
     */

    public List getOrDailyAuditIsNull(Date date) {
        String hsql = "from OrDailyAudit od where od.checkNight = ? and od.assignTo is null  order by od.orderNumbers desc";

        return query(hsql, new Object[] { date });
    }
	   /**
     * 根据参数获得orDailyAudit对象
     * 
     * @param hotelId
     * @param date
     * @return
     */
    public List<OrDailyAudit> findSimilarDailyAudit(Object hotelId, Date date) {
        return queryByNamedQuery("hQueryAudit_Daily", new Object[] { hotelId,date });

    }
    
    public DaDailyauditItemSubtable queryAuditItemSubForCheckOut(Long auditItemSubId){
    	String sql = " select * "+
    	   " from Da_Dailyaudit_Item_Subtable ddi "+
    	   " where ddi.dailyaudit_item_id in "+
    	   " (select dd.dailyaudit_item_id "+
    	   " from da_dailyaudit_item dd "+
    	   " where dd.audittype = 2 "+
    	   " and dd.orderid in "+
    	   " (select d.orderid "+
    	   " from da_dailyaudit_item d "+
    	   " where d.dailyaudit_item_id in "+
    	   " (select sub.dailyaudit_item_id "+
    	   " from Da_Dailyaudit_Item_Subtable sub "+
    	   " where sub.dailyaudit_item_subtable_id = ? "+
    	   " and sub.roomindex = ddi.roomindex "+
    	   " ) "+ 
    	   " and d.returnvisitid is not null "+
    	   " and d.audittype = 1)) ";
    	List<DaDailyauditItemSubtable> auditItemSubList = super.queryByNativeSQL(sql,0,1,new Object[]{auditItemSubId},DaDailyauditItemSubtable.class);
    	if(null == auditItemSubList || auditItemSubList.size() == 0 ){
			return null;
		}else{
			return auditItemSubList.get(0);
		}
    }

}
