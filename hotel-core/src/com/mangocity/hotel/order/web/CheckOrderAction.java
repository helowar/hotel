package com.mangocity.hotel.order.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hdl.hotel.dto.AddExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.AddExRoomOrderResponse;
import com.mangocity.hdl.hotel.dto.CheckReservateExRequest;
import com.mangocity.hdl.hotel.dto.CheckReservateExResponse;
import com.mangocity.hdl.hotel.dto.MGExReservItem;
import com.mangocity.hdl.hotel.dto.MGExResult;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.HotelConfirmType;
import com.mangocity.hotel.order.constant.ModelType;
import com.mangocity.hotel.order.constant.OrderItemType;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderFax;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.persistence.view.CheckParamVO;
import com.mangocity.hotel.order.service.CheckOrderService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.hotel.constant.QuotaType;

/**
 * 试预订相关操作Action
 * 
 * @author chenkeming
 * 
 * 
 *         注:所有的销售价,底价,都从HDL中取,且:销售价和底价都应该在HDL中算好
 */
public class CheckOrderAction extends GenericCCAction {

    private static final long serialVersionUID = -1550923787126096684L;

    /**
     * HDL webservice服务
     */
    private IHDLService hdlService;

    private CheckOrderService checkservice;

    /**
     * 总天数
     */
    private int difdays;

    private Date checkInDate;

    private Date checkOutDate;

    private String returntag = "";

    private Long hotelId;

    private Long roomTypeId;

    private Long childRoomTypeId;

    private String channel;

    private int quantity;

    private String endDate;

    private String beginDate;

    private int hotelroomcount;

    private IOrderService orderService;

    private String orderId;

    // add by shizhongwen 2009-2-1 增加联系人,(为预订酒店(check))
    private String linkMan;

    // 销售价格
    private Map datemap_sale = new HashMap();

    // 底价
    private Map datemap_base = new HashMap();

    private HotelManage hotelManage;

