package com.mangocity.hotel.order.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.order.persistence.OrAuditFaxLog;
import com.mangocity.hotel.order.persistence.OrDailyAudit;
import com.mangocity.hotel.order.persistence.OrPaperContact;
import com.mangocity.hotel.order.persistence.OrPaperDailyAudit;
import com.mangocity.hotel.order.persistence.OrPaperDailyAuditItem;
import com.mangocity.hotel.order.persistence.VOrOrderItem;
import com.mangocity.hotel.order.service.IAuditService;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hotel.order.service.assistant.OrderFax;
import com.mangocity.hotel.order.service.assistant.OrderItemFax;
import com.mangocity.util.DateUtil;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.hotel.constant.IsAutomatismSendFax;
import com.mangocity.util.log.MyLog;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Fax;

/**
 */
public class AutomatismSendFax extends Thread {
	private static final MyLog log = MyLog.getLogger(AutomatismSendFax.class);
//    private int delay;

    private List hotels;

    /**
     * 日审接口
     */
    private IAuditService auditService;

    private SystemDataService systemDataService;

    private OrWorkStates user;

//    private Map params;

    /**
     * 传真邮件辅助类
     */
    private MsgAssist msgAssist;

    /**
     * message接口
     */
    private CommunicaterService communicaterService;

    private OrParam orParam;

    public AutomatismSendFax(Map params, OrWorkStates workuser, OrParam param,
        IAuditService sandAuditService, CommunicaterService communicater, SystemDataService systemDataService,
        MsgAssist msg) {
        // hotels = sendHotels;
        auditService = sandAuditService;
        hotels = auditService.gethotels(params);
        user = workuser;
        orParam = param;
        communicaterService = communicater;
        systemDataService = systemDataService;
        msgAssist = msg;
        // TODO Auto-generated constructor stub
    }

    public void run() {
        Calendar ca = Calendar.getInstance();
        Date createDate = ca.getTime();
        OrPaperDailyAudit aHotel = null;
        int returnvalue = 0;
        try {
            for (int i = 0; i < hotels.size(); i++) {
                Long ret = null;
                OrDailyAudit orDailyAudit = auditService.getOrDailyAudit(Long.parseLong(hotels.get(
                    i).toString()));
                // 获取该酒店确认的传真号码
                List orPaperContacts = auditService.getOrPaperContact(orDailyAudit.getHotelId());
                OrPaperContact orPaperContact = (OrPaperContact) orPaperContacts.get(0);
                String faxNo = orPaperContact.getFax();

                if (null != faxNo) {
                    try {
                        ret = sendAuditFax(Long.parseLong(hotels.get(i).toString()), faxNo,
                            orDailyAudit);
                        // Thread.sleep(5000);
                    } catch (Exception e) {
                        // 发送日志
                        noteLog(false, orDailyAudit.getHotelId(), orDailyAudit.getCheckNight(),
                            faxNo, user, orDailyAudit.getHotelName());
                        // return;
                    }
                    if (null != ret) {
                        orDailyAudit.setFaxFile("" + ret);
                        OrPaperDailyAuditItem orPaperDailyAuditItem = new OrPaperDailyAuditItem();
                        List aHotels = auditService.findSimilarAudit(orDailyAudit);
                        if (0 >= aHotels.size()) {
                            aHotel = new OrPaperDailyAudit();
                            aHotel.setHotelId(orDailyAudit.getHotelId());
                            aHotel.setAuditId(Long.parseLong(hotels.get(i).toString()));
                            if (0 != returnvalue) {
                                aHotel.setLostCount(1);
                                aHotel.setSuccessCount(0);
                            } else {
                                aHotel.setLostCount(0);
                                aHotel.setSuccessCount(1);
                            }
                        } else {
                            OrPaperDailyAudit isFaxNum = (OrPaperDailyAudit) aHotels.get(0);
                            aHotel = auditService.getOrPaperDailyAudit(isFaxNum.getID());
                            if (0 != returnvalue) {

                                aHotel.setLostCount(isFaxNum.getLostCount() + 1);
                                orPaperDailyAuditItem.setSendId((long) isFaxNum.getLostCount());
                            } else {
                                aHotel.setSuccessCount(isFaxNum.getSuccessCount() + 1);
                                orPaperDailyAuditItem.setSendId((long) isFaxNum.getSuccessCount());
                            }
                        }
                        aHotel.setFax(faxNo);
                        aHotel.setCreatedBy(user.getLogonId());
                        aHotel.setCreateDate(createDate);
                        orPaperDailyAuditItem.setHotelId(orDailyAudit.getHotelId());
                        orPaperDailyAuditItem.setPaperAudit(aHotel);
                        orPaperDailyAuditItem.setFax(aHotel.getFax());
                        orPaperDailyAuditItem.setCreatedBy(user.getLogonId());
                        orPaperDailyAuditItem.setCreateDate(DateUtil.getDate(createDate));
                        orPaperDailyAuditItem.setSendState(returnvalue);
                        aHotel.getItems().add(orPaperDailyAuditItem);

                        auditService.sendAuditFax(aHotel, orDailyAudit);
                        noteLog(true, orDailyAudit.getHotelId(), orDailyAudit.getCheckNight(),
                            faxNo, user, orDailyAudit.getHotelName());
                    } else {
                        noteLog(false, orDailyAudit.getHotelId(), orDailyAudit.getCheckNight(),
                            faxNo, user, orDailyAudit.getHotelName());
                        // return forwardMsg(hotels.get(i).toString()+"发送传真失败！");
                    }
                } else {
                    noteLog(false, orDailyAudit.getHotelId(), orDailyAudit.getCheckNight(), faxNo,
                        user, orDailyAudit.getHotelName());
                    // return forwardMsg("请确认传真号码！");
                }
            }
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        } finally {
            orParam.setValue(IsAutomatismSendFax.SENDSTOP);
            systemDataService.updateSysParamByName(orParam);
        }
        
        //destroy();
    }

