package com.mangocity.hotel.order.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.order.persistence.Audit;
import com.mangocity.hotel.order.persistence.AuditHotel;
import com.mangocity.hotel.order.persistence.OrAuditFaxLog;
import com.mangocity.hotel.order.persistence.OrDailyAudit;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrOrderItemTemp;
import com.mangocity.hotel.order.persistence.OrPaperContact;
import com.mangocity.hotel.order.persistence.OrPaperDailyAudit;
import com.mangocity.hotel.order.persistence.OrPaperDailyAuditItem;
import com.mangocity.hotel.order.persistence.VOrOrder;
import com.mangocity.hotel.order.persistence.VOrOrderItem;
import com.mangocity.hotel.user.UserWrapper;

/**
 */
public interface IAuditService extends Serializable {

    /**
     * 根据AuditHotel主键获取AuditHotel
     * 
     * @param hotelId
     * @return
     */
    public AuditHotel getAuditHotel(long hotelId);

    /**
     * 根据Audit主键获取Audit
     * 
     * @param auditId
     * @return
     */
    public Audit getAudit(long auditId);

    /**
     * 根据hotelid,date主键获取orderList
     * 
     * @param auditId
     * @return
     */

    public List getViewOrder(Long hotelId, Date checkNight);

    public List getOrPaperDailyAuditItem(Long auditId);

    /**
     * 根据参数获取orderItemList
     * 
     * @param hotelId
     * @param data
     *            *
     * @return
     */
    public List getViewOrderItem(Long hotelId, Date data);

    /**
     * 根据参数获取orderItemList
     * 
     * @param id
     * @param auditType
     *            *
     *      */
    public List getViewOrderItems(Long selID, int typeID);

    /**
     * 根据参数获取orderItemList
     * 
     * @param orderid
     * @param auditType
     *            *
     * 
     */
    public List getViewOrderItemTypes(Long selID, int typeID);

    /**
     * 根据参数获取orderItemList
     * 
     * @param id
     * @param orderType
     *            *
     * 
     * 
     */

    public List getViewOrders(Long orderID, int orderType);

    /**
     * 根据参数获取orderItemList
     * 
     * @param hotelId
     * @param data
     *            *
     * @return
     */
    public List getOrderItemNum(Long hotelId, Date data, int roomIndex);

    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param orderid
     * @return
     */
    public List getVOrderItem(Long orderId, Integer auditType2);

    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param orderid
     * @param date
     *            *
     * @return
     */
    public List getVOrderItem1(Long orderId, Date data);

    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param auditId
     * @return
     */
    public List getViewOrderItem(Long hotelId, Date data_1, Date date);

    /**
     * 批量修改AuditItem的值<br>
     * 注: 目前应用的情况, 这些auditItems假定都属于同一个audit的
     * 
     * @param ids
     *            id
     * @param roomNos
     *            房间号
     * @param fellowNames
     *            客人姓名
     * @param states
     *            入住状态
     */
    public void batchSetAuditItem(Long[] ids, String[] roomNos, String[] fellowNames,
        String[] states);

    /**
     * 批量获取指定hoteldIds(主键)的AuditHotel对象列表
     * 
     * @param hotelIds
     * @return
     */
    public List batchGetAuditHotel(Long[] hotelIds);

    /**
     * 发送日审传真
     * 
     */
    public boolean sendAuditFax(OrPaperDailyAudit audits, OrDailyAudit orDailyAudit);

    /**
     * 发送日审传真
     * 
     */
    public void saveOrderItem(OrOrderItem audits);

    /**
     * 日申操作完成
     * 
     */
    public boolean saveOrderItemAndOrOrder(VOrOrderItem audits, VOrOrder order);

    /**
     * 发送日审传真明细
     * 
     */
    public void saveOrPaperDailyAuditItem(OrPaperDailyAuditItem audits) ;

    /**
     * 处理日审订单
     * 
     */
    public boolean sendAuditViewOrder(VOrOrder order);

    /**
     * 处理日审明细
     * 
     */
    public boolean sendAuditDetail(VOrOrderItem audit);

    /**
     * 处理日审明细
     * 
     */
    public boolean sendViewAuditDetail(VOrOrderItem audit);

    /**
     * 根据ORDERID获取ORDER
     * 
     */
    // public OrOrder getOrder(Long orderID){
    // OrOrder order = (OrOrder)orOrderDao.loadOrder(new Long(orderID));
    // return order;
    // }
    /**
     * 根据ORDERID获取vORDER
     * 
     */
    public VOrOrder getVOrder(Long orderID, int orderType);

    /**
     * 根据ORDERID获取vORDER，目前只有补登日审用到这个方法, 而补登日审目前不支持tmc订单，所以默认取酒店订单
     * 
     */
    public VOrOrder getVOrder(Long orderID);

    /**
     * 根据ID获取ORDERITEM
     * 
     */
    public VOrOrderItem getViewOrderItem(Long ID);

    /**
     * 记录日审酒店
     * 
     */
    public boolean saveOrDailyAudit(OrDailyAudit hotels);

