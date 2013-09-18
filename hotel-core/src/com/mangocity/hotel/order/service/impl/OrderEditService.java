package com.mangocity.hotel.order.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.constant.HotelCalcuAssuAmoType;
import com.mangocity.hotel.base.dao.ExMappingDao;
import com.mangocity.hotel.base.dao.HtlElAssureRuleDao;
import com.mangocity.hotel.base.manage.ClauseManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlAssure;
import com.mangocity.hotel.base.persistence.HtlAssureItemEveryday;
import com.mangocity.hotel.base.persistence.HtlBookCaulClause;
import com.mangocity.hotel.base.persistence.HtlBookModifyField;
import com.mangocity.hotel.base.persistence.HtlElAssureRule;
import com.mangocity.hotel.base.persistence.HtlPreconcertItem;
import com.mangocity.hotel.base.persistence.HtlPrepayEveryday;
import com.mangocity.hotel.base.persistence.HtlPrepayItemEveryday;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlReservCont;
import com.mangocity.hotel.base.persistence.HtlReservation;
import com.mangocity.hotel.base.persistence.OrAssureItemEvery;
import com.mangocity.hotel.base.persistence.OrGuaranteeItem;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.service.IQuotaControlService;
import com.mangocity.hotel.base.service.assistant.QuotaQuery;
import com.mangocity.hotel.base.service.assistant.QuotaReturn;
import com.mangocity.hotel.order.constant.DeductType;
import com.mangocity.hotel.order.constant.GuaranteeType;
import com.mangocity.hotel.order.constant.HotelBalanceMethod;
import com.mangocity.hotel.order.constant.HotelPayLimitType;
import com.mangocity.hotel.order.constant.HotelVouchCondiction;
import com.mangocity.hotel.order.constant.ModifyFieldType;
import com.mangocity.hotel.order.constant.ModifyScopeType;
import com.mangocity.hotel.order.constant.MoneyTargetType;
import com.mangocity.hotel.order.constant.MoneyType;
import com.mangocity.hotel.order.constant.OrderItemType;
import com.mangocity.hotel.order.constant.PayDirectionType;
import com.mangocity.hotel.order.constant.QuotaMemberType;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.persistence.HOrder;
import com.mangocity.hotel.order.persistence.HOrderItem;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrOrderMoney;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.persistence.OrQuotaRecord;
import com.mangocity.hotel.order.persistence.assistant.OrderAssist;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.IOrderEditService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.HotelBaseConstantBean;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.hotel.constant.QuotaType;
import com.mangocity.util.log.MyLog;

/**
 * 用于订单基本信息修改相关操作
 * 
 * @author chenkeming Feb 12, 2009 9:21:46 AM
 */
public class OrderEditService implements IOrderEditService {

	private static final MyLog log = MyLog.getLogger(OrderEditService.class);

    /**
     * @author chenkeming Feb 12, 2009 9:29:15 AM
     */
    private OrOrderDao orOrderDao;

    /**
     * 配额接口
     */
	public IQuotaControlService quotaControl;

    /**
     * 订单辅助类
     * 
     * @author chenkeming Feb 16, 2009 1:13:04 PM
     */
    private OrderAssist orderAssist;

    /**
     * 是否允许取消修改
     * 
     * @author guzhijie 2009-07-29
     */
    private int isAllowCancelModify;
    
    private HotelManage hotelManage;

    /**
     * 预订条款相关管理接口 hotel 2.9.2 add by chenjiajie 2009-08-16
     */
    private ClauseManage clauseManage;

    private static CompOrderItem compOrderItem;

    private static CompModCanItem compModCanItem;

    private static CompModCanItemNew compModCanItemNew;
    
    //艺龙担保条款DAO add by huanglingfeng 2012-5-2
    private HtlElAssureRuleDao htlElAssureRuleDao;
    //酒店映射表DAO add by huanglingfeng 2012-5-2
    private ExMappingDao  exMappingDao;
    
    static {
        compOrderItem = new CompOrderItem();
        compModCanItem = new CompModCanItem();
        compModCanItemNew = new CompModCanItemNew();
    }

    /**
     * 根据订单获取配额模式
     * 
     * @author chenkeming Feb 12, 2009 2:52:04 PM
     * @param order
     * @return
     */
    private String getQuotaPattern(OrOrder order) {
        String hsql = "select quotaPattern FROM HtlContract "
            + "WHERE beginDate <= ? AND endDate>=? AND hotelId =?";
        Object[] obj = new Object[] { order.getCheckinDate(), order.getCheckinDate(), order.getHotelId() };
        List<?> patternList = orOrderDao.query(hsql, obj);
        return patternList.isEmpty()?"" : patternList.get(0).toString();
    }

    /**
     * 对于进店模式,修改订单提交时,如果入住日期不变,根据原单和修改单的信息适当 改变order的orderItems,保留能用部分,删除不能用部分,
     * 对于要新增部分如果在原单的日期范围内，则相应添加orderItem
     * 
     * @author chenkeming Feb 26, 2009 11:43:17 AM
     * @param order
     * @param orderH
     */
    private void chgItemForCI(OrOrder order, HOrder orderH) {
        Date begin = order.getCheckinDate();
        Date endNew = order.getCheckoutDate();
        Date endOld = orderH.getCheckoutDate();
        int numOld = orderH.getRoomQuantity();
        int numNew = order.getRoomQuantity();

        // 先删除不能用的items
        List<OrOrderItem> items = order.getOrderItems();
        List<OrOrderItem> liDel = new ArrayList<OrOrderItem>();
        int daysOld = DateUtil.getDay(begin, endOld);
        int nDif = numOld - numNew;
        if (0 < nDif) {
            for (int i = 0; i < daysOld; i++) {
                int nBase = i * numOld;
                for (int j = 0; j < nDif; j++) {
                    liDel.add(items.get(nBase + numNew + j));
                }
            }
        }
        int daysNew = DateUtil.getDay(begin, endNew);
        if (daysOld > daysNew) {
            int dayDif = daysOld - daysNew;
            for (int i = 0; i < dayDif; i++) {
                int nBase = (i + daysNew) * numOld;
                for (int j = 0; j < numOld; j++) {
                    liDel.add(items.get(nBase + j));
                }
            }
        }
        for (OrOrderItem item : liDel) {
            items.remove(item);
        }

        // 再添加
        if (daysNew > daysOld) {
            int dayDif = daysNew - daysOld;
            int numMin = numOld < numNew ? numOld : numNew;
            List<HOrderItem> itemsH = orderH.getOrderItemsH();
            List<OrPriceDetail> liPrice = order.getPriceList();
            for (int i = 0; i < numMin; i++) {
                HOrderItem oldItem = itemsH.get(i);
                for (int j = 0; j < dayDif; j++) {
                    OrOrderItem item = new OrOrderItem();
                    MyBeanUtil.copyProperties(item, oldItem);
                    item.setNight(DateUtil.getDate(endOld, j));
                    item.setDayIndex(daysOld + j);
                    item.setFirstNight(false);
                    item.setLastNight(daysOld + j == daysNew - 1);
                    item.setRoomIndex(i);
                    OrPriceDetail priceDetail = liPrice.get(daysOld + j);
                    item.setSalePrice(priceDetail.getSalePrice());
                    item.setBasePrice(priceDetail.getBasePrice());
                    item.setMarketPrice(priceDetail.getMarketPrice());
                    item.setOrder(order);
                    items.add(item);
                }
            }
        }
    }

    /**
     * 首次修改时计算需要扣取的配额列表,并相应用原单配额填充修改单配额
     * 
     * @author chenkeming Feb 13, 2009 11:23:28 AM
     * @param order
     * @param orderH
     * @param quotaPattern
     * @param lstQuota
     * @return
     */
    private boolean calQuotaList(OrOrder order, HOrder orderH, String quotaPattern,
        List<Object[]> lstQuota) {
        boolean bClear = false;
        Object[] objQuota = null;
        Date beginNew = order.getCheckinDate();
        Date endNew = order.getCheckoutDate();
        Date beginOld = orderH.getCheckinDate();
        Date endOld = orderH.getCheckoutDate();
        int num = order.getRoomQuantity() - orderH.getRoomQuantity();
        if (!order.getPayMethod().equals(orderH.getPayMethod())
            || order.getChildRoomTypeId().longValue() != orderH.getChildRoomTypeId().longValue()
            || 0 <= beginOld.compareTo(endNew) || 
            0 <= beginNew.compareTo(endOld)) { 
            // 支付方式或子房型不一样,或日期不重合,全部新扣配额
            objQuota = createQuotaObj(objQuota, order, beginNew, endNew, order.getRoomQuantity());
            lstQuota.add(objQuota);
        } else { // 有需要copy原单的配额到修改单的情况
            if (HotelBaseConstantBean.QUOTAPATTERNCI.equals(quotaPattern)) { // "C-I"
                if (beginOld.equals(beginNew)) { // 入住日期不变,此时才有可能copy原单配额
                    if (0 < num) { // 修改单房间数大于原单，则需要新扣配额
                        objQuota = createQuotaObj(objQuota, order, beginNew, endNew, num);
                        lstQuota.add(objQuota);
                    }

                    chgItemForCI(order, orderH);
                    bClear = true;

                } else { // 入住日期改变,修改单用不到原单配额,需要新扣首日的所有配额
                    objQuota = createQuotaObj(objQuota, order, beginNew, endNew, order
                        .getRoomQuantity());
                    lstQuota.add(objQuota);
                }
            } else { // "S-I"
                if (beginOld.after(beginNew)) { // 新入住日 旧入住日
                    objQuota = createQuotaObj(objQuota, order, beginNew, beginOld, order
                        .getRoomQuantity());
                    lstQuota.add(objQuota);
                    if (0 <= endOld.compareTo(endNew)) { // 新入住日 旧入住日 新离店日 旧离店日
                        if (0 < num) {
                            objQuota = createQuotaObj(objQuota, order, beginOld, endNew, num);
                            lstQuota.add(objQuota);
                        }
                    } else { // 新入住日 旧入住日 旧离店日 新离店日
                        if (0 < num) {
                            lstQuota.add(createQuotaObj(objQuota, order, beginOld, endOld, num));
                        }
                        objQuota = createQuotaObj(objQuota, order, endOld, endNew, num);
                        lstQuota.add(objQuota);
                    }
                } else { // 旧入住日 新入住日
                    if (0 <= endOld.compareTo(endNew)) { // 旧入住日 新入住日 新离店日 旧离店日
                        if (0 < num) {
                            lstQuota.add(createQuotaObj(objQuota, order, beginNew, endNew, num));
                        }
                    } else { // 旧入住日 新入住日 旧离店日 新离店日
                        if (0 < num) {
                            lstQuota.add(createQuotaObj(objQuota, order, beginNew, endOld, num));
                        }
                        lstQuota.add(createQuotaObj(objQuota, order, endOld, endNew, order
                            .getRoomQuantity()));
                    }
                }

                int nDaysNew = DateUtil.getDay(beginNew, endNew);
                int numOld = orderH.getRoomQuantity();
                if (0 < numOld) {
                    List<HOrderItem> liOld = orderH.getOrderItemsH();
                    int nDifBegin = DateUtil.getDay(beginOld, beginNew);
                    int nDifEnd = DateUtil.getDay(endOld, beginNew);
                    for (int j = 0; j < nDaysNew; j++) { // 遍历修改单各天,每天适当copy原单配额到修改单
                        int indexOld = j + nDifBegin; // 该天在原单是第几天
                        if (0 <= indexOld && 0 > j + nDifEnd) { // 如果该天处于原单日期范围内,则copy配额
                            if (!bClear) {
                                order.getOrderItems().clear();
                                bClear = true;
                            }
                            // 每一天,copy原单配额到修改单
                            for (int i = 0; i < numOld && i < order.getRoomQuantity(); i++) {
                                HOrderItem oldItem = liOld.get(i + indexOld * numOld);
                                OrOrderItem item = new OrOrderItem();
                                MyBeanUtil.copyProperties(item, oldItem);
                                item.setDayIndex(j);
                                item.setFirstNight(0 == j);
                                item.setLastNight(j == nDaysNew - 1);
                                item.setOrder(order);
                                order.getOrderItems().add(item);
                            }
                        }
                    }
                }
            }
        }
        return bClear;
    }

