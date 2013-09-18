package com.mangocity.hotel.order.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctol.mango.pge.common.ParamServiceImpl;
import com.mangocity.hotel.base.service.PayOutCallBackForUpdateOrderPrepayStatusService;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.util.HotelPayOnlieUtil;


/**
 * 银联支付相关Action
 * @author wupx 
 *
 */
public class UnionPayAction extends OrderAction {

	/**
	 * 银联电话支付
	 */
	private static final String RMUNIONIVRPAY = "RMUNIONIVRPAY";

	private static final long serialVersionUID = 7387855879548843772L;
			
	/** 订单ID */
	private Long orderId;
	
	/** 请求财务的Url */
	private String reqUrl;

	/** 外部订单号 in */
	private String outTradeNo;

	/** 支付结果 in */
	private String payResult;

	/** 支付结果消息内容 in */
	private String payMessage;

	/** 支付时间 in */
	private String payTime;

	/** 支付金额 in */
	private String payAmount;

	/** 通知类型 in */
	private String notifyType;

	/** 签名类型 in */
	private String signType;

	/** 签名 in */
	private String sign;

	/** 操作员 in */
	private String operator;
	
	private PayOutCallBackForUpdateOrderPrepayStatusService payOutCallBackForUpdateOrderPrepayStatusService;
		
	private static final MyLog logger = MyLog.getLogger(UnionPayAction.class);
	/**
	 * 银联电话支付
	 */
	public String phone() {
		order = orderService.findOrOrder(orderId);
		reqUrl = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.unionpayphoneurl}");
		signType = HotelPayOnlieUtil.SIGN_TYPE;
		sign = HotelPayOnlieUtil.buildSign(buildSendParams(order),
				HotelPayOnlieUtil.PSD_KEY);
		
		//记录日志
		recordHandleLog();
		