    /**
     * 记录待审查信息
     * 
     */
    public void saveOrDailyAuditAndOrderItem(VOrOrderItem orOrderItem, OrDailyAudit orDailyAudit);

    /**
     * 记录NOSHOW信息
     * 
     */
    public void saveNoShow(VOrOrder order, VOrOrderItem orOrderItem);

    /**
     * 记录延驻明细
     * 
     */
    public void saveOrOrderItemTemp(OrOrderItemTemp orOrderItem);

    /**
     * 记录日审完成信息
     * 
     */
    public void saveFulfill(VOrOrderItem orOrderItem, VOrOrderItem orOrderShow, VOrOrder order);

    /**
     * 记录日审保存信息
     * 
     */
    public void saveAuditInfo(VOrOrderItem orOrderItem, VOrOrderItem orOrderShow);

    /**
     * 查询日审记录
     * 
     * @return
     */
    public List queryAuditRecords() ;

    public boolean saveOrUpdate(OrPaperContact audits);


    /**
     * 移除日审酒店
     * 
     * @param hotelId
     * @return
     */
    public boolean batchDelHotel(Long[] hotelId);

    /**
     * 移除酒店
     * 
     * @param hotelId
     *            表里的主键id
     * @return 是否分配成功
     */
    public boolean delHotel(long hotelId);

    /**
     * 根据AuditHotel的主键ID查询Audit记录
     * 
     * @param auditHotelId
     * @return
     */
    public List getAuditByHotel(long auditHotelId);

    /**
     * 根据audit查询AuditItem记录
     * 
     * @param audit
     * @return
     */
    public List getAuditItemByAudit(Audit audit);

    public boolean editFax(long ID, String fax, String faxConfirm);

    /**
     * 分配某一天的日审酒店给user
     * 
     * @param hotelId
     *            表里的主键id
     * @param user
     * @return 是否分配成功
     */
    public boolean arrangeAuditHotel(long hotelId, OrWorkStates user);

    /**
     * 批处理分配某一天的日审酒店给user
     * 
     * @param hotelId
     *            OrDailyAudit类的主键，不是字面意思上的酒店ID
     * @param user
     * @return
     */
    public boolean batchArrangeAuditHotel(Long[] hotelId, OrWorkStates user);

    public OrDailyAudit getOrDailyAudit(Serializable auditID);

    public Session getSession();

    public List getOrPaperContact(Long hotelID);

    /**
     * 根据OrDailyAudit主键获取OrDailyAudit
     * 
     * @param barCode
     * @return
     */
    public OrDailyAudit getOrDailyAuditId(String barCode);

    /**
     * 取出未被分配的酒店订单
     * 
     * @param date
     * @param max
     */
    public List getOrDailyAuditIsNull(Date date);

    public OrPaperDailyAudit getOrPaperDailyAudit(Serializable auditID);

    public List findSimilarAudit(OrDailyAudit orDailyAudit);

    public List findSimilarOrder1(Date date);

    /**
     * 根据参数获得orDailyAudit对象
     * 
     * @param hotelId
     * @param date
     * @return
     */
    public List findSimilarDailyAudit(Object hotelId, Date date);

    /**
     * 根据参数获得orOrderItrm对象
     * 
     * @param hotelId
     * @param date
     * @return
     */
    public List findReturnDailyAudit(Object hotelId, Date date);

    /**
     * 根据参数获得order对象
     * 
     * @param hotelId
     * @param date
     * @return
     */

    public List findSimilarOrderNumbers(Object hotelId, Date date);

    /**
     * 根据参数获得OrDailyAudit对象
     * 
     * @param date
     * @return
     */

    public List getNewOrDailyAudit(Date date);

    /**
     * 根据参数获得orOrderItem对象
     * 
     * @param orderId
     * @param date
     * @return
     */

    public List findSimilarOrOrderItem(Long orderId, Date date);

    /**
     * 根据参数获得orOrderItem对象
     * 
     * @param orderId
     * @param date
     * @return
     */

    public List findSimilarOrOrderItemTemp(Long orderId, Date date);

    public List findSimilarOrOrderShow(Long orderId, Date date) ;

    public List findSimilarViewOrOrderShow(Long orderId, Date date);

    /**
     * 修改未入住情况下的订单明细显示状态
     * 
     * @param orderId
     * @param date
     * @param roomIndex
     * @return
     */
    public List findSimilarViewOrOrderShow(Long orderId, Date date, int roomIndex);

    public List findSimilarOrder(Date date);

    /**
     * 取出操作人员现处理数量
     * 
     * @param date
     * @param max
     */
    public List getUserWorkNum(Date date);

    /**
     * 自动分配日审酒店 date
     * 
     * @return
     */

    public boolean automatismAllotManager(Date checkNight, List userList);

    /**
     * 获取日审传真信息
     * 
     */
    public List getOrderItemsForAuditFax(Long auditId);

    /**
     * 根据参数获取日审传真list
     * 
     * @param
     * @param auditDate
     *            ,hotelName,hotelstate,hotelcity,hotelzone,sendFax,isSend
     * @return
     */
    public List gethotels(Map params);

