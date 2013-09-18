package com.mangocity.hotel.order.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.web.TranslateUtil;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.MemberConfirmType;
import com.mangocity.hotel.order.constant.ModifyScopeType;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.manager.HraManager;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.persistence.OrRefund;
import com.mangocity.hotel.order.service.IOrderEditService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hotel.order.service.assistant.OrderFax;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.util.MoneyHandle;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;

/**
 * 
 * 用于Ajax调用
 * 
 * @author chenkeming
 * 
 */
public class OrderDwrAction {
	private static final MyLog log = MyLog.getLogger(OrderDwrAction.class);
    /**
     * 会员接口
     */
    protected MemberInterfaceService memberInterfaceService;

    private IOrderService orderService;

    private IOrderEditService orderEditService;

    /**
     * 传真邮件辅助类
     */
    private MsgAssist msgAssist;

    /**
     * 酒店本部接口
     */
    private IHotelService hotelService;

    /**
     * 当前登录权限用户
     */
    private UserWrapper roleUser;

    private TranslateUtil translateUtil;
    
    private HraManager hraManager;

    /**
     * 支付方式
     */
//    private static final String[] NAMES = { "Cash", "CredInt", "CredDom", "Pt", "Bank", "Voucher",
//        "Pos" };

    // private HttpSession dwrGetSession() {
    // WebContext ctx = WebContextFactory.get();
    // return ctx.getSession();
    // }

    /**
     * 确认收款
     * 
     * @param orderId
     * @param paymentId
     * @param payType
     * @return
     */
    public String confirmPrepay(long orderId, long paymentId, String payType, HttpSession session) {

        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        if (null == roleUser || !roleUser.isOrgPayment()) {
            return "fail";
        }
        int nPayType = Integer.parseInt(payType) - 1;

        /** 正常1-7的支付方式可以按照原有减1的逻辑，代金券和余额支付的常量值特殊 不能按照原有逻辑 hotel2.9.3 modify by chenjiajie 2009-09-02 begin **/
    	if(PrepayType.Coupon == Integer.parseInt(payType)){
    		nPayType = PrepayType.Coupon - 1;
    	}else if(PrepayType.BALANCEPAYInt == Integer.parseInt(payType)){
    		nPayType = PrepayType.BALANCEPAYInt - 1;
    	}
        /** 正常1-7的支付方式可以按照原有减1的逻辑，代金券和余额支付的常量值特殊 不能按照原有逻辑 hotel2.9.3 modify by chenjiajie 2009-09-02 end **/
    	
        StringBuffer sb = new StringBuffer();
        sb.append(PrepayType.strNames[nPayType]);
        sb.append(",");

        MemberDTO member = (MemberDTO) session.getAttribute("onlineMember");
        try {
        	OrOrder order = orderService
            .prepayCheck(orderId, paymentId, roleUser.getName(), member, sb);
        	if (null != order) {
                if (order.isHasPrepayed()) {
                    sb.append(",All");
                }
                return sb.toString();
            }
        }catch(Exception e) {
        	log.error("确认收款失败!");
        }
        
        return "fail";
    }

    /**
     * 确认退款
     * 
     * @param orderId
     * @param refundId
     * @param refundType
     * @return
     */
    public String confirmRefund(long orderId, long refundId, 
        String refundType, HttpSession session) {

        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        if (null == roleUser || !roleUser.isOrgRefund()) {
            return "fail";
        }

        MemberDTO member = (MemberDTO) session.getAttribute("onlineMember");

        OrOrder order;
		try {
			order = orderService.refundCheck(orderId, refundId, member);
	        if (null != order) {
	            int nPayType = Integer.parseInt(refundType)-1;
	            String retStr = PrepayType.strNames[nPayType];
	            if (order.isHasRefund()) {
	                return retStr + ",All";
	            } else {
	                return retStr;
	            }
	        }
	        
		} catch (Exception e) {
			log.error("确认退款失败！");
		}
		return "fail";
    }

    /**
     * 退款审批
     * 
     * @param orderId
     * @return
     */
    public String refundBillAuditPass(long orderId, HttpSession session) {

        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        if (null == roleUser || !roleUser.isOrgRefundAudit()) {
            return "fail";
        }

        OrOrder order = orderService.refundBillAuditPass(orderId, roleUser);
        if (null != order) {
            StringBuffer sb = new StringBuffer();
            List refundList = order.getRefundList();
            for (int i = 0; i < refundList.size(); i++) {
                if (0 < i) {
                    sb.append(",");
                }
                OrRefund refund = (OrRefund) refundList.get(i);
                sb.append(refund.getRefundType()).append(",").append(PrepayType.strNames[refund.getRefundType()-1])
                    .append(",").append(refund.getID());
            }
            return sb.toString();
        }
        return "fail";
    }

