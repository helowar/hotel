/**
 * 
 */
package com.mangocity.hk.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mango.it.alipay.AliPayManager;
import mango.it.alipay.beans.ReceiveTransBean;
import mango.it.alipay.beans.SendTransBean;
import mango.it.alipay.util.CheckURL;
import mango.it.alipay.util.Constant;

import com.mangocity.hk.constant.HkConstant;
import com.mangocity.hk.service.PaymentOnlineService;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotelweb.persistence.HotelOrderFromBean;
import com.mangocity.hweb.action.FormartHwebUtil;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelInfoForWeb;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.opensymphony.xwork2.ActionContext;

/**
 * @author wuyun 支付宝在线支付 2009-03-12
 */
public class AliPayAction extends GenericAction {
    /**
     * 订单号
     */
    private String orderCD;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 支付宝在线支付发送交易所用的实体类
     */
    private SendTransBean sendTransBean;

    /**
     * 支付宝在线支付接收交易所用的实体类
     */
    private ReceiveTransBean receiveTransBean = new ReceiveTransBean();

    /**
     * 酒店订单处理接口
     */
    private IOrderService orderService;

    /**
     * 在线支付服务类
     */
    private PaymentOnlineService paymentService;

    /**
     * 会员标志
     */
    private int memClass;

    /**
     * 订单
     */
    private OrOrder order;

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

    /**
     * 预处理
     * 
     * @return
     */
    public String prePay() {
        // 如果订单号为空
        if (null == orderCD || "".equals(orderCD)) {
            log.error("orderCd is null");
            setErrorMessage(HkConstant.REGULAR_EXCEPTION);
            return "forwardToError";
        }
        // 把会员标志放到session中
        putSession("memClass", memClass);
        // 返回URL
        String returnURL = paymentService.getAliPayreturl();// 在数据库中配置
        // 通知URL,在数据库中配置
        String notifyURL = paymentService.getAliPaynotifyurl();
        order = orderService.getCustomOrderByOrderCD(orderCD, null);
        log.error("orderCD=" + orderCD + "  sumRMB=" + String.valueOf(order.getSumRmb())
            + "  orderID=" + String.valueOf(order.getID()) + "   returnURL=" + returnURL
            + "  goodsURL" + notifyURL);
        // 创建发送交易实体类，设置里面各属性的值
        sendTransBean = AliPayManager.createSendTransBean(orderCD, String
            .valueOf(order.getSumRmb()), HkConstant.ONLINE_PAY_SOURCE, notifyURL, returnURL);

        // 检查签名信息
        if ("".equals(sendTransBean.getSign()) || "Exception".equals(sendTransBean.getSign())) {
            log.error("AliPayAction.prePay(): error , signMsg = " + sendTransBean.getSign());
            // 回滚
            hotelManageWeb.rollbackForWebHK(order);
            // 记录操作日志
            OrHandleLog handleLog = new OrHandleLog();
            handleLog.setModifiedTime(new Date());
            handleLog.setContent(HkConstant.REGULAR_EXCEPTION);
            handleLog.setModifierName("网站");
            handleLog.setOrder(order);
            order.getLogList().add(handleLog);
            // 记录失败交易记录
            orderService.updateOrder(order, PrepayType.ALIInt, false, false);
            setErrorMessage(HkConstant.REGULAR_EXCEPTION);
            return "forwardToError";
        }
        // 否则成功
        return "presuccess";
    }

    /**
     * 回调方法
     * 
     * @return
     */
    public String fromAli() {
        log.error("fromAli()");
        populateReceiveTransBean();
        // 取得会员标志
        Object obj = getFromSession("memClass");
        if (null != obj) {
            memClass = (Integer) obj;
        } else {
            memClass = 1;
        }
        ActionContext.getContext().getSession().remove("memClass");
        orderCD = request.getParameter("body");
        Map requestParams = request.getParameterMap();
        // 对receiveTransBean的信息进行验签
        String veriyCode = AliPayManager.verifyReceiveTransBean(receiveTransBean, requestParams);
        String alipayNotifyURL = "http://notify.alipay.com/trade/notify_query.do?partner="
            + mango.it.alipay.util.Constant.PARTNER_ID + "&notify_id="
            + receiveTransBean.getNotifyId();
        // 获取支付宝ATN返回结果，true是正确的订单信息，false 是无效的
        String responseTxt = CheckURL.check(alipayNotifyURL);
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
        // 如果此订单支付状态已经为在线支付成功，直接跳转页面
        if (order.getOrderState() == OrderState.HAS_PAID) {
            hintSigner = 2;
            log.error("支付宝已经支付成功！");
            return "success";
        }
        // 验签成功并且银行返回交易成功结果,更新订单支付状态
        if (!"0".equals(veriyCode) && !"true".equals(responseTxt)) {
            log.error("数据验证失败！orderCd = " + orderCD);
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
            orderService.updateOrder(order, PrepayType.ALIInt, false, false);
            return "failed";
        } else {
            if ("TRADE_SUCCESS".equals(receiveTransBean.getTrade_status())) {
                log.error("支付宝支付成功！orderCd = " + orderCD);
                boolean flag = hotelManageWeb.saleCommitFowWebHK(order);
                // 如果调中科接口确认交易时出现失败
                if (!flag) {
                    hintSigner = 3;
                    orderService.updateOrder(order, PrepayType.ALIInt, true, false);
                    return "failed";
                } else {
                    hintSigner = 2;
                    orderService.updateOrder(order, PrepayType.ALIInt, true, true);
                    return "success";
                }
            } else {
                log.error("支付宝支付失败！orderCd = " + orderCD);
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
                orderService.updateOrder(order, PrepayType.ALIInt, false, false);
                return "failed";
            }
        }

    }

