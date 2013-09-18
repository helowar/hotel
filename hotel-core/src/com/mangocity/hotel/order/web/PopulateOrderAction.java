package com.mangocity.hotel.order.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.persistence.HtlAddBedPrice;
import com.mangocity.hotel.base.persistence.HtlBreakfast;
import com.mangocity.hotel.base.persistence.HtlChargeBreakfast;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlInternet;
import com.mangocity.hotel.base.persistence.HtlWelcomePrice;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.exception.BusinessException;
import com.mangocity.hotel.order.constant.GuaranteeState;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.ResType;
import com.mangocity.hotel.order.persistence.HtlOrderStsLog;
import com.mangocity.hotel.order.persistence.OrCouponRecords;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrFulfillment;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderExtInfo;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrRemark;
import com.mangocity.hotel.order.persistence.assistant.OrderAssist;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.HtlOrderStsLogService;
import com.mangocity.hotel.order.service.IVoucherInterfaceService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.proxy.payment.service.CreditCardPreAuthInterface;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.BaseConstant;

/**
 * 涉及到从表单填充数据到订单实体的操作类<br>
 * 如新增订单，修改订单等
 * 
 * @author chenkeming
 * 
 */
public abstract class PopulateOrderAction extends OrderAction {

    /**
     * 资源接口
     */
    protected ResourceManager resourceManager;

    /**
     * Order辅助类
     */
    protected OrderAssist orderAssist;

    /**
     * 信用卡预授权接口
     */
    protected CreditCardPreAuthInterface creditcardPreAuthService;

    /**
     * 有效的信用卡数
     */
    protected int cardNum;

    /**
     * 入住人数
     */
    protected int fellowNum;

    /**
     * 选中的信用卡id字符串
     */
    private String creditCardIds;
    
    /*
     * 信用卡支付凭证
     */
    private String sid;

    /**
     * 本次操作是否要创建预授权工单
     */
    protected boolean bCanCreatePreAuth = false;

    /**
     * 本次操作是否要创建配送单
     */
    protected boolean bCanCreateFul = false;

    /**
     * 本次修改订单是否修改了入住人
     * 
     * @author chenkeming Mar 2, 2009 2:55:05 PM
     */
    protected boolean bChangeFellow = false;

    /**
     * @author chenkeming Mar 6, 2009 10:48:59 AM
     */
    protected double modifyPrice = 0;

    /**
     * 系统担保金额
     * 
     * @author chenkeming Mar 10, 2009 3:15:14 PM
     */
    protected double sysAssureMoney;

    /**
     * 合同管理类 add by chenjiajie 2009-07-26 hotel2.9.2
     */
    private ContractManage contractManage;
    
    /**
     * 代金券管理接口
     */
    protected IVoucherInterfaceService voucherInterfaceService;
    
    
    /**
     * 取消修改所产生金额
     * add by haibo.li
     */
    protected double reservationDelSum = 0;
    
    protected HtlOrderStsLogService htlOrderStsLogService;
    
