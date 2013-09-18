package com.mangocity.hagtb2b.web;

import java.util.Date;
import java.util.List;

import mango.it.payQuery.common.QueryPayManager;

import com.ctol.mango.pge.common.ParamServiceImpl;
import com.mangocity.hk.constant.HkConstant;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.DesCryptUtil;
import com.mangocity.webnew.service.OnlineMpmService;
import com.mangocity.webnew.service.OrderImmedConfirmService;
import com.mangocity.webnew.util.action.GenericWebAction;

/**
 * 在线支付状态查询action 
 * add by shengwei.zuo 2009-11-23
 * 
 */
public class HotelQueryOnlinePaymentStatusAction extends GenericWebAction {

	private static final MyLog log = MyLog.getLogger(HotelQueryOnlinePaymentStatusAction.class);

    /**
     * 支付查询接口
     */
    private QueryPayManager queryPayManager;
    
    //查询在线支付是否成功的serviece add by shengwei.zuo 
    private OnlineMpmService  onlineMpmService;
    
    //跳转页面
    private String forward ;
    
    /**
     * 订单ID
     */
    private String orderID;
    
    //支付状态
    private String payState="";
    
    //最终的支付状态
    private String payLastFlag;
    
    private static String customerId = "";  
    
    /**
	 * 即时确认，发送短信，Email，传真的Service add by xiaowei.wang 2011-8-4
	 */
    private OrderImmedConfirmService orderImmedConfirmService;
	
