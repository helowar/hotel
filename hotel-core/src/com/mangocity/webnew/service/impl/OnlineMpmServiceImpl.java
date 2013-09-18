package com.mangocity.webnew.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ctol.mango.pge.common.ParamServiceImpl;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.proxy.payment.service.RequestTypeTemplate;
import com.mangocity.proxy.payment.service.ServletSignUtil;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.service.OnlineMpmService;

/**
 * 查询支付是否成功的services实现类
 * @author zuoshengwei
 *
 */

public class OnlineMpmServiceImpl extends DAOHibernateImpl implements OnlineMpmService  {
    
	
	private static final MyLog log = MyLog.getLogger(OnlineMpmServiceImpl.class);

	
    //订单service
    protected IOrderService orderService;    
    
    private static String postURL = "";           // 支付接口请求URL
    private static String customerId = "";        // 酒店在支付接口中的ID号
	private static String privateKey = "";        // 酒店在支付接口中的加密密文
	
	public OnlineMpmServiceImpl(){
	    try {
	        //从数据库参数配制文件configpara表中读取数据
            postURL = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.posturl}");
            customerId = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.customerId}");
            privateKey = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.privateKey}");
        } catch (Exception e) {
        	log.info("支付接口URL读取出错!!!");
        }
	}

	public String getOnlineSucceedFlag(String outTradeNoStr) {
		String payResult = null;
		String payState="";
		
		/**
		 * 在线支付 查询请求
		 * @param customerId 客户ID, 在收款系统注册的ID
		 * @param outTradeNo 外部交易号,生成规则：客户ID+订单编号+2位流水码
		 * @param signType 加密方式,目前只支持MD5
		 * @param privateKey 加密Key
		 * @return String XML格式的支付请求
		*/
		String xmlStr =RequestTypeTemplate.CreditCardPaymentState(customerId,outTradeNoStr, "MD5", privateKey);
		
		InputStream in = null;
        InputStreamReader utfreader = null;
        
		try {
			 
			String resultStr = ServletSignUtil.HttpClientPost(postURL, xmlStr);
			
			 //对于返回的字符串，解析为xml格式
	          SAXReader reader = new SAXReader();
	          in = new ByteArrayInputStream(resultStr.getBytes());
	          utfreader = new InputStreamReader(in, "UTF-8");        
	          Document document = reader.read(utfreader);
	          Element root = document.getRootElement();
	          
	          String preResult = root.element("result").elementText("success");
	          
	          if ("T".equals(preResult)) {
	        	  //支付状态
	              payResult = root.element("result").element("response").elementText("payResult");
	              if ("SUCCESS".equals(payResult)) {
	              	//预授权成功，支付成功
	                  payState = "succeed";
	              } else if ("FAIL".equals(payResult)) {
	              	//预授权成功，支付失败
	                  payState = "failed";
	              } else if ("WAIT".equals(payResult)) {
	              	//预授权成功，尚未支付
	                  payState = "undo";
	              } else if("CANCEL".equals(payResult)){
	              	//支付取消
	              	payState = "cancel";                	
	              } else if("WAITCANCEL".equals(payResult)){
	              	//等待撤销
	              	payState = "waitcancel";
	              } else if("REFUND".equals(payResult)){
	              	//已退款
	              	payState = "refund";
	              }else if("EXCEPTION".equals(payResult)){
	              	//预授权成功，支付异常
	                payState = "exception";
	              }
	          } else {
	          	//1.没有预授权记录 2.生成预授权失败
	              payState = "notfound";
	              return payState;
	          }                                 
		} catch (Exception e) {
	         log.error(e);  
	      }finally{
	          try {
	               utfreader.close();
	               in.close();
	          } catch (IOException e) {
	              log.error(e);
	          }
	         
	      }   		
		
		return payResult;
	}

	public IOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}
}