	/**
     * 保存到前台
     * 
     * @param order
     */
    public void populateOrderSaveToFront(OrOrder order) {

        Map params = super.getParams();

        MyBeanUtil.copyProperties(order, params);

        // 填充价格详情
        if (!order.isManualOrder()) {
            OrderUtil.fillPriceDetail(order, params, true);
        }

        // 填充入住人
        List fellowList = MyBeanUtil.getBatchObjectFromParam(params, OrFellowInfo.class, fellowNum);
        StringBuffer fellowNames = new StringBuffer();
        for (int i = 0; i < fellowList.size(); i++) {
            OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
            fellowNames.append(fellow.getFellowName() + " ");
            fellow.setOrder(order);
            order.getFellowList().add(fellow);
        }
        order.setFellowNames(fellowNames.toString());

        order.setCreditCardIdsSelect(creditCardIds);
        
        //保存订单支付凭证
        order.getOrOrderExtInfoList().add(new OrOrderExtInfo(BaseConstant.ORDER_EXTINFO_SID,sid,order));
        
        // 如果是担保单，则保存信用卡资料
        if (order.isCreditAssured() && !order.isMango()) { // 114
            orderAssist.handle114CreditCard(order, params, false);
        }

        if (order.isPrepayOrder()) {
            orderAssist.handleNewPrepay(order, params, roleUser);
            if (order.isNeedCreditCard()) {
                if (order.isMango()) {
                } else { // 114
                    orderAssist.handle114CreditCard(order, params, false);
                }
            }

            // 需要配送
            String isInvoice = (String) params.get("isInvoice");
            boolean bNeedInvoice = StringUtil.isValidStr(isInvoice) && isInvoice.equals("1") ? true
                : false;
            if (order.isNeedFulfill() || bNeedInvoice) {
                OrFulfillment fulfill = new OrFulfillment();
                MyBeanUtil.copyProperties(fulfill, params);
                order.setFulfill(fulfill);
            }
        }

        // 备注信息
        Date now = new Date();
        OrRemark remark = null;
        if (!order.isNewOrder()) {
            remark = order.getRemark();
        }
        if (null == remark) {
            remark = new OrRemark();
            MyBeanUtil.copyProperties(remark, params);
            if (null != remark.getPrivateRemark()) {
                String append;
                append = roleUser.getName() + "(" + roleUser.getLoginName() + ") "
                    + DateUtil.datetimeToString(now);
                remark.setPrivateRemark(append + "\n" + remark.getPrivateRemark());
            }
        } else {
            String privateRemark = remark.getPrivateRemark();
            MyBeanUtil.copyProperties(remark, params);
            if (null != remark.getPrivateRemark()) {
                String append;
                append = roleUser.getName() + "(" + roleUser.getLoginName() + ") "
                    + DateUtil.datetimeToString(now);
                if (null != privateRemark) {
                    remark.setPrivateRemark(privateRemark + "\n" + append + "\n"
                        + remark.getPrivateRemark());
                } else {
                    remark.setPrivateRemark(append + "\n" + remark.getPrivateRemark());
                }
            } else {
                remark.setPrivateRemark(privateRemark);
            }
        }
        order.setRemark(remark);

        // 设置hraOrderType
        orderAssist.setOrderHraType(order);

        // 设置会员
        order.setMemberName((null == member.getName() ? "" : member.getName()) + " "
            + (null == member.getFirstname() ? "" : member.getFirstname()) + " "
            + (null == member.getLastname() ? "" : member.getLastname()));
        order.setMemberId(member.getId());
        order.setMemberCd(member.getMembercd());

        // 设置创建者
        order.setCreator(roleUser.getLoginName());
        order.setCreatorName(roleUser.getName());
        order.setModifier(roleUser.getLoginName());
        order.setModifierName(roleUser.getName());

        // 设置紧急程度
        order.setEmergencyLevel(orderAssist.getEmergency(order, now, member, params));
        // v2.7.1 订单记录会员的联名商家项目号
        order.setMemberAliasId(member.getAliasid());

    }