    /**
     * 处理Check 后,预订 版本:2.5 创建订单Process add by shizhongwen 时间:Feb 1, 2009 10:36:33 AM
     * 
     * @return
     */
    public String process() {
        AddExRoomOrderResponse addExRoomOrderResponse = null;
        MGExResult mgresult = null;

        // add by shizhongwen 2009-2-1 防止重复提交
        if (isRepeatSubmit()) {
            return forwardMsg("请不要重复提交!");
        }
        // add by shizhongwen 2009-2-1 来自页面所传参数
        Map params = getParams();
        params.get("sum");
        String orderId = (String) params.get("orderId"); // add by shizhongwen 2009-2-1 订单编号

        String channel = (String) params.get("channel");
        // 根据订单号取得订单
        OrOrder order = orderService.getOrder(Long.parseLong(orderId));
        order.setChannel(Integer.parseInt(channel));
        List<HtlPrice> htlpricelist = hotelManage.queryHtlPrice(order.getHotelId(), order
            .getRoomTypeId(), order.getChildRoomTypeId(), order.getPayMethod(), DateUtil
            .dateToString(order.getCheckinDate()), DateUtil.dateToString(order.getCheckoutDate()));

        difdays = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        // add by shizhongwen 2009-03-08 begin 如果orderItems 为空，则从htlprice 表中取数据 然后更新到数据库中
        order.setSumRmb(0.0);
        order.setSum(0.0);
        List orderItems = order.getOrderItems();
        for (Object objectItem : orderItems) {
            OrOrderItem item = (OrOrderItem) objectItem;
            for (HtlPrice htlprice : htlpricelist) {
                if (0 == DateUtil.compare(item.getNight(),htlprice.getAbleSaleDate())) {// 比较日期
                    item.setBasePrice(htlprice.getBasePrice());
                    item.setSalePrice(htlprice.getSalePrice());
                    item.setMarketPrice(htlprice.getSalePrice());
                    if (order.getPayMethod().equals(PayMethod.PAY)) {
                        order.setSumRmb(order.getSumRmb() + htlprice.getSalePrice());
                        order.setSum(order.getSum() + htlprice.getSalePrice());
                    } else {
                        order.setSumRmb(order.getSumRmb() + htlprice.getBasePrice());
                        order.setSum(order.getSum() + htlprice.getSalePrice());
                    }
                }
            }
        }

        AddExRoomOrderRequest addExRoomOrderRequest = new AddExRoomOrderRequest();
        // // 将order和 params的数据封装到 AddExRoomOrderRequest 中
        addExRoomOrderRequest = checkservice
            .encapsulationData(addExRoomOrderRequest, order, params);

        // 通过HDL进行预订
        try {
            addExRoomOrderResponse = hdlService.addExRoomOrder(addExRoomOrderRequest);

            // 重新计算价格
            order.setSumRmb(0.0);
            order.setSum(0.0);
            for (Object objectItem : orderItems) {
                OrOrderItem item = (OrOrderItem) objectItem;
                for (HtlPrice htlprice : htlpricelist) {
                    if (0 == DateUtil.compare(item.getNight(),htlprice.getAbleSaleDate())) {// 比较日期
                        order.setSumRmb(order.getSumRmb() + htlprice.getSalePrice());
                        order.setSum(order.getSum() + htlprice.getSalePrice());
                    }
                }
            }

        } catch (Exception e) {
        	log.error("=============CheckOrderAction.process()==hdlService.addExRoomOrder() exception :",e);
            return forwardMsgBox("预订失败", "post");
        }

        // 预订后结果返回
        if (null != addExRoomOrderResponse) {
            mgresult = addExRoomOrderResponse.getResult();
            // 判断定单是否成功,1为成功,0为不成功
            if (1 == mgresult.getValue()) {
                // 订单预订成功后,将订单数据更新
                order.setOrderCdForChannel(mgresult.getMessage());

                // v2.5 生成一条给酒店的确认记录 add by chenkeming@2009-04-08
                OrOrderFax orderFax = new OrOrderFax();
                orderFax.setChannel(ConfirmType.DIRECT);
                orderFax.setType(HotelConfirmType.CONFIRM);
                orderFax
                    .setModelType(order.isMango() ? ModelType.MODEL_MANGO : ModelType.MODEL_114);
                if (null == roleUser) {
                    roleUser = getOnlineRoleUser();
                }
                if (null != roleUser) {
                    orderFax.setSendMan(roleUser.getName());
                }
                orderFax.setSendTime(new Date());
                orderFax.setSendSucceed(true);
                orderFax.setIsConfirm(true);
                orderFax.setConfirmNo(mgresult.getMessage());
                orderFax.setHotelReturn(true);
                orderFax.setValidConfirm(true);
                orderFax.setHotelId(order.getHotelId());
                orderFax.setOrder(order);
                orderFax.setLogList(null);
                order.getFaxList().add(orderFax);

                orderService.saveOrUpdate(order);
                return "ORDERROOMSUCCESS";
            } else {
                return forwardMsgBox("预订酒店房间失败," + mgresult.getMessage(), "post");
            }
        } else {
            return forwardError("预订失败");
        }

    }