    /**
     * 酒店传真确认
     * 
     * @param orderId
     * @param faxItemId
     * @return
     */
    public String confirmFaxItem(long orderId, long faxItemId, int nConfirm, String confirmNo,
        String confirmNotes, HttpSession session) {

        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        if (null == roleUser || !roleUser.isCanEditOrder()) {
            return "fail";
        }

        boolean isConfirm = 1 == nConfirm ? true : false;
        OrOrder order = orderService.confirmFaxItem(orderId, faxItemId, isConfirm, confirmNo,
            confirmNotes, roleUser);
        if (null != order) {
            if (order.isHotelConfirm()) {
                if (StringUtil.isValidStr(confirmNo)) {
                    return "1," + faxItemId + ",1";
                } else {
                    return "1," + faxItemId + ",0";
                }
            } else {
                return "0," + faxItemId;
            }
        }
        return "fail";
    }

    /**
     * 呼出配额
     * 
     * @param orderId
     * @param roomItemId
     * @param sBasePrice
     * @param sSalePrice
     * @return
     */
    public String callRoom(long orderId, long roomItemId, String sBasePrice, String sSalePrice,
        HttpSession session) {

        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        if (null == roleUser || !roleUser.isOrgMid()) {
            return "fail";
        }

        double basePrice = Double.parseDouble(sBasePrice);
        double salePrice = Double.parseDouble(sSalePrice);
        OrOrder order = orderService.callRoom(orderId, roomItemId, basePrice, salePrice, roleUser);
        if (null != order) {
            if (order.isQuotaOk()) {
                return "" + roomItemId + "," + order.getSum() + 
                (order.isPrepayOrder() ? ("," + order
                    .getSumRmb())
                    : ",1");
            }
            return "" + roomItemId + "," + order.getSum() + (order.isPrepayOrder() ? ("," + MoneyHandle.getMoneyOfInteger(order.getSumRmb())) : "");
        }
        return "fail";
    }

    /**
     * 变价
     * 
     * @param orderId
     * @param roomItemId
     * @param sBasePrice
     * @param sSalePrice
     * @param session
     * @return
     */
    public String changeRoomPrice(long orderId, long roomItemId, String sBasePrice,
        String sSalePrice, HttpSession session) {

        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        if (null == roleUser || !roleUser.isOrgMid()) {
            return "fail";
        }

        double basePrice = Double.parseDouble(sBasePrice);
        double salePrice = Double.parseDouble(sSalePrice);
        OrOrder order = orderService.changeRoomPrice(orderId, roomItemId, basePrice, salePrice,
            roleUser);
        if (null != order) {
            return "" + roomItemId + "," + order.getSum() + (order.isPrepayOrder() ? ("," + MoneyHandle.getMoneyOfInteger(order.getSumRmb())) : "");
        }
        return "fail";
    }

