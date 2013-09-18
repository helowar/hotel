/**
 * 
 */
package com.mangocity.hk.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mango.it.commpay.CommManager;
import mango.it.commpay.beans.ReceiveTransBean;
import mango.it.commpay.beans.SendTransBean;

import com.mangocity.hk.constant.HkConstant;
import com.mangocity.hk.service.PaymentOnlineService;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.web.PopulateOrderAction;
import com.mangocity.hotelweb.persistence.HotelOrderFromBean;
import com.mangocity.hweb.action.FormartHwebUtil;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelInfoForWeb;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.util.ServletUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * @author wuyun 交行在线支付 2009-03-12
 */
public class CommPayAction extends PopulateOrderAction {
    /**
     * 订单号
     */
    private String orderCD;

    /**
     * 交行在线支付发送交易所用的实体类
     */
    private SendTransBean sendTransBean;

    /**
     * 交行在线支付接收交易所用的实体类
     */
    private ReceiveTransBean receiveTransBean = new ReceiveTransBean();

    /**
     * 交行在线支付交易处理类
     */
    private CommManager commManager = new CommManager();

    /**
     * 酒店订单处理接口
     */
//    private IOrderService orderService;

    /**
     * 订单
     */
//    private OrOrder order;

    /**
     * 在线支付服务类
     */
    private PaymentOnlineService paymentService;

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
        // 主动通知URL,为空则不发通知;在数据库中配置http://www.mangocity.com/HWEB/commPay!fromComm.action
        String merURL = paymentService.getCommBgreturl();
        // 取货URL,//在数据库中配置http://www.mangocity.com/HWEB/commPay!fromComm.action
        String goodsURL = paymentService.getCommPagereturl();
        order = orderService.getCustomOrderByOrderCD(orderCD, null);
        log.error("orderCD=" + orderCD + "  sumRMB=" + String.valueOf(order.getSumRmb())
            + "  orderID=" + String.valueOf(order.getID()) + "   merURL=" + merURL + "  goodsURL"
            + goodsURL);
        // 交行数字签名验证报错，初步怀疑是金额包含小数点的问题，因此，把double类型的金额转换成long类型，再转换为String，避免出现"1.0"的情况
        // 此外，酒店预付订单的人民币金额已经格式化为无小数的情况，因此，上述操作不会带来金额的差异
        long sum = Double.doubleToLongBits(order.getSumRmb());
        String sumStr = String.valueOf(sum);
        sendTransBean = commManager.getSendTransBean(orderCD, sumStr,
            String.valueOf(order.getID()), merURL, goodsURL);
        log.error("sendTransBean.getMerSignMsg()=" + sendTransBean.getMerSignMsg());
        // 检查签名信息
        if ("".equals(sendTransBean.getMerSignMsg())
            || "Exception".equals(sendTransBean.getMerSignMsg())) {
            log.error("CommPayAction.prePay(): error , signMsg = " + sendTransBean.getMerSignMsg());

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
            orderService.updateOrder(order, PrepayType.COMMInt, false, false);
            setErrorMessage(HkConstant.REGULAR_EXCEPTION);
            return "forwardToError";
        }
        return "presuccess";
    }

    /**
     * 回调方法
     * 
     * @return
     */
    public String fromComm() {
        log.error("fromComm()");
        // 取得会员标志
        Object obj = getFromSession("memClass");
        if (null != obj) {
            memClass = (Integer) obj;
        } else {
            memClass = 1;
        }
        ActionContext.getContext().getSession().remove("memClass");
        String notifyMsg = ServletUtil.filter(request.getParameter("notifyMsg"));
        // 拆分从交行接收到的信息，调用支付接口
        receiveTransBean = commManager.getReceiveTransBean(notifyMsg);
        // 得到订单ID
        // orderId = receiveTransBean.getMerchBatchNo()==
        // null?0:Long.parseLong(receiveTransBean.getMerchBatchNo());
        // 得到订单号
        orderCD = receiveTransBean.getOrderNo();
        // String transId = receiveTransBean.getSerialNo();
        // 交易结果(“1”表示成功)
        String tranRst = receiveTransBean.getTranRst();
        // 验证码
        int veriyCode = receiveTransBean.getVeriyCode();
        // 得到订单ID
        order = orderService.getCustomOrderByOrderCD(orderCD, null);
        orderId = order.getID();
        // 新会员
        if (2 == memClass) {
            newMemberCd = order.getMemberCd();
        }
        // ///---------------------------
        List<QueryHotelForWebSaleItems> priceLis = new ArrayList<QueryHotelForWebSaleItems>();
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
            log.error("交行在线已经支付成功！ orderCd = " + orderCD);
            hintSigner = 2;
            return "success";
        }
        // 验签成功并且银行返回交易成功结果
        if (0 == veriyCode && "1".equals(tranRst)) {
            log.error("交行在线支付成功！orderCd = " + orderCD);
            boolean flag = hotelManageWeb.saleCommitFowWebHK(order);
            // 如果调中科接口确认交易时出现失败
            if (!flag) {
                orderService.updateOrder(order, PrepayType.COMMInt, true, false);
                hintSigner = 3;
                return "failed";
            }
            hintSigner = 2;
            orderService.updateOrder(order, PrepayType.COMMInt, true, true);
            return "success";
        } else {
            log.error("交行在线支付失败！orderCd = " + orderCD);
            hotelManageWeb.rollbackForWebHK(order);
            // 记录操作日志
            OrHandleLog handleLog = new OrHandleLog();
            handleLog.setModifiedTime(new Date());
            handleLog.setContent(HkConstant.ONLINE_PAY_FIAL);
            handleLog.setModifierName("网站");
            handleLog.setOrder(order);
            order.getLogList().add(handleLog);
            hintSigner = 1;
            orderService.updateOrder(order, PrepayType.COMMInt, false, false);
            return "failed";
        }

    }

    public String fromCommTest() {

        // 取得会员标志
        memClass = 2;
        // 得到订单ID
        orderId = 28949L;
        // 得到订单号
        orderCD = "0904023109511";
        // String transId = receiveTransBean.getSerialNo();

        order = orderService.getOrder(orderId);
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
            log.error("交行在线已经支付成功！ orderCd = " + orderCD);
            return "success";
        }

        log.error("交行在线支付成功！orderCd = " + orderCD);
        // boolean flag = hotelManageWeb.saleCommitFowWebHK(order);
        // 如果调中科接口确认交易时出现失败
        // if(!flag){
        orderService.updateOrder(order, PrepayType.COMMInt, true, false);
        hintSigner = 1;
        return "failed";
        // }
        // hintSigner = 2;
        // orderService.updateOrder(order, PrepayType.COMMInt, true, true);
        // return "success";

        /*
         * log.error("交行在线支付失败！orderCd = " + orderCD); boolean rollbackFlag =
         * hotelManageWeb.rollbackForWebHK(order); //记录操作日志 OrHandleLog handleLog = new
         * OrHandleLog(); handleLog.setModifiedTime(new Date());
         * handleLog.setContent(HkConstant.ONLINE_PAY_FIAL); handleLog.setModifierName("网站");
         * handleLog.setOrder(order); order.getLogList().add(handleLog);
         * orderService.updateOrder(order, PrepayType.COMMInt, false, false); return "failed";
         */

    }

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
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

    public CommManager getCommManager() {
        return commManager;
    }

    public void setCommManager(CommManager commManager) {
        this.commManager = commManager;
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