    /**
     * 预订外部酒店(DB) add by shizhongwen 时间:Dec 23, 2008 10:18:49 AM
     * 
     * @return
     */
    public String checkReservationOrder() {

        Map params = getParams();

        CheckReservateExRequest req = new CheckReservateExRequest();
        CheckReservateExResponse res = null;
        orderId = (String) params.get("orderId");
        channel = (String) params.get("channel");
        OrOrder order = orderService.getOrder(Long.parseLong(orderId));
        List<OrOrderItem> orderItems = order.getOrderItems();
        Date checkindate = order.getCheckinDate();
        Date checkoutdate = order.getCheckoutDate();
        difdays = DateUtil.getDay(checkindate, checkoutdate);
        List<HtlPrice> htlpricelist = hotelManage.queryHtlPrice(order.getHotelId(), order
            .getRoomTypeId(), order.getChildRoomTypeId(), order.getPayMethod(), DateUtil
            .dateToString(order.getCheckinDate()), DateUtil.dateToString(order.getCheckoutDate()));
        List dayslist = DateUtil.getDates(checkindate, checkoutdate);
        // add by shizhongwen 2009-03-08 begin 如果orderItems 为空，则从htlprice 表中取数据 然后更新到数据库中

        boolean bPay = PayMethod.PAY.equals(order.getPayMethod()); 
        String[] fellowNamesArr = null;
        if (bPay) {
            fellowNamesArr = OrderUtil.fillFellowNamesToOrderItem(order); // 每天各个房间的入住人姓名数组 v2.4.2
                                                                          // chenjiajie 2008-12-30
        }
        List<OrOrderItem> oneRoomItem = new ArrayList<OrOrderItem>();
        if (null == orderItems || 0 >= orderItems.size()) {
            int index = 0;
            for (int i = 0; i < difdays; i++) {
                for (int roomCount = 0; roomCount < order.getRoomQuantity(); roomCount++) {
                    OrOrderItem item = new OrOrderItem();
                    item.setOrder(order);
                    item.setDayIndex(i);
                    item.setRoomIndex(roomCount);
                    //增加畅联订单item的FirstNight和LastNight
                    item.setFirstNight(0 == roomCount);
                    item.setLastNight(roomCount == order.getRoomQuantity() - 1);
                    item.setQuantity(1);
                    item.setQuotaType(QuotaType.GENERALQUOTA);
                    if(bPay) {
                    	item.setFellowName(fellowNamesArr[roomCount]);
                    }
                    /**
                     * 生产bug528 网站来源的直联订单没有生成日审记录 因为没有保存酒店id到OrderItem，查看日审记录的时候无法关联查询
                     * OrderItem的orderItemsType属性应该用OrderItemType.NORMAL的值，不设置数据库会默认为0 add by
                     * chenjiajie 2009-06-04
                     */
                    item.setOrderItemsType(OrderItemType.NORMAL);
                    item.setHotelId(order.getHotelId());
                    for (HtlPrice htlprice : htlpricelist) {
                        if (0 == DateUtil.compare((Date)dayslist.get(i),
                            htlprice.getAbleSaleDate())) {// 比较日期
                            item.setBasePrice(htlprice.getBasePrice());
                            item.setSalePrice(htlprice.getSalePrice());
                            item.setMarketPrice(htlprice.getSalesroomPrice());
                            index++;
                        }
                    }
                    item.setNight((Date) dayslist.get(i));
                    orderItems.add(item);

                    if (0 == roomCount) {
                        oneRoomItem.add(item);
                    }
                }
            }
            order.setOrderItems(orderItems);
            // orderService.saveOrUpdate(order);
        } else {
            // orderItem总个数
            int orderItemSize = orderItems.size();
            // 房间总个数
            int roomCnt = order.getRoomQuantity();
            if (orderItemSize >= roomCnt * 2 - 1) {
                for (int i = 0; i < orderItemSize; i += roomCnt) {
                    oneRoomItem.add(orderItems.get(i));
                }
            } else {
                oneRoomItem.add(orderItems.get(0));
            }
        }
        // add by shizhongwen end
        if (null != channel) {
            req.setChannelType(Integer.valueOf(channel).intValue());
        }
        req.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
        req.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
        req.setChildRoomTypeId(order.getChildRoomTypeId());
        req.setHotelId(order.getHotelId());
        req.setQuantity(order.getRoomQuantity());
        req.setRoomTypeId(order.getRoomTypeId());
        req.setChainCode(null);
        for (int i = 0; i < difdays; i++) {
            MGExReservItem item = new MGExReservItem();
            item.setDayIndex(i);
            if (null == params.get("hBasePrice" + i)) {
                item.setBasePrice((float) 0.0);
            } else {
                item.setBasePrice(Float.parseFloat((String) params.get("hBasePrice" + i)));
            }
            if (null == params.get("hSalePrice" + i)) {
                item.setSalePrice((float) 0.0);
            } else {
                item.setSalePrice(Float.parseFloat((String) params.get("hSalePrice" + i)));
            }
            req.getReservItems().add(item);
        }

        try {
            res = hdlService.checkReservate(req);

            if (null != res) {
                // //判断预订定单是否成功,1为成功,0为不成功
                if (1 == res.getResult()) {
                } else {
                    return forwardMsgBox(res.getReason(), null);
                }

            }

        } catch (Exception e) {
            log.error("=============CheckOrderAction.checkReservationOrder()==hdlService.checkReservate() exception : ",e);
            return forwardMsgBox("查询信息失败(外部酒店webservice服务未开启或断开连接!!!)。", null);

        }
        request.setAttribute("dateStrList", DateUtil.getDateStrList(checkindate, checkoutdate,
            false));
        request.setAttribute("checkRes", res);
        request.setAttribute("checkinDate", DateUtil.dateToString(order.getCheckinDate()));
        request.setAttribute("checkoutDate", DateUtil.dateToString(order.getCheckoutDate()));
        request.setAttribute("orderstate", order.getOrderState());
        request.setAttribute("quantity", order.getRoomQuantity());
        request.setAttribute("oldorderItems", oneRoomItem);
        request.setAttribute("payMethod", order.getPayMethod());
        request.setAttribute("ordercdforchannel", order.getOrderCdForChannel());
        return "webcheckPreOrder";

    }

