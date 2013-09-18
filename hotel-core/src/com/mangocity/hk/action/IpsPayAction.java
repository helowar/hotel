/**
 * 
 */
package com.mangocity.hk.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mangocity.hk.constant.HkConstant;
import com.mangocity.hk.service.PaymentOnlineService;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.web.PopulateOrderAction;
import com.mangocity.hotelweb.persistence.HotelOrderFromBean;
import com.mangocity.hweb.action.FormartHwebUtil;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelInfoForWeb;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.pay.CardTypeEnum;
import com.mangocity.pay.ServerReturnTypeEnum;
import com.mangocity.pay.util.MangocityEncodeUtils;
import com.mangocity.util.DateUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * @author wuyun 2009-3-16
 * 
 */
public class IpsPayAction extends PopulateOrderAction {

    /**
     * 订单号
     */
    private String orderCD;

    /**
     * 在线支付类型
     */
    private int onlinePaytype;

    /**
     * 订单处理类
     */
//    private IOrderService orderService;

    private String failureUrl;

    private String successUrl;

    /**
     * 支付系统处理类路径
     */
    private String payAddressUrl;

    /**
     * 实际应该支付金额
     */
    private java.math.BigDecimal price;

    /**
     * 区分是IPS国际还是IPS国外
     */
    private String cardType;

    /**
     * 支付日期
     */
    private String payDate;

    /**
     * 标志订单来源，酒店系统为“HOTEL”
     */
    private String orderType;

    private String transid;

    /**
     * 在线支付服务类
     */
    private PaymentOnlineService paymentService;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 支付金额，保留两位小数 20.56
     */
    private String transactAmount;

    /**
     * 支付日期
     */
    private String transactDate;

    /**
     * 业务系统保留数据
     */
    private String attachment;

    /**
     * 支付订单号或支付流水号， 由支付网关或银行返回的支付订单号或流水号
     */
    private String payOrderCode;

    /**
     * 银行返回时间或支付系统返回时间 格式：yyyyMMddHHmmss
     */
    private String payTime;

    /**
     * 支付状态 Y:支付成功；N：支付失败
     */
    private String payState;

    /**
     * 数字签名 通过调用MangocityEncodeUtils的verifyReturnMD5Digest方法传入指定参数来验证数据的正确性
     */
    private String signature;

    /**
     * 返回类型 浏览器：BROWSER；服务器：SERVER
     */
    private String returnType;

    /**
     * 服务器端返回类型 芒果网支付系统提供了Browser返回方式和服务器端返回方式，这里是设置服务器端返回方式的类型。 ServerReturnTypeEnum
     * .NONE：不使用；ServerReturnTypeEnum .URL：URL方式。
     */
    private String serverReturnType;

    /**
     * 会员标志
     */
    private int memClass;

    private HotelManageWeb hotelManageWeb;

    /**
     * 订单基本参数
     */
    private HotelOrderFromBean hotelOrderFromBean;

    /**
     * 订单核对页面显示价格信息
     */
    private List priceTemplist = new ArrayList();

    /**
     * 汇率
     */
    private double rate;

    /**
     * 新注册会员号
     */
    private String newMemberCd;

    /**
     * 酒店信息
     */
    private HotelInfoForWeb hotelInfoForWeb = new HotelInfoForWeb();

    /**
     * 标志，用于控制页面提示文字 0:默认；1：支付失败；2：支付成功，确认成功；3：支付成功，确认失败
     */
    private int hintSigner;

