package com.mangocity.hotel.order.web;

import java.util.Date;
import java.util.Map;

import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.persistence.HOrder;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.IOrderEditService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;

/**
 * 订单修改基本信息操作
 * 
 * @author chenkeming Feb 11, 2009 3:31:27 PM
 */
public class OrderModifyBaseInfoAction extends PopulateOrderAction {

    private static final long serialVersionUID = 651719926389561259L;

    private IOrderEditService orderEditService;

    /**
     * 是否前台修改
     * 
     * @author chenkeming Feb 11, 2009 1:25:43 PM
     */
    private String isFromFront;

    /**
     * 历史订单ID
     * 
     * @author chenkeming Feb 16, 2009 2:00:40 PM
     */
    private Long hisId;

    // 是否有房费需另缴税
    private String isTaxCharges;

    // 是否促销
    private String isSalesPromo;

    /**
     * 修改基本信息操作
     * 
     * @author chenkeming Feb 11, 2009 3:33:14 PM
     * @return
     */
    public String modify() throws Exception {

        // 获取订单对象
        order = getOrder(orderId);
        if (null == order) {
            return this.forwardError("没有该订单!");
        }

        // 获取登录用户
        roleUser = getOnlineRoleUser();
        if (null == roleUser) {
            return this.forwardError("登录已过期, 请先登录!");
        }

        // 获取会员
        member = getOrderMember(order);
        if (null == member) {
            return this.forwardError("获取不到会员信息!");
        }

        // 复制原单到历史订单
        HOrder orderH = new HOrder();
        OrderUtil.copyOrder(orderH, order);
        orderH.setHisCreator(roleUser.getLoginName());
        // int nextHisNo= orderEditService.getNextHisNo(orderId);
        int oldHisNo = order.getHisNo();
        order.setHisNo(oldHisNo + 1);
        Date nowDate = new Date();
        orderH.setHisCreateDate(nowDate);

        // 以下修改原订单
        populateOrder(order);
        order.setIsCreditAssured(false);

        // 记录修改日志
        OrHandleLog handleLog = OrderUtil.logModifyBaseInfo(order, orderH, roleUser);

        Map params = getParams();
        // 设置是否前台操作,同时设置订单中(前)台修改时间 TODO:是否有必要使用session
        boolean bFromFront = "1".equals(isFromFront) ? true : false;
        putSession("isFromFront", isFromFront);
        if (!bFromFront) {
            order.setModifiedMidTime(nowDate);
        } else {
            order.setModifiedFrontTime(nowDate);
        }
        order.setModifiedTime(nowDate);

        // 初始化一些状态
        order.setSendedHotelFax(false);
        order.setHotelConfirmTel(false);
        order.setHotelConfirmFax(false);
        order.setHotelConfirm(false);
        order.setHotelConfirmFaxReturn(false);
        order.setCustomerConfirm(false);

        // 保存最新的价格详情
        OrderUtil.fillPriceDetail(order, params, false);

        // 获取房费另缴税和促销信息
        if ("1".equals(isTaxCharges)) {
            orderService.getTaxCharge(order);
        }
        if ("1".equals(isSalesPromo)) {
            orderService.getPreSale(order);
        }

        // 增加修改金额
        if (0.001 < modifyPrice) {
            OrderUtil.addModifyMoney(order, modifyPrice, roleUser);
        }

        // 扣配额处理
        int difdays = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        int[] nBreakfast = new int[difdays];
        int[] nBreakNum = new int[difdays];
        for (int i = 0; i < difdays; i++) {
            String sBreakfast = (String) params.get("hBreakfast" + i);
            if (StringUtil.isValidStr(sBreakfast)) {
                nBreakfast[i] = Integer.parseInt(sBreakfast);
            } else {
                nBreakfast[i] = 0;
            }
            String sBreakfastNum = (String) params.get("hBreakNum" + i);
            if (StringUtil.isValidStr(sBreakfastNum)) {
                nBreakNum[i] = Integer.parseInt(sBreakfastNum);
            } else {
                nBreakNum[i] = 0;
            }
        }
        // 处理暂存订单和网站未扣配额订单，需要获取配额
        String sDeductQuota = (String) getParams().get("isDeductQuota");
        if (order.getOrderState() == OrderState.NOT_SUBMIT
            || (StringUtil.isValidStr(sDeductQuota) && "1".equals(sDeductQuota))) {
            // 未扣配额订单不能恢复
            orderH.setHisCanResume(false);
            handleLog.setAfterState(OrderState.HAS_SUBMIT);
            if (orderService
                .deductOrderQuota(order, nBreakfast, nBreakNum, order.getQuotaTypeOld())) {
                // 获取配额成功
                order.setQuotaOk(true);
//              系统自动判断是否满足即时确认条件 addby juesuchen 2009-12-25 begin
                params.put("firstSubmit","1");
                order.setInstantConfirm(OrderUtil.popDialogBox(order, params));
                //系统自动判断是否满足即时确认条件 addby juesuchen 2009-12-25 end
            } else { // 获取配额失败
                order.setQuotaOk(false);
            }
            orderService.calculateTotalAmount(order);
            // 从本部获取预订条款
            orderEditService.getReservationInfo(order, true);
            order.setOrderState(OrderState.HAS_SUBMIT);
            // 拷贝到配额记录表,同时保存订单和历史单
            orderEditService.saveOrderItemToQuotaRecord(order, orderH);
        } else { // 已有配额订单修改单配额操作
            orderH.setHisCanResume(true);
            orderEditService.deductQuota(order, orderH, nBreakfast, nBreakNum, (orderH
                .isHotelConfirm() || 1 > oldHisNo));
        }

        // 供进入修改订单页面用
        putSession("orderEditObj", order);

        return "success";
    }