    /**
     * 直连酒店针对某个子房型点击预订，弹出试预订窗口 TODO: 预订失败信息处理
     * 
     * @return
     */
    public String checkPreOrder() {
        Map params = getParams();
        CheckReservateExRequest req = new CheckReservateExRequest();
        MyBeanUtil.copyProperties(req, params);
        req.setChannelType(Integer.parseInt((String) params.get("channel")));
        CheckParamVO checkParams = new CheckParamVO();
        MyBeanUtil.copyProperties(checkParams, params);
        CheckReservateExResponse res = null;
        // TODO: 房间数量如果为零,就修改成1
        if (0 == hotelroomcount) {
            req.setQuantity(1);
        } else {
            req.setQuantity(hotelroomcount);
        }
        try {
            for (int i = 0; i < difdays; i++) {
                MGExReservItem item = new MGExReservItem();
                item.setDayIndex(i);
                if (null == params.get("hBasePrice" + i)) {
                    item.setBasePrice((float) 0.0);
                } else {
                    item.setBasePrice(Float.parseFloat((String) params.get("hBasePrice" + i)));
                }
                if (null == params.get("hSalePrice" + i)) {
                    item.setSalePrice((float) 0.0);
                } else {
                    item.setSalePrice(Float.parseFloat((String) params.get("hSalePrice" + i)));
                }
                req.getReservItems().add(item);
            }
            res = hdlService.checkReservate(req);
            // ************ 如果销售价和底价没有在HDL中作处理,此作业务逻辑处理
            // 如果是预付，则重新计算预付价
            if (PayMethod.PRE_PAY.equals(checkParams.getPayMethod())) {
                List<MGExReservItem> resList = res.getReservItems();
                List<MGExReservItem> reqList = req.getReservItems();
                for (int i = 0; i < difdays; i++) {
                    MGExReservItem oriItem = reqList.get(i);
                    MGExReservItem item = resList.get(i);
                    item.setSalePrice(item.getBasePrice() + oriItem.getSalePrice()
                        - oriItem.getBasePrice());
                }
            }

        } catch (Exception e) {
        	log.error("=============CheckOrderAction.checkPreOrder()==hdlService.checkReservate() exception : ",e);
            return forwardMsgBox("系统连接对方接口超时，请稍候在试！！", null);
        }
        if (null == res) {
            return forwardMsgBox("暂时没找到试预订信息，请确认HBIZ产品映射管理-房型映射信息已经设定完成,如果映射存在,请联系合作方", null);
        }

        request.setAttribute("checkParams", checkParams);
        request.setAttribute("checkRes", res);
        request.setAttribute("dateStrList", DateUtil.getDateStrList(checkInDate, checkOutDate,
            false));
        request.setAttribute("quantity", req.getQuantity());

        // 直联酒店ChinaOnline可能需要使用的首日房价 add by chenjiajie V2.5@2009-02-02
        float colFirstDayPrice = 0;
        if (null != res) {
            for (MGExReservItem mgExReservItem : res.getReservItems()) {
                if (0 < mgExReservItem.getFirstDayPrice()) {
                    colFirstDayPrice = mgExReservItem.getFirstDayPrice();
                }
            }
        }
        request.setAttribute("colFirstDayPrice", String.valueOf(colFirstDayPrice));

        return "checkPreOrder";
    }

