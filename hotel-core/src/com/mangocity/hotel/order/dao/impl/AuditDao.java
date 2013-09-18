package com.mangocity.hotel.order.dao.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.driver.OracleTypes;

import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.order.constant.OrderItemAuditState;
import com.mangocity.hotel.order.dao.IAuditDao;
import com.mangocity.hotel.order.persistence.Audit;
import com.mangocity.hotel.order.persistence.AuditHotel;
import com.mangocity.hotel.order.persistence.AuditItem;
import com.mangocity.hotel.order.persistence.DaDailyaudit;
import com.mangocity.hotel.order.persistence.HtlAuditInfoSetup;
import com.mangocity.hotel.order.persistence.VOrOrder;
import com.mangocity.hotel.order.persistence.VOrOrderItem;
import com.mangocity.util.StringUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * 日审记录(Audit)Dao类
 * 
 * @author yong.zeng
 * 
 */
public class AuditDao extends GenericDAOHibernateImpl implements IAuditDao {


	/**
	 * 
	 * 根据AuditHotel主键获取AuditHotel
	 * 
	 * @param hotelId
	 * @return
	 */
	public AuditHotel getAuditHotel(long hotelId) {
		return load(AuditHotel.class, Long.valueOf(hotelId));
	}

	/**
	 * 根据Audit主键获取Audit
	 * 
	 * @param auditId
	 * @return
	 */
	public Audit getAudit(long auditId) {
		return load(Audit.class, Long.valueOf(auditId));
	}

	/**
	 * 根据audit的主键列表查询auditItem
	 * 
	 * @param auditIds
	 * @return
	 */
	public List<AuditItem> queryAuditItemByAuditIds(final Long[] auditIds) {
		StringBuffer hql = new StringBuffer().append("from AuditItem a where 1=1 ");
		StringUtil.compositeHSql(hql, auditIds,
		 "a.audit.ID");//compositeHSql方法移到StringUtil里
		hql.append(" order by a.roomIndex, a.fellowDate");
		return super.query(hql.toString(), null);
	}

	/**
	 * 根据日期,酒店id获取HotelAudit对象
	 * 
	 * @param date
	 * @param hotelId
	 * @return
	 */
	public AuditHotel getHotelByDateAndHotel(Date date, Long hotelId) {
		List<AuditHotel> list = queryByNamedQuery("hQueryByDateAndHotel_AuditHotel", new Object[] { date, hotelId });
		// if(list.isEmpty())return new AuditHotel();
		return list.get(0);
	}

	/**
	 * 根据名称查询
	 */
	public <T> List<T> queryByNamedQuery(String queryID, Object[] paramValues) {
		return super.queryByNamedQuery(queryID, paramValues);
	}

	/**
	 * 获取AuditHotel根据id列表
	 * 
	 * @param objs
	 * @return
	 */
	public List<AuditHotel> queryAuditHotelsByIds(Object[] objs) {
		return super.query("select a from AuditHotel a where a.ID in (?)", objs);
	}

	public void batchArrangeAuditHotel(Long[] hotelId, OrWorkStates user) {
		StringBuffer sbArr = new StringBuffer();
		for (int i = 0; i < hotelId.length; i++) {
			sbArr.append(String.valueOf(hotelId[i].longValue()));
			sbArr.append(",");
		}
		String hotelIds = sbArr.toString();
		if (hotelIds.endsWith(",")) {
			hotelIds = hotelIds.substring(0, hotelIds.length() - 1);
		}
		super.updateByQL("update OrDailyAudit od set od.assignTo=(?) where od.ID in(?)", new Object[] {
				user.getLogonId(), hotelIds });
	}

	/**
	 * 根据审核日期和渠道获取审核的批次ID
	 * 
	 * @param auditDate
	 * @param channelId
	 * @return
	 */
	@SuppressWarnings( { "deprecation", "unchecked" })
	public long getBathIdByAutoAudit(String auditDate, Long channelId) {
		// 输入参数的索引和参数值键值对
		Map inParamIdxAndValue = new HashMap(2);
		inParamIdxAndValue.put(1, auditDate);
		inParamIdxAndValue.put(2, channelId);

		// 输出参数的索引和数据类型键值对
		Map<Integer, Integer> outParamIdxAndType = new HashMap<Integer, Integer>(1);
		outParamIdxAndType.put(3, OracleTypes.NUMERIC);

		Map<Integer, ?> resultMap = super.execProcedure("{call prc_autoAudit_call(?,?,?)}", inParamIdxAndValue,
				outParamIdxAndType);

		return Long.valueOf(resultMap.get(Integer.valueOf(3)).toString()).longValue();
	}
	
