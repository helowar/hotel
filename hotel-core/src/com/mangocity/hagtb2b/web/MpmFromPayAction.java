package com.mangocity.hagtb2b.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.mangocity.hk.constant.HkConstant;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.service.OrderImmedConfirmService;
import com.mangocity.webnew.util.HotelPayOnlieUtil;
import com.mangocity.webnew.util.action.GenericWebAction;




/**
 * 支付回调的action
 * @author zuoshengwei
 *
 */
public class MpmFromPayAction extends GenericWebAction {
   
	private static final MyLog log = MyLog.getLogger(MpmFromPayAction.class);

	/** 订单编号 in */
	private String orderCD;

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
	/**
	 * 校验支付请求是否合法,如果合法则更新订单状态和支付状态
	 */
	public String execute() {
		log.info("在线支付异步返回调用开始");
		log.info("parame(返回参数) = " + debugStrPara());
		
		// 从外部交易号获取订单CD
		orderCD = outTradeNo.substring(4, outTradeNo.length() - 2);
		// 取订单对象和支付对象
		OrOrder order = orderService.getOrOrderByOrderCd(orderCD);
		
		//支付方式
		int paymentTypeInt = order.getPrepayType();
		
		List<OrPayment> paymentLst  = order.getPaymentList();
		
		String callBackResult = "F"; //通知支付系统酒店是否接受请求成功
		
		// 签名校验
		if (!HotelPayOnlieUtil.verifySign(sign, setSignMap(), HotelPayOnlieUtil.PSD_KEY)) {
			log.info("密匙验证失败！");
			callBackResult = "F";
			return SUCCESS;
		}
  		if(paymentLst!=null&&!paymentLst.isEmpty()){
  			
			OrPayment paymentEntiy =  paymentLst.get(0);
			
			//支付是否成功
			boolean isSuccessSta = paymentEntiy.isPaySucceed();
     		
			log.info("========MpmFromPayAction=========验证在线支付 outTradeNo = " + outTradeNo + " , payResult = " + payResult);
     		
     		if (StringUtil.isValidStr(payResult)) {
     			
     			// 重新去订单
    	        OrOrder updateOrder = orderService.queryOrderForSyschronous(order.getID());
    	        
    	        // 如果支付成功，则更新本地订单状态，并同步香港中科订单状态
     			if(!isSuccessSta && payResult.trim().equals(HkConstant.RETURN_SUCCESS)){
     				// 确认交易
    	            boolean flag = hotelManageWeb.saleCommitFowWebHK(updateOrder);
    	            // 如果调中科接口确认交易时成功
    	            if (flag) {
    	            	orderService.updateOrder(updateOrder, paymentTypeInt, true, true);
    	            	log.info("========MpmFromPayAction=========同步订单支付状态，支付成功，调中科接口确认交易成功，orderCd=" + orderCD);
    	            } else {
    	            	orderService.updateOrder(updateOrder, paymentTypeInt, true, false);
    	            	log.info("========MpmFromPayAction=========同步订单支付状态，支付成功，调中科接口确认交易失败，orderCd=" + orderCD);
    	            }
         		}
     		}
		}			
  		log.info("========MpmFromPayAction====在线支付异步返回调用结束");
		callBackResult = "S";
		request.setAttribute("callBackResult", callBackResult);
		return SUCCESS;
	}

	private Map<String, String> setSignMap() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("outTradeNo", outTradeNo);
		params.put("payResult", payResult);
		params.put("payMessage", payMessage);
		params.put("payTime", payTime);
		params.put("payAmount", payAmount);
		params.put("notifyType", notifyType);
		params.put("operator", operator);
		return params;
	}

	private String debugStrPara() {
		StringBuffer resultStr = new StringBuffer();
		resultStr.append("outTradeNo = " + outTradeNo);
		resultStr.append("&payResult = " + payResult);
		resultStr.append("&payMessage = " + payMessage);
		resultStr.append("&payTime = " + payTime);
		resultStr.append("&payAmount = " + payAmount);
		resultStr.append("&notifyType = " + notifyType);
		resultStr.append("&operator = " + operator);
		resultStr.append("&signType = " + signType);
		resultStr.append("&sign = " + sign);
		return resultStr.toString();
	}
	

	public String getOrderCD() {
		return orderCD;
	}

	public void setOrderCD(String orderCD) {
		this.orderCD = orderCD;
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

}