		//保存外部交易号中的2位流水号到Payment中,并记录支付日志
		orderService.saveOrUpdate(order);
        return "phone";
    }

	/**
	 * 银联电话支付财务回调处理
	 * @return
	 * @throws IOException 
	 */
	public void returnPhone() throws IOException{
		String result = "";
		String flag = "S";
		if (!validateParams(result)){
			logger.error(result);
			flag = "F";
			getHttpResponse().getWriter().print(flag);
			return ;
		}
		//组装接收参数
		Map<String,String> params = new HashMap<String,String>();
		buildReceiveParams(params);
		
		//校验加密信息
		String buildSign = HotelPayOnlieUtil.buildSign(params, HotelPayOnlieUtil.PSD_KEY);
		if (!sign.equals(buildSign)){
			logger.error("加密信息错误！");
			flag = "F";
			getHttpResponse().getWriter().print(flag);
			return ;
		}
		
		//处理支付结果
		String orderCD = outTradeNo.substring(4, outTradeNo.length() - 2);
		boolean isPaySucc = "success".equals(payResult) || "SUCCESS".equals(payResult);
		try {
			payOutCallBackForUpdateOrderPrepayStatusService.updateOrderUnionPayPhone(orderCD, isPaySucc,operator);
		} catch (Exception e) {
			flag = "F";
			logger.error("回调存储失败：",e);
		}
		
		getHttpResponse().getWriter().print(flag);
	}

	/**
	 * 记录日志
	 */
	private void recordHandleLog() {
		UserWrapper userWrapper = getOnlineRoleUser();
		OrHandleLog handleLog = new OrHandleLog();
        handleLog.setOrder(order);
        handleLog.setModifiedTime(new Date());
        handleLog.setModifierName(userWrapper.getName());
        handleLog.setModifierRole(userWrapper.getName());
        handleLog.setContent("已经使用银联电话支付方式");
        order.getLogList().add(handleLog);
	}
	
	/**
	 * 组装接收参数
	 * @param params
	 */
	private void buildReceiveParams(Map<String, String> params) {
		params.put("outTradeNo", outTradeNo);
		params.put("payResult", payResult);
		params.put("payMessage", payMessage);
		params.put("payTime", payTime);
		params.put("payAmount", payAmount);
		params.put("operator", operator);
		params.put("notifyType", notifyType);
	}

	/**
	 * 参数校验
	 */
	private boolean validateParams(String result) {
		boolean succ = true;
		if (null == outTradeNo || "".equals(outTradeNo)){
			result += "outTradeNo不能为空|";
			succ = false;
		}
		if (null == payResult || "".equals(payResult)){
			result += "payResult不能为空|";
			succ = false;
		}
		if (null == payMessage){
			result += "payMessage不能为空|";
			succ = false;
		}
		if (null == payTime || "".equals(payTime)){
			result += "payTime不能为空|";
			succ = false;
		}
		if (null == payAmount || "".equals(payAmount)){
			result += "payAmount不能为空|";
			succ = false;
		}
		if (null == operator || "".equals(operator)){
			result += "operator不能为空|";
			succ = false;
		}
		if (null == notifyType || "".equals(notifyType)){
			result += "outTradeNo不能为空|";
			succ = false;
		}
		if (null == signType || "".equals(signType)){
			result += "signType不能为空|";
			succ = false;
		}
		if (null == sign || "".equals(sign)){
			result += "sign不能为空|";
			succ = false;
		}
		return succ;
	}

	/**
	 * 组装发送参数
	 * @return
	 */
	private Map<String,String> buildSendParams(OrOrder order) {
		Map<String,String> map = new HashMap<String,String>();
		if (order == null){
			return map;
		}
		map.put("orderCd", order.getOrderCD());
		request.setAttribute("orderCd", order.getOrderCD());
		
		//生成外部交易号
		outTradeNo = buildOutTradeNo(order);
		map.put("outTradeNo", outTradeNo);		
		
		map.put("customerId", HotelPayOnlieUtil.CUSTOMER_ID);
		request.setAttribute("customerId",HotelPayOnlieUtil.CUSTOMER_ID);
		
		map.put("gatheringUnitCode", HotelPayOnlieUtil.GATHERING_UNITCODE);
		request.setAttribute("gatheringUnitCode",HotelPayOnlieUtil.GATHERING_UNITCODE);
		
		map.put("currencyType", order.getPaymentCurrency());
		request.setAttribute("currencyType", order.getPaymentCurrency());
		
		Date requestDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("requestDate", df.format(requestDate));
		request.setAttribute("requestDate", df.format(requestDate));
		
		map.put("productType", HotelPayOnlieUtil.PRODUCT_TYPE_HOTEL);
		request.setAttribute("productType", HotelPayOnlieUtil.PRODUCT_TYPE_HOTEL);
		
		map.put("payMode", RMUNIONIVRPAY);
		request.setAttribute("payMode", RMUNIONIVRPAY);
		
		//从支付列表中找出支付方式为银联电话支付的记录，设置支付金额与币种
		String payAmount = countAmount(order, map);
		map.put("amount", payAmount);
		request.setAttribute("amount", payAmount);
		
		String url = HotelPayOnlieUtil.getRequestUrl(this.getRequest()
				.getRequestURL().toString())
				+ "/servlet/UnionPayPhoneResult";
		map.put("returnURL", url);
		request.setAttribute("returnURL", url);
		
		map.put("memberCD", order.getMemberCd());
		request.setAttribute("memberCD", order.getMemberCd());
		
		String passenger = buildPassenger(order);
		map.put("passenger", passenger);
		request.setAttribute("passenger", passenger);				
		
		return map;
	}


	/**
	 * 生成外部交易号 
	 * 客户ID+订单Code+2位流水号，2位流水号从10开始，每次加1
	 * @param order
	 * @return
	 */
	private String buildOutTradeNo(OrOrder order) {
		String outTradeNo = HotelPayOnlieUtil.CUSTOMER_ID + order.getOrderCD();
		String seriNo = "10";
		List<OrPayment> paymentLst = order.getPaymentList();
		if (paymentLst != null && !paymentLst.isEmpty()) {
			for (int i = 0; i < paymentLst.size(); i++) {
				OrPayment paymentEntiy = paymentLst.get(i);
				if (paymentEntiy.isUnionPayPhone()) {
					// 流水号
					seriNo = paymentEntiy.getPrepayBillNo();
					if (seriNo != null && !"".equals(seriNo)
							&& seriNo.length() == 2) {
						seriNo = String.valueOf((Integer.parseInt(seriNo) + 1));
						paymentEntiy.setPrepayBillNo(seriNo);
					}else{
						seriNo = "10";
					}
					break;
				}
			}
		}
		outTradeNo = outTradeNo + seriNo;
		return outTradeNo;
	}

	/**
	 * 计算支付金额
	 * @param order
	 * @param map
	 * @return
	 */
	private String countAmount(OrOrder order, Map<String, String> map) {
		List<OrPayment> paymentList = order.getPaymentList();
		String payAmount = "";
		for (OrPayment orPayment : paymentList) {
			if (orPayment.getPayType() == PrepayType.UNTIONPAYPHONE){
				payAmount = orPayment.getMoney() + "";
				if (null != orPayment.getCurrencyType() 
						&& !"".equals(orPayment.getCurrencyType())){
					map.put("currencyType", orPayment.getCurrencyType());
					request.setAttribute("currencyType", orPayment.getCurrencyType());
				}
			}
		}
		return payAmount;
	}

	/**
	 * 组装联系人信息
	 * @param order
	 * @return
	 */
	private String buildPassenger(OrOrder order) {
		String passenger;
		StringBuffer sb = new StringBuffer("{");
		sb.append(order.getLinkMan());
		sb.append(",");
		sb.append(order.getMobile());
		sb.append(", , }");
		passenger = sb.toString();
		return passenger;
	}
	
	
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getPayResult() {
		return payResult;
	}

	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}

	public String getPayMessage() {
		return payMessage;
	}

	public void setPayMessage(String payMessage) {
		this.payMessage = payMessage;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	public PayOutCallBackForUpdateOrderPrepayStatusService getPayOutCallBackForUpdateOrderPrepayStatusService() {
		return payOutCallBackForUpdateOrderPrepayStatusService;
	}

	public void setPayOutCallBackForUpdateOrderPrepayStatusService(
			PayOutCallBackForUpdateOrderPrepayStatusService payOutCallBackForUpdateOrderPrepayStatusService) {
		this.payOutCallBackForUpdateOrderPrepayStatusService = payOutCallBackForUpdateOrderPrepayStatusService;
	}
}
