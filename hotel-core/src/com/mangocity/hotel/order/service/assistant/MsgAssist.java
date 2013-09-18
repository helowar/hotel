package com.mangocity.hotel.order.service.assistant;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.dom4j.DocumentHelper;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderFax;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.service.impl.OrderService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.MemberUtil;
import com.mangocity.util.MoneyHandle;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.BeanUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.log.MyLog;

/**
 * 传真邮件辅助类
 * 
 * @author chenkeming
 * 
 */
public class MsgAssist implements Serializable {
	private static final MyLog log = MyLog.getLogger(MsgAssist.class);
    private ResourceManager resourceManager;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

    private HotelManage hotelManage;
    
    private HotelRoomTypeService hotelRoomTypeService;
    
    /**
     * 给Email预览的时候使用，因为CC预览的是使用html模板替换字符串的方式填充数据，
     * 而立减优惠金额只有在特殊的情况下才会显示，在非立减订单预览的时候需要把常量中的字符串替换
     * add by chenjiajie 2009-10-27
     */
    private static final String BENEFIT_AMOUNT_TEMPLATE_STR = "立减金额：<font color='red'>[benefitAmount]</font>";
    
    /**
     * 给Email预览的时候使用，因为CC预览的是使用html模板替换字符串的方式填充数据，
     * 而立减优惠金额只有在特殊的情况下才会显示，在非立减订单预览的时候需要把常量中的字符串替换
     * add by chenjiajie 2009-10-27
     */
    private static final String ACTUAL_AMOUNT_TEMPLATE_STR = "&nbsp;&nbsp;应付金额：<font color='red'>[actualAmount]</font>";

    /**
     * 供邮件预览用
     */
    private Map mailMap;

    public static Map fellowNation;

    public void init() {
        mailMap = new HashMap();
        mailMap.put("linkman", Long.valueOf(1));
        mailMap.put("aliasName", Long.valueOf(1));
        mailMap.put("checkInDate", Long.valueOf(1));
        mailMap.put("checkOutDate", Long.valueOf(1));
        mailMap.put("hotelName", Long.valueOf(1));
        mailMap.put("nightRoomCount", Long.valueOf(1));
        mailMap.put("nightCount", Long.valueOf(1));
        mailMap.put("hotelRoomType", Long.valueOf(1));
        mailMap.put("orderCD", Long.valueOf(1));
        mailMap.put("customerInfo", Long.valueOf(1));
        mailMap.put("isContainMeal", Long.valueOf(1));
        mailMap.put("roomPrice", Long.valueOf(1));
        mailMap.put("totalPrice", Long.valueOf(1));
        mailMap.put("payMethod", Long.valueOf(1));
        mailMap.put("note", Long.valueOf(1));
        mailMap.put("hotelConfirmNo", Long.valueOf(1));
        mailMap.put("hotelAddressTelephone", Long.valueOf(1));
        mailMap.put("hotelTelephone", Long.valueOf(1));
        mailMap.put("hotelFax", Long.valueOf(1));
        mailMap.put("arrivalTimeStart", Long.valueOf(1));
        mailMap.put("specialRequestOverView", Long.valueOf(1));
        mailMap.put("benefitAmount", Long.valueOf(1));
        mailMap.put("actualAmount", Long.valueOf(1));

        fellowNation = new HashMap();
        fellowNation.put("1", "内宾");
        fellowNation.put("2", "外宾");
        fellowNation.put("3", "港澳台");
    }

    /**
     * 根据传过来的OrderFax对象,组建一个org.dom4j.Document对象,提供给邮件和传真使用的XML数据模型
     * 
     * @param orderFax
     * @return XML String
     */
    public String genOrderFaxXml(OrderFax orderFax) {

        org.dom4j.Document document = DocumentHelper.createDocument();
        org.dom4j.Element rootElementE = document.addElement("hotelOrder");

        BeanUtil.SetNullDefault(orderFax);

        PropertyUtilsBean bean = new PropertyUtilsBean();
        PropertyDescriptor[] origDescriptors = bean.getPropertyDescriptors(orderFax.getClass());

        try {
            for (int j = 0; j < origDescriptors.length; j++) {
                String name = origDescriptors[j].getName();
                if ("class".equals(name)) {
                    continue;
                }
                if (bean.isReadable(orderFax, name)) {

                    if (name.equals("orderItemList")) {
                        // 订单明细
                        org.dom4j.Element orderItemsE = rootElementE.addElement(name);
                        List orderItems = orderFax.getOrderItemList();
                        if (null != orderItems) {
                            for (int itemNum = 0; itemNum < orderItems.size(); itemNum++) {

                                OrderItemFax orderItem = (OrderItemFax) orderItems.get(itemNum);

                                BeanUtil.SetNullDefault(orderItem);

                                PropertyUtilsBean itemBean = new PropertyUtilsBean();
                                PropertyDescriptor[] desc = itemBean
                                    .getPropertyDescriptors(orderItem.getClass());

                                org.dom4j.Element orderItemE = orderItemsE.addElement("orderItem");
                                for (int k = 0; k < desc.length; k++) {
                                    String propName = desc[k].getName();
                                    if ("class".equals(propName)) {
                                        continue;
                                    }
                                    if (bean.isReadable(orderItem, propName)) {
                                        org.dom4j.Element orderCDE = orderItemE
                                            .addElement(propName);
                                        Object value = itemBean.getProperty(orderItem, propName);
                                        orderCDE.setText(value.toString());
                                    }
                                }
                            }
                        }
                    } else {
                        org.dom4j.Element orderCDE = rootElementE.addElement(name);
                        Object value = bean.getProperty(orderFax, name);
                        orderCDE.setText(value.toString());
                    }
                }
            }
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }

        String strXML = document.asXML();
        log.info("生成的XML数据模型的字符串======" + strXML);
        return strXML;
    }

    /**
     * 计算按底价(结算价)结算的订单的总金额
     * 
     * @param order
     * @return
     */
    private double calOrderBaseSum(OrOrder order) {
        List<OrOrderItem> orderItemlist = order.getOrderItems();
        double sum = 0.0D;
        for (OrOrderItem orderItem : orderItemlist) {
            sum += orderItem.getBasePrice();
        }
        return sum;
    }
    
    /**
     * 计算按售价结算的订单的总金额
     * @param order
     * @return
     */
    private double calOrderSaleSum(OrOrder order) {
    	List<OrOrderItem> orderItemlist = order.getOrderItems();
        double sum = 0.0D;
        for (OrOrderItem orderItem : orderItemlist) {
            sum += orderItem.getSalePrice() + orderItem.getFavourableAmount();
        }
        return sum;
    }