	/**
	 * 根据审核批次ID获取审核记录
	 * @param bathId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAuditRecordsByTempAuditinfo(long bathId) {
		StringBuilder sql = new StringBuilder(200);
		sql.append("select channelName,hotelNumber,orderNumber,resType ").append(
				" from tmp_auditinfolist where batchid= ? ").append(" order by restype,channelName ");
		List<Object[]> resultList = super.queryByNativeSQL(sql.toString(), 0, 0, new Object[] { bathId }, null);
		if (resultList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return resultList;
	}
	
	/**
	 * 根据ID获取审核信息
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HtlAuditInfoSetup> getAuditInfoSetup(Long id){
		String hsql = " from HtlAuditInfoSetup where htlAuditInfo.ID = ?";
		List<HtlAuditInfoSetup>  lstAuditInfo =  super.find(hsql, new Object[] {id });
		if (lstAuditInfo.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return lstAuditInfo;
	}
	
	/**
	 * 获取退房日审传真信息
	 * 
	 * @param dailyAudit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getItemsForAuditFaxCheckout(DaDailyaudit dailyAudit) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select i.orderCd, i.roomname, o.fellowNames, o.confirmNo, i.checkintime, ")
			.append( "s.roomnumber, s.checkinname, 2 as aType, i.hotelName ")
			.append( " from DaDailyaudit d, DaDailyauditItem i, DaDailyauditItemSubtable s, VOrOrder o  where ")
			.append( "	d.ID = ? 	and i.dailyauditid = d.ID 	and i.hasAuditMark = 0 ")
			.append( "	and s.dailyauditItemID = i.ID  and i.audittype = 2  and o.ID = i.orderid ")
			.append( " and o.orderType = i.orderType  order by i.hotelid,i.orderid,s.ID");
		List<Object[]> itemCheckoutList= super.find(sql.toString(), new Object[] { dailyAudit.getID() });
		if (itemCheckoutList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return itemCheckoutList;
	}
	
	/**
	 * 获取入住日审传真信息
	 * 
	 * @param dailyAudit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getItemsForAuditFaxCheckin(DaDailyaudit dailyAudit) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select i.orderCd, i.roomname, o.fellowNames, o.confirmNo, i.checkintime, ")
			.append("o.roomNum as roomnumber, s.checkinname, 1 as aType, i.hotelName ")
			.append(" from DaDailyaudit d, DaDailyauditItem i, DaDailyauditItemSubtable s, VOrOrder o  where ")
			.append("	d.ID = ? 	and i.dailyauditid = d.ID 	and i.hasAuditMark = 0 ")
			.append("	and s.dailyauditItemID = i.ID  and i.audittype = 1  and o.ID = i.orderid ")
			.append(" and o.orderType = i.orderType order by i.hotelid,i.orderid,s.ID");
		List<Object[]> itemCheckinList= super.find(sql.toString(), new Object[] { dailyAudit.getID() });
		if (itemCheckinList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return itemCheckinList;
	}
	
	/**
	 * 更新入住人或房间号
	 * 
	 * @param order
	 * @param orderItem
	 * @param fellowName
	 * @param roomNo
	 * @param notesMan
	 */
	public void updateNameAndRoomNum(VOrOrder order, VOrOrderItem orderItem, String fellowName, String roomNo,
			String notesMan) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update VOrOrderItem set ")
			.append(StringUtil.isValidStr(fellowName) ? ("fellowName = '" + fellowName.replaceAll("'", "''") + "', "): "")
		    .append(StringUtil.isValidStr(roomNo) ? ("roomNo = '" + roomNo + "', ") : "")
			.append("notesMan = ? where orderId = ? " + " and roomIndex = ? and night >= ? and settlement = 0");
		super.updateByQL(sql.toString(),new Object[] {notesMan,order.getID(),orderItem.getRoomIndex(),order.getCheckinDate()});
		
	}
	
	/**
	 * 更新订单视图的orderitem
	 * @param obj
	 */
	public  void updateVOrOrderItem(Object[] obj){
		StringBuilder sql = new StringBuilder();
		sql.append(" update VOrOrderItem set notesMan = ?, orderState = ?,  show = ?, auditState = ?")
		   .append("where orderId = ?  and roomIndex = ?  and night > ?  and settlement = 0");
		super.updateByQL(sql.toString(),obj);
		
	}
	
	
	/**
	 *：住２天，而且只生成退房审核时，要更新第一天的入住情况 时 更新订单视图的orderitem
	 * @param obj
	 */
	public  void updateVOrOrderItemTwoDays(Object[] obj){
		StringBuilder sql = new StringBuilder();
		sql.append(" update VOrOrderItem set notesMan = ?, orderState = 1, show = 0,  auditState = ")
		   .append(OrderItemAuditState.ACHIEVE).append(" where orderId = ? and version = ? ")
		   .append(" and roomIndex = ?  and night < ?  and settlement = 0 ");
		super.updateByQL(sql.toString(),obj);
		
	}
	
	/**
	 * 住２天，而且只生成退房审核时，要更新每一天的入住情况 时 更新订单视图的orderitem
	 * @param obj
	 */
	public void  updateVorderItemShow(Object[] obj){
		StringBuilder sql = new StringBuilder();
		sql.append(" update VOrOrderItem set notesMan = ?, orderState = 2,  show = 1, ")
		   .append(" auditState = ").append(OrderItemAuditState.ACHIEVE).append(" where orderId = ? ")
		   .append(" and roomIndex = ?  and night >= ? and lastNight = 0 and settlement = 0");
		super.updateByQL(sql.toString(),obj);
	}
	
	public boolean checkHotel(long hotelId) {
        String hql = " select count(*) from HotelFax where hotelId = ? " + hotelId;        
        List<?> hotelFaxList = super.query(hql, new Object[]{Long.valueOf(hotelId)});
        
        return hotelFaxList.isEmpty()?false : true;
	}
}
