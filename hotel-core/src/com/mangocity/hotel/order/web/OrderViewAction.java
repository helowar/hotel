package com.mangocity.hotel.order.web;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.OrLockedOrders;
import com.mangocity.hotel.order.persistence.OrOrderStatistics;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrRefund;
import com.mangocity.hotel.order.persistence.view.OrPaymentVO;
import com.mangocity.hotel.order.persistence.view.OrRefundVO;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.config.ConfigUtil;

/**
 * 订单操作相关，包括查看，修改
 * 
 * @author chenkeming
 * 
 */
public class OrderViewAction extends PopulateOrderAction {

    private static final long serialVersionUID = 6552333123371282306L;

    private static final String BILLDETAIL_URL = ConfigUtil.getResourceByKey(
        "hotelii_i_delivery.billdetail_url");

    private static final String RESDETAIL_URL = ConfigUtil.getResourceByKey(
        "hotelii_i_delivery.resdetail_url");

    private static final String PREDETAIL_URL = ConfigUtil.getResourceByKey(
        "hotelii_i_preauth.detail_url");
    
    private IHotelService hotelService;

    /**
     * 币种符号
     */
    private String idCurStr;

    private String unlockURL;

    private String mostStar;

    private String mostPriceLevel;

    private String HOP_VVIP;

    private String creditRemark;

    private String creditAssureInfo;

    /**
     * 以下用于撤单保存撤单原因
     */
    private String cancelReason;

    private String cancelMessage;

    private String guestCancelMessage;

