package com.mangocity.hotel.order.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ctol.mango.pge.common.ParamServiceImpl;
import com.mangocity.hotel.base.service.PayOutCallBackForUpdateOrderPrepayStatusService;
import com.mangocity.hotel.ejb.reservation.HotelSupplyDelegate;
import com.mangocity.hotel.order.service.IPayOutInterface;
import com.mangocity.proxy.payment.service.ServletSignUtil;
import com.mangocity.util.XMLUtils;
import com.mangocity.util.log.MyLog;

/**
 * 提供给财务rmi接口
 * @author chenkeming
 *
 */
public class PayOutInterfaceImpl extends HttpServlet implements IPayOutInterface , Serializable {
   
	/**
     * 
     */
    private static final long serialVersionUID = 3992722824007014279L;
    private static final MyLog log = MyLog.getLogger(PayOutInterfaceImpl.class);
    private PayOutCallBackForUpdateOrderPrepayStatusService payOutCallBackForUpdateOrderPrepayStatusService;
    private HotelSupplyDelegate hotelSupplyDelegate;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
        
        ServletContext sc = request.getSession().getServletContext();
        WebApplicationContext wc = WebApplicationContextUtils.getWebApplicationContext(sc);
        payOutCallBackForUpdateOrderPrepayStatusService = (PayOutCallBackForUpdateOrderPrepayStatusService)wc.getBean("payOutCallBackForUpdateOrderPrepayStatusService");
        
        hotelSupplyDelegate=(HotelSupplyDelegate)wc.getBean("hotelSupplyDelegate");
        