    /**
     * 响应通知
     * 
     * @return
     */
    public String doNotify() {
        populateReceiveTransBean();
        // memClass = (Integer) getFromSession("memClass");
        // ActionContext.getContext().getSession().remove("memClass");
        orderCD = receiveTransBean.getBody();
        // partner合作伙伴id（必须填写）
        String partner = Constant.PARTNER_ID;
        // partner的对应交易安全校验码（必须填写）
        // 获取支付宝ATN返回结果，true是正确的订单信息，false 是无效的
        String alipayNotifyURL = "http://notify.alipay.com/" + "trade/notify_query.do?partner="
            + partner + "&notify_id=" + receiveTransBean.getNotifyId();
        String responseTxt = CheckURL.check(alipayNotifyURL);
        // 获得POST过来的参数
        Map requestParams = request.getParameterMap();
        // 得到验证结果
        String veriyCode = AliPayManager.verifyReceiveTransBean(receiveTransBean, requestParams);
        log.error("AlipayAction.doNotify() veriyCode = " + veriyCode);
        try {
            if ("0".equals(veriyCode) && responseTxt.equals("true")) {
                log.error("数据验证成功：orderCd = " + orderCD);
                if ("TRADE_SUCCESS".equals(receiveTransBean.getTrade_status())) {
                    OrOrder order = orderService.getCustomOrderByOrderCD(orderCD, null);
                    orderId = order.getID();
                    // 如果此订单支付状态已经为在线支付成功，直接跳转页面
                    if (order.getOrderState() == OrderState.HAS_PAID) {
                        return "notifysuccess";
                    } else {
                        log.error("支付成功：orderCd = " + orderCD);
                        boolean flag = hotelManageWeb.saleCommitFowWebHK(order);
                        // 如果调中科接口确认交易时出现失败
                        if (!flag) {
                            orderService.updateOrder(order, PrepayType.ALIInt, true, false);
                        } else {
                            orderService.updateOrder(order, PrepayType.ALIInt, true, true);
                        }
                        return "notifysuccess";
                    }
                } else {
                    log.error("支付失败：orderCd = " + orderCD);
                    // 回滚
                    hotelManageWeb.rollbackForWebHK(order);
                    // 记录操作日志
                    OrHandleLog handleLog = new OrHandleLog();
                    handleLog.setModifiedTime(new Date());
                    handleLog.setContent(HkConstant.ONLINE_PAY_FIAL);
                    handleLog.setModifierName("网站");
                    order.getLogList().add(handleLog);
                    orderService.updateOrder(order, PrepayType.ALIInt, false, false);
                    return "notifyfailed";
                }
            } else {
                log.error("数据验证失败：orderCd = " + orderCD);
                // 回滚
                hotelManageWeb.rollbackForWebHK(order);
                // 记录操作日志
                OrHandleLog handleLog = new OrHandleLog();
                handleLog.setModifiedTime(new Date());
                handleLog.setContent(HkConstant.ONLINE_PAY_FIAL);
                handleLog.setModifierName("网站");
                order.getLogList().add(handleLog);
                orderService.updateOrder(order, PrepayType.ALIInt, false, false);
                return "notifysuccess";
            }
        } catch (Exception e) {
            log.error("AliPayAction.doNotify() Exception = " + e.getMessage());
        }
        return "notifyfailed";
    }

    /**
     * 组装接收到的信息
     */
    private void populateReceiveTransBean() {
        receiveTransBean.setBody(request.getParameter("body"));
        receiveTransBean.setBuyerEmail(request.getParameter("buyer_email"));
        receiveTransBean.setBuyerId(request.getParameter("buyer_id"));
        receiveTransBean.setNotifyId(request.getParameter("notify_id"));
        receiveTransBean.setNotifyTime(request.getParameter("notify_time"));
        receiveTransBean.setNotifyType(request.getParameter("notify_type"));
        receiveTransBean.setOutTradeNo(request.getParameter("out_trade_no"));
        receiveTransBean.setPaymentType(request.getParameter("payment_type"));
        receiveTransBean.setPrice(request.getParameter("price"));
        receiveTransBean.setQuantity(request.getParameter("quantity"));
        receiveTransBean.setSign(request.getParameter("sign"));
        receiveTransBean.setSignType(request.getParameter("sign_type"));
        receiveTransBean.setSubject(request.getParameter("subject"));
        receiveTransBean.setTotalFee(request.getParameter("total_fee"));
        receiveTransBean.setTrade_status(request.getParameter("trade_status"));
        receiveTransBean.setTradeNo(request.getParameter("trade_no"));
    }

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public SendTransBean getSendTransBean() {
        return sendTransBean;
    }

    public void setSendTransBean(SendTransBean sendTransBean) {
        this.sendTransBean = sendTransBean;
    }

    public ReceiveTransBean getReceiveTransBean() {
        return receiveTransBean;
    }

    public void setReceiveTransBean(ReceiveTransBean receiveTransBean) {
        this.receiveTransBean = receiveTransBean;
    }

    public IOrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public OrOrder getOrder() {
        return order;
    }

    public void setOrder(OrOrder order) {
        this.order = order;
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