    /**
     * 查看订单内容
     * 
     * @return
     */
    public String viewOrder() {
        order = getOrder(orderId);
        if (null == order) {
            return forwardError("order对象为空！");
        }

        String isFromAudit = (String) getParams().get("isFromAudit");
        boolean bFromAudit = false;
        if (StringUtil.isValidStr(isFromAudit)) {
            bFromAudit = StringUtil.isValidStr(isFromAudit) && isFromAudit.equals("1") ? true
                : false;
        }

        request.setAttribute("isFromAudit", bFromAudit ? 1 : 0);

        user = getOnlineWorkStates();
        roleUser = getOnlineRoleUser();
        if (false == bFromAudit) {
            member = getOrderMember(order);
            if (null == member) {
                return forwardError("订单会员信息获取失败!");
            }
            // 会员信息
            if (member.isMango()) {
                OrOrderStatistics orderStat = orderService.getOrderStatByMemberCd(member.getMembercd());
                request.setAttribute("orderStat", orderStat);

                if (null != orderStat) {
                    double price = orderStat.getAvgPrice();
                    double star = orderStat.getAvgStar();
                    mostStar = "";
                    mostPriceLevel = "";

                    if (2 > star) {
                        mostStar = "无星级";
                    } else if (0 == Double.compare(star,2.0)) {
                        mostStar = "二星";
                    } else if (0 == Double.compare(star,3.0)) {
                        mostStar = "三星";
                    } else if (0 == Double.compare(star,3.5)) {
                        mostStar = "四星";
                    } else if (0 == Double.compare(star,4.0)) {
                        mostStar = "四星";
                    } else if (0 == Double.compare(4.5,star)) {
                        mostStar = "五星";
                    } else if (0 == Double.compare(5.0,star)) {
                        mostStar = "五星";
                    } else {
                        mostStar = "无星级";
                    }

                    if (1 == Double.valueOf(price).intValue()) {
                        mostPriceLevel = "0-250";
                    } else if (2 == Double.valueOf(price).intValue()) {
                        mostPriceLevel = "250-400";
                    } else if (3 == Double.valueOf(price).intValue()) {
                        mostPriceLevel = "400-600";
                    } else if (4 == Double.valueOf(price).intValue()) {
                        mostPriceLevel = "600-800";
                    } else if (5 == Double.valueOf(price).intValue()) {
                        mostPriceLevel = ">800";
                    }

                }
            }
        }
        // //////

        String isFromFront = (String) getParams().get("isFromFront");
        putSession("isFromFront", isFromFront);

        if (order.isPrepayOrder()) {
            String[] names = { "Cash", "CredInt", "CredDom", "Pt", "Bank", "Voucher", "Pos","Bla" };
            OrPaymentVO[] selPayment = new OrPaymentVO[8];
            for (int i = 0; 8 > i; i++) {
                selPayment[i] = new OrPaymentVO(names[i]);
            }
            List list = order.getPaymentList();
            for (int i = 0; i < list.size(); i++) {
                OrPayment payment = (OrPayment) list.get(i);
                int payTypeIndex = payment.getPayType() - 1;
                if(payment.getPayType() == PrepayType.BALANCEPAYInt){
                	payTypeIndex = 7;
                }
                selPayment[payTypeIndex].setPayment(payment);
            }
            request.setAttribute("selPayment", selPayment);
        }
        if (order.isHasPrepayed()) {
            String[] names = { "Cash", "CredInt", "CredDom", "Pt", "Bank", "Voucher", "Pos","Bla" };
            OrRefundVO[] selRefund = new OrRefundVO[8];
            for (int i = 0; 8 > i; i++) {
                selRefund[i] = new OrRefundVO(names[i]);
            }
            List list = order.getRefundList();
            for (int i = 0; i < list.size(); i++) {
                OrRefund refund = (OrRefund) list.get(i);
                int payTypeIndex = refund.getRefundType()-1;
                if(refund.getRefundType()== PrepayType.BALANCEPAYInt){
                	payTypeIndex = 7;
                }
                selRefund[payTypeIndex].setRefund(refund);
            }
            request.setAttribute("selRefund", selRefund);
        }
        if (false == bFromAudit) {
            request.setAttribute("oftenFellowCount", null == member.getFellowList() ? 0 : member
                .getFellowList().size());
            request.setAttribute("oftenLinkmanCount", null == member.getLinkmanList() ? 0 : member
                .getLinkmanList().size());
        }
        // 查看预授权工单信息
        if (order.isHasCreatePreAuth()) {
            try {
                String preFlag = creditcardPreAuthService.getPreAuthSucceedFlag("HOTEL", order
                    .getOrderCD());
                request.setAttribute("preFlag", preFlag);
            } catch (Exception re) {
            	log.error(re.getMessage(),re);
            }
            request.setAttribute("preUrl", PREDETAIL_URL);
        }

        // 查看配送单内容url
        if (StringUtil.isValidStr(order.getFulfillmentCD())) {
            request.setAttribute("fulUrl", BILLDETAIL_URL);
        }
        request.setAttribute("fulResUrl", RESDETAIL_URL);

        request.setAttribute("orderItemTotal", order.getOrderItems().size());

        int assureListSize = 0;
        OrReservation orReserv = order.getReservation();
        if (null != orReserv) {
            assureListSize = orReserv.getAssureList().size();
            String str = orReserv.getCreditRemark();
            if (null != str) {
                creditRemark = str.replace("\r\n", "");
            }
        }
        request.setAttribute("assureListSize", assureListSize);

        String creditCardIdsSelect = order.getCreditCardIdsSelect();
        if (StringUtil.isValidStr(creditCardIdsSelect)) {
            creditCardIdsSelect = creditCardIdsSelect.replaceAll("&", ",");
        }
        request.setAttribute("creditCardIdsSelect", creditCardIdsSelect);
        idCurStr = CurrencyBean.idCurMap.get(order.getPaymentCurrency());
        request.setAttribute("rateCurrency", order.getRateId());
        creditAssureInfo = hotelService.qryCreditAssure(order.getHotelId(), order.getCheckinDate(),
            order.getChildRoomTypeId().toString());
        return VIEW_ORDER;
    }

    public String refreshSelf() {
        order = getOrder(orderId);
        if (null == order) {
            return forwardError("order对象不存在!");
        }
        member = getOnlineMember();
        if (null == member || !member.getMembercd().equals(order.getMemberCd())) {
            member = getMemberSimpleInfo(order.getMemberCd(), false);
        }

        return "refreshSelf";
    }

