package com.mangocity.hotel.order.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.persistence.AuditHotel;
import com.mangocity.hotel.order.persistence.AuditItem;
import com.mangocity.hotel.order.persistence.DaDailyaudit;
import com.mangocity.hotel.order.persistence.HtlAuditInfoSetup;
import com.mangocity.hotel.order.persistence.VOrOrder;
import com.mangocity.hotel.order.persistence.VOrOrderItem;
import com.mangocity.util.dao.GenericDAO;

/**
 */
public interface IAuditDao  extends GenericDAO{

    /**
     * 根据audit的主键列表查询auditItem
     * 
     * @param auditIds
     * @return
     */
	public List<AuditItem> queryAuditItemByAuditIds(final Long[] auditIds);
	
	/**
	 * 根据名称查询
	 */
	public <T> List<T> queryByNamedQuery(String queryID, Object[] paramValues);
    /**
     * 根据日期,酒店id获取HotelAudit对象
     * 
     * @param date
     * @param hotelId
     * @return
     */
    public AuditHotel getHotelByDateAndHotel(Date date, Long hotelId);
    /**
     * 获取AuditHotel根据id列表
     * @param objs
     * @return
     */
    public List<AuditHotel> queryAuditHotelsByIds(Object[] objs);
    
    /**
     * 根据审核日期和渠道获取审核的批次ID
     * @param auditDate
     * @param channelId
     * @return
     */
	public long getBathIdByAutoAudit(String auditDate, Long channelId);
	
	/**
	 * 根据审核批次ID获取审核记录
	 * @param bathId
	 * @return
	 */
	public List<Object[]> getAuditRecordsByTempAuditinfo(long bathId);
	
	/**
	 * 根据ID获取审核信息
	 * @param id
	 * @return
	 */
	public List<HtlAuditInfoSetup> getAuditInfoSetup(Long id);
	
	/**
	 * 获取退房日审传真信息
	 * 
	 * @param dailyAudit
	 * @return
	 */
	public List<Object[]> getItemsForAuditFaxCheckout(DaDailyaudit dailyAudit);
	
	/**
	 * 获取入住日审传真信息
	 * 
	 * @param dailyAudit
	 * @return
	 */
	public List<Object[]> getItemsForAuditFaxCheckin(DaDailyaudit dailyAudit);
	
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
			String notesMan);
	
	/**
	 * 更新订单视图的orderitem
	 * @param obj
	 */
	public  void updateVOrOrderItem(Object[] obj);
	
	/**
	 *：住２天，而且只生成退房审核时，要更新第一天的入住情况 时 更新订单视图的orderitem
	 * @param obj
	 */
	public  void updateVOrOrderItemTwoDays(Object[] obj);
	
	/**
	 * 住２天，而且只生成退房审核时，要更新每一天的入住情况 时 更新订单视图的orderitem
	 * @param obj
	 */
	public void  updateVorderItemShow(Object[] obj);
	
	/**
     * 查询是否有给定hotelId的酒店
     * 
     * @param hotelId
     * @return true:有 false:没有
     */
    public boolean checkHotel(long hotelId);
	

}
