package com.mangocity.fantiweb.action;

import java.util.Date;
import java.util.List;

import mango.it.payQuery.common.QueryPayManager;

import com.ctol.mango.pge.common.ParamServiceImpl;
import com.mangocity.hk.constant.HkConstant;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.service.OnlineMpmService;

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
	
	public HotelQueryOnlinePaymentStatusAction(){
	    try {
	        //从数据库参数配制文件configpara表中读取数据
            customerId = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.customerId}");
        } catch (Exception e) {
        	log.info("支付接口URL读取出错!!!");
        }
	}

    public String execute(){
    	
    	String  serialNo ="10";
    	
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
        	        // OrOrder updateOrder = orderService.queryOrderForSyschronous(order.getID());
         			
             		if (payState.trim().equals(HkConstant.RETURN_FAILURE)) {// 支付失败
             			
        	            hotelManageWeb.rollbackForWebHK(order);
        	            
        	            payLastFlag = "onlinePayFail";
        	            
        	            log.info("=======HotelQueryOnlinePaymentStatusAction=======在线支付失败，orderCd=" + orderCd);
        	            
        	            // 记录操作日志
        	            OrHandleLog handleLog = new OrHandleLog();
        	            handleLog.setModifiedTime(new Date());
        	            handleLog.setContent(HkConstant.ONLINE_PAY_FIAL);
        	            handleLog.setModifierName("网站");
        	            handleLog.setOrder(order);
        	            order.getLogList().add(handleLog);
        	            orderService.updateOrder(order, paymentTypeInt, false, false);
        	        }else if (payState.trim().equals(HkConstant.RETURN_WAITING)) {// 支付中
        	        	
        	        	payLastFlag = "onlinePayWaiting";
        	        	
        	        	log.info("=====HotelQueryOnlinePaymentStatusAction=========正在支付中: orderCd = " + orderCd);
            	        
            	    }else if(payState.trim().equals(HkConstant.RETURN_SUCCESS)){
            	    	
            	        // 如果支付成功，则更新本地订单状态，并同步香港中科订单状态
             			if(!isSuccessSta){
             				// 确认交易
            	            boolean flag = hotelManageWeb.saleCommitFowWebHK(order);
            	            // 如果调中科接口确认交易时成功
            	            if (flag) {
            	                
            	            	orderService.updateOrder(order, paymentTypeInt, true, true);
            	                
            	            	payLastFlag = "onlinePaySuccess";
            	            	
            	            	log.info("======HotelQueryOnlinePaymentStatusAction========同步订单支付状态，支付成功，调中科接口确认交易成功，orderCd=" + orderCd);
            	                
            	            } else {
            	                
            	            	orderService.updateOrder(order, paymentTypeInt, true, false);
            	                
            	            	payLastFlag = "onlinePaySuccessHKExp";
            	            	
            	            	log.info("=======HotelQueryOnlinePaymentStatusAction=======同步订单支付状态，支付成功，调中科接口确认交易失败，orderCd=" + orderCd);
            	            }
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


}
