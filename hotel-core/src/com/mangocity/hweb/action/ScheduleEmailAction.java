package com.mangocity.hweb.action;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.dom4j.DocumentHelper;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hotel.order.web.OrderAction;
import com.mangocity.hweb.ScheduleFax;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.BeanUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Mail;

/**
 * 发送预订行程单的Action
 * 
 * @author ChenJiaJie
 * 
 */
public class ScheduleEmailAction extends OrderAction {

    private static final long serialVersionUID = 840168280728204127L;

    public static Map fellowNation;

    /**
     * message接口
     */
    private CommunicaterService communicaterService;
    
    private IHotelService hotelService;

    /**
     * 传真邮件辅助类
     */
    private MsgAssist msgAssist;

    private String sendStatus = "";

    /** 参数 Begin * */
    private String toaddress; // 目的email地址

    private String ulmPoint; // 消费积分

    private String telephone; // 电话

    private String bookhintSpanValue; // 预订提示

    private String cancelModifyItem; // 取消提示

    private String priceParams; // 需要解析的价格组合字符串

    private String totalPrice; // 费用总计

    private String acturalAmount; // 实收金额
    
    private String ulmCoupon; // 代金券使用金额
    
    private String cancelModifyStr;
    
    /**
     * 给网站改版用的
     */
    private String forward;

    /** 参数 End * */

    public void init() {
        fellowNation = new HashMap();
        fellowNation.put("1", "内宾");
        fellowNation.put("2", "外宾");
        fellowNation.put("3", "港澳台");
    }

    /**
     * 处理给客人发送邮件
     * 
     * @return
     */
    public String sendEmailToGuest() {

        log.info("begin sendEmailToGuest");
        init();
        String[] webParams = new String[8];
        // 消费积分
        webParams[0] = ulmPoint;
        webParams[1] = telephone;
        webParams[2] = bookhintSpanValue;
        webParams[3] = cancelModifyItem;
        webParams[4] = acturalAmount;
        webParams[5] = totalPrice;
        //hotel2.9.3 增加代金券支付方式 add by chenjiajie
        webParams[6] = ulmCoupon;
        webParams[7] = cancelModifyStr;
        // 后面需要解析的价格组合字符串
        // String priceParams = (String) params.get("priceParams");

        String sender = "cs@mangocity.com";
        String subject = "芒果网酒店预订行程单";
        String templateNo = "";
        String xmlString = "";

        // 获取订单
        order = getOrder(orderId);

        HtlHotel hotel = hotelService.findHotel(order.getHotelId().longValue());
        templateNo = FaxEmailModel.NOTIRY_HOTEL_BOOKING_SCHEDULE;
        // 生成XML
        xmlString = genOrderMailByBookingSchedule(order, hotel, genPriceList(priceParams), sender,
            webParams);

        Mail mail = new Mail();
        mail.setApplicationName("hotel");
        mail.setTo(new String[] { toaddress });
        mail.setFrom(sender);
        mail.setSubject(subject);
        mail.setTemplateFileName(templateNo);
        mail.setXml(xmlString);
        Long ret = null;
        try {
            ret = communicaterService.sendEmail(mail);
        } catch (RuntimeException e) {
            log.info("调用发送系统出错!" + e.getMessage());
            setErrorMessage("邮件发送失败！");
            return "result";
        }

        if (null == ret || 0 >= ret) {
            sendStatus = "2";// 发送失败
        } else {
            sendStatus = "1";
        }

        log.info("end sendEmailToGuest");

        if ("2".equals(sendStatus)) {
            setErrorMessage("邮件发送失败！");
        } else {
            setErrorMessage("邮件发送成功！");
        }

        return "result";
    }