    //public void destroy() {
        // throw new NoSuchMethodError();
    //}

    public void noteLog(boolean seanState, Long hotelID, Date night, String fax, OrWorkStates user,
        String hotelName) {
        Date now = new Date();
        OrAuditFaxLog orAuditFaxLog = new OrAuditFaxLog();
        orAuditFaxLog.setHotelId(hotelID);
        orAuditFaxLog.setNight(night);
        orAuditFaxLog.setFax(fax);
        orAuditFaxLog.setWorkName(user.getName() + "(" + user.getLogonId() + ")");
        orAuditFaxLog.setWorkTime(now);
        orAuditFaxLog.setHotelName(hotelName);
        if (seanState) {
            orAuditFaxLog.setSendState(1);
        } else {
            orAuditFaxLog.setSendState(2);
        }
        auditService.saveOrAuditFaxLog(orAuditFaxLog);
    }

    /**
     * 发送日审传真
     * 
     * @param auditId
     *            OrDailyAudit表的ID
     * @param faxNo
     *            传真号
     * @return ret:返回ID
     */
    private Long sendAuditFax(Long auditId, String faxNo, OrDailyAudit orDailyAudit) {

        try {
            // TODO: 等待日审真实数据
            List itemList = auditService.getOrderItemsForAuditFax(auditId);
            // 房号回写 add by baofeng.si V2.3 2008-6-19 Start
            if (null != itemList && 0 < itemList.size()) {
                List<VOrOrderItem> orderItemList = new ArrayList<VOrOrderItem>(0);
                // 取itemList的第一条数据，一个批次的日审具有相同的hotelId，date和orderId
                Object[] itemObject = (Object[]) itemList.get(0);
                Long hotelId = (Long) itemObject[8];
                Date date = (Date) itemObject[9];
                Long orderId = (Long) itemObject[10];

                // 计算前一天的日期
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
                Date date_1 = DateUtil.getDate(df.format(new Date(date.getTime() - 1 * 24 * 60 * 60
                    * 1000)));
                // 查询前一天日审的订单明细
                orderItemList = auditService.getViewOrderItem(hotelId, date_1, date);
                // 遍历当天日审明细
                for (int i = 0; i < itemList.size(); i++) {
                    if (null != orderItemList && 0 < orderItemList.size()) {
                        // 遍历前一天的日审明细
                        for (VOrOrderItem orderItem : orderItemList) {
                            Long itemOrderId = orderItem.getOrderID();
                            int roomIndex = ((Integer) itemObject[11]).intValue();
                            // orderId一致，房间索引一致，且当天的房号不为空，满足上述条件时将前一天的房号写入到当天日审明细中的房号中
                            if (null != itemOrderId
                                && itemOrderId.longValue() == orderId.longValue()
                                && roomIndex == orderItem.getRoomIndex() && null == itemObject[7]) {
                                itemObject[7] = orderItem.getRoomNo();
                            }
                        }
                    }
                }
            }
            Object[] res = auditService.getHotelInfoForAuditFax(auditId);
            if (null == res || null == itemList || 0 >= itemList.size()) {
                return null;
            }

            OrderFax orderFax = new OrderFax();
            for (int i = 0; i < itemList.size(); i++) {
                Object[] itemVal = (Object[]) itemList.get(i);
                OrderItemFax item = new OrderItemFax();
                item.setItemOrderCD((String) itemVal[0]);
                item.setItemName((String) itemVal[1]);
                /**
                 * v2.4.2把入住人姓名拆分到OrderItem中，在该版本上线之前有可能为空
                 * 因此在使用OrderItem的入住人姓名之前先判断订单创建日期是否在v2.4.2上线之后， 再判断入住人是否为空，否则使用原来订单主表的入住人姓名 by
                 * chenjiajie 2008-12-31 begin
                 **/
                if (0 < DateUtil.compare(DateUtil.getDate(
                    AuditOrderAction.RELEASED_DATE),(Date)itemVal[13])) {
                    String itemFellowName = (String) itemVal[12];
                    if (null == itemFellowName || itemFellowName.equals("")) {
                        itemFellowName = (String) itemVal[2];
                    }
                    item.setItemFellowNames(itemFellowName);
                } else {
                    item.setItemFellowNames((String) itemVal[2]);
                }
                /** by chenjiajie 2008-12-31 end **/
                item.setItemConfirmNo((String) itemVal[4]);
                Date checkInDate = (Date) itemVal[5];
                Date checkOutDate = (Date) itemVal[6];
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
                item.setItemCheckInDate(df.format(checkInDate));
                item.setItemCheckOutDate(df.format(checkOutDate));
                item.setRoomNo((String) itemVal[7]);
                orderFax.getOrderItemList().add(item);
            }
            orderFax.setHotelName((String) res[0]);
            orderFax.setHotelFax((String) res[2]);
            Date toNight = (Date) res[1];
            orderFax.setArrivalTimeStart(DateUtil.dateToString(toNight));
            Date retTime = DateUtil.getDate(toNight, +1);
            retTime = DateUtil.getDateByHour(retTime, 22);
            orderFax.setArrivalTimeEnd(DateUtil.datetimeToString(retTime));
            orderFax.setBarCode("HB" + orDailyAudit.getID());
            String xmlString = msgAssist.genOrderFaxXml(orderFax);

            Fax fax = new Fax();
            fax.setXml(xmlString);
            fax.setApplicationName("hotel");
            fax.setTemplateFileName(FaxEmailModel.DAY_CHECK_FORM);
            fax.setTo(new String[] { faxNo });
            // fax.setTo(new String[] { "075582876159" });
            fax.setFrom(user.getLogonId());
            Long ret = communicaterService.sendFax(fax);
            return ret;
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            return 0L;
        }
    }

    public IAuditService getAuditService() {
        return auditService;
    }

    public void setAuditService(IAuditService auditService) {
        this.auditService = auditService;
    }

    public OrParam getOrParam() {
        return orParam;
    }

    public void setOrParam(OrParam orParam) {
        this.orParam = orParam;
    }

    public List getHotels() {
        return hotels;
    }

    public void setHotels(List hotels) {
        this.hotels = hotels;
    }

    public MsgAssist getMsgAssist() {
        return msgAssist;
    }

    public void setMsgAssist(MsgAssist msgAssist) {
        this.msgAssist = msgAssist;
    }

    public OrWorkStates getUser() {
        return user;
    }

    public void setUser(OrWorkStates user) {
        this.user = user;
    }

    public CommunicaterService getCommunicaterService() {
        return communicaterService;
    }

    public void setCommunicaterService(CommunicaterService communicaterService) {
        this.communicaterService = communicaterService;
    }
}