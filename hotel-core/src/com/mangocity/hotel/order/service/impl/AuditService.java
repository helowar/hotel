package com.mangocity.hotel.order.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.mangocity.hotel.base.dao.OrWorkStatesDao;
import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.order.constant.AuditItemState;
import com.mangocity.hotel.order.constant.OrdailyauditStatus;
import com.mangocity.hotel.order.constant.OrderItemAuditState;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.dao.IOrderDao;
import com.mangocity.hotel.order.dao.impl.AuditDao;
import com.mangocity.hotel.order.dao.impl.OrAuditFaxLogDao;
import com.mangocity.hotel.order.dao.impl.OrDailyAuditDao;
import com.mangocity.hotel.order.dao.impl.OrFaxLogDao;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.dao.impl.OrOrderItemDao;
import com.mangocity.hotel.order.dao.impl.OrOrderItemTempDao;
import com.mangocity.hotel.order.dao.impl.OrPaperContactDao;
import com.mangocity.hotel.order.dao.impl.OrPaperDailyAuditDao;
import com.mangocity.hotel.order.dao.impl.OrPaperDailyAuditItemDao;
import com.mangocity.hotel.order.dao.impl.ViewOrOrderDao;
import com.mangocity.hotel.order.dao.impl.ViewOrOrderItemDao;
import com.mangocity.hotel.order.persistence.Audit;
import com.mangocity.hotel.order.persistence.AuditHotel;
import com.mangocity.hotel.order.persistence.AuditItem;
import com.mangocity.hotel.order.persistence.HotelWordUser;
import com.mangocity.hotel.order.persistence.OrAuditFaxLog;
import com.mangocity.hotel.order.persistence.OrDailyAudit;
import com.mangocity.hotel.order.persistence.OrFaxLog;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrOrderItemTemp;
import com.mangocity.hotel.order.persistence.OrPaperContact;
import com.mangocity.hotel.order.persistence.OrPaperDailyAudit;
import com.mangocity.hotel.order.persistence.OrPaperDailyAuditItem;
import com.mangocity.hotel.order.persistence.Order;
import com.mangocity.hotel.order.persistence.VOrOrder;
import com.mangocity.hotel.order.persistence.VOrOrderItem;
import com.mangocity.hotel.order.service.IAuditService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;

/**
 * 日审服务
 */
public class AuditService implements IAuditService{
	private static final MyLog log = MyLog.getLogger(AuditService.class);
    private IOrderDao orderDao;

    private AuditDao auditDao;

    private OrOrderItemDao orOrderItemDao;

    private OrOrderItemTempDao orOrderItemTempDao;

    private ViewOrOrderItemDao viewOrOrderItemDao;

    private OrPaperContactDao orPaperContactDao;

    private OrPaperDailyAuditDao orPaperDailyAuditDao;

    private OrPaperDailyAuditItemDao orPaperDailyAuditItemDao;

    private OrOrderDao orOrderDao;

    private ViewOrOrderDao viewOrOrderDao;

    private OrDailyAuditDao orDailyAuditDao;

    private OrDailyAudit orDailyAudit;

    private OrOrderItemTemp orOrderItemTemp;

    private OrFaxLogDao orFaxLogDao;

    private OrAuditFaxLogDao orAuditFaxLogDao;

    private OrWorkStatesDao orWorkStatesDao;

    /**
     * 根据AuditHotel主键获取AuditHotel
     * 
     * @param hotelId
     * @return
     */
    public AuditHotel getAuditHotel(long hotelId) {
        AuditHotel hotel = (AuditHotel) auditDao.load(AuditHotel.class, Long.valueOf(hotelId));
        return hotel;
    }

    /**
     * 根据Audit主键获取Audit
     * 
     * @param auditId
     * @return
     */
    public Audit getAudit(long auditId) {
        Audit audit = (Audit) auditDao.getAudit(auditId);
        return audit;
    }

    /**
     * 根据hotelid,date主键获取orderList
     * 
     * @param auditId
     * @return
     */

    public List getViewOrder(Long hotelId, Date checkNight) {
 
        String hsql = "from VOrOrder a where a.hotelId = ?" +
                " and a.payMethod = 'pay' and a.orderState != 14"
            + "and a.ID in (select b.orderID from VOrOrderItem" +
                    " b where b.hotelId = ? "
            + "and b.night = ? and b.show != 1 and  a.orderType = b.auditType ) "
            + "and a.orderType in (select b.auditType from VOrOrderItem b where b.hotelId = ? "
            + "and b.night = ? and b.show != 1 and  a.orderType = b.auditType ) " + "order by a.ID";

        Object[] obj = new Object[] { hotelId, hotelId, checkNight, hotelId, checkNight };

        return orOrderDao.query(hsql, obj);
    }

    public List getOrPaperDailyAuditItem(Long auditId) {
        List res = orPaperDailyAuditItemDao.queryByNamedQuery("hQueryPaperDailyAuditItem",
            new Object[] { auditId });
        // TODO: 查询酒店ID

        return res;
    }

    /**
     * 根据参数获取orderItemListOrOrder
     * 
     * @param hotelId
     * @param data
     *            *
     * @return
     */
    // public List getOrderItem(Long hotelId,Date data) {
    // List res = orOrderItemDao.queryByNamedQuery("hQueryOrder_item1",
    // new Object[] {hotelId,data});
    //		
    // // TODO: 查询酒店ID
    //				
    // return res;
    // }
    /**
     * 根据参数获取orderItemList
     * 
     * @param hotelId
     * @param data
     *            *
     * @return
     */
    public List getViewOrderItem(Long hotelId, Date data) {
        List res = viewOrOrderItemDao.queryByNamedQuery("hQueryOrder_item", new Object[] { hotelId,
            data });

        // TODO: 查询酒店ID

        return res;
    }

    /**
     * 根据参数获取orderItemList
     * 
     * @param id
     * @param auditType
     *            *
     *      */
    public List getViewOrderItems(Long selID, int typeID) {
        List res = viewOrOrderItemDao.queryByNamedQuery("hQueryOrder_items", new Object[] { selID,
            typeID });

        // TODO: 查询酒店ID

        return res;
    }

    /**
     * 根据参数获取orderItemList
     * 
     * @param orderid
     * @param auditType
     *            *
     * 
     */
    public List getViewOrderItemTypes(Long selID, int typeID) {
        List res = viewOrOrderItemDao.queryByNamedQuery("hQueryOrder_itemType", new Object[] {
            selID, typeID });

        // TODO: 查询酒店ID

        return res;
    }

    /**
     * 根据参数获取orderItemList
     * 
     * @param id
     * @param orderType
     *            *
     * 
     * 
     */

    public List getViewOrders(Long orderID, int orderType) {
        List res = viewOrOrderDao.queryByNamedQuery("hQueryOrder_audit", new Object[] { orderID,
            orderType });

        // TODO: 查询酒店ID

        return res;
    }

