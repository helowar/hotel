package com.mangocity.hotel.order.service;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.order.persistence.HOrder;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.user.UserWrapper;

/**
 * 用于订单基本信息修改相关操作
 * 
 * @author chenkeming Feb 12, 2009 9:20:51 AM
 */
public interface IOrderEditService {

    /**
     * 修改订单时扣取配额
     * 
     * @author chenkeming Feb 12, 2009 10:52:27 AM
     * @param order
     * @param orderH
     * @param breakfast
     * @param breakfastNum
     * @param bFirst
     */
    public void deductQuota(OrOrder order, HOrder orderH, int[] breakfast, int[] breakfastNum,
        boolean bFirst);

    /**
     * 修改单确认酒店
     * 
     * @author chenkeming Feb 13, 2009 9:05:48 AM
     * @param orderId
     * @param confirmWay
     *            1:fax, 2:tel
     * @param roleUser
     */
    public void confirmHotel(long orderId, int confirmWay, UserWrapper roleUser);

    /**
     * 修改单取消订单
     * 
     * @author chenkeming Feb 16, 2009 1:11:19 PM
     * @param order
     * @param cancelReason
     * @param cancelMessage
     * @param guestCancelMessage
     * @param roleUser
     */
    public void cancelOrderEdit(OrOrder order, int cancelReason, String cancelMessage,
        String guestCancelMessage, UserWrapper roleUser);

    /**
     * 根据主键load orderH
     * 
     * @author chenkeming Feb 16, 2009 1:58:48 PM
     * @param hisID
     * @return
     */
    public HOrder getOrderH(Serializable hisID);

    /**
     * 恢复历史订单时退配额
     * 
     * @author chenkeming Feb 16, 2009 2:56:02 PM
     * @param order
     * @param orderH
     */
    public void returnQuotaResume(OrOrder order, HOrder orderH);

    /**
     * 获取给定订单的历史订单列表
     * 
     * @author chenkeming Feb 13, 2009 3:01:02 PM
     * @param orderId
     * @return
     */
    public List<Object[]> getHisOrders(long orderId);

    /**
     * 未扣配额订单修改时,扣完配额后，把所扣配额插入到订单配额记录表
     * 
     * @author chenkeming Feb 17, 2009 11:23:08 AM
     * @param order
     * @param orderH
     */
    public void saveOrderItemToQuotaRecord(OrOrder order, HOrder orderH);

    /**
     * 只更新订单的紧急程度和担保状态字段
     * 
     * @author chenkeming Feb 18, 2009 12:18:23 PM
     * @param order
     * @param level
     * @param suretyState
     */
    public void updateOrderForEdit(OrOrder order, int level, int suretyState);

    /**
     * 用于在修改订单提交时，重新从本部预订条款里获取相关信息填充到<br>
     * OrReservation, OrPriceDetail, OrGuaranteeItem, OrAssureItemEvery实体类<br>
     * 包括获取以下内容:<br>
     * <li>酒店的修改字段和担保计算规则(OrReservation的
     * clauseRule,modifyField) <li>每日的预订条款显示信息(在OrPriceDetail里) <li>
     * 预订条款显示信息(OrReservation的clauseStr) <li>担保(预订)的注意事项(OrReservation的creditRemark) <li>
     * 担保信息(包括明细):OrReservation的lateSuretyTime,assureLetter,needCredit,
     * unCondiction,overTimeAssure,roomsAssure,rooms以及guarantees <li>
     * 取消修改规则(OrReservation的assureList)
     * 
     * @author chenkeming Feb 20, 2009 8:54:35 AM
     * @param order
     * @param bForEdit
     */
    public void getReservationInfo(OrOrder order, boolean bForEdit);

    /**
     * 获取取消/修改规则
     * 
     * @author chenkeming Feb 24, 2009 5:53:24 PM
     * @param reservationID
     * @param sumRmb
     * @return
     */
    // public List getModifyCancelStr(long reservationID, double sumRmb);
    // /**
    // * 检查取消订单扣款金额
    // * @author chenkeming Feb 24, 2009 6:20:49 PM
    // * @param reservationID
    // * @param sumRmb
    // * @param checkIn
    // * @return
    // */
    // public String getCancelCheck(long reservationID, double sumRmb, String checkIn,long
    // roomQuantity);
    //	
    /**
     * 检查取消订单扣款金额 add by shizhongwen 时间:Jun 2, 2009 1:59:24 PM
     * 
     * @param reservationID
     *            预订规则id
     * @param order
     *            //订单
     * @param checkIn
     *            时间
     * @param roomQuantity
     *            房间数
     * @param int scope 1:取消 2:修改 3:取消/修改
     * @return
     */
    public String getModifyandCancelCheck(long reservationID, long orderID, String checkIn,
        long roomQuantity, int scope);

    // /**
    // * 检查修改订单扣款金额
    // * @author chenkeming Feb 24, 2009 7:08:36 PM
    // * @param reservationID
    // * @param sumRmb
    // * @param checkIn
    // * @return
    // */
    // public String getModifyCheck(long reservationID, double sumRmb,
    // String checkIn,long roomQuantity);