    /**
     * 从参数中获取数据，并填充到订单当中<br>
     * 入住人，<br>
     * 信用卡，<br>
     * 支付记录，<br>
     * 备注，<br>
     * 内部备注,<br>
     * 
     * 如果修改了关键的信息，如入住时间等，需要重新获取配额、价格。 需要生成日志
     * 
     * @param order
     */
    public void populateOrder(OrOrder order) throws BusinessException {
        Map params = getParams();

        // 保存修改前的各种状态
        int oriOrderState = order.getOrderState();
        boolean bLinkmanChg = false;
        Date now = new Date();
        // 保存比较信息的字符串
        StringBuffer strCmp = new StringBuffer();
        if (!order.isNewOrder()) {
            OrOrder tempOrder = new OrOrder();
            MyBeanUtil.copyProperties(tempOrder, params);
            // 设置基本修改信息
            bLinkmanChg = orderAssist.setBasicOrderLog(order, tempOrder, strCmp, params);
            
        }

        MyBeanUtil.copyProperties(order, params);

        if (order.isNewOrder()) {
            if (null == member) {
                super.forwardError("因网络或其它原因没有取到会员信息,需要重试！");
            }
            // 设置会员
            order.setMemberName((null == member.getName() ? "" : member.getName()) + " "
                + (null == member.getFirstname() ? "" : member.getFirstname()) + " "
                + (null == member.getLastname() ? "" : member.getLastname()));
            order.setMemberId(member.getId());
            order.setMemberCd(member.getMembercd());

            // 设置订单创建人
            order.setCreator(roleUser.getLoginName());
            order.setCreatorName(roleUser.getName());
        }

        // 设置订单修改人
        order.setModifier(roleUser.getLoginName());
        order.setModifierName(roleUser.getName());

        // 填充价格详情
        if (order.isNewOrder() && order.getOrderState() != OrderState.NOT_SUBMIT
            && !order.isManualOrder()) {
            OrderUtil.fillPriceDetail(order, params, true);
        }

        // 填充入住人
        List fellowList = MyBeanUtil.getBatchObjectFromParam(params, OrFellowInfo.class, fellowNum);
        String strFellow = "";
        if (1 > order.getFellowList().size()) {
            StringBuffer fellowNames = new StringBuffer();
            for (int i = 0; i < fellowList.size(); i++) {
                OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
                fellowNames.append(fellow.getFellowName() + " ");
                fellow.setOrder(order);
                order.getFellowList().add(fellow);
            }
            order.setFellowNames(fellowNames.toString());
        } else {
            strFellow = orderAssist.saveOrderFellow(order, fellowList, true);
            if (StringUtil.isValidStr(strFellow)) {
                bChangeFellow = true;
                strCmp.append(strFellow);
            } else {
                bChangeFellow = false;
            }
        }
        // 更新会员的常入住人和常联系人
        // TODO: 如果速度慢, 考虑用线程
        if (member.isMango()) {
            // long lBegin = System.currentTimeMillis();
            memberInterfaceService
                .updateMemberFellowAndLinkman(member, order, (
                    order.isNewOrder() || 0 < strFellow.length()), (
                        order.isNewOrder() || bLinkmanChg));
            // log.info("更新会员的常入住人和常联系人 : " + (System.currentTimeMillis()) + "毫秒");
        }

        order.setCreditCardIdsSelect(creditCardIds);

        //保存订单支付凭证
        if(!orderService.validateSid(sid)){
        	order.getOrOrderExtInfoList().add(new OrOrderExtInfo(BaseConstant.ORDER_EXTINFO_SID,sid,order));
        }              
        // 如果是担保单，则保存信用卡资料
        // TODO: 中台修改保存订单时，是否有必要更新(更新的方式?)信用卡资料
        boolean bCreditAssured = StringUtil.isValidStr((String) 
            params.get("isCreditAssured")) ? true
            : false;
        if (bCreditAssured || order.isHasCreatePreAuth()) {
            if (order.isNewOrder()) {
                order.setSuretyState(GuaranteeState.PROCESSING);
            }
            bCanCreatePreAuth = false;
            if (order.isMango()) {
                bCanCreatePreAuth = true;
            } else { // 114
                strCmp.append(orderAssist.handle114CreditCard(order, params, true));
                if (!order.isNewOrder()) {
                    String isFromFront = (String) getFromSession("isFromFront");
                    boolean bFromFront = StringUtil.isValidStr(isFromFront)
                        && isFromFront.equals("1") ? true : false;
                    if (!bFromFront) {
                        bCanCreatePreAuth = true;
                    }
                }
            }
            String isNotifyBalance = (String) params.get("isNotifyBalance");
            boolean bNotifyBalance = StringUtil.isValidStr(isNotifyBalance)
                && isNotifyBalance.equals("1") ? true : false;
            if (!bNotifyBalance) {
                bCanCreatePreAuth = false;
            }
        } else {
            order.setIsCreditAssured(false);
        }

        // 处理预付单的预付信息, 如果已经预付成功则不用处理(补充:已经预付成功,但需要发票的话，也要处理)
        String isInvoice = (String) params.get("isInvoice");
        boolean bNeedInvoice = StringUtil.isValidStr(isInvoice) && isInvoice.equals("1") ? true
            : false;
        if (order.isPrepayOrder() && !order.isHasPrepayed() || bNeedInvoice) {
            // 处理预付信息
            if (order.isPrepayOrder() && !order.isHasPrepayed()) {
                if (order.isNewOrder()) {
                    orderAssist.handleNewPrepay(order, params, roleUser);
                    if (order.isNeedCreditCard()) {
                        if (order.isMango()) {
                            // List cardList =
                            // MyBeanUtil.getBatchObjectFromParam(params,
                            // MemberCreditCard.class, cardNum);
                            // orderAssist.handleNewCards(order, cardList);
                        } else { // 114
                            orderAssist.handle114CreditCard(order, params, false);
                        }
                    }
                } else {
                    strCmp.append(orderAssist.handleEditPrepay(order, params, true, member,
                        roleUser));
                    if (order.isNeedCreditCard()) {
                        if (order.isMango()) {
                            // List cardList =
                            // MyBeanUtil.getBatchObjectFromParam(params,
                            // MemberCreditCard.class, cardNum);
                            // strCmp.append(orderAssist.saveOrderCards(order,
                            // cardList, true));
                        } else { // 114
                            strCmp.append(orderAssist.handle114CreditCard(order, params, true));
                        }
                    }
                }

                /** 如果包含代金券的预付方式 记录代金券明细 hotel2.9.3 add by chenjiajie 2009-08-31 begin **/ 
                if(order.isIncludeCouponPrepay()){
                    //使用代金券的数量
                    String couponStr = (String) params.get("couponNum");
                    if(StringUtil.isValidStr(couponStr)){
                        List<OrCouponRecords> couponRecords = MyBeanUtil.getBatchObjectFromParam(params, OrCouponRecords.class, Integer.parseInt(couponStr));
                        for (OrCouponRecords orCouponRecords : couponRecords) {
                            if(StringUtil.isValidStr(orCouponRecords.getCouponCode())){
                                if(roleUser != null){
                                    orCouponRecords.setOperator(roleUser.getName());
                                }
                                orCouponRecords.setCurrencyType("RMB");
                                orCouponRecords.setPaysucceed(false);
                                orCouponRecords.setOperateTime(new Date());
                                orCouponRecords.setOrder(order);
                                order.addCouponRecords(orCouponRecords);
                            }else{
                                couponRecords.remove(orCouponRecords);
                            }
                        }
                    }
                }
                /** 如果包含代金券的预付方式 记录代金券明细 hotel2.9.3 add by chenjiajie 2009-08-31 end **/
                                
            }

            // 需要配送
            bCanCreateFul = false;
            if (order.isNeedFulfill() || bNeedInvoice) {
                OrFulfillment fulfill = order.getFulfill();
                String fulfillCheck = (String) params.get("fulfillCheck");
                boolean bFulfillCheck = StringUtil.isValidStr(fulfillCheck)
                    && fulfillCheck.equals("1") ? true : false;
                if (null == fulfill) {
                    fulfill = new OrFulfillment();
                    MyBeanUtil.copyProperties(fulfill, params);
                    order.setFulfill(fulfill);
                    if (!order.isNewOrder()) {
                        strCmp.append("增加<font color='blue'>配送信息</font>.<br>");
                    }
                } else {
                    orderAssist.handleModifyFulfill(fulfill, strCmp, params);
                }
                if (bFulfillCheck) {
                    bCanCreateFul = true; // 设置要创建配送单标志
                    strCmp.append("执行创建配送单操作.<br>");
                }
            } else if (!order.isNewOrder()) { // 修改订单时，当已经创建了配送单时，是否能删除订单的配送信息?
                OrFulfillment fulfill = order.getFulfill();
                if (null != fulfill) { // 删除fulfill信息:
                    order.setFulfill(null);
                    strCmp.append("删除<font color='blue'>配送信息</font>.<br>");
                }
            }
        }
        
        //确认使用代金券的方法 hotel2.9.3 add by chenjiajie 2009-09-15
        voucherInterfaceService.confirmVoucherState(order, roleUser, member);

        // 如果状态为已付款，则需要处理退款信息和退款审批信息。
        if (order.isNeedHandleRefund()) {
            strCmp.append(orderAssist.handleRefund(order, params, true, roleUser));
        }

        // 备注信息
        OrRemark remark = null;
        // 酒店生产bug518 暂存前台内部备注到提交中台后会被覆盖，不用if (!order.isNewOrder())作为判断条件 modify by chenjiajie
        // 2009-06-01
        if (null != order.getRemark()) {
            remark = order.getRemark();
        }
        if (null == remark) {
            remark = new OrRemark();
            MyBeanUtil.copyProperties(remark, params);
            if (null != remark.getPrivateRemark()) {
                String append = "";
                append = roleUser.getName() + "(" + roleUser.getLoginName() + ") "
                    + DateUtil.datetimeToString(now);
                remark.setPrivateRemark(append + "\n   " + remark.getPrivateRemark());
            }
        } else {
            String privateRemark = remark.getPrivateRemark();
            MyBeanUtil.copyProperties(remark, params);
            if (null != remark.getPrivateRemark()) {
                String append;
                append = roleUser.getName() + "(" + roleUser.getLoginName() + ") "
                    + DateUtil.datetimeToString(now);
                if (null != privateRemark) {
                    remark.setPrivateRemark(privateRemark + "\n" + append + "\n   "
                        + remark.getPrivateRemark());
                } else {
                    remark.setPrivateRemark(append + "\n   " + remark.getPrivateRemark());
                }
            } else {
                remark.setPrivateRemark(privateRemark);
            }
        }
        order.setRemark(remark);
        /** 增加日审备注 by juesu.chen 2009-10-21 begin **/
        addAuditRemark(order,params);
        /** 增加日审备注 by juesu.chen 2009-10-21 end **/

        // 设置订单的中台组别hraOrderType
        orderAssist.setOrderHraType(order);

        order.setModifier(roleUser.getLoginName());
        order.setModifierName(roleUser.getName());

        // 订单状态若改变则添加操作日志
        if (oriOrderState != order.getOrderState()) {
            strCmp.append("订单状态:"
                + resourceManager.getDescription(ResType.ORDERSTATE, oriOrderState)
                + "-><font color='red'>"
                + resourceManager.getDescription(ResType.ORDERSTATE, order.getOrderState())
                + "</font>");
        }

        // 更新操作日志
        String strModify = strCmp.toString();
        if (StringUtil.isValidStr(strModify) && 0 < strModify.trim().length()) {
            OrHandleLog handleLog = new OrHandleLog();
            // handleLog.setModifier(new Long(roleUser.getId()));
            handleLog.setModifierName(roleUser.getName());
            handleLog.setModifierRole(roleUser.getLoginName()); // 保存工号
            handleLog.setBeforeState(oriOrderState);
            handleLog.setAfterState(order.getOrderState());
            handleLog.setContent(strModify);
            handleLog.setModifiedTime(now);
            handleLog.setHisNo(order.getHisNo());

            handleLog.setOrder(order);
            order.getLogList().add(handleLog);
        }

        // 设置紧急程度
        order.setEmergencyLevel(orderAssist.getEmergency(order, now, member, params));

        // 根据酒店口头确认和书面确认结果设置酒店确认值,同时更新订单中台停留状态
        if (order.isHotelConfirmFax() || order.isHotelConfirmTel()) {
            if (!order.isHotelConfirm()) {
                order.setHotelConfirm(true);
            }
        } else {
            if (order.isHotelConfirm()) {
                order.setHotelConfirm(false);
            }
        }
        // v2.7.1 订单记录会员的联名商家项目号 chenjiajie 2009-02-19
        order.setMemberAliasId(member.getAliasid());
    }
    /**
     * 增加日审备注 by juesu.chen 2009-10-21
     * @param order
     * @param params
     */
    private void addAuditRemark(OrOrder order, Map params) {
		// TODO Auto-generated method stub
    	String auditRemarkInput = (String)params.get("auditRemarkInput");
    	if(!StringUtil.isValidStr(auditRemarkInput))
    		return;
		String auditRemark = order.getAuditRemark();
		String append;
        append = roleUser.getName() + "(" + roleUser.getLoginName() + ") "
            + DateUtil.datetimeToString(new Date());
        if(null != auditRemark){
        	order.setAuditRemark(auditRemark + "\n" + append + "\n   "
            + auditRemarkInput.trim());
        }else{
        	order.setAuditRemark(append + "\n   " + auditRemarkInput.trim());
        }
	}