	public HotelQueryOnlinePaymentStatusAction(){
	    try {
	        //从数据库参数配制文件configpara表中读取数据
            customerId = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.customerId}");
        } catch (Exception e) {
        	log.info("支付接口URL读取出错!!!");
        }
	}

    public String execute(){
    	
    	//根据Cookie判断，是否已登录，如果未登录，给出提示。add by shengwei.zuo 2010-1-9
		boolean isLogon = super.checkLogin();
		if(!isLogon){
			setErrorMessage("您未登录，请先登录！");	            	
            return "forwardToError";
		}
		
    	
    	String  serialNo ="10";
    	try {
    		cryptOrderId = new DesCryptUtil("hotel").decrypt(cryptOrderId);
    		orderID = cryptOrderId;
		} catch (java.lang.NumberFormatException e) {
			log.error("订单解密后格式转换错误！订单id="+cryptOrderId);
			return forwardError("查找此订单出错，请重试！");
		} catch (Exception e) {
			log.error("对订单解密错误！订单id="+cryptOrderId);
			return forwardError("查找此订单出错，请重试！");
		}
		
    	order = new OrOrder();
    	order = orderService.getOrder(Long.parseLong(orderID));
    	
    	if(order!=null){
    		
    		//支付方式
    		int paymentTypeInt = order.getPrepayType();
    		
    		//订单CD
    		String orderCd = order.getOrderCD();
    		
    		List<OrPayment> paymentLst  = order.getPaymentList();
    		if(paymentLst!=null&&!paymentLst.isEmpty()){
    			OrPayment paymentEntiy =  paymentLst.get(0);
    			
    			//支付是否成功
    			boolean isSuccessSta = paymentEntiy.isPaySucceed();
    			
    			log.info(" =======HotelQueryOnlinePaymentStatusAction ============isSuccessSta : "  + isSuccessSta);
    			
    			 //流水号
    			serialNo = paymentEntiy.getPrepayBillNo();
    			//外部交易号
         		String outTradeNoStr = customerId + orderCd + serialNo;
         		//查询的支付状态 add by shengwei.zuo 
         		payState = onlineMpmService.getOnlineSucceedFlag(outTradeNoStr);
         		
         		log.info(" =======HotelQueryOnlinePaymentStatusAction ============payState : "  + payState);
         		
         		log.info("验证在线支付  outTradeNoStr = " + outTradeNoStr + " , payState = " + payState);
         		
         		if (StringUtil.isValidStr(payState)) {
         			
         			// 重新去订单
        	        OrOrder updateOrder = orderService.queryOrderForSyschronous(order.getID());
         			
             		if (payState.trim().equals(HkConstant.RETURN_FAILURE)) {// 支付失败
             			
        	            hotelManageWeb.rollbackForWebHK(updateOrder);
        	            
        	            payLastFlag = "onlinePayFail";
        	            
        	            log.info("=======HotelQueryOnlinePaymentStatusAction=======在线支付失败，orderCd=" + orderCd);
        	            
        	            // 记录操作日志
        	            OrHandleLog handleLog = new OrHandleLog();
        	            handleLog.setModifiedTime(new Date());
        	            handleLog.setContent(HkConstant.ONLINE_PAY_FIAL);
        	            handleLog.setModifierName("网站");
        	            handleLog.setOrder(updateOrder);
        	            updateOrder.getLogList().add(handleLog);
        	            orderService.updateOrder(updateOrder, paymentTypeInt, false, false);
        	        }else if (payState.trim().equals(HkConstant.RETURN_WAITING)) {// 支付中
        	        	
        	        	payLastFlag = "onlinePayWaiting";
        	        	
        	        	log.info("=====HotelQueryOnlinePaymentStatusAction=========正在支付中: orderCd = " + orderCd);
            	        
            	    }else if(payState.trim().equals(HkConstant.RETURN_SUCCESS)){
            	    	
            	        // 如果支付成功，则更新本地订单状态，并同步香港中科订单状态
             			if(!isSuccessSta){
             				// 确认交易
            	            boolean flag = hotelManageWeb.saleCommitFowWebHK(updateOrder);
            	            // 如果调中科接口确认交易时成功
            	            if (flag) {
            	                
            	            	orderService.updateOrder(updateOrder, paymentTypeInt, true, true);
            	                
            	            	payLastFlag = "onlinePaySuccess";
            	            	
            	            	log.info("======HotelQueryOnlinePaymentStatusAction========同步订单支付状态，支付成功，调中科接口确认交易成功，orderCd=" + orderCd);
            	                
            	            } else {
            	                
            	            	orderService.updateOrder(updateOrder, paymentTypeInt, true, false);
            	                
            	            	payLastFlag = "onlinePaySuccessHKExp";
            	            	
            	            	log.info("=======HotelQueryOnlinePaymentStatusAction=======同步订单支付状态，支付成功，调中科接口确认交易失败，orderCd=" + orderCd);
            	            }
            	            /** 前提是网银支付 add by  xiaowei.wang 2011.8.4 B2B 非中旅网银支付成功后给酒店发送传真 begin*/
                 		    if(!order.isCtsHK()){//非中旅预付网银支付成功后发传真
                            	if(!order.isSendedHotelFax()){
                            		//发送传真给酒店 
                            		MemberDTO member = null;
                            		String sendHotelResult = //orderImmedConfirmService.sendImmedConfirmToHotel(order);
                            			orderImmedConfirmService.sendImmedConfirmToHotel(order,member);
                            		if("success".equals(sendHotelResult)){
                            			//是否满足配额  
                            			boolean quotaOk = //orderImmedConfirmService.quotaOk(order);
                            				orderImmedConfirmService.quotaOk(order);
                            			
                            			//如果满足配额,且支付成功 
                            			if(quotaOk && (!isSuccessSta&&payState.trim().equals(HkConstant.RETURN_SUCCESS))){
                            				//则进行即时确认给客人   
                            				orderImmedConfirmService.sendImmedConfirmToCus(order,false,member);
                            			}            
                            		}
                            	}
                            	 // 保存订单
                            	orderService.updateOrder(order);
                            }
                 		   /** 前提是网银支付 add by  xiaowei.wang 2011.8.4 B2B 非中旅网银支付成功后给酒店发送传真 end*/
                 		}else{
                 			
                 			log.info("=====HotelQueryOnlinePaymentStatusAction=========同步订单支付状态，支付成功，调中科接口确认交易成功，orderCd=" + orderCd);
                 			payLastFlag = "onlinePaySuccess";
                 			
                 		}
            	    }
         		}
    		}	
    	}
 		
	    return "onlinePayStatus"; 
	    
    }



    public QueryPayManager getQueryPayManager() {
        return queryPayManager;
    }

    public void setQueryPayManager(QueryPayManager queryPayManager) {
        this.queryPayManager = queryPayManager;
    }

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public OnlineMpmService getOnlineMpmService() {
		return onlineMpmService;
	}

	public void setOnlineMpmService(OnlineMpmService onlineMpmService) {
		this.onlineMpmService = onlineMpmService;
	}

	public String getPayLastFlag() {
		return payLastFlag;
	}

	public void setPayLastFlag(String payLastFlag) {
		this.payLastFlag = payLastFlag;
	}

	public OrderImmedConfirmService getOrderImmedConfirmService() {
		return orderImmedConfirmService;
	}

	public void setOrderImmedConfirmService(
			OrderImmedConfirmService orderImmedConfirmService) {
		this.orderImmedConfirmService = orderImmedConfirmService;
	}


}