    /**
     * 根据参数获取orderItemList
     * 
     * @param hotelId
     * @param data
     *            *
     * @return
     */
    public List getOrderItemNum(Long hotelId, Date data, int roomIndex) {
        List res = viewOrOrderItemDao.queryByNamedQuery("hQueryOrder_ItemNum", new Object[] {
            hotelId, data, roomIndex });

        // TODO: 查询酒店ID

        return res;
    }

    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param orderid
     * @return
     */
    // public List getOrderItem1(Long orderId){
    // List res = orOrderItemDao.queryByNamedQuery("hQueryOrder_itemAudit1",
    // new Object[] {orderId});
    //		
    // // TODO: 查询酒店ID
    //				
    // return res;
    // }
    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param orderid
     * @return
     */
    public List getVOrderItem(Long orderId, Integer auditType2) {
        List res = viewOrOrderItemDao.queryByNamedQuery("hQueryOrder_itemAudit", new Object[] {
            orderId, auditType2 });

        // TODO: 查询酒店ID

        return res;
    }

    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param orderid
     * @param date
     *            *
     * @return
     */
    public List getVOrderItem1(Long orderId, Date data) {
        List res = viewOrOrderItemDao.queryByNamedQuery("hQueryOrder_itemDate", new Object[] {
            orderId, data });

        // TODO: 查询酒店ID

        return res;
    }

    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param auditId
     * @return
     */
    // public List getOrderItem(Long hotelId,Date data_1,Date date) {
    // List res = orOrderItemDao.queryByNamedQuery("hQueryOrder_itemRoom1",
    // new Object[] {hotelId,data_1,date});
    //		
    // // TODO: 查询酒店ID
    //				
    // return res;
    // }
    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param auditId
     * @return
     */
    public List getViewOrderItem(Long hotelId, Date data_1, Date date) {
        List res = viewOrOrderItemDao.queryByNamedQuery("hQueryOrder_itemRoom", new Object[] {
            hotelId, data_1, date });

        // TODO: 查询酒店ID

        return res;
    }

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
        String[] states) {
        AuditItem auditItem = null;
        boolean checkin = true, partCheckin = false;
        for (int i = 0; i < ids.length; i++) {
            auditItem = auditDao.load(AuditItem.class, ids[i]);
            auditItem.setRoomNo(roomNos[i]);
            auditItem.setFellowName(fellowNames[i]);
            auditItem.setNormalCheck(states[i]);
            if (states[i].equals(AuditItemState.CHECKIN)) {
                partCheckin = true;
            } else {
                checkin = false;
            }
        }

        // 修改audit的状态
        Audit audit = auditItem.getAudit();
        if (checkin) { // 全部入住
            audit.setAuditState(AuditItemState.CHECKIN);
        } else if (partCheckin) { // 部分入住
            audit.setAuditState(AuditItemState.PART_CHECKIN);
        } else { // noshow
            audit.setAuditState(AuditItemState.NOSHOW);
        }
        auditDao.saveOrUpdate(audit);

        // TODO: 如果noshow,则进行剩下的业务操作，等等..
    }

    /**
     * 批量获取指定hoteldIds(主键)的AuditHotel对象列表
     * 
     * @param hotelIds
     * @return
     */
    public List batchGetAuditHotel(Long[] hotelIds) {
        List resList = new ArrayList();
        for (int i = 0; i < hotelIds.length; i++) {
            AuditHotel hotel = auditDao.load(AuditHotel.class, Long.valueOf(hotelIds[i]));
            resList.add(hotel);
        }
        return resList;
    }

    /**
     * 发送日审传真
     * 
     */
    public boolean sendAuditFax(OrPaperDailyAudit audits, OrDailyAudit orDailyAudit) {

        try {
            orPaperDailyAuditDao.saveOrUpdate(audits);
            orDailyAuditDao.saveOrUpdate(orDailyAudit);
            return true;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * 发送日审传真
     * 
     */
    public void saveOrderItem(OrOrderItem audits) {
        orOrderItemDao.saveOrUpdate(audits);
    }

    /**
     * 日申操作完成
     * 
     */
    public boolean saveOrderItemAndOrOrder(VOrOrderItem audits, VOrOrder order) {
        try {
            viewOrOrderItemDao.saveOrUpdate(audits);
            viewOrOrderDao.saveOrUpdate(order);
            return true;
        } catch (Exception e) {
        	log.error(e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * 发送日审传真明细
     * 
     */
    public void saveOrPaperDailyAuditItem(OrPaperDailyAuditItem audits) {
        orPaperDailyAuditItemDao.saveOrUpdate(audits);
    }

    /**
     * 处理日审订单
     * 
     */
    // public boolean sendAuditOrder(OrOrder order){
    // orOrderDao.saveOrUpdate(order);
    // return true;
    // }
    /**
     * 处理日审订单
     * 
     */
    public boolean sendAuditViewOrder(VOrOrder order) {
        viewOrOrderDao.saveOrUpdate(order);
        return true;
    }

    /**
     * 处理日审明细
     * 
     */
    public boolean sendAuditDetail(VOrOrderItem audit) {
        viewOrOrderItemDao.saveOrUpdate(audit);
        return true;
    }

    /**
     * 处理日审明细
     * 
     */
    public boolean sendViewAuditDetail(VOrOrderItem audit) {
        viewOrOrderItemDao.saveOrUpdate(audit);
        return true;
    }

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
    public VOrOrder getVOrder(Long orderID, int orderType) {
        VOrOrder order = viewOrOrderDao.loadOrder(Long.valueOf(orderID));
        return order;
    }

    /**
     * 根据ORDERID获取vORDER，目前只有补登日审用到这个方法, 而补登日审目前不支持tmc订单，所以默认取酒店订单
     * 
     */
    public VOrOrder getVOrder(Long orderID) {
        String hql = "from VOrOrder where ID=? and orderType=1";
        Object[] params = new Object[1];
        params[0] = orderID;
        List<VOrOrder> orderlist =  viewOrOrderDao.find(hql, params);
        VOrOrder order = new VOrOrder();
        if(orderlist.size()>0){
        	 order = orderlist.get(0);
        }
        return order;
    }

    /**
     * 根据ID获取ORDERITEM
     * 
     */
    // public OrOrderItem getOrderItem(Long ID){
    // OrOrderItem orOrderItem = (OrOrderItem)orOrderItemDao.loadObject(new Long(ID));
    // return orOrderItem;
    // }
    //	
    /**
     * 根据ID获取ORDERITEM
     * 
     */
    public VOrOrderItem getViewOrderItem(Long ID) {
        VOrOrderItem orOrderItem = (VOrOrderItem) viewOrOrderItemDao.loadObject(Long.valueOf(ID));
        return orOrderItem;
    }

    /**
     * 记录日审酒店
     * 
     */
    public boolean saveOrDailyAudit(OrDailyAudit hotels) {
        try {
            orDailyAuditDao.saveOrUpdate(hotels);
            return true;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * 记录待审查信息
     * 
     */
    public void saveOrDailyAuditAndOrderItem(VOrOrderItem orOrderItem, OrDailyAudit orDailyAudit) {

        try {
            viewOrOrderItemDao.saveOrUpdate(orOrderItem);
            orDailyAuditDao.saveOrUpdate(orDailyAudit);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    /**
     * 记录NOSHOW信息
     * 
     */
    public void saveNoShow(VOrOrder order, VOrOrderItem orOrderItem) {

        try {
            viewOrOrderDao.saveOrUpdate(order);
            viewOrOrderItemDao.saveOrUpdate(orOrderItem);
        } catch (Exception e) {
        	log.error(e.getLocalizedMessage());
        }
    }

    /**
     * 记录延驻明细
     * 
     */
    public void saveOrOrderItemTemp(OrOrderItemTemp orOrderItem) {

        try {
            orOrderItemTempDao.saveOrUpdate(orOrderItem);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    /**
     * 记录日审完成信息
     * 
     */
    public void saveFulfill(VOrOrderItem orOrderItem, VOrOrderItem orOrderShow, VOrOrder order) {

        try {
            viewOrOrderItemDao.saveOrUpdate(orOrderItem);
            viewOrOrderItemDao.saveOrUpdate(orOrderShow);
            viewOrOrderDao.saveOrUpdate(order);
        } catch (Exception e) {
        	log.error(e.getLocalizedMessage());
        }
    }

    /**
     * 记录日审保存信息
     * 
     */
    public void saveAuditInfo(VOrOrderItem orOrderItem, VOrOrderItem orOrderShow) {

        try {
            viewOrOrderItemDao.saveOrUpdate(orOrderItem);
            if (null != orOrderShow) {
                viewOrOrderItemDao.saveOrUpdate(orOrderShow);
            }
        } catch (Exception e) {
        	log.error(e.getLocalizedMessage());
        }
    }

    /**
     * 查询日审记录
     * 
     * @return
     */
    public List queryAuditRecords() {
        return null;
    }

    public boolean saveOrUpdate(OrPaperContact audits) {

        try {
            orPaperContactDao.saveOrUpdate(audits);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // public boolean deleteAuditFax(OrPaperContact audits) {
    // try {
    // orPaperContactDao.remove(audits);
    // return true;
    // }
    // catch (Exception e) {
    // return false;
    // }
    // }

    /**
     * 移除日审酒店
     * 
     * @param hotelId
     * @return
     */
    public boolean batchDelHotel(Long[] hotelId) {
        for (int i = 0; i < hotelId.length; i++) {
            if (false == delHotel(hotelId[i].longValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 移除酒店
     * 
     * @param hotelId
     *            表里的主键id
     * @return 是否分配成功
     */
    public boolean delHotel(long hotelId) {
        try {
            OrPaperContact hotel = (OrPaperContact) 
            orPaperContactDao.loadObject(Long.valueOf(hotelId));
            orPaperContactDao.remove(hotel);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据AuditHotel的主键ID查询Audit记录
     * 
     * @param auditHotelId
     * @return
     */
    public List getAuditByHotel(long auditHotelId) {
        List auditList = auditDao.queryByNamedQuery("hQueryAuditByHotel_Audit",
            new Object[] { Long.valueOf(auditHotelId) });

        return auditList;
    }

    /**
     * 根据audit查询AuditItem记录
     * 
     * @param audit
     * @return
     */
    public List getAuditItemByAudit(Audit audit) {
        List auditItems = new ArrayList();
        Date checkinDate = audit.getCheckinDate();
        Order order = audit.getOrder();
        List auditList = auditDao.queryByNamedQuery("hQueryAuditByOrder_Audit",
            new Object[] { order.getID() });
        if (0 < auditList.size()) {
            List auditIds = new ArrayList();
            for (int i = 0; i < auditList.size(); i++) {
                audit = (Audit) auditList.get(i);
                if (0 < DateUtil.compare(checkinDate,audit.getCheckinDate())) {
                    break;
                }
                auditIds.add(audit.getID());
            }
            Long[] lAudits = new Long[auditIds.size()];
            for (int i = 0; i < auditIds.size(); i++) {
                lAudits[i] = (Long) auditIds.get(i);
            }
            auditItems = auditDao.queryAuditItemByAuditIds(lAudits);
        }

        return auditItems;
    }

    public boolean editFax(long ID, String fax, String faxConfirm) {
        try {
            OrPaperContact hotel = (OrPaperContact) orPaperContactDao.loadObject(Long.valueOf(ID));
            hotel.setFax(fax);
            if (faxConfirm.equals("0")) {
                hotel.setFaxConfirm(false);
            } else {
                hotel.setFaxConfirm(true);
            }
            orPaperContactDao.saveOrUpdate(hotel);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 分配某一天的日审酒店给user
     * 
     * @param hotelId
     *            表里的主键id
     * @param user
     * @return 是否分配成功
     */
    public boolean arrangeAuditHotel(long hotelId, OrWorkStates user) {
        try {
            OrDailyAudit hotel = (OrDailyAudit) orDailyAuditDao.loadOrDailyAudit(Long.valueOf(hotelId));
            hotel.setAssignTo(user.getLogonId());
            orDailyAuditDao.saveOrUpdate(hotel);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 批处理分配某一天的日审酒店给user
     * 
     * @param hotelId
     *            OrDailyAudit类的主键，不是字面意思上的酒店ID
     * @param user
     * @return
     */
    public boolean batchArrangeAuditHotel(Long[] hotelId, OrWorkStates user) {
        for (int i = 0; i < hotelId.length; i++) {
            if (false == arrangeAuditHotel(hotelId[i].longValue(),user)) {
                return false;
            }
        }
        return true;
    }

    public OrDailyAudit getOrDailyAudit(Serializable auditID) {
        return (OrDailyAudit) orDailyAuditDao.loadOrDailyAudit(auditID);
    }

    public Session getSession() {
        return orDailyAuditDao.getCurrentSession();
    }

    public List getOrPaperContact(Long hotelID) {

        List res = orPaperContactDao.queryByNamedQuery("hQueryOrPaperContact",
            new Object[] { hotelID });

        // TODO: 检查是否发送过日审传真

        return res;
        // return (OrPaperContact) orPaperContactDao.loadObject(hotelID);
    }

    /**
     * 根据OrDailyAudit主键获取OrDailyAudit
     * 
     * @param barCode
     * @return
     */
    public OrDailyAudit getOrDailyAuditId(String barCode) {
        return orDailyAuditDao.loadOrDailyAudit(Long.valueOf(barCode));
    }

    /**
     * 取出未被分配的酒店订单
     * 
     * @param date
     * @param max
     */
    public List getOrDailyAuditIsNull(Date date) {
        String hsql = "from OrDailyAudit od where od.checkNight = ? and od.assignTo is null "
            + " order by od.orderNumbers desc";
        Object[] obj = new Object[] { date };

        return orDailyAuditDao.query(hsql, obj);
    }

    public OrPaperDailyAudit getOrPaperDailyAudit(Serializable auditID) {
        return orPaperDailyAuditDao.get(OrPaperDailyAudit.class, auditID);
    }

    public List findSimilarAudit(OrDailyAudit orDailyAudit) {
        List res = orPaperDailyAuditDao.queryByNamedQuery("hQueryOrPaperDailyAudit",
            new Object[] { orDailyAudit.getID() });

        // TODO: 检查是否发送过日审传真

        return res;
    }

    public List findSimilarOrder1(Date date) {

        // String hsql = "select DISTINCT(b.hotelId),a.hotelName " +
        // "from VOrOrder a, VOrOrderItem b " +
        // "where a.ID = b.orderID " +
        // "and a.orderState != 5 " +
        // "and a.orderState != 13 " +
        // "and a.orderState != 14 " +
        // "and b.night = ? " +
        // "order by b.hotelId ";
        String hsql = "select DISTINCT (a.hotelId), a.hotelName "
            + "from VOrOrder a where  a.orderState != 5 " + "and a.orderState != 13 "
            + "and a.orderState != 14 " + "and a.payMethod = 'pay' "
            + "and a.ID in (select b.orderID from VOrOrderItem b where  " + "b.night = ?)"
            + "order by a.hotelId ";
        Object[] obj = new Object[] { date };

        return orOrderDao.query(hsql, obj);
    }

    /**
     * 根据参数获得orDailyAudit对象
     * 
     * @param hotelId
     * @param date
     * @return
     */
    public List findSimilarDailyAudit(Object hotelId, Date date) {
        List res = orDailyAuditDao.queryByNamedQuery("hQueryAudit_Daily", new Object[] { hotelId,
            date });

        // TODO: 查询检查重复的日审酒店

        return res;
    }

    /**
     * 根据参数获得orOrderItrm对象
     * 
     * @param hotelId
     * @param date
     * @return
     */
    public List findReturnDailyAudit(Object hotelId, Date date) {
        List res = viewOrOrderItemDao.queryByNamedQuery("hreturnODaily", new Object[] { hotelId,
            date });

        // TODO: 查询检查重复的日审酒店

        return res;
    }

    /**
     * 根据参数获得order对象
     * 
     * @param hotelId
     * @param date
     * @return
     */

    public List findSimilarOrderNumbers(Object hotelId, Date date) {
        String hsql = "SELECT DISTINCT(a.orderID) from VOrOrderItem a " +
                "where	a.hotelId = ? and a.night = ? " +
                "and a.orderID in (select o.ID from VOrOrder o " +
                "where o.payMethod = 'pay' and o.orderState != 14 " +
                " and o.orderState != 13 and o.orderState != 5)";
        Object[] obj = new Object[] { hotelId, date };

        // TODO: 查询日审酒店的订单数量
        return orOrderItemDao.query(hsql, obj);
    }

    /**
     * 根据参数获得OrDailyAudit对象
     * 
     * @param date
     * @return
     */

    public List getNewOrDailyAudit(Date date) {

        List res = orDailyAuditDao.queryByNamedQuery("hQueryOrder_Hotel", new Object[] { date });
        // TODO: 查询检查重复的日审酒店

        return res;
    }

    /**
     * 根据参数获得orOrderItem对象
     * 
     * @param orderId
     * @param date
     * @return
     */

    public List findSimilarOrOrderItem(Long orderId, Date date) {

        List res = viewOrOrderItemDao.queryByNamedQuery("hQueryOrder_ItemState", new Object[] {
            orderId, date });
        // TODO: 查询检查重复的日审酒店

        return res;
    }

    /**
     * 根据参数获得orOrderItem对象
     * 
     * @param orderId
     * @param date
     * @return
     */

    public List findSimilarOrOrderItemTemp(Long orderId, Date date) {

        List res = viewOrOrderItemDao.queryByNamedQuery("hQueryOrder_ItemTemp", new Object[] {
            orderId, date });
        // TODO: 查询检查重复的日审酒店

        return res;
    }

    public List findSimilarOrOrderShow(Long orderId, Date date) {

        List res = orOrderItemDao.queryByNamedQuery("hQueryOrder_ItemShow1", new Object[] {
            orderId, date });
        // TODO: 查询检查重复的日审酒店

        return res;
    }

    public List findSimilarViewOrOrderShow(Long orderId, Date date) {

        List res = viewOrOrderItemDao.queryByNamedQuery("hQueryOrder_ItemShow", new Object[] {
            orderId, date });
        // TODO: 查询检查重复的日审酒店

        return res;
    }

    /**
     * 修改未入住情况下的订单明细显示状态
     * 
     * @param orderId
     * @param date
     * @param roomIndex
     * @return
     */
    public List findSimilarViewOrOrderShow(Long orderId, Date date, int roomIndex) {

        List res = viewOrOrderItemDao.queryByNamedQuery("hQueryOrder_ItemNotShow", new Object[] {
            orderId, date, roomIndex });
        // TODO: 查询检查重复的日审酒店

        return res;
    }

    public List findSimilarOrder(Date date) {
        List res = orOrderDao.queryByNamedQuery("hQueryAudit_Order", new Object[] { date, date });

        // TODO: 查询需要发送日审的酒店

        return res;
    }

    /**
     * 取出操作人员现处理数量
     * 
     * @param date
     * @param max
     */
    public List getUserWorkNum(Date date) {
        String hsql = "select od.assignTo,od.orderNumbers from OrDailyAudit od "
            + "where od.checkNight = ? and od.assignTo is not null " + "order by od.assignTo";
        Object[] obj = new Object[] { date };

        return orDailyAuditDao.query(hsql, obj);
    }

    /**
     * 自动分配日审酒店 date
     * 
     * @return
     */

    public boolean automatismAllotManager(Date checkNight, List userList) {
        // 根据日审日期得到应该在该天进行日审并且没有被分配的所有订单
        List hotels = getOrDailyAuditIsNull(checkNight);
        if (null != hotels && 0 < hotels.size() && null != userList && 0 < userList.size()) {

            // ---------------该节用来统计处于open状态的日审用户手头已经分配的该天(checkNight)的订单数目---------------//
            // Map workMap = new HashMap();
            // 用来保存操作人手头已有的日审订单数目
            List workOrderList = new ArrayList();
            // 取得所有日审操作人手头已经分配的该日期的现有订单
            List workList = getUserWorkNum(checkNight);
            for (int i = 0; i < userList.size(); i++) {
                int orderNumbers = 0;
                OrWorkStates orWorkStates = (OrWorkStates) userList.get(i);
                String userID = orWorkStates.getLogonId();
                Object[] workUserList = null;
                for (int j = 0; j < workList.size(); j++) {
                    workUserList = (Object[]) workList.get(j);
                    String assignTo = workUserList[0].toString();
                    if (userID.equals(assignTo)) {
                        orderNumbers = (Integer) workUserList[1] + orderNumbers;
                    }
                }
                // workMap.put(userID,orderNumbers);
                HotelWordUser hotelWordUser = new HotelWordUser();
                hotelWordUser.setUserID(userID);
                hotelWordUser.setOrderNumbers(orderNumbers);
                hotelWordUser.setGroups(orWorkStates.getGroups());// 日审组别：普通/审核
                workOrderList.add(hotelWordUser);
            }

            // Iterator index = workMap.entrySet().iterator();
            // HotelWordUser hotelWordUser = new HotelWordUser();
            // while(index.hasNext()){
            // Map.Entry map=(Map.Entry)index.next();
            // }
            Collections.sort(workOrderList, new InsensitiveComp());


            for (int i = 0; i < hotels.size(); i++) {
                boolean state = false;
                OrDailyAudit orDailyAudit = (OrDailyAudit) hotels.get(i);// 需要日审的某酒店
                String group = orDailyAudit.getStatus() + "";// 日审状态：未收回传:0/已收回传:1/待审查:2/完成:3
                orDailyAudit.getHotelId();
                int number = (Integer) orDailyAudit.getOrderNumbers();
                // Collections.sort(workOrderList,new InsensitiveComp());
                for (int j = 0; j < workOrderList.size(); j++) {
                    HotelWordUser hotelWordUser = (HotelWordUser) workOrderList.get(j);
                    String groups = hotelWordUser.getGroups();// 日审组别：普通/审核
                    String[] groupID = groups.split(",");
                    for (int p = 0; p < groupID.length; p++) {
                        if (group.equals("0") || group.equals("1")) {// 如果酒店日审状态是未收回传或者已收回传
                            if (groupID[p].equals("1")) {// 如果日审操作人员的组别是"普通"
                                orDailyAudit.setAssignTo(hotelWordUser.getUserID());
                                // 更新操作人员的订单数量
                                hotelWordUser.setOrderNumbers(hotelWordUser.getOrderNumbers()
                                    + number);
                                orDailyAuditDao.saveOrUpdate(orDailyAudit);
                                // 重新按照升序排序
                                for (int k = 1; k < workOrderList.size(); k++) {
                                    HotelWordUser cmpWorker = (HotelWordUser) workOrderList.get(k);
                                    if (hotelWordUser.getOrderNumbers() > cmpWorker
                                        .getOrderNumbers()) {
                                        workOrderList.set(k, hotelWordUser);
                                        workOrderList.set(k - 1, cmpWorker);
                                    } else {
                                        break;
                                    }
                                }
                                state = true;
                                break;
                            }
                        } else if (group.equals("2") 
                            && groupID[p].equals("2")) {
                            // 如果是"待审查"并且组别是"审核"
                            orDailyAudit.setAssignTo(hotelWordUser.getUserID());
                            hotelWordUser.setOrderNumbers(hotelWordUser.getOrderNumbers() + number);
                            orDailyAuditDao.saveOrUpdate(orDailyAudit);
                            for (int k = 1; k < workOrderList.size(); k++) {
                                HotelWordUser cmpWorker = (HotelWordUser) workOrderList.get(k);
                                if (hotelWordUser.getOrderNumbers() > cmpWorker.getOrderNumbers()) {
                                    workOrderList.set(k, hotelWordUser);
                                    workOrderList.set(k - 1, cmpWorker);
                                } else {
                                    break;
                                }
                            }
                            state = true;
                            break;
                        }
                    }
                    if (state) {
                        break;
                    }
                }
                // HotelWordUser hotelWordUser = (HotelWordUser)workOrderList.get(0);
            }
            return true;
        } else {
            return false;
        }
    }

    private final static class InsensitiveComp implements java.util.Comparator {
        public int compare(Object a1, Object a2) {
            Integer s1 = ((HotelWordUser) a1).getOrderNumbers();
            Integer s2 = ((HotelWordUser) a2).getOrderNumbers();
            return s1.compareTo(s2);
        }
    }

    /**
     * 获取日审传真信息
     * 
     */
    public List getOrderItemsForAuditFax(Long auditId) {
        String hsql = " select b.orderCD, b.roomTypeName, b.fellowNames, 1 " +
                "as quantity ,b.confirmNo, b.checkinDate, b.checkoutDate, " +
                "a.roomNo, a.hotelId, a.night, a.orderID, a.roomIndex, " +
                "a.fellowName, b.createDate"
            + " from VOrOrderItem a, VOrOrder b, OrDailyAudit c "
            + " where "
            + "	c.ID = ? "
            + "	and a.hotelId = c.hotelId "
            + "	and a.hotelId = b.hotelId "
            + "	and b.hotelId = c.hotelId "
            + "	and c.checkNight = a.night "
            + " 	and a.orderID = b.ID "
            + "   and b.orderState != 14 "
            + "	and a.auditType = b.orderType "
            + "   and a.show != 1 "
            + "   and b.payMethod = 'pay' " + " order by b.ID,a.ID";

        // " select b.orderCD, b.roomTypeName, b.fellowNames, 1 as quantity " +
        // " from VOrOrder b where b.ID in (select oi.orderID from VOrOrderItem oi,OrDailyAudit od "
        // +
        // " WHERE  oi.hotelId = od.hotelId and oi.night= od.checkNight
        //and od.ID = ? and oi.auditState != 3) "
        // +
        // " order by b.ID";
        List itemList = orOrderDao.query(hsql, new Object[] { auditId });
        return itemList;
    }

    private DAOIbatisImpl queryDao;// parasoft-suppress SERIAL.NSFSC "JAR 包问题"

    /**
     * 根据参数获取日审传真list
     * 
     * @param
     * @param auditDate
     *            ,hotelName,hotelstate,hotelcity,hotelzone,sendFax,isSend
     * @return
     */
    public List gethotels(Map params) {

        List results = queryDao.queryForList("querySendFaxs", params);
        for (int i = 0; i < results.size(); i++) {
            Integer value = (Integer) results.get(i);
            log.info(value + "===");
        }
        return results;
    }

    /**
     * 日审传真获取酒店信息
     * 
     */
    public Object[] getHotelInfoForAuditFax(Long auditId) {
        String hsql = " select a.hotelName, a.checkNight, b.fax "
            + " from OrDailyAudit a, OrPaperContact b " + " where " + "	a.ID = ? "
            + "	and a.hotelId = b.hotelId";

        List itemList = orOrderDao.query(hsql, new Object[] { auditId });
        if (0 < itemList.size()) {
            return (Object[]) itemList.get(0);
        } else {
            return null;
        }

    }

    /**
     * 插入回传日志
     */
    public boolean saveFaxLogAndOrDailyAudit(OrDailyAudit orDailyAudit, OrFaxLog orFaxLog) {
        try {
            orDailyAuditDao.saveOrUpdate(orDailyAudit);
            orFaxLogDao.saveOrUpdate(orFaxLog);
            return true;
        } catch (Exception e) {
        	log.error(e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * 插入自动日审日志
     * 
     */
    public boolean saveOrAuditFaxLog(OrAuditFaxLog orAuditFaxLog) {

        try {
            orAuditFaxLogDao.saveOrUpdate(orAuditFaxLog);
            return true;
        } catch (Exception e) {
        	log.error(e.getLocalizedMessage());
            return false;
        }
    }

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
        String fellowNames, UserWrapper roleUser) {

        System.currentTimeMillis();

        Date modifiedTime = new Date();
        Long[] selID;
        List orderItem;
        int state = OrderItemAuditState.ALREADY_SAVE;
        Date date = DateUtil.getDate(night);
        // VOrOrder order = this.getVOrder(Long.parseLong(orderID));

        List auditList = this.getViewOrders(Long.parseLong(orderID), orderAuditType);
        VOrOrder order = (VOrOrder) auditList.get(0);
        String bare = "bare";
        // 已入住操作
        String auditItemStr = rState;
        if (auditItemStr.equals("") || auditItemStr.equals(bare)) {
        	log.info("没有入住的房间!");
        } else {
            String[] ids = auditItemStr.split(",");
            selID = new Long[ids.length];
            for (int i = 0; i < selID.length; i++) {
                String[] idAndType = ids[i].split("/");
                selID[i] = Long.parseLong(idAndType[1]);
                int auditType = Integer.parseInt(idAndType[0]);
                orderItem = this.getViewOrderItems(selID[i], auditType);
                VOrOrderItem orOrderItem = (VOrOrderItem) orderItem.get(0);
                orOrderItem.setOrderState(1);
                orOrderItem.setAuditState(state);
                orOrderItem.setNotesMan(roleUser.getName());
                orOrderItem.setNoteTime(modifiedTime);

                VOrOrderItem orOrderShow = editOrderNotShow(orderID, date, false, orOrderItem
                    .getRoomIndex());
                this.saveAuditInfo(orOrderItem, orOrderShow);

            }
        }

        // 未入住操作
        String auditItemNotStr = rStateNot;
        if (auditItemNotStr.equals("") || auditItemNotStr.equals(bare)) {
        	log.info("全部房间都已入住!");
        } else {
            String[] ids1 = auditItemNotStr.split(",");
            selID = new Long[ids1.length];
            for (int i = 0; i < ids1.length; i++) {
                String[] idAndType = ids1[i].split("/");
                selID[i] = Long.parseLong(idAndType[1]);
                int auditType = Integer.parseInt(idAndType[0]);
                orderItem = this.getViewOrderItems(selID[i], auditType);
                VOrOrderItem orOrderItem = (VOrOrderItem) orderItem.get(0);
                orOrderItem.setOrderState(2);
                orOrderItem.setAuditState(state);
                orOrderItem.setNotesMan(roleUser.getName());
                orOrderItem.setNoteTime(modifiedTime);

                VOrOrderItem orOrderShow = editOrderNotShow(orderID, date, true, orOrderItem
                    .getRoomIndex());
                this.saveAuditInfo(orOrderItem, orOrderShow);

            }
        }

        // 设置入住人姓名和房间号
        String auditRoomStr = rID;
        String[] rId = auditRoomStr.split(",");
        // String[] roomNos = new String[rId.length];
        selID = new Long[rId.length];
        String auditRoomNoStr = roomNumber;
        String[] rNumber = auditRoomNoStr.split(",");
        String[] fellowName = fellowNames.split(",");
        // QC545用于记录新的主的入住人姓名，保存到Order中 add by chenjiajie@2009-06-11
        StringBuffer newFellowNames = new StringBuffer();
        for (int i = 0; i < rId.length - 1; i++) {
            String[] idAndType = rId[i + 1].split("/");
            selID[i] = Long.parseLong(idAndType[1]);
            int auditType = Integer.parseInt(idAndType[0]);

            // roomNos[i] = (String)params.get("roomNo_" +orderID+"_"+ (i+1));
            orderItem = this.getViewOrderItems(selID[i], auditType);
            VOrOrderItem orOrderItem = (VOrOrderItem) orderItem.get(0);
            orOrderItem.setFellowName(fellowName[i + 1]);
            orOrderItem.setRoomNo(rNumber[i + 1]);
            this.sendViewAuditDetail(orOrderItem);

            // QC545最后一个名字后面不加空格 add by chenjiajie@2009-06-11
            newFellowNames.append(fellowName[i + 1]);
            if (i < rId.length - 2) {
                newFellowNames.append(" ");
            }
        }

        /**
         * QC545日审界面修改OrderItem的入住人姓名，Order的入住人姓名没有更新 add by chenjiajie@2009-06-11
         * 把','换成空格' '，再保存到数据库
         */
        order.setFellowNames(newFellowNames.toString());

        // 记录日审状态
        order.setAuditState(orderState);
        order.setSpecialNote(specialNote);
        order.setModifierName(roleUser.getName());
        // QC444日审操作后最后修改人中文名和工号对不上add by chenjiajie@2009-05-05
        order.setModifier(roleUser.getLoginName());
        order.setModifiedTime(modifiedTime);
        this.sendAuditViewOrder(order);

        return index;
    }

    /**
     * 处理日审单 -- noshow或提前退房
     * 
     * @return
     */
    public int auditWorkNoShow(int index, String orderID, String night, int orderState,
        String noshowCode, String noshowReason, String specialNote, int orderAuditType,
        UserWrapper roleUser) {
        Date modifiedTime = new Date();
        int state = OrderItemAuditState.ALREADY_SAVE;
        // boolean bResult = false ;
        Date date = DateUtil.getDate(night);
        List auditList = this.getViewOrders(Long.parseLong(orderID), orderAuditType);
        VOrOrder order = (VOrOrder) auditList.get(0);
        // VOrOrder order = this.getVOrder(Long.parseLong(orderID));
        order.setAuditState(orderState);
        if (13 == orderState) {
            VOrOrderItem orderItem = noshowDispose(orderID, order.getOrderType());
            this.sendAuditDetail(orderItem);
            order.setNoshowCode(Integer.parseInt(noshowCode));
            order.setNoshowReason(noshowReason);
        } else if (5 == orderState) {
            VOrOrderItem orderItem = fadeRoomDispose(orderID, night);
            this.sendAuditDetail(orderItem);
        }
        order.setSpecialNote(specialNote);
        order.setModifierName(roleUser.getName());
        // QC444日审操作后最后修改人中文名和工号对不上add by chenjiajie@2009-05-05
        order.setModifier(roleUser.getLoginName());
        order.setModifiedTime(modifiedTime);
        VOrOrderItem orOrderItem = editWorkState(orderID, date, state, roleUser.getName());
        this.saveNoShow(order, orOrderItem);
        return index;
    }

    /**
     * 处理日审单 -- 修改为入住情况下的显示状态
     * 
     * @return
     */
    public VOrOrderItem editOrderNotShow(String orderID, Date date, boolean show, int roomIndex) {
        VOrOrderItem orOrderItem = null;
        List workState = this.findSimilarViewOrOrderShow(Long.parseLong(orderID), date, roomIndex);
        for (int i = 0; i < workState.size(); i++) {
            orOrderItem = (VOrOrderItem) workState.get(i);
            if (show) {
                orOrderItem.setOrderState(2);
            }
            orOrderItem.setShow(show);
        }
        return orOrderItem;
    }

    /**
     * 处理日审单 -- 修改操作状态
     * 
     * @return
     */
    public VOrOrderItem editWorkState(String orderID, Date date, int state, String userName) {
        VOrOrderItem orOrderItem = null;
        Date noteTime = new Date();
        List workState = this.findSimilarOrOrderItem(Long.parseLong(orderID), date);
        for (int i = 0; i < workState.size(); i++) {
            orOrderItem = (VOrOrderItem) workState.get(i);
            orOrderItem.setAuditState(state);
            orOrderItem.setNotesMan(userName);
            orOrderItem.setNoteTime(noteTime);
            /** 更新未来几天明细入住人姓名和房间号 V2.4.2 chenjiajie@2008-12-31 begin **/
            if (state == OrderItemAuditState.ACHIEVE) { // 当日审明细状态为完成的时候
                List futurityOrOrderItems = this.findSimilarViewOrOrderShow(orOrderItem
                    .getOrderID(), orOrderItem.getNight(), orOrderItem.getRoomIndex());
                for (Iterator it = futurityOrOrderItems.iterator(); it.hasNext();) {
                    VOrOrderItem futurityOrOrderItem = (VOrOrderItem) it.next();
                    futurityOrOrderItem.setFellowName(orOrderItem.getFellowName());
                    // futurityOrOrderItem.setRoomNo(orOrderItem.getRoomNo());
                    this.sendViewAuditDetail(futurityOrOrderItem);
                }
            }
            /** 更新未来几天明细入住人姓名和房间号 V2.4.2 chenjiajie@2008-12-31 end **/
        }
        return orOrderItem;
    }

    /**
     * noshow情况下的状态处理
     * 
     */
    public VOrOrderItem noshowDispose(String orderID, Integer orderType) {
        List oItem = this.getVOrderItem(Long.parseLong(orderID), orderType);
        VOrOrderItem orderItem = null;
        for (int i = 0; i < oItem.size(); i++) {
            orderItem = (VOrOrderItem) oItem.get(i);
            orderItem.setOrderState(2);
        }
        return orderItem;
    }

    /**
     * 提前退房情况下的状态处理
     * 
     */
    public VOrOrderItem fadeRoomDispose(String orderID, String night) {
        Date date = DateUtil.getDate(night);
        List oItem = this.getVOrderItem1(Long.parseLong(orderID), date);
        VOrOrderItem orderItem = null;
        for (int i = 0; i < oItem.size(); i++) {
            orderItem = (VOrOrderItem) oItem.get(i);
            orderItem.setOrderState(2);
        }
        return orderItem;
    }

    /**
     * 处理日审单 -- 修改操作状态
     * 
     * @return
     */
    public VOrOrderItem editWorkState(String orderID, Date date, int roomIndex, int state,
        UserWrapper roleUser) {
        VOrOrderItem orOrderItem = null;
        Date noteTime = new Date();
        List workState = this.findSimilarOrOrderItem(Long.parseLong(orderID), date);
        for (int i = 0; i < workState.size(); i++) {
            orOrderItem = (VOrOrderItem) workState.get(i);
            orOrderItem.setAuditState(state);
            orOrderItem.setNotesMan(roleUser.getName());
            orOrderItem.setNoteTime(noteTime);
        }
        log.info(workState.size());
        return orOrderItem;
    }

    /**
     * 处理日审单 -- 待审核
     * 
     * @return
     */
    public int auditNotAuditing(int index, String orderID, String night, long hotelID,
        UserWrapper roleUser) {
        int roomIndex = 0;
        // boolean bResult = false ;
        int state = OrderItemAuditState.STAY_AUDITING;
        Date date = DateUtil.getDate(night);
        // orderID = (String)params.get("orderID");
        // hotelId = Long.parseLong(params.get("hotelId").toString());
        List hotels = this.findSimilarDailyAudit(hotelID, date);
        OrDailyAudit orDailyAudit = (OrDailyAudit) hotels.get(0);
        if (orDailyAudit.getStatus() != OrdailyauditStatus.STAY_AUDITING) {
            orDailyAudit.setStatus(OrdailyauditStatus.STAY_AUDITING);
        }
        /**
         * HOP_2.4.1 modify by chenjiajie Begin 功能说明:如果日审员是OPEN状态,并属于"普通"和"审核"两个工作组,订单继续由他跟进
         **/
        List orWorkStateList = orWorkStatesDao.queryByNamedQuery("queryWorkstates_ItemNum1",
            new Object[] { orDailyAudit.getAssignTo(), 2 });
        if (0 < orWorkStateList.size()) {
            OrWorkStates orWorkState = (OrWorkStates) orWorkStateList.get(0);
            // 日审员状态open,组别属于"普通"和"审核"两个工作组
            if (null != orWorkState.getGroups()
                && (orWorkState.getGroups().equals("2,1") ||
                    orWorkState.getGroups().equals("1,2"))) {
                orDailyAudit.setAssignTo(orDailyAudit.getAssignTo());
            } else {
                orDailyAudit.setAssignTo("");
            }
        } else {
            orDailyAudit.setAssignTo("");
        }
        // 增加“变成待审核状态”的时间
        orDailyAudit.setDailyAuditTime(DateUtil.stringToDatetime(DateUtil
            .datetimeToString(new Date())));
        /** HOP_2.4.1 modify by chenjiajie End **/
        VOrOrderItem orOrderItem = editWorkState(orderID, date, roomIndex, state, roleUser);
        this.sendAuditDetail(orOrderItem);

        // List hotels1 = this.getOrderItemNum(hotelID, date, roomIndex);
        // for(int i=0;i<hotels1.size();i++){
        // VOrOrderItem orderItem = (VOrOrderItem)hotels1.get(i);
        // if(orderItem.getAuditState() == 2){
        // bResult = true;
        // break;
        // }
        // }
        // 待审核状态，清除操作人，将日审记录返回分配池。
        // if(bResult){
        // orDailyAudit.setAssignTo("");
        // }
        this.saveOrDailyAudit(orDailyAudit);

        return 0;

    }

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
        int orderAuditType, UserWrapper roleUser) {
        this.auditWorkNoShow(index, orderID, night, orderState, noshowCode, noshowReason,
            specialNote, orderAuditType, roleUser);
        this.auditNotAuditing(index, orderID, night, hotelID, roleUser);
        // this.auditFulfill(index, orderID, night, hotelID, orderAuditType, roleUser);
        return 0;
    }

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
        long hotelID, int orderAuditType, String fellowNames, UserWrapper roleUser) {
        this.auditWork(index, orderID, night, rState, rStateNot, orderState, rID, specialNote,
            roomNumber, orderAuditType, fellowNames, roleUser);
        this.auditFulfill(index, orderID, night, null, null, null, hotelID, orderAuditType,
            roleUser);
        return 0;
    }

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
        long hotelID, int orderAuditType, String fellowNames, UserWrapper roleUser) {
        this.auditWork(index, orderID, night, rState, rStateNot, orderState, rID, specialNote,
            roomNumber, orderAuditType, fellowNames, roleUser);
        this.auditNotAuditing(index, orderID, night, hotelID, roleUser);
        return 0;
    }

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
        int orderAuditType, UserWrapper roleUser) {
        this.auditWorkNoShow(index, orderID, night, orderState, noshowCode, noshowReason,
            specialNote, orderAuditType, roleUser);
        auditFulfill(index, orderID, night, noshowCode, noshowReason, specialNote, hotelID,
            orderAuditType, roleUser);
        return 0;
    }

    /**
     * 处理日审单 -- 完成
     * 
     * @return
     */
    public int auditFulfill(int index, String orderID, String night, String noshowCode,
        String noshowReason, String specialNote, long hotelID, int orderAuditType,
        UserWrapper roleUser) {
        // Map params = super.getParams();
        boolean bResult = false;
        Date date = DateUtil.getDate(night);
        Date now = new Date();
        int roomIndex = 0;
        int state = OrderItemAuditState.ACHIEVE;

        // 取得该订单操作状态
        VOrOrderItem orOrderItem = editWorkState(orderID, date, state, roleUser.getName());
        List auditList = this.getViewOrders(Long.parseLong(orderID), orderAuditType);
        VOrOrder order = (VOrOrder) auditList.get(0);
        order.setModifierName(roleUser.getName());
        // QC444日审操作后最后修改人中文名和工号对不上add by chenjiajie@2009-05-05
        order.setModifier(roleUser.getLoginName());
        order.setModifiedTime(now);
        int orderState = order.getAuditState();
        if (5 == orderState) {
            // 提前退房也要把后面日期的日审明细置为show=1?
            // modified by chenkeming
            VOrOrderItem orOrderShow = editOrderShow(orderID, date, true);
            // order.setAuditState(OrderState.EARLY_QUIT);
            order.setOrderState(OrderState.EARLY_QUIT);
            // this.saveOrderItemAndOrOrder(orOrderItem,order);
            this.saveFulfill(orOrderItem, orOrderShow, order);
        } else if (13 == orderState) {
            VOrOrderItem orOrderShow = editOrderShow(orderID, date, true);
            // order.setAuditState(OrderState.NOSHOW);
            order.setOrderState(OrderState.NOSHOW);
            order.setNoshowCode(Integer.parseInt(noshowCode));
            order.setNoshowReason(noshowReason);
            order.setSpecialNote(specialNote);
            this.saveFulfill(orOrderItem, orOrderShow, order);
        } else if (6 == orderState) {
            VOrOrderItem orOrderShow = editOrderShow(orderID, date, false);
            // order.setAuditState(OrderState.NORMAL_QUIT);
            order.setOrderState(OrderState.NORMAL_QUIT);
            this.saveFulfill(orOrderItem, orOrderShow, order);
        } else if (7 == orderState) {
            VOrOrderItem orOrderShow = editOrderShow(orderID, date, false);
            // order.setAuditState(OrderState.EXTEND);
            order.setOrderState(OrderState.EXTEND);
            this.saveFulfill(orOrderItem, orOrderShow, order);
        } else {
            order.setOrderState(OrderState.CHECKIN);
            order.setAuditState(OrderState.CHECKIN);
            this.saveOrderItemAndOrOrder(orOrderItem, order);
        }
        // 取得该酒店当天的所有订单数量
        List hotels = this.getOrderItemNum(hotelID, date, roomIndex);
        for (int i = 0; i < hotels.size(); i++) {
            VOrOrderItem orderItem = (VOrOrderItem) hotels.get(i);
            if (3 == orderItem.getAuditState()) {
            } else if (2 == orderItem.getAuditState()) {
                bResult = true;
            } else {
                return 0;
            }
        }

        hotels = this.findSimilarDailyAudit(hotelID, date);
        OrDailyAudit orDailyAudit = (OrDailyAudit) hotels.get(0);
        orDailyAudit.setCompleteTime(now);
        if (!bResult) {
            orDailyAudit.setStatus(OrdailyauditStatus.ACHIEVE);
        } else {
            if (orDailyAudit.getStatus() != OrdailyauditStatus.STAY_AUDITING) {
                orDailyAudit.setAssignTo("");
                orDailyAudit.setStatus(OrdailyauditStatus.STAY_AUDITING);
            }
        }
        this.saveOrDailyAudit(orDailyAudit);
        return 0;
    }

    /**
     * 处理日审单 -- 修改操作状态
     * 
     * @return
     */
    public VOrOrderItem editOrderShow(String orderID, Date date, boolean show) {
        VOrOrderItem orOrderItem = null;
        List workState = this.findSimilarViewOrOrderShow(Long.parseLong(orderID), date);
        for (int i = 0; i < workState.size(); i++) {
            orOrderItem = (VOrOrderItem) workState.get(i);
            orOrderItem.setShow(show);
        }
        return orOrderItem;
    }

    /**
     * 根据参数获取OrAuditFaxLog
     * 
     * @param data
     *            *
     * @return
     */
    public List getAuditLogList(Date data) {
        List res = orAuditFaxLogDao.queryByNamedQuery("hQueryAudit_Log", new Object[] { data });

        // TODO: 查询酒店ID

        return res;
    }

    // ========================================

    public IOrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(IOrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public AuditDao getAuditDao() {
        return auditDao;
    }

    public void setAuditDao(AuditDao auditDao) {
        this.auditDao = auditDao;
    }

    public OrPaperContactDao getOrPaperContactDao() {
        return orPaperContactDao;
    }

    public void setOrPaperContactDao(OrPaperContactDao orPaperContactDao) {
        this.orPaperContactDao = orPaperContactDao;
    }

    public OrPaperDailyAuditDao getOrPaperDailyAuditDao() {
        return orPaperDailyAuditDao;
    }

    public void setOrPaperDailyAuditDao(OrPaperDailyAuditDao orPaperDailyAuditDao) {
        this.orPaperDailyAuditDao = orPaperDailyAuditDao;
    }

    public OrDailyAuditDao getOrDailyAuditDao() {
        return orDailyAuditDao;
    }

    public void setOrDailyAuditDao(OrDailyAuditDao orDailyAuditDao) {
        this.orDailyAuditDao = orDailyAuditDao;
    }

    public OrOrderDao getOrOrderDao() {
        return orOrderDao;
    }

    public void setOrOrderDao(OrOrderDao orOrderDao) {
        this.orOrderDao = orOrderDao;
    }

    public OrDailyAudit getOrDailyAudit() {
        return orDailyAudit;
    }

    public void setOrDailyAudit(OrDailyAudit orDailyAudit) {
        this.orDailyAudit = orDailyAudit;
    }

    public OrOrderItemDao getOrOrderItemDao() {
        return orOrderItemDao;
    }

    public void setOrOrderItemDao(OrOrderItemDao orOrderItemDao) {
        this.orOrderItemDao = orOrderItemDao;
    }

    public OrPaperDailyAuditItemDao getOrPaperDailyAuditItemDao() {
        return orPaperDailyAuditItemDao;
    }

    public void setOrPaperDailyAuditItemDao(OrPaperDailyAuditItemDao orPaperDailyAuditItemDao) {
        this.orPaperDailyAuditItemDao = orPaperDailyAuditItemDao;
    }

    public ViewOrOrderDao getViewOrOrderDao() {
        return viewOrOrderDao;
    }

    public void setViewOrOrderDao(ViewOrOrderDao viewOrOrderDao) {
        this.viewOrOrderDao = viewOrOrderDao;
    }

    public ViewOrOrderItemDao getViewOrOrderItemDao() {
        return viewOrOrderItemDao;
    }

    public void setViewOrOrderItemDao(ViewOrOrderItemDao viewOrOrderItemDao) {
        this.viewOrOrderItemDao = viewOrOrderItemDao;
    }

    public OrFaxLogDao getOrFaxLogDao() {
        return orFaxLogDao;
    }

    public void setOrFaxLogDao(OrFaxLogDao orFaxLogDao) {
        this.orFaxLogDao = orFaxLogDao;
    }

    public OrOrderItemTempDao getOrOrderItemTempDao() {
        return orOrderItemTempDao;
    }

    public void setOrOrderItemTempDao(OrOrderItemTempDao orOrderItemTempDao) {
        this.orOrderItemTempDao = orOrderItemTempDao;
    }

    public OrOrderItemTemp getOrOrderItemTemp() {
        return orOrderItemTemp;
    }

    public void setOrOrderItemTemp(OrOrderItemTemp orOrderItemTemp) {
        this.orOrderItemTemp = orOrderItemTemp;
    }

    public DAOIbatisImpl getQueryDao() {
        return queryDao;
    }

    public void setQueryDao(DAOIbatisImpl queryDao) {
        this.queryDao = queryDao;
    }

    public OrAuditFaxLogDao getOrAuditFaxLogDao() {
        return orAuditFaxLogDao;
    }

    public void setOrAuditFaxLogDao(OrAuditFaxLogDao orAuditFaxLogDao) {
        this.orAuditFaxLogDao = orAuditFaxLogDao;
    }

    public OrWorkStatesDao getOrWorkStatesDao() {
        return orWorkStatesDao;
    }

    public void setOrWorkStatesDao(OrWorkStatesDao orWorkStatesDao) {
        this.orWorkStatesDao = orWorkStatesDao;
    }
}