    /**
     * 改变本次修改订单产生的金额
     * 
     * @author chenkeming Feb 25, 2009 6:08:37 PM
     * @param orderId
     * @param reservationID
     * @param modifyPrice
     * @param note
     * @param roleUser
     * @return
     */
    /*
     * public String chgModPrice(long orderId, long reservationID, double modifyPrice, String note,
     * UserWrapper roleUser);
     */

    /**
     * 新下订单时获取取消/修改规则显示字符串信息,不包含具体金额数
     * 
     * @author chenkeming Feb 24, 2009 5:53:24 PM
     * @param payMethod
     * @param itemsStr
     * @return
     */
    // public List getModifyCancelStrNew(String payMethod, String itemsStr);
    /**
     * 新下单保存时从本部获取修改取消规则信息
     * 
     * @author chenkeming Mar 1, 2009 11:27:30 PM
     * @param order
     * @param itemsStr
     */
    public void fillAssureItemEvery(OrOrder order, String itemsStr);

    /**
     * 供下手工单页面ajax调用,返回预订条款相关信息
     * 
     * @author chenkeming Mar 4, 2009 9:07:41 AM
     * @param hotelId
     * @param checkinDate
     * @param checkoutDate
     * @param roomTypeId
     * @param payMethod
     * @return
     */
    public List<OrPriceDetail> getManualOrderReserv(long hotelId, String checkinDate,
        String checkoutDate, String roomTypeId, String payMethod);

    /**
     * 供CC前台查询酒店结果页面调用,返回预订条款相关信息
     * 
     * @author chenkeming Mar 4, 2009 9:07:41 AM
     * @param hotelId
     * @param checkinDate
     * @param checkoutDate
     * @param childRoomTypeId
     * @return
     */
    public OrPriceDetail getHotelQueryReserv(long hotelId, String checkinDate, String checkoutDate,
        String childRoomTypeId);

    /**
     * 确认金额列表
     * 
     * @author chenkeming Mar 6, 2009 3:07:35 PM
     * @param Id
     * @param roleUser
     * @return
     */
    public String confirmMoney(long Id, UserWrapper roleUser);

    /**
     * 修改订单提交担保单时的金额列表处理
     * 
     * @author chenkeming Mar 10, 2009 3:12:47 PM
     * @param order
     * @param roleUser
     * @param oriCreditAssure
     * @param sysAssureMoney
     * @param oriAssureMoney
     * @param bAuth
     */
    public void updateOrderMoney(OrOrder order, UserWrapper roleUser, boolean oriCreditAssure,
        double sysAssureMoney, double oriAssureMoney, boolean bAuth);

    /**
     * 新下单时增加订单金额
     * 
     * @author chenkeming Mar 10, 2009 3:32:15 PM
     * @param order
     * @param roleUser
     * @param sysAssureMoney
     */
    public void newOrderMoney(OrOrder order, UserWrapper roleUser, double sysAssureMoney);

    /**
     * 取消金额列表
     * 
     * @author chenkeming Mar 6, 2009 3:07:35 PM
     * @param Id
     * @param roleUser
     * @return
     */
    public String cancelMoney(long Id, UserWrapper roleUser);

    /**
     * 确认改变修改/取消金额
     * 
     * @author chenkeming Mar 6, 2009 3:07:35 PM
     * @param Id
     * @param money
     * @param roleUser
     * @return
     */
    public String confirmChgMoney(long Id, double money, UserWrapper roleUser);

    /**
     * 计算担保金额 add by haibo.li
     */
    public void calculateVouchPricePerRoom(OrOrder order);

    /**
     * v2.6 在修改订单页面,获取取消/修改规则字符串,需求改变:根据首日条款判断
     * 
     * @author chenkeming Jun 11, 2009 4:33:42 PM
     * @param reservationID
     * @param sumRmb
     * @return
     */
    public List getModifyCancelStr1(long reservationID, double sumRmb);

    /**
     * v2.6 新下订单时获取取消/修改规则显示字符串信息,不包含具体金额数,需求改变后
     * 
     * @author chenkeming Jun 11, 2009 3:56:10 PM
     * @param payMethod
     * @param itemsStr
     * @return
     */
    public List getModifyCancelStrNew1(String payMethod, String itemsStr);

    /**
     * v2.92 新下订单时获取取消/修改规则显示字符串信息,需求改变后
     * 
     * @author guzhijie 2009-08-04
     * @param payMethod
     * @param itemsStr
     * @param horder
     * @return
     */
    public List getCancelModifyStrNew(OrOrder order, String payMethod, String itemsStr);

    /**
     * v2.92 修改订单时获取取消/修改规则显示字符串信息,需求改变后
     * 
     * @author guzhijie 2009-08-04
     * @param payMethod
     * @param itemsStr
     * @param horder
     * @return
     */
    public List getModifyCancelStr2(OrOrder order, long reservationID, double sumRmb);

    /**
     * v2.92 查看历史订单时获取取消/修改规则显示字符串信息,需求改变后
     * 
     * @author guzhijie 2009-08-04
     * @param payMethod
     * @param itemsStr
     * @param horder
     * @return
     */
    public List getHisModifyCancelStr2(HOrder hOrder, long reservationID, double sumRmb);
}