    /**
     * 恢复历史订单
     * 
     * @author chenkeming Feb 16, 2009 12:37:13 PM
     * @return
     */
    public String resume() {

        // 获取历史订单
        HOrder orderH = orderEditService.getOrderH(hisId);
        if (null == orderH) {
            return forwardError("没有该历史订单!");
        }

        // 获取订单
        orderId = orderH.getOrderId();
        order = getOrder(orderId);
        if (null == order) {
            return this.forwardError("没有该订单!");
        }

        // 获取登录用户
        roleUser = getOnlineRoleUser();
        if (null == roleUser) {
            return this.forwardError("登录已过期, 请先登录!");
        }

        // int nextHisNo= orderEditService.getNextHisNo(orderId);
        int nextHisNo = order.getHisNo() + 1;
        order.setHisNo(nextHisNo);

        // 恢复日志
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName());
        handleLog.setBeforeState(order.getOrderState());
        handleLog.setAfterState(orderH.getOrderState());
        int nHis = orderH.getHisNo();
        StringBuffer buffer = new StringBuffer();
        buffer.append("创建新修改单,序号:" + nextHisNo);
        buffer.append(",该单为恢复到历史订单:" + (0 == nHis ? "原单" : ("序号" + orderH.getHisNo())) + "<br>");
        handleLog.setContent(buffer.toString());
        handleLog.setModifiedTime(new Date());
        handleLog.setHisNo(order.getHisNo());
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);

        // 复制历史订单到当前订单
        OrderUtil.copyOrderResume(order, orderH);
        order.setHisNo(nextHisNo);
        order.setResumeNo(orderH.getHisNo());

        // 退配额
        orderEditService.returnQuotaResume(order, orderH);

        // 供进入修改订单页面用
        putSession("orderEditObj", order);

        return "resume";
    }

    public String getIsFromFront() {
        return isFromFront;
    }

    public void setIsFromFront(String isFromFront) {
        this.isFromFront = isFromFront;
    }

    public IOrderEditService getOrderEditService() {
        return orderEditService;
    }

    public void setOrderEditService(IOrderEditService orderEditService) {
        this.orderEditService = orderEditService;
    }

    public Long getHisId() {
        return hisId;
    }

    public void setHisId(Long hisId) {
        this.hisId = hisId;
    }

    public String getIsSalesPromo() {
        return isSalesPromo;
    }

    public void setIsSalesPromo(String isSalesPromo) {
        this.isSalesPromo = isSalesPromo;
    }

    public String getIsTaxCharges() {
        return isTaxCharges;
    }

    public void setIsTaxCharges(String isTaxCharges) {
        this.isTaxCharges = isTaxCharges;
    }

}