    /**
     * 解析价格字符串，生成一个List
     * 
     * @param priceListStr
     * @return
     */
    public List<String> genPriceList(final String priceListStr) {
        List<String> priceList = new ArrayList<String>();
        String[] tempStr = priceListStr.split("\\@|\\*|\\|");
        // todo解析
        if (0 < tempStr.length) {
            // 价格单位
            String unit = "￥";
            if (CurrencyBean.USD.equals(tempStr[0])) {
                unit = "US$";
            } else if (CurrencyBean.MOP.equals(tempStr[0])) {
                unit = "MOP";
            } else if (CurrencyBean.EUR.equals(tempStr[0])) {
                unit = "EUR€";
            } else if (CurrencyBean.GBP.equals(tempStr[0])) {
                unit = "GBP$";
            } else if (CurrencyBean.JPY.equals(tempStr[0])) {
                unit = "JPY￥";
            } else if (CurrencyBean.DEM.equals(tempStr[0])) {
                unit = "DM";
            } else if (CurrencyBean.HKD.equals(tempStr[0])) {
                unit = "HK$";
            } else {
                unit = "￥";
            }
            StringBuffer priceItemStr = null;
            for (int i = 1; i <= tempStr.length - 2; i += 2) {
                priceItemStr = new StringBuffer();
                // 组合字符串，格式："02/01-02/04 ￥780/晚"
                priceItemStr.append(tempStr[i]).append(" " + unit).append(tempStr[i + 1]).append(
                    "/晚");
                priceList.add(priceItemStr.toString());
            }
            return priceList;
        }
        return priceList;

    }

    /**
     * 生成酒店预订行程单确认邮件 芒果网用
     * 
     * @param order
     * @param hotel
     * @param priceList
     *            -价格列表
     * @param sender
     * @param params
     *            -参数列表
     * @return
     */
    public String genOrderMailByBookingSchedule(OrOrder order, HtlHotel hotel,
        List<String> priceList, String sender, String[] params) {
        ScheduleFax scheduleFax = new ScheduleFax();

        String hotelName = null == hotel.getChnName() ? hotel.getEngName() : hotel.getChnName();
        scheduleFax.setHotelName(hotelName);
        scheduleFax.setCheckInDate(DateUtil.dateToString(order.getCheckinDate()));
        scheduleFax.setCheckOutDate(DateUtil.dateToString(order.getCheckoutDate()));
        int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        scheduleFax.setNightCount(nightCount);
        scheduleFax.setRoomQuantity(order.getRoomQuantity());
        scheduleFax.setRoomTypeName(order.getRoomTypeName());
        scheduleFax.setChildRoomTypeName(order.getChildRoomTypeName());

        // 入住人姓名（可能多个）
        String customerNames = order.getFellowNames();
        customerNames = customerNames.substring(0, customerNames.lastIndexOf(" "));
        scheduleFax.setCustomerName(customerNames.replace(' ', ','));

        scheduleFax.setPriceList(priceList);

        scheduleFax.setPayMethod(order.getPayMethod().equals("pay") ? "面付" : "预付");
        scheduleFax.setTotalPrice(params[5]); // 费用总计
        // 积分消费pointExpended
        scheduleFax.setPointExpended((null == params[0] || "".equals(params[0])) ? "0" : params[0]);
        scheduleFax.setAmountReceivable("￥ " + params[4]);
        scheduleFax.setSpecialRequestOverView(order.getSpecialRequest());
        // // 已不适用，但预留：免费修改/取消订单时间orderUpdateDate
        // scheduleFax.setOrderUpdateDate("");
        scheduleFax.setLinkmanName(order.getLinkMan());
        scheduleFax.setLinkmanEmail(order.getEmail());
        scheduleFax.setLinkmanTelephone(params[1]);
        scheduleFax.setBookhint(params[2]);
        if(params[7] != null){
            if(params[3] != null){
                params[3] = params[3]+params[7];
            }else{
                params[3] = params[7];
            }
        }
        scheduleFax.setCancelModify(params[3]);
        /** hotel2.9.3 增加代金券消费voucherExpended add by chenjiajie 2009-09-07 **/
        scheduleFax.setVoucherExpended((null == params[6] || "".equals(params[6])) ? "0" : params[6]);
        // 确认方式confirmType
        int confirmType = order.getConfirmType();
        String confirmTypeStr = "";
        switch (confirmType) {
        case ConfirmType.EMAIL:
            confirmTypeStr = "电子邮件确认";
            break;
        case ConfirmType.FAX:
            confirmTypeStr = "传真确认";
            break;
        case ConfirmType.PHONE:
            confirmTypeStr = "电话确认";
            break;
        case ConfirmType.SMS:
            confirmTypeStr = "手机短信息确认";
            break;
        case ConfirmType.NO:
            confirmTypeStr = "不确认";
            break;
        default:
            confirmTypeStr = "";
            break;
        }
        scheduleFax.setConfirmType(confirmTypeStr);

        String hotelAddres = null == hotel.getChnAddress() ? hotel.getEngAddress() : hotel
            .getChnAddress();
        scheduleFax.setHotelAddress(hotelAddres);
        scheduleFax.setHotelTelephone(hotel.getTelephone());
        scheduleFax.setFaxType(sender);
        return genScheduleFaxXml(scheduleFax);
    }