	/**
     * hotel 2.9.2 进入订单填写页面需要查询房型的附加信息，并传到界面做判断 add by chenjiajie 2009-07-23
     */
    protected void fillRoomAppendInfo() {
        // 根据酒店id取得当前的合同对象，为取合同内提供的服务数据做准备
        HtlContract contract = contractManage.queryCurrentContractByHotelId(order.getHotelId());
        if (null != contract&& null!=contract.getID() && 0<contract.getID()) {
            // 房型id
            Long roomTypeId = order.getRoomTypeId();
            // 订单的入住日期
            Date checkInDate = order.getCheckinDate();
            // 订单的退房日期
            Date checkOutDate = order.getCheckoutDate();
            List<HtlAddBedPrice> addBedPriceList = contractManage.queryAddBed(contract.getID(),
                roomTypeId, checkInDate, checkOutDate,order.getPayMethod());
            List<HtlChargeBreakfast> breakfastList = contractManage.queryBreakfast(
                contract.getID(), checkInDate, checkOutDate);
            List<HtlWelcomePrice> welcomePriceList = contractManage.queryWelcomePrice(contract
                .getID(), checkInDate, checkOutDate);
            List<HtlInternet> internetList = contractManage.queryInternet(contract.getID(),
                roomTypeId, checkInDate, checkOutDate);
            /**
             * 测试bug 1092 检查加早的数据中加床价是否全部都是为0，如果全是0，则清空addBedPriceList modify by chenjiajie
             * 2009-08-04 begin
             */
            int zeroBreakfastCount = 0; // 记录价格小于等于0的数量
            int totolBreakfaseCount = 0; // 记录所有加早的数量
            for (HtlChargeBreakfast chargeBreakfast : breakfastList) {
                for (Iterator it = chargeBreakfast.getBreakfastFees().iterator(); it.hasNext();) {
                    totolBreakfaseCount++;
                    HtlBreakfast breakfast = (HtlBreakfast) it.next();
                    if (0 >= breakfast.getBasePrice()) {
                        zeroBreakfastCount++;
                    }
                }
            }
            if (zeroBreakfastCount < totolBreakfaseCount) {
                // 加早信息
                request.setAttribute("breakfastList", breakfastList);
            }
            /** modify by chenjiajie 2009-08-04 end **/

            // 加床信息
            request.setAttribute("addBedPriceList", addBedPriceList);
            // 接送信息
            request.setAttribute("welcomePriceList", welcomePriceList);
            // 免费宽带信息
            request.setAttribute("internetList", internetList);
        }
    }
    