    public String prePay() {
        // 如果订单号为空
        if (null == orderCD || "".equals(orderCD)) {
            log.error("orderCd is null");
            setErrorMessage(HkConstant.REGULAR_EXCEPTION);
            return "forwardToError";
        }
        if (onlinePaytype == PrepayType.IPSDom || onlinePaytype == PrepayType.IPSInt
            || onlinePaytype == PrepayType.BILLInt) {// 是在线支付
            // 把会员标志放到session中
            putSession("memClass", memClass);
            // 数据库配置http://www.mangocity.com/payWeb/MangocityPayment
            payAddressUrl = paymentService.getIpsAddressurl();
            // 如果是快钱，需要更改处理的类名
            if (onlinePaytype == PrepayType.BILLInt) {
                payAddressUrl = payAddressUrl.replace("MangocityPayment", "BillPayment");
            }
            // 需要数据库配置http://www.mangocity.com/HWEB/fromIps.action
            successUrl = paymentService.getIpsBgreturl();
            failureUrl = successUrl;
            // 数字加密
            // IPS国际支付需要收取服务费
            order = orderService.getCustomOrderByOrderCD(orderCD, null);
            double sumRmb = order.getSumRmb();
            if (onlinePaytype == PrepayType.IPSInt) {
                OrParam param = hotelManageWeb.getIpsServiceFee();
                String serviceFee = param.getValue();
                double total = sumRmb + sumRmb * Double.parseDouble(serviceFee);
                price = new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP);
                ;
                cardType = CardTypeEnum.INTERNATIONAL;
            } else {
                price = new BigDecimal(sumRmb);
                cardType = CardTypeEnum.DOMESTIC;
            }
            // 支付日期，yyyyMMdd格式
            Date now = Calendar.getInstance().getTime();
            payDate = DateUtil.dateToStringNew(now);
            // 来自酒店
            orderType = HkConstant.ONLINE_PAY_SOURCE;
            log.error("IpsPayAction.prePay(): orderType=" + orderType + " ,orderCd=" + orderCD
                + " ,price=" + price + " ,now=" + now + " ,payDate=" + payDate);
            // 签名
            signature = MangocityEncodeUtils.generatePaymentMd5Digest(orderType, orderCD, price,
                "IPS", now);
            serverReturnType = ServerReturnTypeEnum.NONE;
            attachment = "orderId=" + orderId;
            return "presuccess";
        } else {// 不是 直接返回
            log.error("IpsPayAction.prePay(): not onlinepay, return=forwardToError");
            setErrorMessage(HkConstant.REGULAR_EXCEPTION);
            return "forwardToError";
        }
    }

    public String fromIps() {
        log.error("fromIps()");
        // 取得会员标志
        Object obj = getFromSession("memClass");
        if (null != obj) {
            memClass = (Integer) obj;
        } else {
            memClass = 1;
        }
        ActionContext.getContext().getSession().remove("memClass");
        // orderId = Long.parseLong((attachment == null ? "0" : attachment.replace("orderId=",
        // "")));
        orderCD = orderCode;
        // order = orderService.getOrder(orderId);
        // 对方返回的attachment可能为“attachment=orderId=null”，因此根据attachment得到订单ID不可靠，换一种方式
        order = orderService.getCustomOrderByOrderCD(orderCD, null);
        orderId = order.getID();
        // 新会员
        if (2 == memClass) {
            newMemberCd = order.getMemberCd();
        }

        // ///---------------------------
        List priceLis = new ArrayList<QueryHotelForWebSaleItems>();
        priceLis = hotelManageWeb.queryPriceForWeb(order.getHotelId(), order.getRoomTypeId(), order
            .getChildRoomTypeId(), order.getCheckinDate(), order.getCheckoutDate(), 0, 0, order
            .getPayMethod(), order.getQuotaTypeOld());
        priceTemplist = FormartHwebUtil.setPriceTemplistUtil(priceLis, order.getCheckinDate(),
            order.getCheckoutDate());
        hotelOrderFromBean = FormartHwebUtil.pupulateFromBean(order);
        rate = order.getRateId();
        hotelInfoForWeb = hotelManageWeb.queryHotelInfoForWeb(hotelOrderFromBean.getHotelId());
        // ///---------------------------
        // 在处理业务前进行数字验证
        try {
            if (!MangocityEncodeUtils.verifyReturnMD5Digest(signature, orderCode, transactAmount,
                transactDate, payType, payState, payOrderCode, payTime)) {
                log.error("在ips中数字验证失败！！");
                // 回滚
                hotelManageWeb.rollbackForWebHK(order);
                // 记录操作日志
                OrHandleLog handleLog = new OrHandleLog();
                handleLog.setModifiedTime(new Date());
                handleLog.setContent(HkConstant.PAY_SIGN_FIAL);
                handleLog.setModifierName("网站");
                handleLog.setOrder(order);
                order.getLogList().add(handleLog);
                hintSigner = 1;
                orderService.updateOrder(order, order.getPrepayType(), false, false);
                return "failed";
            }
        } catch (Exception e) {
            log.error("数字验证抛出出异常: " + e.getMessage());
        }
        if ("Y".equals(payState)) {
            boolean flag = hotelManageWeb.saleCommitFowWebHK(order);
            // 如果调中科接口确认交易时出现失败
            if (!flag) {
                hintSigner = 3;
                orderService.updateOrder(order, order.getPrepayType(), true, false);
                return "failed";
            }
            hintSigner = 2;
            orderService.updateOrder(order, order.getPrepayType(), true, true);
            return "success";
        } else {
            // 回滚
            hotelManageWeb.rollbackForWebHK(order);
            // 记录操作日志
            OrHandleLog handleLog = new OrHandleLog();
            handleLog.setModifiedTime(new Date());
            handleLog.setContent(HkConstant.ONLINE_PAY_FIAL);
            handleLog.setModifierName("网站");
            handleLog.setOrder(order);
            order.getLogList().add(handleLog);
            hintSigner = 1;
            orderService.updateOrder(order, order.getPrepayType(), false, false);
            return "failed";
        }
    }

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
    }

    public IOrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }

    public int getOnlinePaytype() {
        return onlinePaytype;
    }

    public void setOnlinePaytype(int onlinePaytype) {
        this.onlinePaytype = onlinePaytype;
    }

    public String getFailureUrl() {
        return failureUrl;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getPayAddressUrl() {
        return payAddressUrl;
    }

    public void setPayAddressUrl(String payAddressUrl) {
        this.payAddressUrl = payAddressUrl;
    }

    public java.math.BigDecimal getPrice() {
        return price;
    }

    public void setPrice(java.math.BigDecimal price) {
        this.price = price;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getTransactAmount() {
        return transactAmount;
    }

    public void setTransactAmount(String transactAmount) {
        this.transactAmount = transactAmount;
    }

    public String getTransactDate() {
        return transactDate;
    }

    public void setTransactDate(String transactDate) {
        this.transactDate = transactDate;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getPayOrderCode() {
        return payOrderCode;
    }

    public void setPayOrderCode(String payOrderCode) {
        this.payOrderCode = payOrderCode;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getServerReturnType() {
        return serverReturnType;
    }

    public void setServerReturnType(String serverReturnType) {
        this.serverReturnType = serverReturnType;
    }

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public PaymentOnlineService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentOnlineService paymentService) {
        this.paymentService = paymentService;
    }

    public int getMemClass() {
        return memClass;
    }

    public void setMemClass(int memClass) {
        this.memClass = memClass;
    }

    public HotelManageWeb getHotelManageWeb() {
        return hotelManageWeb;
    }

    public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
        this.hotelManageWeb = hotelManageWeb;
    }

    public HotelOrderFromBean getHotelOrderFromBean() {
        return hotelOrderFromBean;
    }

    public void setHotelOrderFromBean(HotelOrderFromBean hotelOrderFromBean) {
        this.hotelOrderFromBean = hotelOrderFromBean;
    }

    public List getPriceTemplist() {
        return priceTemplist;
    }

    public void setPriceTemplist(List priceTemplist) {
        this.priceTemplist = priceTemplist;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getNewMemberCd() {
        return newMemberCd;
    }

    public void setNewMemberCd(String newMemberCd) {
        this.newMemberCd = newMemberCd;
    }

    public HotelInfoForWeb getHotelInfoForWeb() {
        return hotelInfoForWeb;
    }

    public void setHotelInfoForWeb(HotelInfoForWeb hotelInfoForWeb) {
        this.hotelInfoForWeb = hotelInfoForWeb;
    }

    public int getHintSigner() {
        return hintSigner;
    }

    public void setHintSigner(int hintSigner) {
        this.hintSigner = hintSigner;
    }

}