    /**
     * 预览邮件
     * 
     * @param orderId
     * @return
     * @throws IOException 
     */
    public String previewMail(long orderId, String sSendtype, String sender, ServletContext sc,
        HttpSession session) throws IOException {
        // OrOrder order = orderService.getCustomOrder(new Long(orderId), new
        // String[] { "remark", "fellowList", "orderItems" });
        OrOrder order = orderService.getCustomOrderForMail(Long.valueOf(orderId));

        // 获取订单的member供预览页面用
        MemberDTO memberDTO = (MemberDTO) session.getAttribute("onlineMember");
        if (null == memberDTO || !memberDTO.getMembercd().equals(order.getMemberCd())) {
            try {
            	memberDTO = memberInterfaceService.getMemberByCode(order
                    .getMemberCd());
            } catch (Exception e) {
            	log.error(e.getMessage(),e);
                return "获取订单会员信息错误！";
            }
        }

        HtlHotel hotel = hotelService.findHotel(order.getHotelId().longValue());

        int sendtype = Integer.parseInt(sSendtype);
        String xmlString = "";
        OrderFax orderFax = null;
        String fileName = sc.getRealPath("/order/model");
        
        if(!order.isCooperateOrder()) { // 非交行渠道订单
            if (order.isMango()) {
                fileName += "/mango/hotelOrder";
                if (sendtype == MemberConfirmType.CONFIRM) {
                    orderFax = msgAssist.genOrderFaxMailByGuestMangoConfirm(order, hotel, memberDTO,
                        sender);
                    fileName += "Confirm";
                } else {
                    orderFax = msgAssist
                        .genOrderFaxMailByGuestMangoCancel(order, hotel, memberDTO, sender);
                    fileName += "Cancel";
                }
                if(OrderSource.FAN_TI_NET.equals(order.getSource())){//繁体网站 增加繁体网站的邮件模板 add by shengwei.zuo 2010-9-14
                	 fileName += "_zh_hk.htm";
                }else{
                	 fileName += "_zh_cn.htm";
                }
                xmlString = msgAssist.getHolteOrderMailHtml(orderFax, fileName);
            } else { // 114
                if (null != memberDTO.getState() && memberDTO.getState().equals("LTT")) {
                    fileName += "/unicom/hotelOrder";
                } else if (null != memberDTO.getState() && memberDTO.getState().equals("WTT")) {
                    fileName += "/cnc/hotelOrder";
                } else if (null != memberDTO.getState() && memberDTO.getState().equals("WTBJ")) {
                    fileName += "/wtbj/hotelOrder";
                } else if (null != memberDTO.getState() && memberDTO.getState().equals("NHZY")) {
                    fileName += "/nhzy/hotelOrder";
                } else {
                    fileName += "/114/hotelOrder";
                }

                if (sendtype == MemberConfirmType.CONFIRM) {
                    fileName += "Confirm";
                    orderFax = msgAssist.genOrderFaxMailByGuest114Confirm(order, hotel, memberDTO, sender);
                } else {
                    fileName += "Cancel";
                    orderFax = msgAssist.genOrderFaxMailByGuest114Cancel(order, hotel, memberDTO, sender);
                }
                fileName += "_zh_cn.htm";
                xmlString = msgAssist.getHolteOrderMailHtml(orderFax, fileName);
            }	
        } else { // 交行渠道订单
        	fileName += "/jiaohang/hotelOrder";
            if (sendtype == MemberConfirmType.CONFIRM) {
                fileName += "Confirm";
                orderFax = msgAssist.genOrderFaxMailByGuest114Confirm(order, hotel, memberDTO, sender);
            } else {
                fileName += "Cancel";
                orderFax = msgAssist.genOrderFaxMailByGuest114Cancel(order, hotel, memberDTO, sender);
            }
            fileName += "_zh_cn.htm";
            xmlString = msgAssist.getHolteOrderMailHtml(orderFax, fileName);
        }

        return xmlString;
    }

    /**
     * 修改单确认酒店
     * 
     * @author chenkeming Feb 13, 2009 9:06:08 PM
     * @param orderId
     * @param confirmWay
     * @param session
     * @return
     */
    public String confirmHotel(long orderId, int confirmWay, HttpSession session) {
        try {
            roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
            if (null == roleUser) {
                return "fail," + confirmWay;
            }

            orderEditService.confirmHotel(orderId, confirmWay, roleUser);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return "fail," + confirmWay;
        }
        return "ok," + confirmWay;
    }

    /**
     * 获取取消/修改规则
     * 
     * @author chenkeming Feb 24, 2009 5:52:57 PM
     * @param reservationID
     * @param sumRmb
     * @return
     */
    public List getModifyCancelStr(long reservationID, double sumRmb) {
        return orderEditService.getModifyCancelStr1(reservationID, sumRmb);
    }

    /**
     * 新下订单时获取取消/修改规则显示字符串信息,不包含具体金额数
     * 
     * @author chenkeming Feb 24, 2009 5:53:24 PM
     * @param payMethod
     * @param itemsStr
     * @return
     */
    public List getModifyCancelStrNew(String payMethod, String itemsStr) {
        return orderEditService.getModifyCancelStrNew1(payMethod, itemsStr);
    }

    /**
     * modify by shizhongwen 传入订单ID 检查取消订单扣款金额
     * 
     * @author chenkeming Feb 24, 2009 6:20:10 PM
     * @param reservationID
     * @param orderID
     *            订单的ID
     * @param checkIn
     * @return
     */
    public String checkCancel(long reservationID, long orderID, String checkIn, long roomQuantity) {
        return orderEditService.getModifyandCancelCheck(reservationID, orderID, checkIn,
            roomQuantity, ModifyScopeType.CANCEL);
    }

    /**
     * modify by shizhongwen 传入订单ID 检查修改订单扣款金额
     * 
     * @author chenkeming Feb 24, 2009 7:16:32 PM
     * @param reservationID
     * @param sumRmb
     * @param checkIn
     * @return
     */
    public String checkModify(long reservationID, long orderID, String checkIn, long roomQuantity) {
        return orderEditService.getModifyandCancelCheck(reservationID, orderID, checkIn,
            roomQuantity, ModifyScopeType.MODIFY);
    }