    /**
     * RMS3007底价/售价的操作日志 add by chenjiajie 2010-01-04
     */
    protected void logShowBasePrice(){
        Map params = super.getParams();
        String originalShowBasePriceStr = (String) params.get("originalShowBasePrice");
        boolean originalShowBasePrice = StringUtil.isValidStr(originalShowBasePriceStr) && "1".equals(originalShowBasePriceStr) ? true : false;
        if(originalShowBasePrice != order.isShowBasePrice()){
            String originalValue = originalShowBasePrice ? "底价" : "售价";
            String currentValue = order.isShowBasePrice() ? "底价" : "售价";
            StringBuffer logContent = new StringBuffer();
            logContent.append("价格显示(酒店传真):");
            logContent.append("从<font color='red'>" + originalValue  + "</font>");
            logContent.append("改为<font color='red'>" + currentValue  + "</font>");
            OrHandleLog handleLog = new OrHandleLog();
            handleLog.setModifierName(roleUser.getName());
            handleLog.setModifierRole(roleUser.getLoginName());
            handleLog.setBeforeState(order.getOrderState());
            handleLog.setAfterState(order.getOrderState());
            handleLog.setContent(logContent.toString());
            handleLog.setModifiedTime(new Date());
            handleLog.setHisNo(order.getHisNo());
            handleLog.setOrder(order);
            order.getLogList().add(handleLog);
            orderService.saveOrUpdate(order);
        }
    }
     
    
   
