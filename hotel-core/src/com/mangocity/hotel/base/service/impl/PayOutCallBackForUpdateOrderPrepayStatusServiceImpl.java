package com.mangocity.hotel.base.service.impl;

import hk.com.cts.ctcp.hotel.constant.ResultConstant;
import hk.com.cts.ctcp.hotel.constant.TxnStatusType;
import hk.com.cts.ctcp.hotel.service.HKService;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.TxnStatusData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BasicData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.CalAmtData;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.PayOutCallBackForUpdateOrderPrepayStatusService;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.GuaranteeState;
import com.mangocity.hotel.order.constant.MemberConfirmType;
import com.mangocity.hotel.order.constant.OrderMessageSplit;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.persistence.OrChannelNo;
import com.mangocity.hotel.order.persistence.OrFulfillment;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.service.IVoucherInterfaceService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.service.OrderImmedConfirmService;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Sms;

public class PayOutCallBackForUpdateOrderPrepayStatusServiceImpl implements
		PayOutCallBackForUpdateOrderPrepayStatusService {
	
	private static final MyLog log = MyLog.getLogger(PayOutCallBackForUpdateOrderPrepayStatusService.class);
	 /**
     * 代金券接口
     * hotel2.9.3 add by chenjiajie 2009-09-02
     */
    private IVoucherInterfaceService voucherInterfaceService;
    
    private OrOrderDao orOrderDao;
    
  //即时确认，发送短信，Email，传真的Service add by xiaowei.wang 2010-12-9
	private OrderImmedConfirmService orderImmedConfirmService;
	
	
	 /**
     * message接口
     */
    private CommunicaterService communicaterService;

	
	  /**
     * 需要用到的酒店模块提供的接口
     */
    private IHotelService hotelService;
	
	private HKService hkService;
    
	public boolean updateOrderPrepayStatus(String orderCD, boolean bHasPay, String confirmUser){
	        OrOrder order = this.getOrOrder(orderCD);
	        if (order == null){
	        	return false;
	        }
	        boolean bResult = true;
	        OrHandleLog handleLog = recordHandleLog(confirmUser, order);

	        // 如果支付成功
	        if (bHasPay) {
	        		
	            // 如果是预付单
	            if (order.isPrepayOrder()) {
	            	//添加到中台
	            	order.setIsStayInMid(true);
	                List payments = order.getPaymentList();
	                Date now = new Date();
	                for (int i = 0; i < payments.size(); i++) {
	                    OrPayment payment = (OrPayment) payments.get(i);
	                //这里为信用卡回调处理方法，所以需要过滤掉银联电话支付方式
					if (!payment.isPoints() && !payment.isPaySucceed()
							&& !payment.isUnionPayPhone()) {
	                        if(payment.isCoupon()){
	                            //确认使用代金券的方法 hotel2.9.3 add by chenjiajie 2009-09-15
	                            voucherInterfaceService.confirmVoucherState(order, null, null);
	                        }
	                        payment.setPaySucceed(true);
	                        payment.setConfirmTime(now);
	                        payment.setConfirmer(confirmUser);
	                    }
	                }
	                updatePrepayStatus(order);
	                try {
						sendFaxToHotelWithoutCTS(order,handleLog, null);
					} catch (Exception e) {
						log.error("发送传真或确认短信失败:",e);
					}
	                bResult = syncCtsHKOrderStatus(order, bResult, handleLog, null);
	            } else { // 如果是担保单
	            	
	                assurePaySuceProcess(order, handleLog);
	            }

	        } else { // 如果支付失败
	        	//添加到中台
            	order.setIsStayInMid(true);
	        	bResult=false;
	            payFailedProcess(order, handleLog, null);
	        }

	        // 保存订单
	        try {
				saveOrUpdate(order);
			} catch (Exception e) {
				log.error("保存订单失败:",e);
			}

	        return bResult;
	}
	
	/**
	 * 银联电话支付方式 财务回调处理
	 * @param orderCD
	 * @param isPaySucc 是否支付成功
	 * @param confirmUser
	 * @return
	 */
	public boolean updateOrderUnionPayPhone(String orderCD, boolean isPaySucc, String confirmUser)  throws Exception{
        OrOrder order = getOrOrder(orderCD);
        if (order == null){
        	return false;
        }
        boolean bResult = true;
        
        //记录操作日志
        OrHandleLog handleLog = recordHandleLog(confirmUser, order);

        // 如果支付成功,则进行订单状态处理，支付失败，则记录日志，担保支付时，担保状态改为担保失败
        if (isPaySucc) {
            bResult = paySuccProcess(confirmUser, order, bResult, handleLog);
        } else {
        	//支付失败处理
            payFailedProcess(order, handleLog, "银联电话支付");
        }

        // 保存订单
        saveOrUpdate(order);

        return bResult;
}

	/**
	 * 支付成功处理
	 * @param confirmUser
	 * @param order
	 * @param bResult
	 * @param handleLog
	 * @return
	 */
	private boolean paySuccProcess(String confirmUser, OrOrder order,
			boolean bResult, OrHandleLog handleLog)  throws Exception{
		// 如果是预付单
		if (order.isPrepayOrder()) {
		    bResult = prepayOrderPaySuccProcess(confirmUser, order,
					bResult, handleLog);
		} else { 
		    assurePaySuceProcess(order, handleLog);
		}
		return bResult;
	}

	/**
	 * 支付失败处理
	 * @param order
	 * @param handleLog
	 * @param payMethod TODO
	 */
	private void payFailedProcess(OrOrder order, OrHandleLog handleLog, String payMethod) {
		payMethod = payMethod==null?"":payMethod;
		// 如果是预付单
		if (order.isPrepayOrder()) {
		    handleLog.setContent(payMethod + "订单财务预授权操作收款失败一次!");
		} else { // 如果是担保单
		    order.setSuretyState(GuaranteeState.FAIL);
		    // 增加操作日志
		    handleLog.setContent(payMethod + "订单财务预授权操作失败一次!");
		}
	}

	/**
	 * 担保支付成功
	 * @param order
	 * @param handleLog
	 */
	private void assurePaySuceProcess(OrOrder order, OrHandleLog handleLog) {
		order.setSuretyState(GuaranteeState.SUCCESS);
		// 增加操作日志
		handleLog.setContent("订单预授权操作成功");
	}

	/**
	 * 预付订单支付成功处理
	 * @param confirmUser
	 * @param order
	 * @param bResult
	 * @param handleLog
	 * @return
	 */
	private boolean prepayOrderPaySuccProcess(String confirmUser,
			OrOrder order, boolean bResult, OrHandleLog handleLog) throws Exception{
		String payMethod = "银联电话支付";
		//保存支付结果
		savePayResult(confirmUser, order);
		// 如果已经全部预付完成，则更新支付状态为已支付
		updatePrepayStatus(order);
		//非中旅订单，支付完成后给酒店发传真，可即时确认的，给客户发确认短信
		sendFaxToHotelWithoutCTS(order,handleLog,payMethod );
		// 如果是中旅订单则要同步中旅订单状态		
		return syncCtsHKOrderStatus(order, bResult, handleLog, "银联电话支付");
	}

	/**
	 * 同步中旅订单状态
	 * @param order
	 * @param bResult
	 * @param handleLog
	 * @param payMethod TODO
	 * @return
	 */
	private boolean syncCtsHKOrderStatus(OrOrder order, boolean bResult,
			OrHandleLog handleLog, String payMethod) {
		if (order.isCtsHK()) {
		    bResult = getPayFinishCts(order, handleLog, false, payMethod);
		}
		return bResult;
	}

	/**
	 * 更新订单支付状态为已支付
	 * @param order
	 */
	private void updatePrepayStatus(OrOrder order) {
		if (OrderUtil.checkHasPrepayed(order)) {
		    order.setHasPrepayed(true);
		    order.setOrderState(OrderState.HAS_PAID);
		}
	}

	/**
	 * 非中旅订单，支付完成后给酒店发传真，可即时确认的，给客户发确认短信
	 * @param order
	 * @param payMethod TODO
	 */
	private void sendFaxToHotelWithoutCTS(OrOrder order,OrHandleLog handleLog, String payMethod) throws Exception{
		if(!order.isCtsHK()){
			if(!order.isSendedHotelFax()){
				//发送传真给酒店 
				MemberDTO memberDTO = null;
				String sendHotelResult = "success";
				if("success".equals(sendHotelResult)){
					//是否满足配额  
					boolean quotaOk = orderImmedConfirmService.quotaOk(order);
					
					// 除魅影订单外如果满足配额且支付成功给客人即时确认
					if(quotaOk && order.isHasPrepayed() && !order.getRmpOrder()){
						orderImmedConfirmService.sendImmedConfirmToCus(order,false,memberDTO);
					}            
				}
			}
			handleLog.setContent(payMethod + "订单财务收款成功");
		}
	}

	/**
	 * 保存支付结果:只确认银联电话支付和代金券
	 * @param confirmUser
	 * @param order
	 */
	private void savePayResult(String confirmUser, OrOrder order) {
		List<OrPayment> payments = order.getPaymentList();
		Date now = new Date();
		for (int i = 0; i < payments.size(); i++) {
			OrPayment payment = (OrPayment) payments.get(i);
			if (!payment.isPaySucceed()
					&& (payment.isUnionPayPhone() || payment.isCoupon())) {
				if (payment.isCoupon()) {
					// 确认使用代金券的方法
					voucherInterfaceService.confirmVoucherState(order,
							null, null);
				}
				payment.setPaySucceed(true);
				payment.setConfirmTime(now);
				payment.setConfirmer(confirmUser);
			}
		}
	}

	/**
	 * 据订单号查询订单信息
	 * @param orderCD
	 * @return
	 */
	private OrOrder getOrOrder(String orderCD) {
		List<OrOrder> orderList = orOrderDao.query(" from OrOrder where orderCD = ? ", new Object[]{orderCD});
        if(orderList.isEmpty()){
        	return null;
        }
        OrOrder order = orderList.get(0);
		return order;
	}

	/**
	 * 记录操作日志
	 * @param confirmUser
	 * @param order
	 * @return
	 */
	private OrHandleLog recordHandleLog(String confirmUser, OrOrder order) {
		OrHandleLog handleLog = new OrHandleLog();
        handleLog.setOrder(order);
        handleLog.setModifiedTime(new Date());
        handleLog.setModifierName(confirmUser);
        handleLog.setModifierRole(confirmUser);
        order.getLogList().add(handleLog);
		return handleLog;
	}
	   /**
     * 插入或更新订单
     * 
     * @param order
     */
    public Serializable saveOrUpdate(OrOrder order) throws Exception{
        if (null == order.getID()) {
            order.setCreateDate(new Date());
            return orOrderDao.insertOrder(order);
        } else {
            orOrderDao.updateOrder(order);
            return null;
        }
    }
    
    /**
     * 中旅单支付成功后的处理
     * 
     * @author chenkeming Apr 21, 2009 7:14:04 PM
     * @param order
     * @param handleLog
     * @param bPoint
     * @param payMethod TODO
     * @return
     */
    public boolean getPayFinishCts(OrOrder order, OrHandleLog handleLog, boolean bPoint, String payMethod) {

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
            StringBuffer sb = new StringBuffer("");
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
                            if (TxnStatusType.Begin == rollChannel.getStatus()) {
                                try {
                                    BasicData retRoll = hkService.saleRollback(rollChannel
                                        .getOrderChannel());
                                    if (retRoll.getNRet() < ResultConstant.RESULT_SUCCESS) {
                                        log.error("回滚中旅订单失败, 芒果订单编号:" + order.getOrderCD()
                                            + ", 中旅订单号:" + rollChannel.getOrderChannel()
                                            + ", WebService错误代码: " + retRoll.getNRet() + ", 错误信息:"
                                            + retRoll.getSMessage());
                                    }
                                } catch (Exception e2) {
                                	log.error(e2.getMessage(),e2);
                                    bResult = false;
                                } finally {
                                    rollChannel.setStatus(TxnStatusType.Rollbacked);
                                }
                            } else if(TxnStatusType.Commited == rollChannel.getStatus()) {
								sb.append(" 撤消多余中旅单("
										+ rollChannel.getOrderChannel()
										+ ")失败,请人工联系中旅方撤消 ");
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
                            handleLog.setContent(payMethod + "订单积分全额支付成功,但同步中旅订单状态失败,需中台跟进处理" + sb.toString());
                        } else {
                            handleLog.setContent(payMethod + "订单财务收款成功,但同步中旅订单状态失败,需中台跟进处理" + sb.toString());
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

                    handleLog.setContent(payMethod + sLog);

                } else { // 如果其他确认方式则交中台处理
                    order.setCustomerConfirm(false);
                    order.setStayInMid(true);

                    // 增加操作日志
                    if (bPoint) {
                        handleLog.setContent(payMethod + "订单全额积分支付成功,但客人确认方式不是短信方式,需中台确认客人");
                    } else {
                        handleLog.setContent(payMethod + "订单财务收款成功,但客人确认方式不是短信方式,需中台确认客人");
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
            handleLog.setContent(payMethod + "订单财务收款成功,但不能同步中旅订单状态(配额超时或其他原因),需中台跟进处理");
        }

        return bResult;

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

	public IVoucherInterfaceService getVoucherInterfaceService() {
		return voucherInterfaceService;
	}

	public void setVoucherInterfaceService(
			IVoucherInterfaceService voucherInterfaceService) {
		this.voucherInterfaceService = voucherInterfaceService;
	}

	public OrOrderDao getOrOrderDao() {
		return orOrderDao;
	}

	public void setOrOrderDao(OrOrderDao orOrderDao) {
		this.orOrderDao = orOrderDao;
	}

	public OrderImmedConfirmService getOrderImmedConfirmService() {
		return orderImmedConfirmService;
	}

	public void setOrderImmedConfirmService(
			OrderImmedConfirmService orderImmedConfirmService) {
		this.orderImmedConfirmService = orderImmedConfirmService;
	}

	public CommunicaterService getCommunicaterService() {
		return communicaterService;
	}

	public void setCommunicaterService(CommunicaterService communicaterService) {
		this.communicaterService = communicaterService;
	}

	public IHotelService getHotelService() {
		return hotelService;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

	public HKService getHkService() {
		return hkService;
	}

	public void setHkService(HKService hkService) {
		this.hkService = hkService;
	}
    
    
}