    /**
     * 改变本次修改订单产生的金额
     * 
     * @author chenkeming Feb 25, 2009 6:09:05 PM
     * @param orderId
     * @param reservationID
     * @param modifyPrice
     * @param note
     * @param session
     * @return
     */
    /*
     * public String chgModPrice(long orderId, long reservationID, double modifyPrice, String note,
     * HttpSession session) { roleUser = (UserWrapper) session.getAttribute("onlineRoleUser"); if
     * (roleUser == null || !roleUser.isOrgMid()) { return "fail"; } return
     * orderEditService.chgModPrice(orderId, reservationID, modifyPrice, note, roleUser); }
     */

    /**
     * @author chenkeming Mar 6, 2009 3:03:23 PM
     * @param hotelId
     * @param checkinDate
     * @param checkoutDate
     * @param roomTypeId
     * @param payMethod
     * @return
     */
    public List<OrPriceDetail> getReservInfo(long hotelId, String checkinDate, String checkoutDate,
        String roomTypeId, String payMethod) {
        List<OrPriceDetail> li = orderEditService.getManualOrderReserv(hotelId, checkinDate,
            checkoutDate, roomTypeId, payMethod);
        return li;
    }

    /**
     * 确认金额列表
     * 
     * @author chenkeming Mar 6, 2009 3:09:25 PM
     * @param Id
     * @param session
     * @return
     */
    public String confirmMoney(long Id, HttpSession session) {

        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        if (null == roleUser || !roleUser.isOrgMid()) {
            return "fail";
        }
        String res = orderEditService.confirmMoney(Id, roleUser);
        if ("success".equals(res)) {
            return "" + Id + ",success";
        } else {
            return "fail";
        }
    }

    /**
     * 取消金额列表
     * 
     * @author chenkeming Mar 6, 2009 3:09:25 PM
     * @param Id
     * @param session
     * @return
     */
    public String cancelMoney(long Id, HttpSession session) {

        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        if (null == roleUser || !roleUser.isOrgMid()) {
            return "fail";
        }
        String res = orderEditService.cancelMoney(Id, roleUser);
        if ("success".equals(res)) {
            return "" + Id + ",success";
        } else {
            return "fail";
        }
    }

    /**
     * 改变修改/取消金额
     * 
     * @author chenkeming Mar 6, 2009 3:09:25 PM
     * @param Id
     * @param money
     * @param session
     * @return
     */
    public String confirmChgMoney(long Id, double money, HttpSession session) {

        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        if (null == roleUser || !roleUser.isOrgMid()) {
            return "fail";
        }
        return orderEditService.confirmChgMoney(Id, money, roleUser);
    }

    /**
     * 订单前台转中台处理
     * 
     * @author chenkeming Mar 18, 2009 11:48:07 AM
     * @param orderId
     * @param session
     * @return
     */
    public String confirmToMid(long orderId, HttpSession session) {
        roleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        if (null == roleUser) {
            return "fail";
        }
        String fromFront = (String) session.getAttribute("isFromFront");
        if (!"1".equals(fromFront)) {
            return "fail";
        }
        if (orderService.confirmToMid(orderId, roleUser)) {
            return "ok";
        } else {
            return "fail";
        }
    }
    
    /** getter and setter begin */

    public UserWrapper getRoleUser() {
        return roleUser;
    }

    public void setRoleUser(UserWrapper roleUser) {
        this.roleUser = roleUser;
    }

    public TranslateUtil getTranslateUtil() {
        return translateUtil;
    }

    public void setTranslateUtil(TranslateUtil translateUtil) {
        this.translateUtil = translateUtil;
    }

    public IOrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }

    public MemberInterfaceService getMemberInterfaceService() {
        return memberInterfaceService;
    }

    public void setMemberInterfaceService(MemberInterfaceService memberInterfaceService) {
        this.memberInterfaceService = memberInterfaceService;
    }

    public void setHotelService(IHotelService hotelService) {
        this.hotelService = hotelService;
    }

    public MsgAssist getMsgAssist() {
        return msgAssist;
    }

    public void setMsgAssist(MsgAssist msgAssist) {
        this.msgAssist = msgAssist;
    }

    public IOrderEditService getOrderEditService() {
        return orderEditService;
    }

    public void setOrderEditService(IOrderEditService orderEditService) {
        this.orderEditService = orderEditService;
    }

	public HraManager getHraManager() {
		return hraManager;
	}

	public void setHraManager(HraManager hraManager) {
		this.hraManager = hraManager;
	}

    /** getter and setter end */
}