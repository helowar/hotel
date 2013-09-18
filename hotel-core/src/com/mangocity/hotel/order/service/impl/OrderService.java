package com.mangocity.hotel.order.service.impl;

import hk.com.cts.ctcp.hotel.constant.ResultConstant;
import hk.com.cts.ctcp.hotel.constant.TxnStatusType;
import hk.com.cts.ctcp.hotel.service.HKService;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.TxnStatusData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BasicData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.CalAmtData;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.persistence.HtlLimitFavourableHotel;
import com.mangocity.hotel.base.dao.HtlLimitFavourableDao;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.persistence.B2BAgentCommUtils;
import com.mangocity.hotel.base.persistence.HtlB2bComminfo;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlHotelExt;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlProjectCode;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.persistence.OrReservation;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.CommissionService;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.IQuotaControlService;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.base.service.assistant.BookRoomCondition;
import com.mangocity.hotel.base.service.assistant.QuotaItem;
import com.mangocity.hotel.base.service.assistant.QuotaQuery;
import com.mangocity.hotel.base.service.assistant.QuotaReturn;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.base.service.assistant.ReservationInfo;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.dto.PointDTO;
import com.mangocity.hotel.ext.member.exception.MemberException;
import com.mangocity.hotel.ext.member.service.PointsDelegate;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.GuaranteeState;
import com.mangocity.hotel.order.constant.GuaranteeType;
import com.mangocity.hotel.order.constant.MemberConfirmType;
import com.mangocity.hotel.order.constant.OrderExtInfoType;
import com.mangocity.hotel.order.constant.OrderItemType;
import com.mangocity.hotel.order.constant.OrderMessageSplit;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.constant.QuotaMemberType;
import com.mangocity.hotel.order.dao.impl.OrFaxLogDao;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.dao.impl.OrOrderFaxDao;
import com.mangocity.hotel.order.dao.impl.OrOrderItemDao;
import com.mangocity.hotel.order.dao.impl.OrUserPowerDao;
import com.mangocity.hotel.order.manager.MidOrderTransfer;
import com.mangocity.hotel.order.persistence.B2bModifyOrderInfo;
import com.mangocity.hotel.order.persistence.FellowInfo;
import com.mangocity.hotel.order.persistence.OrChannelNo;
import com.mangocity.hotel.order.persistence.OrFaxLog;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrFulfillment;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderExtInfo;
import com.mangocity.hotel.order.persistence.OrOrderFax;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrOrderOfSMS;
import com.mangocity.hotel.order.persistence.OrOrderStatistics;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrPreSale;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.persistence.OrRefund;
import com.mangocity.hotel.order.persistence.OrTaxCharge;
import com.mangocity.hotel.order.persistence.OrToContractgroup;
import com.mangocity.hotel.order.persistence.OrUserPower;
import com.mangocity.hotel.order.persistence.assistant.OrderAssist;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.persistence.view.OrOrderVO;
import com.mangocity.hotel.order.persistence.view.PaymentVO;
import com.mangocity.hotel.order.service.IHotelReservationInfoService;
import com.mangocity.hotel.order.service.IOrderBenefitService;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.service.IVoucherInterfaceService;
import com.mangocity.hotel.order.service.IcalculatePriceService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.MemberUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.ValidationUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.HotelBaseConstantBean;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.hotel.constant.QuotaType;
import com.mangocity.util.log.MyLog;
import com.mangocity.vch.app.service.exception.VCHException;
import com.mangocity.webnew.util.HotelPayOnlieUtil;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Sms;

/**
 * 主要提供以下功能： 1.创建订单 2.修改保存订单 3.获取配额 4.查看订单 5.查看操作日志 6.查看订单价格明细 7.复制订单 8.取消订单 9.预取消订单
 * 
 * @author chenkeming
 */
public class OrderService implements IOrderService, Serializable {

    /**
	 * 
	 */	
	private static final long serialVersionUID = -8002730041161886733L;

	private static final MyLog log = MyLog.getLogger(OrderService.class);
	
	private HtlLimitFavourableDao htlLimitFavourableDao;

    private OrOrderDao orOrderDao;

    private OrOrderItemDao orOrderItemDao;
    
    private CommissionService commissionService;
    
    private MemberInterfaceService memberInterfaceService;
    private IHotelReservationInfoService hotelReservationInfoService;
    /**
     * 需要用到的酒店模块提供的接口
     */
    private IHotelService hotelService;
    
    private IPriceManage priceManage;

   

	/**
     * 资源接口
     */
    private ResourceManager resourceManager;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

    /**
     * 配额接口
     */
	private IQuotaControlService quotaControl;

    /**
     * 积分接口 改用会员Jar的接口 modify by chenjiajie 2009-05-12
     */
    protected PointsDelegate pointsDelegate;

    private DAOIbatisImpl queryDao;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

    private OrOrderFaxDao orOrderFaxDao;

    private OrFaxLogDao orFaxLogDao;

    private OrderAssist orderAssist;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

    /**
     * 中旅接口
     * 
     * @author chenkeming Mar 27, 2009 10:08:47 AM
     */
    private HKService hkService;

    /**
     * v2.8 自动发客人确认短信用
     * 
     * @author chenkeming Mar 27, 2009 1:12:50 PM
     */
    private CommunicaterService communicaterService;

    private OrUserPowerDao orUserPowerDao;

    /**
     * 计算订单金额
     */
    private IcalculatePriceService calculatePriceService;
    
    /**
     * 代金券接口
     * hotel2.9.3 add by chenjiajie 2009-09-02
     */
    private IVoucherInterfaceService voucherInterfaceService;

    /**
     * 优惠立减服务接口 add by chenjiajie 2009-10-15 2009-10-15 
     */
    private IOrderBenefitService orderBenefitService;
    /**
    * 中台订单流转类
    */
   private MidOrderTransfer midOrderTransfer;
   
   protected SystemDataService systemDataService;
   
    

	/**
     * 定制获取order对象
     * 
     * @param fetchList
     *            - 需要抓取的一对多的list
     */
    public OrOrder getCustomOrderByOrderCD(String orderCD, String[] fetchList) {
        Session session = orOrderDao.getCurrentSession();
        Criteria criteria = session.createCriteria(OrOrder.class).add(
            Restrictions.eq("orderCD", orderCD));
        if (null != fetchList) {
            for (int i = 0; i < fetchList.length; i++) {
                criteria.setFetchMode(fetchList[i], FetchMode.JOIN);
            }
        }
        List orders = criteria.list();

        if (null != orders && 0 < orders.size()) {
            return (OrOrder) orders.get(0);
        } else {
            return null;
        }
    }

    /**
     * 定制获取order对象
     * 
     * @param fetchList
     *            - 需要抓取的一对多的list
     */
    public OrOrder getCustomOrder(long orderId, String[] fetchList) {

        Session session = orOrderDao.getCurrentSession();
        Criteria criteria = session.createCriteria(OrOrder.class).add(
            Restrictions.eq("ID", Long.valueOf(orderId)));
        for (int i = 0; i < fetchList.length; i++) {
            criteria.setFetchMode(fetchList[i], FetchMode.JOIN);
        }
        List orders = criteria.list();

        if (null != orders && 0 < orders.size()) {
            return (OrOrder) orders.get(0);
        } else {
            return null;
        }
    }

    /**
     * 预付收款
     * 
     * @param orderId
     * @param paymentId
     * @param userName
     * @param sb
     * @return
     * 
     */
    public OrOrder prepayCheck(long orderId, long paymentId, String userName, MemberDTO member,
        StringBuffer sb) {
        // OrWorkStates user =
        // workStatesManager.returnWorkStatesBylogonId(userId);
        OrOrder order = this.getCustomOrder(orderId, new String[] { "paymentList" });
        List payments = order.getPaymentList();
        for (int i = 0; i < payments.size(); i++) {
            OrPayment payment = (OrPayment) payments.get(i);
            if (payment.getID().longValue() == paymentId) {

                if (payment.getPayType() == PrepayType.Points) { // 扣积分
                    double points = payment.getMoney() * 100.;
                    PointDTO pt = null;
                    try {
						if (order.getSource() != null
								&& order.getSource().equals(
										OrderSource.FROM_WEB)) {
							pt = pointsDelegate.changePonitsByMemberCd(
									order.getMemberCd(), BaseConstant.USERNAME,
									"1", points, order.getOrderCD(),
									BaseConstant.TRANSCHANNEL_NET,
									BaseConstant.TRANSCHANNELSN_NET);
						} else if (order.getSource() != null
								&& order.getSource().equals(
										OrderSource.FAN_TI_NET)) {
							pt = pointsDelegate.changePonitsByMemberCd(
									order.getMemberCd(), BaseConstant.USERNAME,
									"1", points, order.getOrderCD(),
									BaseConstant.TRANSCHANNEL_NET,
									BaseConstant.TRANSCHANNELSN_BIG);
						} else {
							pt = pointsDelegate.changePonitsByMemberCd(
									order.getMemberCd(), BaseConstant.USERNAME,
									"1", points, order.getOrderCD(),
									BaseConstant.TRANSCHANNEL_CC,
									BaseConstant.TRANSCHANNELSN_CC);
						}
                    } catch (Exception e) {
                        log.error(e.getMessage(),e);
                        return null;
                    }
                    if (null != pt && StringUtil.isValidStr(pt.getRc()) && pt.getRc().equals("0")) {
                        if (null != member && member.getMembercd().equals(order.getMemberCd())) {
                            long extPt = Long.parseLong(member.getPoint().getBalanceValue());
                            extPt -= BigDecimal.valueOf(points).longValue();
                            member.getPoint().setBalanceValue("" + extPt);
                        }
                    } else {
                        log.info("扣积分失败!");
                        return null;
                    }
                }
                
                
                
                /** hotel2.9.3调用代金券接口，扣取代金券 add by chenjiajie 2009-09-10 begin **/
                if(payment.getPayType() == PrepayType.Coupon){
                	String sOrderCD = order.getOrderCD();
                	if(StringUtil.isValidStr(sOrderCD)){
	                	try {
	                		//调用代金券接口预订确认接口
	        				voucherInterfaceService.callVchServiceOrder(order, userName);	
	        			} catch (VCHException e) {
	        				voucherInterfaceService.rollBackVchOrderState(order, e.getMessage());
	        				log.error(" vch010 代金券支付失败!原因："+e.getMessage()+" OrderCD: "+sOrderCD);
	        			}
                	}
                }
                /** hotel2.9.3调用代金券接口，扣取代金券 add by chenjiajie 2009-09-10 end **/

                payment.setPaySucceed(true);
                payment.setConfirmTime(new Date());
                payment.setConfirmer(userName);

                // 如果已经全部预付完成
                if (OrderUtil.checkHasPrepayed(order)) {
                    order.setHasPrepayed(true);
                    order.setOrderState(OrderState.HAS_PAID);
                }
                order.setModifiedMidTime(new Date());
                saveOrUpdate(order);
                sb.append(payment.getConfirmer() + ","
                    + DateUtil.datetimeToString(payment.getConfirmTime()));
                return order;
            }
        }
        return null;
    }

    /**
     * 预付退款
     * 
     * @param orderId
     * @param refundId
     * @return
     * @throws MemberException 
     * 
     */
    public OrOrder refundCheck(long orderId, long refundId, MemberDTO member) throws MemberException {
        OrOrder order = this.getCustomOrder(orderId, new String[] { "refundList" });

        // 检查是否允许确认退款
        if (!order.isPrepayOrder() || !order.isHasPrepayed() || !order.isNeedRefund()
            || !order.isRefundBillAuditPass()) {
            return null;
        }

        List refunds = order.getRefundList();
        for (int i = 0; i < refunds.size(); i++) {
            OrRefund refund = (OrRefund) refunds.get(i);
            if (refund.getID().longValue() == refundId) {

                if (refund.getRefundType() == PrepayType.Points) { // 退积分
                    double points = refund.getMoney() * 100.;
                    PointDTO pt = null;
                	pt = pointsDelegate.changePonitsByMemberCd(
							order.getMemberCd(), BaseConstant.USERNAME,
							"2", points, order.getOrderCD(),
							BaseConstant.TRANSCHANNEL_CC,
							BaseConstant.TRANSCHANNELSN_CC);
                    if (null != pt && StringUtil.isValidStr(pt.getRc()) && pt.getRc().equals("0")) {
                        if (null != member && member.getMembercd().equals(order.getMemberCd())) {
                            long extPt = Long.parseLong(member.getPoint().getBalanceValue());
                            extPt += BigDecimal.valueOf(points).longValue();
                            member.getPoint().setBalanceValue("" + extPt);
                        }
                    } else {
                    	log.info("退积分失败!");
                        return null;
                    }
                }
                

                refund.setRefundSucceed(true);

                // 如果已经全部退款完成
                if (OrderUtil.checkHasRefund(order)) {
                    order.setHasRefund(true);
                    order.setOrderState(OrderState.REFUND_SUCCESS);
                }
                order.setModifiedMidTime(new Date());
                saveOrUpdate(order);
                return order;
            }
        }
        return null;
    }