    /**
     * 根据传过来的ScheduleFax对象,组建一个org.dom4j.Document对象,提供给邮件和传真使用的XML数据模型
     * 
     * @param scheduleFax
     *            --酒店预订行程邮件、传真单实体
     * @return
     */
    public String genScheduleFaxXml(ScheduleFax scheduleFax) {

        org.dom4j.Document document = DocumentHelper.createDocument();
        org.dom4j.Element rootElementE = document.addElement("hotelOrder");

        BeanUtil.SetNullDefault(scheduleFax);

        PropertyUtilsBean bean = new PropertyUtilsBean();
        PropertyDescriptor[] origDescriptors = bean.getPropertyDescriptors(scheduleFax.getClass());

        try {
            for (int j = 0; j < origDescriptors.length; j++) {
                String name = origDescriptors[j].getName();
                if ("class".equals(name)) {
                    continue;
                }
                if (bean.isReadable(scheduleFax, name)) {
                    if ("priceList".equals(name)) {
                        // 多个价格
                        List<String> priceList = scheduleFax.getPriceList();
                        if (null != priceList) {
                            for (int itemNum = 0; itemNum < priceList.size(); itemNum++) {
                                String price = priceList.get(itemNum);
                                org.dom4j.Element priceE = rootElementE.addElement("price");
                                priceE.setText(price);
                            }
                        }
                    } else {
                        org.dom4j.Element orderCDE = rootElementE.addElement(name);
                        Object value = bean.getProperty(scheduleFax, name);
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
     * 封装参数,跳转到sendMail.jsp
     * 
     * @return
     */
    public String fowardToSendMailPage() {

        return "sendMail";
    }
    
    /**
     * 网站改版用
     */
    public String execute(){
    	/* 处理发邮件给客人 */
    	if(StringUtil.isValidStr(forward)
    			&& "result".equals(forward)){


            log.info("begin sendEmailToGuest");
            init();
            String[] webParams = new String[8];
            // 消费积分
            webParams[0] = ulmPoint;
            webParams[1] = telephone;
            webParams[2] = bookhintSpanValue;
            webParams[3] = cancelModifyItem;
            webParams[4] = acturalAmount;
            webParams[5] = totalPrice;
            //hotel2.9.3 增加代金券支付方式 add by chenjiajie
            webParams[6] = ulmCoupon;
            webParams[7] = cancelModifyStr;
            // 后面需要解析的价格组合字符串
            // String priceParams = (String) params.get("priceParams");

            String sender = "cs@mangocity.com";
            String subject = "芒果网酒店预订行程单";
            String templateNo = "";
            String xmlString = "";

            // 获取订单
            order = getOrder(orderId);

            HtlHotel hotel = hotelService.findHotel(order.getHotelId().longValue());
            templateNo = FaxEmailModel.NOTIRY_HOTEL_BOOKING_SCHEDULE;
            // 生成XML
            xmlString = genOrderMailByBookingSchedule(order, hotel, calAveragePrice(order), sender,
                webParams);

            Mail mail = new Mail();
            mail.setApplicationName("hotel");
            mail.setTo(new String[] { toaddress });
            mail.setFrom(sender);
            mail.setSubject(subject);
            mail.setTemplateFileName(templateNo);
            mail.setXml(xmlString);
            Long ret = null;
            try {
                ret = communicaterService.sendEmail(mail);
            } catch (RuntimeException e) {
                log.info("调用发送系统出错!" + e.getMessage());
                setErrorMessage("邮件发送失败！");
                forward = "result";
            }

            if (null == ret || 0 >= ret) {
                sendStatus = "2";// 发送失败
            } else {
                sendStatus = "1";
            }

            log.info("end sendEmailToGuest");

            if ("2".equals(sendStatus)) {
                setErrorMessage("邮件发送失败！");
            } else {
                setErrorMessage("邮件发送成功！");
            }

            forward = "result";
    	}else{
    		forward = "sendMail";
    	}
    	return forward;
    }
    
    /**
     * 计算均价 如：￥300元/晚
     * @param order
     * @return
     */
    private List<String> calAveragePrice(OrOrder order){
    	List<String> returnList = new ArrayList<String>(1);
    	String averagePricStr = "";
    	int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
    	if (order.isPrepayOrder()) { // 预付单显示币种价格
        	//发送给客人传真如果是非人民币币种的预付订单需要逢一进十 modify by chenjiajie 2009-10-27
        	double totalPrice = order.getSumRmb();
        	if(!CurrencyBean.RMB.equals(order.getPaymentCurrency())){
        		totalPrice = Math.ceil(totalPrice);
        	}
        	averagePricStr = CurrencyBean.idCurMap.get(CurrencyBean.RMB) + totalPrice / (order.getRoomQuantity() * nightCount);
        } else { // 面付单显示人民币价格
        	averagePricStr = CurrencyBean.idCurMap.get(order.getPaymentCurrency()) + order.getSum() / (order.getRoomQuantity() * nightCount);
        }
    	averagePricStr += "元/晚";
    	returnList.add(averagePricStr);
    	return returnList;
    }

    /** getter and setter begin */

    public CommunicaterService getCommunicaterService() {
        return communicaterService;
    }

    public void setCommunicaterService(CommunicaterService communicaterService) {
        this.communicaterService = communicaterService;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public MsgAssist getMsgAssist() {
        return msgAssist;
    }

    public void setMsgAssist(MsgAssist msgAssist) {
        this.msgAssist = msgAssist;
    }

    public String getToaddress() {
        return toaddress;
    }

    public void setToaddress(String toaddress) {
        this.toaddress = toaddress;
    }

    public String getUlmPoint() {
        return ulmPoint;
    }

    public void setUlmPoint(String ulmPoint) {
        this.ulmPoint = ulmPoint;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getBookhintSpanValue() {
        return bookhintSpanValue;
    }

    public void setBookhintSpanValue(String bookhintSpanValue) {
        this.bookhintSpanValue = bookhintSpanValue;
    }

    public String getCancelModifyItem() {
        return cancelModifyItem;
    }

    public void setCancelModifyItem(String cancelModifyItem) {
        this.cancelModifyItem = cancelModifyItem;
    }

    public String getPriceParams() {
        return priceParams;
    }

    public void setPriceParams(String priceParams) {
        this.priceParams = priceParams;
    }

    /** getter and setter end */

    public String getActuralAmount() {
        return acturalAmount;
    }

    public void setActuralAmount(String acturalAmount) {
        this.acturalAmount = acturalAmount;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

	public String getUlmCoupon() {
		return ulmCoupon;
	}

	public void setUlmCoupon(String ulmCoupon) {
		this.ulmCoupon = ulmCoupon;
	}

    public String getCancelModifyStr() {
        return cancelModifyStr;
    }

    public void setCancelModifyStr(String cancelModifyStr) {
        this.cancelModifyStr = cancelModifyStr;
    }

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}
	
}