    /** getter and setter begin */

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public int getCardNum() {
        return cardNum;
    }

    public void setCardNum(int cardNum) {
        this.cardNum = cardNum;
    }

    public int getFellowNum() {
        return fellowNum;
    }

    public void setFellowNum(int fellowNum) {
        this.fellowNum = fellowNum;
    }

    public OrderAssist getOrderAssist() {
        return orderAssist;
    }

    public void setOrderAssist(OrderAssist orderAssist) {
        this.orderAssist = orderAssist;
    }

    public String getCreditCardIds() {
        return creditCardIds;
    }

    public void setCreditCardIds(String creditCardIds) {
        this.creditCardIds = creditCardIds;
    }

    public CreditCardPreAuthInterface getCreditcardPreAuthService() {
        return creditcardPreAuthService;
    }

    public void setCreditcardPreAuthService(CreditCardPreAuthInterface creditcardPreAuthService) {
        this.creditcardPreAuthService = creditcardPreAuthService;
    }

    public double getModifyPrice() {
        return modifyPrice;
    }

    public void setModifyPrice(double modifyPrice) {
        this.modifyPrice = modifyPrice;
    }

    public double getSysAssureMoney() {
        return sysAssureMoney;
    }

    public void setSysAssureMoney(double sysAssureMoney) {
        this.sysAssureMoney = sysAssureMoney;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

    public IVoucherInterfaceService getVoucherInterfaceService() {
		return voucherInterfaceService;
	}

	public void setVoucherInterfaceService(
			IVoucherInterfaceService voucherInterfaceService) {
		this.voucherInterfaceService = voucherInterfaceService;
	}

	public double getReservationDelSum() {
		return reservationDelSum;
	}

	public void setReservationDelSum(double reservationDelSum) {
		this.reservationDelSum = reservationDelSum;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public HtlOrderStsLogService getHtlOrderStsLogService() {
		return htlOrderStsLogService;
	}

	public void setHtlOrderStsLogService(HtlOrderStsLogService htlOrderStsLogService) {
		this.htlOrderStsLogService = htlOrderStsLogService;
	}
    /** getter and setter end */
}
