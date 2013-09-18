package com.mangocity.webnew.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mangocity.hk.constant.HkConstant;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.persistence.OrCouponRecords;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrPointRecords;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.orderrecord.model.OrderRecord;
import com.mangocity.hotel.orderrecord.service.AbstractOrderRecord;
import com.mangocity.lang.StringUtils;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.CookieUtils;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.IHotelOrderCompleteService;
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
	 * 即时确认，发送短信，Email，传真的Service add by jiajie.chen 2011-6-15
	 */
    private OrderImmedConfirmService orderImmedConfirmService;
    
  //注入
    private IHotelOrderCompleteService hotelOrderCompleteService; // add by diandian.hou 2011-11-3 代金券

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
		
		String callBackResult = "F"; //通知支付系统酒店是否接受请求成功 F:没有回答 S:成功回调
		
		// 签名校验
		if (!HotelPayOnlieUtil.verifySign(sign, setSignMap(), HotelPayOnlieUtil.PSD_KEY)) {
			log.info("密匙验证失败！");
			callBackResult = "F";
			saveOrderRecord();
			return SUCCESS;
		}
		
  		if(paymentLst!=null&&!paymentLst.isEmpty()){
  			
  		    // 获得非代金券和积分的支付方式 add by diandian.hou
			OrPayment paymentEntiy =  HotelPayOnlieUtil.getPaymentNotCouponNotPoint(paymentLst);
			
			//支付是否成功
			boolean isSuccessSta = paymentEntiy.isPaySucceed();
			log.info("========MpmFromPayAction=========验证在线支付 outTradeNo = " + outTradeNo + " , payResult = " + payResult);
     		
     		if (StringUtil.isValidStr(payResult)) {
     			
     			// 重新去订单
    	        // OrOrder updateOrder = orderService.queryOrderForSyschronous(order.getID());
    	        
    	        // 如果支付成功，则更新本地订单状态，并同步香港中科订单状态
     			if(!isSuccessSta && payResult.trim().equals(HkConstant.RETURN_SUCCESS) && order.getChannel()==8){
     				// 确认交易
    	            boolean flag_CTSConfirm = hotelManageWeb.saleCommitFowWebHK(order);
    	            boolean flag_cutCouponOK = true;
    	            boolean flag_cutPointOK = true;
    	            // 如果调中科接口确认交易时成功
    	            if (flag_CTSConfirm) {
    	            	orderService.updateOrder(order, paymentTypeInt, true, true);
    	            	log.info("========MpmFromPayAction=========同步订单支付状态，支付成功，调中科接口确认交易成功，orderCd=" + orderCD);
    	            	log.debug("========MpmFromPayAction=========The App has sended confirm information to customer，orderCd=" + orderCD);
    	            } else {
    	            	orderService.updateOrder(order, paymentTypeInt, true, false);
    	            	log.info("========MpmFromPayAction=========同步订单支付状态，支付成功，调中科接口确认交易失败，orderCd=" + orderCD);
    	            }
    	            //扣减代金券 add by diandian.hou 2011-11-14
     				List<OrCouponRecords> orCouponRecordsList = order.getCouponRecords();
     				String cuoponRecordsNum = "";
     				if(orCouponRecordsList!=null && orCouponRecordsList.size()> 0){
     					cuoponRecordsNum = String.valueOf(orCouponRecordsList.size());
        	            Map<String,String> _params = new HashMap<String,String>();
	     				_params.put("couponNum", cuoponRecordsNum);
	     				MemberDTO member = memberInterfaceService
	     		                .getMemberByCode(order.getMemberCd());
	 		    		if (!"".equals(cuoponRecordsNum)) {
	 		    			try{
	 		    			    hotelOrderCompleteService.deductUsedCoupon(_params, order, member); //锁定代价券
	 		    			    voucherInterfaceService.confirmVoucherState(order, roleUser, member);//确认并扣除代金券
	 		    			}catch(Exception e){
	 		    				flag_cutCouponOK = false;
	 		    				log.error("HotelOnlinePayCompleteAction 代金券锁定失败" +e);
	 		    				//代金券失败的话，修改订单状态
		 			            order.setOrderState(OrderState.SUBMIT_TO_MID);
		 			            OrderUtil.updateStayInMid(order);
	 		    			}
	 		    		}	
     				}
     				//添加扣积分 add by diandian.hou 2011-11-23
     				List<OrPointRecords> pointRecordsList =  order.getPointRecordsList();
     				if(pointRecordsList!=null && pointRecordsList.size()>0){
	     				long ulmPointValue = 0;
     					for(int i = 0 ;i<pointRecordsList.size();i++){
     						OrPointRecords orPointRecords = pointRecordsList.get(i);
     						ulmPointValue += orPointRecords.getPointValue();
	     				}
     					boolean isDeducted = false;
	     				try {
	     					double ulmPointAmount = ulmPointValue / 100; //积分对应的人民币
	     					isDeducted = hotelOrderCompleteService.deductUlmPoint(order, ulmPointAmount, true);
	     				}catch (Exception e) {
	     					//积分失败的话，修改订单状态
	 			            order.setOrderState(OrderState.SUBMIT_TO_MID);
	 			            OrderUtil.updateStayInMid(order);
	     					flag_cutPointOK = false;
	     					log.error("ErrCord:75211 HotelCompleteAction.fillPrepayOrderInfo() deduct member point is error: " ,e);
	     				}
	 					if(!isDeducted){
	 						flag_cutPointOK = false;
	 						OrHandleLog handleLog = new OrHandleLog();
	 				        handleLog.setOrder(order);
	 				        handleLog.setModifiedTime(new Date());
	 			            handleLog.setModifierName("HWEB");
	 			            handleLog.setModifierRole("HWEB");
	 			            handleLog.setContent("在线支付订单，扣积分失败");
	 			            order.getLogList().add(handleLog);
	 			            //积分失败的话，修改订单状态
	 			            order.setOrderState(OrderState.SUBMIT_TO_MID);
	 			            OrderUtil.updateStayInMid(order);
	 					}
     				}
     				if(flag_CTSConfirm && flag_cutCouponOK && flag_cutPointOK){ //如果中旅确认，积分和代金券都没问题的话，给客人发短信
     				    orderImmedConfirmService.sendImmedConfirmToCus(order, false, member);
     				}
     			    orderService.updateOrder(order);
     			}else{
     				orderService.updateOrder(order, paymentTypeInt, true, false);
	            	log.info("========MpmFromPayAction=========同步订单支付状态，支付成功，调中科接口确认交易失败，orderCd=" + orderCD);
     			}
     		}
		}			
  		log.info("========MpmFromPayAction====在线支付异步返回调用结束");
  		
  		callBackResult = "S";
		request.setAttribute("callBackResult", callBackResult);
		saveOrderRecord();
		return SUCCESS;
	}

	private void saveOrderRecord(){
		try {						
			final OrderRecord orderRecord ;
			
			HttpSession httpSession = request.getSession(false);
			if( httpSession!=null){
				OrderRecord fromSessionOrderRecord=(OrderRecord)httpSession.getAttribute("orderRecord");
				if(fromSessionOrderRecord!=null){
					orderRecord=fromSessionOrderRecord;
				}
				else{
					orderRecord=new OrderRecord();
				}
			}
			else{
				orderRecord=new OrderRecord();
			}	
			String apacheSessionId = getApacheSessionId();
			if(null != apacheSessionId && !"".equals(apacheSessionId)){
				orderRecord.setApacheSessionId(apacheSessionId);//添加sessionId
			}
			hotelOrderFromBean = new HotelOrderFromBean();
			AbstractOrderRecord orderRecoreService = new AbstractOrderRecord(orderRecord, hotelOrderFromBean) {
				public void init() {
				};

				public void combineOrderRecord() {
					orderRecord.setOrorderCd(orderCD);
					orderRecord.setCurrentStep(10);
					orderRecord.setCreateDate(new Date());
				}

			};
			WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
			orderRecoreService.createOrderRecordTemplete(context);
			
			if (httpSession != null) {
				httpSession.setAttribute("orderRecord", orderRecord);
			}
		} catch (Exception e) {
			log.error("HotelBookingAction order Record:" + e);
		}
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
	
	public OrderImmedConfirmService getOrderImmedConfirmService() {
		return orderImmedConfirmService;
	}

	public void setOrderImmedConfirmService(
			OrderImmedConfirmService orderImmedConfirmService) {
		this.orderImmedConfirmService = orderImmedConfirmService;
	}

	public void setHotelOrderCompleteService(
			IHotelOrderCompleteService hotelOrderCompleteService) {
		this.hotelOrderCompleteService = hotelOrderCompleteService;
	}

}