    /**
     * 输入开始时间和结束时间查询合作方价格信息,与checkPreOrder功能相似 Hotel 2.5 add by guojun 2009-02-12 09:25
     * 
     * @return
     */
    public String checkIsModifyPreOrder() {
        Map params = getParams();
        CheckReservateExRequest req = new CheckReservateExRequest();
        MyBeanUtil.copyProperties(req, params);
        req.setChannelType(Integer.parseInt((String) params.get("channel")));
        CheckParamVO checkParams = new CheckParamVO();
        MyBeanUtil.copyProperties(checkParams, params);
        CheckReservateExResponse res = null;
        // TODO: 房间数量如果为零,就修改成1
        if (0 == hotelroomcount) {
            req.setQuantity(1);
        } else {
            req.setQuantity(hotelroomcount);
        }
        try {
            int interval = DateUtil.getDay(DateUtil.getDate(req.getCheckInDate()), DateUtil
                .getDate(req.getCheckOutDate()));
            for (int i = 0; i < interval; i++) {
                MGExReservItem item = new MGExReservItem();
                item.setDayIndex(i);
                if (null == params.get("hBasePrice" + i)) {
                    item.setBasePrice((float) 0.0);
                } else {
                    item.setBasePrice(Float.parseFloat((String) params.get("hBasePrice" + i)));
                }
                if (null == params.get("hSalePrice" + i)) {
                    item.setSalePrice((float) 0.0);
                } else {
                    item.setSalePrice(Float.parseFloat((String) params.get("hSalePrice" + i)));
                }
                req.getReservItems().add(item);
            }
            res = hdlService.checkReservate(req);
        } catch (Exception e) {
        	log.error("===================CheckOrderAction.checkIsModifyPreOrder()==hdlService.checkReservate() exception : ",e);
            return forwardMsgBox("系统连接对方接口超时，请稍候在试！！", null);
        }
        if (null == res) {
            return forwardMsgBox("暂时没找到试预订信息，请确认HBIZ产品映射管理-房型映射信息已经设定完成,如果映射存在,请联系合作方", null);
        }
        request.setAttribute("checkParams", checkParams);
        request.setAttribute("checkRes", res);
        request.setAttribute("returnCheckInDate", DateUtil.toStringByFormat(checkInDate,
            "yyyy-MM-dd"));
        request.setAttribute("returnCheckOutDate", DateUtil.toStringByFormat(checkOutDate,
            "yyyy-MM-dd"));
        request.setAttribute("dateStrList", DateUtil.getDateStrList(checkInDate, checkOutDate,
            false));
        setDifdays(DateUtil.getDay(checkInDate, checkOutDate));
        request.setAttribute("quantity", req.getQuantity());

        // 直联酒店ChinaOnline可能需要使用的首日房价 add by chenjiajie V2.5@2009-02-02
        float colFirstDayPrice = 0;
        if (null != res) {
            for (MGExReservItem mgExReservItem : res.getReservItems()) {
                if (0 < mgExReservItem.getFirstDayPrice()) {
                    colFirstDayPrice = mgExReservItem.getFirstDayPrice();
                }
            }
        }
        request.setAttribute("colFirstDayPrice", String.valueOf(colFirstDayPrice));
        return "checkModifyPreOrder";
    }

    public int getDifdays() {
        return difdays;
    }

    public void setDifdays(int difdays) {
        this.difdays = difdays;
    }

    public IHDLService getHdlService() {
        return hdlService;
    }

    public void setHdlService(IHDLService hdlService) {
        this.hdlService = hdlService;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getReturntag() {
        return returntag;
    }

    public void setReturntag(String returntag) {
        this.returntag = returntag;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(Long childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public int getHotelroomcount() {
        return hotelroomcount;
    }

    public void setHotelroomcount(int hotelroomcount) {
        this.hotelroomcount = hotelroomcount;
    }

    public static void main(String[] args) {
        CheckOrderAction ss = new CheckOrderAction();
        ss.checkReservationOrder();
    }

    public IOrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    /**
     * 处理重复提交
     */
    protected boolean isRepeatSubmit() {
        String strutsToken = (String) getParams().get("struts.token");
        String sessionToken = (String) getFromSession("struts.token.session");
        if (StringUtil.StringEquals2(strutsToken, sessionToken)) {
            return true;
        }
        putSession("struts.token.session", strutsToken);
        return false;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public CheckOrderService getCheckservice() {
        return checkservice;
    }

    public void setCheckservice(CheckOrderService checkservice) {
        this.checkservice = checkservice;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public void setDatemap_sale(Map datemap_sale) {
        this.datemap_sale = datemap_sale;
    }

    public Map getDatemap_sale() {
        return datemap_sale;
    }

    public void setDatemap_base(Map datemap_base) {
        this.datemap_base = datemap_base;
    }

    public Map getDatemap_base() {
        return datemap_base;
    }

}
