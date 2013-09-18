package com.mangocity.hotel.order.web;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.HOrder;
import com.mangocity.hotel.order.persistence.HPayment;
import com.mangocity.hotel.order.persistence.HRefund;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrRefund;
import com.mangocity.hotel.order.persistence.view.OrPaymentVO;
import com.mangocity.hotel.order.persistence.view.OrRefundVO;
import com.mangocity.hotel.order.service.IOrderEditService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.proxy.payment.service.CreditCardPreAuthInterface;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.config.ConfigUtil;

/**
 */
public class HOrderOperateAction extends GenericCCAction {

    private static final long serialVersionUID = 6270768424846057713L;

    /**
     * 返回取消修改规则的提示语
     * 
     * @author guzhijie 2009-07-29
     */
    private String addModifyCancelStrr = "";

    private static final String BILLDETAIL_URL = 
        ConfigUtil.getResourceByKey("hotelii_i_delivery.billdetail_url");

    private static final String RESDETAIL_URL = 
        ConfigUtil.getResourceByKey("hotelii_i_delivery.resdetail_url");

    private static final String PREDETAIL_URL = 
        ConfigUtil.getResourceByKey("hotelii_i_preauth.detail_url");

    private IOrderEditService orderEditService;

    private IOrderService orderService;

    /**
     * 信用卡预授权接口
     */
    private CreditCardPreAuthInterface creditcardPreAuthService;

    /**
     * 币种符号
     */
    private String idCurStr;

    private HOrder hOrder;

    private String isFromFront;

    public String viewHOrder() throws Exception {
        Long hOrderId = Long.parseLong((String) getParams().get("hOrderId"));
        hOrder = orderEditService.getOrderH(hOrderId);
        if (null == hOrder) {
            return forwardError("hOrder对象为空！");
        }

        member = super.getMemberInfo((hOrder.getMemberCd()));
        if (null == member) {
            return forwardError("订单会员信息获取失败!");
        }

        if (hOrder.isPrepayOrder()) {
            String[] names = { "Cash", "CredInt", "CredDom", "Pt", "Bank", "Voucher", "Pos", "Bla" };
            OrPaymentVO[] selPayment = new OrPaymentVO[8];
            for (int i = 0; 8 > i; i++) {
                selPayment[i] = new OrPaymentVO(names[i]);
            }
            List list = hOrder.getPaymentListH();
            for (int i = 0; i < list.size(); i++) {
                HPayment paymentH = (HPayment) list.get(i);
                OrPayment payment = new OrPayment();
                MyBeanUtil.copyProperties(payment, paymentH);
                int payTypeIndex = paymentH.getPayType() - 1;
                if(paymentH.getPayType() == PrepayType.BALANCEPAYInt){//余额支付 add by shengwei.zuo  2010-4-1
            		payTypeIndex = 7;
            	}
                selPayment[payTypeIndex].setPayment(payment);
            }
            request.setAttribute("selPayment", selPayment);
        }
        if (hOrder.isHasPrepayed()) {
            String[] names = { "Cash", "CredInt", "CredDom", "Pt", "Bank", "Voucher", "Pos", "Bla" };
            OrRefundVO[] selRefund = new OrRefundVO[7];
            for (int i = 0; 7 > i; i++) {
                selRefund[i] = new OrRefundVO(names[i]);
            }
            List list = hOrder.getRefundListH();
            for (int i = 0; i < list.size(); i++) {
                HRefund refundH = (HRefund) list.get(i);
                OrRefund refund = new OrRefund();
                MyBeanUtil.copyProperties(refund, refundH);
                
                int payTypeIndex = refundH.getRefundType() - 1;
                if(refundH.getRefundType() == PrepayType.BALANCEPAYInt){//余额支付 add by shengwei.zuo  2010-4-1
            		payTypeIndex = 7;
            	}
                
                selRefund[payTypeIndex].setRefund(refund);
            }
            request.setAttribute("selRefund", selRefund);
        }

        if (hOrder.isHasCreatePreAuth()) {
            try {
                String preFlag = creditcardPreAuthService.getPreAuthSucceedFlag("HOTEL", hOrder
                    .getOrderCD());
                request.setAttribute("preFlag", preFlag);
            } catch (Exception re) {
            	log.error(re.getMessage(),re);
            }
            request.setAttribute("preUrl", PREDETAIL_URL);
        }

        // 查看配送单内容url
        if (StringUtil.isValidStr(hOrder.getFulfillmentCD())) {
            request.setAttribute("fulUrl", BILLDETAIL_URL);
        }
        request.setAttribute("fulResUrl", RESDETAIL_URL);

        request.setAttribute("orderItemTotal", hOrder.getOrderItemsH().size());

        // 得到历史订单的信用卡信息
        String creditCardIdsSelect = hOrder.getCreditCardIdsSelect();
        if (StringUtil.isValidStr(creditCardIdsSelect)) {
            creditCardIdsSelect = creditCardIdsSelect.replaceAll("&", ",");
        }
        request.setAttribute("creditCardIdsSelect", creditCardIdsSelect);
        idCurStr = CurrencyBean.idCurMap.get(hOrder.getPaymentCurrency());
        request.setAttribute("rateCurrency", hOrder.getRateId());
        /** hotel 2.9.2 对不同type的取消修改规则返回不同的提示语add by guzhijie 2009-07-23 begin **/
        List liRes = new ArrayList();
        liRes = orderEditService.getHisModifyCancelStr2(hOrder,
            hOrder.getReservationH().getHisID(), hOrder.getSumRmb());
        if (0 < liRes.size()) {
            addModifyCancelStrr = liRes.get(0).toString();

        }

        return "view_histOrder";
    }

    /** getter and setter begin */
    public IOrderEditService getOrderEditService() {
        return orderEditService;
    }

    public void setOrderEditService(IOrderEditService orderEditService) {
        this.orderEditService = orderEditService;
    }

    public HOrder getHOrder() {
        return hOrder;
    }

    public void setHOrder(HOrder order) {
        hOrder = order;
    }

    public String getIdCurStr() {
        return idCurStr;
    }

    public void setIdCurStr(String idCurStr) {
        this.idCurStr = idCurStr;
    }

    public CreditCardPreAuthInterface getCreditcardPreAuthService() {
        return creditcardPreAuthService;
    }

    public void setCreditcardPreAuthService(CreditCardPreAuthInterface creditcardPreAuthService) {
        this.creditcardPreAuthService = creditcardPreAuthService;
    }

    public IOrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }

    public String getIsFromFront() {
        return isFromFront;
    }

    public void setIsFromFront(String isFromFront) {
        this.isFromFront = isFromFront;
    }

    public String getAddModifyCancelStrr() {
        return addModifyCancelStrr;
    }

    public void setAddModifyCancelStrr(String addModifyCancelStrr) {
        this.addModifyCancelStrr = addModifyCancelStrr;
    }

    /** getter and setter end */
}
