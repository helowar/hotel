/**
 * 
 */
package com.mangocity.hk.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mango.it.yeepay.YeePayManager;
import mango.it.yeepay.beans.ReceiveTransBean;
import mango.it.yeepay.beans.SendTransBean;
import mango.it.yeepay.util.Constant;

import com.mangocity.hk.constant.HkConstant;
import com.mangocity.hk.service.PaymentOnlineService;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.web.PopulateOrderAction;
import com.mangocity.hotelweb.persistence.HotelOrderFromBean;
import com.mangocity.hweb.action.FormartHwebUtil;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelInfoForWeb;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.opensymphony.xwork2.ActionContext;

/**
 * @author wuyun 易宝在线支付 2009-03-13
 */
public class YeePayAction extends PopulateOrderAction {
    /**
     * 订单号
     */
    private String orderCD;

    /**
     * 订单ID
     */
//    private long orderId;

    /**
     * 易宝在线支付发送交易所用的实体类
     */
    private SendTransBean sendTransBean;

    /**
     * 易宝在线支付接收交易所用的实体类
     */
    private ReceiveTransBean receiveTransBean = new ReceiveTransBean();

    /**
     * 酒店订单处理接口
     */
//    private IOrderService orderService;

    /**
     * 订单
     */
//    private OrOrder order;

    // 服务器点对点交互输出页面
    private static final String RESULT_P2P = "p2p";

    // 应答易宝服务器点对点通知 success为成功接收易宝不会重发，否则易宝会重发
    private String serverP2pResponse;

    // 招商银行 bankId=CMBCHINA-NET,农业银行 bankId=ABC-NET,易宝的默认为空
    private String bankId;

    private PaymentOnlineService paymentService;

    private HotelManageWeb hotelManageWeb;

    private int memClass;

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
        // 把会员标志放到session中
        putSession("memClass", memClass);
        String returnURL = paymentService.getYeePayreturl();// 在数据库中配置
        order = orderService.getCustomOrderByOrderCD(orderCD, null);
        log.error("orderCD=" + orderCD + "  sumRMB=" + String.valueOf(order.getSumRmb())
            + "  orderID=" + String.valueOf(order.getID()) + "   returnURL=" + returnURL
            + "  bankId" + bankId);
        sendTransBean = YeePayManager.createSendTransBean(orderCD, String
            .valueOf(order.getSumRmb()), HkConstant.ONLINE_PAY_SOURCE, returnURL, bankId);
        // 检查签名信息
        if ("".equals(sendTransBean.getSign()) || "Exception".equals(sendTransBean.getSign())) {
            log.error("yeePayAction.prePay(): error, signMsg = " + sendTransBean.getSign());
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
            orderService.updateOrder(order, order.getPrepayType(), false, false);
            setErrorMessage(HkConstant.REGULAR_EXCEPTION);
            return "forwardToError";
        }
        return "presuccess";
    }

    public String fromYee() {
        log.error("fromYee()");
        // 取得会员标志
        Object obj = getFromSession("memClass");
        if (null != obj) {
            memClass = (Integer) obj;
        } else {
            memClass = 1;
        }
        ActionContext.getContext().getSession().remove("memClass");
        Map requestParams = request.getParameterMap();
        // 获取易宝返回参数
        receiveTransBean = YeePayManager.createPayReceiveTransBean(requestParams);
        // 服务器点对点通知
        boolean serverP2p = Constant.BTYPE_SERVERP2P.equals(receiveTransBean.getBType());
        // 验证码
        String veriyCode = receiveTransBean.getVerifyCode();
        orderCD = receiveTransBean.getExtendInfo();
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
            if (serverP2p) {
                serverP2pResponse = Constant.SERVERP2P_SUCCESS;
                log.error("服务器点对点通讯[成功]");
                return RESULT_P2P;
            }
            return "success";
        }
        // 验签成功并且银行返回交易成功结果
        if (Constant.VERIFY_SUCCESS.equals(veriyCode)) {
            boolean validatePayAmount = false;// 校验支付金额
            // 校验支付金额
            double sumReceivable = order.getSumRmb();// 应收金额
            // 网关返回的支付金额
            // long realpayAmount = new BigDecimal(
            // receiveTransBean.getTotalFee()).multiply(new BigDecimal(100)).longValue();
            double realpayAmount = Double.parseDouble(receiveTransBean.getTotalFee());
            if (realpayAmount >= sumReceivable) {
                validatePayAmount = true;
            }
            // 支付成功
            if (receiveTransBean.getPayResult() && validatePayAmount) {
                log.error("易宝支付成功！orderCd = " + orderCD);
                boolean flag = hotelManageWeb.saleCommitFowWebHK(order);
                // 如果调中科接口确认交易时出现失败
                if (!flag) {
                    hintSigner = 3;
                    orderService.updateOrder(order, order.getPrepayType(), true, false);
                    return "failed";
                }
                hintSigner = 2;
                orderService.updateOrder(order, order.getPrepayType(), true, true);
                if (serverP2p) {
                    serverP2pResponse = Constant.SERVERP2P_SUCCESS;
                    log.error("服务器点对点通讯[成功] orderCd = " + orderCD);
                    return RESULT_P2P;
                } else {
                    return "success";
                }
            } else {
                log.debug("易宝支付失败 orderCd = " + orderCD);
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
                if (serverP2p) {
                    serverP2pResponse = Constant.SERVERP2P_SUCCESS;
                    log.error("服务器点对点通讯[成功] orderCd = " + orderCD);
                    return RESULT_P2P;
                } else {
                    return "failed";
                }
            }
        } else {
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
            orderService.updateOrder(order, order.getPrepayType(), false, false);
            if (serverP2p) {
                serverP2pResponse = Constant.SERVERP2P_FAILURE;
                log.error("服务器点对点通讯[失败] orderCd = " + orderCD);
                return RESULT_P2P;
            } else {
                return "failed";
            }
        }
    }

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public SendTransBean getSendTransBean() {
        return sendTransBean;
    }

    public void setSendTransBean(SendTransBean sendTransBean) {
        this.sendTransBean = sendTransBean;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
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

    public OrOrder getOrder() {
        return order;
    }

    public void setOrder(OrOrder order) {
        this.order = order;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getServerP2pResponse() {
        return serverP2pResponse;
    }

    public void setServerP2pResponse(String serverP2pResponse) {
        this.serverP2pResponse = serverP2pResponse;
    }

    public PaymentOnlineService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentOnlineService paymentService) {
        this.paymentService = paymentService;
    }

    public HotelManageWeb getHotelManageWeb() {
        return hotelManageWeb;
    }

    public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
        this.hotelManageWeb = hotelManageWeb;
    }

    public int getMemClass() {
        return memClass;
    }

    public void setMemClass(int memClass) {
        this.memClass = memClass;
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