    /**
     * 预付单退款审批
     * 
     * @param orderId
     * @param roleUser
     * @return
     * 
     */
    public OrOrder refundBillAuditPass(long orderId, UserWrapper roleUser) {
        OrOrder order = this.getCustomOrder(orderId, new String[] { "refundList" });

        // 检查是否允许退款审批
        if (!order.isPrepayOrder() || !order.isHasPrepayed() || !order.isNeedRefund()
            || order.isRefundBillAuditPass()) {
            return null;
        }
        if (0 >= order.getRefundList().size()) {
            return null;
        }

        int oldState = order.getOrderState();

        order.setRefundBillAuditPass(true);
        order.setOrderState(OrderState.HAS_AUDIT_REFUND);
        Date now = new Date();
        order.setModifiedMidTime(now);

        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName()); // 保存工号
        handleLog.setBeforeState(oldState);
        handleLog.setAfterState(order.getOrderState());
        handleLog.setContent("订单通过退款审批.<br>");
        handleLog.setModifiedTime(now);
        handleLog.setHisNo(order.getHisNo());
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);

        saveOrUpdate(order);
        return order;
    }

    /**
     * 酒店传真确认
     * 
     * @param orderId
     * @param faxItemId
     * @param isConfirm
     * @param confirmNo
     * @param confirmNotes
     * @return
     * 
     */
    public OrOrder confirmFaxItem(long orderId, long faxItemId, boolean isConfirm,
        String confirmNo, String confirmNotes, UserWrapper roleUser) {
        OrOrder order = this.getCustomOrder(orderId, new String[] { "faxList" });

        boolean bValidConfirm = false;
        List faxList = order.getFaxList();
        for (int i = 0; i < faxList.size(); i++) {
            OrOrderFax fax = (OrOrderFax) faxList.get(i);
            if (fax.getID().longValue() == faxItemId) {
                fax.setHotelReturn(true);
                fax.setConfirm(isConfirm);
                fax.setConfirmNo(confirmNo);
                fax.setNotes(confirmNotes);

                // 如果选了"确认"并且确认号非空
                if (isConfirm && StringUtil.isValidStr(confirmNo)) {
                    fax.setValidConfirm(true);
                    bValidConfirm = true;
                } else {
                    fax.setValidConfirm(false);
                }

                boolean bChange = false;
                StringBuffer strCmp = new StringBuffer();
                if (!order.isHotelConfirmFaxReturn()) {
                    order.setHotelConfirmFaxReturn(true);
                    strCmp.append("订单改为:<font color='red'>" + "已收酒店回传" + "</font>");
                    strCmp.append("<br>");
                    bChange = true;
                }
                if (fax.getType() == MemberConfirmType.CONFIRM
                    && isConfirm != order.isHotelConfirmFax()) {
                    order.setHotelConfirm(isConfirm);
                    order.setHotelConfirmFax(isConfirm);
                    if (isConfirm) {
                        strCmp.append("订单改为:<font color='red'>" + "已书面确认酒店" + "</font>");
                    } else {
                        strCmp.append("订单改为:去掉 <font color='red'>" + "已书面确认酒店" + "</font>");
                    }
                    strCmp.append("<br>");
                    bChange = true;
                }
                if (bChange) {
                    OrHandleLog handleLog = new OrHandleLog();
                    // handleLog.setModifier(new Long(roleUser.getId()));
                    handleLog.setModifierName(roleUser.getName());
                    handleLog.setModifierRole(roleUser.getLoginName()); // 保存工号
                    handleLog.setBeforeState(order.getOrderState());
                    handleLog.setAfterState(order.getOrderState());
                    handleLog.setContent(strCmp.toString());
                    handleLog.setModifiedTime(new Date());
                    handleLog.setHisNo(order.getHisNo());
                    handleLog.setOrder(order);
                    order.getLogList().add(handleLog);
                }

                OrderUtil.updateStayInMid(order);
                if (roleUser.isOrgMid()) {
                    order.setModifiedMidTime(new Date());
                } else if (roleUser.isOrgFront()) {
                    order.setModifiedFrontTime(new Date());
                }

                // 如果确认号有效，则要将其他传真确认号设置为无效
                if (bValidConfirm && 1 < faxList.size()) {
                    break;
                }

                // v2.8.1 增加操作人 add by chenkeming
                order.setModifier(roleUser.getLoginName());
                order.setModifierName(roleUser.getName());

                saveOrUpdate(order);
                return order;
            }
        }

        // 如果上面新的确认号有效，则要将目前其他有效的传真确认号设置为无效
        if (bValidConfirm) {
            for (int i = 0; i < faxList.size(); i++) {
                OrOrderFax fax = (OrOrderFax) faxList.get(i);
                if (fax.isValidConfirm() && fax.getID().longValue() != faxItemId) {
                    fax.setValidConfirm(false);
                    saveOrUpdate(order);
                    return order;
                }
            }
        }

        return null;
    }

    /**
     * 呼出配额
     * 
     * @param orderId
     * @param roomItemId
     * @param basePrice
     * @param salePrice
     * @param roleUser
     * @return
     */
    public OrOrder callRoom(long orderId, long roomItemId, double basePrice, double salePrice,
        UserWrapper roleUser) {

        OrOrder order = this.getCustomOrder(orderId, new String[] { "orderItems" });

        int nNotFill = 0;
        boolean bFound = false;
        List orderItems = order.getOrderItems();
        for (int i = 0; i < orderItems.size(); i++) {
            OrOrderItem orderItem = (OrOrderItem) orderItems.get(i);
            if (!orderItem.isConfirm()) {
                nNotFill++;
                if (bFound) {
                    return order;
                }
            }
            if (orderItem.getID().longValue() == roomItemId) {
                bFound = true;

                orderItem.getBasePrice();
                double oldSalePrice = orderItem.getSalePrice() + orderItem.getFavourableAmount();
                
                //总金额的价格变化
                double differentPrice = salePrice - oldSalePrice;
                //应付金额的价格变化
                double differentPriceForPay = differentPrice;
                
                /* RMS2983 香港组支付币种紧急需求,当预付变价,如果支付币种非人民币,支付币种和订单合同币种不相同 add by chenjiajie 2009-11-25 begin  */
                if(order.isPrepayOrder()
                        && !CurrencyBean.RMB.equals(order.getActualPayCurrency())
                        && !order.getActualPayCurrency().equals(order.getPaymentCurrency())){
                    //这种情况下需要换算成支付币种，再相减
                    String rateStr = CurrencyBean.rateMap.get(order.getActualPayCurrency());
                    double rate = StringUtil.isValidStr(rateStr) ? Double.parseDouble(rateStr) : 1D;
                    differentPrice = BigDecimal.valueOf(differentPrice / rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    differentPriceForPay = BigDecimal.valueOf(differentPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    order.setSum(order.getSum() + differentPrice);
                    order.setSumRmb(order.getSumRmb() + differentPriceForPay);
                }
                //RMS2983 香港组支付币种紧急需求,当预付变价,如果支付币种是人民币,支付币种和订单合同币种不相同
                else if(order.isPrepayOrder()
                        && CurrencyBean.RMB.equals(order.getActualPayCurrency())
                        && !order.getActualPayCurrency().equals(order.getPaymentCurrency())){
                    differentPriceForPay = BigDecimal.valueOf(differentPrice).multiply(
                            BigDecimal.valueOf(order.getRateId()))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    //放在differentPriceForPay后计算，页面显示转换汇率后的RMB总金额 因为getSum()存放的是原币种价格，这里需要加上差价后乘以汇率
                    
                    //begin modify by xuyiwen 2012/4/26 此处differentPrice 不应该再乘以汇率
                    differentPrice = BigDecimal.valueOf(order.getSum() + differentPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    // end modify by xuyiwen 2012/4/26 此处differentPrice 不应该再乘以汇率
                    
                    order.setSum(differentPrice);
                    order.setSumRmb(order.getSumRmb() + differentPriceForPay);
                }
                //RMS2983 香港组支付币种紧急需求,当预付变价,如果支付币种非人民币,支付币种和订单合同币种相同
                else if(order.isPrepayOrder()
                        && !CurrencyBean.RMB.equals(order.getActualPayCurrency())
                        && order.getActualPayCurrency().equals(order.getPaymentCurrency())){
                    
                    order.setSum(order.getSum() + differentPrice);
                    order.setSumRmb(order.getSumRmb() + differentPriceForPay);
                }
                /* RMS2983 香港组支付币种紧急需求,当预付变价,如果支付币种非人民币,支付币种和订单合同币种不相同 add by chenjiajie 2009-11-25 end  */
                //原有的逻辑 需要乘以汇率 换算成RMB
                else{
                    differentPriceForPay = BigDecimal.valueOf(differentPrice).multiply(
                            BigDecimal.valueOf(order.getRateId()))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    
                    order.setSum(order.getSum() + differentPrice);
                    order.setSumRmb(order.getSumRmb() + differentPriceForPay);
                }

                orderItem.setBasePrice(basePrice);
                orderItem.setSalePrice(salePrice);
                /**B2B代理及其手工单 佣金价变价处理  add by xiaowei.wang 2011.04.12  begin*/
                //代理打电话到cc下单，判断当前登录会员是否为一开代理
                if("9400100001".equals(order.getMemberAliasId())){
                	this.changePrice(salePrice,basePrice,order,orderItem);
                }else if(null!=order.getAgentCode() && this.isb2bMember(order.getAgentCode())){
                	this.changePrice(salePrice,basePrice,order,orderItem);
                }
                /**B2B代理及其手工单 变价 佣金 add by xiaowei.wang 2011.04.12  end*/
                if (null == orderItem.getQuotaPattern()
                    || !orderItem.getQuotaPattern().equals("C-I")) {
                    orderItem.setQuantity(1);
                    orderItem.setQuotaType(QuotaType.CALLQUOTA);
                }
                orderItem.setConfirm(true);

                StringBuffer strCmp = new StringBuffer();
                strCmp.append("呼出配额,房间日期:" + DateUtil.formatDateToMMDD(orderItem.getNight())
                    + ",房间号:" + (null == orderItem.getRoomNo() ? "无" : orderItem.getRoomNo())
                    + ",历史序号:"
                    + (0 == orderItem.getHisNo().intValue() ? "原单" : orderItem.getHisNo())
                    + ",底价:<font color='red'>" + basePrice + "</font>,销售价:<font color='red'>"
                    + salePrice + "</font>");
                strCmp.append("<br>");
                OrHandleLog handleLog = new OrHandleLog();
                // handleLog.setModifier(new Long(roleUser.getId()));
                handleLog.setModifierName(roleUser.getName());
                handleLog.setModifierRole(roleUser.getLoginName()); // 保存工号
                handleLog.setBeforeState(order.getOrderState());
                handleLog.setAfterState(order.getOrderState());
                handleLog.setContent(strCmp.toString());
                handleLog.setModifiedTime(new Date());
                handleLog.setHisNo(order.getHisNo());
                handleLog.setOrder(order);
                order.getLogList().add(handleLog);

                Date modifiedTime = new Date();
                order.setModifiedMidTime(modifiedTime);
                order.setModifiedTime(modifiedTime);
                order.setModifier(roleUser.getLoginName());
                order.setModifierName(roleUser.getName());

                nNotFill--;
                if (0 < nNotFill) {
                    saveOrUpdate(order);
                    return order;
                }
            }
        }
        if (!bFound) {
            return null;
        }

        StringBuffer strCmp = new StringBuffer();
        strCmp.append("订单配额状态改为:<font color='red'>").append("已满足配额").append("</font>");
        strCmp.append("<br>");
        OrHandleLog handleLog = new OrHandleLog();
        // handleLog.setModifier(new Long(roleUser.getId()));
        handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName()); // 保存工号
        handleLog.setBeforeState(order.getOrderState());
        handleLog.setAfterState(order.getOrderState());
        handleLog.setContent(strCmp.toString());
        handleLog.setModifiedTime(new Date());
        handleLog.setHisNo(order.getHisNo());
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);
        order.setQuotaOk(true);

        OrderUtil.updateStayInMid(order);

        Date modifiedTime = new Date();
        order.setModifiedMidTime(modifiedTime);
        order.setModifiedTime(modifiedTime);
        order.setModifier(roleUser.getLoginName());
        order.setModifierName(roleUser.getName());

        saveOrUpdate(order);
        return order;
    }

    // add update haibo.li 2008-12-08 重新分配撤单原因
    public String getGuestCancelMessage(int guestCancelMsgId) {
        String guestCancelMessage = "";
        switch (guestCancelMsgId) {
        case 1:
            guestCancelMessage = "againNewOrder_guestReason";
            break;
        case 2:
            guestCancelMessage = "againNewOrder_hotelReason";
            break;
        case 3:
            guestCancelMessage = "againNewOrder_mangoReason";
            break;
        case 4:
            guestCancelMessage = "cancelOrder_guestReason";
            break;
        case 5:
            guestCancelMessage = "cancelOrder_hotelReason";
            break;
        case 6:
            guestCancelMessage = "cancelOrder_mangoReason";
            break;
        case 88:
            guestCancelMessage = "cancelOrder_channelReason";
            break;
        // case 23:
        // guestCancelMessage = "cancelOrder_mangoReason";
        // break;
        // case 24:
        // guestCancelMessage = "mango_subjectiveReason";
        // break;
        // case 25:
        // guestCancelMessage = "hotel_subjectiveReason";
        // break;
        default:
            guestCancelMessage = "";
            break;
        }

        return guestCancelMessage;
    }

    /**
     * 变价
     * 
     * @param orderId
     * @param roomItemId
     * @param basePrice
     * @param salePrice
     * @param roleUser
     * @return
     */
    public OrOrder changeRoomPrice(long orderId, long roomItemId, double basePrice,
        double salePrice, UserWrapper roleUser) {

        OrOrder order = this.getCustomOrder(orderId, new String[] { "orderItems" });
        boolean bFound = false;
        List orderItems = order.getOrderItems();
        OrOrderItem orderItem = null;
        double oldBasePrice = 0.0, oldSalePrice = 0.0;
        for (int i = 0; i < orderItems.size(); i++) {
            orderItem = (OrOrderItem) orderItems.get(i);
            if (orderItem.getID().longValue() == roomItemId) {
                bFound = true;

                oldBasePrice = orderItem.getBasePrice();
                oldSalePrice = orderItem.getSalePrice() + orderItem.getFavourableAmount();
                //总金额的价格变化
                double differentPrice = salePrice - oldSalePrice;
                //应付金额的价格变化
                double differentPriceForPay = differentPrice;
                
                /* RMS2983 香港组支付币种紧急需求,当预付变价,如果支付币种非人民币,支付币种和订单合同币种不相同 add by chenjiajie 2009-11-25 begin  */
                if(order.isPrepayOrder()
                        && !CurrencyBean.RMB.equals(order.getActualPayCurrency())
                        && !order.getActualPayCurrency().equals(order.getPaymentCurrency())){
                    //这种情况下需要换算成支付币种，再相减
                    String rateStr = CurrencyBean.rateMap.get(order.getActualPayCurrency());
                    double rate = StringUtil.isValidStr(rateStr) ? Double.parseDouble(rateStr) : 1D;
                    differentPrice = BigDecimal.valueOf(differentPrice / rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    differentPriceForPay = BigDecimal.valueOf(differentPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    order.setSum(order.getSum() + differentPrice);
                    order.setSumRmb(order.getSumRmb() + differentPriceForPay);
                }
                //RMS2983 香港组支付币种紧急需求,当预付变价,如果支付币种是人民币,支付币种和订单合同币种不相同
                else if(order.isPrepayOrder()
                        && CurrencyBean.RMB.equals(order.getActualPayCurrency())
                        && !order.getActualPayCurrency().equals(order.getPaymentCurrency())){
                    differentPriceForPay = BigDecimal.valueOf(differentPrice).multiply(
                            BigDecimal.valueOf(order.getRateId()))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    //放在differentPriceForPay后计算，页面显示转换汇率后的RMB总金额 因为getSum()存放的是原币种价格，这里需要加上差价后乘以汇率
                    
                    //begin modify by xuyiwen 2012/4/26 此处differentPrice 不应该再乘以汇率
                    differentPrice = BigDecimal.valueOf(order.getSum() + differentPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    //end modify by xuyiwen 2012/4/26 此处differentPrice 不应该再乘以汇率
                    
                    order.setSum(differentPrice);
                    order.setSumRmb(order.getSumRmb() + differentPriceForPay);
                }
                //RMS2983 香港组支付币种紧急需求,当预付变价,如果支付币种非人民币,支付币种和订单合同币种相同
                else if(order.isPrepayOrder()
                        && !CurrencyBean.RMB.equals(order.getActualPayCurrency())
                        && order.getActualPayCurrency().equals(order.getPaymentCurrency())){
                    
                    order.setSum(order.getSum() + differentPrice);
                    order.setSumRmb(order.getSumRmb() + differentPriceForPay);
                }
                /* RMS2983 香港组支付币种紧急需求,当预付变价,如果支付币种非人民币,支付币种和订单合同币种不相同 add by chenjiajie 2009-11-25 end  */
                //原有的逻辑 需要乘以汇率 换算成RMB
                else{
                    differentPriceForPay = BigDecimal.valueOf(differentPrice).multiply(
                            BigDecimal.valueOf(order.getRateId()))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    
                    order.setSum(order.getSum() + differentPrice);
                    order.setSumRmb(order.getSumRmb() + differentPriceForPay);
                }

                orderItem.setBasePrice(basePrice);
                orderItem.setSalePrice(salePrice);
                /**B2B代理及其手工单 佣金价变价处理  add by xiaowei.wang 2011.04.12  begin*/
                //代理打电话到cc下单，判断当前登录会员是否为一开代理
                if("9400100001".equals(order.getMemberAliasId())){
                	this.changePrice(salePrice,basePrice,order,orderItem);
                }else if(null!=order.getAgentCode()){
                	this.changePrice(salePrice,basePrice,order,orderItem);
                }
                /**B2B代理及其手工单 变价 佣金 add by xiaowei.wang 2011.04.12  end*/
                break;
            }
        }
        if (!bFound) {
            return null;
        }

        StringBuffer strCmp = new StringBuffer();
        strCmp.append("订单配额变价,房间日期:" + DateUtil.formatDateToMMDD(orderItem.getNight()) + ",房间号:"
            + (null == orderItem.getRoomNo() ? "无" : orderItem.getRoomNo()) + ",历史序号:"
            + (0 == orderItem.getHisNo().intValue() ? "原单" : orderItem.getHisNo()) + ",底价由:"
            + oldBasePrice + "改为:<font color='red'>" + basePrice + "</font>,销售价由:" + oldSalePrice
            + "改为:<font color='red'>" + salePrice + "</font>");
        strCmp.append("<br>");
        if (order.isCustomerConfirm()) {
            order.setCustomerConfirm(false);
            strCmp.append("订单改为:<font color='red'>未确认客户</font><br>");
        }

        OrHandleLog handleLog = new OrHandleLog();
        // handleLog.setModifier(new Long(roleUser.getId()));
        handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName()); // 保存工号
        handleLog.setBeforeState(order.getOrderState());
        handleLog.setAfterState(order.getOrderState());
        handleLog.setContent(strCmp.toString());
        handleLog.setModifiedTime(new Date());
        handleLog.setHisNo(order.getHisNo());
        handleLog.setOrder(order);
        order.getLogList().add(handleLog);

        OrderUtil.updateStayInMid(order);
        Date modifiedTime = new Date();
        order.setModifiedMidTime(modifiedTime);
        order.setModifiedTime(modifiedTime);
        order.setModifier(roleUser.getLoginName());
        order.setModifierName(roleUser.getName());

        saveOrUpdate(order);
        return order;
    }
   /**
    * B2B代理及其手工单 变价 佣金 add by xiaowei.wang 2011.04.12 
    * @param salePrice
    * @param basePrice
    * @param order
    * @param orderItem
    */
    private void changePrice(double salePrice,double basePrice,OrOrder order,OrOrderItem orderItem){
    	orderItem.setAgentReadComissionPrice(salePrice);
            int intStar = Math.round(order.getHotelStar());
        	double readRate = commissionService.getCommissionRate(salePrice, orderItem.getCommission(), order.getAgentCode(), String.valueOf(intStar),order.getPaymentCurrency(), order.getHotelId(), order.getRoomTypeId(), 
        			order.getChildRoomTypeId(), order.getPayMethod(),order.getCheckinDate());
        	orderItem.setAgentReadComissionRate(readRate);
        orderItem.setAgentReadComission(salePrice*orderItem.getAgentReadComissionRate());
    }
    /**
     * 给配送提供获取订单对象
     * 
     * @param orderCD
     * @return
     */
    public OrOrderVO getHotelOrderByOrderCD(final String orderCD) {
        OrOrder order = orOrderDao.getCustomOrderByCD(orderCD);

        OrOrderVO orderVO = new OrOrderVO();
        try {
            BeanUtils.copyProperties(orderVO, order);
            orderVO.setPaymentList(MyBeanUtil.copyCollection(order.getPaymentList(),
                PaymentVO.class));
            orderVO.setFulfill(order.getFulfill());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return null;
        }

        // 设置orderVO的hotelConfirmNo
        List faxList = order.getFaxList();
        for (int i = 0; i < faxList.size(); i++) {
            OrOrderFax faxItem = (OrOrderFax) faxList.get(i);
            if (faxItem.getType() == MemberConfirmType.CONFIRM && faxItem.isConfirm()) {
                orderVO.setHotelConfirmNo(faxItem.getConfirmNo());
                break;
            }
        }

        return orderVO;
    }

    /**
     * 获取发邮件订单对象
     * 
     * @param orderID
     * @return
     */
    public OrOrder getCustomOrderForMail(Long orderID) {
        return orOrderDao.getCustomOrderByID(orderID);
    }

    /**
     * 判断是否系统配额
     * 
     * @param order
     * @return
     */
    public boolean getIsSystemQuota(OrOrder order) {
        List orderItems = order.getOrderItems();
        for (int i = 0; i < orderItems.size(); i++) {
            OrOrderItem orderItem = (OrOrderItem) orderItems.get(i);
            if (0 >= orderItem.getQuantity()) {
                // 如果orderItem的短日期不是订单的checkin day的话，而且orderItem的配额类型是进店模式的话，
                // 认为是系统内配额，返回true add by baofeng.si V2.3 2008-6-6
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
                if (!df.format(orderItem.getNight()).equals(df.format(order.getCheckinDate()))
                    && orderItem.getQuotaPattern().equals("C-I")) {
                    return true;
                }// add by baofeng.si V2.3 2008-6-6
                return false;
            } else {
                if (orderItem.getQuotaType().equals(QuotaType.CALLQUOTA)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 预订前验证
     * 
     * @param order
     * @return 是否验证成功的字符串信息, 空串表示成功
     */
    public String checkBeforeOrder(OrOrder order) {

        return "";
    }

    /**
     * 计算订单总金额<br>
     * 
     * TODO: 考虑币种
     * 
     * @param order
     */
    public void calculateTotalAmount(OrOrder order) {
       List<OrOrderItem> roomItems = order.getOrderItems();
        List<OrPriceDetail> priceLis = order.getPriceList();
    	double amount = 0.0;
        for (int m = 0; m < roomItems.size(); m++) {
            OrOrderItem item = (OrOrderItem) roomItems.get(m);
            
            // V2.9.3.1 优惠立减，订单总金额计算需要加上优惠的金额，如果没有优惠，优惠金额默认是0 modify by chenjiajie 2009-10-19
            amount += item.getSalePrice() + item.getFavourableAmount();

        }
        
        order.setSum(BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_UP).doubleValue());
        
        // V2.9.3.1 优惠立减，优惠立减订单的应付金额需要减去已经换算成RMB的立减的金额 modify by chenjiajie 2009-10-20
        double rmbAmount = BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(order.getRateId()))
        .setScale(2, BigDecimal.ROUND_UP).doubleValue();        
        if(0 < order.getFavourableFlag()){
        	// 订单保存的优惠金额是带币种的，需要转化成人民币
        	rmbAmount -= order.getRmbFavourableAmount();
        }
        
        /* 香港组紧急需求(预付)，订单可以使用港币支付，当支付币种使用了港币，订单总金额和应付金额需要用人民币除以币种 add by chenjiajie 2009-11-24 begin */
        if(StringUtil.isValidStr(order.getActualPayCurrency()) 
                && CurrencyBean.HKD.equals(order.getActualPayCurrency())
                && order.isPrepayOrder()){
            
            String rateStr = CurrencyBean.rateMap.get(CurrencyBean.HKD);
            double rateHKD = 1D;
            if(StringUtil.isValidStr(rateStr)){
                rateHKD = Double.parseDouble(rateStr);
            }
            //订单的合同币种是RMB
            if(CurrencyBean.RMB.equals(order.getPaymentCurrency())){
                //总金额需要除以汇率
                double hkAmount = BigDecimal.valueOf(amount / rateHKD).setScale(2, BigDecimal.ROUND_UP).doubleValue();
                order.setSum(hkAmount);

                //应付金额
                rmbAmount = BigDecimal.valueOf(rmbAmount / rateHKD).setScale(2, BigDecimal.ROUND_UP).doubleValue();
            }
            //订单的合同币种非RMB,总金额不需要汇率处理
            else{
                //用原币种总金额 - 原币种立减金额(界面传入的金额已经乘以房间数量)
                double amountForPay = order.getSum() - order.getFavourableAmount();
                //应付金额
                rmbAmount = BigDecimal.valueOf(amountForPay).setScale(2, BigDecimal.ROUND_UP).doubleValue();
            }
        }
        /* 香港组紧急需求(预付)，订单可以使用港币支付，当支付币种使用了港币，订单总金额和应付金额需要用人民币除以币种 add by chenjiajie 2009-11-24 end */
        
        order.setSumRmb(rmbAmount);
    }

    /**
     * 根据预订信息计算预订1间时的金额 TODO: 改为调用本部接口
     * 
     * @param order
     */
    public double calcAmount(ReservationInfo reserv) {
        double amount = 0.0;

        // List roomItems = reserv.getRoomItems();
        // Date prevDate = null;
        // Date curDate = null;
        // for (int m = 0; m < roomItems.size(); m++) {
        // RoomItem item = (RoomItem) roomItems.get(m);
        // curDate = item.getSaleDate();
        // if(prevDate != null && DateUtil.compare(prevDate, curDate) == 0) {
        // continue;
        // }
        // amount += item.getSalePrice();
        // prevDate = curDate;
        // }

        return amount;
    }

    /**
     * 根据预订信息计算订单担保金额<br>
     * 包括新建订单和修改订单两种情况
     * 
     * @param reserv
     */
    private double calculateSuretyPrice(ReservationInfo reserv, OrOrder order) {
        if (!reserv.isNeedCredit()) {
            return 0.0;
        }
        double amount = 0.0;

        if (null == order.getID() || 0 == order.getID().longValue()) { // 新建订单
            if (reserv.getSuretyType() == GuaranteeType.ALL_DAY) { // 全额担保
                return order.getSumRmb();
            }
            List<Object[]> list = reserv.getSuretyDetails();
            amount = order.getSum() * list.size();
            /** v2.4.2 外测TD84提出的bug:新建订单全额担保没有精确两位小数 by chenjiajie@2009-01-08 begin **/
            amount = BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            /** v2.4.2 外测TD84提出的bug:新建订单全额担保没有精确两位小数 by chenjiajie@2009-01-08 end **/
        } else { // 修改订单 modify by chenkeming@2008.12.01 V2.4.1 修改订单担保金额计算逻辑
            if (reserv.getSuretyType() == GuaranteeType.ALL_DAY) { // 全额担保
                amount = order.getSumRmb() / order.getRoomQuantity();
                amount = BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
                return amount;
            }
            List<Object[]> list = reserv.getSuretyDetails();
            Date checkInDate = order.getCheckinDate();
            int quantity = order.getRoomQuantity();
            double perPrice = order.getSumRmb() / quantity
                / DateUtil.getDay(checkInDate, order.getCheckoutDate());
            amount = 0;
            List<OrOrderItem> items = order.getOrderItems();
            int nSize = items.size();
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = list.get(i);
                int nIndex = DateUtil.getDay(checkInDate, (Date) (obj[0])) * quantity;
                if (nSize > nIndex) {
                    amount += items.get(nIndex).getSalePrice();
                } else {
                    amount += perPrice;
                }
            }
            amount = BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return amount;
    }

    /**
     * 计算支付总金额
     * 
     * @param order
     */
    public void calculateTotalPayAmount(OrOrder order) {
        List payments = order.getPaymentList();

        double amount = 0.0;
        for (int m = 0; m < payments.size(); m++) {
            OrPayment item = (OrPayment) payments.get(m);
            amount += item.getMoney();
        }

        order.setPrepayTotalRmb(amount);
    }

    /**
     * 复制订单<br>
     * TODO: 未完成
     * 
     * @param newOrder
     * @param sourceOrderId
     * @param isCopyPayInfo
     *            是否要COPY支付和信用卡担保信息
     */
    public void copyOrder(OrOrder newOrder, String sourceOrderId, boolean isCopyPayInfo) {
        OrOrder order = this.getOrder(Long.valueOf(sourceOrderId));

        MyBeanUtil.copyProperties(newOrder, order);

        /**
         * 以下是要拷贝集合
         */

        List fellowList = MyBeanUtil.copyCollection(order.getFellowList(), FellowInfo.class);
        newOrder.setFellowList(fellowList);

    }

    public BookRoomCondition getBookRoomCondition(OrOrder order) {
        return getBookRoomCondition(order, order.getCheckinDate(), order.getCheckoutDate(), order
            .getRoomQuantity());
    }

    /**
     * 当修改订单的入住时间和退房日期时及修改入住间夜数时，需要重新向酒店请求，获取价格和配额
     * 
     * @param order
     * @param startDate
     * @param endDate
     * @param roomNum
     * @return
     */
    public BookRoomCondition getBookRoomCondition(OrOrder order, Date startDate, Date endDate,
        int roomNum) {
        BookRoomCondition condition = new BookRoomCondition();

        condition.setCheckinDate(startDate);
        condition.setCheckoutDate(endDate);

        condition.setRoomTypeId(order.getRoomTypeId());
        condition.setRoomNum(roomNum);
        condition.setChildRoomTypeId(order.getChildRoomTypeId());
        condition.setHotelId(order.getHotelId());
        if (null != order.getMemberId()) {
            condition.setMemberId(order.getMemberId());
        }

        condition.setPayMethod(order.getPayMethod());

        condition.setQuotaType("");

        return condition;
    }

//    /**
//     * 判断是否要取消担保，还是需要进行担保
//     * 
//     * @param order
//     * @param reserv
//     * @return
//     */
//    private String getGuaranteeState(OrOrder order, ReservationInfo reserv) {
//
//        // String oldState = order.getSuretyState();
//        //
//        // boolean isNeedGurantee = reserv.isNeedCredit();
//        //
//        // /**
//        // * 最晚担保
//        // */
//        // if (isNeedGurantee
//        // && GuaranteeType.LATE_CHECK_IN.equals(reserv.getCreditType())) {
//        // String lateCheckInTime = order.getLinkman().getLateArriveTime();
//        //
//        // /**
//        // * 如果是最晚担保，但订单的最晚入住时间又符合规定，则不需要担保
//        // */
//        // if (lateCheckInTime.compareTo(reserv.getLatestCheckInTime()) <= 0) {
//        // isNeedGurantee = false;
//        // }
//        // }
//        //
//        // if (isNeedGurantee) {
//        //
//        // /**
//        // * 如果原来没有担保成功，则需要担保
//        // */
//        // if (!GuaranteeState.CREDTED.equals(oldState))
//        // return GuaranteeState.NEED_CREDIT;
//        // } else {
//        // /**
//        // * 如果已经担保成功，要取消担保
//        // */
//        // if (GuaranteeState.CREDTED.equals(oldState)
//        // || GuaranteeState.NEED_CREDIT.equals(oldState))
//        // return GuaranteeState.NEED_CANCEL;
//        // }
//        //
//        return null;
//    }

    /**
     * 如果是积分支付，且没有扣款，就要扣除积分！<br>
     * 扣除成功后，对于已支付总金额，需要重新再计算一次。
     * 
     * @param order
     */
    public void deductScore(OrOrder order) {
        // List payments = order.getPaymentList();
        //
        // for (int m = 0; m < payments.size(); m++) {
        // OrPayment payment = (OrPayment) payments.get(m);
        //
        // if (PrepayType.Points.equals(payment.getPayType())
        // && PayState.NO_PAY.equals(payment.getState())) {
        // double score = memberService.deductScore(""
        // + order.getMemberId(), payment.getFundMoney());
        //
        // payment.setState(PayState.HAVE_PAY);
        // }
        // }
        // /**
        // * 计算总支付金额
        // */
        // this.calculateTotalPayAmount(order);

        return;
    }

    /**
     * 将预订结果信息填充到订单当中
     * 
     * @param order
     * @param reserv
     */
    public void populateOrder(OrOrder order, ReservationInfo reserv) {

        boolean bReservNull = false;
        OrReservation orReserv = order.getReservation();
        if (null == orReserv) {
            orReserv = new OrReservation();
            bReservNull = true;
        }

        if (order.isPayToPrepay()) {
            order.setPayMethod(PayMethod.PRE_PAY);
        }

        // 获取面付的信用卡担保信息
        if (!order.isPrepayOrder() && !order.isPayToPrepay()) {
            // 计算担保金额
            if (reserv.isNeedCredit()) {
                orReserv.setNeedCredit(true);
                orReserv.setReservSuretyPrice(calculateSuretyPrice(reserv, order));
            }
        } else { // 预付单(或者面付转预付单)的相关条款
            order.setPrepayTotalRmb(order.getSumRmb());
            order.setSum(order.getSumRmb());
        }

        if (bReservNull) {
            order.setReservation(orReserv);
        }
    }

    /**
     * hotel2.6 填充订单担保或者预付金额 add by zhineng.zhuang 2009-02-12
     * 
     * @param order
     * @param reserv
     */
    public void pupulateOrderPrice(OrOrder order, ReservationAssist reservationAssist) {

        OrReservation orReserv = order.getReservation();
        if (null == orReserv) {
            orReserv = new OrReservation();
        }

        // 获取面付的信用卡担保信息
        if (!order.isPrepayOrder() && !order.isPayToPrepay()) {
            // 计算担保金额
            if (orReserv.isNeedCredit()) {
                orReserv.setReservSuretyPrice(calculatePriceService.getcaluclateVouchPrice(
                    reservationAssist, order));
            }
        } else { // 预付单(或者面付转预付单)的相关条款
            order.setPrepayTotalRmb(order.getSumRmb());
            if(order.getRateId()>0)order.setSum(order.getSumRmb()/order.getRateId());
            else order.setSum(order.getSumRmb());
        }

        order.setReservation(orReserv);
    }

    /**
     * hotel2.6 网站：取出预订条款，并计算订单担保或者预付金额 add by zhineng.zhuang 2009-02-12
     * 
     * @param order
     */
    public ReservationAssist loadBookClauseForWeb(OrOrder order) {
        BookRoomCondition bookRoomCond = this.getBookRoomCondition(order);
        // 把预订条款取出
        ReservationAssist reservationAssist = hotelReservationInfoService.getReservationInfoForWeb(bookRoomCond,
            order);
        // 计算并填充订单担保或者预付金额
        this.pupulateOrderPrice(order, reservationAssist);
        return reservationAssist;
    }

    /**
     * 获得预订传真号
     * 
     * @param hotelId
     * @param date
     * @return
     */
    public String getReservationFaxNo(String hotelId, Date date) {
        return "3681031";
    }

    /**
     * 取消订单
     * 
     * @param orderCD
     */
    public void cancelOrderByOrderCD(String orderCD, int cancelReason, String cancelMessage,
        String guestCancelMessage, UserWrapper roleUser) {
        OrOrder order = getCustomOrderByOrderCD(orderCD, null);
        order.setCancelReason(cancelReason);
        order.setCancelMessage(cancelMessage);
        order.setGuestCancelMessage(guestCancelMessage);

        cancelOrder(order, cancelReason, cancelMessage, guestCancelMessage, roleUser);
    }

    /**
     * 取消订单
     * 
     * @param order
     */
    public void cancelOrder(OrOrder order, int cancelReason, String cancelMessage,
        String guestCancelMessage, UserWrapper roleUser) {
        if (order.getOrderState() != OrderState.NOT_SUBMIT
            && StringUtil.isValidStr(order.getQuotaTypeOld())) {
            // 退配额
            List<OrOrderItem> orderItems = order.getOrderItems();
            if(null != orderItems&&0 < orderItems.size()){
            	List<QuotaReturn> quotaReturnList = new ArrayList<QuotaReturn>();
    	        for(OrOrderItem orderItem : orderItems){
    	        	QuotaReturn quotaReturn = new QuotaReturn();
    	        	quotaReturn.setHotelId(order.getHotelId());
    	        	quotaReturn.setRoomTypeId(order.getRoomTypeId());
    	        	quotaReturn.setBedId(order.getBedType());
    	        	quotaReturn.setChildRoomTypeId(order.getChildRoomTypeId());
    	        	quotaReturn.setQuotaNum(1);
    	        	quotaReturn.setUseQuotaDate(orderItem.getNight());
    	        	quotaReturn.setQuotaDate(orderItem.getNight());
    	        	quotaReturn.setPayMethod(order.isPayToPrepay() ? PayMethod.PAY : order.getPayMethod());
    	        	// CC的memberType为1
    	        	quotaReturn.setMemberType(1);
    	        	// 设置配额类型
    	        	if(null == orderItem.getQuotaPattern()){
    	        		quotaReturn.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNSI);
    	        	}else{
    	        		quotaReturn.setQuotaPattern(orderItem.getQuotaPattern());
    	        	}
    	        	// 设置配额类型
    	        	if(null == orderItem.getQuotaType()){
    	        		quotaReturn.setQuotaType(HotelBaseConstantBean.GENERALQUOTA);
    	        	}else{
    	        		quotaReturn.setQuotaType(orderItem.getQuotaType());
    	        	}
    	        	// 设置配额共享方式
    	        	if(null == orderItem.getQuotashare()){
    	        		quotaReturn.setQuotaShare(new Long(HotelBaseConstantBean.QUOTASHARETYPE));
    	        	}else{
    	        		quotaReturn.setQuotaShare(new Long(orderItem.getQuotashare()));
    	        	}
    	        	if(orderItem.getIsConfirm()==false){
    	        		//配额不可退
    	        		quotaReturn.setSign(1);
    	        	}else{
    	        		//配额可退
    	        		quotaReturn.setSign(0);
    	        	}
    	        	quotaReturn.setOperatorName(roleUser.getName());
    	        	quotaReturn.setOperatorId(roleUser.getLoginName());
    	        	
    	        	quotaReturnList.add(quotaReturn);
    	        }
    	        if(!order.isCtsHK()) {
    	        	quotaControl.returnQuota(quotaReturnList);
    	        }
            }
        }

        // 如果预付单状态为已付款，则需要进行退款申请
        // if(order.isNeedHandleRefund()) {
        // order.setOrderState(OrderState.HAS_CREATE_REFUND);
        // order.setRefund(order.getSumRmb());
        // order.setHasRefund(false);
        // }

        orderAssist.getCancelLog(order, cancelReason, cancelMessage, guestCancelMessage, roleUser);

        OrderUtil.updateStayInMid(order);

        saveOrUpdate(order);
    }
    
    /**
     * 处理异常订单（直联和中旅订单提交时出现异常，不直接撤单，而是保存订单，记录日志，然后由人工跟进订单）
     */
    public void handleExceptionOrder(OrOrder order,int exceptionType,String message,UserWrapper roleUser) {
    	
    	orderAssist.handleExceptionLog(order, exceptionType,message, roleUser);
    	
    	saveOrUpdate(order);
    }

    /**
     * 获取订单配额, 如果获取成功则同时填充orderItems
     * 
     * @param order
     * @return true:获取配额成功, false:没有全部获取到配额
     */
    public boolean deductOrderQuota(OrOrder order, int[] breakfast, int[] breakfastNum,
        String quotaType) {

        // 调用本部扣配额接口
        QuotaQuery quotaQuery = new QuotaQuery();
        quotaQuery.setQuotaType(quotaType);
        quotaQuery.setRoomTypeId(order.getRoomTypeId());
        quotaQuery.setChildRoomId(order.getChildRoomTypeId());
        quotaQuery.setHotelId(order.getHotelId());
        quotaQuery.setPayMethod(order.isPayToPrepay() ? PayMethod.PAY : order.getPayMethod());
        quotaQuery.setMemberType(QuotaMemberType.CC);
        quotaQuery.setBeginDate(order.getCheckinDate());
        quotaQuery.setEndDate(order.getCheckoutDate());
        quotaQuery.setQuotaNum(order.getRoomQuantity());
        quotaQuery.setBedID(order.getBedType());
        quotaQuery.setOperatorName(order.getCreatorName());
        quotaQuery.setChannel(Long.valueOf(order.getChannel()));
        quotaQuery.setOperatorId(order.getCreator());
        List<QuotaReturn> quotas = quotaControl.deductQuota(quotaQuery);

        if(quotas!=null&&quotas.size()>0){
        	log.info("  quotaControl.deductQuota(quotaQuery):"+quotas.size());
        }
        
        if (0 >= quotas.size()) {
            return false;
        }

        int nDays = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());

        // 如果为空则从本部获取早餐信息(网站面付单有这种情况)
        if (null == breakfast) {
            List lstPrice = orOrderDao.queryByNamedQuery("findPriceBizKeyEx", new Object[] {
                order.getHotelId(), order.getRoomTypeId(), order.getChildRoomTypeId(), quotaType,
                order.isPayToPrepay() ? PayMethod.PAY : order.getPayMethod(),
                order.getCheckinDate(), order.getCheckoutDate() });
            if (0 < lstPrice.size()) {
                breakfast = new int[nDays];
                breakfastNum = new int[nDays];
                HtlPrice htlPrice = null;
                Date startDate = order.getCheckinDate();
                int j = 0;
                for (int i = 0; i < nDays; i++) {
                    Date curDate = DateUtil.getDate(startDate, i);
                    if (lstPrice.size() > j) {
                        htlPrice = (HtlPrice) lstPrice.get(j++);
                        if (0 == DateUtil.compare(htlPrice.getAbleSaleDate(),curDate)) {
                            breakfast[i] = Integer.parseInt(htlPrice.getIncBreakfastType());
                            breakfastNum[i] = Integer.parseInt(htlPrice.getIncBreakfastNumber());
                        } else {
                            breakfast[i] = 0;
                            breakfastNum[i] = 0;
                        }
                    } else {
                        breakfast[i] = 0;
                        breakfastNum[i] = 0;
                    }
                }
            }
        }

        // 填充orderItems,这里quotas依次按日期，房间号排序
        boolean bQuotaOk = true;
        Date tonight = null;
        int roomIndex = -1;
        int dayIndex = -1;
        boolean quotaCanReturn = true;
        boolean bSetCanReturn = false;
        String[] sQuotaTypes = new String[order.getRoomQuantity()];
        boolean bPay = !order.isPrepayOrder();
        String[] fellowNamesArr = null;
        
        float hstar = order.getHotelStar();
        if(hstar > 10){
        	String strHotelStar = resourceManager.getDescription("res_hotelStarToNum",Math.round(order.getHotelStar()));
        	hstar = Float.parseFloat(strHotelStar);
        }
        int intStar = Math.round(hstar);
        
        String b2bCD = order.getMemberCd();
        
        int policyScope = orOrderDao.getPolicyScope(b2bCD);
        String payMo = order.getPayMethod();
		if(payMo.equals(PayMethod.PRE_PAY)){
			if(order.isPayToPrepay()){
				payMo = PayMethod.PAY;
			}
		}
		
        
        if (bPay) {
            fellowNamesArr = OrderUtil.fillFellowNamesToOrderItem(order); // 每天各个房间的入住人姓名数组 v2.4.2
                                                                          // chenjiajie 2008-12-30
        }
        for (int i = 0; i < quotas.size(); i++) {
            QuotaReturn returnPo = quotas.get(i);
            OrOrderItem orderItem = new OrOrderItem();

            Date curDate = returnPo.getQuotaDate();
            if (null == tonight || 0 < DateUtil.compare(tonight,curDate)) {
                // 如果是下一天
                tonight = curDate;
                roomIndex = 0;
                dayIndex++;
            } else {
                roomIndex++;
            }
            if (bPay) {
            	if(fellowNamesArr.length>roomIndex){
            		orderItem.setFellowName(fellowNamesArr[roomIndex]); // 把拆分后的入住人姓名封装到orderItem中
                                                                    // V2.4.2 chenjiajie 2008-12-30
            	}
            }
            orderItem.setRoomIndex(roomIndex);
            orderItem.setDayIndex(dayIndex);
            orderItem.setFirstNight(0 == dayIndex ? true : false);
            orderItem.setLastNight(dayIndex >= nDays - 1 ? true : false);
            orderItem.setNight(curDate);

            String returnQuotaType = returnPo.getQuotaType();
            String returnQuotaPattern = returnPo.getQuotaPattern();
            if (null != returnQuotaPattern && returnQuotaPattern.equals("C-I")) { // 进店模式
                if (0 == dayIndex) { // 首天
                    sQuotaTypes[roomIndex] = returnQuotaType;
                }
                if (sQuotaTypes.length>roomIndex &&(null == sQuotaTypes[roomIndex]
                    || sQuotaTypes[roomIndex].equals(QuotaType.CALLQUOTA))) {
                    orderItem.setConfirm(false);
                    bQuotaOk = false;
                } else {
                    orderItem.setConfirm(true);
                }
                if(returnPo.isTakebackQuota()==true){
                	orderItem.setConfirm(true);
                }else{
                	orderItem.setConfirm(false);
                }
                
            } else {
                // 呼出配额一律为获取不成功
                if (null == returnQuotaType || returnQuotaType.equals(QuotaType.CALLQUOTA)) {
                    orderItem.setConfirm(false);
                    bQuotaOk = false;
                } else {
                    int nSign = returnPo.getSign();
                    if (0 != nSign) {
                        bQuotaOk = false;
                    }
                    orderItem.setConfirm(0 == nSign ? true : false);
                }
            }
            
            /**
             * 呼出配额一律设置为FALSE
             */
            if(null == returnQuotaType || returnQuotaType.equals(QuotaType.CALLQUOTA)){
            	 orderItem.setConfirm(false);
            }
            //如果是普通配额或者临时配额或包房配额,配额扣成功,否则就失败
            if(null!=returnQuotaType){
			    if("1".equals(returnQuotaType)||"2".equals(returnQuotaType)||"3".equals(returnQuotaType)){
				    bQuotaOk = true;
			    }else{
				    bQuotaOk = false;
			    }
			}

            orderItem.setQuantity(returnPo.getQuotaNum());
            // orderItem.setQuotaType(returnQuotaType != null ? returnQuotaType
            // : QuotaType.CALLQUOTA);
            orderItem.setQuotaType(returnQuotaType);
            orderItem.setBasePrice(returnPo.getBasePrice());
            orderItem.setSalePrice(returnPo.getSalePrice());
            orderItem.setMarketPrice(returnPo.getSalesroomPrice());
            orderItem.setRoomState(returnPo.getRoomState());
            orderItem.setQuotaPattern(returnPo.getQuotaPattern());
            orderItem.setQuotaholder("CC");
            orderItem.setQuotaType(returnPo.getQuotaType());
            orderItem.setQuotashare(returnPo.getQuotaShare());
            orderItem.setHotelId(order.getHotelId());
            
            //如果是 B2B 代理过来的订单,则得把代理佣金填充到orderItem里面 add by shengwei.zuo 2010-1-13
            if(order.getType()==OrderType.TYPE_B2BAGENT){
            	 B2BAgentCommUtils B2BAgentCommUtilsInfo  = commissionService.getB2BCommInfo(ChannelType.CHANNEL_ELONG==order.getChannel() ,order.getPaymentCurrency(),orderItem.getNight(), order.getHotelId(),order.getRoomTypeId(),order.getChildRoomTypeId(),payMo,String.valueOf(intStar),b2bCD);

                if(B2BAgentCommUtilsInfo!=null){
                	log.info("OrderService  deductOrderQuota B2BAgentCommUtilsInfo : "+
                            " agentComission  "+ B2BAgentCommUtilsInfo.getAgentComission() +","+
                            " agentComissionPrice" + B2BAgentCommUtilsInfo.getAgentComissionPrice()+","+
                            " agentComissionRate" + B2BAgentCommUtilsInfo.getAgentComissionRate() + ","+
                            " comissionType"+ B2BAgentCommUtilsInfo.getComissionType() + ","+
                            " comissionTypeValue" + B2BAgentCommUtilsInfo.getComissionTypeValue());
                	
                	orderItem.setAgentComission(B2BAgentCommUtilsInfo.getAgentComission());
                    orderItem.setAgentComissionPrice(B2BAgentCommUtilsInfo.getAgentComissionPrice());
                    orderItem.setAgentComissionRate(B2BAgentCommUtilsInfo.getAgentComissionRate());
                    orderItem.setComissionType(B2BAgentCommUtilsInfo.getComissionType());
                    orderItem.setComissionTypeValue(B2BAgentCommUtilsInfo.getComissionTypeValue());
                    orderItem.setAgentReadComission(B2BAgentCommUtilsInfo.getAgentReadComission());
                    orderItem.setAgentReadComissionPrice(B2BAgentCommUtilsInfo.getAgentReadComissionPrice());
                    orderItem.setAgentReadComissionRate(B2BAgentCommUtilsInfo.getAgentReadComissionRate());
                }
            }
            
            if (null != breakfast) {
            	if(breakfast.length>dayIndex){
            		orderItem.setBreakfast(breakfast[dayIndex]);
            		orderItem.setBreakfastNum(breakfastNum[dayIndex]);
            	}else{
                    orderItem.setBreakfast(0);
                    orderItem.setBreakfastNum(0);
            	}
            } else {
                orderItem.setBreakfast(0);
                orderItem.setBreakfastNum(0);
            }
            orderItem.setQuotaPattern(returnPo.getQuotaPattern());

            orderItem.setOrderItemsType(OrderItemType.NORMAL);
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);

            // 系统内配额如果不可退，设置不可退标志
            if (!bSetCanReturn && !returnPo.isTakebackQuota()
                && (null != returnQuotaType && !returnQuotaType.equals(QuotaType.CALLQUOTA))) {
                orderItem.setQuotaCantReturn(true);
                quotaCanReturn = false;
                bSetCanReturn = true;
            }
        }
        //连住优惠把每天佣金放进OrPriceDetail表 add by guzhijie 2009-09-17 begin
        
    	List<OrPriceDetail>  orPriceDetailList = order.getPriceList();
    	List<OrOrderItem> orderItems1List = order.getOrderItems();
    	int dateNum = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        if(!orderItems1List.isEmpty()){
	        for(int k=0;k < dateNum;k++){
	        	Date day = DateUtil.getDate(order.getCheckinDate(), k);
	        	HtlPrice htlPrice = hotelService.qryHtlPriceForCC(quotaQuery
	    				.getChildRoomId(), day, quotaQuery.getPayMethod(),
	    				quotaType);
	        	for(int x=0;x<orPriceDetailList.size();x++){
	        		OrPriceDetail orPriceDetailItems = orPriceDetailList.get(x);
	            	if(orPriceDetailItems.getNight().equals(htlPrice.getAbleSaleDate())){
	            		orPriceDetailItems.setCommission(htlPrice.getCommission());
	            		orPriceDetailItems.setBasePrice(htlPrice.getBasePrice());
	            		orPriceDetailItems.setSalePrice(htlPrice.getSalePrice());
	            		
	            	}
	        		
	        	}
	        }
        }
        if(!orPriceDetailList.isEmpty() && !orderItems1List.isEmpty()){
        	//连住优惠改变相应佣金和低价到OrPriceDetail，方便下面赋值到OrderItems
        	int f=0;
        	for(int i =0;i<orPriceDetailList.size();i++){
        		Date day = DateUtil.getDate(order.getCheckinDate(), i);
	        	HtlPrice htlPrice = hotelService.qryHtlPriceForCC(quotaQuery
	    				.getChildRoomId(), day, quotaQuery.getPayMethod(),
	    				quotaType);
        		OrPriceDetail orPriceDetailItem = orPriceDetailList.get(i);
        		f=hotelReservationInfoService.changeBasePriceCommission(htlPrice, orPriceDetailList, orPriceDetailItem, order.getHotelId(), order.getChildRoomTypeId(), i, f);
        	}
        	
        	//是否有连住优惠
        	boolean isFavourable = false;
        	
        	for(int x= 0 ;x<orPriceDetailList.size();x++){
        		OrPriceDetail orPriceDetailItems = orPriceDetailList.get(x);
        		isFavourable = orPriceDetailItems.getIsFavourable();
        		for(int y=0;y<orderItems1List.size();y++){
        			OrOrderItem orderItems1 = orderItems1List.get(y);
        			if(orPriceDetailItems.getNight().getTime() == orderItems1.getNight().getTime()){
        				orderItems1List.get(y).setSalePrice(orPriceDetailItems.getSalePrice());
        				orderItems1List.get(y).setCommission(orPriceDetailItems.getCommission());
        				orderItems1List.get(y).setBasePrice(orPriceDetailItems.getBasePrice());
        				 //当为 代理订单时 ，才会去算代理佣金.则得把代理佣金填充到orderItem里面 add by shengwei.zuo 2010-1-13
        	            if(order.getType() == OrderType.TYPE_B2BAGENT){
            				//有连住优惠 add by shengwei.zuo 2010-1-15
            				if(isFavourable){
            					//代理佣金辅助类
            					B2BAgentCommUtils agentCommUtilsInfo = commissionService.getB2BCommInfo(orPriceDetailItems.getSalePrice(),orPriceDetailItems.getCommission(),order.getPaymentCurrency(), order.getHotelId(),order.getRoomTypeId(),order.getChildRoomTypeId(),payMo,String.valueOf(intStar),b2bCD);
            					if(agentCommUtilsInfo!=null){
            						log.info("OrderService  deductOrderQuota agentCommUtilsInfo  : "+
            								"isFavourable :"+isFavourable+","+
            	                            " agentComission  "+ agentCommUtilsInfo.getAgentComission() +","+
            	                            " agentComissionPrice" + agentCommUtilsInfo.getAgentComissionPrice()+","+
            	                            " agentComissionRate" + agentCommUtilsInfo.getAgentComissionRate() + ","+
            	                            " comissionType"+ agentCommUtilsInfo.getComissionType() + ","+
            	                            " comissionTypeValue" + agentCommUtilsInfo.getComissionTypeValue());
            						
            						orderItems1List.get(y).setAgentComission(agentCommUtilsInfo.getAgentComission());
                					orderItems1List.get(y).setAgentComissionPrice(agentCommUtilsInfo.getAgentComissionPrice());
                					orderItems1List.get(y).setAgentComissionRate(agentCommUtilsInfo.getAgentComissionRate());
                					orderItems1List.get(y).setComissionType(agentCommUtilsInfo.getComissionType());
                					orderItems1List.get(y).setAgentReadComission(agentCommUtilsInfo.getAgentReadComission());
                					orderItems1List.get(y).setAgentReadComissionPrice(agentCommUtilsInfo.getAgentReadComissionPrice());
                					orderItems1List.get(y).setAgentReadComissionRate(agentCommUtilsInfo.getAgentReadComissionRate());
            					}
            				}
        	            	
        	            }
  
        			}
        		}
        	}
    		
    	}
    	
        //连住优惠相应改变OrOrderItem的售价和佣金 add by guzhijie 2009-09-17 end
        order.setQuotaCanReturn(quotaCanReturn);
        
        /** V2.9.3.1 必须放在最后计算，如果是优惠立减订单，使用之前已经查询出来的立减规则计算明细的立减金额 add by chenjiajie 2009-10-19 begin **/
        if(0 < order.getFavourableFlag()){
        	//生成订单明细的时候，计算订单明细的立减金额
        	orderBenefitService.calculateOrderItemBenefit(order);
        }
        /** V2.9.3.1 必须放在最后计算，如果是优惠立减订单，使用之前已经查询出来的立减规则计算明细的立减金额 add by chenjiajie 2009-10-19 end **/
        
        return bQuotaOk;
    }

    /**
     * 手工单生成orderItems
     * 
     * @param order
     */
    public void getManualOrderQuota(OrOrder order, int breakfast, int breakfastNum) {

        Date curDate = order.getCheckinDate();
        int nDays = DateUtil.getDay(curDate, order.getCheckoutDate());
        String[] fellowNamesArr = null;
        boolean bPay = !order.isPrepayOrder();
        if (bPay) {
            fellowNamesArr = OrderUtil.fillFellowNamesToOrderItem(order); // 每天各个房间的入住人姓名数组 v2.4.2
                                                                          // chenjiajie 2008-12-30
        }
        for (int i = 0; i < nDays; i++) {
            for (int j = 0; j < order.getRoomQuantity(); j++) {
                OrOrderItem orderItem = new OrOrderItem();
                orderItem.setRoomIndex(j);
                orderItem.setDayIndex(i);
                orderItem.setFirstNight(0 == i ? true : false);
                orderItem.setLastNight(i >= nDays - 1 ? true : false);

                if (bPay) {
                    orderItem.setFellowName(fellowNamesArr[j]); // 把拆分后的入住人姓名封装到orderItem中 V2.4.2
                                                                // chenjiajie 2008-12-30
                }

                orderItem.setNight(curDate);
                orderItem.setQuantity(0);
                orderItem.setConfirm(false);
                orderItem.setHotelId(order.getHotelId());
                orderItem.setBreakfast(breakfast);
                orderItem.setBreakfastNum(breakfastNum);
                orderItem.setQuotaType(QuotaType.CALLQUOTA);

                orderItem.setOrderItemsType(OrderItemType.NORMAL);
                orderItem.setOrder(order);

                order.getOrderItems().add(orderItem);
            }
            curDate = DateUtil.getDate(curDate, 1);
        }

        order.setQuotaCanReturn(false);

    }

    /**
     * CC下中旅单,填充配额明细
     * 
     * @author chenkeming Apr 21, 2009 3:29:36 PM
     * @param order
     * @param breakfast
     * @param breakfastNum
     * @param params
     */
    public void getCtsOrderQuota(OrOrder order, int[] breakfast, int[] breakfastNum, Map params) {

        Date curDate = order.getCheckinDate();
        int nDays = DateUtil.getDay(curDate, order.getCheckoutDate());
        
        
        float hstar = order.getHotelStar();
        if(hstar > 10){
        	String strHotelStar = resourceManager.getDescription("res_hotelStarToNum",Math.round(order.getHotelStar()));
        	hstar = Float.parseFloat(strHotelStar);
        }
        int intStar = Math.round(hstar);
        
        String b2bCD = order.getMemberCd();
        int policyScope = orOrderDao.getPolicyScope(b2bCD);
        
   	 String payMo = order.getPayMethod();
		if(payMo.equals(PayMethod.PRE_PAY)){
			if(order.isPayToPrepay()){
				payMo = PayMethod.PAY;
			}
	}
		
        for (int i = 0; i < nDays; i++) {
            double salePrice = Double.valueOf((String) params.get("hSalePrice" + i));
            double basePrice = Double.valueOf((String) params.get("hBasePrice" + i));
            for (int j = 0; j < order.getRoomQuantity(); j++) {
                OrOrderItem orderItem = new OrOrderItem();
                orderItem.setRoomIndex(j);
                orderItem.setDayIndex(i);
                orderItem.setFirstNight(0 == i ? true : false);
                orderItem.setLastNight(i >= nDays - 1 ? true : false);

                orderItem.setNight(curDate);
                orderItem.setQuantity(1);
                orderItem.setConfirm(true);
                orderItem.setHotelId(order.getHotelId());

                if (null != breakfast) {
                    orderItem.setBreakfast(breakfast[i]);
                    orderItem.setBreakfastNum(breakfastNum[i]);
                } else {
                    orderItem.setBreakfast(0);
                    orderItem.setBreakfastNum(0);
                }

                orderItem.setQuotaType(QuotaType.GENERALQUOTA);
                orderItem.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNCI);
                orderItem.setOrderItemsType(OrderItemType.NORMAL);

                orderItem.setSalePrice(salePrice);
                orderItem.setBasePrice(basePrice);

                orderItem.setOrder(order);
                
                /**************************************************计算代理佣金start add by yong.zeng 2010-6-11**************************************************/
                if(order.getType()==OrderType.TYPE_B2BAGENT){
	                B2BAgentCommUtils B2BAgentCommUtilsInfo  =   commissionService.getB2BCommInfoZL(salePrice,basePrice,order.getPaymentCurrency(), order.getHotelId(),order.getRoomTypeId(),order.getChildRoomTypeId(),
        					payMo,b2bCD,String.valueOf(intStar));
	                if(B2BAgentCommUtilsInfo!=null){
	                	orderItem.setAgentComission(B2BAgentCommUtilsInfo.getAgentComission());
	                    orderItem.setAgentComissionPrice(B2BAgentCommUtilsInfo.getAgentComissionPrice());
	                    orderItem.setAgentComissionRate(B2BAgentCommUtilsInfo.getAgentComissionRate());
	                    orderItem.setComissionType(B2BAgentCommUtilsInfo.getComissionType());
	                    orderItem.setComissionTypeValue(B2BAgentCommUtilsInfo.getComissionTypeValue());
	                    orderItem.setAgentReadComission(B2BAgentCommUtilsInfo.getAgentReadComission());
	                    orderItem.setAgentReadComissionPrice(B2BAgentCommUtilsInfo.getAgentReadComissionPrice());
	                    orderItem.setAgentReadComissionRate(B2BAgentCommUtilsInfo.getAgentReadComissionRate());
	                }
                }
                /**************************************************计算代理佣金end**************************************************/
                
                
                order.getOrderItems().add(orderItem);
            }
            curDate = DateUtil.getDate(curDate, 1);
        }

        order.setQuotaCanReturn(true);
        
        /** V2.9.3.1 必须放在最后计算，如果是优惠立减订单，使用之前已经查询出来的立减规则计算明细的立减金额 add by chenjiajie 2009-10-19 begin **/
        if(0 < order.getFavourableFlag()){
        	//生成订单明细的时候，计算订单明细的立减金额
        	orderBenefitService.calculateOrderItemBenefit(order);
        }
        /** V2.9.3.1 必须放在最后计算，如果是优惠立减订单，使用之前已经查询出来的立减规则计算明细的立减金额 add by chenjiajie 2009-10-19 end **/
        
        

    }

    /**
     * 取消订单
     * 
     * @param order
     * @param startDate
     * @param endDate
     * @param cancelNum
     */
    public void cancelOrder(OrOrder order, Date startDate, Date endDate, int cancelNum) {

        List quotaItems = new ArrayList();

        Map roomMap = this.getRoomNumMap(order);
        int interval = DateUtil.getDay(startDate, endDate);

        for (int m = 0; m < interval; m++) {

            Date saleDate = DateUtil.getDate(startDate, interval);

            List roomItems = (List) roomMap.get(saleDate);

            int remain = cancelNum;

            for (int j = 0; j < roomItems.size(); j++) {

                OrOrderItem item = (OrOrderItem) roomItems.get(m);

                remain = item.getQuantity() - remain;

                if (0 <= remain) {
                    item.setQuantity(remain);

                    QuotaItem quotaItem = new QuotaItem();
                    // quotaItem.setQuotaId(item.getQuotaId());
                    quotaItem.setRoomNum(cancelNum);
                    quotaItem.setSaleDate(saleDate);

                    quotaItems.add(quotaItem);

                    break;
                }

            }

        }

        /**
         * 回收配额
         */
      //该方法没有实现，所以注释掉
//        hotelService.recycleQuota(order.getHotelId(), order.getRoomTypeId(), quotaItems);
    }

    /**
     * 获取自动发送确认短信的内容
     * 
     * @author chenkeming Mar 27, 2009 12:59:04 PM
     * @param order
     * @return
     */
    private String getSMSConfirmStr(OrOrder order) {

        HtlHotel hotel = hotelService.findHotel(order.getHotelId().longValue());
        String hotelLink = "(" + hotel.getChnAddress() + " " + hotel.getTelephone() + ")";

        Date choutDate = new Date(order.getCheckoutDate().getTime() + 24 * 60 * 60 * 1000);
        String dateStr = "";
        long checkInLong = order.getCheckinDate().getTime() + 24 * 60 * 60 * 1000;
        long checkOutLong = choutDate.getTime();
        if (checkOutLong > checkInLong) {
            dateStr = DateUtil.toStringByFormat(order.getCheckinDate(), "MM") + "月"
                + DateUtil.toStringByFormat(order.getCheckinDate(), "dd") + "-"
                + DateUtil.toStringByFormat(order.getCheckoutDate(), "dd") + "日";
        } else {
            dateStr = DateUtil.toStringByFormat(order.getCheckinDate(), "MM月dd日");
        }

        String roomType = (order.getRoomQuantity() + "").trim() + "间" + order.getRoomTypeName();

        String hotelInfoStr = order.getHotelName();

        String confirmString = "";
        //needArrivalTime是否显示保留时间点 add by chenjiajie 2010-02-02
        boolean needArrivalTime = OrderUtil.isShowArrivalTime(order);
        if (order.isMango()) {
            // 12月27－28日成都海悦酒店（四川省成都市春熙路东段步行街02881918888）2间标准间已确认,保留至18点
            confirmString = dateStr + hotelInfoStr + hotelLink + roomType + "已确认";
            if(needArrivalTime){
                confirmString += "，保留至" + order.getLatestArrivalTime() + "点";
            }
        } else {
            String memberState = order.getMemberState();
            if ("LTT".equals(memberState)) {
                confirmString = dateStr + hotelInfoStr + hotelLink + roomType + "已确认";
                if(needArrivalTime){
                    confirmString += "，保留至" + order.getLatestArrivalTime() + "点";
                }
            } else if ("WTT".equals(memberState)) {
                confirmString = dateStr + hotelInfoStr + hotelLink + roomType + "已确认";
                if(needArrivalTime){
                    confirmString += "，保留至" + order.getLatestArrivalTime() + "点";
                }
            } else if ("WTBJ".equals(memberState)) {
                confirmString = dateStr + hotelInfoStr + hotelLink + roomType + "已确认";
                if(needArrivalTime){
                    confirmString += "，保留至" + order.getLatestArrivalTime() + "点";
                }
            } else if ("NHZY".equals(memberState)) { // 南航
                confirmString = dateStr + hotelInfoStr + hotelLink + roomType + "已确认";
                if(needArrivalTime){
                    confirmString += "，保留至" + order.getLatestArrivalTime() + "点";
                }
            } else {
                confirmString = dateStr + hotelInfoStr + hotelLink + roomType + "已确认";
                if(needArrivalTime){
                    confirmString += "，保留至" + order.getLatestArrivalTime() + "点";
                }
            }
        }

        return confirmString;
    }

    /**
     * 更新订单的付款状态
     * 
     * @param orderCD
     * @param bHasPay
     *            true:已付款, false:未付款
     * @param confirmUser
     *            操作人
     * @return true:成功 false:失败
     */
    public boolean updateOrderPrepayStatus(final String orderCD, final boolean bHasPay, String confirmUser) {
        OrOrder order = orOrderDao.getCustomOrderByCD(orderCD);
        if (null == order) {
            return false;
        }

        boolean bResult = true;
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setOrder(order);
        handleLog.setModifiedTime(new Date());
        handleLog.setModifierName(confirmUser);
        handleLog.setModifierRole(confirmUser);
        order.getLogList().add(handleLog);

        // 如果支付成功
        if (bHasPay) {

            // 如果是预付单
            if (order.isPrepayOrder()) {
                List payments = order.getPaymentList();
                Date now = new Date();
                for (int i = 0; i < payments.size(); i++) {
                    OrPayment payment = (OrPayment) payments.get(i);
                    if (!payment.isPoints() && !payment.isPaySucceed()) {
                        if(payment.isCoupon()){
                            //确认使用代金券的方法 hotel2.9.3 add by chenjiajie 2009-09-15
                            voucherInterfaceService.confirmVoucherState(order, null, null);
                        }
                        payment.setPaySucceed(true);
                        payment.setConfirmTime(now);
                        payment.setConfirmer(confirmUser);
                    }
                }
                // 如果已经全部预付完成
                if (OrderUtil.checkHasPrepayed(order)) {
                    order.setHasPrepayed(true);
                    order.setOrderState(OrderState.HAS_PAID);
                }

                // 如果是中旅订单则要同步中旅订单状态
                if (order.isCtsHK()) {
                    bResult = getPayFinishCts(order, handleLog, false);
                } else { // 非中旅预付单
                    // 增加操作日志
                    handleLog.setContent("订单财务收款成功");
                }
            } else { // 如果是担保单
                order.setSuretyState(GuaranteeState.SUCCESS);
                // 增加操作日志
                handleLog.setContent("订单预授权操作成功");
            }

        } else { // 如果支付失败

            // 如果是预付单
            if (order.isPrepayOrder()) {
                // 如果是中旅订单
                /*
                 * if(order.isCtsHK()) {
                 * 
                 * // 回滚中旅订单 List<OrChannelNo> li = order.getChannelList(); int nSize = li.size();
                 * for (int j = 0; j < nSize; j++) { OrChannelNo rollChannel = li.get(j);
                 * 
                 * // 对可提交和已提交的中旅订单回滚 if (TxnStatusType.Begin == rollChannel .getStatus() ||
                 * TxnStatusType.Commited == rollChannel .getStatus()) { try { BasicData retRoll =
                 * hkService .saleRollback(rollChannel .getOrderChannel()); if (retRoll.getNRet() <
                 * ResultConstant.RESULT_SUCCESS) { log.error("回滚中旅订单失败, 芒果订单编号:" +
                 * order.getOrderCD() + ", 中旅订单号:" + rollChannel.getOrderChannel() +
                 * ", WebService错误代码: " + retRoll.getNRet() + ", 错误信息:" + retRoll.getSMessage()); }
                 * } catch (Exception e2) { log.error(e2.getMessage(),e2);; bResult = false; } finally {
                 * rollChannel.setStatus(TxnStatusType.Rollbacked); } } }
                 * 
                 * // 增加操作日志 handleLog.setContent("订单财务收款失败,中旅订单已释放配额,需中台跟进重新下单");
                 * 
                 * } else { // 非中旅预付单 // 增加操作日志 handleLog.setContent("订单财务预授权操作收款失败一次!"); }
                 */
                handleLog.setContent("订单财务预授权操作收款失败一次!");
            } else { // 如果是担保单
                order.setSuretyState(GuaranteeState.FAIL);

                // 增加操作日志
                handleLog.setContent("订单财务预授权操作失败一次!");
            }
        }

        // 保存订单
        saveOrUpdate(order);

        return bResult;
    }

    /**
     * 获取给定order的根据日期的各个orderItems
     * 
     * @param order
     * @return map
     */
    public Map getRoomNumMap(OrOrder order) {
        List roomItems = order.getOrderItems();

        Map roomMap = new HashMap();

        for (int m = 0; m < roomItems.size(); m++) {
            OrOrderItem item = (OrOrderItem) roomItems.get(m);

            Date saleDate = item.getNight();

            List quotaItems = (List) roomMap.get(saleDate);

            if (null == quotaItems) {
                quotaItems = new ArrayList();
            }

            quotaItems.add(item);

            roomMap.put(saleDate, quotaItems);

        }

        return roomMap;
    }

    /**
     * 预订之前，查找?是否预订同一卡号，同一城市，相同的入住日期的酒店<br>
     * 把"重复"订单列出来
     * 
     * @param order
     * @return
     */
    public List findSimilarOrder(OrOrder order) {
        List res = orOrderDao.queryByNamedQuery("hQuerySimilar_Order", new Object[] {
            order.getMemberId(), order.getCheckinDate() });

        // TODO: 检查是否同一城市

        return res;
    }

    /**
     * 获取酒店传真邦定ID
     */
    public Long getparmsId() {
        List list = orOrderFaxDao.queryByNamedQuery("hQueryOrderFax", new Object[] {});
        Long ID = Long.parseLong(list.get(0).toString());
        log.info("=========" + ID);
        // TODO: 查询酒店邦定的ID

        return ID;
    }

    /**
     * 根据会籍编号获取其订单统计信息
     * @param memberCd
     * @return
     */
    public OrOrderStatistics getOrderStatByMemberCd(String memberCd){
    	 String hql = "from OrOrderStatistics a where a.memberCd = ? ";
         List<OrOrderStatistics> statisticList = orOrderDao.query(hql, new Object[]{memberCd});
         
         return statisticList.isEmpty()?null: statisticList.get(0);
    }

    /**
     * 定时更新OrOrderStatistics统计信息表
     * 
     */
    public void updateOrderStatistics() {

        Session session = orOrderDao.getCurrentSession();
        String hqlDelete = "delete OrOrderStatistics";
        session.createQuery(hqlDelete).executeUpdate();

        String hqlInsert = "insert into OrOrderStatistics " +
                "(ID, memberId, avgStar, avgPrice, totalOrder, noshow,memberCd) "
            + "select "
            + "seq_order_orderstatistics.nextval,"
            + "a.memberId as memberId, "
            + "trunc(avg(to_number(a.hotelStar)),1) as avgStar,"
            + "trunc((sum(a.sum) / sum(a.roomQuantity)),2) as  avgPrice,"
            + "sum(1) as totalOrder,"
            + "sum((case when a.orderState = 13 then 1 else 0 end)) as noshow, "
            +"a.memberCd as memberCd"
            + "from OrOrder a "
            + "group by " + "a.memberCd " + "having length(a.memberCd)>0 ";
        /*
         * "select seq_order_orderstatistics.nextval, b.memberId, b.avgStar, b.avgPrice,
         * b.totalOrder, b.noshow, trunc((b.noshow/b.totalOrder*100),2) as noshowRate " + "from
         * (select a.memberId as memberId, " + "trunc(avg(to_number(a.hotelStar)),1) as
         * avgStar," + "trunc((sum(a.sum) / sum(a.roomQuantity)),2) as avgPrice," + "sum(1) as
         * totalOrder," + "sum((case when a.orderState = 13 then 1 else 0 end)) as noshow " + "from
         * OrOrder a " + "group by " + "a.menberId " + "having length(a.memberId)>0 " + ") b";
         */
        session.createQuery(hqlInsert).executeUpdate();
    }

    /**
     * 
     * @param orderid
     * @return
     */
    public List hQueryOrderItemByFaxGroup(Long orderId) {
        // List orderItemGroupByList =
        // orOrderItemDao.queryByNamedQuery("hQueryOrderItemByFax", new Object[]
        // {orderId});

        // List orderItemGroupByList =

        Map resultList = new HashMap();

        resultList.put("orderid", orderId);

        List orderItemGroupList = queryDao.queryForList("hQueryOrderItemByFaxGroup", resultList);

        return orderItemGroupList;
    }

    /**
     * 获取114用户所在省份的114会员列表
     * 
     * @param sState
     *            114用户的省份(3字编码)
     * @return
     */
    public List get114Member(String sState) {
        Map params = new HashMap();
        params.put("memberstate", MemberUtil.codeMap.get(sState));
        List memberList = queryDao.queryForList("query114MemberByUserState", params);
        return memberList;
    }

    /**
     * 根据订单ID获取订单CD
     * 
     * @param sID
     * @return
     */
    public String getOrderCDByID(Serializable sID) {
        Map params = new HashMap();
        params.put("ID", sID);
        return (String) queryDao.queryForObject("queryOrderCDByID", params);
    }

    /**
     * 获取系统内酒店币种信息
     * 
     * @param hotelId
     */
    public String getHotelSysCurrency(Serializable hotelId) {
        Map params = new HashMap();
        params.put("hotelId", hotelId);
        params.put("curDate", DateUtil.dateToString(new Date()));
        return (String) queryDao.queryForObject("querySysHotelCurrency", params);
    }

    /** simple Dao method begin */
    public OrOrder getOrder(Serializable orderID) {
        return orOrderDao.getOrder(orderID);
    }
    
    public OrOrder findOrOrder(Serializable orderID) {
        OrOrder orOrder = orOrderDao.findOrOrder(orderID);
        return orOrder;
    }
    
    /** simple Dao method begin */
    public OrOrder getOrderItems(Serializable orderID) {
        OrOrder orOrder = orOrderDao.load(OrOrder.class, orderID);
        if (null != orOrder) {
            Hibernate.initialize(orOrder.getOrderItems());
            /* Hibernate.initialize(orOrder.getFellowList());
            Hibernate.initialize(orOrder.getRemark());
            Hibernate.initialize(orOrder.getFaxList());
            Hibernate.initialize(orOrder.getMemberConfirmList());*/
        }
        return orOrder;
    }

    public void newOrder(OrOrder order) {
        orOrderDao.insertOrder(order);
    }

    public void updateOrder(OrOrder order) {
        orOrderDao.updateOrder(order);
    }

    /**
     * 根据OrOrderFax主键获取OrOrderFax
     * 
     * @param barCode
     * @return
     */
    public OrOrderFax getOrOrderFax(String barCode) {
        OrOrderFax orOrderFax = (OrOrderFax) orOrderFaxDao.loadObject(Long.valueOf(barCode));
        return orOrderFax;
    }

    /**
     * 插入回传日志
     */
    public boolean saveFaxLogAndOrderFax(OrOrderFax orOrderFax, OrFaxLog orFaxLog) {
        try {
            orOrderFaxDao.saveOrUpdate(orOrderFax);
            orFaxLogDao.saveOrUpdate(orFaxLog);
            return true;
        } catch (Exception e) {
        	log.error(e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * 插入回传日志
     */
    public boolean saveFaxLog(OrFaxLog orFaxLog) {
        try {
            orFaxLogDao.saveOrUpdate(orFaxLog);
            return true;
        } catch (Exception e) {
        	log.error(e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * 插入或更新订单
     * 
     * @param order
     */
    public Serializable saveOrUpdate(OrOrder order) {
        if (null == order.getID()) {
            order.setCreateDate(new Date());
            return orOrderDao.insertOrder(order);
        } else {
            orOrderDao.updateOrder(order);
            return null;
        }
    }

    /**
     * 获取114订单信息
     */
    public List queryHotelOrder114(Date subdate, Date subdate1) {

        String hsql = "from OrOrder o " + "where o.type = 2  " + "and o.createDate >  ? "
            + "and o.createDate <  ? " + "and o.memberState = 'GDN' ";

        Object[] obj = new Object[] { subdate, subdate1 };

        return orOrderDao.query(hsql, obj);
    }

    /**
     * 获取114日审信息
     */
    public List queryHotelDailyAult114(Date subdate, Date subdate1) {

        String hsql = "from OrOrderItem oi "
            + "where "
            + "oi.noteTime >  ? "
            + "and oi.noteTime <  ? "
            + "and oi.order.ID in (select id from OrOrder o where o.type = 2" +
                    " and o.memberState = 'GDN')"
            + "and oi.auditState = 3 ";
        // "and o.memberState = 'GDN' " ;

        Object[] obj = new Object[] { subdate, subdate1 };

        return orOrderItemDao.query(hsql, obj);
    }

    /**
     * 查询酒店订单的所有订单号绑定的回传
     * 
     * @param orderCD
     * @return
     */
    public List getOrderFaxLot(String orderCD) {
        String hsql = "from OrFaxLog ofl " + "where ofl.type = 'HA' " + "	and ofl.barCode = '"
            + orderCD + "' " + "order by ofl.logTime";
        return orOrderDao.query(hsql, null);
    }

    /**
     * 读FaxLog
     * 
     * @param objID
     *            FaxLog的主键ID
     * @param bCanChangeState
     *            能否改faxlog的state
     * @return
     */
    public OrFaxLog updateFaxLog(long objID, boolean bCanChangeState) {
        OrFaxLog faxLog = orFaxLogDao.loadObject(Long.valueOf(objID));
        if (bCanChangeState && 0 == faxLog.getState()) {
            faxLog.setState(1);
            orFaxLogDao.saveOrUpdate(faxLog);
        }
        return faxLog;
    }

    /**
     * 更新合作方酒店订单编号到数据库
     * 
     * @param orderId
     * @param orderCD
     * @param hotelOrderForChannel
     *            合作方的定单编号
     * @return true:成功 false:失败
     */
    public boolean updateOrdercdForChannel(String orderId, String orderCD, String hotelOrderForChannel) {
        String hql = "update OrOrder set orderCdForChannel = ?, quotaok = 1, sendedhotelfax = 1 " 
        	+ " where ID = ? and orderCD = ? ";
        int count = orOrderDao.updateByQL(hql, new Object[]{hotelOrderForChannel, Long.valueOf(orderId), orderCD});
        
        return count > 0 ?true : false;
    }

    /**
     * v2.8 订单前台转中台处理
     * 
     * @author chenkeming Mar 18, 2009 11:34:13 AM
     * @param orderId
     * @return
     */
    public boolean confirmToMid(long orderId, UserWrapper roleUser) {
        OrOrder order = orOrderDao.getOrder(orderId);
        if (null == order) {
            return false;
        }
        Date now = new Date();
        String hql = "update OrOrder set hotelConfirmFax=0,hotelConfirmTel=0,"
            + "hotelConfirm=0,hotelConfirmFaxReturn=0,modifier=?,modifierName=?,modifiedTime=?,toMidTime=?,isStayInMid=1 where ID=?";
        orOrderDao.doUpdateBatch(hql, new Object[] { roleUser.getLoginName(),roleUser.getName(),now,now,Long.valueOf(orderId) });
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setContent("前台转中台处理,酒店确认改为:否,已收酒店回传改为:否");
        handleLog.setModifierRole(roleUser.getLoginName());
        handleLog.setModifierName(roleUser.getName());
        handleLog.setModifiedTime(new Date());
        handleLog.setOrder(order);
        orOrderDao.save(handleLog);
        
        return true;
    }

    /** simple Dao method end */


    public void setHotelService(IHotelService hotelService) {
        this.hotelService = hotelService;
    }

    public OrOrderDao getOrOrderDao() {
        return orOrderDao;
    }

    public void setOrOrderDao(OrOrderDao orOrderDao) {
        this.orOrderDao = orOrderDao;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
    public IQuotaForCCService getQuotaForCCService() {
        return quotaForCCService;
    }

    public void setQuotaForCCService(IQuotaForCCService quotaForCCService) {
        this.quotaForCCService = quotaForCCService;
    }
	**/
    
    public DAOIbatisImpl getQueryDao() {
        return queryDao;
    }

    public void setQueryDao(DAOIbatisImpl queryDao) {
        this.queryDao = queryDao;
    }

    public OrOrderFaxDao getOrOrderFaxDao() {
        return orOrderFaxDao;
    }

    public void setOrOrderFaxDao(OrOrderFaxDao orOrderFaxDao) {
        this.orOrderFaxDao = orOrderFaxDao;
    }

    public OrFaxLogDao getOrFaxLogDao() {
        return orFaxLogDao;
    }

    public void setOrFaxLogDao(OrFaxLogDao orFaxLogDao) {
        this.orFaxLogDao = orFaxLogDao;
    }

    public OrOrderItemDao getOrOrderItemDao() {
        return orOrderItemDao;
    }

    public PointsDelegate getPointsDelegate() {
		return pointsDelegate;
	}

	public void setPointsDelegate(PointsDelegate pointsDelegate) {
		this.pointsDelegate = pointsDelegate;
	}

    public void setOrOrderItemDao(OrOrderItemDao orOrderItemDao) {
        this.orOrderItemDao = orOrderItemDao;
    }

    /**
     * 拆分入住人到OrOrderItem v2.4.2 2008-12-30
     * 
     * @param order
     * @return 每天各个房间的入住人姓名数组
     * @author chenjiajie
     */
    private String[] fillFellowNamesToOrderItem(OrOrder order) {
        int roomQuantity = order.getRoomQuantity();
        String[] fellowNamesArr = new String[roomQuantity]; // 用于封装入住人姓名到OrOrderItem里的数组
        List<OrFellowInfo> fellowList = order.getFellowList();
        int fellowSize = fellowList.size(); // 入住人数量
        /**
         * 入住人数量=房间数量 如A,B,C三个入住人，预订了三间房在同一张订单： 第一间房：A 第二间房：B 第三间房：C
         */
        if (fellowSize == roomQuantity) {
            for (int i = 0; i < roomQuantity; i++) {
                OrFellowInfo orFellowInfo = fellowList.get(i);
                fellowNamesArr[i] = orFellowInfo.getFellowName();
            }
        }
        /**
         * 入住人数量>房间数量 如A,B,C,D,E五个入住人，预订了三间房在同一张订单： 第一间房：A D 第二间房：B E 第三间房：C
         */
        else if (fellowSize > roomQuantity) {
            for (int i = 0; i < fellowSize; i++) {
                int fellowIndex = i - (i / roomQuantity) - (roomQuantity - 1) 
                * (i / roomQuantity); // 房间列表的下标
                OrFellowInfo orFellowInfo = fellowList.get(i);
                if (i > fellowIndex) { // 第一次增加入住人的时候前面不需要有空格
                    fellowNamesArr[fellowIndex] += " " + orFellowInfo.getFellowName();
                } else {
                    fellowNamesArr[fellowIndex] = orFellowInfo.getFellowName();
                }
            }
        }
        /**
         * 入住人数量<房间数量 如A,B,C三个入住人，预订了五间房在同一张订单 第一间房：A 第二间房：B 第三间房：C 第四间房：A代订 第五间房：B代订
         */
        else {
            for (int i = 0; i < roomQuantity; i++) {
                int fellowIndex = i - (i / fellowSize) - (fellowSize - 1) * 
                (i / fellowSize); // 入住人列表的下标
                OrFellowInfo orFellowInfo = fellowList.get(fellowIndex);
                if (i > fellowIndex) { // 第一组入住人填充完后，重复填充的时候在入住人姓名后加"代订"
                    if (0 < orFellowInfo.getFellowName().indexOf("代订")) {
                        fellowNamesArr[i] = orFellowInfo.getFellowName();
                    } else {
                        fellowNamesArr[i] = orFellowInfo.getFellowName() + "代订";
                    }
                } else {
                    fellowNamesArr[i] = orFellowInfo.getFellowName();
                }
            }
        }
        return fellowNamesArr;
    }

    /**
     * 修改OrderItem的入住人信息 v2.4.2 2008-12-31
     * 
     * @param order
     * @return
     */
    public boolean modifyOrderItem(OrOrder order) {
        List<OrOrderItem> orderItemLst = order.getOrderItems();
        String[] fellowNamesArr = fillFellowNamesToOrderItem(order);
        for (int i = 0; i < orderItemLst.size(); i++) {
            OrOrderItem orderItem = orderItemLst.get(i);
            orderItem.setFellowName(fellowNamesArr[orderItem.getRoomIndex()]);
            orderItem.setOrder(order);
        }
        order.setOrderItems(orderItemLst);
        return true;
    }

    /**
     * 根据当天的日期,查询当天的订单 (CC短信需求,自动发送入住提醒短信) add by shizhongwen 时间:Jan 13, 2009 3:04:34 PM
     * 
     * @param triggerdate
     * @return
     */
    public List<OrOrderOfSMS> getOrderforTrigger(String triggerdate) {

        return orOrderDao.getOrderforTrigger(triggerdate);
    }

    /**
     * 根据hql进行删除 add by shizhongwen 时间:Jan 13, 2009 12:19:34
     * 
     * @param triggerdate
     */
    public void delete(String hql) {
        orOrderDao.updateByQL(hql, null);

    }

    /**
     * 保存已发送短信的订单 add by shizhongwen 时间:Jan 15, 2009 1:46:06 PM
     * 
     * @param ordersms
     */
    public void saveOrUpdateOrderSMS(OrOrderOfSMS ordersms) {
    	//查询该订单有没有发送过，如果有，则不再往短信表插入记录
    	OrOrderOfSMS sms = orOrderDao.getOrOrderOfSMS(ordersms.getID());
    	if (null == sms){
    		orOrderDao.save(ordersms);
    	}
    }

    /**
     * 查询该类(表)中的所有数据 add by shizhongwen 时间:Jan 15, 2009 3:00:47 PM
     * 
     * @param clazz
     * @return
     */
    public List listAll(Class clazz) {
        return orOrderDao.loadAll(clazz);
    }

    /**
     * 根据酒店ID,起始日期获取税费设定信息
     * 
     * @return
     */
    public List getTaxCharges(Long contractId, Date beginDate, Date endDate) {
        return orOrderDao.queryByNamedQuery("queryTaxCharges1", new Object[] { contractId,
            beginDate, endDate });
    }

    /**
     * 获取芒果网促销信息
     * 
     * @param
     * @return
     */
    /*
     * public List getPreOrderPresaleList(Long hotelID,Date beginDate,Date endDate) { return
     * orOrderDao.queryByNamedQuery("lstPreOrderPresale", new
     * Object[]{hotelID,beginDate,endDate,beginDate,endDate,beginDate,endDate}); }
     */

    /**
     * 获取酒店促销信息
     * 
     * @param
     * @return
     */
    /*
     * public List getPreOrderSalePromos(Long hotelID,Date beginDate,Date endDate){ return
     * orOrderDao.queryByNamedQuery("lstPreOrderSalePromos", new
     * Object[]{hotelID,beginDate,endDate,beginDate,endDate,beginDate,endDate}); }
     */

    /**
     * v2.6 获取酒店或芒果促销信息,并保存到订单
     * 
     * @author chenkeming Feb 18, 2009 3:26:48 PM
     * @param order
     */
    public void getPreSale(OrOrder order) {

        Date beginDate = order.getCheckinDate();
        Date endDate = order.getCheckoutDate();
        Long hotelID = order.getHotelId();
        boolean bHasPreSale = false;

        // 酒店促销
        List<HtlSalesPromo> list = orOrderDao.queryByNamedQuery("lstPreOrderSalePromos",
            new Object[] { hotelID, beginDate, endDate, beginDate, endDate, beginDate, endDate });
        String childRoom = String.valueOf(order.getChildRoomTypeId());
        for (HtlSalesPromo item : list) {
            boolean hotelType = "1".equals(item.getSalePromoType());
            if (hotelType || 0 <= item.getRoomType().indexOf(childRoom)) {
                OrPreSale preSale = new OrPreSale();
                preSale.setBeginEnd(DateUtil.dateToString(item.getBeginDate()) + "至"
                    + DateUtil.dateToString(item.getEndDate()));
                preSale.setContent(item.getSalePromoCont());
                if (hotelType) {
                    preSale.setNameStr("酒店促销");
                } else {
                    preSale.setNameStr("房型促销");
                }
                preSale.setType(1);
                preSale.setOrder(order);
                order.getPreSales().add(preSale);
                bHasPreSale = true;
            }
        }
        list = null;

        // 芒果促销
        List<HtlPresale> li = orOrderDao.queryByNamedQuery("lstPreOrderPresale", new Object[] {
            hotelID, beginDate, endDate, beginDate, endDate, beginDate, endDate });
        for (HtlPresale item : li) {
            OrPreSale preSale = new OrPreSale();
            preSale.setBeginEnd(DateUtil.dateToString(item.getBeginDate()) + "至"
                + DateUtil.dateToString(item.getEndDate()));
            preSale.setContent(item.getPresaleContent());
            preSale.setNameStr(item.getPresaleName());
            preSale.setUrl(item.getUrl());
            preSale.setType(2);
            preSale.setOrder(order);
            order.getPreSales().add(preSale);
        }
        if (0 < li.size()) {
            bHasPreSale = true;
        }

        order.getReservation().setHasPresale(bHasPreSale);
    }

    /**
     * v2.6 获取房费另缴税信息
     * 
     * @author chenkeming Feb 18, 2009 4:47:23 PM
     * @param order
     */
    public void getTaxCharge(OrOrder order) {
        List<HtlTaxCharge> li = orOrderDao.queryByNamedQuery("queryTaxCharges1", new Object[] {
            order.getHotelId(), order.getCheckinDate(), order.getCheckoutDate() });
        for (HtlTaxCharge item : li) {
            OrTaxCharge charge = new OrTaxCharge();
            charge.setBeginEnd(DateUtil.dateToString(item.getTaxBeginDate()) + "至"
                + DateUtil.dateToString(item.getTaxEndDate()));
            charge.setRoomIncTax(item.getRoomIncTax());
            charge.setRoomTax(item.getRoomTax());
            charge.setRoomTaxName(item.getRoomTaxName());
            charge.setRoomTaxUnit(item.getRoomTaxUnit());
            charge.setOrder(order);
            order.getTaxCharges().add(charge);
        }

        order.getReservation().setHasTaxCharge(0 < li.size());
    }

    /**
     * hotel2.6 更新或者保存权限
     * 
     * @author zhuangzhineng
     * @param orUserPower
     * @return
     */
    public String saveOrUpdateUserPower(OrUserPower orUserPower) {
        if (null != orUserPower && 0 != orUserPower.getID()) {
            orUserPowerDao.updateUserPower(orUserPower);
        } else {
            orUserPowerDao.saveUserPower(orUserPower);
        }
        return "success";
    }

    /**
     * hotel2.6 删除权限
     * 
     * @author zhuangzhineng
     * @param id
     * @return
     */
    public String delUserPower(Long orUserPowerId) {
        orUserPowerDao.delUserPower(orUserPowerId);
        return "success";
    }

    /**
     * hotel2.6 查询权限
     * 
     * @author zhuangzhineng
     * @param memberId
     * @return
     */
    public boolean queryUserPower(String memberCD) {
        return orUserPowerDao.queryUserPower(memberCD);
    }

    public OrderAssist getOrderAssist() {
        return orderAssist;
    }

    /**
     * Hotel 2.5.0 判断是否满足担保条款 add by guojun
     * 
     * @return
     */
    public boolean isAssureTerm() {
        return true;
    }

    public String queryHotelDirectionModify(String hotelid) {
        HtlHotel htlHotel = (HtlHotel) orOrderDao.get(HtlHotel.class, Long.valueOf(hotelid));
        String isModify = null;
        List hoteHotelExtList = htlHotel.getHtelHotelExt();
        if (null != hoteHotelExtList) {
            for (Iterator itr = hoteHotelExtList.iterator(); itr.hasNext();) {
                HtlHotelExt htlHotelExt = (HtlHotelExt) itr.next();
                if (null == htlHotelExt.getIsModify()) {
                    isModify = "0";
                } else {
                    isModify = htlHotelExt.getIsModify().toString();
                }
            }
        }
        return isModify;
    }

    /**
     * Hotel2.5 重新下单时，返回原订单的Item详细情况
     * 
     * @param orderCd
     *            当前订单的cd
     * @param oriOrderCd
     *            旧订单的详细信息
     * @param add
     *            by guojun 2009-02-11 14:53
     */
    public OrOrder getOriOrOrder(String orderCd, String oriOrderCd) {
        // TODO Auto-generated method stub
        OrOrder orOrder = null;
        if (null == orderCd || null == oriOrderCd) {
            return null;
        }
        if (orderCd.equals(oriOrderCd)) {
            return null;
        } else {
            orOrder = getOrOrderByOrderCd(oriOrderCd);
        }
        return orOrder;

    }

    /**
     * Hotel2.5 重新下单时，通过orderCd查找旧订单的详细信息 add by guojun 2009-02-11 14:53
     */
    public OrOrder getOrOrderByOrderCd(String orderCd) {
        // TODO Auto-generated method stub
        if (null == orderCd) {
            return null;
        }
        String hql = "from OrOrder o where o.orderCD = ? ";
        List<OrOrder> orderList = orOrderDao.query(hql, new Object[]{orderCd});
        
        return orderList.isEmpty()?null : orderList.get(0);
    }

    /**
     * Hotel2.5 根据酒店Id查找该订单所对应的酒店的渠道 原因(在hotelOrderEdit.jsp 页面中check
     * 从网站下的德比的订单的渠道也为0,故改为从htl_hotel_ext表中取channel ) add by shizhongwen 时间:Feb 24, 2009 2:13:25 PM
     * 
     * @param hotelId
     * @return
     */
    public String getChannelByHotelId(String hotelId) {
        List<String> channellist = new ArrayList<String>();
        channellist = orOrderDao.getChannelByHotelId(hotelId);
        String channel = "";
        int listsize = 0;
        if (null != channellist) {
            listsize = channellist.size();
            if (0 < listsize) {
                for (int i = 0; i < listsize; i++) {
                    channel = channellist.get(i);
                }
            }
        }
        if ("".equals(channel)) {
            channel = "0";
        }
        return channel;

    }

    public HKService getHkService() {
        return hkService;
    }

    public void setHkService(HKService hkService) {
        this.hkService = hkService;
    }

    public CommunicaterService getCommunicaterService() {
        return communicaterService;
    }

    public void setCommunicaterService(CommunicaterService communicaterService) {
        this.communicaterService = communicaterService;
    }

    public void setOrderAssist(OrderAssist orderAssist) {
        this.orderAssist = orderAssist;
    }

    public IcalculatePriceService getCalculatePriceService() {
        return calculatePriceService;
    }

    public void setCalculatePriceService(IcalculatePriceService calculatePriceService) {
        this.calculatePriceService = calculatePriceService;
    }

    public OrUserPowerDao getOrUserPowerDao() {
        return orUserPowerDao;
    }

    public void setOrUserPowerDao(OrUserPowerDao orUserPowerDao) {
        this.orUserPowerDao = orUserPowerDao;
    }

    /**
     * ADD BY WUYUN 2009-03-27 更新订单状态
     * 
     * @param order
     *            ： 订单
     * @param payType
     *            ： 在线支付方式
     * @param isSuccess
     *            ： 支付是否成功
     * @param isSuccess
     *            ： 中科交易是否确认
     */
    public void updateOrder(OrOrder order, int payType, boolean isSuccess, boolean isConfirm) {
        // TODO Auto-generated method stub
    	Date now = new Date();
    	List<OrPayment> li = order.getPaymentList();
    	OrPayment payment = null;
    	if(null != li && 0 < li.size()) {
    		payment = HotelPayOnlieUtil.getPaymentNotCouponNotPoint(li); //add by diandian.hou 2011-11-9
    	} else {
    		payment = new OrPayment();	
			li.add(payment);
			payment.setOrder(order);
			payment.setCreateTime(now);
			//支付方式
			payment.setPayType(payType);
			//支付币种：这里因为针对香港中科预付，故币种未人民币
			payment.setCurrencyType(CurrencyBean.RMB);
			payment.setMoney(order.getSumRmb());
			payment.setOperateTime(now);
    	}				
		//支付是否成功
		payment.setPaySucceed(isSuccess);
		if(isSuccess){
		    payment.setConfirmTime(now);
		}
		
        if (isSuccess) {
            // 设置已经付款成功
            order.setHasPrepayed(true);
            if (isConfirm) {// 支付成功且交易已经确认，订单状态为“已付款”
                order.setOrderState(OrderState.HAS_PAID);
                // 设置KPI状态，订单不提交中台
                order.setSendedHotelFax(true);
                order.setSendedMemberFax(true);
                order.setCustomerConfirm(true);
                order.setHotelConfirm(true);
                order.setHotelConfirmFax(true);
                order.setHotelConfirmTel(true);
                order.setHotelConfirmFaxReturn(true);
                order.setStayInMid(false);
            } else {// 如果交易未被确认，订单提交中台；如果支付失败，则订单状态仍然为“暂存前台“
                // order.setOrderState(OrderState.SUBMIT_TO_MID);
                order.setOrderState(OrderState.HAS_PAID); // 交易成功但中旅单未确认,仍设置为已付款状态 modify by
                                                          // chenkeming@2009-08-13
                order.setToMidTime(new Date());
                OrderAssist assist = new OrderAssist();
                assist.setOrderHraType(order);
                order.setStayInMid(true);
            }
        }
        //确认使用代金券的方法 hotel2.9.3 add by chenjiajie 2009-09-15
        //voucherInterfaceService.confirmVoucherState(order, null, null);
        this.saveOrUpdate(order);
    }

    /**
     * 中旅单支付成功后的处理
     * 
     * @author chenkeming Apr 21, 2009 7:14:04 PM
     * @param order
     * @param handleLog
     * @param bPoint
     * @return
     */
    public boolean getPayFinishCts(OrOrder order, OrHandleLog handleLog, boolean bPoint) {

        boolean bResult = true;

        // 计算港币汇率
        String rateStr = CurrencyBean.rateMap.get(CurrencyBean.HKD);
        double rate = StringUtil.isValidStr(rateStr) ? Double.valueOf(rateStr) : 1.0;

        List<OrChannelNo> channelList = order.getChannelList();
        int nSize = channelList.size();
        boolean bCanSubmit = true; // 是否能提交中旅订单
        int nRet;

        // 先查询中旅订单状态
        try {
            for (OrChannelNo channelOrder : channelList) {
                TxnStatusData ret = hkService.enqTxnStatus(channelOrder.getOrderChannel());
                nRet = ret.getNRet();
                channelOrder.setStatus(nRet);
                if (TxnStatusType.Rollbacked == nRet) {
                    bCanSubmit = false;
                } else if (nRet < ResultConstant.RESULT_SUCCESS) {
                    bCanSubmit = false;
                    bResult = false;
                }
            }
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            bCanSubmit = false;
            bResult = false;
        }

        // 如果可以提交中旅订单
        if (bCanSubmit) {
            int nDays = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
            BasicData ret = new BasicData();
            boolean bSuc = true;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < nSize; i++) {
                OrChannelNo channelOrder = channelList.get(i);
                nRet = channelOrder.getStatus();
                if (TxnStatusType.Begin == nRet) { // 可以提交

                    // 查询中旅单结算净金额(可能有折扣的)
                    try {
                        CalAmtData amtData = hkService.saleCalAmt(channelOrder.getOrderChannel());
                        channelOrder.setOrderValRmb(BigDecimal.valueOf(amtData.getNNetAmt() * rate)
                            .setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
                    } catch (Exception e) {
                        log.error("查询中旅订单净金额失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                            + channelOrder.getOrderChannel());
                        log.error(e.getMessage(),e);
                    }

                    try {
                        ret = hkService.saleCommit(channelOrder.getOrderChannel(), nDays
                            * channelOrder.getQuantity());
                        /*
                         * if(ret.getNRet() >= ResultConstant.RESULT_SUCCESS) {
                         * channelOrder.setStatus(ret.getNRet()); }
                         */
                    } catch (Exception e1) { // 如果提交出异常
                        ret.setNRet(ResultConstant.RESULT_FAIL);
                        log.error(e1.getMessage(),e1);
                        bSuc = false;
                        bResult = false;
                    }

                    // 如果提交返回错误
                    if (bSuc && ret.getNRet() < ResultConstant.RESULT_SUCCESS) {
                        bSuc = false;
                        log.error("提交中旅订单失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                            + channelOrder.getOrderChannel() + ", WebService错误代码: " + ret.getNRet()
                            + ", 错误信息:" + ret.getSMessage());
                    }
                    
                    // 查询中旅订单状态
                    try {
                    	Thread.sleep(100); // 先等0.1秒再查询
                        TxnStatusData eRet = hkService
								.enqTxnStatus(channelOrder.getOrderChannel());
						nRet = eRet.getNRet();
						if (TxnStatusType.Rollbacked == nRet
								|| TxnStatusType.Commited != nRet) {
							bSuc = false;
						} else if (nRet < ResultConstant.RESULT_SUCCESS) {
							bSuc = false;
						}
                    } catch (Exception e) {
                    	log.error("提交中旅订单后查询中旅订单状态失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                                + channelOrder.getOrderChannel());
                    	log.error(e.getMessage(),e);
                        bSuc = false;                        
                    }

                    // 如果提交失败回滚所有中旅订单
                    if (!bSuc) {
                        for (int j = 0; j < nSize; j++) {
                            OrChannelNo rollChannel = channelList.get(j);

                            // 对可提交和已提交的中旅订单回滚
                            if (TxnStatusType.Begin == rollChannel.getStatus()
                                || TxnStatusType.Commited == rollChannel.getStatus()) {
                                try {
                                    BasicData retRoll = hkService.saleRollback(rollChannel
                                        .getOrderChannel());
                                    if (retRoll.getNRet() < ResultConstant.RESULT_SUCCESS) {
                                        log.error("回滚中旅订单失败, 芒果订单编号:" + order.getOrderCD()
                                            + ", 中旅订单号:" + rollChannel.getOrderChannel()
                                            + ", WebService错误代码: " + retRoll.getNRet() + ", 错误信息:"
                                            + retRoll.getSMessage());
                                        if (TxnStatusType.Commited == rollChannel.getStatus()) {
        									sb.append(" 撤消多余中旅单("
        											+ rollChannel.getOrderChannel()
        											+ ")失败,请人工联系中旅方撤消 ");
        								}
                                    }
                                } catch (Exception e2) {
                                	log.error(e2.getMessage(),e2);
                                    bResult = false;
                                    if (TxnStatusType.Commited == rollChannel.getStatus()) {
    									sb.append(" 撤消多余中旅单("
    											+ rollChannel.getOrderChannel()
    											+ ")失败,请人工联系中旅方撤消 ");
    								}
                                } finally {
                                    rollChannel.setStatus(TxnStatusType.Rollbacked);
                                }
                            }
                        }

                        // 订单提交中台中旅组处理
                        order.setHotelConfirm(false);
                        order.setHotelConfirmFax(false);
                        order.setHotelConfirmTel(false);
                        order.setHotelConfirmFaxReturn(false);
                        order.setStayInMid(true);

                        // 增加操作日志
                        if (bPoint) {
                            handleLog.setContent("订单积分全额支付成功,但同步中旅订单状态失败,需中台跟进处理" + sb.toString());
                        } else {
                            handleLog.setContent("订单财务收款成功,但同步中旅订单状态失败,需中台跟进处理" + sb.toString());
                        }

                        break;
                    } else { // 如果提交中旅单成功
                        channelOrder.setStatus(TxnStatusType.Commited);
                        channelOrder.setCommitTime(new Date()); // 设置提交成功时间
                    }
                }
            }

            // 如果提交同步中旅订单全部成功
            if (bSuc) {
                // 如果客人确认方式为短信则自动确认客人
                if (ConfirmType.SMS == order.getConfirmType()) {
                    String smsText = getSMSConfirmStr(order);
                    String phoneNo = order.getMobile();
                    Sms sms = new Sms();
                    sms.setApplicationName("hotel");
                    sms.setTo(new String[] { phoneNo });
                    sms.setMessage(smsText);
                    sms.setFrom("SYSTEM");
                    Long res = communicaterService.sendSms(sms);
                    OrderUtil.genMemberConfirm(order, phoneNo, MemberConfirmType.CONFIRM, smsText,
                        res);

                    // 如果有附加手机号，则同时发附加手机号
                    String appendMobile = order.getAppendMobile();
                    if (StringUtil.isValidStr(appendMobile)) {
                        String[] sendMessage = appendMobile
                            .split(OrderMessageSplit.SPLIT_APPENDMOBILE);
                        for (String message : sendMessage) {
                            sms.setTo(new String[] { message });
                            res = communicaterService.sendSms(sms);
                            OrderUtil.genMemberConfirm(order, phoneNo, MemberConfirmType.CONFIRM,
                                smsText, res);
                        }
                    }

                    // 设置KPI状态，订单不提交中台
                    order.setSendedHotelFax(true);
                    order.setSendedMemberFax(true);
                    order.setCustomerConfirm(true);
                    order.setHotelConfirm(true);
                    order.setHotelConfirmFax(true);
                    order.setHotelConfirmTel(true);

                    // 增加操作日志
                    String sLog = "";
                    if (bPoint) {
                        sLog = "订单全额积分支付成功,并自动发短信确认客人";
                    } else {
                        sLog = "订单财务收款成功,并自动发短信确认客人";
                    }

                    // 中旅单如果需要发票，且尚未创建配送单，则订单先不出中台 add by chenkeming@2009-08-04
                    OrFulfillment fulfill = order.getFulfill();
                    boolean bStayMid = null != fulfill && fulfill.isInvoice()
                        && (!StringUtil.isValidStr(order.getFulfillmentCD()));
                    if (bStayMid) {
                        order.setHotelConfirmFaxReturn(false);
                        sLog += ".客人需要发票且尚未创建配送单.";
                    } else {
                        order.setHotelConfirmFaxReturn(true);
                        order.setStayInMid(false);
                    }

                    handleLog.setContent(sLog);

                } else { // 如果其他确认方式则交中台处理
                    order.setCustomerConfirm(false);
                    order.setStayInMid(true);

                    // 增加操作日志
                    if (bPoint) {
                        handleLog.setContent("订单全额积分支付成功,但客人确认方式不是短信方式,需中台确认客人");
                    } else {
                        handleLog.setContent("订单财务收款成功,但客人确认方式不是短信方式,需中台确认客人");
                    }
                }
            }
        } else { // 如果不能提交中旅订单
            // 订单提交中台中旅组处理
            order.setHotelConfirm(false);
            order.setHotelConfirmFax(false);
            order.setHotelConfirmTel(false);
            order.setHotelConfirmFaxReturn(false);
            order.setStayInMid(true);

            // 增加操作日志
            handleLog.setContent("订单财务收款成功,但不能同步中旅订单状态(配额超时或其他原因),需中台跟进处理");
        }

        return bResult;

    }

    public List<OrOrder> queryOrdersForSynchronous() {
        List<OrOrder> result = new ArrayList<OrOrder>();
        // 查询创建日期为当前时间之前两个小时开始的订单
        Date twoHoursAgo = new Date();
        twoHoursAgo.setTime(twoHoursAgo.getTime() - 3600 * 2 * 1000);
        result = orOrderDao.queryByNamedQuery("queryForSynchronous", new Object[] { twoHoursAgo });
        return result;
    }

    public OrOrder queryOrderForSyschronous(Long orderId) {
        OrOrder orOrder = orOrderDao.load(OrOrder.class, orderId);
        // 立即加载需要的关联表
        if (null != orOrder) {
            Hibernate.initialize(orOrder.getChannelList());
            Hibernate.initialize(orOrder.getPaymentList());
            Hibernate.initialize(orOrder.getLogList());
        }
        return orOrder;
    }

    /**
     * 转合约组
     * 
     * @author guojun 2009-05-19 16:30
     */
    public boolean toContractGoup(OrToContractgroup orToContractgroup) {
        // TODO Auto-generated method stub
        if (null == orToContractgroup.getID()) {
            return null != orOrderDao.save(orToContractgroup);
        } else {
            orOrderDao.update(orToContractgroup);
            return true;
        }
    }

    /**
     * 根据订单编号获取OrToContractgroup对象
     * 
     * @author chenkeming Jul 2, 2009 10:09:02 AM
     * @param orderId
     * @return
     */
    public OrToContractgroup getContractgroup(Long orderId) {
        String hsql = " from OrToContractgroup where orderid=? order by ccToRSCTime desc";
        List li = orOrderDao.query(hsql, new Object[] { orderId });
        if (null != li && 0 < li.size()) {
            OrToContractgroup toContractgroup = (OrToContractgroup) li.get(0);
            return toContractgroup;
        }
        return null;
    }

	public String isHotelDirectionModify(String hotelid) {
		HtlHotel htlHotel= orOrderDao.get(HtlHotel.class, Long.valueOf(hotelid));
		String isModify = null;
		List hoteHotelExtList = htlHotel.getHtelHotelExt();
		if(hoteHotelExtList!=null){
			for(Iterator itr = hoteHotelExtList.iterator();itr.hasNext();){
				HtlHotelExt htlHotelExt = (HtlHotelExt)itr.next();
				if(htlHotelExt.getIsModify() == null){
				    isModify="0";
				}else{
					isModify=htlHotelExt.getIsModify().toString();
				}
			}
		}
		return isModify;
	}
	/**
	 * 根据订单CD查询代理订单对象
	 * add by haibo.li 2010-1-10
	 * @return
	 */
	public List getB2bHagtOrder(String orderCD){
		String sql = "from B2bModifyOrderInfo where orderCD =? order by createDate desc";
		List b2bOrderLst = orOrderDao.doquery(sql, orderCD,false);
		return b2bOrderLst;
	}
	/**
	 * 取出芒果网得到的佣金 代理系统
	 * add by haibo.li 2010-1-15
	 */
	public HtlB2bComminfo queryB2bComminfo(){
		String hql = "from HtlB2bComminfo";
		return (HtlB2bComminfo) orOrderDao.query(hql, null);
	}
	
	 /**
     * 判断这个membercd是否为代理卡号
     * add by haibo.li 2010-3-23
     */
    public boolean isb2bMember(String memberCd){
    	boolean isb2bMember = false;
    	String[] b2bMemberCd = new String[]{"1000003535",
    										"1000003699",
    										"1000020007",
    								    	"1000020366",
    								    	"1000020367",
    								    	"1000020370",
    								    	"1000020382",
    								    	"1000020383",
    								    	"1000020455",
    								    	"1000020479",
    								    	"1000020480",
    								    	"1000020483",
    								    	"1000020551",
    								    	"1000020555",
    								    	"1000020699",
    								    	"1000021910",
    								    	"1000022514",
    								    	"1000022588",
    								    	"1000022633",
    								    	"1000022691",
    								    	"1000022705",
    								    	"1000022733",
    								    	"1000068635",
    								    	"1000148288",
    								    	"1000162801",
    								    	"1000162803",
    								    	"1000162807",
    								    	"1000162814",
    								    	"1000162828",
    								    	"1000162832",
    								    	"1000162834",
    								    	"1000162869",
    								    	"1000162877",
    								    	"1000162909",
    								    	"1000163010",
    								    	"1001006567",
    								    	"1001006599",
    								    	"1001006618",
    								    	"1001006653",
    								    	"1001006740",
    								    	"1001006798",
    								    	"1001006800",
    								    	"1001149270",
    								    	"1001149510",
    								    	"1001151803",
    								    	"1001152485",
    								    	"1001152510",
    								    	"1001152599",
    								    	"1001152637",
    								    	"1001152700",
    								    	"1001570904",
    								    	"1003496680",
    								    	"1003683352",
    								    	"1003692669",
    								    	"1004835569",
    								    	"1004835578",
    								    	"1004845266",
    								    	"1004846565",
    								    	"1004846655",
    								    	"1005433629",
    								    	"1005514737",
    								    	"1005519088",
    								    	"1005530778",
    								    	"1005530909",
    								    	"1005530919",
    								    	"1005530925",
    								    	"1018353018",
    								    	"1018379271",
    								    	"1018812582",
    								    	"1018812818",
    								    	"1019043888",
    								    	"1020553823",
    								    	"1021535099",
    								    	"1001152691",
    								    	"1043079371",
    								    	"1043079470",
    								    	"1000022702",
    								    	"1000003768",
    								    	"1005514700",
    								    	"1000003707",
    								    	"1018811333",
    								    	"1003496660",
    								    	"1005514802",
    								    	"1000020573",
    								    	"1018379275",
    								    	"0001914917"};
    	for(int i=0;i< b2bMemberCd.length;i++){
    		String mCd = b2bMemberCd[i];
    		if(mCd.equals(memberCd)){
    			isb2bMember=true;
    		}
    	}
    	return isb2bMember;
    }
	
	/**
	 * 
	 * @return
	 */
	
	/**
	 * 保存代理修改实体
	 * add by haibo.li 2010-1-18
	 */
	public void saveB2BOrderWithOrderState(String orderCD, boolean isCancelOrder){
		//代理保存修改订单,同时更新到B2BMODIFYORDER表中数据 add by haibo.li 2010-1-17
        List orderLst = this.getB2bHagtOrder(orderCD);
        if(ValidationUtil.isEmpty(orderLst)){
        	return;
        }
        
    	for(int i=0;i<orderLst.size();i++){
    		B2bModifyOrderInfo b2bOrder = (B2bModifyOrderInfo)orderLst.get(i);
    		if(isCancelOrder){//如果是撤单处理，则看有无代理撤单，如果有，则更新代理记录表中的数据
    			if(b2bOrder.getOrderState() == 2){
    				b2bOrder.setOrderState(3);//如果取出来这条是修改未处理,则保存订单时候保存0，表示修改已经处理
    				orOrderDao.updateB2bModifyOrderWithOrderState(b2bOrder.getID(), b2bOrder.getOrderState());
    			}
    		}else{
    			if(b2bOrder.getOrderState() == 1){
        			b2bOrder.setOrderState(0);//如果取出来这条是修改未处理,则保存订单时候保存0，表示修改已经处理
        			orOrderDao.updateB2bModifyOrderWithOrderState(b2bOrder.getID(), b2bOrder.getOrderState());
        		}
    		}    		
    	}		
	}	
	
	public Long getOrderGroup(Long id) {
		String sql="select grouptype from TEMP_ORDER where orderid = ? ";
		List<?> groupTypeList = orOrderDao.queryByNativeSQL(sql, new Object[]{id});
		
		return groupTypeList.isEmpty()?null : Long.valueOf(groupTypeList.get(0).toString());
	}
    
    
    
	  /**
     * 同步配额明细中的总价格到订单表，防止变价，导致总金额和配额明细中的金额不相等 add by shengwei.zuo 2010-6-4
     * @param order
     * @return
     */
    public void synchroSumPriceToOrder(OrOrder order){
    	
    	boolean favFlag = false;
    	//有优惠立减
    	if(order.getFavourableFlag()==1){
    		favFlag = true;
    	}
    	
    	//实际每天的价格的价格总和
    	double sumItemPrice = 0d;
    	
    	//每天预付立减的价格总和
    	float sumFavPrice = 0f; 
    	
    	List<OrOrderItem> orderIteLst = order.getOrderItems();
    	if(orderIteLst!=null && !orderIteLst.isEmpty()){
    		int osize = orderIteLst.size();
    		for(int i =0;i<osize;i++){
    			OrOrderItem  OrIte = new OrOrderItem();
    			OrIte = orderIteLst.get(i);
    			
    			BigDecimal  bigSumItePrice  =  new  BigDecimal(Double.toString(sumItemPrice));  
    			//每天的销售价
    			BigDecimal  bigsaPri  =  new  BigDecimal(Double.toString(OrIte.getSalePrice()));   
    			sumItemPrice  = bigSumItePrice.add(bigsaPri).doubleValue();
    			
    			if(favFlag){//有预付立减
    				BigDecimal  bigSumFavPrice  =  new  BigDecimal(Float.toString(sumFavPrice));  
    				//每天的预付立减的价格
    				BigDecimal   bigfavPri  =  new   BigDecimal(Float.toString(OrIte.getFavourableAmount()));   
    				sumFavPrice = bigSumFavPrice.add(bigfavPri).floatValue();
    			}
    		}
    		
    		BigDecimal  sumIte =  new  BigDecimal(Double.toString(sumItemPrice));  
        	
        	BigDecimal  sumFav  =  new  BigDecimal(Float.toString(sumFavPrice));  
        	
        	//订单总金额
        	double sum =  sumIte.add(sumFav).doubleValue();
        	
        	order.setSum(sum);
        	
        	BigDecimal  sumBig =  new BigDecimal(Double.toString(sum));
        	
        	//汇率
        	BigDecimal  rateBig = new BigDecimal(Double.toString(order.getRateId()));
        	
        	double sumRmb = sumBig.multiply(rateBig).doubleValue();
        	
        	//结果四舍五入取两位小数
        	sumRmb = StringUtil.roundDouble(sumRmb, 2);
        	
        	if(favFlag){
        		
        		sumRmb = sumIte.multiply(rateBig).doubleValue();
        		
        		order.setFavourableAmount(sumFavPrice);
        		
        	}
        	
        	order.setSumRmb(sumRmb);
    		
    	}
    	
    }
    
	/**
	 * 处理订单的附加信息，如提示信息
	 */
	public void updateExtInfo(OrOrder order, Map params) {
		// TODO Auto-generated method stub
		String tipContent = (String)params.get("tipInfo");
		if(tipContent != null && !"".equals(tipContent.trim())) {
			OrOrderExtInfo orderExtInfo = new OrOrderExtInfo();
			//设置类型为01（订单提示信息提示信息）
			orderExtInfo.setType("01");
			//设置内容
			orderExtInfo.setContext(tipContent);
			orderExtInfo.setOrder(order);
			order.getOrOrderExtInfoList().add(orderExtInfo);
		}
	}
	
	/**
	 * 记录订单的来源信息，醒狮计划促销活动
	 */
	public void updateExtInfoForWakeUp(OrOrder order){
		//记录订单信息
        OrParam beginParm=systemDataService.getSysParamByName("WAKE_UP_BEGIN_DATE");
        OrParam endParm=systemDataService.getSysParamByName("WAKE_UP_END_DATE");
        if(beginParm != null && endParm !=null){
        	Date currDate=DateUtil.getSystemDate();
            Date beginDate=DateUtil.getDate(beginParm.getValue());
            Date endDate=DateUtil.getDate(endParm.getValue());
            if(DateUtil.compare(beginDate, currDate) >= 0 && DateUtil.compare(endDate, currDate) <= 0 ){
            	OrOrderExtInfo orderExtInfo = new OrOrderExtInfo();
    			//设置类型为04（订单来自醒狮计划促销活动）
    			orderExtInfo.setType(OrderExtInfoType.WakeUp);
    			//设置具体促销活动内容
    			//System.out.println(order.getHotelId());
    			List<HtlLimitFavourableHotel> htlLimitFavourableHotels= htlLimitFavourableDao.queryLimitFavourableHotel(order.getHotelId().toString(), order.getCheckinDate(), order.getCheckoutDate());
    		        if(htlLimitFavourableHotels.size()!=0){
    				//System.out.println("size:"+htlLimitFavourableHotels.size());
    				HtlLimitFavourableHotel htlLimitFavourableHotel =htlLimitFavourableHotels.get(0); 
    				//System.out.println("text:"+htlLimitFavourableHotel.getFavName());
    				orderExtInfo.setContext(htlLimitFavourableHotel.getFavId()+"|"+htlLimitFavourableHotel.getFavName());
    			}else{
    				orderExtInfo.setContext("醒狮计划促销活动");	
    			}
    			orderExtInfo.setOrder(order);
    			order.getOrOrderExtInfoList().add(orderExtInfo);
            }
        }
	}
	
	public void updateExtInfoforBFD(OrOrder order) {
		OrOrderExtInfo orderExtInfo = new OrOrderExtInfo();
		orderExtInfo.setType(OrderExtInfoType.BFD);
		orderExtInfo.setContext("百分点推荐栏");
		orderExtInfo.setOrder(order);
		order.getOrOrderExtInfoList().add(orderExtInfo);
		
	}
	/**
	 * 获取订单附近信息(包括订单提示信息、订单支付凭证)
	 */
	public void getOrOrderExtInfo(HttpServletRequest request, OrOrder order) {
		List<OrOrderExtInfo> orderExtInfoList = order.getOrOrderExtInfoList();
		if(orderExtInfoList != null && orderExtInfoList.size() >0) {
			for(OrOrderExtInfo orderExtInfo : orderExtInfoList) {
				if(BaseConstant.ORDER_EXTINFO_TIPINFO.equals(orderExtInfo.getType())) {
					request.setAttribute("tipInfo", orderExtInfo.getContext());
				}else if(BaseConstant.ORDER_EXTINFO_SID.equals(orderExtInfo.getType())) {
					request.setAttribute("sid", orderExtInfo.getContext());
				}
			}
		}
	}
	
    public String changeOrderForSencondConfirm(OrOrder order){
    	order.setQuotaOk(false);
		order.setSendedHotelFax(true);
		order.setHotelConfirm(false);
		order.setHotelConfirmFax(false);
		order.setHotelConfirmTel(false);
		order.setHotelConfirmFaxReturn(false);
		return "二次确认,已发送酒店确认";
    }
    public String getMemberAliasId(String memberCd) {
    	MemberDTO member = memberInterfaceService.getMemberByCode(memberCd);
    	return member.getAliasid();
    }
    
    /**
     * 根据订单号和信息类型获取订单附加信息
     * @param order
     * @param type
     * @return
     */
    public String getOrderExtInfoByType(OrOrder order, String type) {
    	List<OrOrderExtInfo> orderExtInfoList = order.getOrOrderExtInfoList();
    	Long id=0l;
    	
    	if(orderExtInfoList != null) {
    		
    		for(OrOrderExtInfo orderExtInfo : orderExtInfoList) {
    			if(id<orderExtInfo.getID()){
    			    id=orderExtInfo.getID();
    			}   			
    		}
    		
    		for(OrOrderExtInfo orderExtInfo : orderExtInfoList) {
    			if(type.equals(orderExtInfo.getType()) && orderExtInfo.getID().equals(id)) {
    				return orderExtInfo.getContext();
    			}
    		}
    	}
    	return null;
    }
    
    /**
	 * 获取酒店确认号
	 * @param id
	 * @return
	 */
	public String getConfirmNum(Long orderId){
		String confirmNum = "";
		List<OrOrderFax> list = orOrderDao.query("from OrOrderFax c where c.order.ID=?",new Object[]{orderId});
		if(list!=null && !list.isEmpty()){
			for(OrOrderFax confirm :list){
				if(confirm.getType()==1 && StringUtil.isValidStr(confirm.getNotes())){
					confirmNum = confirm.getNotes();
					break;
				}
			}
		}
		return confirmNum;
	}
	
	
    
    /**
     * 增加订单操作日至
     * @param log
     */
   
    public void addHandleLog(OrHandleLog log){
    	orOrderDao.save(log);
    }
    
	public int getPolicyScope(String agentcode){
		return orOrderDao.getPolicyScope(agentcode);
	}

	public IQuotaControlService getQuotaControl() {
		return quotaControl;
	}

	public void setQuotaControl(IQuotaControlService quotaControl) {
		this.quotaControl = quotaControl;
	}


	
	public IVoucherInterfaceService getVoucherInterfaceService() {
		return voucherInterfaceService;
	}

	public void setVoucherInterfaceService(
			IVoucherInterfaceService voucherInterfaceService) {
		this.voucherInterfaceService = voucherInterfaceService;
	}

	public MidOrderTransfer getMidOrderTransfer() {
		return midOrderTransfer;
	}

	public void setMidOrderTransfer(MidOrderTransfer midOrderTransfer) {
		this.midOrderTransfer = midOrderTransfer;
	}

	 public IHotelReservationInfoService getHotelReservationInfoService() {
			return hotelReservationInfoService;
		}

	public void setHotelReservationInfoService(
			IHotelReservationInfoService hotelReservationInfoService) {
		this.hotelReservationInfoService = hotelReservationInfoService;
		}
public MemberInterfaceService getMemberInterfaceService() {
		return memberInterfaceService;
	}

	public void setMemberInterfaceService(
			MemberInterfaceService memberInterfaceService) {
		this.memberInterfaceService = memberInterfaceService;
	}

	public void setPriceManage(IPriceManage priceManage) {
		this.priceManage = priceManage;
	}

	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}

	/**
	 * 根据多个订单号查询订单 
	 * @param orderIds 订单ID字符串
	 * @return
	 */
	public List<OrOrder> getOrOrderList(String orderIds) {
		return orOrderDao.getOrOrderList(orderIds);
	}

	public boolean validateSid(String sid) {
		// TODO Auto-generated method stub
		return orOrderDao.validateSid(sid);
	}
	
	public void setSystemDataService(SystemDataService systemDataService) {
    	this.systemDataService = systemDataService;
    }

	public HtlLimitFavourableDao getHtlLimitFavourableDao() {
		return htlLimitFavourableDao;
	}

	public void setHtlLimitFavourableDao(HtlLimitFavourableDao htlLimitFavourableDao) {
		this.htlLimitFavourableDao = htlLimitFavourableDao;
	}
	
	/**
	 * 查询projectcode
	 * @param orderCD 
	 */
	public String queryOrderProject(String orderCD){
		List list = orOrderDao.queryByNativeSQL("select projectcode from htl_projectcode where ordercd=? ", new Object[]{orderCD});
		if(list!=null && list.size()>0){
			return String.valueOf(list.get(0));
		}
		return null;
	}
	
	/**
	 * 查询projectcode
	 * @param orderCD 
	 */
	public HtlProjectCode queryHtlOrderProject(String orderCD){
		List<HtlProjectCode> list = orOrderDao.query("from HtlProjectCode c where c.orderCD=?",new Object[]{orderCD});
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}


	
}