        // 解析回传信息
        String result = "";
        //返回给支付系统的参数 add by chenjiajie 2010-08-31
        String resultForCallBack = "F";
        try {
            String xmlStr = request.getParameter("xmlStr");
            DeBug("xmlStr ====>>> " + xmlStr);
            result = readPayXml(URLDecoder.decode(xmlStr, "UTF-8"));
            if("SUCCESS".equals(result)){
            	 hotelSupplyDelegate.payAssureOrderProcess(getOrderCdFromRequest(URLDecoder.decode(xmlStr, "UTF-8")));
            }
            
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        	resultForCallBack = "F";
        }
        resultForCallBack = "S";
        DeBug(result);
        // 页面输出
        drawPayXml(response, resultForCallBack);
	}
	 
	/**
     * RMI回写接口
     */
    public boolean updateOrderPrepayStatus(final String orderCD, final boolean bHasPay, String confirmUser) {

        DeBug(":::::::::::开始接受财务回调:");
        DeBug("orderCD:" + orderCD);
        DeBug("bHasPay:" + bHasPay);
        DeBug("confirmUser:" + confirmUser);
        DeBug("payOutCallBackForUpdateOrderPrepayStatusService:" + payOutCallBackForUpdateOrderPrepayStatusService);
        return payOutCallBackForUpdateOrderPrepayStatusService.updateOrderPrepayStatus(orderCD, bHasPay, confirmUser);
    }
    
    /**
	 * 获取订单CD
	 */
	@SuppressWarnings("unchecked")
	private String getOrderCdFromRequest(String xmlStr) {
		Element resultElement;
		String orderCd = null;
		try {
			resultElement = XMLUtils.getElementByXmlString(xmlStr);
			List<Element> resultList = resultElement.getChildren();

			for (Element result : resultList) {
				Element response = result.getChild("response");
				String outTradeNo = response.getChild("outTradeNo").getText();
				if (outTradeNo != null) {
					orderCd = outTradeNo.substring(4, outTradeNo.length() - 2);
				}

			}
		} catch (Exception e) {
			log.error("收款系统Xml解析异常", e);
		}

		return orderCd;
	}
	/**
	 * 读取函数
	 * @param xmlStr
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public String readPayXml(String xmlStr) {
        DeBug("信用卡担保支付回调方法结果 : ");
	    try {
            Element resultElement = XMLUtils.getElementByXmlString(xmlStr);
            List<Element> resultList = resultElement.getChildren();
            for(Element result : resultList) {
                // 生成预授权请求是否成功
                String resultPrePay = result.getChild("success").getText();
                String messagePrePay = result.getChild("message").getText();
                DeBug("success = " + resultPrePay + ", message = " + messagePrePay);
                Element response = result.getChild("response");
                // 外部交易号
                String outTradeNo = response.getChild("outTradeNo").getText();
                // 成功SUCCESS, 取消CANCEL, 失败FAIL, 退款：REFUND, 支付中WAIT, 异常：EXCEPTION
                String payResult = response.getChild("payResult").getText();  
                // 支付信息
                String payMessage = response.getChild("payMessage")==null ? null : response.getChild("payMessage").getText();  
                // 支付时间
                String payTime = response.getChild("payTime").getText();
                // 支付金额
                String payAmount  = response.getChild("payAmount").getText();
                // 操作员
                String operator = response.getChild("operator").getText();
                // 通知模式 1：同步返回，2：异步通知
                String notifyType = response.getChild("notifyType").getText();
                // 加密方式
                String signType = response.getChild("signType").getText();
                // 密文
                String sign = response.getChild("sign").getText();
                // 密匙
                String privateKey = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.privateKey}");
                
                Map<String, String> payProperty = new HashMap<String, String>();
                payProperty.put("outTradeNo", outTradeNo);
                payProperty.put("payResult", payResult);
                payProperty.put("payMessage", payMessage);
                payProperty.put("payTime", payTime);
                payProperty.put("operator", operator);
                payProperty.put("payAmount", payAmount);
                payProperty.put("notifyType", notifyType);
                
                // 输出调试信息
                DeBug("outTradeNo = " + outTradeNo + ", payMessage = " + payMessage + 
                      ", payTime = " + payTime + ", payAmount = " + payAmount + 
                      ", notifyType = " + notifyType + ", signType = " + signType +
                      ", sign = " + sign + notifyType + ", operator = " + operator);
                if(ServletSignUtil.buildSign(payProperty,privateKey).equals(sign)) {
                    if(updateOrderPrepayStatus(outTradeNo.substring(4, outTradeNo.length() - 2),
                        payResult.equals("SUCCESS"), operator)){
                        return "SUCCESS";
                    } else {
                        return "回调存储出错 !";
                    }
                } else {
                    return "加密信息错误 !";
                }
            }
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            DeBug("收款系统Xml解析异常, xmlStr = " + xmlStr);
            DeBug("异常信息 : " + e);
        }
        return "收款系统Xml解析异常";
    }

	/**
	 * 页面提示与输出函数
	 * @param response
	 * @param returnXml
	 * @throws IOException
	 */
	private void drawPayXml(HttpServletResponse response, String returnXml) throws IOException {
//	    response.setContentType("text/xml");
//	    response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(returnXml);
	}
	
	/**
	 * 铺获XML异常
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private boolean readPostXml(HttpServletRequest request) throws ServletException, IOException {
	    // 接收XML
        char[] readerBuffer = new char[request.getContentLength()];
        BufferedReader bufferedReader = request.getReader();
        int portion = bufferedReader.read(readerBuffer);
        int amount = portion;
        while (amount < readerBuffer.length) {
            portion = bufferedReader.read(readerBuffer, amount,readerBuffer.length - amount);
            amount = amount + portion;
        }
        String postXml = new String(readerBuffer);
        
        postXml = postXml.replaceAll("%2B"," ");
        postXml = postXml.replaceAll("%253C","<");
        postXml = postXml.replaceAll("%253F","?");
        postXml = postXml.replaceAll("%253D","=");
        postXml = postXml.replaceAll("%2522","\"");
        postXml = postXml.replaceAll("%252F","/");
        postXml = postXml.replaceAll("%253E",">");
        
        DeBug("readPostXml = " + postXml);
        
        String success = "", message = "", outTradeNo = "", payResult = "", payMessage = "", payTime = "", 
        payAmount = "", notifyType = "", signType = "", sign = "";
        
        // 请求结果 成功：T，失败：F （只是生成预授权成功或失败，不是指支付成功失败
        try {
            success = postXml.substring(postXml.indexOf("<success>"), postXml.indexOf("</success>"));
        } catch (Exception e) {
            DeBug("XML格式错误,不含有 请求结果 字段!");
        }
        // 请求结果是失败时，显示失败原因，成功的话为空
        try {
            message = postXml.substring(postXml.indexOf("<message>"), postXml.indexOf("</message>"));
        } catch (Exception e) {
            DeBug("XML格式错误,不含有 请求消息 字段!");
        }
        // 外部交易号
        try {
            outTradeNo = postXml.substring(postXml.indexOf("<outTradeNo>"), postXml.indexOf("</outTradeNo>"));
        } catch (Exception e) {
            DeBug("XML格式错误,不含有 外部交易号 字段!");
        }
        // 支付结果—是否支付成功
        try {
            payResult = postXml.substring(postXml.indexOf("<payResult>"), postXml.indexOf("</payResult>"));
        } catch (Exception e) {
            DeBug("XML格式错误,不含有 支付结果 字段!");
        }
        // 支付信息
        try {
            payMessage = postXml.substring(postXml.indexOf("<payMessage>"), postXml.indexOf("</payMessage>"));
        } catch (Exception e) {
            DeBug("XML格式错误,不含有 支付信息 字段!");
        }
        // 支付时间
        try {
            payTime = postXml.substring(postXml.indexOf("<payTime>"), postXml.indexOf("</payTime>"));
        } catch (Exception e) {
            DeBug("XML格式错误,不含有 支付时间 字段!");
        }
        // 支付金额 单位：元
        try {
            payAmount = postXml.substring(postXml.indexOf("<payAmount>"), postXml.indexOf("</payAmount>"));
        } catch (Exception e) {
            DeBug("XML格式错误,不含有 支付金额 字段!");
        }
        // 通知模式 1：同步返回，2：异步通知
        try {
            notifyType = postXml.substring(postXml.indexOf("<notifyType>"), postXml.indexOf("</notifyType>"));
        } catch (Exception e) {
            DeBug("XML格式错误,不含有 通知模式 字段!");
        }
        // 加密方式
        try {
            signType = postXml.substring(postXml.indexOf("<signType>"), postXml.indexOf("</signType>"));
        } catch (Exception e) {
            DeBug("XML格式错误,不含有 加密方式 字段!");
        }
        // 密文
        try {
            sign = postXml.substring(postXml.indexOf("<sign>"), postXml.indexOf("</sign>"));
        } catch (Exception e) {
            DeBug("XML格式错误,不含有 密文 字段!");
        }
        DeBug(success + message + outTradeNo + payResult + payMessage + payTime + 
                    payAmount + notifyType + signType + sign);
	    return false;
	}

	public void setPayOutCallBackForUpdateOrderPrepayStatusService(
			PayOutCallBackForUpdateOrderPrepayStatusService payOutCallBackForUpdateOrderPrepayStatusService) {
		this.payOutCallBackForUpdateOrderPrepayStatusService = payOutCallBackForUpdateOrderPrepayStatusService;
	}	
	
    private static void DeBug(String info) {
    	log.info(info);
    }

	public void setHotelSupplyDelegate(HotelSupplyDelegate hotelSupplyDelegate) {
		this.hotelSupplyDelegate = hotelSupplyDelegate;
	}
    
    
}