    /**
     * 日审传真获取酒店信息
     * 
     */
    public Object[] getHotelInfoForAuditFax(Long auditId);

    /**
     * 插入自动日审日志
     * 
     */
    public boolean saveOrAuditFaxLog(OrAuditFaxLog orAuditFaxLog);

    /**
     * 暂存 - 有入住的情况
     * 
     * @param index
     * @param orderID
     * @param night
     * @param rState
     * @param rStateNot
     * @param orderState
     * @param rID
     * @param specialNote
     * @param roomNumber
     * @param orderAuditType
     * @param fellowNames
     * @param roleUser
     * @return
     */
    public int auditWork(int index, String orderID, String night, String rState, String rStateNot,
        int orderState, String rID, String specialNote, String roomNumber, int orderAuditType,
        String fellowNames, UserWrapper roleUser) ;

    /**
     * 处理日审单 -- noshow或提前退房
     * 
     * @return
     */
    public int auditWorkNoShow(int index, String orderID, String night, int orderState,
        String noshowCode, String noshowReason, String specialNote, int orderAuditType,
        UserWrapper roleUser) ;

    /**
     * 处理日审单 -- 修改为入住情况下的显示状态
     * 
     * @return
     */
    public VOrOrderItem editOrderNotShow(String orderID, Date date, boolean show, int roomIndex);

    /**
     * 处理日审单 -- 修改操作状态
     * 
     * @return
     */
    public VOrOrderItem editWorkState(String orderID, Date date, int state, String userName);

    /**
     * noshow情况下的状态处理
     * 
     */
    public VOrOrderItem noshowDispose(String orderID, Integer orderType);

    /**
     * 提前退房情况下的状态处理
     * 
     */
    public VOrOrderItem fadeRoomDispose(String orderID, String night);

    /**
     * 处理日审单 -- 修改操作状态
     * 
     * @return
     */
    public VOrOrderItem editWorkState(String orderID, Date date, int roomIndex, int state,
        UserWrapper roleUser);

    /**
     * 处理日审单 -- 待审核
     * 
     * @return
     */
    public int auditNotAuditing(int index, String orderID, String night, long hotelID,
        UserWrapper roleUser);

    /**
     * 确定 - Noshow或提前退房
     * 
     * @param index
     * @param orderID
     * @param night
     * @param orderState
     * @param noshowCode
     * @param noshowReason
     * @param specialNote
     * @param hotelID
     * @param orderAuditType
     * @param roleUser
     * @return
     */
    public int auditWorkNoShowFulfill(int index, String orderID, String night, int orderState,
        String noshowCode, String noshowReason, String specialNote, long hotelID,
        int orderAuditType, UserWrapper roleUser);

    /**
     * 确定 - 全部入住的情况
     * 
     * @param index
     * @param orderID
     * @param night
     * @param rState
     * @param rStateNot
     * @param orderState
     * @param rID
     * @param specialNote
     * @param roomNumber
     * @param hotelID
     * @param orderAuditType
     * @param fellowNames
     * @param roleUser
     * @return
     */
    public int auditWorkFulfill(int index, String orderID, String night, String rState,
        String rStateNot, int orderState, String rID, String specialNote, String roomNumber,
        long hotelID, int orderAuditType, String fellowNames, UserWrapper roleUser);

    /**
     * 确定 - 部分入住的情况
     * 
     * @param index
     * @param orderID
     * @param night
     * @param rState
     * @param rStateNot
     * @param orderState
     * @param rID
     * @param specialNote
     * @param roomNumber
     * @param hotelID
     * @param orderAuditType
     * @param fellowNames
     * @param roleUser
     * @return
     */
    public int auditWorkNotFulfill(int index, String orderID, String night, String rState,
        String rStateNot, int orderState, String rID, String specialNote, String roomNumber,
        long hotelID, int orderAuditType, String fellowNames, UserWrapper roleUser);
    /**
     * 审核完成 - Noshow或提前退房
     * 
     * @param index
     * @param orderID
     * @param night
     * @param orderState
     * @param noshowCode
     * @param noshowReason
     * @param specialNote
     * @param hotelID
     * @param orderAuditType
     * @param roleUser
     * @return
     */
    public int auditNoShowFulfill(int index, String orderID, String night, int orderState,
        String noshowCode, String noshowReason, String specialNote, long hotelID,
        int orderAuditType, UserWrapper roleUser) ;

    /**
     * 处理日审单 -- 完成
     * 
     * @return
     */
    public int auditFulfill(int index, String orderID, String night, String noshowCode,
        String noshowReason, String specialNote, long hotelID, int orderAuditType,
        UserWrapper roleUser);

    /**
     * 处理日审单 -- 修改操作状态
     * 
     * @return
     */
    public VOrOrderItem editOrderShow(String orderID, Date date, boolean show);

    /**
     * 根据参数获取OrAuditFaxLog
     * 
     * @param data
     *            *
     * @return
     */
    public List getAuditLogList(Date data) ;
    
}