    /**
     * 删除未提交订单 并未真正删除，仅置illusive为真
     * 
     * @return
     */
    public String updateOrderIllusiveFlag() {
        roleUser = getOnlineRoleUser();
        order = getOrder(orderId);
        member = getOrderMember(order);
        if (null == member) {
            return forwardError("会员session过期,请重新获取会员信息");
        }
        if (roleUser.isOrgMid()) {
            order.setModifiedMidTime(new Date());
        } else if (roleUser.isOrgFront()) {
            order.setModifiedFrontTime(new Date());
        }
        order.setIllusive(true);
        saveOrUpdateOrder(order);
        return "frontNotSubmit";
    }

    /**
     * 解锁订单
     * 
     * @return
     */
    public String unLockOrder() {
        roleUser = getOnlineRoleUser();
        order = getOrder(orderId);
        String locker = (String) getParams().get("locker");
        // 解锁订单
        if (roleUser.isOrgMidAdmin() || roleUser.getLoginName().equals(locker)) {
            OrLockedOrders lockedOrders = new OrLockedOrders();
            lockedOrders.setOrderId(orderId);
            lockedOrderService.deleteLockedOrder(lockedOrders);
        }

        return super.forwardMsgBox("解锁订单成功！", "refreshSelf()");
    }

    /**
     * 批解锁订单(只有中台管理员可以操作)
     * 
     * @return
     */
    public String unLockOrders() {
        String setIDs = (String) getParams().get("setIDs");
        // 解锁订单
        lockedOrderService.deleteLockedOrders(setIDs);

        return super.forwardMsgBox("解锁订单成功！", "refreshSelf()");
    }

    /**
     * 查看会员资料
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String readVIP() {
        Map params = getParams();
        String vvipname = params.get("vvipname").toString().trim();

        log.info(vvipname);
        String[] retVip = vvipname.split("##");
        log.info(retVip[0]);
        return "readVip";
    }

//    /**
//     * 判断是否需要通知结算组。
//     * 
//     * 说明： <li>（1）预付单指信用卡支付。 <li>（2）面付单指信用卡担保。
//     * 
//     * @return 是否需要通知结算组。
//     */
//    private boolean isNotifyBalance() {
//        Map parameters = this.getParams();
//        String isNotifyBalance = (String) parameters.get("isNotifyBalance");
//        boolean bNotifyBalance = StringUtil.isValidStr(isNotifyBalance)
//            && isNotifyBalance.equals("1") ? true : false;
//        return bNotifyBalance;
//    }

    /** getter and setter begin */

    public String getUnlockURL() {
        return unlockURL;
    }

    public void setUnlockURL(String unlockURL) {
        this.unlockURL = unlockURL;
    }

    public String getMostPriceLevel() {
        return mostPriceLevel;
    }

    public void setMostPriceLevel(String mostPriceLevel) {
        this.mostPriceLevel = mostPriceLevel;
    }

    public String getMostStar() {
        return mostStar;
    }

    public void setMostStar(String mostStar) {
        this.mostStar = mostStar;
    }

    public String getHOP_VVIP() {
        return HOP_VVIP;
    }

    public void setHOP_VVIP(String hop_vvip) {
        HOP_VVIP = hop_vvip;
    }

    public String getIdCurStr() {
        return idCurStr;
    }

    public void setIdCurStr(String idCurStr) {
        this.idCurStr = idCurStr;
    }

    public String getCreditAssureInfo() {
        return creditAssureInfo;
    }

    public void setCreditAssureInfo(String creditAssureInfo) {
        this.creditAssureInfo = creditAssureInfo;
    }

    public String getCreditRemark() {
        return creditRemark;
    }

    public void setCreditRemark(String creditRemark) {
        this.creditRemark = creditRemark;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    public void setCancelMessage(String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getGuestCancelMessage() {
        return guestCancelMessage;
    }

    public void setGuestCancelMessage(String guestCancelMessage) {
        this.guestCancelMessage = guestCancelMessage;
    }

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

    /** getter and setter end */
}
