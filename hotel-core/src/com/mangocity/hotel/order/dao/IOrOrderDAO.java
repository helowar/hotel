package com.mangocity.hotel.order.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.CommisionAdjust;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.order.constant.MidOptPrcParameter;
import com.mangocity.hotel.order.persistence.Myworkingrate;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderOfSMS;
import com.mangocity.hotel.order.persistence.Order;
import com.mangocity.hotel.order.persistence.Orderstations;
import com.mangocity.hotel.order.persistence.VOrOrder;
import com.mangocity.util.dao.GenericDAO;

/**
 */
public interface IOrOrderDAO extends GenericDAO {

	/**
     * 根据orderCD获取订单对象
     * 
     * @param orderCD
     * @return
     */
    public OrOrder getCustomOrderByCD(String orderCD);

    /**
     * 根据ID获取OrOrder对象
     * 
     * @param orderID
     * @return
     */
    public OrOrder getCustomOrderByID(Long orderID);
    /**
     * 加入日志到DB
     * @param sql
     */
    public void insertlog(String sql);

   
    /**
     * @param contractId
     * @return 根据酒店ID,起始日期获取税费设定信息
     */
    public List getTaxCharges(Long contractId, Date beginDate, Date endDate);
    
	/**
	 *根据checkindate获取订单列表 
	 * @param checkinDate
	 * @return
	 */
    public List<Order> getOrderList(Date checkinDate);
    /**
     * 根据日期查询当天适合条件的订单
     * 
     * @param triggerdate
     * @return
     */
    public List<OrOrderOfSMS> getOrderforTrigger(String triggerdate);


    /**
     *根据HotelId获取合作方
     * 
     * @param hotelId
     * @return
     */
    public List<String> getChannelByHotelId(String hotelId);
    

    
    /**
     * 需要移到pricedao
     * @param abselDate
     * @param hotelId
     * @param roomTypeID
     * @param priceTypeId
     * @param payMethod
     * @return
     */
    
	public HtlPrice getPricInfoByFor(Date abselDate, long hotelId,long roomTypeID, long priceTypeId,String payMethod);
	/**
	 * 需要移到pricedao
	 * @param contractId
	 * @param currDate
	 * @return
	 */
	public HtlTaxCharge getCommTaxInfo(long contractId,Date currDate);
    
	/**
     * 获取工作效率及订单实时监控数据
     * @param type 获取数据类型，1：工作效率，2：订单实时监控
     * @return
     */
    public void getWorkingRateAndOrderStations()throws SQLException;
    /**
     * 调用中台优化的存储过程或函数
     * @param execType 执行类型，1：存储过程，2：函数
     * @param mp 参数
     */
    public void execMidOptPrcOrFnc(int execType,MidOptPrcParameter mp)throws SQLException;
    /**
     * proc调用
     * @param loginName
     * @return
     * @throws SQLException
     */
    public int autoAllotOrderBy(String loginName)throws SQLException;
    
    /**
     * 查询工作量
     * @param loginName
     * @return
     */
    public List<Myworkingrate> getMyWorkingRateByLoginName(String loginName);
	
	/**
	 * 得到工作量和工作状态
	 * @param params
	 * @return
	 */
	public List<Orderstations> getWorkingRateAndOrderStationsByAjax(Map<String, String> params);
    /**
    *
    * 根据hotelid,date主键获取orderList
    *
    * @param hotelId
    * @param checkNight
    * @return
    */
   public List<VOrOrder> getViewOrder(Long hotelId, Date checkNight);
   
   
   /**
    * 查询日审订单
    * @param date
    * @return
    */
   public List<OrOrder> findSimilarOrder(Date date);
   
   
   /**
    * 获取日审传真信息
    * 
    */
   public List getOrderItemsForAuditFax(Long auditId);
   
   public void updateB2bModifyOrderWithOrderState(long orderId, int orderState);
   
   
   /**
	 * 根据代理商code得到享受政策范围
	 * add by yong.zeng
	 * @param agentcode
	 * @return
	 */
	public int getPolicyScope(String agentcode);
	
	public List<CommisionAdjust> getCommisionAdjustByHotelIds(String hotelIds, String agentId, Date startDate, Date endDate);
   
	/**
	 * 根据多个订单号查询订单 
	 * @param orderIds 订单ID字符串
	 * @return
	 */
    List<OrOrder> getOrOrderList(String orderIds);
    /**
	 * 查询连住包价
	 * @param hotelId
	 * @param priceTypeId
	 * @param currDate
	 * @return
	 */
	public List getFavourable(String hotelIds,Date begin,Date end);
	
	/**
	 * 据ID查询订单短信
	 * @param orderID
	 * @return
	 */
	 public OrOrderOfSMS getOrOrderOfSMS(Serializable id);
}