    /**
     * 根据同一个订单创建扣取配额的obj
     * 
     * @author chenkeming Feb 12, 2009 9:38:53 AM
     * @param obj
     * @param order
     * @param begin
     * @param end
     * @param num
     * @return
     */
    private Object[] createQuotaObj(Object[] obj, OrOrder order, Date begin, Date end, int num) {
        Object[] objQuota;
        if (null == obj) {
            objQuota = new Object[10];
            objQuota[0] = order.getHotelId();
            objQuota[1] = QuotaMemberType.CC;
            objQuota[2] = order.getPayMethod();
            objQuota[3] = order.getRoomTypeId();
            objQuota[4] = order.getChildRoomTypeId();
            objQuota[5] = order.getQuotaTypeOld();
            objQuota[6] = order.getBedType();
            objQuota[7] = begin;
            objQuota[8] = end;
            objQuota[9] = num;
        } else {
            objQuota = obj.clone();
            objQuota[7] = begin;
            objQuota[8] = end;
            objQuota[9] = num;
        }
        return objQuota;
    }

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
        boolean bFirst) {

        List<Object[]> lstQuota = new ArrayList<Object[]>();
        String quotaPattern = getQuotaPattern(order);
        boolean bClear = false;
        if (bFirst) {
            bClear = calQuotaList(order, orderH, quotaPattern, lstQuota);

            String sql = "select count(*) from or_quota_record qr where qr.orderId = ?";
            List li = orOrderDao.queryByNativeSQL(sql, new Object[]{order.getID()});
            int num = ((BigDecimal) li.get(0)).intValue();
            if (0 >= num) {
                List<HOrderItem> itemsH = orderH.getOrderItemsH();
                if (!itemsH.isEmpty()) {
                    if (HotelBaseConstantBean.QUOTAPATTERNCI.equals(quotaPattern)) { // C-I
                        int oldQuantity = orderH.getRoomQuantity();
                        for (int i = 0; i < oldQuantity; i++) {
                            HOrderItem item = itemsH.get(i);
                            OrQuotaRecord record = new OrQuotaRecord();
                            MyBeanUtil.copyProperties(record, item);
                            record.setPayMethod(orderH.getPayMethod());
                            record.setRoomTypeId(orderH.getRoomTypeId());
                            record.setChildRoomTypeId(orderH.getChildRoomTypeId());
                            record.setID(null);
                            record.setHisNo(orderH.getHisNo());
                            record.setOrderId(order.getID());
                            record.setQuotaTypeOld(orderH.getQuotaTypeOld());
                            saveOrUpdate(record);
                        }
                    } else { // S-I
                        for (HOrderItem item : itemsH) {
                            OrQuotaRecord record = new OrQuotaRecord();
                            MyBeanUtil.copyProperties(record, item);
                            record.setPayMethod(orderH.getPayMethod());
                            record.setRoomTypeId(orderH.getRoomTypeId());
                            record.setChildRoomTypeId(orderH.getChildRoomTypeId());
                            record.setID(null);
                            record.setHisNo(orderH.getHisNo());
                            record.setOrderId(order.getID());
                            record.setQuotaTypeOld(orderH.getQuotaTypeOld());
                            saveOrUpdate(record);
                        }
                    }
                }
            }
        } else { // 不是第一次修改
            String hql;
            Object[] obj = null;
            List<OrQuotaRecord> li = null;
            if (HotelBaseConstantBean.QUOTAPATTERNCI.equals(quotaPattern)) { // C-I
                hql = "from OrQuotaRecord as c where c.orderId = ? "
                    + " and c.payMethod = ? and c.childRoomTypeId = ? "
                    + " and c.quotaPattern = ? and c.night = ? order by c.hisNo";
                li = orOrderDao.query(hql, new Object[] { order.getID(), order.getPayMethod(), order.getChildRoomTypeId(), quotaPattern, order.getCheckinDate() });
                int nSize = li.size();
                int num = order.getRoomQuantity() - nSize;
                if (0 < num) {
                    obj = createQuotaObj(obj, order, order.getCheckinDate(), order
                        .getCheckoutDate(), num);
                    lstQuota.add(obj);
                }

                if (0 < nSize) { // 如果可以用到历史单的配额
                    Date begin = order.getCheckinDate();

                    if (!order.getPayMethod().equals(orderH.getPayMethod())
                        || order.getChildRoomTypeId().longValue() != orderH.getChildRoomTypeId()
                            .longValue() || !orderH.getCheckinDate().
                            equals(begin)) {
                        // 支付方式或子房型不一样,或入住日期不一样,原单配额不保留
                        List<OrOrderItem> items = order.getOrderItems();
                        items.clear();
                        bClear = true;

                        List<OrPriceDetail> liPrice = order.getPriceList();
                        int numNew = order.getRoomQuantity();
                        int nMin = nSize <= numNew ? nSize : numNew;
                        int daysNew = DateUtil.getDay(begin, order.getCheckoutDate());
                        for (int i = 0; i < nMin; i++) {
                            OrQuotaRecord oldItem = li.get(i);
                            for (int j = 0; j < daysNew; j++) {
                                OrOrderItem item = new OrOrderItem();

                                MyBeanUtil.copyProperties(item, oldItem);
                                item.setID(null);
                                item.setNight(DateUtil.getDate(begin, j));
                                item.setDayIndex(j);
                                item.setFirstNight(0 == j);
                                item.setLastNight(j == daysNew - 1);
                                item.setRoomIndex(i);
                                item.setOrderItemsType(OrderItemType.NORMAL);
                                // TODO: 这里的价格在少数情况下仍可能没取已有的历史订单的
                                OrPriceDetail priceDetail = liPrice.get(j);
                                item.setSalePrice(priceDetail.getSalePrice());
                                item.setBasePrice(priceDetail.getBasePrice());
                                item.setMarketPrice(priceDetail.getMarketPrice());
                                item.setOrder(order);
                                items.add(item);
                            }
                        }
                    } else {
                        Date endOld = orderH.getCheckoutDate();
                        int nDays = DateUtil.getDay(begin, order.getCheckoutDate());
                        // 先删除相应item
                        List<OrOrderItem> items = order.getOrderItems();
                        List<OrOrderItem> liDel = new ArrayList<OrOrderItem>();
                        int numOld = orderH.getRoomQuantity();
                        int daysOld = DateUtil.getDay(begin, endOld);
                        if (numOld > nSize) {
                            for (int i = 0; i < daysOld; i++) {
                                int nBase = i * numOld;
                                int nDif = numOld - nSize;
                                for (int j = 0; j < nDif; j++) {
                                    liDel.add(items.get(nBase + nSize + j));
                                }
                            }
                        }
                        if (daysOld > nDays) {
                            int dayDif = daysOld - nDays;
                            for (int i = 0; i < dayDif; i++) {
                                int nBase = (i + nDays) * numOld;
                                for (int j = 0; j < numOld; j++) {
                                    liDel.add(items.get(nBase + j));
                                }
                            }
                        }
                        for (OrOrderItem item : liDel) {
                            items.remove(item);
                        }
                        // 再添加
                        if (nDays > daysOld) {
                            int dayDif = nDays - daysOld;
                            int numMin = numOld < nSize ? numOld : nSize;
                            List<HOrderItem> itemsH = orderH.getOrderItemsH();
                            List<OrPriceDetail> liPrice = order.getPriceList();
                            for (int i = 0; i < numMin; i++) {
                                HOrderItem oldItem = itemsH.get(i);
                                for (int j = 0; j < dayDif; j++) {
                                    OrOrderItem item = new OrOrderItem();
                                    MyBeanUtil.copyProperties(item, oldItem);
                                    item.setNight(DateUtil.getDate(endOld, j));
                                    item.setDayIndex(daysOld + j);
                                    item.setFirstNight(false);
                                    item.setLastNight(daysOld + j == nDays - 1);
                                    item.setRoomIndex(i);
                                    OrPriceDetail priceDetail = liPrice.get(daysOld + j);
                                    item.setSalePrice(priceDetail.getSalePrice());
                                    item.setBasePrice(priceDetail.getBasePrice());
                                    item.setMarketPrice(priceDetail.getMarketPrice());
                                    item.setOrder(order);
                                    items.add(item);
                                }
                            }
                        }
                        if (nSize > numOld && num + nSize > numOld) {
                            int dayDif = nDays - daysOld;
                            int numDif = nSize - numOld;
                            numDif = numDif <= (num + nSize - numOld) ? numDif
                                : (num + nSize - numOld);
                            List<OrPriceDetail> liPrice = order.getPriceList();
                            for (int i = 0; i < numDif; i++) {
                                OrQuotaRecord oldItem = li.get(numOld + i);
                                for (int j = 0; j < dayDif; j++) {
                                    OrOrderItem item = new OrOrderItem();
                                    MyBeanUtil.copyProperties(item, oldItem);
                                    item.setID(null);
                                    item.setNight(DateUtil.getDate(endOld, j));
                                    item.setDayIndex(daysOld + j);
                                    item.setFirstNight(false);
                                    item.setLastNight(daysOld + j == nDays - 1);
                                    item.setRoomIndex(i);
                                    item.setOrderItemsType(OrderItemType.NORMAL);
                                    OrPriceDetail priceDetail = liPrice.get(daysOld + j);
                                    item.setSalePrice(priceDetail.getSalePrice());
                                    item.setBasePrice(priceDetail.getBasePrice());
                                    item.setMarketPrice(priceDetail.getMarketPrice());
                                    item.setOrder(order);
                                    items.add(item);
                                }
                            }
                        }
                        bClear = true;
                    }
                } else {
                    order.getOrderItems().clear();
                    bClear = true;
                }
            } else {
                hql = "from OrQuotaRecord as c where c.orderId=" + order.getID()
                    + " and c.payMethod='" + order.getPayMethod() + "' and c.childRoomTypeId="
                    + order.getChildRoomTypeId()
                    + " and c.night>=? and c.night<? order by c.night,c.hisNo";
                Date begin = order.getCheckinDate();
                Date end = order.getCheckoutDate();
                li = orOrderDao.query(hql, new Object[] { begin, end });
                int nSize = li.size();
                if (0 < nSize) {
                    order.getOrderItems().clear();
                    bClear = true;
                    int dayIndex = 0;
                    int nDays = DateUtil.getDay(begin, end);
                    int[] totals = new int[nDays];
                    Date tmpBegin = null;
                    int roomIndex = 0;
                    for (int i = 0; i < nSize; i++) {
                        OrQuotaRecord record = li.get(i);
                        int curDayIndex = DateUtil.getDay(begin, record.getNight());
                        if (0 == i) {
                            dayIndex = curDayIndex;
                        } else if (curDayIndex > dayIndex) {
                            roomIndex = 0;
                        }
                        totals[curDayIndex]++;

                        OrOrderItem item = new OrOrderItem();
                        MyBeanUtil.copyProperties(item, record);
                        item.setID(null);
                        item.setDayIndex(curDayIndex);
                        item.setFirstNight(0 == curDayIndex);
                        item.setLastNight(i == nDays - 1);
                        item.setRoomIndex(roomIndex++);
                        item.setOrderItemsType(OrderItemType.NORMAL);
                        item.setOrder(order);
                        order.getOrderItems().add(item);
                    }

                    dayIndex = 0;
                    int curNum = 0;
                    int num = totals[dayIndex++];
                    curNum = num;
                    tmpBegin = begin;
                    int quantity = order.getRoomQuantity();
                    while (dayIndex < nDays) {
                        curNum = totals[dayIndex];
                        while (curNum == num && dayIndex < nDays - 1) {
                            curNum = totals[++dayIndex];
                        }
                        if (curNum != num) {
                            if (quantity > num) {
                                lstQuota.add(createQuotaObj(obj, order, tmpBegin, DateUtil.getDate(
                                    begin, dayIndex), quantity - num));
                            }
                            tmpBegin = DateUtil.getDate(begin, dayIndex);
                            num = curNum;
                        }
                        dayIndex++;
                    }
                    if (curNum == num && quantity > num) {
                        lstQuota.add(createQuotaObj(obj, order, tmpBegin, end, quantity - num));
                    }
                } else {
                    lstQuota.add(createQuotaObj(obj, order, begin, end, order.getRoomQuantity()));
                }
            }
        }

        // 扣配额
        if (0 < lstQuota.size()) {
            QuotaQuery quotaQuery = new QuotaQuery();
            boolean quotaCanReturn = true;
            boolean bSetCanReturn = false;

            Date beginNew = order.getCheckinDate();
            int nTotalDays = DateUtil.getDay(beginNew, order.getCheckoutDate());

            for (Object[] obj : lstQuota) {
            	quotaQuery.setHotelId((Long) obj[0]);
            	quotaQuery.setMemberType((Integer) obj[1]);
            	quotaQuery.setPayMethod((String) obj[2]);
                quotaQuery.setRoomTypeId((Long) obj[3]);
                quotaQuery.setChildRoomId((Long) obj[4]);
                quotaQuery.setQuotaType((String) obj[5]);
                quotaQuery.setBedID(((Integer) obj[6]).longValue());
                quotaQuery.setBeginDate((Date) obj[7]);
                quotaQuery.setEndDate((Date) obj[8]);
                quotaQuery.setQuotaNum((Integer) obj[9]);
                quotaQuery.setOperatorName(order.getCreatorName());
                quotaQuery.setChannel(Long.valueOf(order.getChannel()));
                quotaQuery.setOperatorId(order.getCreator());
                List<QuotaReturn> quotas = quotaControl.deductQuota(quotaQuery);
                if (0 >= quotas.size()) {
                    continue;
                }

                if (!bClear) {
                    order.getOrderItems().clear();
                    bClear = true;
                }

                // 填充orderItems,这里quotas依次按日期，房间号排序
                Date tonight = null;
                int roomIndex = -1;
                int dayIndex = DateUtil.getDay(order.getCheckinDate(), quotaQuery.getBeginDate()) - 1;
                String[] sQuotaTypes = new String[order.getRoomQuantity()];
                // String[] fellowNamesArr = fillFellowNamesToOrderItem(order); //每天各个房间的入住人姓名数组
                // v2.4.2 chenjiajie 2008-12-30
                for (int i = 0; i < quotas.size(); i++) {
                    QuotaReturn quotaReturn = quotas.get(i);
                    OrOrderItem orderItem = new OrOrderItem();

                    // 设置历史序号
                    orderItem.setHisNo(Long.valueOf(order.getHisNo()));

                    Date curDate = quotaReturn.getQuotaDate();
                    if (null == tonight || 0 < DateUtil.compare(tonight,curDate)) {
                        // 如果是下一天
                        tonight = curDate;
                        roomIndex = order.getRoomQuantity() - quotaQuery.getQuotaNum();
                        dayIndex++;
                    } else {
                        roomIndex++;
                    }
                    // orderItem.setFellowName(fellowNamesArr[roomIndex]); //
                    // 把拆分后的入住人姓名封装到orderItem中 V2.4.2 chenjiajie 2008-12-30
                    orderItem.setRoomIndex(roomIndex);
                    orderItem.setDayIndex(dayIndex);
                    orderItem.setFirstNight(0 == dayIndex);
                    orderItem.setLastNight(dayIndex >= nTotalDays - 1);
                    orderItem.setNight(curDate);

                    String returnQuotaType = quotaReturn.getQuotaType();
                    String returnQuotaPattern = quotaReturn.getQuotaPattern();
                    if (HotelBaseConstantBean.QUOTAPATTERNCI.equals(returnQuotaPattern)) { // 进店模式
                        if (0 == dayIndex) { // 首天
                            sQuotaTypes[roomIndex] = returnQuotaType;
                        }
                        if (null == sQuotaTypes[roomIndex]
                            || sQuotaTypes[roomIndex].equals(QuotaType.CALLQUOTA)) {
                            orderItem.setConfirm(false);
                        } else {
                            orderItem.setConfirm(true);
                        }
                    } else {
                        // 呼出配额一律为获取不成功
                        if (null == returnQuotaType || 
                            returnQuotaType.equals(QuotaType.CALLQUOTA)) {
                            orderItem.setConfirm(false);
                        } else {
                            int nSign = quotaReturn.getSign();
                            orderItem.setConfirm(0 == nSign);
                        }
                    }
                    //呼出配额设置成false
                    if(QuotaType.CALLQUOTA.equals(returnQuotaType)){
                        orderItem.setConfirm(false);
                   }
                    orderItem.setQuantity(quotaReturn.getQuotaNum());
                    orderItem.setQuotaType(returnQuotaType);
                    orderItem.setBasePrice(quotaReturn.getBasePrice());
                    orderItem.setSalePrice(quotaReturn.getSalePrice());
                    orderItem.setMarketPrice(quotaReturn.getSalesroomPrice());
                    orderItem.setRoomState(quotaReturn.getRoomState());
                    orderItem.setQuotaPattern(quotaReturn.getQuotaPattern());
                    orderItem.setQuotaholder("CC");
                    orderItem.setQuotaType(quotaReturn.getQuotaType());
                    orderItem.setQuotashare(quotaReturn.getQuotaShare());
                    
                    orderItem.setHotelId(order.getHotelId());
                    if (null != breakfast) {
                        orderItem.setBreakfast(breakfast[dayIndex]);
                        orderItem.setBreakfastNum(breakfastNum[dayIndex]);
                    } else {
                        orderItem.setBreakfast(0);
                        orderItem.setBreakfastNum(0);
                    }
                    orderItem.setQuotaPattern(quotaReturn.getQuotaPattern());

                    orderItem.setOrderItemsType(OrderItemType.NORMAL);
                    orderItem.setOrder(order);

                    order.getOrderItems().add(orderItem);

                    // 系统内配额如果不可退，设置不可退标志
                    if (!bSetCanReturn
                        && !quotaReturn.isTakebackQuota()
                        && (null != returnQuotaType &&
                            !returnQuotaType.equals(QuotaType.CALLQUOTA))) {
                        orderItem.setQuotaCantReturn(true);
                        quotaCanReturn = false;
                        bSetCanReturn = true;
                    }
                    //呼出配额设置成false
                    if(QuotaType.CALLQUOTA.equals(returnQuotaType)){
                        orderItem.setConfirm(false);
                   }
                    // 往配额记录表添加记录
                    boolean bAddRecord = HotelBaseConstantBean.
                    QUOTAPATTERNCI.equals(quotaPattern) ? (0 == dayIndex)
                        : true;
                    if (bAddRecord) {
                        OrQuotaRecord record = new OrQuotaRecord();
                        MyBeanUtil.copyProperties(record, orderItem);
                        record.setPayMethod(order.getPayMethod());
                        record.setRoomTypeId(order.getRoomTypeId());
                        record.setChildRoomTypeId(order.getChildRoomTypeId());
                        record.setID(null);
                        record.setOrderId(order.getID());
                        record.setQuotaTypeOld(order.getQuotaTypeOld());
                        saveOrUpdate(record);
                    }
                }
            }
            order.setQuotaCanReturn(quotaCanReturn);
        } else {
            order.setQuotaCanReturn(true);
        }

        Collections.sort(order.getOrderItems(), compOrderItem);

        calculateTotalAmount(order);

        // 获取预订条款信息
        getReservationInfo(order, true);

        saveOrUpdate(orderH);
        orOrderDao.updateOrder(order);
    }

    /**
     * 插入或更新历史订单
     * 
     * @param orderH
     */
    public Serializable saveOrUpdate(HOrder orderH) {
        if (null == orderH.getHisID()) {
            return orOrderDao.save(orderH);
        } else {
            orOrderDao.saveOrUpdate(orderH);
            return null;
        }
    }

    /**
     * 插入或更新配额记录
     * 
     * @param record
     */
    private Serializable saveOrUpdate(OrQuotaRecord record) {
        if (null == record.getID()) {
            record.setCreateTime(new Date());
            return orOrderDao.save(record);
        } else {
            orOrderDao.saveOrUpdate(record);
            return null;
        }
    }

    /**
     * 计算订单总金额并设置OrReservation的首日金额字段<br>
     * 同时设置订单的quotaOk,quotaCanReturn字段<br>
     * 面付单同时更新orderitem的入住人信息
     * 
     * @param order
     */
    private void calculateTotalAmount(OrOrder order) {
        List roomItems = order.getOrderItems();

        boolean bQuotaOk = true;
        boolean quotaCanReturn = order.isQuotaCanReturn();
        double amount = 0.0;
        double firstPrice = 0.0;
        int nSize = roomItems.size();
        boolean bPay = !order.isPrepayOrder();
        String[] fellowNames = null;
        if (bPay) {
            fellowNames = OrderUtil.fillFellowNamesToOrderItem(order);
        }
        for (int m = 0; m < nSize; m++) {
            OrOrderItem item = (OrOrderItem) roomItems.get(m);
            double price = item.getSalePrice();
            amount += price;
            if (item.isFirstNight()) {
                firstPrice += price;
            }

            if (!item.isConfirm()) {
                bQuotaOk = false;
            }

            if (quotaCanReturn && item.isQuotaCantReturn()) {
                quotaCanReturn = false;
            }

            if (bPay) {
                item.setFellowName(fellowNames[item.getRoomIndex()]);
            }
        }
        order.setQuotaOk(bQuotaOk);
        order.setQuotaCanReturn(quotaCanReturn);

        order.setSum(amount);
        order.setSumRmb(BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(order.getRateId()))
            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        OrReservation reserv = order.getReservation();
        if (null != reserv) {
            reserv.setFirstPrice(OrderUtil.roundPrice(order.getRateId() * firstPrice));
        }
    }

    /**
     * 创建退配额元素
     * 
     * @author chenkeming Feb 17, 2009 6:07:28 PM
     * @param li
     * @param start
     * @param end
     * @param maps
     * @param order
     * @param bSameChildRoom
     * @return
     */
    private Object[] createReturnQuota(List<Object[]> li, int start, int end,
        Map<String, Integer> maps, OrOrder order, boolean bSameChildRoom) {
        Object[] tmpObj = li.get(start);
        Date tmpBegin = (Date) tmpObj[2];
        int num = ((BigDecimal) tmpObj[3]).intValue();
        Date tmpEnd = DateUtil.getDate(tmpBegin);

        Date checkIn = order.getCheckinDate();
        Date checkOut = order.getCheckoutDate();

        boolean bInOrder = bSameChildRoom && 0 <= DateUtil.getDay(checkIn,tmpBegin)
            && 0 > DateUtil.getDay(checkOut,tmpBegin);
        if (bInOrder) {
            String key = tmpObj[0].toString() + tmpObj[1] + DateUtil.dateToString(tmpBegin);
            Object val = maps.get(key);
            if (null != val) {
                int numVal = ((Integer) val).intValue();
                if (0 > numVal) {
                    num += numVal;
                    maps.put(key, num);
                }
            } else {
                num -= order.getRoomQuantity();
                maps.put(key, num);
            }
        }

        for (int i = start + 1; i < end; i++) {
            tmpObj = li.get(i);
            Date curDay = (Date) tmpObj[2];
            int curNum = ((BigDecimal) tmpObj[3]).intValue();

            bInOrder = bSameChildRoom && 0 <= DateUtil.getDay(checkIn,curDay)
                && 0 > DateUtil.getDay(checkOut,curDay);
            String key = tmpObj[0].toString() + tmpObj[1] + DateUtil.dateToString(curDay);
            if (bInOrder) {
                Object val = maps.get(key);
                if (null != val) {
                    int numVal = ((Integer) val).intValue();
                    if (0 > numVal) {
                        curNum += numVal;
                        if (curNum != num || 1 < DateUtil.getDay(tmpEnd,curDay)) {
                            break;
                        }
                        maps.put(key, curNum);
                    }
                } else {
                    curNum -= order.getRoomQuantity();
                    if (curNum != num || 1 < DateUtil.getDay(tmpEnd,curDay)) {
                        break;
                    }
                    maps.put(key, curNum);
                }
            }

            if (curNum != num || 1 < DateUtil.getDay(tmpEnd,curDay)) {
                break;
            }
            tmpEnd = curDay;
        }
        Object[] obj = new Object[7];
        obj[0] = tmpBegin;
        obj[1] = DateUtil.getDate(tmpEnd, 1);
        obj[2] = num;
        obj[3] = tmpObj[0];
        obj[4] = tmpObj[1];
        obj[5] = tmpObj[4];
        obj[6] = tmpObj[5];
        return obj;
    }

    /**
     * 创建退配额元素,无需考虑和order重合的情况
     * 
     * @author chenkeming Feb 16, 2009 1:46:59 PM
     * @param li
     * @param start
     * @param end
     * @param order
     * @return
     */
    private Object[] createReturnQuota(List<Object[]> li, int start, int end, OrOrder order) {
        Object[] tmpObj = li.get(start);
        Date tmpBegin = (Date) tmpObj[2];
        int num = ((BigDecimal) tmpObj[3]).intValue();
        Date tmpEnd = DateUtil.getDate(tmpBegin);
        for (int i = start + 1; i < end; i++) {
            tmpObj = li.get(i);
            Date curDay = (Date) tmpObj[2];
            int curNum = ((BigDecimal) tmpObj[3]).intValue();
            if (curNum != num || 1 < DateUtil.getDay(tmpEnd,curDay)) {
                break;
            }
            tmpEnd = curDay;
        }
        Object[] obj = new Object[7];
        obj[0] = tmpBegin;
        obj[1] = DateUtil.getDate(tmpEnd, 1);
        obj[2] = num;
        obj[3] = tmpObj[0];
        obj[4] = tmpObj[1];
        obj[5] = tmpObj[4];
        obj[6] = tmpObj[5];
        return obj;
    }

    /**
     * 获取相同支付方式和子房型的最大下标
     * 
     * @author chenkeming
     * @param li
     * @param payMethod
     * @param childRoom
     * @param quotaType
     * @param start
     * @return
     */
    private int getChildRoomIndex(List<Object[]> li, String payMethod, long childRoom,
        String quotaType, int start) {
        int i;
        for (i = start; i < li.size(); i++) {
            Object[] obj = li.get(i);
            String curPayMethod = (String) obj[0];
            if (!curPayMethod.equals(payMethod)) {
                return i;
            }
            long curChildRoom = ((BigDecimal) obj[1]).longValue();
            if (curChildRoom != childRoom) {
                return i;
            }
            String curQuotaType = (String) obj[4];
            if (!curQuotaType.equals(quotaType)) {
                return i;
            }
        }
        return i;
    }

    /**
     * 修改单确认酒店
     * 
     * @author chenkeming Feb 13, 2009 9:05:48 AM
     * @param orderId
     * @param confirmWay
     *            1:fax, 2:tel
     * @param roleUser
     */
    public void confirmHotel(long orderId, int confirmWay, UserWrapper roleUser) {
        OrOrder order = orOrderDao.getOrder(Long.valueOf(orderId));
        order.setHotelConfirm(true);
        if (1 == confirmWay) {
            order.setHotelConfirmFax(true);
        } else {
            order.setHotelConfirmTel(true);
        }

        // 获取要退的配额
        Map<String, Integer> maps = new HashMap<String, Integer>();
        List<Object[]> li = null;
        String hql;
        String quotaPattern = getQuotaPattern(order);
        List<Object[]> lstQuota = new ArrayList<Object[]>();
        if (HotelBaseConstantBean.QUOTAPATTERNCI.equals(quotaPattern)) { // "C-I"
            hql = "select qr.payMethod,qr.childRoomTypeId,qr.night," 
            	+ "(case when (payMethod != ? or childRoomTypeId != ? "
                + " or night = ?) then sum(qr.quantity)" + " else " + "     sum(qr.quantity)"
                + " end), quotaTypeOld, roomTypeId  " + " from or_quota_record qr "
                + " where qr.orderId = ? "
                + " group by qr.payMethod, roomTypeId, qr.childRoomTypeId, quotaTypeOld, qr.night"
                + " order by qr.payMethod, roomTypeId, qr.childRoomTypeId, quotaTypeOld, qr.night";
            li = orOrderDao.query(hql, new Object[] { order.getPayMethod(), order.getChildRoomTypeId(), 
            		order.getCheckinDate(), order.getID() });
            for (Object[] tmpObj : li) {
                Integer nNum = ((BigDecimal) tmpObj[3]).intValue();

                String payMethod = tmpObj[0].toString();
                long childRoomType = ((BigDecimal) tmpObj[1]).longValue();
                Date tmpBegin = (Date) tmpObj[2];

                if (order.getPayMethod().equals(payMethod)
                    && order.getChildRoomTypeId().longValue() == childRoomType
                    && 0 <= DateUtil.getDay(order.getCheckinDate(),tmpBegin)
                    && 0 > DateUtil.getDay(order.getCheckoutDate(),tmpBegin)) {
                    String key = payMethod + childRoomType
                        + DateUtil.dateToString((Date) tmpObj[2]);
                    Object val = maps.get(key);
                    if (null != val) {
                        int numVal = ((Integer) val).intValue();
                        if (0 > numVal) {
                            nNum += numVal;
                            maps.put(key, nNum);
                        } else {
                            nNum = -999;
                        }
                    } else {
                        nNum -= order.getRoomQuantity();
                        maps.put(key, nNum);
                    }
                }

                if (0 < nNum.intValue()) {
                    Object[] obj = new Object[7];
                    obj[0] = tmpBegin;
                    obj[1] = DateUtil.getDate(tmpBegin, 1);
                    obj[2] = nNum;
                    obj[3] = tmpObj[0];
                    obj[4] = tmpObj[1];
                    obj[5] = tmpObj[4];
                    obj[6] = tmpObj[5];
                    lstQuota.add(obj);
                }
            }
        } else { // "S-I"

            hql = "select qr.payMethod,qr.childRoomTypeId,qr.night," + "(case when (payMethod != ? or childRoomTypeId != ? " 
            	+ " or night < ? or night >= ?) then sum(qr.quantity)" + " else "
                + "     sum(qr.quantity)" + " end), quotaTypeOld, roomTypeId "
                + " from or_quota_record qr " + " where qr.orderId = ? "
                + " group by qr.payMethod, roomTypeId, qr.childRoomTypeId, quotaTypeOld, qr.night"
                + " order by qr.payMethod, roomTypeId, qr.childRoomTypeId, quotaTypeOld, qr.night";
            Date begin = order.getCheckinDate();
            Date end = order.getCheckoutDate();
            li = orOrderDao.query(hql, new Object[] { order.getPayMethod(), order.getChildRoomTypeId(), 
            		begin, end, order.getID() });
            int nSize = li.size();
            if (0 < nSize) {
                int index = 0;
                int indexEnd;
                String payMethod = "";
                long childRoom = 0;
                String quotaType = "kkk";
                for (Object[] curObj : li) {
                    String curPayMethod = (String) curObj[0];
                    long curChildRoom = ((BigDecimal) curObj[1]).longValue();
                    String curQuotaType = (String) curObj[4];
                    if (curPayMethod.equals(payMethod) && curChildRoom == childRoom
                        && curQuotaType.equals(quotaType)) {
                        index++;
                        continue;
                    }
                    indexEnd = getChildRoomIndex(li, curPayMethod, curChildRoom, curQuotaType,
                        index);
                    payMethod = curPayMethod;
                    childRoom = curChildRoom;
                    quotaType = curQuotaType;

                    Object[] tmpObj = li.get(index);
                    Date day = (Date) tmpObj[2];
                    int num = -99999;
                    boolean bSameChildRoom = order.getPayMethod().equals(payMethod)
                        && order.getChildRoomTypeId().longValue() == childRoom;
                    for (int i = index; i < indexEnd; i++) {
                        tmpObj = li.get(i);
                        int curNum = ((BigDecimal) tmpObj[3]).intValue();
                        Date curDay = (Date) tmpObj[2];

                        boolean bInOrder = bSameChildRoom
                            && 0 <= DateUtil.getDay(order.getCheckinDate(),curDay)
                            && 0 > DateUtil.getDay(order.getCheckoutDate(),curDay);
                        String key = null;
                        if (bInOrder) {
                            key = tmpObj[0].toString() + tmpObj[1] + DateUtil.dateToString(curDay);
                            Object val = maps.get(key);
                            if (null != val) {
                                int numVal = ((Integer) val).intValue();
                                if (0 > numVal) {
                                    curNum += numVal;
                                } else {
                                    curNum = -999;
                                }
                            } else {
                                curNum -= order.getRoomQuantity();
                            }
                        }

                        if (curNum == num && 1 == DateUtil.getDay(day,curDay)) {
                            day = curDay;
                            continue;
                        }
                        if (0 < curNum) {
                            lstQuota.add(createReturnQuota(li, i, indexEnd, maps, order,
                                bSameChildRoom));
                        }
                        day = curDay;
                        num = curNum;
                    }

                    index++;
                }
            }

        }

        // 退配额
        /**
        if (!lstQuota.isEmpty()) {
            QuotaQuery queryPo = new QuotaQueryPo();
            queryPo.setHotelId(order.getHotelId());
            queryPo.setMemberType(QuotaMemberType.CC);
            queryPo.setBedID(order.getBedType());
            for (Object[] obj : lstQuota) {
                queryPo.setQuotaType((String) obj[5]);
                queryPo.setRoomTypeId(((BigDecimal) obj[6]).longValue());
                queryPo.setChildRoomId(((BigDecimal) obj[4]).longValue());
                queryPo.setPayMethod((String) obj[3]);
                queryPo.setBeginDate((Date) obj[0]);
                queryPo.setEndDate((Date) obj[1]);
                queryPo.setQuotaNum((Integer) obj[2]);
                quotaForCCService.returnQuota(queryPo);
            }
        }
        **/
        List<OrOrderItem> orderItems = order.getOrderItems();
        if(orderItems!=null&&orderItems.size()>0){
        	List<QuotaReturn> quotaReturnList = new ArrayList<QuotaReturn>();
	        for(OrOrderItem orderItem : orderItems){
	        	QuotaReturn quotaReturn = new QuotaReturn();
	        	quotaReturn.setHotelId(order.getHotelId());
	        	quotaReturn.setRoomTypeId(order.getRoomTypeId());
	        	quotaReturn.setBedId(order.getBedType());
	        	quotaReturn.setChildRoomTypeId(order.getChildRoomTypeId());
	        	quotaReturn.setQuotaNum(1);
	        	quotaReturn.setUseQuotaDate(orderItem.getNight());
	        	quotaReturn.setQuotaDate(orderItem.getNight());
	        	quotaReturn.setPayMethod(order.isPayToPrepay() ? PayMethod.PAY : order.getPayMethod());
	        	// CC的memberType为1
	        	quotaReturn.setMemberType(1);
	        	// 设置配额类型
	        	if(orderItem.getQuotaPattern()==null){
	        		quotaReturn.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNSI);
	        	}else{
	        		quotaReturn.setQuotaPattern(orderItem.getQuotaPattern());
	        	}
	        	// 设置配额类型
	        	if(orderItem.getQuotaType()==null){
	        		quotaReturn.setQuotaType(HotelBaseConstantBean.GENERALQUOTA);
	        	}else{
	        		quotaReturn.setQuotaType(orderItem.getQuotaType());
	        	}
	        	// 设置配额共享方式
	        	if(null==orderItem.getQuotashare()){
	        		quotaReturn.setQuotaShare(new Long(HotelBaseConstantBean.QUOTASHARETYPE));
	        	}else{
	        		quotaReturn.setQuotaShare(new Long(orderItem.getQuotashare()));
	        	}
	        	if(orderItem.getIsConfirm()==false){
	        		//配额不可退
	        		quotaReturn.setSign(1);
	        	}else{
	        		//配额可退
	        		quotaReturn.setSign(0);
	        	}
	        	quotaReturn.setOperatorName(roleUser.getName());
	        	quotaReturn.setOperatorId(roleUser.getLoginName());
	        	quotaReturnList.add(quotaReturn);
	        }
	        quotaControl.returnQuota(quotaReturnList);
        }

        // 删掉该订单的历史配额记录
        hql = " delete OrQuotaRecord where orderId = " + order.getID();
        orOrderDao.doUpdateBatch(hql, null);

        // 设置所有历史订单不可恢复
        hql = "update HOrder set hisCanResume=0 where hisValid = 1 and orderId= " + order.getID();
        orOrderDao.doUpdateBatch(hql, null);

        // 设置其后所有历史金额为无效
        hql = "update OrOrderMoney set valid=0 where orderId = " + order.getID() + " and hisNo > "
            + order.getHisNo();
        orOrderDao.doUpdateBatch(hql, null);

        // 添加日志
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName());
        handleLog.setBeforeState(order.getOrderState());
        handleLog.setAfterState(order.getOrderState());
        handleLog.setContent("修改单" + (1 == confirmWay ? "书面" : "口头") + "确认酒店<br>");
        handleLog.setModifiedTime(new Date());
        handleLog.setHisNo(order.getHisNo());
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);

        orOrderDao.saveOrUpdate(order);
    }

    /**
     * 根据查询的结果集li创建退配额列表lstQuota,无需考虑和order重合的情况
     * 
     * @author chenkeming Feb 16, 2009 1:51:29 PM
     * @param li
     * @param lstQuota
     * @param order
     */
    private void createReturnQuotas(List<Object[]> li, List<Object[]> lstQuota, OrOrder order) {

        int index = 0;
        int indexEnd;
        String payMethod = "";
        long childRoom = 0;
        String quotaType = "kkk";
        for (Object[] curObj : li) {
            String curPayMethod = (String) curObj[0];
            long curChildRoom = ((BigDecimal) curObj[1]).longValue();
            String curQuotaType = (String) curObj[4];
            if (curPayMethod.equals(payMethod) && curChildRoom == childRoom
                && curQuotaType.equals(quotaType)) {
                continue;
            }
            indexEnd = getChildRoomIndex(li, curPayMethod, curChildRoom, curQuotaType, index);
            payMethod = curPayMethod;
            childRoom = curChildRoom;
            quotaType = curQuotaType;

            Object[] tmpObj = li.get(index);
            Date day = (Date) tmpObj[2];
            int num = -99999;
            for (int i = index; i < indexEnd; i++) {
                tmpObj = li.get(i);
                int curNum = ((BigDecimal) tmpObj[3]).intValue();
                Date curDay = (Date) tmpObj[2];

                if (curNum == num && 1 == DateUtil.getDay(day,curDay)) {
                    day = curDay;
                    continue;
                }
                if (0 < curNum) {
                    lstQuota.add(createReturnQuota(li, i, indexEnd, order));
                }
                day = curDay;
                num = curNum;
            }

            index = indexEnd;
        }
    }

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
        String guestCancelMessage, UserWrapper roleUser) {

        // 获取要退的配额
        List<Object[]> li = null;
        String hql;
        String quotaPattern = getQuotaPattern(order);
        List<Object[]> lstQuota = new ArrayList<Object[]>();
        if (HotelBaseConstantBean.QUOTAPATTERNCI.equals(quotaPattern)) { // "C-I"
            hql = "select qr.payMethod,qr.childRoomTypeId,qr.night,"
                + "sum(qr.quantity), quotaTypeOld, roomTypeId from or_quota_record qr "
                + " where qr.orderId = ? " 
                + " group by qr.payMethod, roomTypeId, qr.childRoomTypeId, quotaTypeOld, qr.night"
                + " order by qr.payMethod, roomTypeId, qr.childRoomTypeId, quotaTypeOld, qr.night";
            li = orOrderDao.query(hql, new Object[]{order.getID()});
            for (Object[] tmpObj : li) {
                Integer nNum = ((BigDecimal) tmpObj[3]).intValue();
                Date tmpBegin = (Date) tmpObj[2];
                if (0 < nNum.intValue()) {
                    Object[] obj = new Object[7];
                    obj[0] = tmpBegin;
                    obj[1] = DateUtil.getDate(tmpBegin, 1);
                    obj[2] = nNum;
                    obj[3] = tmpObj[0];
                    obj[4] = tmpObj[1];
                    obj[5] = tmpObj[4];
                    obj[6] = tmpObj[5];
                    lstQuota.add(obj);
                }
            }
        } else { // "S-I"
            hql = "select qr.payMethod,qr.childRoomTypeId,qr.night,"
                + "sum(qr.quantity), quotaTypeOld, roomTypeId from or_quota_record qr "
                + " where qr.orderId = "
                + " group by qr.payMethod, roomTypeId, qr.childRoomTypeId, quotaTypeOld, qr.night"
                + " order by qr.payMethod, roomTypeId, qr.childRoomTypeId, quotaTypeOld, qr.night";
            li = orOrderDao.query(hql, new Object[]{order.getID()});
            int nSize = li.size();
            if (0 < nSize) {
                createReturnQuotas(li, lstQuota, order);
            }
        }

        // 退配额
        /***
        if (!lstQuota.isEmpty()) {
            QuotaQueryPo queryPo = new QuotaQueryPo();
            queryPo.setHotelId(order.getHotelId());
            queryPo.setMemberType(QuotaMemberType.CC);
            queryPo.setBedID(order.getBedType());
            for (Object[] obj : lstQuota) {
                queryPo.setQuotaType((String) obj[5]);
                queryPo.setRoomTypeId(((BigDecimal) obj[6]).longValue());
                queryPo.setChildRoomId(((BigDecimal) obj[4]).longValue());
                queryPo.setPayMethod((String) obj[3]);
                queryPo.setBeginDate((Date) obj[0]);
                queryPo.setEndDate((Date) obj[1]);
                queryPo.setQuotaNum((Integer) obj[2]);
                quotaForCCService.returnQuota(queryPo);
            }
        }
		***/
        List<OrOrderItem> orderItems = order.getOrderItems();
        if(orderItems!=null&&orderItems.size()>0){
        	List<QuotaReturn> quotaReturnList = new ArrayList<QuotaReturn>();
	        for(OrOrderItem orderItem : orderItems){
	        	QuotaReturn quotaReturn = new QuotaReturn();
	        	quotaReturn.setHotelId(order.getHotelId());
	        	quotaReturn.setRoomTypeId(order.getRoomTypeId());
	        	quotaReturn.setBedId(order.getBedType());
	        	quotaReturn.setChildRoomTypeId(order.getChildRoomTypeId());
	        	quotaReturn.setQuotaNum(1);
	        	quotaReturn.setUseQuotaDate(orderItem.getNight());
	        	quotaReturn.setQuotaDate(orderItem.getNight());
	        	quotaReturn.setPayMethod(order.isPayToPrepay() ? PayMethod.PAY : order.getPayMethod());
	        	// CC的memberType为1
	        	quotaReturn.setMemberType(1);
	        	// 设置配额类型
	        	if(orderItem.getQuotaPattern()==null){
	        		quotaReturn.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNSI);
	        	}else{
	        		quotaReturn.setQuotaPattern(orderItem.getQuotaPattern());
	        	}
	        	// 设置配额类型
	        	if(orderItem.getQuotaType()==null){
	        		quotaReturn.setQuotaType(HotelBaseConstantBean.GENERALQUOTA);
	        	}else{
	        		quotaReturn.setQuotaType(orderItem.getQuotaType());
	        	}
	        	// 设置配额共享方式
	        	if(orderItem.getQuotashare()==0){
	        		quotaReturn.setQuotaShare(new Long(HotelBaseConstantBean.QUOTASHARETYPE));
	        	}else{
	        		quotaReturn.setQuotaShare(new Long(orderItem.getQuotashare()));
	        	}
	        	if(orderItem.getIsConfirm()==false){
	        		//配额不可退
	        		quotaReturn.setSign(1);
	        	}else{
	        		//配额可退
	        		quotaReturn.setSign(0);
	        	}
	        	quotaReturn.setOperatorName(roleUser.getName());
	        	quotaReturn.setOperatorId(roleUser.getLoginName());
	        	quotaReturnList.add(quotaReturn);
	        }
	        quotaControl.returnQuota(quotaReturnList);
        }
        
        // 删掉该订单的历史配额记录
        hql = " delete OrQuotaRecord where orderId = " + order.getID();
        orOrderDao.doUpdateBatch(hql, null);

        // 设置所有历史订单不可恢复
        hql = "update HOrder set hisCanResume=0 where hisValid = 1 and orderId= " + order.getID();
        orOrderDao.doUpdateBatch(hql, null);

        // 添加日志
        orderAssist.getCancelLog(order, cancelReason, cancelMessage, guestCancelMessage, roleUser);

        OrderUtil.updateStayInMid(order);

        orOrderDao.saveOrUpdate(order);
    }

    /**
     * 恢复历史订单时退配额
     * 
     * @author chenkeming Feb 16, 2009 2:56:02 PM
     * @param order
     * @param orderH
     */
    public void returnQuotaResume(OrOrder order, HOrder orderH) {

        // 获取要退的配额
        List<Object[]> li = null;
        String hql;
        String quotaPattern = getQuotaPattern(order);
        List<Object[]> lstQuota = new ArrayList<Object[]>();
        if (HotelBaseConstantBean.QUOTAPATTERNCI.equals(quotaPattern)) { // "C-I"
            hql = "select qr.payMethod,qr.childRoomTypeId,qr.night,"
                + "sum(qr.quantity), quotaTypeOld, roomTypeId from or_quota_record qr "
                + " where qr.orderId = ? and hisNo > ? "
                + " group by qr.payMethod, roomTypeId, qr.childRoomTypeId, quotaTypeOld, qr.night"
                + " order by qr.payMethod, roomTypeId, qr.childRoomTypeId, quotaTypeOld, qr.night";
            li = orOrderDao.query(hql, new Object[]{order.getID(), Integer.valueOf(orderH.getHisNo())});
            for (Object[] tmpObj : li) {
                Integer nNum = ((BigDecimal) tmpObj[3]).intValue();
                Date tmpBegin = (Date) tmpObj[2];
                if (0 < nNum.intValue()) {
                    Object[] obj = new Object[7];
                    obj[0] = tmpBegin;
                    obj[1] = DateUtil.getDate(tmpBegin, 1);
                    obj[2] = nNum;
                    obj[3] = tmpObj[0];
                    obj[4] = tmpObj[1];
                    obj[5] = tmpObj[4];
                    obj[6] = tmpObj[5];
                    lstQuota.add(obj);
                }
            }
        } else { // "S-I"
            hql = "select qr.payMethod,qr.childRoomTypeId,qr.night,"
                + "sum(qr.quantity), quotaTypeOld, roomTypeId  " + " from or_quota_record qr "
                + " where qr.orderId = ? and hisNo > ? "
                + " group by qr.payMethod, roomTypeId, qr.childRoomTypeId, quotaTypeOld, qr.night"
                + " order by qr.payMethod, roomTypeId, qr.childRoomTypeId, quotaTypeOld, qr.night";
            li = orOrderDao.query(hql, new Object[]{order.getID(), Integer.valueOf(orderH.getHisNo())});
            int nSize = li.size();
            if (0 < nSize) {
                createReturnQuotas(li, lstQuota, order);
            }
        }

        // 退配额
        /***
        if (!lstQuota.isEmpty()) {
            QuotaQueryPo queryPo = new QuotaQueryPo();
            queryPo.setHotelId(order.getHotelId());
            queryPo.setMemberType(QuotaMemberType.CC);
            queryPo.setBedID(order.getBedType());
            for (Object[] obj : lstQuota) {
                queryPo.setQuotaType((String) obj[5]);
                queryPo.setRoomTypeId(((BigDecimal) obj[6]).longValue());
                queryPo.setChildRoomId(((BigDecimal) obj[4]).longValue());
                queryPo.setPayMethod((String) obj[3]);
                queryPo.setBeginDate((Date) obj[0]);
                queryPo.setEndDate((Date) obj[1]);
                queryPo.setQuotaNum((Integer) obj[2]);
                quotaForCCService.returnQuota(queryPo);
            }
        }
		***/
        List<OrOrderItem> orderItems = order.getOrderItems();
        if(orderItems!=null&&orderItems.size()>0){
        	List<QuotaReturn> quotaReturnList = new ArrayList<QuotaReturn>();
	        for(OrOrderItem orderItem : orderItems){
	        	QuotaReturn quotaReturn = new QuotaReturn();
	        	quotaReturn.setHotelId(order.getHotelId());
	        	quotaReturn.setRoomTypeId(order.getRoomTypeId());
	        	quotaReturn.setBedId(order.getBedType());
	        	quotaReturn.setChildRoomTypeId(order.getChildRoomTypeId());
	        	quotaReturn.setQuotaNum(1);
	        	quotaReturn.setUseQuotaDate(orderItem.getNight());
	        	quotaReturn.setQuotaDate(orderItem.getNight());
	        	// CC的memberType为1
	        	quotaReturn.setMemberType(1);
	        	// 设置配额类型
	        	if(orderItem.getQuotaPattern()==null){
	        		quotaReturn.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNSI);
	        	}else{
	        		quotaReturn.setQuotaPattern(orderItem.getQuotaPattern());
	        	}
	        	// 设置配额类型
	        	if(orderItem.getQuotaType()==null){
	        		quotaReturn.setQuotaType(HotelBaseConstantBean.GENERALQUOTA);
	        	}else{
	        		quotaReturn.setQuotaType(orderItem.getQuotaType());
	        	}
	        	// 设置配额共享方式
	        	if(orderItem.getQuotashare()==0){
	        		quotaReturn.setQuotaShare(new Long(HotelBaseConstantBean.QUOTASHARETYPE));
	        	}else{
	        		quotaReturn.setQuotaShare(new Long(orderItem.getQuotashare()));
	        	}
	        	if(orderItem.getIsConfirm()==false){
	        		//配额不可退
	        		quotaReturn.setSign(1);
	        	}else{
	        		//配额可退
	        		quotaReturn.setSign(0);
	        	}
	        	quotaReturnList.add(quotaReturn);
	        }
	        quotaControl.returnQuota(quotaReturnList);
        }

        // 相应删掉该订单的历史配额记录
        hql = " delete OrQuotaRecord where orderId = " + order.getID() + " and hisNo > "
            + orderH.getHisNo();
        orOrderDao.doUpdateBatch(hql, null);

        // 设置相应历史订单无效
        hql = "update HOrder set hisValid=0 where hisValid = 1 and orderId= " + order.getID()
            + " and hisNo >= " + orderH.getHisNo();
        orOrderDao.doUpdateBatch(hql, null);

        disableOrderMoney(order, orderH);

        orOrderDao.saveOrUpdate(order);
    }

    /**
     * 恢复历史订单时让不成功的发生金额实效
     * 
     * @author chenkeming Mar 6, 2009 10:39:37 AM
     * @param order
     * @param orderH
     */
    private void disableOrderMoney(OrOrder order, HOrder orderH) {
        String hql = "update OrOrderMoney set valid=0 where success=0 and order.ID="
            + order.getID() + " and hisNo >= " + orderH.getHisNo();
        orOrderDao.doUpdateBatch(hql, null);
    }

    /**
     * 根据主键load orderH
     * 
     * @author chenkeming Feb 16, 2009 1:58:48 PM
     * @param hisID
     * @return
     */
    public HOrder getOrderH(Serializable hisID) {
        return (HOrder) orOrderDao.load(HOrder.class, hisID);
    }

    /**
     * 获取给定订单的历史订单列表
     * 
     * @author chenkeming Feb 13, 2009 3:01:02 PM
     * @param orderId
     * @return
     */
    public List<Object[]> getHisOrders(long orderId) {
        String hql = "select c.hisCreateDate,c.payMethod,c.roomTypeName,"
            + "c.childRoomTypeName,c.checkinDate,c.checkoutDate,c.roomQuantity,"
            + "c.hisHotelConfirm, c.hisID, c.hisCanResume, " +
                    "c.orderState,c.hisNo,c.resumeNo from HOrder " +
                    "as c where c.orderId="
            + orderId + " and c.hisValid=1 order by c.hisCreateDate desc";
        return orOrderDao.query(hql, null);
    }

    /**
     * 未扣配额订单修改时,扣完配额后，把所扣配额插入到订单配额记录表
     * 
     * @author chenkeming Feb 17, 2009 11:23:08 AM
     * @param order
     * @param orderH
     */
    public void saveOrderItemToQuotaRecord(OrOrder order, HOrder orderH) {
        String payMethod = order.getPayMethod();
        Long roomTypeId = order.getRoomTypeId();
        Long childRoomTypeId = order.getChildRoomTypeId();
        Long orderID = order.getID();
        String quotaTypeOld = order.getQuotaTypeOld();
        for (OrOrderItem item : order.getOrderItems()) {
            OrQuotaRecord record = new OrQuotaRecord();
            MyBeanUtil.copyProperties(record, item);
            record.setPayMethod(payMethod);
            record.setRoomTypeId(roomTypeId);
            record.setChildRoomTypeId(childRoomTypeId);
            record.setID(null);
            record.setHisNo(order.getHisNo()); // 未扣配额订单一般为原始单?
            record.setOrderId(orderID);
            record.setQuotaTypeOld(quotaTypeOld);
            saveOrUpdate(record);
        }
        saveOrUpdate(orderH);
        orOrderDao.saveOrUpdate(order);
    }

    /**
     * 只更新订单的紧急程度和担保状态字段
     * 
     * @author chenkeming Feb 18, 2009 12:18:23 PM
     * @param order
     * @param level
     * @param suretyState
     */
    public void updateOrderForEdit(OrOrder order, int level, int suretyState) {
        String hql = "update OrOrder set emergencyLevel=" + level + ",suretyState=" + suretyState
            + " where ID= " + order.getID();
        orOrderDao.doUpdateBatch(hql, null);
    }

    /**
     * 将从数据库取出的时间如("18:00")转换为int 1800 add by shizhongwen 时间:Jun 10, 2009 4:59:37 PM
     * 
     * @param s_time
     * @return
     */
    public int getIntbyString(String s_time) {
        int inttime = 0;
        String temp = "";
        int index = s_time.indexOf(":");
        if (0 < index) {
            temp = s_time.substring(0, index) + s_time.substring(index + 1);
        } else {
            temp = s_time;
        }
        inttime = Integer.parseInt(temp);
        return inttime;
    }

    /**
     * 取出night 晚的订单明细 add by shizhongwen 时间:Jun 12, 2009 9:42:34 AM
     * 
     * @param night
     * @param orguaranteelist
     * @return
     */
    public OrGuaranteeItem getOrguarantee(Date night, List<OrGuaranteeItem> orguaranteelist) {
        OrGuaranteeItem oritem = null;
        for (OrGuaranteeItem item : orguaranteelist) {
            // 如果日期相等
            if (0 == DateUtil.getDay(night,item.getNight())) {
                oritem = item;
                break;
            }
        }

        return oritem;
    }

    /**
     * 检查取消订单扣款金额
     * 
     * @author chenkeming Feb 24, 2009 6:20:49 PM
     * @param reservationID
     * @param sumRmb
     * @param checkIn
     * @return
     */
    public String getModifyandCancelCheck(long reservationID, long orderID, String checkIn,
        long roomQuantity, int scope) {
        OrReservation reserv = (OrReservation) orOrderDao.load(OrReservation.class, reservationID);
        OrOrder order = orOrderDao.getOrder(orderID);
        if (null == order) {
            log.error("找不到此订单");
            return "找不到此订单!";
        }
        List<OrPriceDetail> liPrice = order.getPriceList();
        double orderallamount = order.getSumRmb();// 此订单全额金额 
        boolean isGuarantee = false;
        // 获得担保明细
        List<OrGuaranteeItem> orGuaranteelist = reserv.getGuarantees();
        // 若为预付订单
        if (order.isPrepayOrder()) {
        	//如果为预付也要计算出金额和对应的扣款金额 add by haibo.li
        	List<OrAssureItemEvery> li = reserv.getAssureList();
        	Date now = new Date();
        	boolean falg = false;
        	String scopeStr = "";
        	for (OrAssureItemEvery item : li){
        		//int dayIndex = DateUtil.getDay(order.getCheckinDate(), item.getNight());
        		int type = Integer.parseInt(item.getType());
        		int deductType = 0;
        		if(StringUtil.isValidStr(item.getDeductType())){
        			deductType = Integer.parseInt(item.getDeductType());
        		}

//        		if(1 == type){
//        			if(deductType == 1 && (Integer.parseInt(item.getScope()) == scope 
//        					|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH) ){
//        				double firstSalePrice = liPrice.get(0).getSalePrice() * roomQuantity;
//        				scopeStr =  "" + firstSalePrice ;
//        				falg = true;
//        				continue;
//        			}else if(deductType == 2 && (Integer.parseInt(item.getScope()) == scope
//        					|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//        				scopeStr = ""+ order.getSumRmb() ;
//        				falg = true;
//        				continue;
//        			}else if(deductType == 4 && (Integer.parseInt(item.getScope()) == scope 
//        					|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//        				double firstSalePrice = (liPrice.get(0).getSalePrice() * roomQuantity * Double.parseDouble(item.getPercentage()))/100;
//        				scopeStr =  "" + firstSalePrice ;
//        				falg = true;
//        				continue;
//        			}else if(deductType == 6 && (Integer.parseInt(item.getScope()) == scope 
//        					|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//        				scopeStr = ""+ (order.getSumRmb()*Double.parseDouble(item.getPercentage()))/100 ;
//        				falg = true;
//        				continue;
//        			}
//        			
//        			
//        			
//        		}else if(2 ==type){
//        			Date start = DateUtil.stringToDatetime(item.getFirstDateOrDays() + " "
//                            + item.getFirstTime());
//                        Date end = DateUtil.stringToDatetime(item.getSecondDateOrDays() + " "
//                            + item.getSecondTime());
//                        log.info(start);
//                        log.info(end);
//                        if(DateUtil.between(now, start, end) && DateUtil.dateToString(now).equals(DateUtil.dateToString(item.getNight()))){
//                        	
//                        	if(deductType == 1 && (Integer.parseInt(item.getScope()) == scope 
//                        			|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//                				double firstSalePrice = liPrice.get(0).getSalePrice() * roomQuantity;
//                				scopeStr =  "" + firstSalePrice ;
//                				falg = true;
//                				continue;
//                			}else if(deductType == 2 && (Integer.parseInt(item.getScope()) == scope 
//                					|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//                				scopeStr = ""+ order.getSumRmb();
//                				falg = true;
//                				continue;
//                			}else if(deductType == 4 && (Integer.parseInt(item.getScope()) == scope 
//                					|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//                				double firstSalePrice = (liPrice.get(0).getSalePrice() * roomQuantity * Double.parseDouble(item.getPercentage()))/100;
//                				scopeStr =  "" + firstSalePrice;
//                				falg = true;
//                				continue;
//                			}else if(deductType == 6 && (Integer.parseInt(item.getScope()) == scope 
//                					|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//                				scopeStr = ""+ (order.getSumRmb()*Double.parseDouble(item.getPercentage()))/100 ;
//                				falg = true;
//                				continue;
//                			}
//                        }
//                        
//                        
//        		}else if(3==type){
//        			Date start = DateUtil.stringToDatetime(item.getFirstDateOrDays() + " " + item.getFirstTime());
//                        Date end = DateUtil.stringToDatetime(checkIn + " " + item.getSecondTime());
//                        if(DateUtil.between(now, start, end) && DateUtil.dateToString(now).equals(DateUtil.dateToString(item.getNight()))){
//                        	if(deductType == 1 && (Integer.parseInt(item.getScope()) == scope 
//                        			|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//                				double firstSalePrice = liPrice.get(0).getSalePrice() * roomQuantity;
//                				scopeStr =  "" + firstSalePrice  ;
//                				falg = true;
//                				continue;
//                			}else if(deductType == 2 && (Integer.parseInt(item.getScope()) == scope 
//                					|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//                				scopeStr = ""+ order.getSumRmb() ;
//                				falg = true;
//                				continue;
//                			}else if(deductType == 4 && (Integer.parseInt(item.getScope()) == scope 
//                					|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//                				double firstSalePrice = (liPrice.get(0).getSalePrice() * roomQuantity * Double.parseDouble(item.getPercentage()))/100;
//                				scopeStr =  "" + firstSalePrice  ;
//                				falg = true;
//                				continue;
//                			}else if(deductType == 6 && (Integer.parseInt(item.getScope()) == scope 
//                					|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//                				scopeStr = ""+ (order.getSumRmb()*Double.parseDouble(item.getPercentage()))/100 ;
//                				falg = true;
//                				continue;
//                			}
//                        }  
//        		}else if(4==type){
//        			Date checkInDate = DateUtil.getDate(checkIn);
//                    int daysBefore = Integer.parseInt(item.getFirstDateOrDays());
//                    Date start = DateUtil.stringToDatetime(DateUtil.dateToString(DateUtil.getDate(
//                        checkInDate, -daysBefore))
//                        + " " + item.getFirstTime());
//                    daysBefore = Integer.parseInt(item.getSecondDateOrDays());
//                    Date end = DateUtil.stringToDatetime(DateUtil.dateToString(DateUtil.getDate(
//                        checkInDate, -daysBefore))
//                        + " " + item.getSecondTime());
//                    
//                    
//                    if(DateUtil.between(now, start, end) && now == item.getNight()){
//                    	
//                    	if(deductType == 1 && (Integer.parseInt(item.getScope()) == scope 
//                    			|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//            				double firstSalePrice = liPrice.get(0).getSalePrice() * roomQuantity;
//            				scopeStr =  "" + firstSalePrice  ;
//            				falg = true;
//            				continue;
//            			}else if(deductType == 2 && (Integer.parseInt(item.getScope()) == scope 
//            					|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//            				scopeStr = ""+ order.getSumRmb();
//            				falg = true;
//            				continue;
//            			}else if(deductType == 4 && (Integer.parseInt(item.getScope()) == scope 
//            					|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//            				double firstSalePrice = (liPrice.get(0).getSalePrice() * roomQuantity * Double.parseDouble(item.getPercentage()))/100;
//            				scopeStr =  "" + firstSalePrice  ;
//            				falg = true;
//            				continue;
//            			}else if(deductType == 6 && (Integer.parseInt(item.getScope()) == scope 
//            					|| Integer.parseInt(item.getScope())==ModifyScopeType.BOTH)){
//            				scopeStr = ""+ (order.getSumRmb()*Double.parseDouble(item.getPercentage()))/100;
//            				falg = true;
//            				continue;
//            			}
//                    }
//
//        		}else if(5 == type){
//        			
//        			
//        			
//        			
//        			
//        		}	
        	}

            if (scope == ModifyScopeType.CANCEL && falg == true) {
                return ""+ scopeStr+ "@根据酒店预订条款,本次撤单操作,将扣取"+ scopeStr ;
            }
            if (scope == ModifyScopeType.MODIFY && falg == true) {
                return ""+scopeStr + "@根据酒店预订条款,本次修改操作一旦得到酒店确认,将扣取" + scopeStr ;
            } else {
                return "ok";
            }
        } else {

            if (null == reserv) {
                return "ok";
            }
            List<OrAssureItemEvery> li = reserv.getAssureList();
            Date now = new Date();
            double maxPrice = 0.0;
            // 酒店2.6 modify by shizhongwen 2009-05-25
            int clauseRule = Integer.parseInt(reserv.getClauseRule());// 预定条款计算规则，默认是3
            // 判断只计算一次首日担保的标识(如果第一天无担保的情况下)
            boolean firstAssuretag = true;
            // 最大金额百分比
            int maxpercent = 0;
            for (OrAssureItemEvery item : li) {
                int type = Integer.parseInt(item.getType());
                int deductType = Integer.parseInt(item.getDeductType());
                boolean bNeed = false;
                if (1 == type) { // 第1种
                    bNeed = true;
                } else if (2 == type) { // 第2种
                    Date start = DateUtil.stringToDatetime(item.getFirstDateOrDays() + " "
                        + item.getFirstTime());
                    Date end = DateUtil.stringToDatetime(item.getSecondDateOrDays() + " "
                        + item.getSecondTime());
                    bNeed = DateUtil.between(now, start, end);
                } else if (3 == type) { // 第3种
                    Date start = DateUtil.stringToDatetime(item.getFirstDateOrDays() + " "
                        + item.getFirstTime());
                    Date end = DateUtil.stringToDatetime(checkIn + " " + item.getSecondTime());
                    bNeed = DateUtil.between(now, start, end);
                } else if (4 == type) { // 第4种
                    Date checkInDate = DateUtil.getDate(checkIn);
                    int daysBefore = Integer.parseInt(item.getFirstDateOrDays());
                    Date start = DateUtil.stringToDatetime(DateUtil.dateToString(DateUtil.getDate(
                        checkInDate, -daysBefore))
                        + " " + item.getFirstTime());
                    daysBefore = Integer.parseInt(item.getSecondDateOrDays());
                    Date end = DateUtil.stringToDatetime(DateUtil.dateToString(DateUtil.getDate(
                        checkInDate, -daysBefore))
                        + " " + item.getSecondTime());
                    bNeed = DateUtil.between(now, start, end);
                } else { // 第5种
                    // bNeed = DateUtil.compareDateTimeToSys(item.getNight(), item.getFirstTime());
                }

                if (bNeed) {

                    // 需要扣除的百分比；
                    double curPrice = 0.0;
                    // 乘以房间数以后的需扣除金额；
                    // 乘以百分比以后的金额;

                    // 定义四个BigDecimal变量，用于两个double类型的相乘；
                    // 酒店2.6 modify by shizhongwen 2009-05-25
                    // 判断第一天是否担保,如果dayIndex为0,表示第一天有担保
                    int dayIndex = DateUtil.getDay(order.getCheckinDate(), item.getNight());
                    // 取出当晚的订单的担保明细
                    OrGuaranteeItem orguarantee = getOrguarantee(item.getNight(), orGuaranteelist);
                    if (null == orguarantee) {
                        isGuarantee = false;
                    } else {
                        // 如果是无条件担保
                        if (null != orguarantee.getNotConditional()
                            && orguarantee.getNotConditional().equals("1")) {
                            isGuarantee = true;
                        } else { // 不是无条件担保
                            String latestAssureTime = orguarantee.getLatestAssureTime();// 超时
                            int overRoomNumber = 0;
                            overRoomNumber = orguarantee.getOverRoomNumber();// 超房数
                            if (null != latestAssureTime && !"".equals(latestAssureTime)
                                && 0 == dayIndex) {// 超时不为空,且为第一天
                                if (getIntbyString(latestAssureTime) < getIntbyString(order
                                    .getLatestArrivalTime())) {// 担保超时时间小于最晚到店时间
                                    isGuarantee = true;
                                } else {
                                    isGuarantee = false;
                                }
                            } else {
                                isGuarantee = false;
                            }
                            if (!isGuarantee) {
                                // 担保超房数房大于0,担保超房数房小于预订间数,即超房数担保条件成立
                                if (0 < overRoomNumber && overRoomNumber < 
                                    order.getRoomQuantity()) {
                                    isGuarantee = true;
                                } else {
                                    isGuarantee = false;
                                }
                            }
                        }
                    }

                    // 如果担保条件为真,开始计算担保金额
                    if (isGuarantee) {
                        OrPriceDetail priceDetail = liPrice.get(dayIndex);
                        // 获取第一天的销售价格
                        double firstSalePrice = liPrice.get(0).getSalePrice();
                        // 担保金额的计算规则 为check in day
                        if (clauseRule == HotelCalcuAssuAmoType.CHECKIN) {
                            if (deductType == DeductType.FIRST_DAY
                                || deductType == DeductType.ALL_DAY) {
                                curPrice = reserv.getFirstPrice() * roomQuantity;
                                if (scope == ModifyScopeType.CANCEL) {
                                    return "" + curPrice + "@根据酒店预订条款,本次撤单操作,将扣取" + curPrice;
                                }
                                if (scope == ModifyScopeType.MODIFY) {
                                    return "" + curPrice + "@根据酒店预订条款,本次修改操作一旦得到酒店确认,将扣取"
                                        + curPrice ;
                                }

                            } else if (deductType == DeductType.FIRST_DAY_PERCENTAGE
                                || deductType == DeductType.ALL_DAY_PERCENTAGE) {
                                curPrice = getCurPriceBigDecimal(reserv.getFirstPrice(),
                                    roomQuantity, item.getPercentage());
                                if (scope == ModifyScopeType.CANCEL) {
                                    return "" + curPrice + "@根据酒店预订条款,本次撤单操作,将扣取" + curPrice;
                                }
                                if (scope == ModifyScopeType.MODIFY) {
                                    return "" + curPrice + "@根据酒店预订条款,本次修改操作一旦得到酒店确认,将扣取"
                                        + curPrice ;
                                }
                            }
                        }
                        // 担保金额的计算规则 为全额判定
                        else if (clauseRule == HotelCalcuAssuAmoType.ALLAMOUNT) {// 如果是按全额
                            String assureType = priceDetail.getAssureType();
                            // 当天若为全额担保类型
                            if (GuaranteeType.ALL_ASSURE_TYPE.equals(assureType)) {
                                if (deductType == DeductType.FIRST_DAY
                                    || deductType == DeductType.ALL_DAY) {
                                    maxPrice = orderallamount;
                                    break;
                                } else if (deductType == DeductType.FIRST_DAY_PERCENTAGE
                                    || deductType == DeductType.ALL_DAY_PERCENTAGE) {
                                    if (Integer.parseInt(item.getPercentage()) > maxpercent) {
                                        maxpercent = Integer.parseInt(item.getPercentage());
                                    }
                                }
                            }
                            // 当天若为首日担保类型
                            if (GuaranteeType.FIRST_ASSURE_TYPE.equals(assureType)
                                && firstAssuretag) {
                                if (deductType == DeductType.FIRST_DAY
                                    || deductType == DeductType.ALL_DAY) {
                                    curPrice = firstSalePrice * roomQuantity;
                                } else if (deductType == DeductType.FIRST_DAY_PERCENTAGE
                                    || deductType == DeductType.ALL_DAY_PERCENTAGE) {
                                    curPrice = getCurPriceBigDecimal(firstSalePrice, roomQuantity,
                                        item.getPercentage());
                                }
                                firstAssuretag = false;
                            }
                        }
                        // 担保金额的计算规则 为系统默认累加判定
                        else if (clauseRule == HotelCalcuAssuAmoType.TOTTINGUP) {
                            String assureType = priceDetail.getAssureType();
                            if (0 == dayIndex) {// 判断是否为第一天
                                if (deductType == DeductType.FIRST_DAY
                                    || deductType == DeductType.ALL_DAY) {
                                    curPrice = priceDetail.getSalePrice() * roomQuantity;
                                } else if (deductType == DeductType.FIRST_DAY_PERCENTAGE
                                    || deductType == DeductType.ALL_DAY_PERCENTAGE) {
                                    curPrice = getCurPriceBigDecimal(priceDetail.getSalePrice(),
                                        roomQuantity, item.getPercentage());
                                }
                                firstAssuretag = false;
                            } else {
                                // 当天若为全额担保类型
                                if (GuaranteeType.ALL_ASSURE_TYPE.equals(assureType)) {
                                    if (deductType == DeductType.FIRST_DAY
                                        || deductType == DeductType.ALL_DAY) {
                                        curPrice = priceDetail.getSalePrice() * roomQuantity;
                                    } else if (deductType == DeductType.FIRST_DAY_PERCENTAGE
                                        || deductType == DeductType.ALL_DAY_PERCENTAGE) {
                                        curPrice = getCurPriceBigDecimal(
                                            priceDetail.getSalePrice(), roomQuantity, item
                                                .getPercentage());
                                    }
                                }
                                // 当天若为首日担保类型
                                if (GuaranteeType.FIRST_ASSURE_TYPE.equals(assureType)
                                    && firstAssuretag) {
                                    if (deductType == DeductType.FIRST_DAY
                                        || deductType == DeductType.ALL_DAY) {
                                        curPrice = firstSalePrice * roomQuantity;
                                    } else if (deductType == DeductType.FIRST_DAY_PERCENTAGE
                                        || deductType == DeductType.ALL_DAY_PERCENTAGE) {
                                        curPrice = getCurPriceBigDecimal(firstSalePrice,
                                            roomQuantity, item.getPercentage());
                                    }
                                    firstAssuretag = false;
                                }
                            }
                        }
                        maxPrice += curPrice;
                    }
                }
            }
            if (0 < maxpercent) {
                maxPrice = getCurPriceBigDecimal(orderallamount, 1, "" + maxpercent);
            }
            if (0.001 < maxPrice) {
                if (scope == ModifyScopeType.CANCEL) {
                    return "" + maxPrice + "@根据酒店预订条款,本次撤单操作,将扣取" + maxPrice ;
                }
                if (scope == ModifyScopeType.MODIFY) {
                    return "" + maxPrice + "@根据酒店预订条款,本次修改操作一旦得到酒店确认,将扣取" + maxPrice;
                }
            }
            return "ok";
        }
    }

    /**
     * 
     * add by shizhongwen 时间:May 25, 2009 7:49:59 PM
     * 
     * @param price
     *            价格
     * @param roomQuantity
     *            房间数
     * @param percentage
     * @return
     */
    private double getCurPriceBigDecimal(double price, long roomQuantity, String percentage) {
        double curPrice;
        double percent;
        double allAssuse;
        BigDecimal b1;
        BigDecimal b2;
        BigDecimal b3;
        BigDecimal b4;
        // 两个Double数相乘，进行转换；
        b1 = new BigDecimal(percentage);
        b2 = new BigDecimal("0.01");
        // 需要扣除的百分比；
        percent = b1.multiply(b2).doubleValue();
        // 两个Double数相乘，进行转换；
        b3 = new BigDecimal(Double.valueOf(price).toString());
        b4 = new BigDecimal(Double.valueOf(percent).toString());
        // 乘以百分比以后的金额;
        allAssuse = b3.multiply(b4).doubleValue();
        // 乘以房间数以后的需扣除金额；
        curPrice = allAssuse * roomQuantity;
        return curPrice;
    }

    /**
     * 在修改订单页面,获取取消/修改规则字符串
     * 
     * @author chenkeming Feb 24, 2009 5:53:24 PM
     * @param reservationID
     * @param sumRmb
     * @return
     */
    /*
     * public List getModifyCancelStr(long reservationID, double sumRmb) { OrReservation reserv =
     * (OrReservation)orOrderDao.load(OrReservation.class, reservationID); List liRes = new
     * ArrayList(); if(reserv == null) { return liRes; } double firstPrice = reserv.getFirstPrice();
     * List<OrAssureItemEvery> li = reserv.getAssureList(); for(OrAssureItemEvery item : li) {
     * StringBuffer buffer = new StringBuffer(); //取消或修改；扣款类型，不为空的校验；add by shengwei.zuo hotel2.6
     * 2009-05-27
     * if((item.getType()!=null&&!("").equals(item.getType()))&&(item.getScope()!=null&&!(
     * "").equals(item.getScope()))
     * &&(item.getDeductType()!=null&&!("").equals(item.getDeductType()))) { int type =
     * Integer.parseInt(item.getType()); int scope = Integer.parseInt(item.getScope()); int
     * deductType = Integer.parseInt(item.getDeductType());
     * 
     * String strMoney = item.getPercentage(); //修复某酒店预订单修改时，取消条款拉取不到的BUG；add by shengwei.zuo
     * hotel2.6 begin; strMoney = (strMoney!=null)?strMoney:"0"; //修复某酒店预订单修改时，取消条款拉取不到的BUG；add by
     * shengwei.zuo hotel2.6 end; double money = Double.parseDouble(strMoney); String strScope =
     * "<font color=red>"; if(scope == ModifyScopeType.CANCEL) { strScope = "取消"; } else if(scope ==
     * ModifyScopeType.MODIFY) { strScope = "修改"; } else if(scope == ModifyScopeType.BOTH) {
     * strScope = "取消/修改"; } strScope += "</font>"; String strDeduct = "";
     * 
     * if(deductType == DeductType.FIRST_DAY) { strDeduct = "订单首日金额(<font color=red>" + firstPrice +
     * "</font>元)"; } else if(deductType == DeductType.ALL_DAY) { strDeduct =
     * "订单全额金额(<font color=red>" + sumRmb + "</font>元)"; } else if(deductType ==
     * DeductType.FIRST_DAY_PERCENTAGE) { strDeduct = "订单首日金额的" + strMoney + "%(<font color=red>" +
     * OrderUtil.roundPrice(firstPrice * money * 0.01) + "</font>元)"; } else if(deductType ==
     * DeductType.ALL_DAY_PERCENTAGE){ strDeduct = "订单金额的" + strMoney + "%(<font color=red>" +
     * OrderUtil.roundPrice( sumRmb * money * 0.01) + "</font>元)"; }
     * 
     * buffer.append("酒店确认成功后，"); if(type == 1) { // 第1种: 凡是均需 buffer.append("凡是"); } else if(type
     * == 2) { // 第2种: 在几日几点至几日几点
     * buffer.append("在<font color=red>").append(item.getFirstDateOrDays() + " " +
     * item.getFirstTime());
     * buffer.append("</font>至<font color=red>").append(item.getSecondDateOrDays() + " " +
     * item.getSecondTime() + "</font>"); } else if(type == 3) { // 第3种: 在几日几点至入住当日几点
     * buffer.append("在<font color=red>").append(item.getFirstDateOrDays() + " " +
     * item.getFirstTime() + "</font>");
     * buffer.append("至入住当日<font color=red>").append(item.getSecondTime() + "</font>"); } else
     * if(type == 4) { // 第4种: 在入住日期前几天几点至几天几点
     * buffer.append("在入住日期前<font color=red>").append(item.getFirstDateOrDays() + "天" +
     * item.getFirstTime() + "</font>");
     * buffer.append("至<font color=red>").append(item.getSecondDateOrDays() + "天" +
     * item.getSecondTime() + "</font>"); } else if(type == 5){ // 第5种: 几点之前/之后
     * buffer.append("在<font color=red>"
     * ).append(DateUtil.dateToString(item.getNight())).append(" ").append(item.getFirstTime());
     * buffer.append("</font>之后"); } buffer.append(strScope).append(",将按酒店规定扣取").append(strDeduct);
     * liRes.add(buffer.toString());
     * 
     * } } return liRes; }
     */

    /**
     * v2.6 在修改订单页面,获取取消/修改规则字符串,需求改变:根据首日条款判断
     * 
     * @author chenkeming Jun 11, 2009 4:33:42 PM
     * @param reservationID
     * @param sumRmb
     * @return
     */
    public List getModifyCancelStr1(long reservationID, double sumRmb) {
        OrReservation reserv = (OrReservation) orOrderDao.load(OrReservation.class, reservationID);
        List liRes = new ArrayList();
        if (null == reserv) {
            return liRes;
        }
        Date curDate = null;
        boolean bFound = false;
        List<OrAssureItemEvery> li = reserv.getAssureList();
        StringBuffer buffer = new StringBuffer();
        int curScope = 0;
        int curType = 1000;
        Date earlyDate = DateUtil.getDate("2999-12-12");
        int earlyDays = -1;
        for (OrAssureItemEvery item : li) {
            Date tmpDate = item.getNight();
            if (null == curDate) {
                curDate = tmpDate;
            }
            if (tmpDate.after(curDate)) {
                if (bFound) {
                    break;
                } else {
                    curDate = tmpDate;
                }
            }
            // 取消或修改；扣款类型，不为空的校验；add by shengwei.zuo hotel2.6 2009-05-27
            if ((null != item.getType() && !("").equals(item.getType()))
                && (null != item.getScope() && !("").equals(item.getScope()))
                && (null != item.getDeductType() && !("").equals(item.getDeductType()))) {
                bFound = true;
                int type = Integer.parseInt(item.getType());
                int scope = Integer.parseInt(item.getScope());

                if (1 == type) { // 第1种: 凡是均需
                    curType = type;
                    curScope = scope;
                    break;
                } else if (2 == type || 3 == type) { // 第2种: 在几日几点至几日几点,第3种: 在几日几点至入住当日几点
                    String strDate = item.getFirstDateOrDays();
                    if (StringUtil.isValidStr(strDate)) {
                        tmpDate = DateUtil.getDate(strDate);
                        if (tmpDate.before(earlyDate)) {
                            earlyDate = tmpDate;
                        }
                        curType = type;
                        curScope = scope;
                    }
                } else if (4 == type) { // 第4种: 在入住日期前几天几点至几天几点
                    if (4 > curType) {
                        continue;
                    }
                    int nDays = Integer.parseInt(item.getFirstDateOrDays());
                    if (earlyDays < nDays) {
                        earlyDays = nDays;
                        curType = type;
                        curScope = scope;
                    }
                } else if (5 == type) { // 第5种: 几点之前/之后
                    if (5 > curType) {
                        continue;
                    }
                    curType = type;
                    curScope = scope;
                }

            }
        }
        if (bFound) {
            buffer.append("预订成功后，凡是");
            StringBuffer strScope = new StringBuffer().append("<font color=red>");
            if (curScope == ModifyScopeType.CANCEL) {
                strScope.append("取消");
            } else if (curScope == ModifyScopeType.MODIFY) {
                strScope.append("修改");
            } else if (curScope == ModifyScopeType.BOTH) {
                strScope.append("取消/修改");
            }
            strScope.append("</font>");
            if (2 == curType || 3 == curType) {
                strScope.append("请在" + DateUtil.dateToString(DateUtil.getDate(earlyDate, -1))
                    + "之前通知我们");
            } else if (4 == curType) {
                strScope.append("请提前" + (earlyDays + 1) + "天通知我们");
            } else if (5 == curType) {
                strScope.append("请提前1天通知我们");
            }
            if (1 != curType) {
                buffer.append(strScope).append(",否则将按酒店规定扣除相应担保金额。");
            } else {
                buffer.append(strScope).append(",将按酒店规定扣除相应担保金额。");
            }
            liRes.add(buffer.toString());
        }
        return liRes;
    }

    /**
     * 
     * TODO 艺龙担保取消修改条款提示信息.
     * @param liRes
     * @param li
     * @return
     */
	@SuppressWarnings("unchecked")
	private List getElModifyCancelStr(List liRes,List<OrAssureItemEvery> li){				
		for(OrAssureItemEvery item : li){
			StringBuffer buffer = new StringBuffer();
    		int type = Integer.parseInt(item.getType());
    		String cancelDate = item.getFirstDateOrDays();
    		String cancelTime = item.getFirstTime();
    		String cancelStr = cancelDate+" "+cancelTime;
    		if(1 != type){
    			buffer.append("需<font color=red>取消或修改</font>本次预订，请您务必于 ").append(cancelStr).append(" 前致电40066 40066提出变更，否则将按酒店规定比例扣取您的担保金额。");
    		}else{
    			buffer.append("该房型一旦预订并确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定比例扣取您的担保金额。");
    		}
    		liRes.add(buffer.toString());
    	}
    	return liRes;
    }
    
    /**
     * v2.92 在修改订单页面,获取取消/修改规则字符串,需求改变:要求显示跟网站同步
     * 
     * @author guzhijie 2009-08-06
     * @param reservationID
     * @param sumRmb
     * @return
     */
    public List getModifyCancelStr2(OrOrder order, long reservationID, double sumRmb) {
        OrReservation reserv = (OrReservation) orOrderDao.load(OrReservation.class, reservationID);
        List liRes = new ArrayList();
        if (null == reserv) {
            return liRes;
        }
        Date curDate = null;
        boolean bFound = false;
        List<OrAssureItemEvery> li = reserv.getAssureList();
        StringBuffer buffer = new StringBuffer();

        int curScope = 0;
        int curType = 1000;
        int hours = 0;
        int minutes = 0;
        long milliseconds = 0;
        Date earlyDateTwoOrThree = DateUtil.getDate("2999-12-12 00:00");
        int earlyDays = -1;
        Date earlyDateFour = DateUtil.getDate("2999-12-12 00:00");
        Date earlyDateFive = DateUtil.getDate("2999-12-12 00:00");
        
        //添加艺龙担保取消或修改提示信息
        if(9 == order.getChannel()){
        	if(reserv.isUnCondition() || reserv.isOverTimeAssure() || reserv.isRoomsAssure()){
        		return getElModifyCancelStr(liRes, li);
        	}else {
        		return liRes;
        	}
        }
        
        for (OrAssureItemEvery item : li) {
            Date tmpDate = item.getNight();
            if (null == curDate) {
                curDate = tmpDate;
            }
            if (tmpDate.after(curDate)) {
                if (bFound) {
                    break;
                } else {
                    curDate = tmpDate;
                }
            }
            // 取消或修改；扣款类型，不为空的校验；add by shengwei.zuo hotel2.6 2009-05-27
            if ((null != item.getType() && !("").equals(item.getType()))
                && (null != item.getScope() && !("").equals(item.getScope()))
                && (null != item.getDeductType() && !("").equals(item.getDeductType()))) {
                bFound = true;
                int type = Integer.parseInt(item.getType());
                int scope = Integer.parseInt(item.getScope());

                if (1 == type) { // 第1种: 凡是均需
                    curType = type;
                    curScope = scope;
                    break;
                } else if (2 == type || 3 == type) { // 第2种: 在几日几点至几日几点,第3种: 在几日几点至入住当日几点
                    String strDate = item.getFirstDateOrDays();
                    String strTime = item.getFirstTime();
                    if (StringUtil.isValidStr(strDate)) {
                        Date tDate = DateUtil.stringToDateMinute(strDate + " " + strTime);
                        Date tmpeDate = new Date(tDate.getTime() - 24 * 60 * 60 * 1000);
                        if (tmpeDate.before(earlyDateTwoOrThree)) {
                            earlyDateTwoOrThree = tmpeDate;
                        }
                        curType = type;
                        curScope = scope;
                    }
                } else if (4 == type) { // 第4种: 在入住日期前几天几点至几天几点
                    if (4 > curType) {
                        continue;
                    }
                    int nDays = Integer.parseInt(item.getFirstDateOrDays());
                    String sTime = item.getFirstTime();
                    if (earlyDays < nDays) {
                        earlyDays = nDays;
                        curType = type;
                        curScope = scope;
                        String[] sTimes = new String[2];
                        if (-1 < sTime.indexOf(":")) {
                            sTimes = sTime.split(":");
                        }
                        hours = Integer.parseInt(sTimes[0]);
                        minutes = Integer.parseInt(sTimes[1]);
                        milliseconds = (hours * 60 + minutes) * 60 * 1000;
//                        earlyDateFour = new Date(order.getCheckinDate().getTime() - (earlyDays + 1)
//                            * 24 * 60 * 60 * 1000 + milliseconds);
                        /* 生产bug1313 计算在入住日期前XX天X点至XX天X点 逻辑有误,原有逻辑计算到未来的日子 modify by chenjiajie 2010-04-09 begin */
                        //firstEarlyDate = 入住日期 - (在入住日期前几天 + 1) 后面加上小时
                        Date firstEarlyDate = DateUtil.getDate(order.getCheckinDate(), (earlyDays + 1) * -1);
                        earlyDateFour = new Date(firstEarlyDate.getTime() + milliseconds);
                        /* 生产bug1313 计算在入住日期前XX天X点至XX天X点 逻辑有误,原有逻辑计算到未来的日子 modify by chenjiajie 2010-04-09 end */
                    }
                } else if (5 == type) { // 第5种: 几点之前/之后
                    if (5 > curType) {
                        continue;
                    }
                    curType = type;
                    curScope = scope;
                    String sTime = item.getFirstTime();
                    String[] sTimes = new String[2];
                    if (-1 < sTime.indexOf(":")) {
                        sTimes = sTime.split(":");
                    }
                    hours = Integer.parseInt(sTimes[0]);
                    minutes = Integer.parseInt(sTimes[1]);
                    milliseconds = (hours * 60 + minutes) * 60 * 1000;
                    earlyDateFive = new Date(order.getCheckinDate().getTime() + milliseconds - 24
                        * 60 * 60 * 1000);
                }
            }
        }
        if (bFound) {

            if (2 == curType || 3 == curType) {
                isAllowCancelModify = Float.valueOf(((float) 
                    (earlyDateTwoOrThree.getTime() - DateUtil
                    .getSystemDate().getTime()) / DateUtil.millionSecondsOfDay + 1)).intValue();
            } else if (4 == curType) {
                isAllowCancelModify =Float.valueOf( ((float) 
                    (earlyDateFour.getTime() - DateUtil
                    .getSystemDate().getTime()) / DateUtil.millionSecondsOfDay + 1)).intValue();
            } else if (5 == curType) {
                isAllowCancelModify =Float.valueOf(((float) 
                    (earlyDateFive.getTime() - DateUtil
                    .getSystemDate().getTime()) / DateUtil.millionSecondsOfDay + 1)).intValue();
            } else if (1 == curType) {
                isAllowCancelModify = 1;
            }
            if (0 < isAllowCancelModify) {
                if (1 != curType) {
                    buffer.append("需<font color=red>取消或修改</font>本次预定，请您务必于");
                    StringBuffer strScope = new StringBuffer();
                    if (2 == curType || 3 == curType) {
                        strScope.append(DateUtil.datetimeToString(earlyDateTwoOrThree));
                    } else if (4 == curType) {
                        strScope.append(DateUtil.datetimeToString(earlyDateFour));
                    } else if (5 == curType) {
                        strScope.append(DateUtil.datetimeToString(earlyDateFive));
                    }
                    buffer.append(strScope).append("前致电40066 40066提出变更,否则将按酒店规定比例扣取您的担保金额。");
                } else if (1 == curType) {
                    buffer.append("该房型一旦预定并确认成功将不接受免费");
                    StringBuffer strScope = new StringBuffer().append("<font color=red>");
                    if (curScope == ModifyScopeType.CANCEL) {
                        strScope.append("取消");
                    } else if (curScope == ModifyScopeType.MODIFY) {
                        strScope.append("修改");
                    } else if (curScope == ModifyScopeType.BOTH) {
                        strScope.append("取消/修改");
                    }
                    strScope.append("</font>");
                    strScope.append("，如需");
                    strScope.append("<font color=red>");
                    if (curScope == ModifyScopeType.CANCEL) {
                        strScope.append("取消");
                    } else if (curScope == ModifyScopeType.MODIFY) {
                        strScope.append("修改");
                    } else if (curScope == ModifyScopeType.BOTH) {
                        strScope.append("取消/修改");
                    }
                    strScope.append("</font>");
                    buffer.append(strScope);
                    buffer.append("将按酒店规定比例扣取您的担保金额");

                }
            } else {
                buffer.append("该房型一旦预订并确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定比例扣取您的担保金额。");
            }
            liRes.add(buffer.toString());
        }
        return liRes;
    }

    /**
     * v2.92 在修改订单页面,获取取消/修改规则字符串,需求改变:要求显示跟网站同步
     * 
     * @author guzhijie 2009-08-06
     * @param reservationID
     * @param sumRmb
     * @return
     */
    public List getHisModifyCancelStr2(HOrder hOrder, long reservationID, double sumRmb) {
        OrReservation reserv = (OrReservation) orOrderDao.load(OrReservation.class, reservationID);
        List liRes = new ArrayList();
        if (null == reserv) {
            return liRes;
        }
        Date curDate = null;
        boolean bFound = false;
        List<OrAssureItemEvery> li = reserv.getAssureList();
        StringBuffer buffer = new StringBuffer();

        int curScope = 0;
        int curType = 1000;
        int hours = 0;
        int minutes = 0;
        long milliseconds = 0;
        Date earlyDateTwoOrThree = DateUtil.getDate("2999-12-12 00:00");
        int earlyDays = -1;
        Date earlyDateFour = DateUtil.getDate("2999-12-12 00:00");
        Date earlyDateFive = DateUtil.getDate("2999-12-12 00:00");
        
      //添加艺龙担保取消或修改提示信息
        if(9 == hOrder.getChannel()){
        	return getElModifyCancelStr(liRes, li);
        }
        
        for (OrAssureItemEvery item : li) {
            Date tmpDate = item.getNight();
            if (null == curDate) {
                curDate = tmpDate;
            }
            if (tmpDate.after(curDate)) {
                if (bFound) {
                    break;
                } else {
                    curDate = tmpDate;
                }
            }
            // 取消或修改；扣款类型，不为空的校验；add by shengwei.zuo hotel2.6 2009-05-27
            if ((null != item.getType() && !("").equals(item.getType()))
                && (null != item.getScope() && !("").equals(item.getScope()))
                && (null != item.getDeductType() && !("").equals(item.getDeductType()))) {
                bFound = true;
                int type = Integer.parseInt(item.getType());
                int scope = Integer.parseInt(item.getScope());

                if (1 == type) { // 第1种: 凡是均需
                    curType = type;
                    curScope = scope;
                    break;
                } else if (2 == type || 3 == type) { // 第2种: 在几日几点至几日几点,第3种: 在几日几点至入住当日几点
                    String strDate = item.getFirstDateOrDays();
                    String strTime = item.getFirstTime();
                    if (StringUtil.isValidStr(strDate)) {
                        Date tDate = DateUtil.stringToDateMinute(strDate + " " + strTime);
                        Date tmpeDate = new Date(tDate.getTime() - 24 * 60 * 60 * 1000);
                        if (tmpeDate.before(earlyDateTwoOrThree)) {
                            earlyDateTwoOrThree = tmpeDate;
                        }
                        curType = type;
                        curScope = scope;
                    }
                } else if (4 == type) { // 第4种: 在入住日期前几天几点至几天几点
                    if (4 > curType) {
                        continue;
                    }
                    int nDays = Integer.parseInt(item.getFirstDateOrDays());
                    String sTime = item.getFirstTime();
                    if (earlyDays < nDays) {
                        earlyDays = nDays;
                        curType = type;
                        curScope = scope;
                        String[] sTimes = new String[2];
                        if (-1 < sTime.indexOf(":")) {
                            sTimes = sTime.split(":");
                        }
                        hours = Integer.parseInt(sTimes[0]);
                        minutes = Integer.parseInt(sTimes[1]);
                        milliseconds = (hours * 60 + minutes) * 60 * 1000;
//                        earlyDateFour = new Date(hOrder.getCheckinDate().getTime()
//                            - (earlyDays + 1) * 24 * 60 * 60 * 1000 + milliseconds);
                        /* 生产bug1313 计算在入住日期前XX天X点至XX天X点 逻辑有误,原有逻辑计算到未来的日子 modify by chenjiajie 2010-04-09 begin */
                        //firstEarlyDate = 入住日期 - (在入住日期前几天 + 1) 后面加上小时
                        Date firstEarlyDate = DateUtil.getDate(hOrder.getCheckinDate(), (earlyDays + 1) * -1);
                        earlyDateFour = new Date(firstEarlyDate.getTime() + milliseconds);
                        /* 生产bug1313 计算在入住日期前XX天X点至XX天X点 逻辑有误,原有逻辑计算到未来的日子 modify by chenjiajie 2010-04-09 end */
                    }
                } else if (5 == type) { // 第5种: 几点之前/之后
                    if (5 > curType) {
                        continue;
                    }
                    curType = type;
                    curScope = scope;
                    String sTime = item.getFirstTime();
                    String[] sTimes = new String[2];
                    if (-1 < sTime.indexOf(":")) {
                        sTimes = sTime.split(":");
                    }
                    hours = Integer.parseInt(sTimes[0]);
                    minutes = Integer.parseInt(sTimes[1]);
                    milliseconds = (hours * 60 + minutes) * 60 * 1000;
                    earlyDateFive = new Date(hOrder.getCheckinDate().getTime() + milliseconds - 24
                        * 60 * 60 * 1000);
                }
            }
        }
        if (bFound) {

            if (2 == curType || 3 == curType) {
                isAllowCancelModify = Float.valueOf( ((float) 
                    (earlyDateTwoOrThree.getTime() - DateUtil
                    .getSystemDate().getTime()) / DateUtil.millionSecondsOfDay + 1)).intValue();
            } else if (4 == curType) {
                isAllowCancelModify = Float.valueOf(((float) 
                    (earlyDateFour.getTime() - DateUtil
                    .getSystemDate().getTime()) / DateUtil.millionSecondsOfDay + 1)).intValue();
            } else if (5 == curType) {
                isAllowCancelModify = Float.valueOf(((float) 
                    (earlyDateFive.getTime() - DateUtil
                    .getSystemDate().getTime()) / DateUtil.millionSecondsOfDay + 1)).intValue();
            } else if (1 == curType) {
                isAllowCancelModify = 1;
            }
            if (0 < isAllowCancelModify) {
                if (1 != curType) {
                    buffer.append("需<font color=red>取消或修改</font>本次预定，请您务必于");
                    StringBuffer strScope = new StringBuffer();
                    if (2 == curType || 3 == curType) {
                        strScope.append(DateUtil.datetimeToString(earlyDateTwoOrThree));
                    } else if (4 == curType) {
                        strScope.append(DateUtil.datetimeToString(earlyDateFour));
                    } else if (5 == curType) {
                        strScope.append(DateUtil.datetimeToString(earlyDateFive));
                    }
                    buffer.append(strScope).append("前致电40066 40066提出变更,否则将按酒店规定比例扣取您的担保金额。");
                } else if (1 == curType) {
                    buffer.append("该房型一旦预定并确认成功将不接受免费");
                    StringBuffer strScope = new StringBuffer().append("<font color=red>");
                    if (curScope == ModifyScopeType.CANCEL) {
                        strScope.append("取消");
                    } else if (curScope == ModifyScopeType.MODIFY) {
                        strScope.append("修改");
                    } else if (curScope == ModifyScopeType.BOTH) {
                        strScope.append("取消/修改");
                    }
                    strScope.append("</font>");
                    strScope.append("，如需");
                    strScope.append("<font color=red>");
                    if (curScope == ModifyScopeType.CANCEL) {
                        strScope.append("取消");
                    } else if (curScope == ModifyScopeType.MODIFY) {
                        strScope.append("修改");
                    } else if (curScope == ModifyScopeType.BOTH) {
                        strScope.append("取消/修改");
                    }
                    strScope.append("</font>");
                    buffer.append(strScope);
                    buffer.append("将按酒店规定比例扣取您的担保金额");

                }
            } else {
                buffer.append("该房型一旦预订并确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定比例扣取您的担保金额。");
            }
            liRes.add(buffer.toString());
        }
        return liRes;
    }

    /**
     * 根据支付方式和面付/预付的取消修改规则表的id列表，获得取消修改规则的信息
     * 
     * @author chenkeming Mar 1, 2009 11:13:01 PM
     * @param payMethod
     * @param itemsStr
     * @return
     */
    private List getModifyCancelItem(String payMethod, String itemsStr) {
        List li = new ArrayList();
        if (!StringUtil.isValidStr(itemsStr)) {
            return li;
        }
        StringBuffer hql = new StringBuffer();
        hql.append("select type,scope,DEDUCT_TYPE,percentage,FIRST_DATE_OR_DAYS,"
            + "FIRST_TIME,SECOND_DATE_OR_DAYS,SECOND_TIME, ");
        if (!PayMethod.PRE_PAY.equals(payMethod)) {
            hql.append("EVERYDAY_ASSURE_CLAUSE_ID from HTL_ASSURE_ITEM_EVERYDAY "
                + "where EVERYDAY_ASSURE_CLAUSE_ID in (");
        } else {
            hql.append("EVERYDAY_PREPAY_CLAUSE_ID from HTL_PREPAY_ITEM_EVERYDAY "
                + "where EVERYDAY_PREPAY_CLAUSE_ID in (");
        }
        hql.append(itemsStr);
        hql.append(") ");
        if (!PayMethod.PRE_PAY.equals(payMethod)) {
            hql.append("order by EVERYDAY_ASSURE_CLAUSE_ID,type");
        } else {
            hql.append("order by EVERYDAY_PREPAY_CLAUSE_ID,type");
        }

        return orOrderDao.queryByNativeSQL(hql.toString(), null);
    }

    /**
     * 新下单保存时从本部获取修改取消规则信息
     * 
     * @author chenkeming Mar 1, 2009 11:27:30 PM
     * @param order
     * @param itemsStr
     */
    public void fillAssureItemEvery(OrOrder order, String itemsStr) {
        if (null == itemsStr) {
            return;
        }
        String[] pairs = itemsStr.split("/");
        // int[] dayIndexs = new int[pairs.length];
        StringBuffer itemsBuf = new StringBuffer();
        Map dayIndexMap = new HashMap();
        for (int i = 0; i < pairs.length; i++) {
            String[] tmpStrs = pairs[i].split(",");
            if (0 < i) {
                itemsBuf.append(",");
            }
            itemsBuf.append(tmpStrs[0]);
            // dayIndexs[i] = Integer.parseInt(tmpStrs[1]);
            dayIndexMap.put(Long.valueOf(tmpStrs[0]), Integer.parseInt(tmpStrs[1]));
        }

        List<Object[]> li = getModifyCancelItem(order.getPayMethod(), itemsBuf.toString());
        if (!li.isEmpty()) {
            Date checkIn = order.getCheckinDate();
            OrReservation reserv = order.getReservation();
            // int i = -1;
            // long curId = 0;
            for (Object[] item : li) {
                OrAssureItemEvery assureItem = new OrAssureItemEvery();
                long newId = Long.parseLong(item[8].toString());
                Integer dayIndex = (Integer) dayIndexMap.get(newId);
                /*
                 * if(curId != newId) { i ++; curId = newId; }
                 */
                if (null != item[0]) {
                    assureItem.setType(item[0].toString());
                }
                if (null != item[1]) {
                    assureItem.setScope(item[1].toString());
                }
                assureItem.setDeductType(null == item[2] ? String.valueOf(DeductType.ALL_DAY)
                    : item[2].toString());
                if (null != item[3]) {
                    assureItem.setPercentage(item[3].toString());
                }
                int type = Integer.parseInt(item[0].toString());
                if (2 == type || 4 == type) { // 第2种: 在几日几点至几日几点 第4种: 在入住日期前几天几点至几天几点
                    assureItem.setFirstDateOrDays(item[4].toString());
                    assureItem.setFirstTime(item[5].toString());
                    assureItem.setSecondDateOrDays(item[6].toString());
                    assureItem.setSecondTime(item[7].toString());
                } else if (3 == type) { // 第3种: 在几日几点至入住当日几点
                    assureItem.setFirstDateOrDays(item[4].toString());
                    assureItem.setFirstTime(item[5].toString());
                    assureItem.setSecondTime(item[7].toString());
                } else if (5 == type) { // 第5种: 几点之前/之后
                    assureItem.setFirstTime(item[5].toString());
                }
                // assureItem.setNight(DateUtil.getDate(checkIn, dayIndexs[i]));
                assureItem.setNight(DateUtil.getDate(checkIn, null != dayIndex ? dayIndex
                    .intValue() : 0));
                assureItem.setReserv(reserv);
                reserv.getAssureList().add(assureItem);
            }
        }

    }

    /**
     * 新下订单时获取取消/修改规则显示字符串信息,不包含具体金额数
     * 
     * @author chenkeming Feb 24, 2009 5:53:24 PM
     * @param payMethod
     * @param itemsStr
     * @return
     */
    /*
     * public List getModifyCancelStrNew(String payMethod, String itemsStr) {
     * 
     * String[] pairs = itemsStr.split("/"); StringBuffer itemsBuf = new StringBuffer(); for(int
     * i=0; i<pairs.length; i++) { String[] tmpStrs = pairs[i].split(","); if(i > 0) {
     * itemsBuf.append(","); } itemsBuf.append(tmpStrs[0]); }
     * 
     * List liRes = new ArrayList(); List<Object[]> li = getModifyCancelItem(payMethod,
     * itemsBuf.toString()); if(li.isEmpty()) { return liRes; }
     * 
     * int type = 0; int scope = 0; int deductType = 0; for(Object[] item : li) { StringBuffer
     * buffer = new StringBuffer();
     * 
     * //取消或修改；扣款类型，不为空的校验；add by shengwei.zuo hotel2.6 2009-05-27 if((item[0]!=null &&
     * !item[0].equals(""))&&(item[1]!=null && !item[1].equals(""))&&(item[2]!=null &&
     * !item[2].equals(""))){
     * 
     * //预定规则进行不为空的判断 add by shengwei.zuo HotelV2.6 begin type=(item[0]!=null &&
     * !item[0].equals(""))?Integer.parseInt(item[0].toString()):0;
     * 
     * scope=(item[1]!=null && !item[1].equals(""))?Integer.parseInt(item[1].toString()):0;
     * 
     * deductType=(item[2]!=null && !item[2].equals(""))?Integer.parseInt(item[2].toString()):0;
     * 
     * //预定规则进行不为空的判断 add by shengwei.zuo HotelV2.6 end String strMoney = (String)item[3];
     * //modified by lixiaoyong hotelcc v2.6 屏蔽价格显示为null strMoney = (strMoney!=null)?strMoney:"0";
     * String strScope = "<font color=red>"; if(scope == ModifyScopeType.CANCEL) { strScope = "取消";
     * } else if(scope == ModifyScopeType.MODIFY) { strScope = "修改"; } else if(scope ==
     * ModifyScopeType.BOTH){ strScope = "取消/修改"; } strScope += "</font>"; String strDeduct = "";
     * if(deductType == DeductType.FIRST_DAY) { strDeduct = "订单首日金额"; } else if(deductType ==
     * DeductType.ALL_DAY) { strDeduct = "订单全额金额"; } else if(deductType ==
     * DeductType.FIRST_DAY_PERCENTAGE) { strDeduct = "订单首日金额的" + strMoney + "%"; } else
     * if(deductType == DeductType.ALL_DAY_PERCENTAGE) { strDeduct = "订单金额的" + strMoney + "%"; }
     * buffer.append("酒店确认成功后，"); if(type == 1) { // 第1种: 凡是均需 buffer.append("凡是"); } else if(type
     * == 2) { // 第2种: 在几日几点至几日几点 buffer.append("在<font color=red>").append(item[4] + " " +
     * item[5]); buffer.append("</font>至<font color=red>").append(item[6] + " " + item[7] +
     * "</font>"); } else if(type == 3) { // 第3种: 在几日几点至入住当日几点
     * buffer.append("在<font color=red>").append(item[4] + " " + item[5] + "</font>");
     * buffer.append("至入住当日<font color=red>").append(item[7] + "</font>"); } else if(type == 4) { //
     * 第4种: 在入住日期前几天几点至几天几点 buffer.append("在入住日期前<font color=red>").append(item[4] + "天" + item[5] +
     * "</font>"); buffer.append("至<font color=red>").append(item[6] + "天" + item[7] + "</font>"); }
     * else if(type == 5){ // 第5种: 几点之前/之后 buffer.append("在<font color=red>").append(item[5]);
     * buffer.append("</font>之后"); } buffer.append(strScope).append(",将按酒店规定扣取").append(strDeduct);
     * liRes.add(buffer.toString());
     * 
     * } } return liRes; }
     */

    /**
     * v2.6 新下订单时获取取消/修改规则显示字符串信息,不包含具体金额数,需求改变后
     * 
     * @author chenkeming Jun 11, 2009 3:56:10 PM
     * @param payMethod
     * @param itemsStr
     * @return
     */
    public List getModifyCancelStrNew1(String payMethod, String itemsStr) {
        String[] pairs = itemsStr.split("/");
        StringBuffer itemsBuf = new StringBuffer();
        Map dayIndexMap = new HashMap();
        for (int i = 0; i < pairs.length; i++) {
            String[] tmpStrs = pairs[i].split(",");
            if (0 < i) {
                itemsBuf.append(",");
            }
            itemsBuf.append(tmpStrs[0]);
            dayIndexMap.put(Long.valueOf(tmpStrs[0]), Integer.parseInt(tmpStrs[1]));
        }

        List liRes = new ArrayList();
        List<Object[]> li = getModifyCancelItem(payMethod, itemsBuf.toString());
        if (li.isEmpty()) {
            return liRes;
        }

        List<Object[]> liNew = new ArrayList<Object[]>();
        for (Object[] item : li) {
            Object[] newItem = new Object[10];
            for (int i = 0; 9 > i; i++) {
                newItem[i] = item[i];
            }
            Integer lIndex = (Integer) dayIndexMap.get(Long.parseLong(item[8].toString()));
            newItem[9] = null != lIndex ? lIndex.intValue() : 0;
            liNew.add(newItem);
        }
        Collections.sort(liNew, compModCanItemNew);

        int curIndex = -1;
        boolean bFound = false;
        StringBuffer buffer = new StringBuffer();
        int curScope = 0;
        int curType = 1000;
        Date earlyDate = DateUtil.getDate("2999-12-12");
        int earlyDays = -1;
        for (Object[] item : liNew) {
            int tmpIndex = Integer.parseInt(item[9].toString());
            if (-1 == curIndex) {
                curIndex = tmpIndex;
            }
            if (tmpIndex > curIndex) {
                if (bFound) {
                    break;
                } else {
                    curIndex = tmpIndex;
                }
            }
            String sType = null != item[0] ? item[0].toString() : "";
            String sScope = null != item[1] ? item[1].toString() : "";
            // 取消或修改；扣款类型，不为空的校验；add by shengwei.zuo hotel2.6 2009-05-27
            if (StringUtil.isValidStr(sType) && StringUtil.isValidStr(sScope)) {
                bFound = true;
                int type = Integer.parseInt(sType);
                int scope = Integer.parseInt(sScope);

                if (1 == type) { // 第1种: 凡是均需
                    curType = type;
                    curScope = scope;
                    break;
                } else if (2 == type || 3 == type) { // 第2种: 在几日几点至几日几点,第3种: 在几日几点至入住当日几点
                    String strDate = item[4].toString();
                    if (StringUtil.isValidStr(strDate)) {
                        Date tmpDate = DateUtil.getDate(strDate);
                        if (tmpDate.before(earlyDate)) {
                            earlyDate = tmpDate;
                        }
                        curType = type;
                        curScope = scope;
                    }
                } else if (4 == type) { // 第4种: 在入住日期前几天几点至几天几点
                    if (4 > curType) {
                        continue;
                    }
                    int nDays = Integer.parseInt(item[4].toString());
                    if (earlyDays < nDays) {
                        earlyDays = nDays;
                        curType = type;
                        curScope = scope;
                    }
                } else if (5 == type) { // 第5种: 几点之前/之后
                    if (5 > curType) {
                        continue;
                    }
                    curType = type;
                    curScope = scope;
                }

            }
        }
        if (bFound) {
            buffer.append("预订成功后，凡是");
            StringBuffer strScope = new StringBuffer().append("<font color=red>");
            if (curScope == ModifyScopeType.CANCEL) {
                strScope.append("取消");
            } else if (curScope == ModifyScopeType.MODIFY) {
                strScope.append("修改");
            } else if (curScope == ModifyScopeType.BOTH) {
                strScope.append("取消/修改");
            }
            strScope.append("</font>");
            if (2 == curType || 3 == curType) {
                strScope.append("请在" + DateUtil.dateToString(DateUtil.getDate(earlyDate, -1))
                    + "之前通知我们");
            } else if (4 == curType) {
                strScope.append("请提前" + (earlyDays + 1) + "天通知我们");
            } else if (5 == curType) {
                strScope.append("请提前1天通知我们");
            }
            if (1 != curType) {
                buffer.append(strScope).append(",否则将按酒店规定扣除相应担保金额。");
            } else {
                buffer.append(strScope).append(",将按酒店规定扣除相应担保金额。");
            }
            liRes.add(buffer.toString());

        }
        return liRes;
    }

    /**
     * v2.92 新下订单时获取取消/修改规则显示字符串信息,不包含具体金额数,需求改变后. 字符串信息按需求直接显示在页面。此方法对字符串进行重新组装
     * 
     * @author guzhijie 2009-08-04
     * @param payMethod
     * @param itemsStr
     * @param horder
     * @return
     */

    public List getCancelModifyStrNew(OrOrder order, String payMethod, String itemsStr) {
        String[] pairs = itemsStr.split("/");
        StringBuffer itemsBuf = new StringBuffer();
        Map dayIndexMap = new HashMap();
        for (int i = 0; i < pairs.length; i++) {
            String[] tmpStrs = pairs[i].split(",");
            if (0 < i) {
                itemsBuf.append(",");
            }
            itemsBuf.append(tmpStrs[0]);
            dayIndexMap.put(Long.valueOf(tmpStrs[0]), Integer.parseInt(tmpStrs[1]));
        }

        List liRes = new ArrayList();
        List<Object[]> li = getModifyCancelItem(payMethod, itemsBuf.toString());
        if (li.isEmpty()) {
            return liRes;
        }

        List<Object[]> liNew = new ArrayList<Object[]>();
        for (Object[] item : li) {
            Object[] newItem = new Object[10];
            for (int i = 0; 9 > i; i++) {
                newItem[i] = item[i];
            }
            Integer lIndex = (Integer) dayIndexMap.get(Long.parseLong(item[8].toString()));
            newItem[9] = null != lIndex ? lIndex.intValue() : 0;
            liNew.add(newItem);
        }
        Collections.sort(liNew, compModCanItemNew);

        int curIndex = -1;
        boolean bFound = false;
        StringBuffer buffer = new StringBuffer();
        int curScope = 0;
        int curType = 1000;
        int hours = 0;
        int minutes = 0;
        long milliseconds = 0;

        Date earlyDateTwoOrThree = DateUtil.getDate("2999-12-12 00:00");
        int earlyDays = -1;
        Date earlyDateFour = DateUtil.getDate("2999-12-12 00:00");
        Date earlyDateFive = DateUtil.getDate("2999-12-12 00:00");
        for (Object[] item : liNew) {
            int tmpIndex = Integer.parseInt(item[9].toString());
            if (-1 == curIndex) {
                curIndex = tmpIndex;
            }
            if (tmpIndex > curIndex) {
                if (bFound) {
                    break;
                } else {
                    curIndex = tmpIndex;
                }
            }
            String sType = null != item[0] ? item[0].toString() : "";
            String sScope = null != item[1] ? item[1].toString() : "";
            // 取消或修改；扣款类型，不为空的校验；add by shengwei.zuo hotel2.6 2009-05-27
            if (StringUtil.isValidStr(sType) && StringUtil.isValidStr(sScope)) {
                bFound = true;
                int type = Integer.parseInt(sType);
                int scope = Integer.parseInt(sScope);

                if (1 == type) { // 第1种: 凡是均需
                    curType = type;
                    curScope = scope;
                    break;
                } else if (2 == type || 3 == type) { // 第2种: 在几日几点至几日几点,第3种: 在几日几点至入住当日几点
                    String strDate = item[4].toString();
                    String strTime = item[5].toString();
                    if (StringUtil.isValidStr(strDate)) {
                        Date tDate = DateUtil.stringToDateMinute(strDate + " " + strTime);
                        Date tmpDate = new Date(tDate.getTime() - 24 * 60 * 60 * 1000);
                        if (tmpDate.before(earlyDateTwoOrThree)) {
                            earlyDateTwoOrThree = tmpDate;
                        }
                        curType = type;
                        curScope = scope;
                    }
                } else if (4 == type) { // 第4种: 在入住日期前几天几点至几天几点
                    if (4 > curType) {
                        continue;
                    }
                    
                    int nDays = Integer.parseInt(item[4].toString());
                    String sTime = item[5].toString();
                    if (earlyDays < nDays) {
                        earlyDays = nDays;
                        curType = type;
                        curScope = scope;
                        String[] sTimes = new String[2];
                        if (-1 < sTime.indexOf(":")) {
                            sTimes = sTime.split(":");
                        }
                        hours = Integer.parseInt(sTimes[0]);
                        minutes = Integer.parseInt(sTimes[1]);
                        milliseconds = (hours * 60 + minutes) * 60 * 1000;
//                      earlyDateFour = new Date(order.getCheckinDate().getTime() - (earlyDays + 1)
//                      * 24 * 60 * 60 * 1000 + milliseconds);
                        /* 生产bug1313 计算在入住日期前XX天X点至XX天X点 逻辑有误,原有逻辑计算到未来的日子 modify by chenjiajie 2010-04-09 begin */
                        //firstEarlyDate = 入住日期 - (在入住日期前几天 + 1) 后面加上小时
                        Date firstEarlyDate = DateUtil.getDate(order.getCheckinDate(), (earlyDays + 1) * -1);
                        earlyDateFour = new Date(firstEarlyDate.getTime() + milliseconds);
                        /* 生产bug1313 计算在入住日期前XX天X点至XX天X点 逻辑有误,原有逻辑计算到未来的日子 modify by chenjiajie 2010-04-09 end */
                    }
                } else if (5 == type) { // 第5种: 几点之前/之后
                    if (5 > curType) {
                        continue;
                    }
                    String sTime = item[5].toString();
                    String[] sTimes = new String[2];
                    if (-1 < sTime.indexOf(":")) {
                        sTimes = sTime.split(":");
                    }
                    hours = Integer.parseInt(sTimes[0]);
                    minutes = Integer.parseInt(sTimes[1]);
                    milliseconds = (hours * 60 + minutes) * 60 * 1000;
                    earlyDateFive = new Date(order.getCheckinDate().getTime() + milliseconds - 24
                        * 60 * 60 * 1000);
                    curType = type;
                    curScope = scope;
                }

            }
        }
        if (bFound) {

            if (2 == curType || 3 == curType) {
                isAllowCancelModify = Float.valueOf(((float) 
                    (earlyDateTwoOrThree.getTime() - DateUtil
                    .getSystemDate().getTime()) / DateUtil.millionSecondsOfDay + 1)).intValue();
            } else if (4 == curType) {
                isAllowCancelModify =Float.valueOf(((float) 
                    (earlyDateFour.getTime() - DateUtil
                    .getSystemDate().getTime()) / DateUtil.millionSecondsOfDay + 1)).intValue();
            } else if (5 == curType) {
                isAllowCancelModify = Float.valueOf(((float) 
                    (earlyDateFive.getTime() - DateUtil
                    .getSystemDate().getTime()) / DateUtil.millionSecondsOfDay + 1)).intValue();
            } else if (1 == curType) {
                isAllowCancelModify = 1;
            }
            if (0 < isAllowCancelModify) {
                if (1 != curType) {
                    buffer.append("需<font color=red>取消或修改</font>本次预定，请您务必于");
                    StringBuffer strScope = new StringBuffer();
                    if (2 == curType || 3 == curType) {
                        strScope.append(DateUtil.datetimeToString(earlyDateTwoOrThree));
                    } else if (4 == curType) {
                        strScope.append(DateUtil.datetimeToString(earlyDateFour));
                    } else if (5 == curType) {
                        strScope.append(DateUtil.datetimeToString(earlyDateFive));
                    }
                    buffer.append(strScope).append("前致电40066 40066提出变更,否则将按酒店规定比例扣取您的担保金额。");
                } else if (1 == curType) {
                    buffer.append("该房型一旦预定并确认成功将不接受免费");
                    StringBuffer strScope = new StringBuffer().append("<font color=red>");
                    if (curScope == ModifyScopeType.CANCEL) {
                        strScope.append("取消");
                    } else if (curScope == ModifyScopeType.MODIFY) {
                        strScope.append("修改");
                    } else if (curScope == ModifyScopeType.BOTH) {
                        strScope.append("取消/修改");
                    }
                    strScope.append("</font>");
                    strScope.append("，如需");
                    strScope.append("<font color=red>");
                    if (curScope == ModifyScopeType.CANCEL) {
                        strScope.append("取消");
                    } else if (curScope == ModifyScopeType.MODIFY) {
                        strScope.append("修改");
                    } else if (curScope == ModifyScopeType.BOTH) {
                        strScope.append("取消/修改");
                    }
                    strScope.append("</font>");
                    buffer.append(strScope);
                    buffer.append("将按酒店规定比例扣取您的担保金额");

                }
            } else {
                buffer.append("该房型一旦预订并确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定比例扣取您的担保金额。");
            }
            liRes.add(buffer.toString());

        }
        return liRes;
    }

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
        String childRoomTypeId) {

        OrPriceDetail priceDetail = new OrPriceDetail();

        // 首先获取酒店的修改字段和担保计算规则

        /** 按酒店id和时间段查询计算规则 hotel2.9.2 add by chenjiajie 2009-08-16 begin **/
        List<HtlBookCaulClause> htlBookCaulClauseList = clauseManage.searchBookCaulByDateRange(
            hotelId, DateUtil.getDate(checkinDate), DateUtil.getDate(checkoutDate));
        /** 按酒店id和时间段查询计算规则 hotel2.9.2 add by chenjiajie 2009-08-16 end **/

        if (null == htlBookCaulClauseList || htlBookCaulClauseList.isEmpty()) {
            log.info("取不到HtlBookCaulClause对象，酒店id:" + hotelId);
            priceDetail.setDateStr("3");
        } else {
            // 取出计算规则中最严格的计算规则 hotel2.9.2 modify by chenjiajie 2009-08-16
            priceDetail.setDateStr(clauseManage.drawoutHtlBookCaulClause(htlBookCaulClauseList));
        }

        Date beginDate = DateUtil.getDate(checkinDate);
        List<HtlPreconcertItem> reservationList = hotelManage.queryReservationForCC(hotelId,
            beginDate, DateUtil.getDate(checkoutDate), childRoomTypeId);
        if (reservationList.isEmpty()) {
            return priceDetail;
        }

        // 预订担保预付条款总类
        HtlPreconcertItem htlPreconcertItem = null;
        // 酒店预订条款类
        HtlReservation htlReservation = null;
        // 预订必须连住日期类
        HtlReservCont htlReservCont = null;

        // 结算方法
        String banlanceType = "";
        // 预付日期
        Date prePayDate = beginDate;
        String oldAheadDateDay = "";
        StringBuffer allAssure = new StringBuffer();
        StringBuffer allAssureCondi = new StringBuffer();
        for (int i = 0; i < reservationList.size(); i++) {
            htlPreconcertItem = reservationList.get(i);
            Date validDate = htlPreconcertItem.getValidDate();
            int dayIndex = DateUtil.getDay(beginDate, validDate);
            boolean bUnCondition = false;

            // 每天的预订条款详情
            List<HtlReservation> htlReservationList = htlPreconcertItem.getHtlReservationList();
            // 每天的预订条款详情提前日期
            StringBuffer beforDateDay = new StringBuffer();
            // 每天的预订条款详情提前天数
            StringBuffer beforeDateNumDay = new StringBuffer();
            // 每天的预订条款详情的连住天数
            StringBuffer continueInDay = new StringBuffer();
            // 每天的预订条款详情中必住日期
            StringBuffer mustContainDate = new StringBuffer();
            // 预订条款详情中的每天最终的担保条件（面付）
            StringBuffer assureCondiDay = new StringBuffer();
            // 预订条款详情中的每天的担保类型（面付）
            String assureDay = "";
            // 预订条款详情中的每天的结算方法（预付）
            String balanceDay = "";
            // 预订条款详情中的每天的提前日期(预付)
            StringBuffer aheadDateDay = new StringBuffer();

            // 设置每天的预订条款详情(就是提前多少天预订等信息),和HtlPreconcertItem是一对一的
            for (Iterator reservItor = htlReservationList.iterator(); reservItor.hasNext();) {
                htlReservation = (HtlReservation) reservItor.next();
                if (0 == dayIndex) {
                    Date aheadDate = htlReservation.getMustAheadDate();
                    if (null != aheadDate) {
                        beforDateDay.append("必须在");
                        beforDateDay.append(DateUtil.dateToString(aheadDate) + " "
                            + StringUtil.toZeroIfNull(htlReservation.getMustAheadTime()));
                        beforDateDay.append("之前预订");
                        priceDetail.setBeforeTime(StringUtil.convertStringIfNull(beforDateDay
                            .toString()));
                    }
                    Long lTemp = htlReservation.getAheadDay();
                    if (null != lTemp && 0 <= lTemp.intValue()) {
                        beforeDateNumDay.append("提前");
                        beforeDateNumDay.append(lTemp + "天，并且在预订当天的"
                            + StringUtil.toZeroIfNull(htlReservation.getAheadTime()));
                        beforeDateNumDay.append("之前预订");
                        priceDetail.setBeforeDayNum(StringUtil.convertStringIfNull(beforeDateNumDay
                            .toString()));
                    }
                    lTemp = htlReservation.getContinueNights();
                    if (null != lTemp && 0 < lTemp.intValue()) {
                        continueInDay.append("入住期间必须连住");
                        continueInDay.append(lTemp);
                        continueInDay.append("晚");
                        priceDetail.setContinueDay(continueInDay.toString());
                    }
                    // 获取必住日期，和HtlReservation是多对一的
                    List<HtlReservCont> htlReservContList = htlReservation.getHtlReservacontList();
                    for (Iterator resContItor = htlReservContList.iterator();
                    resContItor.hasNext();) {
                        htlReservCont = (HtlReservCont) resContItor.next();
                        mustContainDate.append(DateUtil.dateToString(htlReservCont
                            .getContinueDate()));
                        if (1 < htlReservContList.size()) {
                            mustContainDate.append(",");
                        }
                        // 每天的预订条款详情中必住日期
                        priceDetail.setMustDate(StringUtil.convertStringIfNull(mustContainDate
                            .toString()));
                    }
                }

                // 一对一
                break;
            }

            // 当为面付时

            // 取当天担保条款, 和HtlPreconcertItem是一对一的
            List<HtlAssure> htlAssureList = htlPreconcertItem.getHtlAssureList();
            for (HtlAssure htlAssure : htlAssureList) {
                String sAssureType = htlAssure.getAssureType();
                int assureType = StringUtil.isValidStr(sAssureType) ? Integer.parseInt(sAssureType)
                    : 0;

                // 如果是第一天
                if (0 == dayIndex) {
                    // 担保类型中首日担保只判断第一天
                    if (assureType == GuaranteeType.FIRST_DAY) {
                        // 保类型首日担保担
                        assureDay = "首日担保";
                    }

                }
                // 担保条件超时担保只判断第一天
                String assureTime = htlAssure.getLatestAssureTime();
                if (StringUtil.isValidStr(assureTime) && !bUnCondition) {
                    // 担保条件超时担保
                    assureCondiDay.append("超时担保(");
                    assureCondiDay.append(assureTime);
                    assureCondiDay.append(") ");
                }

                // 判断最终的担保条件 超房数担保
                Long overRoomNumber = htlAssure.getOverRoomNumber();
                if (null != overRoomNumber && 0 < overRoomNumber.longValue() && !bUnCondition) {

                    // 担保条件超房数担保
                    assureCondiDay.append("超房数担保(");
                    assureCondiDay.append(overRoomNumber);
                    assureCondiDay.append(") ");
                }

                // 判断无条件担保
                if ("1".equals(htlAssure.getIsNotConditional()) && !bUnCondition) {
                    // 担保条件无条件担保
                    assureCondiDay.delete(0, assureCondiDay.length());
                    assureCondiDay.append("无条件担保");
                    bUnCondition = true;
                }

                // 担保类型 全额担保
                sAssureType = htlAssure.getAssureType();
                assureType = StringUtil.isValidStr(sAssureType) ? Integer.parseInt(sAssureType) : 0;
                if (assureType == GuaranteeType.ALL_DAY) {
                    // 担保类型全额担保
                    assureDay = "全额担保";
                }

                // 预订条款详情中的每天最终的担保条件
                String strCond = assureCondiDay.toString();
                String strDay = DateUtil.dateToString(DateUtil.getDate(beginDate, dayIndex));
                if (0 < strCond.length()) {
                    allAssureCondi.append(strDay + ":" + strCond + ",");
                }
                // 预订条款详情中的每天的担保类型
                if (StringUtil.isValidStr(assureDay)) {
                    allAssure.append(strDay + ":" + assureDay + ",");
                }

                break;
            }

            // 预付时
            List<HtlPrepayEveryday> htlPrepayEverydayList = htlPreconcertItem
                .getHtlPrepayEverydayList();
            // 每日预付条款和HtlPreconcertItem为一对一
            for (HtlPrepayEveryday htlPrepayEveryday : htlPrepayEverydayList) {

                // 结算方法
                if (StringUtil.isValidStr(htlPrepayEveryday.getBalanceType())
                    && !StringUtil.isValidStr(banlanceType)) {
                    banlanceType = htlPrepayEveryday.getBalanceType();
                }

                // add by shengwei.zuo begin 注释掉; 当不录入结算方法时，就为空，不要默认赋值。修正bug: TD 506
                // 如果不录入结算方法,默认为月结
                // if("".equals(banlanceType)) {
                // banlanceType = HotelBalanceMethod.SEND_BILL;
                // }
                // add by shengwei.zuo end 注释掉; 当不录入结算方法时，就为空，不要默认赋值。修正bug: TD 506

                // 校验banlanceType是否为空 add by shengwei.zuo 2009-04-07
                if (!banlanceType.equals("") && null != banlanceType) {

                    // 当一单一结时 预付日期还要再提前2天
                    if (Integer.parseInt(HotelBalanceMethod.COA) == Integer
                        .parseInt((banlanceType))) {
                        balanceDay = "客人到之前";
                        // 预付付款时限判断，当为提前日期
                        String timeLimitType = htlPrepayEveryday.getTimeLimitType();
                        if (HotelPayLimitType.BEFOREDATE.equals(timeLimitType)) {
                            Date limitDate = htlPrepayEveryday.getLimitDate();
                            String limitTime = htlPrepayEveryday.getLimitTime();
                            // 新取的提前日期-2比之前存的最提前日期还要早，更新这个总的最提前日期
                            if (0 < DateUtil.compare(DateUtil.getDate(limitDate,-2),prePayDate)) {
                                prePayDate = DateUtil.getDate(limitDate, -2);
                                aheadDateDay.delete(0, aheadDateDay.length());
                                aheadDateDay.append("在");
                                aheadDateDay.append(DateUtil.dateToString(DateUtil.getDate(
                                    limitDate, -2)));
                                aheadDateDay.append(limitTime);
                                aheadDateDay.append("之前");
                                oldAheadDateDay = aheadDateDay.toString();
                            }
                            // 当为提前天数
                        } else if (HotelPayLimitType.BEFOREDATENUM.equals(timeLimitType)) {
                            Long aheadDaysL = htlPrepayEveryday.getLimitAheadDays();
                            String aheadDaysTime = htlPrepayEveryday.getLimitAheadDaysTime();
                            // 提前时间点
                            if (null != aheadDaysL) {
                                int aheadDays = aheadDaysL.intValue();
                                // 新取的经过计算后的提前日期-2比之前存的最提前日期还要早，更新这个总的最提前日期
                                if (0 < DateUtil.compare(DateUtil.getDate(
                                    beginDate,-aheadDays - 2),prePayDate)) {
                                    prePayDate = DateUtil.getDate(beginDate, -aheadDays - 2);
                                    aheadDateDay.delete(0, aheadDateDay.length());
                                    aheadDateDay.append("在");
                                    aheadDateDay.append(DateUtil.dateToString(DateUtil.getDate(
                                        beginDate, -aheadDays - 2)));
                                    aheadDateDay.append(aheadDaysTime);// 提前时间点
                                    aheadDateDay.append("之前");
                                    oldAheadDateDay = aheadDateDay.toString();
                                }
                            }
                        }
                    }
                    // 当为月结时
                    else if (Integer.parseInt(HotelBalanceMethod.SEND_BILL) == Integer
                        .parseInt(banlanceType)) {
                        balanceDay = "月结房费";
                        String timeLimitType = htlPrepayEveryday.getTimeLimitType();
                        // 预付付款时限判断，当为提前日期
                        if (HotelPayLimitType.BEFOREDATE.equals(timeLimitType)) {
                            // 新取的提前日期比之前存的最提前日期还要早，更新这个最提前日期
                            Date limitDate = htlPrepayEveryday.getLimitDate();
                            String limitTime = htlPrepayEveryday.getLimitTime();
                            if (0 < DateUtil.compare(limitDate,prePayDate)) {
                                prePayDate = limitDate;
                                // 预订条款详情中的每天的提前日期
                                aheadDateDay.delete(0, aheadDateDay.length());
                                aheadDateDay.append("在");
                                aheadDateDay.append(DateUtil.dateToString(htlPrepayEveryday
                                    .getLimitDate()));
                                aheadDateDay.append(limitTime);// 提前时间点
                                aheadDateDay.append("之前");
                                oldAheadDateDay = aheadDateDay.toString();
                            }
                            // 当为提前天数
                        } else if (HotelPayLimitType.BEFOREDATENUM.equals(timeLimitType)) {
                            int aheadDays = htlPrepayEveryday.getLimitAheadDays().intValue();
                            String aheadDaysTime = htlPrepayEveryday.getLimitAheadDaysTime();
                            // 新取的经过计算后的提前日期比之前存的最提前日期还要早，更新这个最提前日期
                            if (0 < DateUtil.compare(DateUtil.getDate(
                                beginDate,-aheadDays),prePayDate)) {
                                prePayDate = DateUtil.getDate(beginDate, -aheadDays);
                                // 预订条款详情中的每天的提前日期
                                aheadDateDay.delete(0, aheadDateDay.length());
                                aheadDateDay.append("在");
                                aheadDateDay.append(DateUtil.dateToString(DateUtil.getDate(
                                    beginDate, -aheadDays)));
                                aheadDateDay.append(aheadDaysTime);
                                aheadDateDay.append("之前");
                                oldAheadDateDay = aheadDateDay.toString();
                            }
                        }
                    }

                }

                // 预订条款详情中的每天的结算方法
                priceDetail.setBalanceMode(StringUtil.convertStringIfNull(balanceDay));
                // 预订条款详情中的每天的提前日期
                priceDetail.setPrepayTime(StringUtil.convertStringIfNull(oldAheadDateDay));

                // 一对一
                break;
            }
        }
        // 预订条款详情中的最终的担保条件
        if (0 < allAssureCondi.length()) {
            priceDetail.setAssureCond(allAssureCondi.toString());
        }
        // 预订条款详情中的担保类型
        if (0 < allAssure.length()) {
            priceDetail.setAssureType(allAssure.toString());
        }

        return priceDetail;
    }

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
        String checkoutDate, String roomTypeId, String payMethod) {

        // 先查该房型的所有子房型
        String hql = "from HtlPriceType where roomType.ID=" + roomTypeId;
        List<HtlPriceType> li = orOrderDao.query(hql, null);
        List<OrPriceDetail> liRes = new ArrayList<OrPriceDetail>();
        int nSize = li.size();
        if (1 > nSize) {
            return liRes;
        }

        for (HtlPriceType priceType : li) {
            Date beginDate = DateUtil.getDate(checkinDate);
            List<HtlPreconcertItem> reservationList = hotelManage.queryReservationForCC(hotelId,
                beginDate, DateUtil.getDate(checkoutDate), priceType.getID().toString());
            if (reservationList.isEmpty()) {
                continue;
            }

            // 预订担保预付条款总类
            HtlPreconcertItem htlPreconcertItem = null;
            // 酒店预订条款类
            HtlReservation htlReservation = null;
            // 预订必须连住日期类
            HtlReservCont htlReservCont = null;

            boolean bPay = PayMethod.PRE_PAY.equals(payMethod) ? false : true;

            // 结算方法
            String banlanceType = "";
            // 预付日期
            Date prePayDate = beginDate;
            String oldAheadDateDay = "";
            StringBuffer allAssure = new StringBuffer();
            StringBuffer allAssureCondi = new StringBuffer();
            OrPriceDetail priceDetail = new OrPriceDetail();
            priceDetail.setDateStr("子房型:<font color=red>" + priceType.getPriceType() + "</font>");
            for (int i = 0; i < reservationList.size(); i++) {
                htlPreconcertItem = reservationList.get(i);
                Date validDate = htlPreconcertItem.getValidDate();
                int dayIndex = DateUtil.getDay(beginDate, validDate);
                boolean bUnCondition = false;

                // 每天的预订条款详情
                List<HtlReservation> htlReservationList = htlPreconcertItem.getHtlReservationList();
                // 每天的预订条款详情提前日期
                StringBuffer beforDateDay = new StringBuffer();
                // 每天的预订条款详情提前天数
                StringBuffer beforeDateNumDay = new StringBuffer();
                // 每天的预订条款详情的连住天数
                StringBuffer continueInDay = new StringBuffer();
                // 每天的预订条款详情中必住日期
                StringBuffer mustContainDate = new StringBuffer();
                // 预订条款详情中的每天最终的担保条件（面付）
                StringBuffer assureCondiDay = new StringBuffer();
                // 预订条款详情中的每天的担保类型（面付）
                String assureDay = "";
                // 预订条款详情中的每天的结算方法（预付）
                String balanceDay = "";
                // 预订条款详情中的每天的提前日期(预付)
                StringBuffer aheadDateDay = new StringBuffer();

                // 设置每天的预订条款详情(就是提前多少天预订等信息),和HtlPreconcertItem是一对一的
                for (Iterator reservItor = htlReservationList.iterator(); reservItor.hasNext();) {
                    htlReservation = (HtlReservation) reservItor.next();
                    if (0 == dayIndex) {
                        Date aheadDate = htlReservation.getMustAheadDate();
                        if (null != aheadDate) {
                            beforDateDay.append("必须在");
                            beforDateDay.append(DateUtil.dateToString(aheadDate) + " "
                                + StringUtil.toZeroIfNull(htlReservation.getMustAheadTime()));
                            beforDateDay.append("之前预订");
                            priceDetail.setBeforeTime(StringUtil.convertStringIfNull(beforDateDay
                                .toString()));
                        }
                        Long lTemp = htlReservation.getAheadDay();
                        if (null != lTemp && 0 <= lTemp.intValue()) {
                            beforeDateNumDay.append("提前");
                            beforeDateNumDay.append(lTemp + "天，并且在预订当天的"
                                + StringUtil.toZeroIfNull(htlReservation.getAheadTime()));
                            beforeDateNumDay.append("之前预订");
                            priceDetail.setBeforeDayNum(StringUtil
                                .convertStringIfNull(beforeDateNumDay.toString()));
                        }
                        lTemp = htlReservation.getContinueNights();
                        if (null != lTemp && 0 < lTemp.intValue()) {
                            continueInDay.append("入住期间必须连住");
                            continueInDay.append(lTemp);
                            continueInDay.append("晚");
                            priceDetail.setContinueDay(continueInDay.toString());
                        }
                        // 获取必住日期，和HtlReservation是多对一的
                        List<HtlReservCont> htlReservContList = htlReservation
                            .getHtlReservacontList();
                        for (Iterator resContItor = htlReservContList.iterator(); resContItor
                            .hasNext();) {
                            htlReservCont = (HtlReservCont) resContItor.next();
                            mustContainDate.append(DateUtil.dateToString(htlReservCont
                                .getContinueDate()));
                            if (1 < htlReservContList.size()) {
                                mustContainDate.append(",");
                            }
                            // 每天的预订条款详情中必住日期
                            priceDetail.setMustDate(StringUtil.convertStringIfNull(mustContainDate
                                .toString()));
                        }
                    }

                    // 一对一
                    break;
                }

                // 当为面付时
                if (bPay) {
                    // 取当天担保条款, 和HtlPreconcertItem是一对一的
                    List<HtlAssure> htlAssureList = htlPreconcertItem.getHtlAssureList();
                    for (HtlAssure htlAssure : htlAssureList) {
                        String sAssureType = htlAssure.getAssureType();
                        if (null != sAssureType && !sAssureType.equals("")) {
                            int assureType = StringUtil.isValidStr(sAssureType) ? Integer
                                .parseInt(sAssureType) : 0;

                            // 如果是第一天
                            if (0 == dayIndex) {
                                // 担保类型中首日担保只判断第一天
                                if (assureType == GuaranteeType.FIRST_DAY) {
                                    // 保类型首日担保担
                                    assureDay = "首日担保";
                                }

                                // 担保条件超时担保只判断第一天
                                String assureTime = htlAssure.getLatestAssureTime();
                                if (StringUtil.isValidStr(assureTime) && !bUnCondition) {
                                    // 担保条件超时担保
                                    assureCondiDay.append("超时担保(");
                                    assureCondiDay.append(assureTime);
                                    assureCondiDay.append(") ");
                                }

                            }
                            // 判断最终的担保条件 超房数担保
                            Long overRoomNumber = htlAssure.getOverRoomNumber();
                            if (null != overRoomNumber && 0 < overRoomNumber.longValue()
                                && !bUnCondition) {

                                // 担保条件超房数担保
                                assureCondiDay.append("超房数担保(");
                                assureCondiDay.append(overRoomNumber);
                                assureCondiDay.append(") ");
                            }
                            // 判断无条件担保
                            if ("1".equals(htlAssure.getIsNotConditional()) && !bUnCondition) {
                                // 担保条件无条件担保
                                assureCondiDay.delete(0, assureCondiDay.length());
                                assureCondiDay.append("无条件担保");
                                bUnCondition = true;
                            }
                            // 担保类型 全额担保
                            sAssureType = htlAssure.getAssureType();
                            if (null != sAssureType && !sAssureType.equals("")) {
                                assureType = StringUtil.isValidStr(sAssureType) ? Integer
                                    .parseInt(sAssureType) : 0;
                            }
                            if (assureType == GuaranteeType.ALL_DAY) {
                                // 担保类型全额担保
                                assureDay = "全额担保";
                            }
                            // 预订条款详情中的每天最终的担保条件
                            String strCond = assureCondiDay.toString();
                            String strDay = DateUtil.dateToString(DateUtil.getDate(beginDate,
                                dayIndex));
                            if (0 < strCond.length()) {
                                allAssureCondi.append(strDay + ":" + strCond + ",");
                            }
                            // 预订条款详情中的每天的担保类型
                            if (StringUtil.isValidStr(assureDay)) {
                                allAssure.append(strDay + ":" + assureDay + ",");
                            }
                        }

                        break;
                    }
                }
                // 预付时
                else {
                    List<HtlPrepayEveryday> htlPrepayEverydayList = htlPreconcertItem
                        .getHtlPrepayEverydayList();
                    // 每日预付条款和HtlPreconcertItem为一对一
                    for (HtlPrepayEveryday htlPrepayEveryday : htlPrepayEverydayList) {

                        // 结算方法
                        if (StringUtil.isValidStr(htlPrepayEveryday.getBalanceType())
                            && !StringUtil.isValidStr(banlanceType)) {
                            banlanceType = htlPrepayEveryday.getBalanceType();
                            // 当一单一结时 预付日期还要再提前2天
                            if (Integer.parseInt(HotelBalanceMethod.COA) == Integer
                                .parseInt((banlanceType))) {
                                balanceDay = "客人到之前";
                                // 预付付款时限判断，当为提前日期
                                String timeLimitType = htlPrepayEveryday.getTimeLimitType();
                                if (HotelPayLimitType.BEFOREDATE.equals(timeLimitType)) {
                                    Date limitDate = htlPrepayEveryday.getLimitDate();
                                    // 新取的提前日期-2比之前存的最提前日期还要早，更新这个总的最提前日期
                                    if (0 < DateUtil.compare(
                                        DateUtil.getDate(limitDate,-2),prePayDate)) {
                                        prePayDate = DateUtil.getDate(limitDate, -2);
                                        aheadDateDay.delete(0, aheadDateDay.length());
                                        aheadDateDay.append("在");
                                        aheadDateDay.append(DateUtil.dateToString(DateUtil.getDate(
                                            limitDate, -2)));
                                        aheadDateDay.append("之前");
                                        oldAheadDateDay = aheadDateDay.toString();
                                    }
                                    // 当为提前天数
                                } else if (HotelPayLimitType.BEFOREDATENUM.equals(timeLimitType)) {
                                    int aheadDays = htlPrepayEveryday.getLimitAheadDays()
                                        .intValue();
                                    // 新取的经过计算后的提前日期-2比之前存的最提前日期还要早，更新这个总的最提前日期
                                    if (0 < DateUtil.compare(
                                        DateUtil.getDate(beginDate,-aheadDays - 2),prePayDate)) {
                                        prePayDate = DateUtil.getDate(beginDate, -aheadDays - 2);
                                        aheadDateDay.delete(0, aheadDateDay.length());
                                        aheadDateDay.append("在");
                                        aheadDateDay.append(DateUtil.dateToString(DateUtil.getDate(
                                            beginDate, -aheadDays - 2)));
                                        aheadDateDay.append("之前");
                                        oldAheadDateDay = aheadDateDay.toString();
                                    }
                                }
                            }
                            // 当为月结时
                            else if (Integer.parseInt(HotelBalanceMethod.SEND_BILL) == Integer
                                .parseInt(banlanceType)) {
                                balanceDay = "月结房费";
                                String timeLimitType = htlPrepayEveryday.getTimeLimitType();
                                // 预付付款时限判断，当为提前日期
                                if (HotelPayLimitType.BEFOREDATE.equals(timeLimitType)) {
                                    // 新取的提前日期比之前存的最提前日期还要早，更新这个最提前日期
                                    Date limitDate = htlPrepayEveryday.getLimitDate();
                                    if (0 < DateUtil.compare(limitDate,prePayDate)) {
                                        prePayDate = limitDate;
                                        // 预订条款详情中的每天的提前日期
                                        aheadDateDay.delete(0, aheadDateDay.length());
                                        aheadDateDay.append("在");
                                        aheadDateDay.append(DateUtil.dateToString(htlPrepayEveryday
                                            .getLimitDate()));
                                        aheadDateDay.append("之前");
                                        oldAheadDateDay = aheadDateDay.toString();
                                    }
                                    // 当为提前天数
                                } else if (HotelPayLimitType.BEFOREDATENUM.equals(timeLimitType)) {
                                    int aheadDays = htlPrepayEveryday.getLimitAheadDays()
                                        .intValue();
                                    // 新取的经过计算后的提前日期比之前存的最提前日期还要早，更新这个最提前日期
                                    if (0 < DateUtil.compare(
                                        DateUtil.getDate(beginDate,-aheadDays),prePayDate)) {
                                        prePayDate = DateUtil.getDate(beginDate, -aheadDays);
                                        // 预订条款详情中的每天的提前日期
                                        aheadDateDay.delete(0, aheadDateDay.length());
                                        aheadDateDay.append("在");
                                        aheadDateDay.append(DateUtil.dateToString(DateUtil.getDate(
                                            beginDate, -aheadDays)));
                                        aheadDateDay.append("之前");
                                        oldAheadDateDay = aheadDateDay.toString();
                                    }
                                }
                            }

                        }

                        // 预订条款详情中的每天的结算方法
                        priceDetail.setBalanceMode(StringUtil.convertStringIfNull(balanceDay));
                        // 预订条款详情中的每天的提前日期
                        priceDetail.setPrepayTime(StringUtil.convertStringIfNull(oldAheadDateDay));

                        // 一对一
                        break;
                    }
                }
            }
            // 预订条款详情中的最终的担保条件
            if (0 < allAssureCondi.length()) {
                priceDetail.setAssureCond(allAssureCondi.toString());
            }
            // 预订条款详情中的担保类型
            if (0 < allAssure.length()) {
                priceDetail.setAssureType(allAssure.toString());
            }

            liRes.add(priceDetail);
        }

        return liRes;
    }

    /**
     * 计算订单的担保金额
     * 
     * @author chenkeming Feb 24, 2009 3:46:56 PM
     * @param order
     */
    private void calculateVouchPrice(OrOrder order) {
        OrReservation reserv = order.getReservation();
        String sRule = reserv.getClauseRule();
        List<OrOrderItem> li = order.getOrderItems();
        boolean falg = false;// 判断取第一次首日金额
        boolean fall = false;// 判断第一天担保是否为空
        double dailyPrice = 0;
        int quantity = order.getRoomQuantity();
        double totalPrice = 0.0;
        for (OrGuaranteeItem item : reserv.getGuarantees()) {
            if (("" + HotelCalcuAssuAmoType.TOTTINGUP).equals(sRule)) { // 默认累加判定
                int dayIndex = DateUtil.getDay(order.getCheckinDate(), item.getNight());
                int start = dayIndex * quantity;
                dailyPrice = 0;
                // add by shengwei.zuo 2009-05-11 hotel 2.6 修复订单修改时担保金额计算错误BUG begin
                String sAssureType = item.getAssureType();
                if (null != sAssureType && !sAssureType.equals("")) {
                    int assureType = StringUtil.isValidStr(sAssureType) ? Integer
                        .parseInt(sAssureType) : 0;

                    if (0 == dayIndex) {// 判断第一天的担保类型
                        if (assureType == GuaranteeType.FIRST_DAY
                            || assureType == GuaranteeType.ALL_DAY) {// 如果第一天为首日或者全额都加进金额
                            for (int i = start; i < start + quantity; i++) {
                                OrOrderItem orderItem = li.get(i);
                                dailyPrice += orderItem.getSalePrice();
                            }
                            double price = order.getRateId() * dailyPrice;
                            totalPrice += price;
                            continue;
                        }

                    } else {
                        if (assureType == GuaranteeType.ALL_DAY) {// 如果不是第一天的 也为全额担保也要加入计算
                            for (int i = start; i < start + quantity; i++) {
                                OrOrderItem orderItem = li.get(i);
                                dailyPrice += orderItem.getSalePrice();
                            }
                            double price = order.getRateId() * dailyPrice;
                            totalPrice += price;
                            continue;
                        }
                    }

                }
                // add by shengwei.zuo 2009-05-11 hotel 2.6 修复订单修改时担保金额计算错误BUG end

            } else if (("" + HotelCalcuAssuAmoType.ALLAMOUNT).equals(sRule)) {
                // 按全额判定
                int dayIndex = DateUtil.getDay(order.getCheckinDate(), item.getNight());
                String sAssureType = item.getAssureType();
                int assureType = StringUtil.isValidStr(sAssureType) ? Integer.parseInt(sAssureType)
                    : 0;
                if (null != sAssureType && !sAssureType.equals("")) {

                    if (0 == dayIndex) {
                        if (assureType == GuaranteeType.ALL_DAY) {
                            reserv.setReservSuretyPrice(order.getSumRmb());
                            return;
                        } else if (assureType == GuaranteeType.FIRST_DAY) {
                            totalPrice += order.getRateId() * reserv.getFirstPrice();
                            fall = true;
                            falg = true;
                            continue;
                        }

                    } else {
                        if (assureType == GuaranteeType.ALL_DAY) {
                            reserv.setReservSuretyPrice(order.getSumRmb());
                            return;
                        } else if (assureType == GuaranteeType.FIRST_DAY && !fall && !falg) {
                            totalPrice += order.getRateId() * reserv.getFirstPrice();
                            continue;
                        }

                    }

                }
            } else { // 按checkinday判定
                int dayIndex = DateUtil.getDay(order.getCheckinDate(), item.getNight());
                String sAssureType = item.getAssureType();
                int assureType = StringUtil.isValidStr(sAssureType) ? Integer.parseInt(sAssureType)
                    : 0;
                if (null != sAssureType && !sAssureType.equals("")) {// 只判断第一天而且担保类型不为空

                    if (0 == dayIndex
                        && (assureType == GuaranteeType.FIRST_DAY || 
                            assureType == GuaranteeType.ALL_DAY)) {

                        double price = order.getRateId() * reserv.getFirstPrice();
                        reserv.setReservSuretyPrice(OrderUtil.roundPrice(price));
                        return;

                    }

                }

            }
        }
        reserv.setReservSuretyPrice(OrderUtil.roundPrice(totalPrice));
    }

    /**
     * 新增订单时计算每间房订单的担保金额
     * 
     * @author chenkeming Feb 24, 2009 3:46:56 PM
     * @param order
     */
    public void calculateVouchPricePerRoom(OrOrder order) {
        OrReservation reserv = order.getReservation();
        String sRule = reserv.getClauseRule();
        boolean falg = false;// 判断取第一次首日金额
        boolean fall = false;// 判断第一天担保是否为空
        List<OrPriceDetail> liPrice = order.getPriceList();
        double totalPrice = 0.0;
        // 酒店2.6 add by shizhognwen 2009-05-26 新增首日担保的价格
        double firstPrice = 0.0;
        for (OrGuaranteeItem item : reserv.getGuarantees()) {
            if (("" + HotelCalcuAssuAmoType.TOTTINGUP).equals(sRule)) { // 默认累加判定
                int dayIndex = DateUtil.getDay(order.getCheckinDate(), item.getNight());
                OrPriceDetail priceDetail = liPrice.get(dayIndex);
                String sAssureType = item.getAssureType();
                if (null != sAssureType && !sAssureType.equals("")) {
                    int assureType = StringUtil.isValidStr(sAssureType) ? Integer
                        .parseInt(sAssureType) : 0;
                    if (0 == dayIndex) {// 判断第一天的担保类型
                        if (assureType == GuaranteeType.FIRST_DAY
                            || assureType == GuaranteeType.ALL_DAY) {// 如果第一天为首日或者全额都加进金额
                            totalPrice += order.getRateId() * priceDetail.getSalePrice();
                            fall = true;
                            continue;
                        }

                    } else {
                        // 如果第一天无担保，并且第二天后出现首日金额。这里的首日金额直接取第一天的价格
                        if (assureType == GuaranteeType.FIRST_DAY && !falg && !fall) {
                            totalPrice += order.getRateId() * reserv.getFirstPrice();
                            falg = true;
                            continue;
                        }

                        if (assureType == GuaranteeType.ALL_DAY) {// 如果不是第一天的 也为全额担保也要加入计算
                            totalPrice += order.getRateId() * priceDetail.getSalePrice();
                            continue;
                        }
                    }
                }
            } else if (("" + HotelCalcuAssuAmoType.ALLAMOUNT).equals(sRule)) { // 按全额判定
                int dayIndex = DateUtil.getDay(order.getCheckinDate(), item.getNight());
                String sAssureType = item.getAssureType();
                int assureType = StringUtil.isValidStr(sAssureType) ? Integer.parseInt(sAssureType)
                    : 0;
                if (null != sAssureType && !sAssureType.equals("")) {
                    if (0 == dayIndex) {
                        if (assureType == GuaranteeType.ALL_DAY) {
                            reserv.setReservSuretyPrice(order.getSumRmb());
                            return;
                        } else if (assureType == GuaranteeType.FIRST_DAY) {
                            totalPrice += order.getRateId() * reserv.getFirstPrice();
                            fall = true;
                            falg = true;
                            continue;
                        }
                    } else {
                        if (assureType == GuaranteeType.ALL_DAY) {
                            reserv.setReservSuretyPrice(order.getSumRmb());
                            return;
                        } else if (assureType == GuaranteeType.FIRST_DAY && !falg && !fall) {
                            totalPrice += order.getRateId() * reserv.getFirstPrice();
                            falg = true;
                            continue;
                        }
                    }
                }
                // 否则只能是首日
                if (assureType == GuaranteeType.FIRST_DAY && !falg) {
                    totalPrice += order.getRateId() * reserv.getFirstPrice();
                    firstPrice = reserv.getFirstPrice();
                    falg = true;
                    continue;
                }

            } else if (("" + HotelCalcuAssuAmoType.CHECKIN).equals(sRule)) { // 按checkinday判定
                int dayIndex = DateUtil.getDay(order.getCheckinDate(), item.getNight());
                String sAssureType = item.getAssureType();
                StringUtil.isValidStr(sAssureType);
                Integer.parseInt(sAssureType);
                if (0 == dayIndex && null != sAssureType && 
                    !sAssureType.equals("")) {// 只判断第一天而且担保类型不为空
                    if (null != item.getLatestAssureTime()) {// 如果第一天只是首日担保，则不管后面是否出现无条件，担保选项都不勾选
                        reserv.setNeedCredit(false);
                    }
                    double price = order.getRateId() * reserv.getFirstPrice();
                    reserv.setReservSuretyPrice(OrderUtil.roundPrice(price));
                    return;
                }
              //如果是艺龙判定 则按照艺龙的计算规则计算担保金额  add by huanglingfeng 2012-5-2 start
            } else if(("" + HotelCalcuAssuAmoType.ELONG_JUDGE).equals(sRule)){
            	//获取该订单价格类型的映射
            	ExMapping ex=exMappingDao.queryExPrice(order.getChildRoomTypeId());
            	//入住首日
            	Date checkinDate=order.getCheckinDate();
            	long checkinTime=checkinDate.getTime();
            	//获取艺龙担保条款
            	List<HtlElAssureRule> elRules = htlElAssureRuleDao.queryElAssureList(ex.getHotelcodeforchannel(), ex.getRateplancode(), checkinDate);
            	//是否满足担保条件
            	boolean isNeedAssure=false;
            	//担保类型1为首晚房费，2为全额房费
            	int vouchMoneyType=0;
            	//首晚星期
            	String weekOfDay=DateUtil.getWeekOfDate(checkinDate)+"";
            	//最小的ruleID
            	Long minRuleId=null;
            	//遍历满足此价格类型的所有担保条款，找出优先的担保条款（ruleID最小的优先）
            	Map<Long, HtlElAssureRule> ruleMap=new HashMap<Long, HtlElAssureRule>();
            	for(HtlElAssureRule rule:elRules){
            		ruleMap.put(rule.getRuleid(), rule);
            		if(minRuleId==null||minRuleId>rule.getRuleid()){
            			minRuleId=rule.getRuleid();
            		}
            	}
            	HtlElAssureRule minAssureRule=ruleMap.get(minRuleId);
            	if(minAssureRule!=null){
            		//首晚时间在担保开始结束日期之间，并且满足首晚星期在Weekset里面才需要担保
            		if(minAssureRule.getWeekset().contains(weekOfDay)){
            			isNeedAssure=true;
            			vouchMoneyType=Integer.valueOf(minAssureRule.getVouchmoneytype()+"");
            		}
            	}
            	//如果满足担保条件
            	if(isNeedAssure){
            		//如果是首晚担保，则只计算入住首晚的房费作为担保金额
            		if(vouchMoneyType==1){
            			reserv.setReservSuretyPrice(reserv.getFirstPrice());
            			//如果是全额担保，则计算一个房间的所有房费作为担保金额
            		}else if(vouchMoneyType==2){
            			reserv.setReservSuretyPrice(order.getSumRmb());
            		}
            		reserv.setNeedCredit(true);
            	}else{
            		reserv.setNeedCredit(false);
            	}
            	return;
            }//end
        }
        // add by shizhongwen 2009-05-26 增加首日当保金额
        reserv.setFirstPrice(OrderUtil.roundPrice(firstPrice));
        reserv.setReservSuretyPrice(OrderUtil.roundPrice(totalPrice));
    }

    /**
     * 用于在修改订单提交时，重新从本部预订条款里获取相关信息填充到<br>
     * OrReservation, OrPriceDetail, OrGuaranteeItem, OrAssureItemEvery实体类<br>
     * 包括获取以下内容:<br>
     * <li>酒店的修改字段和担保计算规则(OrReservation
     * 的clauseRule,modifyField) <li>每日的预订条款显示信息(在OrPriceDetail里) <li>
     * 预订条款显示信息(OrReservation的clauseStr) <li>担保(预订)的注意事项(OrReservation的creditRemark) <li>
     * 担保信息(包括明细):OrReservation的lateSuretyTime,assureLetter,needCredit,
     * unCondiction,overTimeAssure,roomsAssure,rooms以及guarantees <li>
     * 取消修改规则(OrReservation的assureList)
     * 
     * @author chenkeming Feb 20, 2009 8:54:35 AM
     * @param order
     * @param bForEdit
     */
    public void getReservationInfo(OrOrder order, boolean bForEdit) {

        // 首先获取酒店的修改字段和担保计算规则
        // String hql = "from HtlBookCaulClause where hotelId=" + order.getHotelId();
        // HtlBookCaulClause bookClause = (HtlBookCaulClause)orOrderDao.find(hql);

        /** 按酒店id和时间段查询计算规则 hotel2.9.2 add by chenjiajie 2009-08-16 begin **/
        List<HtlBookCaulClause> htlBookCaulClauseList = clauseManage.searchBookCaulByDateRange(
            order.getHotelId(), order.getCheckinDate(), order.getCheckoutDate());
        /** 按酒店id和时间段查询计算规则 hotel2.9.2 add by chenjiajie 2009-08-16 end **/

        /** 根据酒店id查询修改字段定义，返回一条记录 hotel 2.9.2 add by chenjiajie 2009-08-16 begin **/
        HtlBookModifyField htlBookModifyField = clauseManage.searchBookModifyFieldByHTLID(order
            .getHotelId());
        if (null == htlBookModifyField) {
            htlBookModifyField = new HtlBookModifyField();
            log.info("取不到htlBookModifyField对象，酒店id:" + order.getHotelId());
        }
        /** 根据酒店id查询修改字段定义，返回一条记录 hotel 2.9.2 add by chenjiajie 2009-08-16 end **/
        OrReservation reserv = order.getReservation();
        if (null == reserv) {
            reserv = new OrReservation();
            order.setReservation(reserv);
        }

        // 预订条款计算规则
        String sRule = "";
        int nRule;
        
    	if (null == htlBookCaulClauseList || htlBookCaulClauseList.isEmpty()) {
    		log.info("取不到HtlBookCaulClause对象，酒店id:" + order.getHotelId());
    		//如果是艺龙订单则使用 艺龙判定 add by huanglingfeng 2012-5-2 start
    		if(order.getChannel()==9){
    			reserv.setClauseRule("4");//end
    			sRule="4";
    		}else{
    			//其他渠道订单则默认累加判定
    			reserv.setClauseRule("3");
    		}
    		reserv.setModifyField(ModifyFieldType.CHECKOUTDATE);
    		nRule = 3;
    	} else {
    		// 取出计算规则中最严格的计算规则，如果没有计算规则，默认累加判定 hotel2.9.2 modify by chenjiajie
    		sRule = clauseManage.drawoutHtlBookCaulClause(htlBookCaulClauseList);
    		reserv.setClauseRule(sRule);
    		reserv.setModifyField(htlBookModifyField.getModifyField());
    		nRule = StringUtil.isValidStr(sRule) ? Integer.parseInt(sRule) : 0;
    	}
       

        List<HtlPreconcertItem> reservationList = hotelManage.queryReservationForCC(order
            .getHotelId(), order.getCheckinDate(), order.getCheckoutDate(), order
            .getChildRoomTypeId().toString());

        Date beginDate = order.getCheckinDate();

        // 预订担保预付条款总类
        HtlPreconcertItem htlPreconcertItem = null;
        // 酒店预订条款类
        HtlReservation htlReservation = null;
        // 预订必须连住日期类
        HtlReservCont htlReservCont = null;

        // 最终的担保条件 1超时担保 2超房数担保 3无条件担保 (判断最终当天是否需要担保)
        int vouchCondiction = 0;
        // 结算方法
        String banlanceType = "";
        // 预付日期
        Date prePayDate = beginDate;
        // 是否需要担保函
        Boolean isNeedLetter = false;
        List<OrPriceDetail> priceList = order.getPriceList();
        boolean bPay = !order.isPrepayOrder() && !order.isPayToPrepay();
        boolean bClearAssureList = false;
        boolean bClearGuarantees = false;
        boolean bOnlyClear = false;
        StringBuffer cancelModifyStr = new StringBuffer();

        List lModCanStr = new ArrayList();

        for (int i = 0; i < reservationList.size(); i++) {
            htlPreconcertItem = reservationList.get(i);
            Date validDate = htlPreconcertItem.getValidDate();
            int dayIndex = DateUtil.getDay(beginDate, validDate);
            boolean bUnCondition = false;
            OrPriceDetail priceDetail = priceList.get(dayIndex);
            priceDetail.setHasReserv(true);

            // 每天的预订条款详情
            List<HtlReservation> htlReservationList = htlPreconcertItem.getHtlReservationList();
            // 每天的预订条款详情提前日期
            StringBuffer beforDateDay = new StringBuffer();
            // 每天的预订条款详情提前天数
            StringBuffer beforeDateNumDay = new StringBuffer();
            // 每天的预订条款详情的连住天数
            StringBuffer continueInDay = new StringBuffer();
            // 每天的预订条款详情中必住日期
            StringBuffer restrictNights = new StringBuffer();
            // 每天的预订条款详情中必住日期
            StringBuffer mustContainDate = new StringBuffer();
            // 预订条款详情中的每天最终的担保条件（面付）
            StringBuffer assureCondiDay = new StringBuffer();
            // 预订条款详情中的每天的担保类型（面付）
            String assureDay = "";
            // 预订条款详情中的每天的结算方法（预付）
            String balanceDay = "";
            // 预订条款详情中的每天的提前日期(预付)
            StringBuffer aheadDateDay = new StringBuffer();

            // 设置每天的预订条款详情(就是提前多少天预订等信息),和HtlPreconcertItem是一对一的
            for (Iterator reservItor = htlReservationList.iterator(); reservItor.hasNext();) {
                htlReservation = (HtlReservation) reservItor.next();
                Date aheadDate = htlReservation.getMustAheadDate();
                if (null != aheadDate) {
                    beforDateDay.append("必须在");
                    beforDateDay.append(DateUtil.dateToString(aheadDate) + " "
                        + StringUtil.toZeroIfNull(htlReservation.getMustAheadTime()));
                    beforDateDay.append("之前预订");
                    priceDetail.setBeforeTime(beforDateDay.toString());
                }
                Long lTemp = htlReservation.getAheadDay();
                if (null != lTemp && 0 <= lTemp.intValue()) {
                    beforeDateNumDay.append("提前");
                    beforeDateNumDay.append(lTemp + "天，并且在预订当天的"
                        + StringUtil.toZeroIfNull(htlReservation.getAheadTime()));
                    beforeDateNumDay.append("之前预订");
                    priceDetail.setBeforeDayNum(beforeDateNumDay.toString());
                }
                lTemp = htlReservation.getContinueNights();
                if (null != lTemp && 0 < lTemp.intValue()) {
                    continueInDay.append("入住期间必须连住");
                    continueInDay.append(lTemp);
                    continueInDay.append("晚");
                    priceDetail.setContinueDay(continueInDay.toString());
                }

                // Long minRestrictNights = htlReservation.getMinRestrictNights();
                Long maxRestrictNights = htlReservation.getMaxRestrictNights();
                /*
                 * if(minRestrictNights != null && minRestrictNights.intValue() > 0) {
                 * restrictNights.append("至少入住") .append(minRestrictNights.intValue())
                 * .append("间夜"); priceDetail.setRestrictNights(restrictNights.toString()); }
                 */
                if (null != maxRestrictNights && 0 < maxRestrictNights.intValue()) {
                    restrictNights.append("要入住").append(maxRestrictNights.intValue()).append("间夜");
                    priceDetail.setRestrictNights(restrictNights.toString());
                }

                // 获取必住日期，和HtlReservation是多对一的
                List<HtlReservCont> htlReservContList = htlReservation.getHtlReservacontList();
                for (Iterator resContItor = htlReservContList.iterator(); resContItor.hasNext();) {
                    htlReservCont = (HtlReservCont) resContItor.next();
                    mustContainDate.append(DateUtil.dateToString(htlReservCont.getContinueDate()));
                    if (1 < htlReservContList.size()) {
                        mustContainDate.append(",");
                    }
                    // 每天的预订条款详情中必住日期
                    priceDetail.setMustDate(mustContainDate.toString());
                }

                // 一对一
                break;
            }

            // 取首日预订注意事项
            if (0 == dayIndex) {
                String attention = htlPreconcertItem.getAttention();
                reserv.setCreditRemark(StringUtil.isValidStr(attention) ? StringUtil
                    .formatHtmlString(attention) : "");
                // 设置预订条款
                StringBuffer clauseStrBu = new StringBuffer();
                // hotel2.6 modify by chenjiajie 2009-03-27
                if (StringUtil.isValidStr(beforDateDay.toString())) {
                    clauseStrBu.append(beforDateDay);
                }
                // hotel2.6 modify by chenjiajie 2009-03-27
                if (StringUtil.isValidStr(beforeDateNumDay.toString())) {
                    clauseStrBu.append(",");
                    clauseStrBu.append(beforeDateNumDay);
                }
                // hotel2.6 modify by chenjiajie 2009-03-27
                if (StringUtil.isValidStr(continueInDay.toString())) {
                    clauseStrBu.append(",");
                    clauseStrBu.append(continueInDay);
                }
                // hotel2.6 modify by chenjiajie 2009-03-27
                if (StringUtil.isValidStr(mustContainDate.toString())) {
                    clauseStrBu.append(",");
                    clauseStrBu.append("必住日期为:");
                    clauseStrBu.append(mustContainDate);
                }
                // hotel2.6 当第一位是,则去除 modify by chenjiajie 2009-03-27
                if (null != clauseStrBu && 0 < clauseStrBu.length()) {
                    if (0 == clauseStrBu.indexOf(",")) {
                        String clauseStr = clauseStrBu.substring(1, clauseStrBu.length());
                        reserv.setClauseStr(clauseStr);
                    }
                } else {
                    reserv.setClauseStr(clauseStrBu.toString());
                }
            }

            // 当为面付时
            if (bPay) {

                log.info("为面付单时进入：");
                boolean assBo = false;
                // 取当天担保条款, 和HtlPreconcertItem是一对一的
                List<HtlAssure> htlAssureList = htlPreconcertItem.getHtlAssureList();
                for (HtlAssure htlAssure : htlAssureList) {
                    String sAssureType = htlAssure.getAssureType();
                    int assureType = StringUtil.isValidStr(sAssureType) ? Integer
                        .parseInt(sAssureType) : 0;

                    // 填充担保明细和取消修改规则明细
                    OrGuaranteeItem guarantee = new OrGuaranteeItem();
                    if (0 != assureType) { // 只有担保类型非空，才参与到担保金额的计算，才可能添加该担保明细
                        boolean bAdd = false;
                        if (HotelCalcuAssuAmoType.CHECKIN == nRule) {
                            if (0 == dayIndex) {
                                bAdd = true;
                            }
                        } else {
                            if (0 == dayIndex) {
                                if (assureType == GuaranteeType.FIRST_DAY
                                    || assureType == GuaranteeType.ALL_DAY) {
                                    bAdd = true;
                                }
                            } else {
                                bAdd = true;
                            }
                        }
                        if (bAdd) {
                            // 添加担保明细
                            if (!bClearGuarantees) {
                                reserv.getGuarantees().clear();
                                bClearGuarantees = true;
                            }

                            // 添加取消/修改规则明细(只在修改订单提交时获取,新下订单时不用获取)
                            if (bForEdit) {
                                List<HtlAssureItemEveryday> li = htlAssure
                                    .getHtlAssureItemEverydayList();
                                if (0 < li.size()) {
                                    if (!bClearAssureList) {
                                        reserv.getAssureList().clear();
                                        bClearAssureList = true;
                                    }
                                    for (HtlAssureItemEveryday item : li) {
                                        OrAssureItemEvery assureItem = new OrAssureItemEvery();
                                        MyBeanUtil.copyProperties(assureItem, item);
                                        assureItem.setID(null);
                                        assureItem.setReserv(reserv);
                                        assureItem.setNight(validDate);
                                        reserv.getAssureList().add(assureItem);
                                    }
                                }
                            } else {
                                int nValidIndex = DateUtil
                                    .getDay(order.getCheckinDate(), validDate);
                                lModCanStr.add(htlAssure.getId() + "," + nValidIndex + "/");
                            }
                        }
                    }

                    // 担保类型中首日担保只判断第一天
                    if (assureType == GuaranteeType.FIRST_DAY) {
                        // 保类型首日担保担
                        assureDay = "首日担保";
                    }

                    // 担保条件超时担保只判断第一天
                    String assureTime = htlAssure.getLatestAssureTime();
                    if (StringUtil.isValidStr(assureTime) && !bUnCondition) {
                        // 担保条件超时担保
                        assureCondiDay.append("超时担保(");
                        assureCondiDay.append(assureTime);
                        assureCondiDay.append(") ");
                        if (0 == dayIndex ) {// 如果是第一天
                            // 当没有设过无条件担保时，设置总的担保条件
                            if (vouchCondiction < HotelVouchCondiction.UNCONDICTIONVOUCH) {
                                reserv.setLateSuretyTime(assureTime);
                                reserv.setOverTimeAssure(true);
                                vouchCondiction = HotelVouchCondiction.OVERTIMEVOUCH;
                                // 取出计算规则中最严格的计算规则，如果没有计算规则，默认累加判定 hotel2.9.2 modify by chenjiajie
                                // 2009-8-16
                                if (sRule.equals("1") || sRule.equals("4")) {//艺龙判定也只判断第一天add by huanglingfeng 2012-5-8
                                    bOnlyClear = true;
                                }
                            }
                            guarantee.setAssureType(sAssureType);
                            guarantee.setLatestAssureTime(htlAssure.getLatestAssureTime());
                            guarantee.setNight(validDate);
                            guarantee.setNotConditional(htlAssure.getIsNotConditional());
                            guarantee.setReserv(reserv);
                            reserv.getGuarantees().add(guarantee);
                        }
                    }

                    // 判断最终的担保条件 超房数担保
                    Long overRoomNumber = htlAssure.getOverRoomNumber();
                    if(ChannelType.CHANNEL_ELONG==order.getChannel()){
                    	//艺龙超房数为2，则2间房就要担保，这里要减1适应芒果的超房担保规则
                    	if(overRoomNumber!=null && overRoomNumber>=1){
                    		overRoomNumber = overRoomNumber-1;
                    		//htlAssure.setOverRoomNumber(overRoomNumber);
                    	}
                    }
                    if (null != overRoomNumber && 0 < overRoomNumber.longValue() && !bUnCondition ) {
                        guarantee.setOverRoomNumber(overRoomNumber.intValue());
                        reserv.setRooms(overRoomNumber.intValue());
                        // add by haibo.li 2009-6-15
                        // 如果是check in day的情况下，第一天为超房担保，不管是否房间数超过了都参与计算担保金额.
                        // 如果是如果是艺龙判定的情况下，第一天为超房担保，不管是否房间数超过了都参与计算担保金额. add by huanglingfeng 2012-5-7
                        if (0 == dayIndex && (sRule.equals("1")||sRule.equals("4"))&& !assBo) {
                        	if(0 == dayIndex && StringUtil.isValidStr(assureTime)){
                             	reserv.getGuarantees().clear();
                             }
                            guarantee.setAssureType(sAssureType);
                            guarantee.setLatestAssureTime(htlAssure.getLatestAssureTime());
                            guarantee.setNight(validDate);
                            guarantee.setNotConditional(htlAssure.getIsNotConditional());
                            guarantee.setReserv(reserv);
                            reserv.getGuarantees().add(guarantee);
                            reserv.setNeedCredit(true);
                            reserv.setRoomsAssure(true);
                            bOnlyClear = true;
                            assBo = true;
                        }
                        // 如果预订房间数超过超房数，默认必须要担保
                        else if (order.getRoomQuantity() > overRoomNumber.intValue()
                            && !sRule.equals("1") && !assBo) {
                        	 if(0 == dayIndex && StringUtil.isValidStr(assureTime)){
                             	reserv.getGuarantees().clear();
                             }
                            guarantee.setAssureType(sAssureType);
                            guarantee.setLatestAssureTime(htlAssure.getLatestAssureTime());
                            guarantee.setNight(validDate);
                            guarantee.setNotConditional(htlAssure.getIsNotConditional());
                            guarantee.setReserv(reserv);
                            reserv.getGuarantees().add(guarantee);
                            reserv.setNeedCredit(true);
                            reserv.setRoomsAssure(true);
                            assBo = true;
                        }

                        // 担保条件超房数担保
                        assureCondiDay.append("超房数担保(");
                        assureCondiDay.append(overRoomNumber);
                        assureCondiDay.append(") ");
                        // 当没有设过无条件担保时，设置总的担保条件
                        if (vouchCondiction < HotelVouchCondiction.UNCONDICTIONVOUCH) {
                            vouchCondiction = HotelVouchCondiction.OVERROOMNUMVOUCH;
                        }
                    }

                    // 判断最终的担保条件 超间夜担保
                    Long overNightsNumber = htlAssure.getOverNightsNumber();
                    if (null != overNightsNumber && 0 < overNightsNumber.longValue()
                        && !bUnCondition ) {
                        guarantee.setOverNightsNumber(Integer.valueOf(overNightsNumber.intValue()));
                        reserv.setNights(overNightsNumber.intValue());
                        // 如果是check in day的情况下，第一天为超房担保，不管是否房间数超过了都参与计算担保金额.
                        // if(dayIndex == 0 && sRule.equals("1")){
                        // guarantee.setAssureType(sAssureType);
                        // guarantee.setLatestAssureTime(htlAssure.getLatestAssureTime());
                        // guarantee.setOverRoomNumber(htlAssure
                        //.getOverRoomNumber()==null?0:htlAssure.getOverRoomNumber().intValue());
                        // guarantee.setNotConditional(htlAssure.getIsNotConditional());
                        // guarantee.setNight(validDate);
                        // guarantee.setReserv(reserv);
                        // reserv.getGuarantees().add(guarantee);
                        // reserv.setNeedCredit(true);
                        // reserv.setNightsAssure(true);
                        // bOnlyClear = true;
                        // }
                        // // 如果预订间夜数超过间夜数，默认必须要担保
                        // else
                        if (overNightsNumber < (DateUtil.getDay(order.getCheckinDate(), order
                            .getCheckoutDate()))
                            * order.getRoomQuantity() && !assBo) {// 如果预订间夜数超过间夜数，默认必须要担保
                        	if(0 == dayIndex && StringUtil.isValidStr(assureTime)){
                            	reserv.getGuarantees().clear();
                            }
                            guarantee.setAssureType(sAssureType);
                            guarantee.setLatestAssureTime(htlAssure.getLatestAssureTime());
                            guarantee.setOverRoomNumber(null == overRoomNumber ? 0
                                : overRoomNumber.intValue());
                            guarantee.setNotConditional(htlAssure.getIsNotConditional());
                            guarantee.setNight(validDate);
                            guarantee.setReserv(reserv);
                            reserv.getGuarantees().add(guarantee);
                            reserv.setNeedCredit(true);
                            reserv.setNightsAssure(true);
                            assBo = true;
                        }

                        // 担保条件超房数担保
                        assureCondiDay.append("超间夜担保(");
                        assureCondiDay.append(overNightsNumber);
                        assureCondiDay.append(") ");
                        // 当没有设过无条件担保时，设置总的担保条件
                        if (vouchCondiction < HotelVouchCondiction.UNCONDICTIONVOUCH) {
                            vouchCondiction = HotelVouchCondiction.OVERNIGHTNUMVOUCH;
                        }
                    }
                    // 判断无条件担保
                    if ("1".equals(htlAssure.getIsNotConditional()) && !bUnCondition) {

                        // 设置总的担保条件，无条件担保优先级最高
                        if (vouchCondiction < HotelVouchCondiction.UNCONDICTIONVOUCH) {
                            if (!bOnlyClear) {
                                // 担保条件当无条件担保，勾才勾上
                                reserv.setUnCondition(true);
                                reserv.setNeedCredit(true);

                                // 当无条件担保，把超时担保去掉，变成要担保
                                reserv.setOverTimeAssure(false);
                                reserv.setRoomsAssure(false);
                                vouchCondiction = HotelVouchCondiction.UNCONDICTIONVOUCH;
                                assureCondiDay.delete(0, assureCondiDay.length());
                            }

                        }
                        guarantee.setAssureType(sAssureType);
                        guarantee.setLatestAssureTime(htlAssure.getLatestAssureTime());
                        guarantee.setNight(validDate);
                        guarantee.setNotConditional(htlAssure.getIsNotConditional());
                        guarantee.setReserv(reserv);
                        reserv.getGuarantees().add(guarantee);
                        // 担保条件无条件担保
                        assureCondiDay.delete(0, assureCondiDay.length());
                        assureCondiDay.append("无条件担保");
                        bUnCondition = true;
                    }

                    // 担保类型 全额担保
                    sAssureType = htlAssure.getAssureType();
                    assureType = StringUtil.isValidStr(sAssureType) ? Integer.parseInt(sAssureType)
                        : 0;
                    if (assureType == GuaranteeType.ALL_DAY) {
                        // 担保类型全额担保
                        assureDay = "全额担保";
                    }
                    // 只要有一天需要担保函，就需要
                    String sLetter = htlAssure.getAssureLetter();
                    if ("1".equals(sLetter) && !isNeedLetter) {
                        reserv.setAssureLetter(true);
                        guarantee.setAssureLetter(sLetter);
                    }

                    // 预订条款详情中的每天最终的担保条件
                    String strCond = assureCondiDay.toString();
                    priceDetail.setAssureCond(strCond);
                    guarantee.setAssureCondiction(strCond);
                    // 预订条款详情中的每天的担保类型
                    priceDetail.setAssureType(assureDay);

                    break;
                }
            }
            // 预付时
            else {

                log.info("为预付单时进入：");

                List<HtlPrepayEveryday> htlPrepayEverydayList = htlPreconcertItem
                    .getHtlPrepayEverydayList();
                // 每日预付条款和HtlPreconcertItem为一对一
                for (HtlPrepayEveryday htlPrepayEveryday : htlPrepayEverydayList) {

                    // 计算预付给酒店的金额
                    String amountType = htlPrepayEveryday.getAmountType();
                    if (StringUtil.isValidStr(amountType)) {
                        int nAmountType = Integer.parseInt(amountType);
                        String paymentType = htlPrepayEveryday.getPaymentType();
                        String sPercent = htlPrepayEveryday.getPrepayDeductType();
                        double percent = StringUtil.isValidStr(sPercent) ? (Double
                            .parseDouble(sPercent) * 0.01) : 1;
                        double tmpPrice = 0.0;
                        if (DeductType.ALL_DAY == nAmountType
                            || DeductType.FIRST_DAY_PERCENTAGE == nAmountType) {
                            if ("0".equals(paymentType)) {
                                tmpPrice = priceDetail.getBasePrice();
                            } else {
                                tmpPrice = priceDetail.getSalePrice();
                            }
                        } else {
                            if ("0".equals(paymentType)) {
                                tmpPrice = priceDetail.getBasePrice() * percent;
                            } else {
                                tmpPrice = priceDetail.getSalePrice() * percent;
                            }
                        }
                        reserv.setPayToHotelAdv(reserv.getPayToHotelAdv() + tmpPrice);
                    }

                    // 支付类型 1为底价 2为售价
                    if (StringUtil.isValidStr(htlPrepayEveryday.getPaymentType())
                        && "2".equals(htlPrepayEveryday.getPaymentType())
                        && 0 == DateUtil.getDay(
                            htlPreconcertItem.getValidDate(),
                            order.getCheckinDate())) {
                        order.setShowBasePrice(false);
                    }
                    // 结算方法
                    String tmpBanlanceType = htlPrepayEveryday.getBalanceType();
                    if (StringUtil.isValidStr(tmpBanlanceType)) {
                        if (!StringUtil.isValidStr(banlanceType)) {
                            banlanceType = tmpBanlanceType;
                            // 一个酒店统一都是同一个结算方法
                            reserv.setBalanceMode(banlanceType);
                        } else {
                            banlanceType = tmpBanlanceType;
                        }
                    }

                    if (null != banlanceType && !banlanceType.equals("")) {
                        // 当一单一结时 预付日期还要再提前2天
                        if (Integer.parseInt(HotelBalanceMethod.COA) == Integer
                            .parseInt((banlanceType))) {
                            balanceDay = "客人到之前";
                            // 预付付款时限判断，当为提前日期
                            String timeLimitType = htlPrepayEveryday.getTimeLimitType();
                            if (HotelPayLimitType.BEFOREDATE.equals(timeLimitType)) {
                                aheadDateDay.append("在");
                                Date limitDate = htlPrepayEveryday.getLimitDate();
                                aheadDateDay.append(DateUtil.dateToString(DateUtil.getDate(
                                    limitDate, -2)));
                                aheadDateDay.append("之前");
                                // 新取的提前日期-2比之前存的最提前日期还要早，更新这个总的最提前日期
                                if (0 == dayIndex
                                    && 0 < DateUtil.compare(
                                        DateUtil.getDate(limitDate,-2),
                                        prePayDate)) {
                                    prePayDate = DateUtil.getDate(limitDate, -2);
                                }
                                // 当为提前天数
                            } else if (HotelPayLimitType.BEFOREDATENUM.equals(timeLimitType)) {
                                aheadDateDay.append("在");
                                Long aheadDaysDt = htlPrepayEveryday.getLimitAheadDays();
                                // add by shengwei.zuo 2009-04-07 aheadDaysDt不为空时才执行下面的代码,修复td 495
                                // bug
                                if (null != aheadDaysDt) {
                                    int aheadDays = aheadDaysDt.intValue();
                                    aheadDateDay.append(DateUtil.dateToString(DateUtil.getDate(
                                        beginDate, -aheadDays - 2)));
                                    aheadDateDay.append("之前");
                                    // 新取的经过计算后的提前日期-2比之前存的最提前日期还要早，更新这个总的最提前日期
                                    if (0 == dayIndex
                                        && 0 < DateUtil.compare(
                                            DateUtil.getDate(beginDate,
                                                -aheadDays - 2),prePayDate)) {
                                        prePayDate = DateUtil.getDate(beginDate, -aheadDays - 2);
                                    }
                                }

                            }
                        }
                        // 当为月结时
                        else if (Integer.parseInt(HotelBalanceMethod.SEND_BILL) == Integer
                            .parseInt(banlanceType)) {
                            balanceDay = "月结房费";
                            String timeLimitType = htlPrepayEveryday.getTimeLimitType();
                            // 预付付款时限判断，当为提前日期
                            if (HotelPayLimitType.BEFOREDATE.equals(timeLimitType)) {
                                // 预订条款详情中的每天的提前日期
                                aheadDateDay.append("在");
                                aheadDateDay.append(DateUtil.dateToString(htlPrepayEveryday
                                    .getLimitDate()));
                                aheadDateDay.append("之前");
                                // 新取的提前日期比之前存的最提前日期还要早，更新这个最提前日期
                                Date limitDate = htlPrepayEveryday.getLimitDate();
                                if (0 == dayIndex && 0 < DateUtil.compare(limitDate,prePayDate)) {
                                    prePayDate = limitDate;
                                }
                                // 当为提前天数
                            } else if (HotelPayLimitType.BEFOREDATENUM.equals(timeLimitType)) {
                                int aheadDays = htlPrepayEveryday.getLimitAheadDays().intValue();
                                // 预订条款详情中的每天的提前日期
                                aheadDateDay.append("在");
                                aheadDateDay.append(DateUtil.dateToString(DateUtil.getDate(
                                    beginDate, -aheadDays)));
                                aheadDateDay.append("之前");
                                // 新取的经过计算后的提前日期比之前存的最提前日期还要早，更新这个最提前日期
                                if (0 == dayIndex
                                    && 0 < DateUtil.compare(
                                        DateUtil.getDate(beginDate,-aheadDays),prePayDate)) {
                                    prePayDate = DateUtil.getDate(beginDate, -aheadDays);
                                }
                            }
                        }
                    }

                    // 预订条款详情中的每天的结算方法
                    priceDetail.setBalanceMode(balanceDay);
                    // 预订条款详情中的每天的提前日期
                    priceDetail.setPrepayTime(aheadDateDay.toString());

                    // 获取取消/修改规则
                    if (bForEdit) {
                        List<HtlPrepayItemEveryday> li = htlPrepayEveryday
                            .getHtlPrepayItemEverydayList();
                        if (0 < li.size()) {
                            if (!bClearAssureList) {
                                reserv.getAssureList().clear();
                                bClearAssureList = true;
                            }
                            List <OrAssureItemEvery> list = new ArrayList<OrAssureItemEvery>();
                            for (HtlPrepayItemEveryday item : li) {
                                OrAssureItemEvery assureItem = new OrAssureItemEvery();
                                MyBeanUtil.copyProperties(assureItem, item);
                                assureItem.setID(null);
                                assureItem.setReserv(reserv);
                                assureItem.setNight(validDate);
                                //list.add(assureItem);
                               reserv.getAssureList().add(assureItem);
                            }
                            //reserv.setAssureList(list);
                        }
                    } else {
                        int nValidIndex = DateUtil.getDay(order.getCheckinDate(), validDate);
                        lModCanStr.add(htlPrepayEveryday.getId() + "," + nValidIndex + "/");
                    }

                    // 一对一
                    break;
                }
            }
        }

        if (!bForEdit) {
            if (!lModCanStr.isEmpty()) {
                Collections.sort(lModCanStr, compModCanItem);
                for (Object strItem : lModCanStr) {
                    cancelModifyStr.append(strItem.toString());
                }
            }
            reserv.setCancelModifyStr(cancelModifyStr.toString());
        }

        // 当有设了预付付款时限的时候，设置订单最终总的预付时限日期
        if (!bPay && 0 < DateUtil.compare(prePayDate,beginDate)) {
            reserv.setAdvancePayTime(prePayDate);
        }

        // 面付单则计算担保金额
        if (bPay && reserv.isCanAssure()) {
            if (bForEdit) {
                calculateVouchPrice(order);
            } else {
                calculateVouchPricePerRoom(order);
            }
            if (reserv.isNeedCredit()) {
                order.setSuretyPrice(reserv.getReservSuretyPrice());
            }
            if (0 == Double.compare(0.0,reserv.getReservSuretyPrice())) {
                reserv.setNeedCredit(false);
            }
        }
        order.setReservation(reserv);
    }

    /**
     * 用于修改扣配额后给orderItem排序
     * 
     * @author chenkeming Feb 27, 2009 10:43:02 AM
     */
    private static class CompOrderItem implements Comparator {
        public int compare(Object o1, Object o2) {
            OrOrderItem item1 = (OrOrderItem) o1;
            OrOrderItem item2 = (OrOrderItem) o2;
            int nIndex1 = item1.getDayIndex();
            int nIndex2 = item2.getDayIndex();
            if (nIndex1 != nIndex2) {
                return nIndex1 < nIndex2 ? 0 : 1;
            } else {
                return item1.getRoomIndex() <= item2.getRoomIndex() ? 0 : 1;
            }
        }
    }

    /**
     * 用于修改扣配额后给orderItem排序
     * 
     * @author chenkeming Feb 27, 2009 10:43:02 AM
     */
    private static class CompModCanItem implements Comparator {
        public int compare(Object o1, Object o2) {
            String item1 = (String) o1;
            String item2 = (String) o2;
            long l1 = Long.parseLong(item1.substring(item1.indexOf(',') + 1, item1.indexOf('/')));
            long l2 = Long.parseLong(item2.substring(item2.indexOf(',') + 1, item2.indexOf('/')));
            return l1 < l2 ? 0 : 1;
        }
    }

    /**
     * 用于取消修改规则排序
     * 
     * @author chenkeming Feb 27, 2009 10:43:02 AM
     */
    private static class CompModCanItemNew implements Comparator {
        public int compare(Object o1, Object o2) {
            Object[] item1 = (Object[]) o1;
            Object[] item2 = (Object[]) o2;
            int l1 = Integer.parseInt(item1[9].toString());
            int l2 = Integer.parseInt(item2[9].toString());
            return l1 < l2 ? 0 : 1;
        }
    }

    /**
     * 确认金额列表
     * 
     * @author chenkeming Mar 6, 2009 3:07:35 PM
     * @param Id
     * @param roleUser
     * @return
     */
    public String confirmMoney(long Id, UserWrapper roleUser) {
        OrOrderMoney moneyObj = (OrOrderMoney) orOrderDao.load(OrOrderMoney.class, Id);
        if (null == moneyObj) {
            return "fail";
        }
        if (!moneyObj.isSuccess()) {
            Date now = new Date();
            moneyObj.setSuccess(true);
            moneyObj.setModifier(roleUser.getLoginName());
            moneyObj.setModifyTime(now);
            orOrderDao.saveOrUpdate(moneyObj);

            OrHandleLog hLog = new OrHandleLog();
            OrOrder order = orOrderDao.getOrder(moneyObj.getOrder().getID());
            hLog.setHisNo(order.getHisNo());
            hLog.setContent("确认金额为支付成功:金额:" + moneyObj.getMoney() + ",原因:" + moneyObj.getReason()
                + ",创建时间:" + DateUtil.datetimeToString(moneyObj.getCreateTime()));
            hLog.setOrder(order);
            hLog.setModifierName(roleUser.getName());
            hLog.setModifierRole(roleUser.getLoginName());
            hLog.setModifiedTime(now);
            orOrderDao.saveOrUpdate(hLog);
        }
        return "success";
    }

    /**
     * 取消金额列表
     * 
     * @author chenkeming Mar 6, 2009 3:07:35 PM
     * @param Id
     * @param roleUser
     * @return
     */
    public String cancelMoney(long Id, UserWrapper roleUser) {
        OrOrderMoney moneyObj = (OrOrderMoney) orOrderDao.load(OrOrderMoney.class, Id);
        if (null == moneyObj) {
            return "fail";
        }
        if (!moneyObj.isCancel()) {
            moneyObj.setCancel(true);
            moneyObj.setModifier(roleUser.getLoginName());
            moneyObj.setModifyTime(new Date());
            orOrderDao.saveOrUpdate(moneyObj);

            OrHandleLog hLog = new OrHandleLog();
            OrOrder order = orOrderDao.getOrder(moneyObj.getOrder().getID());
            hLog.setHisNo(order.getHisNo());
            hLog.setContent("取消金额列表:金额:" + moneyObj.getMoney() + ",原因:" + moneyObj.getReason()
                + ",创建时间:" + DateUtil.datetimeToString(moneyObj.getCreateTime()));
            hLog.setOrder(order);
            hLog.setModifierName(roleUser.getName());
            hLog.setModifierRole(roleUser.getLoginName());
            hLog.setModifiedTime(new Date());
            orOrderDao.saveOrUpdate(hLog);
        }
        return "success";
    }

    /**
     * 确认改变修改/取消金额
     * 
     * @author chenkeming Mar 6, 2009 3:07:35 PM
     * @param Id
     * @param money
     * @param roleUser
     * @return
     */
    public String confirmChgMoney(long Id, double money, UserWrapper roleUser) {
        OrOrderMoney moneyObj = (OrOrderMoney) orOrderDao.load(OrOrderMoney.class, Id);
        if (null == moneyObj) {
            return "fail";
        }
        OrOrderMoney newMoney = new OrOrderMoney();
        Date now = new Date();
        newMoney.setID(null);
        newMoney.setHisNo(moneyObj.getHisNo());
        newMoney.setMoneyType(moneyObj.getMoneyType());
        newMoney.setCreator(roleUser.getLoginName());
        newMoney.setDirection(PayDirectionType.IN);
        newMoney.setTarget(MoneyTargetType.CUSTOMER);
        newMoney.setReason("人工修改的" + moneyObj.getReason());
        newMoney.setCreateTime(now);
        newMoney.setValid(true);
        newMoney.setSuccess(false);
        newMoney.setCancel(false);
        newMoney.setMoney(money);
        newMoney.setOrder(moneyObj.getOrder());

        orOrderDao.saveOrUpdate(newMoney);
        if (moneyObj.isValid()) {
            moneyObj.setValid(false);
            moneyObj.setModifier(roleUser.getLoginName());
            moneyObj.setModifyTime(now);
            orOrderDao.saveOrUpdate(moneyObj);
        }

        OrHandleLog hLog = new OrHandleLog();
        OrOrder order = orOrderDao.getOrder(moneyObj.getOrder().getID());
        hLog.setHisNo(order.getHisNo());
        hLog.setContent("修改金额列表:原来金额:" + moneyObj.getMoney() + ",新增金额:" + newMoney.getMoney()
            + ",原因:" + newMoney.getReason() + ",创建时间:"
            + DateUtil.datetimeToString(newMoney.getCreateTime()));
        hLog.setOrder(order);
        hLog.setModifierName(roleUser.getName());
        hLog.setModifierRole(roleUser.getLoginName());
        hLog.setModifiedTime(now);
        orOrderDao.saveOrUpdate(hLog);

        return "success";
    }

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
     * UserWrapper roleUser) { OrReservation reserv =
     * (OrReservation)orOrderDao.load(OrReservation.class, reservationID); OrOrder order =
     * orOrderDao.loadOrder(orderId); if(reserv == null || order == null) { return "fail"; } double
     * oldPrice = reserv.getModifyPrice(); if(oldPrice != modifyPrice) {
     * reserv.setModifyPrice(modifyPrice);
     * 
     * StringBuffer strCmp = new StringBuffer(); strCmp.append("本次订单修改产生的金额由:" + oldPrice + "改为:" +
     * modifyPrice); strCmp.append(" 原因:" + note); strCmp.append("<br>"); OrHandleLog handleLog =
     * new OrHandleLog(); handleLog.setModifierName(roleUser.getName());
     * handleLog.setModifierRole(roleUser.getLoginName()); handleLog.setContent(strCmp.toString());
     * handleLog.setModifiedTime(new Date()); handleLog.setHisNo(order.getHisNo());
     * handleLog.setOrder(order); orOrderDao.insertObject(handleLog); }
     * orOrderDao.saveOrUpdate(reserv); return "ok"; }
     */

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
        double sysAssureMoney, double oriAssureMoney, boolean bAuth) {
        if (!oriCreditAssure) {
            if (order.isCreditAssured()) {
                String hql = " update OrOrderMoney a set a.valid=0 where a.order.ID="
                    + order.getID()
                    + " and a.moneyType=1 and a.valid=1 and not exists " +
                            "(select b.ID from OrOrderMoney b "
                    + "where b.order.ID=" + order.getID() + "and b.moneyType=1 "
                    + "and b.valid=1 and b.hasPreAuth=1)";
                orOrderDao.doUpdateBatch(hql, null);

                if (0.001 < sysAssureMoney && 0 != Double.compare(
                    order.getSuretyPrice(),sysAssureMoney)) {
                    OrOrderMoney moneyHotel = new OrOrderMoney();
                    moneyHotel.setCreateTime(new Date());
                    moneyHotel.setHisNo(order.getHisNo());
                    moneyHotel.setMoneyType(MoneyType.ASSURE);
                    moneyHotel.setCreator(roleUser.getLoginName());
                    moneyHotel.setDirection(PayDirectionType.IN);
                    moneyHotel.setMoney(sysAssureMoney);
                    moneyHotel.setSuccess(false);
                    moneyHotel.setTarget(MoneyTargetType.CUSTOMER);
                    moneyHotel.setValid(false);
                    moneyHotel.setReason("根据条款计算的担保金额");
                    moneyHotel.setCancel(false);
                    moneyHotel.setManual(false);
                    moneyHotel.setHasPreAuth(false);
                    moneyHotel.setOrder(order);
                    order.getMoneyList().add(moneyHotel);
                }

                OrOrderMoney moneyHotel = new OrOrderMoney();
                moneyHotel.setCreateTime(new Date());
                moneyHotel.setHisNo(order.getHisNo());
                moneyHotel.setMoneyType(MoneyType.ASSURE);
                moneyHotel.setCreator(roleUser.getLoginName());
                moneyHotel.setDirection(PayDirectionType.IN);
                moneyHotel.setMoney(order.getSuretyPrice());
                moneyHotel.setSuccess(false);
                moneyHotel.setTarget(MoneyTargetType.CUSTOMER);
                moneyHotel.setValid(true);
                moneyHotel.setReason("CC手工改的担保金额");
                moneyHotel.setCancel(false);
                moneyHotel.setManual(true);
                moneyHotel.setOrder(order);
                moneyHotel.setHasPreAuth(bAuth);
                moneyHotel.setPayIdent(order.getCreditCardIdsSelect());
                order.getMoneyList().add(moneyHotel);
            }
        } else {
            if (order.isCreditAssured()) {
                if (0 != Double.compare(oriAssureMoney,order.getSuretyPrice())) {
                    String hql = " update OrOrderMoney a set a.valid=0 where a.order.ID="
                        + order.getID()
                        + " and a.moneyType=1 and a.valid=1 and not exists" +
                                " (select b.ID from OrOrderMoney b "
                        + "where b.order.ID=" + order.getID() + "and b.moneyType=1 "
                        + "and b.valid=1 and b.hasPreAuth=1)";
                    orOrderDao.doUpdateBatch(hql, null);
                    OrOrderMoney moneyHotel = new OrOrderMoney();
                    moneyHotel.setCreateTime(new Date());
                    moneyHotel.setHisNo(order.getHisNo());
                    moneyHotel.setMoneyType(MoneyType.ASSURE);
                    moneyHotel.setCreator(roleUser.getLoginName());
                    moneyHotel.setDirection(PayDirectionType.IN);
                    moneyHotel.setMoney(order.getSuretyPrice());
                    moneyHotel.setSuccess(false);
                    moneyHotel.setTarget(MoneyTargetType.CUSTOMER);
                    moneyHotel.setValid(true);
                    moneyHotel.setReason("CC手工改的担保金额");
                    moneyHotel.setCancel(false);
                    moneyHotel.setManual(true);
                    moneyHotel.setHasPreAuth(bAuth);
                    moneyHotel.setPayIdent(order.getCreditCardIdsSelect());
                    moneyHotel.setOrder(order);
                    order.getMoneyList().add(moneyHotel);
                } else if (bAuth) {
                    String hql = " update OrOrderMoney a set a.hasPreAuth=1 where a.order.ID="
                        + order.getID()
                        + " and a.moneyType=1 and a.valid=1 and ID=(" +
                                "select max(b.ID) from OrOrderMoney b "
                        + "where b.order.ID=" + order.getID() + "and b.moneyType=1 "
                        + "and b.valid=1 and b.hasPreAuth=0)";
                    orOrderDao.doUpdateBatch(hql, null);
                }
            } else {
                String hql = " update OrOrderMoney a set a.valid=0 where a.order.ID="
                    + order.getID()
                    + " and a.moneyType=1 and a.valid=1 and not exists " +
                            "(select b.ID from OrOrderMoney b "
                    + "where b.order.ID=" + order.getID() + "and b.moneyType=1 "
                    + "and b.valid=1 and b.hasPreAuth=1)";
                orOrderDao.doUpdateBatch(hql, null);
            }
        }

        orOrderDao.saveOrUpdate(order);
    }

    /**
     * 新下单时增加订单金额
     * 
     * @author chenkeming Mar 10, 2009 3:32:15 PM
     * @param order
     * @param roleUser
     * @param sysAssureMoney
     */
    public void newOrderMoney(OrOrder order, UserWrapper roleUser, double sysAssureMoney) {
        OrOrderMoney moneyHotel = new OrOrderMoney();
        moneyHotel.setCreateTime(new Date());
        moneyHotel.setHisNo(order.getHisNo());
        moneyHotel.setMoneyType(MoneyType.ASSURE);
        moneyHotel.setCreator(roleUser.getLoginName());
        moneyHotel.setDirection(PayDirectionType.IN);
        moneyHotel.setMoney(order.getSuretyPrice());
        moneyHotel.setSuccess(false);
        moneyHotel.setTarget(MoneyTargetType.CUSTOMER);
        moneyHotel.setValid(true);
        if (0 != Double.compare(sysAssureMoney,order.getSuretyPrice())) {
            moneyHotel.setReason("CC手工改的担保金额");
            moneyHotel.setManual(true);
        } else {
            moneyHotel.setReason("系统担保金额");
            moneyHotel.setManual(false);
        }
        moneyHotel.setCancel(false);
        moneyHotel.setHasPreAuth(order.isHasCreatePreAuth());
        moneyHotel.setPayIdent(order.getCreditCardIdsSelect());
        moneyHotel.setOrder(order);
        order.getMoneyList().add(moneyHotel);
        orOrderDao.saveOrUpdate(order);
    }

    public OrOrderDao getOrOrderDao() {
        return orOrderDao;
    }

    public void setOrOrderDao(OrOrderDao orOrderDao) {
        this.orOrderDao = orOrderDao;
    }

    /***
    public IQuotaForCCService getQuotaForCCService() {
        return quotaForCCService;
    }

    public void setQuotaForCCService(IQuotaForCCService quotaForCCService) {
        this.quotaForCCService = quotaForCCService;
    }
	***/
    
    public OrderAssist getOrderAssist() {
        return orderAssist;
    }

    public void setOrderAssist(OrderAssist orderAssist) {
        this.orderAssist = orderAssist;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public String getCancelCheck(long reservationID, double sumRmb, String checkIn) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getModifyCheck(long reservationID, double sumRmb, String checkIn) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setClauseManage(ClauseManage clauseManage) {
        this.clauseManage = clauseManage;
    }

	public IQuotaControlService getQuotaControl() {
		return quotaControl;
	}

	public void setQuotaControl(IQuotaControlService quotaControl) {
		this.quotaControl = quotaControl;
	}

	public void setHtlElAssureRuleDao(HtlElAssureRuleDao htlElAssureRuleDao) {
		this.htlElAssureRuleDao = htlElAssureRuleDao;
	}

	public void setExMappingDao(ExMappingDao exMappingDao) {
		this.exMappingDao = exMappingDao;
	}
}