    /**
     * 生成酒店确认传真 114和mango共用
     * 
     * @param order
     * @param hotel
     * @param itemList
     * @return
     */
    public String genOrderFaxByHotelFaxConfirm(OrOrder order, HtlHotel hotel, String isAnewSend,
        Long ID, List itemList, Map modifiedInfo) {
        OrderFax orderFax = new OrderFax();
        // BeanUtil.SetNullDefault(order);
        Date nowTime = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss",Locale.CHINA);
        orderFax.setNowDate(df.format(nowTime));
        orderFax.setNewTime(dt.format(nowTime));
        orderFax.setOrderCD(order.getOrderCDHotel());

        if (null != modifiedInfo.get("sender")) {
            if (!((String) modifiedInfo.get("sender")).equals("1")) {
                orderFax.setFaxType((String) modifiedInfo.get("sender"));
            }
        }
        if(order.getRmpOrder()){
        	orderFax.setSupplierName(order.getOrOrderRMP().getSupplierName());
        }
        String cityName = "";
        if(null!=order.getMemberState()&&!"XJG".equals(order.getMemberState())){
        	cityName = (String) MemberUtil.chineseMap.get((modifiedInfo.get("city")));
        }
        orderFax.setCityName(cityName);
        //添加供应商名称

        // 酒店传真绑定ID
        orderFax.setBarCode("HA" + ID);
        String hotelName = null == hotel.getChnName() ? hotel.getEngName() : hotel.getChnName();
        orderFax.setHotelName(hotelName);
        orderFax.setHotelFax((String) modifiedInfo.get("faxNum"));
        orderFax.setHotelEmail((String) modifiedInfo.get("faxNum"));
        String nameInfo = "";
        List fellowList = order.getFellowList();
        for (int i = 0; i < fellowList.size(); i++) {
            OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
            nameInfo += fellow.getFellowName() + "("
                + fellowNation.get(fellow.getFellowNationality());
            if (fellow.isFellowSub()) {
                nameInfo += ",代订)      ";
            } else {
                nameInfo += ")      ";
            }
        }
        orderFax.setCustomerInfo(nameInfo);
        orderFax.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
        orderFax.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
        int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        orderFax.setNightCount(nightCount);
        int nrc = nightCount * order.getRoomQuantity();
        orderFax.setNightRoomCount(nrc);

        if (null != itemList && 0 < itemList.size()) {
            for (int i = 0; i < itemList.size(); i++) {
                OrderItemGroupBy oriItem = (OrderItemGroupBy) itemList.get(i);
                OrderItemFax item = new OrderItemFax();
                item.setItemCheckInDate(DateUtil.dateToString(oriItem.getNight()));
                if (order.isShowBasePrice()) {
                    item.setBasePrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                        + oriItem.getBasepriceByAvg());
                } else {
                    item.setSalePrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                        + oriItem.getSalepriceByAvg());
                }
                item.setItemCount(oriItem.getQuantityByNight());
                item.setItemName(order.getRoomTypeName() + "(" 
                        + order.getChildRoomTypeName() + ")");
                item.setPayType(order.getPayMethod().equals(PayMethod.PAY) ? "面付" : "预付");
                if(order.getRmpOrder()){
                	item.setPayType("月结");
                }                
                if (0 == oriItem.getBreakfast()) {
                    item.setBreakfast("无");
                } else {
                    String breakfast = resourceManager.getDescription("breakfast_type", oriItem
                        .getBreakfast())
                        + "("
                        + resourceManager
                            .getDescription("breakfast_num_new", oriItem.getBreakfastNum()) + ")";
                    item.setBreakfast(breakfast);
                }

                orderFax.getOrderItemList().add(item);
            }
        }
        // 是否为配额内配额
        OrderService orderService = new OrderService();
        boolean isQuotaInner = orderService.getIsSystemQuota(order);
        if (isQuotaInner) {
            orderFax.setIsQuotaInner("1");
        } else {
            orderFax.setIsQuotaInner("0");
        }
        // 是否重发
        orderFax.setIsAnewSend(isAnewSend);
        // 床型
        if (1 == order.getBedType()) {
            orderFax.setBedType("大床");
        } else if (2 == order.getBedType()) {
            orderFax.setBedType("双床");
        } else if (3 == order.getBedType()) {
            orderFax.setBedType("单床");
        } else {
            orderFax.setBedType("无需求");
        }
        if (order.isShowBasePrice()) {
            orderFax.setTotalPrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                + calOrderBaseSum(order));
        } else {
            orderFax.setTotalPrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                + calOrderSaleSum(order));
        }

        orderFax.setArrivalTimeStart(order.getArrivalTime());
        orderFax.setArrivalTimeEnd(order.getLatestArrivalTime());
        String arrivalTraffic = "";
        if (null != order.getArrivalTraffic()) {
            arrivalTraffic = resourceManager.getDescription("res_vehicleType", order
                .getArrivalTraffic());
            if (null != order.getFlight()) {
                arrivalTraffic += order.getFlight();
            }
        }
        orderFax.setArrivalTraffic(arrivalTraffic);
        orderFax.setSpecialRequestOverView(order.getSpecialRequest());

        // TODO: 一期是根据担保状态插入notes
        orderFax.setNote((String) modifiedInfo.get("tohotelNotes"));

        // TODO: 获取酒店所有房型信息
        String space = "     ";
        String hotelRoomTypeStr = "□全部房型" + space;
        // hotelRoomTypeStr += ("□" + order.getRoomTypeName() + space);
        List lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotel.getID());
        for (int i = 0; i < lstRoomType.size(); i++) {
            HtlRoomtype roomType = (HtlRoomtype) lstRoomType.get(i);
            hotelRoomTypeStr += ("□" + roomType.getRoomName() + space);
        }
        orderFax.setHotelRoomType(hotelRoomTypeStr);

        return genOrderFaxXml(orderFax);
    }

    /**
     * 生成酒店修改传真 114和mango共用
     * 
     * @param order
     * @param hotel
     * @param itemList
     * @return
     */
    public String genOrderFaxByHotelFaxModify(OrOrder order, HtlHotel hotel, String isAnewSend,
        Long ID, List itemList, Map modifiedInfo) {
        OrderFax orderFax = new OrderFax();
        // BeanUtil.SetNullDefault(order);
        Date nowTime = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss",Locale.CHINA);
        orderFax.setNowDate(df.format(nowTime));
        orderFax.setNewTime(dt.format(nowTime));
        orderFax.setOrderCD(order.getOrderCDHotel());

        String cityName = "";
        if(null!=order.getMemberState()&&!"XJG".equals(order.getMemberState())){
        	cityName = (String) MemberUtil.chineseMap.get((modifiedInfo.get("city")));
        }
        if(order.getRmpOrder()){
        	orderFax.setSupplierName(order.getOrOrderRMP().getSupplierName());
        }
        orderFax.setCityName(cityName);
        if (null != modifiedInfo.get("sender")) {
            if (!((String) modifiedInfo.get("sender")).equals("1")) {
                orderFax.setFaxType((String) modifiedInfo.get("sender"));
            }
        }
        // 酒店传真绑定ID
        orderFax.setBarCode("HA" + ID);
        String hotelName = null == hotel.getChnName() ? hotel.getEngName() : hotel.getChnName();
        orderFax.setHotelName(hotelName);
        orderFax.setHotelFax((String) modifiedInfo.get("faxNum"));
        orderFax.setHotelEmail((String) modifiedInfo.get("faxNum"));
        String nameInfo = "";
        List fellowList = order.getFellowList();
        for (int i = 0; i < fellowList.size(); i++) {
            OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
            nameInfo += fellow.getFellowName() + "("
                + fellowNation.get(fellow.getFellowNationality());
            if (fellow.isFellowSub()) {
                nameInfo += ",代订)      ";
            } else {
                nameInfo += ")      ";
            }
        }
        orderFax.setCustomerInfo(nameInfo);
        orderFax.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
        orderFax.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
        int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        orderFax.setNightCount(nightCount);
        int nrc = nightCount * order.getRoomQuantity();
        orderFax.setNightRoomCount(nrc);

        if (null != itemList && 0 < itemList.size()) {
            for (int i = 0; i < itemList.size(); i++) {
                OrderItemGroupBy oriItem = (OrderItemGroupBy) itemList.get(i);
                OrderItemFax item = new OrderItemFax();
                item.setItemCheckInDate(DateUtil.dateToString(oriItem.getNight()));
                if (order.isShowBasePrice()) {
                    item.setBasePrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                        + oriItem.getBasepriceByAvg());
                } else {
                    item.setSalePrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                        + oriItem.getSalepriceByAvg());
                }
                item.setItemCount(oriItem.getQuantityByNight());
                item
                    .setItemName(order.getRoomTypeName() + "(" 
                        + order.getChildRoomTypeName() + ")");
                item.setPayType(order.getPayMethod().equals(PayMethod.PAY) ? "面付" : "预付");
                if(order.getRmpOrder()){
                	item.setPayType("月结");	
                }  
                if (0 == oriItem.getBreakfast()) {
                    item.setBreakfast("无");
                } else {
                    String breakfast = resourceManager.getDescription("breakfast_type", oriItem
                        .getBreakfast())
                        + "("
                        + resourceManager
                            .getDescription("breakfast_num_new", oriItem.getBreakfastNum()) + ")";
                    item.setBreakfast(breakfast);
                }

                orderFax.getOrderItemList().add(item);
            }
        }
        for (int i = order.getFaxList().size() - 1; 0 <= i; i--) {
            OrOrderFax orOrderFax = order.getFaxList().get(i);
            if (orOrderFax.isValidConfirm()) {
                String confirmNo = orOrderFax.getConfirmNo();
                // 酒店确认号
                orderFax.setConfirmNo(confirmNo);
                break;
            }
        }
        // 是否为配额内配额
        OrderService orderService = new OrderService();
        boolean isQuotaInner = orderService.getIsSystemQuota(order);
        if (isQuotaInner) {
            orderFax.setIsQuotaInner("1");
        } else {
            orderFax.setIsQuotaInner("0");
        }
        // 是否重发
        orderFax.setIsAnewSend(isAnewSend);
        // 床型
        if (1 == order.getBedType()) {
            orderFax.setBedType("大床");
        } else if (2 == order.getBedType()) {
            orderFax.setBedType("双床");
        } else if (3 == order.getBedType()) {
            orderFax.setBedType("单床");
        } else {
            orderFax.setBedType("无需求");
        }

        if (order.isShowBasePrice()) {
            orderFax.setTotalPrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                + calOrderBaseSum(order));
        } else {
            orderFax.setTotalPrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                + calOrderSaleSum(order));
        }

        orderFax.setArrivalTimeStart(order.getArrivalTime());
        orderFax.setArrivalTimeEnd(order.getLatestArrivalTime());
        String arrivalTraffic = "";
        if (null != order.getArrivalTraffic()) {
            arrivalTraffic = resourceManager.getDescription("res_vehicleType", order
                .getArrivalTraffic());
            if (null != order.getFlight()) {
                arrivalTraffic += order.getFlight();
            }
        }

        orderFax.setArrivalTraffic(arrivalTraffic);
        orderFax.setSpecialRequestOverView(order.getSpecialRequest());

        // TODO: 一期是根据担保状态插入notes
        orderFax.setNote((String) modifiedInfo.get("tohotelNotes"));

        // TODO: 获取酒店所有房型信息
        String space = "";
        String hotelRoomTypeStr = "□全部房型" + space;
        // hotelRoomTypeStr += ("□" + order.getRoomTypeName() + space);
        List lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotel.getID());
        for (int i = 0; i < lstRoomType.size(); i++) {
            HtlRoomtype roomType = (HtlRoomtype) lstRoomType.get(i);
            hotelRoomTypeStr += ("□" + roomType.getRoomName() + space);
        }
        orderFax.setHotelRoomType(hotelRoomTypeStr);

        return genOrderFaxXml(orderFax);
    }

    /**
     * 生成酒店取消传真 114和mango共用
     * 
     * @param order
     * @param hotel
     * @param itemList
     * @return
     */
    public String genOrderFaxByHotelFaxCancel(OrOrder order, HtlHotel hotel, String isAnewSend,
        Long ID, List itemList, Map modifiedInfo) {
        OrderFax orderFax = new OrderFax();
        // BeanUtil.SetNullDefault(order);
        Date nowTime = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss",Locale.CHINA);
        orderFax.setNowDate(df.format(nowTime));
        orderFax.setNewTime(dt.format(nowTime));
        orderFax.setOrderCD(order.getOrderCDHotel());
        if (null != modifiedInfo.get("sender")) {
            if (!((String) modifiedInfo.get("sender")).equals("1")) {
                orderFax.setFaxType((String) modifiedInfo.get("sender"));
            }
        }
        String cityName = "";
        if(null!=order.getMemberState()&&!"XJG".equals(order.getMemberState())){
        	cityName = (String) MemberUtil.chineseMap.get(MemberUtil.codeMap.get(modifiedInfo.get("city").toString()));
        }
        if(order.getRmpOrder()){
        	orderFax.setSupplierName(order.getOrOrderRMP().getSupplierName());
        }
        orderFax.setCityName(cityName);

        // 酒店传真绑定ID
        orderFax.setBarCode("HA" + ID);
        String hotelName = null == hotel.getChnName() ? hotel.getEngName() : hotel.getChnName();
        orderFax.setHotelName(hotelName);
        orderFax.setHotelFax((String) modifiedInfo.get("faxNum"));
        orderFax.setHotelEmail((String) modifiedInfo.get("faxNum"));
        String nameInfo = "";
        List fellowList = order.getFellowList();
        for (int i = 0; i < fellowList.size(); i++) {
            OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
            nameInfo += fellow.getFellowName() + "("
                + fellowNation.get(fellow.getFellowNationality());
            if (fellow.isFellowSub()) {
                nameInfo += ",代订)      ";
            } else {
                nameInfo += ")      ";
            }
        }
        // 是否为配额内配额
        OrderService orderService = new OrderService();
        boolean isQuotaInner = orderService.getIsSystemQuota(order);
        if (isQuotaInner) {
            orderFax.setIsQuotaInner("1");
        } else {
            orderFax.setIsQuotaInner("0");
        }
        // 是否重发
        orderFax.setIsAnewSend(isAnewSend);
        // 床型
        if (1 == order.getBedType()) {
            orderFax.setBedType("大床");
        } else if (2 == order.getBedType()) {
            orderFax.setBedType("双床");
        } else if (3 == order.getBedType()) {
            orderFax.setBedType("单床");
        } else {
            orderFax.setBedType("无需求");
        }
        orderFax.setCustomerInfo(nameInfo);
        orderFax.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
        orderFax.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
        int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        orderFax.setNightCount(nightCount);
        int nrc = nightCount * order.getRoomQuantity();
        orderFax.setNightRoomCount(nrc);

        if (null != itemList && 0 < itemList.size()) {
            for (int i = 0; i < itemList.size(); i++) {
                OrderItemGroupBy oriItem = (OrderItemGroupBy) itemList.get(i);
                OrderItemFax item = new OrderItemFax();
                item.setItemCheckInDate(DateUtil.dateToString(oriItem.getNight()));
                if (order.isShowBasePrice()) {
                    item.setBasePrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                        + oriItem.getBasepriceByAvg());
                } else {
                    item.setSalePrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                        + oriItem.getSalepriceByAvg());
                }
                item.setItemCount(oriItem.getQuantityByNight());
                item
                    .setItemName(order.getRoomTypeName() + "(" 
                        + order.getChildRoomTypeName() + ")");
                item.setPayType(order.getPayMethod().equals(PayMethod.PAY) ? "面付" : "预付");
                if(order.getRmpOrder()){
                	item.setPayType("月结");	
                }  
                if (0 == oriItem.getBreakfast()) {
                    item.setBreakfast("无");
                } else {
                    String breakfast = resourceManager.getDescription("breakfast_type", oriItem
                        .getBreakfast())
                        + "("
                        + resourceManager
                            .getDescription("breakfast_num_new", oriItem.getBreakfastNum()) + ")";
                    item.setBreakfast(breakfast);
                }

                orderFax.getOrderItemList().add(item);
            }
        }
        for (int i = order.getFaxList().size() - 1; 0 <= i; i--) {
            OrOrderFax orOrderFax = order.getFaxList().get(i);
            if (orOrderFax.isValidConfirm()) {
                String confirmNo = orOrderFax.getConfirmNo();
                // 酒店确认号
                orderFax.setConfirmNo(confirmNo);
                break;
            }
        }
        // TODO: 获取酒店所有房型信息
        // TODO: 一期是根据担保状态插入notes
        orderFax.setNote((String) modifiedInfo.get("tohotelNotes"));

        String space = "     ";
        String hotelRoomTypeStr = "□全部房型" + space;
        // hotelRoomTypeStr += ("□" + order.getRoomTypeName() + space);
        List lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotel.getID());
        for (int i = 0; i < lstRoomType.size(); i++) {
            HtlRoomtype roomType = (HtlRoomtype) lstRoomType.get(i);
            hotelRoomTypeStr += ("□" + roomType.getRoomName() + space);
        }
        orderFax.setHotelRoomType(hotelRoomTypeStr);

        return genOrderFaxXml(orderFax);
    }

    /**
     * 生成客人确认传真 mango用
     * 
     * @param order
     * @param hotel
     * @param itemList
     * @return
     */
    public String genOrderFaxByGuestMangoConfirm(OrOrder order, HtlHotel hotel, List itemList,
    		MemberDTO memberDTO, String sender) {
        OrderFax orderFax = new OrderFax();

        orderFax.setOrderCD(order.getOrderCDHotel());
        orderFax.setMemberName(memberDTO.getName());
        orderFax.setOrderMemberName(order.getLinkMan());
        orderFax.setMemberNameEnglish(memberDTO.getFirstname() + " " + memberDTO.getLastname());
        orderFax.setMemberNumber(memberDTO.getMembercd());

        String hotelName = null == hotel.getChnName() ? hotel.getEngName() : hotel.getChnName();
        orderFax.setHotelName(hotelName);
        int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        orderFax.setNightCount(nightCount);
        int nrc = nightCount * order.getRoomQuantity();
        orderFax.setNightRoomCount(nrc);
        orderFax.setHotelRoomType(order.getRoomTypeName() + "(" + order.getChildRoomTypeName()
            + ")");

        String nameInfo = "";
        List fellowList = order.getFellowList();
        for (int i = 0; i < fellowList.size(); i++) {
            OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
            nameInfo += fellow.getFellowName() + "("
                + fellowNation.get(fellow.getFellowNationality());
            if (fellow.isFellowSub()) {
                nameInfo += ",代订)      ";
            } else {
                nameInfo += ")      ";
            }
        }
        orderFax.setCustomerInfo(nameInfo);

        orderFax.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
        orderFax.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
        // 传真类型
        orderFax.setFaxType(sender);
        if (null != itemList && 0 < itemList.size()) {
            for (int i = 0; i < itemList.size(); i++) {
                OrderItemGroupBy oriItem = (OrderItemGroupBy) itemList.get(i);
                OrderItemFax item = new OrderItemFax();
                item.setItemCheckInDate(DateUtil.dateToString(oriItem.getNight()));
                double nSalePrice = oriItem.getSalepriceByAvg();
                oriItem.getBasepriceByAvg();
                if (!order.isPrepayOrder()) { // 面付单显示币种价格
                    item.setSalePrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                        + nSalePrice);
                    // item.setBasePrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency()) +
                    // nBasePrice);
                } else { // 预付单显示支付币种+价格 modify by chenjiajie 2009-11-27
                    //计算预付每天均价 返回 支付币种 + 金额
                    item.setSalePrice(getActualPaySalePrice(order, nSalePrice));
                }
                item.setItemCount(oriItem.getQuantityByNight());
                item
                    .setItemName(order.getRoomTypeName() + "("
                        + order.getChildRoomTypeName() + ")");
                item.setPayType(order.getPayMethod().equals(PayMethod.PAY) ? "面付" : "预付");
                if(order.getRmpOrder()){
                	item.setPayType("月结");	
                }  
                if (0 == oriItem.getBreakfast()) {
                    item.setBreakfast("无");
                } else {
                    String breakfast = resourceManager.getDescription("breakfast_type", oriItem
                        .getBreakfast())
                        + "("
                        + resourceManager
                            .getDescription("breakfast_num_new", oriItem.getBreakfastNum()) + ")";
                    item.setBreakfast(breakfast);
                }

                orderFax.getOrderItemList().add(item);
            }
        }
        //计算传真，邮件的总金额(包含立减优惠逻辑计算特意重构出来的方法) add by chenjiajie 2009-10-27 
        calOrderFaxTotalPrice(orderFax,order,nightCount);
        if(order.getRmpOrder()){
        	orderFax.setTotalPrice("RMB"+order.getSumRmb());
        }
        orderFax.setArrivalTimeStart(order.getArrivalTime());
        orderFax.setArrivalTimeEnd(order.getLatestArrivalTime());
        String arrivalTraffic = "";
        if (null != order.getArrivalTraffic()) {
            arrivalTraffic = resourceManager.getDescription("res_vehicleType", order
                .getArrivalTraffic());
            if (null != order.getFlight()) {
                arrivalTraffic += order.getFlight();
            }
        }

        orderFax.setArrivalTraffic(arrivalTraffic);
        orderFax.setSpecialRequestOverView(order.getSpecialRequest());
        orderFax.setNote(order.getRemark().getMemberRemark()); // TODO: notes
        orderFax.setHotelAddressTelephone(hotel.getChnAddress() + "Tel:" + hotel.getTelephone());

        for (int i = order.getFaxList().size() - 1; 0 <= i; i--) {
            OrOrderFax orOrderFax = order.getFaxList().get(i);
            if (orOrderFax.isValidConfirm()) {
                String confirmNo = orOrderFax.getConfirmNo();
                // 酒店确认号
                orderFax.setHotelConfirmNo(confirmNo);
                break;
            }
        }
        
        return genOrderFaxXml(orderFax);
    }

    /**
     * 计算传真，邮件的总金额(包含立减优惠逻辑计算特意重构出来的方法) add by chenjiajie 2009-10-27
     * @param orderFax
     * @param order
     */
    private void calOrderFaxTotalPrice(OrderFax orderFax,OrOrder order,int nightCount){
        
        // 预付单显示币种价格
        if (order.isPrepayOrder()) { 

            // RMB的立减总金额 
            double benefitAmount = order.getRmbFavourableAmount();
            // 实收金额
            double actualAmount = order.getSumRmb();
            
            /* 计算订单总金额 begin */            
            double totalPrice = order.getSum();
            
            // 香港组紧急需求，发给客人的币种以订单的实际支付币种绑定，默认人民币
            
            //香港组紧急需求 支持香港币种支付 用于订单总金额币种显示 
            String currencyStrForSum = "";
            //面付情况
            if(PayMethod.PAY.equals(order.getPayMethod())){
            	currencyStrForSum = CurrencyBean.idCurMap.get(order.getPaymentCurrency());
            }
            //预付情况
            else{
            	currencyStrForSum = CurrencyBean.idCurMap.get(order.getActualPayCurrency());
            }
            
            if(CurrencyBean.idCurMap.get(CurrencyBean.HKD).equals(currencyStrForSum)){
                currencyStrForSum = currencyStrForSum.replaceAll("\\$", "D ");
            }else if(!CurrencyBean.RMB.equals(order.getPaymentCurrency())){
                totalPrice = Math.ceil(totalPrice * order.getRateId());
            }
            
            //应付金额和优惠金额的币种
            String currencyStrForPaySum = CurrencyBean.idCurMap.get(order.getActualPayCurrency());
            if(CurrencyBean.HKD.equals(order.getActualPayCurrency())){
                currencyStrForPaySum = currencyStrForPaySum.replaceAll("\\$", "D ");
            }
            
            //为了避免计算不平衡，用总金额-实收金额=立减金额(带币种)
            benefitAmount = Math.floor(totalPrice - actualAmount);
            orderFax.setRoomPrice(currencyStrForSum + Math.ceil(totalPrice / (order.getRoomQuantity() * nightCount)));
            orderFax.setTotalPrice(currencyStrForSum + Math.ceil(totalPrice));
            /* 计算订单总金额 end */
            
            /** V2.9.3.1 立减优惠发送给客人传真，当订单式立减订单，需要填充两个字段 add by chenjiajie 2009-10-27 begin **/
            if(0 < order.getFavourableFlag() && 0 < order.getFavourableAmount()){
                
                orderFax.setBenefitAmount(currencyStrForPaySum + String.valueOf(benefitAmount));
                orderFax.setActualAmount(currencyStrForPaySum + String.valueOf(Math.ceil(actualAmount)));
            }else{
                orderFax.setBenefitAmount("");
                orderFax.setActualAmount("");
            }
            /** V2.9.3.1 立减优惠发送给客人传真，当订单式立减订单，需要填充两个字段 add by chenjiajie 2009-10-27 end **/
        } 
        // 面付单显示人民币价格
        else { 
            orderFax.setRoomPrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                + order.getSum() / (order.getRoomQuantity() * nightCount));
            orderFax.setTotalPrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                + order.getSum());
        }
    }
    
    /**
     * 生成客人取消传真 mango用
     * 
     * @param order
     * @param hotel
     * @param itemList
     * @return
     */
    public String genOrderFaxByGuestMangoCancel(OrOrder order, HtlHotel hotel, List itemList,
        MemberDTO member, String sender) {
        OrderFax orderFax = new OrderFax();

        orderFax.setOrderCD(order.getOrderCDHotel());
        orderFax.setMemberName(member.getName());
        orderFax.setOrderMemberName(order.getLinkMan());
        orderFax.setMemberNameEnglish(member.getFirstname() + " " + member.getLastname());
        orderFax.setMemberNumber(member.getMembercd());

        String hotelName = null == hotel.getChnName() ? hotel.getEngName() : hotel.getChnName();
        orderFax.setHotelName(hotelName);
        int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        orderFax.setNightCount(nightCount);
        int nrc = nightCount * order.getRoomQuantity();
        orderFax.setNightRoomCount(nrc);
        orderFax.setHotelRoomType(order.getRoomTypeName() + "(" + order.getChildRoomTypeName()
            + ")");

        String nameInfo = "";
        List fellowList = order.getFellowList();
        for (int i = 0; i < fellowList.size(); i++) {
            OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
            nameInfo += fellow.getFellowName() + "("
                + fellowNation.get(fellow.getFellowNationality());
            if (fellow.isFellowSub()) {
                nameInfo += ",代订)      ";
            } else {
                nameInfo += ")      ";
            }
        }
        orderFax.setCustomerInfo(nameInfo);

        orderFax.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
        orderFax.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
        // 传真类型
        orderFax.setFaxType(sender);
        /**
         * 备注 add by guojun 2008-12-24
         */
        orderFax.setNote(order.getRemark().getMemberRemark());
        if (null != itemList && 0 < itemList.size()) {
            for (int i = 0; i < itemList.size(); i++) {
                OrderItemGroupBy oriItem = (OrderItemGroupBy) itemList.get(i);
                OrderItemFax item = new OrderItemFax();
                item.setItemCheckInDate(DateUtil.dateToString(oriItem.getNight()));

                double nSalePrice = oriItem.getSalepriceByAvg();
                oriItem.getBasepriceByAvg();
                if (!order.isPrepayOrder()) { // 面付单显示币种价格
                    item.setSalePrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                        + nSalePrice);
                } else { // 预付单显示支付币种+价格 modify by chenjiajie 2009-11-27
                    //计算预付每天均价 返回 支付币种 + 金额
                    item.setSalePrice(getActualPaySalePrice(order, nSalePrice));
                }

                item.setItemCount(oriItem.getQuantityByNight());
                item
                    .setItemName(order.getRoomTypeName() + "(" 
                        + order.getChildRoomTypeName() + ")");
                item.setPayType(order.getPayMethod().equals(PayMethod.PAY) ? "面付" : "预付");
                if (0 == oriItem.getBreakfast()) {
                    item.setBreakfast("无");
                } else {
                    String breakfast = resourceManager.getDescription("breakfast_type", oriItem
                        .getBreakfast())
                        + "("
                        + resourceManager
                            .getDescription("breakfast_num_new", oriItem.getBreakfastNum()) + ")";
                    item.setBreakfast(breakfast);
                }

                orderFax.getOrderItemList().add(item);
            }
        }

        return genOrderFaxXml(orderFax);
    }

    /**
     * 生成客人确认传真 114用
     * 
     * @param order
     * @param hotel
     * @param itemList
     * @return
     */
    public String genOrderFaxByGuest114Confirm(OrOrder order, HtlHotel hotel, List itemList,
        MemberDTO memberDTO, String sender) {
        OrderFax orderFax = new OrderFax();

        orderFax.setOrderCD(order.getOrderCDHotel());
        orderFax.setMemberName(memberDTO.getName());
        orderFax.setOrderMemberName(order.getLinkMan());
        orderFax.setMemberNameEnglish(memberDTO.getFirstname() + " " + memberDTO.getLastname());
        orderFax.setMemberNumber(memberDTO.getMembercd());

        String hotelName = null == hotel.getChnName() ? hotel.getEngName() : hotel.getChnName();
        orderFax.setHotelName(hotelName);
        int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        orderFax.setNightCount(nightCount);
        int nrc = nightCount * order.getRoomQuantity();
        orderFax.setNightRoomCount(nrc);
        orderFax.setHotelRoomType(order.getRoomTypeName() + "(" + order.getChildRoomTypeName()
            + ")");

        String nameInfo = "";
        List fellowList = order.getFellowList();
        for (int i = 0; i < fellowList.size(); i++) {
            OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
            nameInfo += fellow.getFellowName() + "("
                + fellowNation.get(fellow.getFellowNationality());
            if (fellow.isFellowSub()) {
                nameInfo += ",代订)      ";
            } else {
                nameInfo += ")      ";
            }
        }
        orderFax.setCustomerInfo(nameInfo);

        orderFax.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
        orderFax.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
        // 传真类型
        orderFax.setFaxType(sender);
        if (null != itemList && 0 < itemList.size()) {
            for (int i = 0; i < itemList.size(); i++) {
                OrderItemGroupBy oriItem = (OrderItemGroupBy) itemList.get(i);
                OrderItemFax item = new OrderItemFax();
                item.setItemCheckInDate(DateUtil.dateToString(oriItem.getNight()));

                double nSalePrice = oriItem.getSalepriceByAvg();
                oriItem.getBasepriceByAvg();
                if (!order.isPrepayOrder()) { // 面付单显示币种价格
                    item.setSalePrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                        + nSalePrice);
                } else { // 预付单显示支付币种+价格 modify by chenjiajie 2009-11-27
                    //计算预付每天均价 返回 支付币种 + 金额
                    item.setSalePrice(getActualPaySalePrice(order, nSalePrice));
                }

                item.setItemCount(oriItem.getQuantityByNight());
                item
                    .setItemName(order.getRoomTypeName() + "(" 
                        + order.getChildRoomTypeName() + ")");
                item.setPayType(order.getPayMethod().equals(PayMethod.PAY) ? "面付" : "预付");
                if (0 == oriItem.getBreakfast()) {
                    item.setBreakfast("无");
                } else {
                    String breakfast = resourceManager.getDescription("breakfast_type", oriItem
                        .getBreakfast())
                        + "("
                        + resourceManager
                            .getDescription("breakfast_num_new", oriItem.getBreakfastNum()) + ")";
                    item.setBreakfast(breakfast);
                }
                orderFax.getOrderItemList().add(item);
            }
        }
        
        //计算传真，邮件的总金额(包含立减优惠逻辑计算特意重构出来的方法) add by chenjiajie 2009-10-27 
        calOrderFaxTotalPrice(orderFax,order,nightCount);
        if(order.getRmpOrder()){
        	orderFax.setTotalPrice("RMB"+order.getSumRmb());
        }
        orderFax.setArrivalTimeStart(order.getArrivalTime());
        orderFax.setArrivalTimeEnd(order.getLatestArrivalTime());
        String arrivalTraffic = "";
        if (null != order.getArrivalTraffic()) {
            arrivalTraffic = resourceManager.getDescription("res_vehicleType", order
                .getArrivalTraffic());
            if (null != order.getFlight()) {
                arrivalTraffic += order.getFlight();
            }
        }

        orderFax.setArrivalTraffic(arrivalTraffic);
        orderFax.setSpecialRequestOverView(order.getSpecialRequest());
        orderFax.setNote(order.getRemark().getMemberRemark());// TODO: notes

        orderFax.setHotelAddressTelephone(hotel.getChnAddress() + "Tel:" + hotel.getTelephone());
        for (int i = order.getFaxList().size() - 1; 0 <= i; i--) {
            OrOrderFax orOrderFax = order.getFaxList().get(i);
            if (orOrderFax.isValidConfirm()) {
                String confirmNo = orOrderFax.getConfirmNo();
                // 酒店确认号
                orderFax.setHotelConfirmNo(confirmNo);
                break;
            }
        }
        return genOrderFaxXml(orderFax);
    }

    /**
     * 生成客人取消传真 114用
     * 
     * @param order
     * @param hotel
     * @param itemList
     * @return
     */
    public String genOrderFaxByGuest114Cancel(OrOrder order, HtlHotel hotel, List itemList,
    		MemberDTO memberDTO, String sender) {
        OrderFax orderFax = new OrderFax();

        orderFax.setOrderCD(order.getOrderCDHotel());
        orderFax.setMemberName(memberDTO.getName());
        orderFax.setOrderMemberName(order.getLinkMan());
        orderFax.setMemberNameEnglish(memberDTO.getFirstname() + " " + memberDTO.getLastname());
        orderFax.setMemberNumber(memberDTO.getMembercd());

        String hotelName = null == hotel.getChnName() ? hotel.getEngName() : hotel.getChnName();
        orderFax.setHotelName(hotelName);
        int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        orderFax.setNightCount(nightCount);
        int nrc = nightCount * order.getRoomQuantity();
        orderFax.setNightRoomCount(nrc);
        orderFax.setHotelRoomType(order.getRoomTypeName() + "(" + order.getChildRoomTypeName()
            + ")");

        String nameInfo = "";
        List fellowList = order.getFellowList();
        for (int i = 0; i < fellowList.size(); i++) {
            OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
            nameInfo += fellow.getFellowName() + "("
                + fellowNation.get(fellow.getFellowNationality());
            if (fellow.isFellowSub()) {
                nameInfo += ",代订)      ";
            } else {
                nameInfo += ")      ";
            }
        }
        orderFax.setCustomerInfo(nameInfo);

        orderFax.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
        orderFax.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
        // 传真类型
        orderFax.setFaxType(sender);
        orderFax.setNote(order.getRemark().getMemberRemark());
        if (null != itemList && 0 < itemList.size()) {
            for (int i = 0; i < itemList.size(); i++) {
                OrderItemGroupBy oriItem = (OrderItemGroupBy) itemList.get(i);
                OrderItemFax item = new OrderItemFax();
                item.setItemCheckInDate(DateUtil.dateToString(oriItem.getNight()));

                double nSalePrice = oriItem.getSalepriceByAvg();
                oriItem.getBasepriceByAvg();
                if (!order.isPrepayOrder()) { // 面付单显示币种价格
                    item.setSalePrice(CurrencyBean.idCurMap.get(order.getPaymentCurrency())
                        + nSalePrice);
                } else { // 预付单显示支付币种+价格 modify by chenjiajie 2009-11-27
                    //计算预付每天均价 返回 支付币种 + 金额
                    item.setSalePrice(getActualPaySalePrice(order, nSalePrice));
                }

                item.setItemCount(oriItem.getQuantityByNight());
                item
                    .setItemName(order.getRoomTypeName() + "(" 
                        + order.getChildRoomTypeName() + ")");
                item.setPayType(order.getPayMethod().equals(PayMethod.PAY) ? "面付" : "预付");
                if (0 == oriItem.getBreakfast()) {
                    item.setBreakfast("无");
                } else {
                    String breakfast = resourceManager.getDescription("breakfast_type", oriItem
                        .getBreakfast())
                        + "("
                        + resourceManager
                            .getDescription("breakfast_num_new", oriItem.getBreakfastNum()) + ")";
                    item.setBreakfast(breakfast);
                }
                orderFax.getOrderItemList().add(item);
            }
        }

        return genOrderFaxXml(orderFax);
    }

    /**
     * 生成客人确认邮件的OrderFax对象 芒果网用
     * 
     * @param order
     * @param hotel
     * @param member
     * @return
     */
    public OrderFax genOrderFaxMailByGuestMangoConfirm(OrOrder order, HtlHotel hotel,
    		MemberDTO memberDTO, String sender) {
        OrderFax orderFax = new OrderFax();

        orderFax.setLinkman(order.getLinkMan());
        orderFax.setAliasName(memberDTO.getAliasname());
        orderFax.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
        orderFax.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
        String hotelName = null == hotel.getChnName() ? hotel.getEngName() : hotel.getChnName();
        orderFax.setHotelName(hotelName);
        int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        orderFax.setNightRoomCount(order.getRoomQuantity());
        orderFax.setNightCount(nightCount);
        orderFax.setHotelRoomType(order.getRoomTypeName() + "(" + order.getChildRoomTypeName()
            + ")");
        orderFax.setOrderCD(order.getOrderCDHotel());

        List<OrOrderItem> list = order.getOrderItems();
        if (0 < list.size()) {
            OrOrderItem oriItem = list.get(0);
            if (0 == oriItem.getBreakfast()) {
                orderFax.setIsContainMeal("无");
            } else {
                String breakfast = resourceManager.getDescription("breakfast_type", oriItem
                    .getBreakfast())
                    + "("
                    + resourceManager.getDescription("breakfast_num_new", oriItem.getBreakfastNum())
                    + ")";
                orderFax.setIsContainMeal(breakfast);
            }
        }
        
        //计算传真，邮件的总金额(包含立减优惠逻辑计算特意重构出来的方法) add by chenjiajie 2009-10-27 
        calOrderFaxTotalPrice(orderFax, order, nightCount);
        if(order.getRmpOrder()){
        	orderFax.setTotalPrice("RMB"+order.getSumRmb());
        }
        orderFax.setPayMethod(order.getPayMethod().equals("pay") ? "面付" : "预付");
        orderFax.setArrivalTimeStart(order.getArrivalTime() + "-" + order.getLatestArrivalTime());
        orderFax.setSpecialRequestOverView(order.getSpecialRequest());
        if (null != order.getRemark()) {
            orderFax.setNote(order.getRemark().getMemberRemark());
        }
        for (int i = order.getFaxList().size() - 1; 0 <= i; i--) {
            OrOrderFax orOrderFax = order.getFaxList().get(i);
            if (orOrderFax.isValidConfirm()) {
                String confirmNo = orOrderFax.getConfirmNo();
                // 酒店确认号
                orderFax.setHotelConfirmNo(confirmNo);
                break;
            }
        }
        orderFax.setHotelAddressTelephone(hotel.getChnAddress());
        orderFax.setHotelTelephone(hotel.getTelephone());
        orderFax.setHotelFax(hotel.getWorkingFax());
        orderFax.setFaxType(sender);
        String nameInfo = "";
        List fellowList = order.getFellowList();
        
        //去掉繁体网站的（内宾）字样add by diandian hou 2010-8-25
        if(OrderSource.FAN_TI_NET.equals(order.getSource())){
        	for (int i = 0; i < fellowList.size(); i++) {
                OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
                nameInfo += fellow.getFellowName();
            }
        }else{
        for (int i = 0; i < fellowList.size(); i++) {
            OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
            nameInfo += fellow.getFellowName() + "("
                + fellowNation.get(fellow.getFellowNationality()) + ")      ";
            }
        }
        orderFax.setCustomerInfo(nameInfo);

        return orderFax;
    }

    /**
     * 生成客人确认邮件 芒果网用
     * 
     * @param order
     * @param hotel
     * @param member
     * @return
     */
    public String genOrderMailByGuestMangoConfirm(OrOrder order, HtlHotel hotel,
    		MemberDTO memberDTO, String sender) {
        OrderFax orderFax = genOrderFaxMailByGuestMangoConfirm(order, hotel, memberDTO, sender);
        return genOrderFaxXml(orderFax);
    }

    /**
     * 生成客人取消邮件的OrderFax对象 芒果网用
     * 
     * @param order
     * @param hotel
     * @param member
     * @return
     */
    public OrderFax genOrderFaxMailByGuestMangoCancel(OrOrder order, HtlHotel hotel,
    		MemberDTO memberDTO, String sender) {
        OrderFax orderFax = new OrderFax();

        orderFax.setLinkman(order.getLinkMan());
        orderFax.setAliasName(memberDTO.getAliasname());
        orderFax.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
        orderFax.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
        String hotelName = null == hotel.getChnName() ? hotel.getEngName() : hotel.getChnName();
        orderFax.setHotelName(hotelName);
        int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        order.getRoomQuantity();
        orderFax.setNightRoomCount(order.getRoomQuantity());
        orderFax.setNightCount(nightCount);
        orderFax.setHotelRoomType(order.getRoomTypeName() + "(" + order.getChildRoomTypeName()
            + ")");
        orderFax.setOrderCD(order.getOrderCDHotel());

        //计算传真，邮件的总金额(包含立减优惠逻辑计算特意重构出来的方法) add by chenjiajie 2009-10-27 
        this.calOrderFaxTotalPrice(orderFax, order, nightCount);
        if(order.getRmpOrder()){
        	orderFax.setTotalPrice("RMB"+order.getSumRmb());
        }
        orderFax.setPayMethod(order.getPayMethod().equals("pay") ? "面付" : "预付");
        orderFax.setFaxType(sender);
        String nameInfo = "";
        List fellowList = order.getFellowList();
        for (int i = 0; i < fellowList.size(); i++) {
            OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
            nameInfo += fellow.getFellowName() + "("
                + fellowNation.get(fellow.getFellowNationality()) + ")      ";
        }
        orderFax.setCustomerInfo(nameInfo);

        return orderFax;
    }

    /**
     * 生成客人取消邮件 芒果网用
     * 
     * @param order
     * @param hotel
     * @param member
     * @return
     */
    public String genOrderMailByGuestMangoCancel(OrOrder order, HtlHotel hotel,
    		MemberDTO memberDTO, String sender) {
        OrderFax orderFax = genOrderFaxMailByGuestMangoCancel(order, hotel, memberDTO, sender);
        return genOrderFaxXml(orderFax);
    }

    /**
     * 生成客人确认邮件的OrderFax对象 114用
     * 
     * @param order
     * @param hotel
     * @param member
     * @return
     */
    public OrderFax genOrderFaxMailByGuest114Confirm(OrOrder order, HtlHotel hotel,
    		MemberDTO memberDTO, String sender) {
        OrderFax orderFax = new OrderFax();

        orderFax.setLinkman(order.getLinkMan());
        orderFax.setAliasName(memberDTO.getAliasname());
        orderFax.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
        orderFax.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
        String hotelName = null == hotel.getChnName() ? hotel.getEngName() : hotel.getChnName();
        orderFax.setHotelName(hotelName);
        int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        order.getRoomQuantity();
        orderFax.setNightRoomCount(order.getRoomQuantity());
        orderFax.setNightCount(nightCount);
        orderFax.setHotelRoomType(order.getRoomTypeName() + "(" + order.getChildRoomTypeName()
            + ")");
        orderFax.setOrderCD(order.getOrderCDHotel());

        List<OrOrderItem> list = order.getOrderItems();
        OrOrderItem oriItem = list.get(0);
        if (0 == oriItem.getBreakfast()) {
            orderFax.setIsContainMeal("无");
        } else {
            String breakfast = resourceManager.getDescription("breakfast_type", oriItem
                .getBreakfast())
                + "("
                + resourceManager.getDescription("breakfast_num_new", oriItem.getBreakfastNum())
                + ")";
            orderFax.setIsContainMeal(breakfast);
        } // TODO:

        //计算传真，邮件的总金额(包含立减优惠逻辑计算特意重构出来的方法) add by chenjiajie 2009-10-27 
        this.calOrderFaxTotalPrice(orderFax, order, nightCount);
        if(order.getRmpOrder()){
        	orderFax.setTotalPrice("RMB"+order.getSumRmb());
        }
        orderFax.setPayMethod(order.getPayMethod().equals("pay") ? "面付" : "预付");
        orderFax.setArrivalTimeStart(order.getArrivalTime() + "-" + order.getLatestArrivalTime());
        orderFax.setSpecialRequestOverView(order.getSpecialRequest());
        orderFax.setNote(order.getRemark().getMemberRemark());

        for (int i = order.getFaxList().size() - 1; 0 <= i; i--) {
            OrOrderFax orOrderFax = order.getFaxList().get(i);
            if (orOrderFax.isValidConfirm()) {
                String confirmNo = orOrderFax.getConfirmNo();
                // 酒店确认号
                orderFax.setHotelConfirmNo(confirmNo);
                break;
            }
        }

        orderFax.setHotelAddressTelephone(hotel.getChnAddress());
        orderFax.setHotelTelephone(hotel.getTelephone());
        orderFax.setHotelFax(hotel.getWorkingFax());
        orderFax.setFaxType(sender);
        String nameInfo = "";
        List fellowList = order.getFellowList();
        for (int i = 0; i < fellowList.size(); i++) {
            OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
            nameInfo += fellow.getFellowName() + "("
                + fellowNation.get(fellow.getFellowNationality()) + ")      ";
        }
        orderFax.setCustomerInfo(nameInfo);

        return orderFax;
    }

    /**
     * 生成客人确认邮件 114用
     * 
     * @param order
     * @param hotel
     * @param member
     * @return
     */
    public String genOrderMailByGuest114Confirm(OrOrder order, HtlHotel hotel,
    		MemberDTO memberDTO, String sender) {
        OrderFax orderFax = genOrderFaxMailByGuest114Confirm(order, hotel, memberDTO, sender);
        return genOrderFaxXml(orderFax);
    }

    /**
     * 生成客人取消邮件的OrderFax对象 114用
     * 
     * @param order
     * @param hotel
     * @param member
     * @return
     */
    public OrderFax genOrderFaxMailByGuest114Cancel(OrOrder order, HtlHotel hotel,
    		MemberDTO memberDTO, String sender) {
        OrderFax orderFax = new OrderFax();

        orderFax.setLinkman(order.getLinkMan());
        orderFax.setAliasName(memberDTO.getAliasname());
        orderFax.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
        orderFax.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
        String hotelName = null == hotel.getChnName() ? hotel.getEngName() : hotel.getChnName();
        orderFax.setHotelName(hotelName);
        int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        order.getRoomQuantity();
        orderFax.setNightRoomCount(order.getRoomQuantity());
        orderFax.setNightCount(nightCount);
        orderFax.setHotelRoomType(order.getRoomTypeName() + "(" + order.getChildRoomTypeName()
            + ")");
        orderFax.setOrderCD(order.getOrderCDHotel());

        //计算传真，邮件的总金额(包含立减优惠逻辑计算特意重构出来的方法) add by chenjiajie 2009-10-27 
        this.calOrderFaxTotalPrice(orderFax, order, nightCount);
        if(order.getRmpOrder()){
        	orderFax.setTotalPrice("RMB"+order.getSumRmb());
        }
        orderFax.setPayMethod(order.getPayMethod().equals("pay") ? "面付" : "预付");
        orderFax.setNote(order.getRemark().getMemberRemark());
        orderFax.setFaxType(sender);
        String nameInfo = "";
        List fellowList = order.getFellowList();
        for (int i = 0; i < fellowList.size(); i++) {
            OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
            nameInfo += fellow.getFellowName() + "("
                + fellowNation.get(fellow.getFellowNationality()) + ")      ";
        }
        orderFax.setCustomerInfo(nameInfo);

        return orderFax;
    }

    /**
     * 生成客人取消邮件 114用
     * 
     * @param order
     * @param hotel
     * @param member
     * @return
     */
    public String genOrderMailByGuest114Cancel(OrOrder order, HtlHotel hotel, MemberDTO memberDTO,
        String sender) {
        OrderFax orderFax = genOrderFaxMailByGuest114Cancel(order, hotel, memberDTO, sender);
        return genOrderFaxXml(orderFax);
    }

    /**
     * 网站集体订房给客人发Email确认
     * 
     * @param order
     * @param hotel
     * @param member
     * @return
     */

    public String genGroupOrderMailXmlForHotelWeb(Object beanObj) {

        org.dom4j.Document document = DocumentHelper.createDocument();
        org.dom4j.Element rootElementE = document.addElement("hotelOrder");

        BeanUtil.SetNullDefault(beanObj);

        PropertyUtilsBean bean = new PropertyUtilsBean();
        PropertyDescriptor[] origDescriptors = bean.getPropertyDescriptors(beanObj.getClass());

        try {
            for (int j = 0; j < origDescriptors.length; j++) {
                String name = origDescriptors[j].getName();
                if ("class".equals(name)) {
                    continue;
                }
                if (bean.isReadable(beanObj, name)) {

                    org.dom4j.Element orderCDE = rootElementE.addElement(name);
                    Object value = bean.getProperty(beanObj, name);
                    orderCDE.setText(value.toString());

                }
            }
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }

        String strXML = document.asXML();
        log.debug("生成的XML数据模型的字符串======" + strXML);
        return strXML;

    }

    /**
     * 返回预览邮件的String
     * 
     * @param orderFax
     * @param fileName
     * @return
     * @throws IOException 
     */
    public String getHolteOrderMailHtml(OrderFax orderFax, String fileName) throws IOException {
        // 读取模板
        String template = loadTemplate(fileName);
        PropertyUtilsBean bean = new PropertyUtilsBean();
        PropertyDescriptor[] origDescriptors = bean.getPropertyDescriptors(orderFax.getClass());
        try {
            for (int j = 0; j < origDescriptors.length; j++) {
                String name = origDescriptors[j].getName();
                if ("class".equals(name)) {
                    continue;
                }
                if (bean.isReadable(orderFax, name) && mailMap.containsKey(name)) {
                    Object value = bean.getProperty(orderFax, name);

                    if (null != value) {
                        String strTemp = value.toString();
                        if (0 <= strTemp.indexOf("$")) {
                            template = template.replace("[" + name + "]", strTemp);
                        }else if(StringUtil.isValidStr(strTemp) && "benefitAmount".equals(name)){
                    		String templateStr = BENEFIT_AMOUNT_TEMPLATE_STR.replaceAll("\\[" + name + "]", null == value ? ""
                                    : value.toString());
                    		template = template.replaceAll("\\[" + name + "]", templateStr);
                        }else if(StringUtil.isValidStr(strTemp) && "actualAmount".equals(name)){
                    		String templateStr = ACTUAL_AMOUNT_TEMPLATE_STR.replaceAll("\\[" + name + "]", null == value ? ""
                                    : value.toString());
                    		template = template.replaceAll("\\[" + name + "]", templateStr);
                        }else {
                            template = template.replaceAll("\\[" + name + "]", null == value ? ""
                                : value.toString());
                        }
                    } else {
                        template = template.replaceAll("\\[" + name + "]", null == value ? ""
                            : value.toString());
                    }

                }
            }
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }

        return template;
    }

    /**
     * 读取模板
     * 
     * @param filename
     *            模板文件名
     * @return
     * @throws IOException 
     */
    private String loadTemplate(String filename) throws IOException {
        String templateStr = "";
        BufferedReader reader = null;
        // 读取邮件模板
        try {
            File template = new File(filename);
             reader = new BufferedReader(new InputStreamReader(new FileInputStream(
                template), "gb2312"));
            String line = reader.readLine();
            while (null != line) {
                templateStr += line + "\n";
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            reader.close();
        } finally{
            reader.close();
        }
        return templateStr;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    /**
     * 酒店直联增加特殊要求的的传真 HOTEL 2.5 方法名称： 将客人的特殊要求传给酒店
     * 
     */
    public String genSpecitalRequestFaxHotel(OrOrder order, HtlHotel hotel, String isAnewSend,
        Long ID, List itemList, Map specailRequest) {
        String strXML = "";
        org.dom4j.Document document = DocumentHelper.createDocument();
        org.dom4j.Element rootElementE = document.addElement("ChannelOrderSpecialRequest");
        org.dom4j.Element barCode = rootElementE.addElement("barCode");
        org.dom4j.Element faxDate = rootElementE.addElement("faxDate");
        org.dom4j.Element faxTime = rootElementE.addElement("faxTime");
        org.dom4j.Element orderCd = rootElementE.addElement("orderCd");
        org.dom4j.Element crsConfirmID = rootElementE.addElement("crsConfirmID");
        org.dom4j.Element crsHotelName = rootElementE.addElement("crsHotelName");
        org.dom4j.Element crsHotelFaxNo = rootElementE.addElement("crsHotelFaxNo");
        org.dom4j.Element memberName = rootElementE.addElement("memberName");
        org.dom4j.Element checkInDate = rootElementE.addElement("checkInDate");
        org.dom4j.Element checkOutDate = rootElementE.addElement("checkOutDate");
        org.dom4j.Element sumNight = rootElementE.addElement("sumNight");
        org.dom4j.Element roomType = rootElementE.addElement("roomType");
        org.dom4j.Element roomCount = rootElementE.addElement("roomCount");
        org.dom4j.Element bedName = rootElementE.addElement("bedName");
        org.dom4j.Element sumRMB = rootElementE.addElement("sumRMB");
        org.dom4j.Element arrivalTime = rootElementE.addElement("arrivalTime");
        org.dom4j.Element arrivalTraffic = rootElementE.addElement("arrivalTraffic");
        org.dom4j.Element specialRequest = rootElementE.addElement("specialRequest");

        barCode.setText("HA" + ID);
        Date nowTime = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss",Locale.CHINA);
        faxDate.setText(df.format(nowTime));
        faxTime.setText(dt.format(nowTime));
        orderCd.setText(order.getOrderCD());
        crsConfirmID.setText(order.getOrderCdForChannel());
        crsHotelName.setText(order.getHotelName());
        crsHotelFaxNo.setText(String.valueOf(specailRequest.get("faxNum")));
        List<OrFellowInfo> followList = order.getFellowList();
        String followInfo = "";
        for (OrFellowInfo orFellowInfo : followList) {
            followInfo += orFellowInfo.getFellowName();
            if (null != orFellowInfo.getFellowNationality()) {
                if ("1".equals(orFellowInfo.getFellowNationality())) {
                    followInfo += "(内宾) ";
                } else if ("2".equals(orFellowInfo.getFellowNationality())) {
                    followInfo += "(外宾) ";
                } else if ("3".equals(orFellowInfo.getFellowNationality())) {
                    followInfo += "(港澳) ";
                }
            }
        }

        memberName.setText(followInfo);
        checkInDate.setText(DateUtil.dateToString(order.getCheckinDate()));
        checkOutDate.setText(DateUtil.dateToString(order.getCheckoutDate()));
        sumNight.setText(String.valueOf(DateUtil.getDay(order.getCheckinDate(), order
            .getCheckoutDate())));
        roomType.setText(order.getRoomTypeName());
        roomCount.setText(String.valueOf(order.getRoomQuantity()));

        String bed = "";
        if (1 == order.getBedType()) {
            bed = "大床";
        } else if (2 == order.getBedType()) {
            bed = "双床";
        } else if (3 == order.getBedType()) {
            bed = "单床";
        } else if (0 == order.getBedType()) {
            bed = "无需求";
        }
        bedName.setText(bed);
        sumRMB.setText(String.valueOf(order.getSumRmb()));
        arrivalTime.setText(order.getArrivalTime() + "--" + order.getLatestArrivalTime());

        if (null == order.getArrivalTraffic()) {
            arrivalTraffic.setText("");
        } else {
            String strArrivalTraffic = "";
            strArrivalTraffic = resourceManager.getDescription("res_vehicleType", order
                .getArrivalTraffic());
            if (null != order.getFlight()) {
                strArrivalTraffic += order.getFlight();
            }
            arrivalTraffic.setText(strArrivalTraffic);
        }
        if (null == order.getSpecialRequest()) {
            specialRequest.setText("");
        } else {
            specialRequest.setText(order.getSpecialRequest());
        }

        strXML += document.asXML();
        log.info("---特殊要求--: " + strXML);
        return strXML;
    }

    /**
     * 酒店直联增加特殊要求的的传真 HOTEL 2.8.1 方法名称： 将客人的特殊要求传给酒店
     */
    public String genMailContractor(OrOrder order) {
        String strXML = "";
        String contractorURL = "http://10.10.5.166/HOP/order/orderOperate!edit.action?orderId=";
        org.dom4j.Document document = DocumentHelper.createDocument();
        org.dom4j.Element rootElementE = document.addElement("DifficultOrder");
        org.dom4j.Element orderUrl = rootElementE.addElement("orderUrl");
        orderUrl.setText(contractorURL + order.getID());
        strXML += document.asXML();
        return strXML;
    }
    
    /**
     * 计算预付每天均价
     * @param order
     * @param salePrice
     * @return 支付币种 + 金额
     */
    private String getActualPaySalePrice(OrOrder order,double salePrice){
        String resultStr = "";
        String idCurrency = CurrencyBean.idCurMap.get(order.getActualPayCurrency());
        //支付币种是人民币
        if(CurrencyBean.RMB.equals(order.getActualPayCurrency())){
            //订单原币种是人民币 取原币种
            if(CurrencyBean.RMB.equals(order.getPaymentCurrency())){
                resultStr = idCurrency 
                    + BigDecimal.valueOf(salePrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            //订单原币种不是人民币
            else{
                resultStr = idCurrency + Math.ceil(salePrice * order.getRateId());
            }
        }
        //支付币种不是人民币
        else{
            //订单原币种是人民币 价格需要除以港币的汇率 逢一进十
            if(CurrencyBean.RMB.equals(order.getPaymentCurrency())){
                resultStr = idCurrency 
                    + Math.ceil(salePrice / MoneyHandle.getCurrenyRate(order.getActualPayCurrency()));
            }
            //订单原币种不是人民币 取原币种
            else{
                resultStr = idCurrency
                    + BigDecimal.valueOf(salePrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();;
            }
        }
        return resultStr;
    }

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}	
}
